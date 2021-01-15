package nc.bs.tg.outside.business.servlet;

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
import nc.bs.tg.outside.business.utils.BusinessBillUtils;
import nc.bs.tg.outside.sale.utils.SaleBillUtils;
import nc.itf.tg.outside.ISyncBusinessBillService;
import nc.vo.pub.BusinessException;
import uap.json.JSONObject;

@SuppressWarnings("restriction")
public class BusinessBillServlet extends HttpServlet implements
		IHttpServletAdaptor {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
				BusinessBillUtils.DataSourceName);// 设置全局数据源
		String result = "";
		try {
			String josn = readRequestBody(request);

			if (josn == null) {
				throw new BusinessException("来源参数信息为空,请查看传参设置!");
			}

			sc = SaleBillUtils.getUtils().token("ISyncInvBillServcie:" + josn,
					"onSyncBill_RequiresNew");
			// HashMap<String, Object> value;
			JSONObject jsonObj = new JSONObject((String) josn);
			if (jsonObj == null) {
				throw new BusinessException("json格式转换失败,请检查!");
			}
			// try {
			//
			// // value = JSON.parseObject(josn, HashMap.class);
			// } catch (Exception e) {
			// throw new BusinessException("信息无法转换成JSON格式!");
			// }
			String billytpe = (String) jsonObj.get("srctype");
			result = NCLocator.getInstance()
					.lookup(ISyncBusinessBillService.class)
					.onSyncBill_RequiresNew(jsonObj, billytpe);
			// resultVO.setRsmstate(BusinessBillUtils.STATUS_SUCCESS);
			// resultVO.setData(result);
		} catch (Exception e) {
			Logger.error(e.getMessage(), e);
			// resultVO.setRsmstate(BusinessBillUtils.STATUS_FAILED);
			// resultVO.setMsg(e.getMessage());
			if (result == null || "".equals(result)) {
				result = e.getMessage();
			}
		} finally {
			SaleBillUtils.getUtils().restore(sc);
		}
		outputJson(response, result);
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
	private void outputJson(HttpServletResponse response, String result) {
		response.setContentType("application/json; charset=" + encoding);
		response.setCharacterEncoding(encoding);
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		// String json = JSON.toJSONString(result);
		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.print(result);
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
