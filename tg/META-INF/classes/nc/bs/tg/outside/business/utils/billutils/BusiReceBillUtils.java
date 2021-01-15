package nc.bs.tg.outside.business.utils.billutils;

import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.itf.tools.ItfJsonTools;
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.itf.utils.ItfUtils;
import nc.itf.baseapp.IItfformulacfgMaintain;
import nc.itf.tg.outside.BusinessBillCont;
import nc.itf.tg.outside.ItfConstants;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.itf.result.ResultVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.BusinessBillLogVO;
import nc.vo.tg.recebill.ReceivableBodyVO;
import nc.vo.tg.recebill.ReceivableHeadVO;
import uap.json.JSONObject;



/**
 * 
 * @author 黄冠华
 * 商业板块应收单工具类
 * 20200903
 *
 */
public class BusiReceBillUtils extends BusinessBillUtils{
	static BusiReceBillUtils utils;
	private IItfformulacfgMaintain itfMaintain=null;

	public static BusiReceBillUtils getUtils() {
		if (utils == null) {
			utils = new BusiReceBillUtils();
		}
		return utils;
	}
	
	/**
	 * 应收单
	 * 
	 * @param value
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

		ReceivableHeadVO headVO=null;
		List<ReceivableBodyVO> bodyVOs=null;
		
		JSONObject data=null;
		String rsInfo = "";
		String billNo="";//NC单据号
		String billTpye="";//交易类型
		String billId="";//NC单据pk
		String billkey="";
		String srcId="";//外系统单据号
		ResultVO resultVO = new ResultVO();
		ReceivableBillVO parentVO=null;
		String wbBillNO="";//外部单据号
		
		try {
			data=(JSONObject) jsonObj.get("data");
			wbBillNO=data.getJSONObject("headInfo").get("def2")+"";
			AggregatedValueObject[] billVOs = null;
			AggReceivableBillVO billVO = null;
			billVO = ItfJsonTools.jsonToAggVO(data ,AggReceivableBillVO.class, ReceivableBillVO.class, ReceivableBillItemVO.class);
			parentVO=(ReceivableBillVO) billVO.getParentVO();
			srcId=parentVO.getDef2();
			billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
			EBSBillUtils.addBillQueue(billkey);// 增加队列处理
			resultVO.setBillid(srcId);
		
			//校验重复
			String sql="select billno from ar_recbill where def2='"+wbBillNO+"'";
			Object obj=getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if(obj!=null){
				resultVO.setBillid(obj.toString());
				resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			}else {
				//转换前非空校验、执行公式、翻译后各项非空校验			
				ItfUtils.notNullCheckAndExFormula(ItfConstants.BUSIREC_CODEH,ItfConstants.BUSIREC_CODEB, billVO);

				AggReceivableBillVO billvo =null;
				HashMap eParam = new HashMap();
				IWorkflowMachine iWorkflowMachine = NCLocator.getInstance()
					      .lookup(IWorkflowMachine.class);
					    WorkflownoteVO worknote = null;
					    worknote = iWorkflowMachine.checkWorkFlow(
					      IPFActionName.SAVE,
					      (String) billVO.getParentVO().getAttributeValue("pk_tradetype"), billVO, null);
				billVOs= (AggregatedValueObject[]) getPfBusiAction().processAction("SAVE", "F0", worknote, billVO, null,
						null);
//				billVO=(AggReceivableBillVO) billVOs[0];
				parentVO=(ReceivableBillVO) billVOs[0].getParentVO();
				parentVO.getPk_tradetype();
				billNo=parentVO.getBillno();
				billId=parentVO.getPrimaryKey();
				resultVO.setBillid(billNo);
				resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
				resultVO.setMsg("操作成功");
			}
		} catch (Exception e) {
			resultVO.setBillid(wbBillNO);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("操作异常："+e.getMessage());
		} finally {
			EBSBillUtils.removeBillQueue(billkey);
			rsInfo=net.sf.json.JSONObject.fromObject(resultVO).toString();
			//构建日志VO
			BusinessBillLogVO logVO=new BusinessBillLogVO();
			logVO.setBillno(billNo);
			logVO.setBusinessno(srcId);
			logVO.setTrantype(billTpye);
			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(net.sf.json.JSONObject.fromObject(jsonObj.toString()).toString());
			logVO.setDesbill("商业应收单写入接口");
			logVO.setResult(resultVO.getIssuccess());
			logVO.setErrmsg(resultVO.getMsg());
			logVO.setOperator(OperatorName);
			logVO.setPrimaryKey(billId);
			getItfMaintain().insertVO_RequiresNew(logVO);
		}
		return rsInfo;

	}
	
	
	public IItfformulacfgMaintain getItfMaintain() {
		if (itfMaintain == null) {
			itfMaintain = NCLocator.getInstance().lookup(IItfformulacfgMaintain.class);
		}
		return itfMaintain;
	}
}
