package nc.bs.tg.outside.sale.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.adaptor.IHttpServletAdaptor;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.utils.EBSBillUtils;
import nc.itf.tg.ISaveLog;
import nc.itf.tg.ISqlThread;
import nc.itf.tg.outside.IProjectDocSyn;
import nc.itf.tg.outside.ISaveLogService;
import nc.login.vo.ISystemIDConstants;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.pub.PubAppTool;
import nc.vo.tg.outside.NcToEbsLogVO;
import nc.vo.tg.outside.OutsideLogVO;
import nc.vo.tg.projectbatchlog.ActionResult;
import nc.vo.tg.projectbatchlog.ProjectDocParam;
import nc.vo.tg.projectbatchlog.SynProjectDocParam;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("restriction")
public class SynProjectDocServlet implements IHttpServletAdaptor {

	private static SecureRandom rand = new SecureRandom();

	@Override
	public void doAction(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ActionResult result = new ActionResult();
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.create();
		byte[] requstBytes = IOUtils.toByteArray(request.getInputStream());
		InputStreamReader requestReader = new InputStreamReader(
				new ByteArrayInputStream(requstBytes), "utf-8");
		
		NcToEbsLogVO logVO = new NcToEbsLogVO();
		ISaveLog save=NCLocator.getInstance().lookup(ISaveLog.class);
		try{
		logVO.setTaskname("�����");
		logVO.setResult("1");
		logVO.setNcparm(new String(requstBytes, "utf-8"));
		logVO.setSrcsystem("SALE");
		}catch(Exception e){
			e.printStackTrace();
		}
		try {
			
			SynProjectDocParam param = gson.fromJson(requestReader,
					SynProjectDocParam.class);
			beforeAction(param);

			Integer actioncode = param.getActioncode();
			if ((actioncode == null) || (actioncode == 0)) {
				String epscode = param.getEpscode();
				if (PubAppTool.isNull(epscode)) {
					throw new BusinessException("epscode������Ϊ��");
				}
				Logger.info("~~~ProjectDocSyn~~~������˽��յ���Ŀ����ͬ������epscode="
						+ epscode);
				ProjectDocParam[] projectDocParams = param.getData();
				if ((projectDocParams != null) && (projectDocParams.length > 0)) {
					// ����VO
					IProjectDocSyn service = NCLocator.getInstance().lookup(
							IProjectDocSyn.class);
					service.synProjectDoc(epscode, projectDocParams);
				}
				result.setResultflag(0);
				result.setMessage("ok");
				result.setRsmstate(EBSBillUtils.STATUS_SUCCESS);
				result.setMsg("NC�˽������ݳɹ�");

				String resultStr = gson.toJson(result);
				response.setContentType("application/json; charset='utf-8'");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(resultStr);
			} else if (actioncode == 1) {
				String pk_projectbatchlog = param.getPk_projectbatchlog();
				if (PubAppTool.isNull(pk_projectbatchlog)) {
					throw new BusinessException("pk_projectbatchlog������Ϊ��");
				}
				Logger.info("~~~ProjectDocSyn~~~������˽��յ���Ŀ����ͬ������pk_projectbatchlog="
						+ pk_projectbatchlog);
				IProjectDocSyn service = NCLocator.getInstance().lookup(
						IProjectDocSyn.class);
				service.synProjectDocByPk_projectbatchlog(pk_projectbatchlog);

				result.setResultflag(0);
				result.setMessage("ok");
				result.setRsmstate(EBSBillUtils.STATUS_SUCCESS);
				result.setMsg("NC�˽������ݳɹ�");

				String resultStr = gson.toJson(result);
				response.setContentType("application/json; charset='utf-8'");
				response.setCharacterEncoding("utf-8");
				response.getWriter().write(resultStr);
			} else {
				throw new BusinessException("�Ƿ��Ĺ��ܺš�");
			}
		} catch (Exception e) {
			result.setResultflag(1);
			result.setMessage(e.getMessage());
			result.setRsmstate(EBSBillUtils.STATUS_FAILED);
			result.setMsg(e.getMessage());
			Logger.error("~~~ProjectDocSyn~~~��" + e.getMessage(), e);
			logVO.setMsg(e.getMessage());
			logVO.setResult("0");
			String resultStr = gson.toJson(result);
			response.setContentType("application/json; charset='utf-8'");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(resultStr);
		}finally{
			try {
				save.SaveLog_RequiresNew(logVO);
			} catch (Exception e) {
				// TODO �Զ����ɵ� catch ��
				BaseDAO dao=new BaseDAO();
				try {
					dao.insertVO(logVO);
				} catch (DAOException e1) {
					// TODO �Զ����ɵ� catch ��
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}

	private void beforeAction(SynProjectDocParam param) throws Exception {
		InvocationInfoProxy.getInstance().setUserDataSource(
				IProjectDocSyn.NC_DataSourceName);
		InvocationInfoProxy.getInstance().setUserId(IProjectDocSyn.SaleUserId);
		InvocationInfoProxy.getInstance().setUserCode(
				IProjectDocSyn.SaleOperatorName);
		// ע����֤���
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
