package nc.bs.tg.outside.reimbursement.bpm;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.tg.outside.utils.BPMBillUtil;
import nc.itf.tg.outside.ITGSyncService;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.erm.common.MessageVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import uap.distribution.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BpmLLBXBillStatesUtils extends BPMBillUtil implements
		ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		Map<String, String> resultInfo = new HashMap<String, String>();// 返回信息
		JSONObject jsonhead = (JSONObject) info.get("headinfo");// 外系统来源表头数据
		valid(jsonhead);
		String operator = jsonhead.getString("operator");
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("操作员【" + operator
						+ "】未能在NC用户档案关联到,请联系系统管理员!");
			}
		}
		String bpmid = jsonhead.getString("bpmid");
		String action = jsonhead.getString("billstate");/*
														 * UNSAVE 集团财务退回；
														 * UNAPPROVE 地区财务退回；
														 * GROUPAPPROVE 集团财务审批；
														 * REGAPPROVE 地区财务审批；
														 * REFUSE 拒绝；
														 */

		String returnMsg = jsonhead.getString("returnMsg");
		if (returnMsg.contains("退回发起人")) {/*
										 * 处理BPM反调机制； 例如共享退回发起人NC单据已经回到自由态
										 * ，但BPM还会回调NC取消审批接口
										 * ，故直接返回成功;该退回原因是由NC发给BPM，BPM直接返回
										 * 的，涉及类有hz
										 * .itf.fssc.impl.billhandler.around
										 * .after.MatterHandlerAfterHandler
										 * 和nc.ui.cmp.settlement.actions.
										 * ChargebackBillAction
										 */
			resultInfo.put("BPMID", bpmid);
			resultInfo.put("msg", "【" + action + "】单据操作完成");
			return JSON.toJSONString(resultInfo);
		}
		if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
				|| "REFUSE".equals(action)) {
			if (StringUtil.isBlank(returnMsg)) {
				throw new BusinessException("操作异常，退回操作时退回信息不能为空!");
			}
		}
		BXVO aggVO = (BXVO) getBillVO(BXVO.class, "nvl(dr,0)=0 and zyx30='"
				+ bpmid + "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("NC系统bpm主键未有对应单据");
			}
			String billno = aggVO.getParentVO().getDjbh();
			String billid = aggVO.getParentVO().getPrimaryKey();
			resultInfo.put("billno", billno);
			HashMap paramMap = new HashMap();
			paramMap.put("flowdefpk", aggVO.getParentVO().getPrimaryKey());
			paramMap.put("nolockandconsist", true);
			if ("REGAPPROVE".equals(action) || "GROUPAPPROVE".equals(action)) {
				approveSilently("264X", aggVO.getParentVO().getPrimaryKey(),
						"Y", "批准", "", false);
			} else if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
					|| "REFUSE".equals(action)) {// BPM集团财务退回操作：不清除taskid
				MessageVO[] messagevos1 = null;
				if (aggVO.getParentVO().getSpzt() == 1) {// 审批通过的单据需要取消审批之后才能终止流程
					messagevos1 = (MessageVO[]) getPfBusiAction()
							.processAction(IPFActionName.UNAPPROVE, "264X",
									null, aggVO, null, paramMap);
				}
				messagevos1 = (MessageVO[]) approveSilently("264X", aggVO
						.getParentVO().getPrimaryKey(), "R", returnMsg, "",
						true);
				// 更新VO数据(将BPM流程修改)
				if (messagevos1 != null && messagevos1.length > 0
						&& messagevos1[0].getSuccessVO() != null) {
					if ("REFUSE".equals(action)) {
						messagevos1[0].getSuccessVO().getParentVO()
								.setAttributeValue(JKBXHeaderVO.ZYX30, "~");// BPM主键
					}
					messagevos1[0].getSuccessVO().getParentVO()
							.setAttributeValue(JKBXHeaderVO.DEF65, "~");// 共享主键
					messagevos1[0].getSuccessVO().getParentVO()
							.setAttributeValue(JKBXHeaderVO.ZYX29, "N");// 是否在BPM流程中
					updateBillWithAttrs(messagevos1[0].getSuccessVO(),
							new String[] { JKBXHeaderVO.ZYX29,
									JKBXHeaderVO.ZYX30, JKBXHeaderVO.DEF65 });
				}
				// 通知共享清除流程
				Map<String, Object> map = new HashMap<String, Object>();
				String solId = aggVO.getParentVO().getDef65();// 共享平台流程ID
				if (!StringUtil.isBlank(solId)) {
					map.put("instId", solId);
					TGCallUtils.getUtils().onDesCallService(billid, "SHARE",
							"onAfterDelNoticeShare", map);
				}
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		resultInfo.put("BPMID", bpmid);
		resultInfo.put("msg", "【" + action + "】单据操作完成");
		return JSON.toJSONString(resultInfo);
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("操作状态不能为空");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPM业务单据主键不能为空");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("目标单据不能为空");
	}
}
