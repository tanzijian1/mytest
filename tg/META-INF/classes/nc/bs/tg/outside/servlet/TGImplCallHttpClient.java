package nc.bs.tg.outside.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import nc.itf.tg.outside.ITGCallService;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 对外接口客户端接口, 仅支持Http+JSON/XML
 * 
 * @author ASUS
 * 
 */
public abstract class TGImplCallHttpClient implements ITGCallService {
	private String encoding = "utf-8";

	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		try {
			// 创建url资源
			URL url = new URL(info.getUrls());
			// 建立http连接
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			// 初始化配置信息
			initConnParameter(conn, info);
			// 开始连接请求
			conn.connect();
			onWriteInfo(conn, info.getPostdata());
			result = getResultInfo(conn, info);

			result = onBusinessProcessing(info, result);

		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 业务处理
	 * 
	 * @param info
	 *            Nc内部传入参数
	 * @param result
	 *            调用接口反馈参数
	 * @return
	 */
	protected abstract String onBusinessProcessing(CallImplInfoVO info,
			String result);

	/**
	 * 将传出参数写到外部系统BODY
	 * 
	 * @param conn
	 * @param postdata
	 * @throws IOException
	 */
	protected void onWriteInfo(HttpURLConnection conn, String postdata)
			throws IOException {
		byte[] data = postdata.getBytes(getEncoding());
		OutputStream out = conn.getOutputStream();
		// 写入请求的字符串
		out.write(data);
		out.flush();
		out.close();
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
		// 设置允许输出
		conn.setDoOutput(true);
		// 设置不用缓存
		conn.setUseCaches(false);
		// 设置传递方式
		conn.setRequestMethod("POST");
		// 设置维持长连接
		conn.setRequestProperty("Connection", "Keep-Alive");
		// 设置文件字符集:
		conn.setRequestProperty("Charset", getEncoding());
		// 设置文件类型:
		conn.setRequestProperty("Content-Type", "application/json");
		// 转换为字节数组
		byte[] data = info.getPostdata().getBytes(getEncoding());
		// 设置文件长度
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
	}

	/**
	 * 字符集
	 * 
	 * @return
	 */
	protected String getEncoding() {
		return encoding;
	}

	/**
	 * 获得反馈信息【案例】
	 * 
	 * @param conn
	 * @param info
	 * @return
	 * @throws JSONException
	 * @throws BusinessException
	 */
	protected String getResultInfo(HttpURLConnection conn, CallImplInfoVO info)
			throws BusinessException {
		String resultMsg = null;
		try {
			// 请求返回的状态
			String msg = null;
			if (conn.getResponseCode() == 200) {
				// 请求返回的数据
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				JSONObject result = JSONObject.parseObject(msg);
				String flag = (String) result.getString("success");
				if (!flag.equals("true")) {
					throw new BusinessException("【来自" + info.getDessystem()
							+ "的错误信息：" + result.getString("errorMessage") + "】");
				} else {
					resultMsg = result.getString("data");
				}
			} else {
				throw new BusinessException("连接失败");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	/**
	 * Md5加密
	 * 
	 * @param str
	 * @return
	 */
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
}
