package nc.bs.tg.call.ebs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.tg.outside.OutsideUtils;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.pf.IplatFormEntry;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONObject;

public class PushRefundDepositToEBSImpl extends TGImplCallHttpClient {
	private IUAPQueryBS bs = null;

	protected IUAPQueryBS getIUAPQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	/**
	 * 流程交互类
	 * 
	 * @return
	 */
	private IplatFormEntry pfBusiAction = null;

	public IplatFormEntry getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IplatFormEntry.class);
		}
		return pfBusiAction;
	}

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		/**
		 * 接口地址 测试：http://times-dpctest.timesgroup.cn:6001/postMethod?code=
		 * contractreturnbond&syscode=test&token=
		 */
		CallImplInfoVO info = new CallImplInfoVO();
		try {
			Map<String, Object> map = (Map<String, Object>) value;
			if (map != null && map.size() > 0) {
				JSONObject req = new JSONObject();
				req.put("req", map);
				String ip = OutsideUtils.getOutsideInfo("EBS-URL");
				String syscode = OutsideUtils.getOutsideInfo("EBS-SYSTEM");
				String token = "";
				String tokenkey = "";
				String key = OutsideUtils.getOutsideInfo("EBS-KEY").trim();
				if ("nc".equals(syscode)) {
					Date date = new Date();
					SimpleDateFormat formater = new SimpleDateFormat(
							"yyyyMMddHHmm");// 年月日时分
					String time = formater.format(date);
					tokenkey = time + key;
					token = MD5Util.getMD5(tokenkey).toUpperCase();
				}
				String urls = ip
						+ "/postMethod?code=contractreturnbond&syscode="
						+ syscode + "&token=" + token;
				;
				info.setToken(tokenkey);
				info.setClassName(methodname);
				info.setDessystem(dessystem);
				info.setUrls(urls);
				info.setIsrequiresnew("Y");
				info.setPostdata(req.toJSONString());
				Map<String, Object> other = new HashMap<String, Object>();
				info.setOther(other);

			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return info;
	}

	@Override
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = "";
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				org.json.JSONObject result = new org.json.JSONObject(msg);
				String flag = (String) result.getString("code");
				if (!flag.equals("S")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("msg") + "】");
				} else {
					resultMsg = "【来自" + info.getDessystem() + "的信息：" + result
							+ "】";
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		return result;
	}

	public String EncoderByMd5(String str) {
		String result = "";
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");

			md5.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte b[] = md5.digest();
		int i;
		StringBuffer buf = new StringBuffer("");
		for (int offset = 0; offset < b.length; offset++) {
			i = b[offset];
			if (i < 0)
				i += 256;
			if (i < 16)
				buf.append("0");
			buf.append(Integer.toHexString(i));
		}
		result = buf.toString();

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
