package nc.bs.tg.outside.bpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;
import nc.vo.tg.financingexpense.FinancexpenseVO;

import org.apache.commons.lang.StringUtils;

/**
 * �ƹ˷�/���ʹ���
 * 
 * @author HUANGDQ
 * @date 2019��6��26�� ����6:32:46
 */
public class FinancexpenseUtils extends BPMBillUtils {
	static FinancexpenseUtils utils;

	public static FinancexpenseUtils getUtils() {
		if (utils == null) {
			utils = new FinancexpenseUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggFinancexpenseVO aggVO = (AggFinancexpenseVO) bill;
		
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("pk_applicationdept"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = PushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						IBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def19"));
		aggVO.getParentVO().setAttributeValue("def34", null);//����֪ͨbpm��־
		aggVO.getParentVO().setAttributeValue("def19", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def20", infoMap.get("openurl"));
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
			AggFinancexpenseVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		if (IBPMBillCont.BILL_RZ06_01.equals(billCode)) {
			formData = getFinancialAdvisorFeeInfo(aggVO);
		} else {
			formData = getFinancingCostsInfo(aggVO);
		}

		return formData;
	}

	/**
	 * ������ʷ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFinancingCostsInfo(AggFinancexpenseVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����

		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue("title"));// ����
		
		//����֯
		String pk_org = (String) aggVO.getParentVO().getAttributeValue("pk_org");
		String pk_payer =  (String) aggVO.getParentVO().getAttributeValue("pk_payer");
		String easy = null;
		if(StringUtils.isNotBlank(pk_org)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11")==null?null:orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else if(StringUtils.isBlank(pk_org)&&StringUtils.isNotBlank(pk_payer)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_payer);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11");
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else{
			purchase.put("ProposalProcess", null);
		}
		//��Ʊ���
		if (aggVO.getParentVO().getAttributeValue("def27") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def27"))) {
			purchase.put("RequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def27").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("RequestInvoiceAmount", null);
		}
		
		//�ۼ���Ʊ���
		if (aggVO.getParentVO().getAttributeValue("def28") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def28"))) {
			purchase.put("CumulativeRequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def28").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("CumulativeRequestInvoiceAmount", null);
		}
		
		//֧����ʽ
		String paytype = (String) aggVO.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = QueryDocInfoUtils.getUtils().getDefdocInfo(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//֧����ʽcode
			}else{
				purchase.put("PaymentMethod", null);//֧����ʽcode
			}
		}else{
			purchase.put("PaymentMethod", null);//֧����ʽcode
		}
		
		purchase.put("BarCode", aggVO.getParentVO().getAttributeValue("def21")==null?null:aggVO.getParentVO().getAttributeValue("def21"));//Ӱ�����

		// ������
		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue("pk_applicant");
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// �Ƶ������˺�
			}else{
				purchase.put("ApplicantCode", null);
			}
			purchase.put("Applicant", psnInfoMap==null?null:psnInfoMap.get("name"));// ������
		}else{
			purchase.put("ApplicantCode", null);// �Ƶ������˺�
			purchase.put("Applicant", null);// ������
		}

		// ��������
		if(aggVO.getParentVO().getAttributeValue("applicationdate")!=null){
			UFDate date = new UFDate(aggVO.getParentVO().getAttributeValue("applicationdate").toString());
			if (date != null) {
				String applicationDate = date.toStdString();
				purchase.put("ApplicationDate", applicationDate);// ��������
			}
		}else{
			purchase.put("ApplicationDate", null);// ��������
		}

		// ���빫˾
		String pk_applicationorg = (String) aggVO.getParentVO().getAttributeValue("pk_applicationorg");
		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_applicationorg);
			purchase.put("ApplicationCompany", orgInfoMap==null?null:orgInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", orgInfoMap==null?null:orgInfoMap.get("code"));// ���빫˾����
		}else{
			purchase.put("ApplicationCompany", null);// ���빫˾
			purchase.put("ApplicationCompanyCode", null);// ���빫˾����
		}

		// ���벿��
		String pk_applicationdept = (String) aggVO.getParentVO().getAttributeValue("pk_applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
					.getDeptInfo(pk_applicationdept);
			String deptid = getHCMDeptID((String) aggVO.getParentVO().getPk_applicationdept());
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿�Ŵ���
			purchase.put("ApplicationDepartment", deptInfoMap==null?null:deptInfoMap.get("name"));// ���벿��
		}else{
			purchase.put("ApplicationDepartmentCode", null);// ���벿�Ŵ���
			purchase.put("ApplicationDepartment", null);// ���벿��
		}


		// ��Ŀ����
		String pk_project = (String) aggVO.getParentVO().getAttributeValue("pk_project");
		if (pk_project != null && !"~".equals(pk_project)) {
			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
					.getProjectDataInfo(pk_project);
			purchase.put("ProjectName", projectInfoMap==null?null:projectInfoMap.get("name"));// ��Ŀ����
		}else{
			purchase.put("ProjectName", null);// ��Ŀ����
		}

		// ժҪ/��ע
		if (aggVO.getParentVO().getAttributeValue("big_text_b") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("big_text_b"))) {
			purchase.put(
					"Remarks",
					aggVO.getParentVO().getAttributeValue("big_text_b"));// ��Ŀ����
		}else{
			purchase.put(
					"Remarks",
					null);// ��Ŀ����
		}

		// �տλ
//		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_PAYEE);
//		if (pk_payee != null && !"~".equals(pk_payee)) {
//			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
//					.getCustInfo(pk_payee);
//			purchase.put("ReceivingUnit", custInfoMap.get("name"));
//		}
		
		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
				"pk_payee");
//		if (pk_payee != null && !"~".equals(pk_payee)) {
//			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
//					.getCustInfo(pk_payee);
//			purchase.put("ReceivingUnit", custInfoMap.get("name"));
//		}
		if(pk_payee != null && !"~".equals(pk_payee)){
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils().getCustInfo(pk_payee);
			purchase.put("ReceivingUnit", psnInfoMap ==null?null:psnInfoMap.get("name"));// �տ����
		}else{
			purchase.put("ReceivingUnit", null);// �տ����
		}
		
		// ���λ
		if (pk_payer != null && !"~".equals(pk_payer)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_payer);
			purchase.put("PaymentUnit", infoMap==null?null:infoMap.get("name"));
		}else{
			purchase.put("PaymentUnit", null);
		}
		String totalAmout = aggVO.getParentVO().getAttributeValue("contractmoney") == null ? "" : aggVO.getParentVO().getAttributeValue("contractmoney").toString();
		purchase.put("TotalAmount", totalAmout);// ��ͬ�ܽ��

		String accumulatedPayment = aggVO.getParentVO().getAttributeValue("paymentamount") == null ? "" : aggVO.getParentVO().getAttributeValue("paymentamount")
				.toString();
		purchase.put("AccumulatedPayment", accumulatedPayment);// �ۼ��Ѹ����

		String amountRequested = aggVO.getParentVO().getAttributeValue("applyamount") == null ? "" : aggVO.getParentVO().getAttributeValue("applyamount").toString();
		purchase.put("AmountRequested", amountRequested);// ���������

		String pk_costitem = (String) aggVO.getParentVO().getAttributeValue("def5");
		if (pk_costitem != null && !"~".equals(pk_costitem)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(pk_costitem);
			purchase.put("CostItem", infoMap==null?null:infoMap.get("name"));
		}else{
			purchase.put("CostItem", null);
		}
		

		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue("pk_accountant");// ���Ż��
		if (pk_accountant != null && !"~".equals(pk_accountant)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_accountant);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// ���Ż�ƴ���BookKeeper
				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż��
//				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//���Ż������
//				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ������
			}else{
				purchase.put("BookKeeper", "");// ���Ż��
				purchase.put("BookKeeperName", "");// ���Ż�ƴ���
//				purchase.put("Accounting", "");//���Ż������
//				purchase.put("AccountingAccount", "");//������
			}
		}
		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue("pk_cashier");// ����
		if (pk_cashier != null && !"~".equals(pk_cashier)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_cashier);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// ��������
				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���ɴ���(������)
//				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
//				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ������
			}else{
				purchase.put("CaShier", "");// ����
				purchase.put("CaShierName", "");// ��������
//				purchase.put("Cashier", "");// (���ֶ�)��������
//				purchase.put("CashierAccount", "");// ������
			}
		}
		// purchase.put("Remarks",
		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.MESSAGE));// ��ע
		

		// ����
		String Attachments = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
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
		FinancexpenseVO parentVO=aggVO.getParentVO();
		String def29=parentVO.getDef29();
		if(isNotBlank(def29)){
			Map<String, String> paymentTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def29);
			if(paymentTypeMap!=null){
				paymentType=paymentTypeMap.get("name");
			}
		}
		purchase.put("PaymentType", paymentType);
		
		String pk_bankaccsub=parentVO.getDef7();
		List<Map<String,String>> lists=getBankDetails(pk_bankaccsub);
		if(lists!=null&&lists.size()>0){
			receivingBankAccount=lists.get(0).get("accnum");
			receivingBankAccountName=lists.get(0).get("accname");
			receivingBankName=lists.get(0).get("docname");
		}
		purchase.put("ReceivingBankAccount", receivingBankAccount);
		purchase.put("ReceivingBankAccountName", receivingBankAccountName);
		purchase.put("ReceivingBankName", receivingBankName);
		
		List<Map<String, Object>> bodys = new ArrayList<Map<String, Object>>();// ��������
		Map<String, Object> purchaseBody=new HashMap<String, Object>();
		String def8=parentVO.getDef8();
		if(isNotBlank(def8)){
			Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def8);
			if(invoiceTypeMap!=null){
				invoiceType=invoiceTypeMap.get("name");
			}
		}
		purchaseBody.put("InvoiceType", invoiceType);
		
		String def27=parentVO.getDef27();
		String def23=parentVO.getDef23();
		invoiceAmount=new UFDouble(isNotBlank(def27)?def27:def23);
		purchaseBody.put("InvoiceAmount", invoiceAmount);
		
		String def26=parentVO.getDef26();
		if(isNotBlank(def26)){
			taxRate=def26;
		}
		purchaseBody.put("TaxRate", taxRate);
		
		String def11=parentVO.getDef11();
		String def24=parentVO.getDef24();
		taxAmount=new UFDouble(isNotBlank(def11)?def11:def24);
		purchaseBody.put("TaxAmount", taxAmount);
		
		withoutTaxAmount=invoiceAmount.sub(taxAmount);
		purchaseBody.put("WithoutTaxAmount", withoutTaxAmount);
		
		bodys.add(purchaseBody);
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_FinancingCosts", purchase);
        
		formData.put("C_ThisInvoiceInfo_Detail", bodys);// ��������
		
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
		return formData;
	}

	/**
	 * ��ò�����ʷ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFinancialAdvisorFeeInfo(
			AggFinancexpenseVO aggVO) throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����

		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue("title"));// ����
		//��������֯
		String pk_payer =  (String) aggVO.getParentVO().getAttributeValue("pk_payer");
		//������֯
		String def61 = (String) aggVO.getParentVO().getAttributeValue("def61");
		String easy = null;
		if(StringUtils.isNotBlank(pk_payer)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_payer);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else if(StringUtils.isBlank(pk_payer)&&StringUtils.isNotBlank(def61)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(def61);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else{
			purchase.put("ProposalProcess", null);
		}
		purchase.put("BarCode", aggVO.getParentVO().getAttributeValue("def21")==null?"":aggVO.getParentVO().getAttributeValue("def21"));//Ӱ�����
		
		// ��ͬ����
		String pk_contract = (String) aggVO.getParentVO().getAttributeValue(
				"def4");
		
		if (pk_contract != null && !"~".equals(pk_contract)) {
			if(QueryDocInfoUtils.getUtils().getContract(pk_contract)!=null){
				purchase.put("ContractName", QueryDocInfoUtils.getUtils().getContract(pk_contract).get("name")==null?"0":QueryDocInfoUtils.getUtils().getContract(pk_contract).get("name"));//��ͬ����
				purchase.put("ContractNo", QueryDocInfoUtils.getUtils().getContract(pk_contract).get("code")==null?"0":QueryDocInfoUtils.getUtils().getContract(pk_contract).get("code"));//��ͬ����
			}
		}else{
			purchase.put("ContractName", "0");//��ͬ����
			purchase.put("ContractNo", "0");//��ͬ����
		}
		
		//��Ʊ���
		if (aggVO.getParentVO().getAttributeValue("def27") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def27"))) {
			purchase.put("RequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def27").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("RequestInvoiceAmount", null);
		}
		
		//�ۼ���Ʊ���
		if (aggVO.getParentVO().getAttributeValue("def28") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def28"))) {
			purchase.put("CumulativeRequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def28").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("CumulativeRequestInvoiceAmount", null);
		}
		
		//֧����ʽ
		String paytype = (String) aggVO.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = QueryDocInfoUtils.getUtils().getDefdocInfo(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//֧����ʽcode
			}else{
				purchase.put("PaymentMethod", null);//֧����ʽcode
			}
		}else{
			purchase.put("PaymentMethod", null);//֧����ʽcode
		}
		
		// ������
		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue("pk_applicant");
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���������˺�
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap.get("name"));// ������
		}else{
			purchase.put("Applicant", null);// ������
		}

		// ��������
		if(aggVO.getParentVO().getAttributeValue("applicationdate")!=null){
			UFDate date = new UFDate(aggVO.getParentVO().getAttributeValue("applicationdate").toString());
			if (date != null) {
				String applicationDate = date.toStdString();
				purchase.put("ApplicationDate", applicationDate);// ��������
			}
		}else{
			purchase.put("ApplicationDate", null);// ��������
		}

		// ���빫˾
		String pk_applicationorg = (String) aggVO.getParentVO().getAttributeValue("pk_applicationorg");
		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_applicationorg);
			purchase.put("ApplicationCompany", orgInfoMap==null?null:orgInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", orgInfoMap==null?null:orgInfoMap.get("code"));// ���빫˾����
		}else{
			purchase.put("ApplicationCompany", null);// ���빫˾
			purchase.put("ApplicationCompanyCode", null);// ���빫˾����
		}

		// ���벿��
		String pk_applicationdept = (String) aggVO.getParentVO()
				.getAttributeValue("pk_applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
					.getDeptInfo(pk_applicationdept);
			String deptid = getHCMDeptID((String) aggVO.getParentVO()
					.getPk_applicationdept());
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿������
			purchase.put("ApplicationDepartment", deptInfoMap==null?null:deptInfoMap.get("name"));// ���벿��
		}else{
			purchase.put("ApplicationDepartmentCode", null);// ���벿������
			purchase.put("ApplicationDepartment", null);// ���벿��
		}

		// ��Ŀ����
		String pk_project = (String) aggVO.getParentVO().getAttributeValue("pk_project");
		if (pk_project != null && !"~".equals(pk_project)) {
			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
					.getProjectDataInfo(pk_project);
			purchase.put("ProjectName", projectInfoMap.get("name"));// ��Ŀ����
		}else{
			purchase.put("ProjectName", null);// ��Ŀ����
		}

		// �տλ
		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
				"pk_payee");
//		if (pk_payee != null && !"~".equals(pk_payee)) {
//			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
//					.getCustInfo(pk_payee);
//			purchase.put("ReceivingUnit", custInfoMap.get("name"));
//		}
		if(pk_payee != null && !"~".equals(pk_payee)){
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils().getCustInfo(pk_payee);
			purchase.put("ReceivingUnit", psnInfoMap ==null?"":psnInfoMap.get("name"));// �տ����
		}else{
			purchase.put("ReceivingUnit", null);// �տ����
		}
		//������֯
		if (pk_payer != null && !"~".equals(pk_payer)) {//�����λ��Ϊ���򴫸��λ
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_payer);
			if(infoMap!=null){
				purchase.put("PaymentUnit", infoMap.get("name"));
			}else{
				purchase.put("PaymentUnit", null);
			}
		}else if(StringUtils.isBlank(pk_payer)&&StringUtils.isNotBlank(def61)){//�����λΪ���Ҳ�����֯��Ϊ���򴫲�����֯,���ഫnull
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(def61);
			if(infoMap!=null){
				purchase.put("PaymentUnit", infoMap.get("name"));
			}else{
				purchase.put("PaymentUnit", null);
			}
		}else{
			purchase.put("PaymentUnit", null);
		}

		String totalAmout = aggVO.getParentVO().getAttributeValue("contractmoney") == null ? "0" : aggVO.getParentVO().getAttributeValue("contractmoney")
				.toString();
		purchase.put("TotalAmount", totalAmout);// ��ͬ�ܽ��

		String accumulatedPayment = aggVO.getParentVO().getAttributeValue("paymentamount") == null ? "" : aggVO.getParentVO().getAttributeValue("paymentamount")
				.toString();
		purchase.put("AlreadyPaid", accumulatedPayment);// �ۼ��Ѹ����

		String amountRequested = aggVO.getParentVO().getAttributeValue("applyamount") == null ? "" : aggVO.getParentVO().getAttributeValue("applyamount").toString();
		purchase.put("RequestAmount", amountRequested);// ���������

		purchase.put(
				"UseContent",
				aggVO.getParentVO().getAttributeValue("big_text_b"));// �ÿ�����

		// purchase.put("RelatedProcesses",
		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.PK_FLOW));//
		// �������

		
		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue("pk_accountant");// ���Ż��
		if (pk_accountant != null && !"~".equals(pk_accountant)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_accountant);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// ���Ż������
				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż��(���˺�)
//				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//���Ż������
//				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż�ƴ���(������)
			}else{
				purchase.put("BookKeeperName", "");// ���Ż�ƴ���
				purchase.put("BookKeeper", "");// ���Ż��
//				purchase.put("Accounting", "");//���Ż������
//				purchase.put("AccountingAccount", "");//������
			}
		}
		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue("pk_cashier");// ����
		if (pk_cashier != null && !"~".equals(pk_cashier)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_cashier);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// ����
				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// ��������
//				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
//				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ������
			}else{
				purchase.put("CaShier", "");// ����
				purchase.put("CaShierName", "");// ��������
//				purchase.put("Cashier", "");// (���ֶ�)��������
//				purchase.put("CashierAccount", "");// ������
			}
		}

		// �������
		// String def10 = (String) aggVO.getParentVO().getAttributeValue(
		// FinancexpenseVO.DEF10);// �������
		// if(def10 != null && !"~".equals(def10)){
		// Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
		// .getDefdocInfo(def10);
		// purchase.put("RelatedProcesses", infoMap.get("name"));// �������
		// }

		// ����
		String Attachments = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
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
		FinancexpenseVO parentVO=aggVO.getParentVO();
		String def29=parentVO.getDef29();
		if(isNotBlank(def29)){
			Map<String, String> paymentTypeMap = QueryDocInfoUtils.getUtils()
			          .getDefdocInfo(def29);
	        if(paymentTypeMap!=null){
	          paymentType=paymentTypeMap.get("name");
	        }
		}
		purchase.put("PaymentType", paymentType);
		
		String pk_bankaccsub=parentVO.getDef7();
		List<Map<String,String>> lists=getBankDetails(pk_bankaccsub);
		if(lists!=null&&lists.size()>0){
			receivingBankAccount=lists.get(0).get("accnum");
			receivingBankAccountName=lists.get(0).get("accname");
			receivingBankName=lists.get(0).get("docname");
		}

		purchase.put("ReceivingBankAccount", receivingBankAccount);
		purchase.put("ReceivingBankAccountName", receivingBankAccountName);
		purchase.put("ReceivingBankName", receivingBankName);
		
		List<Map<String, Object>> bodys = new ArrayList<Map<String, Object>>();// ��������
		Map<String, Object> purchaseBody=new HashMap<String, Object>();
		String def41=parentVO.getDef41();
		if(isNotBlank(def41)){
			Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(def41);
			if(invoiceTypeMap!=null){
				invoiceType=invoiceTypeMap.get("name");
			}
		}
		purchaseBody.put("InvoiceType", invoiceType);
		
		String def27=parentVO.getDef27();
		String def23=parentVO.getDef23();
		invoiceAmount=new UFDouble(isNotBlank(def27)?def27:def23) ;
		purchaseBody.put("InvoiceAmount", invoiceAmount);
		
		String def26=parentVO.getDef26();
		if(isNotBlank(def26)){
			taxRate=def26;
		}
		purchaseBody.put("TaxRate", taxRate);
		
		String def5=parentVO.getDef5();
		String def24=parentVO.getDef24();
		taxAmount=new UFDouble(isNotBlank(def5)?def5:def24);
		purchaseBody.put("TaxAmount", taxAmount);
		
		withoutTaxAmount=invoiceAmount.sub(taxAmount);
		purchaseBody.put("WithoutTaxAmount", withoutTaxAmount);
		
		bodys.add(purchaseBody);

		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_FinancialAdvisorFee", purchase);

		formData.put("C_FinancialAdvisorFeeInvoice_D", bodys);// ��������
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
		return formData;
	}
	
	//֧����ʽ����
	
}
