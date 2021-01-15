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
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String BPMOperatorName = "BPM";// BPMĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

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
	 * ����pk��ȡ�������ͱ���
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
	 * @author �ƹڻ�
	 * �ж��ַ����Ƿ�Ϊ��
	 * @param str
	 */
	public static boolean isNotBlank(String str){
		if (str == null || "null".equals(str)||"".equals(str)||"~".equals(str)){
			return false;
		}
		return true;
	}
	
	/**
	 * @author �ƹڻ�
	 * ��ȡ�����˺ŵ���Ϣ
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
