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
 * ����ӿڿͻ��˽ӿ�, ��֧��Http+JSON/XML
 * 
 * @author ASUS
 * 
 */
public abstract class TGImplCallHttpClient implements ITGCallService {
	private String encoding = "utf-8";

	public String onCallMethod(CallImplInfoVO info) throws BusinessException {
		String result = null;
		try {
			// ����url��Դ
			URL url = new URL(info.getUrls());
			// ����http����
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(60000);
			conn.setReadTimeout(60000);
			// ��ʼ��������Ϣ
			initConnParameter(conn, info);
			// ��ʼ��������
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
	 * ҵ����
	 * 
	 * @param info
	 *            Nc�ڲ��������
	 * @param result
	 *            ���ýӿڷ�������
	 * @return
	 */
	protected abstract String onBusinessProcessing(CallImplInfoVO info,
			String result);

	/**
	 * ����������д���ⲿϵͳBODY
	 * 
	 * @param conn
	 * @param postdata
	 * @throws IOException
	 */
	protected void onWriteInfo(HttpURLConnection conn, String postdata)
			throws IOException {
		byte[] data = postdata.getBytes(getEncoding());
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
	}

	/**
	 * ���ò�������
	 * 
	 * @param conn
	 * @param info
	 * @throws ProtocolException
	 * @throws IOException
	 */
	protected void initConnParameter(HttpURLConnection conn, CallImplInfoVO info)
			throws ProtocolException, IOException {
		// �����������
		conn.setDoOutput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", getEncoding());
		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");
		// ת��Ϊ�ֽ�����
		byte[] data = info.getPostdata().getBytes(getEncoding());
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));
	}

	/**
	 * �ַ���
	 * 
	 * @return
	 */
	protected String getEncoding() {
		return encoding;
	}

	/**
	 * ��÷�����Ϣ��������
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
			// ���󷵻ص�״̬
			String msg = null;
			if (conn.getResponseCode() == 200) {
				// ���󷵻ص�����
				BufferedReader in = new BufferedReader(new InputStreamReader(
						conn.getInputStream(), getEncoding()));
				String line = null;
				while ((line = in.readLine()) != null) {
					msg += line;
				}
				JSONObject result = JSONObject.parseObject(msg);
				String flag = (String) result.getString("success");
				if (!flag.equals("true")) {
					throw new BusinessException("������" + info.getDessystem()
							+ "�Ĵ�����Ϣ��" + result.getString("errorMessage") + "��");
				} else {
					resultMsg = result.getString("data");
				}
			} else {
				throw new BusinessException("����ʧ��");
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return resultMsg;
	}

	/**
	 * Md5����
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
