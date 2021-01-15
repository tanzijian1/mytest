package nc.bs.tg.outside.business.utils.changeBill;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.exception.ExceptionUtils;

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
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillBVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;
import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;

/**
 * ��Ʊ������Ʊ������
 * @author yy
 *
 */
public class BusinessChangeBill extends BusinessBillUtils{
	public static BusinessChangeBill utils;
	
	public static BusinessChangeBill newInstance(){
		if(utils==null){
			utils=new BusinessChangeBill();
		}
		return utils;
	}
	
	/**
	 * ��Ʊ��
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
		AggChangeBillHVO billVO=null;
		billVO = ItfJsonTools.jsonToAggVO(data,AggChangeBillHVO.class, ChangeBillHVO.class, ChangeBillBVO.class);
		 srcId=billVO.getParentVO().getDef2();//��ϵͳ���ݺ�
		    String billno=(String)	getBaseDAO().executeQuery("select billno from ap_paybill where nvl(dr,0)=0 and  def2='"+srcId+"'", new ColumnProcessor());
		    if(billno!=null&&billno.length()>0){
				throw new BusinessException("��"+srcId+"��NC�������ѹ��� NC���ݺš�"+billno+"��");
			}
		    billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
			EBSBillUtils.addBillQueue(billkey);// ���Ӷ��д���
			ItfUtils.notNullCheckAndExFormula("FN11-Cxx-SY002_HEAD", "FN11-Cxx-SY002_BODY", billVO);
			IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
			nc.vo.pub.workflownote.WorkflownoteVO	worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
					"FN11", billVO, null);
			AggChangeBillHVO[] aggvos = (AggChangeBillHVO[]) getPfBusiAction().processAction(
					"SAVEBASE", "FN11", null, billVO, null, null);
			aggvos = (AggChangeBillHVO[]) getPfBusiAction().processAction(
					"SAVE", "FN11", worknoteVO, aggvos[0], null, null);
			if(aggvos!=null){
				billId= aggvos[0].getParentVO().getPrimaryKey();
				billNo=(String)aggvos[0].getParentVO().getBillno();
				billtype=(String)aggvos[0].getParentVO().getTranstype();
				}
			resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			resultVO.setMsg("�����ɹ�");
			resultVO.setBillid(billNo);
	        }catch(Exception e){
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
			logVO.setDesbill("��ҵ��Ʊ����д��ӿ�");
			logVO.setResult(resultVO.getIssuccess());
			logVO.setErrmsg(resultVO.getMsg());
			logVO.setOperator(OperatorName);
			logVO.setPrimaryKey(billNo);
			try{
				sql.billInsert_RequiresNew(logVO);
				}catch(Exception e){
					e.printStackTrace();
				}
				EBSBillUtils.removeBillQueue(billkey);
			}
	        return JSON.toJSONString(resultVO); 
	}
}
