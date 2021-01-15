package nc.ui.tg.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uap.pub.fs.client.FileStorageClient;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pub.filesystem.FileSystemUtil;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pub.attachment.config.itf.IAttachmentOperation;
import nc.pub.filesystem.newui.AttachCloudStorageFactory;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.org.DeptVO;
import nc.vo.org.FinanceOrgVO;
import nc.vo.pmpub.project.ProjectBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.filesystem.FileTypeConst;
import nc.vo.pub.filesystem.NCFileVO;
import nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO;

public class SendBPMUtils {

	static SendBPMUtils utils = null;

	public static SendBPMUtils getUtils() {
		if (utils == null) {
			utils = new SendBPMUtils();
		}
		return utils;
	}

	IUAPQueryBS queryBS = null;
	IMDPersistenceQueryService persistenceQueryService = null;
	public static final String File_NAME = "name";// 文件上传名称
	public static final String File_SIZE = "size";// 文件上传大小
	public static final String File_TYPE = "type";// 文件上传类型
	public static final String File_URL = "url";// 文件上传地址
	public static final String File_NCBILLNO = "ncBillNo";// 文件上传来源单据号
	
	public IUAPQueryBS getQueryBS() {
		if (queryBS == null) {
			queryBS = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}

		return queryBS;
	}
	
	public IMDPersistenceQueryService getPersistenceQueryService() {
		if (persistenceQueryService == null) {
			persistenceQueryService = NCLocator.getInstance().lookup(IMDPersistenceQueryService.class);
		}

		return persistenceQueryService;
	}
	
	/**
	 * 获取公司名称和编码
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public Object[] getOrgmsg(String pk_org)
			throws BusinessException {
		String sql = "select o.code,o.name from org_financeorg o where o.pk_financeorg='"
				+ pk_org + "'";
		Object[] orgMsg=null;
		orgMsg = (Object[]) getQueryBS()
				.executeQuery(sql, new ArrayProcessor());
		if(orgMsg==null){
			throw new BusinessException("组织信息查询失败，请检查！");
		}
//		orgMsg =  list.toArray(new Object[list.size()]);
		return orgMsg;
	}
	
	/**
	 * 获取部门名称和编码
	 * @param pk_dept
	 * @return
	 * @throws BusinessException
	 */
	public Object[] getDeptmsg(String pk_dept) throws BusinessException {
		String sql = "select o.code ,o.name from org_dept o where o.pk_dept='"
				+ pk_dept + "'";
		Object[] deptMsg=null;
		deptMsg = (Object[]) getQueryBS()
				.executeQuery(sql, new ArrayProcessor());
		if(deptMsg==null){
			throw new BusinessException("部门信息查询失败，请检查！");
		}
		return deptMsg;
	}
	
	/**
	 * 获取人员名称
	 * 
	 * @param pk_person
	 * @return person_name 
	 * @throws BusinessException 
	 */
	public String getPerson_name(String pk_person) throws BusinessException {
		String sql = "select b.name from bd_psndoc b where b.pk_psndoc='"
				+ pk_person + "'";
		String person_name="";
		person_name = (String) getQueryBS().executeQuery(sql, new ColumnProcessor());
		return person_name;
	}
	
	/**
	 * 获取项目名称
	 * 
	 * @param pk_project
	 * @return projectMsg  projectMsg[0]=code  projectMsg[1]=name 
	 * @throws BusinessException 
	 */
	public Object[] getProject_name(String pk_project) throws BusinessException {
		String sql = "select b.project_code,b.project_name from bd_project b where b.pk_project='"
				+ pk_project + "'";
		Object[] projectMsg=null;
		projectMsg = (Object[]) getQueryBS().executeQuery(sql, new ArrayProcessor());
		if(projectMsg==null){
			throw new BusinessException("获取项目信息查询失败，请检查！");
		}
		return projectMsg;
	}
	
	/**
	 * 客户信息
	 * 
	 * @param pk_customer
	 * @return
	 * @throws BusinessException 
	 */
	public Object[] getCustomerMsg(String pk_customer) throws BusinessException {
		String sql = "select b.code,b.name from bd_customer b where b.pk_customer='"
				+ pk_customer + "'";
		Object[] customerMsg=null;
		customerMsg = (Object[]) getQueryBS().executeQuery(sql, new ArrayProcessor());
		if(customerMsg==null){
			throw new BusinessException("获取项目信息查询失败，请检查！");
		}
		return customerMsg;
	}
	
	/**
	 * 流程类别编码和名称
	 * 
	 * @param pk_defdoc
	 * @return flowMsg   flowMsg[0]:流程类别编码  flowMsg[1]:流程类别名称
	 * @throws BusinessException 
	 */
	public Object[] getFlowMsg(String pk_defdoc) throws BusinessException {
		String sql = "select b.code,b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_defdoc + "'";
		Object[] flowMsg=null;
		flowMsg = (Object[]) getQueryBS().executeQuery(sql, new ArrayProcessor());
		if(flowMsg==null){
			throw new BusinessException("获取项目信息查询失败，请检查！");
		}
		return flowMsg;
	}
	
	/**
	 * 合同名称
	 * 
	 * @param pk_defdoc
	 * @return contract_name
	 * @throws BusinessException 
	 */
	public String getContract_name(String pk_defdoc) throws BusinessException {
		String sql = "select b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_defdoc + "'";
		String contract_name="";
		contract_name = (String) getQueryBS().executeQuery(sql, new ColumnProcessor());
		return contract_name;
	}
	
	/**
	 * 获取附件
	 * @param pk_key
	 * @return
	 * @throws BusinessException
	 */
	public Object getFiles(String pk_key,MortgageAgreementVO parent) throws BusinessException {
		String sql = "select * from sm_pub_filesystem where ( filepath like '"
				+ pk_key + "%' ) and  isfolder='n' and nvl(dr,0)=0";
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		List<Map<String, String>> fileInfoList = (List<Map<String, String>>) getQueryBS()
				.executeQuery(sql, new MapListProcessor());
		if (fileInfoList == null || fileInfoList.size() == 0) {
			return null;
		}
		List<String> pathList = new ArrayList<String>();
		for (Map<String, String> fileInfo : fileInfoList) {
			pathList.add(fileInfo.get("filepath"));
		}
		Map<String, String> urlMap = getFileUrl(pathList);
		for (Map<String, String> fileInfo : fileInfoList) {
			Map<String, String> bodymap = new HashMap<String, String>();
			String[] names = fileInfo.get("filepath").split("/");
			bodymap.put(File_NAME, names[names.length - 1]);
			bodymap.put(File_SIZE, fileInfo.get("filelength") + "");
			bodymap.put(File_TYPE, fileInfo.get("filetype") + "");
			bodymap.put(File_URL, urlMap.get(fileInfo.get("filepath")));
			bodymap.put(File_NCBILLNO, parent.getBillno());
			list.add(bodymap);
		}
		return list;
	}
	
	/**
	 * by lhh 2018-08-20 根据系统保存filepath翻译web页面下载url
	 * 
	 * @param pathlist
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, String> getFileUrl(List<String> pathlist)
			throws BusinessException {
		Map<String, String> urlMap = new HashMap<>();
		for (int i = 0; i < pathlist.size(); i++) {
			String path = pathlist.get(i);
			Logger.debug("------------------------sungbztest showFileInWeb--------------------");

			IFileSystemService fser = NCLocator.getInstance().lookup(
					IFileSystemService.class);

			ArrayList<String> fullpathlist = new ArrayList<String>();
			path = FileSystemUtil.validatePathString(path);

			fullpathlist.add(path);
			NCFileVO[] arrfilevo = null;
			arrfilevo = fser.queryFileVOsByPathList(fullpathlist);
			if (arrfilevo.length == 0) {
				Logger.error("未获取到相应路径的附件NCFileVO对象");
				// return;
			}
			if (FileTypeConst.getAttachMentStoreType(arrfilevo[0].getIsdoc())
					.equals(FileTypeConst.STORE_TYPE_2)) {
				String pk_doc = arrfilevo[0].getPk_doc();
				String urlstring = FileStorageClient.getInstance()
						.getDownloadURL(null, pk_doc);
				urlMap.put(path, urlstring);

			} else if (FileTypeConst.getAttachMentStoreType(
					arrfilevo[0].getIsdoc()).equals(FileTypeConst.STORE_TYPE_1)) {
				// 63存储方式暂不处理
				// showFileInWebStorebyV63(dsName,path,lastmodtime);
			} else if (FileTypeConst.getAttachMentStoreType(
					arrfilevo[0].getIsdoc()).equals(FileTypeConst.STORE_TYPE_3)) {
				String uniqcode = arrfilevo[0].getIsdoc();
				IAttachmentOperation downloadclass = AttachCloudStorageFactory
						.getInstance().getDownloadImp(uniqcode);
				String urlstring = downloadclass.getDownloadURL(arrfilevo[0]
						.getPk_doc());
				urlMap.put(path, urlstring);
			}

		}
		return urlMap;

	}
}
