package nc.bs.tg.outside.gathering;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.tg.outside.utils.BillUtils;
import nc.itf.bd.bankacc.cust.ICustBankaccService;
import nc.itf.bd.bankdoc.IBankdocQueryService;
import nc.itf.bd.cust.assign.ICustAssignService;
import nc.itf.bd.cust.baseinfo.ICustBaseInfoService;
import nc.itf.bd.cust.baseinfo.ICustSupplierService;
import nc.itf.bd.supplier.assign.ISupplierAssignService;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pub.billcode.itf.IBillcodeManage;
import nc.vo.arap.gathering.AggGatheringBillVO;
import nc.vo.arap.gathering.GatheringBillVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.bd.bankaccount.BankAccSubVO;
import nc.vo.bd.bankaccount.BankAccbasVO;
import nc.vo.bd.bankaccount.cust.CustBankaccUnionVO;
import nc.vo.bd.bankdoc.BankdocVO;
import nc.vo.bd.cust.CustbankVO;
import nc.vo.bd.cust.CustomerVO;
import nc.vo.bd.pub.IPubEnumConst;
import nc.vo.bd.supplier.SupplierVO;
import nc.vo.org.OrgVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.tg.outside.GatheringBodyVO;
import nc.vo.tg.outside.GatheringHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class GatheringUtils extends BillUtils implements ITGSyncService {
	public static final String DefaultOperator = "LLWYSF";// 默认制单人
	static GatheringUtils utils;
	String wysfUserid = null;// 物业收费系统操作用户

	public static GatheringUtils getUtils() {
		if (utils == null) {
			utils = new GatheringUtils();
		}
		return utils;
	}

	private ICustAssignService custOrgService = null;

	private ICustAssignService getCustOrgService() {
		if (custOrgService == null) {
			custOrgService = NCLocator.getInstance().lookup(
					ICustAssignService.class);
		}
		return custOrgService;
	}

	private ISupplierAssignService supplierOrgService = null;

	private ISupplierAssignService getSupplierOrgService() {
		if (supplierOrgService == null) {
			supplierOrgService = NCLocator.getInstance().lookup(
					ISupplierAssignService.class);
		}
		return supplierOrgService;
	}

	/**
	 * 收款单
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException {
		// 设置组id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// 设置用户数据
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// 设置用户id
		InvocationInfoProxy.getInstance().setUserId(
				getUserIDByCode(DefaultOperator));
		// 设置用户码
		InvocationInfoProxy.getInstance().setUserCode(DefaultOperator);
		// 处理表单信息
		JSONObject jsonData = (JSONObject) info.get("data");// 表单数据
		String jsonhead = jsonData.getString("headInfo");// 外系统来源表头数据
		String jsonbody = jsonData.getString("bodyInfo");// 外系统来源表体数据
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("表单数据为空，请检查！json:" + jsonData);
		}
		// 转换json
		GatheringHeadVO headVO = JSONObject.parseObject(jsonhead,
				GatheringHeadVO.class);
		List<GatheringBodyVO> bodyVOs = JSONObject.parseArray(jsonbody,
				GatheringBodyVO.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("表单数据转换失败，请检查！json:" + jsonData);
		}

		String srcid = headVO.getSrcid();// 外系统业务单据ID
		String srcno = headVO.getSrcbillno();// 外系统业务单据单据号
		Map<String, String> resultInfo = new HashMap<String, String>();

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		try {
			AggGatheringBillVO aggVO = (AggGatheringBillVO) getBillVO(
					AggGatheringBillVO.class, "isnull(dr,0)=0 and def1 = '"
							+ srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("【物业收费系统单据号:"
						+ srcno
						+ "】,【物业收费系统单据主键:"
						+ srcid
						+ "】NC已存在对应的业务单据【"
						+ aggVO.getParentVO().getAttributeValue(
								GatheringBillVO.BILLNO) + "】,请勿重复上传!");
			}

			AggGatheringBillVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,
					PfUtilBaseTools.PARAM_NOTE_CHECKED);

			String pk_tradetype = (String) billvo.getParentVO()
					.getAttributeValue("pk_tradetype");

			WorkflownoteVO worknoteVO = NCLocator.getInstance()
					.lookup(IWorkflowMachine.class)
					.checkWorkFlow("SAVE", pk_tradetype, billvo, eParam);

			Object obj = (AggGatheringBillVO[]) getPfBusiAction()
					.processAction("SAVE", "F2", worknoteVO, billvo, null,
							eParam);
			AggGatheringBillVO[] billvos = (AggGatheringBillVO[]) obj;
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO()
					.getAttributeValue(ReceivableBillVO.BILLNO));
		} catch (Exception e) {
			throw new BusinessException("【" + billkey + "】," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}

		return JSON.toJSONString(resultInfo);
	}

	protected AggGatheringBillVO onTranBill(GatheringHeadVO headVO,
			List<GatheringBodyVO> bodyVOs) throws BusinessException {
		return null;
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getUserPkByCode(String code) {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据编码或名称找客商
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getcustomer(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select pk_customer from bd_customer where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * 根据编码或名称找客商名称
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getCustomerName(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		String name = (String) dao.executeQuery(
				"select name from bd_customer where code='" + code
						+ "'  and nvl(dr,0)=0", new ColumnProcessor());
		return name;
	}

	/**
	 * 根据【公司编码】获取财务组织
	 * 
	 * @param code
	 * @return
	 * @throws Exception
	 */
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
	 * 根据银行账户编码读取对应主键
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getAccountIDByCode(String recaccount, String pk_org)
			throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub  ");
		query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_custbank  ");
		query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub  ");
		query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas  ");
		query.append("   AND bd_custbank.pk_bankaccsub != '~'  ");
		query.append("   AND bd_bankaccsub.Accnum = '" + recaccount + "'  ");
		query.append("   AND exists  ");
		query.append(" (select 1  ");
		query.append("          from bd_bankaccbas bas  ");
		query.append("         where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  ");
		query.append("           and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y'))  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_org = '" + pk_org + "' and  ");
		query.append("       pk_custbank IN (SELECT min(pk_custbank)  ");
		query.append("                          FROM bd_custbank  ");
		query.append("                         GROUP BY pk_bankaccsub, pk_cust));  ");

		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 预算科目查询
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("unchecked")
	protected List<Map<String, String>> getBudgetsubNameByCode(String itemcode)
			throws BusinessException {
		List<Map<String, String>> result = null;
		StringBuffer query = new StringBuffer();
		// query.append("select t.objname as itemname, t1.objname as itemtype  ");
		// query.append("  from tb_budgetsub t  ");
		// query.append(" inner join tb_budgetsub t1  ");
		// query.append("    on t.pk_parent = t1.pk_obj  ");
		// query.append(" where t.objcode = '" + itemcode + "'  ");
		// query.append("   and nvl(t.dr, 0) = 0  ");
		// query.append("   and t.enablestate = 2  ");

		query.append("select pk_defdoc,pid from bd_defdoc where def1 = '"
				+ itemcode + "'");
		try {
			result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					query.toString(), new MapListProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 客户基本信息保存类
	 * 
	 * @param value
	 * @param dectype
	 * @param srcsystem
	 * @param srctype
	 * @return
	 * @throws BusinessException
	 */
	public String[] onBasicBill(OrgVO orgvo, GatheringBodyVO gatheringBodyVO)
			throws BusinessException {
		String pk_org = orgvo.getPk_org();
		String bodycustomer = gatheringBodyVO.getCustomer();
		String recaccount = gatheringBodyVO.getRecaccount();
		String bankofdeposit = gatheringBodyVO.getBankofdeposit();
		if (bodycustomer == null) {
			throw new BusinessException("客户customer不能为空，请检查参数设置");
		}

		// 供应商（客户，客商）
		String customer = getcustomer(bodycustomer);
		/**
		 * 如果传过来的客户在NC找不到就新增客户和供应商档案
		 */
		if (customer == null || "".equals(customer)) {
			if (!"".equals(recaccount) && recaccount != null) {
				// 银行账户在客户或者供应商范围内唯一
				String accname = isCustBankAccUnique(recaccount);
				if (null != accname && !"".equals(accname)) {
					throw new BusinessException("NC客户:【" + accname
							+ "】下已存在该银行账户【" + recaccount + "】不能在customer【"
							+ bodycustomer + "】下新建一样的银行账户!");
				}

			}

			CustomerVO custvo2;

			String pk_timezone = getStringvalue("bd_timezone", "pk_timezone",
					"name", "北京时间(UTC+08:00)");
			String pk_custclass = getcustomerClass();
			String pk_currtype = getpk_currtype();
			String pk_format = getStringvalue("bd_formatdoc", "pk_formatdoc",
					"name", "中文简体");
			String pk_country = getStringvalue("bd_countryzone", "pk_country ",
					"name", "中国");

			CustomerVO custvo = new CustomerVO();

			custvo.setAttributeValue(CustomerVO.NAME, bodycustomer);
			custvo.setAttributeValue(CustomerVO.PK_TIMEZONE, pk_timezone);// 北京时间(UTC+08:00)"0001Z010000000079U2P"
			custvo.setAttributeValue(CustomerVO.PK_CUSTCLASS, pk_custclass);// 客户基本分类
			custvo.setAttributeValue(CustomerVO.ENABLESTATE, Integer.valueOf(2));
			custvo.setAttributeValue(CustomerVO.PK_FORMAT, pk_format);// 数据格式--中文简体
			custvo.setAttributeValue(CustomerVO.PK_CURRTYPE, pk_currtype);// 币种
			custvo.setAttributeValue(CustomerVO.PK_COUNTRY, pk_country);// 国家--中国
			custvo.setAttributeValue(CustomerVO.PK_GROUP,
					"000112100000000005FD");// 集团
			custvo.setAttributeValue(CustomerVO.PK_ORG, "000112100000000005FD");// 组织
			custvo.setAttributeValue(CustomerVO.CUSTPROP, 0);// 客户类型
			custvo.setAttributeValue(CustomerVO.DEF1, "LLWYSF");// 来源系统类型
			custvo.setAttributeValue(CustomerVO.DEF2, recaccount);
			IBillcodeManage codeManage = NCLocator.getInstance().lookup(
					IBillcodeManage.class);
			// 根据编码规则创建客商编码
			String code = codeManage.getBillCode_RequiresNew("customer",
					pk_org, pk_org, custvo);
			custvo.setAttributeValue(CustomerVO.CODE, code);
			ICustBaseInfoService service = NCLocator.getInstance().lookup(
					ICustBaseInfoService.class);
			custvo2 = service.insertCustomerVO(custvo, false);
			if (custvo2 != null) {
				SupplierVO supvo = new SupplierVO();
				supvo.setAttributeValue(SupplierVO.PK_SUPPLIER,
						custvo.getPk_customer());
				supvo.setAttributeValue(SupplierVO.PK_ORG, custvo2.getPk_org());// 组织
				supvo.setAttributeValue(SupplierVO.PK_GROUP,
						custvo2.getPk_group());// 集团
				supvo.setAttributeValue(SupplierVO.CODE, code);// 编码
				supvo.setAttributeValue(SupplierVO.PK_CURRTYPE, pk_currtype);
				supvo.setAttributeValue(SupplierVO.NAME,
						custvo.getAttributeValue(CustomerVO.NAME));// 名称
				supvo.setAttributeValue(SupplierVO.ISCUSTOMER, UFBoolean.FALSE);// 是否客户
				supvo.setAttributeValue(SupplierVO.PK_COUNTRY, pk_country);// 国家地区
				supvo.setAttributeValue(SupplierVO.PK_TIMEZONE, pk_timezone);// 时区
				supvo.setAttributeValue(SupplierVO.PK_SUPPLIERCLASS,
						getpk_supplierclass());
				supvo.setAttributeValue(SupplierVO.PK_FORMAT, pk_format);// 数据格式
				supvo.setAttributeValue(SupplierVO.SUPPROP,
						custvo.getCustprop());// 客户类型
				supvo.setStatus(VOStatus.NEW);
				supvo.setDef1("LLWYSF");// 来源系统类型
				supvo.setDef2(recaccount);// 银行账号
				NCLocator.getInstance().lookup(ICustSupplierService.class)
						.insertSupAndRelaToCust(supvo, custvo2);

				customer = custvo2.getPk_supplier();
				// 分配财务组织
				custvo2.setPk_org(orgvo.getPk_org());
				getCustOrgService().assignCustomerToSelfOrg(custvo2);
				supvo.setPk_org(orgvo.getPk_org());
				getSupplierOrgService().assignSupplierToSelfOrg(supvo);
			}
		}

		String pk_bankaccbas = null;
		String payaccount = null;
		if (!"".equals(recaccount) && recaccount != null) {
			String org_bankaccsub = getAccinfoByOrg(recaccount, pk_org);
			String pk_bankaccsub = getAccinfo(customer, recaccount);
			if (org_bankaccsub != null) {
				pk_bankaccbas = org_bankaccsub;
			} else {
				if (pk_bankaccsub != null) {
					payaccount = pk_bankaccsub;
				}

			}

			if (pk_bankaccsub == null && org_bankaccsub == null) {
				BankAccbasVO bankaccbasVO = new BankAccbasVO();
				BankdocVO bankdoc = getIBankdoc(bankofdeposit);
				if (bankdoc != null) {
					bankaccbasVO.setAccattribute(1);// 账户属性
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);// 启用状态
					bankaccbasVO.setStatus(VOStatus.NEW);
					bankaccbasVO.setAccclass(Integer.valueOf(1));// 账户分类
					bankaccbasVO
							.setAccname(getCustomerName(bodycustomer) != null ? getCustomerName(bodycustomer)
									: bodycustomer);// 户名
					bankaccbasVO.setAccnum(recaccount);// 账号
					bankaccbasVO.setAccopendate(new UFDate());
					bankaccbasVO.setAccountproperty(Integer.valueOf(1));// 账户性质
					bankaccbasVO.setAccstate(Integer.valueOf(0));// 账户状态
					bankaccbasVO.setBankarea(bankdoc.getBankarea());// 开户地区
					bankaccbasVO.setCity(bankdoc.getCity());// 城市
					bankaccbasVO.setCombinenum(bankdoc.getCombinenum());// 联行号
					bankaccbasVO.setDataoriginflag(Integer.valueOf(0));// 分布式
					bankaccbasVO
							.setEnablestate(IPubEnumConst.ENABLESTATE_ENABLE);
					bankaccbasVO.setEnabletime(new UFDateTime());
					bankaccbasVO.setEnableuser("#UAP#");
					bankaccbasVO.setPk_bankdoc(bankdoc.getPk_bankdoc());// 开户银行
					bankaccbasVO.setPk_banktype(bankdoc.getPk_banktype());// 银行类别
					bankaccbasVO.setPk_group(orgvo.getPk_group());// 所属集团
					bankaccbasVO.setPk_org(orgvo.getPk_org());// 所属组织
					bankaccbasVO.setProvince(bankdoc.getProvince());
					bankaccbasVO.setQrybalanceitf(Integer.valueOf(0));
					bankaccbasVO.setSharetag(UFBoolean.FALSE);
					bankaccbasVO.setAreacode(bankdoc.getAreacode());// 地区代码
					bankaccbasVO.setCombineaccnum(bankdoc.getPcombinenum());// 人行联行号
					bankaccbasVO.setCombineaccname(bankdoc.getPcombinename());// 人行联行名称
					bankaccbasVO.setOrgnumber(bankdoc.getOrgnumber());// 机构号/分行号
					bankaccbasVO.setCustomernumber(bankdoc.getCustomernumber());// 客户编号
					bankaccbasVO.setIssigned(bankdoc.getIssigned());// 签约
					// 客商银行子账户
					BankAccSubVO subvo = new BankAccSubVO();
					subvo.setCode(recaccount);
					subvo.setName(gatheringBodyVO.getRecaccountname());
					subvo.setAccname(getCustomerName(bodycustomer) != null ? getCustomerName(bodycustomer)
							: bodycustomer);
					subvo.setAccnum(recaccount);
					subvo.setPk_currtype(getpk_currtype());
					subvo.setStatus(VOStatus.NEW);
					bankaccbasVO.setBankaccsub(new BankAccSubVO[] { subvo });
					// 客户银行账户
					CustbankVO custbankVO = new CustbankVO();
					custbankVO.setAccclass(Integer.valueOf(1));// 账户分类
					custbankVO.setPk_cust(customer);
					custbankVO.setDataoriginflag(Integer.valueOf(0));// 分布式
					CustBankaccUnionVO banaccUnionvo = new CustBankaccUnionVO();
					bankaccbasVO.setStatus(VOStatus.NEW);
					banaccUnionvo.setBankaccbasVO(bankaccbasVO);
					banaccUnionvo.setCustbankVO(custbankVO);
					ICustBankaccService custservice = NCLocator.getInstance()
							.lookup(ICustBankaccService.class);
					CustBankaccUnionVO newUnionvo = custservice
							.insertCustBankacc(banaccUnionvo);
					newUnionvo.getBankaccbasVO().setEnablestate(
							IPubEnumConst.ENABLESTATE_ENABLE);
					newUnionvo.getBankaccbasVO()
							.setEnabletime(new UFDateTime());
					newUnionvo.getBankaccbasVO().setEnableuser(
							"NC_USER0000000000000");
					custservice.enableCustBankacc(newUnionvo);
					payaccount = subvo.getPk_bankaccsub();
				} else {
					throw new BusinessException("该开户行并未在NC中关联");
				}
			}

		}

		String[] result = new String[3];
		result[0] = customer;
		result[1] = pk_bankaccbas;
		result[2] = payaccount;
		return result;
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

	public String getcustomerClass() throws BusinessException {

		BaseDAO dao = new BaseDAO();
		Object obj = dao
				.executeQuery(
						"select pk_custclass from bd_custclass where name='外部客户' and nvl(dr,0)=0",
						new ColumnProcessor());
		return (String) obj;
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

	public String getpk_supplierclass() throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao
				.executeQuery(
						"select pk_supplierclass from bd_supplierclass where name='外部供应商'",
						new ColumnProcessor());
		return (String) obj;
	}

	public String getAccinfo(String customer, String accnum) {
		String result = null;
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select bd_bankaccsub.pk_bankaccsub  ");
		query.append("  from bd_customer  ");
		query.append("  left join bd_custbank  ");
		query.append("    on bd_custbank.pk_cust = bd_customer.pk_customer  ");
		query.append("  left join bd_bankaccbas  ");
		query.append("    on bd_bankaccbas.pk_bankaccbas = bd_custbank.pk_bankaccbas  ");
		query.append("  left join bd_bankaccsub  ");
		query.append("    on bd_bankaccsub.pk_bankaccbas = bd_bankaccbas.pk_bankaccbas  ");
		query.append(" where nvl(bd_bankaccsub.dr, 0) = 0  ");
		query.append("   and nvl(bd_custbank.dr, 0) = 0  ");
		query.append("   and nvl(bd_customer.dr, 0) = 0  ");
		query.append("   and bd_custbank.accclass = '1'  ");
		query.append("   and bd_bankaccsub.accnum = '" + accnum + "'  ");
		query.append("   and (bd_customer.code = '" + customer + "' or  ");
		query.append("        bd_customer.pk_customer = '" + customer + "')  ");
		query.append("   and bd_bankaccbas.enablestate = 2;  ");

		try {
			result = (String) dao.executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return result;
	}

	public boolean isNotNull(String value) {
		return value != null && !"".equals(value) && value.trim().length() > 0;
	}

	public BankdocVO getIBankdoc(String bankcode) {
		if (isNotNull(bankcode)) {
			IBankdocQueryService service = NCLocator.getInstance().lookup(
					IBankdocQueryService.class);
			try {
				BankdocVO VOs = getBankdocBillVO(BankdocVO.class,
						"nvl(dr,0)=0 and (code ='" + bankcode + "' or name='"
								+ bankcode + "')");
				if (VOs != null) {
					return VOs;
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage());
			}
		}
		return null;
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
	public BankdocVO getBankdocBillVO(Class c, String whereCondStr)
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
	 * 根据公司查询银行账户-2020-11-20-谈子健
	 */
	public String getAccinfoByOrg(String accnum, String pk_org) {
		String result = null;
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_bankaccsub.pk_bankaccsub AS pk_bankaccsub");
		query.append("  from bd_bankaccbas  ");
		query.append(" INNER JOIN bd_bankaccsub  ");
		query.append("    ON bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append(" INNER JOIN bd_bankdoc  ");
		query.append("    on bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc  ");
		query.append(" INNER JOIN bd_banktype  ");
		query.append("    on bd_banktype.pk_banktype = bd_bankaccbas.pk_banktype  ");
		query.append(" INNER JOIN org_orgs  ");
		query.append("    on org_orgs.pk_org = bd_bankaccbas.pk_org  ");
		query.append(" where bd_bankaccbas.enablestate = 2  ");
		query.append("   and nvl(bd_bankaccbas.dr, 0) = 0  ");
		query.append("   and nvl(bd_bankaccsub.dr, 0) = 0  ");
		query.append("   and nvl(bd_bankdoc.dr, 0) = 0  ");
		query.append("   and nvl(bd_banktype.dr, 0) = 0  ");
		query.append("   and bd_bankaccbas.accclass = 2  ");
		query.append("   and (org_orgs.pk_org in  ");
		query.append("       (select org_orgs.pk_org  ");
		query.append("           from org_orgs  ");
		query.append("          start with pk_org in  ");
		query.append("                     (select pk_org from org_orgs where code in ('LL1016'))  ");
		query.append("         connect by prior pk_org = pk_fatherorg  ");
		query.append("                and nvl(dr, 0) = 0))  ");
		query.append("   and bd_bankaccsub.accnum = '" + accnum + "'  ");
		query.append("   and org_orgs.pk_org = '" + pk_org + "'  ");
		query.append("   and bd_bankaccbas.enablestate = 2;  ");

		try {
			result = (String) dao.executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (BusinessException e) {
			Logger.error(e.getMessage());
		}
		return result;
	}

	// 银行账户在客户或者供应商范围内唯一
	private String isCustBankAccUnique(String recaccount)
			throws BusinessException {
		String result = null;
		BaseDAO dao = new BaseDAO();
		StringBuffer query = new StringBuffer();
		query.append("select bd_bankaccbas.accname  ");
		query.append("  from bd_bankaccbas  ");
		query.append(" where accnum = '" + recaccount + "'  ");
		query.append("   and accclass = 1  ");
		result = (String) dao.executeQuery(query.toString(),
				new ColumnProcessor());
		return result;

	}

}
