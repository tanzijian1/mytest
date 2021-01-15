package nc.bs.tg.outside.salebpm.utils;

import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.BusinessException;

public class SaleBPMBillUtils {
	public static final String DataSourceName = "design";// 接口默认数据源
	public static final String BPMOperatorName = "BPM";// BPM默认操作员
	public static final String STATUS_SUCCESS = "1";// 成功
	public static final String STATUS_FAILED = "0";// 失败

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM操作用户

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	/**
	 * 接口保存操作前增加 用于防止保存排队情况(耗时长)时，外系统重新送单据，NC出现重复操作
	 * 
	 * @param billcode
	 *            外系统单据唯一标识字符串
	 * @throws BusinessException
	 */
	public static String addBillQueue(String billqueue)
			throws BusinessException {
		if (null != billqueue && !"".equals(billqueue)
				&& !m_billqueue.add(billqueue)) {
			throw new BusinessException("业务单据【" + billqueue
					+ "】,已在执行队列,请勿重复执行!");
		}
		return billqueue;
	}

	/**
	 * 接口退出时移出
	 * 
	 * @param billcode
	 *            外系统单据唯一标识字符串,用于防止保存排除情况重复导入
	 * @return
	 */
	public static boolean removeBillQueue(String billqueue) {
		if (null != billqueue && !"".equals(billqueue))
			m_billqueue.remove(billqueue);
		return true;
	}
	/**
	 * 根据编码得到结算方式pk
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getbd_balatypeBycode(String code) throws DAOException{
		String pk=(String)getBaseDAO().executeQuery("select   pk_balatype  from bd_balatype where code='"+code+"' and nvl(dr,0) = 0 ", new ColumnProcessor());
		
		return pk;
				
	}
	/**
	 * 根据编码在自定义档案查地区财务审核状态
	 * @param code
	 * @return
	 * @throws DAOException 
	 */
public String getregionstate(String code) throws DAOException{
	String pk_defdoc=(String)getBaseDAO().executeQuery("select   pk_defdoc  from bd_defdoc where code='"+code+"'", new ColumnProcessor());
	return pk_defdoc;
}
	/**
	 * 元数据持久化查询接口
	 * 
	 * @return
	 */
	public IMDPersistenceQueryService getMDQryService() {
		if (mdQryService == null) {
			mdQryService = NCLocator.getInstance().lookup(
					IMDPersistenceQueryService.class);
		}
		return mdQryService;
	}

	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	/**
	 * 读取BPM操作员默认用户
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getBPMUserID() throws BusinessException {
		if (bpmUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ BPMOperatorName + "'";
			bpmUserid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return bpmUserid;
	}

	/**
	 * 读操作员信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getUserInfo(String username)
			throws BusinessException {
		String sql = "select cuserid,user_code from sm_user  where user_code = '"
				+ username + "'";
		Map<String, String> userInfo = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());

		return userInfo;
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
		Map<String, String> userInfo = (Map<String, String>) getBaseDAO()
				.executeQuery(sql.toString(), new MapProcessor());

		return userInfo;
	}
	private ISecurityTokenCallback getTokenCallBacck() {
		if (sc == null) {
			sc = NCLocator.getInstance().lookup(ISecurityTokenCallback.class);
		}
		return sc;
	}

	/**
	 * 注入安全令牌
	 * 
	 * @param key
	 * @param methodName
	 * @return
	 */
	public byte[] token(String key, String methodName) {
		return getTokenCallBacck().token((key + methodName).getBytes(),
				methodName.getBytes());

	}

	/**
	 * 释放安全令牌
	 * 
	 * @param token
	 */
	public void restore(byte[] token) {
		getTokenCallBacck().restore(token);

	}

	/**
	 * 通过NC部门主键从中间表读取HCM部门主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getHCMDeptID(String id) throws BusinessException {
		String sql = "select t.id from organizationitem t  inner join org_dept  on org_dept.code = t.seqnum where org_dept.pk_dept ='"
				+ id + "'";
		String hcmid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return hcmid;
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
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO().executeQuery(sql, new MapProcessor());
		return infoMap;
	}
	
	/**
	 * 根据pk获取交易类型编码
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public String getTranstypeCode(String pk) throws BusinessException{
		String sql = "select pk_billtypecode from bd_billtype where pk_billtypeid = '"+pk+"'";
		String transtypeCode = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return transtypeCode;
	}
	
	
	/**
	 * 获取结算方式
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getBalatype(String pk_balatype)
			throws BusinessException {
		String sql = "select bd_balatype.pk_balatype pk_balatype, bd_balatype.code code, bd_balatype.name name from bd_balatype where nvl(dr,0) = 0 and bd_balatype.pk_balatype = '"+pk_balatype+"' ";
		Map<String, String> balatypeInfo = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());

		return balatypeInfo;
	}
	/**
	 * 获取任务模板名称
	 * @throws BusinessException 
	 */
	public String getTaskdefNameByPk(String pk_task) throws BusinessException {
		String sql = "select tb_md_taskdef.objname from tb_md_taskdef left join tb_md_task on tb_md_task.pk_taskdef = tb_md_taskdef.pk_obj"
				+ " where nvl(tb_md_taskdef.dr,0)=0 and tb_md_task.pk_obj = '"+pk_task+"'";
		String objname = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return objname;
	}
	
	/**
	 * 获取币种名称
	 * @throws BusinessException 
	 */
	public String getCurrNameByPk(String pk_currtype) throws BusinessException{
		String sql = "select name from bd_currtype where pk_currtype = '"+pk_currtype+"'";
		String name = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return name;
	}
	/**
	 * 获取预算科目(二级科目-三级科目-四级科目)
	 * @param sub
	 * @param pk_obj
	 * @return
	 * @throws BusinessException
	 */
	public String getBudgetSub(String sub,String pk_obj,String nodeCode) throws BusinessException{
		String sql = null;
		if("20200806".equals(nodeCode)){
			sql = "select pk_parent,objname from tb_budgetsub where pk_obj = '"+pk_obj+"'";
		}
		Map<String, String> map = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		if(map.get("pk_parent")!= null){
			if(sub.equals("")){
				sub = map.get("objname");
			}else{
				sub = map.get("objname")+"-"+sub;
			}
			return getBudgetSub(sub, map.get("pk_parent"),nodeCode);
		}else{
			String[] subArr = sub.split("-");
			if(subArr.length >3){
				sub = subArr[0]+"-"+subArr[1]+"-"+subArr[2];
			}
			return sub.equals("")?null:sub;
		}
	}
	
	/**
	 * 根据【公司编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_orgByCode(String code) {
		String sql = "select pk_org from org_orgs where (code='" + code
				+ "' or name = '" + code + "') and dr=0 and enablestate=2 ";
		String pk_org = null;
		try {
			pk_org = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 读取交易类型PK
	 * 
	 * @param budgetsub
	 * @return
	 */
	protected String gettradeTypeByCode(String tradetypecode) {
		String result = null;
		String sql = "select pk_billtypeid from bd_billtype where  pk_billtypecode ='"
				+ tradetypecode + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 根据pk获取档案名称
	 * 
	 * @param budgetsub
	 * @return
	 */
	public String getDocNameByPk(String pk_defdoc) throws BusinessException{
		String sql = "select name from bd_defdoc where pk_defdoc = '"+pk_defdoc+"'";
		String docName = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return docName;
	}
	
	/**
	 * 根据pk获取用户名称
	 * 
	 * @param budgetsub
	 * @return
	 * @throws BusinessException 
	 */
	public String getUserNameByPk(String pk_psndoc) throws BusinessException{
		String sql = "select bd_psndoc.name from bd_psndoc where pk_psndoc='"+pk_psndoc+"' ";
		String userName = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return userName;
	}
	/**
	 * 根据pk获取部门名称
	 * 
	 * @param budgetsub
	 * @return
	 * @throws BusinessException 
	 */
	public String getDeptNameByPk(String pk_dept) throws BusinessException{
		String sql = "select name from org_dept where pk_dept = '"+pk_dept+"'";
		String deptName = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return deptName;
	}
}
