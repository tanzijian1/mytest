package nc.bs.tg.outside.bpm.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.ISyncBPMBillServcie;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.BPMBillStateParaVO;
import nc.vo.tg.outside.BPMBillStateResultVO;
import nc.vo.tg.outside.OutsideLogVO;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("restriction")
public class BPMBillStatesServlet extends HttpServlet implements
		IHttpServletAdaptor {
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
		initBPMServlet(request, response);

		Logger.debug("##BPMBillStatesServlet.doAction() called");
//		String josn = request.getParameter("json");
		String josn = readRequestBody(request);
		byte[] sc = SyncBPMBillStatesUtils.getUtils().token(
				"ISyncBPMBillServcie:" + josn, "onSyncBillState_RequiresNew");
		BPMBillStateResultVO resultVO = new BPMBillStateResultVO();
		InvocationInfoProxy.getInstance().setUserDataSource(
				SyncBPMBillStatesUtils.DataSourceName);// 设置全局数据源
		OutsideLogVO logVO = new OutsideLogVO();
		logVO.setSrcparm(josn);
		logVO.setSrcsystem("BPM");
		logVO.setExedate(new UFDateTime().toString());
		try {
			if (josn == null) {
				throw new BusinessException("来源参数信息为空,请查看传参设置!");
			}

			BPMBillStateParaVO value = JSON.parseObject(josn,
					BPMBillStateParaVO.class);
			logVO.setDesbill(IBPMBillCont.getBillNameMap()
					.get(value.getBilltypeName()));
			logVO.setPrimaryKey(value.getBpmid());
			logVO.setOperator(value.getOperator() == null
					|| "".equals(value.getOperator()) ? SyncBPMBillStatesUtils.BPMOperatorName
					: value.getOperator());
			String result = NCLocator.getInstance()
					.lookup(ISyncBPMBillServcie.class)
					.onSyncBillState_RequiresNew(value);
			resultVO.setRsmstate(SyncBPMBillStatesUtils.STATUS_SUCCESS);
			resultVO.setMsg(result);
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_SUCCESS);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			resultVO.setRsmstate(SyncBPMBillStatesUtils.STATUS_FAILED);
			resultVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			logVO.setErrmsg(e.getMessage());
		} finally {
			SyncBPMBillStatesUtils.getUtils().restore(sc);
			try {
				SyncBPMBillStatesUtils.getUtils().getBaseDAO().insertVO(logVO);
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
			}
		}
		outputJson(response, resultVO);
	}

	private void initBPMServlet(HttpServletRequest request,
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
	 * 输出JSON
	 * 
	 * @param response
	 * @param data
	 */
	private void outputJson(HttpServletResponse response,
			BPMBillStateResultVO result) {
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
			Logger.error("接口读取内容异常：", e);
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
