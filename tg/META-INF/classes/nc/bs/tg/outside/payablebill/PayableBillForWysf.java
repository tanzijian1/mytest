package nc.bs.tg.outside.payablebill;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.core.service.TimeService;
import nc.bs.pf.pub.PfDataCache;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.billstatus.ARAPBillStatus;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.arap.pub.BillEnumCollection.ApproveStatus;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.billtype.BilltypeVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.appaybill.PayBillItemVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * wysfϵͳ����Ӧ�������ݵ�NC����ϵͳ�ӿ�
 * 
 * @author lhh
 * 
 */
public class PayableBillForWysf extends PayableBillUtilsForWy {
	public static final String DefaultOperator = "LLWYSF";// Ĭ���Ƶ���
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);
		// ��ϵͳ��Ϣ
		JSONObject data = (JSONObject) info.get("data");// ��ϵͳ��Դ��ͷ����
		JSONObject jsonhead = (JSONObject) data.get("headInfo");// ��ϵͳ��Դ��ͷ����
		String srcid = jsonhead.getString("srcid");// ��ԴID
		String srcno = jsonhead.getString("srcbillno");// ��Դ���ݺ�
		Map<String, String> resultInfo = new HashMap<String, String>();
		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
					AggPayableBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			AggPayableBillVO billvo = onTranBill(info);
			if(billvo.getChildrenVO().length==1)
				billvo.getParentVO().setAttributeValue("def17", billvo.getChildrenVO()[0].getAttributeValue("contractno"));
			
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVE", "F1", null,
					billvo, null, null);
			AggPayableBillVO[] billvos = (AggPayableBillVO[]) obj;
			// WorkflownoteVO noteVO =
			// getWorkflowMachine().checkWorkflowActions(
			// "F1", billvos[0].getPrimaryKey());
			// getPfBusiAction().processAction("SAVE", "F1", noteVO, billvos[0],
			// null, null);
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}
	@Override
	protected AggPayableBillVO onDefaultValue(JSONObject head,
			JSONArray bodylist) throws BusinessException {

		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();
		String billdate = head.getString("billdate") == null ? new UFDate()
				.toString() : head.getString("billdate");
		UFDateTime currTime = TimeService.getInstance().getUFDateTime();// ��ǰʱ��
		hvo.setAttributeValue(PayableBillVO.PK_GROUP, InvocationInfoProxy
				.getInstance().getGroupId());// ����
		hvo.setAttributeValue(PayableBillVO.PK_ORG, DocInfoQryUtils.getUtils()
				.getOrgInfo(head.getString("pk_org")).get("pk_org"));// Ӧ��������֯->NCҵ��Ԫ����
		hvo.setAttributeValue(PayableBillVO.BILLMAKER,
				getUserIDByCode(DefaultOperator));// �Ƶ���
		hvo.setAttributeValue(PayableBillVO.CREATOR, hvo.getBillmaker());// ������
		hvo.setAttributeValue(PayableBillVO.CREATIONTIME, currTime);// ����ʱ��
		hvo.setAttributeValue(PayableBillVO.PK_BILLTYPE, IBillFieldGet.F1);// �������ͱ���
		hvo.setAttributeValue(PayableBillVO.BILLCLASS, IBillFieldGet.YF);// ���ݴ���
		hvo.setAttributeValue(PayableBillVO.SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ��������ϵͳ
		hvo.setAttributeValue(PayableBillVO.SRC_SYSCODE,
				BillEnumCollection.FromSystem.AP.VALUE);// ������Դϵͳ
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE, IBillFieldGet.D1);// Ӧ������code
		hvo.setAttributeValue(PayableBillVO.BILLSTATUS,
				ARAPBillStatus.SAVE.VALUE);// ����״̬
		// ��������
		BilltypeVO billTypeVo = PfDataCache.getBillType(getTradetype());

		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPEID,
				billTypeVo.getPk_billtypeid());// Ӧ������
		hvo.setAttributeValue(PayableBillVO.PK_TRADETYPE,
				billTypeVo.getPk_billtypecode());// Ӧ������
		hvo.setAttributeValue(PayableBillVO.ISINIT, UFBoolean.FALSE);// �ڳ���־
		hvo.setAttributeValue(PayableBillVO.ISREDED, UFBoolean.FALSE);// �Ƿ����
		hvo.setAttributeValue(PayableBillVO.BILLDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(PayableBillVO.BUSIDATE, new UFDate(billdate));// ��������
		hvo.setAttributeValue(ReceivableBillVO.BILLYEAR,
				String.valueOf(new UFDate(billdate).getYear()));// ���ݻ�����
		hvo.setAttributeValue(ReceivableBillVO.BILLPERIOD,
				new UFDate(billdate).getStrMonth());// ���ݻ���ڼ�
		hvo.setApprovestatus(ApproveStatus.NOSTATE.VALUE);
		setHeaderVO(hvo, head);

		aggvo.setParentVO(hvo);
		PayableBillItemVO[] itemVOs = new PayableBillItemVO[bodylist.size()];
		for (int i$ = 0; i$ < bodylist.size(); i$++) {
			itemVOs[i$] = new PayableBillItemVO();
			itemVOs[i$].setAttributeValue(IBillFieldGet.ROWNO, new Integer(i$));// �к�
			itemVOs[i$].setAttributeValue(IBillFieldGet.SETTLECURR, null);
			itemVOs[i$].setAttributeValue(IBillFieldGet.PK_CURRTYPE, null);
		}
		aggvo.setChildrenVO(itemVOs);
		getArapBillPubQueryService().getDefaultVO(aggvo, true);

		return aggvo;
	
	}
	/**
	 * ����-��Ӧ�̺�ͬ\�Ǻ�ͬ���
	 */
	@Override
	protected String getTradetype() {
		// TODO �Զ����ɵķ������
		return "F1-Cxx-LL05";
	}

	/**
	 * �����ͷ��¼��Ϣ
	 * 
	 * @param headvo
	 * @throws BusinessException
	 */
	protected void checkHeaderNotNull(JSONObject head) throws BusinessException {
		if (StringUtils.isBlank(head.getString("pk_org"))) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcid"))) {
			throw new BusinessException("WYSF��������Ϊ��");
		}
		if (StringUtils.isBlank(head.getString("srcbillno"))) {
			throw new BusinessException("WYSF���ݺŲ���Ϊ��");
		}
//		if (StringUtils.isBlank(head.getString("contcode"))) {
//			throw new BusinessException("��ͬ���벻��Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("contname"))) {
//			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("conttype"))) {
//			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("plate"))) {
//			throw new BusinessException("��鲻��Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("supplier"))) {
//			throw new BusinessException("��Ӧ�̲���Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("proejctdata"))) {
//			throw new BusinessException("��Ŀ����Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("proejctstages"))) {
//			throw new BusinessException("��Ŀ���ڲ���Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
//			throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
//		}
//		if (StringUtils.isBlank(head.getString("auditstate"))) {
//			throw new BusinessException("������������״̬����Ϊ��");
//		}
		 if (StringUtils.isBlank(head.getString("billdate"))) {
		 throw new BusinessException("�������ڲ���Ϊ��");
		 }

	}


	@Override
	protected void setHeaderVO(PayableBillVO hvo, JSONObject head)
			throws BusinessException {

		checkHeaderNotNull(head);
		// bpmid
		hvo.setAttributeValue("bpmid", head.getString("bpmid"));
		hvo.setAttributeValue("def1", head.getString("srcid"));// �Զ�����1 ��ϵͳ����
		hvo.setAttributeValue("def2", head.getString("srcbillno"));// �Զ�����2
																	// ��ϵͳ���ݺ�->���������
		hvo.setAttributeValue("def3", head.getString("imgcode"));// �Զ�����3
																// Ӱ�����->Ӱ�����
		hvo.setAttributeValue("def4", head.getString("imgstate"));// �Զ�����4
																	// Ӱ��״̬->Ӱ��״̬
//		hvo.setAttributeValue("def5", head.getString("contcode"));// �Զ�����5
																	// ��ͬ����->��ͬ����
//		hvo.setAttributeValue("def6", head.getString("contname"));// ��ͬ����->��ͬ����
																	// def6
//		hvo.setAttributeValue("def7", head.getString("conttype"));// �Զ�����7
//		String itemtype = head.getString("itemtype");
//		if(StringUtils.isNotBlank(itemtype)){
//			Map<String,String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if(itemtypeInfo==null){
//				throw new BusinessException("ҵ�����͡�" + itemtype
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def37",itemtypeInfo.get("pk_obj"));// �շ���Ŀ����
//		}
//		String itemname = head.getString("itemname");
//		if(StringUtils.isNotBlank(itemname)){
//			Map<String,String> itemnameInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo( itemname);
//			if(itemnameInfo==null){
//				throw new BusinessException("ҵ�����͡�" + itemname
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def38",itemnameInfo.get("pk_obj"));// �շ���Ŀ����
//		}
//		
		
		String businesstype = head.getString("businesstype");
		if(StringUtils.isNotBlank(businesstype)){
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(businesstype, "SDLL003");
			if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
				throw new BusinessException("ҵ�����͡�" + businesstype
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def39", head.getString("businesstype"));// ҵ������
		}
		
		
		hvo.setAttributeValue("def41", head.getString("mailbox"));// ����
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -start
		 */
//	
		/**
		 * �ɱ�Ӧ�����ӿڣ��Զ����ݺ�ͬ���ʹ���Ӧ������ͷ������Ʊ�����͡���NC�ֶ�-def56��,���յ���zdy008-2020-05-08-̸�ӽ�
		 * -end
		 */
		// ��ͬϸ��->��ͬϸ��
		// ��ͬ����->��ͬ����
//		hvo.setAttributeValue("def8", head.getString("contcell"));// �Զ�����8
																	// ��ͬϸ��->��ͬϸ��
		// �����̶�->�����̶�2020-02-18-tzj
//		String emergency = head.getString("mergency");
//		Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
//				.getDefdocInfo(emergency, "zdy031");
//		if (emergencyInfo != null) {
//			hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));// �Զ�����9
//		}
//		hvo.setAttributeValue("def10", "");// �Զ�����10 ��Դ�ⲿϵͳ

//		if (StringUtils.isNotBlank(head.getString("plate"))) {
//			Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(head.getString("plate"), "bkxx");
//			if (plateInfo == null) {
//				throw new BusinessException("��顾" + head.getString("plate")
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// �Զ�����15
//																		// ���
//		}
//		if (StringUtils.isNotBlank(head.getString("accorg"))) {
//			Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
//					.getOrgInfo(head.getString("accorg"));
//			if (accorgInfo == null) {
//				throw new BusinessException("���˹�˾��" + head.getString("accorg")
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// �Զ�����17
//			// ���˹�˾->NCҵ��Ԫ����
//		}
	

		// hvo.setPk_busitype(null);//��������
		hvo.setAttributeValue(PayableBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

//		if (StringUtils.isNotBlank(head.getString("proejctdata"))) {
//			HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
//					.getSpecialProjectInfo(head.getString("proejctdata"));
//			if (projectInfo == null) {
//				throw new BusinessException("��Ŀ��"
//						+ head.getString("proejctdata") + "��δ����NC���������������Ϣ!");
//
//			}
////			hvo.setAttributeValue("def19", projectInfo.get("pk_project"));// ��Ŀ
			// Ӧ�����ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
//			hvo.setAttributeValue("def61", projectInfo.get("def8"));// �Ƿ��ʱ�
//			hvo.setAttributeValue("def62", projectInfo.get("def9"));// ��Ŀ����

//		}

//		if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
//			HashMap<String, String> projectstagesInfo = DocInfoQryUtils
//					.getUtils().getSpecialProjectInfo(
//							head.getString("proejctstages"));
//			if (projectstagesInfo == null) {
//				throw new BusinessException("��Ŀ���� ��"
//						+ head.getString("proejctstages") + "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def32", projectstagesInfo.get("pk_project"));// �Զ�����32
//																				// ��Ŀ����
//		}
		hvo.setAttributeValue("money",
				head.getString("money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("money")));// ����Ʊ�ݽ��
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// �ۼ������
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// �ۼƸ�����
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// �Ƿ��ȸ����Ʊ
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// ��������ۼƸ�����
//		hvo.setAttributeValue("def26",
//				head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
//						: new UFDouble(head.getString("totalmny_inv")));// �ۼƷ�Ʊ���
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// �Ƿ����ʱ���۳�
//		hvo.setAttributeValue("def31", head.getString("note"));// ˵��
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// ������������״̬
//		if (StringUtils.isNotBlank(head.getString("auditstate"))) {
//			Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(head.getString("auditstate"), "zdy032");
//			if (auditstateInfo == null) {
//				throw new BusinessException("������������״̬ ��"
//						+ head.getString("auditstate") + "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));// �Զ�����32
//																			// ��Ŀ����
//		}
//		hvo.setAttributeValue("def43", head.getString("def43"));// �Ǳ�׼ָ�����def43
//		hvo.setAttributeValue("def44", head.getString("def44"));// ������def44
//		hvo.setAttributeValue("def45", head.getString("def45"));// �Ѳ�ȫdef45
//		hvo.setAttributeValue("def46", head.getString("dept"));// ���첿��
//		hvo.setAttributeValue("def47", head.getString("psndoc"));// ������
//		hvo.setAttributeValue("def50", "N");// Ʊ��Ȩ������
//		hvo.setAttributeValue("def55", head.getString("def55"));// EBS��ʽ-def55

//		if (StringUtils.isNotBlank(head.getString("signorg"))) {
//			Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
//					.getOrgInfo(head.getString("signorg"));
//			if (signOrgInfo == null) {
//				throw new BusinessException("ǩԼ��˾��" + head.getString("signorg")
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// ǩԼ��˾
//		}
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-05-26-̸�ӽ�
		 */
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("pk_balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ
		hvo.setAttributeValue(PayableBillVO.RATE, new UFDouble(1));

	}

	@Override
	protected void setItemVO(PayableBillVO headVO, PayableBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayableBillItemVO.PK_GROUP,
				headVO.getPk_group());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		itemVO.setAttributeValue(PayableBillItemVO.BILLDATE,
				headVO.getBilldate());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.BUSIDATE,
				headVO.getBusidate());// ��������
		itemVO.setAttributeValue(PayableBillItemVO.PK_DEPTID,
				headVO.getPk_deptid());// ����
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// �� ��
		itemVO.setAttributeValue(PayableBillItemVO.PK_PSNDOC,
				headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(PayableBillItemVO.OBJTYPE, headVO.getObjtype());// ��������

		itemVO.setAttributeValue(PayableBillItemVO.DIRECTION,
				BillEnumCollection.Direction.CREDIT.VALUE);// ����

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayableBillItemVO.PAUSETRANSACT,
				UFBoolean.FALSE);// �����־
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// ��������
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// ����ó��
		itemVO.setAttributeValue(PayableBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// ����
		itemVO.setAttributeValue(PayableBillItemVO.RATE, headVO.getRate());// ��֯���һ���
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// �ջ���

		itemVO.setAttributeValue(PayableBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// ˰��
		itemVO.setAttributeValue(PayableBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_CR, body
				.getString("local_money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_money_cr")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�->����ƻ���ϸ���
//		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR,
//				itemVO.getMoney_cr());// ���ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_CR, body
				.getString("local_notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_cr")));// // ��֯������˰���
		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_TAX_CR, body
				.getString("local_tax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_cr")));// ˰��->����ƻ���ϸ����˰���*˰��
		itemVO.setAttributeValue(PayableBillItemVO.NOTAX_CR,  body
				.getString("notax_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_cr")));//�跽ԭ����˰���
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_CR,  body
				.getString("money_cr") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_cr")));//�跽ԭ�ҽ��
		itemVO.setAttributeValue(PayableBillItemVO.MONEY_BAL,  body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));//�跽ԭ�����
		itemVO.setAttributeValue("memo",
				body.getString("memo"));// ��ע
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_BAL,  itemVO.getMoney_bal());
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_MONEY_DE,  itemVO.getLocal_money_cr());
//		itemVO.setAttributeValue(PayableBillItemVO.LOCAL_NOTAX_DE,  itemVO.getLocal_notax_cr());
		itemVO.setAttributeValue(PayableBillItemVO.CONTRACTNO,  body.getString("contractno"));//��ͬ����
		itemVO.setAttributeValue(PayableBillItemVO.INVOICENO,  body.getString("invoiceno"));//��Ʊ����
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,  body.getString("contractname"));//��ͬ����
		itemVO.setRate(body
				.getString("rate") == null ? UFDouble.ONE_DBL
				: new UFDouble(body.getString("rate")));//����
		if(StringUtils.isNotBlank(body.getString("productline"))){
			itemVO.setAttributeValue(PayableBillItemVO.PRODUCTLINE,  DocInfoQryUtils.getUtils().saveProdlineByname(body.getString("productline")));//��Ʒ��
			}
		String itemname = body.getString("itemname");
		if(StringUtils.isNotBlank(itemname)){
			Map<String,String> itemnameInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo( itemname);
			if(itemnameInfo==null){
				throw new BusinessException("ҵ�����͡�" + itemname
						+ "��δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF31,itemnameInfo.get("pk_obj"));// �շ���Ŀ����
			itemVO.setAttributeValue(PayableBillItemVO.DEF29,itemnameInfo.get("pk_parent"));// �շ���Ŀ����
		}
		
		
		String projectphase = body.getString("projectphase");
		if(StringUtils.isNotBlank(projectphase)){
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(projectphase, "SDLL006");
			if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
				throw new BusinessException("ҵ�����͡�" + projectphase
						+ "��δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.DEF32,  businesstypeInfo.get("pk_defdoc"));//��Ŀ�׶�
		}
		// ������Ӧ��2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(), headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + body.getString("supplier")
					+ "��δ����NC������ѯ�������Ϣ");
		}
		itemVO.setAttributeValue("supplier", pk_supplier);// ��Ӧ��
		itemVO.setAttributeValue(PayableBillItemVO.SCOMMENT,
				body.getString("scomment"));// ժҪ
		
		itemVO.setAttributeValue(PayableBillItemVO.DEF24,
				body.getString("rowid"));// ��id
		itemVO.setAttributeValue(PayableBillItemVO.DEF28,
				body.getString("contractname"));// ��ͬ����
		itemVO.setAttributeValue(PayableBillItemVO.QUANTITY_CR,
				body.getString("quantity_cr")==null ? UFDouble.ZERO_DBL:new UFDouble(body.getString("quantity_cr")));//����
		// �տ������˻�
		if(StringUtils.isNotBlank(body.getString("recaccount"))){
			Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
					.getBankaccnumInfo(itemVO.getPk_org(),
							body.getString("recaccount"));
			if (recaccountInfo == null) {
				throw new BusinessException("�տ������˻�["
						+ body.getString("recaccount") + "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
					recaccountInfo.get("pk_bankaccsub"));// ���������˻�
		}
		

		// �������˻�
		if(StringUtils.isNotBlank(body.getString("payaccount"))){
		Map<String, String> payaccountInfo = DocInfoQryUtils.getUtils()
				.getBankaccnumInfo(itemVO.getPk_org(),
						body.getString("payaccount"));
		if (payaccountInfo == null) {
			throw new BusinessException("���������˻�["
					+ body.getString("payaccount") + "]δ����NC���������������Ϣ!");
		}
		itemVO.setAttributeValue(PayableBillItemVO.PAYACCOUNT,
				payaccountInfo.get("pk_bankaccsub"));
		}
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("�Ƿ�����Ƿ�ѡ�" + body.getString("arrears")
						+ "��δ����NC������ѯ�������Ϣ");
			}
			itemVO.setAttributeValue("def37",
					detdocMap.get("pk_defdoc"));// �Ƿ�����Ƿ��
		}
		 if (StringUtils.isNotBlank(body.getString("project"))) {
			 HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
			 .getSpecialProjectInfo(body.getString("project"));
			 if (projectInfo == null) {
			 throw new BusinessException("��Ŀ��"
			 + body.getString("project") + "��δ����NC���������������Ϣ!");
			
			 }
			 itemVO.setProject(projectInfo.get("pk_project"));
		 }
		 String businessbreakdown = body.getString("businessbreakdown");
			if(StringUtils.isNotBlank(businessbreakdown)){
				Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
						.getDefdocInfo(businessbreakdown, "SDLL004");
				if(businesstypeInfo==null||businesstypeInfo.get("pk_defdoc")==null){
					throw new BusinessException("ҵ��ϸ�ࡾ" + businessbreakdown
							+ "��δ����NC���������������Ϣ!");
				}
				itemVO.setAttributeValue("def38", businessbreakdown);//ҵ��ϸ��
			}
			
//		if (StringUtils.isNotBlank(body.getString("subjcode"))) {
//			String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
//					body.getString("subjcode"), headVO.getPk_org());
//			if (pk_subjcode == null) {
//				throw new BusinessException("��ƿ�Ŀ["
//						+ body.getString("subjcode") + "]δ����NC���������������Ϣ!");
//			}
//			itemVO.setAttributeValue(PayableBillItemVO.SUBJCODE, pk_subjcode);// ��Ŀ����
//																				// ��ƿ�Ŀ
//		}
//		if (StringUtils.isNotBlank(body.getString("costsubject"))) {
//			Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(body.getString("costsubject"), "zdy024");
//			if (costsubjectInfo == null) {
//				throw new BusinessException("�ɱ���Ŀ["
//						+ body.getString("costsubject") + "]δ����NC����������!");
//			}
//			itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));// �ɱ���Ŀ
//
//		}

//		if (StringUtils.isNotBlank(body.getString("invtype"))) {
//			Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
//					.getDefdocInfo(body.getString("invtype"), "pjlx");
//			if (invtypetInfo == null) {
//				throw new BusinessException("Ʊ��[" + body.getString("invtype")
//						+ "]δ����NC����������!");
//			}
//			itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
//		}
//		// ����ҵ̬ def3
//		itemVO.setAttributeValue("def3", body.getString("format"));
//		// ���ñ��� def4
//		UFDouble formatratio = body.getString("formatratio") == null ? UFDouble.ZERO_DBL
//				: new UFDouble(body.getString("formatratio"));
//		itemVO.setAttributeValue("def4", formatratio.multiply(100).toString());
//		// ����Ʊ������
//		itemVO.setAttributeValue("def9", "100112100000000069WH");
//		// �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
//		String deductibleTax = getDeductibleTax(body);
//		itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("�Ƿ�����Ƿ�ѡ�" + body.getString("arrears")
						+ "��δ����NC������ѯ�������Ϣ");
			}
			itemVO.setAttributeValue("",
					detdocMap.get("pk_defdoc"));// �Ƿ�����Ƿ��
		}

	}

	/**
	 * ��������Ϣ��¼
	 * 
	 * @param body
	 * @throws BusinessException
	 */
	private void checkItemNotNull(JSONObject body) throws BusinessException {
		 if (StringUtils.isBlank(body.getString("supplier"))) {
		 throw new BusinessException("��Ӧ�̲���Ϊ��");
		 }
		 
		 if (StringUtils.isBlank(body.getString("notax_cr"))) {
		 throw new BusinessException("�跽ԭ����˰����Ϊ��");
		 }
		 if(new UFDouble(body.getString("notax_cr")).doubleValue()<0){
			 throw new BusinessException("�跽ԭ����˰����С��0");
		 }
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
//		if (StringUtils.isBlank(body.getString("local_notax_cr"))) {
//			throw new BusinessException("��˰����Ϊ��");
//		}
//		if(new UFDouble(body.getString("local_notax_cr")).doubleValue()<0){
//			 throw new BusinessException("��˰����С��0");
//		 }
		if (StringUtils.isBlank(body.getString("local_tax_cr"))) {
			throw new BusinessException("˰���Ϊ��");
		}
		if(new UFDouble(body.getString("local_tax_cr")).doubleValue()<0){
			 throw new BusinessException("˰���С��0");
		 }
//		if (StringUtils.isBlank(body.getString("local_money_cr"))) {
//			throw new BusinessException("��˰�ϼƲ���Ϊ��");
//		}
//		if(new UFDouble(body.getString("local_money_cr")).doubleValue()<0){
//			 throw new BusinessException("��˰�ϼƲ���С��0");
//		 }
		 if (StringUtils.isBlank(body.getString("money_cr"))) {
		 throw new BusinessException("�跽ԭ�ҽ���Ϊ��");
		 }
		 if(new UFDouble(body.getString("money_cr")).doubleValue()<0){
			 throw new BusinessException("�跽ԭ�ҽ���С��0");
		 }
		 if (StringUtils.isBlank(body.getString("money_bal"))) {
		 throw new BusinessException("ԭ������Ϊ��");
		 }
		 if(new UFDouble(body.getString("money_bal")).doubleValue()<0){
			 throw new BusinessException("ԭ������С��0");
		 }
		 if (StringUtils.isBlank(body.getString("rowid"))) {
		 throw new BusinessException("��ҵ�շ�ϵͳ������ID����Ϊ��");
		 }
		 if (StringUtils.isBlank(body.getString("productline"))) {
		 throw new BusinessException("�����벻��Ϊ��");
		 }
//		if (StringUtils.isBlank(body.getString("costsubject"))) {
//			throw new BusinessException("�ɱ���Ŀ����Ϊ��");
//		}
//		if (StringUtils.isBlank(body.getString("invtype"))) {
//			throw new BusinessException("Ʊ������(Ʊ��)����Ϊ��");
//		}
		// if (StringUtils.isBlank(body.getString("scomment"))) {
		// throw new BusinessException("ժҪ����Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("supplier"))) {
		// throw new BusinessException("��Ӧ�̲���Ϊ��");
		// }
		// if (StringUtils.isBlank(body.getString("budgetsub"))) {
		// throw new BusinessException("Ԥ���Ŀ����Ϊ��");
		// }
	}
}
