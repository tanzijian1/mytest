package nc.bs.tg.zhsc.utils;

import java.util.ArrayList;
import java.util.List;

import nc.bs.dao.BaseDAO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.receivable.AggReceivableBillVO;
import nc.vo.arap.receivable.ReceivableBillItemVO;
import nc.vo.arap.receivable.ReceivableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.tg.outside.ReceivableBodyVO;
import nc.vo.tg.outside.ReceivableHeadVO;


public class ZhscRecbillConvertUtils{
public static final String DefaultOperator = "LLZHSC";// Ĭ���Ƶ���
	
	private static ZhscRecbillConvertUtils convertUtils;
	
	public static ZhscRecbillConvertUtils getInstance() {
		if (convertUtils == null) {
			convertUtils = new ZhscRecbillConvertUtils();
		}
		return convertUtils;
	}
	
	BaseDAO baseDAO = null;
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
	
	public AggReceivableBillVO onTranBill(ReceivableHeadVO headVO,
			List<ReceivableBodyVO> bodyVOs) throws BusinessException{
		
		AggReceivableBillVO aggvo = new AggReceivableBillVO();
		ReceivableBillVO save_headVO = new ReceivableBillVO();
		
		
		/**
		 * �ۺ��̳Ǵ������ı�ͷ�ֶ�
		 */
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if(pk_org != null){
			save_headVO.setPk_org(pk_org);// ������֯
		}else {
			throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ�յ���"+ headVO.getSrcid() +" �Ĳ�����֯��NC����ϵͳ�в����ڣ�������֯��"+headVO.getPk_org());
		}
		
		save_headVO.setAttributeValue("billdate", new UFDate(headVO.getBilldate()));// ��������
		save_headVO.setAttributeValue("money", headVO.getMoney());// ԭ�ҽ��
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// ��֯���ҽ��
		save_headVO.setAttributeValue("rate", 1);// ��֯���һ���
		save_headVO.setAttributeValue("def11", headVO.getHyperlinks());//�ۺ��̳Ǹ���������
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// Ӧ������code
		save_headVO.setAttributeValue("pk_tradetypeid",headVO.getPk_tradetypeid());// Ӧ������
		save_headVO.setAttributeValue("def7", headVO.getBusinesstype());// ҵ������
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("billstatus", 2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setAttributeValue("pk_billtype", "F0");// �������ͱ���
		save_headVO.setAttributeValue("billclass", "ys");// ���ݴ���,Ĭ��ΪӦ�յ�
		save_headVO.setAttributeValue("syscode", 0);// ��������ϵͳ��Ĭ��Ϊ0��0=Ӧ��ϵͳ
		save_headVO.setAttributeValue("src_syscode", 0);// ������Դϵͳ
		save_headVO.setAttributeValue("billmaker", getUserPkByCode(DefaultOperator));// �Ƶ��ˣ�Ĭ��Ϊ�����ۺ��̳�ϵͳ
		save_headVO.setAttributeValue("objtype", 0); //��������Ĭ��Ϊ0=�ͻ�
		save_headVO.setAttributeValue("creator",getUserPkByCode(DefaultOperator));// ������
		
		save_headVO.setStatus(VOStatus.NEW);
		
		UFDouble headMoney = new UFDouble(headVO.getMoney());
		UFDouble bodyTotalMoney = new UFDouble();
		
		/**
		 * �ۺ��̳Ǳ��Ĵ������ֶ�
		 */
		List<ReceivableBillItemVO> bodylist = new ArrayList<>();
		for (ReceivableBodyVO receivableBodyVO : bodyVOs) {
			ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			
			save_bodyVO.setAttributeValue("scomment", save_bodyVO.getScomment());// ժҪ
			save_bodyVO.setAttributeValue("customer", getcustomer(receivableBodyVO.getCustomer()));// �ͻ�
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(receivableBodyVO.getQuantity_de()));// �跽����
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(receivableBodyVO.getTaxrate()));// ˰��
			save_bodyVO.setAttributeValue("taxprice", receivableBodyVO.getTaxprice());// ��˰����
			save_bodyVO.setAttributeValue("local_tax_de", receivableBodyVO.getLocal_tax_de());// ˰��
			save_bodyVO.setAttributeValue("notax_de", receivableBodyVO.getNotax_de());// �跽ԭ����˰���
			save_bodyVO.setAttributeValue("money_de", new UFDouble(receivableBodyVO.getMoney_de()));// �跽ԭ�ҽ��
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(receivableBodyVO.getMoney_bal()));// ԭ�����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(receivableBodyVO.getMoney_bal()));// ��֯�������
			save_bodyVO.setAttributeValue("local_money_de", new UFDouble(receivableBodyVO.getMoney_bal()));// ��֯�������
			save_bodyVO.setAttributeValue("local_notax_de", new UFDouble(receivableBodyVO.getMoney_bal()));// ��֯������˰���
			save_bodyVO.setAttributeValue("def12", receivableBodyVO.getRowid()); // �ۺ��̳�ϵͳ������ID
			save_bodyVO.setAttributeValue("def25", receivableBodyVO.getRatio()); // ���˱���
			save_bodyVO.setAttributeValue("def26", new UFDouble(receivableBodyVO.getMoney_de())); //Ӧ���˽��
			save_bodyVO.setAttributeValue("memo", receivableBodyVO.getMemo());//��ע
			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass","ys");// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",save_headVO.getPk_tradetypeid());// Ӧ������
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
			save_bodyVO.setAttributeValue("objtype", "0");// ��������Ĭ��Ϊ0=�ͻ�
			
			String project = getpk_projectByCode(receivableBodyVO.getProject());
			if(project == null){
				throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ�յ���"+ headVO.getSrcid() +" ����Ŀ��NC����ϵͳ�в����ڣ���Ŀ����:"+project);
			}
			save_bodyVO.setAttributeValue("project", project); //��Ŀ

			bodyTotalMoney = bodyTotalMoney.add(new UFDouble(receivableBodyVO.getMoney_bal()));


			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new ReceivableBillItemVO[0]));

		if(!(headMoney.compareTo(bodyTotalMoney) == 0)){
			throw new BusinessException("�ۺ��̳�Ӧ�յ���"+ headVO.getSrcid() +" �ı�ͷ���������ܽ���ȣ�");
		}
		
		return aggvo;
		
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
	
}
