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
 * @author �ƹڻ�
 * ��ҵ���Ӧ�յ�������
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
	 * Ӧ�յ�
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
		String billNo="";//NC���ݺ�
		String billTpye="";//��������
		String billId="";//NC����pk
		String billkey="";
		String srcId="";//��ϵͳ���ݺ�
		ResultVO resultVO = new ResultVO();
		ReceivableBillVO parentVO=null;
		String wbBillNO="";//�ⲿ���ݺ�
		
		try {
			data=(JSONObject) jsonObj.get("data");
			wbBillNO=data.getJSONObject("headInfo").get("def2")+"";
			AggregatedValueObject[] billVOs = null;
			AggReceivableBillVO billVO = null;
			billVO = ItfJsonTools.jsonToAggVO(data ,AggReceivableBillVO.class, ReceivableBillVO.class, ReceivableBillItemVO.class);
			parentVO=(ReceivableBillVO) billVO.getParentVO();
			srcId=parentVO.getDef2();
			billkey = BusinessBillCont.getBillNameMap().get(dectype) + ":" + srcId;
			EBSBillUtils.addBillQueue(billkey);// ���Ӷ��д���
			resultVO.setBillid(srcId);
		
			//У���ظ�
			String sql="select billno from ar_recbill where def2='"+wbBillNO+"'";
			Object obj=getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if(obj!=null){
				resultVO.setBillid(obj.toString());
				resultVO.setIssuccess(BusinessBillUtils.STATUS_SUCCESS);
			}else {
				//ת��ǰ�ǿ�У�顢ִ�й�ʽ����������ǿ�У��			
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
				resultVO.setMsg("�����ɹ�");
			}
		} catch (Exception e) {
			resultVO.setBillid(wbBillNO);
			resultVO.setIssuccess(BusinessBillUtils.STATUS_FAILED);
			resultVO.setMsg("�����쳣��"+e.getMessage());
		} finally {
			EBSBillUtils.removeBillQueue(billkey);
			rsInfo=net.sf.json.JSONObject.fromObject(resultVO).toString();
			//������־VO
			BusinessBillLogVO logVO=new BusinessBillLogVO();
			logVO.setBillno(billNo);
			logVO.setBusinessno(srcId);
			logVO.setTrantype(billTpye);
			logVO.setSrcsystem(BusinessBillCont.SRCSYS);
			logVO.setSrcparm(net.sf.json.JSONObject.fromObject(jsonObj.toString()).toString());
			logVO.setDesbill("��ҵӦ�յ�д��ӿ�");
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
