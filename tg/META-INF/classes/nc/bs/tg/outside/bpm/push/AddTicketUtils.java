package nc.bs.tg.outside.bpm.push;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.salebpm.utils.SalePushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.addticket.AddTicket;
import nc.vo.tg.addticket.AggAddTicket;
import nc.vo.tg.addticket.Ticketbody;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

public class AddTicketUtils extends BPMBillUtils{

	static AddTicketUtils utils;

	public static AddTicketUtils getUtils() {
		if (utils == null) {
			utils = new AddTicketUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggAddTicket aggVO = (AggAddTicket) bill;
		
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = null;
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = SalePushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						ISaleBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def19"));
		aggVO.getParentVO().setAttributeValue("def34", null);//����֪ͨbpm��־
		aggVO.getParentVO().setAttributeValue("def19", infoMap.get("taskID"));
		aggVO.getParentVO().setAttributeValue("def20", infoMap.get("OpenUrl"));
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
			AggAddTicket aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = pushBillsToBpm(aggVO);

		return formData;
	}
	
	/**
	 * ������Ϣ
	 * 
	 * @author tjl 20190604
	 * @param aggvo
	 * @param url
	 *            �����ַ
	 * @return
	 * @throws BusinessException
	 * @throws JsonProcessingException
	 */
	public Map<String, Object> pushBillsToBpm(AggAddTicket aggvo)
			throws BusinessException {
		AddTicket parnet = aggvo.getParentVO();
		SendBPMUtils util = new SendBPMUtils();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		
		purchase.put("Title",
				parnet.getDef23());// ����
		
		//����֯
		String pk_org =parnet.getPk_org();
		String easy = null;
		if(StringUtils.isNotBlank(pk_org)){
			Map<String, String> orgsInfoMap = QueryDocInfoUtils.getUtils().getOrgInfo(pk_org);
			if(orgsInfoMap!=null){
				purchase.put("PaymentUnit", orgsInfoMap.get("name"));//���λ
				easy = orgsInfoMap.get("def11");
			}else{
				purchase.put("PaymentUnit",null);
			}
		}else{
			purchase.put("PaymentUnit",null);
		}
		
		if(StringUtils.isNotBlank(easy)&&"��ҵ��ģʽ".equals(easy)){
			purchase.put("ProposalProcess", easy);
		}else{
			purchase.put("ProposalProcess",null);
		}
		
		String pk_applicant = parnet.getDef24();
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
			purchase.put("Applicant", psnInfoMap.get("name"));// ������
		}else{
			purchase.put("ApplicantCode", null);// �Ƶ������˺�
			purchase.put("Applicant", null);// ������
		}
		
		if(parnet.getBilldate()!=null){
			purchase.put(
					"ApplicationDate",parnet.getBilldate().toString());//��������
		}else{
			purchase.put(
					"ApplicationDate",null);//��������
		}
		
		String pk_applicationorg = parnet.getDef26();
		if (pk_applicationorg != null && !"~".equals(pk_applicationorg)) {
			Map<String, String> orgInfoMap = QueryDocInfoUtils.getUtils()
					.getOrgInfo(pk_applicationorg);
			purchase.put("ApplicationCompany", orgInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", orgInfoMap.get("code"));// ���빫˾����
		}else{
			purchase.put("ApplicationCompany", null);// ���빫˾
			purchase.put("ApplicationCompanyCode", null);// ���빫˾����
		}
		
		String pk_applicationdept = parnet.getDef25();
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			Map<String, String> deptInfoMap = QueryDocInfoUtils.getUtils()
					.getDeptInfo(pk_applicationdept);
			String deptid = getHCMDeptID(pk_applicationdept);
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿�Ŵ���
			if(deptInfoMap!=null){
				purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// ���벿��
			}else{
				purchase.put("ApplicationDepartment", null);// ���벿��
			}
		}else{
			purchase.put("ApplicationDepartmentCode", null);// ���벿�Ŵ���
			purchase.put("ApplicationDepartment", null);// ���벿��
		}
		
		
		
		String requestMoneyType = parnet.getDef1();
		if (requestMoneyType != null && !"~".equals(requestMoneyType)) {
			Map<String, String> requestMoneyMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(requestMoneyType);
			purchase.put("RequestMoneyType", requestMoneyMap.get("name"));//�������
		}else{
			purchase.put("RequestMoneyType", null);//�������
		}
		
		String invoiceReceivingPersonn = parnet.getDef28();
		if (invoiceReceivingPersonn != null && !"~".equals(invoiceReceivingPersonn)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(invoiceReceivingPersonn);
			purchase.put("InvoiceReceivingPersonnel", psnInfoMap.get("name"));// �����շ�Ʊ��Ա
		}else{
			purchase.put("InvoiceReceivingPersonnel", null);// �����շ�Ʊ��Ա
		}
		
		purchase.put("Description", parnet.getDef29()==null?"":parnet.getDef29());//����������Ա˵��
		
		purchase.put("ContractNo", parnet.getDef2()==null?"":util.getContract(parnet.getDef2()).get("code"));//�����ͬ���
		
		purchase.put("ContractName", parnet.getDef18()==null?"":parnet.getDef18());//��ͬ����
		
		purchase.put("ActualPaidAmount", parnet.getDef30()==null?"":parnet.getDef30());//ʵ�����
		
		purchase.put("InvoiceTotalAmount", parnet.getDef10()==null?"":parnet.getDef10());//��Ʊ�ܽ��
		
		purchase.put("InvoiceTotalTaxAmount", parnet.getDef11()==null?"":parnet.getDef11());//��Ʊ��˰��
		
		purchase.put("BarCode", parnet.getDef21()==null?"":parnet.getDef21());//Ӱ�����
		
//		purchase.put("Remark", parnet.getBig_text_b()==null?"":parnet.getBig_text_b());//�󵥻�Ʊ�ע
		
		String imageStatus = parnet.getDef14();
		if (imageStatus != null && !"~".equals(imageStatus)) {
			Map<String, String> imageStatusMap = QueryDocInfoUtils.getUtils()
					.getDefdocInfo(imageStatus);
			if(imageStatusMap!=null){
				purchase.put("ImageStatus", imageStatusMap.get("name"));//Ӱ��״̬
			}else{
				purchase.put("ImageStatus", null);//Ӱ��״̬
			}
		}else{
			purchase.put("ImageStatus", null);//Ӱ��״̬
		}
		
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 begin
		String paymentType = "";// ��ʽ
	
		UFDouble invoiceAmount = new UFDouble(0);// ��Ʊ���
		String taxRate = "";// ˰��
		UFDouble withoutTaxAmount = new UFDouble(0);// ����˰���
		UFDouble taxAmount =new UFDouble(0);// ˰��
		UFDouble invTotalAmt=new UFDouble(0);//��Ʊ�ܽ��
		UFDouble invTotalTaxAmt=new UFDouble(0);//��Ʊ��˰��
		
		String sql = "";
		String def16=parnet.getDef16();
		if(isNotBlank(def16)){
			Map<String, String> paymentTypeMap = QueryDocInfoUtils.getUtils()
			          .getDefdocInfo(def16);
	        if(paymentTypeMap!=null){
	          paymentType=paymentTypeMap.get("name");
	        }
		}
		purchase.put("PaymentType", paymentType);
		
		String def10=parnet.getDef10();
		String def37=parnet.getDef37();
		if(isNotBlank(def10)){
			invTotalAmt=new UFDouble(def10);
		}
		if(isNotBlank(def37)){
			invTotalAmt=invTotalAmt.add(new UFDouble(def37));
		}
		purchase.put("InvoiceTotalAmount", invTotalAmt);
		
		String def11=parnet.getDef11();
		String def38=parnet.getDef38();
		if(isNotBlank(def11)){
			invTotalAmt=new UFDouble(def11);
		}
		if(isNotBlank(def38)){
			invTotalAmt=invTotalAmt.add(new UFDouble(def38));
		}
		purchase.put("InvoiceTotalTaxAmount", invTotalAmt);
		
		String def5="";
		String def6="";
		//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
		
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("I_OnlyBuBill", purchase);
		
		List<Map<String, Object>> listPurchase = new ArrayList<Map<String, Object>>();//Ticketbody
		Ticketbody[] bodyvos = (Ticketbody[]) aggvo.getChildrenVO();
		for (Ticketbody ticketbody : bodyvos) {
			Map<String, Object> bodyPurchase = new HashMap<String, Object>();// ��������
			String invoiceType = ticketbody.getDef7();
			if (invoiceType != null && !"~".equals(invoiceType)) {
				Map<String, String> InvoiceTypeMap = QueryDocInfoUtils.getUtils()
						.getDefdocInfo(invoiceType);
				bodyPurchase.put("InvoiceType", InvoiceTypeMap.get("name"));//��Ʊ����
			}else{
				bodyPurchase.put("InvoiceType", null);//��Ʊ����
			}
			
			bodyPurchase.put("TaxRate", ticketbody.getDef8()==null?"":ticketbody.getDef8());//˰��
			
			bodyPurchase.put("InvoiceAmount", ticketbody.getDef5()==null?"":ticketbody.getDef5());//��Ʊ���
			
			if(ticketbody.getDef1() != null && !"~".equals(ticketbody.getDef1())){
				if(QueryDocInfoUtils.getUtils().getPayReqNo(ticketbody.getDef1())!=null){
					bodyPurchase.put("RequestMoneyNo", QueryDocInfoUtils.getUtils().getPayReqNo(ticketbody.getDef1()).get("billno"));//����
				}else{
					bodyPurchase.put("RequestMoneyNo", null);//����
				}
			}else if(ticketbody.getDef9() != null && !"~".equals(ticketbody.getDef9())&&(ticketbody.getDef1() == null || "~".equals(ticketbody.getDef1()))){
				bodyPurchase.put("RequestMoneyNo", ticketbody.getDef9());//���ʷ�����
			}else{
				bodyPurchase.put("RequestMoneyNo", null);//����
			}
			
			bodyPurchase.put("TaxAmount", ticketbody.getDef6()==null?"":ticketbody.getDef6());//˰��
			
			//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 begin
			def5=ticketbody.getDef5();
			def6=ticketbody.getDef6();
			if(isNotBlank(def5)){
				invoiceAmount=new UFDouble(def5);
				withoutTaxAmount=invoiceAmount;
			}
			if(isNotBlank(def6)){
				taxAmount=new UFDouble(def6);
				withoutTaxAmount=invoiceAmount.sub(taxAmount);
			}
			bodyPurchase.put("WithoutTaxAmount", withoutTaxAmount);//˰��
			bodyPurchase.put("InvoiceAmount", invoiceAmount);//˰��
			bodyPurchase.put("TaxAmount", taxAmount);//˰��
			//add by �ƹڻ� SDYC-477����ҵ��-�Խ�BPM ���� 20200831 end
			
			listPurchase.add(bodyPurchase);
		}
		formData.put("C_OnlyBuBill_Detail", listPurchase);
		
		return formData;
	}
}
