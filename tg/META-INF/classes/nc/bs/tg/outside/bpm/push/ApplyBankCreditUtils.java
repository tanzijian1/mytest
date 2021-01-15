package nc.bs.tg.outside.bpm.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.BPMBillUtils;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.pnt.vo.FileManageVO;
import nc.vo.cdm.applybankcredit.AggApplyBankCreditVO;
import nc.vo.cdm.applybankcredit.ApplyBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.AppContext;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * ���д�������(�����ͬ���)
 * 
 * @author wenhao
 * @date 2019��6��26�� ����6:33:32
 */
public class ApplyBankCreditUtils extends BPMBillUtils {
	static ApplyBankCreditUtils utils;

	public static ApplyBankCreditUtils getUtils() {
		if (utils == null) {
			utils = new ApplyBankCreditUtils();
		}
		return utils;
	}

	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggApplyBankCreditVO aggVO = (AggApplyBankCreditVO) bill;

		// String userid = AppContext.getInstance().getPkUser();
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		String deptid = getHCMDeptID((String) aggVO.getParentVO()
				.getAttributeValue("applicationdept"));
		Map<String, Object> formData = getFormData(billCode, aggVO);
		Map<String, String> infoMap = PushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData,
						IBPMBillCont.getBillNameMap().get(billCode), deptid,
						bill.getPrimaryKey(),
						(String) bill.getParentVO().getAttributeValue("vdef19"));
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
			AggApplyBankCreditVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// ������
		formData = pushBillsToBpm(aggVO);

		return formData;
	}

	/**
	 * ������Ϣ
	 * 
	 * @author wenhao 20190704
	 * @param aggvo
	 * @param url
	 *            �����ַ
	 * @return
	 * @throws BusinessException
	 * @throws JsonProcessingException
	 */
	public Map<String, Object> pushBillsToBpm(AggApplyBankCreditVO aggvo)
			throws BusinessException {
		ApplyBankCreditVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// ��ͷ����
		SendBPMUtils util = new SendBPMUtils();
		// ������֯
		/*
		 * Map<String, String> orgmsg = util.getOrgmsg((String)
		 * parent.getPk_org()); purchase.put("org_code", orgmsg.get(""));// ��֯����
		 * purchase.put("org_name", orgmsg[1]);// ��֯����
		 */
		// ����
		purchase.put("Title", parent.getAttributeValue("title") == null ? ""
				: parent.getAttributeValue("title"));
		// ������
		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"proposer");
		purchase.put("Applicant", pk_applicant);// ����������
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// �Ƶ������˺�
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap.get("name"));// ������
		}
		// ��������
		purchase.put("ApplicationDate",
				parent.getAttributeValue("applydate") == null ? "" : parent
						.getAttributeValue("applydate").toString());

		// ���빫˾
		Map<String, String> applicationorg = util.getOrgmsg((String) parent
				.getAttributeValue("applicationorg"));
		// ���빫˾����
		purchase.put("ApplicationCompanyCode", applicationorg.get("code"));// ���빫˾����
		purchase.put("ApplicationCompany", applicationorg.get("name"));// ���빫˾����

		// ���벿��
		Map<String, String> deptmsg = util.getDeptmsg((String) parent
				.getAttributeValue("applicationdept"));
		String deptid = getHCMDeptID((String) parent
				.getAttributeValue("applicationdept"));
		purchase.put("ApplicationDepartmentCode", deptid);// ���벿�ű���
		purchase.put("ApplicationDepartment", deptmsg.get("name"));// ���벿������

		// ��Ŀ
		Map<String, String> projectMsg = util.getProject_name((String) parent
				.getAttributeValue("proname"));
		purchase.put("ProjectName", projectMsg==null?"":projectMsg.get("name"));// ��Ŀ����
		// ���й�˾������Ŀ��������
		Map<String, String> CityCompany = util.getOrgmsg(((String) parent
				.getAttributeValue("citycompany")));
		purchase.put("CityCompany", CityCompany==null?"":CityCompany.get("name"));// ��Ŀ����

		// ��ͬ��ͬ�ܽ�� ��Ŀ���� n_amount // contractamount
		purchase.put(
				"TotalAmount",
				parent.getAttributeValue("n_amount") == null ? UFDouble.ZERO_DBL
						.setScale(2, 2).toString() : ((UFDouble) parent
						.getAttributeValue("n_amount")).setScale(2, 2)
						.toString());
		// �ƹ˷Ѻ�ͬ��� ��Ŀ���� i_totalpayamount
		purchase.put(
				"AlreadyPaid",
				parent.getAttributeValue("i_totalpayamount") == null ? UFDouble.ZERO_DBL
						.setScale(2, 2).toString() : ((UFDouble) parent
						.getAttributeValue("i_totalpayamount")).setScale(2, 2)
						.toString());
		/*
		 * // �����
		 * purchase.put("RequestAmount",parent.getI_applyamount()==null?UFDouble
		 * .ZERO_DBL.setScale(2, 2).toString():
		 * parent.getI_applyamount().setScale(2, 2).toString());
		 */
		// Ԥ��ǩԼʱ��
		purchase.put("ContractTime",
				parent.getAttributeValue("expectsigndate") == null ? ""
						: parent.getAttributeValue("expectsigndate").toString());
		// ��ͬ����
		purchase.put("ContractOverview", parent
				.getAttributeValue("contractsummary") == null ? "" : parent
				.getAttributeValue("contractsummary").toString());
		// ��ͬ���[ContractCategory]
		/*
		 * purchase.put("ContractCategory",parent.getAttributeValue("contracttype"
		 * )==null?"": parent.getAttributeValue("contracttype").toString());
		 */
		Map<String, String> contracttype = util.getFlowMsg(((String) parent
				.getAttributeValue("contracttype")));
		purchase.put("ContractCategory", contracttype.get("name"));
		// ��ͬ���� ContractTitle
		purchase.put("ContractTitle",
				parent.getAttributeValue("contractname") == null ? "" : parent
						.getAttributeValue("contractname").toString());
		// ��������
		purchase.put("SpecialClause",
				parent.getAttributeValue("specialterms") == null ? "" : parent
						.getAttributeValue("specialterms").toString());
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
		Map<String, Object> formData = new HashMap<String, Object>();// ��ͷ����
		formData.put("T_CapitalCenterLoanContract", purchase);
		return formData;
	}

}
