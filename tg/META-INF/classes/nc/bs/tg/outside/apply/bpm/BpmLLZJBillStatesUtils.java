package nc.bs.tg.outside.apply.bpm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.sf.allocateapply.IAllocateApplyQueryService;
import nc.itf.sf.fundtransferapply.IFundTransferApplyOuterService;
import nc.itf.sf.fundtransferapply.IFundTransferApplyQueryService;
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
import nc.vo.sf.allocateapply.AggAllocateApplyVO;
import nc.vo.sf.allocateapply.IAllocateApplyConst;
import nc.vo.sf.fundtransferapply.AggFundTransferApplyVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class BpmLLZJBillStatesUtils extends BillUtils implements ITGSyncService {

	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		Map<String, String> resultInfo = new HashMap<String, String>();// 返回信息
		
		String json = JSON.toJSONString(info);//map转String
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
		AbstractBill aggVO=null;
		if("36K5".equals(billtype)){
			IFundTransferApplyQueryService sv=NCLocator.getInstance().lookup(IFundTransferApplyQueryService.class);
			List<AggFundTransferApplyVO> aggvo=(List<AggFundTransferApplyVO>) sv.queryFundTransferApplyByCondtion(" vbillno='"+billno+"' and dr = 0 and vuserdef2='"+bpmid+"'");
			if (aggvo == null||aggvo.size()<1) {
				throw new BusinessException("NC系统未有对应单据"+billno+",taskid :"+bpmid);
			}
			aggVO=aggvo.get(0);
		}else{
			IAllocateApplyQueryService sv=NCLocator.getInstance().lookup(IAllocateApplyQueryService.class);
			List<AggAllocateApplyVO> aggvo=(List<AggAllocateApplyVO>) sv.queryAllocateApplyByCondtion(" vbillno='"+billno+"' and dr = 0 and vuserdef2='"+bpmid+"'" );
			if (aggvo == null||aggvo.size()<1) {
				throw new BusinessException("NC系统未有对应单据"+billno+",taskid :"+bpmid);
			}
			aggVO=aggvo.get(0);
		}
		try {
			
//			String billno = aggVO.getParentVO().getDjbh();
//			resultInfo.put("billno", billno);
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
				worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.APPROVE,
						billtype, aggVO, null);
				worknoteVO = iWorkflowMachine.checkWorkFlow(
						IPFActionName.APPROVE, billtype, aggVO, null);
				worknoteVO.setChecknote("批准");
				worknoteVO.setApproveresult("Y");
				Object obj = getPfBusiAction().processAction(IPFActionName.APPROVE, billtype,
						worknoteVO, aggVO, null, null);
				//审批通过后执行委托办理
				getPfBusiAction().processAction(IAllocateApplyConst.ActionCode_Submit+ InvocationInfoProxy.getInstance().getUserId(), billtype,worknoteVO, aggVO, null, null);

			} else if ("UNAPPROVE".equals(action)) {// 拒绝，即领导拒绝该流程，NC单据状态为待提交，可修改单据重新提交，但是重新提交也是新的流程了（BPMtaskid清空）。
				aggVO.getParentVO().setAttributeValue("vuserdef2", null);
				AggregatedValueObject returnvo=(AggregatedValueObject) getPfBusiAction().processAction("UNSAVEBILL" + maker,
						billtype, worknoteVO, aggVO, null, null);
//				
				getBaseDAO().updateVO((SuperVO) returnvo.getParentVO());

			}else if("UNSAVE".equals(action)){//退回，即退回给制单人，NC单据状态为待提交，可修改单据重新提交，但是重新提交也是老的流程了（BPMtaskid不清空）
				AggregatedValueObject returnvo=(AggregatedValueObject) getPfBusiAction().processAction("UNSAVEBILL" + maker,
						billtype, worknoteVO, aggVO, null, null);
				getBaseDAO().updateVO((SuperVO) returnvo.getParentVO());

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
}
