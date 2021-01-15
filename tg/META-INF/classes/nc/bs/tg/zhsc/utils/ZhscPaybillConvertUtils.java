package nc.bs.tg.zhsc.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.BaseDAO;
import nc.bs.os.outside.TGOutsideUtils;
import nc.bs.tg.call.vo.PayableBodyVO;
import nc.bs.tg.call.vo.PayableHeadVO;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.arap.payable.AggPayableBillVO;
import nc.vo.arap.payable.PayableBillItemVO;
import nc.vo.arap.payable.PayableBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;


public class ZhscPaybillConvertUtils{
	

public static final String DefaultOperator = "LLZHSC";// Ĭ���Ƶ���
	
	private static ZhscPaybillConvertUtils convertUtils;
	
	public static ZhscPaybillConvertUtils getInstance() {
		if (convertUtils == null) {
			convertUtils = new ZhscPaybillConvertUtils();
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
	
	public AggPayableBillVO onTranBill(PayableHeadVO headVO,
			List<PayableBodyVO> bodyVOs) throws BusinessException{
		
		AggPayableBillVO aggvo  = new AggPayableBillVO();
		PayableBillVO save_headVO = new PayableBillVO();
		
		
		/**
		 * �ۺ��̳Ǵ������ı�ͷ�ֶ�
		 */
		String pk_org = getPk_orgByCode(headVO.getPk_org());
		if(pk_org != null){
			save_headVO.setPk_org(pk_org);// ������֯
		}else {
			throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ������"+ headVO.getSrcid() +" �Ĳ�����֯��NC����ϵͳ�в����ڣ�������֯��"+headVO.getPk_org());
		}
		
		save_headVO.setAttributeValue("billdate", new UFDate(headVO.getBilldate()));// ��������
		save_headVO.setAttributeValue("money", headVO.getMoney());// ԭ�ҽ��
		save_headVO.setAttributeValue("def1", headVO.getSrcid());// ��ϵͳ����
		save_headVO.setAttributeValue("def2", headVO.getSrcbillno());// ��ϵͳ����
		save_headVO.setAttributeValue("local_money", headVO.getMoney());// ��֯���ҽ��
		save_headVO.setAttributeValue("rate", 1);// ��֯���һ���
		save_headVO.setAttributeValue("def35", headVO.getHyperlinks());//�ۺ��̳Ǹ���������
		save_headVO.setAttributeValue("pk_tradetype", headVO.getPk_tradetype());// Ӧ������code
		save_headVO.setAttributeValue("pk_tradetypeid",getPkTradeTypeid(save_headVO.getPk_tradetype()));// Ӧ������
		save_headVO.setAttributeValue("def36", headVO.getBusinesstype());// ҵ������
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("billstatus", 2);// ����״̬,Ĭ��Ϊ������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setAttributeValue("pk_billtype", "F1");// �������ͱ���
		save_headVO.setAttributeValue("billclass", "yf");// ���ݴ���,Ĭ��ΪӦ�յ�
		save_headVO.setAttributeValue("syscode", 1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		save_headVO.setAttributeValue("billmaker", getUserPkByCode(DefaultOperator));// �Ƶ��ˣ�Ĭ��Ϊ�����ۺ��̳�ϵͳ
		save_headVO.setAttributeValue("objtype", 1); //��������Ĭ��Ϊ1=��Ӧ��
		save_headVO.setAttributeValue("src_syscode", 1);// ������Դϵͳ
		save_headVO.setAttributeValue("creationtime", new UFDateTime());
		

		/*save_headVO.setAttributeValue("billyear", null);// ���ݻ�����
		save_headVO.setAttributeValue("billperiod", null);// ���ݻ���ڼ�
		save_headVO.setAttributeValue("scomment", null);// ժҪ
		save_headVO.setAttributeValue("effectstatus", 0);// ��Ч״̬
		save_headVO.setAttributeValue("effectuser", null);// ��Ч��
		save_headVO.setAttributeValue("effectdate", null);// ��Ч����
		save_headVO.setAttributeValue("lastapproveid", null);// ����������
		save_headVO.setAttributeValue("grouplocal", null);// ���ű��ҽ��
		save_headVO.setAttributeValue("globallocal", null);// ȫ�ֱ��ҽ��

		save_headVO.setAttributeValue("approver", null);// �����
		save_headVO.setAttributeValue("approvedate", null);// ���ʱ��
		save_headVO.setAttributeValue("lastadjustuser", null);*/	// ���յ�����
		//save_headVO.setAttributeValue("pk_busitype", "1001341000000057FW29");// ҵ������
		//save_headVO.setAttributeValue("coordflag", null);// ����Эͬ��־
		//save_headVO.setAttributeValue("taxcountryid", null);// ��˰��
		save_headVO.setAttributeValue("creator",getUserPkByCode(DefaultOperator));// ������
		
		save_headVO.setAttributeValue("approvestatus", 3);// ����״̬
		
		save_headVO.setStatus(VOStatus.NEW);
		
		UFDouble headMoney = new UFDouble(headVO.getMoney());
		UFDouble bodyTotalMoney = new UFDouble(0);
		
		String payaccount = TGOutsideUtils.getUtils().getOutsidInfo("ZHSCYFFKZH");
		String payaccountpk = null;
		if(StringUtils.isNotBlank(payaccount)){
			payaccountpk = getPayBankAccountPK(payaccount);
			if(payaccountpk == null){
				throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ������"+ headVO.getSrcid() +" �ĸ��������˻���NC����ϵͳ�в����ڣ����������˻�:"+payaccount);
			}
		}else {
			throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ������"+ headVO.getSrcid() +" �ĸ��������˻���NC����ϵͳ�в����ڣ�");
		}
		
		/**
		 * �ۺ��̳Ǳ��Ĵ������ֶ�
		 */
		List<PayableBillItemVO> bodylist = new ArrayList<>();
		for (PayableBodyVO payableBodyVO : bodyVOs) {
			
			//ReceivableBillItemVO save_bodyVO = new ReceivableBillItemVO();
			PayableBillItemVO save_bodyVO = new PayableBillItemVO();
			
			String pk_supplier = getSupplier(payableBodyVO.getCustomer());
			
			save_bodyVO.setAttributeValue("scomment", payableBodyVO.getScomment());// ժҪ
			save_bodyVO.setAttributeValue("supplier", pk_supplier);// �ͻ�
			save_bodyVO.setAttributeValue("quantity_de", new UFDouble(payableBodyVO.getQuantity_cr()));// �跽����
			save_bodyVO.setAttributeValue("taxrate", new UFDouble(payableBodyVO.getTaxrate()));// ˰��
			save_bodyVO.setAttributeValue("taxprice", payableBodyVO.getTaxprice());// ��˰����
			save_bodyVO.setAttributeValue("local_tax_cr", payableBodyVO.getLocal_tax_cr());// ˰��
			save_bodyVO.setAttributeValue("notax_cr", payableBodyVO.getNotax_cr());// �跽ԭ����˰���
			save_bodyVO.setAttributeValue("money_cr", new UFDouble(payableBodyVO.getMoney_cr()));// �跽ԭ�ҽ��
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(payableBodyVO.getMoney_bal()));// ԭ�����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
			save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(payableBodyVO.getMoney_bal()));// ��֯�������
			save_bodyVO.setAttributeValue("local_money_cr", new UFDouble(payableBodyVO.getMoney_bal()));// ��֯�������
			save_bodyVO.setAttributeValue("local_notax_cr", new UFDouble(payableBodyVO.getMoney_bal()));// ��֯������˰���
			save_bodyVO.setAttributeValue("def24", payableBodyVO.getRowid()); // �ۺ��̳�ϵͳ������ID
			save_bodyVO.setAttributeValue("def25", payableBodyVO.getRatio()); // ���˱���
			save_bodyVO.setAttributeValue("def26", new UFDouble(payableBodyVO.getMoney_cr())); //Ӧ���˽��
			save_bodyVO.setAttributeValue("memo", payableBodyVO.getMemo());//��ע
			save_bodyVO.setAttributeValue("objtype", 1); //��������Ĭ��Ϊ1=��Ӧ��
			
			save_bodyVO.setAttributeValue("payaccount", payaccountpk); //���������˻�
			
			String recaccount = getBankNUm(pk_supplier,payableBodyVO.getRecaccount());
			if(recaccount == null){
				throw new BusinessException("�ۺ��̳�ͬ��NC����ϵͳӦ������"+ headVO.getSrcid() +" �ĸ��������˻���NC����ϵͳ�в����ڣ��տ������˻�:"+recaccount);
			}
			save_bodyVO.setAttributeValue("recaccount", recaccount); //�տ������˻�

			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass","yf");// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype",save_headVO.getPk_tradetype());// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid",getPkTradeTypeid(save_headVO.getPk_tradetype()));// Ӧ������ save_headVO.getPk_tradetypeid()
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
			save_bodyVO.setAttributeValue("direction", -1);
			
			save_bodyVO.setStatus(VOStatus.NEW);
			bodyTotalMoney = bodyTotalMoney.add(new UFDouble(payableBodyVO.getMoney_cr()));
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayableBillItemVO[0]));

		if(!(bodyTotalMoney.compareTo(headMoney) == 0)){
			throw new BusinessException("�ۺ��̳�Ӧ������"+ headVO.getSrcid() +" �ı�ͷ���������ܽ���ȣ�");
		}
		
		return aggvo;
	}
	
	public String getPkTradeTypeid(String pkTradeType){
		String sql = "select pk_billtypeid from bd_billtype where pk_billtypecode = '" + pkTradeType+ "'";
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
	 * ��ȡ�����տ���������˻���pk
	 * @param bankNum ���������˻�
	 * @return
	 */
	public String getPayBankAccountPK(String bankAccount){
		String sql = "select pk_bankaccsub from bd_bankaccsub where accnum = '"
				+ bankAccount + "' and nvl(dr,0)=0 ";
		
		String pk_account = null;
		try {
			pk_account = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_account != null) {
				return pk_account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * ���ݸ��˾pk�������˺Ż�ȡ�˺�
	 */
	public String getBankNUm(String pkcust, String accnum){
		String result = null;
		
		String sql = "  select b.pk_bankaccsub "+
					" from bd_bankaccbas a , bd_bankaccsub b , bd_custbank c "+
						" where a.pk_bankaccbas = c.pk_bankaccbas and c.pk_bankaccsub != '~' and a.pk_bankaccbas = b.pk_bankaccbas "+
						" AND b.pk_bankaccbas = c.pk_bankaccbas and a.accnum = '"+accnum+"' and c.pk_cust = '"+pkcust+"' and c.accclass = '3'";
		
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ȡ�����տ������˻���pk
	 * @param bankNum ���������˻�
	 * @return
	 */
	public String getRecBankAccountPK(String bankAccount){
		String sql = "select pk_bankaccsub from bd_bankaccsub where accnum = '"
				+ bankAccount + "' and dr=0 ";
		
		String pk_account = null;
		try {
			pk_account = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (pk_account != null) {
				return pk_account;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
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
