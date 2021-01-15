package nc.bs.tg.outside.bpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pnt.vo.FileManageVO;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.cdm.repayreceiptbankcredit.RePayReceiptBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.FinancexpenseVO;

import org.apache.commons.lang.StringUtils;

/**
 * ����/��Ϣ
 * 
 * @author HUANGDQ
 * @date 2019��6��26�� ����6:33:12
 */
public class RePayReceiptBankCreditUtils extends BPMBillUtils {
	static RePayReceiptBankCreditUtils utils;

	public static RePayReceiptBankCreditUtils getUtils() {
		if (utils == null) {
			utils = new RePayReceiptBankCreditUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggRePayReceiptBankCreditVO aggVO = (AggRePayReceiptBankCreditVO) bill;
		
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("pk_appdept"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = PushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						IBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("vdef19"));
		aggVO.getParentVO().setAttributeValue("def34", null);//����֪ͨbpm��־
		aggVO.getParentVO().setAttributeValue("vdef19", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("vdef20", infoMap.get("openurl"));
		return aggVO;
	}

	/**
	 * ��Ϣת��
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggRePayReceiptBankCreditVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		if (aggVO.getParentVO().getRepayamount() == null
				&& (aggVO.getParentVO().getInterest() != null||aggVO.getParentVO().getAttributeValue("preinterestmoney")!=null)) {// ��Ϣ
			formData = pushBillsInterestToBpm(aggVO);
		} else {// ����
			formData = pushBillsPrincipalToBpm(aggVO);
		}

		return formData;
	}

	/**
	 * ����
	 * 
	 * @param aggVO
	 * @return
	 */
	private Map<String, Object> pushBillsPrincipalToBpm(
			AggRePayReceiptBankCreditVO aggvo) throws BusinessException {
		// TODO �Զ����ɵķ������
		RePayReceiptBankCreditVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		SendBPMUtils util = new SendBPMUtils();
		// ��ͬ����
		String pk_contract = (String) aggvo.getParentVO().getAttributeValue(
				"pk_contract");
		//��Ϣ���
		UFDouble interest =  (UFDouble) aggvo.getParentVO().getAttributeValue(
				 "interest");
		if(interest==null){
			interest = UFDouble.ZERO_DBL;
		}
		
		//��������֯
		String pk_org = (String) aggvo.getParentVO().getAttributeValue("payunit");
		String easy = null;
		if(StringUtils.isNotBlank(pk_org)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
		}
		if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
			purchase.put("ProposalProcess", easy);
		}else{
			purchase.put("ProposalProcess", null);
		}
		
		purchase.put("BarCode", aggvo.getParentVO().getAttributeValue("def21")==null?null:aggvo.getParentVO().getAttributeValue("def21"));//Ӱ�����
		
		//֧����ʽ
		String paytype = (String) aggvo.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = util.getFlowMsg(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//֧����ʽcode
			}else{
				purchase.put("PaymentMethod", null);//֧����ʽcode
			}
		}else{
			purchase.put("PaymentMethod", null);//֧����ʽcode
		}
		
		
		// �ѻ�����
		UFDouble repayamount = util.getRepayamount(pk_contract);
		// �ѻ���Ϣ
		UFDouble payinterest = util.getPayinterest(pk_contract);
		purchase.put("AlreadyPaid", repayamount);
		purchase.put("AlreadyRates", payinterest);
		//���������Ϣ
		purchase.put("RequestRates", interest.add(new UFDouble(parent.getAttributeValue("preinterestmoney")==null?"0":parent.getAttributeValue("preinterestmoney").toString())));

		
		// ���벿��
		String pk_applicationdept = (String) aggvo.getParentVO()
				.getAttributeValue("pk_appdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = util
					.getDeptmsg(pk_applicationdept);
			String deptid = getHCMDeptID(pk_applicationdept);
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿�ű���
			purchase.put("ApplicationDepartment", deptInfoMap==null?null:deptInfoMap.get("name"));// ���벿��
		}else{
			purchase.put("ApplicationDepartmentCode", null);// ���벿�ű���
			purchase.put("ApplicationDepartment", null);// ���벿��
		}
		// ������
		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"pk_applicant");
		// String applicant = util.getPerson_name((String) parent
		// .getAttributeValue("proposer"));
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = util.getPerson_name(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = util
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// �Ƶ������˺�
			}else{
				purchase.put("ApplicantCode", null);
			}
			purchase.put("Applicant", psnInfoMap==null?"":psnInfoMap.get("name"));// ������
		}else{
			purchase.put("Applicant", null);// ������
		}
		// ���빫˾
		String applicationorg = (String) aggvo.getParentVO().getAttributeValue(
				"pk_apporg");
		if (applicationorg != null && !"~".equals(applicationorg)) {
			Map<String, String> psnInfoMap = util.getOrgmsg(applicationorg);
			purchase.put("ApplicationCompany", psnInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", psnInfoMap.get("code"));// ���빫˾����
		}else{
			purchase.put("ApplicationCompany", "");// ���빫˾
			purchase.put("ApplicationCompanyCode", "");// ���빫˾����
		}

		// ��������
		UFDate date = (UFDate) aggvo.getParentVO().getAttributeValue("time");
		if (date != null) {
			String applicationDate = date.toStdString();
			purchase.put("ApplicationDate", applicationDate);// ��������
		}
		// ��Ŀ
		 String projectMsg = (String) aggvo.getParentVO().getAttributeValue(
		 "proname");
		 if (projectMsg != null && !"~".equals(projectMsg)) {
		 Map<String, String> projectInfoMap = util
		 .getProject_name(projectMsg);
		 purchase.put("ProjectName", projectInfoMap==null?"":projectInfoMap.get("name"));// ��Ŀ����
		 }
		// �տ
		String pk_payee = (String) aggvo.getParentVO().getAttributeValue(
				"reunit");
		if(pk_payee != null && !"~".equals(pk_payee)){
			Map<String, String> psnInfoMap = util.getCustomerMsg(pk_payee);
			purchase.put("ReceivingUnit", psnInfoMap ==null?"":psnInfoMap.get("name"));// �տ����
		}else{
			purchase.put("ReceivingUnit", "");// �տ����
		}
		// ���
		String pk_payer = (String) aggvo.getParentVO().getAttributeValue(
				"payunit");
		if (pk_payer != null && !"~".equals(pk_payer)) {
			Map<String, String> custInfoMap = util.getOrgInfo(pk_payer);
			purchase.put("PaymentUnit", custInfoMap.get("name"));// �տ����
		}
		// ���Ż��
		if (parent.getAttributeValue("pk_accounting") != null
				&& !"~".equals(parent.getAttributeValue("pk_accounting"))) {
			Map<String, String> accountant_name = util
					.getPerson_name((String) parent
							.getAttributeValue("pk_accounting"));
			purchase.put("Accounting", accountant_name.get("name"));// ���Ż������
		}
		// ���Ż���˺�(���˺�)
		if (parent.getAttributeValue("pk_accounting") != null
				&& !"~".equals(parent.getAttributeValue("pk_accounting"))) {
			Map<String, String> accountant_name = util
					.getPerson_name((String) parent
							.getAttributeValue("pk_accounting"));
			if(accountant_name!=null){
				Map<String, String> accountant_account = util
						.getRegionNameByPersonCode(accountant_name.get("code"));
				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż������
			}else{
				purchase.put("AccountingAccount", "");
			}
		}else{
			purchase.put("AccountingAccount", "");// ���Ż������
		}
		// ����
		if (parent.getAttributeValue("pk_cashier") != null
				&& !"~".equals(parent.getAttributeValue("pk_cashier"))) {
			Map<String, String> pk_cashier = util
					.getPerson_name((String) parent
							.getAttributeValue("pk_cashier"));
			purchase.put("Cashier", pk_cashier==null?"":pk_cashier.get("name"));// ��������
		}else{
			purchase.put("Cashier", null);// ��������
		}
		// �����˺�(���˺�)
		if (parent.getAttributeValue("pk_cashier") != null
				&& !"~".equals(parent.getAttributeValue("pk_cashier"))) {
			Map<String, String> accountant_name = util
					.getPerson_name((String) parent
							.getAttributeValue("pk_cashier"));
			if(accountant_name!=null){
				Map<String, String> accountant_account = util
						.getRegionNameByPersonCode(accountant_name.get("code"));
				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż��(���˺�)
			}else{
				purchase.put("CashierAccount", "");
			}
		}else{
			purchase.put("CashierAccount", "");// ���Ż������
		}
		// �ÿ�����
		if (parent.getAttributeValue("moneycontent") != null
				&& !"~".equals(parent.getAttributeValue("moneycontent"))) {
			purchase.put("UseContent", parent.getAttributeValue("moneycontent"));
		}else{
			purchase.put("UseContent", "");
		}
		// ҵ����Ϣ
		// purchase.put("businessmsg",
		// parent.getBusiinformation()==null?"":parent.getBusiinformation());
		// ��ͬ�ܽ��
		if (parent.getAttributeValue("contractamount") != null
				&& !"~".equals(parent.getAttributeValue("contractamount"))) {
			purchase.put("TotalAmount",
					new UFDouble(parent.getAttributeValue("contractamount")
							.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("TotalAmount",
					"");
		}
		// �ۼƸ�����
		if (parent.getAttributeValue("totalpaymoney") != null
				&& !"~".equals(parent.getAttributeValue("totalpaymoney"))) {
			purchase.put("AlreadyPaid",
					new UFDouble(parent.getAttributeValue("totalpaymoney")
							.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("AlreadyPaid",
					"");
		}
		// ǰ����Ϣ���(bpm���ܽ��)
		// if(parent.getAttributeValue("preinterestmoney")!=null&&
		// !"~".equals(parent.getAttributeValue("preinterestmoney"))){
		// purchase.put("AmountRequested",
		// new
		// UFDouble(parent.getAttributeValue("preinterestmoney").toString()).setScale(2,
		// 2).toString());
		// }
		// �����(bpm���ܽ��)
			purchase.put("RequestAmount", new UFDouble(parent
					.getAttributeValue("repayamount")==null?"0":parent
							.getAttributeValue("repayamount").toString()).setScale(2, 2));
		// ����
		if (parent.getAttributeValue("vdef6") != null
				&& !"~".equals(parent.getAttributeValue("vdef6"))) {
			purchase.put("Title", parent.getAttributeValue("vdef6"));
		}else{
			purchase.put("Title", "");
		}
		
		
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
		
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 begin
		String paymentType = "";// ��ʽ
		String receivingBankAccount = "";// �տ������˻�
		String receivingBankAccountName = "";// �տ������˻�����
		String receivingBankName = "";// �տ������˻�������
		String invoiceType = "";// ��Ʊ����
		UFDouble invoiceAmount = new UFDouble(0);// ��Ʊ���
		String taxRate = "";// ˰��
		UFDouble withoutTaxAmount = new UFDouble(0);// ����˰���
		UFDouble taxAmount =new UFDouble(0);// ˰��
		
		String sql = "";
		RePayReceiptBankCreditVO parentVO=aggvo.getParentVO();
		String pk_bankaccsub=parentVO.getVdef2();
		List<Map<String,String>> lists=getBankDetails(pk_bankaccsub);
		if(lists!=null&&lists.size()>0){
			receivingBankAccount=lists.get(0).get("accnum");
			receivingBankAccountName=lists.get(0).get("accname");
			receivingBankName=lists.get(0).get("docname");
		}
		purchase.put("receivingBankAccount", receivingBankAccount);
		purchase.put("receivingBankAccountName", receivingBankAccountName);
		purchase.put("receivingBankName", receivingBankName);

		String def35=(String)parent.getAttributeValue("def35");
		if(isNotBlank(def35)){
			Map<String, String> paymentTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def35);
			if(paymentTypeMap!=null){
				paymentType=paymentTypeMap.get("name");
			}
		}
		purchase.put("PaymentType", paymentType);
		
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();
		Map<String, Object> purchaseBody=new HashMap<String, Object>();
		
		String def46=(String)parent.getAttributeValue("def46");
		if(isNotBlank(def46)){
			Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def46);
			if(invoiceTypeMap!=null){
				invoiceType=invoiceTypeMap.get("name");
			}
		}
		purchaseBody.put("InvoiceType", invoiceType);
		
		String def22=(String)parent.getAttributeValue("def22");
		if(isNotBlank(def22)){
			invoiceAmount=new UFDouble(def22);
		}
		purchaseBody.put("InvoiceAmount", invoiceAmount);
		
		String def23=(String)parent.getAttributeValue("def23");
		if(isNotBlank(def23)){
			taxAmount=new UFDouble(def23);
		}
		purchaseBody.put("TaxAmount", taxAmount);
		
		if(!new UFDouble(0).equals(invoiceAmount)){
			taxRate=new UFDouble(taxAmount).div(new UFDouble(invoiceAmount)).toString();
		}
		purchaseBody.put("TaxRate", taxRate);
		
		withoutTaxAmount=invoiceAmount.sub(taxAmount);
		purchaseBody.put("WithoutTaxAmount", withoutTaxAmount);
		listPurchase.add(purchaseBody);
		
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_BankRepayment", purchase);
		formData.put("C_BankRepaymentInvoice_D", listPurchase);
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
		return formData;
	}

	/**
	 * ��Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> pushBillsInterestToBpm(
			AggRePayReceiptBankCreditVO aggvo) throws BusinessException {
		RePayReceiptBankCreditVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		SendBPMUtils util = new SendBPMUtils();
		
		//��Ϣ���
		UFDouble interest =  (UFDouble) aggvo.getParentVO().getAttributeValue(
						 "interest");
		if(interest==null){
			interest = UFDouble.ZERO_DBL;
		}
		
		//����֯
		String pk_org = (String) aggvo.getParentVO().getAttributeValue("payunit");
		String easy = null;
		if(StringUtils.isNotBlank(pk_org)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
		}
		if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
			purchase.put("ProposalProcess", easy);
		}else{
			purchase.put("ProposalProcess", "");
		}
		
		purchase.put("BarCode", aggvo.getParentVO().getAttributeValue("def21")==null?"":aggvo.getParentVO().getAttributeValue("def21"));//Ӱ�����
		
		// ��ͬ����
		String pk_contract = (String) aggvo.getParentVO().getAttributeValue(
				"pk_contract");
		// �ѻ�����
		UFDouble repayamount = util.getRepayamount(pk_contract);
		// �ѻ���Ϣ
		UFDouble payinterest = util.getPayinterest(pk_contract);
		purchase.put("AlreadyPaid", repayamount);
		purchase.put("AlreadyRates", payinterest);
		purchase.put("AmountRequested", interest==null?UFDouble.ZERO_DBL:interest);
		

		if (pk_contract != null && !"~".equals(pk_contract)) {
			if(util.getContract(pk_contract)!=null){
				purchase.put("ContractName", util.getContract(pk_contract).get("name"));//��ͬ����
				purchase.put("ContractNo", util.getContract(pk_contract).get("code"));//��ͬ����
			}
		}else{
			purchase.put("ContractName", "");//��ͬ����
			purchase.put("ContractNo", "");//��ͬ����
		}
		
		//֧����ʽ
		String paytype = (String) aggvo.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = util.getFlowMsg(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//֧����ʽcode
			}else{
				purchase.put("PaymentMethod", "");//֧����ʽcode
			}
		}else{
			purchase.put("PaymentMethod", "");//֧����ʽcode
		}
		
		//��Ʊ���
		if (parent.getAttributeValue("def22") != null
				&& !"~".equals(parent.getAttributeValue("def22"))) {
			purchase.put("RequestInvoiceAmount", new UFDouble(parent.getAttributeValue("def22")
					.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("RequestInvoiceAmount", "");
		}
		
		//�ۼ���Ʊ���
		if (parent.getAttributeValue("def36") != null
				&& !"~".equals(parent.getAttributeValue("def36"))) {
			purchase.put("CumulativeRequestInvoiceAmount", new UFDouble(parent.getAttributeValue("def36")
					.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("CumulativeRequestInvoiceAmount", "");
		}
		
		// ���벿��
		String pk_applicationdept = (String) aggvo.getParentVO()
				.getAttributeValue("pk_appdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = util
					.getDeptmsg(pk_applicationdept);
			String deptid = getHCMDeptID(pk_applicationdept);

			purchase.put("ApplicationDepartment", deptInfoMap==null?"":deptInfoMap.get("name"));//
			// ���벿��
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿�Ŵ���
		}else{
			purchase.put("ApplicationDepartment", "");// ���벿��
			purchase.put("ApplicationDepartmentCode", "");// ���벿�Ŵ���
		}
		
		// ������

		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"pk_applicant");
		// String applicant = util.getPerson_name((String) parent
		// .getAttributeValue("proposer"));
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = util.getPerson_name(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = util
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// �Ƶ������˺�
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap==null?"":psnInfoMap.get("name"));// ������
		}else{
			purchase.put("ApplicantCode", "");
			purchase.put("Applicant", "");// ������
		}
		// ���빫˾
		String applicationorg = (String) aggvo.getParentVO().getAttributeValue(
				"pk_apporg");
		if (applicationorg != null && !"~".equals(applicationorg)) {
			Map<String, String> psnInfoMap = util.getOrgmsg(applicationorg);
			purchase.put("ApplicationCompany", psnInfoMap==null?"":psnInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", psnInfoMap==null?"":psnInfoMap.get("code"));// ���빫˾����
		}else{
			purchase.put("ApplicationCompany", "");// ���빫˾
			purchase.put("ApplicationCompanyCode", "");// ���빫˾����
		}

		// ��������
		UFDate date = (UFDate) aggvo.getParentVO().getAttributeValue("time");
		if (date != null) {
			String applicationDate = date.toStdString();
			purchase.put("ApplicationDate", applicationDate);// ��������
		}else{
			purchase.put("ApplicationDate", "");// ��������
		}
		// ��Ŀ
		 String projectMsg = (String) aggvo.getParentVO().getAttributeValue(
		 "proname");
		 if (projectMsg != null && !"~".equals(projectMsg)) {
		 Map<String, String> projectInfoMap = util
		 .getProject_name(projectMsg);
		 purchase.put("ProjectName", projectInfoMap==null?"":projectInfoMap.get("name"));// ��Ŀ����
		 }else{
			 purchase.put("ProjectName", "");// ��Ŀ����
		 }
		// �տ
		String pk_payee = (String) aggvo.getParentVO().getAttributeValue(
				"reunit");
		if(pk_payee != null && !"~".equals(pk_payee)){
			Map<String, String> psnInfoMap = util.getCustomerMsg(pk_payee);
			purchase.put("ReceivingUnit", psnInfoMap ==null?"":psnInfoMap.get("name"));// �տ����
		}else{
			purchase.put("ReceivingUnit", "");// �տ����
		}
//		if (pk_payee != null && !"~".equals(pk_payee)) {
//			Map<String, String> custInfoMap = util.getCustomerMsg(pk_payee);
//			purchase.put("ReceivingUnit", custInfoMap.get("name"));// �տ����
//		}
		// ���
		String pk_payer = (String) aggvo.getParentVO().getAttributeValue(
				"payunit");
		if (pk_payer != null && !"~".equals(pk_payer)) {
			Map<String, String> custInfoMap = util.getOrgInfo(pk_payer);
			purchase.put("PaymentUnit", custInfoMap==null?"":custInfoMap.get("name"));// �տ����
		}else{
			purchase.put("PaymentUnit", "");// �տ����
		}
		
		// ���Ż��
		// String accountant_name = (String)
		// aggvo.getParentVO().getAttributeValue("pk_accounting");
		// if (accountant_name != null && !"~".equals(accountant_name)) {
		// Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
		// .getPsnInfo(accountant_name);
		// purchase.put("BookKeeper", psnInfoMap.get("code"));// ���Ż�ƴ���
		// purchase.put("BookKeeperName", psnInfoMap.get("name"));// ���Ż��
		// }
		// String accountant_name = util.getPerson_name((String) parent
		// .getGroupaccounting());
		// purchase.put("Accounting",
		// accountant_name==null?"":accountant_name);// ���Ż������
		// ����
		// String cashier_name = (String)
		// aggvo.getParentVO().getAttributeValue("pk_accounting");
		// if (cashier_name != null && !"~".equals(cashier_name)) {
		// Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
		// .getPsnInfo(cashier_name);
		// purchase.put("CaShier", psnInfoMap.get("code"));// ���ɴ���
		// purchase.put("CaShierName", psnInfoMap.get("name"));// ����
		// }
		// purchase.put("Cashier", cashier_name==null?"":cashier_name);// ��������
		// �ÿ�����
		if (parent.getAttributeValue("moneycontent") != null
				&& !"~".equals(parent.getAttributeValue("moneycontent"))) {
			purchase.put("MoneyContent",
					parent.getAttributeValue("moneycontent"));
		}else{
			purchase.put("MoneyContent",
					"");
		}
		// ҵ����Ϣ
		// purchase.put("businessmsg",
		// parent.getBusiinformation()==null?"":parent.getBusiinformation());
		// ��ͬ�ܽ��
		if (parent.getAttributeValue("contractamount") != null
				&& !"~".equals(parent.getAttributeValue("contractamount"))) {
			purchase.put("TotalAmount",
					new UFDouble(parent.getAttributeValue("contractamount")
							.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("TotalAmount",
					"");
		}
		// �ۼ��Ѹ���Ϣ
		if (parent.getAttributeValue("vdef10") != null
				&& !"~".equals(parent.getAttributeValue("vdef10"))) {
			purchase.put("AccumulatedPaymentRates",
					new UFDouble(parent.getAttributeValue("vdef10")
							.toString()).setScale(2, 2).toString());
		}else{
			purchase.put("AccumulatedPayment",
					0);
		}
		// ǰ����Ϣ���(bpm���ܽ��)
		// if(parent.getAttributeValue("preinterestmoney")!=null&&
		// !"~".equals(parent.getAttributeValue("preinterestmoney"))){
		// purchase.put("AmountRequested",
		// new
		// UFDouble(parent.getAttributeValue("preinterestmoney").toString()).setScale(2,
		// 2).toString());
		// }
		// ���������Ϣ
			purchase.put("AmountRequestedRates", new UFDouble(parent
					.getAttributeValue("repayamount")==null?"0":parent
							.getAttributeValue("repayamount").toString()).add(interest==null?UFDouble.ZERO_DBL:interest).add(new UFDouble(parent.getAttributeValue("preinterestmoney")==null?"0":parent.getAttributeValue("preinterestmoney").toString()).setScale(2, 2)).toString())
									;
		// ����
		if (parent.getAttributeValue("vdef6") != null
				&& !"~".equals(parent.getAttributeValue("vdef6"))) {
			purchase.put("Title", parent.getAttributeValue("vdef6"));
		}else{
			purchase.put("Title", "");
		}
		
		
		
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
		
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 begin
		String paymentType = "";// ��ʽ
		String receivingBankAccount = "";// �տ������˻�
		String receivingBankAccountName = "";// �տ������˻�����
		String receivingBankName = "";// �տ������˻�������
		String invoiceType = "";// ��Ʊ����
		UFDouble invoiceAmount = new UFDouble(0);// ��Ʊ���
		String taxRate = "";// ˰��
		UFDouble withoutTaxAmount = new UFDouble(0);// ����˰���
		UFDouble taxAmount =new UFDouble(0);// ˰��
		
		String sql = "";
		String def35=(String)parent.getAttributeValue("def35");
		if(isNotBlank(def35)){
			Map<String, String> paymentTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def35);
			if(paymentTypeMap!=null){
				paymentType=paymentTypeMap.get("name");
			}
		}
		purchase.put("PaymentType", paymentType);
		
		String pk_bankaccsub=(String)parent.getAttributeValue("vdef2");
		List<Map<String,String>> lists=getBankDetails(pk_bankaccsub);
		if(lists!=null&&lists.size()>0){
			receivingBankAccount=lists.get(0).get("accnum");
			receivingBankAccountName=lists.get(0).get("accname");
			receivingBankName=lists.get(0).get("docname");
		} 
		purchase.put("ReceivingBankAccount", receivingBankAccount);
		purchase.put("ReceivingBankAccountName", receivingBankAccountName);
		purchase.put("ReceivingBankName", receivingBankName);
		
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();
		Map<String, Object> purchaseBody=new HashMap<String, Object>();
		
		String def46=(String)parent.getAttributeValue("def46");
		if(isNotBlank(def46)){
			Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def46);
			if(invoiceTypeMap!=null){
				invoiceType=invoiceTypeMap.get("name");
			}
		}
		purchaseBody.put("InvoiceType", invoiceType);
		
		String def22=(String)parent.getAttributeValue("def22");
		if(isNotBlank(def22)){
			invoiceAmount=new UFDouble(def22);
		}
		purchaseBody.put("InvoiceAmount", invoiceAmount);
		
		String def23=(String)parent.getAttributeValue("def23");
		if(isNotBlank(def23)){
			taxAmount=new UFDouble(def23);
		}
		purchaseBody.put("TaxAmount", taxAmount);
		
		if(!new UFDouble(0).equals(invoiceAmount)){
			taxRate=new UFDouble(taxAmount).div(new UFDouble(invoiceAmount)).toString();
		}
		purchaseBody.put("TaxRate", taxRate);
		
		withoutTaxAmount=invoiceAmount.sub(taxAmount);
		purchaseBody.put("WithoutTaxAmount", withoutTaxAmount);
		listPurchase.add(purchaseBody);
		
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_RepaymentInterest", purchase);
		formData.put("C_RepaymentInterestInvoice_D", listPurchase);
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
		return formData;
	}

}
