package nc.bs.tg.outside.ebs.utils.margin;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
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
public class MarginReceivableUtils extends EBSBillUtils {

	static MarginReceivableUtils utils;

	public static MarginReceivableUtils getUtils() {
		if (utils == null) {
			utils = new MarginReceivableUtils();
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
		String srcid = jsonObject.getString("def2");// ��֤������
		String srcno = jsonObject.getString("def1");// ��֤����
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// srcid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "nvl(dr,0)=0 and def1 = '" + srcno
						+ "'");
		if (aggVO != null) {
			throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ srcno + "��,�����ظ��ϴ�!");
		}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���

		AggReceivableBillVO[] aggvo = null;

		try {
			AggReceivableBillVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			aggvo = (AggReceivableBillVO[]) getPfBusiAction().processAction(
					"SAVE", "F0-Cxx-002", null, billvo, null, eParam);
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
	private AggReceivableBillVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {

		// json����
		JSONObject date = (JSONObject) value.get("data");
		// json����ͷ
		JSONObject headjson = (JSONObject) date.get("headInfo");
		// ����������
		JSONArray bodyArray = (JSONArray) date.get("itemInfo");

		// ��ͷ��ֵУ��
		CheckHeadNull(headjson);

		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO hvo = new ReceivableBillVO();
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
		hvo.setPk_billtype("F0");// ��������
		hvo.setPk_busitype(getBusitypeID("AR01", hvo.getPk_group()));// ҵ������
		hvo.setPk_fiorg(pk_org);
		hvo.setBillclass("ys");// ���ݴ���
		hvo.setPk_tradetype("F0-Cxx-002");// Ӧ�����ͱ���
		hvo.setPk_tradetypeid(getBillTypePkByCode("F0-Cxx-002",
				hvo.getPk_group()));// Ӧ������
		hvo.setBillstatus(-1);// ����״̬
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setEffectstatus(2);// ��Ч״̬
		hvo.setCreationtime(new UFDateTime());// �Ƶ�ʱ��
		hvo.setBilldate(new UFDate());// �Ƶ�ʱ��
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
		hvo.setDef10("EBSϵͳSRM");// ��ϵͳ����
		hvo.setDef1(headjson.getString("def1"));// ��ϵͳ��Դ����id
		hvo.setDef2(headjson.getString("def2"));// ��ϵͳ��Դ���ݱ��
		hvo.setDef3(headjson.getString("def3"));// Ӱ�����
		hvo.setDef4(headjson.getString("def4"));// Ӱ��״̬
		hvo.setDef6(headjson.getString("def6"));// ��ע
		hvo.setDef11(headjson.getString("def11"));// ������
		hvo.setDef12(headjson.getString("def12"));// ��ע
		hvo.setAttributeValue("bpmid", headjson.getString("bpmid"));
		
		if (headjson.getString("def13") != null) {
			hvo.setDef13(headjson.getString("def13"));// �Ƿ�û
		} else {
			throw new BusinessException("�Ƿ�û����Ϊ��");
		}

		aggvo.setParentVO(hvo);

		ReceivableBillItemVO[] bvos = new ReceivableBillItemVO[bodyArray.size()];
		for (int i = 0; i < bvos.length; i++) {
			ReceivableBillItemVO bvo = new ReceivableBillItemVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			CheckBodyNull(bJSONObject);
			// ����Ĭ��ֵ����

			String pk_supplier = getPkByCode(bJSONObject.getString("supplier"),
					"pk_customer");
			if (!isNull(pk_supplier)) {
				throw new BusinessException("Ͷ�깩Ӧ�̱���δ��NC������������������");
			}
			bvo.setCustomer(pk_supplier);
			// �տ������˻�
			String recaccount = getrecaccount(
					bJSONObject.getString("recaccount"), pk_org);
			if (!isNull(recaccount)) {
				throw new BusinessException("�ù�Ӧ�����޸��տ������˻���������������");
			}

			bvo.setRecaccount(recaccount);
			if (isNull(bJSONObject.getString("payaccount"))) {
				// ���������˻�
				bvo.setRecaccount(recaccount);
				String payaccount = getAccountIDByCode(
						bJSONObject.getString("payaccount"), pk_supplier);
				if (!isNull(payaccount)) {
					throw new BusinessException("���������˻�����δ��NC������������������");
				}
				bvo.setPayaccount(payaccount);
			}
			// Ĭ������
			bvo.setObjtype(0);
			bvo.setPk_currtype(pk_currtype);
			bvo.setSupplier(pk_supplier);// ��Ӧ��
			bvo.setPk_billtype("F0");// ��������
			bvo.setPk_tradetype("F0-Cxx-002");// Ӧ�����ͱ���
			bvo.setPk_tradetypeid(getBillTypePkByCode("F0-Cxx-002",
					hvo.getPk_group()));// Ӧ������
			bvo.setPk_fiorg(pk_org);
			bvo.setPk_fiorg_v(pk_vid);
			bvo.setSett_org(pk_org);
			bvo.setSett_org_v(pk_vid);
			bvo.setTriatradeflag(UFBoolean.FALSE);
			bvo.setBillclass("ys");// ���ݴ���
			bvo.setGrouprate(UFDouble.ONE_DBL);
			bvo.setGlobalrate(UFDouble.ONE_DBL);
			bvo.setRate(UFDouble.ONE_DBL);
			bvo.setRowno(0);
			bvo.setDirection(1);
			bvo.setTaxtype(1);

			String pk_balatype = null;
			if ("����".equals(bJSONObject.getString("pk_balatype"))) {
				pk_balatype = getBalatypePkByCode("1");
			} else {
				pk_balatype = getBalatypePkByCode(bJSONObject
						.getString("pk_balatype"));
			}

			if (pk_balatype == null || "".equals(pk_balatype)) {
				throw new BusinessException("���㷽ʽ����δ��NC������������������");
			}

			bvo.setAttributeValue("pk_balatype", pk_balatype);// ���㷽ʽ

			/*
			 * String pk_balatype =
			 * getPkByCode(bJSONObject.getString("pk_balatype"), "pk_balatype");
			 * if (!isNull(pk_balatype)) { throw new
			 * BusinessException("���㷽ʽ����δ��NC������������������");
			 */
			// ��ʱĬ�Ͻ��㷽ʽ
			bvo.setDef10(bJSONObject.getString("def10"));// ��������
			bvo.setDef11(bJSONObject.getString("def11"));// �б걣֤������
			bvo.setDef9(bJSONObject.getString("def9"));// Ͷ�깩Ӧ��id
			bvo.setLocal_money_de(new UFDouble(bJSONObject
					.getString("local_money_de")));// Ͷ�걣֤����
			bvo.setMoney_de(new UFDouble(bJSONObject
					.getString("local_money_de")));// ����ԭ�ҽ��
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
			sql = "SELECT pk_org from org_orgs where (code = '" + code
					+ "' or name = '" + code
					+ "' ) and enablestate = 2 and nvl(dr,0)=0";
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
			sql = "select pk_supplier from bd_supplier where ( code = '" + code
					+ "' or name = '" + code + "' ) and nvl(dr,0)=0";
		}
		if ("pk_customer".equals(key)) {
			sql = "select pk_customer from bd_customer where ( code = '" + code
					+ "' or name = '" + code + "' ) and nvl(dr,0)=0";
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
		String sql = "select pk_bankaccsub from bd_bankaccsub where pk_bankaccbas = "
				+ "(select pk_bankaccbas from bd_bankaccbas where ( accclass = 2 ) "
				+ "and ( enablestate in ( 2, 1 ) and ( pk_group = '000112100000000005FD' AND "
				+ "accnum = '" + code + "' and pk_org = '" + pk_org + "')))  ";
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
		if (obj != null && !"".equals((String) obj)) {
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
		if (!isNull(headjson.getString("pk_org"))) {
			throw new BusinessException("������֯����Ϊ�գ������������");
		}
		if (!isNull(headjson.getString("pk_currtype"))) {
			throw new BusinessException("���ֲ���Ϊ�գ������������");
		}
		if (!isNull(headjson.getString("def1"))) {
			throw new BusinessException("��ϵͳ��������Ϊ�գ������������");
		}
		if (!isNull(headjson.getString("def2"))) {
			throw new BusinessException("��ϵͳ���ݺŲ���Ϊ�գ������������");
		}
		// if (!isNull(headjson.get("def3"))) {
		// throw new BusinessException("Ӱ����벻��Ϊ�գ������������");
		// }
		// if (!isNull(headjson.getString("def4"))) {
		// throw new BusinessException("def4����Ϊ�գ������������");
		// }

	}

	/**
	 * �����ֵУ��
	 * 
	 * @param bodyjson
	 */
	public void CheckBodyNull(JSONObject bodyjson) throws BusinessException {
		if (!isNull(bodyjson.getString("pk_balatype"))) {
			throw new BusinessException("���㷽ʽ����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.getString("def9"))) {
			throw new BusinessException("��Ӧ��ID����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.getString("def10"))) {
			throw new BusinessException("�������Ͳ���Ϊ�գ������������");
		}
		if (!isNull(bodyjson.getString("def11"))) {
			throw new BusinessException("�б걣֤�����Ͳ���Ϊ�գ������������");
		}
		if (!isNull(bodyjson.getString("recaccount"))) {
			throw new BusinessException("�տ������˻�����Ϊ�գ������������");
		}
		/*
		 * if (!isNull(bodyjson.get("payaccount"))) { throw new
		 * BusinessException("���������˻�����Ϊ�գ������������"); }
		 */
		if (!isNull(bodyjson.getString("local_money_de"))) {
			throw new BusinessException("Ͷ�걣֤�����Ϊ�գ������������");
		}
		if (!isNull(bodyjson.getString("supplier"))) {
			throw new BusinessException("Ͷ�깩Ӧ�����Ʋ���Ϊ�գ������������");
		}

	}
}
