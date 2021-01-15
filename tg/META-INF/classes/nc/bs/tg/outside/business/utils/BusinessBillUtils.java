package nc.bs.tg.outside.business.utils;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.NCLocator;
import nc.bs.framework.server.ISecurityTokenCallback;
import nc.itf.uap.pf.IPFBusiAction;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.md.persist.framework.IMDPersistenceQueryService;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;

public class BusinessBillUtils {
	static BusinessBillUtils utils;
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String OperatorName = "SY";// INVĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "Y";// �ɹ�
	public static final String STATUS_FAILED = "N";// ʧ��

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM�����û�

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static BusinessBillUtils getUtils() {
		if (utils == null) {
			utils = new BusinessBillUtils();
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
	public String getUserID() throws BusinessException {
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

	public String getOrgName(String orgid) throws BusinessException {
		String sql = "select name from org_orgs where pk_org = '" + orgid + "'";
		Object value = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return (String) value;
	}
	
	public String getPk_tradetypeid(String code) throws BusinessException {
		String sql = "select bd_billtype.pk_billtypeid from bd_billtype where pk_billtypecode = '" + code + "'";
		Object value = getBaseDAO().executeQuery(sql, new ColumnProcessor());
		return (String) value;
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
	public SuperVO getHeadVO(Class c, String whereCondStr)
			throws BusinessException {
		Collection coll = getMDQryService().queryBillOfVOByCond(c,
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (SuperVO) coll.toArray()[0];
		} else {
			return null;
		}
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
}
