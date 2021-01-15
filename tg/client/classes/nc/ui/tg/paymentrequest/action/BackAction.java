package nc.ui.tg.paymentrequest.action;

import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.infotransform.IInfoTransformService;
import nc.bs.tg.pub.regex.SpecialSymbols;
import nc.itf.tg.IUnsavebillAndDeleteBill;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.tg.showpanel.ReturnMsgPanel;
import nc.ui.uif2.NCAction;
import nc.ui.uif2.editor.BillForm;
import nc.ui.uif2.model.AbstractUIAppModel;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;
import nc.vo.tgfn.paymentrequest.Payrequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BackAction extends NCAction {
	ReturnMsgPanel msgPanel = null;

	/**
	 * 付款申请单退回 2020-03-19-谈子健
	 */
	@Override
	public void doAction(ActionEvent e) throws Exception {
		Object obj = getModel().getSelectedData();
		if (obj == null) {
			throw new BusinessException("请选择一条数据");
		}
		AggPayrequest aggvo = (AggPayrequest) obj;

		// if ("FN01-Cxx-003".equals(aggvo.getParentVO().getTranstype())) {
		// throw new BusinessException("该单据不可回退");
		// }

		List<String> billno = (List<String>) getIUAPQueryBS()
				.executeQuery(
						"select billno from ap_paybill a where a.pk_paybill in (select item.pk_paybill from ap_payitem item  where item.src_billid='"
								+ aggvo.getPrimaryKey() + "' and dr = 0)",
						new ColumnListProcessor());// 是否推对应付款单
		if (billno != null && billno.size() > 0)
			throw new BusinessException("单据有对应下游付款单单据,付款单单据号为" + billno);
		Integer approvestatus = ((Payrequest) aggvo.getParentVO())
				.getApprovestatus();
		if (approvestatus != -1 && approvestatus != 3)
			throw new BusinessException("该付款申请单审批状态不是自由态或提交态不能进行退回操作!");
		if ("FN01-Cxx-002".equals(aggvo.getParentVO().getTranstype())
				|| "FN01-Cxx-006".equals(aggvo.getParentVO().getTranstype())) {
			throw new BusinessException("自动审批的付款申请单不能直接退回,请在应付单界面做对应的退回操作!");
		}

		/**
		 * 弹出退回原因输入框2020-03-19-谈子健 -start
		 */
		getMsgPanel().showModal();
		String msg = getMsgPanel().getMsg();
		if ("".equals(msg) || msg == null) {
			throw new BusinessException("回退原因不能为空!");
		}
		// 增加特殊符号校验
		SpecialSymbols sp = new SpecialSymbols();
		String specia = sp.specialSymbols(msg);

		if (specia.length() > 0) {
			throw new BusinessException("退回原因不能包含特殊字符：" + specia);
		}
		// 设置自定义项48回退原因
		// 成本付款申请单
		aggvo.getParentVO().setDef48(msg);

		/**
		 * 弹出退回原因输入框2020-03-19-谈子健 -end
		 */

		// 将删除和退回外系统放到同一事务中
		try {

			NCLocator.getInstance().lookup(IUnsavebillAndDeleteBill.class)
					.BackAndDeletePayreq_RequiresNew(aggvo, approvestatus);

		} catch (Exception e2) {
			Logger.getLogger(e2.getMessage());
			throw new BusinessException(e2.getMessage());
		}
		MessageDialog.showHintDlg(editor, "提示", "退回成功，请将BPMID："
				+ aggvo.getParentVO().getBpmid() + "告知地区财务");
	}

	IUAPQueryBS bs = null;

	public IUAPQueryBS getIUAPQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;

	}

	IMDPersistenceQueryService iMDPersistenceQueryService = null;

	public IMDPersistenceQueryService getIMDPersistenceQueryService() {
		if (iMDPersistenceQueryService == null) {
			iMDPersistenceQueryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return iMDPersistenceQueryService;

	}

	IPFBusiAction pfBusiAction = null;

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	private AbstractUIAppModel model = null;

	public AbstractUIAppModel getModel() {
		return model;
	}

	public void setModel(AbstractUIAppModel model) {
		this.model = model;
		this.model.addAppEventListener(this);
	}

	private BillForm editor = null;

	public BillForm getEditor() {
		return editor;
	}

	public void setEditor(BillForm editor) {
		this.editor = editor;
	}

	public BackAction() {
		setBtnName("退回");
		setCode("BillBackAction");
	}

	/**
	 * 删除付款申请单
	 * 
	 * @param aggvo
	 * @param eParam
	 * @throws BusinessException
	 */
	/*
	 * private AggPayBillVO deleteBill(AggPayBillVO aggvo, HashMap eParam)
	 * throws BusinessException { AggPayrequest aggPayrequest = null; // 删除付款申请单
	 * if (null != aggvo.getChildrenVO()[0].getAttributeValue("src_billid") &&
	 * !"~".equals(aggvo.getChildrenVO()[0] .getAttributeValue("src_billid"))) {
	 * aggPayrequest = (AggPayrequest) getIMDPersistenceQueryService()
	 * .queryBillOfNCObjectByPK( AggPayrequest.class,
	 * aggvo.getChildrenVO()[0].getAttributeValue( "src_billid").toString())
	 * .getContainmentObject(); } else { throw new
	 * BusinessException("该付款单未关联付款申请单，请检查：" +
	 * aggvo.getParentVO().getAttributeValue("billno")); } if
	 * (aggPayrequest.getParentVO().getApprovestatus() == 1) { aggPayrequest =
	 * (AggPayrequest) getPfBusiAction().processAction( "UNAPPROVE", "FN01",
	 * null, aggPayrequest, null, eParam); aggPayrequest = (AggPayrequest)
	 * getPfBusiAction().processAction( "DELETE", "FN01", null, aggPayrequest,
	 * null, eParam); } else { aggPayrequest = (AggPayrequest)
	 * getPfBusiAction().processAction( "DELETE", "FN01", null, aggPayrequest,
	 * null, eParam); } return aggvo;
	 * 
	 * }
	 */

	@Override
	protected boolean isActionEnable() {
		// Integer invalid=(Integer)
		// editor.getBillCardPanel().getHeadItem("approvestatus").getValueObject();
		// if(invalid!=null){
		// if(invalid==-1){
		// return false;
		// }
		// }

		return true;
	}

	public ReturnMsgPanel getMsgPanel() {
		if (msgPanel == null) {
			msgPanel = new ReturnMsgPanel();
		}
		return msgPanel;
	}

}
