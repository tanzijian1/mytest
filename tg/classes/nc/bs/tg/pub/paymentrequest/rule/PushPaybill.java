package nc.bs.tg.pub.paymentrequest.rule;

import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tg.ISqlThread;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IPFBusiAction;
import nc.itf.uap.pf.IPfExchangeService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.pubapp.pattern.model.entity.bill.IBill;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Payrequest;

public class PushPaybill implements IRule {

	private IPfExchangeService ips = null;

	@Override
	public void process(Object[] vos) {
		IBill[] bills = (IBill[]) vos;

		for (IBill bill : bills) {
			AggPayrequest aggvo = (AggPayrequest) bill;
			Payrequest headvo = (Payrequest) aggvo.getParentVO();
			InvocationInfoProxy.getInstance().setGroupId(
					(String) headvo.getAttributeValue("pk_group"));

			String transtype = headvo.getTranstype();
			// 如果是走自动审批的不能在付款申请单里面审批2019-12-30-谈子健
			/*
			 * if ("FN01-Cxx-002".equals(transtype) ||
			 * "FN01-Cxx-006".equals(transtype)) { ExceptionUtils
			 * .wrappBusinessException("自动审批的单据不能在此节点审批,请到对应的【应付单管理】节点审批!"); }
			 */
			if ("FN01-Cxx-005".equals(transtype)
					|| "FN01-Cxx-001".equals(transtype)
					|| "FN01-Cxx-003".equals(transtype)
					|| "FN01-Cxx-007".equals(transtype)) {
				try {
					pushvo(aggvo, headvo);
				} catch (Exception e) {
					ExceptionUtils.wrappBusinessException(e.getMessage());
				}
			}

		}
	}

	/**
	 * 
	 * @param aggvo
	 * @param headvo
	 */
	public void pushvo(AggPayrequest aggvo, Payrequest headvo)
			throws BusinessException {
		BaseDAO dao=new BaseDAO();
		StringBuffer sb=new StringBuffer();
		IPFBusiAction iPFBusiAction = (IPFBusiAction) NCLocator.getInstance()
				.lookup(IPFBusiAction.class.getName());
		HashMap eParam = new HashMap();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);
		AggPayBillVO[] vo = null;
		if ("FN01-Cxx-007".equals(headvo.getTranstype())) {
			vo = (AggPayBillVO[]) getPfExchangeService()
					.runChangeDataAryNeedClassify("FN01", "F3-Cxx-028",
							new AggPayrequest[] { aggvo }, null, 2);
		} else {
			vo = (AggPayBillVO[]) getPfExchangeService()
					.runChangeDataAryNeedClassify("FN01", "F3",
							new AggPayrequest[] { aggvo }, null, 2);
		}

		Boolean istrue = vo == null || vo.length == 0;
		if (istrue) {
			throw new BusinessException("付款申请单["
					+ headvo.getAttributeValue("pk_billtype")
					+ "]转换付款单异常 [未转换成功]!");
		}
		// 表头默认值设置
		for (AggPayBillVO payvo : vo) {
			// 表头默认值设置
			payvo.getParent().setAttributeValue("billstatus", -1);
			payvo.getParent().setAttributeValue("isflowbill", UFBoolean.FALSE);
			payvo.getParent().setAttributeValue("isforce", UFBoolean.FALSE);
			payvo.getParent().setAttributeValue("isfromindependent",
					UFBoolean.FALSE);
			payvo.getParent()
					.setAttributeValue("ismandatepay", UFBoolean.FALSE);
			payvo.getParent().setAttributeValue("isnetpayready",
					UFBoolean.FALSE);
			payvo.getParent().setAttributeValue("isonlinepay", UFBoolean.FALSE);
			payvo.getParent().setAttributeValue("isreded", UFBoolean.FALSE);
//			payvo.getParent().setAttributeValue("objtype", 1);
			payvo.getParent().setAttributeValue("sddreversalflag",
					UFBoolean.FALSE);
			if(aggvo.getParentVO().getBpmid()!=null&&aggvo.getParentVO().getBpmid().length()>0){
				payvo.getHeadVO().setBpmid(aggvo.getParentVO().getBpmid());
				payvo.getParentVO().setAttributeValue("bpmid", aggvo.getParentVO().getBpmid());
			}
			payvo.getParent().setAttributeValue("pk_balatype",
					headvo.getPk_balatype());
			if ("FN01-Cxx-001".equals(aggvo.getParent().getAttributeValue(
					"transtype"))
					|| "FN01-Cxx-006".equals(aggvo.getParent()
							.getAttributeValue("transtype"))) {
				payvo.getParent().setAttributeValue("pk_tradetype",
						"F3-Cxx-007");
				String F3_Cxx_007pk_billtypeid = getPk_billtypeidByCode("F3-Cxx-007");
				payvo.getParent().setAttributeValue("pk_tradetypeid",
						F3_Cxx_007pk_billtypeid);
			}
			if ("FN01-Cxx-002".equals(aggvo.getParent().getAttributeValue(
					"transtype"))
					|| "FN01-Cxx-005".equals(aggvo.getParent()
							.getAttributeValue("transtype"))) {
				payvo.getParent().setAttributeValue("pk_tradetype",
						"F3-Cxx-004");
				String F3_Cxx_004pk_billtypeid = getPk_billtypeidByCode("F3-Cxx-004");
				payvo.getParent().setAttributeValue("pk_tradetypeid",
						F3_Cxx_004pk_billtypeid);
			}
			if ("FN01-Cxx-003".equals(aggvo.getParent().getAttributeValue(
					"transtype"))) {
				payvo.getParent().setAttributeValue("pk_tradetype",
						"F3-Cxx-005");
				String F3_Cxx_005pk_billtypeid = getPk_billtypeidByCode("F3-Cxx-005");
				payvo.getParent().setAttributeValue("pk_tradetypeid",
						F3_Cxx_005pk_billtypeid);
			}

			// 表体默认值设置
			for (PayBillItemVO bvo : payvo.getBodyVOs()) {
				bvo.setAttributeValue("isdiscount", UFBoolean.FALSE);
//				bvo.setAttributeValue("objtype", 1);
				bvo.setAttributeValue("pk_currtype", "1002Z0100000000001K1");
				bvo.setAttributeValue("pk_fiorg",
						bvo.getAttributeValue("pk_org"));
				bvo.setAttributeValue("pk_fiorg_v",
						bvo.getAttributeValue("pk_org_v"));
				bvo.setAttributeValue("pk_payitem", "ID_INDEX0");
				bvo.setAttributeValue("rowno", 0);
				bvo.setAttributeValue("sett_org",
						bvo.getAttributeValue("pk_org"));
				bvo.setAttributeValue("sett_org_v",
						bvo.getAttributeValue("pk_org_v"));
			}
			WorkflownoteVO srcworknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow(IPFActionName.SAVE, "F3", payvo, null);
			
			AggPayBillVO[] aggpayvos=(AggPayBillVO[])iPFBusiAction.processAction("SAVE", "F3", srcworknoteVO, payvo,
					null, eParam);
			if(aggpayvos!=null){//BPMID丢失再保存一次
				for(AggPayBillVO billvo:aggpayvos){
					dao.executeUpdate("update ap_paybill set bpmid='"+aggvo.getParentVO().getBpmid()+"' where pk_paybill='"+billvo.getHeadVO().getPk_paybill()+"'");
				}
			}
			
		}
	}

	/**
	 * VO转换服务类
	 * 
	 * @return
	 */
	public IPfExchangeService getPfExchangeService() {
		if (ips == null) {
			ips = NCLocator.getInstance().lookup(IPfExchangeService.class);
		}
		return ips;
	}

	/**
	 * 根据code查询对应的交易类型主键
	 * 
	 * @return
	 * @throws BusinessException
	 */
	private String getPk_billtypeidByCode(String code) throws BusinessException {
		IUAPQueryBS bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		String pk_billtypeid = (String) bs.executeQuery(
				"select pk_billtypeid from bd_billtype t where t.pk_billtypecode = '"
						+ code + "'", new ColumnProcessor());
		return pk_billtypeid;

	}
}
