package nc.bs.tg.outside.ebs.utils.appayPayrequest;

import java.util.HashMap;

import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.ebs.utils.appaybill.ApPayBillUtils;
import nc.bs.tg.outside.ebs.utils.paybill.PayBillUtils;
import nc.vo.pub.BusinessException;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class ApayRequestutil extends EBSBillUtils {
	static ApayRequestutil utils;

	public static ApayRequestutil getUtils() {
		if (utils == null) {
			utils = new ApayRequestutil();
		}
		return utils;
	}

	public String onSyncBill(HashMap<String, Object> value, String dectype,
			String srctype) throws BusinessException {
		String resultap = null;
		String resultreq = null;
		String returnMssage = "";

		try {
			JSONObject jsonobjreq = (JSONObject) value.get("request");
			if (jsonobjreq == null) {
				throw new BusinessException("付款申请单数据为空");
			}
			String srctypereq = (String) jsonobjreq.getString("srctypereq");// 付款申请单业务单据类型
			JSONObject jsonobja = (JSONObject) value.get("appay");
			if (jsonobja == null) {
				throw new BusinessException("应付单数据为空");
			}
			String srctypeapp = (String) jsonobja.getString("srctypeapp");// 应付单业务单据类型
			HashMap<String, Object> reqmap = JSON.parseObject(
					jsonobjreq.toJSONString(),
					new TypeReference<HashMap<String, Object>>() {
					});
			HashMap<String, Object> amap = JSON.parseObject(
					jsonobja.toJSONString(),
					new TypeReference<HashMap<String, Object>>() {
					});
			// 判断付款申请单是否先付款后补票和发票金额是否为0来确认是否保存应付单 2020-03-16-谈子健-start
//			JSONObject jsonData = (JSONObject) jsonobjreq.get("data");
//			String jsonhead = jsonData.getString("applyHeadVO");
//			String isbuyticket = JSONObject.parseObject(jsonhead).getString(
//					"isbuyticket");// 是否先付款后补票
//			String def40 = JSONObject.parseObject(jsonhead).getString("def40");// 发票金额是否为0
//			if ("N".equals(isbuyticket) && "N".equals(def40)) {
//				resultap = ApPayBillUtils.getUtils().onSyncBill(amap,
//						srctypeapp);// 应付单保存
//			}
			// 判断付款申请单是否先付款后补票和发票金额是否为0来确认是否保存应付单 2020-03-16-谈子健-end
			resultap = ApPayBillUtils.getUtils().onSyncBill(amap,
					srctypeapp);// 应付单保存
			resultreq = PayBillUtils.getUtils().onSyncBill(reqmap, dectype,
					srctypereq);// 付款申请单保存
			// 合并返回报文2020-02-14-谈子健
			returnMssage = getRreurnMsg(resultap, resultreq);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return returnMssage;

	}

	private String getRreurnMsg(String resultap, String resultreq) {
		String returnMssage = "";
		HashMap resultapMap = JSON.parseObject(resultap, HashMap.class);
		HashMap resultreqMap = JSON.parseObject(resultreq, HashMap.class);
		HashMap mssageMap = new HashMap<>();
		if (resultapMap != null) {
			String appBillNo = (String) resultapMap.get("billno");
			String appBillId = (String) resultapMap.get("billid");
			mssageMap.put("billnoApp", appBillNo);
			mssageMap.put("billidApp", appBillId);
		}

		if (resultreqMap != null) {
			String reqBillNo = (String) resultreqMap.get("billno");
			String reqBillId = (String) resultreqMap.get("billid");
			mssageMap.put("billnoReq", reqBillNo);
			mssageMap.put("billidReq", reqBillId);
		}
		
		
		returnMssage = JSON.toJSONString(mssageMap);

		return returnMssage;
	}
}
