package nc.bs.tg.outside.bpm.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.itf.tg.outside.IBPMBillCont;
import nc.vo.cdm.applybankcredit.AggApplyBankCreditVO;
import nc.vo.cdm.contractbankcredit.AggContractBankCreditVO;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.outside.BPMBillStateParaVO;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.trade.pub.IBillStatus;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

public class SyncBPMBillStatesUtils extends BPMBillUtils {
	static SyncBPMBillStatesUtils utils;
	private Map<String, String> acttionMap = null;
	private Map<String, Integer> billStateMap = null;

	public static SyncBPMBillStatesUtils getUtils() {
		if (utils == null) {
			utils = new SyncBPMBillStatesUtils();
		}
		return utils;
	}

	public String onSyncBillState(BPMBillStateParaVO vo)
			throws BusinessException {
		String billtypeName = vo.getBilltypeName();
		String billstate = vo.getBillstate();
		String state=billstate;
		String bpmid = vo.getBpmid();
		String operator = vo.getOperator();
		String deletestate = null;
		
		if("UNSAVE".equals(billstate)){
			deletestate = "UNSAVE";
		}
		if(IPFActionName.UNAPPROVE.equals(billstate)){
			billstate = IPFActionName.UNSAVE;
		}
		
		String billqueue = IBPMBillCont.getBillNameMap().get(billtypeName)
				+ ":" + bpmid;
		if (!getActtionMap().containsKey(billstate)) {
			throw new BusinessException("【" + billqueue + "】,不存在" + billstate
					+ "处理操作,请联系系统管理员!");
		}

		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("操作员【" + operator
						+ "】未能在NC用户档案关联到,请联系系统管理员！");
			}
			InvocationInfoProxy.getInstance()
					.setUserId(userInfo.get("cuserid"));
			InvocationInfoProxy.getInstance().setUserCode(
					userInfo.get("user_code"));
		}
		try {

			String actionName = null;
			String billType = null;
			WorkflownoteVO worknoteVO = null;
			AggregatedValueObject billvo = null;
			Object userObj = null;
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String pk_group = null;
			boolean isexecute = true;
			BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
			
			if ("01".equals(billtypeName) || "02".equals(billtypeName)) {// 融资财顾费/融资费用
				billType = "RZ06";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;
				AggFinancexpenseVO aggVO = (AggFinancexpenseVO) getBillVO(
						AggFinancexpenseVO.class,
						"isnull(dr,0)=0 and def19 = '" + bpmid + "'");
				//add by tjl 2020-11-04
				//如果单据状态是审批态或者审批进行中,则直接返回BPM成功,不做审批操作
				if(aggVO.getParentVO().getApprovestatus()==1||aggVO.getParentVO().getApprovestatus()==2){
					return "【" + billqueue + "】,"+"操作完成";
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = (String) aggVO.getParentVO().getAttributeValue(
						"pk_group");
				billvo = aggVO;

			} else if ("04".equals(billtypeName) || "03".equals(billtypeName)) {// 还本/还息
				
				billType = "36FF";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;

				AggRePayReceiptBankCreditVO aggVO = (AggRePayReceiptBankCreditVO) getBillVO(
						AggRePayReceiptBankCreditVO.class,
						"isnull(dr,0)=0 and vdef19 = '" + bpmid + "'");
				//add by tjl 2020-11-04
				//如果单据状态是审批态或者审批进行中,则直接返回BPM成功,不做审批操作
				if(aggVO.getParentVO().getAttributeValue("vbillstatus")
						.equals(1)
						|| aggVO.getParentVO()
								.getAttributeValue("vbillstatus").equals(2)){
					return "【" + billqueue + "】,"+"操作完成";
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = (String) aggVO.getParentVO().getAttributeValue(
						"pk_group");
				billvo = aggVO;

			} else if ("05".equals(billtypeName)) {// 银行贷款合同申请
				billType = "36FA";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;
				AggApplyBankCreditVO aggVO = (AggApplyBankCreditVO) getBillVO(
						AggApplyBankCreditVO.class,
						"isnull(dr,0)=0 and vdef19 = '" + bpmid + "'");
				//add by tjl 2020-11-04
				//如果单据状态是审批态或者审批进行中,则直接返回BPM成功,不做审批操作
				if(aggVO.getParentVO().getAttributeValue("vbillstatus")
						.equals(1)
						|| aggVO.getParentVO()
								.getAttributeValue("vbillstatus").equals(2)){
					return "【" + billqueue + "】,"+"操作完成";
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = aggVO.getParentVO().getPk_group();
				billvo = aggVO;

			} else if ("06".equals(billtypeName)) {// 按揭协议
				billType = "RZ04";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;
				AggMortgageAgreementVO aggVO = (AggMortgageAgreementVO) getBillVO(
						AggMortgageAgreementVO.class,
						"isnull(dr,0)=0 and def19 = '" + bpmid + "'");
				//add by tjl 2020-11-04
				//如果单据状态是审批态或者审批进行中,则直接返回BPM成功,不做审批操作
				if(aggVO.getParentVO().getAttributeValue("approvestatus")
						.equals(1)
						|| aggVO.getParentVO()
								.getAttributeValue("approvestatus").equals(2)){
					return "【" + billqueue + "】,"+"操作完成";
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = aggVO.getParentVO().getPk_group();
				billvo = aggVO;

			}else if("07".equals(billtypeName)){//补票单
				billType = "RZ30";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;
				AggAddTicket aggVO = (AggAddTicket) getBillVO(
						AggAddTicket.class,
						"isnull(dr,0)=0 and def19 = '" + bpmid + "'");
				//add by tjl 2020-11-04
				//如果单据状态是审批态或者审批进行中,则直接返回BPM成功,不做审批操作
				if(aggVO.getParentVO().getAttributeValue("approvestatus")
						.equals(1)
						|| aggVO.getParentVO()
								.getAttributeValue("approvestatus").equals(2)){
					return "【" + billqueue + "】,"+"操作完成";
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = aggVO.getParentVO().getPk_group();
				billvo = aggVO;
			}
			

			// else if ("贷款合同明细".equals(billtypeName)) {// 银行贷款合同
			// billType = "36FB";
			// AggContractBankCreditVO aggVO = (AggContractBankCreditVO)
			// getBillVO(
			// AggContractBankCreditVO.class,
			// "isnull(dr,0)=0 and vdef1 = '" + bpmid + "'");
			//
			// isexecute = checkBillState(aggVO, billstate);
			// pk_group = aggVO.getParentVO().getPk_group();
			// }

			else {
				throw new BusinessException("【" + billqueue
						+ "】,未启用业务单据对接,请联系系统管理员!");
			}

			String maker = (String) billvo.getParentVO().getAttributeValue(
					"creator");
			if(IPFActionName.UNSAVE.equals(billstate)){
				actionName = actionName+maker;
			}
			if(IPFActionName.UNAPPROVE.equals(state)){//BPM操作为UNAPPROVE不删影像编码
				List<String> list= Arrays.asList(new String[]{"36FF","RZ30","RZ06"});
				if(list.contains(billType)){
					billvo.getParentVO().setAttributeValue("def47", "Y");
				}
				List<String> listApply= Arrays.asList(new String[]{"36FA"});
				if(listApply.contains(billType)){
					billvo.getParentVO().setAttributeValue("vdef10", "Y");
				}
				List<String> listMort= Arrays.asList(new String[]{"RZ04"});
				if(listMort.contains(billType)){
					billvo.getParentVO().setAttributeValue("def10", "Y");
				}
			}
//			billvo.getParentVO().getAttributeValue("isunapprove");
			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			if (!isexecute) {
				getPfBusiAction().processAction(actionName, billType,
						worknoteVO, billvo, userObj, eParam);
			}
			//删除bpmid，在状态为BACK时删除
			
//			if ("UNSAVE".equals(deletestate)) {
//
//				
//				String sql = null;
//				if ("RZ06".equals(billType)) {
//					sql = "update tgrz_financexpense set def19 = '~' where nvl(dr,0)=0 and def19='"
//							+ bpmid + "'";
//				} else if ("36FF".equals(billType)) {
//					sql = "update cdm_repayrcpt set vdef19 = '~' where nvl(dr,0)=0 and vdef19='"
//							+ bpmid + "'";
//				} else if ("36FA".equals(billType)) {
//					sql = "update cdm_apply set vdef19 = '~' where nvl(dr,0)=0 and vdef19='"
//							+ bpmid + "'";
//				} else if ("RZ04".equals(billType)) {
//					sql = "update tgrz_mortgageagreement set def19 = '~' where nvl(dr,0)=0 and def19='"
//							+ bpmid + "'";
//				}else if("RZ30".equals(billType)){
//					sql = "update tg_addticket set def19 = '~' where nvl(dr,0)=0 and def19='"
//							+ bpmid + "'";
//				}
//				BaseDAO dao = new BaseDAO();
//				dao.executeUpdate(sql);
//			}
		} catch (Exception e) {
			throw new BusinessException(
					"【" + billqueue + "】," + e.getMessage(), e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}

		return "【" + billqueue + "】," + getActtionMap().get(billstate)
				+ "操作完成!";
	}

	private boolean checkBillState(AggAddTicket aggVO, String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getApprovestatus().intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 检验还本/还息是否当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggRePayReceiptBankCreditVO aggVO,
			String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getVbillstatus().intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 财顾费请款/融资费用请款检验业务单据是否已为当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggFinancexpenseVO aggVO, String billstate) {
		if (getBillStateMap().get(billstate).intValue() == ((Integer) aggVO
				.getParentVO().getAttributeValue("approvestatus")).intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 银行贷款合同(贷款合同明细)检验业务单据是否已为当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggContractBankCreditVO aggVO,
			String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getVbillstatus().intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 按揭协议检验业务单据是否已为当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggMortgageAgreementVO aggVO,
			String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getApprovestatus().intValue()) {
			return true;
		}
		return false;
	}

	/**
	 * 贷款合同（贷款合同申请）检验业务单据是否已为当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggApplyBankCreditVO aggVO, String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getVbillstatus().intValue()) {
			return true;
		}

		return false;
	}

	/**
	 * 流程操作MAP
	 * 
	 * @return
	 */
	public Map<String, String> getActtionMap() {
		if (acttionMap == null) {
			acttionMap = new HashMap<String, String>();
			acttionMap.put(IPFActionName.SAVE, "提交");
			acttionMap.put(IPFActionName.APPROVE, "审批");
			acttionMap.put(IPFActionName.UNAPPROVE, "弃审");
			acttionMap.put(IPFActionName.UNSAVE, "撤回");

		}
		return acttionMap;
	}

	/**
	 * 
	 * @return
	 */
	public Map<String, Integer> getBillStateMap() {
		if (billStateMap == null) {
			billStateMap = new HashMap<String, Integer>();
			billStateMap.put(IPFActionName.SAVE, IBillStatus.COMMIT);
			billStateMap.put(IPFActionName.APPROVE, IBillStatus.CHECKPASS);
			billStateMap.put(IPFActionName.UNAPPROVE, IBillStatus.FREE);
			billStateMap.put(IPFActionName.UNSAVE, IBillStatus.FREE);

		}
		return billStateMap;
	}

	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}
}
