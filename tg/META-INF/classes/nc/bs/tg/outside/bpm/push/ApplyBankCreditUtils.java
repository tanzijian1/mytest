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
 * 银行贷款申请(贷款合同审核)
 * 
 * @author wenhao
 * @date 2019年6月26日 下午6:33:32
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
	 * 信息转换
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggApplyBankCreditVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		formData = pushBillsToBpm(aggVO);

		return formData;
	}

	/**
	 * 传参信息
	 * 
	 * @author wenhao 20190704
	 * @param aggvo
	 * @param url
	 *            请求地址
	 * @return
	 * @throws BusinessException
	 * @throws JsonProcessingException
	 */
	public Map<String, Object> pushBillsToBpm(AggApplyBankCreditVO aggvo)
			throws BusinessException {
		ApplyBankCreditVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		SendBPMUtils util = new SendBPMUtils();
		// 财务组织
		/*
		 * Map<String, String> orgmsg = util.getOrgmsg((String)
		 * parent.getPk_org()); purchase.put("org_code", orgmsg.get(""));// 组织编码
		 * purchase.put("org_name", orgmsg[1]);// 组织名字
		 */
		// 标题
		purchase.put("Title", parent.getAttributeValue("title") == null ? ""
				: parent.getAttributeValue("title"));
		// 申请人
		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"proposer");
		purchase.put("Applicant", pk_applicant);// 申请人名称
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
					.getPsnInfo(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = QueryDocInfoUtils.getUtils()
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// 制单人域账号
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap.get("name"));// 申请人
		}
		// 申请日期
		purchase.put("ApplicationDate",
				parent.getAttributeValue("applydate") == null ? "" : parent
						.getAttributeValue("applydate").toString());

		// 申请公司
		Map<String, String> applicationorg = util.getOrgmsg((String) parent
				.getAttributeValue("applicationorg"));
		// 申请公司代码
		purchase.put("ApplicationCompanyCode", applicationorg.get("code"));// 申请公司代码
		purchase.put("ApplicationCompany", applicationorg.get("name"));// 申请公司名称

		// 申请部门
		Map<String, String> deptmsg = util.getDeptmsg((String) parent
				.getAttributeValue("applicationdept"));
		String deptid = getHCMDeptID((String) parent
				.getAttributeValue("applicationdept"));
		purchase.put("ApplicationDepartmentCode", deptid);// 申请部门编码
		purchase.put("ApplicationDepartment", deptmsg.get("name"));// 申请部门名称

		// 项目
		Map<String, String> projectMsg = util.getProject_name((String) parent
				.getAttributeValue("proname"));
		purchase.put("ProjectName", projectMsg==null?"":projectMsg.get("name"));// 项目名称
		// 城市公司参照项目所属区域
		Map<String, String> CityCompany = util.getOrgmsg(((String) parent
				.getAttributeValue("citycompany")));
		purchase.put("CityCompany", CityCompany==null?"":CityCompany.get("name"));// 项目名称

		// 合同合同总金额 项目主键 n_amount // contractamount
		purchase.put(
				"TotalAmount",
				parent.getAttributeValue("n_amount") == null ? UFDouble.ZERO_DBL
						.setScale(2, 2).toString() : ((UFDouble) parent
						.getAttributeValue("n_amount")).setScale(2, 2)
						.toString());
		// 财顾费合同金额 项目主键 i_totalpayamount
		purchase.put(
				"AlreadyPaid",
				parent.getAttributeValue("i_totalpayamount") == null ? UFDouble.ZERO_DBL
						.setScale(2, 2).toString() : ((UFDouble) parent
						.getAttributeValue("i_totalpayamount")).setScale(2, 2)
						.toString());
		/*
		 * // 请款金额
		 * purchase.put("RequestAmount",parent.getI_applyamount()==null?UFDouble
		 * .ZERO_DBL.setScale(2, 2).toString():
		 * parent.getI_applyamount().setScale(2, 2).toString());
		 */
		// 预计签约时间
		purchase.put("ContractTime",
				parent.getAttributeValue("expectsigndate") == null ? ""
						: parent.getAttributeValue("expectsigndate").toString());
		// 合同概述
		purchase.put("ContractOverview", parent
				.getAttributeValue("contractsummary") == null ? "" : parent
				.getAttributeValue("contractsummary").toString());
		// 合同类别[ContractCategory]
		/*
		 * purchase.put("ContractCategory",parent.getAttributeValue("contracttype"
		 * )==null?"": parent.getAttributeValue("contracttype").toString());
		 */
		Map<String, String> contracttype = util.getFlowMsg(((String) parent
				.getAttributeValue("contracttype")));
		purchase.put("ContractCategory", contracttype.get("name"));
		// 合同名称 ContractTitle
		purchase.put("ContractTitle",
				parent.getAttributeValue("contractname") == null ? "" : parent
						.getAttributeValue("contractname").toString());
		// 特殊条款
		purchase.put("SpecialClause",
				parent.getAttributeValue("specialterms") == null ? "" : parent
						.getAttributeValue("specialterms").toString());
		// 附件
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
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("T_CapitalCenterLoanContract", purchase);
		return formData;
	}

}
