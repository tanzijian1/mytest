package nc.bs.tg.call.ebs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.tg.outside.OutsideUtils;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONObject;

/**
 * srm接收NC回传邻里的投标保证金的核销状态
 * 
 * 
 * @author ASUS
 * 
 */
public class PushSRMBondCheckReceiptInfoImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		/**
		 * 接口地址 测试：http://times-dpctest.timesgroup.cn:6001/postMethod?code=
		 * writeOffDepositLinli&syscode=test&token=
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

			String urls = ip + "/postMethod?code=writeOffDepositLinli&syscode="
					+ syscode + "&token=" + token;
			info.setToken(tokenkey);
			info.setClassName(methodname);
			info.setDessystem(dessystem);
			info.setUrls(urls);
			info.setIsrequiresnew("N");
			Map<String, Object> other = new HashMap<String, Object>();
			info.setOther(other);
			Map<String, Object> map = (Map<String, Object>) value;
			String billid = (String) map.get("billid");
			String isverific = (String) map.get("isverific");
			String userId = InvocationInfoProxy.getInstance().getUserId();
			String operator = getwriteOffOperator(userId);
			Map<String, Object> billInfo = getDate(billid);

			Map<String, Object> pushdata = new HashMap<String, Object>();

			if (billInfo != null) {
				pushdata.put("depositNumber", billInfo.get("srcbillno"));
				// 保证金编号,必填
				// pushdata.put("depositNumber", "B202006230013");// 保证金编号,必填
				pushdata.put("vendorNumber", billInfo.get("supplier"));
				// 供应商编码,必填
				// pushdata.put("vendorNumber", "10002010");// 供应商编码,必填
				pushdata.put("writeOffStatus", "Y".equals(isverific) ? "已核销"
						: "未核销");// 核销状态,必填,包括未核销、已核销
				pushdata.put("ncDocumentId", billid);// NC单据ID,必填
				pushdata.put("ncDocumentNumber", billInfo.get("billno"));// NC单据编号,必填
				pushdata.put("writeOffDate", new UFDate().toStdString());// 核销日期,必填,格式YYYY-MM-DD
				String mny = billInfo.get("namout") == null ? "0.00" : billInfo
						.get("namout").toString();
				UFDouble amout = "Y".equals(isverific) ? new UFDouble(mny)
						: new UFDouble(-1).multiply(new UFDouble(mny));
				pushdata.put("writeOffAmount", amout.toDouble());// 核销金额,必填
				pushdata.put("writeOffOperator", operator);// 核销人编号,必填
				// pushdata.put("writeOffOperator", "08167");// 核销人编号,必填
			}
			JSONObject infodata = new JSONObject();
			infodata.put("req", pushdata);
			info.setPostdata(infodata.toJSONString());

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return info;
	}

	/**
	 * 通过用户id找员人编码 2020-04-21-谈子健
	 * 
	 * @throws BusinessException
	 */
	private String getwriteOffOperator(String userid) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select b.code  ");
		query.append("  from bd_psndoc b  ");
		query.append("  left join sm_user s  ");
		query.append("    on b.pk_psndoc = s.pk_psndoc  ");
		query.append(" where s.cuserid = '" + userid + "'  ");
		query.append("   and b.dr = 0  ");
		query.append("   and s.dr = 0  ");
		query.append("   and b.enablestate = 2  ");
		String code = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());
		return code;
	}

	private Map<String, Object> getDate(String billid) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("       select r.def2  as srcbillno,  ");
		query.append("       c.code   as supplier,  ");
		query.append("       r.pk_recbill as billid,  ");
		query.append("       r.billno     as billno,  ");
		query.append("       r.money      as namout  ");
		query.append("  from ar_recbill r  ");
		query.append("  left join ar_recitem a  ");
		query.append("    on r.pk_recbill = a.pk_recbill  ");
		query.append("    left join bd_customer c  ");
		query.append("    on a.customer = c.pk_customer  ");
		query.append(" where r.pk_recbill = '" + billid + "'  ");
		query.append("   and r.dr = 0  ");
		query.append("   and a.dr = 0  ");
		BaseDAO dao = new BaseDAO();
		Map<String, Object> list = (Map<String, Object>) dao.executeQuery(
				query.toString(), new MapProcessor());
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
				return resultMsg;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return null;
	}
}
