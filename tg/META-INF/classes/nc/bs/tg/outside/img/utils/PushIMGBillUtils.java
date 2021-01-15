package nc.bs.tg.outside.img.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISqlThread;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PushIMGBillUtils extends IMGBillUtils {
	static PushIMGBillUtils utils;

	public static PushIMGBillUtils getUtils() {
		if (utils == null) {
			utils = new PushIMGBillUtils();
		}
		return utils;
	}

	public void pushBillToImg(Map<String, Object> formData,
			Map<String, Object> formBody, Boolean isLLBill) throws Exception {
		// TODO �Զ����ɵķ������
		OutsideLogVO logVO = new OutsideLogVO();
		StringBuffer srcparm = new StringBuffer();
		logVO.setSrcsystem("IMG");
		logVO.setDesbill("Ӱ��ȡ��Ʊ����");
		try {
			PropertiesUtil util = PropertiesUtil
					.getInstance("image_url.properties");
			String urls = util.readValue("imgInvUrl");

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			postdata.put("safety", formData);
			postdata.put("serverbody", formBody);
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setSrcparm(json);
			srcparm.append("NC���ñ��ģ�" + json);
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
			conn.setRequestProperty("Content-Length",
					String.valueOf(data.length));

			// �����ļ�����:
			conn.setRequestProperty("Content-Type", "application/json");

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
				srcparm.append("\n\rӰ��ش����ģ�" + a);
				logVO.setSrcparm(srcparm == null ? "" : srcparm.toString());
				String flag = (String) result.getString("result");
				String errmsg = null;
				// String openUrl = null;
				if (flag.equals("0")) {
					org.json.JSONArray jarr = result.getJSONArray("items");
					Logger.error("huangxj���룺insertImgInvToInvoiceVO");
					insertImgInvToInvoiceVO(jarr,isLLBill);
					logVO.setResult(EBSBillUtils.STATUS_SUCCESS);
					// Desktop.getDesktop().browse(new URI(openUrl));
				} else {
					errmsg = (String) result.getString("errormsg");
					throw new BusinessException("������Ӱ��Ʊ�Ĵ�����Ϣ��" + errmsg
							+ "��,��NC���ñ���" + json + "��");
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ��" + json);
			}
		} catch (Exception e) {
			Logger.error("����Ӱ��:" + e.getMessage());
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(EBSBillUtils.STATUS_FAILED);
			logVO.setOperator(EBSBillUtils.OperatorName);
			logVO.setErrmsg("����Ӱ��:" + e.getMessage());
			throw new BusinessException("����ʧ��" + e.getMessage(), e);
		} finally {
			try {
				ISqlThread thread = NCLocator.getInstance().lookup(
						ISqlThread.class);
				thread.billInsert_RequiresNew(logVO);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
