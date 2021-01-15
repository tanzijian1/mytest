package nc.bs.tg.outside.bpm;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import uap.serverdes.appesc.MD5Util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.push.QueryDocInfoUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.AppContext;

public class SendBillToBPMUtils {
	static SendBillToBPMUtils sendbillutils;

	public static SendBillToBPMUtils getSendbill() {
		if (sendbillutils == null) {
			sendbillutils = new SendBillToBPMUtils();
		}
		return sendbillutils;
	}

	public void SendBillToBPM(String bussinessId, String command)
			throws Exception {
		String getUrlSql = "select url from NC_URLPORT where sysid = 'bpm'";
		String address = QueryDocInfoUtils.getUtils().getColumnValue(getUrlSql);
		String urls = address + "/YZSoft/WebService/YZService.ashx";
		ObjectMapper objectMapper = new ObjectMapper();

		UFDateTime time = AppContext.getInstance().getServerTime();
		String app_key = PropertiesUtil.getInstance("ebs_url.properties")
				.readValue("APP_KEY");
		String app_key_ticket = PropertiesUtil
				.getInstance("ebs_url.properties").readValue("APP_KEY_TICKET");

		String key = app_key + app_key_ticket + time.getYear()
				+ time.getStrMonth() + time.getStrDay();
		String app_key_tickening = MD5Util.getMD5(key);

		Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
		if (bussinessId != null && !"".equals(bussinessId)) {
			postdata.put("BussinessId", postdata);// EBS系统主键ID
		}

		if (command != null && !"".equals(command)) {
			postdata.put("Command", command);// 退单意见
		}

		String json = objectMapper.writeValueAsString(postdata);
		Logger.error("----------请求开始---------------");
		Logger.error("----------请求参数：【" + json + "】---------------");
		// 创建url资源
		URL url = new URL(urls);
		// 建立http连接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置允许输出
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", "utf-8");
		// 转换为字节数组
		byte[] data = json.getBytes("utf-8");
		// 设置文件长度
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// 设置文件类型:
		conn.setRequestProperty("Content-Type", "application/json");
		// 设置密钥
		conn.setRequestProperty("Authorization", app_key_tickening);
		// 开始连接请求
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// 写入请求的字符串
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// 请求返回的状态
		if (conn.getResponseCode() == 200) {
			Logger.error("连接成功");
			// 请求返回的数据
			BufferedReader in = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			while ((line = in.readLine()) != null) {
				a += line;
			}
			JSONObject result = new JSONObject(a);
			String flag = (String) result.getString("success");
			String errmsg = null;
			if (flag.equals("true")) {
				// 成功
			} else {
				errmsg = (String) result.getString("errorMessage");
				throw new BusinessException("【来自BPM的错误信息：" + errmsg + "】");
			}
		} else {
			Logger.error("连接失败");
			throw new BusinessException("连接失败");
		}
	}
}
