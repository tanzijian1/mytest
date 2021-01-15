package nc.bs.tg.outside.ebs.utils.appaybill.infotran;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSONObject;

/**
 * ������Ӧ����
 * 
 * @author king
 * 
 */
public class ComCopewithUtils extends EBSBillUtils {

	static ComCopewithUtils utils;

	public static ComCopewithUtils getUtils() {
		if (utils == null) {
			utils = new ComCopewithUtils();
		}
		return utils;
	}

	/**
	 * 
	 * @param value
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
		AggPayableBillVO aggVO = (AggPayableBillVO) getBillVO(
				AggPayableBillVO.class, "isnull(dr,0)=0 and def2 = '" + srcid
						+ "'");

		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			// ������������VO����
			AggPayableBillVO billvo = onTranBill(bJSONObject, billkey);
			HashMap eParam = new HashMap();
			// �������Ƿ��Ѽ��
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			if (aggVO != null) {
				String pk = aggVO.getParentVO().getPrimaryKey();
				PayableBillItemVO bvo = new PayableBillItemVO();
				bvo = (PayableBillItemVO) billvo.getChildrenVO()[0];
				bvo.setPk_payablebill(pk);
				bvo.setDr(0);
				getBaseDAO().insertVO(bvo);
				// UFDouble money = new UFDouble(bJSONObject.getString("def24"))
				// .add((UFDouble) aggVO.getParentVO().getAttributeValue(
				// "money"));
				// String sql = "update ap_payablebill set money = '" + money
				// + "' where pk_payablebill = '" + pk + "'";
				// getBaseDAO().executeUpdate(sql);
				// getBaseDAO().insertVO(bvo);
				UFDouble money = new UFDouble(bJSONObject.getString("def24"));
				String sql = "update ap_payablebill set money = money+"
						+ money.doubleValue()
						+ " where nvl(dr,0)=0 and pk_payablebill = '" + pk
						+ "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				getPfBusiAction().processAction("SAVE", "F1-Cxx-006", null,
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
	private AggPayableBillVO onTranBill(JSONObject bJSONObject, String billkey)
			throws BusinessException {
		AggPayableBillVO aggvo = new AggPayableBillVO();
		PayableBillVO hvo = new PayableBillVO();

		String pk_org = getPkByCode(bJSONObject.getString("pk_org"));
		if (!isNull(pk_org)) {
			throw new BusinessException("������֯����Ϊ�գ�������������");
		}
		String pk_vid = getvidByorg(pk_org);

		hvo.setPk_org(pk_org);// ��֯
		hvo.setPk_group("000112100000000005FD");// ����
		hvo.setPk_billtype("F1");// ��������
		hvo.setPk_busitype(getBusitypeID("AR01", hvo.getPk_group()));// ҵ������
		hvo.setPk_fiorg(pk_org);
		hvo.setPk_currtype("1002Z0100000000001K1");// ����
		hvo.setBillclass("yf");// ���ݴ���
		hvo.setPk_tradetype("F1-Cxx-006");// Ӧ�����ͱ���
		hvo.setPk_tradetypeid(gettradeTypeByCode("F1-Cxx-006"));// Ӧ������
		hvo.setBillstatus(-1);// ����״̬
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setEffectstatus(2);// ��Ч״̬
		hvo.setCreationtime(new UFDateTime());// �Ƶ�ʱ��
		hvo.setCreator(getUserPkByCode("Gadmin1"));
		hvo.setBillmaker(getUserPkByCode("Gadmin1"));// �Ƶ���
		hvo.setIsflowbill(UFBoolean.FALSE);
		hvo.setIsreded(UFBoolean.FALSE);
		hvo.setObjtype(1);
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
		if (isNull(getcusPkByCode(bJSONObject.getString("supplier")))) {
			hvo.setSupplier(getcusPkByCode(bJSONObject.getString("supplier")));
		} else {
			throw new BusinessException("��Ӧ�̱��δ��NC������������������");
		}
		// �ɹ��������
		if (isNull(bJSONObject.getString("def4"))) {
			hvo.setDef5(bJSONObject.getString("def4"));
		} else {
			throw new BusinessException("�ɹ�������� ����Ϊ�գ�������������");
		}
		// �ɹ�����id
		if (isNull(bJSONObject.getString("def5"))) {
			hvo.setDef6(bJSONObject.getString("def5"));
		} else {
			throw new BusinessException("�ɹ�����id ����Ϊ�գ�������������");
		}
		// �ջ���Ŀ����
		if (isNull(bJSONObject.getString("def41"))) {
			hvo.setAttributeValue("Def32",
					getProjectPkByCode(bJSONObject.getString("def41")));
		} else {
			throw new BusinessException("�ջ���Ŀ����δ��NC������������������");
		}
		// �ڲ���Ʊ��Ʊ̧ͷ��˾
		if (isNull(bJSONObject.getString("def52"))) {
			hvo.setDef56(bJSONObject.getString("def52"));
		}
		// �ڲ������տ��ͬ����
		if (isNull(bJSONObject.getString("def53"))) {
			hvo.setDef57(bJSONObject.getString("def53"));
		} else {
			throw new BusinessException("�ڲ������տ��ͬ���벻��Ϊ�գ�������������");
		}
		// �ڲ������տ��ͬ����
		if (isNull(bJSONObject.getString("def54"))) {
			hvo.setDef58(bJSONObject.getString("def54"));
		} else {
			throw new BusinessException("�ڲ������տ��ͬ���Ʋ���Ϊ�գ�������������");
		}
		// �ڲ�����ID
		if (isNull(bJSONObject.getString("def55"))) {
			hvo.setDef35(bJSONObject.getString("def55"));
		} else {
			throw new BusinessException("�ڲ�����ID����Ϊ�գ�������������");
		}
		// �ڲ����˱���
		if (isNull(bJSONObject.getString("def56"))) {
			hvo.setDef36(bJSONObject.getString("def56"));
		} else {
			throw new BusinessException("�ڲ����˱��벻��Ϊ�գ�������������");
		}

		aggvo.setParentVO(hvo);

		PayableBillItemVO[] bvos = new PayableBillItemVO[1];
		PayableBillItemVO bvo = new PayableBillItemVO();
		// Ĭ������
		bvo.setSupplier(getcusPkByCode(bJSONObject.getString("supplier")));
		bvo.setObjtype(1);
		bvo.setOrdercubasdoc(getcusPkByCode(bJSONObject.getString("supplier")));
		bvo.setPk_currtype("1002Z0100000000001K1");
		hvo.setPk_billtype("F1");// ��������
		bvo.setPk_tradetype("F1-Cxx-006");// Ӧ�����ͱ���
		bvo.setPk_tradetypeid(gettradeTypeByCode("F1-Cxx-006"));// Ӧ������
		bvo.setPk_fiorg(pk_org);
		bvo.setPk_fiorg_v(pk_vid);
		bvo.setPk_fiorg(pk_org);
		bvo.setPk_org(pk_org);
		bvo.setPk_group("000112100000000005FD");// ����
		bvo.setPk_org_v(pk_vid);
		bvo.setRececountryid("0001Z010000000079UJJ");
		bvo.setSett_org(pk_org);
		bvo.setSett_org_v(pk_vid);
		bvo.setTriatradeflag(UFBoolean.FALSE);
		bvo.setBilldate(new UFDate());
		bvo.setBillclass("yf");// ���ݴ���
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

		if (isNull(bJSONObject.getString("def3"))) {
			bvo.setDef21(bJSONObject.getString("def3"));
		} else {
			throw new BusinessException("������id����Ϊ�գ�������������");
		}
		// ���ϱ���
		if (isNull(bJSONObject.getString("def13"))) {
			bvo.setDef16(bJSONObject.getString("def13"));
		} else {
			throw new BusinessException("���ϱ��벻��Ϊ�գ�������������");
		}
		// ����˵��
		if (isNull(bJSONObject.getString("def14"))) {
			bvo.setDef17(bJSONObject.getString("def14"));
		} else {
			throw new BusinessException("����˵������Ϊ�գ�������������");
		}
		// ��λ
		if (isNull(bJSONObject.getString("def15"))) {
			bvo.setDef18(bJSONObject.getString("def15"));
		} else {
			throw new BusinessException("��λ����Ϊ�գ�������������");
		}
		// ���ε�������
		if (isNull(bJSONObject.getString("def16"))) {
			bvo.setDef19(bJSONObject.getString("def16"));
		} else {
			throw new BusinessException("���ε�����������Ϊ�գ�������������");
		}
		// ���ۺ�˰
		if (isNull(bJSONObject.getString("def17"))) {
			bvo.setDef20(bJSONObject.getString("def17"));
		}
		// ���۲���˰
		if (isNull(bJSONObject.getString("def18"))) {
			bvo.setDef60(bJSONObject.getString("def18"));
		}
		// ��Ӧ������˰��
		if (isNull(bJSONObject.getString("def19"))) {
			bvo.setDef22(bJSONObject.getString("def19"));
		} else {
			throw new BusinessException("��Ӧ������˰������Ϊ�գ�������������");
		}
		// ��Ӧ��˰��
		if (isNull(bJSONObject.getString("def20"))) {
			bvo.setDef23(bJSONObject.getString("def20"));
		} else {
			throw new BusinessException("��Ӧ��˰����Ϊ�գ�������������");
		}
		// ��Ӧ��������˰��
		if (isNull(bJSONObject.getString("def21"))) {
			bvo.setDef24(bJSONObject.getString("def21"));
		} else {
			throw new BusinessException("��Ӧ��������˰������Ϊ�գ�������������");
		}
		// ��Ӧ����˰�
		if (isNull(bJSONObject.getString("def22"))) {
			bvo.setDef25(bJSONObject.getString("def22"));
		} else {
			throw new BusinessException("��Ӧ����˰�����Ϊ�գ�������������");
		}
		// �ӳ���
		if (isNull(bJSONObject.getString("def23"))) {
			bvo.setDef26(bJSONObject.getString("def23"));
		} else {
			throw new BusinessException("�ӳ��ʲ���Ϊ�գ�������������");
		}
		// ʩ����ͬ���
		if (isNull(bJSONObject.getString("def36"))) {
			bvo.setDef27(bJSONObject.getString("def36"));
		} /*else {
			throw new BusinessException("ʩ����ͬ��Ų���Ϊ�գ�������������");
		}*/
		// ʩ����ͬ����
		if (isNull(bJSONObject.getString("def37"))) {
			bvo.setDef28(bJSONObject.getString("def37"));
		} /*else {
			throw new BusinessException("ʩ����ͬ���Ʋ���Ϊ�գ�������������");
		}*/
		// ����ȷ������
		if (isNull(bJSONObject.getString("def38"))) {
			bvo.setDef29(bJSONObject.getString("def38"));
		} else {
			throw new BusinessException("����ȷ�����ڲ���Ϊ�գ�������������");
		}

		// �ɹ�������id
		if (isNull(bJSONObject.getString("def6"))) {
			bvo.setDef59(bJSONObject.getString("def6"));
		} else {
			throw new BusinessException("�ɹ�������id����Ϊ�գ�������������");
		}

		// ˰��
		if (isNull(bJSONObject.getString("def25"))) {
			bvo.setTaxrate(new UFDouble(bJSONObject.getString("def25")));
		} else {
			throw new BusinessException("��Ŀ��˾˰�� ����Ϊ�գ�������������");
		}
		// ˰��
		if (isNull(bJSONObject.getString("def27"))) {
			bvo.setLocal_tax_cr(new UFDouble(bJSONObject.getString("def27")));
		} else {
			throw new BusinessException("��Ŀ��˾˰�� ����Ϊ�գ�������������");
		}
		// ����˰���
		if (isNull(bJSONObject.getString("def26"))) {
			bvo.setLocal_notax_cr(new UFDouble(bJSONObject.getString("def26")));
		} else {
			throw new BusinessException("��Ŀ��˾����˰��� ����Ϊ�գ�������������");
		}
		// ��˰�ϼ�
		if (isNull(bJSONObject.getString("def24"))) {
			bvo.setLocal_money_cr(new UFDouble(bJSONObject.getString("def24")));
			bvo.setMoney_cr(new UFDouble(bJSONObject.getString("def24")));
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

	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}

	public String getProjectPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_project from bd_project where project_code = '"
				+ code + "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}

	public String getcusPkByCode(String code) throws DAOException {
		String sql = "SELECT pk_supplier from bd_supplier where code = '"
				+ code + "' and nvl(dr,0)=0";
		String pk_supplier = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_supplier;
	}

	/**
	 * �пշ���
	 * 
	 * @param obj
	 * @return
	 */
	public Boolean isNull(String obj) {
		if (!"".equals(obj) && obj != null) {
			return true;
		}
		return false;

	}

}
