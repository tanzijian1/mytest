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
		// TODO 自动生成的方法存根

		OutsideLogVO logVO = new OutsideLogVO();
		String address = OutsideUtils.getOutsideInfo("BPM");
		String urls = address
				+ "/YZSoft/WebService/YZService.ashx?Method=PostTaskForEBS&UserAccount="
				+ usercode;
		if(processname!=null&&processname.contains("@@@")){//费控地址2020-10-14
			processname=processname.split("@@@")[0];
			urls=address+"/YZSoft/WebService/YZService.ashx?Method=PostTaskNew&UserAccount="
					+usercode;
		}
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> postdata = new HashMap<String, Object>();// 整体报文
			
			if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_011)){// 内部资金调拨单
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_011);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_012)){// 税费缴纳申请单
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_012);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_016)||processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_017)){// 统借统还-内部放款单 or // 统借统还-内部还款单 
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_016);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
			}else if(processname.equals(ISaleBPMBillCont.PROCESSNAME_F3_Cxx_027)){// 银行手续费单
				postdata.put("ProcessName", ISaleBPMBillCont.PROCESSNAMETOBPM_F3_Cxx_027);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
			}else if(processname.equals(ISaleBPMBillCont.BILLNAME_19)){//补票单
				postdata.put("ProcessName", processname);
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
				postdata.put("Action", "提交");
				postdata.put("Comment", "签核意见");
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
						throw new BusinessException("【来自BPM的错误信息：" + result.get("errorMessage") + "】");
					}else{
						throw new BusinessException("【来自BPM的错误信息：" + errmsg + "】");
					}
				}
			} else {
				Logger.error("连接失败");
				throw new BusinessException("连接失败【NC传BPM报文：" + json + "】");
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
