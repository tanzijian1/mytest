package nc.bs.tg.outside.bpm.utils;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.BusinessException;

public class BPMBillUtils {
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
	 * @author 黄冠华
	 * 判断字符串是否为空
	 * @param str
	 */
	public static boolean isNotBlank(String str){
		if (str == null || "null".equals(str)||"".equals(str)||"~".equals(str)){
			return false;
		}
		return true;
	}
	
	/**
	 * @author 黄冠华
	 * 获取银行账号等信息
	 * @param str
	 */
	public  List<Map<String,String>>  getBankDetails(String pk_bankaccsub){
		List<Map<String,String>> lists=null;
		if(isNotBlank(pk_bankaccsub)){
			String sql = new StringBuffer().append(
					"select sub.accnum, sub.accname,doc.name docname").append("\n").append(
							"from bd_bankaccbas bas").append("\n").append(
									"left join bd_bankaccsub  sub on bas.pk_bankaccbas=sub.pk_bankaccbas").append("\n").append(
											"left join bd_bankdoc doc on bas.pk_bankdoc=doc.pk_bankdoc").append("\n").append(
													"where pk_bankaccsub ='"+pk_bankaccsub+"'").toString();
			try {
				lists=(List<Map<String, String>>) getBaseDAO().executeQuery(sql,new  MapListProcessor());
			} catch (DAOException e) {
				Logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		return lists;
	}
}
