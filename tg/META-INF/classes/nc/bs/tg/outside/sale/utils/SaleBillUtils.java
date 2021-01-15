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
	public static final String DataSourceName = "design";// �ӿ�Ĭ������Դ
	public static final String SaleOperatorName = "SALE";// BPMĬ�ϲ���Ա
	public static final String STATUS_SUCCESS = "1";// �ɹ�
	public static final String STATUS_FAILED = "0";// ʧ��

	private ISecurityTokenCallback sc;

	IMDPersistenceQueryService mdQryService = null;
	BaseDAO baseDAO = null;
	IPFBusiAction pfBusiAction = null;

	String bpmUserid = null;// BPM�����û�

	private static java.util.Set<String> m_billqueue = java.util.Collections
			.synchronizedSet(new HashSet<String>());//

	public static SaleBillUtils getUtils() {
		if (utils == null) {
			utils = new SaleBillUtils();
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
				whereCondStr, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	/**
	 * ���ݷ�Ʊ�����ȡ��ƱPK
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
	 * ���ݡ����ز���Ŀ���롿��ȡ����
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
	 * ���ݡ����ز���Ŀ���롿��ȡ����
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
	 * ���ݡ����ز���Ŀ���ơ���ȡ����
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
	 * ͨ�����ƻ�����Զ�����õ�pk
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
	 * ���ݡ�˰�ձ��롿��ȡPK
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
	 * ���ݡ��������͡���ȡ��֧��ĿPK
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
	 * ��ȡ��Ʊ��֯ά�������Ϣ
	 * @param code �������
	 * @param no ��Ʊ����
	 * @return
	 * @throws BusinessException
	 */
	public BillmaintenanceVO getSellerMsgVO(String code,String no) throws BusinessException{
		if(code == null || "".equals(code)){
			throw new BusinessException("�����쳣���������Ʋ���Ϊ�գ�");
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
    * ͨ����Ա����õ��û�pk
 * @throws DAOException 
    */
	public String getUserByPsondoc(String psndoccode) throws DAOException{
		String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where pk_base_doc=(select pk_psndoc from bd_psndoc where code='"+psndoccode+"')", new ColumnProcessor());
	    return pk;
	}
	
	/**
	    * ͨ���û�����õ��û�pk
	 * @throws DAOException 
	    */
	public String getUserByCode(String psndoccode) throws DAOException{
			String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where user_code='"+psndoccode+"'", new ColumnProcessor());
		    return pk;
		}
	/**
	 * ���̱�������Ƶõ�pk
	 * @param code
	 * @return
	 * @throws DAOException
	 */
	public String getBd_cust_supplier(String code) throws DAOException {
		String pk=(String) getBaseDAO().executeQuery("select   pk_cust_sup  from bd_cust_supplier where (code='"+code+"' or name='"+code+"')", new ColumnProcessor());
	    return pk;
	}
}
