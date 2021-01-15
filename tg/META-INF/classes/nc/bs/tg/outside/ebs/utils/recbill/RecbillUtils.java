package nc.bs.tg.outside.ebs.utils.recbill;

import java.util.HashMap;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.tgfn.outbill.OutbillBVO;
import nc.vo.tgfn.outbill.OutbillHVO;

import com.alibaba.fastjson.JSONObject;

/**
 * ���ó��ⵥ
 * 
 * @author king
 * 
 */
public class RecbillUtils extends EBSBillUtils {

	static RecbillUtils utils;

	public static RecbillUtils getUtils() {
		if (utils == null) {
			utils = new RecbillUtils();
		}
		return utils;
	}

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
	public String onRecBill(JSONObject bJSONObject) throws BusinessException {
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		// �����û���
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);

		// JSONObject jsonObject = (JSONObject) value.get("data");// ��ȡ��ͷ��Ϣ
		// JSONObject accrualHeadVO = (JSONObject)
		// jsonObject.get("OutbillHVO");// ��ȡ��ͷ��Ϣ

		String srcid = bJSONObject.getString("def2");// ��ϵͳ����
		String srcno = bJSONObject.getString("def1");// ��ϵͳ���ݺ�
		// Ŀ��ҵ�񵥾�����������
		String billqueue = bJSONObject.getString("def49")
				+ bJSONObject.getString("def3");
		// Ŀ��ҵ�񵥾���������
		String billkey = bJSONObject.getString("def49") + srcno;
		// ��������ϵͳҵ�񵥾�ID��ѯ��Ӧ�����ۺ�VO
		AggOutbillHVO aggVO = (AggOutbillHVO) getBillVO(AggOutbillHVO.class,
				"isnull(dr,0)=0 and def1 = '" + srcid + "'");

		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			// ������������VO����
			AggOutbillHVO billvo = onTranBill(bJSONObject);
			HashMap eParam = new HashMap();
			// �������Ƿ��Ѽ��
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggVO != null) {
				String pk = aggVO.getParentVO().getPk_outbill_h();
				boolean check = selectOutbill(bJSONObject.getString("def3"), pk);
				if (check) {
					/*
					 * throw new BusinessException("��" +
					 * bJSONObject.getString("def3") + "���������ϸ�Ѵ���");
					 */
					String def43 = bJSONObject.getString("def43");// ���˵���
					String def44 = bJSONObject.getString("def44");// ���˵��ܽ��

					String sql = "update tgfn_outbill_o set def24 = '" + def43
							+ "',def25 = '" + def44
							+ "'  where nvl(dr,0)=0 and def14 = '"
							+ bJSONObject.getString("def3")
							+ "' and pk_outbill_h = '" + pk + "'";
					getBaseDAO().executeUpdate(sql);
					return null;
				}
				OutbillBVO bvo = new OutbillBVO();
				bvo = (OutbillBVO) billvo.getChildrenVO()[0];
				bvo.setPk_outbill_h(pk);
				bvo.setDr(0);
				getBaseDAO().insertVO(bvo);
				Double def8 = new Double(aggVO.getParentVO().getDef8())
						+ new Double(billvo.getParentVO().getDef8());
				Double def9 = new Double(aggVO.getParentVO().getDef9())
						+ new Double(billvo.getParentVO().getDef9());
				Double def10 = new Double(aggVO.getParentVO().getDef10())
						+ new Double(billvo.getParentVO().getDef10());
				String sql = "update tgfn_outbill_h set def8 = "
						+ def8.toString() + ",def9 = " + def9.toString()
						+ ",def10 = " + def10.toString()
						+ " where nvl(dr,0)=0 and pk_outbill_h = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				getPfBusiAction().processAction("SAVEBASE", "FN04", null,
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
	private AggOutbillHVO onTranBill(JSONObject bJSONObject)
			throws BusinessException {
		AggOutbillHVO aggvo = new AggOutbillHVO();
		aggvo.getChildrenVO();
		OutbillHVO hvo = new OutbillHVO();
		// json����ͷ

		// JSONObject date = (JSONObject) value.get("data");
		// ����
		// JSONObject headjson = (JSONObject) date.get("OutbillHVO");
		String pk_org = getPkByCode(bJSONObject.getString("pk_org"), "pk_org");
		String supplier = getPkByCode(bJSONObject.getString("supplier"),
				"supplier");
		String customer = getPkByCode(bJSONObject.getString("customer"),
				"customer");
		if (!isNull(pk_org)) {
			throw new BusinessException("������֯δ��NC������������������");
		}
		if (!isNull(supplier)) {
			throw new BusinessException("��Ӧ��δ��NC������������������");
		}
		// json������
		// JSONArray bodyjson = (JSONArray) date.get("OutbillBVO");
		String srcid = bJSONObject.getString("def2");// ��ϵͳid
		String srcno = bJSONObject.getString("def1");// ��ϵͳ���ݺ�
		String purchasecode = bJSONObject.getString("def11");// �ɹ�Э�����
		String purchasename = bJSONObject.getString("def12");// �ɹ�Э������
		String purordercode = bJSONObject.getString("def4");// �ɹ���������
		if ("��Ӧ�����۳���".equals(bJSONObject.get("def49"))) {
			if (!isNull(customer)) {
				throw new BusinessException("�ͻ�δ��NC������������������");
			}
			if (!isNull(purchasecode)) {
				throw new BusinessException("�ɹ�Э����벻��Ϊ�գ�������������");
			}
			if (!isNull(purchasename)) {
				throw new BusinessException("�ɹ�Э�����Ʋ���Ϊ�գ�������������");
			}
		}

		hvo.setPk_group("000112100000000005FD");// ����
		hvo.setPk_org(pk_org);// ��֯
		hvo.setBilltype("FN04");// ����״̬
		hvo.setBillstatus(null);// ����״̬
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setEffectstatus(0);// ��Ч״̬
		hvo.setDef11(supplier);// ��Ӧ��
		hvo.setDef12(customer);// �ͻ�
		hvo.setBilldate(new UFDate());// ��������

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			String def18 = getPkByCode(bJSONObject.getString("def51"), "pk_org");
			if ("".equals(def18) || def18 == null) {
				throw new BusinessException("��Ŀ��˾����,NC�޶�Ӧ��˾");
			}
			hvo.setDef18(def18);// ��Ŀ��˾

		}
		hvo.setDef15(bJSONObject.getString("def52"));// �ڲ���Ʊ��Ʊ̧ͷ��˾
		hvo.setDef16(bJSONObject.getString("def53"));// �ڲ������տ��ͬ����
		hvo.setDef17(bJSONObject.getString("def54"));// �ڲ������տ��ͬ����

		UFDouble dhjebhs = UFDouble.ZERO_DBL;
		UFDouble dhjese = UFDouble.ZERO_DBL;
		UFDouble dhjehs = UFDouble.ZERO_DBL;

		if ("��Ӧ�����۳���".equals(bJSONObject.getString("def49"))) {
			// ���������˰��
			dhjebhs = new UFDouble(bJSONObject.getString("def21"));
			// �����˰�
			dhjese = new UFDouble(bJSONObject.getString("def22"));
			// �������˰��
			dhjehs = new UFDouble(bJSONObject.getString("def19"));
		} else {
			// ���������˰��
			dhjebhs = (new UFDouble(bJSONObject.getString("def26")));
			// �����˰�
			dhjese = new UFDouble(bJSONObject.getString("def27"));
			// �������˰��
			dhjehs = new UFDouble(bJSONObject.getString("def24"));
		}
		hvo.setDef10(dhjese.toString());
		hvo.setDef9(dhjebhs.toString());
		hvo.setDef8(dhjehs.toString());

		if (isNull(srcid)) {
			hvo.setDef1(srcid);
		} else {
			throw new BusinessException(" ��ϵͳid����Ϊ�գ�������������");
		}
		if (isNull(srcno)) {
			hvo.setDef2(srcno);
		} else {
			throw new BusinessException(" ��ϵͳ���ݺŲ���Ϊ�գ�������������");
		}
		if (isNull(purchasecode)) {
			hvo.setDef5(purchasecode);
		}
		if (isNull(purchasename)) {
			hvo.setDef6(purchasename);
		}
		if (isNull(purordercode)) {
			hvo.setDef7(purordercode);
		} else {
			throw new BusinessException(" �ɹ��������벻��Ϊ�գ�������������");
		}
		if (isNull(bJSONObject.getString("def5"))) {
			hvo.setDef13(bJSONObject.getString("def5"));
		} else {
			throw new BusinessException(" �ɹ�����id����Ϊ�գ�������������");
		}

		aggvo.setParentVO(hvo);

		OutbillBVO[] bvos = new OutbillBVO[1];
		for (int i = 0; i < 1; i++) {
			OutbillBVO bvo = new OutbillBVO();
			String def14 = bJSONObject.getString("def3");// ��id
			String def13 = bJSONObject.getString("def48");// ��������
			String project_c = getPkByCode(bJSONObject.getString("def41"),
					"project");// ��Ŀ����
			String supplymny_in = bJSONObject.getString("def19");// ��Ӧ������˰��
			String supply_rate = bJSONObject.getString("def20");// ��Ӧ��˰��
			String supplymny_out = bJSONObject.getString("def21");// ��Ӧ��������˰��
			String supplymny_tax = bJSONObject.getString("def22");// ��Ӧ����˰�
			String additionrate = bJSONObject.getString("def23");// �ӳ���
			String compnymny_in = bJSONObject.getString("def24");// ��Ŀ��˾����˰��
			String compny_rate = bJSONObject.getString("def25");// ��Ŀ��˾˰��
			String compnymny_out = bJSONObject.getString("def26");// ��Ŀ��˾������˰��
			String compnymny_tax = bJSONObject.getString("def27");// ��Ŀ��˾��˰�

			if ("��Ӧ�����۳���".equals(bJSONObject.get("def49"))) {
				if (!isNull(supplymny_in)) {
					throw new BusinessException("��Ӧ������˰������Ϊ�գ�������������");
				}
				if (!isNull(supply_rate)) {
					throw new BusinessException("��Ӧ��˰�ʲ���Ϊ�գ�������������");
				}
				if (!isNull(supplymny_out)) {
					throw new BusinessException("��Ӧ��������˰������Ϊ�գ�������������");
				}
				if (!isNull(supplymny_tax)) {
					throw new BusinessException("��Ӧ����˰�����Ϊ�գ�������������");
				}
			}
			if ("��Ŀ��˾���ó���".equals(bJSONObject.get("def49"))) {
				if (!isNull(additionrate)) {
					throw new BusinessException("�ӳ��ʲ���Ϊ�գ�������������");
				}
				if (!isNull(compnymny_in)) {
					throw new BusinessException("��Ŀ��˾����˰������Ϊ�գ�������������");
				}
				if (!isNull(compny_rate)) {
					throw new BusinessException("��Ŀ��˾˰�ʲ���Ϊ�գ�������������");
				}
				if (!isNull(compnymny_out)) {
					throw new BusinessException("��Ŀ��˾������˰������Ϊ�գ�������������");
				}
				if (!isNull(compnymny_tax)) {
					throw new BusinessException("��Ŀ��˾��˰�����Ϊ�գ�������������");
				}
			}

			if (isNull(def13)) {
				bvo.setDef15(def13);
			}
			if (isNull(def14)) {
				bvo.setDef14(def14);
			}
			if (isNull(project_c)) {
				bvo.setDef3(project_c);
			} else {
				throw new BusinessException("��"
						+ bJSONObject.getString("def49") + "���ջ���Ŀ����δ��NC����");
			}
			if (isNull(supplymny_in)) {
				bvo.setDef5(supplymny_in);
			}

			if (isNull(supply_rate)) {
				bvo.setDef6(supply_rate);
			}

			if (isNull(supplymny_out)) {
				bvo.setDef7(supplymny_out);
			}

			if (isNull(supplymny_tax)) {
				bvo.setDef8(supplymny_tax);
			}

			if (isNull(additionrate)) {
				bvo.setDef9(additionrate);
			}

			if (isNull(compnymny_in)) {
				bvo.setDef10(compnymny_in);
			}

			if (isNull(compny_rate)) {
				bvo.setDef11(compny_rate);
			}

			if (isNull(compnymny_out)) {
				bvo.setDef12(compnymny_out);
			}

			if (isNull(compnymny_tax)) {
				bvo.setDef13(compnymny_tax);
			}

			if (isNull(bJSONObject.getString("def13"))) {
				bvo.setDef16(bJSONObject.getString("def13"));
			} else {
				throw new BusinessException(" ���ϱ��벻��Ϊ�գ�������������");
			}
			if (isNull(bJSONObject.getString("def14"))) {
				bvo.setDef17(bJSONObject.getString("def14"));
			} else {
				throw new BusinessException(" ����˵������Ϊ�գ�������������");
			}
			if (isNull(bJSONObject.getString("def15"))) {
				bvo.setDef18(bJSONObject.getString("def15"));
			} else {
				throw new BusinessException(" ��λ����Ϊ�գ�������������");
			}
			if (isNull(bJSONObject.getString("def16"))) {
				bvo.setDef19(bJSONObject.getString("def16"));
			} else {
				throw new BusinessException(" ���ε�����������Ϊ�գ�������������");
			}
			if (isNull(bJSONObject.getString("def17"))) {
				bvo.setDef20(bJSONObject.getString("def17"));
			} else {
				throw new BusinessException(" ���ۺ�˰����Ϊ�գ�������������");
			}
			if (isNull(bJSONObject.getString("def18"))) {
				bvo.setDef21(bJSONObject.getString("def18"));
			} else {
				throw new BusinessException(" ���۲���˰����Ϊ�գ�������������");
			}

			if (isNull(bJSONObject.getString("def36"))) {
				bvo.setDef22(bJSONObject.getString("def36"));
			} /*else {
				throw new BusinessException(" ʩ����ͬ��Ų���Ϊ�գ�������������");
			}*/
			if (isNull(bJSONObject.getString("def37"))) {
				bvo.setDef23(bJSONObject.getString("def37"));
			} /*
			 * else { throw new BusinessException(" ʩ����ͬ���Ʋ���Ϊ�գ�������������"); }
			 */
			// ���˵���
			if (isNull(bJSONObject.getString("def43"))) {
				bvo.setDef24(bJSONObject.getString("def43"));
			}
			// ���˵��ܽ��
			if (isNull(bJSONObject.getString("def44"))) {
				bvo.setDef25(bJSONObject.getString("def44"));
			}
			// ��Ӧ���Ƿ�Ʊ(Y/N)
			if (isNull(bJSONObject.getString("def45"))) {
				bvo.setDef26("Y".equals(bJSONObject.getString("def45")) ? "Y"
						: "N");
			}
			// �ɹ�������id
			if (isNull(bJSONObject.getString("def6"))) {
				bvo.setDef27(bJSONObject.getString("def6"));
			} else {
				throw new BusinessException(" �ɹ�������id��Ϊ�գ�������������");
			}

			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	/**
	 * 
	 * 
	 * @param code
	 *            ���������PKֵ��ת��Ϊ���ݿ��е�PKֵ
	 * @return ����ת�����PKֵ
	 */
	public String getPkByCode(String code, String key) throws DAOException {

		String result = null;
		String sql = null;
		if ("pk_org".equals(key)) {
			sql = "SELECT pk_org from org_orgs where code = '" + code
					+ "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("project".equals(key)) {
			sql = "SELECT pk_project from bd_project where project_code = '"
					+ code + "' and enablestate = 2 and nvl(dr,0)=0";
		}
		if ("supplier".equals(key)) {
			sql = "SELECT pk_supplier from bd_supplier where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if ("customer".equals(key)) {
			sql = "SELECT pk_customer from bd_customer where code = '" + code
					+ "' and nvl(dr,0)=0";
		}
		if (sql != null) {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}

		return result;
	}

	private Boolean selectOutbill(String def3, String pk)
			throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from tgfn_outbill_o where nvl(dr,0)=0 and def14 = '"
				+ def3 + "' and pk_outbill_h = '" + pk + "'";

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
