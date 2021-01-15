package nc.bs.tg.outside.salebpm.push;

import java.util.Collection;
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
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class SaleBankCommissionUtils extends SaleBPMBillUtils{

	static SaleBankCommissionUtils utils;

	public static SaleBankCommissionUtils getUtils() {
		if (utils == null) {
			utils = new SaleBankCommissionUtils();
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
		Map<String, String> infoMap = SalePushBPMBillUtils.getUtils()
				.pushBillToBpm(userid, formData,
						ISaleBPMBillCont.getBillNameMap().get(billCode),
						deptid, bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def55"));
		aggVO.getParentVO().setAttributeValue("def55", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def56", infoMap.get("OpenUrl"));
		aggVO.getParentVO().setAttributeValue("def60", "N");// nc�ջ�bpm��ʶ
		aggVO.getParentVO().setAttributeValue("def71", "N");// nc֪ͨbpm���ر�ʶ
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
	private Map<String, Object> getFormData(String billCode, AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = getPayBillsInfo(aggVO);
		return formData;
	}
	
	/**
	 * ��ô�����Ϣ
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getPayBillsInfo(AggPayBillVO aggVO)
			throws BusinessException {
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		// ��������
		purchase.put("SerialNumber",
				aggVO.getParentVO().getAttributeValue(PayBillVO.PK_PAYBILL));

		// ����
		purchase.put("Title",
				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF54));

		// �����˺����벿��
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER) != null) {
			if (getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(
					PayBillVO.BILLMAKER)) != null) {
				purchase.put(
						"Applicant",
						getUserInfoByID(
								(String) aggVO.getParentVO().getAttributeValue(
										PayBillVO.BILLMAKER)).get("psnname"));
				purchase.put(
						"ApplicantCode",
						getPerson_name(
								(String) aggVO.getParentVO().getAttributeValue(
										PayBillVO.BILLMAKER)));//���������˺�
				purchase.put("ApplicationDepartment",getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER)).get("deptname"));
				purchase.put("ApplicationDepartmentCode",getUserInfoByID((String) aggVO.getParentVO().getAttributeValue(PayBillVO.BILLMAKER)).get("deptcode"));
			} else {
				purchase.put("Applicant", null);
				purchase.put("ApplicantCode", null);
				purchase.put("ApplicationDepartment",null);
				purchase.put("ApplicationDepartmentCode",null);
			}
		} else {
			purchase.put("Applicant", null);
			purchase.put("ApplicantCode", null);
			purchase.put("ApplicationDepartment",null);
			purchase.put("ApplicationDepartmentCode",null);
		}

//		// ��������
		purchase.put(
				"ApplicationDate",
				aggVO.getParentVO().getAttributeValue(PayBillVO.BILLDATE) == null ? null
						: aggVO.getParentVO()
								.getAttributeValue(PayBillVO.BILLDATE)
								.toString());

//		// ���빫˾
		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG) != null) {
			if (QueryDocInfoUtils.getUtils().getOrgInfo(
					(String) aggVO.getParentVO().getAttributeValue(
							PayBillVO.PK_ORG)) != null) {
				purchase.put(
						"ApplicationCompany",
						QueryDocInfoUtils
								.getUtils()
								.getOrgInfo(
										(String) aggVO.getParentVO()
												.getAttributeValue(
														PayBillVO.PK_ORG))
								.get("name"));
				purchase.put("ApplicationDepartmentCode", QueryDocInfoUtils
						.getUtils()
						.getOrgInfo(
								(String) aggVO.getParentVO()
										.getAttributeValue(
												PayBillVO.PK_ORG))
						.get("code"));
			} else {
				purchase.put("ApplicationCompany", null);
				purchase.put("ApplicationDepartmentCode", null);
			}
		} else {
			purchase.put("ApplicationCompany", null);
			purchase.put("ApplicationDepartmentCode", null);
		}
//
//		// ���λ
//		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_ORG) != null) {
//			if (QueryDocInfoUtils.getUtils().getOrgInfo(
//					(String) aggVO.getParentVO().getAttributeValue(
//							PayBillVO.PK_ORG)) != null) {
//				purchase.put(
//						"PaymentUnit",
//						QueryDocInfoUtils
//								.getUtils()
//								.getOrgInfo(
//										(String) aggVO.getParentVO()
//												.getAttributeValue(
//														PayBillVO.PK_ORG))
//								.get("name"));
//			} else {
//				purchase.put("PaymentUnit", null);
//			}
//		} else {
//			purchase.put("PaymentUnit", null);
//		}
//
//		// �տλ
//		if (aggVO.getParentVO().getAttributeValue(PayBillVO.SUPPLIER) != null) {
//			if (QueryDocInfoUtils.getUtils().getSupplierInfo(
//					(String) aggVO.getParentVO().getAttributeValue(
//							PayBillVO.SUPPLIER)) != null) {
//				purchase.put(
//						"CollectionUnit",
//						QueryDocInfoUtils
//								.getUtils()
//								.getSupplierInfo(
//										(String) aggVO.getParentVO()
//												.getAttributeValue(
//														PayBillVO.SUPPLIER))
//								.get("name"));
//			} else {
//				purchase.put("CollectionUnit", null);
//			}
//		} else {
//			purchase.put("CollectionUnit", null);
//		}
//
//		// �����
//		purchase.put("AmountOfPayment",
//				aggVO.getParentVO().getAttributeValue(PayBillVO.MONEY));
//
//		// �ۼ��Ѹ���
//		purchase.put("CumulativePaid",
//				aggVO.getParentVO().getAttributeValue(PayBillVO.DEF57));
//
//		// ��Ŀ����
//		if (aggVO.getParentVO().getAttributeValue(PayBillVO.DEF32) != null) {
//			if (QueryDocInfoUtils.getUtils().getProjectByPK(
//					(String) aggVO.getParentVO().getAttributeValue(
//							PayBillVO.DEF32)) != null) {
//				purchase.put(
//						"ProjectName",
//						QueryDocInfoUtils
//								.getUtils()
//								.getProjectByPK(
//										(String) aggVO.getParentVO()
//												.getAttributeValue(
//														PayBillVO.DEF32))
//								.get("project_name"));
//			} else {
//				purchase.put("ProjectName", null);
//			}
//		} else {
//			purchase.put("ProjectName", null);
//		}
//
//		// ���㷽ʽ
//		if (aggVO.getParentVO().getAttributeValue(PayBillVO.PK_BALATYPE) != null) {
//			if (QueryDocInfoUtils.getUtils().getBalatypeByPK(
//					(String) aggVO.getParentVO().getAttributeValue(
//							PayBillVO.PK_BALATYPE)) != null) {
//				purchase.put(
//						"SettlementMethod",
//						QueryDocInfoUtils
//								.getUtils()
//								.getBalatypeByPK(
//										(String) aggVO.getParentVO()
//												.getAttributeValue(
//														PayBillVO.PK_BALATYPE))
//								.get("name"));
//			} else {
//				purchase.put("SettlementMethod", null);
//			}
//		} else {
//			purchase.put("SettlementMethod", null);
//		}
//
//		// �ÿ�����
//		purchase.put("UsageContent",
//				aggVO.getParentVO().getAttributeValue(PayBillVO.BIG_TEXT_A));

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
		

//		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();

		PayBillItemVO[] bodyvos = aggVO.getBodyVOs();
		int i = 1;
		for (PayBillItemVO payBillItemVO : bodyvos) {
//			Map<String, Object> bodyPurchase = new HashMap<String, Object>();// ��������

			if(i>1){
				break;
			}
			// ���ݱ��
			purchase.put("DocumentNo", payBillItemVO.getBillno());
			
			//������֯
			if(payBillItemVO.getPk_org()!=null){
				if(QueryDocInfoUtils.getUtils().getOrgInfo(payBillItemVO.getPk_org())!=null){
					purchase.put("AccountCompany", QueryDocInfoUtils.getUtils().getOrgInfo(payBillItemVO.getPk_org()).get("name"));
				}else{
					purchase.put("AccountCompany", null);
				}
			}else{
				purchase.put("AccountCompany", null);
			}
			
			//�跽ԭ�ҽ��
			purchase.put("ClaimAmount", payBillItemVO.getMoney_de()==null?null:payBillItemVO.getMoney_de().setScale(2, UFDouble.ROUND_CEILING).toString());
			
			//Ԥ���Ŀ
			if(payBillItemVO.getDef1()!=null){
				if(QueryDocInfoUtils.getUtils().getBudgetInfo(payBillItemVO.getDef1())!=null){
					purchase.put("ExpenseAccount", QueryDocInfoUtils.getUtils().getBudgetInfo(payBillItemVO.getDef1()).get("name"));
				}else{
					purchase.put("ExpenseAccount", null);
				}
			}else{
				purchase.put("ExpenseAccount", null);
			}
			
//			//�ͻ�
//			if(payBillItemVO.getSupplier()!=null){
//				if(QueryDocInfoUtils.getUtils().getCustInfo(payBillItemVO.getSupplier())!=null){
//					purchase.put("Payee", QueryDocInfoUtils.getUtils().getSupplierInfo(payBillItemVO.getSupplier()).get("name"));
//				}else{
//					purchase.put("Payee", null);
//				}
//			}else{
//				purchase.put("Payee", null);
//			}
			
			//ҵ��Ա
			if(payBillItemVO.getPk_psndoc()!=null){
				if(QueryDocInfoUtils.getUtils().getPsnInfo(payBillItemVO.getPk_psndoc())!=null){
					purchase.put("Payee", QueryDocInfoUtils.getUtils().getPsnInfo(payBillItemVO.getPk_psndoc()).get("name"));
				}else{
					purchase.put("Payee", null);
				}
			}else{
				purchase.put("Payee", null);
			}
			
			if(payBillItemVO.getRecaccount()!=null){
				if(QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount())!=null){
					//�տ������˺�
					purchase.put("BankAccountPayee", QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount()).get("accnum"));
					//�տ����п�����
					purchase.put("BeneficiaryBank", QueryDocInfoUtils.getUtils().getBankDoctInfo(payBillItemVO.getRecaccount()).get("name"));
				}else{
					purchase.put("BankAccountPayee", null);
					purchase.put("BeneficiaryBank", null);
				}
			}else{
				purchase.put("BankAccountPayee", null);
				purchase.put("BeneficiaryBank", null);
			}

			i++;
//			listPurchase.add(bodyPurchase);
		}
		
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("I_yhsxfcksp", purchase);

//		formData.put("C_TaxPayment", listPurchase);

		return formData;
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
				whereCondStr, true, false);
		if (coll.size() > 0) {
			return (AggregatedValueObject) coll.toArray()[0];
		} else {
			return null;
		}
	}
	
}
