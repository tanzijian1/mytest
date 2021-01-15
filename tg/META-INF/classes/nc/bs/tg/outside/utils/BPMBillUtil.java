package nc.bs.tg.outside.utils;

import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.model.MetaDataException;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.md.persist.framework.IMDPersistenceService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class BPMBillUtil extends BillUtils {
	public static final String DataSourceName = "design";// 接口默认数据源
	public static final String BPMOperatorName = "LLBPM";// BPM默认操作员
	public static final String STATUS_SUCCESS = "1";// 成功
	public static final String STATUS_FAILED = "0";// 失败

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IMDPersistenceService perService = null;
	IUAPQueryBS bs = null;

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
	 * 
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getbd_balatypeBycode(String code) throws DAOException {
		String pk = (String) getBaseDAO().executeQuery(
				"select   pk_balatype  from bd_balatype where code='" + code
						+ "' and nvl(dr,0) = 0 ", new ColumnProcessor());

		return pk;

	}

	/**
	 * 根据编码在自定义档案查地区财务审核状态
	 * 
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getregionstate(String code) throws DAOException {
		String pk_defdoc = (String) getBaseDAO().executeQuery(
				"select   pk_defdoc  from bd_defdoc where code='" + code + "'",
				new ColumnProcessor());
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

	public IUAPQueryBS getQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
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
		sql.append("select bd_psndoc.name psnname,org_dept.name deptname,org_dept.code deptcode from sm_user  ");
		sql.append(" left join bd_psndoc on sm_user.pk_psndoc = bd_psndoc.pk_psndoc ");
		sql.append(" left join bd_psnjob on sm_user.pk_psndoc = bd_psnjob.pk_psndoc and bd_psnjob.ismainjob = 'Y' ");
		sql.append(" left join org_dept on org_dept.pk_dept = bd_psnjob.pk_dept ");
		sql.append(" where sm_user.cuserid = '" + cuserid + "' ");
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
	public Map<String, String> getPerson_name(String pk_psndoc)
			throws BusinessException {
		String sql = "select pk_psndoc,code,name from bd_psndoc  where pk_psndoc = '"
				+ pk_psndoc + "'";
		// String person_name="";
		// person_name = (String) getBaseDao().executeQuery(sql, new
		// ColumnProcessor());
		Map<String, String> infoMap = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return infoMap;
	}

	/**
	 * 根据pk获取交易类型编码
	 * 
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public String getTranstypeCode(String pk) throws BusinessException {
		String sql = "select pk_billtypecode from bd_billtype where pk_billtypeid = '"
				+ pk + "'";
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
		String sql = "select bd_balatype.pk_balatype pk_balatype, bd_balatype.code code, bd_balatype.name name from bd_balatype where nvl(dr,0) = 0 and bd_balatype.pk_balatype = '"
				+ pk_balatype + "' ";
		Map<String, String> balatypeInfo = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());

		return balatypeInfo;
	}

	/**
	 * 元数据持久化接口
	 * 
	 * @return
	 */
	public IMDPersistenceService getMDService() {
		if (perService == null) {
			perService = NCLocator.getInstance().lookup(
					IMDPersistenceService.class);
		}
		return perService;
	}

	/**
	 * 更新VO数据
	 * 
	 * @param vo
	 * @param strs
	 * @throws MetaDataException
	 */
	public void updateBillWithAttrs(AggregatedValueObject vo, String[] strs)
			throws MetaDataException {
		getMDService().updateBillWithAttrs(new Object[] { vo }, strs);
	}

}
