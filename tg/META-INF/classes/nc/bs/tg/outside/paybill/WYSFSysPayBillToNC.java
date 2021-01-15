package nc.bs.tg.outside.paybill;

import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.arap.fieldmap.IBillFieldGet;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.arap.pub.BillEnumCollection;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.workflownote.WorkflownoteVO;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * ��Ӧ�̸��
 * 
 * @author kyy
 * 
 */
public class WYSFSysPayBillToNC extends PayBillBaseUtils {
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
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,
					"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��"
						+ billkey
						+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue(
								PayBillVO.BILLNO) + "��,�����ظ��ϴ�!");
			}
			AggPayBillVO billvo = onTranBill(info);
			if(billvo.getChildrenVO().length==1)
				billvo.getParentVO().setAttributeValue("def74", billvo.getChildrenVO()[0].getAttributeValue("contractno"));
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			WorkflownoteVO worknoteVO = ((IWorkflowMachine) NCLocator.getInstance().lookup(
					IWorkflowMachine.class)).checkWorkFlow("SAVE",
							(String) billvo.getParentVO().getAttributeValue(PayBillVO.PK_BILLTYPE), billvo, eParam);
			AggPayBillVO[] obj = (AggPayBillVO[]) getPfBusiAction().processAction("SAVE", "F3", worknoteVO,
					billvo, null, null);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			// WorkflownoteVO noteVO =
			// getWorkflowMachine().checkWorkflowActions(
			// "F1", billvos[0].getPrimaryKey());
			// getPfBusiAction().processAction("SAVE", "F1", noteVO, billvos[0],
			// null, null);
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(PayBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	}

	@Override
	protected AggPayBillVO onDefaultValue(JSONObject head, JSONArray bodylist)
			throws BusinessException {
		// TODO �Զ����ɵķ������
		return super.onDefaultValue(head, bodylist);
	}

	/**
	 * ����-��Ӧ�̺�ͬ\�Ǻ�ͬ���
	 */
	@Override
	protected String getTradetype() {
		// TODO �Զ����ɵķ������
		return "F3-Cxx-LL02";
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
		// if (StringUtils.isBlank(head.getString("contcode"))) {
		// throw new BusinessException("��ͬ���벻��Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("contname"))) {
		// throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("conttype"))) {
		// throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("plate"))) {
		// throw new BusinessException("��鲻��Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("supplier"))) {
		// throw new BusinessException("��Ӧ�̲���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("proejctdata"))) {
		// throw new BusinessException("��Ŀ����Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("proejctstages"))) {
		// throw new BusinessException("��Ŀ���ڲ���Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("totalmny_inv"))) {
		// throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
		// }
		// if (StringUtils.isBlank(head.getString("auditstate"))) {
		// throw new BusinessException("������������״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(head.getString("billdate"))) {
			throw new BusinessException("�������ڲ���Ϊ��");
		}

	}

	@Override
	protected void setHeaderVO(PayBillVO hvo, JSONObject head)
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
																	// hvo.setAttributeValue("def5",
																	// head.getString("contcode"));//
																	// �Զ�����5
		// ��ͬ����->��ͬ����
		// hvo.setAttributeValue("def6", head.getString("contname"));//
		// ��ͬ����->��ͬ����
		// def6
		// hvo.setAttributeValue("def7", head.getString("conttype"));// �Զ�����7
//		String itemtype = head.getString("itemtype");
//		if (StringUtils.isNotBlank(itemtype)) {
//			Map<String, String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if (itemtypeInfo == null) {
//				throw new BusinessException("ҵ�����͡�" + itemtype
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def82", itemtypeInfo.get("pk_obj"));// �շ���Ŀ����
//		}
//		String itemname = head.getString("itemname");
//		if (StringUtils.isNotBlank(itemname)) {
//			Map<String, String> itemnameInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemname);
//			if (itemnameInfo == null) {
//				throw new BusinessException("ҵ�����͡�" + itemname
//						+ "��δ����NC���������������Ϣ!");
//			}
//			hvo.setAttributeValue("def83", itemnameInfo.get("pk_obj"));// �շ���Ŀ����
//		}

		String businesstype = head.getString("businesstype");
		if (StringUtils.isNotBlank(businesstype)) {
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(businesstype, "SDLL003");
			if (businesstypeInfo == null
					|| businesstypeInfo.get("pk_defdoc") == null) {
				throw new BusinessException("ҵ�����͡�" + businesstype
						+ "��δ����NC���������������Ϣ!");
			}
			hvo.setAttributeValue("def84", head.getString("businesstype"));// ҵ������
		}
	

		hvo.setAttributeValue("def86", head.getString("mailbox"));// ����
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
		// hvo.setAttributeValue("def8", head.getString("contcell"));// �Զ�����8
		// ��ͬϸ��->��ͬϸ��
		// �����̶�->�����̶�2020-02-18-tzj
		// String emergency = head.getString("mergency");
		// Map<String, String> emergencyInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(emergency, "zdy031");
		// if (emergencyInfo != null) {
		// hvo.setAttributeValue("def9", emergencyInfo.get("pk_defdoc"));//
		// �Զ�����9
		// }
		// hvo.setAttributeValue("def10", "");// �Զ�����10 ��Դ�ⲿϵͳ

		// if (StringUtils.isNotBlank(head.getString("plate"))) {
		// Map<String, String> plateInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(head.getString("plate"), "bkxx");
		// if (plateInfo == null) {
		// throw new BusinessException("��顾" + head.getString("plate")
		// + "��δ����NC���������������Ϣ!");
		// }
		// hvo.setAttributeValue("def15", plateInfo.get("pk_defdoc"));// �Զ�����15
		// // ���
		// }
		// if (StringUtils.isNotBlank(head.getString("accorg"))) {
		// Map<String, String> accorgInfo = DocInfoQryUtils.getUtils()
		// .getOrgInfo(head.getString("accorg"));
		// if (accorgInfo == null) {
		// throw new BusinessException("���˹�˾��" + head.getString("accorg")
		// + "��δ����NC���������������Ϣ!");
		// }
		// hvo.setAttributeValue("def17", accorgInfo.get("pk_org"));// �Զ�����17
		// // ���˹�˾->NCҵ��Ԫ����
		// }

		// hvo.setPk_busitype(null);//��������
		hvo.setAttributeValue(PayBillVO.OBJTYPE,
				BillEnumCollection.ObjType.SUPPLIER.VALUE);// ��������

//		 if (StringUtils.isNotBlank(head.getString("project"))) {
//		 HashMap<String, String> projectInfo = DocInfoQryUtils.getUtils()
//		 .getSpecialProjectInfo(head.getString("proejctdata"));
//		 if (projectInfo == null) {
//		 throw new BusinessException("��Ŀ��"
//		 + head.getString("proejctdata") + "��δ����NC���������������Ϣ!");
//		
//		 }
		 // hvo.setAttributeValue("def19", projectInfo.get("pk_project"));//
		// ��Ŀ
		// Ӧ�����ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
		// hvo.setAttributeValue("def61", projectInfo.get("def8"));// �Ƿ��ʱ�
		// hvo.setAttributeValue("def62", projectInfo.get("def9"));// ��Ŀ����

		// }

		// if (StringUtils.isNotBlank(head.getString("proejctstages"))) {
		// HashMap<String, String> projectstagesInfo = DocInfoQryUtils
		// .getUtils().getSpecialProjectInfo(
		// head.getString("proejctstages"));
		// if (projectstagesInfo == null) {
		// throw new BusinessException("��Ŀ���� ��"
		// + head.getString("proejctstages") + "��δ����NC���������������Ϣ!");
		// }
		// hvo.setAttributeValue("def32",
		// projectstagesInfo.get("pk_project"));// �Զ�����32
		// // ��Ŀ����
		// }
		hvo.setAttributeValue("money",
				head.getString("money") == null ? UFDouble.ZERO_DBL
						: new UFDouble(head.getString("money")));// ����Ʊ�ݽ��
		// hvo.setAttributeValue("def21", headvo.getTotalmny_request());//
		// �ۼ������
		// hvo.setAttributeValue("def22", headvo.getTotalmny_pay());// �ۼƸ�����
		// hvo.setAttributeValue("def23", headvo.getIsshotgun());// �Ƿ��ȸ����Ʊ
		// hvo.setAttributeValue("def24", headvo.getTotalmny_paybythisreq());//
		// ��������ۼƸ�����
		// hvo.setAttributeValue("def26",
		// head.getString("totalmny_inv") == null ? UFDouble.ZERO_DBL
		// : new UFDouble(head.getString("totalmny_inv")));// �ۼƷ�Ʊ���
		// hvo.setAttributeValue("def28", headvo.getIsdeduction());// �Ƿ����ʱ���۳�
		// hvo.setAttributeValue("def31", head.getString("note"));// ˵��
		// hvo.setAttributeValue("def33", headvo.getAuditstate());
		// ������������״̬
		// if (StringUtils.isNotBlank(head.getString("auditstate"))) {
		// Map<String, String> auditstateInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(head.getString("auditstate"), "zdy032");
		// if (auditstateInfo == null) {
		// throw new BusinessException("������������״̬ ��"
		// + head.getString("auditstate") + "��δ����NC���������������Ϣ!");
		// }
		// hvo.setAttributeValue("def33", auditstateInfo.get("pk_defdoc"));//
		// �Զ�����32
		// // ��Ŀ����
		// }
		// hvo.setAttributeValue("def43", head.getString("def43"));//
		// �Ǳ�׼ָ�����def43
		// hvo.setAttributeValue("def44", head.getString("def44"));// ������def44
		// hvo.setAttributeValue("def45", head.getString("def45"));// �Ѳ�ȫdef45
		// hvo.setAttributeValue("def46", head.getString("dept"));// ���첿��
		// hvo.setAttributeValue("def47", head.getString("psndoc"));// ������
		// hvo.setAttributeValue("def50", "N");// Ʊ��Ȩ������
		// hvo.setAttributeValue("def55", head.getString("def55"));//
		// EBS��ʽ-def55

		// if (StringUtils.isNotBlank(head.getString("signorg"))) {
		// Map<String, String> signOrgInfo = DocInfoQryUtils.getUtils()
		// .getOrgInfo(head.getString("signorg"));
		// if (signOrgInfo == null) {
		// throw new BusinessException("ǩԼ��˾��" + head.getString("signorg")
		// + "��δ����NC���������������Ϣ!");
		// }
		// hvo.setAttributeValue("def34", signOrgInfo.get("pk_org"));// ǩԼ��˾
		// }
		String pk_balatype = DocInfoQryUtils.getUtils().getBalatypeKey(
				head.getString("pk_balatype"));
		hvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ
		hvo.setAttributeValue(PayBillVO.RATE, new UFDouble(1));

	}

	@Override
	protected void setItemVO(PayBillVO headVO, PayBillItemVO itemVO,
			JSONObject body) throws BusinessException {
		checkItemNotNull(body);
		itemVO.setAttributeValue(PayBillItemVO.PK_GROUP, headVO.getPk_group());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_BILLTYPE,
				headVO.getPk_billtype());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPE,
				headVO.getPk_tradetype());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_TRADETYPEID,
				headVO.getPk_tradetypeid());// ��������ID
		itemVO.setAttributeValue(PayBillItemVO.BILLDATE, headVO.getBilldate());// ��������
		itemVO.setAttributeValue(PayBillItemVO.BUSIDATE, headVO.getBusidate());// ��������
		itemVO.setAttributeValue(PayBillItemVO.PK_DEPTID, headVO.getPk_deptid());// ����
		itemVO.setAttributeValue(IBillFieldGet.PK_DEPTID_V,
				headVO.getPk_deptid_v());// �� ��
		itemVO.setAttributeValue(PayBillItemVO.PK_PSNDOC, headVO.getPk_psndoc());// ҵ��Ա

		itemVO.setAttributeValue(PayBillItemVO.OBJTYPE, headVO.getObjtype());// ��������

		itemVO.setAttributeValue(PayBillItemVO.DIRECTION,
				BillEnumCollection.Direction.DEBIT.VALUE);// ����

		itemVO.setAttributeValue(IBillFieldGet.OPPTAXFLAG, UFBoolean.FALSE);//
		itemVO.setAttributeValue(PayBillItemVO.PAUSETRANSACT, UFBoolean.FALSE);// �����־
		itemVO.setAttributeValue(IBillFieldGet.SENDCOUNTRYID,
				headVO.getSendcountryid());

		itemVO.setAttributeValue(IBillFieldGet.BUYSELLFLAG,
				BillEnumCollection.BuySellType.IN_BUY.VALUE);// ��������
		itemVO.setAttributeValue(IBillFieldGet.TRIATRADEFLAG, UFBoolean.FALSE);// ����ó��
		itemVO.setAttributeValue(PayBillItemVO.PK_CURRTYPE,
				headVO.getPk_currtype());// ����
		itemVO.setAttributeValue(PayBillItemVO.RATE, headVO.getRate());// ��֯���һ���
		itemVO.setAttributeValue(IBillFieldGet.RECECOUNTRYID,
				headVO.getRececountryid());// �ջ���

		itemVO.setAttributeValue(PayBillItemVO.TAXRATE, body
				.getString("taxrate") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("taxrate")));// ˰��
		itemVO.setAttributeValue(PayBillItemVO.TAXTYPE,
				BillEnumCollection.TaxType.TAXOUT.VALUE);// ��˰���
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// �跽ԭ�ҽ�� //
		// ��˰�ϼ�->����ƻ���ϸ���
		itemVO.setAttributeValue(PayBillItemVO.MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// ���ҽ��
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_NOTAX_DE, body
				.getString("local_notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_notax_de")));// // ��֯������˰���
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_TAX_DE, body
				.getString("local_tax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("local_tax_de")));// ˰��->����ƻ���ϸ����˰���*˰��
		itemVO.setAttributeValue(PayBillItemVO.NOTAX_DE, body
				.getString("notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_de")));// �跽ԭ����˰���
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_NOTAX_DE, body
				.getString("notax_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("notax_de")));// ������˰���
		itemVO.setAttributeValue(PayBillItemVO.MONEY_DE, body
				.getString("money_de") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_de")));// �跽ԭ�ҽ��
		itemVO.setAttributeValue(PayBillItemVO.MONEY_BAL, body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));// �跽ԭ�����
		itemVO.setAttributeValue(PayBillItemVO.LOCAL_MONEY_BAL, body
				.getString("money_bal") == null ? UFDouble.ZERO_DBL
				: new UFDouble(body.getString("money_bal")));// ����������

		itemVO.setAttributeValue(PayBillItemVO.CONTRACTNO,
				body.getString("contractno"));// ��ͬ����
		itemVO.setAttributeValue(PayBillItemVO.INVOICENO,
				body.getString("invoiceno"));// ��Ʊ����
		itemVO.setAttributeValue(PayBillItemVO.DEF28,
				body.getString("contractname"));// ��ͬ����
		itemVO.setAttributeValue("memo",
				body.getString("memo"));// ��ע
		if (StringUtils.isNotBlank(body.getString("productline"))) {
			itemVO.setAttributeValue(
					PayBillItemVO.PRODUCTLINE,
					DocInfoQryUtils.getUtils().saveProdlineByname(
							body.getString("productline")));// ��Ʒ��
		}
		// TODO ���屸ע
//		String itemtype = body.getString("itemtype");
//		if (StringUtils.isNotBlank(itemtype)) {
//			Map<String, String> itemtypeInfo = DocInfoQryUtils.getUtils()
//					.getBudgetsubInfo(itemtype);
//			if (itemtypeInfo == null) {
//				throw new BusinessException("ҵ�����͡�" + itemtype
//						+ "��δ����NC���������������Ϣ!");
//			}
//			itemVO.setAttributeValue(PayBillItemVO.DEF57,
//					itemtypeInfo.get("pk_obj"));// �շ���Ŀ����
//		}
		String itemname = body.getString("itemname");
		if (StringUtils.isNotBlank(itemname)) {
			Map<String, String> itemnameInfo = DocInfoQryUtils.getUtils()
					.getBudgetsubInfo(itemname);
			if (itemnameInfo == null) {
				throw new BusinessException("ҵ�����͡�" + itemname
						+ "��δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF58,
					itemnameInfo.get("pk_obj"));// �շ���Ŀ����
			itemVO.setAttributeValue(PayBillItemVO.DEF57,itemnameInfo.get("pk_parent"));// �շ���Ŀ����
		}

		String projectphase = body.getString("projectphase");
		if (StringUtils.isNotBlank(projectphase)) {
			Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
					.getDefdocInfo(projectphase, "SDLL006");
			if (businesstypeInfo == null
					|| businesstypeInfo.get("pk_defdoc") == null) {
				throw new BusinessException("ҵ�����͡�" + projectphase
						+ "��δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF59,
					businesstypeInfo.get("pk_defdoc"));// ��Ŀ�׶�
		}
		// ������Ӧ��2019-11-01-tzj
		String pk_supplier = DocInfoQryUtils.getUtils().getSupplierInfo(
				body.getString("supplier"), headVO.getPk_org(),
				headVO.getPk_group());
		if (pk_supplier == null) {
			throw new BusinessException("��Ӧ�̡�" + body.getString("supplier")
					+ "��δ����NC������ѯ�������Ϣ");
		}
		itemVO.setAttributeValue("supplier", pk_supplier);// ��Ӧ��
		itemVO.setAttributeValue(PayBillItemVO.SCOMMENT,
				body.getString("scomment"));// ժҪ

		itemVO.setAttributeValue(PayBillItemVO.DEF55, body.getString("rowid"));// ��id
		itemVO.setAttributeValue(PayBillItemVO.DEF56,
				body.getString("contractname"));// ��ͬ����
		if(StringUtils.isNotBlank(body.getString("arrears"))){
			Map<String, String> detdocMap = DocInfoQryUtils.getUtils().getDefdocInfo(body.getString("arrears"), "SDLL002");
			if(detdocMap==null){
				throw new BusinessException("�Ƿ�����Ƿ�ѡ�" + body.getString("arrears")
						+ "��δ����NC������ѯ�������Ϣ");
			}
			itemVO.setAttributeValue(PayBillItemVO.DEF25,
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
			if (StringUtils.isNotBlank(businessbreakdown)) {
				Map<String, String> businesstypeInfo = DocInfoQryUtils.getUtils()
						.getDefdocInfo(businessbreakdown, "SDLL004");
				if (businesstypeInfo == null
						|| businesstypeInfo.get("pk_defdoc") == null) {
					throw new BusinessException("ҵ��ϸ�ࡾ" + businessbreakdown
							+ "��δ����NC���������������Ϣ!");
				}
				itemVO.setAttributeValue("def27", businessbreakdown);// ҵ��ϸ��
			}
		// if (StringUtils.isNotBlank(body.getString("subjcode"))) {
		// String pk_subjcode = DocInfoQryUtils.getUtils().getAccSubInfo(
		// body.getString("subjcode"), headVO.getPk_org());
		// if (pk_subjcode == null) {
		// throw new BusinessException("��ƿ�Ŀ["
		// + body.getString("subjcode") + "]δ����NC���������������Ϣ!");
		// }
		// itemVO.setAttributeValue(PayBillItemVO.SUBJCODE, pk_subjcode);// ��Ŀ����
		// // ��ƿ�Ŀ
		// }
		// if (StringUtils.isNotBlank(body.getString("costsubject"))) {
		// Map<String, String> costsubjectInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(body.getString("costsubject"), "zdy024");
		// if (costsubjectInfo == null) {
		// throw new BusinessException("�ɱ���Ŀ["
		// + body.getString("costsubject") + "]δ����NC����������!");
		// }
		// itemVO.setAttributeValue("def7", costsubjectInfo.get("pk_defdoc"));//
		// �ɱ���Ŀ
		//
		// }

		// if (StringUtils.isNotBlank(body.getString("invtype"))) {
		// Map<String, String> invtypetInfo = DocInfoQryUtils.getUtils()
		// .getDefdocInfo(body.getString("invtype"), "pjlx");
		// if (invtypetInfo == null) {
		// throw new BusinessException("Ʊ��[" + body.getString("invtype")
		// + "]δ����NC����������!");
		// }
		// itemVO.setAttributeValue("def8", invtypetInfo.get("pk_defdoc"));
		// }
		// // ����ҵ̬ def3
		// itemVO.setAttributeValue("def3", body.getString("format"));
		// // ���ñ��� def4
		// UFDouble formatratio = body.getString("formatratio") == null ?
		// UFDouble.ZERO_DBL
		// : new UFDouble(body.getString("formatratio"));
		// itemVO.setAttributeValue("def4",
		// formatratio.multiply(100).toString());
		// // ����Ʊ������
		// itemVO.setAttributeValue("def9", "100112100000000069WH");
		// // �ɱ�Ӧ��������def15�ɵֿ�˰����ݷ�Ʊ���������¼���-2020-04-24-̸�ӽ�
		// String deductibleTax = getDeductibleTax(body);
		// itemVO.setAttributeValue("def15", deductibleTax);
		/**
		 * �ɱ�Ӧ������ӽ��㷽ʽ-2020-06-04-̸�ӽ�
		 */
		itemVO.setAttributeValue("pk_balatype", headVO.getPk_balatype());// ���㷽ʽ
		if (StringUtils.isNotBlank(body.getString("recaccount"))) {
			Map<String, String> recaccountInfo = DocInfoQryUtils.getUtils()
					.getCustAccnumInfo(itemVO.getSupplier(),
							body.getString("recaccount"));
			if (recaccountInfo == null) {
				throw new BusinessException("�տ������˻�["
						+ body.getString("recaccount") + "]δ����NC���������������Ϣ!");
			}
			itemVO.setAttributeValue(PayableBillItemVO.RECACCOUNT,
					recaccountInfo.get("pk_bankaccsub"));// ���������˻�
		}

		// �������˻�
		if (StringUtils.isNotBlank(body.getString("payaccount"))) {
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
		if (StringUtils.isBlank(body.getString("notax_de"))) {
			throw new BusinessException("�跽ԭ����˰����Ϊ��");
		}
		if (new UFDouble(body.getString("notax_de")).doubleValue() < 0) {
			throw new BusinessException("�跽ԭ����˰����С��0");
		}
		if (StringUtils.isBlank(body.getString("local_tax_de"))) {
			throw new BusinessException("˰���Ϊ��");
		}
		if (new UFDouble(body.getString("local_tax_de")).doubleValue() < 0) {
			throw new BusinessException("˰���С��0");
		}
		if (StringUtils.isBlank(body.getString("notax_de"))) {
			throw new BusinessException("�跽ԭ����˰����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("money_de"))) {
			throw new BusinessException("�跽ԭ�ҽ���Ϊ��");
		}
		if (new UFDouble(body.getString("money_de")).doubleValue() < 0) {
			throw new BusinessException("�跽ԭ�ҽ���С��0");
		}
		if (StringUtils.isBlank(body.getString("money_bal"))) {
			throw new BusinessException("ԭ������Ϊ��");
		}
		if (new UFDouble(body.getString("money_bal")).doubleValue() < 0) {
			throw new BusinessException("ԭ������С��0");
		}
		if (StringUtils.isBlank(body.getString("taxrate"))) {
			throw new BusinessException("˰�ʲ���Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("local_tax_de"))) {
			throw new BusinessException("��˰����Ϊ��");
		}
		if (new UFDouble(body.getString("local_tax_de")).doubleValue() < 0) {
			throw new BusinessException("��˰����С��0");
		}
		 if (StringUtils.isBlank(body.getString("project"))) {
		 throw new BusinessException("����Ϊ��");
		 }
		// if (StringUtils.isBlank(body.getString("local_money_de"))) {
		// throw new BusinessException("��˰�ϼƲ���Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(body.getString("money_cr"))) { throw new
		 * BusinessException("�跽ԭ�ҽ���Ϊ��"); } if
		 * (StringUtils.isBlank(body.getString("money_bal"))) { throw new
		 * BusinessException("ԭ������Ϊ��"); }
		 */
		if (StringUtils.isBlank(body.getString("rowid"))) {
			throw new BusinessException("��ҵ�շ�ϵͳ������ID����Ϊ��");
		}
		if (StringUtils.isBlank(body.getString("productline"))) {
			throw new BusinessException("�����벻��Ϊ��");
		}
		 if (StringUtils.isBlank(body.getString("project"))) {
		 throw new BusinessException("��Ŀ����Ϊ��");
		 }
		// if (StringUtils.isBlank(body.getString("rate"))) {
		// throw new BusinessException("Ʊ������(Ʊ��)����Ϊ��");
		// }
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
