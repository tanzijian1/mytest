package nc.bs.tg.outside.salebpm.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.pub.pf.PfUtilTools;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.pub.doc.DefdocUtils;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.IUpdateVOService;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tb.adjbill.AdjustBillAggregatedVO;
import nc.vo.tb.adjbill.AdjustBillVO;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
//import nc.vo.tg.bpmoutside.SealFlowOutsideBodyVO;
//import nc.vo.tg.bpmoutside.SealFlowOutsideHeadVO;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.outside.BPMBillStateParaVO;
//import nc.vo.tg.sealflow.SealFlowVO;
import nc.vo.trade.pub.IBillStatus;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class SyncSaleBPMBillStatesUtils extends SaleBPMBillUtils {
	static SyncSaleBPMBillStatesUtils utils;
	private Map<String, String> acttionMap = null;
	private Map<String, Integer> billStateMap = null;
	IUpdateVOService updateService = null;
	IUAPQueryBS qry = NCLocator.getInstance().lookup(IUAPQueryBS.class);
	
	public IUpdateVOService getUpdateVOService() {
		if (updateService == null) {
			updateService = NCLocator.getInstance().lookup(
					IUpdateVOService.class);
		}
		return updateService;
	}

	public static SyncSaleBPMBillStatesUtils getUtils() {
		if (utils == null) {
			utils = new SyncSaleBPMBillStatesUtils();
		}
		return utils;
	}

	/**
	 * 支付中心付款单弃审或审批流程
	 * 
	 * @param vo
	 * @return
	 * @throws BusinessException
	 */
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
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		try {

			// SaleBPMBillUtils.addBillQueue(billqueue);// 增加队列处理
			//
			// if ("01".equals(billtypeName) ||
			// "02".equals(billtypeName)||"03".equals(billtypeName) ||
			// "04".equals(billtypeName)) {// 融资财顾费/融资费用
			// billType = "F3";
			// actionName = IPFActionName.UNSAVE.equals(billstate) ?
			// "UNSAVEBILL"
			// : billstate;
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def55 = '" + bpmid + "'");
//			if (aggVO.getParentVO().getAttributeValue("approvestatus").equals(1)||aggVO.getParentVO().getAttributeValue("approvestatus").equals(2)){
//				return "【" + billqueue + "】,"+"审批操作完成";
//			}
			// isexecute = checkBillState(aggVO, billstate);
			// pk_group = (String) aggVO.getParentVO().getAttributeValue(
			// "pk_group");
			// billvo = aggVO;
			// }

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

			// 删除bpmid，在状态为BACK时删除
			String actionName = null;
			String billType = null;
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			WorkflownoteVO worknoteVO = null;
			Object userObj = null;
			billType = "F3";
			actionName = IPFActionName.UNAPPROVE.equals(billstate) ? "UNAPPROVE"
					: billstate;
			if (IPFActionName.UNAPPROVE.equals(actionName)
					|| IPFActionName.UNSAVE.equals(actionName)) {
				if (aggVO.getParentVO().getAttributeValue("approvestatus")
						.equals(1)
						|| aggVO.getParentVO()
								.getAttributeValue("approvestatus").equals(2)) {
					actionName = IPFActionName.UNAPPROVE + getBPMUserID();
				} else {
					// 因为付款单没有unsave和unsavebill,所以当状态不是审批中和审批态即提交态时,actionName为save
					if (IPFActionName.UNSAVE.equals(actionName)
							|| IPFActionName.UNAPPROVE.equals(actionName)) {
						actionName = "SAVE";
					}
				}
				// worknoteVO = iWorkflowMachine.checkWorkFlow(actionName,
				// billType, aggVO, null);
				// 先弃审
				getPfBusiAction().processAction(actionName, billType,
						worknoteVO, aggVO, userObj, eParam);
				// 再退回
				/*
				 * getPfBusiAction().processAction( "ROLLBACK", billType, null,
				 * aggVO, userObj, null);
				 */
				String back = null;
				try {
					back = getAuditstateByCode("back");
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}

				String sql = null;
				sql = "update ap_paybill set def33 = '" + back
						+ "' , def56 = '~' where nvl(dr,0)=0 and def55='"
						+ bpmid + "'";
				getBaseDAO().executeUpdate(sql);
			} else if ("APPROVE".equals(IPFActionName.APPROVE)) {
//				aggVO.getParentVO().setAttributeValue(
//						"def33",
//						DefdocUtils.getUtils()
//								.getDefdocByCode("zdy032", "finapprove")
//								.get("pk_defdoc"));
//				getPfBusiAction().processAction(IPFActionName.SAVE, billType,
//						null, aggVO, userObj, null);
				// getBaseDAO().executeUpdate("update ap_paybill set def33 = '"+DefdocUtils.getUtils()
				// .getDefdocByCode("zdy032", "finapprove")
				// .get("pk_defdoc")+"' where pk_paybill = '"+aggVO.getPrimaryKey()+"' ");
				// AggregatedValueObject newVO = getBillVO(AggPayBillVO.class,
				// "isnull(dr,0)=0 and def55 = '" + bpmid + "'");
				// worknoteVO = iWorkflowMachine.checkWorkFlow(actionName,
				// billType, newVO, null);
				// if (worknoteVO != null) {
				// worknoteVO.setChecknote("批准");
				// worknoteVO.setApproveresult("Y");
				// }
				// getPfBusiAction().processAction(actionName+getBPMUserID(),
				// billType,
				// worknoteVO, newVO, userObj, eParam);
				String sql = null;
				sql =
						"update ap_paybill set def33 = '"+DefdocUtils.getUtils().getDefdocByCode("zdy032",
								"finapprove").get("pk_defdoc")+"'  where nvl(dr,0)=0 and def55='"
								+ bpmid + "'";
				BaseDAO dao = new BaseDAO();
				dao.executeUpdate(sql);
				PfUtilTools.approveSilently(billType, aggVO.getPrimaryKey(),
						"Y", "批准", getBPMUserID(), null, actionName);

			}
			// 如果bpm传的状态是unsave则请掉bpmid
			if (IPFActionName.UNSAVE.equals(billstate)) {
				String rejected = null;
				try {
					rejected = getAuditstateByCode("rejected");
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
				String sql = null;
				sql = "update ap_paybill set def33 = '"
						+ rejected
						+ "' , def55 = '~' , def56 = '~' where nvl(dr,0)=0 and def55='"
						+ bpmid + "'";
				BaseDAO dao = new BaseDAO();
				dao.executeUpdate(sql);
			}

			// if (checkBillState(aggVO,billstate)) {
			// String sql = null;
			// sql =
			// "update ap_paybill set def33 = '"+billstate+"' where nvl(dr,0)=0 and def1='"
			// + bpmid + "'";
			// BaseDAO dao = new BaseDAO();
			// dao.executeUpdate(sql);
			// }
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
	 * 融资改造弃审或审批流程
	 * 
	 * @param vo
	 * @param json
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncFinBillState(BPMBillStateParaVO vo, String json)
			throws Exception {
		String billtypeName = vo.getBilltypeName();
		String billstate = vo.getBillstate();
		String bpmid = vo.getBpmid();
		String operator = vo.getOperator();
		String deletestate = null;
		JSONObject jobj = JSON.parseObject(json);
		valid(jobj);
		InvocationInfoProxy.getInstance().getUserCode();
		if ("UNSAVE".equals(billstate)) {
			deletestate = "UNSAVE";
		}
		// if(IPFActionName.UNAPPROVE.equals(billstate)){
		// billstate = IPFActionName.UNSAVE;
		// }

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

			String actionName = null;
			String billType = null;
			WorkflownoteVO worknoteVO = null;
			AggregatedValueObject billvo = null;
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			Object userObj = null;
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String pk_group = null;
			boolean isexecute = true;
			BPMBillUtils.addBillQueue(billqueue);// 增加队列处理

			if (ISaleBPMBillCont.BILL_17.equals(billtypeName)
					|| ISaleBPMBillCont.BILL_18.equals(billtypeName)) {// 融资财顾费/融资费用
				billType = "RZ06";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;

				AggFinancexpenseVO aggVO = (AggFinancexpenseVO) getBillVO(
						AggFinancexpenseVO.class,
						"isnull(dr,0)=0 and def19 = '" + bpmid + "'");
				if (aggVO.getParentVO().getAttributeValue("approvestatus").equals(1)||aggVO.getParentVO().getAttributeValue("approvestatus").equals(2)){
					return "【" + billqueue + "】,"+"审批操作完成";
				}
				String maker = (String) aggVO.getParentVO().getAttributeValue(
						"creator");
				if (IPFActionName.UNAPPROVE.equals(billstate)) {
					if (aggVO.getParentVO().getAttributeValue("approvestatus")
							.equals(1)
							|| aggVO.getParentVO()
									.getAttributeValue("approvestatus")
									.equals(2)) {
						String approve = (String) aggVO.getParentVO()
								.getAttributeValue("approver");
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				} else if ("UNSAVE".equals(billstate)) {
					if (aggVO.getParentVO().getAttributeValue("approvestatus")
							.equals(1)
							|| aggVO.getParentVO()
									.getAttributeValue("approvestatus")
									.equals(2)) {
						String approve = (String) aggVO.getParentVO()
								.getAttributeValue("approver");
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = (String) aggVO.getParentVO().getAttributeValue(
						"pk_group");
				billvo = aggVO;

			} else if (ISaleBPMBillCont.BILL_15.equals(billtypeName)
					|| ISaleBPMBillCont.BILL_16.equals(billtypeName)) {// 还本/还息

				billType = "36FF";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;

				AggRePayReceiptBankCreditVO aggVO = (AggRePayReceiptBankCreditVO) getBillVO(
						AggRePayReceiptBankCreditVO.class,
						"isnull(dr,0)=0 and vdef19 = '" + bpmid + "'");
//				if (aggVO.getParentVO().getAttributeValue("vbillstatus").equals(1)||aggVO.getParentVO().getAttributeValue("vbillstatus").equals(2)){
//					return "【" + billqueue + "】,"+"审批操作完成";
//				}
				String maker = (String) aggVO.getParentVO().getAttributeValue(
						"creator");
				if (IPFActionName.UNAPPROVE.equals(billstate)) {
					if (aggVO.getParentVO().getAttributeValue("vbillstatus")
							.equals(1)
							|| aggVO.getParentVO()
									.getAttributeValue("vbillstatus").equals(2)) {
						String approve = (String) aggVO.getParentVO()
								.getAttributeValue("approver");
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				} else if ("UNSAVE".equals(billstate)) {
					if (aggVO.getParentVO().getAttributeValue("vbillstatus")
							.equals(1)
							|| aggVO.getParentVO()
									.getAttributeValue("vbillstatus").equals(2)) {
						String approve = (String) aggVO.getParentVO()
								.getAttributeValue("approver");
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = (String) aggVO.getParentVO().getAttributeValue(
						"pk_group");
				billvo = aggVO;

			} else if (ISaleBPMBillCont.BILL_19.equals(billtypeName)) {// 补票单

				billType = "RZ30";
				actionName = IPFActionName.UNSAVE.equals(billstate) ? "UNSAVEBILL"
						: billstate;

				AggAddTicket aggVO = (AggAddTicket) getBillVO(
						AggAddTicket.class, "isnull(dr,0)=0 and def19 = '"
								+ bpmid + "'");
				if (aggVO.getParentVO().getAttributeValue("approvestatus").equals(1)||aggVO.getParentVO().getAttributeValue("approvestatus").equals(2)){
					return "【" + billqueue + "】,"+"审批操作完成";
				}
				String maker = (String) aggVO.getParentVO().getAttributeValue(
						"creator");
				if (IPFActionName.UNAPPROVE.equals(billstate)) {
					if (aggVO.getParentVO().getApprovestatus().equals(1)
							|| aggVO.getParentVO().getApprovestatus().equals(2)) {
						String approve = aggVO.getParentVO().getApprover();
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				} else if ("UNSAVE".equals(billstate)) {
					if (aggVO.getParentVO().getAttributeValue("approvestatus")
							.equals(1)
							|| aggVO.getParentVO()
									.getAttributeValue("approvestatus")
									.equals(2)) {
						String approve = (String) aggVO.getParentVO()
								.getAttributeValue("approver");
						actionName = IPFActionName.UNAPPROVE + approve;
					} else {
						actionName = "UNSAVEBILL" + maker;
					}
				}
				isexecute = checkBillState(aggVO, billstate);
				pk_group = (String) aggVO.getParentVO().getAttributeValue(
						"pk_group");
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

			InvocationInfoProxy.getInstance().setGroupId(pk_group);
			if (!isexecute) {
				if (IPFActionName.APPROVE.equals(actionName)) {
					worknoteVO = iWorkflowMachine.checkWorkFlow(actionName,
							billType, billvo, null);
					if (worknoteVO != null) {
						worknoteVO.setChecknote("批准");
						worknoteVO.setApproveresult("Y");
					}
					Map<String, String> map = (Map<String, String>) jobj
							.get("data");
					if ("36FF".equals(billType)) {// 还款单
						billvo.getParentVO().setStatus(VOStatus.UPDATED);
						billvo.getParentVO().setAttributeValue(
								"def32",
								getdefdocBycode(map.get("billpriority"),
										"zdy031"));// 紧急程度
						if (map.get("balatype") != null) {
							billvo.getParentVO().setAttributeValue("def38",
									getbd_balatypeBycode(map.get("balatype")));// 结算方式
						} else {
							billvo.getParentVO().setAttributeValue("def38",
									null);// 结算方式
						}
						billvo.getParentVO().setAttributeValue("big_text_a",
								map.get("remark"));// 审单会计备注
						billvo.getParentVO().setAttributeValue("def27",
								map.get("isaddannex"));// 是否补附件
						billvo.getParentVO().setAttributeValue("def28",
								map.get("iscompletion"));// 是否补全附件
						// billvo =
						// getUpdateVOService().UpdateVO(AggRePayReceiptBankCreditVO.class,billvo);
						//
						// billvo = getBillVO(
						// AggRePayReceiptBankCreditVO.class,
						// "isnull(dr,0)=0 and vdef19 = '" + bpmid + "'");
					} else if ("RZ06".equals(billType)) {// 财顾费/融资费
						billvo.getParentVO().setStatus(VOStatus.UPDATED);
						billvo.getParentVO().setAttributeValue(
								"def3",
								getdefdocBycode(map.get("billpriority"),
										"zdy031"));// 紧急程度
						if (map.get("balatype") != null) {
							billvo.getParentVO().setAttributeValue("def35",
									getbd_balatypeBycode(map.get("balatype")));// 结算方式
						} else {
							billvo.getParentVO().setAttributeValue("def35",
									null);// 结算方式
						}
						billvo.getParentVO().setAttributeValue("big_text_a",
								map.get("remark"));// 审单会计备注
						billvo.getParentVO().setAttributeValue("def37",
								map.get("isaddannex"));// 是否补附件
						billvo.getParentVO().setAttributeValue("def38",
								map.get("iscompletion"));// 是否补全附件
						/**
						 * 先把消息优先级等数据更新到数据库再查下出来2020-04-14-谈子健-start
						 */
						IMDPersistenceService md = NCLocator.getInstance()
								.lookup(IMDPersistenceService.class);
						md.updateBillWithAttrs(new Object[] { billvo },
								new String[] { "def3", "def35", "big_text_a",
										"def37", "def38" });
						billvo = getBillVO(AggFinancexpenseVO.class,
								"isnull(dr,0)=0 and def19 = '" + bpmid + "'");
						/**
						 * 先把消息优先级等数据更新到数据库再查下出来2020-04-14-谈子健-end
						 */
					} else if ("RZ30".equals(billType)) {// 补票单
						billvo.getParentVO().setStatus(VOStatus.UPDATED);
						billvo.getParentVO().setAttributeValue("def31",
								map.get("iscompletion"));// 已补全
						billvo.getParentVO().setAttributeValue("def32",
								map.get("isaddannex"));// 是否补附件
						billvo.getParentVO().setAttributeValue("big_text_b",
								map.get("remark"));// 审单会计备注
					}
				}

				if (IPFActionName.APPROVE.equals(actionName)) {
					getPfBusiAction().processAction(actionName, billType,
							worknoteVO, billvo, userObj, eParam);
				} else {
					if (IPFActionName.UNAPPROVE.equals(billstate)) {// BPM操作为UNAPPROVE不删影像编码
						List<String> list = Arrays.asList(new String[] {
								"36FF", "RZ30", "RZ06" });
						if (list.contains(billType)) {
							billvo.getParentVO()
									.setAttributeValue("def47", "Y");
						}
						List<String> listApply = Arrays
								.asList(new String[] { "36FA" });
						if (listApply.contains(billType)) {
							billvo.getParentVO().setAttributeValue("vdef10",
									"Y");
						}
						List<String> listMort = Arrays
								.asList(new String[] { "RZ04" });
						if (listMort.contains(billType)) {
							billvo.getParentVO()
									.setAttributeValue("def10", "Y");
						}
					}
					getPfBusiAction().processAction(actionName, billType,
							worknoteVO, billvo, userObj, eParam);
				}

			}
			if (IPFActionName.UNAPPROVE.equals(billstate)
					|| "UNSAVE".equals(billstate)) {
				String sql = null;
				if ("RZ06".equals(billType)) {
					sql = "update tgrz_financexpense set def3 = '~', def35 = '~' , big_text_a = '', def37 = '~', def38 = '~' where nvl(dr,0)=0 and def19='"
							+ bpmid + "'";
				} else if ("36FF".equals(billType)) {
					sql = "update cdm_repayrcpt set def32 = '~', def34 = '~', big_text_a = '', def27 = '~', def28 = '~' where nvl(dr,0)=0 and vdef19='"
							+ bpmid + "'";
				} else if ("RZ30".equals(billType)) {
					sql = "update tg_addTicket set def31 = '~'，def32 = '~', big_text_b = '' where nvl(dr,0)=0 and def19='"
							+ bpmid + "'";
				}
				BaseDAO dao = new BaseDAO();
				dao.executeUpdate(sql);
			}

			// 删除bpmid，在状态为BACK时删除
			if ("UNSAVE".equals(deletestate)) {

				String sql = null;
				if ("RZ06".equals(billType)) {
					sql = "update tgrz_financexpense set def19 = '~',def20 = '~' where nvl(dr,0)=0 and def19='"
							+ bpmid + "'";
				} else if ("36FF".equals(billType)) {
					sql = "update cdm_repayrcpt set vdef19 = '~',vdef20 = '~'  where nvl(dr,0)=0 and vdef19='"
							+ bpmid + "'";
				} else if ("RZ30".equals(billType)) {
					sql = "update tg_addTicket set def19 = '~',def20 = '~' where nvl(dr,0)=0 and def19='"
							+ bpmid + "'";
				}
				BaseDAO dao = new BaseDAO();
				dao.executeUpdate(sql);
			}
		} catch (Exception e) {
			throw new BusinessException(
					"【" + billqueue + "】," + e.getMessage(), e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}

		return "【" + billqueue + "】," + getActtionMap().get(billstate)
				+ "操作完成!";
	}
	
	/**
	 * 资本市场还款审批，退回
	 */
	public String onSyncMarketBillState(BPMBillStateParaVO vo) throws Exception {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		String billtypeName = vo.getBilltypeName();
		String actionName = vo.getBillstate();
		String action = vo.getBillstate();
		String bpmid = vo.getBpmid();
		Map<String, String> dataMap = JSON.parseObject(vo.getData(), HashMap.class);
		
		String billqueue = ISaleBPMBillCont.getBillNameMap().get(billtypeName)
				+ ":" + bpmid;
		if (!getActtionMap().containsKey(actionName)) {
			throw new BusinessException("【" + billqueue + "】,不存在" + actionName
					+ "处理操作,请联系系统管理员!");
		}
		AggMarketRepalayVO aggvo = (AggMarketRepalayVO)getBillVO(
				AggMarketRepalayVO.class, "isnull(dr,0)=0 and def20 = '"+ bpmid + "'");
//		if (aggvo.getParentVO().getAttributeValue("approvestatus").equals(1)||aggvo.getParentVO().getAttributeValue("approvestatus").equals(2)){
//			return "【" + billqueue + "】,"+"操作完成";
//		}
		boolean isexecute = false;//判断单据状态是否与当前单据动作一致
		if(getBillStateMap().get(actionName).intValue() == aggvo.getParentVO()
				.getApprovestatus().intValue()){
			isexecute = true;
		}
		if(!isexecute){
			WorkflownoteVO worknoteVO = null;
			if (IPFActionName.APPROVE.equals(actionName)) {
				IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(IWorkflowMachine.class);
				worknoteVO = iWorkflowMachine.checkWorkFlow(actionName,"SD08", aggvo, null);
				if(worknoteVO != null){
					worknoteVO.setChecknote("批准");
					worknoteVO.setApproveresult("Y");
				}
				aggvo.getParentVO().setStatus(VOStatus.UPDATED);
				aggvo.getParentVO().setDef38(getbd_balatypeBycode(dataMap.get("balatype")));
				aggvo.getParentVO().setDef37(dataMap.get("iscompletion"));
				aggvo.getParentVO().setDef36(dataMap.get("isaddannex"));
				aggvo.getParentVO().setBig_text_a(dataMap.get("remark"));
				aggvo.getParentVO().setDef32(getdefdocBycode(dataMap.get("billpriority"),"zdy031"));
			}else if(IPFActionName.UNAPPROVE.equals(actionName)){
				if(aggvo.getParentVO().getApprovestatus()==1 
						|| aggvo.getParentVO().getApprovestatus()==2){
					actionName = "UNSAVEBILL"+aggvo.getParentVO().getApprover();
				}else{
					actionName = "UNSAVEBILL"+aggvo.getParentVO().getCreator();
				}
				aggvo.getParentVO().setDef47("Y");
			}else if(IPFActionName.UNSAVE.equals(actionName)){
				if(aggvo.getParentVO().getApprovestatus()==1 
						|| aggvo.getParentVO().getApprovestatus()==2){
					actionName = "UNSAVEBILL"+aggvo.getParentVO().getApprover();
				}else{
					actionName = "UNSAVEBILL"+aggvo.getParentVO().getCreator();
				}
				aggvo.getParentVO().setDef47("N");
			}
			getPfBusiAction().processAction(actionName, "SD08", worknoteVO, aggvo,null, null);
		}
		
		String sql = null;
		if(IPFActionName.UNAPPROVE.equals(actionName)){
			sql = "update sdfn_marketrepalay set def32 = '~',set def36 = '~',set def37 = '~',set def38 = '~',"
					+ " set big_text_a='~' where nvl(dr,0)=0 and def20='"+ bpmid + "'";
		}else if(IPFActionName.UNSAVE.equals(actionName)){
			sql = "update sdfn_marketrepalay set def20 = '~', def41 = '~' , def47 = '~', def21 = '~'  where nvl(dr,0)=0 and def20='"
					+ bpmid + "'";
		}
		if(sql != null){
			BaseDAO dao = new BaseDAO();
			dao.executeUpdate(sql);
		}
		return "【" + billqueue + "】," + getActtionMap().get(action)+ "操作完成!";
	}


	/** 获取文档系统的访问安全码 */
	private String getKey() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = new Date();
			String strDate = sdf.format(date);
			String key = "NC";
			String app_key_ticket = "DF6AF297";
			String com = key + app_key_ticket + strDate;
			return MD5(com);
		} catch (Exception e) {
			return "";
		}
	}

	/** MD5加密 */
	private String MD5(String psw) {
		{
			try {
				MessageDigest md5 = MessageDigest.getInstance("MD5");
				md5.update(psw.getBytes("UTF-8"));
				byte[] encryption = md5.digest();

				StringBuffer strBuf = new StringBuffer();
				for (int i = 0; i < encryption.length; i++) {
					if (Integer.toHexString(0xff & encryption[i]).length() == 1) {
						strBuf.append("0").append(
								Integer.toHexString(0xff & encryption[i]));
					} else {
						strBuf.append(Integer.toHexString(0xff & encryption[i]));
					}
				}

				return strBuf.toString().toUpperCase();
			} catch (NoSuchAlgorithmException e) {
				return "";
			} catch (UnsupportedEncodingException e) {
				return "";
			}
		}
	}


	private void checknullbt(JSONObject object) throws BusinessException {
		if (object.getString("name") == null || object.getString("") == "") {
			throw new BusinessException("传入信息name不能为空");
		}
		if (object.getString("proposer") == null || object.getString("") == "") {
			throw new BusinessException("传入信息proposer不能为空");
		}
		if (object.getString("applicationtime") == null
				|| object.getString("") == "") {
			throw new BusinessException("传入信息applicationtime不能为空");
		}
		if (object.getString("applicationcompany") == null
				|| object.getString("") == "") {
			throw new BusinessException("传入信息applicationcompany不能为空");
		}
		if (object.getString("applicationdepartment") == null
				|| object.getString("") == "") {
			throw new BusinessException("传入信息applicationdepartment不能为空");
		}
		if (object.getString("sealer") == null || object.getString("") == "") {
			throw new BusinessException("传入信息sealer不能为空");
		}
		if (object.getString("sealreason") == null
				|| object.getString("") == "") {
			throw new BusinessException("传入信息sealreason不能为空");
		}
//		if (object.getString("sealname") == null || object.getString("") == "") {
//			throw new BusinessException("传入信息sealname不能为空");
//		}
//		if (object.getString("sealID") == null || object.getString("") == "") {
//			throw new BusinessException("传入信息sealID不能为空");
//		}

	}

	private void checknull(JSONObject jobj) throws BusinessException {
		if (jobj.getString("bpmid") == null || jobj.getString("") == "") {
			throw new BusinessException("BPM业务单据主键不能为空");
		}
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "") {
			throw new BusinessException("目标单据不能为空");
		}
		if (jobj.getString("data") == null || jobj.getString("") == "") {
			throw new BusinessException("传入信息DATA不能为空");
		}
	}

	private void checknulltt(JSONObject jobj) throws BusinessException {
		if (jobj.getString("sealfile") == null || jobj.getString("") == "") {
			throw new BusinessException("传入信息sealfile不能为空");
		}
		if (jobj.getString("primarydirectory") == null
				|| jobj.getString("") == "") {
			throw new BusinessException("传入信息primarydirectory不能为空");
		}
		if (jobj.getString("secondarydirectory") == null
				|| jobj.getString("") == "") {
			throw new BusinessException("传入信息secondarydirectory不能为空");
		}
		if (jobj.getString("threedirectories") == null
				|| jobj.getString("") == "") {
			throw new BusinessException("传入信息threedirectories不能为空");
		}
		if (jobj.getString("companyname") == null || jobj.getString("") == "") {
			throw new BusinessException("传入信息companyname不能为空");
		}
		if (jobj.getString("number") == null || jobj.getString("") == "") {
			throw new BusinessException("传入信息number 不能为空");
		}

	}

	public void bpmExist(IUAPQueryBS qry, String billqueue)
			throws BusinessException {
		if (billqueue != null) {
			String sqlid = "select count(1)  from sdfn_sealflow where sdfn_sealflow.def60 = '"
					+ billqueue + "'";
			Integer bpmid = (Integer) qry.executeQuery(sqlid,
					new ColumnProcessor());
			if (bpmid > 0) {
				throw new BusinessException("您好，PBM单据已存在，主键号是" + billqueue);
			}
		}
	}

	/**
	 * 预算调整弃审或审批流程
	 * 
	 * @param vo
	 * @param json
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncAdjustBillState(BPMBillStateParaVO vo, String json)
			throws Exception {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode(BPMOperatorName);
		String billtypeName = vo.getBilltypeName();
		String action = vo.getBillstate();
		String actionName = vo.getBillstate();
		String bpmid = vo.getBpmid();
		
		String billqueue = ISaleBPMBillCont.getBillNameMap().get(billtypeName)
				+ ":" + bpmid;
		if (!getActtionMap().containsKey(action)) {
			throw new BusinessException("【" + billqueue + "】,不存在" + action
					+ "处理操作,请联系系统管理员!");
		}
		AdjustBillAggregatedVO aggVO = (AdjustBillAggregatedVO) getBillVO(
				AdjustBillAggregatedVO.class, "isnull(dr,0)=0 and def2 = '"
						+ bpmid + "'");
		if (aggVO == null)
			throw new BusinessException("NC系统bpm主键未有对应单据");
		
		if (aggVO.getParentVO().getAttributeValue("vbillstatus").equals(1)||aggVO.getParentVO().getAttributeValue("vbillstatus").equals(2)){
			return "【" + billqueue + "】,"+"审批操作完成";
		}
		String billtype=null;
		if(ISaleBPMBillCont.BILL_22.equals(billtypeName)){
			billtype = "TBWT";
		}else if(ISaleBPMBillCont.BILL_23.equals(billtypeName)){
			billtype = "TBWW";
		}
		
		AdjustBillVO parentVO = (AdjustBillVO)aggVO.getParentVO();
		if(parentVO.getVbillstatus().intValue() != getBillStateMap().get(action).intValue()){
			//单据状态与操作不一致时，进行操作
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(IWorkflowMachine.class);
			if (IPFActionName.APPROVE.equals(action)) {
				WorkflownoteVO worknoteVO = iWorkflowMachine.checkWorkFlow(action,billtype, aggVO, null);
				if(worknoteVO != null){
					worknoteVO.setChecknote("批准");
					worknoteVO.setApproveresult("Y");
				}
				getPfBusiAction().processAction(action, billtype, worknoteVO, aggVO,null, null);
			} else if (IPFActionName.UNAPPROVE.equals(action)) {
				if(parentVO.getVbillstatus()==1 || parentVO.getVbillstatus()==2){
					action = "UNSAVE"+parentVO.getApprovedby();
				}else{
					action = "UNSAVE"+parentVO.getCreator();
				}
				aggVO.getParentVO().setAttributeValue("def1", "Y");
				getPfBusiAction().processAction(action, billtype, null,aggVO, null, null);
			} else if (IPFActionName.UNSAVE.equals(action)) {
				if(parentVO.getVbillstatus()==1 || parentVO.getVbillstatus()==2){
					action = "UNSAVE"+parentVO.getApprovedby();
				}else{
					action = "UNSAVE"+parentVO.getCreator();
				}
				aggVO.getParentVO().setAttributeValue("def1", "N");
				getPfBusiAction().processAction(action, billtype,null, aggVO, null, null);
			}
		}
		//单据状态与当前操作一致时，不进行操作，但是数据库的字段要做更改
		String sql = null;
		if(IPFActionName.UNSAVE.equals(action)){
			sql = "update tb_adjustbill set def1 = '~', def2 = '~' , def3 = '~'  where nvl(dr,0)=0 and def2='"
					+ bpmid + "'";
		}
		if(sql != null){
			BaseDAO dao = new BaseDAO();
			dao.executeUpdate(sql);
		}
		
		
		return "【" + billqueue + "】," + getActtionMap().get(actionName)+ "操作完成!";
	}

	/**
	 * 表体空值校验
	 * 
	 * @param bodyjson
	 */

	/**
	 * 付款单检验业务单据是否已为当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggPayBillVO aggVO, String billstate) {
		if (getBillStateMap().get(billstate).toString() == ((String) aggVO
				.getParentVO().getAttributeValue("def33"))) {
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
				whereCondStr, true, false);
		if (coll == null || coll.size() == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (AggregatedValueObject) coll.toArray()[0];
	}

	/**
	 * 根据code 获取地区财务审批状态 2019-3-20-黄小剑
	 */
	public String getAuditstateByCode(String code) throws BusinessException {
		String result = null;
		String sql = "select pk_defdoc from bd_defdoc where nvl(dr,0)=0 and enablestate='2' and code ='"
				+ code + "'";

		BaseDAO baseDao = new BaseDAO();
		try {
			result = (String) baseDao.executeQuery(sql, new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
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

	// AggAddTicket
	/**
	 * 检验补票单是否当前状态
	 * 
	 * @param aggVO
	 * @param billstate
	 * @return
	 */
	private boolean checkBillState(AggAddTicket aggVO, String billstate) {
		if (getBillStateMap().get(billstate).intValue() == aggVO.getParentVO()
				.getApprovestatus().intValue()) {
			return true;
		}
		return false;
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("操作状态不能为空");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPM业务单据主键不能为空");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("目标单据不能为空");
		if ("APPROVE".equals(jobj.getString("billstate"))) {
			if (jobj.getString("data") == null || jobj.getString("") == "")
				throw new BusinessException("传入信息DATA不能为空");
		}
	}

	/**
	 * 
	 * 
	 * @param 自定义档案列表code
	 * @param docCode
	 *            自定义档案code
	 * @return
	 */
	public String getdefdocBycode(String code, String docCode) {
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist c, bd_defdoc d  ");
		query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
		query.append("   and c.code = '" + docCode + "'  ");
		query.append("   and d.code = '" + code + "'  ");
		query.append("   and nvl(c.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and d.enablestate = '2'  ");

		// String sql =
		// "select d.pk_defdoc from bd_defdoclist c,bd_defdoc d where c.pk_defdoclist=d.pk_defdoclist and  c.code='"
		// + docCode + "' and d.code='" + code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

}
