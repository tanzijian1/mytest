package nc.bs.tg.outside.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.bs.logging.Logger;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.pf.pub.PfDataCache;
import nc.itf.bd.defdoc.IDefdocService;
import nc.itf.uap.pf.IPFConfig;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.itf.uap.pf.IplatFormEntry;
import nc.jdbc.framework.processor.BeanProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.pubitf.arap.bill.IArapBillPubQueryService;
import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.prodline.ProdLineVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.org.OrgVO;
import nc.vo.pf.pub.util.SQLUtil;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.uap.pf.PFBusinessException;
import nc.vo.wfengine.core.activity.Activity;
import nc.vo.wfengine.core.parser.XPDLParserException;
import nc.vo.wfengine.core.workflow.WorkflowProcess;
import nc.vo.wfengine.pub.WFTask;
import nc.vo.wfengine.pub.WfTaskType;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

public class BillUtils {
	static BillUtils utils;
	public static final String DataSourceName = "design";// 接口默认数据源
	public static final String STATUS_SUCCESS = "1";// 成功
	public static final String STATUS_FAILED = "0";// 失败
	public static final String DefaultOperator = "EBS";// 默认制单人
	public static final String DefaultGroup = "0001";// 默认集团/时代集团
	private String secretkey = null;// 密钥
	private ISecurityTokenCallback sc;
	private IMDPersistenceQueryService mdQryService = null;
	private BaseDAO baseDAO = null;
	private IplatFormEntry pfBusiAction = null;
	private IArapBillPubQueryService arapBillPubQueryService = null;
	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//
	private IWorkflowMachine workflowMachine = null;// 流程平台调用的工作流机服务

	public static BillUtils getUtils() {
		if (utils == null) {
			utils = new BillUtils();
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
	 * 流程交互类
	 * 
	 * @return
	 */
	public IplatFormEntry getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IplatFormEntry.class);
		}
		return pfBusiAction;
	}

	/**
	 * 检验密钥是否通过
	 * 
	 * @param token
	 * @param srcsytem
	 * @param key
	 * @return
	 * @throws BusinessException
	 */
	public boolean verifyToken(String token, String srcsytem)
			throws BusinessException {
		String key = getSecretkey(srcsytem);
		if (StringUtils.isBlank(key)) {
			throw new BusinessException("对外交互接口密钥未维护,请联系系统管理员");
		}
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMddHHmm");
		String strdate = sd.format(new UFDate().toDate());
		String encodeStr = DigestUtils.md5Hex(srcsytem + key + strdate);
		if (encodeStr.equals(token)) {
			return true;
		} else {
			encodeStr = DigestUtils.md5Hex(srcsytem + key);
			if (encodeStr.equals(token)) {
				return true;
			}
		}
		return false;
	}

	public String getSecretkey(String srcsytem) throws BusinessException {
		if (secretkey == null) {
			Map<String, String> info = TGOutsideUtils.getUtils()
					.getReleaseImplInfo(srcsytem, "Secretkey");
			if (info != null) {
				secretkey = info.get("classname");
			}
		}
		return secretkey;
	}

	/**
	 * 将where值转换为SQL条件
	 * 
	 * @param where
	 * @return
	 */
	public static String onConvertSQLCondition(String fieldname, String where) {
		String sqlwhere = "";
		if (where != null && !"".equals(where)) {
			String[] fieldvalues = where.split(",");
			sqlwhere = SQLUtil.buildSqlForIn(fieldname, fieldvalues);
		}
		return sqlwhere;
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
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}

	/**
	 * 根据【用户编码】获取主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getUserIDByCode(String code) throws BusinessException {
		String sql = "select cuserid  from sm_user where nvl(dr,0)=0 and user_code='"
				+ code + "'";
		String result = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return result;
	}

	public IArapBillPubQueryService getArapBillPubQueryService() {
		if (arapBillPubQueryService == null) {
			arapBillPubQueryService = NCLocator.getInstance().lookup(
					IArapBillPubQueryService.class);
		}
		return arapBillPubQueryService;
	}

	/**
	 * 根据【交易类型编码】获取主键
	 * 
	 * @param code
	 * @return
	 */

	public String getBillTypePkByCode(String code, String pk_group) {
		String sql = "select pk_billtypeid from bd_billtype where nvl(dr,0)=0 and PK_BILLTYPECODE='"
				+ code + "' and pk_group='" + pk_group + "'";
		String pk_billtypeid = null;
		try {
			pk_billtypeid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_billtypeid != null) {
				return pk_billtypeid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 得到默认经办人部门
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOperator_dept(String code, String pk_org) throws Exception {
		Object pk_dept = getBaseDAO()
				.executeQuery(
						"select pk_dept  from org_dept where pk_group = '000112100000000005FD' and code='"
								+ code + "' and pk_org = '" + pk_org + "' ",
						new ColumnProcessor());
		return (String) pk_dept;
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
	 * 根据【人员编码】获取主键
	 * 
	 * @param code
	 * @return
	 */
	public String getPsndocPkByCode(String code) {
		String sql = "select bd_psndoc.pk_psndoc from bd_psndoc "
				+ "  where nvl(bd_psndoc.dr,0)=0 and bd_psndoc.code ='" + code
				+ "'";
		String pk = null;
		try {
			pk = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
			if (pk != null && !pk.equals("")) {
				return pk;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取车牌档案
	 * 
	 * @param name
	 * @return
	 */
	public String getcarnum(String name) {
		String sql = "select pk_defdoc from bd_defdoc where (code='"
				+ name
				+ "' or pk_defdoc='"
				+ name
				+ "') and  pk_defdoclist=(select pk_defdoclist from bd_defdoclist where code='ys008')";
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
	 * 得到默认经办人pk
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getopertor(String psncode) throws BusinessException {
		Object pk_base_doc = getBaseDAO()
				.executeQuery(
						"select pk_psndoc from bd_psndoc where code='"
								+ psncode + "' ", new ColumnProcessor());
		if (pk_base_doc == null) {
			throw new BusinessException("经办人为空");
		}
		return (String) pk_base_doc;
	}

	public IWorkflowMachine getWorkflowMachine() {
		if (workflowMachine == null) {
			workflowMachine = NCLocator.getInstance().lookup(
					IWorkflowMachine.class);
		}
		return workflowMachine;
	}

	/**
	 * 得到默认经办人单位
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getopertor_org() throws Exception {
		Object pk_org = getBaseDAO()
				.executeQuery(
						"select  pk_org  from  bd_psndoc where pk_psndoc =(select pk_base_doc  from sm_user where user_code='TY01') ",
						new ColumnProcessor());
		return (String) pk_org;
	}

	/**
	 * 默认费用承担部门编码
	 * 
	 * @param org
	 * @return
	 * @throws Exception
	 */
	public String getassume_dept(String org) throws Exception {
		Object assume_dept = getBaseDAO().executeQuery(
				"select * from org_dept where pk_group = '000112100000000005FD' and pk_org = '"
						+ org + "' and code='12'", new ColumnProcessor());

		return (String) assume_dept;
	}

	/**
	 * 得到默认经办人部门
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getOperator_dept() throws Exception {
		Object pk_dept = getBaseDAO()
				.executeQuery(
						"select pk_dept  from org_dept where pk_group = '000112100000000005FD' and code='12' and pk_org = '"
								+ getopertor_org() + "' ",
						new ColumnProcessor());
		return (String) pk_dept;
	}

	/**
	 * 得到默认经办人pk
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getopertor() throws BusinessException {
		Object pk_base_doc = getBaseDAO().executeQuery(
				"select pk_base_doc from sm_user where user_code='TY01' ",
				new ColumnProcessor());
		if (pk_base_doc == null) {
			throw new BusinessException("默认经办人为空");
		}
		return (String) pk_base_doc;
	}

	/**
	 * 根据单据id找到审批流程为制单人的审批流程id
	 * 
	 * @param billid
	 * @return
	 * @throws BusinessException
	 * @throws XPDLParserException
	 */
	public String getCreatorActiveid(WorkflownoteVO worknoteVO)
			throws Exception {

		WFTask task = worknoteVO.getTaskInfo().getTask();
		WorkflowProcess process = PfDataCache.getWorkflowProcess(task
				.getWfProcessDefPK());
		ArrayList<Activity> array = (ArrayList<Activity>) process
				.getActivities();
		String creatorActid = null;
		for (Activity act : array) {
			if (act.getMultiLangName().toString().contains("制单人")) {
				creatorActid = act.getId();
				break;
			}
		}
		return creatorActid;
	}

	/**
	 * 
	 * 2020-08-20-谈子健
	 * 
	 * @param 自定义档案列表code
	 * @param docCode
	 *            自定义档案code
	 * @return
	 * @throws BusinessException
	 */
	public String getdefdocBycode(String code, String docCode)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist c, bd_defdoc d  ");
		query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
		query.append("   and c.code = '" + docCode + "'  ");
		query.append("   and d.code = '" + code + "'  ");
		query.append("   and nvl(c.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and d.enablestate = '2'  ");
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 结算方式查询 2020-08-20-谈子健
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getBalatypePkByCode(String balatype) {
		String result = null;
		String sql = "select  pk_balatype from bd_balatype where dr = 0 and code ='"
				+ balatype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据银行账户编码读取对应主键
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getAccountIDByCode(String account, String pk_receiver,
			String condition) throws BusinessException {
		String result = null;
		String sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = '"
				+ account
				+ "' "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and ("
				+ condition
				+ " = '"
				+ pk_receiver
				+ "' and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust)) ";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 后台审批某张单据
	 * 
	 * @param billType
	 * @param billId
	 * @param checkResult
	 * @param checkNote
	 * @param checkman
	 * @param dispatched_ids
	 * @return
	 * @throws Exception
	 */
	public static Object approveSilently(String billType, String billId,
			String checkResult, String checkNote, String checkman,
			boolean isBackToFirst) throws Exception {
		Logger.debug("******进入PfUtilTools.approveSilently方法*************************");
		Logger.debug("* billType=" + billType);
		Logger.debug("* billId=" + billId);
		Logger.debug("* checkResult=" + checkResult);
		Logger.debug("* checkNote=" + checkNote);
		Logger.debug("* checkman=" + checkman);

		// 1.获得单据聚合VO
		IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(
				IPFConfig.class.getName());
		AggregatedValueObject billVo = bsConfig.queryBillDataVO(billType,
				billId);
		if (billVo == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("busitype", "busitypehint-000063")/*
																 * 错误：
																 * 根据单据类型和单据ID获取不到单据聚合VO
																 */);

		// 2.获得工作项并设置审批意见
		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator
				.getInstance().lookup(IWorkflowMachine.class.getName());
		HashMap hmPfExParams = new HashMap();
		hmPfExParams.put("nolockandconsist", true);

		WorkflownoteVO worknoteVO = bsWorkflow.checkWorkFlow(
				IPFActionName.APPROVE + checkman, billType, billVo,
				hmPfExParams);
		if (worknoteVO != null) {
			worknoteVO.setChecknote(checkNote);
			// 获取审批结果-通过/不通过/驳回
			if ("Y".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("Y");
			} else if ("N".equalsIgnoreCase(checkResult)) {
				worknoteVO.setApproveresult("N");
			} else if ("R".equalsIgnoreCase(checkResult)) {
				worknoteVO.getTaskInfo().getTask()
						.setTaskType(WfTaskType.Backward.getIntValue());
				if (isBackToFirst) {
					worknoteVO.getTaskInfo().getTask()
							.setBackToFirstActivity(isBackToFirst);
				} else {
					WorkflowProcess process = PfDataCache
							.getWorkflowProcess(worknoteVO.getTaskInfo()
									.getTask().getWfProcessDefPK());
					List<Activity> activityList = process.getActivities();
					Activity activity = process.findActivityByID(worknoteVO
							.getTaskInfo().getTask().getActivityID());
					if (activityList != null && activityList.size() > 0) {
						String jumpToActivity = null;
						S1: for (int i$ = 0; i$ < activityList.size(); i$++) {
							if (activity.getId().equals(
									activityList.get(i$).getId())) {
								break S1;
							}
							jumpToActivity = activityList.get(i$).getId();
						}
						worknoteVO.getTaskInfo().getTask()
								.setJumpToActivity(jumpToActivity);
					}
				}
			}
		} else
			Logger.debug("checkWorkflow返回的结果为null");

		// 3.执行审批动作
		IplatFormEntry pff = (IplatFormEntry) NCLocator.getInstance().lookup(
				IplatFormEntry.class.getName());
		Object value = pff.processAction(IPFActionName.APPROVE + checkman,
				billType, worknoteVO, billVo, null, hmPfExParams);
		return value;
	}

	/**
	 * 存储【产品线】档案 2020-09-03 谈子健
	 * 
	 * @throws BusinessException
	 */
	public String saveProductlineArchives(String productline)
			throws BusinessException {
		String productlineId = "";
		if (productline != null && !"".equals(productline)) {
			String code = "SDLL" + productline;
			StringBuffer query = new StringBuffer();
			query.append("select d.pk_prodline  ");
			query.append("  from bd_prodline d  ");
			query.append(" where d.code = '" + code + "'  ");
			query.append("   and d.enablestate = 2  ");
			query.append("   and d.dr = 0  ");
			String pk_prodline = (String) getBaseDAO().executeQuery(
					query.toString(), new ColumnProcessor());
			if (pk_prodline != null && !"".equals(pk_prodline)) {
				productlineId = pk_prodline;
			} else {
				ProdLineVO prodLineVO = new ProdLineVO();
				prodLineVO.setCode("SDLL" + productline);
				prodLineVO.setPk_group("000112100000000005FD");
				prodLineVO.setPk_org("000112100000000005FD");
				prodLineVO.setEnablestate(2);
				prodLineVO.setName(productline);
				prodLineVO.setCreationtime(new UFDateTime());
				prodLineVO.setCreator(getUserIDByCode("LLWYSF"));
				prodLineVO.setDataoriginflag(0);
				prodLineVO.setStatus(VOStatus.NEW);
				productlineId = getBaseDAO().insertVO(prodLineVO);
			}

		}

		return productlineId;
	}

	/**
	 * 项目信息
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getProject(String code) throws BusinessException {
		String bd_project = "";
		String sql = "select bd_project.pk_project from bd_project where nvl(dr,0)=0"
				+ "  and enablestate='2' and bd_project.project_code = '"
				+ code + "'";
		bd_project = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());

		return bd_project;
	}

	/**
	 * 根据客商编码读取对应主键
	 * 
	 * @param code
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getSupplierIDByCode(String code, String pk_org,
			String pk_group) throws BusinessException {
		String result = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_supplier ")
				.append(" from bd_supplier where dr = 0 ")
				.append(" and (enablestate = 2) ")
				.append("  and ((pk_supplier in (select pk_supplier from bd_suporg   where pk_org in ('"
						+ pk_org
						+ "')  and enablestate = 2 union  select pk_supplier   from bd_supplier  where (pk_org = '"
						+ pk_org + "' or   pk_org = '" + pk_group + "'))))  ")
				.append(" and code = '" + code + "' ");

		try {
			result = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
			if (result == null) {
				throw new BusinessException("供应商编码" + code + "未能在NC档案中关联");
			}
		} catch (Exception e) {
			throw new BusinessException("查询供应商时出错:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 根据组织编码查询组织VO
	 * 
	 * @param orgCode
	 * @return
	 * @throws BusinessException
	 */
	protected OrgVO getOrgVO(String orgCode) throws BusinessException {
		OrgVO orgvo = (OrgVO) getBaseDAO().executeQuery(
				"select * from org_orgs where code ='" + orgCode + "'",
				new BeanProcessor(OrgVO.class));
		return orgvo;
	}

	/**
	 * 获取银行档案2020-09-23-谈子健
	 * 
	 * @throws BusinessException
	 */
	public String getBankdocIDByCode(String code) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("select pk_bankdoc  ");
		query.append("  from bd_bankdoc  ");
		query.append(" where dr = 0  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and code = '" + code + "'  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			if (result == null) {
				throw new BusinessException("付款单位开户行" + code + "未能在NC档案中关联");
			}
		} catch (Exception e) {
			throw new BusinessException("查询供应商时出错:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 获取供应商分类2020-09-23-谈子健
	 * 
	 * @throws BusinessException
	 */
	public String getSupplierclassByCode(String code) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("select pk_supplierclass  ");
		query.append("  from bd_supplierclass  ");
		query.append(" where dr = 0  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and code = '" + code + "'  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			if (result == null) {
				throw new BusinessException("供应商分类" + code + "未能在NC档案中关联");
			}
		} catch (Exception e) {
			throw new BusinessException("查询供应商时出错:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 存储【房产资料】档案 2020-09-23 谈子健
	 * 
	 * @throws BusinessException
	 */
	public String saveHousePropertyArchives(String propertycode,
			String propertyname, String pk_org) throws BusinessException {
		IDefdocService defdocService = NCLocator.getInstance().lookup(
				IDefdocService.class);
		String pk_defdoclist = getPkDefdocListByCode("zdy010");
		String pk_defdoc = "";
		if (propertycode != null && !"".equals(propertycode)) {
			String code = "SDLL" + propertycode;
			Collection<DefdocVO> oldVO = getBaseDAO()
					.retrieveByClause(
							DefdocVO.class,
							"pk_defdoclist = '"
									+ pk_defdoclist
									+ "' and code = '"
									+ code
									+ "'  and nvl(dr, 0) = 0 and enablestate = '2'  and ((pk_org = '000112100000000005FD' or pk_org = '"
									+ pk_org + "'))  ");
			// 如果找到档案并且与传过来的名称一样的话就返回主键
			if (oldVO != null && oldVO.size() > 0) {
				DefdocVO[] defdocVOs = oldVO.toArray(new DefdocVO[0]);
				if (defdocVOs[0].getName().equals(propertyname)) {
					pk_defdoc = defdocVOs[0].getPk_defdoc();
				} else {
					defdocVOs[0].setStatus(VOStatus.UPDATED);
					defdocVOs[0].setName(propertyname);
					DefdocVO[] updateDefdocs = defdocService.updateDefdocs(
							pk_org, defdocVOs);
					pk_defdoc = updateDefdocs[0].getPk_defdoc();
				}

			} else {
				DefdocVO defdocVO = new DefdocVO();
				defdocVO.setPk_group("000112100000000005FD");
				defdocVO.setPk_org(pk_org);
				defdocVO.setMemo(null);
				defdocVO.setName(propertyname);
				defdocVO.setEnablestate(2);
				defdocVO.setDr(0);
				defdocVO.setStatus(VOStatus.NEW);
				defdocVO.setCreator(getUserIDByCode("LLWYSF"));
				defdocVO.setModifier(new UFDate().toString());
				defdocVO.setDatatype(1);
				defdocVO.setCode(code);
				defdocVO.setPid(null);
				defdocVO.setDef1(propertycode);
				defdocVO.setPk_defdoclist(pk_defdoclist);
				DefdocVO[] insertDefdocs = defdocService.insertDefdocs(
						"000112100000000005FD", new DefdocVO[] { defdocVO });
				pk_defdoc = insertDefdocs[0].getPk_defdoc();

			}

		}

		return pk_defdoc;
	}

	/**
	 * 
	 * 
	 * @param 自定义档案列表code
	 * @param docCode
	 *            自定义档案code
	 * @return
	 * @throws BusinessException
	 */
	public String getPkDefdocListByCode(String code) throws BusinessException {
		String sql = "select c.pk_defdoclist from bd_defdoclist c where  c.code='"
				+ code + "' and nvl(dr,0) = 0";
		String result = null;
		result = (String) getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return result;
	}

	/**
	 * 
	 * 
	 * 查询供应商是否散户-2020-09-24-谈子健
	 */
	public String supplierIsfreecust(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		String isfreecust = null;
		query.append("select d.isfreecust from bd_supplier d where d.dr = 0 and d.enablestate = 2 and d.code = '"
				+ code + "'  ");
		isfreecust = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return isfreecust;
	}

	/**
	 * 查询业务流程 (bd_busitype) -2020-09-24-谈子健
	 */
	public String getBusitypeByCode(String code, String pk_group)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		String pk_busitype = "";
		query.append("select b.pk_busitype from bd_busitype b where b.busicode = '"
				+ code + "' and b.pk_group = '" + pk_group + "' and b.dr = 0  ");

		pk_busitype = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return pk_busitype;
	}

	/**
	 * 查询客户基本分类 bd_custclass -2020-09-25-谈子健
	 */
	public String getCustclassByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_custclass  ");
		query.append("  from bd_custclass  ");
		query.append(" where dr = 0  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and code = '" + code + "'  ");

		String pk_custclass = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return pk_custclass;
	}

	/**
	 * 根据科目对照表传入结算方式查询收付款类型 -2020-11-10-谈子健
	 */
	public String getRecpaytype(String pk_balatype) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("SELECT b.factorvalue2  ");
		query.append("  FROM fip_docview d  ");
		query.append("  left join fip_docview_b b  ");
		query.append("    on d.pk_classview = b.pk_classview  ");
		query.append(" WHERE d.viewcode = 'SDLL99'  ");
		query.append("   AND d.dr = 0  ");
		query.append("   and b.dr = 0  ");
		query.append("   and b.factorvalue1 = '" + pk_balatype + "'  ");

		String pk_recpaytype = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return pk_recpaytype;
	}

	/**
	 * 查询现金账户 (bd_cashaccount)-2020-09-25-谈子健
	 */
	public String getCashaccountByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_cashaccount  ");
		query.append("  from bd_cashaccount  ");
		query.append(" where dr = 0  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and code = '" + code + "'  ");

		String pk_recpaytype = (String) getBaseDAO().executeQuery(
				query.toString(), new ColumnProcessor());
		return pk_recpaytype;
	}

	/**
	 * 
	 * 2020-09-23-谈子健
	 * 
	 * 获取收费项目名称
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getItemnameByPk(String pk, String docCode)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist c, bd_defdoc d  ");
		query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
		query.append("   and c.code = '" + docCode + "'  ");
		query.append("   and d.def1 = '" + pk + "'  ");
		query.append("   and nvl(c.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and d.enablestate = '2'  ");
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}

	/**
	 * 获取指定年月的第一天-2020-11-03-谈子健
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最小天数
		int firstDay = cal.getMinimum(Calendar.DATE);
		// 设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * 获取客户主键2020-11-23-谈子健
	 * 
	 * @throws BusinessException
	 */
	public String getCustomerByCode(String code) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("select bd_customer.pk_customer  ");
		query.append("     from bd_customer  ");
		query.append("    where nvl(bd_customer.dr, 0) = 0  ");
		query.append("      and bd_customer.code = '" + code + "'  ");
		query.append("      and bd_customer.enablestate = 2  ");

		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			if (result == null) {
				throw new BusinessException("客户" + code + "未能在NC档案中关联");
			}
		} catch (Exception e) {
			throw new BusinessException("查询客户时出错:" + e.getMessage(), e);
		}
		return result;
	}

}
