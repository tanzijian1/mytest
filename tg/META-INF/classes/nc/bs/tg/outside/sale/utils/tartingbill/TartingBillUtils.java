package nc.bs.tg.outside.sale.utils.tartingbill;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.tg.tartingbill.TartingBillBVO;
import nc.vo.tg.tartingbill.TartingBillVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/**
 * ̢������(FN16)
 * @author ln
 *
 */
public class TartingBillUtils extends SaleBillUtils{
	static TartingBillUtils utils;

	public static TartingBillUtils getUtils() {
		if (utils == null) {
			utils = new TartingBillUtils();
		}
		return utils;
	}
	/**
	 * ̢������
	 * @param value
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncBill(HashMap<String, Object> value, String billtype)
			throws BusinessException {
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(getSaleUserID());
		InvocationInfoProxy.getInstance().setUserCode(SaleOperatorName);

		JSONObject jsonObject = (JSONObject) value.get("headInfo");// ��ȡ��ͷ��Ϣ
		
		String saleid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�
		HashMap<String, String> dataMap = new HashMap<String, String>();
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		// TODO Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		AggTartingBillVO aggVO = (AggTartingBillVO) getBillVO(
				AggTartingBillVO.class, "isnull(dr,0)=0 and def1 = '"
						+ saleid + "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			AggTartingBillVO billvo = onTranBill(value);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "FN16", null, billvo,
					null, eParam);
			AggTartingBillVO[] billvos = (AggTartingBillVO[]) obj;
			obj = getPfBusiAction().processAction("SAVE", "FN16", null, billvos[0],
					null, eParam);
			billvos = (AggTartingBillVO[]) obj;
			getPfBusiAction().processAction("APPROVE", "FN16", null, billvos[0],
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
	private AggTartingBillVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {
		AggTartingBillVO aggvo = new AggTartingBillVO();
		JSONObject headjson = (JSONObject) value.get("headInfo");
		if(headjson.isEmpty()){
			throw new BusinessException("�����쳣����ͷ���ݲ���Ϊ�գ�");
		}
		JSONArray bodyjson = (JSONArray) value.get("itemInfo");
		if(bodyjson.isEmpty()){
			throw new BusinessException("�����쳣���������ݲ���Ϊ�գ�");
		}
		validHeadData(headjson);
		validBodyData(bodyjson);
		
		//��ͷ��Ϣ��ֵ
		TartingBillVO hvo = new TartingBillVO();
		String pk_billtype = value.get("billtype").toString();
		hvo.setPk_group("000112100000000005FD");
		hvo.setPk_org(getRefAttributePk("pk_org", headjson.getString("pk_org")));
		hvo.setBilldate(new UFDate(headjson.getString("billdate")));
		hvo.setTranstype(pk_billtype);
		hvo.setBilltype(pk_billtype);
		hvo.setBillmaker(getSaleUserID());
		hvo.setCreator(getSaleUserID());
		hvo.setDef1(headjson.getString("def1"));
		hvo.setDef2(headjson.getString("def2"));
		hvo.setDef3(headjson.getString("def3"));
		hvo.setDef4(headjson.getString("def4"));
		
		hvo.setDef5(headjson.getString("def5"));//
		hvo.setDef6(headjson.getString("def6"));//
		hvo.setDef7(headjson.getString("def7"));//
		hvo.setDef8(headjson.getString("def8"));//
		hvo.setDef9(headjson.getString("def9"));//
		hvo.setApprovestatus(-1);//����״̬
		hvo.setEffectstatus(0);//��Ч״̬
		hvo.setDef10(getUserByPsondoc(headjson.getString("def10")));//����ϵͳ������
		hvo.setDef15(headjson.getString("def15"));//��������
		aggvo.setParentVO(hvo);
		
		//������Ϣ��ֵ
		TartingBillBVO[] bvos = new TartingBillBVO[bodyjson.size()];
		for(int i = 0; i<bodyjson.size(); i++){
			TartingBillBVO bvo = new TartingBillBVO();
			bvo.setScomment(bodyjson.getJSONObject(i).getString("scomment"));
			bvo.setDef1(getPk_projectByCode(bodyjson.getJSONObject(i).getString("def1")));
			bvo.setDef2(getPk_projectByCode(bodyjson.getJSONObject(i).getString("def2")));
			bvo.setDef3(getPk_defdocByCode(bodyjson.getJSONObject(i).getString("def3")));
			bvo.setDef4(getPk_defdocByCode(bodyjson.getJSONObject(i).getString("def4")));
			bvo.setDef5(getPk_defdocByName(bodyjson.getJSONObject(i).getString("def5")));
			bvo.setDef6(getPk_defdocByName(bodyjson.getJSONObject(i).getString("def6")));
			bvo.setDef7(bodyjson.getJSONObject(i).getString("def7"));
			bvo.setDef8(bodyjson.getJSONObject(i).getString("def8"));
			bvo.setDef9(bodyjson.getJSONObject(i).getString("def9"));
			bvo.setDef10(bodyjson.getJSONObject(i).getString("def10"));//̢�����
			bvo.setDef11(bodyjson.getJSONObject(i).getString("def11"));
			bvo.setDef12(bodyjson.getJSONObject(i).getString("def12"));
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}
	/**
	 * ��ͷ������Ч��У��
	 * @param json
	 * @throws BusinessException 
	 */
	private void validHeadData(JSONObject json) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		String pk_org = getRefAttributePk("pk_org" , json.getString("pk_org"));
		String billdate = json.getString("billdate");
		String saleid = json.getString("def1");
//		String saleno = json.getString("def2");
		String def3 = json.getString("def3");
		String def4 = json.getString("def4");
		
		if (pk_org == null || "".equals(pk_org)) 
			msg.append("�����쳣��������֯Ϊ�ջ򲻴�����NCϵͳ�У�");
		if(billdate == null || "".equals(billdate))
			msg.append("�������ڲ���Ϊ�գ�");
		if(saleid == null || "".equals(saleid))
			msg.append("��ϵͳ��������Ϊ�գ�");
//		if(saleno == null || "".equals(saleno))
//			msg.append("��ϵͳ���Ų���Ϊ�գ�");
//		if(def3 == null || "".equals(def3))
//			msg.append("Ӱ����벻��Ϊ�գ�");
//		if(def4 == null || "".equals(def4))
//			msg.append("Ӱ��״̬����Ϊ�գ�");
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
	
	/**
	 * ����������Ч��У��
	 * @param json
	 * @throws BusinessException 
	 */
	private void validBodyData(JSONArray jsonArray) throws BusinessException{
		StringBuffer msg = new StringBuffer();
		for(int i = 0; i<jsonArray.size(); i++){
			JSONObject json = jsonArray.getJSONObject(i);
//			String scomment = json.getString("scomment");
			String def1 = json.getString("def1");
			String def2 = json.getString("def2");
			String def3 = json.getString("def3");
			String def4 = json.getString("def4");
			String def5 = json.getString("def5");
			String def6 = json.getString("def6");
			String def7 = json.getString("def7");
			String def8 = json.getString("def8");
			String def9 = json.getString("def9");
//			String def11 = json.getString("def11");
//			String def12 = json.getString("def12");
			
//			if (scomment == null || "".equals(scomment)) {
//				msg.append("�ڡ�" + (i + 1) + "����,ժҪΪ�ջ򲻴���NCϵͳ��!");
//			}
			if (def1 == null || "".equals(def1)) {
				msg.append("�ڡ�" + (i + 1) + "����,���ز���Ŀ����Ϊ��!");
			}
			if (def2 == null || "".equals(def2)) {
				msg.append("�ڡ�" + (i + 1) + "����,���ز���Ŀ��ϸ����Ϊ��!");
			}
			if (def3 == null || "".equals(def3)) {
				msg.append("�ڡ�" + (i + 1) + "����,�������Ͳ���Ϊ��!");
			}
			if (def4 == null || "".equals(def4)) {
				msg.append("�ڡ�" + (i + 1) + "����,�������Ʋ���Ϊ��!");
			}
			if (def5 == null || "".equals(def5)) {
				msg.append("�ڡ�" + (i + 1) + "����,ҵ̬����Ϊ��!");
			}
			if (def6 == null || "".equals(def6)) {
				msg.append("�ڡ�" + (i + 1) + "����,��˰��ʽ����Ϊ��!");
			}
			if (def7 == null || "".equals(def7)) {
				msg.append("�ڡ�" + (i + 1) + "����,˰�ʲ���Ϊ��!");
			}
			if (def8 == null || "".equals(def8)) {
				msg.append("�ڡ�" + (i + 1) + "����,˰���Ϊ��!");
			}
			if (def9 == null || "".equals(def9)) {
				msg.append("�ڡ�" + (i + 1) + "����,����˰����Ϊ��!");
			}
//			if (def11 == null || "".equals(def11)) {
//				msg.append("�ڡ�" + (i + 1) + "����,ԭ�����Ͳ���Ϊ��!");
//			}
//			if (def12 == null || "".equals(def12)) {
//				msg.append("�ڡ�" + (i + 1) + "����,����˵������Ϊ��!");
//			}
			if(msg.length() > 0)
				throw new BusinessException(msg.toString());
		}
	}
	
}
