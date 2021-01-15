package nc.bs.tg.outside.word.servlet;

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

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.bs.tg.outside.word.utils.WordBillUtils;
import nc.itf.tg.outside.ISyncWordBillServcie;
import nc.itf.tg.outside.WordBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.outside.SaleResultVO;

import com.alibaba.fastjson.JSON;

@SuppressWarnings("restriction")
public class WordBillServlet extends HttpServlet implements IHttpServletAdaptor {
	protected String encoding = "utf-8";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doAction(req, resp);
	}

	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		initServlet(request, response);
		byte[] sc = null;
		InvocationInfoProxy.getInstance().setUserDataSource(
				SyncBPMBillStatesUtils.DataSourceName);// 设置全局数据源
		OutsideLogVO logVO = new OutsideLogVO();
		SaleResultVO resultVO = new SaleResultVO();
		try {
			String josn = readRequestBody(request);
			logVO.setSrcparm(josn);
			logVO.setSrcsystem("WORD");
			logVO.setExedate(new UFDateTime().toString());
			logVO.setResult(SaleBillUtils.STATUS_SUCCESS);
			logVO.setOperator(WordBillUtils.OperatorName);

			if (josn == null) {
				throw new BusinessException("来源参数信息为空,请查看传参设置!");
			}

			sc = SaleBillUtils.getUtils().token("ISyncWordBillServcie:" + josn,
					"onSyncBill_RequiresNew");
			HashMap<String, Object> value;
			try {
				value = JSON.parseObject(josn, HashMap.class);
			} catch (Exception e) {
				throw new BusinessException("信息无法转换成JSON格式!");
			}
			String billytpe = (String) value.get("srctype");
			logVO.setDesbill(WordBillCont.getBillNameMap().get(billytpe));
			String result = NCLocator.getInstance()
					.lookup(ISyncWordBillServcie.class)
					.onSyncBill_RequiresNew(value, billytpe);
			resultVO.setRsmstate(SaleBillUtils.STATUS_SUCCESS);
			resultVO.setData(result);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			resultVO.setRsmstate(SyncBPMBillStatesUtils.STATUS_FAILED);
			resultVO.setMsg(e.getMessage());
			logVO.setResult(SyncBPMBillStatesUtils.STATUS_FAILED);
			logVO.setErrmsg(e.getMessage());
		} finally {
			SaleBillUtils.getUtils().restore(sc);
			try {
				SaleBillUtils.getUtils().getBaseDAO().insertVO(logVO);
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

	public String readRequestBody(HttpServletRequest request) throws Exception {
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

	/**
	 * 输出JSON
	 * 
	 * @param response
	 * @param data
	 */
	private void outputJson(HttpServletResponse response, SaleResultVO result) {
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

}
