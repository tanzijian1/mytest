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
		setBtnName("��������");
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
				throw new BusinessException("��ѡ��һ������");
			}

			AggPayrequest aggvo = (AggPayrequest) obj;
			// �жϵ����Ƿ�������
			String def27 = aggvo.getParentVO().getDef27();
			if ("Y".equals(def27)) {
				throw new BusinessException("�ø������뵥�����ϲ����ظ�����!");
			}

			Integer approvestatus = aggvo.getParentVO().getApprovestatus();
			if (approvestatus != 1) {
				throw new BusinessException("�ø������뵥δ������������������");
			}
			// ���ϵ���

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
				throw new BusinessException("��������δ����ĸ�����������ϣ�����ţ�"
						+ array.toString());
			}

			if (invalidBillVO != null) {
				// ���鸶�����뵥���ۼƸ�������Ϊ0����������
				// String def25 = invalidBillVO.getParentVO().getDef25();
				// Double mny = 0d;
				// if (def25 != null) {
				// mny = new Double(def25);
				// }
				// if (mny <= 0 || mny == null) {
				// throw new BusinessException("�õ���δ�й����㣬��������");
				// }

				Business_b[] children = (Business_b[]) invalidBillVO
						.getChildren(Business_b.class);
				for (Business_b bvo : children) {
					ip.updateVO(bvo);
				}
				ip.updateVO(invalidBillVO.getParentVO());
				MessageDialog.showWarningDlg(getBillFormEditor(), "��ʾ",
						"�������ϳɹ�!");
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
	 * ���ϵ��� 2020-01-10-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public AggPayrequest invalidBill(AggPayrequest aggvo)
			throws BusinessException {

		// UFDouble def49 = null;
		UFDouble totalPaymentAmount = new UFDouble(UFDouble.ZERO_DBL);
		// ��ϵͳ����
		Business_b[] bvos = (Business_b[]) aggvo.getChildren(Business_b.class);
		// ��ͷ���Ͻ��
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
					: new UFDouble(money.toString());// ��������ۼƸ�����
			// def47δ����� = ���帶����local_money_de - ����������ѽ���ĸ�����money_de
			UFDouble def47 = local_money_de.sub(money_de);
			bvo.setAttributeValue("def47", "0");//����δ�����
			bvo.setAttributeValue("def46", def47.toString());//�������Ͻ��
			bvo.setDr(0);
			totalPaymentAmount = totalPaymentAmount.add(money_de);
			headImny.add(def47);
		}
		aggvo.getParentVO().setDef50(headImny.toString());
		
		// ��������ۼƸ�����
		if (totalPaymentAmount != null) {
			aggvo.getParentVO().setDef25(totalPaymentAmount.toString());
		}
		// ��ͷδ�����
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
