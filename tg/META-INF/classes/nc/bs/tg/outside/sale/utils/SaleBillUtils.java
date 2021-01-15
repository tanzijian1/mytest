package nc.bs.tg.outside.sale.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.SQLParameter;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.hzvat.billmaintenance.BillmaintenanceVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;

public class SaleBillUtils {
	static SaleBillUtils utils;
	public static final String DataSourceName = "design";// 接口默认数据源
	public static final String SaleOperatorName = "SALE";// BPM默认操作员
	public static final String STATUS_SUCCESS = "1";// 成功
	public static final String STATUS_FAILED = "0";// 失败

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM操作用户

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static SaleBillUtils getUtils() {
		if (utils == null) {
			utils = new SaleBillUtils();
		}
		return utils;
	}

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
	public String getSaleUserID() throws BusinessException {
		if (bpmUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ SaleOperatorName + "'";
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
		String sql = "select cuserid,user_code from sm_user  where user_name = '"
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
	
	public String getRefAttributePk(String key, String value) throws BusinessException {
		if (value == null || "".equals(value))
			return null;
		String pkValue = null;
		String sql = null;
		SQLParameter parameter = new SQLParameter();
		if ("billmaker".equals(key)) {
			sql = "select cuserid from sm_user where user_code = ?";
			parameter.addParam(value);
		} else if ("pk_org".equals(key)) {
			sql = "select pk_financeorg from org_financeorg where code = ?";
			parameter.addParam(value);
		} else if ("scomment".equals(key)) {
			sql = "select pk_summary from fipub_summary where code = ?";
			parameter.addParam(value);
		} else if ("transtype".equals(key)) {
			sql = "select  pk_billtypeid from bd_billtype where pk_billtypecode = ?";
			parameter.addParam(value);
		} else if ("billtype".equals(key)) {
			sql = "select pk_billtypeid from bd_billtype where pk_billtypecode = ?";
			parameter.addParam(value);
		} else if ("pk_currtype".equals(key)){
			sql= "SELECT pk_currtype FROM bd_currtype WHERE CODE = ?";
			parameter.addParam(value);
		} else if ("transformoutbank".equals(key)){
			sql = "SELECT pk_bankdoc FROM bd_bankdoc WHERE enablestate = '2' AND NVL(dr,0) = 0 AND code = ?";
			parameter.addParam(value);
		} else if ("transformoutaccount".equals(key)){
			sql = "SELECT pk_bankaccsub FROM bd_bankaccsub WHERE accnum = ?";
			parameter.addParam(value);
		} else if ("pk_balatype".equals(key)){
			sql = "SELECT pk_balatype FROM bd_balatype WHERE enablestate = '2' AND NVL(dr,0) = 0 AND NAME = ?";
			parameter.addParam(value);
		} else if ("pk_busitype".equals(key)){
			sql = "select pk_busitype from bd_busitype  where (pk_group='000112100000000005FD' and validity=1 and  busicode = ?) ";
			parameter.addParam(value);
		}else if("balatypecode".equals(key)){
			sql = "SELECT pk_balatype FROM bd_balatype WHERE enablestate = '2' AND NVL(dr,0) = 0 AND code = ?";
			parameter.addParam(value);
		}
		
		pkValue = (String) getBaseDAO().executeQuery(sql, parameter, 
				new ColumnProcessor());
		return pkValue;
	}

	/**
	 * 读取业务单据聚合VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public AggregatedValueObject getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	/**
	 * 根据发票编码获取发票PK
	 * @param code
	 * @return pk_invoicetype
	 * @throws BusinessException 
	 */
	public String getPk_invoicetype(Integer code) throws BusinessException{
		String sql = "SELECT pk_invoicetype FROM hzvat_invoicetype WHERE CODE = '"+code+"'";
		String pk_invoicetype = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_invoicetype;
	}
	
	/**
	 * 根据【房地产项目编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_projectByCode(String code) throws BusinessException {
		String sql = "SELECT de.pk_project FROM bd_project de "
				+ "WHERE de.def2 = '" + code + "' AND NVL(de.dr,0) = 0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	/**
	 * 根据【房地产项目编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_defdocByCode(String code) throws BusinessException {
		String sql = "SELECT def.pk_defdoc FROM bd_defdoc def\n" +
						"WHERE def.code = '"+code+"' AND NVL(def.dr,0) = 0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	/**
	 * 根据【房地产项目名称】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_defdocByName(String name) throws BusinessException {
		String sql = "SELECT def.pk_defdoc FROM bd_defdoc def\n" +
						"WHERE def.name = '"+name+"' AND NVL(def.dr,0) = 0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	/**
	 * 通过名称或编码自定义项得到pk
	 * @param name
	 * @param deflistcode
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_defdocByNameORCode(String name,String deflistcode) throws BusinessException {
		String sql = "SELECT def.pk_defdoc FROM bd_defdoc def\n" +
						"WHERE (def.name = '"+name+"' or def.code='"+name+"') AND NVL(def.dr,0) = 0 and def.pk_defdoclist=(select bd_defdoclist.pk_defdoclist from bd_defdoclist where bd_defdoclist.code='"+deflistcode+"')";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	/**
	 * 根据【税收编码】获取PK
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_vatInfoByCode(String code) throws BusinessException {
		String sql = "SELECT pk_vatinfo FROM hzvat_vatInfo WHERE hbcode = '"+code+"' AND NVL(dr,0) = 0";
		String pk_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_project;
	}
	
	/**
	 * 根据【款项类型】获取收支科目PK
	 * @param pk
	 * @return
	 * @throws BusinessException
	 */
	public String getPk_subcode(String pk) throws BusinessException {
		String sql = "SELECT factorvalue2 FROM fip_docview_b WHERE NVL(dr,0) = 0 AND factorvalue1 = '"+pk+"' "
				+ "AND pk_classview = (SELECT pk_classview FROM fip_docview WHERE viewcode = 'KX01' AND NVL(dr,0) = 0)";
		String pk_subcode = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_subcode;
	}
	/**
	 * 获取开票组织维护相关信息
	 * @param code 财务编码
	 * @param no 开票机号
	 * @return
	 * @throws BusinessException
	 */
	public BillmaintenanceVO getSellerMsgVO(String code,String no) throws BusinessException{
		if(code == null || "".equals(code)){
			throw new BusinessException("数据异常，购方名称不能为空！");
		}
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT BM.NAME, BM.ADDRESS, BM.TELEPHONE, BM.KHH, BM.YHZH, BM.TAXID");
		sb.append("  FROM HZVAT_BILLMAINTENANCE BM");
		sb.append(" WHERE BM.PK_ORG = (SELECT PK_ORG");
		sb.append("                      FROM ORG_ORGS");
		sb.append("                     WHERE CODE = '"+code+"' and bm.opennum = '"+no+"' AND ENABLESTATE = '2' AND NVL(DR, 0) = 0)");
		sb.append("   AND NVL(BM.DR, 0) = 0");
		BillmaintenanceVO vo = (BillmaintenanceVO) getBaseDAO().executeQuery(sb.toString(), new BeanProcessor(BillmaintenanceVO.class));
		
		return vo;
	}
   /**
    * 通过人员编码得到用户pk
 * @throws DAOException 
    */
	public String getUserByPsondoc(String psndoccode) throws DAOException{
		String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where pk_base_doc=(select pk_psndoc from bd_psndoc where code='"+psndoccode+"')", new ColumnProcessor());
	    return pk;
	}
	
	/**
	    * 通过用户编码得到用户pk
	 * @throws DAOException 
	    */
	public String getUserByCode(String psndoccode) throws DAOException{
			String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where user_code='"+psndoccode+"'", new ColumnProcessor());
		    return pk;
		}
	/**
	 * 客商编码或名称得到pk
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getBd_cust_supplier(String code) throws DAOException {
		String pk=(String) getBaseDAO().executeQuery("select   pk_cust_sup  from bd_cust_supplier where (code='"+code+"' or name='"+code+"')", new ColumnProcessor());
	    return pk;
	}
}
