package nc.bs.tg.outside.ebs.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.pmpub.prv.IProjectAssignService;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ArrayProcessor;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.data.access.NCObject;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.bd.account.AccAsoaVO;
import nc.vo.org.AccountingBookVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.EBSDefdocVO;

import org.apache.commons.lang.StringUtils;

public class EBSBillUtils {
	static EBSBillUtils utils;
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String OperatorName = "EBS";// BPMĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��

	private ISecurityTokenCallback sc;

	IProjectAssignService projectAssignService = null;
	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM�����û�

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static EBSBillUtils getUtils() {
		if (utils == null) {
			utils = new EBSBillUtils();
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

	public String getbudget(String code) throws BusinessException {
		String sql = "select pk_planbudget org_planbudget  where code='" + code
				+ "'";
		Object obj = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		if (obj == null) {
			throw new BusinessException("Ԥ������Ϊ��");
		}
		return (String) obj;
	}

	/**
	 * �õ�Ĭ�ϴ�����pk
	 * 
	 * @return
	 * @throws DAOException
	 */
	public String getcreator() throws BusinessException {
		Object cuserid = getBaseDAO().executeQuery(
				"select cuserid from sm_user where user_code='TY01' ",
				new ColumnProcessor());
		if (cuserid == null) {
			throw new BusinessException("Ĭ�ϴ�����Ϊ��");
		}
		return (String) cuserid;
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
	 * ��Ŀ���乫˾�ӿ�
	 * 
	 * @return
	 */
	public IProjectAssignService getProjectAssignService() {
		if (projectAssignService == null) {
			projectAssignService = NCLocator.getInstance().lookup(
					IProjectAssignService.class);
		}
		return projectAssignService;
	}

	public IPFBusiAction getPfBusiAction() {
		if (pfBusiAction == null) {
			pfBusiAction = NCLocator.getInstance().lookup(IPFBusiAction.class);
		}
		return pfBusiAction;
	}

	/**
	 * ��ȡBPM����ԱĬ���û�
	 * 
	 * @return
	 * @throws BusinessException
	 */
	public String getSaleUserID() throws BusinessException {
		if (bpmUserid == null) {
			String sql = "select cuserid from sm_user  where user_code = '"
					+ OperatorName + "'";
			bpmUserid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		}
		return bpmUserid;
	}

	/**
	 * ������Ա��Ϣ
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
	 * ͨ��NC�����������м���ȡHCM��������
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getHCMDeptID(String id) throws BusinessException {
		String sql = "select t.id from organizationitem t  inner join org_dept  on org_dept.code = t.seqnum where t.pk_dept ='"
				+ id + "'";
		String hcmid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return hcmid;
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
	 * ��ȡҵ�񵥾�VO
	 * 
	 * @param c
	 * @param whereCondStr
	 * @return
	 * @throws BusinessException
	 */
	@SuppressWarnings("rawtypes")
	public NCObject[] getHeadVO(Class c, String whereCondStr)
			throws BusinessException {
		NCObject[] ncobject = getMDQryService().queryBillOfNCObjectByCond(c,
				whereCondStr, false);
		if (ncobject.length > 0) {
			return ncobject;
		} else {
			return null;
		}
	}

	/**
	 * ���ݡ����ű��롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_DeptByCode(String code, String pk_org) {
		String sql = "select pk_dept from org_dept "
				+ " inner join organizationitem on org_dept.code = organizationitem.seqnum  "
				+ " where nvl(org_dept.dr,0)=0 and nvl(organizationitem.dr,0)=0 and org_dept.enablestate = '2' and organizationitem.seqnum='"
				+ code + "' and org_dept.pk_org='" + pk_org + "'";
		String pk_dept = null;
		try {
			pk_dept = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_dept != null) {
				return pk_dept;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ݡ����ű��롿��ȡ����,�汾��Ϣ
	 * 
	 * @param code
	 * @return
	 */
	public Object[] getDeptpksByCode(String code, String pk_org) {
		String sql = "select pk_dept,pk_vid  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		Object[] pk_depts = null;
		try {
			pk_depts = (Object[]) getBaseDAO().executeQuery(sql,
					new ArrayProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ݡ����ű��롿��ȡ����,�汾��Ϣ
	 * 
	 * @param code
	 * @return
	 */
	public Map<String, String> getDeptpksByCode(String code) {
		String sql = "select pk_dept,pk_vid  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.code='"
				+ code + "'";
		Map<String, String> pk_depts = null;
		try {
			pk_depts = (Map<String, String>) getBaseDAO().executeQuery(sql,
					new MapProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getDept_v_pk(String pk_dept) {
		String sql = "select pk_vid  from org_dept "
				+ "  where nvl(org_dept.dr,0)=0  and nvl(org_dept.dr,0)=0 and enablestate = '2' and org_dept.pk_dept='"
				+ pk_dept + "'";
		Map<String, String> pk_depts = null;
		try {
			pk_depts = (Map<String, String>) getBaseDAO().executeQuery(sql,
					new MapProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<String, String> getPsnjobMainDeptByPk(String pk_psndoc) {
		String sql = "select pk_dept from bd_psnjob where pk_psndoc = '"
				+ pk_psndoc + "' and nvl(dr,0) = 0 and ismainjob = 'Y' ";
		Map<String, String> pk_depts = null;
		try {
			pk_depts = (Map<String, String>) getBaseDAO().executeQuery(sql,
					new MapProcessor());
			if (pk_depts != null) {
				return pk_depts;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ݡ���˾���롿��ȡ��������
	 * 
	 * @param code
	 * @return
	 */
	public String getpk_groupByCode(String code) {
		String sql = "select pk_group from org_orgs where code='" + code
				+ "' and dr=0 and enablestate=2 ";
		String pk_group = null;
		try {
			pk_group = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_group != null) {
				return pk_group;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	 * ���ݡ���˾pk����ȡ��֯�汾
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_org_vByPK(String pk_org) {
		String sql = "select pk_vid from org_orgs where pk_org='" + pk_org
				+ "' and dr=0 and enablestate=2 ";
		String pk_vid = null;
		try {
			pk_vid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_vid != null) {
				return pk_vid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���ݡ���˾���롿��ȡ��֯�汾
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_org_vByCode(String code) {
		String sql = "select pk_vid from org_orgs where code='" + code
				+ "' and dr=0 and enablestate=2 ";
		String pk_vid = null;
		try {
			pk_vid = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_vid != null) {
				return pk_vid;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
	 * ���ݡ���Ա���롿��ȡ������֯����
	 * 
	 * @param code
	 * @return
	 */
	public String getPkOrgPkByCode(String code) {
		String sql = "select pk_org from org_dept "
				+ " inner join organizationitem on org_dept.code = organizationitem.seqnum  "
				+ " where nvl(org_dept.dr,0)=0 and nvl(organizationitem.dr,0)=0 and org_dept.enablestate = '2' and organizationitem.seqnum='"
				+ code + "' ";
		String pk_dept = null;
		try {
			pk_dept = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_dept != null) {
				return pk_dept;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * ���
	 * 
	 * @param pk
	 * @return
	 */
	public String getplate(String pk) {
		String sql = "select name from bd_defdoc where pk_defdoc='" + pk + "'";
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
	 * 
	 * 
	 * @param �Զ��嵵���б�code
	 * @param docCode
	 *            �Զ��嵵��code
	 * @return
	 */
	public String getdefdocBycode(String code, String docCode) {
		StringBuffer query = new StringBuffer();
		query.append("select d.pk_defdoc  ");
		query.append("  from bd_defdoclist c, bd_defdoc d  ");
		query.append(" where c.pk_defdoclist = d.pk_defdoclist  ");
		query.append("   and c.code = '" + docCode + "'  ");
		query.append("   and d.code = '" + code + "'  ");
		query.append("   and nvl(c.dr, 0) = 0  ");
		query.append("   and nvl(d.dr, 0) = 0  ");
		query.append("   and d.enablestate = '2'  ");

		// String sql =
		// "select d.pk_defdoc from bd_defdoclist c,bd_defdoc d where c.pk_defdoclist=d.pk_defdoclist and  c.code='"
		// + docCode + "' and d.code='" + code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 
	 * 
	 * @param �Զ��嵵���б�code
	 * @param docCode
	 *            �Զ��嵵��code
	 * @return
	 */
	public String getPkDefdocListByCode(String code) {
		String sql = "select c.pk_defdoclist from bd_defdoclist c where  c.code='"
				+ code + "' and nvl(dr,0) = 0";
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
	 * 
	 * 
	 * @param ��Ŀ����code
	 * 
	 * @return
	 */
	public String getPkEPSByCode(String code) {
		String sql = "select c.pk_eps from pm_eps c where  c.eps_code='" + code
				+ "' and nvl(dr,0) = 0";
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
	 * 
	 * 
	 * @param ��Ŀ���͵���code
	 * 
	 * @return
	 */
	public String getPkProjectClassByCode(String code) {
		String sql = "select c.pk_projectclass from bd_projectclass c where  c.type_code='"
				+ code + "' and nvl(dr,0) = 0";
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
	 * ���ݡ��û����롿��ȡ����
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
	 * ���ݡ��û����롿��ȡ��Ա����
	 * 
	 * @param code
	 * @return
	 */
	public String getPsnPkByCode(String code) {
		String sql = "select pk_psndoc  from sm_user where nvl(dr,0)=0 and user_code='"
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
	 * ���ݡ���֧��Ŀ���롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */

	public String getInoutPkByCode(String code) {
		String sql = "select pk_inoutbusiclass from bd_inoutbusiclass where nvl(dr,0)=0 and enablestate='2' and code='"
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
	 * ���ݡ���Ŀ���롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */

	public String getpk_projectByCode(String code) {
		String sql = "select pk_project from bd_project where nvl(dr,0)=0 and enablestate='2' and def1='"
				+ code + "'";
		// �ɱ���Ϊ����Ŀ����
		String costsql = "select pk_project from bd_project where nvl(dr,0)=0 and enablestate='2' and  project_code  = '"
				+ code + "'";
		String result = null;
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (result == null) {
				result = (String) getBaseDAO().executeQuery(costsql,
						new ColumnProcessor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ݿ��̱����ȡ��Ӧ����
	 * 
	 * @param code
	 * @param pk_org
	 * @return
	 */
	public String getSupplierIDByCode(String code, String pk_org,
			String pk_group) {
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
	 */
	public String getAccountIDByCode(String recaccount, String pk_receiver) {
		String result = null;
		String sql = "SELECT bd_custbank.pk_bankaccsub AS pk_bankaccsub "
				+ "   FROM bd_bankaccbas, bd_bankaccsub, bd_custbank "
				+ " WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas "
				+ " AND bd_bankaccsub.pk_bankaccsub = bd_custbank.pk_bankaccsub "
				+ " AND bd_bankaccsub.pk_bankaccbas = bd_custbank.pk_bankaccbas "
				+ " AND bd_custbank.pk_bankaccsub != '~' "
				+ " AND bd_bankaccsub.Accnum = '"
				+ recaccount
				+ "' "
				+ " AND exists "
				+ " (select 1 from bd_bankaccbas bas  where bas.pk_bankaccbas = bd_custbank.pk_bankaccbas  and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y')) "
				+ " and (enablestate = 2) "
				+ " and (pk_cust = '"
				+ pk_receiver
				+ "' and pk_custbank IN (SELECT min(pk_custbank) FROM bd_custbank GROUP BY pk_bankaccsub, pk_cust)) ";
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
	 */
	public Map<String, String> getCityByrecaccount(String recaccount) {
		Map<String, String> result = null;
		String sql = "select bd_bankdoc.city,bd_bankdoc.province from bd_bankaccbas "
				+ "left join bd_bankdoc on bd_bankaccbas.pk_bankdoc = bd_bankdoc.pk_bankdoc "
				+ "where nvl(bd_bankdoc.dr,0)=0 and nvl(bd_bankaccbas.dr,0)=0 "
				+ " and bd_bankaccbas.pk_bankaccbas = (select pk_bankaccbas from bd_bankaccsub  where pk_bankaccsub  = "
				+ "'" + recaccount + "' and nvl(dr,0) = 0)";
		try {
			result = (Map<String, String>) getBaseDAO().executeQuery(sql,
					new MapProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ͨ��������ѯ��ӦVO��Ϣ
	 * 
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	protected Object queryVOByKey(String key, Class c) throws BusinessException {
		return getBaseDAO().retrieveByPK(c, key);
	}

	/**
	 * ���ݱ���������ҿ���
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
	 * ���ݱ���������ҹ�Ӧ��
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */

	public String getSupplier(String code) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		Object obj = dao.executeQuery(
				"select corcustomer from bd_supplier where (code='" + code
						+ "' or name='" + code + "') and nvl(dr,0)=0",
				new ColumnProcessor());
		return (String) obj;
	}

	/**
	 * ���㷽ʽ��ѯ
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
	 * Ʊ����Ϣ��ѯ
	 * 
	 * @param checktype
	 * @return
	 */
	protected String getNotetypeByCode(String checktype) {
		String result = null;
		String sql = "select    pk_notetype  from bd_notetype where dr = 0 and code ='"
				+ checktype + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��ȡԤ���Ŀ��Ϣ
	 * 
	 * @param budgetsub
	 * @return7
	 */
	protected String getBudgetsubByCode(String budgetsub) {
		String result = null;
		String sql = "select tb_budgetsub.pk_obj from tb_budgetsub where dr = 0 and  tb_budgetsub.objcode = '"
				+ budgetsub + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ݿ�Ŀ�����ѯ�ԵĿ�Ŀ����
	 * 
	 * @param pk_corp
	 * @param kmbm
	 * @return
	 * @throws ClassNotFoundException
	 * @throws Exception
	 */
	public String getAccsubjKeyByCode(String acccode, String pk_org)
			throws BusinessException {
		String pk_accsubj = null;
		// ����65������֯ �ҵ����˲�������ס�˲��ͻ�ƿ�Ŀ�ҵ���ƿ�Ŀ��¼
		// ���ݹ�˾�ҵ�������壬�������˲������������˲��ͻ�ƿ�Ŀ����ҵ���ƿ�Ŀ��¼nc.vo.org.AccountingBookVO
		String sValue = "";
		String whereClause = "pk_relorg = '" + pk_org + "'and accounttype='1' ";
		AccountingBookVO[] glorgs = (AccountingBookVO[]) getBaseDAO()
				.retrieveByClause(nc.vo.org.AccountingBookVO.class, whereClause)
				.toArray(new AccountingBookVO[0]);
		if (glorgs != null && glorgs.length == 1) {
			sValue = glorgs[0].getPk_curraccchart();
			whereClause = " nvl(dr,0)=0 and pk_accchart = '" + sValue + "' ";

			whereClause += " and pk_account in (select pk_account from bd_account where nvl(dr,0)=0   and (code='"
					+ acccode + "' or name ='" + acccode + "') "
					// +" and pk_accchart='" + sValue + "'"
					+ ")";
			AccAsoaVO[] accs = (AccAsoaVO[]) getBaseDAO().retrieveByClause(
					nc.vo.bd.account.AccAsoaVO.class, whereClause).toArray(
					new AccAsoaVO[0]);
			if (accs != null && accs.length == 1) {
				sValue = accs[0].getPrimaryKey();
			}
		}
		return sValue;
	}

	/**
	 * ��ȡ��������PK
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
	 * ������ݿ��������id�ʹ�������������id�Ƿ�ƥ��
	 * 
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkChangeEbsId(HashMap<String, Object> value)
			throws BusinessException {
		if (value.get("ebs_id") != null && value.get("code") != null) {
			if (StringUtils.isNotBlank(value.get("ebs_id").toString())
					&& StringUtils.isNotBlank(value.get("code").toString())) {
				// �������ݿ�������ݱ���
				String oldMainCode = (String) getBaseDAO().executeQuery(
						"select def1 from bd_defdoc where code = '"
								+ value.get("code").toString()
								+ "' and nvl(dr,0) = 0", new ColumnProcessor());
				if (value.get("ebs_id").equals(oldMainCode)) {
					return true;
				} else {
					return false;
				}
			}
		}
		return false;

	}

	/**
	 * ������ݿ��������id�ʹ�������������id�Ƿ�ƥ��
	 * 
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public boolean checkChangeEbsId(EBSDefdocVO defdocheadVO)
			throws BusinessException {
		if (StringUtils.isNotBlank(defdocheadVO.getEbs_id())
				&& StringUtils.isNotBlank(defdocheadVO.getCode())) {
			// �������ݿ�������ݱ���
			String oldMainCode = (String) getBaseDAO().executeQuery(
					"select def1 from bd_defdoc where code = '"
							+ defdocheadVO.getCode() + "' and nvl(dr,0) = 0",
					new ColumnProcessor());
			if (defdocheadVO.getEbs_id().equals(oldMainCode)) {
				return true;
			} else {
				return false;
			}
		}
		return false;

	}

	/**
	 * ͨ����Ա���룬���ű��룬��λ�����ȡ��صĵ�����Ϣ
	 * 
	 * @param psncode
	 * @param deptcode
	 * @param postcode
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getPsnInfo(String psncode, String deptcode,
			String postcode) throws BusinessException {
		String sql = "select distinct bd_psnjob.pk_dept,bd_psnjob.pk_post,bd_psndoc.pk_psndoc "
				+ "  from bd_psndoc "
				+ "  inner join bd_psnjob  on bd_psnjob. pk_psndoc = bd_psndoc.pk_psndoc "
				+ "  inner join org_dept  on org_dept.pk_dept = bd_psnjob.pk_dept and org_dept.enablestate = 2 "
				+ "  inner join om_post on om_post.pk_post = bd_psnjob.pk_post "
				+ "  where bd_psndoc.code = '"
				+ psncode
				+ "' and org_dept.code = '"
				+ deptcode
				+ "' and om_post.postcode = '"
				+ postcode
				+ "' "
				+ "  and bd_psndoc.dr = 0 and org_dept.dr = 0 and om_post.dr =0 "
				+ "  and bd_psnjob.dr = 0  ";
		Map<String, String> result = null;
		try {
			result = (Map<String, String>) getBaseDAO().executeQuery(sql,
					new MapProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ݿ��̱����ȡ��Ӧ����
	 * 
	 * @param code
	 * @param pk_org
	 * @return
	 */
	public String getSupplierIDByCodeOrName(String code) {
		String result = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_supplier from bd_supplier where dr = 0 ")
				.append(" and (enablestate = 2) ")
				.append(" and (code = '" + code + "' ")
				.append(" or name = '" + code + "') ");

		try {
			result = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ݿ��̱����ȡ��Ӧ��Ӧ�̻�������
	 * 
	 * @param code
	 * @param pk_org
	 * @return
	 */
	public String getSupplierClassByCode(String code) {
		String result = null;
		StringBuffer sql = new StringBuffer();
		sql.append("select pk_supplierclass from bd_supplier where dr = 0 ")
				.append(" and enablestate = 2 ")
				.append(" and code = '" + code + "' ");
		try {
			result = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ��Ӧ��
	 * 
	 * @param balatype
	 * @return
	 */
	protected String getCustPkByCode(String custCode) {
		String result = null;
		String sql = "select pk_cust_sup from bd_cust_supplier where dr = 0 and code ='"
				+ custCode + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * ���ݱ����ȡ����2020-01-14-tjl
	 * 
	 * @throws BusinessException
	 */
	public String getPkcurrtypeByCode(String code) throws BusinessException {
		String result = null;
		String sql = "select b.pk_currtype from bd_currtype b where b.code = '"
				+ code + "' and nvl(dr,0) = 0";
		try {
			result = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
		} catch (DAOException e) {
			throw new BusinessException("��������ı��ֱ���");
		}
		return result;
	}

	/**
	 * ��֤����2019-11-04-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String checkPk_currtype(String pk_currtype) throws BusinessException {
		String result = null;
		String sql = "select PK_CURRTYPE name from bd_currtype b where b.pk_currtype = '"
				+ pk_currtype + "' and dr = 0";
		try {
			result = (String) getBaseDAO().executeQuery(sql.toString(),
					new ColumnProcessor());
		} catch (DAOException e) {
			throw new BusinessException("��������ı�������");
		}
		return result;
	}

	/**
	 * ����code ��ȡ������������״̬ 2019-12-02-̸�ӽ�
	 */
	public String getAuditstateByCode(String code) throws BusinessException {
		String result = null;
		String sql = "select pk_defdoc from bd_defdoc where nvl(dr,0)=0 and enablestate='2' and code ='"
				+ code + "'";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	/**
	 * ͨ������ĳɱ���Ŀ������֧��Ŀ 2020-01-06-̸�ӽ�
	 */
	public String getSubjcode(String def7, String def47)
			throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT b.factorvalue3  ");
		query.append("  FROM fip_docview d  ");
		query.append("  left join fip_docview_b b  ");
		query.append("    on d.pk_classview = b.pk_classview  ");
		query.append(" WHERE d.viewcode = 'CB04'  ");
		query.append("   AND d.dr = 0  ");
		query.append("   and b.dr = 0  ");
		query.append("   and b.factorvalue1 = '" + def7 + "'  ");
		query.append("   and b.factorvalue2 = '" + def47 + "'  ");

		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return result;
	}

	/**
	 * add by tjl 2020-02-13 ���ݱ����ѯ����Ԥ�ᵥpk
	 * 
	 * @param code
	 * @return
	 */
	public String getAccruedPkByCode(String code) {
		String sql = "select pk_accrued_bill from er_accrued where billno='"
				+ code + "' and nvl(dr,0) = 0 ";
		String pk_accrued_bill = null;
		try {
			pk_accrued_bill = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_accrued_bill != null) {
				return pk_accrued_bill;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * EBS��ͨ��Ӧ����ͨ��Ԥ���Ŀ�����ѯ��֧��Ŀ����2020-04-16-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String getPksubjcodeByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("SELECT b.factorvalue2  ");
		query.append("  FROM fip_docview d  ");
		query.append("  left join fip_docview_b b  ");
		query.append("    on d.pk_classview = b.pk_classview  ");
		query.append("  left join tb_budgetsub s  ");
		query.append("    on b.factorvalue1 = s.pk_obj  ");
		query.append(" WHERE d.viewcode = 'YC01'  ");
		query.append("   AND d.dr = 0  ");
		query.append("   and b.dr = 0  ");
		query.append("   and s.dr = 0  ");
		query.append("   and s.objcode = '" + code + "'  ");
		String pk_subjcode = null;
		try {
			pk_subjcode = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			if (pk_subjcode != null) {
				return pk_subjcode;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	/***
	 * �ɱ�Ӧ�������������뵥�ɴ�����Ŀ��Ϣ�����Ƿ��ʱ�(def8),��Ŀ����(def9)
	 * �ֱ����Ӧ����def60,def61;�������뵥def56,def57 2020-04-17-̸�ӽ�
	 * ͨ��EBS����������Ŀ�����ѯ��Ӧ�ķ��ʱ�(def8),��Ŀ����(def9)
	 */
	public HashMap<String, Object> getProjectDataByCode(String code)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select def8,def9 from bd_project where nvl(dr,0)=0 and enablestate='2' and  project_code  = '"
				+ code + "'  ");

		try {
			HashMap<String, Object> projectData = (HashMap<String, Object>) getBaseDAO()
					.executeQuery(query.toString(), new MapProcessor());
			if (projectData != null) {
				return projectData;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	/**
	 * ͨ��Ӧ�����ӿڴ����г�����ĿΪ��������ͨ�����Ĳ�ѯ��Ŀ������def9��Ŀ����
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getProjectNatureByName(String name) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select def9 from bd_project where nvl(dr,0)=0 and enablestate='2' and bd_project.project_name = '"
				+ name + "'");

		try {
			String projectNature = (String) getBaseDAO().executeQuery(
					query.toString(), new ColumnProcessor());
			if (projectNature != null) {
				return projectNature;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * �ɱ��������뵥�ӿڣ�����EBS�������Ĳ�����֯+��ͬ���룬 �����ú�ͬ��Ӧ�ġ�ҵ���š����浽�������뵥��ҵ���š���ͷdef58
	 * 2020-04-20-̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String getDepartment(String contractcode, String pk_org)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		String def99 = null;
		query.append("select a.def101  ");
		query.append("  from fct_ap a  ");
		query.append(" where a.vbillcode = '" + contractcode + "'  ");
		query.append("   and a.pk_org = '" + pk_org + "'  ");
		query.append("   and a.dr = 0  ");
		query.append("   and a.blatest = 'Y'  ");
		try {
			def99 = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			if (def99 != null) {
				return def99;
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		return null;
	}

	/**
	 * �ɱ�Ӧ�������ݱ�ͷdef7��ͬ���ʹ�������Ʊ������-2020-05-08-̸�ӽ�
	 */
	public String getNoteType(String name) {
		String result = null;
		try {

			StringBuffer query = new StringBuffer();
			query.append("select d.def2  ");
			query.append("      from bd_defdoclist c, bd_defdoc d  ");
			query.append("     where c.pk_defdoclist = d.pk_defdoclist  ");
			query.append("       and c.code = 'zdy045'  ");
			query.append("       and d.name = '" + name + "'  ");
			query.append("       and nvl(c.dr, 0) = 0  ");
			query.append("       and nvl(d.dr, 0) = 0  ");
			query.append("       and d.enablestate = '2'  ");

			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
			// �����ѯΪ��Ĭ��Ϊ�ɱ���
			if (result == null || "".equals(result)) {
				query.append("select d.pk_defdoc  ");
				query.append("      from bd_defdoclist c, bd_defdoc d  ");
				query.append("     where c.pk_defdoclist = d.pk_defdoclist  ");
				query.append("       and c.code = 'zdy008'  ");
				query.append("       and d.code = '01'  ");
				query.append("       and nvl(c.dr, 0) = 0  ");
				query.append("       and nvl(d.dr, 0) = 0  ");
				query.append("       and d.enablestate = '2'  ");
				result = (String) getBaseDAO().executeQuery(query.toString(),
						new ColumnProcessor());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	/**
	 * ��ѯҵ������
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getBusitypeID(String code, String pk_group)
			throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select pk_busitype   ");
		query.append("  from bd_busitype ");
		query.append(" where validity = 1  ");
		query.append("   and busicode = '" + code + "' ");
		query.append(" and pk_group = '" + pk_group + "'");
		query.append("   and isnull(dr,0) = 0  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());

		return value;
	}

	/**
	 * ͨ��ҵ��Ա�������ҵ��Ա���� 2020-07-02 ̸�ӽ�
	 * 
	 * @throws BusinessException
	 */
	public String getPk_psndocByCode(String code) throws BusinessException {
		StringBuffer query = new StringBuffer();
		query.append("select c.pk_psndoc from bd_psndoc c where c.code = '"
				+ code + "' and nvl(c.dr,0) = 0 and c.enablestate = 2  ");
		String value = (String) getBaseDAO().executeQuery(query.toString(),
				new ColumnProcessor());
		return value;
	}

	/**
	 * ���ݸ��������˻������ȡ��Ӧ���� 2020-07-03-̸�ӽ�
	 * 
	 * @param recaccount
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	public String getPersonalAccountIDByCode(String recaccount,
			String pk_receiver) throws BusinessException {
		String result = null;
		StringBuffer query = new StringBuffer();
		query.append("SELECT bd_psnbankacc.pk_bankaccsub AS pk_bankaccsub  ");
		query.append("  FROM bd_bankaccbas, bd_bankaccsub, bd_psnbankacc  ");
		query.append(" WHERE bd_bankaccbas.pk_bankaccbas = bd_bankaccsub.pk_bankaccbas  ");
		query.append("   AND bd_bankaccsub.pk_bankaccsub = bd_psnbankacc.pk_bankaccsub  ");
		query.append("   AND bd_bankaccsub.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("   and bd_psnbankacc.pk_bankaccsub != '~'  ");
		query.append("   AND bd_bankaccsub.Accnum = '" + recaccount + "'  ");
		query.append("   AND exists (select 1  ");
		query.append("          from bd_bankaccbas bas  ");
		query.append("         where bas.pk_bankaccbas = bd_psnbankacc.pk_bankaccbas  ");
		query.append("           and (nvl(bd_bankaccbas.isinneracc, 'N') != 'Y'))  ");
		query.append("   and (enablestate = 2)  ");
		query.append("   and (pk_psndoc = '" + pk_receiver + "' and  ");
		query.append("       pk_psnbankacc IN  ");
		query.append("       (SELECT min(pk_psnbankacc)  ");
		query.append("           FROM bd_psnbankacc  ");
		query.append("          GROUP BY pk_bankaccsub, pk_psndoc))  ");
		try {
			result = (String) getBaseDAO().executeQuery(query.toString(),
					new ColumnProcessor());
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}
		return result;
	}
}
