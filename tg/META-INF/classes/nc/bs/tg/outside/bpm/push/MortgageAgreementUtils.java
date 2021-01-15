package nc.bs.tg.outside.bpm.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.financingexpense.FinancexpenseVO;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO;

/**
 * ����Э��
 * 
 * @author HUANGDQ
 * @date 2019��6��26�� ����6:32:54
 */
public class MortgageAgreementUtils extends BPMBillUtils {
	static MortgageAgreementUtils utils;

	public static MortgageAgreementUtils getUtils() {
		if (utils == null) {
			utils = new MortgageAgreementUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggMortgageAgreementVO aggVO = (AggMortgageAgreementVO) bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("applicationdept"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = PushBPMBillUtils.getUtils()
				.pushBillToBpm(userid, formData,
						IBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("def19"));
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
			AggMortgageAgreementVO aggVO) throws BusinessException {
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
	public Map<String, Object> pushBillsToBpm(AggMortgageAgreementVO aggvo)
			throws BusinessException {
		// TODO add by tjl 2019-06-04
		MortgageAgreementVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		SendBPMUtils util = new SendBPMUtils();
		// ������֯
		// Object[] orgmsg = util.getOrgmsg((String) parent
		// .getAttributeValue("pk_org"));
		// purchase.put("org_code", orgmsg[0]);// ��֯����
		// purchase.put("org_name", orgmsg[1]);// ��֯����
		// ���벿��
		String pk_applicationdept = (String) aggvo.getParentVO()
				.getAttributeValue("applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			String deptid = getHCMDeptID(pk_applicationdept);
			purchase.put("ApplicationDepartmentCode", deptid);// ���벿������
			Map<String, String> deptInfoMap = util
					.getDeptmsg(pk_applicationdept);
			purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// ���벿��

		}
		// ������

		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"proposer");
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
			purchase.put("Applicant", psnInfoMap.get("name"));// ������
		}
		// ���빫˾
		String applicationorg = (String) aggvo.getParentVO().getAttributeValue(
				"applicationorg");
		if (applicationorg != null && !"~".equals(applicationorg)) {
			Map<String, String> psnInfoMap = util.getOrgmsg(applicationorg);
			purchase.put("ApplicationCompany", psnInfoMap.get("name"));// ���빫˾
			purchase.put("ApplicationCompanyCode", psnInfoMap.get("code"));// ���빫˾����
		}

		// ��������
		UFDate date = (UFDate) aggvo.getParentVO().getApplicationdate();
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
			purchase.put("ProjectName", projectInfoMap.get("name"));// ��Ŀ����
		}
		// ��ͬ���
		String flowMsg = (String) aggvo.getParentVO().getDef2();
		if (flowMsg != null && !"~".equals(flowMsg)) {
			Map<String, String> projectInfoMap = util.getFlowMsg(flowMsg);
			purchase.put("ContractCategory", projectInfoMap.get("name"));// ��ͬ�������
		}
		// ��ͬ����
		String conName = (String) aggvo.getParentVO().getDef3();
		if (StringUtils.isNotBlank(conName)) {
			purchase.put("ContractTitle", conName);// ��ͬ����
		}
		// Ԥ��ǩԼʱ��
		if (StringUtils.isNotBlank(aggvo.getParentVO().getDef6())
				&& !"~".equals(aggvo.getParentVO().getDef6())) {
			UFDate signDate = new UFDate(aggvo.getParentVO().getDef6());
			String applicationSignDate = signDate.toStdString();
			purchase.put("ContractTime", applicationSignDate);// ǩԼʱ��
		}
		// ��ͬ����
		if (StringUtils.isNotBlank(aggvo.getParentVO().getBig_text_a())
				&& !"~".equals(aggvo.getParentVO().getBig_text_a())) {
			purchase.put("ContractOverview", aggvo.getParentVO().getBig_text_a());
		}
		// ��������
		if (StringUtils.isNotBlank(aggvo.getParentVO().getBig_text_b())
				&& !"~".equals(aggvo.getParentVO().getBig_text_b())) {
			purchase.put("SpecialClause", aggvo.getParentVO().getBig_text_b());
		}
		// ���й�˾
		if (StringUtils.isNotBlank(aggvo.getParentVO().getDef9())
				&& !"~".equals(aggvo.getParentVO().getDef9())) {
			Map<String, String> psnInfoMap = util.getOrgmsg(aggvo.getParentVO()
					.getDef9());
			purchase.put("CityCompany", psnInfoMap.get("name"));
			purchase.put("CityCompanyCode", psnInfoMap.get("code"));
		}
		

		// ��ͬ���
		// String conCode = (String)
		// aggvo.getParentVO().getAttributeValue("def4");
		// if(StringUtils.isNotBlank(conCode)){
		// purchase.put("ContractCode", conCode);// ��Ŀ����
		// }
		// �տ
		// Object[] payeeMsg = util.getCustomerMsg((String) parent
		// .getAttributeValue("pk_payee"));
		// String pk_payee = (String)
		// aggvo.getParentVO().getAttributeValue("pk_payee");
		// if (pk_payee != null && !"~".equals(pk_payee)) {
		// Map<String, String> custInfoMap = util.getCustomerMsg(pk_payee);
		// purchase.put("ReceivingUnit", custInfoMap.get("name"));// �տ����
		// }
		// ���
		// Object[] payerMsg = util.getCustomerMsg((String) parent
		// .getAttributeValue("pk_payer"));
		// purchase.put("PaymentUnit", payerMsg[1]);// �������
		// String pk_payer = (String)
		// aggvo.getParentVO().getAttributeValue("pk_payer");
		// if (pk_payer != null && !"~".equals(pk_payer)) {
		// Map<String, String> custInfoMap = util.getCustomerMsg(pk_payer);
		// purchase.put("PaymentUnit", custInfoMap.get("name"));// �տ����
		// }
		// ���Ż��
		// String accountant_name = (String)
		// aggvo.getParentVO().getGroupaccounting();
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
		// String cashier_name = (String) aggvo.getParentVO().getCashier();
		// if (cashier_name != null && !"~".equals(cashier_name)) {
		// Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
		// .getPsnInfo(cashier_name);
		// purchase.put("CaShier", psnInfoMap.get("code"));// ���ɴ���
		// purchase.put("CaShierName", psnInfoMap.get("name"));// ����
		// }
		// purchase.put("Cashier", cashier_name==null?"":cashier_name);// ��������
		// �ÿ�����
		// purchase.put("UseContent",
		// parent.getMoneycontent()==null?"":parent.getMoneycontent());
		// ҵ����Ϣ
		// purchase.put("businessmsg",
		// parent.getBusiinformation()==null?"":parent.getBusiinformation());
		// �ۼƸ�����
		// purchase.put("AlreadyPaid",parent.getI_totalpayamount()==null?UFDouble.ZERO_DBL.setScale(2,
		// 2).toString():
		// parent.getI_totalpayamount().setScale(2, 2).toString());

		// ��ͬ�ܽ��
		purchase.put("TotalAmount",
				parent.getN_amount() == null ? UFDouble.ZERO_DBL.setScale(2, 2)
						.toString() : parent.getN_amount().setScale(2, 2)
						.toString());
		// ����
		purchase.put("Title",
				parent.getTitle() == null ? "" : parent.getTitle());
		// ����
		String Attachments = "";
		List<FileManageVO> fileVOs = util.getFileInfos(aggvo.getPrimaryKey());
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
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_CapitalCenterLoanContract", purchase);
		return formData;
	}

}
