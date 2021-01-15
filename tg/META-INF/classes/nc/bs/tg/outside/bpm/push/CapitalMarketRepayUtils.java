package nc.bs.tg.outside.bpm.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;

/**
 * �ʱ��г���������Ϣ�Խ�bpm
 * @author wenjie
 *
 */
public class CapitalMarketRepayUtils {
	static CapitalMarketRepayUtils utils;
	BaseDAO baseDAO = null;
	public static CapitalMarketRepayUtils getUtils() {
		if (utils == null) {
			utils = new CapitalMarketRepayUtils();
		}
		return utils;
	}
	
	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggMarketRepalayVO aggvo =  (AggMarketRepalayVO)bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		Map<String, Object> formData = pushBillsPrincipalToBpm(aggvo);
		Map<String, String> infoMap = PushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData, IBPMBillCont.getBillNameMap().get(billCode), 
						null, bill.getPrimaryKey(), aggvo.getParentVO().getDef20());
		aggvo.getParentVO().setDef34(null);//�������֪ͨBPM
		aggvo.getParentVO().setAttributeValue("def20", infoMap.get("taskID"));
		aggvo.getParentVO().setAttributeValue("def41", infoMap.get("openurl"));
		return aggvo;
	}
	
	
	/**
	 * ����
	 */
	private Map<String, Object> pushBillsPrincipalToBpm(AggMarketRepalayVO aggvo) throws BusinessException {
		MarketRepalayVO parentVO = aggvo.getParentVO();
		Map<String, Object> formData = new HashMap<String, Object>();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		String title = parentVO.getName();
		SendBPMUtils util = new SendBPMUtils();
		title = "~".equals(title) || title==null?"":title;
		purchase.put("Title", title);//����
		
		String pk_applicant = parentVO.getDef1();
		Map<String, String> psnInfoMap = util.getPerson_name(pk_applicant);
		psnInfoMap = psnInfoMap==null?new HashMap<String, String>():psnInfoMap;
		purchase.put("Applicant", psnInfoMap.get("name"));// ������
		Map<String, String> accountant_account = util.getRegionNameByPersonCode(psnInfoMap.get("code"));
		accountant_account = accountant_account==null?new HashMap<String, String>():accountant_account;
		purchase.put("ApplicantCode",accountant_account.get("userprincipalname"));// �������˺�
		
		String applicationorg = parentVO.getDef3();
		Map<String, String> orgInfoMap = util.getOrgmsg(applicationorg);
		orgInfoMap = orgInfoMap==null?new HashMap<String, String>():orgInfoMap;
		purchase.put("ApplicationCompany", orgInfoMap.get("name"));// ���빫˾
		purchase.put("ApplicationCompanyCode", orgInfoMap.get("code"));// ���빫˾����
		
		String pk_applicationdept = parentVO.getDef4();
		Map<String, String> deptInfoMap = util.getDeptmsg(pk_applicationdept);
		deptInfoMap = deptInfoMap==null?new HashMap<String, String>():deptInfoMap;
		String deptid = getHCMDeptID(pk_applicationdept);
		purchase.put("ApplicationDepartmentCode", deptid);// ���벿�ű���
		purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// ���벿��
		purchase.put("ApplicationDate",parentVO.getCreationtime().toString());//����ʱ��
		
		String pk_project = parentVO.getDef6();
		Map<String, String> projectMap = util.getProject_name(pk_project);
		projectMap = projectMap==null?new HashMap<String, String>():projectMap;
		purchase.put("ProjectName", projectMap.get("name")==null?"ʱ���ع�":projectMap.get("name"));//��Ŀ����
		
		String pk_payee = parentVO.getDef7();
		purchase.put("ReceivingUnit", util.getPayeeName(pk_payee));//�տλ
		
		String pk_bankaccsub = parentVO.getDef8();
		Map<String, String> bankMap = util.getBankInfo(pk_bankaccsub);
		bankMap = bankMap==null?new HashMap<String, String>():bankMap;
		purchase.put("ReceivingBankAccount", bankMap.get("accnum"));//�տ������˻�
		purchase.put("ReceivingBankAccountName", bankMap.get("accname"));//�տ������˻�����
		purchase.put("ReceivingBankName", bankMap.get("name"));//�տ������˻�������
		
		String pk_defdoc = parentVO.getDef35();
		Map<String, String> defMap = util.getFlowMsg(pk_defdoc);
		defMap = defMap==null?new HashMap<String, String>():defMap;
		purchase.put("PaymentType", defMap.get("name"));//��ʽ
		
		String pk_payer = parentVO.getDef9();
		purchase.put("PaymentUnit", util.getPayerName(pk_payer));//���λ
		
		UFDouble sumMoney = parentVO.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef11());
		purchase.put("TotalAmount", sumMoney);//��ͬ�ܽ��
		
		String pk_singleissue = parentVO.getDef5();
		Map<String, String> info = util.getContractInfo(pk_singleissue);
		purchase.put("ContractName", info.get("name"));//��ͬ����
		purchase.put("ContractCode", info.get("billno"));//��ͬ����
		purchase.put("FinancingType", info.get("type"));//�������� ȡҵ������
		purchase.put("UseContent", parentVO.getBig_text_b());//�ÿ�����
		
		purchase.put("RelatedProcesses", IBPMBillCont.PROCESSNAME_SD08);//�������
		// ����
		String Attachments = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggvo.getPrimaryKey());
		if (fileVOs != null && fileVOs.size() > 0) {
			for (int i = 0; i < fileVOs.size(); i++) {
				Attachments += fileVOs.get(i).getDocument_name() + "&"
						+ fileVOs.get(i).getFile_id() + ";";
			}
			purchase.put("Attachment",
					Attachments.substring(0, Attachments.lastIndexOf(";")));
		} else {
			purchase.put("Attachment", null);
		}
		
		UFDouble m = parentVO.getDef13()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef13());
		purchase.put("AlreadyPaid", m);//�ۼ��Ѹ�����
		
		purchase.put("RequestAmount", 
				parentVO.getDef23()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef23()));//��������
		
		purchase.put("AlreadyRates", 
				parentVO.getDef14()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef14()));//�ۼ��Ѹ���Ϣ
		purchase.put("RequestRates", 
				parentVO.getDef24()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef24()));//���������Ϣ
		
		purchase.put("Remarks", parentVO.getDef40());//��ע
		purchase.put("BarCode", parentVO.getDef21());//Ӱ�����
		
		purchase.put("ThisRequestAmount", 
				parentVO.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef12()));//��������� def12
		purchase.put("SecondaryIncome", 
				parentVO.getDef46()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef46()));//�μ����� def46
		purchase.put("RelatedCosts", 
				parentVO.getDef45()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef45()));//��ط��� def45
		formData.put("T_BankRepayment", purchase);
		
		Map<String, Object> purchasebody = new HashMap<String, Object>();// ��������
		
		String def33 = parentVO.getDef33();
		Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils().getDefdocInfo(def33);
		invoiceTypeMap = invoiceTypeMap==null?new HashMap<String, String>():invoiceTypeMap;
		purchasebody.put("InvoiceType", invoiceTypeMap.get("name"));//��Ʊ����
		
		UFDouble InvoiceAmount = parentVO.getDef18()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef18());
		purchasebody.put("InvoiceAmount", InvoiceAmount);//��Ʊ���
		UFDouble TaxAmount = parentVO.getDef26()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef26());
		purchasebody.put("TaxAmount", TaxAmount);//˰��
		purchasebody.put("WithoutTaxAmount", InvoiceAmount.sub(TaxAmount));//����˰���
		purchasebody.put("TaxRate", parentVO.getDef30());//˰��
		
		formData.put("C_BankRepaymentInvoice_D", purchasebody);
		return formData;
	}
	
	/**
	 * ��Ϣ
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
//	private Map<String, Object> pushBillsInterestToBpm(AggMarketRepalayVO aggvo) throws BusinessException{
//		MarketRepalayVO parentVO = aggvo.getParentVO();
//		Map<String, Object> formData = new HashMap<String, Object>();
//		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
//		
//		String title = parentVO.getName();
//		title = "~".equals(title) || title==null?"":title;
//		purchase.put("Title", title);//����
//		String pk_billmarker = parentVO.getBillmaker();
//		SendBPMUtils util = new SendBPMUtils();
//		Map<String, String> personMap = util.getUserInfoByID(pk_billmarker);
//		personMap = personMap==null?new HashMap<String, String>():personMap;
//		purchase.put("Applicant", personMap.get("psnname"));// ������
//		purchase.put("ApplicantCode",personMap.get("psncode"));//�������˺�
//		purchase.put("ApplicationCompany", personMap.get("compname"));// ���빫˾
//		purchase.put("ApplicationCompanyCode", personMap.get("compcode"));// ���빫˾����
//		purchase.put("ApplicationDepartment", personMap.get("deptname"));// ���벿��
//		purchase.put("ApplicationDepartmentCode", personMap.get("deptcode"));// ���벿�Ŵ���
//		purchase.put("ApplicationDate",parentVO.getCreationtime().toString());//����ʱ��
//		
//		String pk_project = parentVO.getDef6();
//		Map<String, String> projectMap = util.getProject_name(pk_project);
//		projectMap = projectMap==null?new HashMap<String, String>():projectMap;
//		purchase.put("ProjectName", projectMap.get("name")==null?"ʱ���ع�":projectMap.get("name"));//��Ŀ����
//		
//		String pk_payee = parentVO.getDef7();
//		purchase.put("ReceivingUnit", util.getPayeeName(pk_payee));//�տλ
//		
//		String pk_payer = parentVO.getDef9();
//		purchase.put("PaymentUnit", util.getPayerName(pk_payer));//���λ
//		
//		String pk_bankaccsub = parentVO.getDef8();
//		Map<String, String> bankMap = util.getBankInfo(pk_bankaccsub);
//		bankMap = bankMap==null?new HashMap<String, String>():bankMap;
//		purchase.put("ReceivingBankAccount", bankMap.get("accnum"));//�տ������˻�
//		purchase.put("ReceivingBankAccountName", bankMap.get("accname"));//�տ������˻�����
//		purchase.put("ReceivingBankName", bankMap.get("name"));//�տ������˻�������
//		
//		purchase.put("UseContent", parentVO.getBig_text_b());//�ÿ�����
//		
//		String pk_singleissue = parentVO.getDef5();
//		Map<String, String> info = util.getContractInfo(pk_singleissue);
//		purchase.put("ContractName", info.get("name"));//��ͬ����
//		purchase.put("ContractCode", info.get("billno"));//��ͬ����
//		purchase.put("FinancingType", info.get("type"));//�������� ȡҵ������
//		
//		UFDouble sumMoney = parentVO.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef11());
//		purchase.put("TotalAmount", sumMoney);//��ͬ�ܽ��
//		
//		purchase.put("CumulativePaidInterest", 
//				parentVO.getDef14()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef14()));//�ۼ��Ѹ���Ϣ
//		
//		purchase.put("AmountRequestedRates", 
//				parentVO.getDef24()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef24()));//���������Ϣ
//		
//		String pk_defdoc = parentVO.getDef35();
//		Map<String, String> defMap = util.getFlowMsg(pk_defdoc);
//		defMap = defMap==null?new HashMap<String, String>():defMap;
//		purchase.put("PaymentType", defMap.get("name"));//��ʽ
//		
//		UFDouble InvoiceAmount = parentVO.getDef18()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef18());
//		purchase.put("RequestInvoiceAmount", InvoiceAmount);//��Ʊ���
//		
//		// ����
//		String Attachments = "";
//		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
//				aggvo.getPrimaryKey());
//		if (fileVOs != null && fileVOs.size() > 0) {
//			for (int i = 0; i < fileVOs.size(); i++) {
//				Attachments += fileVOs.get(i).getDocument_name() + "&"
//						+ fileVOs.get(i).getFile_id() + ";";
//			}
//			purchase.put("Attachment",
//					Attachments.substring(0, Attachments.lastIndexOf(";")));
//		} else {
//			purchase.put("Attachment", null);
//		}
//		
//		purchase.put("BarCode", parentVO.getDef21());//Ӱ�����
//		purchase.put("ThisRequestAmount", 
//				parentVO.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef12()));//��������� def12
//		purchase.put("SecondaryIncome", 
//				parentVO.getDef46()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef46()));//�μ����� def46
//		purchase.put("RelatedCosts", 
//				parentVO.getDef45()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef45()));//��ط��� def45
//		Map<String, Object> purchasebody = new HashMap<String, Object>();// ��������
//		
//		String def33 = parentVO.getDef33();
//		Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils().getDefdocInfo(def33);
//		invoiceTypeMap = invoiceTypeMap==null?new HashMap<String, String>():invoiceTypeMap;
//		purchasebody.put("InvoiceType", invoiceTypeMap.get("name"));//��Ʊ����
//		
//		purchasebody.put("InvoiceAmount", InvoiceAmount);//��Ʊ���
//		UFDouble TaxAmount = parentVO.getDef26()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef26());
//		purchasebody.put("TaxAmount", TaxAmount);//˰��
//		purchasebody.put("WithoutTaxAmount", InvoiceAmount.sub(TaxAmount));//����˰���
//		purchasebody.put("TaxRate", parentVO.getDef30());//˰��
//		
//		formData.put("T_RepaymentInterest", purchase);
//		formData.put("C_RepaymentInterestInvoice_D", purchasebody);
//		
//		return formData;
//	}
	
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
}