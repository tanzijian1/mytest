package nc.impl.tg.outside;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGCallUtils;
import nc.bs.tg.outside.payablebill.PayablebillSyncStateForBPM;
import nc.bs.tg.outside.salebpm.utils.LLSaleNoteBPMUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.impl.tg.outside.utils.PushBpmDataUtils;
import nc.itf.tg.outside.IPushBPMLLBillService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IWorkflowAdmin;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.erm.matterapp.AggMatterAppVO;
import nc.vo.erm.matterapp.MatterAppVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pf.flowinstance.FlowInstanceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.outside.ResultVO;
import nc.vo.workflow.admin.WorkflowManageContext;
import uap.distribution.util.StringUtil;

public class PushBPMLLBillServiceImpl extends BillUtils implements
		IPushBPMLLBillService {

	private IUAPQueryBS bs = null;

	@Override
	public void chargebackBillToImg(Map<String, String> map)
			throws BusinessException {
		LLSaleNoteBPMUtils.getUtils().chargebackBillToImg(map);
	}

	@Override
	public void dealChargebackMattApp(String taskid, String returnMsg)
			throws BusinessException {

		Integer approvestatus = null;
		String solId = null;

		AggMatterAppVO aggVO = (AggMatterAppVO) getBillVO(AggMatterAppVO.class,
				"nvl(dr,0)=0 and defitem1 ='" + taskid + "'");
		MatterAppVO hvo = aggVO.getParentVO();
		String billtype = hvo.getPk_tradetype();
		String billid = hvo.getPrimaryKey();
		String defitem14 = hvo.getDefitem14();// �Ƿ�Ԥ����
		String isCycleProvision = hvo.getDefitem29();// �Ƿ������Լ���
		String concode = hvo.getDefitem7();// ��ͬ���
		String sql = "select pk_billtypeid from bd_billtype where pk_billtypecode = '"
				+ billtype + "' and nvl(dr,0) = 0";
		String pk_tradetypeid = (String) getIUAPQueryBS().executeQuery(sql,
				new ColumnProcessor());

		Boolean flag = false;
		// ���������뵥�����̲����Ƶ���
		if (hvo.getApprstatus() == 1) {
			flag = true;
		}
		dealChargebackBill(AggMatterAppVO.class, billid, returnMsg, "261X",
				flag);
		sql = "update er_mtapp_bill set billstatus = 0,apprstatus = -1 where pk_mtapp_bill = '"
				+ billid + "'";
		getBaseDAO().executeUpdate(sql);

		HashMap hmPfExParams = new HashMap();
		hmPfExParams.put("nolockandconsist", true);

		if ("Y".equals(defitem14)) {
			// ��ֹ������̲�ɾ��
			AggPayBillVO paybillaggvo = (AggPayBillVO) getBillVO(
					AggPayBillVO.class,
					"nvl(dr,0)=0 and pk_tradetype in('F3-Cxx-LL06','F3-Cxx-LL07') and bpmid ='"
							+ taskid + "'");
			if (paybillaggvo != null) {
				billid = paybillaggvo.getPrimaryKey();
				solId = (String) paybillaggvo.getParentVO().getAttributeValue(
						PayBillVO.DEF57);
				pk_tradetypeid = (String) paybillaggvo.getParentVO()
						.getAttributeValue(PayBillVO.PK_TRADETYPEID);
				approvestatus = (Integer) paybillaggvo.getParentVO()
						.getAttributeValue(PayBillVO.APPROVESTATUS);
				try {
					if (approvestatus != -1) {
						terminateBill(billid, returnMsg, pk_tradetypeid);
						paybillaggvo = (AggPayBillVO) getBillVO(
								AggPayBillVO.class,
								"nvl(dr,0)=0 and pk_tradetype in('F3-Cxx-LL06','F3-Cxx-LL07') and bpmid ='"
										+ taskid + "'");
					}
					getPfBusiAction().processAction("DELETE", "F3", null,
							paybillaggvo, null, hmPfExParams);
				} catch (Exception e) {
					throw new BusinessException("��ֹ�������ʧ��:" + e.getMessage(),
							e);
				}
				// ɾ������֮��֪ͨ����
				noticeShare(billid, solId);
			}
		} else {
			// ��ֹӦ�������̲�ɾ��
			AggPayableBillVO payaggvo = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class,
					"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL02','F1-Cxx-LL03') and bpmid ='"
							+ taskid + "'");
			if (payaggvo != null) {
				approvestatus = (Integer) payaggvo.getParentVO()
						.getAttributeValue(PayableBillVO.APPROVESTATUS);
				billid = payaggvo.getPrimaryKey();
				pk_tradetypeid = (String) payaggvo.getParentVO()
						.getAttributeValue(PayableBillVO.PK_TRADETYPEID);
				solId = (String) payaggvo.getParentVO().getAttributeValue(
						PayableBillVO.DEF57);
				try {
					if (approvestatus != -1) {// ����״̬��Ϊ����̬ʱ
						terminateBill(billid, returnMsg, pk_tradetypeid);
						payaggvo = (AggPayableBillVO) getBillVO(
								AggPayableBillVO.class,
								"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL02','F1-Cxx-LL03') and bpmid ='"
										+ taskid + "'");
					}
					getPfBusiAction().processAction("DELETE", "F1", null,
							payaggvo, null, hmPfExParams);
				} catch (Exception e) {
					throw new BusinessException("��ֹӦ��������ʧ��:" + e.getMessage(),
							e);
				}
				// ɾ������֮��֪ͨ����
				noticeShare(billid, solId);
			}

			// Ӧ���������������ɵĸ��
			AggPayBillVO paybillaggvo = (AggPayBillVO) getBillVO(
					AggPayBillVO.class,
					"nvl(dr,0)=0 and pk_tradetype in('F3-Cxx-LL06','F3-Cxx-LL07') and bpmid ='"
							+ taskid + "'");
			if (paybillaggvo != null) {
				billid = paybillaggvo.getPrimaryKey();
				pk_tradetypeid = (String) paybillaggvo.getParentVO()
						.getAttributeValue(PayBillVO.PK_TRADETYPEID);
				approvestatus = (Integer) paybillaggvo.getParentVO()
						.getAttributeValue(PayBillVO.APPROVESTATUS);
				try {
					if (approvestatus != -1) {
						terminateBill(billid, returnMsg, pk_tradetypeid);
						paybillaggvo = (AggPayBillVO) getBillVO(
								AggPayBillVO.class,
								"nvl(dr,0)=0 and pk_tradetype in('F3-Cxx-LL06','F3-Cxx-LL07') and bpmid ='"
										+ taskid + "'");
					}
					getPfBusiAction().processAction("DELETE", "F3", null,
							paybillaggvo, null, hmPfExParams);
				} catch (Exception e) {
					throw new BusinessException("��ֹӦ�������ɸ������ʧ��:"
							+ e.getMessage(), e);
				}
			}

			if ("Y".equals(isCycleProvision) && "N".equals(defitem14)) {// ����Ӧ����
				AggPayableBillVO redpayaggvo = (AggPayableBillVO) getBillVO(
						AggPayableBillVO.class,
						"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL08') and bpmid ='"
								+ taskid + "'");
				if (redpayaggvo != null) {
					billid = redpayaggvo.getPrimaryKey();
					pk_tradetypeid = (String) redpayaggvo.getParentVO()
							.getAttributeValue(PayableBillVO.PK_TRADETYPEID);
					approvestatus = (Integer) redpayaggvo.getParentVO()
							.getAttributeValue(PayableBillVO.APPROVESTATUS);
					try {
						if (approvestatus != -1) {// ����״̬��Ϊ����̬ʱ
						// terminateBill(billid, returnMsg, pk_tradetypeid);
							getPfBusiAction().processAction("UNAPPROVE", "F1",
									null, redpayaggvo, null, hmPfExParams);
						}

						// ����ͬ�ĺ���������г��
						Object local_money = redpayaggvo.getParentVO()
								.getAttributeValue(PayableBillVO.LOCAL_MONEY);
						UFDouble red_money = new UFDouble(
								local_money == null ? ""
										: local_money.toString());
						// sql = "update fct_ap set def50 = (def50 - "
						// + red_money.div(-1)
						// + ") where fct_ap.vbillcode = '"
						// + concode
						// + "' and fct_ap.blatest = 'Y' and fct_ap.dr = 0;";
						// getBaseDAO().executeUpdate(sql);

						// ���²�ѯ�����ݣ��ٽ�����Ӧ����ɾ��
						redpayaggvo = (AggPayableBillVO) getBillVO(
								AggPayableBillVO.class,
								"nvl(dr,0)=0 and pk_tradetype in('F1-Cxx-LL08') and bpmid ='"
										+ taskid + "'");
						getPfBusiAction().processAction("DELETE", "F1", null,
								redpayaggvo, null, hmPfExParams);
					} catch (Exception e) {
						throw new BusinessException("��ֹ����Ӧ��������ʧ��:"
								+ e.getMessage(), e);
					}
				}
			}
		}

	}

	public void dealChargebackBill(Class c, String billid, String returnMsg,
			String pk_tradetype, Boolean isApproved) throws BusinessException {
		AggregatedValueObject aggVO = getBillVO(c, billid, false);

		if (pk_tradetype.startsWith("261X")) {
			aggVO.getParentVO().setAttributeValue("defitem2", "N");
		}

		String userid = null;
		if (isApproved) {
			userid = getFinalApproverID(billid);
			HashMap hmPfExParams = new HashMap();
			hmPfExParams.put("nolockandconsist", true);
			getPfBusiAction().processAction(IPFActionName.UNAPPROVE + userid,
					pk_tradetype, null, aggVO, null, hmPfExParams);
		}
		try {
			userid = getCurrentApproverID(billid);
			approveSilently(pk_tradetype, billid, "R", returnMsg, userid, true);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
	}

	@Override
	public void dealChargebackPaybill(String billid, String returnMsg,
			String pk_tradetypeid) throws BusinessException {
		terminateBill(billid, returnMsg, pk_tradetypeid);
	}

	/**
	 * ֪ͨ����ϵͳ����ɾ��
	 * 
	 * @param billid
	 *            ��������
	 * @param solId
	 *            ��������ID
	 * @throws BusinessException
	 */
	protected void noticeShare(String billid, String solId)
			throws BusinessException {
		// �����������ID��Ϊ��ʱ��ȡ�ص���ʱ��Ҫɾ����������
		if (!StringUtil.isBlank(solId)) {
			Map<String, Object> map = new HashMap<String, Object>();
			map = new HashMap<String, Object>();
			map.put("instId", solId);
			try {
				TGCallUtils.getUtils().onDesCallService(billid, "SHARE",
						"onAfterDelNoticeShare", map);
			} catch (Exception e) {
				throw new BusinessException("֪ͨ����ɾ���쳣:" + e.getMessage(), e);
			}
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
			FlowInstanceVO data = (FlowInstanceVO) getIUAPQueryBS()
					.executeQuery(
							"select * from pub_wf_instance  where billid ='"
									+ billid + "'",
							new BeanProcessor(FlowInstanceVO.class));
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

	/**
	 * ��ȡ���������ID
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 */
	protected String getFinalApproverID(String billid) throws BusinessException {
		StringBuffer str = new StringBuffer();
		str.append("SELECT checkman FROM pub_workflownote ");
		str.append("where approvestatus = 1 and pub_workflownote.billid ='"
				+ billid + "' ");
		str.append("order by dealdate desc");
		String cuserid = (String) getIUAPQueryBS().executeQuery(str.toString(),
				new ColumnProcessor());
		return cuserid;
	}

	/**
	 * ��ȡ��ǰ������ID
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 */
	protected String getCurrentApproverID(String billid)
			throws BusinessException {
		StringBuffer str = new StringBuffer();
		str.append("SELECT checkman FROM pub_workflownote ");
		str.append("where approvestatus = 0 and approveresult is null and pub_workflownote.actiontype = 'Z' ");
		str.append("and pub_workflownote.pk_wf_task <> '~' and pub_workflownote.billid ='"
				+ billid + "' ");
		str.append("order by dealdate desc");
		String cuserid = (String) getIUAPQueryBS().executeQuery(str.toString(),
				new ColumnProcessor());
		return cuserid;
	}

	/**
	 * ��ȡҵ�񵥾ݾۺ�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String pk, Boolean flag)
			throws BusinessException {
		return getMDQryService().queryBillOfVOByPK(c, pk, flag);
	}

	private IUAPQueryBS getIUAPQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	@Override
	public ResultVO pushBXbillToBpm(String billtype, JKBXVO jkbxvo)
			throws BusinessException {
		ResultVO revo = PushBpmDataUtils.getUtils().pushLLBillToBpm(billtype,
				jkbxvo);
		return revo;
	}

	@Override
	public ResultVO pushMattbillToBpm(String billtype, AggMatterAppVO aggvo)
			throws BusinessException {
		ResultVO revo = PushBpmDataUtils.getUtils().pushMattBillToBpm(billtype,
				aggvo);
		return revo;
	}

	@Override
	public void dealChargebackSRMPay(String taskid, String returnMsg)
			throws BusinessException {
		new PayablebillSyncStateForBPM()
				.dealChargebackSRMPay(taskid, returnMsg);
	}

	@Override
	public void checkInvoiceMsg(String billid, UFDouble totalInvMny,
			UFDouble totalInvTax) throws BusinessException {
		// upload_status; //�ϴ�״̬��0Ϊ�ɹ���1Ϊʧ��;
		// invoice_type:1Ϊ��ֵ˰ר�÷�Ʊ��2Ϊ��ֵ˰��ͨ��Ʊ��3Ϊ��ֵ˰������ͨ��Ʊ
		String sql = "select billno from tg_guoxinfileuploadresult where nvl(dr,0) = 0 and billno = '"
				+ billid + "' and upload_status = 1 and invoice_type in(1,2,3)";
		String isUploadSuccess = (String) getIUAPQueryBS().executeQuery(sql,
				new ColumnProcessor());
		UFDouble invMny = UFDouble.ZERO_DBL;// ��Ʊ���
		UFDouble tax = UFDouble.ZERO_DBL;// ˰��
		if (StringUtil.isEmpty(isUploadSuccess)) {
			sql = "select sum(tax_money) tax_money,sum(tax_included_money) "
					+ "tax_included_money from tg_guoxinfileuploadresult "
					+ "where nvl(dr,0) = 0 and billno = '" + billid
					+ "' and upload_status = 0 and invoice_type in(1,2,3)";
			Map<Object, Object> map = (Map<Object, Object>) getIUAPQueryBS()
					.executeQuery(sql, new MapProcessor());
			if (map != null && map.size() > 0) {
				invMny = map.get("tax_included_money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(map.get("tax_included_money").toString());
				tax = map.get("tax_money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(map.get("tax_money").toString());
			}
			if (totalInvMny.compareTo(invMny) != 0) {
				throw new BusinessException(
						"���ݵ���ֵ˰��Ʊ�ܽ���롾��Ʊ��ָ��������ϴ��ķ�Ʊ��һ�£����飡");
			}
			if (totalInvTax.compareTo(tax) != 0) {
				throw new BusinessException(
						"���ݵ���ֵ˰��Ʊ��˰���롾��Ʊ��ָ��������ϴ��ķ�Ʊ˰�һ�£����飡");
			}
		} else {
			throw new BusinessException("��ֵ˰��ƱУ�鲻ͨ���������ύ���ݣ����飡");
		}
	}

}
