package nc.vo.tg.pub;

import java.util.Collection;
import java.util.HashMap;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.util.Querydata;
import nc.itf.bd.bankdoc.IBankdocQueryService;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.banktype.BankTypeVO;
import nc.vo.org.OrgVO;
import nc.vo.pub.BusinessException;

public class BusinessQuerydata {

	// private HashMap<String, String> m_corphash;// 公司
	//
	// private HashMap<String, String> m_currhash;// 币种
	//
	// private HashMap<String, String> m_custhash;// 客户

	private HashMap<String, String> m_bankhash;// 银行账户

	private HashMap<String, BankTypeVO> m_banktypehash;// 银行类型
	private HashMap<String, String> m_grouphash;// 集团
	private HashMap<String, String> m_addresshash;// 地址
	private HashMap<String, String> m_Countryhash;// 国家
	private HashMap<String, String> m_Areacodehash;// 地区代码

	// String nc65test20171207
	// =InvocationInfoProxy.getInstance().getUserDataSource();
	private static BusinessQuerydata querydata = null;
	
	IMDPersistenceQueryService mdQryService = null;

	private BusinessQuerydata() {
		Logger.error("@@重新实例化@@ Querydata ");
		// m_corphash = new HashMap<String, String>();// 公司
		// m_currhash = new HashMap<String, String>();// 币种
		// m_custhash = new HashMap<String, String>();// 客户
		m_bankhash = new HashMap<String, String>();// 银行账户
		m_banktypehash = new HashMap<String, BankTypeVO>();// 银行类型
		m_grouphash = new HashMap<String, String>();// 集团
		m_addresshash = new HashMap<String, String>();// 地址
		m_Countryhash = new HashMap<String, String>();// 国家
		m_Areacodehash = new HashMap<String, String>();// 地区代码

	}

	public static BusinessQuerydata getInstance() {
		if (querydata == null || checkOverTime()) {
			querydata = new BusinessQuerydata();
		}
		return querydata;
	}

	private static long overTime = 24 * 60 * 60 * 1000;// (秒) x小时后需要清空缓存表。
	private static long cacheTime = 0;

	private static boolean checkOverTime() {
		if (cacheTime == 0) {
			cacheTime = System.currentTimeMillis();
			return false;
		}
		long current = System.currentTimeMillis();
		if ((current - cacheTime) > overTime) {
			cacheTime = current;
			return true;
		}
		return false;
	}

	/**
	 * 集团银行账户
	 */
	public String getpk_bank(String bfyhzh, String returnType, OrgVO corpVO)
			throws BusinessException {
		if (null == bfyhzh || "".equals(bfyhzh)) {
			return null;
		}
		String pk_corp = corpVO.getPk_org();
		String corpCode = corpVO.getCode();
		String pk_bank = m_bankhash.get(pk_corp + bfyhzh + "-" + returnType);
		if (isNotNull(pk_bank)) {
			return pk_bank;
		}
		// Logger.info("================getpk_bank：" + pk_corp + bfyhzh + "-" +
		// returnType);
		// if (m_bankhash.containsKey(pk_corp + bfyhzh + "-" + returnType)){
		// Logger.info("================getpk_bank result1：" +
		// m_bankhash.get(pk_corp + bfyhzh + "-" + returnType));
		// return m_bankhash.get(pk_corp + bfyhzh + "-" + returnType);
		// }
		else {
			BaseDAO dao = new BaseDAO();
			long s = System.currentTimeMillis();
			Object[] obj = (Object[]) dao
					.executeQuery(
							"select pk_bankaccbas,accname from bd_bankaccbas where isnull(dr,0)=0 and ( accnum='"
									+ bfyhzh
									+ "' or code='"
									+ bfyhzh
									+ "'  or accname='"
									+ bfyhzh
									+ "' ) and ( financeorg='"
									+ pk_corp
									+ "' or pk_org = 'GLOBLE00000000000000' or pk_group ='"
									+ corpVO.getPk_group() + "')",
							new ArrayProcessor());
			long e = System.currentTimeMillis();
			if ((e - s) > 100) {
				Logger.warn("#### getpk_bank(" + pk_corp + ", " + bfyhzh + ","
						+ returnType + ") Query ####" + (e - s));
			}
			// if (obj == null || obj.length == 0)
			// throw new EsbTranBusinessException(getTransErrMessage("银行账号档案",
			// "银行账号", bfyhzh, "根据账号翻译", corpCode));
			m_bankhash.put(pk_corp + bfyhzh + "-id", obj[0].toString());
			m_bankhash.put(pk_corp + bfyhzh + "-name", obj[1].toString());
			Logger.info("================getpk_bank result2："
					+ m_bankhash.get(pk_corp + bfyhzh + "-" + returnType));
			return m_bankhash.get(pk_corp + bfyhzh + "-" + returnType);
		}
	}

	/**
	 * 币种
	 * 
	 * @param bzbm
	 * @param corpVO
	 * @return
	 * @throws BusinessException
	 */
	public String getpk_currtype() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		long s = System.currentTimeMillis();
		Object obj = dao
				.executeQuery(
						"select pk_currtype from bd_currtype  where (CODE ='CNY' or name ='人民币')",
						new ColumnProcessor());
		long e = System.currentTimeMillis();
		if ((e - s) > 100) {
			Logger.warn("#### getpk_currtype(" + "币种" + ") Query ####"
					+ (e - s));
		}
		return (String) obj;
	}

	public String getcustomer(String code) throws BusinessException {

		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	public String getcustomerClass() throws BusinessException {

		BaseDAO dao = new BaseDAO();
		Object obj = dao
				.executeQuery(
						"select pk_custclass from bd_custclass where name='外部客户' and nvl(dr,0)=0",
						new ColumnProcessor());
		return (String) obj;
	}

	public String getStringvalue(String table, String pk, String column,
			String value) throws BusinessException {
		BaseDAO dao = new BaseDAO(InvocationInfoProxy.getInstance()
				.getUserDataSource());
		Object obj = dao.executeQuery("select " + pk + " from " + table
				+ " where " + column + "='" + value + "' and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	public String getpk_supplierclass() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao
				.executeQuery(
						"select pk_supplierclass from bd_supplierclass where name='外部供应商'",
						new ColumnProcessor());
		return (String) obj;
	}

	public String getpk_org() throws BusinessException {
		BaseDAO dao = new BaseDAO();

		try {
			String pk_org = (String) dao.executeQuery(
					"select pk_group from org_orgs where code='0001'",
					new ColumnProcessor());
			if (pk_org != null) {
				return pk_org;
			}
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			Logger.error(e.getMessage());
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	public boolean isNotNull(String value) {
		return value != null && !"".equals(value) && value.trim().length() > 0;
	}

	public BankdocVO getIBankdoc(String bankcode) {
		if (isNotNull(bankcode)) {
			IBankdocQueryService service = NCLocator.getInstance().lookup(
					IBankdocQueryService.class);
			try {
				BankdocVO VOs = getBillVO(BankdocVO.class, "nvl(dr,0)=0 and (code ='"
						+ bankcode + "' or name='" + bankcode + "')");
//				BankdocVO[] bankdocVOs = service
//						.queryBankdocVOsByCon("nvl(dr,0)=0 and (code ='"
//								+ bankcode + "' or name='" + bankcode + "')");
				if (VOs != null) {
					return VOs;
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage());
			}
		}
		return null;
	}

	public BankTypeVO getBankTypeVO(String typename) throws BusinessException {
		if (m_banktypehash.containsKey(typename)) {
			return m_banktypehash.get(typename);
		}
		String sql = "select * from bd_banktype where dr = 0 and bd_banktype.name = '"
				+ typename + "'";
		BaseDAO dao = new BaseDAO();
		BankTypeVO typeVO = (BankTypeVO) dao.executeQuery(sql,
				new BeanProcessor(BankTypeVO.class));
		if (typeVO != null) {
			m_banktypehash.put(typename, typeVO);
		}
		return m_banktypehash.get(typename);

	}
	
	public String getPk_org(String code) throws BusinessException {
		String sql = "select pk_org from org_orgs where dr = 0 and code = '"
				+ code + "'";
		BaseDAO dao = new BaseDAO();
		String result = (String) dao.executeQuery(sql,
				new ColumnProcessor());
		return result;

	}

	public String getGroupID() throws BusinessException {
		String groupcode = "0001";
		if (m_grouphash.containsKey(groupcode)) {
			return m_grouphash.get(groupcode);
		}
		String sql = "select pk_group from org_group where code = '0001'";
		BaseDAO dao = new BaseDAO();
		Object value = dao.executeQuery(sql, new ColumnProcessor());
		if (value != null) {
			m_grouphash.put(groupcode, (String) value);
		}
		return m_grouphash.get(m_grouphash);

	}

	public String getAddressID(String detailinfo) throws BusinessException {
		if (m_addresshash.containsKey(detailinfo)) {
			return m_addresshash.get(detailinfo);
		}
		String sql = "select s.pk_address from bd_address s where s.detailinfo = '"
				+ detailinfo + "' and dr = 0";
		BaseDAO dao = new BaseDAO();
		Object value = dao.executeQuery(sql, new ColumnProcessor());
		if (value != null) {
			m_addresshash.put(detailinfo, (String) value);
		}
		return m_addresshash.get(detailinfo);
	}

	public String getCountryID(String country) throws BusinessException {
		if (m_Countryhash.containsKey(country)) {
			return m_Countryhash.get(country);
		}
		String sql = "select   pk_country  from Bd_Countryzone where (code = '"
				+ country + "' or name = '" + country + "') and dr = 0";
		BaseDAO dao = new BaseDAO();
		Object value = dao.executeQuery(sql, new ColumnProcessor());
		if (value != null) {
			m_Countryhash.put(country, (String) value);
		}
		return m_Countryhash.get(country);
	}

	public String getAreacodeID(String area) throws BusinessException {
		if (m_Areacodehash.containsKey(area)) {
			return m_Areacodehash.get(area);
		}
		String sql = "select bd_defdoc.pk_defdoc,bd_defdoc.code,bd_defdoc.name from bd_defdoc "
				+ " inner join bd_defdoclist on bd_defdoc.pk_defdoclist = bd_defdoclist.pk_defdoclist "
				+ "  where (bd_defdoc.code = '"
				+ area
				+ "' or bd_defdoc.name = '"
				+ area
				+ "') and bd_defdoc.dr = 0 and bd_defdoc.enablestate = 2";
		BaseDAO dao = new BaseDAO();
		Object value = dao.executeQuery(sql, new ColumnProcessor());
		if (value != null) {
			m_Areacodehash.put(area, (String) value);
		}
		return m_Areacodehash.get(area);
	}
	
	public String getaccinfo(String suppliercode, String accnum) {
		String result = null;
		BaseDAO dao = new BaseDAO();
		StringBuffer sql = new StringBuffer();
		sql.append(" select ");
		sql.append("   bd_bankaccsub.pk_bankaccsub ");
		sql.append(" from ");
		sql.append("   bd_supplier ");
		sql.append("   left join bd_custbank on bd_custbank.pk_cust = bd_supplier.pk_supplier ");
		sql.append("   left join bd_bankaccsub on bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub ");
		sql.append(" where ");
		sql.append("   nvl(bd_bankaccsub.dr, 0) = 0 ");
		sql.append("   and nvl(bd_custbank.dr, 0) = 0 ");
		sql.append("   and nvl(bd_supplier.dr, 0) = 0 ");
		sql.append("   and bd_bankaccsub.accnum = '" + accnum + "' ");
		sql.append("   and bd_supplier.pk_supplier = '" + suppliercode + "' ");
		try {
			result =  (String) dao.executeQuery(sql.toString(), new ColumnProcessor());
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return result;
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
	public BankdocVO getBillVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (BankdocVO) coll.toArray()[0];
		} else {
			return null;
		}
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
}
