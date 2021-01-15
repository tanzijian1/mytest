package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.utils.BPMBillUtil;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowAdmin;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.arap.basebill.BaseAggVO;
import nc.vo.arap.estipayable.AggEstiPayableBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.flowinstance.FlowInstanceVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.workflow.admin.WorkflowManageContext;
import uap.distribution.util.StringUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ��Դ��SRM���ҵ��,BPMͬ������˻��������
 * 
 * @author ASUS
 * 
 */
public class PayablebillSyncStateForBPM extends BPMBillUtil implements
		ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		Map<String, String> resultInfo = new HashMap<String, String>();// ������Ϣ
		JSONObject jsonhead = (JSONObject) JSONObject.toJSON(info);// ��ϵͳ��Դ��ͷ����
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
		BaseAggVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class,
				"nvl(dr,0)=0 and bpmid='"
						+ bpmid
						+ "' and  pk_tradetype in('F1-Cxx-LL01','F1-Cxx-LL06')  ");
		if (aggVO == null) {
			aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"nvl(dr,0)=0 and bpmid='" + bpmid
							+ "' and  pk_tradetype ='F3-Cxx-LL01'");
		}
		try {
			if (aggVO == null) {
				throw new BusinessException("NCϵͳbpm����δ�ж�Ӧ����");
			}
			String billno = (String) aggVO.getParentVO().getAttributeValue(
					IBillFieldGet.BILLNO);
			String pk_billtype = (String) aggVO.getParentVO()
					.getAttributeValue(IBillFieldGet.PK_BILLTYPE);
			resultInfo.put("billno", billno);
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if ("REGAPPROVE".equals(action) || "GROUPAPPROVE".equals(action)) {
				approveSilently(pk_billtype, aggVO.getParentVO()
						.getPrimaryKey(), "Y", "��׼", "", false);
			} else if ("UNSAVE".equals(action) || "UNAPPROVE".equals(action)
					|| "REFUSE".equals(action)) {// BPM���Ų����˻ز����������taskid
				// ��ֹSRM��������
				try {
					IPushBPMLLBillService service = NCLocator.getInstance()
							.lookup(IPushBPMLLBillService.class);
					service.dealChargebackSRMPay(bpmid, returnMsg);
				} catch (Exception e) {
					throw new BusinessException("��ֹ���̷����쳣:" + e.getMessage(), e);
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
	}

	public void dealChargebackSRMPay(String taskid, String returnMsg)
			throws BusinessException {

		HashMap<String, String> eParam = new HashMap<String, String>();
		eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
				PfUtilBaseTools.PARAM_NOTE_CHECKED);

		BaseAggVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class,
				"nvl(dr,0)=0 and bpmid='"
						+ taskid
						+ "' and  pk_tradetype in('F1-Cxx-LL01','F1-Cxx-LL06') ");
		if (aggVO == null) {
			aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"nvl(dr,0)=0 and bpmid='" + taskid
							+ "' and  pk_tradetype ='F3-Cxx-LL01'");
		}
		String pk_billtype = (String) aggVO.getParentVO().getAttributeValue(
				IBillFieldGet.PK_BILLTYPE);
		String pk_tradetype = (String) aggVO.getParentVO().getAttributeValue(
				IBillFieldGet.PK_TRADETYPE);
		String pk_tradetypeid = PfDataCache.getBillType(pk_tradetype)
				.getPk_billtypeid();
		String billid = aggVO.getParentVO().getPrimaryKey();
		String isCycleProvision = (String) aggVO.getParentVO()
				.getAttributeValue("defitem29");// �Ƿ������Լ���
		String solId = (String) aggVO.getParentVO().getAttributeValue("def57");// ����ƽ̨����ID;

		try {
			BaseAggVO[] vos = null;
			if ((Integer) aggVO.getParentVO().getAttributeValue(
					IBillFieldGet.BILLSTATUS) == 1) {// ����ͨ���ĵ�����Ҫȡ������֮�������ֹ����
				AggPayBillVO payvo = (AggPayBillVO) getBillVO(
						AggPayBillVO.class, "nvl(dr,0)=0  and bpmid='" + taskid
								+ "' and  pk_tradetype ='F3-Cxx-LL09' ");
				if (payvo != null) {
					AggPayBillVO[] billvos = (AggPayBillVO[]) getPfBusiAction()
							.processAction(IPFActionName.UNAPPROVE,
									payvo.getHeadVO().getPk_billtype(), null,
									payvo, null, eParam);
					getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
							billvos[0].getHeadVO().getPk_billtype(), null,
							billvos[0], null, eParam);
				}
				vos = (BaseAggVO[]) getPfBusiAction().processAction(
						IPFActionName.UNAPPROVE, pk_billtype, null, aggVO,
						null, null);
				// vos = (BaseAggVO[]) approveSilently(
				// pk_billtype,
				// aggVO.getParentVO().getPrimaryKey(),
				// "R",
				// returnMsg,
				// (String) vos[0].getParentVO().getAttributeValue(
				// IBillFieldGet.APPROVER), true);
				terminateBill(billid, returnMsg, pk_tradetypeid);
			} else {
				// String approver = (String) aggVO.getParentVO()
				// .getAttributeValue(IBillFieldGet.APPROVER);
				// if (approver != null) {
				// vos = (BaseAggVO[]) approveSilently(pk_billtype, aggVO
				// .getParentVO().getPrimaryKey(), "R", returnMsg,
				// approver, true);
				// }
				terminateBill(billid, returnMsg, pk_tradetypeid);
			}
			aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class,
					"nvl(dr,0)=0 and bpmid='"
							+ taskid
							+ "' and  pk_tradetype in('F1-Cxx-LL01','F1-Cxx-LL06') ");
			if (aggVO == null) {
				aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
						"nvl(dr,0)=0 and bpmid='" + taskid
								+ "' and  pk_tradetype ='F3-Cxx-LL01'");
			}
			eParam.put(PfUtilBaseTools.PARAM_NO_LOCK, billid);
			getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
					pk_billtype, null, aggVO, null, eParam);
			if ("�����˻ط�����".equals(returnMsg)) {
				Map<String, String> map = new HashMap<String, String>();
				String bpmid = (String) aggVO.getParentVO().getAttributeValue(
						"bpmid");
				map.put("TaskID", bpmid);
				map.put("Comments", returnMsg);
				map.put("usercode", "OtherSysOpt");
				TGCallUtils.getUtils().onDesCallService(
						aggVO.getParentVO().getPrimaryKey(), "BPM",
						"ReturnToInitiator", map);
			}

			if (!"F1-Cxx-LL06".equals(pk_tradetype)) {
				if ("Y".equals(isCycleProvision)) {
					BaseAggVO redAggVO = (AggPayableBillVO) getBillVO(
							AggPayableBillVO.class, "nvl(dr,0)=0 and bpmid='"
									+ taskid
									+ "' and  pk_tradetype ='F1-Cxx-LL09' ");
					BaseAggVO[] redAggVOs = null;
					if ((Integer) redAggVO.getParentVO().getAttributeValue(
							IBillFieldGet.BILLSTATUS) == 1) {// ����ͨ���ĵ�����Ҫȡ������֮�������ֹ����
						redAggVOs = (BaseAggVO[]) getPfBusiAction()
								.processAction(IPFActionName.UNAPPROVE,
										pk_billtype, null, redAggVO, null, null);
					} else {
						redAggVOs = new BaseAggVO[] { redAggVO };
					}
					String pk_billtype_red = (String) redAggVOs[0]
							.getParentVO().getAttributeValue(
									IBillFieldGet.PK_BILLTYPE);
					getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
							pk_billtype_red, null, redAggVOs[0], null, null);
				}

				BaseAggVO esBillVO = (BaseAggVO) getBillVO(
						AggEstiPayableBillVO.class, "nvl(dr,0)=0 and def30 ='"
								+ taskid
								+ "' and  pk_tradetype ='23E1-Cxx-LL03' ");
				if (esBillVO != null) {
					BaseAggVO[] esBillVOs = null;
					if ((Integer) esBillVO.getParentVO().getAttributeValue(
							IBillFieldGet.BILLSTATUS) == 1) {// ����ͨ���ĵ�����Ҫȡ������֮�������ֹ����
						esBillVOs = (BaseAggVO[]) getPfBusiAction()
								.processAction(IPFActionName.UNAPPROVE,
										pk_billtype, null, esBillVO, null, null);
					} else {
						esBillVOs = new BaseAggVO[] { esBillVO };
					}
					String pk_billtype_red = (String) esBillVOs[0]
							.getParentVO().getAttributeValue(
									IBillFieldGet.PK_BILLTYPE);
					getPfBusiAction().processAction(IPFActionName.DEL_DELETE,
							pk_billtype_red, null, esBillVOs[0], null, null);
				}
			}

			// ֪ͨ�����������
			Map<String, Object> map = new HashMap<String, Object>();
			if (!StringUtil.isBlank(solId)) {
				map.put("instId", solId);
				TGCallUtils.getUtils().onDesCallService(billid, "SHARE",
						"onAfterDelNoticeShare", map);
			}

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

	}

	/**
	 * ��ֹ��������
	 * 
	 * @param billid
	 * @param returnMsg
	 * @param pk_tradetype
	 * @param pk_tradetypeid
	 * @throws BusinessException
	 */
	public void terminateBill(String billid, String returnMsg,
			String pk_tradetypeid) throws BusinessException {
		try {
			FlowInstanceVO data = (FlowInstanceVO) getBaseDAO().executeQuery(
					"select * from pub_wf_instance  where billid ='" + billid
							+ "'", new BeanProcessor(FlowInstanceVO.class));
			if (data != null) {
				data.setBilltype(pk_tradetypeid);
			}
			WorkflowManageContext context = getManageContext(data);
			context.setManageReason(returnMsg);
			context.setApproveStatus(0);
			context.setFlowType(2);
			IWorkflowAdmin admin = (IWorkflowAdmin) NCLocator.getInstance()
					.lookup(IWorkflowAdmin.class);
			admin.terminateWorkflow(context);
		} catch (Exception e) {
			throw new BusinessException("��ֹ�������̷����쳣!" + e.getMessage(), e);
		}
	}

	public WorkflowManageContext getManageContext(Object data) {
		WorkflowManageContext context = new WorkflowManageContext();
		if ((data instanceof FlowInstanceVO)) {
			FlowInstanceVO instVO = (FlowInstanceVO) data;
			context.setBillId(instVO.getBillid());
			context.setBillNo(instVO.getBillno());
			String pk_billtypeid = instVO.getBilltype();
			String billType = PfUtilBaseTools
					.getBillTypeCodeByPK(pk_billtypeid);
			context.setBillType(billType);
			context.setApproveStatus(instVO.getProcstatus_value());
			context.setFlowType(instVO.getWorkflow_type_value());
			context.setFlowinstancePk(instVO.getPk_wf_instance());
		}

		return context;
	}

}
