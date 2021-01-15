package nc.bs.itf.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BusinessBillLogVO;

import org.apache.commons.codec.binary.Base64;

import sun.misc.BASE64Encoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpBusinessUtils {
	private static HttpBusinessUtils utils;

	public static HttpBusinessUtils getUtils() {
		if (utils == null)
			utils = new HttpBusinessUtils();
		return utils;
	}

	/**
	 * 商业系统传输
	 * 
	 * @param url
	 * @param context
	 * @param logVO
	 * @return
	 * @throws Exception
	 */
	public String connection(String url, String context, BusinessBillLogVO logVO)
			throws Exception {
		String appkey = OutsideUtils.getOutsideInfo("BUSINESS_appkey");
		String appid = OutsideUtils.getOutsideInfo("BUSINESS_appid");
		long timestamp = System.currentTimeMillis();
		String nonce = "2221";
		String encryptcode = OutsideUtils
				.getOutsideInfo("BUSINESS_encryptcode");
		String zipcode = OutsideUtils.getOutsideInfo("BUSINESS_zipcode");
		String signtype = OutsideUtils.getOutsideInfo("BUSINESS_signtype");
		BASE64Encoder encoder = new BASE64Encoder();
		String context64 = new String(Base64.encodeBase64(context
				.getBytes("UTF-8")));
		String md5str = "appid" + appid + "content" + context64 + "encryptcode"
				+ encryptcode + "nonce" + nonce + "signtype" + signtype
				+ "timestamp" + timestamp + "appkey" + appkey;
		// String signature= MD5Util.getMD5(md5str).toUpperCase();
		String signature = getMD5(md5str);

		HashMap<String, Object> map = new HashMap<>();
		map.put("appid", appid);
		map.put("timestamp", timestamp);
		map.put("signature", signature);
		map.put("encryptcode", encryptcode);
		map.put("signtype", "m");
		map.put("nonce", nonce);
		map.put("content", context64);

		logVO.setSrcparm("【转换前报文】：" + context + "【转换后报文】："
				+ JSON.toJSONString(map));
		String returnjson = null;
		try {
			returnjson = connectionjson(url, JSON.toJSONString(map));
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				String flag = jobj.getString("code");
				if (("0".equals(flag))) {
					JSONArray datas = jobj.getJSONArray("data");
					JSONObject data = (JSONObject) datas.get(0);
					String rspCode = data.getString("rspCode");
					if (("0".equals(rspCode))) {
						logVO.setResult("Y");
					} else {
						logVO.setResult("N");
						logVO.setErrmsg(data.getString("rspMsg"));
						throw new BusinessException("商业 exception: "
								+ data.getString("rspMsg"));
					}
				} else {
					logVO.setResult("N");
					logVO.setErrmsg(jobj.getString("message"));
					throw new BusinessException("商业 exception: "
							+ jobj.getString("message"));
				}
			}
		} catch (Exception e1) {
			logVO.setErrmsg(e1.getMessage() + "返回报文：" + returnjson);
			throw new BusinessException(e1.getMessage());
		} finally {
			NCLocator.getInstance().lookup(ISqlThread.class)
					.billInsert_RequiresNew(logVO);
		}

		return returnjson;

	}

	/*
	 * 传json
	 */
	public String connectionjson(String address, String data)
			throws BusinessException {

		StringBuffer sb = new StringBuffer();
		String responsecode = null;
		try {
			URL url = new URL(address);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setConnectTimeout(60000);
			connection.connect();
			if (data != null) {
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(connection.getOutputStream(),
								"UTF-8"));
				writer.write(data);
				writer.flush();
			}
			String temp = "";
			responsecode = String.valueOf(connection.getResponseCode());
			if (200 == connection.getResponseCode()) {
				BufferedReader br = new BufferedReader(new BufferedReader(
						new InputStreamReader(connection.getInputStream(),
								"utf-8")));
				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}
			} else {
				BufferedReader br = new BufferedReader(new BufferedReader(
						new InputStreamReader(connection.getErrorStream(),
								"utf-8")));

				while ((temp = br.readLine()) != null) {
					sb.append(temp);
				}
				throw new BusinessException("响应码" + responsecode + sb);
			}
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			throw new BusinessException("连接异常 " + responsecode + "    "
					+ e.getMessage());
		}
		return sb.toString();
	}

	public String getMD5(String md5) throws BusinessException {
		try {

			char[] hexDigits = new char[] { '0', '1', '2', '3', '4', '5', '6',
					'7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			mdInst.update(md5.getBytes());
			byte[] md = mdInst.digest();
			int j = md.length;
			char[] str = new char[j * 2];
			int k = 0;

			for (int i = 0; i < j; ++i) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 15];
				str[k++] = hexDigits[byte0 & 15];
			}

			return new String(str);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
	}

	public static void bytesToHex(byte[] bytes) {
	}

}
