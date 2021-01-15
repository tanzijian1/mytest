package nc.bs.tg.outside.fj.bpm;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.cmp.bill.ICmpPayBillQueryService;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.data.access.NCObject;
import nc.vo.ep.bx.BXVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.wfengine.definition.WorkflowTypeEnum;
import nc.vo.wfengine.pub.WfTaskType;
import uap.iweb.wf.vo.WorkFlowQueryUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BpmLLFJBillStatesUtils extends BillUtils implements ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		Map<String, String> resultInfo = new HashMap<String, String>();// 返回信息

		String json = JSON.toJSONString(info);// map转String
		JSONObject jsonhead = JSON.parseObject(json);
		valid(jsonhead);
		String operator = jsonhead.getString("operator");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode("LLBPM");
		String bpmid = jsonhead.getString("bpmid");
		String action = jsonhead.getString("billstate");// 必录;?
		// 枚举：APPROVE:审核;UNAPPROVE:弃审;UNSAVE:撤回
		String billtype = jsonhead.getString("billtypeName");// 必录;?
		String billno = jsonhead.getString("billno");// 必录;?
		AbstractBill aggVO = null;
		ICmpPayBillQueryService sv = NCLocator.getInstance().lookup(
				ICmpPayBillQueryService.class);
		AbstractBill[] aggvo=(AbstractBill[]) sv.queryBillByCondition(" bill_no='"+billno+"' and dr = 0 and def30='"+bpmid+"'");
//		AbstractBill[] aggvo=(AbstractBill[]) sv.queryBillByCondition(" bill_no='"+billno+"' and dr = 0 ");
		if (aggvo == null||aggvo.length<1) {
			throw new BusinessException("NC系统未有对应单据" + billno + ",taskid :"
					+ bpmid);
		}
		aggVO = aggvo[0];
		try {

			// String billno = aggVO.getParentVO().getDjbh();
			// resultInfo.put("billno", billno);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			HashMap paramMap = new HashMap();
			paramMap.put("flowdefpk", aggVO.getParentVO().getPrimaryKey());
			WorkflownoteVO worknoteVO = null;
			nc.vo.pubapp.pflow.PfUserObject userobjec = new nc.vo.pubapp.pflow.PfUserObject();
			userobjec.setBusinessCheckMap(new HashMap());
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			String maker = (String) aggVO.getParentVO().getAttributeValue(
					"creator");
			if ("APPROVE".equals(action)) {
				worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, billtype, aggVO, null);
				worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, billtype, aggVO, null);
				worknoteVO.setChecknote("批准");
				worknoteVO.setApproveresult("Y");
				getPfBusiAction().processAction(IPFActionName.APPROVE,
						billtype, worknoteVO, aggVO, null, null);

			} else if ("UNAPPROVE".equals(action)) {// 拒绝，即领导拒绝该流程，NC单据状态为待提交，可修改单据重新提交，但是重新提交也是新的流程了（BPMtaskid清空）。
				WorkflownoteVO workflownote =queryWorkFlowNoteVO(queryWorkFlowNote(aggVO.getParentVO().getPrimaryKey()));
				workflownote = checkWorkFlow(InvocationInfoProxy.getInstance().getUserId(),
						workflownote, aggVO);
				workflownote.setApproveresult("R");
				workflownote.setChecknote("BPM领导拒绝");
				workflownote.getTaskInfo().getTask()
						.setTaskType(WfTaskType.Backward.getIntValue());
				workflownote.getTaskInfo().getTask()
						.setSubmit2RjectTache(false);
				workflownote.getTaskInfo().getTask()
						.setBackToFirstActivity(true);
				
				
				 getPfBusiAction()
						.processAction("APPROVE" + maker, billtype,
								workflownote, aggVO, null, null);
				//
				 
				 aggvo=(AbstractBill[]) sv.queryBillByCondition(" bill_no='"+billno+"' and dr = 0  ");
				 aggvo[0].getParentVO().setAttributeValue("def30", null);
				 aggvo[0].getParentVO().setAttributeValue("def1", null);

				getBaseDAO().updateVO((SuperVO) aggvo[0].getParentVO());

			} else if ("UNSAVE".equals(action)) {// 退回，即退回给制单人，NC单据状态为待提交，可修改单据重新提交，但是重新提交也是老的流程了（BPMtaskid不清空）
				WorkflownoteVO workflownote =queryWorkFlowNoteVO(queryWorkFlowNote(aggVO.getParentVO().getPrimaryKey()));
				workflownote = checkWorkFlow(InvocationInfoProxy.getInstance().getUserId(),
						workflownote, aggVO);
				workflownote.setApproveresult("R");
				workflownote.setChecknote("BPM退回");
				workflownote.getTaskInfo().getTask()
						.setTaskType(WfTaskType.Backward.getIntValue());
				workflownote.getTaskInfo().getTask()
						.setSubmit2RjectTache(false);
				workflownote.getTaskInfo().getTask()
				.setBackToFirstActivity(true);
				
				 getPfBusiAction()
						.processAction("APPROVE" + maker, billtype,
								workflownote, aggVO, null, null);
				 aggvo=(AbstractBill[]) sv.queryBillByCondition(" bill_no='"+billno+"' and dr = 0  ");
				 aggvo[0].getParentVO().setAttributeValue("def1", null);

				getBaseDAO().updateVO((SuperVO) aggvo[0].getParentVO());

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		resultInfo.put("BPMID", bpmid);
		resultInfo.put("msg", "【" + action + "】单据操作完成");
		return JSON.toJSONString(resultInfo);
	}

	public BXVO getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		NCObject[] nobjs = getMDQryService().queryBillOfNCObjectByCond(c,
				whereCondStr, false);

		if (nobjs == null || nobjs.length == 0) {
			throw new BusinessException("NC系统未能关联信息!");
		}
		return (BXVO) nobjs[0].getContainmentObject();//
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("操作状态不能为空");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPM业务单据主键不能为空");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("目标单据不能为空");
	}

	/**
	 * 读取BPM操作员默认用户
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getBPMUserID() throws BusinessException {
		String bpmUserid = null;
		if (bpmUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = 'LLBPM'";
			bpmUserid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return bpmUserid;
	}
	
	public static WorkflownoteVO queryWorkFlowNoteVO(String pk_workflownote)
			throws DAOException, BusinessException {
		WorkflownoteVO note = (WorkflownoteVO) new BaseDAO().retrieveByPK(
				WorkflownoteVO.class, pk_workflownote);
		if (note == null) {
			throw new BusinessException("该待办工作项已经失效！");
		}
		return note;
	}
	
	private String queryWorkFlowNote(String billid) throws BusinessException {
		String sql = "select pk_checkflow  from pub_workflownote where billid = '"
				+ billid + "' and approveresult  is  null  order by ts desc ";
		return (String) new BaseDAO().executeQuery(sql, new ColumnProcessor());
	}
	
	
	/**
	 * 检查当前用户的审批待办是否正确
	 * 
	 * @param userid
	 * @param workflownote
	 * @param billvo
	 * @return
	 * @throws BusinessException
	 */
	public static WorkflownoteVO checkWorkFlow(String userid,
			WorkflownoteVO workflownote, AggregatedValueObject billvo)
			throws BusinessException {
		try {
			IWorkflowMachine service = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			String actionName = "APPROVE";
			if ((workflownote.getWorkflow_type() == WorkflowTypeEnum.Workflow
					.getIntValue())
					|| (workflownote.getWorkflow_type() == WorkflowTypeEnum.SubWorkflow
							.getIntValue())
					|| (workflownote.getWorkflow_type() == WorkflowTypeEnum.SubWorkApproveflow
							.getIntValue())) {
				actionName = "SIGNAL";
			}
			HashMap<String, Object> mapVO = new HashMap<String, Object>();
			return service.checkWorkFlow(actionName + userid,
					workflownote.getPk_billtype(), billvo, mapVO);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		} finally {

		}

	}
}
