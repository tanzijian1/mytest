package nc.bs.tg.outside.ebs.utils.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.apply.ApplyBillBodyVO;
import nc.vo.tg.apply.ApplyBillHeadVO;
import nc.vo.tg.apply.CostApplyBillBodyVO;
import nc.vo.tg.apply.CostApplyBillHeadVO;
import nc.vo.tg.apply.InsideApplyBillBodyVO;
import nc.vo.tg.apply.InsideApplyBillHeadVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.paymentrequest.Business_b;
import nc.vo.tgfn.paymentrequest.Payrequest;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * ��������->���������
 * 
 * @author ASUS
 * 
 */
public class PayBillUtils extends EBSBillUtils {
	static PayBillUtils utils;

	public static PayBillUtils getUtils() {
		if (utils == null) {
			utils = new PayBillUtils();
		}
		return utils;
	}

	/**
	 * ��������->���������
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
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// �������Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");
		String jsonhead = jsonData.getString("applyHeadVO");
		String jsonbody = jsonData.getString("applyBodyVOs");
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		AggPayrequest[] aggvo = null;
		String srcid = JSONObject.parseObject(jsonhead).getString("ebsid");// EBSϵͳҵ�񵥾�ID
		String srcno = JSONObject.parseObject(jsonhead)
				.getString("ebsbillcode");// EBSϵͳҵ�񵥾ݵ��ݺ�

		/**
		 * �ɱ�nc����ebs��Ϣʱ����������Ӧ�����븶�����뵥������У��Ӧ���к�ͬ 2020-03-10-̸�ӽ� -start
		 * ͨ�õĸ������뵥ҲҪ���ж�
		 */
		// if (/*EBSCont.SRCBILL_15.equals(srctype)||*/
		// EBSCont.SRCBILL_04.equals(srctype)) {
		// String contractcode = JSONObject.parseObject(jsonhead).getString(
		// "contractcode");// ��ͬ����
		// if (contractcode == null || "".equals(contractcode)) {
		// throw new BusinessException("��ͬ���벻��Ϊ��!");
		// }
		// String contractname = JSONObject.parseObject(jsonhead).getString(
		// "contractname");// ��ͬ����
		// if (contractname == null || "".equals(contractname)) {
		// throw new BusinessException("��ͬ���Ʋ���Ϊ��!");
		// }
		// InspectionContract(contractcode, contractname);
		// }

		/**
		 * nc����ebs��Ϣʱ����������Ӧ�����븶�����뵥������У��Ӧ���к�ͬ 2020-03-10-̸�ӽ� -end
		 */
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// TODO ebsid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggPayrequest aggVO = (AggPayrequest) getBillVO(AggPayrequest.class,
				"isnull(dr,0)=0 and def1 = '" + srcid + "'");
		if (aggVO != null) {
			throw new BusinessException("��"
					+ billkey
					+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getAttributeValue(
							PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggPayrequest billvo = onTranBill(jsonhead, jsonbody, srctype);
			// TODO20200825-��Ӻ�ͬ����Ϊ��У��
			// 2020��9��1�� -��ӿ�������ΪͶ�걣֤��ʱȥ����ͬ����Ϊ��У��
			if (!"Ͷ�걣֤��".equals(billvo.getChildrenVO()[0]
					.getAttributeValue("def2"))) {
				BaseDAO dao = new BaseDAO();
				String ifCONTRACT = OutsideUtils.getOutsideInfo("ifCONTRACT");
				if (!("Y".equals(ifCONTRACT))) {
					String def5 = billvo.getParentVO().getDef5();// ��ͬ���룬��ͷdef5��ͬ���ƣ���ͷdef6
					String def6 = billvo.getParentVO().getDef6();
					if (def5 == null || def5.length() < 1 || def6 == null
							|| def6.length() < 1)
						throw new BusinessException("��ͬ������ͬ���Ʋ���Ϊ��");
					if (def5 != null && def6 != null) {
						String pk = (String) dao.executeQuery(
								"select pk_fct_ap from fct_ap f where (f.ctname='"
										+ def6 + "' or f.vbillcode='" + def5
										+ "')", new ColumnProcessor());
						if (pk == null || pk.length() < 1) {
							throw new BusinessException("��ͬ������ͬ������NCϵͳ������");
						}
					}
				}
			}
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggPayrequest[]) getPfBusiAction().processAction(
					"SAVEBASE", "FN01", null, billvo, null, eParam);

			getPfBusiAction().processAction("SAVE", "FN01", null, aggvo[0],
					null, null);
			/*
			 * AggPayrequest[] avo = (AggPayrequest[]) getPfBusiAction()
			 * .processAction("APPROVE", "FN01", null, svo[0], null, eParam);
			 * PushPayApplyUtils.getUtils().pushPayApplyBill(avo[0]);
			 */

		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		HashMap<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("billid", aggvo[0].getPrimaryKey());
		dataMap.put("billno", (String) aggvo[0].getParentVO()
				.getAttributeValue("billno"));
		return JSON.toJSONString(dataMap);

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggPayrequest onTranBill(String jsonhead, String jsonbody,
			String srctype) throws BusinessException {

		AggPayrequest aggvo = new AggPayrequest();
		if ("10".equals(srctype) || "15".equals(srctype)) {
			transforCost(jsonhead, jsonbody, aggvo, srctype);
		} else if ("27".equals(srctype)) {
			transforInside(jsonhead, jsonbody, aggvo, srctype);
		} else {
			transforexpense(jsonhead, jsonbody, aggvo, srctype);

		}

		return aggvo;
	}

	/**
	 * �ڲ�����
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void transforInside(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// ת��json
		InsideApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				InsideApplyBillHeadVO.class);
		List<InsideApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				InsideApplyBillBodyVO.class);
		checkHeadTransforInside(headvo, srctype);// ��ֵ���
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ�����");
		}
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// ��������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ����
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// ������֯
		} else {
			throw new BusinessException("�ò�����֯���룺δ����NC����������");
		}// ������֯
		save_headVO.setAttributeValue("pk_org_v", null);// ��֯�汾
		save_headVO.setAttributeValue("creator", null);// ������
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("modifier", null);// �޸���
		save_headVO.setAttributeValue("modifiedtime", null);// �޸�ʱ��
		save_headVO.setAttributeValue("billno", null);// ���ݺ�
		save_headVO.setAttributeValue("pkorg", save_headVO.getPk_org());// ������֯
		save_headVO.setAttributeValue("busitype", null);// ҵ������
		save_headVO.setAttributeValue("billmaker", null);// �Ƶ���
		save_headVO.setAttributeValue("approver", null);// ������
		save_headVO.setAttributeValue("approvenote", null);// ��������
		save_headVO.setAttributeValue("approvedate", null);// ����ʱ��
		save_headVO.setAttributeValue("recaccount", null);

		String pk_transtype = gettradeTypeByCode("FN01-Cxx-003");
		save_headVO.setAttributeValue("transtype", "FN01-Cxx-003");// ��������
		save_headVO.setAttributeValue("billtype", "FN01");// ��������
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// ��������pk
		save_headVO.setAttributeValue("emendenum", null);// �޶�ö��
		save_headVO.setAttributeValue("billversionpk", null);// ���ݰ汾pk
		save_headVO.setAttributeValue("billdate", new UFDate());// ��������
		save_headVO.setAttributeValue("pk_tradetypeid", null);// ��������
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS����
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS������
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// Ӱ�����
		save_headVO.setAttributeValue("def4", headvo.getImagestatus());// Ӱ��״̬
		save_headVO.setAttributeValue("def5", headvo.getPurchasecode());// �ɹ�Э����
		save_headVO.setAttributeValue("def6", headvo.getPurchasename());// �ɹ�Э������
		save_headVO.setAttributeValue("def7", null);// �Զ�����7
		save_headVO.setAttributeValue("def8", null);// �Զ�����8
		if (StringUtils.isNotBlank(getdefdocBycode(headvo.getEmergency(),
				"zdy031"))) {
			save_headVO.setAttributeValue("def9",
					getdefdocBycode(headvo.getEmergency(), "zdy031"));// ����״̬
		} else {
			throw new BusinessException("�ý���״̬���룺" + headvo.getEmergency()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("def10", headvo.getSrcname());// ��ϵͳ����
		save_headVO.setAttributeValue("def11", null);
		save_headVO.setAttributeValue("def12", null);
		save_headVO.setAttributeValue("objtype", 1);// ��������
		save_headVO.setAttributeValue("pk_deptid_v", null);// ���첿��
		save_headVO.setAttributeValue("pk_psndoc", null);// ������
		String pk_balatype = null;
		// if ("�ֽ�".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("10");
		// }
		// if ("����".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("13");
		// }
		// if ("��Ʊ".equals(headvo.getBalatype())) {
		// pk_balatype = getBalatypePkByCode("7");
		// }
		pk_balatype = getBalatypePkByCode(headvo.getBalatype());
		if (pk_balatype == null) {
			throw new BusinessException("���㷽ʽ��" + headvo.getBalatype()
					+ "��δ����NC������ѯ�������Ϣ!");
		}
		save_headVO.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// ���㷽ʽ

		// �����㷽ʽΪ����ʱ�ش��������ͺ��ʲ�����-2020-06-09-̸�ӽ�-start
		if ("9".equals(headvo.getBalatype())) {
			save_headVO.setAttributeValue("def61", headvo.getDef61());//
			save_headVO.setAttributeValue("def62", headvo.getDef62());// �ʲ�����
		}
		// �����㷽ʽΪ����ʱ�ش��������ͺ��ʲ�����-2020-06-09-̸�ӽ�-end

		save_headVO.setAttributeValue("payaccount", null);// ���������˻�
		save_headVO.setAttributeValue("def13", null);
		save_headVO.setAttributeValue("cashaccount", null);// �ֽ��˻�
		save_headVO.setAttributeValue("money", new UFDouble(headvo.getMoney()));// �����ܽ��
		save_headVO.setAttributeValue("def14", null);
		save_headVO.setAttributeValue("def15", null);
		save_headVO.setAttributeValue("def16", null);
		save_headVO.setAttributeValue("def17", null);
		save_headVO.setAttributeValue("def18", headvo.getContractsign());// ��ͬǩԼ��
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getContractsign()))) {
			save_headVO.setAttributeValue("def18",
					getSupplierIDByCodeOrName(headvo.getContractsign()));// ��ͬǩԼ��(��Ӧ��)
		} else {
			throw new BusinessException("�ú�ͬǩԼ����" + headvo.getContractsign()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("def19", null);
		save_headVO.setAttributeValue("def20", null);
		save_headVO.setAttributeValue("def21", null);
		save_headVO.setAttributeValue("def22", null);
		save_headVO.setAttributeValue("def23", null);
		save_headVO.setAttributeValue("def24", null);
		save_headVO.setAttributeValue("def25", null);
		save_headVO.setAttributeValue("def26", null);
		save_headVO.setAttributeValue("def27", null);// �Ƿ�����
		save_headVO.setAttributeValue("def28", null);
		save_headVO.setAttributeValue("billstatus", null);// ����״̬
		save_headVO.setAttributeValue("approvestatus", -1);// ����״̬
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("def29", null);//
		save_headVO.setAttributeValue("def30", headvo.getBondratio());//
		save_headVO.setAttributeValue("def31", headvo.getExplain());
		save_headVO.setAttributeValue("def32", "100112100000000071JH");// Ĭ����ĿΪȫ��Ŀ
		// TODO ADD BY 20200227 HDQ ����code ��ȡ������������״̬ -START-
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String def33 = getAuditstateByCode(headvo.getFinstatus());
			if (def33 == null) {
				throw new BusinessException("������������״̬��" + headvo.getFinstatus()
						+ "��δ����NC���������������Ϣ!");
			}
			save_headVO.setAttributeValue("def33", def33);// ������������״̬
		}

		// TODO ADD BY 20200227 HDQ ����code ��ȡ������������״̬ -END-

		save_headVO.setAttributeValue("def34", null);
		save_headVO.setAttributeValue("def35", headvo.getDeptname());// ���첿��
		save_headVO.setAttributeValue("def36", headvo.getOperatorname());// ������
		save_headVO.setAttributeValue("def37", headvo.getAbsmny());// ABS�Ǽǽ��
		save_headVO.setAttributeValue("def38", null);// �Զ�����38
		save_headVO.setAttributeValue("def39", null);
		save_headVO.setAttributeValue("def40", null);// �Զ�����40
		save_headVO.setAttributeValue("def41", headvo.getBondcode());// �ձ�֤����
		save_headVO.setAttributeValue("def42", headvo.getBondtype());// ��֤������
		save_headVO.setAttributeValue("def43", headvo.getCompleted());// �Զ�����43
		save_headVO.setAttributeValue("def44", headvo.getRepairannex());// �Զ�����44
		save_headVO.setAttributeValue("def45", headvo.getPaymentletter());// �Զ�����45
		save_headVO.setAttributeValue("def46", null);// �Զ�����46
		save_headVO.setAttributeValue("def47", headvo.getArrivalbuild());// �Զ�����47
		save_headVO.setAttributeValue("def48", null);// �Զ�����48
		save_headVO.setAttributeValue("def49", headvo.getMoney());// �������δ�����
		save_headVO.setAttributeValue("def50", null);// �Զ�����50
		// ������ͣ�def52��paymentType
		// save_headVO.setAttributeValue("def52", headvo.getPaymentType());//
		// �������
		// save_headVO.setAttributeValue("def53", headvo.getTaskID());// BPM����ID
		save_headVO.setAttributeValue("def55", headvo.getAbsratio());// ABSʵ������
		// bpmid
		save_headVO.setAttributeValue("bpmid", headvo.getBpmid());

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (InsideApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforInside(applyBillBodyVO, srctype);// ��ֵ���
			save_bodyVO.setAttributeValue("pk_business_b", null);// ҵ��ҳǩ����
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// ժҪ
			save_bodyVO
					.setAttributeValue("def1", applyBillBodyVO.getNotetype());// Ʊ������
			save_bodyVO.setAttributeValue("pk_subjcode", null);// ��֧��Ŀ
			save_bodyVO.setAttributeValue("def2",
					applyBillBodyVO.getProceedtype());// ��������
			save_bodyVO.setAttributeValue("def3", applyBillBodyVO.getTaxrate());// ˰��
			save_bodyVO.setAttributeValue("def4",
					applyBillBodyVO.getTaxamount());// ˰��
			save_bodyVO.setAttributeValue("objtype", 1);// ��������
			if (StringUtils
					.isNotBlank(getSupplierIDByCodeOrName(applyBillBodyVO
							.getSupplier()))) {
				save_bodyVO
						.setAttributeValue("supplier",
								getSupplierIDByCodeOrName(applyBillBodyVO
										.getSupplier()));// ʵ���տ(��Ӧ��)
			} else {
				throw new BusinessException("�ù�Ӧ�̣�"
						+ applyBillBodyVO.getSupplier() + "δ����NC����������");
			}
			save_bodyVO.setAttributeValue("def5", applyBillBodyVO.getTotal());// ��˰�ϼ�
			save_bodyVO.setAttributeValue("def6", null);// �Զ�����6
			save_bodyVO.setAttributeValue("def7", null);// �Զ�����7
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// ������

			if (!"".equals(applyBillBodyVO.getRecaccount())
					&& applyBillBodyVO.getRecaccount() != null) {
				String recaccount = getAccountIDByCode(
						applyBillBodyVO.getRecaccount(),
						save_bodyVO.getSupplier());

				if ("".equals(recaccount) || recaccount == null) {
					throw new BusinessException("�ù�Ӧ����û�и������˺��ӻ�");
				}

				save_bodyVO.setAttributeValue("def15", recaccount);// �տ������˻�

				// ʡ�ݳ���
				save_bodyVO.setAttributeValue("def43",
						getCityByrecaccount(recaccount).get("province"));
				save_bodyVO.setAttributeValue("def44",
						getCityByrecaccount(recaccount).get("city"));
			}

			save_bodyVO.setAttributeValue("def8", null);// �Զ�����8
			save_bodyVO.setAttributeValue("def9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("def10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("def11", null);// �Զ�����11
			save_bodyVO.setAttributeValue("def12", null);// �Զ�����12
			save_bodyVO.setAttributeValue("def13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("def14", null);// �Զ�����14
			save_bodyVO.setAttributeValue("def16", null);// �Զ�����16
			if (!"".equals(applyBillBodyVO.getCompany())
					&& applyBillBodyVO.getCompany() != null) {
				if (getPk_orgByCode(applyBillBodyVO.getCompany()) == null) {
					throw new BusinessException("�õ�¥������˾��"
							+ applyBillBodyVO.getCompany() + "δ����NC����������");
				}
			}
			save_bodyVO.setAttributeValue("def17",
					getPk_orgByCode(applyBillBodyVO.getCompany()));// ��¥������˾
			save_bodyVO.setAttributeValue("def18",
					applyBillBodyVO.getBidnumber());// ������
			save_bodyVO.setAttributeValue("def19",
					applyBillBodyVO.getBidtitle());// �б�����
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("def22", null);// �Զ�����22
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def29", null);// �Զ�����29
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def31", null);// �Զ�����31
			save_bodyVO.setAttributeValue("def32", null);// �Զ�����32
			save_bodyVO.setAttributeValue("def33", null);// �Զ�����33
			save_bodyVO.setAttributeValue("def34", null);// �Զ�����34
			save_bodyVO.setAttributeValue("def35", null);// �Զ�����35
			save_bodyVO.setAttributeValue("def36", null);// �Զ�����36
			save_bodyVO.setAttributeValue("def37", null);// �Զ�����37
			save_bodyVO.setAttributeValue("def38", null);// �Զ�����38
			save_bodyVO.setAttributeValue("def39",
					applyBillBodyVO.getReaccountno());// �տ�������к�
			save_bodyVO.setAttributeValue("def40", null);// �Զ�����40
			save_bodyVO.setAttributeValue("def41", null);// �Զ�����41
			save_bodyVO.setAttributeValue("def42", null);// �Զ�����42
			save_bodyVO.setAttributeValue("def45", null);// �Զ�����45
			save_bodyVO.setAttributeValue("def46", null);// �Զ�����46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// δ�����
			save_bodyVO.setAttributeValue("def48", null);// �Զ�����48
			save_bodyVO.setAttributeValue("def49", null);// �Զ�����49
			save_bodyVO.setAttributeValue("def50", null);// �Զ�����50
			save_bodyVO.setAttributeValue("pk_payreq", null);// �������뵥_����
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);

	}

	/**
	 * �ɱ��������뵥
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void transforCost(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// TODO �Զ����ɵķ������
		// ת��json
		CostApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				CostApplyBillHeadVO.class);
		List<CostApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				CostApplyBillBodyVO.class);
		checkHeadTransforCost(headvo, srctype);// ��ֵ���
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ�����");
		}
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// ��������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ����
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// ������֯
		} else {
			throw new BusinessException("�ò�����֯���룺" + headvo.getOrg()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("pk_org_v", null);// ��֯�汾
		// if(StringUtils.isNotBlank(headvo.getCreator())){// ������
		// if(StringUtils.isNotBlank(getUserPkByCode(headvo.getCreator()))){
		// save_headVO.setAttributeValue("creator",
		// getUserPkByCode(headvo.getCreator()));
		// }else{
		// throw new
		// BusinessException("�ô����˱��룺"+headvo.getCreator()+"δ����NC����������");
		// }
		// }
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("modifier", "1001ZZ100000001MRF59");// �޸���
		save_headVO.setAttributeValue("modifiedtime", new UFDateTime());// �޸�ʱ��
		save_headVO.setAttributeValue("billno", null);// ���ݺ�
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			String pk_orgCode = getPk_orgByCode(headvo.getOrg());
			save_headVO.setAttributeValue("pkorg", pk_orgCode);// ������֯
			// �ɱ��������뵥�ӿڣ�����EBS�������Ĳ�����֯+��ͬ���룬�����ú�ͬ��Ӧ�ġ�ҵ���š����浽�������뵥��ҵ���š���ͷdef58-2020-04-20-̸�ӽ�
			String department = getDepartment(headvo.getContractcode(),
					pk_orgCode);
			save_headVO.setAttributeValue("def58", department);// ҵ����
		} else {
			throw new BusinessException("��������֯���룺" + headvo.getOrg()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("busitype", headvo.getBusitype());// ҵ������
		save_headVO.setAttributeValue("billmaker",
				getUserInfo(headvo.getCreator()));// �Ƶ���
		save_headVO.setAttributeValue("approver", null);// ������
		save_headVO.setAttributeValue("approvenote", null);// ��������
		save_headVO.setAttributeValue("approvedate", null);// ����ʱ��
		// ���ݷ�Ʊ����Ƿ�Ϊ0�жϽ�������
		String transtype = null;
		String finstatus = headvo.getFinstatus();
		if ("finapprove".equals(finstatus)) {
			if ("Y".equals(headvo.getDef40())) {
				transtype = "FN01-Cxx-005";
			} else {
				transtype = "FN01-Cxx-002";
			}
		} else {
			transtype = "FN01-Cxx-005";
		}
		String pk_transtype = gettradeTypeByCode(transtype);
		save_headVO.setAttributeValue("transtype", transtype);// ��������
		save_headVO.setAttributeValue("billtype", "FN01");// ��������
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// ��������pk
		save_headVO.setAttributeValue("emendenum", null);// �޶�ö��
		save_headVO.setAttributeValue("billversionpk", null);// ���ݰ汾pk
		save_headVO.setAttributeValue("billdate", new UFDate());// ��������
		save_headVO.setAttributeValue("pk_tradetypeid", null);// ��������
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS����
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS������
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// Ӱ�����
		save_headVO.setAttributeValue("def4", headvo.getImagestatus());// Ӱ��״̬
		save_headVO.setAttributeValue("def5", headvo.getContractcode());// ��ͬ����
		save_headVO.setAttributeValue("def6", headvo.getContractname());// ��ͬ����
		save_headVO.setAttributeValue("def7", headvo.getContracttype());// ��ͬ����
		// �ɱ��������뵥�ӿ�Ҳ��Ҫ���ݺ�ͬ���ʹ�����Ӧ�Ĳ���Ʊ�����ͣ��ɱ��ࡢ�����ࣩ,��Ӧ��ϵ���Զ��嵵��zdy045-2020-05-19-̸�ӽ�
		save_headVO.setAttributeValue("def59",
				getNoteType(headvo.getContracttype()));

		save_headVO.setAttributeValue("def8", headvo.getContractsubclass());// ��ͬϸ��
		String def9 = getdefdocBycode(headvo.getEmergency(), "zdy031");
		if (StringUtils.isNotBlank(def9)) {
			save_headVO.setAttributeValue("def9", def9);// �����̶�
		} else {
			throw new BusinessException("�����̶ȣ�" + headvo.getEmergency()
					+ "δ����NC����������");
		}

		save_headVO.setAttributeValue("def10", "EBS�ɱ�ϵͳ");// ��ϵͳ����
		save_headVO.setAttributeValue("def11", headvo.getActuallypaidmoney());// ʵ�����
		save_headVO.setAttributeValue("def12", null);// �Զ�����12

		if (StringUtils.isNotBlank(getBalatypePkByCode(headvo.getBalatype()))) {
			getBalatypePkByCode(headvo.getBalatype());
			save_headVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(headvo.getBalatype()));// ���㷽ʽ
			// �����㷽ʽΪ����ʱ�������ͺ��ʲ�����-2020-06-09-̸�ӽ�-start
			if ("9".equals(headvo.getBalatype())) {
				save_headVO.setAttributeValue("def61", headvo.getDef61());// �������ͣ����ֿ�ѡ��ƽԣ�����գ������ڣ�
				save_headVO.setAttributeValue("def62", headvo.getDef62());// �ʲ�����
			}
			// �����㷽ʽΪ����ʱ�ش��������ͺ��ʲ�����-2020-06-09-̸�ӽ�-end
		} else {
			throw new BusinessException("�ý��㷽ʽ���룺" + headvo.getBalatype()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("payaccount", headvo.getPayaccount());// ���������˻�
		save_headVO.setAttributeValue("def13", null);// �Զ�����13
		save_headVO.setAttributeValue("cashaccount", null);// �ֽ��˻�
		save_headVO.setAttributeValue("money", headvo.getMoney());// ���
		// save_headVO.setAttributeValue("def14",
		// headvo.getAbsregisterdate());// abs�Ǽ�����
		if (StringUtils.isNotBlank(getdefdocBycode(headvo.getPlate(), "bkxx"))) {
			save_headVO.setAttributeValue("def15",
					getdefdocBycode(headvo.getPlate(), "bkxx"));// ���
		} else {
			throw new BusinessException("�ð����룺" + headvo.getPlate()
					+ "δ����NC����������");
		}
		// ����������
		save_headVO.setAttributeValue("def16", headvo.getDef16());// ����������
		if (StringUtils.isNotBlank(headvo.getAccountingorg())) {
			if (StringUtils.isNotBlank(getPk_orgByCode(headvo
					.getAccountingorg()))) {
				save_headVO.setAttributeValue("def17",
						getPk_orgByCode(headvo.getAccountingorg()));// ���˹�˾
			} else {
				throw new BusinessException("�ó��˹�˾��"
						+ headvo.getAccountingorg() + "δ����NC����������");
			}
		}
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getSupplier()))) {
			save_headVO.setAttributeValue("def18",
					getSupplierIDByCodeOrName(headvo.getSupplier()));// �տ
		} else {
			throw new BusinessException("���տ���룺" + headvo.getSupplier()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		if (StringUtils.isNotBlank(headvo.getProject())) {
			if (StringUtils
					.isNotBlank(getpk_projectByCode(headvo.getProject()))) {
				save_headVO.setAttributeValue("def19",
						getpk_projectByCode(headvo.getProject()));// ��Ŀ
				// �������뵥�ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
				HashMap<String, Object> projectData = getProjectDataByCode(headvo
						.getProject());
				if (projectData != null) {
					save_headVO.setAttributeValue("def56",
							projectData.get("def8"));// �Ƿ��ʱ�
					save_headVO.setAttributeValue("def57",
							projectData.get("def9"));// ��Ŀ����
				}

			} else {
				throw new BusinessException("����Ŀ���룺" + headvo.getProject()
						+ "δ����NC����������");
			}
		} else {
			save_headVO.setAttributeValue("def19", null);// ��Ŀ
		}
		save_headVO.setAttributeValue("def20", null);// �Զ�����20
		save_headVO.setAttributeValue("def21", headvo.getTotalapplymoney());// �ۼ������
		save_headVO.setAttributeValue("def22", headvo.getTotalamount());// �ۼƸ�����
		save_headVO.setAttributeValue("def23", headvo.getIspaynote());// �Ƿ��ȸ����Ʊ
		save_headVO.setAttributeValue("def24", null);// �Զ�����24
		save_headVO.setAttributeValue("def25", null);// ��������ۼƸ�����
		save_headVO.setAttributeValue("def26", headvo.getTotalnotemoney());// �ۼƷ�Ʊ���
		save_headVO.setAttributeValue("def27", null);//
		save_headVO.setAttributeValue("def28", headvo.getIsmargin());// �Ƿ��ʱ���۳�
		save_headVO.setAttributeValue("billstatus", null);// ����״̬
		save_headVO.setAttributeValue("approvestatus", -1);// ����״̬
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("def29", headvo.getIspaid());// �Ƿ���
		save_headVO.setAttributeValue("def30", headvo.getDef30());//
		save_headVO.setAttributeValue("def31", headvo.getExplain());// ˵��
		save_headVO.setAttributeValue("def32", headvo.getProjectstages());// ��Ŀ����

		// save_headVO.setAttributeValue("def33", headvo.getFinstatus());//
		// ������������״̬
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String pk_auditstate = getAuditstateByCode(headvo.getFinstatus());
			if (pk_auditstate == null) {
				throw new BusinessException("������������״̬ ��"
						+ headvo.getFinstatus() + "��δ����NC���������������Ϣ!");
			}
			save_headVO.setAttributeValue("def33", pk_auditstate);// �Զ�����33 ��Ŀ����
		}

		save_headVO.setAttributeValue("def34", headvo.getSignorg());// ǩԼ��˾
		save_headVO.setAttributeValue("def35", headvo.getDeptname());// ���첿��
		save_headVO.setAttributeValue("def36", headvo.getOperatorname());// ������
		save_headVO.setAttributeValue("def37", headvo.getDef37());// �Ǽǽ��
		save_headVO.setAttributeValue("def38", headvo.getDef38());// ˮ���
		save_headVO.setAttributeValue("def39", headvo.getDef39());// ����
		save_headVO.setAttributeValue("def40", headvo.getDef40());// ��Ʊ����Ƿ�Ϊ0
		save_headVO.setAttributeValue("def41", null);// �Զ�����41
		save_headVO.setAttributeValue("def42", null);// �Զ�����42
		save_headVO.setAttributeValue("def43", headvo.getPaymentletter());// �Ǳ�׼ָ�����def43
		save_headVO.setAttributeValue("def44", headvo.getRepairannex());// ������def44
		save_headVO.setAttributeValue("def45", headvo.getCompleted());// �Ѳ�ȫdef45

		// save_headVO.setAttributeValue("def43", null);// �Զ�����43
		// save_headVO.setAttributeValue("def44", null);// �Զ�����44
		// save_headVO.setAttributeValue("def45", null);// �Զ�����45
		save_headVO.setAttributeValue("def46", headvo.getDef46());// �����ۿ�
		save_headVO.setAttributeValue("def47", headvo.getDef47());// ��¥��ʶ
		save_headVO.setAttributeValue("def48", null);// �Զ�����48
		save_headVO.setAttributeValue("def49", headvo.getMoney());// �������δ�����
		save_headVO.setAttributeValue("def50", null);// �Զ�����50
		String def51 = headvo.getDef51();
		if (def51 != null && !"".equals(def51)) {
			save_headVO.setAttributeValue("def51", def51);// EBS��ʽ
		} else {
			throw new BusinessException("EBS��ʽ����Ϊ��");
		}
		String def52 = headvo.getDef52();
		if (def52 != null && !"".equals(def52)) {
			save_headVO.setAttributeValue("def52", def52);// ��������
		} else {
			throw new BusinessException("�������Ͳ���Ϊ��");
		}
		// ��֤�����
		save_headVO.setAttributeValue("def54", headvo.getDef54());
		// ABSʵ������
		save_headVO.setAttributeValue("def55", headvo.getDef55());
		// bpmid
		save_headVO.setBpmid(headvo.getBpmid());

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (CostApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforCost(applyBillBodyVO, srctype);// ��ֵ���
			save_bodyVO.setAttributeValue("pk_business_b", null);// ҵ��ҳǩ����
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// ժҪ
			save_bodyVO.setAttributeValue("def1", null);// �Զ�����1
			// save_bodyVO.setAttributeValue("pk_subjcode",
			// applyBillBodyVO.getSubjcode());// ��֧��Ŀ
			// if (StringUtils.isNotBlank(getdefdocBycode(
			// applyBillBodyVO.getProceedtype(), "zdy020"))) {
			// save_bodyVO.setAttributeValue(
			// "def2",
			// getdefdocBycode(applyBillBodyVO.getProceedtype(),
			// "zdy020"));// ��������
			// } else {
			// throw new BusinessException("�ÿ������ͱ��룺"
			// + applyBillBodyVO.getProceedtype() + "δ����NC����������");
			// }
			if (StringUtils.isNotBlank(applyBillBodyVO.getBusinessformat())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getBusinessformat(), "ys004"))) {
					save_bodyVO.setAttributeValue(
							"def3",
							getdefdocBycode(
									applyBillBodyVO.getBusinessformat(),
									"ys004"));// ҵ̬
				} else {
					throw new BusinessException("��ҵ̬���룺"
							+ applyBillBodyVO.getBusinessformat()
							+ "δ����NC����������");
				}
			}
			save_bodyVO.setAttributeValue("def4", applyBillBodyVO.getScale());// ����
			/**
			 * ebs���Ĵ���ͷ��������:objtype ebs���Ĵ���ͷҵ��Ա:psndoc NC�����ڸ������뵥��
			 * ������������:objtype ����ҵ��Ա:def58
			 */

			String objtype = applyBillBodyVO.getObjtype();
			if (objtype == null || "".equals(objtype)) {
				throw new BusinessException("��������objtype����Ϊ��!");
			}
			String supplier = applyBillBodyVO.getSupplier();
			if (null != objtype && "3".equals(objtype)) {
				save_bodyVO.setAttributeValue("objtype", objtype);// ��������
				String pk_psndoc = getPk_psndocByCode(supplier);
				if (pk_psndoc != null) {
					save_bodyVO.setAttributeValue("def58", pk_psndoc);// ҵ��Ա
				} else {
					throw new BusinessException("ҵ��Ա���룺" + supplier
							+ "δ����NC����������");
				}
				if (!"".equals(applyBillBodyVO.getRecaccount())
						&& applyBillBodyVO.getRecaccount() != null) {
					String recaccount = getPersonalAccountIDByCode(
							applyBillBodyVO.getRecaccount(), pk_psndoc);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("�ù�Ӧ����û�и������˺��ӻ�");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// �տ������˻�

					// ʡ�ݳ���
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}
			} else {
				String pk_supplier = getSupplierIDByCodeOrName(supplier);
				if (StringUtils.isNotBlank(pk_supplier)) {
					save_bodyVO.setAttributeValue("supplier", pk_supplier);// ʵ���տ(��Ӧ��)
					// ��Ӧ�̻�������
					String pk_supplierclass = getSupplierClassByCode(supplier);
					save_bodyVO.setDef45(pk_supplierclass);
					if (null != objtype && "1".equals(objtype)) {
						save_bodyVO.setAttributeValue("objtype", objtype);// ��������
					}
				} else {
					throw new BusinessException("�ù�Ӧ�̱��룺" + supplier
							+ "δ����NC����������");
				}

				if (!"".equals(applyBillBodyVO.getRecaccount())
						&& applyBillBodyVO.getRecaccount() != null) {
					String recaccount = getAccountIDByCode(
							applyBillBodyVO.getRecaccount(), pk_supplier);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("�ù�Ӧ����û�и������˺��ӻ�");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// �տ������˻�
					// ʡ�ݳ���
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}
			}

			save_bodyVO.setAttributeValue("def5", null);// �Զ�����5
			save_bodyVO.setAttributeValue("def6",
					applyBillBodyVO.getBankraccountcode());// �տ�������к�
			save_bodyVO.setAttributeValue("def7", null);// �Զ�����7
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// ������
			save_bodyVO.setAttributeValue("def8", null);// �Զ�����8
			String pk_defdoc = getdefdocBycode(
					applyBillBodyVO.getCostsubjects(), "zdy024");
			if (StringUtils.isNotBlank(pk_defdoc)) {
				save_bodyVO.setAttributeValue("def9", pk_defdoc);// �ɱ���Ŀ
				if (save_headVO.getDef7() != null
						&& save_headVO.getDef47() != null) {
					// ��֧��Ŀ
					String pk_subjcode = getSubjcode(save_headVO.getDef7(),
							save_headVO.getDef47());
					save_bodyVO.setAttributeValue("pk_subjcode", pk_subjcode);
				}

			} else {
				throw new BusinessException("�óɱ���Ŀ��EBSID��"
						+ applyBillBodyVO.getCostsubjects() + "δ����NC����������");
			}
			save_bodyVO.setAttributeValue("def10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("def11",
					applyBillBodyVO.getNotetype());// Ʊ������
			save_bodyVO.setAttributeValue("def12", applyBillBodyVO.getNoteno());// Ʊ�ݺ�
			save_bodyVO.setAttributeValue("def13",
					applyBillBodyVO.getAccountsubjcode());// ��ƿ�Ŀ
			save_bodyVO.setAttributeValue("def14",
					applyBillBodyVO.getPayaccount());// ���������˻�
			// save_bodyVO.setAttributeValue(
			// "def15",
			// getAccountIDByCode(applyBillBodyVO.getRecaccount(),
			// pk_supplier));// �տ������˻�

			save_bodyVO.setAttributeValue("def16",
					applyBillBodyVO.getCastaccount());// �ֽ��˻�
			save_bodyVO.setAttributeValue("def17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("def18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("def19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def21", null);// �Զ�����21
			save_bodyVO.setAttributeValue("def22", null);// �Զ�����22
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def29", null);// �Զ�����29
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def31", null);// �Զ�����31
			save_bodyVO.setAttributeValue("def32", null);// �Զ�����32
			save_bodyVO.setAttributeValue("def33", null);// �Զ�����33
			save_bodyVO.setAttributeValue("def34", null);// �Զ�����34
			save_bodyVO.setAttributeValue("def35", null);// �Զ�����35
			save_bodyVO.setAttributeValue("def36", null);// �Զ�����36
			save_bodyVO.setAttributeValue("def37", null);// �Զ�����37
			String def38 = applyBillBodyVO.getDef38();
			if (def38 != null && !"".equals(def38)) {
				save_bodyVO.setAttributeValue("def38", def38);// EBS��ID
			} else {
				throw new BusinessException("EBS��ID����Ϊ��!");
			}
			save_bodyVO.setAttributeValue("def39", null);// �Զ�����39
			String def40 = applyBillBodyVO.getDef40();
			if (def40 != null && !"".equals(def40)) {
				save_bodyVO.setAttributeValue("def40", def40);// EBS����
			} else {
				throw new BusinessException("EBS��������Ϊ��!");
			}
			save_bodyVO.setAttributeValue("def41", null);// �Զ�����41
			save_bodyVO.setAttributeValue("def42", null);// �Զ�����42
			// save_bodyVO.setAttributeValue("def45", null);// �Զ�����45
			save_bodyVO.setAttributeValue("def46", null);// �Զ�����46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// δ�����
			// applyBillBodyVO.getDef48());// ˮ��ѽ��
			// save_bodyVO.setAttributeValue("def48",
			// save_bodyVO.setAttributeValue("def49",
			// applyBillBodyVO.getDef49());// ����
			save_bodyVO.setAttributeValue("def50", null);// �Զ�����50
			save_bodyVO.setAttributeValue("pk_payreq", null);// �������뵥_����
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);
	}

	/**
	 * ͨ�ø������뵥
	 * 
	 * @param jsonhead
	 * @param jsonbody
	 * @param aggvo
	 * @throws BusinessException
	 */
	private void transforexpense(String jsonhead, String jsonbody,
			AggPayrequest aggvo, String srctype) throws BusinessException {
		// TODO �Զ����ɵķ������
		// ת��json
		ApplyBillHeadVO headvo = JSONObject.parseObject(jsonhead,
				ApplyBillHeadVO.class);
		List<ApplyBillBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				ApplyBillBodyVO.class);
		if (headvo == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ�����");
		}
		checkHeadTransforExpense(headvo, srctype);// ��ֵ���
		Payrequest save_headVO = new Payrequest();
		save_headVO.setAttributeValue("pk_payreq", null);// ��������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// ����
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pk_org",
					getPk_orgByCode(headvo.getOrg()));// ������֯
		} else {
			throw new BusinessException("�ò�����֯���룺" + headvo.getOrg()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("pk_org_v", null);// ��֯�汾
		if (StringUtils.isNotBlank(headvo.getCreator())) {// ������
			if (StringUtils.isNotBlank(getUserPkByCode(headvo.getCreator()))) {
				save_headVO.setAttributeValue("creator",
						getUserPkByCode(headvo.getCreator()));
			} else {
				throw new BusinessException("�ô����˱��룺" + headvo.getCreator()
						+ "δ����NC����������");
			}
		}
		save_headVO.setAttributeValue("creationtime", new UFDateTime());// ����ʱ��
		save_headVO.setAttributeValue("modifier", null);// �޸���
		save_headVO.setAttributeValue("modifiedtime", null);// �޸�ʱ��
		save_headVO.setAttributeValue("billno", null);// ���ݺ�
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getOrg()))) {
			save_headVO.setAttributeValue("pkorg",
					getPk_orgByCode(headvo.getOrg()));// ������֯
		} else {
			throw new BusinessException("��������֯���룺" + headvo.getOrg()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("busitype", null);// ҵ������
		if (StringUtils.isNotBlank(headvo.getCreator())) {
			if (StringUtils.isNotBlank(getUserInfo(headvo.getCreator()).get(
					"cuserid"))) {
				save_headVO.setAttributeValue("billmaker",
						getUserInfo(headvo.getCreator()).get("cuserid"));// �Ƶ���
			} else {
				throw new BusinessException("���Ƶ������ƣ�" + headvo.getCreator()
						+ "δ����NC����������");
			}
		}
		save_headVO.setAttributeValue("approver", null);// ������
		save_headVO.setAttributeValue("approvenote", null);// ��������
		save_headVO.setAttributeValue("approvedate", null);// ����ʱ��
		// if("06".equals(srctype)){//EBS-ͨ�����Ϊ04 FN01-Cxx-001 EBSӪ������Ϊ06
		// �ɱ���������ΪFN01-Cxx-002
		// pk_transtype=gettradeTypeByCode("FN01-Cxx-002");
		// }else{
		// pk_transtype=gettradeTypeByCode("FN01-Cxx-001");
		// }

		// ���ݷ�Ʊ����Ƿ�Ϊ0�жϽ�������
		String transtype = null;
		String finstatus = headvo.getFinstatus();
		if ("finapprove".equals(finstatus)) {
			if ("Y".equals(headvo.getDef40())) {
				transtype = "FN01-Cxx-001";
			} else {
				transtype = "FN01-Cxx-006";
			}
		} else {
			transtype = "FN01-Cxx-001";
		}
		String pk_transtype = gettradeTypeByCode(transtype);
		save_headVO.setAttributeValue("transtype", transtype);// ��������
		save_headVO.setAttributeValue("billtype", "FN01");// ��������
		save_headVO.setAttributeValue("transtypepk", pk_transtype);// ��������pk
		save_headVO.setAttributeValue("emendenum", null);// �޶�ö��
		save_headVO.setAttributeValue("billversionpk", null);// ���ݰ汾pk
		save_headVO.setAttributeValue("billdate", new UFDate());// ��������
		save_headVO.setAttributeValue("pk_tradetypeid", null);// ��������
		save_headVO.setAttributeValue("def1", headvo.getEbsid());// EBS����
		save_headVO.setAttributeValue("def2", headvo.getEbsbillcode());// EBS������
		save_headVO.setAttributeValue("def3", headvo.getImagecode());// Ӱ�����
		save_headVO.setAttributeValue("def4",
				headvo.getImagestatus() == null ? "" : "��ɨ��");// Ӱ��״̬
		save_headVO.setAttributeValue("def5", headvo.getContractcode());// ��ͬ����
		save_headVO.setAttributeValue("def6", headvo.getContractname());// ��ͬ����
		save_headVO.setAttributeValue("def7", headvo.getContracttype());// ��ͬ����
		save_headVO.setAttributeValue("def8", headvo.getContractsubclass());// ��ͬϸ��
		save_headVO.setAttributeValue("def9",
				getdefdocBycode(headvo.getEmergency(), "zdy031"));// �����̶�
		save_headVO.setAttributeValue("def10", "EBS����ϵͳ");// ��ϵͳ����Ĭ�ϴ�EBS
		save_headVO.setAttributeValue("def11", headvo.getDef11());// ��ͬID

		save_headVO.setAttributeValue("def12", headvo.getLongproject());// �г�����Ŀ
		// if(StringUtils.isNotBlank(getPk_DeptByCode(headvo.getDept(),
		// save_headVO.getPk_org()))){
		// save_headVO.setAttributeValue("pk_deptid_v",
		// getPk_DeptByCode(headvo.getDept(), save_headVO.getPk_org()));// ���첿��
		// }else{
		// throw new
		// BusinessException("�þ��첿�ű��룺"+headvo.getDept()+"δ����NC����������");
		// }
		// if(StringUtils.isNotBlank(getPsndocPkByCode(headvo.getOperator()))){
		// save_headVO.setAttributeValue("pk_psndoc",
		// getPsndocPkByCode(headvo.getOperator()));// ������
		// }else{
		// throw new
		// BusinessException("�þ����˱��룺"+headvo.getOperator()+"δ����NC����������");
		// }
		if (StringUtils.isNotBlank(getBalatypePkByCode(headvo.getBalatype()))) {
			save_headVO.setAttributeValue("pk_balatype",
					getBalatypePkByCode(headvo.getBalatype()));// ���㷽ʽ
		} else {
			throw new BusinessException("�ý��㷽ʽ���룺" + headvo.getBalatype()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("payaccount", headvo.getPayaccount());// ���������˻�
		save_headVO.setAttributeValue("recaccount", headvo.getBankaccount());// �տ������˻�
		save_headVO.setAttributeValue("def13", null);//
		save_headVO.setAttributeValue("cashaccount", null);// �ֽ��˻�

		// TODO add by huangdq 20200227 ���ȡ�ӿڴ���������� -start-
		// save_headVO.setAttributeValue("money", headvo.getMoney());// ���
		save_headVO.setAttributeValue("money", headvo.getApplymoney());// ���
		// TODO add by huangdq 20200227 ���ȡ�ӿڴ���������� -start-

		save_headVO.setAttributeValue("def14", null);//
		if (StringUtils.isNotBlank(headvo.getPlate())) {
			if (StringUtils.isNotBlank(getdefdocBycode(headvo.getPlate(),
					"bkxx"))) {
				save_headVO.setAttributeValue("def15",
						getdefdocBycode(headvo.getPlate(), "bkxx"));// ���
			} else {
				throw new BusinessException("�ð����룺" + headvo.getPlate()
						+ "δ����NC����������");
			}
		}
		save_headVO.setAttributeValue("def16", null);//
		if (StringUtils.isNotBlank(getPk_orgByCode(headvo.getAccountingorg()))) {
			save_headVO.setAttributeValue("def17",
					getPk_orgByCode(headvo.getAccountingorg()));// ���˹�˾
		} else {
			throw new BusinessException("�ó��˹�˾��" + headvo.getAccountingorg()
					+ "δ����NC����������");
		}
		save_headVO.setAttributeValue("def18",
				getCustPkByCode(headvo.getReceiver()));// �տ
		save_headVO.setAttributeValue("pk_currtype", headvo.getPk_currtype());// ����
		if (StringUtils.isNotBlank(headvo.getProject())) {
			if (StringUtils
					.isNotBlank(getpk_projectByCode(headvo.getProject()))) {
				save_headVO.setAttributeValue("def19",
						getpk_projectByCode(headvo.getProject()));// ��Ŀ
			} else {
				throw new BusinessException("����Ŀ���룺" + headvo.getProject()
						+ "δ����NC����������");
			}
		}
		save_headVO.setAttributeValue("def20", headvo.getIsbuyticket());// �Ƿ��ȸ����Ʊ
		save_headVO.setAttributeValue("def21", headvo.getTotalapplyamount());// �ۼ������
		save_headVO.setAttributeValue("def22", headvo.getTotalamount());// �ۼƸ�����
		save_headVO.setAttributeValue("def23", null);//
		save_headVO.setAttributeValue("def24", headvo.getApplytotalamount());// ��������ۼƸ�����
		save_headVO.setAttributeValue("def25", null);//
		save_headVO.setAttributeValue("def26", null);//
		save_headVO.setAttributeValue("def27", headvo.isIslostimg());// �Ƿ�ȱʧӰ��
		save_headVO.setAttributeValue("def28", null);//
		save_headVO.setAttributeValue("billstatus", null);// ����״̬
		save_headVO.setAttributeValue("approvestatus", -1);// ����״̬
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue(
				"def29",
				Boolean.valueOf(headvo.isIsdelete()) == null ? false : Boolean
						.valueOf(headvo.isIsdelete()));// �Ƿ�����
		save_headVO.setAttributeValue("def30", null);// �Զ�����30
		save_headVO.setAttributeValue("def23", headvo.getIspaynote());// �Ƿ��ȸ����Ʊ
		save_headVO.setAttributeValue("def31", null);// �Զ�����31
		save_headVO.setAttributeValue("def32", null);// �Զ�����32
		// save_headVO.setAttributeValue("def33", null);// �Զ�����33
		// ����code��ȡ������������״̬2020-02-28-̸�ӽ�
		if (StringUtils.isNotBlank(headvo.getFinstatus())) {
			String pk_defdoc = getAuditstateByCode(headvo.getFinstatus());
			if (pk_defdoc == null) {
				throw new BusinessException("������������״̬��" + headvo.getFinstatus()
						+ "��δ����NC���������������Ϣ!");
			}
			save_headVO.setAttributeValue("def33", pk_defdoc);// ������������״̬
		}
		save_headVO.setAttributeValue("def34", null);// �Զ�����34
		save_headVO.setAttributeValue("def35", headvo.getDept());// ���첿��
		save_headVO.setAttributeValue("def36", headvo.getOperator());// ������
		// save_headVO.setAttributeValue("def37", null);// �Զ�����37
		save_headVO.setAttributeValue("def38", null);// �Զ�����38
		save_headVO.setAttributeValue("def39", null);// �Զ�����39
		save_headVO.setAttributeValue("def40", headvo.getDef40());// �Զ�����40
		save_headVO.setAttributeValue("def41", null);// �Զ�����41
		save_headVO.setAttributeValue("def42", null);// �Զ�����42
		save_headVO.setAttributeValue("def43", headvo.getDef43());// �Ǳ�׼ָ�����
		save_headVO.setAttributeValue("def44", headvo.getDef44());// ������
		save_headVO.setAttributeValue("def45", headvo.getDef45());// �Ѳ�ȫ
		save_headVO.setAttributeValue("def46", null);// �Զ�����46
		save_headVO.setAttributeValue("def47", headvo.getDef47());// �Ƿ��¥��ʶ
		save_headVO.setAttributeValue("def48", null);// �Զ�����48
		// TODO add by huangdq 20200227 δ������ɶ�ʱ�����д -start-
		// save_headVO.setAttributeValue("def49", headvo.getApplymoney());//
		// �������δ�����
		save_headVO.setAttributeValue("def49", UFDouble.ZERO_DBL.toString());// �������δ�����
		// TODO add by huangdq 20200227 δ������ɶ�ʱ�����д -end-

		save_headVO.setAttributeValue("def50", null);// �Զ�����50
		save_headVO.setAttributeValue("def61", headvo.getDef61());// �������
		if (StringUtils.isNotBlank(getSupplierIDByCodeOrName(headvo
				.getReceiver()))) {
			save_headVO.setAttributeValue("supplier",
					getSupplierIDByCodeOrName(headvo.getReceiver()));// ��Ӧ��(�տ)
		} else {
			throw new BusinessException("���տ���룺" + headvo.getReceiver()
					+ "δ����NC����������");
		}
		// ��ͷ����֤�������ABSʵ��������ABSʵ�����-2020-04-27-̸�ӽ�-start
		// ��֤�����
		save_headVO.setAttributeValue("def54", headvo.getDef54());
		// ABSʵ������
		save_headVO.setAttributeValue("def55", headvo.getDef55());
		// �Ǽǽ��
		String def37 = "";
		if (null != headvo.getDef37()) {
			UFDouble ufDouble = new UFDouble(headvo.getDef37());
			def37 = ufDouble.div(100).toString();
		}
		save_headVO.setAttributeValue("def37", def37);
		// ��ͷ����֤�������ABSʵ��������ABSʵ�����-2020-04-27-̸�ӽ�-end
		// bpmid
		save_headVO.setAttributeValue("bpmid", headvo.getBpmid());

		// ͨ�ø������뵥���EBS��ʽ-2020-07-10-̸�ӽ�
		String def51 = headvo.getDef51();
		if (def51 != null && !"".equals(def51)) {
			save_headVO.setAttributeValue("def51", def51);// EBS��ʽ
		} else {
			throw new BusinessException("EBS��ʽ����Ϊ��");
		}

		save_headVO.setStatus(VOStatus.NEW);

		List<Business_b> bodylist = new ArrayList<>();
		for (ApplyBillBodyVO applyBillBodyVO : bodyVOs) {
			Business_b save_bodyVO = new Business_b();
			checkBodyTransforExpense(applyBillBodyVO, srctype);// ��ֵ���
			save_bodyVO.setAttributeValue("pk_business_b", null);// ҵ��ҳǩ����
			save_bodyVO
					.setAttributeValue("scomment", applyBillBodyVO.getMemo());// ժҪ
			if (StringUtils.isNotBlank(applyBillBodyVO.getBudgetsubjects())) {
				if (StringUtils.isNotBlank(getBudgetsubByCode(applyBillBodyVO
						.getBudgetsubjects()))) {
					save_bodyVO.setAttributeValue("def1",
							getBudgetsubByCode(applyBillBodyVO
									.getBudgetsubjects()));// Ԥ���Ŀ
				} else {
					throw new BusinessException("��Ԥ���Ŀ���룺"
							+ applyBillBodyVO.getBudgetsubjects()
							+ "δ����NC����������");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getSubjcode())) {
				if (StringUtils.isNotBlank(getInoutPkByCode(applyBillBodyVO
						.getSubjcode()))) {
					save_bodyVO.setAttributeValue("pk_subjcode",
							getInoutPkByCode(applyBillBodyVO.getSubjcode()));// ��֧��Ŀ
				} else {
					throw new BusinessException("����֧��Ŀ���룺"
							+ applyBillBodyVO.getSubjcode() + "δ����NC����������");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getProceedtype())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getProceedtype(), "zdy020"))) {
					save_bodyVO.setAttributeValue(
							"def2",
							getdefdocBycode(applyBillBodyVO.getProceedtype(),
									"zdy020"));// ��������
				} else {
					throw new BusinessException("�ÿ������ͱ��룺"
							+ applyBillBodyVO.getProceedtype() + "δ����NC����������");
				}
			}
			if (StringUtils.isNotBlank(applyBillBodyVO.getBusinessformat())) {
				if (StringUtils.isNotBlank(getdefdocBycode(
						applyBillBodyVO.getBusinessformat(), "ys004"))) {
					save_bodyVO.setAttributeValue(
							"def3",
							getdefdocBycode(
									applyBillBodyVO.getBusinessformat(),
									"ys004"));// ҵ̬
				} else {
					throw new BusinessException("��ҵ̬���룺"
							+ applyBillBodyVO.getBusinessformat()
							+ "δ����NC����������");
				}
			}
			save_bodyVO.setAttributeValue("def4", applyBillBodyVO.getScale());// ����
			save_bodyVO.setAttributeValue("objtype", 1);// ��������

			save_bodyVO.setAttributeValue("def6",
					applyBillBodyVO.getBankraccountcode());// �տ�������к�
			save_bodyVO.setAttributeValue("def7",
					applyBillBodyVO.getBankaccount());// �տ�˻�
			save_bodyVO.setAttributeValue("local_money_de",
					applyBillBodyVO.getPayamount());// ������

			save_bodyVO.setAttributeValue("def8",
					applyBillBodyVO.getOffsetorg());// �ֳ���˹�˾
			save_bodyVO.setAttributeValue("def9", null);// �Զ�����9
			save_bodyVO.setAttributeValue("def10", null);// �Զ�����10
			save_bodyVO.setAttributeValue("def11",
					applyBillBodyVO.getChecktype());// Ʊ������
			save_bodyVO
					.setAttributeValue("def12", applyBillBodyVO.getCheckno());// Ʊ�ݺ�
			save_bodyVO.setAttributeValue("def13", null);// �Զ�����13
			save_bodyVO.setAttributeValue("def14", null);// �Զ�����14
			// save_bodyVO.setAttributeValue("def15", null);// �Զ�����15
			save_bodyVO.setAttributeValue("def16", null);// �Զ�����16
			save_bodyVO.setAttributeValue("def17", null);// �Զ�����17
			save_bodyVO.setAttributeValue("def18", null);// �Զ�����18
			save_bodyVO.setAttributeValue("def19", null);// �Զ�����19
			save_bodyVO.setAttributeValue("def20", null);// �Զ�����20
			save_bodyVO.setAttributeValue("def21",
					applyBillBodyVO.getPayoffaccomunt());// �⸶�˻�
			save_bodyVO.setAttributeValue("def22",
					applyBillBodyVO.getPayoffamount());// �ֳ�/�⸶���
			save_bodyVO.setAttributeValue("def23", null);// �Զ�����23
			save_bodyVO.setAttributeValue("def24", null);// �Զ�����24
			save_bodyVO.setAttributeValue("def25", null);// �Զ�����25
			save_bodyVO.setAttributeValue("def26", null);// �Զ�����26
			save_bodyVO.setAttributeValue("def27", null);// �Զ�����27
			save_bodyVO.setAttributeValue("def28", null);// �Զ�����28
			save_bodyVO.setAttributeValue("def29", null);// �Զ�����29
			save_bodyVO.setAttributeValue("def30", null);// �Զ�����30
			save_bodyVO.setAttributeValue("def31", null);// �Զ�����31
			save_bodyVO.setAttributeValue("def32", null);// �Զ�����32
			save_bodyVO.setAttributeValue("def33", null);// �Զ�����33
			save_bodyVO.setAttributeValue("def34", null);// �Զ�����34
			save_bodyVO.setAttributeValue("def35", null);// �Զ�����35
			save_bodyVO.setAttributeValue("def36", null);// �Զ�����36
			save_bodyVO.setAttributeValue("def37", null);// �Զ�����37
			save_bodyVO.setAttributeValue("def38", null);// �Զ�����38
			save_bodyVO.setAttributeValue("def39", null);// �Զ�����39
			save_bodyVO.setAttributeValue("def40", null);// �Զ�����40
			save_bodyVO.setAttributeValue("def44", null);// �Զ�����41
			save_bodyVO.setAttributeValue("def42", null);// �Զ�����42
			save_bodyVO.setAttributeValue("def43", null);// �Զ�����43
			save_bodyVO.setAttributeValue("def41",
					applyBillBodyVO.getOperationmode());// ������ʽ
			save_bodyVO.setAttributeValue("def45", null);// �Զ�����45
			save_bodyVO.setAttributeValue("def46", null);// �Զ�����46
			save_bodyVO.setAttributeValue("def47",
					applyBillBodyVO.getPayamount());// δ�����
			save_bodyVO.setAttributeValue("def48", null);// �Զ�����48
			save_bodyVO.setAttributeValue("def49", null);// �Զ�����49
			save_bodyVO.setAttributeValue("def50", null);// �Զ�����50
			save_bodyVO.setAttributeValue("def51", applyBillBodyVO.getDef51());// ��Ʊ�����к�
			save_bodyVO.setAttributeValue("def52", applyBillBodyVO.getDef52());// �Ƿ񹲹��˻�
			save_bodyVO.setAttributeValue("pk_payreq", null);// �������뵥_����
			/**
			 * ebs���Ĵ���ͷ��������:objtype ebs���Ĵ���ͷҵ��Ա:psndoc NC�����ڸ������뵥��
			 * ������������:objtype ����ҵ��Ա:def58 2020-07-01-̸�ӽ�
			 */

			String objtype = applyBillBodyVO.getObjtype();
			if (objtype == null || "".equals(objtype)) {
				throw new BusinessException("��������objtype����Ϊ��!");
			}
			if (null != objtype && "3".equals(objtype)) {
				save_bodyVO.setAttributeValue("objtype", objtype);// ��������
				String pk_psndoc = getPk_psndocByCode(applyBillBodyVO
						.getSupplier());
				if (pk_psndoc != null) {
					save_bodyVO.setAttributeValue("def58", pk_psndoc);// ҵ��Ա
				} else {
					throw new BusinessException("ҵ��Ա���룺"
							+ applyBillBodyVO.getSupplier() + "δ����NC����������");
				}

				if (!"".equals(applyBillBodyVO.getRealitybankraccount())
						&& applyBillBodyVO.getRealitybankraccount() != null) {
					String recaccount = getPersonalAccountIDByCode(
							applyBillBodyVO.getRealitybankraccount(), pk_psndoc);

					if ("".equals(recaccount) || recaccount == null) {
						throw new BusinessException("�ù�Ӧ����û�и������˺��ӻ�");
					}

					save_bodyVO.setAttributeValue("def15", recaccount);// �տ������˻�

					// ʡ�ݳ���
					save_bodyVO.setAttributeValue("def43",
							getCityByrecaccount(recaccount).get("province"));
					save_bodyVO.setAttributeValue("def44",
							getCityByrecaccount(recaccount).get("city"));
				}

			} else {
				String supplier = getSupplierIDByCodeOrName(applyBillBodyVO
						.getSupplier());
				if (StringUtils.isNotBlank(supplier)) {
					String pk_supplier = getSupplierIDByCodeOrName(applyBillBodyVO
							.getSupplier());
					save_bodyVO.setAttributeValue("supplier", pk_supplier);// ��Ӧ��
					if (null != objtype && "1".equals(objtype)) {
						save_bodyVO.setAttributeValue("objtype", objtype);// ��������
					}
					if (!"".equals(applyBillBodyVO.getRealitybankraccount())
							&& applyBillBodyVO.getRealitybankraccount() != null) {
						String recaccount = getAccountIDByCode(
								applyBillBodyVO.getRealitybankraccount(),
								pk_supplier);

						if ("".equals(recaccount) || recaccount == null) {
							throw new BusinessException("�ù�Ӧ����û�и������˺��ӻ�");
						}

						save_bodyVO.setAttributeValue("def15", recaccount);// �տ������˻�

						// ʡ�ݳ���
						save_bodyVO
								.setAttributeValue(
										"def43",
										getCityByrecaccount(recaccount).get(
												"province"));
						save_bodyVO.setAttributeValue("def44",
								getCityByrecaccount(recaccount).get("city"));
					}

				} else {
					throw new BusinessException("�ù�Ӧ�̱��룺"
							+ applyBillBodyVO.getSupplier() + "δ����NC����������");
				}

			}

			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setChildrenVO(bodylist.toArray(new Business_b[0]));
		aggvo.setParentVO(save_headVO);

	}

	private String getBalatypeCode(String ebsCode) {
		if ("1".equals(ebsCode)) {
			return "10";// ����ֱ��
		} else if ("2".equals(ebsCode)) {
			return "15";// ֧Ʊ
		} else if ("3".equals(ebsCode)) {
			return "12";// ��¥
		} else if ("4".equals(ebsCode)) {
			return "6";// ���гжһ�Ʊ
		} else if ("5".equals(ebsCode)) {
			return "16";// ��㵥
		} else if ("6".equals(ebsCode)) {
			return "17";// ��Ʊ��ֽ�ʣ�
		} else if ("7".equals(ebsCode)) {
			return "14";// ��Ʊ��������
		}
		return null;
	}

	/**
	 * ���ø����ͷ��ֵ���
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforExpense(ApplyBillHeadVO headvo, String srctype)
			throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("�����������״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getDef40())) {
			throw new BusinessException("��Ʊ�Ƿ�Ϊ0�ֶβ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("EBS�����Ų���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("Ӱ����벻��Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getContractcode())) {
			throw new BusinessException("��ͬ��Ų���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContractname())) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContracttype())) {
			throw new BusinessException("��ͬ���Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("�����̶Ȳ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContractsubclass())) {
			throw new BusinessException("��ͬϸ�಻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getDept())) {
			throw new BusinessException("���첿�Ų���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getReceiver())) {
			throw new BusinessException("�տ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getOperator())) {
			throw new BusinessException("�����˲���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("���㷽ʽ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getApplymoney())) {
			throw new BusinessException("����������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalamount())) {
			throw new BusinessException("�ۼƸ������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalapplyamount())) {
			throw new BusinessException("�ۼ�������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("��鲻��Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getAccountingorg())) {
			throw new BusinessException("���˹�˾����Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getBankaccount())) { throw new
		 * BusinessException("�տ�˻�����Ϊ��"); }
		 */
		if (StringUtils.isBlank(getPk_orgByCode(headvo.getOrg()))) {
			throw new BusinessException("�տ�˻�����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPk_currtype())) {
			throw new BusinessException("���ֲ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getIsbuyticket())) {
			throw new BusinessException("�Ƿ��ȸ����Ʊ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getDef11())) {
			throw new BusinessException("��ͬID����Ϊ��");
		}
	}

	/**
	 * ���ø�������ֵ���
	 * 
	 * @param bodyvo
	 * @throws BusinessException
	 */
	private void checkBodyTransforExpense(ApplyBillBodyVO bodyvo, String srctype)
			throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getPayamount())) {
			throw new BusinessException("�������Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		// if (StringUtils.isBlank(bodyvo.getRealitybankraccount())) {throw new
		// BusinessException("ʵ���տ�����˻�����Ϊ��");}
	}

	/**
	 * �ɱ������ͷ��ֵ���
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforCost(CostApplyBillHeadVO headvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(headvo.getDef47())) {
			throw new BusinessException("��¥��ʶ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("EBS��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("EBS�����Ų���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("Ӱ����벻��Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		if (StringUtils.isBlank(headvo.getContractcode())) {
			throw new BusinessException("��ͬ��Ų���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContractname())) {
			throw new BusinessException("��ͬ���Ʋ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getContractsubclass())) {throw new
		// BusinessException("��ͬϸ�಻��Ϊ��");}
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("�����̶Ȳ���Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getDept())) {throw new
		// BusinessException("���첿�ű��벻��Ϊ��");}
		// if (StringUtils.isBlank(headvo.getOperator())) {throw new
		// BusinessException("�����˱��벻��Ϊ��");}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("���㷽ʽ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalapplymoney())) {
			throw new BusinessException("�ۼ�������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPlate())) {
			throw new BusinessException("��鲻��Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getAccountingorg())) {throw new
		// BusinessException("���˹�˾����Ϊ��");}
		if (StringUtils.isBlank(headvo.getProject())) {
			throw new BusinessException("��Ŀ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalamount())) {
			throw new BusinessException("�ۼƸ������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getApplytotalamount())) {
			throw new BusinessException("��������ۼƸ������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getIsmargin())) {
			throw new BusinessException("�Ƿ��ʱ���۳�����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getTotalnotemoney())) {
			throw new BusinessException("�ۼƷ�Ʊ����Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getExplain())) {throw new
		// BusinessException("˵������Ϊ��");}
		// if (StringUtils.isBlank(headvo.getProjectstages())) {throw new
		// BusinessException("��Ŀ���ڲ���Ϊ��");}
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("�����������״̬����Ϊ��");
		}
		// if (StringUtils.isBlank(headvo.getSignorg())) {throw new
		// BusinessException("ǩԼ��˾����Ϊ��");}
	}

	/**
	 * �ɱ���������ֵ���
	 * 
	 * @param bodyvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkBodyTransforCost(CostApplyBillBodyVO bodyvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getCostsubjects())) {
			throw new BusinessException("�ɱ���Ŀ����Ϊ��");
		}
		// if (StringUtils.isBlank(bodyvo.getSubjcode())) {throw new
		// BusinessException("��֧��Ŀ����Ϊ��");}
		if (StringUtils.isBlank(bodyvo.getRecaccount())) {
			throw new BusinessException("�տ������˻�����Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getPayamount())
				|| "0".equals(bodyvo.getPayamount())) {
			throw new BusinessException("�������Ϊ���Ҳ���Ϊ0");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("ʵ���տ����Ϊ��");
		}
		// if (StringUtils.isBlank(bodyvo.getProceedtype())) {
		// throw new BusinessException("�������Ͳ���Ϊ��");
		// }
	}

	/**
	 * SRM�����ͷ��ֵ���
	 * 
	 * @param headvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkHeadTransforInside(InsideApplyBillHeadVO headvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(headvo.getOrg())) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsid())) {
			throw new BusinessException("�������뵥��������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getEbsbillcode())) {
			throw new BusinessException("�������뵥��Ų���Ϊ��");
		}

		// if (StringUtils.isBlank(headvo.getImagecode())) {
		// throw new BusinessException("Ӱ����벻��Ϊ��");
		// }
		// if (StringUtils.isBlank(headvo.getImagestatus())) {
		// throw new BusinessException("Ӱ��״̬����Ϊ��");
		// }
		/*
		 * if (StringUtils.isBlank(headvo.getPurchasecode())) { throw new
		 * BusinessException("�ɹ�Э����벻��Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getPurchasename())) { throw new
		 * BusinessException("�ɹ�Э�����Ʋ���Ϊ��"); }
		 */
		if (StringUtils.isBlank(headvo.getEmergency())) {
			throw new BusinessException("�����̶Ȳ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getBalatype())) {
			throw new BusinessException("���㷽ʽ����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getContractsign())) {
			throw new BusinessException("��ͬǩԼ������Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getDeptname())) { throw new
		 * BusinessException("���첿�ű��벻��Ϊ��"); } if
		 * (StringUtils.isBlank(headvo.getOperatorname())) { throw new
		 * BusinessException("�����˱��벻��Ϊ��"); }
		 */
		if (StringUtils.isBlank(headvo.getFinstatus())) {
			throw new BusinessException("�����������״̬����Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getMoney())) {
			throw new BusinessException("�����ܽ���Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getPaymentletter())) {
			throw new BusinessException("�Ǳ�׼ָ���������Ϊ��");
		}
		if (StringUtils.isBlank(headvo.getRepairannex())) {
			throw new BusinessException("����������Ϊ��");
		}
		/*
		 * if (StringUtils.isBlank(headvo.getCompleted())) { throw new
		 * BusinessException("�Ѳ�ȫ����Ϊ��"); }
		 */
	}

	/**
	 * SRM��������ֵ���
	 * 
	 * @param bodyvo
	 * @param srctype
	 * @throws BusinessException
	 */
	private void checkBodyTransforInside(InsideApplyBillBodyVO bodyvo,
			String srctype) throws BusinessException {
		if (StringUtils.isBlank(bodyvo.getPayamount())) {
			throw new BusinessException("�������Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getProceedtype())) {
			throw new BusinessException("�������Ͳ���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getSupplier())) {
			throw new BusinessException("��Ӧ�̲���Ϊ��");
		}
		if (StringUtils.isBlank(bodyvo.getRecaccount())) {
			throw new BusinessException("�տ������˻�����Ϊ��");
		}
	}

	private void InspectionContract(String contractcode, String contractname)
			throws BusinessException {

		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select a.pk_fct_ap from fct_ap a  where a.vbillcode = '"
				+ contractcode + "' and a.ctname = '" + contractname
				+ "'  and a.blatest = 'Y' and dr = 0  ");

		String pk_fct_ap = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());

		if ("".equals(pk_fct_ap) || pk_fct_ap == null) {
			throw new BusinessException("����ͬ����ͬ��nc����ͬ���룺" + contractcode
					+ "����ͬ���ƣ�" + contractname + "");
		}
	}

}
