package nc.bs.tg.outside.salebpm.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.salebpm.push.QueryDocInfoUtils;
import nc.itf.tg.outside.IPushBPMBillFileService;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SalePushBPMBillUtils extends SaleBPMBillUtils {
	static SalePushBPMBillUtils utils;

	public static SalePushBPMBillUtils getUtils() {
		if (utils == null) {
			utils = new SalePushBPMBillUtils();
		}
		return utils;
	}
	
	public Map<String, String> pushBillToBpm(String usercode,
			Map<String, Object> formData, String processname, String deptid,
			String billid, String taskid) throws Exception {
		// TODO �Զ����ɵķ������

		OutsideLogVO logVO = new OutsideLogVO();
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=PostTaskForEBS&UserAccount="
				+ usercode;
		if(processname!=null&&processname.contains("@@@")){//�ѿص�ַ2020-10-14
			processname=processname.split("@@@")[0];
			urls=address+"/YZSoft/WebService/YZService.ashx?Method=PostTaskNew&UserAccount="
					+usercode;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> postdata = new HashMap<String, Object>();// ���屨��
			
			if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_011)){// �ڲ��ʽ������
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_011);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
				if (billid != null && !"".equals(billid)) {
					postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
							.getFiles(billid));
				}
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_012)){// ˰�ѽ������뵥
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_012);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
				if (billid != null && !"".equals(billid)) {
					postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
							.getFiles(billid));
				}
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_016)||processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_017)){// ͳ��ͳ��-�ڲ��ſ or // ͳ��ͳ��-�ڲ���� 
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_016);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
				if (billid != null && !"".equals(billid)) {
					postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
							.getFiles(billid));
				}
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_027)){// ���������ѵ�
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_027);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
				if (billid != null && !"".equals(billid)) {
					postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
							.getFiles(billid));
				}
			}else if(processname.equals(ISaleBPMBillCont.BILLNAME_19)){//��Ʊ��
				postdata.put("ProcessName", processname);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
//			if (billid != null && !"".equals(billid)) {
//				postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
//						.getFiles(billid));
//			}
			}else{
				postdata.put("ProcessName", processname);
				postdata.put("Action", "�ύ");
				postdata.put("Comment", "ǩ�����");
				postdata.put("Draft", "false");
				postdata.put("FormData", formData);
				postdata.put("DeptID", deptid == null ? "" : deptid);
				if (taskid != null && !"".equals(taskid) && !"~".equals(taskid)) {
					postdata.put("ExistTaskID", taskid);
				}else{
					postdata.put("ExistTaskID", "");
				}
				if (billid != null && !"".equals(billid)) {
					postdata.put("AttachmentInfo", QueryDocInfoUtils.getUtils()
							.getFiles(billid));
				}
			}
			
			logVO.setSrcsystem("BPM");
			logVO.setDesbill(processname);
			logVO.setResult(SyncSaleBPMBillStatesUtils.STATUS_SUCCESS);
			logVO.setExedate(new UFDateTime().toString());
			logVO.setOperator(usercode);
			logVO.setPrimaryKey(billid);
			String json = objectMapper.writeValueAsString(postdata);
			logVO.setSrcparm(json);
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
				logVO.setErrmsg(String.valueOf(result));
				// String openUrl = null;
				if (flag.equals("true")) {
					Map<String, String> resultInfo = new HashMap<String, String>();
					resultInfo.put("taskID", (String) result.getString("TaskID"));
					resultInfo.put("OpenUrl", (String) result.getString("OpenUrl"));
					resultInfo.put("success", result.getString("success"));
					if(!result.isNull("ApprovalUrl")){
						resultInfo.put("ApprovalUrl", result.getString("ApprovalUrl"));
					}
//					resultInfo.put("status", (String) result.getString("OpenUrl"));
					// Desktop.getDesktop().browse(new URI(openUrl));
					return resultInfo;
				} else {
					if(result.get("errorMessage")!=null&&!"null".equals(result.get("errorMessage"))){
						throw new BusinessException("������BPM�Ĵ�����Ϣ��" + result.get("errorMessage") + "��");
					}else{
						throw new BusinessException("������BPM�Ĵ�����Ϣ��" + errmsg + "��");
					}
				}
			} else {
				Logger.error("����ʧ��");
				throw new BusinessException("����ʧ�ܡ�NC��BPM���ģ�" + json + "��");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			throw new Exception(e.getMessage());
		} finally{
			IPushBPMBillFileService service = 
					NCLocator.getInstance().lookup(IPushBPMBillFileService.class);
			service.saveLog_RequiresNew(logVO);
		}
	}
}
