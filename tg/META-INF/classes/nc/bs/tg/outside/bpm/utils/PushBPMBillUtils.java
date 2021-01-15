package nc.bs.tg.outside.bpm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.push.QueryDocInfoUtils;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class PushBPMBillUtils extends BPMBillUtils {
	static PushBPMBillUtils utils;

	public static PushBPMBillUtils getUtils() {
		if (utils == null) {
			utils = new PushBPMBillUtils();
		}
		return utils;
	}

	public Map<String, String> pushBillToBpm(String usercode,
			Map<String, Object> formData, String processname, String deptid,
			String billid, String taskid) throws Exception {
		return pushBillToBpm(usercode, formData, processname, deptid, billid,
				taskid, null);
	}

	public Map<String, String> pushBillToBpm(String usercode,
			Map<String, Object> formData, String processname, String deptid,
			String billid, String taskid, String easy) throws Exception {
		// TODO �Զ����ɵķ������

		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=PostTask&UserAccount="
				+ usercode;

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
		if (StringUtils.isNotBlank(easy) && "��ҵ��ģʽ".equals(easy)) {
			postdata.put("ProposalProcess", easy);
		}
		postdata.put("ProcessName", processname);
		postdata.put("Action", "�ύ");
		postdata.put("Comment", "ǩ�����");
		postdata.put("Draft", "false");
		postdata.put("FormData", formData);
		postdata.put("DeptID", deptid == null ? "" : deptid);
		if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
			postdata.put("ExistTaskID", taskid);
		} else {
			postdata.put("ExistTaskID", "");
		}
		if (billid != null && !"".equals(billid)) {
			postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
					.getFiles(billid));
		}
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcsystem("BPM");
		logVO.setDesbill(processname);
		logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
		logVO.setExedate(new UFDateTime().toString());
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setSrcparm(json);
		getBaseDAO().insertVO(logVO);

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
			// String openUrl = null;
			if (flag.equals("true")) {
				Map<String, String> resultInfo = new HashMap<String, String>();
				resultInfo.put("taskID", (String) result.getString("TaskID"));
				resultInfo.put("openurl", (String) result.getString("OpenUrl"));
				// Desktop.getDesktop().browse(new URI(openUrl));
				return resultInfo;
			} else {
				if (result.get("errorMessage") != null
						&& !"null".equals(result.get("errorMessage"))) {
					throw new BusinessException("������BPM�Ĵ�����Ϣ��"
							+ result.get("errorMessage") + "����NC��BPM���ģ�" + json
							+ "��");
				} else {
					throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg
							+ "����NC��BPM���ģ�" + json + "��");
				}
			}
		} else {
			Logger.error("����ʧ��");
			throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
		}
	}
}
