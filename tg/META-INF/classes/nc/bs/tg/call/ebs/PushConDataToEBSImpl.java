package nc.bs.tg.call.ebs;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.os.outside.TGOutsideUtils;
import nc.itf.tg.outside.ITGCallService;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONObject;

/**
 * 参照合同请款传数据给EBS
 * 
 * @author ln
 * 
 */
public class PushConDataToEBSImpl implements ITGCallService {

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String ip = TGOutsideUtils.getUtils().getOutsidInfo("EBS-URL");
		JSONObject json = (JSONObject) value;
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName("nc.bs.tg.call.ebs.PushConDataToEBSImpl");
		info.setDessystem(dessystem);
		String token = "";
		String tokenkey = "";
		String key = OutsideUtils.getOutsideInfo("EBS-KEY").trim();
		String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
		if ("nc".equals(syscode)) {
			Date date = new Date();
			SimpleDateFormat formater = new SimpleDateFormat("yyyyMMddHHmm");// 年月日时分
			String time = formater.format(date);
			tokenkey = time + key;
			token = MD5Util.getMD5(tokenkey).toUpperCase();
		}

		String urls = ip + "/postMethod?code=capiface&syscode=" + syscode
				+ "&token=" + token;
		info.setUrls(urls);
		info.setPostdata(json.toJSONString());
		Map<String, Object> other = new HashMap<String, Object>();
		info.setOther(other);
		return info;
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
