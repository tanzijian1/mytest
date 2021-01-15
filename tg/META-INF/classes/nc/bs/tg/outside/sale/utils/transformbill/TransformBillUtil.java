package nc.bs.tg.outside.sale.utils.transformbill;

import java.util.HashMap;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.cmp.bill.TransformBillAggVO;
import nc.vo.cmp.bill.TransformBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TransformBillUtil extends SaleBillUtils{
	static TransformBillUtil utils;
	
	public static TransformBillUtil getUtils() {
		if (utils == null) {
			utils = new TransformBillUtil();
		}
		return utils;
	}
	/**
	 * ���˽���
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
		if(jsonObject.isEmpty()){
			throw new BusinessException("�����쳣����ͷ���ݲ���Ϊ�գ�");
		}
		String saleid = jsonObject.getString("def1");// ����ϵͳҵ�񵥾�ID
		String saleno = jsonObject.getString("def2");// ����ϵͳҵ�񵥾ݵ��ݺ�
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		HashMap<String, String> dataMap = new HashMap<String, String>();
		// TODO Saleid ��ʵ�ʴ�����Ϣλ�ý��б��
		TransformBillAggVO aggVO = (TransformBillAggVO) getBillVO(
				TransformBillAggVO.class, "isnull(dr,0)=0 and def1 = '" + saleid
						+ "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue(TransformBillVO.VBILLNO));
			return JSON.toJSONString(dataMap);}
		BPMBillUtils.addBillQueue(billqueue);// ���Ӷ��д���
		try {
			TransformBillAggVO  billvo = onTranBill(value);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);
			Object obj = getPfBusiAction().processAction("SAVEBASE", "36S4", null, billvo,
					null, eParam);
			TransformBillAggVO[] billvos = (TransformBillAggVO[]) obj;
			dataMap.put("billid", billvos[0].getPrimaryKey());
			dataMap.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(TransformBillVO.VBILLNO));
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
	private TransformBillAggVO  onTranBill(HashMap<String, Object> value)
			throws BusinessException {
		TransformBillAggVO  aggvo= new TransformBillAggVO ();
		JSONObject headjson = (JSONObject) value.get("headInfo");
		if(headjson.isEmpty()){
			throw new BusinessException("�����쳣����ͷ���ݲ���Ϊ�գ�");
		}
//		JSONArray bodyjson = (JSONArray) value.get("itemInfo");
//		if(bodyjson.isEmpty()){
//			throw new BusinessException("�����쳣���������ݲ���Ϊ�գ�");
//		}
		validHeadData(headjson);
		TransformBillVO hvo = new TransformBillVO();
		String pk_org = getRefAttributePk("pk_org" , headjson.getString("pk_org"));
		String pk_currtype = getRefAttributePk("pk_currtype", headjson.getString("pk_currtype"));
		String transformoutbank = getRefAttributePk("transformoutbank", headjson.getString("transformoutbank"));
		String transformoutaccount = getRefAttributePk("transformoutaccount", headjson.getString("transformoutaccount"));
		String transforminbank = getRefAttributePk("transformoutbank", headjson.getString("transforminbank"));
		String transforminaccount = getRefAttributePk("transformoutbank", headjson.getString("transforminaccount"));
		String pk_balatype = getRefAttributePk("pk_balatype", headjson.getString("pk_balatype"));
		String pk_billtype = getRefAttributePk("pk_billtype", value.get("billtype").toString());
		hvo.setPk_org(pk_org);
		hvo.setPk_billtypecode(value.get("billtype").toString());
		hvo.setPk_billtypeid(pk_billtype);
		hvo.setPk_currtype(pk_currtype);
		hvo.setTransformoutbank(transformoutbank);
		hvo.setTransformoutaccount(transformoutaccount);
		hvo.setTransforminbank(transforminbank);
		hvo.setTransforminaccount(transforminaccount);
		hvo.setPk_balatype(pk_balatype);
		hvo.setBilldate(new UFDate(headjson.getString("billdate")));
		hvo.setVdef1(headjson.getString("def1"));
		hvo.setVdef2(headjson.getString("def2"));
		hvo.setVdef3(headjson.getString("def3"));
		hvo.setVdef4(headjson.getString("def4"));
		hvo.setAmount(new UFDouble(headjson.getString("amount")));
		hvo.setOlcrate(new UFDouble(headjson.getString("olcrate")));
		hvo.setOlcamount(new UFDouble(headjson.getString("olcamount")));
		hvo.setBillmaker(getSaleUserID());
		hvo.setBillmakedate(new UFDate());
		
		aggvo.setParentVO(hvo);
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
		String saleno = json.getString("def2");
		String def3 = json.getString("def3");
		String def4 = json.getString("def4");
		String pk_currtype = getRefAttributePk("pk_currtype", json.getString("pk_currtype"));
		String transformoutbank = getRefAttributePk("transformoutbank", json.getString("transformoutbank"));
		String transformoutaccount = getRefAttributePk("transformoutaccount", json.getString("transformoutaccount"));
		String transforminbank = getRefAttributePk("transformoutbank", json.getString("transforminbank"));
		String transforminaccount = getRefAttributePk("transformoutbank", json.getString("transforminaccount"));
		String amount = json.getString("amount");
		String olcrate = json.getString("olcrate");
		String olcamount = json.getString("olcamount");
		String pk_balatype = getRefAttributePk("pk_balatype", json.getString("pk_balatype"));
		
		if (pk_org == null || "".equals(pk_org)) 
			msg.append("�����쳣��������֯Ϊ�ջ򲻴�����NCϵͳ�У�");
		if(billdate == null || "".equals(billdate))
			msg.append("�������ڲ���Ϊ�գ�");
		if(saleid == null || "".equals(saleid))
			msg.append("��ϵͳ��������Ϊ�գ�");
		if(saleno == null || "".equals(saleno))
			msg.append("��ϵͳ���Ų���Ϊ�գ�");
//		if(def3 == null || "".equals(def3))
//			msg.append("Ӱ����벻��Ϊ�գ�");
//		if(def4 == null || "".equals(def4))
//			msg.append("Ӱ��״̬����Ϊ�գ�");
		if(pk_currtype == null || "".equals(pk_currtype))
			msg.append("���ֲ���Ϊ�գ�");
		if(transformoutbank == null || "".equals(transformoutbank))
			msg.append("�������в���Ϊ�գ�");
		if(transformoutaccount == null || "".equals(transformoutaccount))
			msg.append("�����˻�����Ϊ�գ�");
		if(transforminbank == null || "".equals(transforminbank))
			msg.append("�������в���Ϊ�գ�");
		if(transforminaccount == null || "".equals(transforminaccount))
			msg.append("�����˻�����Ϊ�գ�");
		if(amount == null || "".equals(amount))
			msg.append("����Ϊ�գ�");
		if(olcrate == null || "".equals(olcrate))
			msg.append("���һ��ʲ���Ϊ�գ�");
		if(olcamount == null || "".equals(olcamount))
			msg.append("��֯���ҽ���Ϊ�գ�");
		if(pk_balatype == null || "".equals(pk_balatype))
			msg.append("���㷽ʽ����Ϊ�գ�");
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
}
