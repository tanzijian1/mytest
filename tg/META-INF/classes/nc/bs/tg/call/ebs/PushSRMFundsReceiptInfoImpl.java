package nc.bs.tg.call.ebs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * srm供应商请款接口NC回执接口
 * 
 * 读取邻里已结算后的SRM请款单的信息
 * 
 * @author ASUS
 * 
 */
public class PushSRMFundsReceiptInfoImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		/**
		 * 接口地址 测试：http://times-dpctest.timesgroup.cn:6001/postMethod?code=
		 * querySupplierNCReceipt&syscode=test&token=
		 */
		CallImplInfoVO info = new CallImplInfoVO();
		try {
			String ip = OutsideUtils.getOutsideInfo("EBS-URL");
			String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");

			String token = "";
			String tokenkey = "";
			String key = OutsideUtils.getOutsideInfo("EBS-KEY").trim();
			if ("nc".equals(syscode)) {
				Date date = new Date();
				SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
				String time = formater.format(date);
				tokenkey = time + key;
				token = MD5Util.getMD5(tokenkey).toUpperCase();
			}

			String urls = ip
					+ "/postMethod?code=querySupplierNCReceipt&syscode="
					+ syscode + "&token=" + token;
			
			info.setClassName(methodname);
			info.setDessystem(dessystem);
			info.setUrls(urls);
			info.setIsrequiresnew("Y");
			Map<String, Object> map = (Map<String, Object>) value;

			Map<String, Object> other = new HashMap<String, Object>();
			other.put("settid", map.get("pk_settlement"));
			info.setOther(other);
			Map<String, Object> pushdate = new HashMap<String, Object>();
			pushdate.put("pay_billNo", map.get("srcbillno"));// 请款单号
			pushdate.put("billNo", map.get("srcbillno"));// 请款单号
			pushdate.put("pay_billStatus", "APPROVED");// 请款状态
			pushdate.put("contractCode", map.get("contcode"));// 合同编码
			pushdate.put("requestType", map.get("requesttype"));// 请款类型
			pushdate.put("payment_amount", map.get("amount"));// 已付金额
			pushdate.put("TaskID", map.get("bpmid"));// BPMID
			pushdate.put("index", "ncReceipt");

			// 通过结算单追溯回付款信息
			JSONArray lines = new JSONArray();
			pushdate.put("lines", lines);
			List<Map<String, Object>> list = getBoydInfos(
					(String) map.get("pk_tradetype"),
					(String) map.get("srcbillno"));
			if (list != null && list.size() > 0) {
				for (Map<String, Object> bodyInfo : list) {
					JSONObject line = new JSONObject();
					line.put(
							"paymentamount",
							bodyInfo.get("paymentamount") == null ? 0d
									: new UFDouble(bodyInfo
											.get("paymentamount").toString())
											.toDouble());// 付款金额
					line.put("characterType", bodyInfo.get("charactertype"));// 款项类型
					line.put("rowId", bodyInfo.get("srcbodyid"));// 行ID
					lines.add(line);
				}
			} else {
				throw new BusinessException("SRM请款单[" + map.get("srcbillno")
						+ "]未能找到对应明细信息");
			}

			JSONObject req = new JSONObject();
			req.put("req", pushdate);
			info.setPostdata(req.toJSONString());

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return info;
	}

	private List<Map<String, Object>> getBoydInfos(String pk_tradetype,
			String srcbillno) throws BusinessException {
		StringBuffer sql = new StringBuffer();
		if ("F3-Cxx-LL01".equals(pk_tradetype)) {
			sql.append(" SELECT payb.money_de paymentamount")
					// 付款金额
					.append(",bd_defdoc.code charactertype")
					// 款项类型
					.append(",payb.def26 srcbodyid")
					// EBS行主键
					.append(" FROM   ap_paybill payh ")
					.append(" INNER  JOIN ap_payitem payb ON payh.pk_paybill = payb.pk_paybill ")
					.append(" LEFT   JOIN bd_defdoc ON     payb.def14 = bd_defdoc.pk_defdoc ")
					.append(" WHERE  payh.dr = 0 AND    payb.dr = 0 AND    payh.pk_tradetype = 'F3-Cxx-LL01' ")
					.append(" AND    payh.def2 = '" + srcbillno + "'");
		} else if ("F3-Cxx-LL09".equals(pk_tradetype)) {
			sql.append(" SELECT payb.money_cr paymentamount")
					// 付款金额
					.append(",bd_defdoc.code charactertype")
					// 款项类型
					.append(",payb.def26 srcbodyid")
					// EBS行主键
					.append(" FROM   ap_payablebill payh ")
					.append(" INNER  JOIN ap_payableitem payb ON payh.pk_payablebill = payb.pk_payablebill ")
					.append(" LEFT   JOIN bd_defdoc ON     payb.def14 = bd_defdoc.pk_defdoc ")
					.append(" WHERE  payh.dr = 0 AND    payb.dr = 0 AND    payh.pk_tradetype = 'F1-Cxx-LL01' ")
					.append(" AND    payh.def2 = '" + srcbillno + "'");
		}
		List<Map<String, Object>> list = (List<Map<String, Object>>) BillUtils
				.getUtils().getBaseDAO()
				.executeQuery(sql.toString(), new MapListProcessor());

		return list;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		return result;
	}

	@Override
	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		JSONObject json = JSONObject.parseObject(info.getPostdata());
		PostMethod postMethod = null;
		postMethod = new PostMethod(info.getUrls());
		postMethod.setRequestBody(json.toString());
		NameValuePair[] nvps = new NameValuePair[json.size()];
		int i = 0;
		postMethod.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");
		for (String str : json.keySet()) {
			nvps[i] = new NameValuePair(str, json.getString(str).trim());
			i++;
		}
		postMethod.setRequestBody(nvps);
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();
		int response;
		try {
			response = httpClient.executeMethod(postMethod);
			if (response == 200) {
				String msg = postMethod.getResponseBodyAsString().toString();
				org.json.JSONObject result = new org.json.JSONObject(msg);
				String flag = (String) result.getString("code");
				if (!flag.equals("S")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("msg") + "】");
				}
				String resultMsg = result.getString("data");
				String sql = "update cmp_settlement set def1 = 'Y' where pk_settlement = '"
						+ info.getOther().get("settid") + "'";
				new BaseDAO().executeUpdate(sql);

				return resultMsg;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return null;
	}
}
