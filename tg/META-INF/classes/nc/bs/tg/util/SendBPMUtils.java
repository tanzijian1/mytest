package nc.bs.tg.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ufsoft.table.format.UFDoubleFormat;

import uap.pub.fs.client.FileStorageClient;
import nc.bs.dao.BaseDAO;
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
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pnt.vo.FileManageVO;
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
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO;

public class SendBPMUtils {

	static SendBPMUtils utils = null;

	public static SendBPMUtils getUtils() {
		if (utils == null) {
			utils = new SendBPMUtils();
		}
		return utils;
	}

	BaseDAO dao = null;
	IMDPersistenceQueryService persistenceQueryService = null;
	public static final String File_NAME = "name";// 文件上传名称
	public static final String File_SIZE = "size";// 文件上传大小
	public static final String File_TYPE = "type";// 文件上传类型
	public static final String File_URL = "url";// 文件上传地址
	public static final String File_NCBILLNO = "ncBillNo";// 文件上传来源单据号
	
	public BaseDAO getBaseDao() {
		if (dao == null) {
			dao = new BaseDAO();
		}

		return dao;
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
	public Map<String, String> getOrgmsg(String pk_org)
			throws BusinessException {
		String sql = "select pk_org,code,name from org_orgs  where pk_org = '"
				+ pk_org + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 获取部门名称和编码
	 * @param pk_dept
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDeptmsg(String pk_dept) throws BusinessException {
		String sql = "select pk_dept,code,name from org_dept  where pk_dept = '"
				+ pk_dept + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 获取人员名称
	 * 
	 * @param pk_person
	 * @return person_name 
	 * @throws BusinessException 
	 */
	public Map<String, String> getPerson_name(String pk_psndoc) throws BusinessException {
		String sql = "select pk_psndoc,code,name from bd_psndoc  where pk_psndoc = '"
				+ pk_psndoc + "'";
//		String person_name="";
//		person_name = (String) getBaseDao().executeQuery(sql, new ColumnProcessor());
		Map<String, String> infoMap = (Map<String, String>) getBaseDao().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读制单人信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getUserInfoByID(String cuserid)
			throws BusinessException {
		StringBuffer sql = new StringBuffer();
		sql.append("select bd_psndoc.name psnname,org_dept.name deptname,org_dept.code deptcode,"
				+ "org_orgs.name compname,org_dept.pk_dept deptid,bd_psndoc.code psncode,org_orgs.code compcode from sm_user  ");
		sql.append(" left join bd_psndoc on sm_user.pk_psndoc = bd_psndoc.pk_psndoc ");
		sql.append(" left join bd_psnjob on sm_user.pk_psndoc = bd_psnjob.pk_psndoc and bd_psnjob.ismainjob = 'Y' ");
		sql.append(" left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept ");
		sql.append(" left join org_orgs on org_orgs.pk_org = bd_psnjob.pk_org ");
		sql.append(" where sm_user.cuserid = '"+cuserid+"' ");
		Map<String, String> userInfo = (Map<String, String>) getBaseDao()
				.executeQuery(sql.toString(), new MapProcessor());

		return userInfo;
	}
	
	/**
	 * 获取域账号
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getRegionNameByPersonCode(String code) throws BusinessException{
		String sql = "select userprincipalname,code from employeeitem  where code = '"
				+ code + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	
	/**
	 * 获取项目名称
	 * 
	 * @param pk_project
	 * @return projectMsg  projectMsg[0]=code  projectMsg[1]=name 
	 * @throws BusinessException 
	 */
	public Map<String, String> getProject_name(String pk_project) throws BusinessException {
		String sql = "select srcid,code,name from tgrz_projectdata  where pk_projectdata = '"
				+ pk_project + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取组织档案信息
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrgInfo(String pk_org)
			throws BusinessException {
		String sql = "select pk_org,code,name from org_orgs  where pk_org = '"
				+ pk_org + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	/**
	 * 读取银行信息
	 * @param pk_bankaccsub
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,String> getBankInfo(String pk_bankaccsub) throws BusinessException {
		String sql = "select b1.accnum,b1.accname,b3.name from bd_bankaccsub b1"+
					" left join bd_bankaccbas b2 on b1.pk_bankaccbas = b2.pk_bankaccbas and nvl(b2.dr,0)=0"+
					" left join bd_bankdoc b3 on b2.pk_bankdoc = b3.pk_bankdoc and nvl(b3.dr,0)=0"+
					" where b1.pk_bankaccsub = '"+pk_bankaccsub+"' and nvl(b1.dr,0)=0"+
					" and b2.enablestate=2";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	/**
	 * 读取付款单位名称
	 * @throws BusinessException 
	 */
	public String getPayerName(String pk_corp) throws BusinessException{
		String sql = "select name from org_corp where nvl(dr,0)=0 and pk_corp='"+pk_corp+"'";
		return (String)getBaseDao().executeQuery(sql, new ColumnProcessor());
	}
	/**
	 * 读取收款单位名称
	 */
	public String getPayeeName(String pk_cust_sup) throws BusinessException{
		String sql = "select name from bd_cust_supplier where nvl(dr,0)=0 and pk_cust_sup='"+pk_cust_sup+"'";
		return (String)getBaseDao().executeQuery(sql, new ColumnProcessor());
	}
	/**
	 * 读取单期发行情况的合同名称
	 * @param pk_singleissue
	 * @return
	 * @throws BusinessException
	 */
	public Map<String,String> getContractInfo(String pk_singleissue) throws BusinessException{
		String sql = "select sdfn_singleissue.name,sdfn_singleissue.billno,tgrz_fintype.name type "
				+ "	from sdfn_singleissue "
				+ " left join tgrz_fintype on sdfn_singleissue.def42=tgrz_fintype.pk_fintype"
				+ " where pk_singleissue = '"+pk_singleissue+"' and nvl(sdfn_singleissue.dr,0)=0";
		Map<String,String> map = (Map<String,String>)getBaseDao().executeQuery(sql, new MapProcessor());
		return map;
	}
	/**
	 * 客户信息
	 * 
	 * @param pk_customer
	 * @return
	 * @throws BusinessException 
	 */
	public Map<String, String> getCustomerMsg(String pk_cust_sup) throws BusinessException {
		String sql = "select pk_cust_sup,code,name from bd_cust_supplier where pk_cust_sup = '"
				+ pk_cust_sup + "'";
//		Object[] customerMsg=null;
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取客商档案信息
	 * 
	 * @param pk_applicationdept
	 * @return
	 */
	public Map<String, String> getCustInfo(String pk_cust_sup)
			throws BusinessException {
		String sql = "select pk_cust_sup,code,name from bd_cust_supplier where pk_cust_sup = '"
				+ pk_cust_sup + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 读取结算方式名称
	 * @param pk_balatype
	 * @return
	 * @throws BusinessException
	 */
	public String getBalaType(String pk_balatype) throws BusinessException {
		String sql = "select name from bd_balatype where pk_balatype='"+pk_balatype+"'";
		String name = (String)getBaseDao().executeQuery(sql, new ColumnProcessor());
		return name;
	}
	
	/**
	 * 流程类别编码和名称
	 * 
	 * @param pk_defdoc
	 * @return flowMsg   flowMsg[0]:流程类别编码  flowMsg[1]:流程类别名称
	 * @throws BusinessException 
	 */
	public Map<String, String> getFlowMsg(String pk_defdoc) throws BusinessException {
		String sql = "select b.code,b.name from bd_defdoc b where b.pk_defdoc = '"
				+ pk_defdoc + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 银行贷款合同明细
	 * 
	 * @param pk_defdoc
	 * @return flowMsg   flowMsg[0]:流程类别编码  flowMsg[1]:流程类别名称
	 * @throws BusinessException 
	 */
	public Map<String, String> getContract(String pk_contract) throws BusinessException {
		String sql = "select b.contractcode code,b.htmc name from cdm_contract b where b.pk_contract = '"
				+ pk_contract + "'";
		Map<String, String> infoMap = (Map<String, String>) getBaseDao()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
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
		contract_name = (String) getBaseDao().executeQuery(sql, new ColumnProcessor());
		return contract_name;
	}
	
	
	public String getIUAPQueryBS(String sql) {
		String result = "";
		try {
			result = (String) getBaseDao().executeQuery(sql, new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 附件推送
	 * 
	 * @param billid
	 * @return
	 */
	public List<Map<String, Object>> getFiles(String billid) {
		List<FileManageVO> listVOs = getFileInfos(billid);
		List<Map<String, Object>> listMap = new ArrayList<>();
		if (listVOs != null && listVOs.size() > 0) {
			for (FileManageVO vo : listVOs) {
				Map<String, Object> fileMap = new HashMap<String, Object>();
				String sizeSql = "select filelength from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String namesql = "select filepath from sm_pub_filesystem where pk='"
						+ vo.getPk_filemanage() + "'";
				String file_id = vo.getDocument_name() + "&" + vo.getFile_id();
				String name = getColumnValue(namesql).substring(
						getColumnValue(namesql).lastIndexOf("/") + 1);
				fileMap.put("FileID", file_id);
				fileMap.put("Name", name);
				fileMap.put("Ext", name.substring(name.lastIndexOf(".")));
				fileMap.put("Size", getColumnValue(sizeSql) == null ? 0
						: getColumnValue(sizeSql));
				fileMap.put("AppKey", "NC");
				fileMap.put("AppKeyTicket", "DF6AF297");
				listMap.add(fileMap);
			}
			return listMap;
		}
		return null;
	}
	
	/**
	 * 读取附件信息
	 * 
	 * @param billid
	 * @return
	 */
	public List<FileManageVO> getFileInfos(String billid) {
		String sql = "select b.* from bd_filemanage b, sm_pub_filesystem s "
				+ "where s.pk = b.pk_filemanage and s.isfolder = 'n' and  s.filepath like '"
				+ billid + "%'";
		List<FileManageVO> listVos = new ArrayList<FileManageVO>();
		try {
			listVos = (List<FileManageVO>) getBaseDao().executeQuery(sql,
					new BeanListProcessor(FileManageVO.class));
			if (listVos != null && listVos.size() > 0) {
				return listVos;
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 数据持久化，读取单行单列信息
	 * 
	 * @param sql
	 * @return
	 */
	private String getColumnValue(String sql) {
		// TODO 自动生成的方法存根
		String result = "";
		try {
			result = (String) getBaseDao().executeQuery(sql,
					new ColumnProcessor());
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return result;
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
	
	/**
	 * BPM回写id和BPM网站打开地址回NC
	 * @param resultMap
	 * @param key
	 * @throws DAOException
	 */
	public void updateVO(String taskid,String openUrl,String key) throws DAOException{
		StringBuffer sql = new StringBuffer();
		sql.append("update tgrz_mortgageagreement set def19 = '"+taskid+"' ");
		sql.append("def20 = '"+openUrl+"' where pk_moragreement = '"+key+"' and nvl(dr,0) = 0");
		getBaseDao().executeUpdate(sql.toString());
	}
	
	/**
	 * 报销人用户编码
	 */
	public String getUser_code(String pk_psndoc) {
		String sql = "select s.user_code from sm_user s where s.pk_psndoc='"
				+ pk_psndoc + "'";
		return getIUAPQueryBS(sql);
	}
	
	/**
	 * 贷款合同明细已还本金
	 */
	public UFDouble getRepayamount(String pk_contract) {
		String sql = "select sum(repayamount) from cdm_contract_exec where pk_contract='"
				+ pk_contract + "'";
		UFDouble repayamount = new UFDouble(0);
		try {
			Object amount= getBaseDao().executeQuery(sql, new ColumnProcessor());
			if(amount!=null){
				String tmp = amount.toString();
				repayamount = new UFDouble(tmp);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return repayamount;
	}
	
	/**
	 * 贷款合同明细已还利息
	 */
	public UFDouble getPayinterest(String pk_contract) {
		String sql = "select sum(payinterest) from cdm_contract_exec where pk_contract='"
				+ pk_contract + "'";
		UFDouble payinterest = new UFDouble(0);
		try {
			Object interest = getBaseDao().executeQuery(sql, new ColumnProcessor());
			if(interest!=null){
				String tmp = interest.toString();
				payinterest = new UFDouble(tmp);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return payinterest;
	}
}
