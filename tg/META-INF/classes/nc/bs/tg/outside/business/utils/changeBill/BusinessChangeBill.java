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
 * 换票单（发票工单）
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
	 * 换票单
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
		 srcId=billVO.getParentVO().getDef2();//外系统单据号
		    String billno=(String)	getBaseDAO().executeQuery("select billno from ap_paybill where nvl(dr,0)=0 and  def2='"+srcId+"'", new ColumnProcessor());
		    if(billno!=null&&billno.length()>0){
				throw new BusinessException("该"+srcId+"在NC单据中已关联 NC单据号【"+billno+"】");
			}
		    billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
			EBSBillUtils.addBillQueue(billkey);// 增加队列处理
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
			resultVO.setMsg("操作成功");
			resultVO.setBillid(billNo);
	        }catch(Exception e){
	        	resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
				resultVO.setMsg("操作异常："+e.getMessage());
				resultVO.setBillid(srcId);
	        }finally{
	        	EBSBillUtils.removeBillQueue(billkey);
			//构建日志VO
			BusinessBillLogVO logVO=new BusinessBillLogVO();
			logVO.setBillno(billNo);
			logVO.setBusinessno(srcId);
			logVO.setTrantype(billtype);
			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(net.sf.json.JSONObject.fromObject(jsonObj.toString()).toString());
			logVO.setDesbill("商业发票工单写入接口");
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
