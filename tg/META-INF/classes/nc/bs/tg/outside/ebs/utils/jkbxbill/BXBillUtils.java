
package nc.bs.tg.outside.ebs.utils.jkbxbill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.costadvance.CostAdvanceBillUtil;
import nc.bs.tg.outside.salebpm.utils.SyncSaleBPMBillStatesUtils;
import nc.itf.arap.pub.IBXBillPublic;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISaveLogService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.ep.bx.BXBusItemVO;
import nc.vo.ep.bx.BXHeaderVO;
import nc.vo.ep.bx.BXVO;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.erm.accruedexpense.AccruedVO;
import nc.vo.erm.accruedexpense.AccruedVerifyVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.erm.common.MessageVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.ValidationException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.tg.costadvance.CostBodyVO;
import nc.vo.tg.costadvance.CostHeadVO;
import nc.vo.tg.jkbx.OutsideJKBXAccruedVerifyBodyVO;
import nc.vo.tg.jkbx.OutsideJKBXBodyItemVO;
import nc.vo.tg.jkbx.OutsideJKBXHeadVO;
import nc.vo.tg.outside.OutsideLogVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * �������ӿ�
 * @author acer
 *
 */
public class BXBillUtils extends EBSBillUtils {
	
	static BXBillUtils utils;
	IBXBillPublic BXBillPublicService = null;
	private IErmAccruedBillManage billManagerService = null;
	
	public static BXBillUtils getUtils() {
		if (utils == null) {
			utils = new BXBillUtils();
		}
		return utils;
	}
	
	public String onSyncBill(HashMap<String, Object> value, String dectype,String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("EBS");
		logVO.setDesbill("EBS��Ԥ��ռ�õ���Ԥ������Ʊ�����");
		logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		logVO.setSrcparm(value.toString());
		ISaveLogService service = NCLocator.getInstance().lookup(
				ISaveLogService.class);
		// ��ϵͳ��Ϣ
		JSONObject jsonData1 = (JSONObject) value.get("datayz");// ������
		// ����Ԥռ����Ϣ
		StringBuffer operation=new StringBuffer();//nc���в�������
		String yzdcode = EBSCont.BILL_01;//Ԥռ��
		AggAccruedBillVO ServiceaggVO=null;
		ISqlThread iser= NCLocator.getInstance().lookup(ISqlThread.class);//Ԥ�����
		ISaveLog iappover= NCLocator.getInstance().lookup(ISaveLog.class);//Ԥ�����
//		String yzdname = EBSCont.getSrcBillNameMap().get("01");
//		AggAccruedBillVO accruedAggvo=null;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		if(jsonData1!=null){
			String jsonhead1 = jsonData1.getString("accrualHeadVO");// ��ϵͳ��Դ��ͷ����
			String jsonbody1 = jsonData1.getString("accrualBodyVOs");// ��ϵͳ��Դ��������
			// ת��json
			CostHeadVO headVO = JSONObject.parseObject(jsonhead1, CostHeadVO.class);
			headVO.setOperator(headVO.getDef29());
			headVO.setOperatordept(headVO.getDef30());
			List<CostBodyVO> bodyVOs = JSONObject.parseArray(jsonbody1,
					CostBodyVO.class);
			if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
				throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData1);
			}
			
			
			String srcid = headVO.getEbsid();// EBSҵ�񵥾�ID
			String srcno = headVO.getEbsbillcode();// EBSҵ�񵥾ݵ��ݺ�
			String billqueue = "Ԥ��ռ�õ�" + ":" + srcid;
			String billkey = "Ԥ��ռ�õ�" + ":" + srcno;
			// TODO ebsid ��ʵ�ʴ�����Ϣλ�ý��б��
			AggAccruedBillVO aggVO = (AggAccruedBillVO) getBillVO(
					AggAccruedBillVO.class, "isnull(dr,0)=0 and defitem1 = '"
							+ srcid + "'");
			if (aggVO != null && (headVO.getNcid()==null || headVO.getNcid()=="")&& !("Y".equals(headVO.getIsapprove()))) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			
			EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
			try {
				//ɾ������
				if("Y".equals(headVO.getIsdelete())&&!(StringUtils.isEmpty(headVO.getNcid()))){
					AggAccruedBillVO prebillvo=new AggAccruedBillVO();
					IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
					operation.append("ɾ������");
					if("Y".equals(headVO.getIsdelete())){
						AggAccruedBillVO deleaggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
										+ headVO.getNcid()+ "'");
						if(deleaggvo==null){
							throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
						}
						if(deleaggvo.getParentVO().getBillstatus()==3){
							Object returnobj_uapp= pfaction.processAction(IPFActionName.UNAPPROVE, "262X", null,deleaggvo , null, null);
							if(((Object[])returnobj_uapp)[0] instanceof nc.vo.erm.common.MessageVO){
								prebillvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj_uapp)[0]).getSuccessVO();
								if(prebillvo!=null){
									deleaggvo=prebillvo;
								}
							}
						}
						Object returnobj=null;
						if(deleaggvo.getParentVO().getApprstatus()!=-1){
							returnobj= pfaction.processAction(IPFActionName.UNSAVE, "262X", null,deleaggvo , null, null);
						}
						if(returnobj!=null){
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.accruedexpense.AggAccruedBillVO){
								deleaggvo=(AggAccruedBillVO)((Object[])returnobj)[0];
							}
						}
						getAppService().deleteVOs(new AggAccruedBillVO[]{deleaggvo});
						ServiceaggVO=deleaggvo;
					}
				}else{
					if(!(StringUtils.isEmpty(headVO.getNcid()))&&(!"Y".equals(headVO.getIsdelete()))){//���µ���
						AggAccruedBillVO prebillvo=new AggAccruedBillVO();
						AggAccruedBillVO updedbillvo=new AggAccruedBillVO();
						AggAccruedBillVO preupdateAggvo = (AggAccruedBillVO) getBillVO(
								AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
										+ headVO.getNcid()+ "'");
						if(preupdateAggvo==null){
							throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
						}
						if(preupdateAggvo.getParentVO().getApprstatus()==1){//����ǰ���������������
							operation.append("�������");
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.UNAPPROVE, "262X", null,preupdateAggvo , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								prebillvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
						}else{
							prebillvo=preupdateAggvo;
						}
						prebillvo=(AggAccruedBillVO) getBillVO(
	      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
	      	     						+ headVO.getNcid()+ "'");
						operation.append("���²���");
						updedbillvo =CostAdvanceBillUtil.getUtils().updateVO(headVO, prebillvo,yzdcode,bodyVOs);
//						accruedAggvo=getAppService().updateVO(updedbillvo);
						try{
							IErmAccruedBillManage billManagerSer = NCLocator.getInstance().lookup(
									IErmAccruedBillManage.class);
							billManagerSer.updateVO(updedbillvo);
//		        			 updedbillvo.getParentVO().setHasntbcheck(new UFBoolean(flag));
//							ServiceaggVO=iser.ytupdate_RequiresNew(updedbillvo);
		        		 }catch (BugetAlarmBusinessException e){//Ԥ���Դ���
//		        			 updedbillvo.getParentVO().setBillno(null);
//		        			 updedbillvo.getParentVO().setPk_accrued_bill(null);
//	     					 for(AccruedDetailVO bvo:updedbillvo.getChildrenVO()){
//	     						 bvo.setPk_accrued_detail(null);
//	     					 }
		        			 updedbillvo=(AggAccruedBillVO) getBillVO(
			      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
			      	     						+ headVO.getNcid()+ "'");
	     					updedbillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
	     					ServiceaggVO= iser.ytupdate_RequiresNew(updedbillvo);
	     				    }
//						if("Y".equals(headVO.getIsapprove())){//��������
							AggAccruedBillVO billvo=(AggAccruedBillVO) getBillVO(
		      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
		      	     						+ headVO.getNcid()+ "'");
		     				if(billvo==null){
		     					throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
		     				}
		     				billvo.getParentVO().setApprover("000112100000000001IN");
		     				billvo.getParentVO().setApprovetime(new UFDateTime());
							operation.append("&��������");
//							prebillvo=iappover.ytapprove_RequiresNew(preupdateAggvo);
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,billvo , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								ServiceaggVO = (AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
//						}
					} else{//��������
						AggAccruedBillVO billvo = CostAdvanceBillUtil.getUtils().onTranBill(headVO, bodyVOs, yzdcode);
						operation.append("��������");
//						accruedAggvo=getAppService().insertVO(billvo);
						try{
//		        			 updedbillvo.getParentVO().setHasntbcheck(new UFBoolean(flag));
							ServiceaggVO=iser.ytinsert_RequiresNew(billvo);
		        		 }catch (BugetAlarmBusinessException e){//Ԥ���Դ���
//		        			 updedbillvo.getParentVO().setBillno(null);
//		        			 updedbillvo.getParentVO().setPk_accrued_bill(null);
//	     					 for(AccruedDetailVO bvo:updedbillvo.getChildrenVO()){
//	     						 bvo.setPk_accrued_detail(null);
//	     					 }
		        			 AggAccruedBillVO ybillvo =CostAdvanceBillUtil.getUtils().onTranBill(headVO, bodyVOs, srctype);
		     					ybillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
		     					ServiceaggVO= iser.ytinsert_RequiresNew(ybillvo);
	     				    }
//						if("Y".equals(headVO.getIsapprove())){//��������
//							AggAccruedBillVO bill=(AggAccruedBillVO) getBillVO(
//		      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
//		      	     						+ headVO.getNcid()+ "'");
//		     				if(billvo==null){
//		     					throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
//		     				}
//							ServiceaggVO.getParentVO().setApprover("000112100000000001IN");
//							ServiceaggVO.getParentVO().setApprovetime(new UFDateTime());
							ServiceaggVO.getParentVO().setHasntbcheck(UFBoolean.TRUE);
							operation.append("&��������");
							IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
							Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,ServiceaggVO , null, null);
							if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
								ServiceaggVO = (AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
							}
//						}
					}
				}	
			} catch (Exception e) {
				logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
				logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
						.getFullStackTrace(e));
				logVO.setOperator(srcno);//key
				throw new BusinessException("��" + billkey + "��," +e.getMessage(),e);
			} finally{
				EBSBillUtils.removeBillQueue(billqueue);
				try {
					service.saveLog_RequiresNew(logVO);
				} catch (Exception e1) {
					throw new BusinessException("������־ʧ��:"+e1.getMessage());
				}
			}
			
			dataMap.put("yzbillid", ServiceaggVO.getPrimaryKey());
			dataMap.put("yzoperation",operation.toString());
			dataMap.put("yzbillno", (String) ServiceaggVO.getParentVO()
					.getAttributeValue(AccruedVO.BILLNO));
		}
		
		
		
		//Ԥ���Ʊ�����
		JSONObject jsonData2 = (JSONObject) value.get("datayt");// ������
		if(jsonData2!=null){
			
			String jsonhead2 = jsonData2.getString("OutBXHeadVO");// ��ϵͳ��Դ��ͷ����
			String jsonItemBody2 = jsonData2.getString("OutsideJKBXBodyItemVO");// ��ϵͳ��Դ��������������
			String jsonVerifyBody2 = jsonData2.getString("OutsideJKBXAccruedVerifyBodyVO");// ��ϵͳ��Դ��������������
			OutsideJKBXHeadVO  headVO = JSONObject.parseObject(jsonhead2, OutsideJKBXHeadVO.class);
			List<OutsideJKBXBodyItemVO> itemBodyVOs = JSONObject.parseArray(jsonItemBody2,
					OutsideJKBXBodyItemVO.class);
			List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs = JSONObject.parseArray(jsonVerifyBody2,
					OutsideJKBXAccruedVerifyBodyVO.class);
			if(headVO!=null&&itemBodyVOs!=null&&verifyBodyVOs!=null)checkNull(headVO,itemBodyVOs,verifyBodyVOs);//��ֵУ��
			String actionFlag2 =  headVO.getZyx35();// NC���ݶ�����ʶ ö�� 1:���� 2:�޸� 3:ɾ�� 
			{
				String srcid = "Ԥ��Ԥ��->������"+headVO.getZyx32();// EBS��ͬ����ID
				String srcno = "Ԥ��Ԥ��->������"+headVO.getZyx2();// EBS��ͬ���ݵ��ݺ�
				String billqueue = EBSCont.getSrcBillNameMap().get(srctype) + ":"
						+ srcid;
				String billkey = EBSCont.getSrcBillNameMap().get(srctype) + ":" + srcno;
				EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
				
				try {
					BXVO aggvo = (BXVO) getBillVO(
							BXVO.class, "isnull(dr,0)=0 and pk_jkbx = '" + headVO.getZyx44()
							+ "'");
					MessageVO[] messagevos = null;
					StringBuffer title=new StringBuffer();//nc���в�������
					if(aggvo!=null&&"2".equals(actionFlag2)){
						aggvo = (BXVO) onUpdateBill(aggvo,headVO,itemBodyVOs,verifyBodyVOs);//ebs�����Ԥ�ᵥ����ͼ��NCԤ�ᵥ�ĵ��ݱ������,����д˵��ݱ��������,û��������
						title.append("���²���");
						if("Y".equals(headVO.getZyx43())){
							title.append("&��������");
						}
					}else if(aggvo!=null&&"3".equals(actionFlag2)){
						messagevos = onDeleteBill(aggvo);
						title.append("ɾ������");
					}else if(aggvo==null&&"1".equals(actionFlag2)){
						aggvo = (BXVO) onInsertBill(null,null,headVO,itemBodyVOs,verifyBodyVOs);
						title.append("��������");
						if("Y".equals(headVO.getZyx43())){
							title.append("&��������");
						}
					}
					//��˶���
					if("Y".equals(headVO.getZyx43())&&!"3".equals(actionFlag2)){
						HashMap eParam = new HashMap();
						eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
								PfUtilBaseTools.PARAM_NOTE_CHECKED);
						BXVO commitVO = (BXVO) getPfBusiAction().processAction("SAVE", "264X", null,
								aggvo, null, eParam);//�ύ
						getPfBusiAction().processAction("APPROVE", "264X", null,
								commitVO, null, eParam);
					}
					if(aggvo!=null){
						if(messagevos==null){
							dataMap.put("ytbillid", aggvo.getParentVO().getPrimaryKey());
							dataMap.put("yttitle",title.toString());
							dataMap.put("ytbillno", aggvo.getParentVO().getDjbh());
						}else{
							dataMap.put("yttitle",title.toString());
							dataMap.put("ytbillno", aggvo.getParentVO().getDjbh()+"��ɾ��");
						}
					}else{
						throw new BusinessException("�ӿڳ����쳣������������");
					}
					
				} catch (Exception e) {
					logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_FAILED);
					logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
							.getFullStackTrace(e));
					logVO.setOperator(srcno);//key
					throw new BusinessException("��" + billkey + "��," + e.getMessage(),
							e);
				}finally{
					EBSBillUtils.removeBillQueue(billqueue);
					try {
						service.saveLog_RequiresNew(logVO);
					} catch (Exception e) {
						throw new BusinessException("������־ʧ��:"+e.getMessage());
					}
				}
			}
		}
		
		return JSON.toJSONString(dataMap);
	}
	
	/**
	 * ɾ��������,�������кͺ���Ԥ����ϸ
	 * @param aggvo
	 * @return
	 * @throws BusinessException
	 */
	private MessageVO[] onDeleteBill(JKBXVO aggvo) throws BusinessException {
		List<JKBXVO> list = new ArrayList<JKBXVO>();
		list.add(aggvo);
		MessageVO[] message = null;
		if(aggvo!=null){
//			HashMap eParam = new HashMap();
//			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
//					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if(aggvo.getParentVO().getSpzt()==1){
//				getPfBusiAction().processAction(IPFActionName.UNAPPROVE, "264X", null,
//						oldaggvo, null, null);
//				BXVO aggvo = (BXVO) getBillVO(
//						BXVO.class, "isnull(dr,0)=0 and pk_jkbx = '" + headVO.getZyx44()
//						+ "'");
//				getBXBillPublicService().deleteBills(new JKBXVO[]{aggvo});
				BXVO prebillvo=new BXVO();
				IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
				Object returnobj= pfaction.processAction(IPFActionName.UNAPPROVE, "264X", null,aggvo , null, null);
				if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
					prebillvo=(BXVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
				}
				if(prebillvo!=null){
					prebillvo = (BXVO) pfaction.processAction(IPFActionName.UNSAVE, "264X", null,prebillvo , null, null);
				}
//				if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
//					prebillvo=(BXVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
//				}
				message = getBXBillPublicService().deleteBills(new BXVO[]{prebillvo});
			}else{
				message = getBXBillPublicService().deleteBills(list.toArray(new JKBXVO[0]));
			}
		}
		return message;
	}
	
	/**
	 * ����������,�������кͺ���Ԥ����ϸ
	 * @param oldPk 
	 * @param oldBillNo 
	 * @param headVO
	 * @param itemBodyVOs
	 * @param verifyBodyVOs 
	 * @return
	 * @throws BusinessException
	 */
	private JKBXVO onInsertBill(String oldPk, String oldBillNo, OutsideJKBXHeadVO headVO, List<OutsideJKBXBodyItemVO> itemBodyVOs, List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException {
		
		if(headVO==null||"null".equals(headVO)){
			throw new BusinessException("��������ͷΪ��,�����������");
		}
		if(itemBodyVOs==null||"null".equals(itemBodyVOs)){
			throw new BusinessException("���������嵥����ϢΪ��,�����������");
		}
		if(verifyBodyVOs==null||"null".equals(verifyBodyVOs)){
			throw new BusinessException("Ԥ����ϸ��ϢΪ��,�����������");
		}
//		BXBusItemVO[] itemvoArray = itemvos.toArray(new BXBusItemVO[0]);
//		AccruedDetailVO[] verifyArray = detailvos.toArray(new AccruedDetailVO[0]);
		JKBXVO aggvo = new BXVO(); 
		List<JKBXVO> list = new ArrayList<JKBXVO>();
		BXHeaderVO  headvo = new BXHeaderVO();//��ͷ����
		List<BXBusItemVO> busitemvos = new LinkedList<BXBusItemVO>();//������ϸvo
		BXBusItemVO busitemvo = null;
		List<AccruedVerifyVO> verifyvos = new LinkedList<AccruedVerifyVO>();//����Ԥ����ϸ
		AccruedVerifyVO verifyvo = null;
//		BXBusItemVO[] busitemvos = new BXBusItemVO[itemBodyVOs.size()];//������ϸvo
//		AccruedVerifyVO[] verifyvos= new AccruedVerifyVO[verifyBodyVOs.size()];//����Ԥ����ϸ
		
		//��ͷ��Ϣ(������ϵͳ��ͷ�ֶ���nc��������ͷ�ֶβ�ƥ��,�ʴ˴���ͷ��Ҫ��������һ����ϵͳ��Ϊjavabean������)
		headvo.setDr(0);
		headvo.setStatus(VOStatus.NEW);
		headvo.setDjdl("bx");//���ݴ���
		headvo.setDjlxbm("264X-Cxx-006");//�������ͱ���
		headvo.setPk_tradetypeid(getBillTypePkByCode("264X-Cxx-006",getPk_orgByCode("0001")));//��������pk
		headvo.setCreationtime(new UFDateTime());//����ʱ��
		headvo.setCreator(getUserIDByCode("EBS"));//������
		headvo.setDjrq(new UFDate(headVO.getDjrq()));//��������
		headvo.setDjzt(1);//����״̬
		headvo.setFlexible_flag(UFBoolean.FALSE);//�Ƿ����Կ���
		headvo.setIscheck(UFBoolean.FALSE);//�Ƿ��޶�
		headvo.setIscostshare(UFBoolean.FALSE);//�Ƿ��̯
		headvo.setIsexpamt(UFBoolean.FALSE);//�Ƿ��̯
		headvo.setIsmashare(UFBoolean.FALSE);//���뵥�Ƿ��̯
		headvo.setIsneedimag(UFBoolean.FALSE);//�Ƿ�Ӱ��ɨ��
		headvo.setIsexpedited(UFBoolean.FALSE);//�Ƿ����
		headvo.setKjnd(headVO.getDjrq().substring(0, 4));//������
		headvo.setKjqj(headVO.getDjrq().substring(5, 7));//����ڼ�
		headvo.setPayflag(1);//֧��״̬
		headvo.setPk_billtype("264X");//��������
		headvo.setPk_group(getPk_orgByCode("0001"));//����
		headvo.setPk_pcorg("pk_pcorg");//ԭ��������
//		headvo.setPk_tradetypeid(headVO.getPk_tradetypeid());//��������pk
		headvo.setQcbz(UFBoolean.FALSE);//�ڳ���ʶ
		headvo.setQzzt(0);
		headvo.setSpzt(-1);//����״̬
		headvo.setSxbz(0);//��Ч״̬
		headvo.setPaytarget(1);//�տ����
		headvo.setBbhl(new UFDouble(1));//���һ���
		headvo.setOperator(getUserPkByCode("TY01"));//������1001ZZ100000001MR73Z
//		headvo.setOperator("1001ZZ100000001MR73Z");
		if(StringUtils.isNotBlank(oldPk)&&StringUtils.isNotBlank(oldBillNo)){
			headvo.setPk_jkbx(oldPk);
			headvo.setDjbh(oldBillNo);
		}
		
		
//		headvo.setBzbm(getPkcurrtypeByCode("CNY"));//����
		headvo.setBzbm(headVO.getBzbm());//����
		headvo.setPk_org(getPk_orgByCode(headVO.getPk_org()));//���˹�˾
		headvo.setPk_org_v(getPk_org_vByCode(headVO.getPk_org()));//ԭ�����˵�λ��֯�汾
		headvo.setPk_payorg(getPk_orgByCode(headVO.getPk_org()));//ԭ֧����֯
		headvo.setPk_payorg_v(getPk_org_vByCode(headVO.getPk_org()));//ԭ֧����֯�汾
		headvo.setPk_fiorg(getPk_orgByCode(headVO.getPk_org()));//������֯(����)
//		headvo.setJkbxr(headVO.getJkbxr());//������getPsndocPkByCode
		headvo.setJkbxr(getUserPkByCode("TY01"));//������
		headvo.setZyx6(headVO.getZyx6());//���
		headvo.setTotal(new UFDouble(headVO.getTotal()));//���γ�Ԥ������
		headvo.setYbje(new UFDouble(headVO.getTotal()));//ԭ�ҽ��
		headvo.setHbbm(getCustPkByCode(headVO.getHbbm()));//��Ӧ��
		headvo.setFydwbm(getPk_orgByCode(headVO.getFydwbm()));//ԭ���óе���λ
		headvo.setFydwbm_v(getPk_org_vByCode(headVO.getFydwbm()));//ԭ���óе����Ű汾(���쵥λ)
		headvo.setDwbm(getPk_orgByCode(headVO.getFydwbm()));//ԭ�����˵�λ
		headvo.setDwbm_v(getPk_org_vByCode(headVO.getFydwbm()));//ԭ�����˵�λ��֯�汾
//		headvo.setDeptid(headVO.getDeptid());//���첿��
		headvo.setDeptid(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept"));//���첿��
		headvo.setDeptid_v(getDept_v_pk(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept")).get("pk_vid"));//ԭ���첿�Ű汾
//		headvo.setDeptid_v(getDeptpksByCode("00002122").get("pk_vid"));
		headvo.setFydeptid(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept"));//ԭ���óе�����
		headvo.setFydeptid_v(getDept_v_pk(getPsnjobMainDeptByPk(getPsnPkByCode("TY01")).get("pk_dept")).get("pk_vid"));//���óе�����
		headvo.setZyx2(changeNull(headVO.getZyx2()));//EBS��ͬ����
		headvo.setZyx9(changeNull(headVO.getZyx9()));//EBS��ͬ����
		headvo.setZyx32(changeNull(headVO.getZyx32()));//EBS��ͬID
		headvo.setZyx33(changeNull(headVO.getZyx33()));//EBS��ͬ����
		headvo.setZyx34(changeNull(headVO.getZyx34()));//EBS��ͬϸ��
//		headvo.setReceiver(receiver);//�տ���
		
		//���
		UFDouble amount = UFDouble.ZERO_DBL;
		//������ϸ��Ϣ
		for(int i=0;i<itemBodyVOs.size();i++){
			busitemvo = new BXBusItemVO();
			busitemvo.setDr(0);
			busitemvo.setStatus(VOStatus.NEW);
			busitemvo.setAmount(new UFDouble(itemBodyVOs.get(i).getAmount()));//���
			busitemvo.setYbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//ԭ�ҽ��
			busitemvo.setBbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//���ҽ��
			busitemvo.setZfybje(new UFDouble(itemBodyVOs.get(i).getAmount()));//֧��ԭ�ҽ��
			busitemvo.setZfbbje(new UFDouble(itemBodyVOs.get(i).getAmount()));//֧�����ҽ��
			busitemvo.setTablecode("arap_bxbusitem");//ҳǩ����
			busitemvo.setDefitem12(changeNull(itemBodyVOs.get(i).getDefitem12()));//Ԥ���Ŀ
			busitemvo.setJobid(changeNull(itemBodyVOs.get(i).getJobid()));//��Ŀ
			busitemvo.setDefitem22(changeNull(itemBodyVOs.get(i).getDefitem22()));//ҵ̬
			busitemvo.setDefitem26(changeNull(itemBodyVOs.get(i).getDefitem26()));//���Ʋ���
			busitemvo.setDefitem24(changeNull(itemBodyVOs.get(i).getDefitem24()));//����¥��
			busitemvo.setJkbxr(itemBodyVOs.get(i).getJkbxr());//�ÿ���
			busitemvo.setDefitem49(changeNull(itemBodyVOs.get(i).getDefitem49()));//����
			busitemvo.setDefitem30(changeNull(itemBodyVOs.get(i).getDefitem30()));//˵��
			busitemvo.setHbbm(getCustPkByCode(headVO.getHbbm()));//��Ӧ��(����ֻ����һ����Ӧ��,ȡ��ͷ)
			amount = amount.add(busitemvo.getAmount());
			busitemvos.add(busitemvo);
		}
		headvo.setTotal(amount);
		
		//�������
		UFDouble verifyAmount = UFDouble.ZERO_DBL;
		//����Ԥ����ϸ
		for(int i=0;i<verifyBodyVOs.size();i++){
			verifyvo = new AccruedVerifyVO();
			verifyvo.setDr(0);
			verifyvo.setStatus(VOStatus.NEW);
			verifyvo.setVerify_amount(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//�������
			verifyvo.setOrg_verify_amount(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//��֯���Һ������
			verifyvo.setPk_accrued_detail(verifyBodyVOs.get(i).getPk_accrued_detail());//Ԥ����ϸ��
			verifyvo.setAccrued_billno(verifyBodyVOs.get(i).getAccrued_billno());//Ԥ�ᵥ�ݱ��
			verifyvo.setPk_accrued_bill(getAccruedPkByCode(verifyBodyVOs.get(i).getAccrued_billno()));//����Ԥ�ᵥpk
			verifyvo.setVerify_date(new UFDate(verifyBodyVOs.get(i).getVerify_date()));//��������
			verifyvo.setPk_group(getPk_orgByCode("0001"));//Ĭ��ʱ������
			verifyvo.setPk_org(getPk_orgByCode(headVO.getPk_org()));//��֯
			verifyvo.setVerify_man("#EBS#");
			verifyAmount = verifyAmount.add(new UFDouble(verifyBodyVOs.get(i).getVerify_amount()));//�������
			verifyvos.add(verifyvo);
		}
		
		aggvo.setParentVO(headvo);
		aggvo.setChildrenVO(busitemvos.toArray(new BXBusItemVO[0]));
		aggvo.setTableVO("er_accrued_verify", verifyvos.toArray(new AccruedVerifyVO[0]));
		
		checkBxVOValid(aggvo);// У���Ƿ�ɽ��к���Ԥ��
		list.add(aggvo);
		JKBXVO[] jkbxAggvo = getBXBillPublicService().save(list.toArray(new JKBXVO[0]));
		
		return jkbxAggvo[0];
		
	}
	
	/**
	 * ���±�����,�������кͺ���Ԥ����ϸ
	 * @param aggvo
	 * @param headVO
	 * @param itemBodyVOs
	 * @param verifyBodyVOs
	 * @return
	 * @throws BusinessException
	 */
	private JKBXVO onUpdateBill(JKBXVO oldaggvo, OutsideJKBXHeadVO headVO, List<OutsideJKBXBodyItemVO> itemBodyVOs, List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException {
		
		String oldPk = oldaggvo.getParentVO().getPrimaryKey();
		String oldBillNo = oldaggvo.getParentVO().getDjbh();
		onDeleteBill(oldaggvo);//����ǰ�Ȱ�֮ǰ���������
		return onInsertBill(oldPk,oldBillNo,headVO,itemBodyVOs,verifyBodyVOs);//���ݾɵ�pk�͵��ݱ�����²���
	}
	
	
	private void checkBxVOValid(JKBXVO vo) throws ValidationException {
		StringBuffer bf = new StringBuffer();
		UFBoolean iscostshare = vo.getParentVO().getIscostshare();
		if (iscostshare != null && iscostshare.booleanValue()) {
			bf.append(NCLangRes4VoTransl.getNCLangRes().getStrByID("2011v61013_0", "02011v61013-0120")/*
			 * @
			 * res
			 * "�������Ѿ����÷�̯�����ɽ��к���Ԥ��"
			 */);
		}
		
		UFBoolean isexp = vo.getParentVO().getIsexpamt();
		if (isexp != null && isexp.booleanValue()) {
			bf.append("\n" + NCLangRes4VoTransl.getNCLangRes().getStrByID("2011v61013_0", "02011v61013-0121")/*
			 * @
			 * res
			 * "�������Ѿ����ô�̯�����ɽ��к���Ԥ��"
			 */);
		}
		
		if(bf.length() != 0){
			throw new ValidationException(bf.toString());
		}
	}
	
	/**
	 * ��ֵУ��
	 * @param headvo
	 * @param itemBodyVOs
	 * @param verifyBodyVOs
	 * @throws BusinessException
	 */
	public void checkNull(OutsideJKBXHeadVO  headvo,List<OutsideJKBXBodyItemVO> itemBodyVOs,List<OutsideJKBXAccruedVerifyBodyVO> verifyBodyVOs) throws BusinessException{
		if(StringUtils.isBlank(headvo.getBzbm()))throw new BusinessException("���ֲ���Ϊ��");
		if(StringUtils.isBlank(headvo.getPk_org()))throw new BusinessException("���˹�˾����Ϊ��");
//		if(StringUtils.isBlank(headvo.getZyx6()))throw new BusinessException("��鲻��Ϊ��");
		if(StringUtils.isBlank(headvo.getDjrq()))throw new BusinessException("�������ڲ���Ϊ��");
		if(StringUtils.isBlank(headvo.getTotal()))throw new BusinessException("���γ�Ԥ�����Ϊ��");
		if(StringUtils.isBlank(headvo.getFydwbm()))throw new BusinessException("���óе���λ����Ϊ��");
		if(StringUtils.isBlank(headvo.getFydeptid()))throw new BusinessException("���óе����Ų���Ϊ��");
//		if(StringUtils.isBlank(headvo.getPk_tradetypeid()))throw new BusinessException("�������Ͳ���Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx2()))throw new BusinessException("��ͬ���벻��Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx9()))throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx32()))throw new BusinessException("��ͬID����Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx33()))throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx34()))throw new BusinessException("��ͬϸ�಻��Ϊ��");
//		if(StringUtils.isBlank(headvo.getJkbxr()))throw new BusinessException("�����˲���Ϊ��");
//		if(StringUtils.isBlank(headvo.getDeptid()))throw new BusinessException("���첿�Ų���Ϊ��");
//		if(StringUtils.isBlank(headvo.getDwbm_v()))throw new BusinessException("���쵥λ����Ϊ��");
//		if(StringUtils.isBlank(headvo.getPaytarget()))throw new BusinessException("�տ���󲻿�Ϊ��");
		if(StringUtils.isBlank(headvo.getHbbm()))throw new BusinessException("��Ӧ�̲���Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx35()))throw new BusinessException("״̬����Ϊ��");
		if(StringUtils.isBlank(headvo.getZyx43()))throw new BusinessException("�Ƿ���������Ϊ��");
		int i = 1;
		for (OutsideJKBXBodyItemVO outsideJKBXBodyItemVO : itemBodyVOs) {
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem12()))throw new BusinessException("��"+i+"��,Ԥ���Ŀ����Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getJobid()))throw new BusinessException("��"+i+"��,��Ŀ����Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem22()))throw new BusinessException("��"+i+"��,ҵ̬����Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem26()))throw new BusinessException("��"+i+"��,���Ʋ��Ų���Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem24()))throw new BusinessException("��"+i+"��,����¥�㲻��Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getJkbxr()))throw new BusinessException("��"+i+"��,�ÿ��˲���Ϊ��");
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getAmount()))throw new BusinessException("��"+i+"��,����Ϊ��");
			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem49()))throw new BusinessException("��"+i+"��������Ϊ��");
//			if(StringUtils.isBlank(outsideJKBXBodyItemVO.getDefitem30()))throw new BusinessException("��"+i+"˵������Ϊ��");
			i++;
		}
		int j = 1;
		for (OutsideJKBXAccruedVerifyBodyVO outsideJKBXAccruedVerifyBodyVO : verifyBodyVOs) {
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getAccrued_billno()))throw new BusinessException("��"+j+"��,Ԥ�ᵥ�Ų���Ϊ��");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getPk_accrued_detail()))throw new BusinessException("��"+j+"��,Ԥ�ᵥ��pk����Ϊ��");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getVerify_amount()))throw new BusinessException("��"+j+"��,��������Ϊ��");
			if(StringUtils.isBlank(outsideJKBXAccruedVerifyBodyVO.getVerify_date()))throw new BusinessException("��"+j+"��,�������ڲ���Ϊ��");
			j++;
		}
	}
	
	public IBXBillPublic getBXBillPublicService() {
		if (BXBillPublicService == null) {
			BXBillPublicService = NCLocator.getInstance().lookup(
					IBXBillPublic.class);
		}
		return BXBillPublicService;
	}
	
	private IErmAccruedBillManage getAppService() {
		if (billManagerService == null) {
			billManagerService = NCLocator.getInstance().lookup(
					IErmAccruedBillManage.class);
		}
		return billManagerService;
	}
	
	private String changeNull(String arg){
		if("".equals(arg)||"~".equals(arg)||null==arg){
			return null;
		}
		return arg;
		
	}
	
	BaseDAO baseDAO = null;
	/**
	 * ���ݿ�־û�
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	
	private String getUserIDByCode(String code) throws BusinessException{
		StringBuffer sql = new StringBuffer();
		sql.append("select sm_user.cuserid from sm_user where sm_user.user_code = '"+code+"' and dr = 0 and sm_user.enablestate = 2 ");
		String userid = (String) getBaseDAO().executeQuery(sql.toString(), new ColumnProcessor());
		return userid;
	}
}