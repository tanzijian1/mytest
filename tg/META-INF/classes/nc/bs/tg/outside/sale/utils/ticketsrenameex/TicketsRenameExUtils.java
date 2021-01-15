package nc.bs.tg.outside.sale.utils.ticketsrenameex;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.tgfn.renamechangebill.RenameChangeBillBVO;
import nc.vo.tgfn.renamechangebill.RenameChangeBillHVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * ������Ʊ��(FN18)
 * @author ln
 *
 */
public class TicketsRenameExUtils extends SaleBillUtils {
	static TicketsRenameExUtils utils;

	public static TicketsRenameExUtils getUtils() {
		if (utils == null) {
			utils = new TicketsRenameExUtils();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONObject headJson = (JSONObject) value.get("headInfo");
		String saleid = headJson.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = headJson.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�

		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;

		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		// Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggRenameChangeBillHVO aggVO = (AggRenameChangeBillHVO) getBillVO(
				AggRenameChangeBillHVO.class, "isnull(dr,0)=0 and def1 = '"
						+ saleid + "'");
		HashMap<String, String> dataMap = new HashMap<String, String>();
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {

			AggregatedValueObject billvo = onTranBill(value);
			HashMap<String, String> eParam = new HashMap<String, String>();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN18", null, billvo,
					null, eParam);
			AggRenameChangeBillHVO[] billvos = (AggRenameChangeBillHVO[]) obj;
			obj = getPfBusiAction().processAction("SAVE", "FN18", null, billvos[0],
					null, eParam);
			billvos = (AggRenameChangeBillHVO[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN18", null, billvos[0],
					null, eParam);
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {

			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * ����Դ��Ϣת����NC��Ϣ
	 * 
	 * @param hMap
	 * @return
	 */
	private AggRenameChangeBillHVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {

		String errorMsg = null;
		if (value == null || value.size() <= 0) {
			errorMsg = "����Ϊ�գ������������";
			throw new BusinessException(errorMsg);
		}
		// ��ȡ��ͷ�ͱ��������
		JSONObject headObj = (JSONObject) value.get("headInfo");
		JSONArray bodyArray = (JSONArray) value.get("itemInfo");

		if (headObj == null || headObj.size() <= 0) {
			errorMsg = "�����쳣�������б�ͷ����";
			throw new BusinessException(errorMsg);
		}

		// �����ͷ��������
		validateHeadData(headObj);
		validateBodyData(bodyArray);

		AggRenameChangeBillHVO aggvo = new AggRenameChangeBillHVO();

		RenameChangeBillHVO hvo = new RenameChangeBillHVO();
		// Ĭ�ϼ���-ʱ������-code:0001
		hvo.setPk_group("000112100000000005FD");
		hvo.setPk_org(getRefAttributePk("pk_org", headObj.getString("pk_org")));
		hvo.setBilldate(new UFDate(headObj.getString("billdate")));
		// Ĭ�Ͻ�������-code:FN18
		hvo.setTranstype("FN18");
		// Ĭ�ϵ�������-code:FN18
		hvo.setBilltype("FN18");
		hvo.setBillmaker(getRefAttributePk("billmaker",
				headObj.getString("billmaker")));
		// Ĭ�ϵ���״̬-1������
		hvo.setBillstatus(-1);
		// Ĭ������״̬-1������
		hvo.setApprovestatus(-1);
		// Ĭ����Ч״̬0��δ��Ч
		hvo.setEffectstatus(0);
		hvo.setDef1(headObj.getString("def1"));
		hvo.setDef2(headObj.getString("def2"));
		hvo.setDef3(headObj.getString("def3"));
		hvo.setDef4(headObj.getString("def4"));
		hvo.setDef5(headObj.getString("def5"));
		hvo.setDef6(headObj.getString("def6"));
		hvo.setDef7(headObj.getString("def7"));
		hvo.setDef8(getBd_cust_supplier(headObj.getString("def8")));
		hvo.setDef9(headObj.getString("def9"));
		hvo.setDef10(getUserByPsondoc(headObj.getString("def10")));
		hvo.setDef11(headObj.getString("def11"));
		hvo.setDef12(headObj.getString("def12"));
		hvo.setDef13(headObj.getString("def13"));
		hvo.setDef14(headObj.getString("def14"));
		hvo.setDef15(headObj.getString("def15"));
		hvo.setDef16(headObj.getString("def16"));
		hvo.setDef17(headObj.getString("def17"));
		hvo.setDef18(headObj.getString("def18"));
		hvo.setDef19(headObj.getString("def19"));
		hvo.setDef20(headObj.getString("def20"));
		aggvo.setParentVO(hvo);

		RenameChangeBillBVO[] bvos = new RenameChangeBillBVO[bodyArray.size()];

		for (int i = 0; i < bvos.length; i++) {
			RenameChangeBillBVO bvo = new RenameChangeBillBVO();
			JSONObject bJSONObject = bodyArray.getJSONObject(i);
			bvo.setScomment(bJSONObject.getString("scomment"));
			bvo.setDef1(getPk_projectByCode(bJSONObject.getString("def1")));
			bvo.setDef2(getPk_projectByCode(bJSONObject.getString("def2")));
			bvo.setDef3(getPk_defdocByCode(bJSONObject.getString("def3")));
			bvo.setDef4(getPk_defdocByCode(bJSONObject.getString("def4")));
			bvo.setDef5(getPk_defdocByName(bJSONObject.getString("def5")));
			bvo.setDef6(getPk_defdocByName(bJSONObject.getString("def6")));
			bvo.setDef7(bJSONObject.getString("def7"));
			bvo.setDef8(bJSONObject.getString("def8"));
			bvo.setDef9(bJSONObject.getString("def9"));
			bvo.setDef10(bJSONObject.getString("def10"));
			bvo.setDef11(bJSONObject.getString("def11"));
			bvo.setDef12(bJSONObject.getString("def12"));
			bvo.setDef13(bJSONObject.getString("def13"));
			bvo.setDef14(bJSONObject.getString("def14"));
			bvo.setDef15(bJSONObject.getString("def15"));
			bvo.setDef16(bJSONObject.getString("def16"));
			bvo.setDef17(bJSONObject.getString("def17"));
			bvo.setDef18(bJSONObject.getString("def18"));
			bvo.setDef19(bJSONObject.getString("def19"));
			bvo.setDef20(bJSONObject.getString("def20"));
			bvo.setInvoiceno(bJSONObject.getString("invoiceno"));

			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}

	private void validateHeadData(JSONObject data) throws BusinessException {
		
		String pkOrg = data.getString("pk_org");
		String billDate = data.getString("billdate");
		// ����ϵͳ����ID
		String def1 = data.getString("def1");
		// ����ϵͳ����
//		String def2 = data.getString("def2");
		// Ӱ�����
		String def3 = data.getString("def3");
		// Ӱ��״̬
		String def4 = data.getString("def4");
		
		if (pkOrg == null || "".equals(pkOrg)) {
			throw new BusinessException("������֯����Ϊ��");
		}
		if (billDate == null || "".equals(billDate)) {
			throw new BusinessException("�������ڲ���Ϊ��");
		}
		if (def1 == null || "".equals(def1)) {
			throw new BusinessException("����ϵͳ����ID����Ϊ��");
		}
//		if (def2 == null || "".equals(def2)) {
//			throw new BusinessException("����ϵͳ���Ų���Ϊ��");
//		}
//		if (def3 == null || "".equals(def3)) {
//			throw new BusinessException("Ӱ����벻��Ϊ��");
//		}
//		if (def4 == null || "".equals(def4)) {
//			throw new BusinessException("Ӱ��״̬����Ϊ��");
//		}

	}

	private void validateBodyData(JSONArray jsonArray) throws BusinessException {

		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject data = jsonArray.getJSONObject(i);
			// NC,SALE:ժҪ
//			String scomment = data.getString("scomment");
			// NC:���ز���Ŀ-SALE:������Ŀ
//			String def1 = data.getString("def1");
			// NC:���ز���Ŀ��ϸ-SALE:���򷿼�
//			String def2 = data.getString("def2");
			// NC,SALE:��������
			String def3 = data.getString("def3");
			// NC,SALE:��������
			String def4 = data.getString("def4");
			// NC,SALE:ҵ̬
//			String def5 = data.getString("def5");
			// NC,SALE:��˰��ʽ
			String def6 = data.getString("def6");
			// NC,SALE:˰��
			String def7 = data.getString("def7");
			// NC,SALE:˰��
			String def8 = data.getString("def8");
			// NC:����˰���-SALE:������ҽ�����˰��
			String def9 = data.getString("def9");
			// NC,SALE:��Ʊ�ݺ�
			String def11 = data.getString("def11");

//			if (def1 == null || "".equals(def1)) {
//				throw new BusinessException("��" + (i + 1) + "��������Ŀ����Ϊ��");
//			}
//			if (def2 == null || "".equals(def2)) {
//				throw new BusinessException("��" + (i + 1) + "�����򷿼䲻��Ϊ��");
//			}
			if (def3 == null || "".equals(def3)) {
				throw new BusinessException("��" + (i + 1) + "���������Ͳ���Ϊ��");
			}
			if (def4 == null || "".equals(def4)) {
				throw new BusinessException("��" + (i + 1) + "���������Ʋ���Ϊ��");
			}
//			if (def5 == null || "".equals(def5)) {
//				throw new BusinessException("��" + (i + 1) + "��ҵ̬����Ϊ��");
//			}
			if (def6 == null || "".equals(def6)) {
				throw new BusinessException("��" + (i + 1) + "����˰��ʽ����Ϊ��");
			}
			if (def7 == null || "".equals(def7)) {
				throw new BusinessException("��" + (i + 1) + "��˰�ʲ���Ϊ��");
			}
			if (def8 == null || "".equals(def8)) {
				throw new BusinessException("��" + (i + 1) + "��˰���Ϊ��");
			}
			if (def9 == null || "".equals(def9)) {
				throw new BusinessException("��" + (i + 1) + "��������ҽ�����˰������Ϊ��");
			}

//			if (def11 == null || "".equals(def11)) {
//				throw new BusinessException("��" + (i + 1) + "����Ʊ�ݺŲ���Ϊ��");
//			}

//			if (scomment == null || "".equals(scomment)) {
//				throw new BusinessException("��" + (i + 1) + "��ժҪ����Ϊ��");
//			}
		}
	}
}