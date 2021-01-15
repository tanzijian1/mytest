package nc.bs.tg.outside.paybill;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.dao.DAOException;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.framework.common.NCLocator;
import nc.bs.tg.outside.ebs.utils.recbill.RecbillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillConvertor;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.utils.BillUtils;
import nc.bs.tg.outside.utils.DocInfoQryUtils;
import nc.itf.tg.outside.ITGSyncService;
import nc.itf.uap.pf.IWorkflowMachine;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.cdm.innerpay.action.returnBackAction;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pf.change.PfUtilBaseTools;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ISuperVO;
import nc.vo.pub.VOStatus;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.pf.workflow.IPFActionName;
import nc.vo.pub.workflownote.WorkflownoteVO;
import vo.tg.outside.HCMSBBillBodyVo;
import vo.tg.outside.HCMSBBillHeadVo;
import vo.tg.outside.PayBillBodyVO;
import vo.tg.outside.PayBillHeadVO;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class HCMSocialSecurityPaybillToNc extends PayBillUtils implements ITGSyncService{

	//public static final String DefaultOperator = "RLZY";// Ĭ���Ƶ���
	public static final String HCMSalaryOperatorName = "RLZY";// HCMĬ�ϲ���Ա
	
	public AggPayBillVO onTranBill(HCMSBBillHeadVo headVo,List<HCMSBBillBodyVo> bodyVos) throws BusinessException{
		AggPayBillVO aggvo = new AggPayBillVO();
		 
		PayBillVO save_headVO = new PayBillVO();
		
		//У���տ��˻���Ϣ
		//���ñ�ͷ��Ϣ
		
		String pk_org = getPk_orgByCode(headVo.getPk_org());
		headVo.setPk_org(pk_org);
		/*save_headVO.setAttributeValue("pk_org", headVo.getPk_org());
		save_headVO.setAttributeValue("pk_balatype", getBalatypePkByCode(headVo.getPk_balatype()));

		save_headVO.setAttributeValue("billdate", new UFDate(headVo.getBilldate()));
		save_headVO.setAttributeValue("def67", headVo.getDef67().substring(0,7));

		save_headVO.setAttributeValue("def16", headVo.getTotal_gs());
		save_headVO.setAttributeValue("def24", headVo.getTotal_gr());
		
		save_headVO.setAttributeValue("money", headVo.getTotal_gsgr());
		save_headVO.setAttributeValue("def15", headVo.getDef30());
		
		save_headVO.setAttributeValue("def1", headVo.getSrcid());
		save_headVO.setAttributeValue("def2", headVo.getSrcbillno());*/
		
		save_headVO.setPk_org(headVo.getPk_org());
		save_headVO.setPk_balatype(getBalatypePkByCode(headVo.getPk_balatype()));
		save_headVO.setBilldate(new UFDate(headVo.getBilldate()));
		if(StringUtils.isNotBlank(headVo.getDef67())){
			if(headVo.getDef67().length() >=7){
				save_headVO.setDef67(headVo.getDef67().substring(0,7));
			}else {
				save_headVO.setDef67(headVo.getDef67());
			}
		}
		save_headVO.setDef16(headVo.getTotal_gs());
		save_headVO.setDef24(headVo.getTotal_gr());
		if(StringUtils.isNotBlank(headVo.getTotal_gsgr())){
			save_headVO.setMoney(new UFDouble(headVo.getTotal_gsgr()));
		}
		save_headVO.setDef15(headVo.getDef30());
		save_headVO.setDef1(headVo.getSrcid());
		save_headVO.setDef2(headVo.getSrcbillno());
		
		
		
		//���ñ�ͷĬ���ֶ�
		//������֯
		String pk_vid = getvidByorg((String) save_headVO.getAttributeValue("pk_org"));
		//String pk_tradetype = (String) save_headVO.getAttributeValue("pk_tradetype");
		//String pk_tradetypeid = (String) save_headVO.getAttributeValue("pk_tradetypeid");
		/*save_headVO.setAttributeValue("pk_fiorg",(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("pk_fiorg_v", pk_vid);
		save_headVO.setAttributeValue("sett_org",(String) save_headVO.getAttributeValue("pk_org"));
		save_headVO.setAttributeValue("sett_org_v", pk_vid);
		save_headVO.setAttributeValue("creationtime",save_headVO.getAttributeValue("billdate"));
		save_headVO.setAttributeValue("objtype", 1);
		save_headVO.setAttributeValue("billclass", "fk");
		save_headVO.setAttributeValue("approvestatus", -1);
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");
		save_headVO.setAttributeValue("pk_tradetype", "F3-Cxx-030");
		save_headVO.setAttributeValue("pk_tradetypeid", "10013410000000568S8U");
		save_headVO.setAttributeValue("pk_billtype", "F3");// �������ͱ���
		save_headVO.setAttributeValue("billdate",new UFDate(headVo.getBilldate()));// ��������
		save_headVO.setAttributeValue("syscode", 1);// ��������ϵͳ��Ĭ��Ϊ1��1=Ӧ��ϵͳ
		save_headVO.setAttributeValue("src_syscode", 1);// ������Դϵͳ
		save_headVO.setAttributeValue("pk_currtype", "1002Z0100000000001K1");// ����
		save_headVO.setAttributeValue("billstatus", 2);// ����״̬,Ĭ��Ϊ2������
		save_headVO.setAttributeValue("pk_group", "000112100000000005FD");// �������ţ�Ĭ��Ϊʱ������
		save_headVO.setAttributeValue("billmaker", getUserByCode(HCMSalaryOperatorName));// �Ƶ���
		save_headVO.setAttributeValue("objtype", 1); // �������� 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�  
		save_headVO.setAttributeValue("creator", RecbillUtils.getUtils().getUserPkByCode(HCMSalaryOperatorName));// ������
		save_headVO.setAttributeValue("pk_busitype", "1001ZZ1000000057GMMB"); //ҵ������
*/		
		save_headVO.setPk_fiorg(save_headVO.getPk_org());
		save_headVO.setPk_fiorg_v(pk_vid);
		save_headVO.setSett_org(save_headVO.getPk_org());
		save_headVO.setSett_org_v(pk_vid);
		save_headVO.setCreationtime(new UFDateTime());
		save_headVO.setObjtype(1);
		save_headVO.setBillclass("fk");
		save_headVO.setApprovestatus(-1);
		save_headVO.setPk_group("000112100000000005FD");
		save_headVO.setPk_tradetype("F3-Cxx-030");
		save_headVO.setPk_tradetypeid("10013410000000568S8U");
		save_headVO.setPk_billtype("F3");
		save_headVO.setBilldate(new UFDate(headVo.getBilldate()));
		save_headVO.setSyscode(1);
		save_headVO.setSrc_syscode(1);
		save_headVO.setPk_currtype("1002Z0100000000001K1");
		save_headVO.setBillstatus(2);
		save_headVO.setBillmaker(getUserByCode(HCMSalaryOperatorName));
		save_headVO.setObjtype(1);
		save_headVO.setCreator(RecbillUtils.getUtils().getUserPkByCode(HCMSalaryOperatorName));
		save_headVO.setStatus(VOStatus.NEW);
		
		// ���� ��˾���ʡ��ʱ����ֶ� 2020-11-23 zzy start
		List<Map<String, String>> budgetsubNames = getLinkCompany(pk_org);
		if(budgetsubNames != null && budgetsubNames.size() >0){
			for (Map<String, String> names : budgetsubNames) {
				save_headVO.setAttributeValue("def91", names.get("factorvalue5"));// ��˾����
				save_headVO.setAttributeValue("def92", names.get("factorvalue4"));// �Ƿ��ʱ���
			}
		}
		// ���� ��˾���ʡ��ʱ����ֶ� 2020-11-23 zzy end
		
		List<PayBillItemVO> bodylist = new ArrayList<>();
		for (HCMSBBillBodyVo payBillBodyVO : bodyVos) {
			PayBillItemVO save_bodyVO = new PayBillItemVO();
			//save_bodyVO.setAttributeValue("pk_org", save_headVO.getPk_org());// Ӧ�ղ�����֯
			//save_bodyVO.setAttributeValue("def18", getPk_orgByCode(payBillBodyVO.getDef18()));// ��ͬǩ����˾
			
			save_bodyVO.setPk_org(save_headVO.getPk_org());
			
			
			//save_bodyVO.setDef18(getPk_orgByCode(payBillBodyVO.getDef18()));
			if(StringUtils.isNotBlank(payBillBodyVO.getDef18())){
				String pk_supplier = getPk_orgByCode(payBillBodyVO.getDef18());
				if(pk_supplier != null){
					save_bodyVO.setDef18(pk_supplier);
				}
			}
		
			
			//save_bodyVO.setAttributeValue("def30", payBillBodyVO.getDef30());// ��������
			/*String pk_dept = getPk_deptByCode(payBillBodyVO.getDef30());
			if(pk_dept != null){
				save_bodyVO.setAttributeValue("def30", pk_dept);// ��������
			}else {
				throw new BusinessException("��NC���Ҳ�����Ӧ�Ĳ��ű��룬���飡���ݺ�srcid:" + headVo.getSrcid());
			}*/
			
			if(payBillBodyVO.getDef30() != null){
				Map<String, String> map_dept = getDefdocInfo(payBillBodyVO.getDef30(), "zdy046");
				if(map_dept.size() >0 && StringUtils.isNotBlank(map_dept.get("pk_defdoc"))){
					save_bodyVO.setDef30(map_dept.get("pk_defdoc"));
				}else{
					throw new BusinessException("�������Բ���Ϊ�գ����飡���ݺ�srcid:" + headVo.getSrcid());
				}
			
			}else {
				throw new BusinessException("�������Բ���Ϊ�գ����飡���ݺ�srcid:" + headVo.getSrcid());
			}
			//save_bodyVO.setAttributeValue("def22", payBillBodyVO.getDef22());// ����
			save_bodyVO.setDef22(payBillBodyVO.getDef22());
		
			//������㷽ʽΪ�Զ�����ʱ���籣������A12488 
			//save_bodyVO.setAttributeValue("supplier", getCustomerPK(payBillBodyVO.getSsname()));
			save_bodyVO.setSupplier(getCustomerPK(payBillBodyVO.getSsname()));
			String num = getBankNUm(getCustomerPK(payBillBodyVO.getSsname()),payBillBodyVO.getRecaccount());
			
			if(num != null){
				//save_bodyVO.setAttributeValue("recaccount", num);
				save_bodyVO.setRecaccount(num);
			}else {
				throw new BusinessException("�籣���Ŀ��������˻����籣������������飡���ݺ�srcid:" + headVo.getSrcid());
			}
						
			/*save_bodyVO.setAttributeValue("def12", payBillBodyVO.getDef12());// ���Ϲ�˾����
			save_bodyVO.setAttributeValue("def11", payBillBodyVO.getDef11());// ���ϸ��˲���
			save_bodyVO.setAttributeValue("def51", payBillBodyVO.getDef51());// ����ҽ�ƹ�˾����
			save_bodyVO.setAttributeValue("def52", payBillBodyVO.getDef52());// ����ҽ�Ƹ��˲���
			save_bodyVO.setAttributeValue("def10", payBillBodyVO.getDef10());// ����ҽ�ƹ�˾����
			save_bodyVO.setAttributeValue("def9", payBillBodyVO.getDef9());// ����ҽ�Ƹ��˲���
			save_bodyVO.setAttributeValue("def8", payBillBodyVO.getDef8());// ʧҵ��˾����
			save_bodyVO.setAttributeValue("def7", payBillBodyVO.getDef7());// ʧҵ���˲���
			save_bodyVO.setAttributeValue("def5", payBillBodyVO.getDef5());// ���˹�˾����
			save_bodyVO.setAttributeValue("def4", payBillBodyVO.getDef4());// ���˸��˲���
			save_bodyVO.setAttributeValue("def3", payBillBodyVO.getDef3());// ������˾����
*/			
			save_bodyVO.setDef12(payBillBodyVO.getDef12());
			save_bodyVO.setDef11(payBillBodyVO.getDef11());
			save_bodyVO.setDef51(payBillBodyVO.getDef51());
			save_bodyVO.setDef52(payBillBodyVO.getDef52());
			save_bodyVO.setDef10(payBillBodyVO.getDef10());
			save_bodyVO.setDef9(payBillBodyVO.getDef9());
			save_bodyVO.setDef8(payBillBodyVO.getDef8());
			save_bodyVO.setDef7(payBillBodyVO.getDef7());
			save_bodyVO.setDef5(payBillBodyVO.getDef5());
			save_bodyVO.setDef4(payBillBodyVO.getDef4());
			save_bodyVO.setDef3(payBillBodyVO.getDef3());
			

			/*save_bodyVO.setAttributeValue("def2", payBillBodyVO.getDef2());// �������˲���
			save_bodyVO.setAttributeValue("def55", payBillBodyVO.getDef55());// ��˾���ֺϼ�
			save_bodyVO.setAttributeValue("def56", payBillBodyVO.getDef56());// ���˲��ֺϼ�
			
			save_bodyVO.setAttributeValue("def57", payBillBodyVO.getDef57()); //�ش󼲲�ҽ�Ʋ���-��˾����
			save_bodyVO.setAttributeValue("def58", payBillBodyVO.getDef58()); //�ش󼲲�ҽ�Ʋ���-���˲���
			save_bodyVO.setAttributeValue("def59", payBillBodyVO.getDef59()); //ҽ�������˻�-��˾����
			save_bodyVO.setAttributeValue("def60", payBillBodyVO.getDef60()); //ҽ�������˻�-���˲���
			save_bodyVO.setAttributeValue("def61", payBillBodyVO.getDef61()); //����-��˾����
			save_bodyVO.setAttributeValue("def62", payBillBodyVO.getDef62()); //����-���˲���
			
			save_bodyVO.setAttributeValue("money_de", new UFDouble(payBillBodyVO.getMoney_de()));// ��˾+���˺ϼ�
*/			
			
			save_bodyVO.setAttributeValue("def60", payBillBodyVO.getDef60()); //ҽ�������˻�-���˲���
			save_bodyVO.setAttributeValue("def61", payBillBodyVO.getDef61()); //����-��˾����
			save_bodyVO.setAttributeValue("def62", payBillBodyVO.getDef62()); //����-���˲���
			
			save_bodyVO.setDef2(payBillBodyVO.getDef2());
			save_bodyVO.setDef55(payBillBodyVO.getDef55());
			save_bodyVO.setDef56(payBillBodyVO.getDef56());
			save_bodyVO.setDef57(payBillBodyVO.getDef57());
			save_bodyVO.setDef58(payBillBodyVO.getDef58());
			save_bodyVO.setDef59(payBillBodyVO.getDef59());
			/*save_bodyVO.setDef60(payBillBodyVO.getDef8());
			save_bodyVO.setDef61(payBillBodyVO.getDef7());
			save_bodyVO.setDef62(payBillBodyVO.getDef5());*/
			save_bodyVO.setMoney_de(new UFDouble(payBillBodyVO.getMoney_de()));
			

			/*save_bodyVO.setAttributeValue("local_money_bal", new UFDouble(payBillBodyVO.getMoney_de()));// ����=ԭ�ң�����һ���� 
			save_bodyVO.setAttributeValue("local_money_de", new UFDouble(payBillBodyVO.getMoney_de()));// ���
			save_bodyVO.setAttributeValue("local_notax_de", new UFDouble(payBillBodyVO.getMoney_de()));// ���
			save_bodyVO.setAttributeValue("money_bal", new UFDouble(payBillBodyVO.getMoney_de()));// ���
			save_bodyVO.setAttributeValue("pausetransact", UFBoolean.FALSE);// �����־
			save_bodyVO.setAttributeValue("billdate", save_headVO.getBilldate());// ��������

			save_bodyVO.setAttributeValue("pk_balatype", save_headVO.getPk_balatype());// ���㷽ʽ
			save_bodyVO.setAttributeValue("pk_group", save_headVO.getPk_group());// ��������
			save_bodyVO.setAttributeValue("pk_billtype",save_headVO.getPk_billtype());// �������ͱ���
			save_bodyVO.setAttributeValue("billclass",save_headVO.getBillclass());// ���ݴ���
			save_bodyVO.setAttributeValue("pk_tradetype","F3-Cxx-030");// Ӧ������code
			save_bodyVO.setAttributeValue("pk_tradetypeid","10013410000000568S8U");// Ӧ������
			save_bodyVO.setAttributeValue("busidate", save_headVO.getBilldate());// ��������
			save_bodyVO.setAttributeValue("objtype", 1);// �������� 2=���ţ�3=ҵ��Ա��1=��Ӧ�̣�0=�ͻ�  
			save_bodyVO.setAttributeValue("direction", 1);// ����
			save_bodyVO.setAttributeValue("pk_currtype",save_headVO.getPk_currtype());// ����
			save_bodyVO.setAttributeValue("rate", 1);// ��֯���һ���
*/			
			save_bodyVO.setLocal_money_bal(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setLocal_money_de(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setLocal_notax_de(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setMoney_bal(new UFDouble(payBillBodyVO.getMoney_de()));
			save_bodyVO.setPausetransact(UFBoolean.FALSE);
			save_bodyVO.setBilldate(save_headVO.getBilldate());
			save_bodyVO.setPk_balatype(save_headVO.getPk_balatype());
			save_bodyVO.setPk_group(save_headVO.getPk_group());
			save_bodyVO.setPk_billtype(save_headVO.getPk_billtype());
			save_bodyVO.setBillclass(save_headVO.getBillclass());

			save_bodyVO.setPk_tradetype("F3-Cxx-030");
			save_bodyVO.setPk_tradetypeid("10013410000000568S8U");
			save_bodyVO.setBusidate(save_headVO.getBilldate());
			save_bodyVO.setObjtype(1);
			save_bodyVO.setDirection(1);
			save_bodyVO.setPk_currtype(save_headVO.getPk_currtype());
			save_bodyVO.setRate(new UFDouble("1"));
			
			save_bodyVO.setStatus(VOStatus.NEW);
			bodylist.add(save_bodyVO);
		}
		aggvo.setParentVO(save_headVO);
		aggvo.setChildrenVO(bodylist.toArray(new PayBillItemVO[0]));
		
		return aggvo;
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
	 * ��ȡorg_orgs��pk_orgֵ
	 * 
	 * @param pk_org����
	 * 
	 * @return ����ת�����pk_vidֵ
	 */
	public String getvidByorg(String pk_org) throws DAOException {
		String sql = "SELECT pk_vid from org_orgs_v where pk_org = '" + pk_org
				+ "' and enablestate = 2 and nvl(dr,0)=0";
		String pk_vid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return pk_vid;
	}
	
	/**
	 * ��ȡ����pk
	 * @param code  
	 * @return
	 */
	public String getCustomerPK(String code){
		String result = null;
		//String sql = "SELECT pk_cust_sup FROM bd_cust_supplier WHERE CODE = '"+code+"' AND custenablestate = '2' AND NVL(dr,0)=0";
		String sql = "SELECT pk_customer FROM bd_customer WHERE (CODE = '"+code+"' or name = '"+ code + "') AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ȡ��������
	 */
	public String getCustomerName(String code){
		String result = null;
		//String sql = "SELECT name FROM bd_cust_supplier WHERE CODE = '"+code+"' AND custenablestate = '2' AND NVL(dr,0)=0";
		String sql = "SELECT name FROM bd_customer WHERE (CODE = '"+code+"' or name = '"+ code + "') AND NVL(dr,0)=0";
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * ��ȡ���������˻�
	 * @param pk_org
	 * @return
	 */
	public String getCustomerBankNum(String pk_org,String accnum){
		String result = null;
		String sql = "SELECT a.accnum FROM bd_bankaccsub a inner join bd_bankaccuse b on a.pk_bankaccbas = b.pk_bankaccbas and b.pk_org = '"+pk_org+"' where a.accnum = '"+ accnum +"' AND NVL(a.dr,0)=0";
		
		try {
			result = (String) getBaseDAO().executeQuery(sql,
					new ColumnProcessor());
			if (result != null) {
				return result;
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
		//String sql = "SELECT a.accnum FROM bd_bankaccbas a where a.name = '"+ name +"'and a.accnum = '"+ accnum +"'";
		
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
	    * ͨ���û�����õ��û�pk
	 * @throws DAOException 
	    */
	public String getUserByCode(String psndoccode) throws DAOException{
			String pk=(String)getBaseDAO().executeQuery("select  cuserid  from sm_user where user_code='"+psndoccode+"'", new ColumnProcessor());
		    return pk;
	}
	
	/**
	 * ���ݡ����ű��롿��ȡ����
	 * 
	 * @param code
	 * @return
	 */
	public String getPk_deptByCode(String code) {
		
		String sql = "select pk_dept from org_dept  where code = '"
				+ code + "' and dr=0 ";
		
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
	 * �Զ��嵵��
	 * 
	 * @param key
	 * @param pk_project
	 * @param
	 * @return
	 * @throws BusinessException
	 */
	public Map<String, String> getDefdocInfo(String key, String listcode)
			throws BusinessException {
		// listcode = "zdy046";
		return DocInfoQryUtils.getUtils().getDefdocInfo(key, listcode);
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
	
	@Override
	public String onSyncInfo(HashMap<String, Object> info, String methodname) throws BusinessException {

		Map<String, String> resultInfo = new HashMap<String, String>();
			
		// ������id
		InvocationInfoProxy.getInstance().setGroupId("000112100000000005FD");
		// �����û�����
		InvocationInfoProxy.getInstance().setUserDataSource(DataSourceName);
		// �����û�id
		InvocationInfoProxy.getInstance().setUserId(getUserByCode(HCMSalaryOperatorName));
		InvocationInfoProxy.getInstance().setUserCode(HCMSalaryOperatorName);
		
		PayBillConvertor convertor = new PayBillConvertor();
		// �������Ϣ
		JSONObject jsonData = (JSONObject) info.get("data");// ������
		String jsonhead = jsonData.getString("headInfo");// ��ϵͳ��Դ��ͷ����
		String jsonbody = jsonData.getString("bodyInfo");// ��ϵͳ��Դ��������
		if (jsonData == null || jsonhead == null || jsonbody == null) {
			throw new BusinessException("������Ϊ�գ����飡json:" + jsonData);
		}
		HashMap<String, Object> value = JSONObject.parseObject(jsonData.toJSONString(),new TypeReference<HashMap<String, Object>>() {});

		// ת��json
		HCMSBBillHeadVo headVO = JSONObject.parseObject(jsonhead,HCMSBBillHeadVo.class);
		List<HCMSBBillBodyVo> bodyVOs = JSONObject.parseArray(jsonbody,HCMSBBillBodyVo.class);
		if (headVO == null || bodyVOs == null || bodyVOs.size() < 1) {
			throw new BusinessException("������ת��ʧ�ܣ����飡json:" + jsonData);
		}
		
		String srcid = headVO.getSrcid();// ��ϵͳҵ�񵥾�ID
		String srcno = headVO.getSrcbillno();// ��ϵͳҵ�񵥾ݵ��ݺ�

		String billqueue = methodname + ":" + srcid;
		String billkey = methodname + ":" + srcno;
		BillUtils.addBillQueue(billqueue);
		
		
		try {
			AggPayBillVO aggVO = (AggPayBillVO) getBillVO(AggPayBillVO.class,"isnull(dr,0)=0 and def1 = '" + srcid + "'");
			if (aggVO != null) {
				throw new BusinessException("��" + billkey + "��,NC�Ѵ��ڶ�Ӧ��ҵ�񵥾ݡ�"
						+ aggVO.getParentVO().getAttributeValue("billno")
						+ "��,�����ظ��ϴ�!");
			}
			// AggPayBillVO billvo = (AggPayBillVO) convertor.castToBill(value,
			// AggPayBillVO.class, aggVO);
			AggPayBillVO billvo = onTranBill(headVO, bodyVOs);
			HashMap eParam = new HashMap();
			eParam.put(PfUtilBaseTools.PARAM_NOTE_CHECKED,PfUtilBaseTools.PARAM_NOTE_CHECKED);
			//����
			Object obj = (AggPayBillVO[]) getPfBusiAction().processAction("SAVE", "F3", null, billvo, null, eParam);
			AggPayBillVO[] billvos = (AggPayBillVO[]) obj;
			
			//��һ���ύ����
			WorkflownoteVO worknoteVO = NCLocator.getInstance().lookup(IWorkflowMachine.class)
					   .checkWorkFlow("SAVE", "F3", billvos[0], eParam);
			obj = (AggPayBillVO[]) getPfBusiAction().processAction("SAVE", "F3", worknoteVO, billvos[0], null, eParam);
			
			resultInfo.put("billid", billvos[0].getPrimaryKey());
			resultInfo.put("billno", (String) billvos[0].getParentVO().getAttributeValue("billno"));
		} catch (Exception e) {
			throw new BusinessException("��" + billkey + "��," + e.getMessage(),
					e);
		} finally {
			BillUtils.removeBillQueue(billqueue);
		}
		return JSON.toJSONString(resultInfo);
	
	}
	
	/**
	 * ��ȡ�Ƿ��ʱ����͹�˾���� ���Ի����ĵ���������XM02����ʽ����XM01
	 * @param pk_org
	 * @return
	 * @throws BusinessException
	 */
	 protected List<Map<String, String>> getLinkCompany(String pk_org) throws BusinessException {
		 List<Map<String, String>> result = null;
		 StringBuffer query = new StringBuffer();
		 query.append("select factorvalue4,factorvalue5 from fip_docview_b where pk_classview=(select pk_classview from fip_docview ta where ta.viewcode ='XM01') and factorvalue2 = '"
				 + pk_org + "'");
		 try {
			 result = (List<Map<String, String>>) getBaseDAO().executeQuery(
					 query.toString(), new MapListProcessor());
		 } catch (Exception e) {
			 throw new BusinessException(e.getMessage(), e);
		 }
		 return result;
	 } 

}
