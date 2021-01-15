package nc.bs.tg.outside.business.utils.bxBill;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.ItfJsonTools;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.itf.utils.ItfUtils;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXHeaderVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.outside.BusinessBillLogVO;

public class BusinessHistoryBxBillUtils extends BusinessBillUtils{
	private static BusinessHistoryBxBillUtils utils;
	public static BusinessHistoryBxBillUtils newInstance(){
		if(utils==null){
			utils=new BusinessHistoryBxBillUtils();
		}
		return utils;
	}
	/**
	 * ��ʷ���ݲ�¼��
	 * @param jsonObj
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws Exception
	 */
	public String onSyncBill(JSONObject jsonObj, String dectype,
				String srctype) throws Exception {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		ISqlThread sql=NCLocator.getInstance().lookup(ISqlThread.class);
		Map<String,String> map=new HashMap<>();
		String billkey = null;
		String billNo=null;
		String srcId=null;
		String billId=null;
		String billtype=null;
		ResultVO resultVO = new ResultVO();
		resultVO.setBillid(srcId);
		try{
			 JSONObject data=(JSONObject) jsonObj.get("data");
			 JKBXVO billVO = null;
			billVO = ItfJsonTools.jsonToAggVO(data, BXVO.class, BXHeaderVO.class, BXBusItemVO.class);
			 srcId=billVO.getParentVO().getDef69();//��ϵͳ���ݺ�
		    String billno=(String)	getBaseDAO().executeQuery("select djbh from er_bxzb where nvl(dr,0)=0 and  def69='"+srcId+"'", new ColumnProcessor());
		    if(billno!=null&&billno.length()>0){
				throw new BusinessException("��"+srcId+"��NC�������ѹ��� NC���ݺš�"+billno+"��");
			}
		    billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
			EBSBillUtils.addBillQueue(billkey);// ���Ӷ��д���
		    ItfUtils.notNullCheckAndExFormula("264X-Cxx-SY002_HEAD", "264X-Cxx-SY002_BODY", billVO);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			nc.vo.pub.workflownote.WorkflownoteVO	worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
					"264X-Cxx-003", billVO, null);
			setdefaultValue(billVO);
			JKBXVO[] aggvos=(JKBXVO[])getPfBusiAction().processAction("WRITE", "264X", worknoteVO, billVO, null, null);
				if(aggvos!=null){
					billId= aggvos[0].getParentVO().getPrimaryKey();
					billNo=(String)aggvos[0].getParentVO().getAttributeValue("djbh");
					billtype=(String)aggvos[0].getParentVO().getAttributeValue("djlxbm");
					}
				resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
				resultVO.setMsg("�����ɹ�");
				resultVO.setBillid(billNo);
		}catch(Exception e){
			ExceptionUtils.getFullStackTrace(e);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("�����쳣��"+e.getMessage());
			resultVO.setBillid(srcId);
		}finally{
			EBSBillUtils.removeBillQueue(billkey);
			//������־VO
			BusinessBillLogVO logVO=new BusinessBillLogVO();
			logVO.setBillno(billNo);
			logVO.setBusinessno(srcId);
			logVO.setTrantype(billtype);
			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(net.sf.json.JSONObject.fromObject(jsonObj.toString()).toString());
			logVO.setDesbill("��ҵ��ʷ���ݲ�¼��д��ӿ�");
			logVO.setResult(resultVO.getIssuccess());
			logVO.setErrmsg(resultVO.getMsg());
			logVO.setOperator(OperatorName);
			logVO.setPrimaryKey(billId);
			try{
				sql.billInsert_RequiresNew(logVO);
				}catch(Exception e){
					e.printStackTrace();
				}
				EBSBillUtils.removeBillQueue(billkey);
		}
		return JSON.toJSONString(resultVO);
	}
	/**
	 * ����Ĭ��ֵ
	 * @param billVO
	 */
	private void setdefaultValue(JKBXVO billVO){
		if(billVO!=null){
			if(billVO.getBxBusItemVOS()!=null){
		for(BXBusItemVO bvo:billVO.getBxBusItemVOS()){
			bvo.setTablecode("arap_bxbusitem");
			bvo.setAttributeValue("receiver",bvo.getReceiver());
		}
			}
		}
	}
}
