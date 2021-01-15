package nc.bs.tg.outside.business.utils.paybill;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.ItfJsonTools;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.itf.utils.ItfUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BasicInformationUtil;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.outside.BusinessBillLogVO;

import org.apache.commons.lang.exception.ExceptionUtils;

import uap.json.JSONObject;

import com.alibaba.fastjson.JSON;

/**
 * 商业退款单接口
 * @author yy
 *
 */
public class BusinessPayBillUtills extends BusinessBillUtils{
	public static BusinessPayBillUtills utils;
	
	public static BusinessPayBillUtills newInstance(){
		if(utils==null){
			utils=new BusinessPayBillUtills();
		}
		return utils;
	}
	/**
	 * 商业退款单
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws Exception 
	 */
	public String onSyncBill(JSONObject jsonObj, String dectype,
			String srctype) throws Exception {
		// TODO 自动生成的方法存根
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		Map<String,String> map=new HashMap<>();
		ISqlThread sql=NCLocator.getInstance().lookup(ISqlThread.class);
		String billkey = null;
		String billNo=null;
		String srcId=null;
		String billId=null;
		String billtype=null;
		ResultVO resultVO = new ResultVO();
		resultVO.setBillid(srcId);
        try{
        	JSONObject data=(JSONObject) jsonObj.get("data");
		AggPayBillVO billVO = null;
		billVO = ItfJsonTools.jsonToAggVO(data,AggPayBillVO.class, PayBillVO.class, PayBillItemVO.class);
		 srcId=billVO.getHeadVO().getDef2();//外系统单据号
	    String billno=(String)	getBaseDAO().executeQuery("select billno from ap_paybill where nvl(dr,0)=0 and  def2='"+srcId+"'", new ColumnProcessor());
	    if(billno!=null&&billno.length()>0){
			throw new BusinessException("该"+srcId+"在NC单据中已关联 NC单据号【"+billno+"】");
		}
	    billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
		EBSBillUtils.addBillQueue(billkey);// 增加队列处理
		if(billVO.getParentVO().getAttributeValue("supplier")==null||((String)billVO.getParentVO().getAttributeValue("supplier")).length()<1){
			throw new BusinessException("供应商字段为空");
		}
		if(billVO.getParentVO().getAttributeValue("def43")==null||((String)billVO.getParentVO().getAttributeValue("def43")).length()<1){
			throw new BusinessException("退款人银行账户不能为空");
		}
		//供应商处理
		// 检验客商银行账户唯一性 add by huangxj
				Collection<SupplierVO> docVO =
						getBaseDAO()
						.retrieveByClause(
								SupplierVO.class,
								"isnull(dr,0)=0 and def2 = '"
								+ billVO.getParentVO().getAttributeValue("def43")
								+ "'and name = '" + billVO.getParentVO().getAttributeValue("supplier")+ "'");
		
		String[] result = new String[2];
		if (docVO!=null&&docVO.size() <= 0) {
			// result = service.saveSupplierAccount_RequiresNew(headJson);
			result = BasicInformationUtil.getUtils().onBasicBill(JSON.parseObject(data.getString("headInfo")));
			billVO.getParentVO().setAttributeValue("supplier",  result[0]);
		}else{
			String Supplier=getSupplier((String) billVO.getParentVO()
					.getAttributeValue("supplier"),
			(String) billVO.getParentVO()
					.getAttributeValue("pk_org"),
			(String) billVO.getParentVO()
					.getAttributeValue("pk_org"));
			billVO.getParentVO().setAttributeValue("supplier", Supplier );
		}
		//end
		ItfUtils.notNullCheckAndExFormula("F3-Cxx-SY001_HEAD", "F3-Cxx-SY001_BODY", billVO);
		IWorkflowMachine iWorkflowMachine = NCLocator.getInstance().lookup(
				IWorkflowMachine.class);
		nc.vo.pub.workflownote.WorkflownoteVO	worknoteVO = iWorkflowMachine.checkWorkFlow(IPFActionName.SAVE,
				"F3", billVO, null);
		AggPayBillVO[] aggvos=(AggPayBillVO[])getPfBusiAction().processAction("SAVE", "F3", worknoteVO, billVO, null, null);
		
		if(aggvos!=null){
			billId= aggvos[0].getParentVO().getPrimaryKey();
			billNo=(String)aggvos[0].getParentVO().getAttributeValue("billno");
			billtype=(String)aggvos[0].getParentVO().getAttributeValue("pk_tradetype");
			}
		resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
		resultVO.setMsg("操作成功");
		resultVO.setBillid(billNo);
        }catch(Exception e){
        	ExceptionUtils.getFullStackTrace(e);
        	resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("操作异常："+e.getMessage());
			resultVO.setBillid(srcId);
        }finally{
        	
		//构建日志VO
		BusinessBillLogVO logVO=new BusinessBillLogVO();
		logVO.setBillno(billNo);
		logVO.setBusinessno(srcId);
		logVO.setTrantype(billtype);
		logVO.setSrcsystem(BusinessBillCont.SRCSYS);
		String data=jsonObj.getString("data");
		logVO.setSrcparm(net.sf.json.JSONObject.fromObject(jsonObj.toString()).toString());
		
		logVO.setDesbill("商业退款单写入接口");
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
	
protected String getSupplier(String...conditions) throws BusinessException {
	SQLParameter parameter=new SQLParameter();
	String pkValue=null;
	String sql = "SELECT pk_supplier FROM bd_supplier WHERE "
			+ " name = ? AND ( enablestate = 2 ) AND ((pk_supplier IN (SELECT pk_supplier FROM bd_suporg  WHERE "
			+ "pk_org IN ( ? ) AND enablestate = 2 UNION SELECT pk_supplier FROM bd_supplier WHERE ( pk_org = '000112100000000005FD' OR "
			+ "pk_org = ? ))))";
	try {
		for (String condition : conditions) {
			parameter.addParam(condition);
		}
		pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
				new ColumnProcessor());
	} catch (DAOException e) {
		throw new BusinessException(e.getMessage());
	}
   return pkValue;
}	
}

