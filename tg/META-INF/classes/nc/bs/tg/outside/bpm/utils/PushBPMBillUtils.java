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
		// TODO 自动生成的方法存根

		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=PostTask&UserAccount="
				+ usercode;

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
		if (StringUtils.isNotBlank(easy) && "简化业财模式".equals(easy)) {
			postdata.put("ProposalProcess", easy);
		}
		postdata.put("ProcessName", processname);
		postdata.put("Action", "提交");
		postdata.put("Comment", "签核意见");
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
					throw new BusinessException("【来自BPM的错误信息："
							+ result.get("errorMessage") + "】【NC传BPM报文：" + json
							+ "】");
				} else {
					throw new BusinessException("【来自BPM的错误信息：" + errmsg
							+ "】【NC传BPM报文：" + json + "】");
				}
			}
		} else {
			Logger.error("连接失败");
			throw new BusinessException("连接失败【NC传BPM报文：" + json + "】");
		}
	}
}
