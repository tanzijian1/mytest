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
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String BPMOperatorName = "LLBPM";// BPMĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IMDPersistenceService perService = null;
	IUAPQueryBS bs = null;

	String bpmUserid = null;// BPM�����û�

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

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
	 * ���ݱ���õ����㷽ʽpk
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
	 * ���ݱ������Զ��嵵��������������״̬
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

	public IUAPQueryBS getQueryBS() {
		if (bs == null) {
			bs = NCLocator.getInstance().lookup(IUAPQueryBS.class);
		}
		return bs;
	}

	/**
	 * ��ȡBPM����ԱĬ���û�
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
	 * ������Ա��Ϣ
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
	 * ���Ƶ�����Ϣ
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
		String sql = "select t.id from organizationitem t  inner join org_dept  on org_dept.code = t.seqnum where org_dept.pk_dept ='"
				+ id + "'";
		String hcmid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return hcmid;
	}

	/**
	 * ��ȡ��Ա����
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
	 * ����pk��ȡ�������ͱ���
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
	 * ��ȡ���㷽ʽ
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
	 * Ԫ���ݳ־û��ӿ�
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
	 * ����VO����
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
