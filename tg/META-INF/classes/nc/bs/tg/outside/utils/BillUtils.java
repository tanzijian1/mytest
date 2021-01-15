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
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��
	public static final String DefaultOperator = "EBS";// Ĭ���Ƶ���
	public static final String DefaultGroup = "0001";// Ĭ�ϼ���/ʱ������
	private String secretkey = null;// ��Կ
	private ISecurityTokenCallback sc;
	private IMDPersistenceQueryService mdQryService = null;
	private BaseDAO baseDAO = null;
	private IplatFormEntry pfBusiAction = null;
	private IArapBillPubQueryService arapBillPubQueryService = null;
	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//
	private IWorkflowMachine workflowMachine = null;// ����ƽ̨���õĹ�����������

	public static BillUtils getUtils() {
		if (utils == null) {
			utils = new BillUtils();
		}
		return utils;
	}

	/**
	 * �ӿڱ������ǰ���� ���ڷ�ֹ�����Ŷ����(��ʱ��)ʱ����ϵͳ�����͵��ݣ�NC�����ظ�����
	 * 
	 * @param billcode
	 *            ��ϵͳ����Ψһ��ʶ�ַ���
	 * @throws BusinessException
	 */
	public static String addBillQueue(String billqueue)
			throws BusinessException {
		if (null != billqueue && !"".equals(billqueue)
				&& !m_billqueue.add(billqueue)) {
			throw new BusinessException("ҵ�񵥾ݡ�" + billqueue
					+ "��,����ִ�ж���,�����ظ�ִ��!");
		}
		return billqueue;
	}

	/**
	 * �ӿ��˳�ʱ�Ƴ�
	 * 
	 * @param billcode
	 *            ��ϵͳ����Ψһ��ʶ�ַ���,���ڷ�ֹ�����ų�����ظ�����
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
	 * ע�밲ȫ����
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
	 * �ͷŰ�ȫ����
	 * 
	 * @param token
	 */
	public void restore(byte[] token) {
		getTokenCallBacck().restore(token);

	}

	/**
	 * ���ݿ�־û�
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
	 * Ԫ���ݳ־û���ѯ�ӿ�
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
	 * ���̽�����
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
	 * ������Կ�Ƿ�ͨ��
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
			throw new BusinessException("���⽻���ӿ���Կδά��,����ϵϵͳ����Ա");
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
	 * ��whereֵת��ΪSQL����
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
	 * ��ȡҵ�񵥾ݾۺ�VO
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
	 * ���ݡ��û����롿��ȡ����
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
	 * ���ݡ��������ͱ��롿��ȡ����
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
	 * �õ�Ĭ�Ͼ����˲���
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
	 * ���ݡ���˾���롿��ȡ����
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
	 * ���ݡ���Ա���롿��ȡ����
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
	 * ��ȡ���Ƶ���
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
	 * �õ�Ĭ�Ͼ�����pk
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
			throw new BusinessException("������Ϊ��");
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
	 * �õ�Ĭ�Ͼ����˵�λ
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
	 * Ĭ�Ϸ��óе����ű���
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
	 * �õ�Ĭ�Ͼ����˲���
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
	 * �õ�Ĭ�Ͼ�����pk
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getopertor() throws BusinessException {
		Object pk_base_doc = getBaseDAO().executeQuery(
				"select pk_base_doc from sm_user where user_code='TY01' ",
				new ColumnProcessor());
		if (pk_base_doc == null) {
			throw new BusinessException("Ĭ�Ͼ�����Ϊ��");
		}
		return (String) pk_base_doc;
	}

	/**
	 * ���ݵ���id�ҵ���������Ϊ�Ƶ��˵���������id
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
			if (act.getMultiLangName().toString().contains("�Ƶ���")) {
				creatorActid = act.getId();
				break;
			}
		}
		return creatorActid;
	}

	/**
	 * 
	 * 2020-08-20-̸�ӽ�
	 * 
	 * @param �Զ��嵵���б�code
	 * @param docCode
	 *            �Զ��嵵��code
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
	 * ���㷽ʽ��ѯ 2020-08-20-̸�ӽ�
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
	 * ���������˻������ȡ��Ӧ����
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
	 * ��̨����ĳ�ŵ���
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
		Logger.debug("******����PfUtilTools.approveSilently����*************************");
		Logger.debug("* billType=" + billType);
		Logger.debug("* billId=" + billId);
		Logger.debug("* checkResult=" + checkResult);
		Logger.debug("* checkNote=" + checkNote);
		Logger.debug("* checkman=" + checkman);

		// 1.��õ��ݾۺ�VO
		IPFConfig bsConfig = (IPFConfig) NCLocator.getInstance().lookup(
				IPFConfig.class.getName());
		AggregatedValueObject billVo = bsConfig.queryBillDataVO(billType,
				billId);
		if (billVo == null)
			throw new PFBusinessException(NCLangRes4VoTransl.getNCLangRes()
					.getStrByID("busitype", "busitypehint-000063")/*
																 * ����
																 * ���ݵ������ͺ͵���ID��ȡ�������ݾۺ�VO
																 */);

		// 2.��ù���������������
		IWorkflowMachine bsWorkflow = (IWorkflowMachine) NCLocator
				.getInstance().lookup(IWorkflowMachine.class.getName());
		HashMap hmPfExParams = new HashMap();
		hmPfExParams.put("nolockandconsist", true);

		WorkflownoteVO worknoteVO = bsWorkflow.checkWorkFlow(
				IPFActionName.APPROVE + checkman, billType, billVo,
				hmPfExParams);
		if (worknoteVO != null) {
			worknoteVO.setChecknote(checkNote);
			// ��ȡ�������-ͨ��/��ͨ��/����
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
			Logger.debug("checkWorkflow���صĽ��Ϊnull");

		// 3.ִ����������
		IplatFormEntry pff = (IplatFormEntry) NCLocator.getInstance().lookup(
				IplatFormEntry.class.getName());
		Object value = pff.processAction(IPFActionName.APPROVE + checkman,
				billType, worknoteVO, billVo, null, hmPfExParams);
		return value;
	}

	/**
	 * �洢����Ʒ�ߡ����� 2020-09-03 ̸�ӽ�
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
	 * ��Ŀ��Ϣ
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
	 * ���ݿ��̱����ȡ��Ӧ����
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
				throw new BusinessException("��Ӧ�̱���" + code + "δ����NC�����й���");
			}
		} catch (Exception e) {
			throw new BusinessException("��ѯ��Ӧ��ʱ����:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * ������֯�����ѯ��֯VO
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
	 * ��ȡ���е���2020-09-23-̸�ӽ�
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
				throw new BusinessException("���λ������" + code + "δ����NC�����й���");
			}
		} catch (Exception e) {
			throw new BusinessException("��ѯ��Ӧ��ʱ����:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * ��ȡ��Ӧ�̷���2020-09-23-̸�ӽ�
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
				throw new BusinessException("��Ӧ�̷���" + code + "δ����NC�����й���");
			}
		} catch (Exception e) {
			throw new BusinessException("��ѯ��Ӧ��ʱ����:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * �洢���������ϡ����� 2020-09-23 ̸�ӽ�
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
			// ����ҵ����������봫����������һ���Ļ��ͷ�������
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
	 * @param �Զ��嵵���б�code
	 * @param docCode
	 *            �Զ��嵵��code
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
	 * ��ѯ��Ӧ���Ƿ�ɢ��-2020-09-24-̸�ӽ�
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
	 * ��ѯҵ������ (bd_busitype) -2020-09-24-̸�ӽ�
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
	 * ��ѯ�ͻ��������� bd_custclass -2020-09-25-̸�ӽ�
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
	 * ���ݿ�Ŀ���ձ�����㷽ʽ��ѯ�ո������� -2020-11-10-̸�ӽ�
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
	 * ��ѯ�ֽ��˻� (bd_cashaccount)-2020-09-25-̸�ӽ�
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
	 * 2020-09-23-̸�ӽ�
	 * 
	 * ��ȡ�շ���Ŀ����
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
	 * ��ȡָ�����µĵ�һ��-2020-11-03-̸�ӽ�
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static String getFirstDayOfMonth1(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// �������
		cal.set(Calendar.YEAR, year);
		// �����·�
		cal.set(Calendar.MONTH, month - 1);
		// ��ȡĳ����С����
		int firstDay = cal.getMinimum(Calendar.DATE);
		// �����������·ݵ���С����
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		// ��ʽ������
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(cal.getTime());
	}

	/**
	 * ��ȡ�ͻ�����2020-11-23-̸�ӽ�
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
				throw new BusinessException("�ͻ�" + code + "δ����NC�����й���");
			}
		} catch (Exception e) {
			throw new BusinessException("��ѯ�ͻ�ʱ����:" + e.getMessage(), e);
		}
		return result;
	}

}
