package nc.bs.tg.call.share;

import hz.vo.fssc.HzSystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSONObject;

/**
 * 单据删除时通知共享
 * 
 * @author ln
 * 
 */
public class PushDeleteDataToShareImpl extends TGImplCallHttpClient {

	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String ip = TGOutsideUtils.getUtils().getOutsidInfo("SHARE_URL");
		Map<String, Object> map = (Map<String, Object>) value;
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName("nc.bs.tg.call.share.PushDeleteDataToShareImpl");
		info.setDessystem(dessystem);
		String urls = ip;
		info.setUrls(urls);
		try {
			String token = getToken();
			info.setToken(token);
		} catch (Exception e) {
			throw new BusinessException("获取共享Token异常:" + e.getMessage());
		}
		info.setPostdata(JSONObject.toJSONString(map));
		Map<String, Object> other = new HashMap<String, Object>();
		info.setOther(other);

		return info;
	}

	@Override
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		// TODO 自动生成的方法存根
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
				String flag = (String) result.getString("success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("message") + "】");
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	private String getToken() throws HttpException, IOException {
		HzSystem hzSystem = getSystemId("getBpmToken");
		PostMethod postMethod = null;
		postMethod = new PostMethod(hzSystem.getUrl());
		postMethod.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=utf-8");

		NameValuePair[] data = {
				new NameValuePair("appId", hzSystem.getAPPID()),
				new NameValuePair("secret", hzSystem.getSecret()) };
		postMethod
				.setRequestBody((org.apache.commons.httpclient.NameValuePair[]) data);
		org.apache.commons.httpclient.HttpClient httpClient = new org.apache.commons.httpclient.HttpClient();

		int response = httpClient.executeMethod(postMethod);
		if (response == 200) {
			return ((Map) (JSONObject.parse(postMethod
					.getResponseBodyAsString().toString()))).get("data")
					.toString();
		}
		return null;
	}

	private static HzSystem getSystemId(String systemid) {
		IUAPQueryBS querybs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		StringBuffer sql = new StringBuffer(
				"select * from hz_system where systemid = '" + systemid + "'");
		HzSystem result = null;
		try {
			result = (HzSystem) querybs.executeQuery(sql.toString(),
					new BeanProcessor(HzSystem.class));
		} catch (BusinessException e) {
			e.printStackTrace();
		}

		return result;
	}

	@Override
	protected String onBusinessProcessing(CallImplInfoVO info, String result) {
		// TODO 自动生成的方法存根
		return result;
	}

}
