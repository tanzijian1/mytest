package nc.ui.tg.paymentrequest.action;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pubapp.uif2app.view.BillForm;
import nc.ui.pubapp.uif2app.view.ShowUpableBillForm;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.model.AbstractAppModel;
import nc.uif2.annoations.MethodType;
import nc.uif2.annoations.ModelMethod;
import nc.uif2.annoations.ModelType;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;

public class InvalidAction extends NCAction {
	private AbstractAppModel model;
	private ShowUpableBillForm editor;
	private BillForm billForm;
	ISyncEBSServcie ebsService = null;

	public InvalidAction() {
		setBtnName("单据作废");
		setCode("InvalidAction");
	}

	public BillForm getBillFormEditor() {
		return billForm;
	}

	IMDPersistenceQueryService iMDPersistenceQueryService = null;

	IUAPQueryBS bs = null;

	public IUAPQueryBS getQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	public IMDPersistenceQueryService getIMDPersistenceQueryService() {
		if (iMDPersistenceQueryService == null) {
			iMDPersistenceQueryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return iMDPersistenceQueryService;

	}

	@Override
	public void doAction(ActionEvent e) throws Exception {
		try {
			IVOPersistence ip = NCLocator.getInstance().lookup(
					IVOPersistence.class);
			Object obj = getModel().getSelectedData();
			if (obj == null) {
				throw new BusinessException("请选择一条数据");
			}

			AggPayrequest aggvo = (AggPayrequest) obj;
			// 判断单据是否已作废
			String def27 = aggvo.getParentVO().getDef27();
			if ("Y".equals(def27)) {
				throw new BusinessException("该付款申请单已作废不能重复操作!");
			}

			Integer approvestatus = aggvo.getParentVO().getApprovestatus();
			if (approvestatus != 1) {
				throw new BusinessException("该付款申请单未进行审批，不能作废");
			}
			// 作废单据

			AggPayrequest invalidBillVO = invalidBill(aggvo);
			StringBuffer query = new StringBuffer();
			query.append("select distinct a.billno  ");
			query.append("  from ap_payitem ai  ");
			query.append("  left join ap_paybill a  ");
			query.append("    on a. pk_paybill = ai.pk_paybill  ");
			query.append(" where nvl(ai.dr, 0) = 0  ");
			query.append("   and nvl(a.dr, 0) = 0  ");
			query.append("   and nvl(settleflag,0) <> 1  ");
			query.append("   and ai.src_billid = '" + aggvo.getPrimaryKey()
					+ "'  ");

			ArrayList<String> array = (ArrayList<String>) getQueryBS()
					.executeQuery(query.toString(), new ColumnListProcessor());

			if (array.size() > 0) {
				throw new BusinessException("存在下游未结算的付款单，不能作废！付款单号："
						+ array.toString());
			}

			if (invalidBillVO != null) {
				// 检验付款申请单的累计付款金额，如果为0，则不能作废
				// String def25 = invalidBillVO.getParentVO().getDef25();
				// Double mny = 0d;
				// if (def25 != null) {
				// mny = new Double(def25);
				// }
				// if (mny <= 0 || mny == null) {
				// throw new BusinessException("该单据未有过结算，不能作废");
				// }

				Business_b[] children = (Business_b[]) invalidBillVO
						.getChildren(Business_b.class);
				for (Business_b bvo : children) {
					ip.updateVO(bvo);
				}
				ip.updateVO(invalidBillVO.getParentVO());
				MessageDialog.showWarningDlg(getBillFormEditor(), "提示",
						"单据作废成功!");
			}

		} catch (Exception e2) {
			Logger.error(e2.getMessage());
			throw new BusinessException(e2);
		}
	}

	@Override
	protected boolean isActionEnable() {
		if (editor.getBillCardPanel().getHeadItem("def27") == null) {
			return true;
		}
		Boolean invalid = (Boolean) editor.getBillCardPanel()
				.getHeadItem("def27").getValueObject();
		if (invalid) {
			return false;
		}

		return true;
	}

	@ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.GETTER)
	public AbstractAppModel getModel() {
		return model;
	}

	@ModelMethod(modelType = ModelType.AbstractAppModel, methodType = MethodType.SETTER)
	public void setModel(AbstractAppModel model) {
		this.model = model;
		model.addAppEventListener(this);
	}

	public ShowUpableBillForm getEditor() {
		return editor;
	}

	public void setEditor(ShowUpableBillForm editor) {
		this.editor = editor;
	}

	public ISyncEBSServcie getEBSServcie() {
		if (ebsService == null) {
			ebsService = NCLocator.getInstance().lookup(ISyncEBSServcie.class);
		}
		return ebsService;
	}

	/**
	 * 作废单据 2020-01-10-谈子健
	 * 
	 * @throws BusinessException
	 */
	public AggPayrequest invalidBill(AggPayrequest aggvo)
			throws BusinessException {

		// UFDouble def49 = null;
		UFDouble totalPaymentAmount = new UFDouble(UFDouble.ZERO_DBL);
		// 外系统主键
		Business_b[] bvos = (Business_b[]) aggvo.getChildren(Business_b.class);
		// 表头作废金额
		UFDouble headImny = UFDouble.ZERO_DBL;
		for (Business_b bvo : bvos) {
			String pk_business_b = bvo.getPk_business_b();
			UFDouble local_money_de = bvo.getLocal_money_de();
			local_money_de = local_money_de == null ? UFDouble.ZERO_DBL
					: new UFDouble(local_money_de);
			StringBuffer query = new StringBuffer();
			query.append("select sum(money_de) money_de ");
			query.append("  from ap_payitem ai  ");
			query.append("  left join ap_paybill a  ");
			query.append("    on a. pk_paybill = ai.pk_paybill  ");
			query.append("  left join cmp_settlement t  ");
			query.append("    on t.pk_busibill = a.pk_paybill  ");
			query.append(" where nvl(t.dr, 0) = 0  ");
			query.append("   and nvl(ai.dr, 0) = 0  ");
			query.append("   and t.settlestatus = '5'  ");
			query.append("   and src_itemid = '" + pk_business_b + "'  ");
			Object money = (Object) getQueryBS().executeQuery(
					query.toString(), new ColumnProcessor());
			UFDouble money_de = money == null ? UFDouble.ZERO_DBL
					: new UFDouble(money.toString());// 本次请款累计付款金额
			// def47未付金额 = 表体付款金额local_money_de - 结算表里面已结算的付款金额money_de
			UFDouble def47 = local_money_de.sub(money_de);
			bvo.setAttributeValue("def47", "0");//表体未付金额
			bvo.setAttributeValue("def46", def47.toString());//表体作废金额
			bvo.setDr(0);
			totalPaymentAmount = totalPaymentAmount.add(money_de);
			headImny.add(def47);
		}
		aggvo.getParentVO().setDef50(headImny.toString());
		
		// 本次请款累计付款金额
		if (totalPaymentAmount != null) {
			aggvo.getParentVO().setDef25(totalPaymentAmount.toString());
		}
		// 表头未付金额
		String money = aggvo.getParentVO().getDef49();
		if (money != null) {
			UFDouble amount = aggvo.getParentVO().getMoney();
			aggvo.getParentVO().setDef49("0");
			UFDouble def50 = amount.sub(totalPaymentAmount);
			aggvo.getParentVO().setDef50(def50.toString());
			aggvo.getParentVO().setDef27("Y");
			aggvo.getParentVO().setDr(0);
		}
		return aggvo;
	}

}
