package nc.bs.tg.outside.utils;

import java.util.HashMap;
import java.util.Map;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.cmp.utils.BillcodeGenerater;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.vo.bd.account.AccAsoaVO;
import nc.vo.bd.prodline.ProdLineVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDateTime;

public class DocInfoQryUtils extends BillUtils {
	static DocInfoQryUtils utils;
	private static long overTime = 12 * 60 * 60 * 1000;// (秒) x小时后需要清空缓存表。
	private static long cacheTime = 0;
	private HashMap<String, String> m_group;// 集团
	private HashMap<String, Map<String, String>> m_orgs;// 财务组织
	private HashMap<String, Map<String, String>> m_defdoc;// 自定义档案
	private HashMap<String, String> m_defdoc_name;// 自定义档案名称
	private HashMap<String, Map<String, String>> m_user;// 用户
	private HashMap<String, Map<String, String>> m_custsup;// 供应商/客户/客商档案
	private HashMap<String, Map<String, String>> m_billtype; // 单据类型
	private HashMap<String, String> m_busitype; // 业务类型
	private HashMap<String, String> m_balatype; // 结算方式
	private HashMap<String, Map<String, String>> m_dept; // 部门
	private HashMap<String, Map<String, String>> m_psn; // 业务员
	private HashMap<String, HashMap<String, String>> m_inoutbusiclass;// 收支项目
	private HashMap<String, HashMap<String, String>> m_psnbankacc;// 个人银行账户
	private HashMap<String, HashMap<String, String>> m_custbank;// 客商银行账户
	private HashMap<String, HashMap<String, String>> m_banksub;// 银行账户子户
	private HashMap<String, HashMap<String, String>> m_cashaccount;// 现金账户
	private HashMap<String, HashMap<String, String>> m_currtype;// 币种
	private HashMap<String, HashMap<String, String>> m_project;// 项目
	private HashMap<String, HashMap<String, String>> m_project2;// 项目2
	private HashMap<String, HashMap<String, String>> m_bankaccnum;// 使用权账号
	private HashMap<String, HashMap<String, String>> m_custaccnum;// 客商账号
	private HashMap<String, HashMap<String, String>> m_suppaccnum;// 供应商账号

	private HashMap<String, HashMap<String, String>> m_funplan;// 资金计划

	private HashMap<String, HashMap<String, String>> m_budgetsub;// 预算科目
	private HashMap<String, String> m_supplier;// 供应商

	private HashMap<String, String> m_accsubj;// 会计科目信息
	private HashMap<String, String> m_recpaytype;// 收款业务类型
	private HashMap<String, HashMap<String, String>> m_bankdoc;// 银行档案

	private HashMap<String, String> m_materieltype;// 物料分类

	public static DocInfoQryUtils getUtils() {
		if (utils == null || checkOverTime()) {
			utils = new DocInfoQryUtils();
		}
		return utils;

	}

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

	public DocInfoQryUtils() {
		m_group = new HashMap<String, String>();// 集团
		m_orgs = new HashMap<String, Map<String, String>>();// 业务单元
		m_defdoc = new HashMap<String, Map<String, String>>();// 自定义档案
		m_user = new HashMap<String, Map<String, String>>();// 用户
		m_custsup = new HashMap<String, Map<String, String>>();// 客户档案
		m_defdoc_name = new HashMap<String, String>();// 自定义档案名称
		m_billtype = new HashMap<String, Map<String, String>>();// 单据类型
		m_busitype = new HashMap<String, String>();// 业务类型
		m_balatype = new HashMap<String, String>(); // 结算方式
		m_dept = new HashMap<String, Map<String, String>>(); // 部门
		m_psn = new HashMap<String, Map<String, String>>(); // 业务员
		m_inoutbusiclass = new HashMap<String, HashMap<String, String>>();// 收支项目
		m_psnbankacc = new HashMap<String, HashMap<String, String>>();// 个人银行账户
		m_custbank = new HashMap<String, HashMap<String, String>>();// 客商银行账户
		m_cashaccount = new HashMap<String, HashMap<String, String>>();// 现金账户
		m_currtype = new HashMap<String, HashMap<String, String>>();// 币种
		m_project = new HashMap<String, HashMap<String, String>>();// 项目
		m_project2 = new HashMap<String, HashMap<String, String>>();// 项目2

		m_supplier = new HashMap<String, String>();// 供应商
		m_budgetsub = new HashMap<String, HashMap<String, String>>();// 预算科目
		m_accsubj = new HashMap<String, String>();// 会计科目信息
		m_bankdoc = new HashMap<String, HashMap<String, String>>();// 银行档案
		m_banksub = new HashMap<String, HashMap<String, String>>();// 银行账户子户
		m_funplan = new HashMap<String, HashMap<String, String>>();// 资金计划项目

		m_bankaccnum = new HashMap<String, HashMap<String, String>>();// 使用权账号
		m_custaccnum = new HashMap<String, HashMap<String, String>>();// 客商账号
		m_recpaytype = new HashMap<String, String>();// 收款业务类型

		m_materieltype = new HashMap<String, String>();// 物料分类

		m_suppaccnum = new HashMap<String, HashMap<String, String>>();// 客商账号

	}

	/**
	 * 业务流程
	 * 
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public String getGroup(String code) throws BusinessException {
		if (m_group.get(code) == null) {
			String sql = "select ORG_GROUP.Pk_Group from ORG_GROUP where pk_group  ='"
					+ code + "' and isnull(dr,0)=0";
			String info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_group.put(code, info);
		}

		return m_group.get(code);
	}

	/**
	 * 业务流程
	 * 
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public String getBusitype(String key) throws BusinessException {
		if (m_busitype.get(key) == null) {
			String sql = "select t.pk_busitype from bd_busitype t where t.busicode = '"
					+ key + "' and isnull(dr,0)=0";
			String info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_busitype.put(key, info);
		}

		return m_busitype.get(key);
	}

	/**
	 * 单据/交易类型
	 * 
	 * @param string
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getBillType(String billcode)
			throws BusinessException {
		if (m_billtype.get(billcode) == null) {
			String sql = "select t.pk_billtypecode,t.pk_billtypeid,t.billtypename "
					+ " from bd_billtype t "
					+ "  where t.pk_billtypecode =  '"
					+ billcode + "'";

			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_billtype.put(billcode, info);

		}
		return m_billtype.get(billcode);
	}

	/**
	 * 自定义项档案对应名称
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public String getDefNameInfo(String key) throws BusinessException {
		if (m_defdoc_name.get(key) == null) {
			String sql = " select name  " + "  from bd_defdoc  "
					+ " where pk_defdoc = '" + key + "'";
			String info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_defdoc_name.put(key, info);
		}
		return m_defdoc_name.get(key);
	}

	/**
	 * 用户信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfo(String usercode)
			throws BusinessException {
		if (m_user.get(usercode) == null) {
			String sql = "select cuserid,user_code from sm_user  where user_code = '"
					+ usercode + "'";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_user.put(usercode, info);
		}
		return m_user.get(usercode);
	}

	/**
	 * 用户信息
	 * 
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getUserInfoByiD(String usercode)
			throws BusinessException {
		if (m_user.get(usercode) == null) {
			String sql = "select user_code,user_name from sm_user  where cuserid = '"
					+ usercode + "'";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_user.put(usercode, info);
		}
		return m_user.get(usercode);
	}

	/**
	 * 财务组织信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrgInfo(String key) throws BusinessException {
		if (m_orgs.get(key) == null) {

			String sql = "select org_orgs.pk_group,org_orgs.code,org_orgs.name,org_orgs.pk_org,org_orgs.pk_vid from org_orgs "
					+ " inner join org_financeorg on org_financeorg.pk_financeorg = org_orgs.pk_org "
					+ " where org_orgs.code = '"
					+ key
					+ "'"
					+ " and org_orgs.enablestate  = 2 "
					+ " and org_orgs.dr  =0 ";

			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_orgs.put(key, info);

		}
		return m_orgs.get(key);
	}

	/**
	 * 财务组织信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getOrgInfoByID(String key)
			throws BusinessException {
		if (m_orgs.get(key) == null) {

			String sql = "select org_orgs.pk_group,org_orgs.code,org_orgs.name,org_orgs.pk_org,org_orgs.pk_vid from org_orgs "
					+ " inner join org_financeorg on org_financeorg.pk_financeorg = org_orgs.pk_org "
					+ " where org_orgs.pk_org = '"
					+ key
					+ "'"
					+ " and org_orgs.enablestate  = 2 "
					+ " and org_orgs.dr  =0 ";

			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_orgs.put(key, info);

		}
		return m_orgs.get(key);
	}

	/**
	 * 自定义档案
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		if (m_defdoc.get(key + "&" + listcode) == null) {
			String sql = "select code, name, memo, pk_defdoc from bd_defdoc where ( bd_defdoc.code = '"
					+ key
					+ "' or bd_defdoc.name = '"
					+ key
					+ "' ) and bd_defdoc.enablestate = 2"
					+ " and bd_defdoc.dr  =0 "
					+ " and pk_defdoclist in(select pk_defdoclist from bd_defdoclist where code = '"
					+ listcode + "')";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_defdoc.put(key + "&" + listcode, info);
		}
		return m_defdoc.get(key + "&" + listcode);
	}

	/**
	 * 供应商/客户档案
	 * 
	 * @param pk_first
	 * @param pk_org
	 * @param pk_group
	 * @param customer
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getCustSupInfo(String code, String pk_org,
			String pk_group) throws BusinessException {
		if (m_custsup.get(code + "&" + pk_org) == null) {
			String sql = "select  code, name, pk_customer, pk_custclass "
					+ " from bd_customer "
					+ " where isnull(dr,0)=0 and  (enablestate = 2) "
					+ " and (((pk_customer in (select pk_customer from bd_custorg where pk_org in ('"
					+ pk_org + "')and enablestate = 2))  or  ((pk_org = '"
					+ pk_group + "' or   pk_org = '" + pk_org + "'))))  "
					+ " and code = '" + code + "'";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_custsup.put(code + "&" + pk_org, info);

		}

		return m_custsup.get(code + "&" + pk_org);
	}

	/**
	 * 结算方式
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getBalatypeKey(String code) throws BusinessException {
		if (m_balatype.get(code) == null) {
			String info = null;
			String sql = "select  pk_balatype " + " from bd_balatype "
					+ " where isnull(dr,0)=0  " + " and code = '" + code + "'";
			info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_balatype.put(code, info);
		}
		return m_balatype.get(code);
	}

	/**
	 * 财务组织信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getSaleOrgInfo(String code)
			throws BusinessException {
		if (m_orgs.get(code) == null) {
			String sql = "select org_orgs.pk_group,org_orgs.code,org_orgs.name,org_orgs.pk_org,org_orgs.pk_vid from org_orgs "
					+ " inner join org_salesorg on org_salesorg.pk_financeorg = org_orgs.pk_org "
					+ " where org_orgs.code = '"
					+ code
					+ "'"
					+ " and org_orgs.enablestate  = 2 "
					+ " and org_orgs.dr  =0 ";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_orgs.put(code, info);

		}
		return m_orgs.get(code);
	}

	/**
	 * 部门信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getDeptInfo(String orgid, String code)
			throws BusinessException {
		String key = orgid + "&" + code;
		if (m_dept.get(key) == null) {
			String sql = "SELECT \n"
					+ "  CODE,\n"
					+ "	NAME,\n"
					+ "	mnecode,\n"
					+ "	pk_dept,\n"
					+ "	pk_fatherorg,\n"
					+ "	displayorder,\n"
					+ "	innercode,\n"
					+ "	pk_vid,\n"
					+ "	pk_org \n"
					+ "FROM\n"
					+ "	org_dept \n"
					+ "WHERE\n"
					+ "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n"
					+ "	AND code = '"
					+ code
					+ "' \n"
					+ "	AND ( ( pk_group = '000112100000000004S0' AND pk_org = '"
					+ orgid + "' ) )";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_dept.put(key, info);

		}
		return m_dept.get(key);
	}

	/**
	 * 部门信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getdeptBypk(String pk_dept)
			throws BusinessException {
		String key = pk_dept;
		String sql = "select code,name from org_dept where pk_dept ='"
				+ pk_dept + "'";
		Map<String, String> info = (Map<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return info;
	}

	/**
	 * 业务员信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	public Map<String, String> getPsnInfo(String code) throws BusinessException {
		String key = code;
		if (m_psn.get(key) == null) {
			String sql = "SELECT DISTINCT\n" + "	bd_psndoc.pk_psndoc\n"
					+ "FROM\n" + "	bd_psndoc \n" + "WHERE\n" + "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n" + "	and( code = '" + code
					+ "' or name = '" + code + "')";
			Map<String, String> info = (Map<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_psn.put(key, info);

		}
		return m_psn.get(key);
	}

	/**
	 * 收支项目
	 * 
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getInOutBusiclass(String code)
			throws DAOException {
		String key = code;
		if (m_inoutbusiclass.get(key) == null) {
			String sql = "SELECT\n" + "	code,\n" + "	name,\n" + "	mnecode,\n"
					+ "	pk_inoutbusiclass,\n" + "	pk_parent \n" + "FROM\n"
					+ "	bd_inoutbusiclass \n" + "WHERE\n" + "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n" + "	AND code = '" + key
					+ "'";
			@SuppressWarnings("unchecked")
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_inoutbusiclass.put(key, info);
		}
		return m_inoutbusiclass.get(key);
	}

	/**
	 * @param code
	 *            个人银行账号
	 * @param pkorg
	 *            人员所在公司
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getPsnbankacc(String code, String pkorg)
			throws DAOException {
		String key = code;
		if (m_psnbankacc.get(key) == null) {
			String sql = "SELECT\n"
					+ "	accnum,\n"
					+ "	accname,\n"
					+ "	pk_bankdoc,\n"
					+ "	pk_banktype,\n"
					+ "	pk_currtype,\n"
					+ "	payacc,\n"
					+ "	isexpenseacc,\n"
					+ "	pk_bankaccsub,\n"
					+ "	pk_bankaccbas,\n"
					+ "	enablestate,\n"
					+ "	pk_org,\n"
					+ "	pk_psndoc \n"
					+ "FROM\n"
					+ "	(\n"
					+ "SELECT\n"
					+ "	bd_bankaccsub.accnum,\n"
					+ "	bd_bankaccsub.accname,\n"
					+ "	pk_bankdoc,\n"
					+ "	pk_banktype,\n"
					+ "	pk_currtype,\n"
					+ "	payacc,\n"
					+ "	isexpenseacc,\n"
					+ "	bd_psnbankacc.pk_bankaccsub pk_bankaccsub,\n"
					+ "	bd_psnbankacc.pk_bankaccbas pk_bankaccbas,\n"
					+ "	enablestate,\n"
					+ "	bd_psnbankacc.pk_org pk_org,\n"
					+ "	pk_psndoc \n"
					+ "FROM\n"
					+ "	bd_bankaccbas,\n"
					+ "	bd_bankaccsub,\n"
					+ "	bd_psnbankacc \n"
					+ "WHERE\n"
					+ "	bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas \n"
					+ "	AND bd_bankaccsub.pk_bankaccsub = bd_psnbankacc.pk_bankaccsub \n"
					+ "	AND bd_bankaccsub.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas \n"
					+ "	AND bd_psnbankacc.pk_bankaccsub != '~' \n"
					+ "	AND bd_bankaccbas.accnum = '" + key + "' \n"
					+ "	) bd_psnbankacctmp \n" + "WHERE\n" + "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n" + "	AND ( pk_org = '"
					+ pkorg + "' ) \n" + "ORDER BY\n" + "	accnum";
			@SuppressWarnings("unchecked")
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_psnbankacc.put(key, info);
		}
		return m_psnbankacc.get(key);
	}

	/**
	 * @param code
	 *            客商银行账号
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getCustbank(String code) throws DAOException {
		String key = code;
		if (m_custbank.get(key) == null) {
			String sql = "SELECT DISTINCT\n"
					+ "	accnum,\n"
					+ "	accname,\n"
					+ "	pk_bankdoc,\n"
					+ "	pk_banktype,\n"
					+ "	pk_currtype,\n"
					+ "	pk_bankaccsub,\n"
					+ "	pk_bankaccbas,\n"
					+ "	enablestate,\n"
					+ "	accountproperty,\n"
					+ "	isinneracc,\n"
					+ "	pk_custbank \n"
					+ "FROM\n"
					+ "	(\n"
					+ "SELECT\n"
					+ "	bd_bankaccsub.accnum,\n"
					+ "	bd_bankaccsub.accname,\n"
					+ "	pk_bankdoc,\n"
					+ "	pk_banktype,\n"
					+ "	pk_currtype,\n"
					+ "	bd_custbank.pk_bankaccsub pk_bankaccsub,\n"
					+ "	bd_custbank.pk_bankaccbas pk_bankaccbas,\n"
					+ "	enablestate,\n"
					+ "	pk_cust,\n"
					+ "	accountproperty,\n"
					+ "	isinneracc,\n"
					+ "	bd_custbank.accclass,\n"
					+ "	pk_custbank \n"
					+ "FROM\n"
					+ "	bd_bankaccbas,\n"
					+ "	bd_bankaccsub,\n"
					+ "	bd_custbank \n"
					+ "WHERE\n"
					+ "	bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas \n"
					+ "	AND bd_bankaccbas.accnum = '"
					+ code
					+ "' \n"
					+ "	AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub \n"
					+ "	AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas \n"
					+ "	AND bd_custbank.pk_bankaccsub != '~' \n"
					+ "	) bd_psnbankacctmp \n"
					+ "WHERE\n"
					+ "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n"
					+ "	AND ( 1 = 0 AND pk_custbank IN ( SELECT min( pk_custbank ) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust ) ) \n"
					+ "ORDER BY\n" + "	accnum";
			@SuppressWarnings("unchecked")
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_custbank.put(key, info);
		}
		return m_custbank.get(key);
	}

	/**
	 * 现金账户
	 * 
	 * @param pk_org
	 *            组织
	 * @return
	 * @throws DAOException
	 */
	public Map<String, String> getCashAccount(String pk_org)
			throws DAOException {
		String key = pk_org;
		if (m_cashaccount.get(key) == null) {
			String sql = "SELECT \n" + "	CODE,\n" + "	NAME,\n" + "	pk_org,\n"
					+ "	pk_moneytype,\n" + "	pk_cashaccount \n" + "FROM\n"
					+ "	bd_cashaccount \n" + "WHERE\n" + "	11 = 11 \n"
					+ "	AND ( enablestate = 2 ) \n" + "	AND ( pk_org = '"
					+ pk_org + "' )";
			@SuppressWarnings("unchecked")
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_cashaccount.put(key, info);
		}
		return m_cashaccount.get(key);
	}

	/**
	 * 币种信息
	 * 
	 * @throws BusinessException
	 */
	public HashMap<String, String> getCurrtypeInfo(String code)
			throws BusinessException {
		if (m_currtype.get(code) == null) {
			String sql = "select b.pk_currtype,b.code,b.name from bd_currtype b where b.code = '"
					+ code + "' and dr = 0";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_currtype.put(code, info);
		}
		return m_currtype.get(code);
	}

	/**
	 * 银行档案信息
	 * 
	 * @throws BusinessException
	 */
	public HashMap<String, String> getBankSubDocInfo(String key)
			throws BusinessException {
		if (m_banksub.get(key) == null) {
			String sql = "select accnum,accname from bd_bankaccsub where pk_bankaccsub='"
					+ key + "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_banksub.put(key, info);
		}
		return m_banksub.get(key);
	}

	/**
	 * 银行档案信息
	 * 
	 * @throws BusinessException
	 */
	public HashMap<String, String> getBankDocInfo(String key)
			throws BusinessException {
		if (m_bankdoc.get(key) == null) {
			String sql = "select name,code from bd_bankdoc where pk_bankdoc ='"
					+ key + "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_bankdoc.put(key, info);
		}
		return m_bankdoc.get(key);
	}

	/**
	 * 资金计划项目信息
	 * 
	 * @throws BusinessException
	 */
	public HashMap<String, String> getfunPlanInfo(String key)
			throws BusinessException {
		if (m_funplan.get(key) == null) {
			String sql = "select name,code from bd_fundplan where pk_fundplan ='"
					+ key + "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_funplan.put(key, info);
		}
		return m_funplan.get(key);
	}

	/**
	 * 项目信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getProjectInfo(String code)
			throws BusinessException {
		if (m_project.get(code) == null) {
			String sql = "select bd_project.pk_project,bd_project.project_code,bd_project.project_name,bd_project.def9,bd_project.def8 from bd_project where nvl(dr,0)=0"
					+ "  and enablestate='2' and bd_project.project_code = '"
					+ code + "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_project.put(code, info);
		}
		return m_project.get(code);
	}

	/**
	 * 项目信息(成本编码,由对照码关联)
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getProject2Info(String code)
			throws BusinessException {
		if (m_project2.get(code) == null) {
			String sql = "select bd_project.pk_project,bd_project.project_code,bd_project.project_name,bd_project.def9,bd_project.def8 from bd_project where nvl(dr,0)=0"
					+ "  and enablestate='2' and bd_project.def1 = '"
					+ code
					+ "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_project2.put(code, info);
		}
		return m_project2.get(code);
	}

	/**
	 * 项目信息(对照码未能读取,则取通过项目编码读取)
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getSpecialProjectInfo(String code)
			throws BusinessException {
		HashMap<String, String> info = getProject2Info(code);
		if (info == null) {
			info = getProjectInfo(code);
		}
		return info;
	}

	/**
	 * 供应商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getSupplierInfo(String code, String pk_org, String pk_group)
			throws BusinessException {
		String key = pk_group + "&" + pk_org + "&" + code;
		if (m_supplier.get(key) == null) {
			StringBuffer sql = new StringBuffer();
			sql.append("select pk_supplier ")
					.append(" from bd_supplier where dr = 0 ")
					.append(" and (enablestate = 2) ")
					.append("  and ((pk_supplier in (select pk_supplier from bd_suporg   where pk_org in ('"
							+ pk_org
							+ "')  and enablestate = 2 union  select pk_supplier   from bd_supplier  where (pk_org = '"
							+ pk_org
							+ "' or   pk_org = '"
							+ pk_group
							+ "'))))  ").append(" and code = '" + code + "' ");
			String info = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
			m_supplier.put(key, info);
		}
		return m_supplier.get(key);
	}

	/**
	 * 会计科目
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getAccSubInfo(String code, String pk_org)
			throws BusinessException {
		String key = pk_org + "&" + code;
		if (m_accsubj.get(code) == null) {
			String sValue = null;
			String whereClause = "pk_relorg = '" + pk_org
					+ "'and accounttype='1' ";
			AccountingBookVO[] glorgs = (AccountingBookVO[]) getBaseDAO()
					.retrieveByClause(nc.vo.org.AccountingBookVO.class,
							whereClause).toArray(new AccountingBookVO[0]);
			if (glorgs != null && glorgs.length == 1) {
				sValue = glorgs[0].getPk_curraccchart();
				whereClause = " nvl(dr,0)=0 and pk_accchart = '" + sValue
						+ "' ";
				whereClause += " and pk_account in (select pk_account from bd_account where nvl(dr,0)=0   and (code='"
						+ code + "' or name ='" + code + "') "
						// +" and pk_accchart='" + sValue + "'"
						+ ")";
				AccAsoaVO[] accs = (AccAsoaVO[]) getBaseDAO().retrieveByClause(
						nc.vo.bd.account.AccAsoaVO.class, whereClause).toArray(
						new AccAsoaVO[0]);
				if (accs != null && accs.length == 1) {
					sValue = accs[0].getPrimaryKey();
				}
				m_accsubj.put(key, sValue);
			}
		}
		return m_accsubj.get(key);
	}

	/**
	 * 客户账户
	 * 
	 * @param pk_cust
	 * @param accnum
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getCustAccnumInfo(String pk_cust,
			String accnum) throws BusinessException {
		String key = pk_cust + "&" + accnum;
		if (m_custaccnum.get(key) == null) {
			StringBuffer query = new StringBuffer();
			query.append("SELECT distinct bd_bankaccsub.accnum,  ");
			query.append("                bd_bankaccsub.pk_bankaccsub,  ");
			query.append("                bd_bankaccsub.accname,  ");
			query.append("                bd_custbank.pk_bankaccsub   AS pk_bankaccsub,  ");
			query.append("                bd_custbank.pk_bankaccbas   AS pk_bankaccbas,  ");
			query.append("                bd_bankdoc.code             AS bankcode,  ");
			query.append("                bd_bankdoc.name             AS bankname  ");
			query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_custbank, bd_bankdoc  ");
			query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
			query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub  ");
			query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas  ");
			query.append("   AND bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc  ");
			query.append("   AND bd_custbank.pk_bankaccsub != '~'  ");
			query.append("   AND (bd_bankaccbas.accclass = 1)  ");
			query.append("   AND bd_bankaccbas.enablestate = 2  ");
			query.append("   AND bd_bankdoc.enablestate = 2  ");
			query.append("   AND (pk_cust = '" + pk_cust + "')  ");
			query.append("   AND bd_bankaccsub.accnum = '" + accnum + "';  ");

			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(query.toString(), new MapProcessor());
			m_custaccnum.put(key, info);
		}
		return m_custaccnum.get(key);
	}

	/**
	 * 供应商账户
	 * 
	 * @param pk_cust
	 * @param accnum
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getSupplierAccnumInfo(String pk_cust,
			String accnum) throws BusinessException {
		String key = pk_cust + "&" + accnum;
		if (m_suppaccnum.get(key) == null) {
			StringBuffer query = new StringBuffer();
			query.append("SELECT distinct bd_bankaccsub.accnum,  ");
			query.append("                bd_bankaccsub.pk_bankaccsub,  ");
			query.append("                bd_bankaccsub.accname,  ");
			query.append("                bd_custbank.pk_bankaccsub   AS pk_bankaccsub,  ");
			query.append("                bd_custbank.pk_bankaccbas   AS pk_bankaccbas,  ");
			query.append("                bd_bankdoc.code             AS bankcode,  ");
			query.append("                bd_bankdoc.name             AS bankname  ");
			query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_custbank, bd_bankdoc  ");
			query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
			query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub  ");
			query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas  ");
			query.append("   AND bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc  ");
			query.append("   AND bd_custbank.pk_bankaccsub != '~'  ");
			query.append("   AND (bd_bankaccbas.accclass in(1,3))  ");
			query.append("   AND bd_bankaccbas.enablestate = 2  ");
			query.append("   AND bd_bankdoc.enablestate = 2  ");
			query.append("   AND (pk_cust = '" + pk_cust + "')  ");
			query.append("   AND bd_bankaccsub.accnum = '" + accnum + "';  ");
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(query.toString(), new MapProcessor());
			m_suppaccnum.put(key, info);
		}
		return m_suppaccnum.get(key);
	}

	/**
	 * 使用权账户
	 * 
	 * @param pk_cust
	 * @param accnum
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getBankaccnumInfo(String pk_org,
			String accnum) throws BusinessException {
		String key = pk_org + "&" + accnum;
		if (m_bankaccnum.get(key) == null) {
			String sql = "SELECT  bd_bankaccsub.accnum, "
					+ " bd_bankaccsub.accname, "
					+ " bd_bankaccsub.pk_bankaccsub AS pk_bankaccsub, "
					+ " bd_bankaccsub.pk_bankaccbas AS pk_bankaccbas,"
					+ " bd_bankdoc.code bankcode,"
					+ " bd_bankdoc.name bankname "
					+ "  from bd_bankaccbas "
					+ " INNER JOIN bd_bankaccsub ON bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
					+ " INNER JOIN bd_bankdoc on bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc "
					+ " where (bd_bankaccbas.accclass = 2)  and bd_bankaccbas.enablestate = 2 and (bd_bankaccsub.acctype = 0 or bd_bankaccsub.acctype = 3) "
					+ " and exists  (select 1  from bd_bankaccuse   where pk_org = '"
					+ pk_org
					+ "'  and bd_bankaccuse. enablestate = 2 and bd_bankaccsub.pk_bankaccsub = bd_bankaccuse.pk_bankaccsub )  "
					+ " AND bd_bankaccsub.accnum ='" + accnum + "' ";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_bankaccnum.put(key, info);
		}
		return m_bankaccnum.get(key);
	}

	/**
	 * 使用权账户
	 * 
	 * @param pk_cust
	 * @param accnum
	 * @return
	 * @throws BusinessException
	 */
	public String getRecpaytypeInfo(String code) throws BusinessException {
		String key = code;
		if (m_recpaytype.get(key) == null) {
			String sql = "select pk_recpaytype  from fi_recpaytype  "
					+ " where  pretype in (0, 1, 2) and (enablestate = 2) "
					+ " AND (code ='" + code + "' or name = '" + code + "')";
			String info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_recpaytype.put(key, info);
		}
		return m_recpaytype.get(key);
	}

	/**
	 * 预算科目
	 * 
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public HashMap<String, String> getBudgetsubInfo(String key)
			throws BusinessException {
		if (m_budgetsub.get(key) == null) {
			String sql = "select tb_budgetsub.pk_obj,tb_budgetsub.objcode,tb_budgetsub.objname from tb_budgetsub where dr = 0 and  tb_budgetsub.objcode = '"
					+ key + "'";
			HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
					.executeQuery(sql, new MapProcessor());
			m_budgetsub.put(key, info);
		}
		return m_budgetsub.get(key);
	}

	/**
	 * 根据传过来的产品线名称生成一条档案
	 * 
	 * @param prodlineNamee
	 * @return
	 */
	public String saveProdlineByname(String prodlineName)
			throws BusinessException {
		ProdLineVO prodlinevo = new ProdLineVO();
		BillcodeGenerater gen = new BillcodeGenerater();
		String prodline = gen.getBillCode("code", InvocationInfoProxy
				.getInstance().getGroupId(), InvocationInfoProxy.getInstance()
				.getGroupId(), null, null);
		prodlinevo.setCode(prodline);
		prodlinevo.setName(prodlineName);
		prodlinevo.setEnablestate(2);
		prodlinevo.setCreator(InvocationInfoProxy.getInstance().getUserId());
		prodlinevo.setCreationtime(new UFDateTime());
		prodlinevo.setDataoriginflag(0);
		prodlinevo.setPk_group("000112100000000005FD");
		prodlinevo.setPk_org("000112100000000005FD");

		return getBaseDAO().insertVO(prodlinevo);

	}

	/**
	 * 读取付款合同信息
	 * 
	 * @param contcode
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getPayContInfo(String contcode)
			throws BusinessException {
		String sql = "select t.vbillcode contcode,  t.ctname contname,t.contype conttype,t.condetails contcell, t.accountorg org,t.Cvendorid  supplier,t.def81 conttypeid,t.def82 contcellid"
				+ " from fct_ap t where t.vbillcode = '"
				+ contcode
				+ "'  and t.blatest = 'Y' and nvl(dr,0)=0";
		HashMap<String, String> info = (HashMap<String, String>) getBaseDAO()
				.executeQuery(sql, new MapProcessor());
		return info;
	}

	/**
	 * 读取物料分类
	 * 
	 * @param contcode
	 * @return
	 * @throws BusinessException
	 */
	public String getMaterielTypeInfo(String code) throws BusinessException {
		if (m_materieltype.get(code) == null) {
			String sql = "select pk_marbasclass from bd_marbasclass t "
					+ "  where t.code = '" + code + "'and nvl(dr,0)=0";
			String info = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			m_materieltype.put(code, info);
		}
		return m_materieltype.get(code);
	}
}
