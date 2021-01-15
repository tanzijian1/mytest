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
 * 财顾费/融资管理
 * 
 * @author HUANGDQ
 * @date 2019年6月26日 下午6:32:46
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
		aggVO.getParentVO().setAttributeValue("def34", null);//驳回通知bpm标志
		aggVO.getParentVO().setAttributeValue("def19", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def20", infoMap.get("openurl"));
		return aggVO;
	}

	/**
	 * 信息转换
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggFinancexpenseVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		if (IBPMBillCont.BILL_RZ06_01.equals(billCode)) {
			formData = getFinancialAdvisorFeeInfo(aggVO);
		} else {
			formData = getFinancingCostsInfo(aggVO);
		}

		return formData;
	}

	/**
	 * 获得融资费用传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFinancingCostsInfo(AggFinancexpenseVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据

		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue("title"));// 标题
		
		//主组织
		String pk_org = (String) aggVO.getParentVO().getAttributeValue("pk_org");
		String pk_payer =  (String) aggVO.getParentVO().getAttributeValue("pk_payer");
		String easy = null;
		if(StringUtils.isNotBlank(pk_org)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11")==null?null:orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"简化业财模式".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else if(StringUtils.isBlank(pk_org)&&StringUtils.isNotBlank(pk_payer)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_payer);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11");
			}
			if(StringUtils.isNotBlank(easy)&&"简化业财模式".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else{
			purchase.put("ProposalProcess", null);
		}
		//请款发票金额
		if (aggVO.getParentVO().getAttributeValue("def27") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def27"))) {
			purchase.put("RequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def27").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("RequestInvoiceAmount", null);
		}
		
		//累计请款发票金额
		if (aggVO.getParentVO().getAttributeValue("def28") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def28"))) {
			purchase.put("CumulativeRequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def28").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("CumulativeRequestInvoiceAmount", null);
		}
		
		//支付方式
		String paytype = (String) aggVO.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = QueryDocInfoUtils.getUtils().getDefdocInfo(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//支付方式code
			}else{
				purchase.put("PaymentMethod", null);//支付方式code
			}
		}else{
			purchase.put("PaymentMethod", null);//支付方式code
		}
		
		purchase.put("BarCode", aggVO.getParentVO().getAttributeValue("def21")==null?null:aggVO.getParentVO().getAttributeValue("def21"));//影像编码

		// 申请人
		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue("pk_applicant");
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// 制单人域账号
			}else{
				purchase.put("ApplicantCode", null);
			}
			purchase.put("Applicant", psnInfoMap==null?null:psnInfoMap.get("name"));// 申请人
		}else{
			purchase.put("ApplicantCode", null);// 制单人域账号
			purchase.put("Applicant", null);// 申请人
		}

		// 申请日期
		if(aggVO.getParentVO().getAttributeValue("applicationdate")!=null){
			UFDate date = new UFDate(aggVO.getParentVO().getAttributeValue("applicationdate").toString());
			if (date != null) {
				String applicationDate = date.toStdString();
				purchase.put("ApplicationDate", applicationDate);// 申请日期
			}
		}else{
			purchase.put("ApplicationDate", null);// 申请日期
		}

		// 申请公司
		String pk_applicationorg = (String) aggVO.getParentVO().getAttributeValue("pk_applicationorg");
		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_applicationorg);
			purchase.put("ApplicationCompany", orgInfoMap==null?null:orgInfoMap.get("name"));// 申请公司
			purchase.put("ApplicationCompanyCode", orgInfoMap==null?null:orgInfoMap.get("code"));// 申请公司代码
		}else{
			purchase.put("ApplicationCompany", null);// 申请公司
			purchase.put("ApplicationCompanyCode", null);// 申请公司代码
		}

		// 申请部门
		String pk_applicationdept = (String) aggVO.getParentVO().getAttributeValue("pk_applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
					.getDeptInfo(pk_applicationdept);
			String deptid = getHCMDeptID((String) aggVO.getParentVO().getPk_applicationdept());
			purchase.put("ApplicationDepartmentCode", deptid);// 申请部门代码
			purchase.put("ApplicationDepartment", deptInfoMap==null?null:deptInfoMap.get("name"));// 申请部门
		}else{
			purchase.put("ApplicationDepartmentCode", null);// 申请部门代码
			purchase.put("ApplicationDepartment", null);// 申请部门
		}


		// 项目名称
		String pk_project = (String) aggVO.getParentVO().getAttributeValue("pk_project");
		if (pk_project != null && !"~".equals(pk_project)) {
			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
					.getProjectDataInfo(pk_project);
			purchase.put("ProjectName", projectInfoMap==null?null:projectInfoMap.get("name"));// 项目名称
		}else{
			purchase.put("ProjectName", null);// 项目名称
		}

		// 摘要/备注
		if (aggVO.getParentVO().getAttributeValue("big_text_b") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("big_text_b"))) {
			purchase.put(
					"Remarks",
					aggVO.getParentVO().getAttributeValue("big_text_b"));// 项目名称
		}else{
			purchase.put(
					"Remarks",
					null);// 项目名称
		}

		// 收款单位
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
			purchase.put("ReceivingUnit", psnInfoMap ==null?null:psnInfoMap.get("name"));// 收款方名称
		}else{
			purchase.put("ReceivingUnit", null);// 收款方名称
		}
		
		// 付款单位
		if (pk_payer != null && !"~".equals(pk_payer)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_payer);
			purchase.put("PaymentUnit", infoMap==null?null:infoMap.get("name"));
		}else{
			purchase.put("PaymentUnit", null);
		}
		String totalAmout = aggVO.getParentVO().getAttributeValue("contractmoney") == null ? "" : aggVO.getParentVO().getAttributeValue("contractmoney").toString();
		purchase.put("TotalAmount", totalAmout);// 合同总金额

		String accumulatedPayment = aggVO.getParentVO().getAttributeValue("paymentamount") == null ? "" : aggVO.getParentVO().getAttributeValue("paymentamount")
				.toString();
		purchase.put("AccumulatedPayment", accumulatedPayment);// 累计已付金额

		String amountRequested = aggVO.getParentVO().getAttributeValue("applyamount") == null ? "" : aggVO.getParentVO().getAttributeValue("applyamount").toString();
		purchase.put("AmountRequested", amountRequested);// 本次请款金额

		String pk_costitem = (String) aggVO.getParentVO().getAttributeValue("def5");
		if (pk_costitem != null && !"~".equals(pk_costitem)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(pk_costitem);
			purchase.put("CostItem", infoMap==null?null:infoMap.get("name"));
		}else{
			purchase.put("CostItem", null);
		}
		

		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue("pk_accountant");// 集团会计
		if (pk_accountant != null && !"~".equals(pk_accountant)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_accountant);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// 集团会计代码BookKeeper
				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// 集团会计
//				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//集团会计名称
//				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 域名称
			}else{
				purchase.put("BookKeeper", "");// 集团会计
				purchase.put("BookKeeperName", "");// 集团会计代码
//				purchase.put("Accounting", "");//集团会计名称
//				purchase.put("AccountingAccount", "");//域名称
			}
		}
		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue("pk_cashier");// 出纳
		if (pk_cashier != null && !"~".equals(pk_cashier)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_cashier);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// 出纳名称
				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// 出纳代码(域名称)
//				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
//				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 域名称
			}else{
				purchase.put("CaShier", "");// 出纳
				purchase.put("CaShierName", "");// 出纳名称
//				purchase.put("Cashier", "");// (新字段)出纳名称
//				purchase.put("CashierAccount", "");// 域名称
			}
		}
		// purchase.put("Remarks",
		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.MESSAGE));// 备注
		

		// 附件
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
		
		//add by 黄冠华 SDYC-477融资业财-对接BPM 需求 20200831 begin
		String paymentType = "";// 请款方式
		String receivingBankAccount = "";// 收款银行账户
		String receivingBankAccountName = "";// 收款银行账户户名
		String receivingBankName = "";// 收款银行账户开户行
		String invoiceType = "";// 发票类型
		UFDouble invoiceAmount = new UFDouble(0);// 发票金额
		String taxRate = "";// 税率
		UFDouble withoutTaxAmount = new UFDouble(0);// 不含税金额
		UFDouble taxAmount =new UFDouble(0);// 税额
		
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
		
		List<Map<String, Object>> bodys = new ArrayList<Map<String, Object>>();// 表体数据
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
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("T_FinancingCosts", purchase);
        
		formData.put("C_ThisInvoiceInfo_Detail", bodys);// 表体数据
		
		//add by 黄冠华 SDYC-477融资业财-对接BPM 需求 20200831 end
		return formData;
	}

	/**
	 * 获得财务顾问费用传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFinancialAdvisorFeeInfo(
			AggFinancexpenseVO aggVO) throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据

		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue("title"));// 标题
		//主付款组织
		String pk_payer =  (String) aggVO.getParentVO().getAttributeValue("pk_payer");
		//财务组织
		String def61 = (String) aggVO.getParentVO().getAttributeValue("def61");
		String easy = null;
		if(StringUtils.isNotBlank(pk_payer)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_payer);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"简化业财模式".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else if(StringUtils.isBlank(pk_payer)&&StringUtils.isNotBlank(def61)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(def61);
			if(orgsInfoMap!=null){
				easy = orgsInfoMap.get("def11").trim();
			}
			if(StringUtils.isNotBlank(easy)&&"简化业财模式".equals(easy)){
				purchase.put("ProposalProcess", easy);
			}else{
				purchase.put("ProposalProcess", null);
			}
		}else{
			purchase.put("ProposalProcess", null);
		}
		purchase.put("BarCode", aggVO.getParentVO().getAttributeValue("def21")==null?"":aggVO.getParentVO().getAttributeValue("def21"));//影像编码
		
		// 合同主键
		String pk_contract = (String) aggVO.getParentVO().getAttributeValue(
				"def4");
		
		if (pk_contract != null && !"~".equals(pk_contract)) {
			if(QueryDocInfoUtils.getUtils().getContract(pk_contract)!=null){
				purchase.put("ContractName", QueryDocInfoUtils.getUtils().getContract(pk_contract).get("name")==null?"0":QueryDocInfoUtils.getUtils().getContract(pk_contract).get("name"));//合同名称
				purchase.put("ContractNo", QueryDocInfoUtils.getUtils().getContract(pk_contract).get("code")==null?"0":QueryDocInfoUtils.getUtils().getContract(pk_contract).get("code"));//合同编码
			}
		}else{
			purchase.put("ContractName", "0");//合同名称
			purchase.put("ContractNo", "0");//合同编码
		}
		
		//请款发票金额
		if (aggVO.getParentVO().getAttributeValue("def27") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def27"))) {
			purchase.put("RequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def27").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("RequestInvoiceAmount", null);
		}
		
		//累计请款发票金额
		if (aggVO.getParentVO().getAttributeValue("def28") != null
				&& !"~".equals(aggVO.getParentVO().getAttributeValue("def28"))) {
			purchase.put("CumulativeRequestInvoiceAmount", new UFDouble(aggVO.getParentVO().getAttributeValue("def28").toString()).setScale(2, 2).toString());
		}else{
			purchase.put("CumulativeRequestInvoiceAmount", null);
		}
		
		//支付方式
		String paytype = (String) aggVO.getParentVO().getAttributeValue("def42");
		if(paytype != null && !"~".equals(paytype)){
			Map<String, String> payTypeInfoMap = QueryDocInfoUtils.getUtils().getDefdocInfo(paytype);
			if(payTypeInfoMap!=null){
				purchase.put("PaymentMethod", QueryDocInfoUtils.getUtils().payTypeInfo(payTypeInfoMap.get("code")));//支付方式code
			}else{
				purchase.put("PaymentMethod", null);//支付方式code
			}
		}else{
			purchase.put("PaymentMethod", null);//支付方式code
		}
		
		// 申请人
		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue("pk_applicant");
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// 申请人域账号
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap.get("name"));// 申请人
		}else{
			purchase.put("Applicant", null);// 申请人
		}

		// 申请日期
		if(aggVO.getParentVO().getAttributeValue("applicationdate")!=null){
			UFDate date = new UFDate(aggVO.getParentVO().getAttributeValue("applicationdate").toString());
			if (date != null) {
				String applicationDate = date.toStdString();
				purchase.put("ApplicationDate", applicationDate);// 申请日期
			}
		}else{
			purchase.put("ApplicationDate", null);// 申请日期
		}

		// 申请公司
		String pk_applicationorg = (String) aggVO.getParentVO().getAttributeValue("pk_applicationorg");
		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_applicationorg);
			purchase.put("ApplicationCompany", orgInfoMap==null?null:orgInfoMap.get("name"));// 申请公司
			purchase.put("ApplicationCompanyCode", orgInfoMap==null?null:orgInfoMap.get("code"));// 申请公司代码
		}else{
			purchase.put("ApplicationCompany", null);// 申请公司
			purchase.put("ApplicationCompanyCode", null);// 申请公司代码
		}

		// 申请部门
		String pk_applicationdept = (String) aggVO.getParentVO()
				.getAttributeValue("pk_applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
					.getDeptInfo(pk_applicationdept);
			String deptid = getHCMDeptID((String) aggVO.getParentVO()
					.getPk_applicationdept());
			purchase.put("ApplicationDepartmentCode", deptid);// 申请部门名称
			purchase.put("ApplicationDepartment", deptInfoMap==null?null:deptInfoMap.get("name"));// 申请部门
		}else{
			purchase.put("ApplicationDepartmentCode", null);// 申请部门名称
			purchase.put("ApplicationDepartment", null);// 申请部门
		}

		// 项目名称
		String pk_project = (String) aggVO.getParentVO().getAttributeValue("pk_project");
		if (pk_project != null && !"~".equals(pk_project)) {
			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
					.getProjectDataInfo(pk_project);
			purchase.put("ProjectName", projectInfoMap.get("name"));// 项目名称
		}else{
			purchase.put("ProjectName", null);// 项目名称
		}

		// 收款单位
		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
				"pk_payee");
//		if (pk_payee != null && !"~".equals(pk_payee)) {
//			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
//					.getCustInfo(pk_payee);
//			purchase.put("ReceivingUnit", custInfoMap.get("name"));
//		}
		if(pk_payee != null && !"~".equals(pk_payee)){
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils().getCustInfo(pk_payee);
			purchase.put("ReceivingUnit", psnInfoMap ==null?"":psnInfoMap.get("name"));// 收款方名称
		}else{
			purchase.put("ReceivingUnit", null);// 收款方名称
		}
		//财务组织
		if (pk_payer != null && !"~".equals(pk_payer)) {//若付款单位不为空则传付款单位
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_payer);
			if(infoMap!=null){
				purchase.put("PaymentUnit", infoMap.get("name"));
			}else{
				purchase.put("PaymentUnit", null);
			}
		}else if(StringUtils.isBlank(pk_payer)&&StringUtils.isNotBlank(def61)){//若付款单位为空且财务组织不为空则传财务组织,其余传null
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
		purchase.put("TotalAmount", totalAmout);// 合同总金额

		String accumulatedPayment = aggVO.getParentVO().getAttributeValue("paymentamount") == null ? "" : aggVO.getParentVO().getAttributeValue("paymentamount")
				.toString();
		purchase.put("AlreadyPaid", accumulatedPayment);// 累计已付金额

		String amountRequested = aggVO.getParentVO().getAttributeValue("applyamount") == null ? "" : aggVO.getParentVO().getAttributeValue("applyamount").toString();
		purchase.put("RequestAmount", amountRequested);// 本次请款金额

		purchase.put(
				"UseContent",
				aggVO.getParentVO().getAttributeValue("big_text_b"));// 用款内容

		// purchase.put("RelatedProcesses",
		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.PK_FLOW));//
		// 相关流程

		
		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue("pk_accountant");// 集团会计
		if (pk_accountant != null && !"~".equals(pk_accountant)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_accountant);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// 集团会计名称
				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// 集团会计(域账号)
//				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//集团会计名称
//				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 集团会计代码(域名称)
			}else{
				purchase.put("BookKeeperName", "");// 集团会计代码
				purchase.put("BookKeeper", "");// 集团会计
//				purchase.put("Accounting", "");//集团会计名称
//				purchase.put("AccountingAccount", "");//域名称
			}
		}
		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue("pk_cashier");// 出纳
		if (pk_cashier != null && !"~".equals(pk_cashier)) {
			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_cashier);
			if(infoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(infoMap.get("code"));
				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// 出纳
				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// 出纳名称
//				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
//				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 域名称
			}else{
				purchase.put("CaShier", "");// 出纳
				purchase.put("CaShierName", "");// 出纳名称
//				purchase.put("Cashier", "");// (新字段)出纳名称
//				purchase.put("CashierAccount", "");// 域名称
			}
		}

		// 相关流程
		// String def10 = (String) aggVO.getParentVO().getAttributeValue(
		// FinancexpenseVO.DEF10);// 相关流程
		// if(def10 != null && !"~".equals(def10)){
		// Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
		// .getDefdocInfo(def10);
		// purchase.put("RelatedProcesses", infoMap.get("name"));// 相关流程
		// }

		// 附件
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
		
		//add by 黄冠华 SDYC-477融资业财-对接BPM 需求 20200831 begin
		String paymentType = "";// 请款方式
		String receivingBankAccount = "";// 收款银行账户
		String receivingBankAccountName = "";// 收款银行账户户名
		String receivingBankName = "";// 收款银行账户开户行
		String invoiceType = "";// 发票类型
		UFDouble invoiceAmount = new UFDouble(0);// 发票金额
		String taxRate = "";// 税率
		UFDouble withoutTaxAmount = new UFDouble(0);// 不含税金额
		UFDouble taxAmount =new UFDouble(0);// 税额
		
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
		
		List<Map<String, Object>> bodys = new ArrayList<Map<String, Object>>();// 表体数据
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

		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("T_FinancialAdvisorFee", purchase);

		formData.put("C_FinancialAdvisorFeeInvoice_D", bodys);// 表体数据
		//add by 黄冠华 SDYC-477融资业财-对接BPM 需求 20200831 end
		return formData;
	}
	
	//支付方式对照
	
}
