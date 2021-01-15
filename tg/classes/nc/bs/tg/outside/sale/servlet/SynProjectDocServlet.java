package nc.bs.tg.outside.sale.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.login.vo.ISystemIDConstants;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.projectbatchlog.ActionResult;
import nc.vo.tg.projectbatchlog.ProjectDocParam;
import nc.vo.tg.projectbatchlog.SynProjectDocParam;

@SuppressWarnings("restriction")
public class SynProjectDocServlet implements IHttpServletAdaptor {

	private static SecureRandom rand = new SecureRandom();

	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ActionResult result = new ActionResult();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		try {
			byte[] requstBytes = IOUtils.toByteArray(request.getInputStream());
			InputStreamReader requestReader = new InputStreamReader(
					new ByteArrayInputStream(requstBytes), "utf-8");
			SynProjectDocParam param = gson.fromJson(requestReader,
					SynProjectDocParam.class);
			beforeAction(param);

			Integer actioncode = param.getActioncode();
			if ((actioncode == null) || (actioncode == 0)) {
				String epscode = param.getEpscode();
				if (PubAppTool.isNull(epscode)) {
					throw new BusinessException("epscode不允许为空");
				}
				Logger.info("~~~ProjectDocSyn~~~，服务端接收到项目档案同步请求，epscode="
						+ epscode);
				ProjectDocParam[] projectDocParams = param.getData();
				if ((projectDocParams != null) && (projectDocParams.length > 0)) {
					// 保存VO
					IProjectDocSyn service = NCLocator.getInstance().lookup(
							IProjectDocSyn.class);
					service.synProjectDoc(epscode, projectDocParams);
				}
				result.setResultflag(0);
				result.setMessage("ok");
				result.setRsmstate(EBSBillUtils.STATUS_SUCCESS);
				result.setMsg("NC端接收数据成功");

				String resultStr = gson.toJson(result);
				response.setContentType("application/json; charset='utf-8'");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(resultStr);
			} else if (actioncode == 1) {
				String pk_projectbatchlog = param.getPk_projectbatchlog();
				if (PubAppTool.isNull(pk_projectbatchlog)) {
					throw new BusinessException("pk_projectbatchlog不允许为空");
				}
				Logger.info("~~~ProjectDocSyn~~~，服务端接收到项目档案同步请求，pk_projectbatchlog="
						+ pk_projectbatchlog);
				IProjectDocSyn service = NCLocator.getInstance().lookup(
						IProjectDocSyn.class);
				service.synProjectDocByPk_projectbatchlog(pk_projectbatchlog);

				result.setResultflag(0);
				result.setMessage("ok");
				result.setRsmstate(EBSBillUtils.STATUS_SUCCESS);
				result.setMsg("NC端接收数据成功");

				String resultStr = gson.toJson(result);
				response.setContentType("application/json; charset='utf-8'");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(resultStr);
			} else {
				throw new BusinessException("非法的功能号。");
			}
		} catch (Exception e) {
			result.setResultflag(1);
			result.setMessage(e.getMessage());
			result.setRsmstate(EBSBillUtils.STATUS_FAILED);
			result.setMsg(e.getMessage());
			Logger.error("~~~ProjectDocSyn~~~，" + e.getMessage(), e);

			String resultStr = gson.toJson(result);
			response.setContentType("application/json; charset='utf-8'");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resultStr);
		}
	}

	private void beforeAction(SynProjectDocParam param) throws Exception {
		InvocationInfoProxy.getInstance().setUserDataSource(
				IProjectDocSyn.NC_DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(IProjectDocSyn.SaleUserId);
		InvocationInfoProxy.getInstance().setUserCode(
				IProjectDocSyn.SaleOperatorName);
		// 注册认证结果
		ISecurityTokenCallback sc = NCLocator.getInstance().lookup(
				ISecurityTokenCallback.class);
		byte[] bytes = new byte[64];
		rand.nextBytes(bytes);
		sc.token(
				(ISystemIDConstants.NCSYSTEM + ":" + IProjectDocSyn.SaleUserId)
						.getBytes("UTF-8"), bytes);
		InvocationInfoProxy.getInstance().setGroupId(IProjectDocSyn.GroupId);
	}

}
