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
		Map<String, String> resultInfo = new HashMap<String, String>();// ������Ϣ
		JSONObject jsonhead = (JSONObject) info.get("headinfo");// ��ϵͳ��Դ��ͷ����
		valid(jsonhead);
		String operator = jsonhead.getString("operator");
		if (operator == null || "".equals(operator)) {
			InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
			InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		} else {
			Map<String, String> userInfo = getUserInfo(operator);
			if (userInfo == null) {
				throw new BusinessException("����Ա��" + operator
						+ "��δ����NC�û�����������,����ϵϵͳ����Ա!");
			}
		}
		String bpmid = jsonhead.getString("bpmid");
		String action = jsonhead.getString("billstate");/*
														 * UNSAVE ���Ų����˻أ�
														 * UNAPPROVE ���������˻أ�
														 * GROUPAPPROVE ���Ų���������
														 * REGAPPROVE ��������������
														 * REFUSE �ܾ���
														 */

		String returnMsg = jsonhead.getString("returnMsg");
		if (returnMsg.contains("�˻ط�����")) {/*
										 * ����BPM�������ƣ� ���繲���˻ط�����NC�����Ѿ��ص�����̬
										 * ����BPM����ص�NCȡ�������ӿ�
										 * ����ֱ�ӷ��سɹ�;���˻�ԭ������NC����BPM��BPMֱ�ӷ���
										 * �ģ��漰����hz
										 * .itf.fssc.impl.billhandler.around
										 * .after.MatterHandlerAfterHandler
										 * ��nc.ui.cmp.settlement.actions.
										 * ChargebackBillAction
										 */
			resultInfo.put("BPMID", bpmid);
			resultInfo.put("msg", "��" + action + "�����ݲ������");
			return JSON.toJSONString(resultInfo);
		}
		if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
				|| "REFUSE".equals(action)) {
			if (StringUtil.isBlank(returnMsg)) {
				throw new BusinessException("�����쳣���˻ز���ʱ�˻���Ϣ����Ϊ��!");
			}
		}
		BXVO aggVO = (BXVO) getBillVO(BXVO.class, "nvl(dr,0)=0 and zyx30='"
				+ bpmid + "'");
		try {
			if (aggVO == null) {
				throw new BusinessException("NCϵͳbpm����δ�ж�Ӧ����");
			}
			String billno = aggVO.getParentVO().getDjbh();
			String billid = aggVO.getParentVO().getPrimaryKey();
			resultInfo.put("billno", billno);
			HashMap paramMap = new HashMap();
			paramMap.put("flowdefpk", aggVO.getParentVO().getPrimaryKey());
			paramMap.put("nolockandconsist", true);
			if ("REGAPPROVE".equals(action) || "GROUPAPPROVE".equals(action)) {
				approveSilently("264X", aggVO.getParentVO().getPrimaryKey(),
						"Y", "��׼", "", false);
			} else if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
					|| "REFUSE".equals(action)) {// BPM���Ų����˻ز����������taskid
				MessageVO[] messagevos1 = null;
				if (aggVO.getParentVO().getSpzt() == 1) {// ����ͨ���ĵ�����Ҫȡ������֮�������ֹ����
					messagevos1 = (MessageVO[]) getPfBusiAction()
							.processAction(IPFActionName.UNAPPROVE, "264X",
									null, aggVO, null, paramMap);
				}
				messagevos1 = (MessageVO[]) approveSilently("264X", aggVO
						.getParentVO().getPrimaryKey(), "R", returnMsg, "",
						true);
				// ����VO����(��BPM�����޸�)
				if (messagevos1 != null && messagevos1.length > 0
						&& messagevos1[0].getSuccessVO() != null) {
					if ("REFUSE".equals(action)) {
						messagevos1[0].getSuccessVO().getParentVO()
								.setAttributeValue(JKBXHeaderVO.ZYX30, "~");// BPM����
					}
					messagevos1[0].getSuccessVO().getParentVO()
							.setAttributeValue(JKBXHeaderVO.DEF65, "~");// ��������
					messagevos1[0].getSuccessVO().getParentVO()
							.setAttributeValue(JKBXHeaderVO.ZYX29, "N");// �Ƿ���BPM������
					updateBillWithAttrs(messagevos1[0].getSuccessVO(),
							new String[] { JKBXHeaderVO.ZYX29,
									JKBXHeaderVO.ZYX30, JKBXHeaderVO.DEF65 });
				}
				// ֪ͨ�����������
				Map<String, Object> map = new HashMap<String, Object>();
				String solId = aggVO.getParentVO().getDef65();// ����ƽ̨����ID
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
		resultInfo.put("msg", "��" + action + "�����ݲ������");
		return JSON.toJSONString(resultInfo);
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("����״̬����Ϊ��");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPMҵ�񵥾���������Ϊ��");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("Ŀ�굥�ݲ���Ϊ��");
	}
}
