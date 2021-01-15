package nc.bs.tg.outside.ebs.pub;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.alter.plugin.ebs.Httpconnectionutil;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.bs.tg.outside.utils.PropertiesUtil;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.outside.OutsideUtils;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.NcToEbsLogVO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

public class PushSrmDataUtils extends EBSBillUtils {
	static PushSrmDataUtils utils;

	public static PushSrmDataUtils getUtils() {
		if (utils == null) {
			utils = new PushSrmDataUtils();
		}
		return utils;
	}

	public Map<String, String> pushBillToSRM(String code, String syscode,
			String token, Object postdata, String registryName)
			throws Exception {
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String flag = null;

		// String json = JSON.toJSONString(postdata);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setTaskname(registryName);
		logVO.setNcparm(json);
		logVO.setSrcsystem("EBS");
		logVO.setOperator("NC");
		logVO.setExedate(new UFDateTime().toString());
		String params = /*
						 * "code=" + code + "&syscode=" + syscode + "&token=" +
						 * token + "&req=" +
						 */URLEncoder.encode(json, "utf-8");

		String address  = OutsideUtils.getOutsideInfo("EBS0301");
		String urls = address;

		Logger.error("----------请求开始---------------");
		Logger.error("-----url:" + urls + "-----请求参数：【" + json
				+ "】---------------");

		String receive = null;
		// 创建post方式请求对象
		PostMethod method = new PostMethod(address);
		try {
			// 创建httpclient对象
			HttpClient client = new HttpClient();

			// 设置参数到请求对象中
			method.setRequestHeader("Content-type",
					"application/json; charset=UTF-8");
			method.setRequestHeader("Accept", "application/json; charset=UTF-8");
			method.setRequestBody("&params=" + json);
			int rspCode = client.executeMethod(method);
			receive = method.getResponseBodyAsString();

			if (200 == rspCode) {
				JSONObject jobj = JSON.parseObject(receive);
				flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				logVO.setTaskname("SRM付款完成");
				logVO.setSrcsystem("SRM");
				logVO.setOperator("NC");
				logVO.setExedate(new UFDateTime().toString());
				logVO.setDr(0);

				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					throw new BusinessException("EBS反馈异常:" + msg);
				}
			}
		} catch (Exception e) {
			logVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			method.releaseConnection();

			saveLog.SaveLog_RequiresNew(logVO);

		}
		Gson gson = new Gson();
		
		Map<String, String> map = new HashMap<String, String>();
		
		map = gson.fromJson(receive, map.getClass());
		
		return map;
	}

	public Map<String, String> pushInvoicelToSRM(String code, String syscode,
			String token, Object postdata, String registryName) throws Exception{
		
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog saveLog = NCLocator.getInstance().lookup(ISaveLog.class);
		String flag = null;
		
		// String json = JSON.toJSONString(postdata);
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(postdata);
		logVO.setTaskname(registryName);
		logVO.setNcparm(json);
		logVO.setSrcsystem("EBS");
		logVO.setOperator("NC");
		logVO.setExedate(new UFDateTime().toString());
		String params = /*
						 * "code=" + code + "&syscode=" + syscode + "&token=" +
						 * token + "&req=" +
						 */URLEncoder.encode(json, "utf-8");

		String address = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("SRMPAYURL");
		syscode = PropertiesUtil.getInstance("srm_url.properties")
				.readValue("SRMSYSTEM");
		String urls = address+"/postMethod?code=" + code + "&syscode=" + syscode;

		Logger.error("----------请求开始---------------");
		Logger.error("-----url:" + urls + "-----请求参数：【" + json
				+ "】---------------");

		String receive = null;
		// 创建post方式请求对象
		String returnjson = null;
		try {
			returnjson = Httpconnectionutil.newinstance().connection(urls,
					token + "&req=" + json);
			if (returnjson != null) {
				JSONObject jobj = JSON.parseObject(returnjson);
				flag = jobj.getString("code");
				String msg = jobj.getString("msg");
				
				logVO.setExedate(new UFDateTime().toString());
				if ("S".equals(flag)) {
					logVO.setResult(flag);
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
				} else {
					logVO.setMsg(msg);
					logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
					throw new BusinessException(msg);
				}
			}
		} catch (Exception e) {
			logVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			throw new BusinessException(e.getMessage(), e);
		} finally {
			saveLog.SaveLog_RequiresNew(logVO);

		}
		Gson gson = new Gson();
		
		Map<String, String> map = new HashMap<String, String>();
		
		map = gson.fromJson(receive, map.getClass());
		
		return map;
	}
}
