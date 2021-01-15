package nc.bs.tg.outside.ebs.utils.landfundshare;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;

import com.alibaba.fastjson.JSONObject;

/**
 * ���ؿ��̯����
 * 
 * @author king
 * 
 */
public class LandFundsShareUtils extends EBSBillUtils {

	static LandFundsShareUtils utils;

	/**
	 * ��ȡ���ط�̯����ʵ��
	 * 
	 * @return
	 */
	public static LandFundsShareUtils getUtils() {
		if (utils == null) {
			utils = new LandFundsShareUtils();
		}
		return utils;
	}

	/**
	 * ͬ������
	 * 
	 * @param value
	 *            ����Ľ�����
	 * @param dectype
	 *            Ŀ�굥��
	 * @param srctype
	 *            ҵ�񵥾�
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

		// json����
		JSONObject date = (JSONObject) value.get("data");

		JSONObject accrualHeadVO = (JSONObject) date.get("headInfo");// ��ȡ��ͷ��Ϣ

		String srcid = accrualHeadVO.getString("def1");// ��ϵͳ����
		String srcno = accrualHeadVO.getString("def3");// ��ϵͳ���ݺ�
		// Ŀ��ҵ�񵥾�����������
		String billqueue = EBSCont.getBillNameMap().get(dectype) + ":" + srcid;
		// Ŀ��ҵ�񵥾���������
		String billkey = EBSCont.getBillNameMap().get(dectype) + ":" + srcno;
		// ��������ϵͳҵ�񵥾�ID��ѯ��Ӧ�����ۺ�VO
		AggContractAptmentVO aggVO = (AggContractAptmentVO) getBillVO(
				AggContractAptmentVO.class, "isnull(dr,0)=0 and def1 = '"
						+ srcid + "'");

		// ��Ϊ���׳�����
		if (aggVO != null) {
			throw new BusinessException("��"
					+ billkey
					+ "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
					+ aggVO.getParentVO().getAttributeValue(
							PayableBillVO.BILLNO) + "��,�����ظ��ϴ�!");
		}
		EBSBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			// ����Դ��Ϣת����NC��Ϣ
			AggContractAptmentVO billvo = onTranBill(value, dectype);
			HashMap eParam = new HashMap();
			// �������Ƿ��Ѽ��
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			// ��������ͱ���
			getPfBusiAction().processAction("SAVEBASE", "FN02", null, billvo,
					null, eParam);
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
	private AggContractAptmentVO onTranBill(HashMap<String, Object> value,
			String dectype) throws BusinessException {
		// ��ͷVO
		AggContractAptmentVO aggvo = new AggContractAptmentVO();
		aggvo.getChildrenVO();
		ContractAptmentVO hvo = new ContractAptmentVO();
		// json����
		JSONObject date = (JSONObject) value.get("data");
		// json����ͷ
		JSONObject headjson = (JSONObject) date.get("headInfo");

		String pk_org = headjson.getString("pk_org");
		if (!isNull(pk_org)) {
			throw new BusinessException("������֯����Ϊ�գ�������������");
		}
		String pk_org_id = getRefAttributePk("pk_org", pk_org);
		hvo.setPk_org(pk_org_id);
		// ��ϵͳ����
		String def1 = headjson.getString("def1");
		// ��ϵͳ���ݺ�
		String def3 = headjson.getString("def3");
		// ����ͨ�ú�ͬ����
		String def4 = headjson.getString("def4");
		// �ɱ���ͬ����
		String def5 = headjson.getString("def5");
		// ��̯ǰ��Ŀ���ڱ���
		String def6 = getRefAttributePk("bd_project",
				headjson.getString("def6"), pk_org_id, pk_org_id);
		// ��̯����Ŀ���ڱ���
		String def7 = getRefAttributePk("bd_project",
				headjson.getString("def7"), pk_org_id, pk_org_id);
		// ��̯���
		Double def9 = headjson.getDouble("def9");
		// ��������
		hvo.setDbilldate(new UFDate());

		hvo.setPk_group("000112100000000005FD");

		if (isNull(def1)) {
			hvo.setDef1(def1);
		} else {
			throw new BusinessException("��ϵͳ��������Ϊ�գ�������������");
		}
		// Ĭ��
		hvo.setDef2("EBSϵͳ�ɱ�");
		hvo.setStatus(VOStatus.NEW);
		hvo.setApprovestatus(-1);// ����״̬
		hvo.setBilltype("FN02");//��������
		if (isNull(def3)) {
			hvo.setDef3(def3);
		} else {
			throw new BusinessException("��ϵͳ���ݺŲ���Ϊ�գ�������������");
		}

		if (isNull(def4)) {
			hvo.setDef4(def4);
		} else {
			throw new BusinessException("����ͨ�ú�ͬ���벻��Ϊ�գ�������������");
		}

		if (isNull(def5)) {
			hvo.setDef5(def5);
		} else {
			throw new BusinessException("�ɱ���ͬ���벻��Ϊ�գ�������������");
		}
		if (isNull(def6)) {
			hvo.setDef6(def6);
		} else {
			throw new BusinessException("��̯ǰ��Ŀ���ڱ��벻��Ϊ�գ�������������");
		}
		if (isNull(def7)) {
			hvo.setDef7(def7);
		} else {
			throw new BusinessException("��̯����Ŀ���ڱ��벻��Ϊ�գ�������������");
		}
		if (isNull(def9)) {
			hvo.setDef9(def9.toString());
		} else {
			throw new BusinessException("��̯����Ϊ�գ�������������");
		}
		aggvo.setParentVO(hvo);
		return aggvo;
	}

	/**
	 * ���ݴ���ļ���
	 * 
	 * @param key
	 *            ����
	 * @param conditions
	 *            ���ݿ�����
	 * @return
	 * @throws DAOException
	 */
	private String getRefAttributePk(String key, String... conditions)
			throws BusinessException {
		String code = conditions[0];
		String pkValue = null;
		String sql = null;
		BaseDAO dao = getBaseDAO();
		SQLParameter parameter = new SQLParameter();
		if ("bd_project".equals(key)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			sql = "select  bd_project.pk_project from bd_project bd_project  left   join  bd_project_b b on bd_project.PK_PROJECT=b.PK_PROJECT   where "
					+ "project_code = ? and ( bd_project.dr = 0  ) and (enablestate = 2)  and ( deletestate is null or deletestate<>1) and (   b.dr = 0)  and ("
					+ "b.PK_PARTI_ORG=?  or " + "b.PK_ORG =? )";

			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ��Ŀ���ڱ���" + code
							+ "δ����NC�����й���");
				}
			} catch (Exception e) {
				throw new BusinessException("��ͷ��Ŀ���ڱ���" + code + "δ����NC�����й���");
			}
		} else if ("pk_org".equals(key)) {
			if (conditions[0] == null || "".equals(conditions[0])) {
				return null;
			}
			sql = "SELECT pk_org from org_orgs where code = ? and enablestate = 2 and nvl(dr,0)=0";
			try {
				for (String condition : conditions) {
					parameter.addParam(condition);
				}
				pkValue = (String) dao.executeQuery(sql, parameter,
						new ColumnProcessor());
				if (pkValue == null) {
					throw new BusinessException("��ͷ������֯" + code + "δ����NC�����й���");
				}
			} catch (Exception e) {
				throw new BusinessException("��ͷ������֯" + code + "δ����NC�����й���");
			}
		}
		return pkValue;
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

}
