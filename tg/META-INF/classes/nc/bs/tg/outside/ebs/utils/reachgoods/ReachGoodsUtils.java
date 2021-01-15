package nc.bs.tg.outside.ebs.utils.reachgoods;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.ComCopewithUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.infotran.WareToReceivableUtils;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.fct.ap.entity.AggCtApVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.tgfn.temporaryestimate.Business;
import nc.vo.tgfn.temporaryestimate.Temest;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * SRM-������->������ϸ���ݹ�Ӧ������
 * 
 * @author huangxj
 * 
 */
public class ReachGoodsUtils extends EBSBillUtils {
	static ReachGoodsUtils utils;

	public static ReachGoodsUtils getUtils() {
		if (utils == null) {
			utils = new ReachGoodsUtils();
		}
		return utils;
	}

	/**
	 * SRM����/���� �ִ�NC�������� huangxj 2019��11��15��14:19:15
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException7
	 */
	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(OperatorName);
		// �������Ϣ
		JSONObject jsonData = (JSONObject) value.get("data");
		String jsonbody = jsonData.getString("BodyVO");

		JSONArray jsonArr = JSON.parseArray(jsonbody);
		if (jsonArr.size() < 1) {
			throw new BusinessException("������ϢΪ��");
		}
		for (int i = 0; i < jsonArr.size(); i++) {
			JSONObject bJSONObject = jsonArr.getJSONObject(i);

			/*// �����Ƿ��ж�Ӧ�Ĳɹ�Э��

			if (!"".equals(bJSONObject.get("def11"))
					|| bJSONObject.get("def11") != null) {

				AggCtApVO ApVO = (AggCtApVO) getBillVO(
						AggCtApVO.class,
						"isnull(dr,0)=0 and vbillcode = '"
								+ bJSONObject.get("def11") + "' and ctname = '"
								+ bJSONObject.get("def12") + "'");

				if (ApVO == null) {
					throw new BusinessException("����ͬ��Э�鵽NC��Э����룺"
							+ bJSONObject.get("def11") + "��Э�����ƣ�"
							+ bJSONObject.get("def12"));
				}
			}*/

			// ���ܸ�����Ϣ
			JSONArray JSarr = bJSONObject.getJSONArray("attachment");
			if (JSarr != null) {

				for (int j = 0; j < JSarr.size(); j++) {
					JSONObject JSObj = (JSONObject) JSarr.get(j);

					String srmno = JSObj.getString("srmno");
					String srmid = JSObj.getString("srmid");
					String srmtype = JSObj.getString("srmtype");
					String att_name = JSObj.getString("att_name");
					String att_address = JSObj.getString("att_address");

					String select_sql = "select count(1) from attachment where nvl(dr,0) = 0 and srmno = '"
							+ srmno + "' and srmid = '" + srmid + "'";

					int num = (int) getBaseDAO().executeQuery(select_sql,
							new ColumnProcessor());

					if (num == 0) {

						String att_sql = "insert into attachment values('"
								+ srmno + "','" + srmid + "','" + srmtype
								+ "','" + att_name + "','" + att_address
								+ "',0)";

						getBaseDAO().executeUpdate(att_sql);
					}
				}
			}

			if ("��Ӧ����������".equals(bJSONObject.get("def49"))) {
				// У���ݹ�Ӧ������
				validateBodyData(bJSONObject);
				// �����ݹ�Ӧ������
				onTranTemestBill(bJSONObject);
			} else if ("��Ӧ�����۳���".equals(bJSONObject.get("def49"))) {
				// ���ɳ��⹤��
				RecbillUtils.getUtils().onRecBill(bJSONObject);
				// ����Ӧ�յ�
				WareToReceivableUtils.getUtils().onpushBill(bJSONObject);
			} else if ("��Ӧ���˻�".equals(bJSONObject.get("def49"))) {
				validateBodyData(bJSONObject);
				// �ݹ�Ӧ������������
				onTranTemestBill(bJSONObject);
			} else if ("��Ŀ��˾��������".equals(bJSONObject.get("def49"))) {
				// У���ݹ�Ӧ������
				validateBodyData(bJSONObject);
				// �����ݹ�Ӧ������
				onTranTemestBill(bJSONObject);
				// ���ɲ���Ӧ����
				ComCopewithUtils.getUtils().onpushBill(bJSONObject);
			} else if ("��Ŀ��˾���ó���".equals(bJSONObject.get("def49"))) {
				// ���ɳ��⹤��
				RecbillUtils.getUtils().onRecBill(bJSONObject);
			} else if ("��Ŀ��˾�˻�".equals(bJSONObject.get("def49"))) {
				validateBodyData(bJSONObject);
				// �����ݹ�Ӧ������������
				onTranTemestBill(bJSONObject);
			} else {
				throw new BusinessException("��������Ϊ�ջ��������������������");
			}
		}

		return JSON.toJSONString("SRM-���� �������");
	}

	/**
	 * SRM-������->������ϸ���ݹ�Ӧ������
	 * 
	 * @param value
	 * @param dectype
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String onReachBill(JSONObject bJSONObject) throws BusinessException {
		return null;
	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param dectype
	 * 
	 * @param hMap
	 * @return
	 * @return
	 */
	private void onTranTemestBill(JSONObject bJSONObject)
			throws BusinessException {
		// / ���м���
		List<String> queues = new ArrayList<String>();

		// ����ÿһ�б��壬������������ID�����ظ�
		String srcid = bJSONObject.getString("def3"); // ������ID

		// ������IDΨһ����������־
		String billqueue;
		billqueue = "SRM-������->�ݹ�Ӧ������:" + srcid;
		queues.add(billqueue);

		AggTemest aggTemestVO = (AggTemest) getBillVO(AggTemest.class,
				"isnull(dr,0)=0 and def7 = '" + bJSONObject.getString("def2")
						+ "'");
		String pk = "";
		if (aggTemestVO != null) {
			pk = aggTemestVO.getParentVO().getPk_temest();
		}

		// ����ݹ������Ƿ����ظ��ϴ��ĵ�������Ϣ
		boolean check = selectTemp(bJSONObject.getString("def3"), pk);
		if (check) {
			// if (("��Ŀ��˾��������".equals(bJSONObject.getString("def49")) ||
			// "��Ӧ����������"
			// .equals(bJSONObject.get("def49")))) {
			// //onTranAGoodsDetail(bJSONObject);
			// } else {
			String def43 = bJSONObject.getString("def43");// ���˵���
			String def44 = bJSONObject.getString("def44");// ���˵��ܽ��

			String sql = "update tgfn_temestbus set def43 = '" + def43
					+ "',def44 = '" + def44
					+ "'  where nvl(dr,0)=0 and def3 = '"
					+ bJSONObject.getString("def3") + "' and pk_temest = '"
					+ pk + "'";
			getBaseDAO().executeUpdate(sql);
			// }
			return;
		}

		AggTemest aggvo = new AggTemest();

		Temest hvo = new Temest();
		// Ĭ�ϼ���-ʱ������-code:0001
		hvo.setPk_group("000112100000000005FD");
		if (getRefAttributePk("pk_org", bJSONObject.getString("pk_org")) != null) {
			hvo.setPk_org(getRefAttributePk("pk_org",
					bJSONObject.getString("pk_org")));
		} else {
			throw new BusinessException("�ò�����֯����δ��NC����");
		}

		// ��������
		hvo.setBilldate(new UFDate());
		// Ĭ�ϵ�������-code:FN03
		hvo.setBilltype("FN03");

		// Ĭ������״̬-1������
		hvo.setApprovestatus(-1);
		// Ĭ����Ч״̬0��δ��Ч
		hvo.setEffectstatus(0);
		// ��������-Ĭ�Ϲ�Ӧ��
		hvo.setObjtype(1);
		// �Ƿ��˻�
		hvo.setDef6("Y".equals(bJSONObject.getString("def50")) ? UFBoolean.TRUE
				: UFBoolean.FALSE);

		UFDouble dhjebhs = UFDouble.ZERO_DBL;
		UFDouble dhjese = UFDouble.ZERO_DBL;
		UFDouble dhjehs = UFDouble.ZERO_DBL;

		if ("��Ӧ����������".equals(bJSONObject.getString("def49"))
				|| "��Ŀ��˾��������".equals(bJSONObject.getString("def49"))) {

			if ("��Ӧ����������".equals(bJSONObject.getString("def49"))) {
				// ����������˰��
				dhjebhs = new UFDouble(bJSONObject.getString("def21"));
				// ������˰�
				dhjese = new UFDouble(bJSONObject.getString("def22"));
				// ��������˰��
				dhjehs = new UFDouble(bJSONObject.getString("def19"));

			} else {
				dhjebhs = new UFDouble(bJSONObject.getString("def26"));
				dhjese = new UFDouble(bJSONObject.getString("def27"));
				dhjehs = new UFDouble(bJSONObject.getString("def24"));
			}

		} else {

			if ("��Ӧ���˻�".equals(bJSONObject.get("def49"))) {
				// ����������˰��
				dhjebhs = new UFDouble(bJSONObject.getString("def21"))
						.multiply(-1);
				// ������˰�
				dhjese = new UFDouble(bJSONObject.getString("def22"))
						.multiply(-1);
				// ��������˰��
				dhjehs = new UFDouble(bJSONObject.getString("def19"))
						.multiply(-1);
			} else {
				dhjebhs = new UFDouble(bJSONObject.getString("def26"))
						.multiply(-1);
				dhjese = new UFDouble(bJSONObject.getString("def27"))
						.multiply(-1);
				dhjehs = new UFDouble(bJSONObject.getString("def24"))
						.multiply(-1);
			}

		}
		hvo.setDef3(dhjebhs);
		hvo.setDef4(dhjese);
		hvo.setDef5(dhjehs);

		hvo.setSupplier(getRefAttributePk("supplier",
				bJSONObject.getString("supplier")));// ����
		hvo.setDef7(bJSONObject.getString("def2"));// ����id
		hvo.setDef8(bJSONObject.getString("def1"));// ���ձ��

		if (!"".equals(bJSONObject.getString("def51"))
				&& bJSONObject.getString("def51") != null) {
			hvo.setDef9(getRefAttributePk("pk_org",
					bJSONObject.getString("def51")));// ��Ŀ��˾
			if ("".equals(hvo.getDef9()) || hvo.getDef9() == null) {
				throw new BusinessException("��Ŀ��˾δ��NC����");
			}
		}
		hvo.setDef10(bJSONObject.getString("def52"));// �ڲ���Ʊ��Ʊ̧ͷ��˾
		hvo.setDef11(bJSONObject.getString("def53"));// �ڲ������տ��ͬ����
		hvo.setDef12(bJSONObject.getString("def54"));// �ڲ������տ��ͬ����

		aggvo.setParentVO(hvo);

		Business[] bvos = new Business[1];
		for (int i = 0; i < 1; i++) {
			Business bvo = new Business();
			// JSONObject bJSONObject = bodyArray.getJSONObject(i);
			bvo.setDr(0);
			// bvo.setSupplier(getRefAttributePk("supplier",
			// bJSONObject.getString("supplier")));// �ɹ���Ӧ��
			bvo.setDef3(bJSONObject.getString("def3"));// ������id
			bvo.setDef4(bJSONObject.getString("def4"));// ��������
			bvo.setDef5(bJSONObject.getString("def5"));// ����id
			bvo.setDef6(bJSONObject.getString("def6"));// ������id
			bvo.setDef7(bJSONObject.getString("def7"));// ԭ���ձ��
			// bvo.setDef8(bJSONObject.getString("def8"));// ��ƿ�Ŀ���루������
			// bvo.setDef9(bJSONObject.getString("def9"));// ��ƿ�Ŀ���ƣ�������
			// bvo.setDef10(bJSONObject.getString("def10"));// ��Ӧ�̱���
			bvo.setDef11(bJSONObject.getString("def11"));// �ɹ�Э����
			bvo.setDef12(bJSONObject.getString("def12"));// �ɹ�Э������
			bvo.setDef13(bJSONObject.getString("def13"));// ���ϱ���
			bvo.setDef14(bJSONObject.getString("def14"));// ����˵��
			bvo.setDef15(bJSONObject.getString("def15"));// ��λ
			if ("��Ӧ����������".equals(bJSONObject.getString("def49"))
					|| "��Ŀ��˾��������".equals(bJSONObject.getString("def49"))) {
				bvo.setDef20(new UFDouble(bJSONObject.getString("def20")));// ��Ӧ��˰��
				bvo.setDef16(new UFDouble(bJSONObject.getString("def16")));// ���ε�������
				bvo.setDef23(new UFDouble(bJSONObject.getString("def23")));// �ӳ���
				bvo.setDef25(new UFDouble(bJSONObject.getString("def25")));// ��Ŀ��˾˰��
				bvo.setDef17(new UFDouble(bJSONObject.getString("def17")));// ��˰����
				bvo.setDef18(new UFDouble(bJSONObject.getString("def18")));// ���۲���˰
				bvo.setDef19(new UFDouble(bJSONObject.getString("def19")));// ��Ӧ������˰��
				bvo.setDef21(new UFDouble(bJSONObject.getString("def21")));// ��Ӧ��������˰��
				bvo.setDef22(new UFDouble(bJSONObject.getString("def22")));// ��Ӧ����˰�
				bvo.setDef24(new UFDouble(bJSONObject.getString("def24")));// ��Ŀ��˾����˰��
				bvo.setDef26(new UFDouble(bJSONObject.getString("def26")));// ��Ŀ��˾������˰��
				bvo.setDef27(new UFDouble(bJSONObject.getString("def27")));// ��Ŀ��˾��˰�
			} else {
				bvo.setDef20((new UFDouble(bJSONObject.getString("def20")))
						.multiply(-1));// ��Ӧ��˰��
				bvo.setDef16((new UFDouble(bJSONObject.getString("def16")))
						.multiply(-1));// ���ε�������
				bvo.setDef23((new UFDouble(bJSONObject.getString("def23")))
						.multiply(-1));// �ӳ���
				bvo.setDef25((new UFDouble(bJSONObject.getString("def25")))
						.multiply(-1));// ��Ŀ��˾˰��
				bvo.setDef17((new UFDouble(bJSONObject.getString("def17")))
						.multiply(-1));// ��˰����
				bvo.setDef18((new UFDouble(bJSONObject.getString("def18")))
						.multiply(-1));// ���۲���˰
				bvo.setDef19((new UFDouble(bJSONObject.getString("def19")))
						.multiply(-1));// ��Ӧ������˰��
				bvo.setDef21((new UFDouble(bJSONObject.getString("def21")))
						.multiply(-1));// ��Ӧ��������˰��
				bvo.setDef22((new UFDouble(bJSONObject.getString("def22")))
						.multiply(-1));// ��Ӧ����˰�
				bvo.setDef24((new UFDouble(bJSONObject.getString("def24")))
						.multiply(-1));// ��Ŀ��˾����˰��
				bvo.setDef26((new UFDouble(bJSONObject.getString("def26")))
						.multiply(-1));// ��Ŀ��˾������˰��
				bvo.setDef27((new UFDouble(bJSONObject.getString("def27")))
						.multiply(-1));// ��Ŀ��˾��˰�
			}

			bvo.setDef31(bJSONObject.getString("def43"));// ���˵���
			bvo.setDef32(bJSONObject.getString("def44"));// ���˵��ܽ��
			bvo.setDef33(StringUtils.isBlank(bJSONObject.getString("def45")) ? "N"
					: bJSONObject.getString("def45"));// ��Ӧ���Ƿ�Ʊ(Y/N)

			bvo.setDef36(bJSONObject.getString("def36"));// ʩ����ͬ���
			bvo.setDef37(bJSONObject.getString("def37"));// ʩ����ͬ����
			bvo.setDef41(bJSONObject.getString("def38"));// ����/�˻�ȷ������

			String def28 = getRefAttributePk("pk_project",
					bJSONObject.getString("def39"));
			/*if (def28 == null || "".equals(def28)) {
				throw new BusinessException("��"
						+ bJSONObject.getString("def49") + "���ջ���Ŀδ��NC����");
			}*/
			bvo.setDef28(def28);// �ջ���Ŀ
			
			String def30 = getRefAttributePk("pk_project",
					bJSONObject.getString("def41"));
			
			if (def30 == null || "".equals(def30)) {
				throw new BusinessException("��"
						+ bJSONObject.getString("def49") + "���ջ���Ŀ����δ��NC����");
			}
			bvo.setDef30(def30);// �ջ���Ŀ����
			
			bvo.setDef38(bJSONObject.getString("def46"));// ��Ŀ��˾�ɹ��ɱ���ͬ���
			bvo.setDef39(bJSONObject.getString("def47"));// ��Ŀ��˾�ɹ��ɱ���ͬ����
			bvo.setDef40(bJSONObject.getString("def48"));// �˻�����

			bvos[i] = bvo;
		}

		// ���Ӷ��д���
		EBSBillUtils.addBillQueue(queues.toString());
		try {
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			if (aggTemestVO != null) {
				bvos[0].setPk_temest(pk);
				getBaseDAO().insertVO(bvos[0]);// dhjehs dhjese dhjebhs
				String sql = "update tgfn_temest set def3 = def3+"
						+ dhjebhs.doubleValue() + ",def4=def4+"
						+ dhjese.doubleValue() + ",def5=def5+"
						+ dhjehs.doubleValue()
						+ " where nvl(dr,0)=0 and pk_temest = '" + pk + "'";
				getBaseDAO().executeUpdate(sql);
			} else {
				aggvo.setChildrenVO(bvos);
				getPfBusiAction().processAction("SAVEBASE", "FN03", null,
						aggvo, null, null);
			}
		} catch (Exception e) {
			throw new BusinessException("��" + queues.toString() + "��,"
					+ e.getMessage(), e);
		} finally {
			EBSBillUtils.removeBillQueue(queues.toString());
		}
		/*
		 * if (aggvo != null &&
		 * ("��Ŀ��˾��������".equals(bJSONObject.getString("def49")) || "��Ӧ����������"
		 * .equals(bJSONObject.get("def49")))) {
		 * onTranAGoodsDetail(bJSONObject); }
		 */
		return;

	}

	/*
	 * private void onTranAGoodsDetail(JSONObject bJSONObject) throws
	 * BusinessException { // / ���м��� List<String> queues = new
	 * ArrayList<String>();
	 * 
	 * // ����ÿһ�б��壬������������ID�����ظ� String srcid = bJSONObject.getString("def3"); //
	 * ����ID // ������IDΨһ����������־ String billqueue; billqueue =
	 * bJSONObject.getString("def49") + srcid; queues.add(billqueue);
	 * 
	 * // ��鵽����ϸ���Ƿ����ظ��ϴ��ĵ�������Ϣ boolean check =
	 * selectGoods(bJSONObject.getString("def3"));
	 * 
	 * if (check) { // �ڶ�����ı���˵��źͶ��˽�� upadteGoodsDetail(bJSONObject); return; }
	 * AggAGoodsDetail aggvo = new AggAGoodsDetail();
	 * 
	 * AGoodsDetail hvo = new AGoodsDetail(); // Ĭ�ϼ���-ʱ������-code:0001
	 * hvo.setPk_group("000112100000000005FD"); // ��֯-ʱ������-code:def32
	 * hvo.setPk_org(getRefAttributePk("pk_org",
	 * bJSONObject.getString("pk_org"))); // �������� hvo.setBilldate(new UFDate());
	 * // Ĭ�ϵ�������-code:FN20 hvo.setBilltype("FN20"); // Ĭ������״̬-1������
	 * hvo.setApprovestatus(-1);
	 * 
	 * hvo.setDef1(bJSONObject.getString("def1"));// ���ձ��
	 * hvo.setDef2(bJSONObject.getString("def2"));// ����id
	 * hvo.setDef3(bJSONObject.getString("def3"));// ������id
	 * hvo.setDef4(bJSONObject.getString("def4"));// ��������
	 * hvo.setDef5(bJSONObject.getString("def5"));// ����id
	 * hvo.setDef6(bJSONObject.getString("def6"));// ������id //
	 * hvo.setDef7(bodyObj.getString("def7"));ҵ�����ͣ��˻�/���/����/����/����/�ӷ��� //
	 * hvo.setDef8(bodyObj.getString("def7"));// ԭ���ձ�� //
	 * hvo.setDef9(bodyObj.getString("def8"));// ��ƿ�Ŀ���루������ //
	 * hvo.setDef10(bodyObj.getString("def9"));// ��ƿ�Ŀ���ƣ������� //
	 * hvo.setDef11(bodyObj.getString("def10"));// ��Ӧ�̱���
	 * hvo.setDef12(getRefAttributePk("supplier",
	 * bJSONObject.getString("supplier")));// �ɹ���Ӧ��-����
	 * hvo.setDef13(bJSONObject.getString("def11"));// Э����
	 * hvo.setDef14(bJSONObject.getString("def12"));// ��ͬ����(Э������)
	 * hvo.setDef15(bJSONObject.getString("def13"));// ���ϱ���
	 * hvo.setDef16(bJSONObject.getString("def14"));// ����˵��
	 * hvo.setDef17(bJSONObject.getString("def15"));// ��λ hvo.setDef18(new
	 * UFDouble(bJSONObject.getString("def16")));// ���ε������� hvo.setDef19(new
	 * UFDouble(bJSONObject.getString("def17")));// ���ۺ�˰ hvo.setDef20(new
	 * UFDouble(bJSONObject.getString("def18")));// ���۲���˰ hvo.setDef21(new
	 * UFDouble(bJSONObject.getString("def19")));// ��Ӧ������˰�� hvo.setDef22(new
	 * UFDouble(bJSONObject.getString("def20")));// ��Ӧ��˰�� hvo.setDef23(new
	 * UFDouble(bJSONObject.getString("def21")));// ��Ӧ��������˰�� hvo.setDef24(new
	 * UFDouble(bJSONObject.getString("def22")));// ��Ӧ����˰� hvo.setDef25(new
	 * UFDouble(bJSONObject.getString("def23")));// �ӳ��� hvo.setDef26(new
	 * UFDouble(bJSONObject.getString("def24")));// ��Ŀ��˾����˰�� hvo.setDef27(new
	 * UFDouble(bJSONObject.getString("def25")));// ��Ŀ��˾˰�� hvo.setDef28(new
	 * UFDouble(bJSONObject.getString("def26")));// ��Ŀ��˾������˰�� hvo.setDef29(new
	 * UFDouble(bJSONObject.getString("def27")));// ��Ŀ��˾��˰�
	 * 
	 * hvo.setDef46(bJSONObject.getString("def36"));// ʩ����ͬ���
	 * hvo.setDef47(bJSONObject.getString("def37"));// ʩ����ͬ���� hvo.setDef30(new
	 * UFDateTime(bJSONObject.getString("def38")));// ����ȷ������
	 * 
	 * String def33 = getRefAttributePk("def28",
	 * bJSONObject.getString("def41")); if (def33 == null || "".equals(def33)) {
	 * throw new BusinessException("��" + bJSONObject.getString("def49") +
	 * "���ջ���Ŀ����δ��NC����"); }
	 * 
	 * hvo.setDef33(def33);// �ջ���Ŀ����-������Ŀ
	 * hvo.setDef37(bJSONObject.getString("def43"));// ���˵��� hvo.setDef38(new
	 * UFDouble(bJSONObject.getString("def44")));// ���˵��ܽ��
	 * hvo.setDef39(StringUtils.isBlank(bJSONObject.getString("def45")) ?
	 * UFBoolean.FALSE : new UFBoolean(bJSONObject.getString("def45")));//
	 * ��Ӧ���Ƿ�Ʊ(Y/N) hvo.setDef49(bJSONObject.getString("def46"));// ��Ŀ��˾�ɹ��ɱ���ͬ���
	 * hvo.setDef50(bJSONObject.getString("def47"));// ��Ŀ��˾�ɹ��ɱ���ͬ����
	 * aggvo.setParentVO(hvo); try { HashMap<String, String> eParam = new
	 * HashMap<String, String>(); eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
	 * PfUtilBaseTools.PARAM_NOTE_CHECKED);
	 * 
	 * getPfBusiAction().processAction("SAVEBASE", "FN20", null, aggvo, null,
	 * eParam); } catch (Exception e) { throw new BusinessException("��" +
	 * queues.toString() + "��," + e.getMessage(), e); } finally {
	 * EBSBillUtils.removeBillQueue(queues.toString()); } return; }
	 */

	private void validateBodyData(JSONObject data) throws BusinessException {

		// ������֯
		String pkOrg = data.getString("pk_org");
		// ����
		String supplier = data.getString("supplier");

		if (pkOrg == null || "".equals(pkOrg)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "��������֯����Ϊ��");
		}
		if (supplier == null || "".equals(supplier)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����̲���Ϊ��");
		}

		// ���ձ��
		String def1 = data.getString("def1");
		// ����id
		String def2 = data.getString("def2");
		// ������id
		String def3 = data.getString("def3");
		// ��������
		String def4 = data.getString("def4");
		// ����id
		String def5 = data.getString("def5");
		// ������id
		String def6 = data.getString("def6");
		// ԭ���ձ��
		String def7 = data.getString("def6");
		// �ɹ�Э�����
		String def11 = data.getString("def11");
		// �ɹ�Э������
		String def12 = data.getString("def12");
		// ���ϱ���
		String def13 = data.getString("def13");
		// ����˵��
		String def14 = data.getString("def14");
		// ��λ
		String def15 = data.getString("def15");
		// ���ε�������
		String def16 = data.getString("def16");
		// ���ۺ�˰
		String def17 = data.getString("def17");
		// ���۲���˰
		String def18 = data.getString("def18");
		// ��Ӧ������˰��
		String def19 = data.getString("def19");
		// ��Ӧ��˰��
		String def20 = data.getString("def20");
		// ��Ӧ��������˰��
		String def21 = data.getString("def21");
		// ��Ӧ����˰�
		String def22 = data.getString("def22");
		// �ӳ���
		String def23 = data.getString("def23");
		// ��Ŀ��˾����˰��
		String def24 = data.getString("def24");
		// ��Ŀ��˾˰��
		String def25 = data.getString("def25");
		// ��Ŀ��˾������˰��
		String def26 = data.getString("def26");
		// ��Ŀ��˾��˰�
		String def27 = data.getString("def27");

		// ʩ����ͬ���
		//String def36 = data.getString("def36");

		// ʩ����ͬ����
		//String def37 = data.getString("def37");

		String def38 = data.getString("def38");// ����ȷ������
		String def41 = data.getString("def41");// �ջ���Ŀ����
		String def48 = data.getString("def48");// �����������

		/*
		 * if (supplier == null || "".equals(supplier)) { throw new
		 * BusinessException("��" + (i + 1) + "���ɹ���Ӧ�̲���Ϊ��"); }
		 */

		if ("��Ӧ����������".equals(data.get("def49"))) {
			if (def11 == null || "".equals(def11)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ɹ�Э����벻��Ϊ��");
			}
			if (def12 == null || "".equals(def12)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ɹ�Э�����Ʋ���Ϊ��");
			}

			/*
			 * // �����Ƿ��ж�Ӧ�Ĳɹ�Э�� AggCtApVO ApVO = (AggCtApVO)
			 * getBillVO(AggCtApVO.class, "isnull(dr,0)=0 and vbillcode = '" +
			 * def11 + "' and ctname = '" + def12 + "'");
			 * 
			 * if (ApVO == null) { throw new BusinessException("����ͬ��Э�鵽NC��Э����룺"
			 * + def11 + "��Э�����ƣ�" + def12); }
			 */

			if (def19 == null || "".equals(def19)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ������˰������Ϊ��");
			}
			if (def20 == null || "".equals(def20)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ��˰�ʲ���Ϊ��");
			}
			if (def21 == null || "".equals(def21)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ��������˰������Ϊ��");
			}
			if (def22 == null || "".equals(def22)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ����˰�����Ϊ��");
			}
			if (def38 == null || "".equals(def38)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "������ȷ�����ڲ���Ϊ��");
			}
		}
		if ("��Ӧ���˻�".equals(data.get("def49"))) {
			if (def11 == null || "".equals(def11)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ɹ�Э����벻��Ϊ��");
			}
			if (def12 == null || "".equals(def12)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ɹ�Э�����Ʋ���Ϊ��");
			}

			// �����Ƿ��ж�Ӧ�Ĳɹ�Э��
			AggCtApVO ApVO = (AggCtApVO) getBillVO(AggCtApVO.class,
					"isnull(dr,0)=0 and vbillcode = '" + def11
							+ "' and ctname = '" + def12 + "'");

			if (ApVO == null) {
				throw new BusinessException("����ͬ��Э�鵽NC��Э����룺" + def11
						+ "��Э�����ƣ�" + def12);
			}

			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "��ԭ���ձ�Ų���Ϊ��");
			}
			if (def19 == null || "".equals(def19)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ������˰������Ϊ��");
			}
			if (def20 == null || "".equals(def20)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ��˰�ʲ���Ϊ��");
			}
			if (def21 == null || "".equals(def21)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ��������˰������Ϊ��");
			}
			if (def22 == null || "".equals(def22)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ӧ����˰�����Ϊ��");
			}
			if (def48 == null || "".equals(def48)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "������������ڲ���Ϊ��");
			}
		}

		if ("��Ŀ��˾��������".equals(data.get("def49"))) {
			if (def23 == null || "".equals(def23)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ӳ��ʲ���Ϊ��");
			}
			if (def24 == null || "".equals(def24)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾����˰������Ϊ��");
			}
			if (def25 == null || "".equals(def25)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾˰�ʲ���Ϊ��");
			}
			if (def26 == null || "".equals(def26)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾����Ϊ��");
			}
			if (def27 == null || "".equals(def27)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾��˰�����Ϊ��");
			}
			// if (def36 == null || "".equals(def36)) {
			// throw new BusinessException("��" + data.getString("def49")
			// + "��ʩ����ͬ��Ų���Ϊ��");
			// }
			// if (def37 == null || "".equals(def37)) {
			// throw new BusinessException("��" + data.getString("def49")
			// + "��ʩ����ͬ���Ʋ���Ϊ��");
			// }
			if (def38 == null || "".equals(def38)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "������ȷ�����ڲ���Ϊ��");
			}
		}
		if ("��Ŀ��˾�˻�".equals(data.get("def49"))) {
			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "��ԭ���ձ�Ų���Ϊ��");
			}
			if (def23 == null || "".equals(def23)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "���ӳ��ʲ���Ϊ��");
			}
			if (def24 == null || "".equals(def24)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾����˰������Ϊ��");
			}
			if (def25 == null || "".equals(def25)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾˰�ʲ���Ϊ��");
			}
			if (def26 == null || "".equals(def26)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾����Ϊ��");
			}
			if (def27 == null || "".equals(def27)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "����Ŀ��˾��˰�����Ϊ��");
			}
			// if (def36 == null || "".equals(def36)) {
			// throw new BusinessException("��" + data.getString("def49")
			// + "��ʩ����ͬ��Ų���Ϊ��");
			// }
			// if (def37 == null || "".equals(def37)) {
			// throw new BusinessException("��" + data.getString("def49")
			// + "��ʩ����ͬ���Ʋ���Ϊ��");
			// }
			if (def48 == null || "".equals(def48)) {
				throw new BusinessException("��" + data.getString("def49")
						+ "������������ڲ���Ϊ��");
			}
		}

		if (def1 == null || "".equals(def1)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����ձ�Ų���Ϊ��");
		}
		if (def2 == null || "".equals(def2)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "������id����Ϊ��");
		}
		if (def3 == null || "".equals(def3)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "��������id����Ϊ��");
		}
		if (def4 == null || "".equals(def4)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "���������벻��Ϊ��");
		}
		if (def5 == null || "".equals(def5)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "������id����Ϊ��");
		}
		if (def6 == null || "".equals(def6)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "��������id����Ϊ��");
		}
		if (def13 == null || "".equals(def13)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����ϱ��벻��Ϊ��");
		}
		if (def14 == null || "".equals(def14)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "������˵������Ϊ��");
		}
		if (def15 == null || "".equals(def15)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "����λ����Ϊ��");
		}
		if (def16 == null || "".equals(def16)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����ν�����������Ϊ��");
		}
		if (def17 == null || "".equals(def17)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����ۺ�˰����Ϊ��");
		}
		if (def18 == null || "".equals(def18)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "�����۲���˰����Ϊ��");
		}
		if (def41 == null || "".equals(def41)) {
			throw new BusinessException("��" + data.getString("def49")
					+ "���ջ���Ŀ���ڲ���Ϊ��");
		}
		if (data.getString("def45") == null
				|| "".equals(data.getString("def45"))) {
			throw new BusinessException("��" + data.getString("def49")
					+ "����Ӧ���Ƿ�Ʊ(Y/N)����Ϊ��");
		}
		if (data.getString("def50") == null
				|| "".equals(data.getString("def50"))) {
			throw new BusinessException("��" + data.getString("def49")
					+ "���Ƿ��˻�����Ϊ��");
		}

	}

	private String getRefAttributePk(String key, String value)
			throws BusinessException {
		if (value == null || "".equals(value))
			return null;
		String pkValue = null;
		String sql = null;
		if ("pk_org".equals(key) || "def32".equals(key)) {
			sql = "select pk_financeorg from org_financeorg where code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		} else if ("supplier".equals(key)) {
			sql = "select  pk_supplier  from bd_supplier where code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		} else if ("pk_project".equals(key)) {// ��Ŀ����
			sql = "select pk_project from bd_project where project_code = ? and nvl(dr, 0) =0";
			SQLParameter parameter = new SQLParameter();
			parameter.addParam(value);

			pkValue = (String) getBaseDAO().executeQuery(sql, parameter,
					new ColumnProcessor());
		}
		return pkValue;
	}

	/**
	 * ���ݹ�Ӧ���������Ψһ�Խ���У��
	 * 
	 * @param def1
	 * @return
	 */
	private Boolean selectTemp(String def3, String pk) throws BusinessException {
		boolean check = false;

		int result = 0;

		String sql = "select count(1) from tgfn_temestbus where nvl(dr,0)=0 and def3 = '"
				+ def3 + "' and pk_temest = '" + pk + "'";

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
}
