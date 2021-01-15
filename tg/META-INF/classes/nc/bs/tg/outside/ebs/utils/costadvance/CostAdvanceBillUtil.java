package nc.bs.tg.outside.ebs.utils.costadvance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.fi.pub.Currency;
import nc.itf.tg.ISqlThread;
import nc.itf.uap.pf.IPFBusiAction;
import nc.pubitf.erm.accruedexpense.IErmAccruedBillManage;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.er.exception.BugetAlarmBusinessException;
import nc.vo.erm.accruedexpense.AccruedDetailVO;
import nc.vo.erm.accruedexpense.AccruedVO;
import nc.vo.erm.accruedexpense.AggAccruedBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pubapp.AppContext;
import nc.vo.tg.costadvance.CostBodyVO;
import nc.vo.tg.costadvance.CostHeadVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class CostAdvanceBillUtil extends EBSBillUtils {
	static CostAdvanceBillUtil utils;
	private IErmAccruedBillManage billManagerService;
    public String errormessage=null;
	public static CostAdvanceBillUtil getUtils() {
		if (utils == null) {
			utils = new CostAdvanceBillUtil();
		}
		return utils;
	}

	/**
	 * ����Ԥ�ᵥ
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getcreator());
//		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
			ISqlThread iser= NCLocator.getInstance().lookup(ISqlThread.class);//Ԥ�����
		AggAccruedBillVO aggvo=null;
		// �������Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");// ������
		String jsonhead = jsonData.getString("accrualHeadVO");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("accrualBodyVOs");// ��ϵͳ��Դ��������
		StringBuffer operation=new StringBuffer();//nc���в�������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		// ת��json
		CostHeadVO headVO = JSONObject.parseObject(jsonhead, CostHeadVO.class);
		headVO.setOperator(headVO.getDef29());
		headVO.setOperatordept(headVO.getDef30());
		List<CostBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				CostBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		  }

         
		String srcid = headVO.getEbsid();// EBSҵ�񵥾�ID
		String srcno = headVO.getEbsbillcode();// EBSҵ�񵥾ݵ��ݺ�
		String billqueue = "Ԥ��ռ�õ�" + ":" + srcid;
		String billkey = "Ԥ��ռ�õ�" + ":" + srcno;
		// // TODO ebsid ��ʵ�ʴ�����Ϣλ�ý��б��
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
	        	 aggvo=deleaggvo;
	        	 }
	         }else{
	        	 if(!(StringUtils.isEmpty(headVO.getNcid()))&&!("Y".equals(headVO.getIsapprove()))){//���µ���
	        		 IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
	        		 AggAccruedBillVO prebillvo=new AggAccruedBillVO();
	        		 AggAccruedBillVO updedbillvo=new AggAccruedBillVO();
	        		 AggAccruedBillVO preupdateAggvo = (AggAccruedBillVO) getBillVO(
	 	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
	 	     						+ headVO.getNcid()+ "'");
	        		 if(preupdateAggvo==null){
	        			 throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
	        		 }
//	        			boolean flag=false;
	        		 if(preupdateAggvo.getParentVO().getApprstatus()==1){//����ǰ���������������
//	        			flag=true; 
	        			 operation.append("�������");
	        			  prebillvo=iser.ytunapprove_RequiresNew(preupdateAggvo);
	        		 }else{
	        			 prebillvo=preupdateAggvo;
	        		 }
	        		 operation.append("���²���");
	        		 updedbillvo =updateVO(headVO, prebillvo,srctype,bodyVOs);
	        		 try{
//	        			 updedbillvo.getParentVO().setHasntbcheck(new UFBoolean(flag));
	        		 aggvo=iser.ytupdate_RequiresNew(updedbillvo);
	        		 }catch (BugetAlarmBusinessException e){//Ԥ���Դ���
//	        			 updedbillvo.getParentVO().setBillno(null);
//	        			 updedbillvo.getParentVO().setPk_accrued_bill(null);
//     					 for(AccruedDetailVO bvo:updedbillvo.getChildrenVO()){
//     						 bvo.setPk_accrued_detail(null);
//     					 }
     					updedbillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
     					  aggvo= iser.ytupdate_RequiresNew(updedbillvo);
     					  
     				    }
	        		 //TODO20200805��������������
	        		 aggvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
	        		 Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,aggvo , null, null);
	        		 if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
	         				aggvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
	         			}
	        	 }else if("Y".equals(headVO.getIsapprove())){//��������
	     				AggAccruedBillVO billvo=(AggAccruedBillVO) getBillVO(
	      	     				AggAccruedBillVO.class, "isnull(dr,0)=0 and  pk_accrued_bill = '"
	      	     						+ headVO.getNcid()+ "'");
	     				if(billvo==null){
	     					throw new BusinessException("����ncid��Ч���Ҳ�����Ӧ����");
	     				}
	     				billvo.getParentVO().setApprover("000112100000000001IN");
	     				billvo.getParentVO().setApprovetime(new UFDateTime());
	     				 operation.append("��������");
	     				IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
	     				billvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
	     				if(billvo.getParentVO().getApprstatus()!=1){
	         			Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,billvo , null, null);
	         			if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
	         				aggvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
	         			}
	     				}else{
	     					aggvo=billvo;
	     				}
	     			} else{//��������
	     				IPFBusiAction pfaction=NCLocator.getInstance().lookup(IPFBusiAction.class);
		        		 AggAccruedBillVO billvo = onTranBill(headVO, bodyVOs, srctype);
		        		 AccruedDetailVO[]  bvoss=billvo.getChildrenVO();
		        		 AggAccruedBillVO nbillvo=(AggAccruedBillVO)billvo.clone();
		        		 operation.append("��������");
		        		 try{
//			     			aggvo=getAppService().insertVO(billvo);
		        			 aggvo= iser.ytinsert_RequiresNew(billvo);
	     				  }catch (BugetAlarmBusinessException e){//Ԥ���Դ���
	     					 
	     					 AggAccruedBillVO ybillvo =onTranBill(headVO, bodyVOs, srctype);
	     					ybillvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
	     					  aggvo= iser.ytinsert_RequiresNew(ybillvo);
	     				    }
		        		 //TODO20200805��������������
		        		 aggvo.getParentVO().setHasntbcheck(UFBoolean.TRUE);
		        		 Object returnobj= pfaction.processAction(IPFActionName.APPROVE, "262X", null,aggvo , null, null);
		        		 if(((Object[])returnobj)[0] instanceof nc.vo.erm.common.MessageVO){
		         				aggvo=(AggAccruedBillVO)((nc.vo.erm.common.MessageVO)((Object[])returnobj)[0]).getSuccessVO();
		         			}
			        	 }
	        	 }

			
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," +e.getMessage(),e);
		} finally{
			EBSBillUtils.removeBillQueue(billqueue);
		}
		
        
		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo.getPrimaryKey());
		dataMap.put("operation",operation.toString());
		dataMap.put("billno", (String) aggvo.getParentVO()
				.getAttributeValue(AccruedVO.BILLNO));
		if(aggvo==null){
        	throw new BusinessException("��" + billkey + "��," + "����**�޷��ص���"+"���в���**"+operation.toString());
        }
		return JSON.toJSONString(dataMap);
	}
	/*
	 * ����Ԥ�ᵥ��vo
	 * */
public AggAccruedBillVO updateVO(CostHeadVO headvo,AggAccruedBillVO aggvo,String srctype,List<CostBodyVO> bodyVOs) throws Exception{
	AccruedVO update_hvo=aggvo.getParentVO();
	AccruedDetailVO[] bodyvos=aggvo.getChildrenVO();
//	AggAccruedBillVO billvo=new AggAccruedBillVO();
	List<AccruedDetailVO> list=Arrays.asList(new AccruedDetailVO[0]);
//	update_hvo.setAttributeValue("pk_accrued_bill", null);// ����
//	update_hvo.setAttributeValue("pk_group", getpk_groupByCode(headvo.getOrg()));// ��������
//	update_hvo.setAttributeValue("pk_org",
//			getPk_orgByCode(headvo.getOrg()));// ������֯
//	update_hvo.setAttributeValue("pk_billtype", "262X");// ��������
//	if ("1".equals(srctype)) {// 1Ϊͨ�ú�ͬ
//		update_hvo.setAttributeValue("pk_tradetype", "262X-Cxx-FY01");// �������ͱ��
//	} else {
//		update_hvo.setAttributeValue("pk_tradetype", "2621");// �������ͱ��
//	}
//	update_hvo.setAttributeValue("pk_tradetypeid",getBillTypePkByCode(update_hvo.getPk_tradetype(),update_hvo.getPk_group()));// ��������
//	update_hvo.setAttributeValue("billstatus", 1);// ����״̬
//	update_hvo.setAttributeValue("apprstatus", -1);// ����״̬
//	update_hvo.setAttributeValue("effectstatus", 0);// ��Ч״̬
	update_hvo.setAttributeValue("org_currinfo", null);// ��֯���һ���
	update_hvo.setBilldate(new UFDate(headvo.getBilldate()));// ��������
	update_hvo.setAttributeValue("group_currinfo", null);// ���ű��һ���
	update_hvo.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
	update_hvo.setAttributeValue("amount", null);// ���
	update_hvo.setAttributeValue("org_amount", null);// ��֯���ҽ��
	update_hvo.setAttributeValue("group_amount", null);// ���ű��ҽ��
	update_hvo.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
	update_hvo.setAttributeValue("verify_amount", null);// �������
	update_hvo.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
	update_hvo.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
	update_hvo.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
	update_hvo.setAttributeValue("predict_rest_amount", null);// Ԥ�����
	update_hvo.setAttributeValue("rest_amount", null);// ���
	update_hvo.setAttributeValue("org_rest_amount", null);// ��֯�������
	update_hvo.setAttributeValue("group_rest_amount", null);// ���ű������
	update_hvo.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
	update_hvo.setAttributeValue("reason", headvo.getReason());// ����
	update_hvo.setAttributeValue("attach_amount", null);// ��������
//	update_hvo.setAttributeValue("operator_org",
//			getPkOrgPkByCode(headvo.getOperatororg()));// �����˵�λ
//	update_hvo.setAttributeValue("operator_dept",getPk_DeptByCode(headvo.getOperatordept(),update_hvo.getPk_org()));// �����˲���
//	update_hvo.setAttributeValue("operator",getPsndocPkByCode(headvo.getOperator()));// ������
////	update_hvo.setAttributeValue("operator_org",
//			"000112100000000024G6");// �����˵�λ
//	update_hvo.setAttributeValue("operator_dept","1001121000000007V38J");
//	update_hvo.setAttributeValue("operator","1001ZZ100000001NHREE");
	update_hvo.setAttributeValue("defitem1", headvo.getEbsid());// EBSID
	update_hvo.setAttributeValue("defitem2", headvo.getEbsbillcode());// EBS����
	update_hvo.setAttributeValue("defitem3", null);// �Զ�����3
	update_hvo.setAttributeValue("defitem4", null);// �Զ�����4
	update_hvo.setAttributeValue("defitem5", null);// �Զ�����5
	update_hvo.setAttributeValue("defitem6",headvo.getPlate());// ���
	update_hvo.setAttributeValue("defitem7", headvo.getIspostcontract());// �Ƿ�����ͬ
	update_hvo.setAttributeValue("defitem8", headvo.getContractmoney());// ��ͬ��̬���
	update_hvo.setAttributeValue("defitem9", null);// �Զ�����9
	update_hvo.setAttributeValue("defitem10", null);// �Զ�����10
	update_hvo.setAttributeValue("defitem11", headvo.getContractcode());// ��ͬ����
	update_hvo.setAttributeValue("defitem12", headvo.getContractname());// ��ͬ����
	update_hvo.setAttributeValue("defitem13", headvo.getContractversion());// ��ͬ�汾
	update_hvo.setAttributeValue("defitem14", headvo.getBilltype());// ��������
	update_hvo.setAttributeValue("defitem15", null);// Ԥ������
	update_hvo.setAttributeValue("defitem16", null);// �Զ�����16
	update_hvo.setAttributeValue("defitem17", null);// �Զ�����17
	update_hvo.setAttributeValue("defitem18", null);// �Զ�����18
	update_hvo.setAttributeValue("defitem19", null);// �Զ�����19
	update_hvo.setAttributeValue("defitem20", null);// �Զ�����20
	update_hvo.setAttributeValue("defitem21", null);// �Զ�����21
	update_hvo.setAttributeValue("defitem22", null);// �Զ�����22
	update_hvo.setAttributeValue("defitem23", null);// �Զ�����23
	update_hvo.setAttributeValue("defitem24", null);// �Զ�����24
	update_hvo.setAttributeValue("defitem25", null);// �Զ�����25
	update_hvo.setAttributeValue("defitem26", null);// �Զ�����26
//	update_hvo.setAttributeValue("defitem27", headvo.getPlatedetail());// ��ҵ�����ϸ
	update_hvo.setAttributeValue("defitem28", null);// �Զ�����28
	update_hvo.setAttributeValue("defitem29", headvo.getOperatordept());// ���첿��
	update_hvo.setAttributeValue("defitem30", headvo.getOperator());// ������
	update_hvo.setAttributeValue("approver", null);// ������
	update_hvo.setAttributeValue("approvetime", null);// ����ʱ��
	update_hvo.setAttributeValue("printer", null);// ��ʽ��ӡ��
	update_hvo.setAttributeValue("printdate", null);// ��ʽ��ӡ����
//	update_hvo.setAttributeValue("creator",getUserPkByCode(headvo.getCreator()));// ������
//	save_headVO.setAttributeValue("creator","1001ZZ100000001MUNR7");
//	update_hvo.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
	update_hvo.setAttributeValue("modifier", null);// ����޸���
	update_hvo.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
	update_hvo.setAttributeValue("auditman", getcreator());// ������������
	update_hvo.setAttributeValue("redflag", null);// ����־
	update_hvo.setAttributeValue("imag_status", null);// Ӱ��״̬
	update_hvo.setAttributeValue("isneedimag", UFBoolean.FALSE);// ��ҪӰ��ɨ��
	update_hvo.setAttributeValue("isexpedited", UFBoolean.FALSE);// ����
	update_hvo.setAttributeValue("red_amount", null);// �����
	update_hvo.setAttributeValue("org_amount", null);// ��֯���
	if(headvo.getCurrency()!=null&&headvo.getCurrency()!=""){
		update_hvo.setAttributeValue("pk_currtype",headvo.getCurrency());//����
	}
	List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();
	int row=1;
	for (CostBodyVO costBodyVO : bodyVOs) {
		AccruedDetailVO save_bodyVO = new AccruedDetailVO();
//		checkBodyTransforExpense(costBodyVO);//��ֵ���
//		save_bodyVO.setAttributeValue("pk_accrued_detail", null);// ����
		save_bodyVO.setAttributeValue("pk_accrued_bill", update_hvo.getAttributeValue("pk_accrued_bill"));
		save_bodyVO.setAttributeValue("rowno", row);// �к�
		row++;
//		save_bodyVO.setAttributeValue("assume_org_name",costBodyVO.getAssumeorg_name());// ���óе���λ����
		save_bodyVO.setAttributeValue("assume_org",getPk_orgByCode(costBodyVO.getAssumeorg()));// ���óе���λ����
//		save_bodyVO.setAttributeValue("assume_dept_name",costBodyVO.getAssumedept_name());// ���óе���������
		String assume_dept=costBodyVO.getAssumedept();
		if(assume_dept==null ||assume_dept.length()<1){
			save_bodyVO.setAttributeValue("assume_dept",getassume_dept(getPk_orgByCode(costBodyVO.getAssumeorg())));// ���óе����ű���
		}else{
		save_bodyVO.setAttributeValue("assume_dept",assume_dept);// ���óе����ű���
		}		save_bodyVO.setAttributeValue("pk_iobsclass", null);// ��֧��Ŀ
		save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
		save_bodyVO.setAttributeValue("pk_resacostcenter", null);// �ɱ�����
		save_bodyVO.setAttributeValue("pk_checkele", null);// ����Ҫ��
		String sql="";
		
		save_bodyVO.setAttributeValue("pk_project",costBodyVO.getProject());// ��Ŀgetpk_projectByCode()
		save_bodyVO.setAttributeValue("pk_wbs", null);// ��Ŀ����
		save_bodyVO.setAttributeValue("pk_supplier", null);// ��Ӧ��
		save_bodyVO.setAttributeValue("pk_customer", null);// �ͻ�
		save_bodyVO.setAttributeValue("pk_proline", null);// ��Ʒ��
		save_bodyVO.setAttributeValue("pk_brand", null);// Ʒ��
		save_bodyVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
		save_bodyVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
		save_bodyVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
		save_bodyVO.setAttributeValue("amount",new UFDouble(costBodyVO.getAmount()));// ���
		save_bodyVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
		save_bodyVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
		save_bodyVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
		save_bodyVO.setAttributeValue("verify_amount", null);// �������
		save_bodyVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
		save_bodyVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
		save_bodyVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
		save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(costBodyVO.getAmount()));// Ԥ�����
		save_bodyVO.setAttributeValue("rest_amount", new UFDouble(costBodyVO.getAmount()));// ���
		save_bodyVO.setAttributeValue("org_rest_amount", null);// ��֯�������
		save_bodyVO.setAttributeValue("group_rest_amount", null);// ���ű������
		save_bodyVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
		save_bodyVO.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// ����¥��
//		save_bodyVO.setAttributeValue("defitem2", null);// �Զ�����2
//		if("262X-Cxx-FY01".equals(update_hvo.getPk_tradetype())&&Float.valueOf(costBodyVO.getAdvanceamount())!=null){
//			save_bodyVO.setAttributeValue("defitem2", costBodyVO.getAdvanceamount());// �Ƿ��Ԥ��
//		}
//		save_bodyVO.setAttributeValue("defitem3", null);// �Զ�����3
		if("262X-Cxx-FY01".equals(update_hvo.getPk_tradetype())){
			save_bodyVO.setAttributeValue("defitem7", costBodyVO.getAdvanceamount());// ��Ԥ����
		}
		save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// ��ֱ���
		save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// ˵��
//		save_bodyVO.setAttributeValue("defitem6", null);// �Զ�����6
//		save_bodyVO.setAttributeValue("defitem7", null);// �Զ�����7
		save_bodyVO.setAttributeValue("defitem8", costBodyVO.getBudgetyear());// Ԥ�����
		save_bodyVO.setAttributeValue("defitem9", null);// �Զ�����9
		save_bodyVO.setAttributeValue("defitem10", null);// �Զ�����10
		save_bodyVO.setAttributeValue("defitem11", null);// �Զ�����11
		save_bodyVO.setAttributeValue("defitem12",costBodyVO.getBudgetsubject());// Ԥ���Ŀ
//		save_bodyVO.setAttributeValue("defitem48",costBodyVO.getBudgetsubjectname());// Ԥ���Ŀȫ��
//		save_bodyVO.setAttributeValue("defitem12","1011001");// Ԥ���Ŀ
		save_bodyVO.setAttributeValue("defitem13", null);// �Զ�����13
		save_bodyVO.setAttributeValue("defitem14", null);// �Զ�����14
		save_bodyVO.setAttributeValue("defitem15", null);// �Զ�����15
		save_bodyVO.setAttributeValue("defitem16", null);// �Զ�����16
		save_bodyVO.setAttributeValue("defitem17", null);// �Զ�����17
		save_bodyVO.setAttributeValue("defitem18", null);// �Զ�����18
		save_bodyVO.setAttributeValue("defitem19", null);// �Զ�����19
		save_bodyVO.setAttributeValue("defitem20", null);// �Զ�����20
		save_bodyVO.setAttributeValue("defitem21", null);// �Զ�����21
//		save_bodyVO.setAttributeValue("defitem22",getdefdocBycode(costBodyVO.getBusinessformat(), "ys004"));// ҵ̬
		save_bodyVO.setAttributeValue("defitem22",costBodyVO.getBusinessformat());
		save_bodyVO.setAttributeValue("defitem23", null);// �Զ�����23
		save_bodyVO.setAttributeValue("defitem24", null);// �Զ�����24
		save_bodyVO.setAttributeValue("defitem25",getPsndocPkByCode(costBodyVO.getLender()));// �ÿ���
		save_bodyVO.setAttributeValue("defitem26",getcarnum(costBodyVO.getCardeptdoc()));// ���Ʋ��ŵ���
		save_bodyVO.setAttributeValue("defitem27", null);// �Զ�����27
		save_bodyVO.setAttributeValue("defitem28", null);// �Զ�����28
		save_bodyVO.setAttributeValue("defitem29",costBodyVO.getBudgetsubjectname());// �Զ�����29
		save_bodyVO.setAttributeValue("defitem30", null);// �Զ�����30
		save_bodyVO.setAttributeValue("src_accruedpk", null);// ��ԴԤ�ᵥpk
		save_bodyVO.setAttributeValue("srctype", null);// ��Դ��������
		save_bodyVO.setAttributeValue("src_detailpk", null);// ��ԴԤ��detailpk
		save_bodyVO.setAttributeValue("red_amount", null);// �����
		save_bodyVO.setAttributeValue("pk_accrued_bill", null);// ����Ԥ�ᵥ_����
		save_bodyVO.setAttributeValue("pk_project", costBodyVO.getProject());
		save_bodyVO.setStatus(VOStatus.NEW);
//		save_bodyVO.setAttributeValue("assume_dept","1001121000000007V38J");
//				save_bodyVO.setAttributeValue("assume_org","000112100000000024G6");
		save_bodyVOs.add(save_bodyVO);
	}
	update_hvo.setStatus(VOStatus.UPDATED);
	aggvo.setParentVO(update_hvo);
	aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
	resetCurrencyRate(aggvo.getParentVO());
	setHeadAmountByBodyAmounts(aggvo);
	List<AccruedDetailVO> update_bodyVOs = new ArrayList<>();
	update_bodyVOs.addAll(Arrays.asList(aggvo.getChildrenVO()));
	for(AccruedDetailVO bvo:bodyvos){
		bvo.setStatus(VOStatus.DELETED);
		bvo.setDr(1);
		update_bodyVOs.add(bvo);
	}
	aggvo.setChildrenVO(update_bodyVOs.toArray(new AccruedDetailVO[0]));
	resetBody(aggvo);
	return aggvo;
}
	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @throws Exception
	 */
	public AggAccruedBillVO onTranBill(CostHeadVO headvo,
			List<CostBodyVO> bodyVOs, String srctype) throws Exception {
		AggAccruedBillVO aggvo = new AggAccruedBillVO();
		AccruedVO save_headVO = new AccruedVO();
		
		checkHeadTransforExpense(headvo);//��ֵ���
		save_headVO.setAttributeValue("pk_accrued_bill", null);// ����
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ��������
		save_headVO.setAttributeValue("pk_org",
				"00011210000000000ZI0");// ������֯
		save_headVO.setAttributeValue("pk_billtype", "262X");// ��������
		if ("01".equals(srctype)) {// 1Ϊͨ�ú�ͬ
			save_headVO.setAttributeValue("pk_tradetype", "262X-Cxx-FY01");// �������ͱ��
		} else {
			save_headVO.setAttributeValue("pk_tradetype", "2621");// �������ͱ��
		}
//		save_headVO.setAttributeValue("pk_tradetypeid","00011210000000000OIW");
		if(StringUtils.isNotBlank(getBillTypePkByCode(save_headVO.getPk_tradetype(),save_headVO.getPk_group()))){
			save_headVO.setAttributeValue("pk_tradetypeid",getBillTypePkByCode(save_headVO.getPk_tradetype(),save_headVO.getPk_group()));// ��������
		}else{
			throw new BusinessException("�ý������ͱ��룺"+save_headVO.getPk_tradetype()+"δ����NC����������");
		}

		save_headVO.setAttributeValue("billno", null);// ���ݱ��
		save_headVO.setBilldate(new UFDate(headvo.getBilldate()));// ��������
//		save_headVO.setAttributeValue("billdate", );
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		if(headvo.getCurrency()!=null&&headvo.getCurrency()!=""){
			save_headVO.setAttributeValue("pk_currtype",headvo.getCurrency());
		}
		save_headVO.setAttributeValue("billstatus", 1);// ����״̬
		save_headVO.setAttributeValue("apprstatus", -1);// ����״̬
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
		save_headVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
		save_headVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
		save_headVO.setAttributeValue("amount", null);// ���
		save_headVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
		save_headVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
		save_headVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
		save_headVO.setAttributeValue("verify_amount", null);// �������
		save_headVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
		save_headVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
		save_headVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
		save_headVO.setAttributeValue("predict_rest_amount", null);// Ԥ�����
		save_headVO.setAttributeValue("rest_amount", null);// ���
		save_headVO.setAttributeValue("org_rest_amount", null);// ��֯�������
		save_headVO.setAttributeValue("group_rest_amount", null);// ���ű������
		save_headVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
		save_headVO.setAttributeValue("reason", headvo.getReason());// ����
		save_headVO.setAttributeValue("attach_amount", null);// ��������
		
		save_headVO.setAttributeValue("operator_org",
				getopertor_org());// �����˵�λ
		save_headVO.setAttributeValue("operator_dept",getOperator_dept());// �����˲���
		save_headVO.setAttributeValue("operator",getopertor());// ������
//		save_headVO.setAttributeValue("operator_org",
//				"000112100000000024G6");// �����˵�λ
//		save_headVO.setAttributeValue("operator_dept","1001121000000007V38J");
//		save_headVO.setAttributeValue("operator","1001ZZ100000001NHREE");
		save_headVO.setAttributeValue("defitem1", headvo.getEbsid());// EBSID
		save_headVO.setAttributeValue("defitem2", headvo.getEbsbillcode());// EBS����
		save_headVO.setAttributeValue("defitem3", null);// �Զ�����3
		save_headVO.setAttributeValue("defitem4", null);// �Զ�����4
		save_headVO.setAttributeValue("defitem5", null);// �Զ�����5
		save_headVO.setAttributeValue("defitem6",headvo.getPlate());// ���
		save_headVO.setAttributeValue("defitem7", headvo.getIspostcontract());// �Ƿ��ǿ����ͬ
		save_headVO.setAttributeValue("defitem8", headvo.getContractmoney());// ��ͬ��̬���
		save_headVO.setAttributeValue("defitem9", null);// �Զ�����9
		save_headVO.setAttributeValue("defitem10", null);// �Զ�����10
		save_headVO.setAttributeValue("defitem11", headvo.getContractcode());// ��ͬ����
		save_headVO.setAttributeValue("defitem12", headvo.getContractname());// ��ͬ����
		save_headVO.setAttributeValue("defitem13", headvo.getContractversion());// ��ͬ�汾
		save_headVO.setAttributeValue("defitem14", headvo.getBilltype());// ��������
		save_headVO.setAttributeValue("defitem15", null);// Ԥ������
		save_headVO.setAttributeValue("defitem16", null);// �Զ�����16
		save_headVO.setAttributeValue("defitem17", null);// �Զ�����17
		save_headVO.setAttributeValue("defitem18", null);// �Զ�����18
		save_headVO.setAttributeValue("defitem19", null);// �Զ�����19
		save_headVO.setAttributeValue("defitem20", null);// �Զ�����20
		save_headVO.setAttributeValue("defitem21", null);// �Զ�����21
		save_headVO.setAttributeValue("defitem22", null);// �Զ�����22
		save_headVO.setAttributeValue("defitem23", null);// �Զ�����23
		save_headVO.setAttributeValue("defitem24", null);// �Զ�����24
		save_headVO.setAttributeValue("defitem25", null);// �Զ�����25
		save_headVO.setAttributeValue("defitem26", null);// �Զ�����26
//		save_headVO.setAttributeValue("defitem27", headvo.getPlatedetail());// ��ҵ�����ϸ
		save_headVO.setAttributeValue("defitem28", null);// �Զ�����28
		save_headVO.setAttributeValue("defitem29", headvo.getOperatordept());// ���첿��
		save_headVO.setAttributeValue("defitem30", headvo.getOperator());// ������
		save_headVO.setAttributeValue("approver", null);// ������
		save_headVO.setAttributeValue("approvetime", null);// ����ʱ��
		save_headVO.setAttributeValue("printer", null);// ��ʽ��ӡ��
		save_headVO.setAttributeValue("printdate", null);// ��ʽ��ӡ����
//		save_headVO.setAttributeValue("creator",getUserPkByCode(headvo.getCreator()));// ������
		save_headVO.setAttributeValue("creator",getcreator());
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("modifier", null);// ����޸���
		save_headVO.setAttributeValue("modifiedtime", null);// ����޸�ʱ��
//		save_headVO.setAttributeValue("auditman", getUserPkByCode(headvo.getCreator()));// ������������
		save_headVO.setAttributeValue("auditman", getcreator());
		save_headVO.setAttributeValue("redflag", null);// ����־
		save_headVO.setAttributeValue("imag_status", null);// Ӱ��״̬
		save_headVO.setAttributeValue("isneedimag", UFBoolean.FALSE);// ��ҪӰ��ɨ��
		save_headVO.setAttributeValue("isexpedited", UFBoolean.FALSE);// ����
		save_headVO.setAttributeValue("red_amount", null);// �����
		save_headVO.setAttributeValue("org_amount", null);// ��֯���
//		save_headVO.setAttributeValue("pk_org", "000112100000000024G6");
//		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");
		List<AccruedDetailVO> save_bodyVOs = new ArrayList<>();
		int row=1;
		for (CostBodyVO costBodyVO : bodyVOs) {
			AccruedDetailVO save_bodyVO = new AccruedDetailVO();
			checkBodyTransforExpense(costBodyVO);//��ֵ���
			save_bodyVO.setAttributeValue("pk_accrued_detail", null);// ����
			save_bodyVO.setAttributeValue("rowno", row);// �к�
			row++;
//			save_bodyVO.setAttributeValue("assume_org_name",costBodyVO.getAssumeorg_name());// ���óе���λ����
			save_bodyVO.setAttributeValue("assume_org",getPk_orgByCode(costBodyVO.getAssumeorg()));// ���óе���λ����
//			save_bodyVO.setAttributeValue("assume_dept_name",costBodyVO.getAssumedept_name());// ���óе���������
			String assume_dept=costBodyVO.getAssumedept();
			if(assume_dept==null ||assume_dept.length()<1){
				save_bodyVO.setAttributeValue("assume_dept",getassume_dept(getPk_orgByCode(costBodyVO.getAssumeorg())));// ���óе����ű���
			}else{
			save_bodyVO.setAttributeValue("assume_dept",assume_dept);// ���óе����ű���
			}
			save_bodyVO.setAttributeValue("pk_iobsclass", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("pk_pcorg", null);// ��������
			save_bodyVO.setAttributeValue("pk_resacostcenter", null);// �ɱ�����
			save_bodyVO.setAttributeValue("pk_checkele", null);// ����Ҫ��
			save_bodyVO.setAttributeValue("pk_wbs", null);// ��Ŀ����
			save_bodyVO.setAttributeValue("pk_supplier", null);// ��Ӧ��
			save_bodyVO.setAttributeValue("pk_customer", null);// �ͻ�
			save_bodyVO.setAttributeValue("pk_proline", null);// ��Ʒ��
			save_bodyVO.setAttributeValue("pk_brand", null);// Ʒ��
			save_bodyVO.setAttributeValue("org_currinfo", null);// ��֯���һ���
			save_bodyVO.setAttributeValue("group_currinfo", null);// ���ű��һ���
			save_bodyVO.setAttributeValue("global_currinfo", null);// ȫ�ֱ��һ���
			save_bodyVO.setAttributeValue("amount",new UFDouble(costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_amount", null);// ��֯���ҽ��
			save_bodyVO.setAttributeValue("group_amount", null);// ���ű��ҽ��
			save_bodyVO.setAttributeValue("global_amount", null);// ȫ�ֱ��ҽ��
			save_bodyVO.setAttributeValue("verify_amount", null);// �������
			save_bodyVO.setAttributeValue("org_verify_amount", null);// ��֯���Һ������
			save_bodyVO.setAttributeValue("group_verify_amount", null);// ���ű��Һ������
			save_bodyVO.setAttributeValue("global_verify_amount", null);// ȫ�ֱ��Һ������
			save_bodyVO.setAttributeValue("predict_rest_amount", new UFDouble(costBodyVO.getAmount()));// Ԥ�����
			save_bodyVO.setAttributeValue("rest_amount", new UFDouble(costBodyVO.getAmount()));// ���
			save_bodyVO.setAttributeValue("org_rest_amount", null);// ��֯�������
			save_bodyVO.setAttributeValue("group_rest_amount", null);// ���ű������
			save_bodyVO.setAttributeValue("global_rest_amount", null);// ȫ�ֱ������
			save_bodyVO.setAttributeValue("defitem2", costBodyVO.getDeptfloor());// ����¥��
//			save_bodyVO.setAttributeValue("defitem2", null);// �Զ�����2
			if("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())&&Float.valueOf(costBodyVO.getAdvanceamount())!=null){
				save_bodyVO.setAttributeValue("defitem7", costBodyVO.getAdvanceamount());// �Ƿ��Ԥ��
			}
			save_bodyVO.setAttributeValue("defitem3", null);// �Զ�����3
			if("262X-Cxx-FY01".equals(save_headVO.getPk_tradetype())){
				save_bodyVO.setAttributeValue("defitem3", costBodyVO.getAdvanceamount());// ��Ԥ����
			}
			save_bodyVO.setAttributeValue("defitem4", costBodyVO.getScale());// ��ֱ���
			save_bodyVO.setAttributeValue("defitem5", costBodyVO.getExplain());// ˵��
			save_bodyVO.setAttributeValue("defitem6", null);// �Զ�����6
//			save_bodyVO.setAttributeValue("defitem7", null);// �Զ�����7
			save_bodyVO.setAttributeValue("defitem8", costBodyVO.getBudgetyear());// Ԥ�����
			save_bodyVO.setAttributeValue("defitem9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("defitem10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("defitem11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("defitem12",costBodyVO.getBudgetsubject());// Ԥ���Ŀ
			save_bodyVO.setAttributeValue("defitem48",costBodyVO.getBudgetsubjectname());// Ԥ���Ŀȫ��
//			save_bodyVO.setAttributeValue("defitem12","10011210000000002ALE");// Ԥ���Ŀ
			save_bodyVO.setAttributeValue("defitem13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("defitem14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("defitem15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("defitem16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("defitem17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("defitem18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("defitem19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("defitem20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("defitem21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("defitem22",costBodyVO.getBusinessformat());// ҵ̬
//			save_bodyVO.setAttributeValue("defitem22","10011210000000002BD7");
			save_bodyVO.setAttributeValue("defitem23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("defitem24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("defitem25",getPsndocPkByCode(costBodyVO.getLender()));// �ÿ���
			save_bodyVO.setAttributeValue("defitem26",getcarnum(costBodyVO.getCardeptdoc()));// ���Ʋ��ŵ���
			save_bodyVO.setAttributeValue("defitem27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("defitem28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("defitem29",costBodyVO.getBudgetsubjectname());// �Զ�����29
			save_bodyVO.setAttributeValue("defitem30",null);// �Զ�����30
			save_bodyVO.setAttributeValue("src_accruedpk", null);// ��ԴԤ�ᵥpk
			save_bodyVO.setAttributeValue("srctype", null);// ��Դ��������
			save_bodyVO.setAttributeValue("src_detailpk", null);// ��ԴԤ��detailpk
			save_bodyVO.setAttributeValue("red_amount", null);// �����
			save_bodyVO.setAttributeValue("pk_accrued_bill", null);// ����Ԥ�ᵥ_����
			save_bodyVO.setAttributeValue("pk_project", costBodyVO.getProject());
			save_bodyVO.setStatus(VOStatus.NEW);
//			save_bodyVO.setAttributeValue("assume_dept","1001121000000007V38J");
//					save_bodyVO.setAttributeValue("assume_org","000112100000000024G6");
			save_bodyVOs.add(save_bodyVO);
		}
		
		save_headVO.setStatus(VOStatus.NEW);
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(save_bodyVOs.toArray(new AccruedDetailVO[0]));
		resetCurrencyRate(aggvo.getParentVO());
		setHeadAmountByBodyAmounts(aggvo);
		resetBody(aggvo);
		return aggvo;
	}
	private IErmAccruedBillManage getAppService() {
		if (billManagerService == null) {
			billManagerService = NCLocator.getInstance().lookup(
					IErmAccruedBillManage.class);
		}
		return billManagerService;
	}

	/**
	 * ���ݱ���������ͷ�ܽ��������ܽ��
	 * 
	 * @param cardPanel
	 */
	public static UFDouble setHeadAmountByBodyAmounts(AggAccruedBillVO aggvo) {
		UFDouble newYbje = null;
		AccruedDetailVO[] items = (AccruedDetailVO[]) aggvo.getChildrenVO();

		int length = items.length;

		for (int i = 0; i < length; i++) {
			if (items[i].getAmount() != null) {// �������д��ڿ���ʱ��ԭ�ҽ��Ϊ�գ������������п�
				if (newYbje == null) {
					newYbje = items[i].getAmount();
				} else {
					newYbje = newYbje.add(items[i].getAmount());
				}
			}
		}

		aggvo.getParentVO().setAmount(newYbje);
       aggvo.getParentVO().setPredict_rest_amount(newYbje);
       aggvo.getParentVO().setOrg_rest_amount(newYbje);
       aggvo.getParentVO().setOrg_verify_amount(newYbje);
       aggvo.getParentVO().setRest_amount(newYbje);
       aggvo.getParentVO().setOrg_amount(newYbje);
		return newYbje;
	}
 public String getPk_group(){
	 return AppContext.getInstance().getPkGroup();
 }
	/**
	 * ���ݱ������ݽ��м���
	 * 
	 * @param aggvo
	 * @throws Exception
	 */
	public void resetBody(AggAccruedBillVO aggvo) throws Exception {
		AccruedVO headvo = aggvo.getParentVO();
		String headPk_org = headvo.getPk_org();
		AccruedDetailVO[] bodyVOs = aggvo.getChildrenVO();

		for (int i = 0; i < bodyVOs.length; i++) {
			AccruedDetailVO accruedDetailVO = bodyVOs[i];
			String assume_org = accruedDetailVO.getAssume_org();
			String pk_currtype = headvo.getPk_currtype();// ����
			UFDate date = headvo.getBilldate();// ��������
			if (headPk_org == null || assume_org == null || pk_currtype == null
					|| date == null) {
				return;
			}

			try {
				// �������
				String headOrgCurrPk = Currency.getOrgLocalCurrPK(headPk_org);
				String assume_orgCurrPk = Currency
						.getOrgLocalCurrPK(assume_org);// ������ͬʱ��ȡ��ͷ����
				if (headPk_org.equals(assume_org)
						|| (headOrgCurrPk != null && assume_orgCurrPk != null && assume_orgCurrPk
								.equals(headOrgCurrPk))) {

					accruedDetailVO.setOrg_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.ORG_CURRINFO));
					accruedDetailVO.setGroup_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GROUP_CURRINFO));
					accruedDetailVO.setGlobal_currinfo(getHeadUFDoubleValue(
							headvo, AccruedVO.GLOBAL_CURRINFO));
				} else {
					// ����(���ң����ű��ң�ȫ�ֱ��һ���)
					UFDouble orgRate = Currency.getRate(assume_org,
							pk_currtype, date);
					UFDouble groupRate = Currency.getGroupRate(assume_org,
							getPk_group(), pk_currtype, date);
					UFDouble globalRate = Currency.getGlobalRate(assume_org,
							pk_currtype, date);

					accruedDetailVO.setOrg_currinfo(orgRate);
					accruedDetailVO.setGroup_currinfo(groupRate);
					accruedDetailVO.setGlobal_currinfo(globalRate);
				}

				Object amount = accruedDetailVO.getAmount();
				accruedDetailVO.setRest_amount(accruedDetailVO.getAmount());
				accruedDetailVO.setPredict_rest_amount(accruedDetailVO
						.getAmount());

				// ������
				UFDouble ori_amount = accruedDetailVO.getAmount();// ���þ���

				accruedDetailVO.setVerify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setOrg_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGroup_verify_amount(UFDouble.ZERO_DBL);
				accruedDetailVO.setGlobal_verify_amount(UFDouble.ZERO_DBL);

				String pk_org = accruedDetailVO.getAssume_org();

				// ����
				String pk_group = (String) headvo
						.getAttributeValue(AccruedVO.PK_GROUP);

				// ��ȡ������(�ܸ��ݱ�����õ�λ)������屾�ҽ��
				UFDouble hl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.ORG_CURRINFO);
				UFDouble grouphl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GROUP_CURRINFO);
				UFDouble globalhl = (UFDouble) accruedDetailVO
						.getAttributeValue(AccruedDetailVO.GLOBAL_CURRINFO);

				UFDouble[] bbje = null;
				// ��֯���ҽ��
				bbje = Currency.computeYFB(pk_org, Currency.Change_YBCurr,
						pk_currtype, ori_amount, null, null, null, hl,
						AppContext.getInstance().getServerTime().getDate());

				accruedDetailVO.setAttributeValue(AccruedDetailVO.ORG_AMOUNT,
						bbje[2]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.ORG_REST_AMOUNT, bbje[2]);
				// ���š�ȫ�ֽ��
				UFDouble[] money = null;
				if (bbje == null || bbje[2] == null) {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							UFDouble.ZERO_DBL, pk_currtype,
							AppContext.getInstance().getServerTime().getDate(), pk_org, pk_group, globalhl,
							grouphl);

				} else {
					money = Currency.computeGroupGlobalAmount(ori_amount,
							bbje[2], pk_currtype, AppContext.getInstance().getServerTime().getDate(),
							pk_org, pk_group, globalhl, grouphl);
				}

				accruedDetailVO.setAttributeValue(AccruedDetailVO.GROUP_AMOUNT,
						money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GROUP_REST_AMOUNT, money[0]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_AMOUNT, money[1]);
				accruedDetailVO.setAttributeValue(
						AccruedDetailVO.GLOBAL_REST_AMOUNT, money[1]);
			} catch (Exception e) {
				throw new Exception(e.getMessage());
			}
		}
	}

	public UFDouble getHeadUFDoubleValue(AccruedVO headvo, String key) {
		if (headvo.getAttributeValue(key) == null) {
			return UFDouble.ZERO_DBL;
		}
		return (UFDouble) headvo.getAttributeValue(key);
	}

	/**
	 * ���û���
	 * 
	 * @param pk_org
	 *            ��֯pk
	 * @param pk_currtype
	 *            ԭ�ұ���
	 * @param date
	 *            �Ƶ�ʱ��
	 * @throws Exception
	 */
	public void resetCurrencyRate(AccruedVO headvo) throws Exception {
		String pk_org = (String) headvo.getAttributeValue(AccruedVO.PK_ORG);// ��֯
		String pk_currtype = (String) headvo
				.getAttributeValue(AccruedVO.PK_CURRTYPE);// ����
		UFDate date = (UFDate) headvo.getAttributeValue(AccruedVO.BILLDATE);// ��������

		try {
			// ����(���ң����ű��ң�ȫ�ֱ��һ���)
			UFDouble orgRate = Currency.getRate(pk_org, pk_currtype, date);
			UFDouble groupRate = Currency.getGroupRate(pk_org,
					headvo.getPk_group(), pk_currtype, date);
			UFDouble globalRate = Currency.getGlobalRate(pk_org, pk_currtype,
					date);

			headvo.setAttributeValue(AccruedVO.ORG_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GROUP_CURRINFO, orgRate);
			headvo.setAttributeValue(AccruedVO.GLOBAL_CURRINFO, orgRate);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}
	
	private void checkHeadTransforExpense(CostHeadVO headvo) throws BusinessException{
		if (StringUtils.isBlank(headvo.getOrg())) {throw new BusinessException("���˹�˾����Ϊ��");}
		if (StringUtils.isBlank(headvo.getEbsid())) {throw new BusinessException("EBS��������Ϊ��");}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {throw new BusinessException("ebs���Ų���Ϊ��");}
		if (StringUtils.isBlank(Float.valueOf(headvo.getAmount()).toString())) {throw new BusinessException("����Ϊ��");}
//		if (StringUtils.isBlank(headvo.getOperatororg())) {throw new BusinessException("�����˵�λ����Ϊ��");}
//		if (StringUtils.isBlank(headvo.getOperator())) {throw new BusinessException("�����˲���Ϊ��");}
//		if (StringUtils.isBlank(headvo.getOperatordept())) {throw new BusinessException("�����˲��Ų���Ϊ��");}
//		if (StringUtils.isBlank(headvo.getReason())) {throw new BusinessException("���ɲ���Ϊ��");}
//		if (StringUtils.isBlank(headvo.getCreator())) {throw new BusinessException("�Ƶ��˲���Ϊ��");}
	}
	
	private void checkBodyTransforExpense(CostBodyVO bodyvo) throws BusinessException{
		if (StringUtils.isBlank(bodyvo.getAssumeorg())) {throw new BusinessException("���óе���λ����Ϊ��");}
//		if (StringUtils.isBlank(bodyvo.getAssumedept())) {throw new BusinessException("���óе����Ų���Ϊ��");}
		if (StringUtils.isBlank(Float.valueOf(bodyvo.getAmount()).toString())) {throw new BusinessException("����Ϊ��");}
	}

}
