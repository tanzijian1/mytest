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
		Map<String, String> resultInfo = new HashMap<String, String>();// ������Ϣ
		
		String json = JSON.toJSONString(info);//mapתString
		JSONObject jsonhead = JSON.parseObject(json);
		valid(jsonhead);
		String operator = jsonhead.getString("operator");
		InvocationInfoProxy.getInstance().setUserId(getBPMUserID());
		InvocationInfoProxy.getInstance().setUserCode("LLBPM");
		String bpmid = jsonhead.getString("bpmid");
		String action = jsonhead.getString("billstate");// ��¼;?
		// ö�٣�APPROVE:���;UNAPPROVE:����;UNSAVE:����
		String billtype = jsonhead.getString("billtypeName");// ��¼;?
		String billno = jsonhead.getString("billno");// ��¼;?
		AbstractBill aggVO=null;
		if("36K5".equals(billtype)){
			IFundTransferApplyQueryService sv=NCLocator.getInstance().lookup(IFundTransferApplyQueryService.class);
			List<AggFundTransferApplyVO> aggvo=(List<AggFundTransferApplyVO>) sv.queryFundTransferApplyByCondtion(" vbillno='"+billno+"' and dr = 0 and vuserdef2='"+bpmid+"'");
			if (aggvo == null||aggvo.size()<1) {
				throw new BusinessException("NCϵͳδ�ж�Ӧ����"+billno+",taskid :"+bpmid);
			}
			aggVO=aggvo.get(0);
		}else{
			IAllocateApplyQueryService sv=NCLocator.getInstance().lookup(IAllocateApplyQueryService.class);
			List<AggAllocateApplyVO> aggvo=(List<AggAllocateApplyVO>) sv.queryAllocateApplyByCondtion(" vbillno='"+billno+"' and dr = 0 and vuserdef2='"+bpmid+"'" );
			if (aggvo == null||aggvo.size()<1) {
				throw new BusinessException("NCϵͳδ�ж�Ӧ����"+billno+",taskid :"+bpmid);
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
				worknoteVO.setChecknote("��׼");
				worknoteVO.setApproveresult("Y");
				Object obj = getPfBusiAction().processAction(IPFActionName.APPROVE, billtype,
						worknoteVO, aggVO, null, null);
				//����ͨ����ִ��ί�а���
				getPfBusiAction().processAction(IAllocateApplyConst.ActionCode_Submit+ InvocationInfoProxy.getInstance().getUserId(), billtype,worknoteVO, aggVO, null, null);

			} else if ("UNAPPROVE".equals(action)) {// �ܾ������쵼�ܾ������̣�NC����״̬Ϊ���ύ�����޸ĵ��������ύ�����������ύҲ���µ������ˣ�BPMtaskid��գ���
				aggVO.getParentVO().setAttributeValue("vuserdef2", null);
				AggregatedValueObject returnvo=(AggregatedValueObject) getPfBusiAction().processAction("UNSAVEBILL" + maker,
						billtype, worknoteVO, aggVO, null, null);
//				
				getBaseDAO().updateVO((SuperVO) returnvo.getParentVO());

			}else if("UNSAVE".equals(action)){//�˻أ����˻ظ��Ƶ��ˣ�NC����״̬Ϊ���ύ�����޸ĵ��������ύ�����������ύҲ���ϵ������ˣ�BPMtaskid����գ�
				AggregatedValueObject returnvo=(AggregatedValueObject) getPfBusiAction().processAction("UNSAVEBILL" + maker,
						billtype, worknoteVO, aggVO, null, null);
				getBaseDAO().updateVO((SuperVO) returnvo.getParentVO());

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		resultInfo.put("BPMID", bpmid);
		resultInfo.put("msg", "��" + action + "�����ݲ������");
		return JSON.toJSONString(resultInfo);
	}

	public BXVO getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		NCObject[] nobjs = getMDQryService().queryBillOfNCObjectByCond(c,
				whereCondStr, false);

		if (nobjs == null || nobjs.length == 0) {
			throw new BusinessException("NCϵͳδ�ܹ�����Ϣ!");
		}
		return (BXVO) nobjs[0].getContainmentObject();//
	}

	public void valid(JSONObject jobj) throws BusinessException {
		if (jobj.getString("billstate") == null || jobj.getString("") == "")
			throw new BusinessException("����״̬����Ϊ��");
		if (jobj.getString("bpmid") == null || jobj.getString("") == "")
			throw new BusinessException("BPMҵ�񵥾���������Ϊ��");
		if (jobj.getString("billtypeName") == null || jobj.getString("") == "")
			throw new BusinessException("Ŀ�굥�ݲ���Ϊ��");
	}

	/**
	 * ��ȡBPM����ԱĬ���û�
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
