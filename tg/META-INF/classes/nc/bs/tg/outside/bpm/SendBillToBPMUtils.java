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

		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		if (bussinessId != null && !"".equals(bussinessId)) {
			postdata.put("BussinessId", postdata);// EBSϵͳ����ID
		}

		if (command != null && !"".equals(command)) {
			postdata.put("Command", command);// �˵����
		}

		String json = objectMapper.writeValueAsString(postdata);
		Logger.error("----------����ʼ---------------");
		Logger.error("----------�����������" + json + "��---------------");
		// ����url��Դ
		URL url = new URL(urls);
		// ����http����
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// �����������
		conn.setDoOutput(true);

		conn.setDoInput(true);
		// ���ò��û���
		conn.setUseCaches(false);
		// ���ô��ݷ�ʽ
		conn.setRequestMethod("POST");
		// ����ά�ֳ�����
		conn.setRequestProperty("Connection", "Keep-Alive");
		// �����ļ��ַ���:
		conn.setRequestProperty("Charset", "utf-8");
		// ת��Ϊ�ֽ�����
		byte[] data = json.getBytes("utf-8");
		// �����ļ�����
		conn.setRequestProperty("Content-Length", String.valueOf(data.length));

		// �����ļ�����:
		conn.setRequestProperty("Content-Type", "application/json");
		// ������Կ
		conn.setRequestProperty("Authorization", app_key_tickening);
		// ��ʼ��������
		conn.connect();
		OutputStream out = conn.getOutputStream();
		// д��������ַ���
		out.write(data);
		out.flush();
		out.close();
		String a = "";
		// ���󷵻ص�״̬
		if (conn.getResponseCode() == 200) {
			Logger.error("���ӳɹ�");
			// ���󷵻ص�����
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
				// �ɹ�
			} else {
				errmsg = (String) result.getString("errorMessage");
				throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
			}
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ��");
		}
	}
}
