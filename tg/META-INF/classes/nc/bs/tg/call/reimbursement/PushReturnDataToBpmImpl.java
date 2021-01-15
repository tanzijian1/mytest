package nc.bs.tg.call.reimbursement;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.outside.servlet.TGImplCallHttpClient;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;
import uap.serverdes.appesc.MD5Util;

import com.alibaba.fastjson.JSONObject;

/**
 * BPM退回发起人接口 2020年9月21日09:43:00
 * 
 * @author ln
 * 
 */
public class PushReturnDataToBpmImpl extends TGImplCallHttpClient {
	@Override
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException {
		String ip = TGOutsideUtils.getUtils().getOutsidInfo("BPM");
		Map<String, Object> map = (Map<String, Object>) value;
		CallImplInfoVO info = new CallImplInfoVO();
		info.setClassName("nc.bs.tg.call.reimbursement.PushReturnDataToBpmImpl");
		info.setDessystem(dessystem);
		String urls = ip
				+ "/YZSoft/WebService/TimesNeighborhood/TimesNeighborhood.ashx?Method=ReturnToInitiator&UserAccount="
				+ map.get("usercode") + "&IsPrivilege=true";
		info.setUrls(urls);
		Date date = new Date();
		String app_key = TGOutsideUtils.getUtils().getOutsidInfo("BPM_KEY");
		String app_key_ticket = TGOutsideUtils.getUtils().getOutsidInfo(
				"BPM_KEY_TICKET");
		SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");// 年月日时分
		String time = formater.format(date);
		String tokenkey = app_key + app_key_ticket + time;
		String token = MD5Util.getMD5(tokenkey).toUpperCase();
		info.setToken(token);
		map.remove("usercode");
		info.setPostdata(JSONObject.toJSONString(map));
		Map<String, Object> other = new HashMap<String, Object>();
		info.setOther(other);
		return info;
	}

	/**
	 * 设置参数配置
	 * 
	 * @param conn
	 * @param info
	 * @throws ProtocolException
	 * @throws IOException
	 */
	protected void initConnParameter(HttpURLConnection conn, CallImplInfoVO info)
			throws ProtocolException, IOException {
		super.initConnParameter(conn, info);
		conn.setRequestProperty("Authorization", info.getToken());
		conn.setRequestProperty("System", "NC");
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
				resultMsg = msg;
				String flag = (String) result.getString("Success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("Content") + "】");
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
		// TODO 自动生成的方法存根
		return result;
	}

}
