package nc.bs.tg.outside.ebs.utils.margin;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillItemVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * ��֤��->�տ
 * 
 * @author huangxj
 * 
 */
public class MarginPayment extends EBSBillUtils {

	static MarginPayment utils;

	public static MarginPayment getUtils() {
		if (utils == null) {
			utils = new MarginPayment();
		}
		return utils;
	}

	/**
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		JSONObject date = (JSONObject) value.get("data");

		JSONObject jsonObject = (JSONObject) date.get("headInfo");// ��ȡ��ͷ��Ϣ
		String srcid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String srcno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// srcid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
				AggGatheringBillVO.class, "nvl(dr,0)=0 and def1 = '" + srcid
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ srcid + "��,�����ظ��ϴ�!");
		}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���

		AggGatheringBillVO[] aggvo = null;

		try {
			AggGatheringBillVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggGatheringBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F2-Cxx-006", null, billvo, null, eParam);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
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
	private AggGatheringBillVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {

		// json����
		JSONObject date = (JSONObject) value.get("data");
		// json����ͷ
		JSONObject headjson = (JSONObject) date.get("headInfo");
		// ����������
		JSONArray bodyArray = (JSONArray) date.get("itemInfo");

		// ��ͷ��ֵУ��
		CheckHeadNull(headjson);

		AggGatheringBillVO aggvo = new AggGatheringBillVO();
		GatheringBillVO hvo = new GatheringBillVO();
		// ��֯
		String pk_org = getPkByCode(headjson.getString("pk_org"), "pk_org");
		if (!isNull(pk_org)) {
			throw new BusinessException("������֯����δ��NC������������������");
		}
		// ����
		String pk_currtype = getPkByCode(headjson.getString("pk_currtype"),
				"pk_currtype");
		if (!isNull(pk_currtype)) {
			throw new BusinessException("���ֱ���δ��NC������������������");
		}
		// ��֯�汾
		String pk_vid = getPkByCode(pk_org, "pk_vid");

		// ��ͷĬ��ֵ����
		hvo.setPk_org(pk_org);// ��֯
		hvo.setPk_group("000112100000000005FD");// ����
		hvo.setPk_billtype("F2");// ��������
		hvo.setPk_busitype(getBusitypeID("AR02", hvo.getPk_group()));// ҵ������
		hvo.setPk_fiorg(pk_org);
		hvo.setBillclass("sk");// ���ݴ���
		hvo.setPk_tradetype("F2-Cxx-006");// Ӧ�����ͱ���
		hvo.setPk_tradetypeid(getBillTypePkByCode("F2-Cxx-006",
				hvo.getPk_group()));// Ӧ������
		hvo.setBillstatus(-1);// ����״̬
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setEffectstatus(2);// ��Ч״̬
		hvo.setCreationtime(new UFDateTime());// �Ƶ�ʱ��
		hvo.setCreator(getSaleUserID());
		hvo.setBillmaker(getSaleUserID());// �Ƶ���
		hvo.setIsflowbill(UFBoolean.FALSE);
		hvo.setIsreded(UFBoolean.FALSE);
		hvo.setObjtype(0);
		hvo.setPk_fiorg_v(pk_vid);
		hvo.setSrc_syscode(0);
		hvo.setSyscode(0);
		hvo.setDr(0);
		hvo.setPk_org_v(pk_vid);// ��֯�汾

		// ��ͷ����ֵ
		hvo.setPk_currtype(pk_currtype);// ����
		hvo.setDef10(headjson.getString("def10"));// ��ϵͳ����
		hvo.setDef1(headjson.getString("def1"));// ��ϵͳ��Դ����id
		hvo.setDef2(headjson.getString("def2"));// ��ϵͳ��Դ���ݱ��
		hvo.setDef3(headjson.getString("def3"));// Ӱ�����
		hvo.setDef4(headjson.getString("def4"));// Ӱ��״̬
		hvo.setDef6(headjson.getString("def6"));// ��ע
		hvo.setDef20(headjson.getString("pk_psndoc"));// ������

		aggvo.setParentVO(hvo);

		GatheringBillItemVO[] bvos = new GatheringBillItemVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			GatheringBillItemVO bvo = new GatheringBillItemVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			CheckBodyNull(bJSONObject);
			// ����Ĭ��ֵ����

			String pk_supplier = getPkByCode(bJSONObject.getString("def25"),
					"pk_supplier");
			if (!isNull(pk_supplier)) {
				throw new BusinessException("��Ӧ�̱���δ��NC������������������");
			}
			// �տ������˻�
			bvo.setSupplier(pk_supplier);
			String recaccount = getrecaccount(
					bJSONObject.getString("recaccount"), pk_org);
			if (!isNull(recaccount)) {
				throw new BusinessException("�տ������˻�δ��NC������������������");
			}
			// ���������˻�
			bvo.setRecaccount(recaccount);
			String payaccount = getAccountIDByCode(
					bJSONObject.getString("payaccount"), pk_supplier);
			if (!isNull(payaccount)) {
				throw new BusinessException("���������˻�����δ��NC������������������");
			}
			bvo.setPayaccount(payaccount);
			// Ĭ������
			bvo.setObjtype(1);
			bvo.setPk_currtype(pk_currtype);
			bvo.setSupplier(pk_supplier);// ��Ӧ��
			bvo.setPk_billtype("F1");// ��������
			bvo.setPk_tradetype("F2-Cxx-006");// Ӧ�����ͱ���
			bvo.setPk_tradetypeid(getBillTypePkByCode("F2-Cxx-006",
					hvo.getPk_group()));// Ӧ������
			bvo.setPk_fiorg(pk_org);
			bvo.setPk_fiorg_v(pk_vid);
			bvo.setRececountryid("0001Z010000000079UJJ");
			bvo.setSett_org(pk_org);
			bvo.setSett_org_v(pk_vid);
			bvo.setTriatradeflag(UFBoolean.FALSE);
			bvo.setBillclass("sk");// ���ݴ���
			bvo.setGrouprate(UFDouble.ONE_DBL);
			bvo.setGlobalrate(UFDouble.ONE_DBL);
			bvo.setRate(UFDouble.ONE_DBL);
			bvo.setRowno(0);
			bvo.setDirection(1);
			bvo.setTaxtype(1);

			String pk_balatype = null;
			if ("�ֽ�".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("10");
			}
			if ("����".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("13");
			}
			if ("��Ʊ".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("7");
			}
			if (pk_balatype == null) {
				throw new BusinessException("���㷽ʽ��"
						+ bJSONObject.getString("pk_balatype")
						+ "��δ����NC������ѯ�������Ϣ!");
			}
			bvo.setAttributeValue(PayableBillVO.PK_BALATYPE, pk_balatype);// ���㷽ʽ

			/*
			 * String pk_balatype =
			 * getPkByCode(bJSONObject.getString("pk_balatype"), "pk_balatype");
			 * if (!isNull(pk_balatype)) { throw new
			 * BusinessException("���㷽ʽ����δ��NC������������������");
			 */
			// ��ʱĬ�Ͻ��㷽ʽ
			bvo.setDef21(bJSONObject.getString("def21"));// ��������
			bvo.setDef22(bJSONObject.getString("def22"));// ������
			bvo.setDef23(bJSONObject.getString("def23"));// �б�����
			bvo.setDef24(bJSONObject.getString("def24"));// Ͷ�깩Ӧ��id
			bvo.setLocal_money_cr(new UFDouble(bJSONObject
					.getString("local_money_cr")));// Ͷ�걣֤����
			bvo.setMoney_cr(new UFDouble(bJSONObject
					.getString("local_money_cr")));// ����ԭ�ҽ��
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);

		return aggvo;
	}

	/**
	 * 
	 * @param code���������PKֵ
	 *            ��ת��Ϊ���ݿ��е�PKֵ
	 * 
	 * @return ����ת�����PKֵ
	 */
	public String getPkByCode(String code, String key) throws DAOException {

		String sql = null;
		if ("pk_org".equals(key)) {
			sql = "SELECT pk_org from org_orgs where code = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("pk_currtype".equals(key)) {
			sql = "select pk_currtype from bd_currtype where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("pk_balatype".equals(key)) {
			sql = "select pk_balatype from bd_balatype where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("pk_supplier".equals(key)) {
			sql = "select pk_supplier from bd_supplier where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("pk_vid".equals(key)) {
			sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	public String getrecaccount(String code, String pk_org) {
		String result = null;
		String sql = "SELECT distinct bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ "WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ "AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ "AND bd_custbank.pk_bankaccsub != '~' "
				+ "AND bd_bankaccsub.Accnum = '"
				+ code
				+ "' "
				+ "AND exists (select 1 from bd_bankaccbas bas "
				+ "where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ "and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ "and (enablestate = 2) ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���������˻������ȡ��Ӧ����
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 */
	public String getAccountIDByCode(String recaccount, String pk_receiver) {
		String result = null;
		String sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = '"
				+ recaccount
				+ "' "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and (pk_cust = '"
				+ pk_receiver
				+ "' and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust)) ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * �пշ���
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(Object obj) {
		if (obj != null) {
			return true;
		}
		return false;

	}

	/**
	 * ��ͷ��ֵУ��
	 * 
	 * @param headjson
	 */
	public void CheckHeadNull(JSONObject headjson) throws BusinessException {

		if (!isNull(headjson.get("billdate"))) {
			throw new BusinessException("�������ڲ���Ϊ�գ������������");
		}
		if (!isNull(headjson.get("pk_currtype"))) {
			throw new BusinessException("���ֲ���Ϊ�գ������������");
		}
		if (!isNull(headjson.get("pk_psndoc"))) {
			throw new BusinessException("�����˲���Ϊ�գ������������");
		}
		if (!isNull(headjson.get("def1"))) {
			throw new BusinessException("��ϵͳ��������Ϊ�գ������������");
		}
		if (!isNull(headjson.get("def2"))) {
			throw new BusinessException("��ϵͳ���ݺŲ���Ϊ�գ������������");
		}
		if (!isNull(headjson.get("def10"))) {
			throw new BusinessException("��ϵͳ���Ʋ���Ϊ�գ������������");
		}
		// if (!isNull(headjson.get("def3"))) {
		// throw new BusinessException("Ӱ����벻��Ϊ�գ������������");
		// }
		if (!isNull(headjson.get("def4"))) {
			throw new BusinessException("def4����Ϊ�գ������������");
		}

	}

	/**
	 * �����ֵУ��
	 * 
	 * @param bodyjson
	 */
	public void CheckBodyNull(JSONObject bodyjson) throws BusinessException {
		if (!isNull(bodyjson.get("pk_balatype"))) {
			throw new BusinessException("���㷽ʽ����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("def21"))) {
			throw new BusinessException("�������Ͳ���Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("def22"))) {
			throw new BusinessException("�����Ų���Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("def23"))) {
			throw new BusinessException("�б����Ʋ���Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("recaccount"))) {
			throw new BusinessException("�տ������˻�����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("payaccount"))) {
			throw new BusinessException("���������˻�����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("local_money_cr"))) {
			throw new BusinessException("Ͷ�걣֤�����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("def24"))) {
			throw new BusinessException("��Ӧ��ID����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.get("def25"))) {
			throw new BusinessException("Ͷ�깩Ӧ�����Ʋ���Ϊ�գ������������");
		}

	}
}
