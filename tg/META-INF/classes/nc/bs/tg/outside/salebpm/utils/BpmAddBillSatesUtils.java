package nc.bs.tg.outside.salebpm.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.BPMBillStateParaVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.yer.AggAddBillHVO;

/**
 * BPM推送NC进行审批，反审操作
 * 
 * @author ln
 * 
 */
public class BpmAddBillSatesUtils extends SaleBPMBillUtils {
	static BpmAddBillSatesUtils utils;
	private Map<String, String> acttionMap = null;
	private Map<String, Integer> billStateMap = null;

	public static BpmAddBillSatesUtils getUtils() {
		if (utils == null) {
			utils = new BpmAddBillSatesUtils();
		}
		return utils;
	}

	public String onSyncBillState(BPMBillStateParaVO vo)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		String billtypeName = vo.getBilltypeName();
		String billstate = vo.getBillstate();
		String bpmid = vo.getBpmid();
		String operator = vo.getOperator();
		if ("UNAPPROVE".equals(billstate)) {
			billstate = "UNAPPROVE";
		}
		String billqueue = ISaleBPMBillCont.getBillNameMap().get(billtypeName)
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
			AggAddBillHVO aggVO = (AggAddBillHVO) getBillVO(
					AggAddBillHVO.class, "isnull(dr,0)=0 and def18 = '" + bpmid
							+ "'");
			// 删除bpmid，在状态为BACK时删除
			String actionName = null;
			String billType = "267X";
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			WorkflownoteVO worknoteVO = null;
			Object userObj = null;
			actionName = IPFActionName.UNAPPROVE.equals(billstate) ? "UNAPPROVE"
					: billstate;
			if (IPFActionName.UNAPPROVE.equals(actionName)) {
				aggVO.getParentVO().setDef60("UNAPPROVE");//用于判定BPM传递的操作
				getPfBusiAction().processAction("UNSAVEBILL"+aggVO.getParentVO().getCreator(), billType,
						worknoteVO, aggVO, userObj, null);
			} else if ("APPROVE".equals(billstate)) {
				worknoteVO = iWorkflowMachine.checkWorkFlow(actionName,
						billType, aggVO, null);
				worknoteVO.setChecknote("批准");// 审批批语
				worknoteVO.setApproveresult("Y");// 审批通过
				AggAddBillHVO[] aggvo = (AggAddBillHVO[]) getPfBusiAction()
						.processAction(billstate, billType, worknoteVO, aggVO,
								userObj, null);
				getinvoiceByImg(aggvo[0].getParentVO().getImagcode());
			} else if("UNSAVE".equals(billstate)){
				aggVO.getParentVO().setDef60("UNSAVE");
				getPfBusiAction().processAction("UNSAVEBILL"+aggVO.getParentVO().getCreator(), billType,
						worknoteVO, aggVO, userObj, null);
			}
			// 如果bpm传的状态是unsave则请掉bpmid
			BaseDAO dao = new BaseDAO();
			String sql = "";
			if (IPFActionName.UNSAVE.equals(actionName) || IPFActionName.UNAPPROVE.equals(actionName)) {
				if(IPFActionName.UNSAVE.equals(actionName)){
					sql = "update yer_fillbill set def18 = '~',def19 = '~' where nvl(dr,0)=0 and def18='"
							+ bpmid + "'";
				}else if(IPFActionName.UNAPPROVE.equals(actionName)){
					sql = "update yer_fillbill set def19 = 'N' where nvl(dr,0)=0 and def18='"
							+ bpmid + "'";
				}
				dao.executeUpdate(sql);
			}
			
		} catch (Exception e) {
			throw new BusinessException(
					"【" + billqueue + "】," + e.getMessage(), e);
		} finally {
			SaleBPMBillUtils.removeBillQueue(billqueue);
		}

		return "【" + billqueue + "】," + getActtionMap().get(billstate)
				+ "操作完成!";
	}

	/**
	 * 流程操作MAP
	 * 
	 * @return
	 */
	public Map<String, String> getActtionMap() {
		if (acttionMap == null) {
			acttionMap = new HashMap<String, String>();
			acttionMap.put("SAVEBASE", "提交");
			acttionMap.put(IPFActionName.APPROVE, "审批");
			acttionMap.put(IPFActionName.UNAPPROVE, "弃审");
			acttionMap.put(IPFActionName.UNSAVE, "撤回");
		}
		return acttionMap;
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
				whereCondStr, true, false);
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}
	/**
	 * 去影像系统获取发票数据
	 * @param str
	 * @throws Exception 
	 */
	public void getinvoiceByImg(String str) throws Exception {
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("IMG");
		logVO.setDesbill("影像取发票数据");
		ISyncIMGBillServcie servcie = NCLocator.getInstance().lookup(
				ISyncIMGBillServcie.class);
		try {
			servcie.onSyncInv_RequiresNew(str);
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(EBSBillUtils.STATUS_SUCCESS);
			logVO.setOperator(EBSBillUtils.OperatorName);
		} catch (Exception e) {
			Logger.error("调用影像:" + e.getMessage());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(EBSBillUtils.STATUS_FAILED);
			logVO.setOperator(EBSBillUtils.OperatorName);
			logVO.setSrcparm(e.getMessage());
		} finally { 
			IPushBPMBillFileService service = NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
			service.saveLog_RequiresNew(logVO);
		}
	}
}
