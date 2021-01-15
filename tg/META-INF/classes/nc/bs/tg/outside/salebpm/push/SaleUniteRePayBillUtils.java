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
 * ͳ��ͳ��-�ڲ����
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
		aggVO.getParentVO().setAttributeValue("def60", "N");//nc�ջ�bpm��ʶ
		aggVO.getParentVO().setAttributeValue("def71", "N");//nc֪ͨbpm���ر�ʶ
		aggVO.getParentVO().setAttributeValue("def33", null);
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
			AggPayBillVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = getPayBillsInfo(aggVO);
		return formData;
	}

	/**
	 * ��ø��������Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getPayBillsInfo(AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		// ��������
		purchase.put("BussinessId",
				aggVO.getParentVO().getAttributeValue(PayBillVO.PK_PAYBILL));
		
		//����
		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF54));
		
		//�����˺����벿��
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
		
		//��������
		purchase.put("ApplicationDate",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE)==null?null:aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE).toString());

		//���빫˾
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
		
		
		
		//���λ
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
		
		//�ſ0,���1
		purchase.put("IsPayBackOrNot",1);
		
		//���λ������
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
		
		//���λ�����˺�
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
		//��������
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
		
		//�ܽ��
		purchase.put("Amount",
				aggVO.getParentVO().getAttributeValue(PayBillVO.MONEY));
		
		//�ÿ�����
		purchase.put("UsageContent",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_A));
		
		//��ע
		purchase.put("Remarks",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_B));
		
		
		
		// ����
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
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("I_JTAllborrowallalsoProcess", purchase);
		
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();
		
		PayBillItemVO[] bodyvos = aggVO.getBodyVOs();
		for (PayBillItemVO payBillItemVO : bodyvos) {
			Map<String, Object> bodyPurchase = new HashMap<String, Object>();// ��ͷ����
			//�տλ
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
			
			//�տλ������
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
			
			//�տλ�����˺�
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
			
			//�ܽ��
			bodyPurchase.put("Amount",
					payBillItemVO.getMoney_de());
			
			listPurchase.add(bodyPurchase);
		}
		
		formData.put("C_JTAllborrowallalso_Detail", listPurchase);
		

		return formData;
	}

	/**
	 * ��ò�����ʷ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
//	private Map<String, Object> getFinancialAdvisorFeeInfo(
//			AggPayBillVO aggVO) throws BusinessException {
//		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
//
//		purchase.put("Title",
//				aggVO.getParentVO().getAttributeValue(FinancexpenseVO.TITLE));// ����
//		// ������
//		String pk_applicant = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_APPLICANT);
//		if (pk_applicant != null && !"~".equals(pk_applicant)) {
//			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_applicant);
//			if(psnInfoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(psnInfoMap.get("code"));
//				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���������˺�
//			}else{
//				purchase.put("ApplicantCode", "");
//			}
//			purchase.put("Applicant", psnInfoMap.get("name"));// ������
//		}
//
//		// ��������
//		UFDate date = (UFDate) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.APPLICATIONDATE);
//		if (date != null) {
//			String applicationDate = date.toStdString();
//			purchase.put("ApplicationDate", applicationDate);// ��������
//		}
//
//		// ���빫˾
//		String pk_applicationorg = (String) aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.PK_APPLICATIONORG);
//		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
//			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
//					.getOrgInfo(pk_applicationorg);
//			purchase.put("ApplicationCompany", orgInfoMap.get("name"));// ���빫˾
//			purchase.put("ApplicationCompanyCode", orgInfoMap.get("code"));// ���빫˾����
//		}
//
//		// ���벿��
//		String pk_applicationdept = (String) aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.PK_APPLICATIONDEPT);
//		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
//			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
//					.getDeptInfo(pk_applicationdept);
//			String deptid = getHCMDeptID((String) aggVO.getParentVO()
//					.getAttributeValue(FinancexpenseVO.PK_APPLICATIONDEPT));
//			purchase.put("ApplicationDepartmentCode", deptid);// ���벿������
//			purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// ���벿��
//
//		}
//
//		// ��Ŀ����
//		String pk_project = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_PROJECT);
//		if (pk_project != null && !"~".equals(pk_project)) {
//			Map<String, String> projectInfoMap = QueryDocInfoUtils.getUtils()
//					.getProjectDataInfo(pk_project);
//			purchase.put("ProjectName", projectInfoMap.get("name"));// ��Ŀ����
//		}
//
//		// �տλ
//		String pk_payee = (String) aggVO.getParentVO().getAttributeValue(
//				"def13");
////		if (pk_payee != null && !"~".equals(pk_payee)) {
////			Map<String, String> custInfoMap = QueryDocInfoUtils.getUtils()
////					.getCustInfo(pk_payee);
////			purchase.put("ReceivingUnit", custInfoMap.get("name"));
////		}
//		purchase.put("ReceivingUnit", pk_payee==null?"":pk_payee);
//		// ���λ
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
//		purchase.put("TotalAmount", totalAmout);// ��ͬ�ܽ��
//
//		String accumulatedPayment = aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PAYMENTAMOUNT) == null ? "" : aggVO
//				.getParentVO().getAttributeValue(FinancexpenseVO.PAYMENTAMOUNT)
//				.toString();
//		purchase.put("AlreadyPaid", accumulatedPayment);// �ۼ��Ѹ����
//
//		String amountRequested = aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.APPLYAMOUNT) == null ? "" : aggVO.getParentVO()
//				.getAttributeValue(FinancexpenseVO.APPLYAMOUNT).toString();
//		purchase.put("RequestAmount", amountRequested);// ���������
//
//		purchase.put(
//				"UseContent",
//				aggVO.getParentVO().getAttributeValue(
//						FinancexpenseVO.USECONTENT));// �ÿ�����
//
//		// purchase.put("RelatedProcesses",
//		// aggVO.getParentVO().getAttributeValue(FinancexpenseVO.PK_FLOW));//
//		// �������
//
//		
//		String pk_accountant = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_ACCOUNTANT);// ���Ż��
//		if (pk_accountant != null && !"~".equals(pk_accountant)) {
//			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_accountant);
//			if(infoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(infoMap.get("code"));
//				purchase.put("BookKeeperName", infoMap.get("name")==null?"":infoMap.get("name"));// ���Ż������
//				purchase.put("BookKeeper", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż��(���˺�)
////				purchase.put("Accounting", infoMap.get("name")==null?"":infoMap.get("name"));//���Ż������
////				purchase.put("AccountingAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ���Ż�ƴ���(������)
//			}else{
//				purchase.put("BookKeeperName", "");// ���Ż�ƴ���
//				purchase.put("BookKeeper", "");// ���Ż��
////				purchase.put("Accounting", "");//���Ż������
////				purchase.put("AccountingAccount", "");//������
//			}
//		}
//		String pk_cashier = (String) aggVO.getParentVO().getAttributeValue(
//				FinancexpenseVO.PK_CASHIER);// ����
//		if (pk_cashier != null && !"~".equals(pk_cashier)) {
//			Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//					.getPsnInfo(pk_cashier);
//			if(infoMap!=null){
//				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
//						.getRegionNameByPersonCode(infoMap.get("code"));
//				purchase.put("CaShier", accountant_account==null?"":accountant_account.get("userprincipalname"));// ����
//				purchase.put("CaShierName", infoMap.get("name")==null?"":infoMap.get("name"));// ��������
////				purchase.put("Cashier", infoMap.get("name")==null?"":infoMap.get("name"));
////				purchase.put("CashierAccount", accountant_account==null?"":accountant_account.get("userprincipalname"));// ������
//			}else{
//				purchase.put("CaShier", "");// ����
//				purchase.put("CaShierName", "");// ��������
////				purchase.put("Cashier", "");// (���ֶ�)��������
////				purchase.put("CashierAccount", "");// ������
//			}
//		}
//
//		// �������
//		// String def10 = (String) aggVO.getParentVO().getAttributeValue(
//		// FinancexpenseVO.DEF10);// �������
//		// if(def10 != null && !"~".equals(def10)){
//		// Map<String, String> infoMap = QueryDocInfoUtils.getUtils()
//		// .getDefdocInfo(def10);
//		// purchase.put("RelatedProcesses", infoMap.get("name"));// �������
//		// }
//
//		// ����
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
//		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
//		formData.put("T_FinancialAdvisorFee", purchase);
//
//		return formData;
//	}
}
