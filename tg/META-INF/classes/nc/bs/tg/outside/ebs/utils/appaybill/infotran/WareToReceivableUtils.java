package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
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

import com.alibaba.fastjson.JSONObject;

/**
 * ���ⵥ��Ӧ�յ�
 * 
 * @author king
 * 
 */
public class WareToReceivableUtils extends EBSBillUtils {

	static WareToReceivableUtils utils;

	public static WareToReceivableUtils getUtils() {
		if (utils == null) {
			utils = new WareToReceivableUtils();
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
		return null;
	}

	/**
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onpushBill(JSONObject bJSONObject) throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		String srcid = bJSONObject.getString("def2");// ��ϵͳ����
		String srcbillcode = bJSONObject.getString("def1");// ��ϵͳ���ݺ�
		// Ŀ��ҵ�񵥾�����������
		String billqueue = bJSONObject.getString("def49") + srcid;
		// Ŀ��ҵ�񵥾���������
		String billkey = bJSONObject.getString("def49") + srcbillcode;
		// ��������ϵͳҵ�񵥾�ID��ѯ��Ӧ�յ��ۺ�VO
		AggReceivableBillVO aggVO = (AggReceivableBillVO) getBillVO(
				AggReceivableBillVO.class, "isnull(dr,0)=0 and def2 = '"
						+ srcid + "'");

		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			// ������������VO����
			AggReceivableBillVO billvo = onTranBill(bJSONObject, billkey);
			HashMap eParam = new HashMap();
			// �������Ƿ��Ѽ��
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggVO != null) {
				String pk = aggVO.getParentVO().getPrimaryKey();
				boolean check = selectRecbill(bJSONObject.getString("def3"), pk);
				if (check) {
					String def43 = bJSONObject.getString("def43");// ���˵���
					String def44 = bJSONObject.getString("def44");// ���˵��ܽ��

					String sql = "update ar_recbill set def18 = '" + def43
							+ "',def19 = '" + def44
							+ "'  where nvl(dr,0)=0 and def3 = '"
							+ bJSONObject.getString("def3")
							+ "' and pk_recbill = '" + pk + "'";
					getBaseDAO().executeUpdate(sql);
					return null;
				}
				ReceivableBillItemVO bvo = new ReceivableBillItemVO();
				bvo = (ReceivableBillItemVO) billvo.getChildrenVO()[0];
				bvo.setPk_recbill(pk);
				bvo.setDr(0);
				getBaseDAO().insertVO(bvo);
				UFDouble money = bvo.getLocal_money_de();
				String sql = "update ar_recbill set money = money+"
						+ money.doubleValue()
						+ " where nvl(dr,0)=0 and pk_recbill = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				getPfBusiAction().processAction("SAVE", "F0-Cxx-001", null,
						billvo, null, eParam);
			}
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			EBSBillUtils.removeBillQueue(billqueue);
		}

		return "��" + billkey + "��," + "�������!";

	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 */
	private AggReceivableBillVO onTranBill(JSONObject bJSONObject,
			String billkey) throws BusinessException {
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO hvo = new ReceivableBillVO();

		String pk_org = getPkByCode(bJSONObject.getString("pk_org"));
		if (!isNull(pk_org)) {
			throw new BusinessException("������֯����Ϊ�գ�������������");
		}
		String pk_vid = getvidByorg(pk_org);

		hvo.setPk_org(pk_org);// ��֯
		hvo.setPk_group("000112100000000005FD");// ����
		hvo.setPk_billtype("F0");// ��������
		hvo.setPk_busitype(getBusitypeID("AR01", hvo.getPk_group())/*"0001ZZ10000000258BF2"*/);// ҵ������
		hvo.setPk_fiorg(pk_org);
		hvo.setPk_currtype("1002Z0100000000001K1");// ����
		hvo.setBillclass("ys");// ���ݴ���
		hvo.setPk_tradetype("F0-Cxx-001");// Ӧ�����ͱ���
		hvo.setPk_tradetypeid(gettradeTypeByCode("F0-Cxx-001"));// Ӧ������
		hvo.setBillstatus(-1);// ����״̬
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setEffectstatus(2);// ��Ч״̬
		hvo.setCreationtime(new UFDateTime());// �Ƶ�ʱ��
		hvo.setCreator(getUserPkByCode("Gadmin1"));
		hvo.setBillmaker(getUserPkByCode("Gadmin1"));// �Ƶ���
		hvo.setIsflowbill(UFBoolean.FALSE);
		hvo.setIsreded(UFBoolean.FALSE);
		hvo.setObjtype(0);
		hvo.setPk_fiorg_v(pk_vid);
		hvo.setSrc_syscode(0);
		hvo.setSyscode(0);
		hvo.setDr(0);
		hvo.setPk_org_v(pk_vid);// ��֯�汾
		// ��ϵͳ��Դ����id
		if (isNull(bJSONObject.getString("def2"))) {
			hvo.setDef2(bJSONObject.getString("def2"));
		} else {
			throw new BusinessException("��ϵͳ��Դ����id ����Ϊ�գ�������������");
		}
		// ��ϵͳ��Դ���ݱ��
		if (isNull(bJSONObject.getString("def1"))) {
			hvo.setDef1(bJSONObject.getString("def1"));
		} else {
			throw new BusinessException("��ϵͳ��Դ���ݱ�� ����Ϊ�գ�������������");
		}
		// �ͻ�
		if (isNull(getcusPkByCode(bJSONObject.getString("customer")))) {
			hvo.setCustomer(getcusPkByCode(bJSONObject.getString("customer")));
		} else {
			throw new BusinessException("�ͻ����δ��NC������������������");
		}

		if (isNull(bJSONObject.getString("def4"))) {
			hvo.setDef14(bJSONObject.getString("def4"));
		} else {
			throw new BusinessException("�ɹ��������� ����Ϊ�գ�������������");
		}
		// ��ϵͳ��Դ���ݱ��
		if (isNull(bJSONObject.getString("def5"))) {
			hvo.setDef15(bJSONObject.getString("def5"));
		} else {
			throw new BusinessException("�ɹ�����id ����Ϊ�գ�������������");
		}

		if (isNull(bJSONObject.getString("def11"))) {
			hvo.setDef16(bJSONObject.getString("def11"));
		} else {
			throw new BusinessException("�ɹ�Э����� ����Ϊ�գ�������������");
		}
		// ��ϵͳ��Դ���ݱ��
		if (isNull(bJSONObject.getString("def12"))) {
			hvo.setDef17(bJSONObject.getString("def12"));
		} else {
			throw new BusinessException("�ɹ�Э������ ����Ϊ�գ�������������");
		}

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			hvo.setDef18(getPkByCode(bJSONObject.getString("def51")));// ��Ŀ��˾
			if ("".equals(hvo.getDef18()) || hvo.getDef18() == null) {
				throw new BusinessException("��Ŀ��˾δ��NC����");
			}
		}

		hvo.setDef19(bJSONObject.getString("def52"));// �ڲ���Ʊ��Ʊ̧ͷ��˾

		// �ڲ������տ��ͬ����
		if (isNull(bJSONObject.getString("def53"))) {
			hvo.setDef20(bJSONObject.getString("def53"));
		} else {
			throw new BusinessException("�ڲ������տ��ͬ���벻��Ϊ�գ�������������");
		}
		// �ڲ������տ��ͬ����
		if (isNull(bJSONObject.getString("def54"))) {
			hvo.setDef21(bJSONObject.getString("def54"));
		} else {
			throw new BusinessException("�ڲ������տ��ͬ���Ʋ���Ϊ�գ�������������");
		}
		// �ڲ�����ID
		if (isNull(bJSONObject.getString("def55"))) {
			hvo.setDef29(bJSONObject.getString("def55"));
		} else {
			throw new BusinessException("�ڲ�����ID����Ϊ�գ�������������");
		}
		// �ڲ����˱���
		if (isNull(bJSONObject.getString("def56"))) {
			hvo.setDef30(bJSONObject.getString("def56"));
		} else {
			throw new BusinessException("�ڲ����˱��벻��Ϊ�գ�������������");
		}

		// ��ͬ���
		/*
		 * if(isNull(bJSONObject.getString("contractcode"))){
		 * hvo.setDef5(bJSONObject.getString("contractcode")); }else { throw new
		 * BusinessException("��ͬ��� ����Ϊ�գ�������������"); }
		 */

		// ��ͬ����
		/*
		 * if(isNull(data.get("contractname"))){ hvo.setDef6((String)
		 * data.get("contractname")); }else { throw new
		 * BusinessException("��ͬ���� ����Ϊ�գ�������������"); }
		 */

		aggvo.setParentVO(hvo);

		ReceivableBillItemVO[] bvos = new ReceivableBillItemVO[1];
		ReceivableBillItemVO bvo = new ReceivableBillItemVO();
		// Ĭ������
		bvo.setCustomer(getcusPkByCode(bJSONObject.getString("customer")));
		bvo.setObjtype(0);
		bvo.setOrdercubasdoc(getcusPkByCode(bJSONObject.getString("customer")));
		bvo.setPk_currtype("1002Z0100000000001K1");
		bvo.setPk_fiorg(pk_org);
		bvo.setPk_org(pk_org);
		bvo.setPk_group("000112100000000005FD");// ����
		bvo.setPk_fiorg_v(pk_vid);
		bvo.setPk_org_v(pk_vid);
		bvo.setPk_recitem("ID_INDEX0");
		bvo.setRececountryid("0001Z010000000079UJJ");
		bvo.setSett_org(pk_org);
		bvo.setSett_org_v(pk_vid);
		bvo.setTriatradeflag(UFBoolean.FALSE);
		bvo.setBilldate(new UFDate());
		bvo.setBillclass("ys");// ���ݴ���
		bvo.setGrouprate(UFDouble.ONE_DBL);
		bvo.setGlobalrate(UFDouble.ONE_DBL);
		bvo.setQuantity_bal(UFDouble.ZERO_DBL);
		bvo.setMoney_bal(UFDouble.ZERO_DBL);
		bvo.setLocal_money_bal(UFDouble.ZERO_DBL);
		bvo.setGroupdebit(UFDouble.ZERO_DBL);
		bvo.setGlobaldebit(UFDouble.ZERO_DBL);
		bvo.setGroupbalance(UFDouble.ZERO_DBL);
		bvo.setGlobalbalance(UFDouble.ZERO_DBL);
		bvo.setGroupnotax_de(UFDouble.ZERO_DBL);
		bvo.setGlobalnotax_de(UFDouble.ZERO_DBL);
		bvo.setOccupationmny(UFDouble.ZERO_DBL);
		bvo.setCaltaxmny(UFDouble.ZERO_DBL);
		bvo.setLocal_notax_de(UFDouble.ZERO_DBL);
		bvo.setRate(UFDouble.ONE_DBL);
		bvo.setRowno(0);
		bvo.setDirection(1);
		bvo.setTaxtype(1);

		// ������id
		if (isNull(bJSONObject.getString("def3"))) {
			bvo.setDef8(bJSONObject.getString("def3"));
		} else {
			throw new BusinessException("������id����Ϊ�գ�������������");
		}

		// �ɹ�������id
		if (isNull(bJSONObject.getString("def6"))) {
			bvo.setDef24(bJSONObject.getString("def6"));
		} else {
			throw new BusinessException("�ɹ�������id����Ϊ�գ�������������");
		}
		// ��Ӧ������˰��
		if (isNull(bJSONObject.getString("def19"))) {
			bvo.setDef9(bJSONObject.getString("def19"));
		} else {
			throw new BusinessException("��Ӧ������˰������Ϊ�գ�������������");
		}
		// ��Ӧ��˰��
		if (isNull(bJSONObject.getString("def20"))) {
			bvo.setDef10(bJSONObject.getString("def20"));
		} else {
			throw new BusinessException("��Ӧ��˰�ʲ���Ϊ�գ�������������");
		}
		// ��Ӧ��������˰��
		if (isNull(bJSONObject.getString("def21"))) {
			bvo.setDef11(bJSONObject.getString("def21"));
		} else {
			throw new BusinessException("��Ӧ��������˰������Ϊ�գ�������������");
		}
		// ��Ӧ����˰�
		if (isNull(bJSONObject.getString("def22"))) {
			bvo.setDef12(bJSONObject.getString("def22"));
		} else {
			throw new BusinessException("��Ӧ����˰�����Ϊ�գ�������������");
		}
		// �ӳ���
		if (isNull(bJSONObject.getString("def23"))) {
			bvo.setDef13(bJSONObject.getString("def23"));
		} else {
			throw new BusinessException("�ӳ��ʲ���Ϊ�գ�������������");
		}

		// ʩ����ͬ���
		if (isNull(bJSONObject.getString("def36"))) {
			bvo.setDef14(bJSONObject.getString("def36"));
		} /*else {
			throw new BusinessException("ʩ����ͬ��Ų���Ϊ�գ�������������");
		}*/
		// ʩ����ͬ����
		if (isNull(bJSONObject.getString("def37"))) {
			bvo.setDef15(bJSONObject.getString("def37"));
		} /*else {
			throw new BusinessException("ʩ����ͬ���Ʋ���Ϊ�գ�������������");
		}*/
		// �ջ���Ŀ
		if (isNull(getpk_projectByCode(bJSONObject.getString("def39")))) {
			bvo.setDef16(getpk_projectByCode(bJSONObject.getString("def39")));
		} /*
		 * else { throw new BusinessException("�ջ���Ŀλ��NC����Ϊ�գ�������������"); }
		 */
		// �ջ���Ŀ����
		if (isNull(getpk_projectByCode(bJSONObject.getString("def41")))) {
			bvo.setDef17(getpk_projectByCode(bJSONObject.getString("def41")));
		} else {
			throw new BusinessException("�ջ���Ŀ���ڲ���Ϊ�գ�������������");
		}
		// ���˵���
		if (isNull(bJSONObject.getString("def43"))) {
			bvo.setDef18(bJSONObject.getString("def43"));
		}

		// ���˵��ܽ��
		if (isNull(bJSONObject.getString("def44"))) {
			bvo.setDef19(bJSONObject.getString("def44"));
		}
		// ��Ӧ���Ƿ�Ʊ
		if (isNull(bJSONObject.getString("def45"))) {
			bvo.setDef20(bJSONObject.getString("def45"));
		} else {
			throw new BusinessException("��Ӧ���Ƿ�Ʊ����Ϊ�գ�������������");
		}
		// ��������
		if (isNull(bJSONObject.getString("def48"))) {
			bvo.setDef23(bJSONObject.getString("def48"));
		} else {
			throw new BusinessException("�������ڲ���Ϊ�գ�������������");
		}

		// ���ϱ���
		if (isNull(bJSONObject.getString("def13"))) {
			bvo.setDef2(bJSONObject.getString("def13"));
		} else {
			throw new BusinessException("���ϱ��� ����Ϊ�գ�������������");
		}
		// ����˵��
		if (isNull(bJSONObject.get("def14"))) {
			bvo.setDef3(bJSONObject.getString("def14"));
		} else {
			throw new BusinessException("����˵�� ����Ϊ�գ�������������");
		}
		// ��λ
		if (isNull(bJSONObject.getString("def15"))) {
			bvo.setDef4(bJSONObject.getString("def15"));
		} else {
			throw new BusinessException("��λ ����Ϊ�գ�������������");
		}
		// ���ε�������
		if (isNull(bJSONObject.getString("def16"))) {
			bvo.setDef5(bJSONObject.getString("def16"));
		} else {
			throw new BusinessException("���ε������� ����Ϊ�գ�������������");
		}
		// ���ۺ�˰
		if (isNull(bJSONObject.getString("def17"))) {
			bvo.setDef6(bJSONObject.getString("def17"));
		} else {
			throw new BusinessException("���ۺ�˰ ����Ϊ�գ�������������");
		}
		// ���۲���˰
		if (isNull(bJSONObject.getString("def18"))) {
			bvo.setDef7(bJSONObject.getString("def18"));
		} else {
			throw new BusinessException("���۲���˰ ����Ϊ�գ�������������");
		}
		// ˰��
		if (isNull(bJSONObject.getString("def25"))) {
			bvo.setTaxrate(new UFDouble(bJSONObject.getString("def25")));
		} else {
			throw new BusinessException("��Ŀ��˾˰�� ����Ϊ�գ�������������");
		}
		// ˰��
		if (isNull(bJSONObject.getString("def27"))) {
			bvo.setLocal_tax_de(new UFDouble(bJSONObject.getString("def27")));
		} else {
			throw new BusinessException("��Ŀ��˾˰�� ����Ϊ�գ�������������");
		}
		// ����˰���
		if (isNull(bJSONObject.getString("def26"))) {
			bvo.setNotax_de(new UFDouble(bJSONObject.getString("def26")));
		} else {
			throw new BusinessException("��Ŀ��˾����˰��� ����Ϊ�գ�������������");
		}
		// ��Ŀ��˾����˰��
		if (isNull(bJSONObject.getString("def24"))) {
			bvo.setLocal_money_de(new UFDouble(bJSONObject.getString("def24")));
			bvo.setMoney_de(new UFDouble(bJSONObject.getString("def24")));
		} else {
			throw new BusinessException("��Ŀ��˾����˰�� ����Ϊ�գ�������������");
		}
		bvos[0] = bvo;
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	/**
	 * ��ȡorg_orgs��pk_orgֵ
	 * 
	 * @param code���������PKֵ
	 *            ��ת��Ϊ���ݿ��е�PKֵ
	 * 
	 * @return ����ת�����PKֵ
	 */
	public String getPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_org from org_orgs where code = '" + code
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_org = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_org;
	}

	/**
	 * ��ȡorg_orgs��pk_orgֵ
	 * 
	 * @param code���������PKֵ
	 *            ��ת��Ϊ���ݿ��е�PKֵ
	 * 
	 * @return ����ת�����PKֵ
	 */
	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}

	/**
	 * ��ȡ��Ŀ��pkֵ
	 * 
	 * @param code���������PKֵ
	 *            ��ת��Ϊ���ݿ��е�PKֵ
	 * 
	 * @return ����ת�����PKֵ
	 */
	public String getprojectBycode(String code) throws DAOException {
		String sql = "select pk_project from bd_project where project_code = '"
				+ code + "' and nvl(dr, 0) =0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}

	/**
	 * ��ȡbd_customer��pk_customerֵ
	 * 
	 * @param code���������PKֵ
	 *            ��ת��Ϊ���ݿ��е�PKֵ
	 * 
	 * @return ����ת�����PKֵ
	 */
	public String getcusPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_customer from bd_customer where code = '"
				+ code + "' and nvl(dr,0)=0";
		String pk_customer = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_customer;
	}

	private Boolean selectRecbill(String def3, String pk)
			throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from ar_recitem where nvl(dr,0)=0 and def8 = '"
				+ def3 + "' and pk_recbill = '" + pk + "' ";

		try {
			result = (int) getBaseDAO()
					.executeQuery(sql, new ColumnProcessor());
		} catch (DAOException e) {
			e.printStackTrace();
		}
		if (result > 0) {
			check = true;
		}

		return check;
	}

	/**
	 * �пշ���
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(Object obj) {
		if (!"".equals(obj) && obj != null) {
			return true;
		}
		return false;

	}
}
