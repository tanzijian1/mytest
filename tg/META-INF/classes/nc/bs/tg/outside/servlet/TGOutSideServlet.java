package nc.bs.tg.outside.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.tg.outside.ISyncTGInfoServcie;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OSImplLogVO;
import nc.vo.tg.outside.ResultVO;

import com.alibaba.fastjson.JSON;

public class TGOutSideServlet extends HttpServlet implements
		IHttpServletAdaptor {
	protected String encoding = "UTF-8";

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
		OSImplLogVO logVO = new OSImplLogVO();
		ResultVO resultVO = new ResultVO();
		byte[] sc = null;
		String json = null;
		try {
			String srcsystem = request.getHeader("srcsys");
			String method = request.getHeader("methodname");
			String token = request.getHeader("authorization");
			json = readRequestBody(request);
			sc = BillUtils.getUtils().token("ISyncTGInfoServcie:" + json,
					"onSyncInfo_RequiresNew");
			InvocationInfoProxy.getInstance().setUserDataSource(
					BillUtils.DataSourceName);// 设置全局数据源
			logVO.setSrcparm(json);
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(BillUtils.STATUS_SUCCESS);
			logVO.setSrcsystem(srcsystem);
			logVO.setDessystem("NC");
			logVO.setMethod(method);
			logVO.setDr(new Integer(0));
			if (json == null) {
				throw new BusinessException("来源参数信息为空,请查看传参设置!");
			}

			if (!BillUtils.getUtils().verifyToken(token, srcsystem)) {
				throw new BusinessException("密钥检验未通过,请联系系统管理员");
			}
			HashMap<String, Object> info;
			try {
				info = JSON.parseObject(json, HashMap.class);
			} catch (Exception e) {
				throw new BusinessException("信息无法转换成JSON格式!");
			}
			String result = NCLocator.getInstance()
					.lookup(ISyncTGInfoServcie.class)
					.onSyncInfo_RequiresNew(srcsystem, method, info);
			resultVO.setRsmstate(BillUtils.STATUS_SUCCESS);
			resultVO.setData(result);
			logVO.setMsg(result);
		} catch (Exception e) {
			logVO.setMsg(org.apache.commons.lang.exception.ExceptionUtils
					.getFullStackTrace(e));
			Logger.error(e.getMessage(), e);
			String msg = e.getMessage();
			if (StringUtils.isNotBlank(msg) && msg.length() > 800) {
				msg = msg.substring(0, 800);
			}
			resultVO.setMsg(msg);
			resultVO.setRsmstate(BillUtils.STATUS_FAILED);
			logVO.setResult(BillUtils.STATUS_FAILED);
		} finally {
			try {
				BillUtils.getUtils().getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
			BillUtils.getUtils().restore(sc);
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
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		response.setContentType("text/html;charset=" + encoding);

	}

	/**
	 * 输出JSON
	 * 
	 * @param response
	 * @param data
	 */
	private void outputJson(HttpServletResponse response, ResultVO result) {
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

	/**
	 * 读取内容
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public String readRequestBody(HttpServletRequest request) throws Exception {
		InputStream inputStream = null;
		StringBuffer requestJsonBuffer = null;
		try {
			inputStream = request.getInputStream();
			requestJsonBuffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, encoding));
			String str = null;
			while ((str = reader.readLine()) != null) {
				requestJsonBuffer.append(str);
			}
		} catch (Exception e) {
			Logger.error("接口读取内容异常：", e);
			throw new Exception(e.getMessage());
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
