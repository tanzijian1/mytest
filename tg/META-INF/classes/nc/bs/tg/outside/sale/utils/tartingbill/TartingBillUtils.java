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
 * 挞定工单(FN16)
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
	 * 挞定工单
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

		JSONObject jsonObject = (JSONObject) value.get("headInfo");// 获取表头信息
		
		String saleid = jsonObject.getString("def1");// 销售系统业务单据ID
		String saleno = jsonObject.getString("def2");// 销售系统业务单据单据号
		HashMap<String, String> dataMap = new HashMap<String, String>();
		String billqueue = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleid;
		String billkey = SaleBillCont.getBillNameMap().get(billtype) + ":"
				+ saleno;
		// TODO Saleid 按实际存入信息位置进行变更
		AggTartingBillVO aggVO = (AggTartingBillVO) getBillVO(
				AggTartingBillVO.class, "isnull(dr,0)=0 and def1 = '"
						+ saleid + "'");
		if (aggVO != null) {
			dataMap.put("billid", aggVO.getPrimaryKey());
			dataMap.put("billno", (String) aggVO.getParentVO()
					.getAttributeValue("billno"));
			return JSON.toJSONString(dataMap);
		}
		BPMBillUtils.addBillQueue(billqueue);// 增加队列处理
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
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BPMBillUtils.removeBillQueue(billqueue);
		}
	}

	/**
	 * 将来源信息转换成NC信息
	 * 
	 * @param hMap
	 * @return
	 */
	private AggTartingBillVO onTranBill(HashMap<String, Object> value)
			throws BusinessException {
		AggTartingBillVO aggvo = new AggTartingBillVO();
		JSONObject headjson = (JSONObject) value.get("headInfo");
		if(headjson.isEmpty()){
			throw new BusinessException("数据异常，表头数据不能为空！");
		}
		JSONArray bodyjson = (JSONArray) value.get("itemInfo");
		if(bodyjson.isEmpty()){
			throw new BusinessException("数据异常，表体数据不能为空！");
		}
		validHeadData(headjson);
		validBodyData(bodyjson);
		
		//表头信息设值
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
		hvo.setApprovestatus(-1);//审批状态
		hvo.setEffectstatus(0);//生效状态
		hvo.setDef10(getUserByPsondoc(headjson.getString("def10")));//销售系统审批人
		hvo.setDef15(headjson.getString("def15"));//附件张数
		aggvo.setParentVO(hvo);
		
		//表体信息设值
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
			bvo.setDef10(bodyjson.getJSONObject(i).getString("def10"));//挞定金额
			bvo.setDef11(bodyjson.getJSONObject(i).getString("def11"));
			bvo.setDef12(bodyjson.getJSONObject(i).getString("def12"));
			bvos[i] = bvo;
		}
		aggvo.setChildrenVO(bvos);
		return aggvo;
	}
	/**
	 * 表头数据有效性校验
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
			msg.append("数据异常，财务组织为空或不存在于NC系统中！");
		if(billdate == null || "".equals(billdate))
			msg.append("单据日期不能为空！");
		if(saleid == null || "".equals(saleid))
			msg.append("外系统主键不能为空！");
//		if(saleno == null || "".equals(saleno))
//			msg.append("外系统单号不能为空！");
//		if(def3 == null || "".equals(def3))
//			msg.append("影像编码不能为空！");
//		if(def4 == null || "".equals(def4))
//			msg.append("影像状态不能为空！");
		if(msg.length() > 0)
			throw new BusinessException(msg.toString());
	}
	
	/**
	 * 表体数据有效性校验
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
//				msg.append("第【" + (i + 1) + "】行,摘要为空或不存在NC系统中!");
//			}
			if (def1 == null || "".equals(def1)) {
				msg.append("第【" + (i + 1) + "】行,房地产项目不能为空!");
			}
			if (def2 == null || "".equals(def2)) {
				msg.append("第【" + (i + 1) + "】行,房地产项目明细不能为空!");
			}
			if (def3 == null || "".equals(def3)) {
				msg.append("第【" + (i + 1) + "】行,款项类型不能为空!");
			}
			if (def4 == null || "".equals(def4)) {
				msg.append("第【" + (i + 1) + "】行,款项名称不能为空!");
			}
			if (def5 == null || "".equals(def5)) {
				msg.append("第【" + (i + 1) + "】行,业态不能为空!");
			}
			if (def6 == null || "".equals(def6)) {
				msg.append("第【" + (i + 1) + "】行,计税方式不能为空!");
			}
			if (def7 == null || "".equals(def7)) {
				msg.append("第【" + (i + 1) + "】行,税率不能为空!");
			}
			if (def8 == null || "".equals(def8)) {
				msg.append("第【" + (i + 1) + "】行,税额不能为空!");
			}
			if (def9 == null || "".equals(def9)) {
				msg.append("第【" + (i + 1) + "】行,不含税金额不能为空!");
			}
//			if (def11 == null || "".equals(def11)) {
//				msg.append("第【" + (i + 1) + "】行,原因类型不能为空!");
//			}
//			if (def12 == null || "".equals(def12)) {
//				msg.append("第【" + (i + 1) + "】行,申请说明不能为空!");
//			}
			if(msg.length() > 0)
				throw new BusinessException(msg.toString());
		}
	}
	
}
