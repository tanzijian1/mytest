package nc.bs.tg.outside.ebs.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISyncEBSServcie;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.outside.EBSResultVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@SuppressWarnings("restriction")
public class EBSDocServlet extends HttpServlet implements IHttpServletAdaptor {
	protected String encoding = "utf-8";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req, resp);
	}

	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initServlet(request, response);
		String json = readRequestBody(request);
		// String json = request.getParameter("json");
		// String json =
		// URLDecoder.decode(request.getParameter("json"),"Unicode");
		// String testjson = new String(json.getBytes("UTF-16"),"Unicode");
		byte[] sc = EBSBillUtils.getUtils().token("ISyncEBSServcie:" + json,
				"onSyncDoc_RequiresNew");
		EBSResultVO resultVO = new EBSResultVO();
		InvocationInfoProxy.getInstance().setUserDataSource(
				EBSBillUtils.DataSourceName);// ����ȫ������Դ
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcparm(json);
		logVO.setSrcsystem("EBS");
		logVO.setExedate(new UFDateTime().toString());
		logVO.setResult(EBSBillUtils.STATUS_SUCCESS);
		logVO.setOperator(EBSBillUtils.OperatorName);
		try {
			if (json == null) {
				throw new BusinessException("��Դ������ϢΪ��,��鿴��������!");
			}
			HashMap<String, Object> value;
			try {
				value = JSON.parseObject(json, HashMap.class);
			} catch (Exception e) {
				throw new BusinessException("��Ϣ�޷�ת����JSON��ʽ!");
			}
			String desdoc = (String) value.get("desdoc");

			if ("18".equals(desdoc)) {
				if (value.get("sysname") != null) {
					logVO.setDesbill((String) value.get("sysname") + "->"
							+ EBSCont.getDocNameMap().get(desdoc));
				} else {
					throw new BusinessException("��Դϵͳ���Ʋ���Ϊ��");
				}
			} else {
				logVO.setDesbill(EBSCont.getDocNameMap().get(desdoc));
			}
			String result = NCLocator.getInstance()
					.lookup(ISyncEBSServcie.class)
					.onSyncDoc_RequiresNew(value, desdoc);
			resultVO.setRsmstate(EBSBillUtils.STATUS_SUCCESS);
			HashMap<String, Object> resMap = JSON.parseObject(result,
					HashMap.class);
			resultVO.setMsg((String)resMap.get("msg"));
			resultVO.setData(resMap.get("data"));
			logVO.setErrmsg(result);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			resultVO.setRsmstate(SyncBPMBillStatesUtils.STATUS_FAILED);
			resultVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			logVO.setErrmsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
		} finally {
			EBSBillUtils.getUtils().restore(sc);
			try {
				EBSBillUtils.getUtils().getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		outputJson(response, resultVO);
	}

	private void initServlet(HttpServletRequest request,
			HttpServletResponse response) throws UnsupportedEncodingException {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.addHeader("Access-Control-Allow-Headers",
				"Origin, x-requested-with, Content-Type, Accept,X-Cookie");
		response.addHeader("Access-Control-Allow-Credentials", "true");
		response.addHeader("Access-Control-Allow-Methods",
				"GET,POST,PUT,OPTIONS,DELETE");
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=utf-8");

	}

	/**
	 * ���JSON
	 * 
	 * @param response
	 * @param data
	 */
	private void outputJson(HttpServletResponse response, EBSResultVO result) {
		response.setContentType("application/json; charset=" + encoding);
		response.setCharacterEncoding(encoding);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		String json = JSON.toJSONString(result);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(json);
		} catch (IOException e) {
			Logger.error("Encountered an error when outputing json!", e);
		} finally {
			if (out != null) {
				out.flush();
				out.close();
			}
		}
	}

	public String readRequestBody(HttpServletRequest request) {
		InputStream inputStream = null;
		StringBuffer requestJsonBuffer = null;
		try {
			inputStream = request.getInputStream();
			requestJsonBuffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"));
			String str = null;
			while ((str = reader.readLine()) != null) {
				requestJsonBuffer.append(str);
			}
		} catch (Exception e) {
			Logger.error("�ӿڶ�ȡ�����쳣��", e);
		} finally {
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null == requestJsonBuffer ? null : requestJsonBuffer.toString();
	}

}
