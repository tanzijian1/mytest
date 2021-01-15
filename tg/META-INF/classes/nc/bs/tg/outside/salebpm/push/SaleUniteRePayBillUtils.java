package nc.bs.tg.outside.salebpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.salebpm.utils.SaleBPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.pnt.vo.FileManageVO;
import nc.vo.arap.pay.AggPayBillVO;
import nc.vo.arap.pay.PayBillItemVO;
import nc.vo.arap.pay.PayBillVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.FinancexpenseVO;

/**
 * 统借统还-内部还款单
 * @author nctanjingliang
 *
 */
public class SaleUniteRePayBillUtils extends SaleBPMBillUtils{

	static SaleUniteRePayBillUtils utils;

	public static SaleUniteRePayBillUtils getUtils() {
		if (utils == null) {
			utils = new SaleUniteRePayBillUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggPayBillVO aggVO = (AggPayBillVO) bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("pu_deptid"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = SalePushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						ISaleBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def55"));
		aggVO.getParentVO().setAttributeValue("def55", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def56", infoMap.get("OpenUrl"));
		aggVO.getParentVO().setAttributeValue("def60", "N");//nc收回bpm标识
		aggVO.getParentVO().setAttributeValue("def71", "N");//nc通知bpm驳回标识
		aggVO.getParentVO().setAttributeValue("def33", null);
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
			AggPayBillVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		formData = getPayBillsInfo(aggVO);
		return formData;
	}

	/**
	 * 获得付款单传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getPayBillsInfo(AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		// 单据主键
		purchase.put("BussinessId",
				aggVO.getParentVO().getAttributeValue(PayBillVO.PK_PAYBILL));
		
		//标题
		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF54));
		
		//申请人和申请部门
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER)!=null){
			if(getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER))!=null){
				purchase.put("Applicant",
						getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER)).get("psnname"));
				purchase.put("ApplicationDepartment",getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER)).get("deptname"));
			}else{
				purchase.put("Applicant",null);
				purchase.put("ApplicationDepartment",null);
			}
		}else{
			purchase.put("Applicant",null);
			purchase.put("ApplicationDepartment",null);
		}
		
		//申请日期
		purchase.put("ApplicationDate",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE)==null?null:aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE).toString());

		//申请公司
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG)!=null){
			if(QueryDocInfoUtils.getUtils().getOrgInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG))!=null){
				purchase.put("ApplicationCompany",
						QueryDocInfoUtils.getUtils().getOrgInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG)).get("name"));
			}else{
				purchase.put("ApplicationCompany",null);
			}
		}else{
			purchase.put("ApplicationCompany",null);
		}
		
		
		
		//付款单位
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG)!=null){
			if(QueryDocInfoUtils.getUtils().getOrgInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG))!=null){
				purchase.put("PaymentUnit",
						QueryDocInfoUtils.getUtils().getOrgInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG)).get("name"));
			}else{
				purchase.put("PaymentUnit",null);
			}
		}else{
			purchase.put("PaymentUnit",null);
		}
		
		//放款传0,还款传1
		purchase.put("IsPayBackOrNot",1);
		
		//付款单位开户行
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT)!=null){
			if(QueryDocInfoUtils.getUtils().getBankDoctInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT))!=null){
				purchase.put("OpeningBankOfPayer",
						QueryDocInfoUtils.getUtils().getBankDoctInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT)).get("name"));
			}else{
				purchase.put("OpeningBankOfPayer",null);
			}
		}else{
			purchase.put("OpeningBankOfPayer",null);
		}
		
		//付款单位银行账号
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT)!=null){
			if(QueryDocInfoUtils.getUtils().getBankDoctInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT))!=null){
				purchase.put("BankAccountNoOfPayer",
						QueryDocInfoUtils.getUtils().getBankDoctInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.PAYACCOUNT)).get("accnum"));
			}else{
				purchase.put("BankAccountNoOfPayer",null);
			}
		}else{
			purchase.put("BankAccountNoOfPayer",null);
		}
		//款项类型
		if(aggVO.getParentVO().getAttributeValue(PayBillVO.DEF68)!=null){
			if(QueryDocInfoUtils.getUtils().getDefdocInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.DEF68))!=null){
				purchase.put("TypeOfPayment",
						QueryDocInfoUtils.getUtils().getDefdocInfo((String) aggVO.getParentVO().getAttributeValue(PayBillVO.DEF68)).get("name"));
			}else{
				purchase.put("TypeOfPayment",null);
			}
		}else{
			purchase.put("TypeOfPayment",null);
		}
		
		//总金额
		purchase.put("Amount",
				aggVO.getParentVO().getAttributeValue(PayBillVO.MONEY));
		
		//用款内容
		purchase.put("UsageContent",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_A));
		
		//备注
		purchase.put("Remarks",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_B));
		
		
		
		// 附件
		String Attachments = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
		if (fileVOs != null && fileVOs.size() > 0) {
			for (int i = 0; i < fileVOs.size(); i++) {
				Attachments += fileVOs.get(i).getDocument_name() + "&"
						+ fileVOs.get(i).getFile_id() + ";";
			}
			purchase.put("File",
					Attachments.substring(0, Attachments.lastIndexOf(";")));
		} else {
			purchase.put("File", null);
		}
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("I_JTAllborrowallalsoProcess", purchase);
		
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();
		
		PayBillItemVO[] bodyvos = aggVO.getBodyVOs();
		for (PayBillItemVO payBillItemVO : bodyvos) {
			Map<String, Object> bodyPurchase = new HashMap<String, Object>();// 表头数据
			//收款单位
			if(payBillItemVO.getSupplier()!=null){
				if(QueryDocInfoUtils.getUtils().getCustInfo(payBillItemVO.getSupplier())!=null){
					bodyPurchase.put("CollectionUnit",
							QueryDocInfoUtils.getUtils().getCustInfo(payBillItemVO.getSupplier()).get("name"));
				}else{
					bodyPurchase.put("CollectionUnit",null);
				}
			}else{
				bodyPurchase.put("CollectionUnit",null);
			}
			
			//收款单位开户行
			if(payBillItemVO.getRecaccount()!=null){
				if(QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount())!=null){
					bodyPurchase.put("OpeningBankOfPayee",
							QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount()).get("name"));
				}else{
					bodyPurchase.put("OpeningBankOfPayee",null);
				}
			}else{
				bodyPurchase.put("OpeningBankOfPayee",null);
			}
			
			//收款单位银行账号
			if(payBillItemVO.getRecaccount()!=null){
				if(QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount())!=null){
					bodyPurchase.put("BankAccountNoOfPayee",
							QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount()).get("accnum"));
				}else{
					bodyPurchase.put("BankAccountNoOfPayee",null);
				}
			}else{
				bodyPurchase.put("BankAccountNoOfPayee",null);
			}
			
			//总金额
			bodyPurchase.put("Amount",
					payBillItemVO.getMoney_de());
			
			listPurchase.add(bodyPurchase);
		}
		
		formData.put("C_JTAllborrowallalso_Detail", listPurchase);
		

		return formData;
	}

	/**
	 * 获得财务顾问费用传参信息
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
//	private Map<String, Object> getFinancialAdvisorFeeInfo(
//			AggPayBillVO aggVO) throws BusinessException {
//		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
//
//		purchase.put("Title",
//				aggVO.getParentVO().getAttributeValue(FinancexpenseVO.TITLE));// 标题
//		// 申请人
//		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_APPLICANT);
//		if (pk_applicant != null && !"~".equals(pk_applicant)) {
//			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_applicant);
//			if(psnInfoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(psnInfoMap.get("code"));
//				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// 申请人域账号
//			}else{
//				purchase.put("ApplicantCode", "");
//			}
//			purchase.put("Applicant", psnInfoMap.get("name"));// 申请人
//		}
//
//		// 申请日期
//		UFDate date = (UFDate) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.APPLICATIONDATE);
//		if (date != null) {
//			String applicationDate = date.toStdString();
//			purchase.put("ApplicationDate", applicationDate);// 申请日期
//		}
//
//		// 申请公司
//		String pk_applicationorg = (String) aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.PK_APPLICATIONORG);
//		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
//			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
//					.getOrgInfo(pk_applicationorg);
//			purchase.put("ApplicationCompany", orgInfoMap.get("name"));// 申请公司
//			purchase.put("ApplicationCompanyCode", orgInfoMap.get("code"));// 申请公司代码
//		}
//
//		// 申请部门
//		String pk_applicationdept = (String) aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.PK_APPLICATIONDEPT);
//		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
//			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
//					.getDeptInfo(pk_applicationdept);
//			String deptid = getHCMDeptID((String) aggVO.getParentVO()
//					.getAttributeValue(FinancexpenseVO.PK_APPLICATIONDEPT));
//			purchase.put("ApplicationDepartmentCode", deptid);// 申请部门名称
//			purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// 申请部门
//
//		}
//
//		// 项目名称
//		String pk_project = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_PROJECT);
//		if (pk_project != null && !"~".equals(pk_project)) {
//			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
//					.getProjectDataInfo(pk_project);
//			purchase.put("ProjectName", projectInfoMap.get("name"));// 项目名称
//		}
//
//		// 收款单位
//		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
//				"def13");
////		if (pk_payee != null && !"~".equals(pk_payee)) {
////			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
////					.getCustInfo(pk_payee);
////			purchase.put("ReceivingUnit", custInfoMap.get("name"));
////		}
//		purchase.put("ReceivingUnit", pk_payee==null?"":pk_payee);
//		// 付款单位
//		String pk_payer = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_PAYER);
//		if (pk_payer != null && !"~".equals(pk_payer)) {
//			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//					.getOrgInfo(pk_payer);
//			purchase.put("PaymentUnit", infoMap.get("name"));
//		}
//
//		String totalAmout = aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.CONTRACTMONEY) == null ? "" : aggVO
//				.getParentVO().getAttributeValue(FinancexpenseVO.CONTRACTMONEY)
//				.toString();
//		purchase.put("TotalAmount", totalAmout);// 合同总金额
//
//		String accumulatedPayment = aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PAYMENTAMOUNT) == null ? "" : aggVO
//				.getParentVO().getAttributeValue(FinancexpenseVO.PAYMENTAMOUNT)
//				.toString();
//		purchase.put("AlreadyPaid", accumulatedPayment);// 累计已付金额
//
//		String amountRequested = aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.APPLYAMOUNT) == null ? "" : aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.APPLYAMOUNT).toString();
//		purchase.put("RequestAmount", amountRequested);// 本次请款金额
//
//		purchase.put(
//				"UseContent",
//				aggVO.getParentVO().getAttributeValue(
//						FinancexpenseVO.USECONTENT));// 用款内容
//
//		// purchase.put("RelatedProcesses",
//		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.PK_FLOW));//
//		// 相关流程
//
//		
//		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_ACCOUNTANT);// 集团会计
//		if (pk_accountant != null && !"~".equals(pk_accountant)) {
//			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_accountant);
//			if(infoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(infoMap.get("code"));
//				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// 集团会计名称
//				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// 集团会计(域账号)
////				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//集团会计名称
////				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 集团会计代码(域名称)
//			}else{
//				purchase.put("BookKeeperName", "");// 集团会计代码
//				purchase.put("BookKeeper", "");// 集团会计
////				purchase.put("Accounting", "");//集团会计名称
////				purchase.put("AccountingAccount", "");//域名称
//			}
//		}
//		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_CASHIER);// 出纳
//		if (pk_cashier != null && !"~".equals(pk_cashier)) {
//			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_cashier);
//			if(infoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(infoMap.get("code"));
//				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// 出纳
//				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// 出纳名称
////				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
////				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// 域名称
//			}else{
//				purchase.put("CaShier", "");// 出纳
//				purchase.put("CaShierName", "");// 出纳名称
////				purchase.put("Cashier", "");// (新字段)出纳名称
////				purchase.put("CashierAccount", "");// 域名称
//			}
//		}
//
//		// 相关流程
//		// String def10 = (String) aggVO.getParentVO().getAttributeValue(
//		// FinancexpenseVO.DEF10);// 相关流程
//		// if(def10 != null && !"~".equals(def10)){
//		// Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//		// .getDefdocInfo(def10);
//		// purchase.put("RelatedProcesses", infoMap.get("name"));// 相关流程
//		// }
//
//		// 附件
//		String Attachments = "";
//		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
//				aggVO.getPrimaryKey());
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
//		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
//		formData.put("T_FinancialAdvisorFee", purchase);
//
//		return formData;
//	}
}
