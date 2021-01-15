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
 * 按揭协议
 * 
 * @author HUANGDQ
 * @date 2019年6月26日 下午6:32:54
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
	 * 信息转换
	 * 
	 * @param billCode
	 * 
	 * @param aggVO
	 * @return
	 * @throws BusinessException
	 */
	private Map<String, Object> getFormData(String billCode,
			AggMortgageAgreementVO aggVO) throws BusinessException {
		Map<String, Object> formData = new HashMap<String, Object>();// 表单数据
		formData = pushBillsToBpm(aggVO);

		return formData;
	}

	/**
	 * 传参信息
	 * 
	 * @author tjl 20190604
	 * @param aggvo
	 * @param url
	 *            请求地址
	 * @return
	 * @throws BusinessException
	 * @throws JsonProcessingException
	 */
	public Map<String, Object> pushBillsToBpm(AggMortgageAgreementVO aggvo)
			throws BusinessException {
		// TODO add by tjl 2019-06-04
		MortgageAgreementVO parent = aggvo.getParentVO();
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		SendBPMUtils util = new SendBPMUtils();
		// 财务组织
		// Object[] orgmsg = util.getOrgmsg((String) parent
		// .getAttributeValue("pk_org"));
		// purchase.put("org_code", orgmsg[0]);// 组织编码
		// purchase.put("org_name", orgmsg[1]);// 组织名字
		// 申请部门
		String pk_applicationdept = (String) aggvo.getParentVO()
				.getAttributeValue("applicationdept");
		if (pk_applicationdept != null && !"~".equals(pk_applicationdept)) {
			String deptid = getHCMDeptID(pk_applicationdept);
			purchase.put("ApplicationDepartmentCode", deptid);// 申请部门名称
			Map<String, String> deptInfoMap = util
					.getDeptmsg(pk_applicationdept);
			purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// 申请部门

		}
		// 申请人

		String pk_applicant = (String) aggvo.getParentVO().getAttributeValue(
				"proposer");
		// String applicant = util.getPerson_name((String) parent
		// .getAttributeValue("proposer"));
		if (pk_applicant != null && !"~".equals(pk_applicant)) {
			Map<String, String> psnInfoMap = util.getPerson_name(pk_applicant);
			if(psnInfoMap!=null){
				Map<String, String> accountant_account = util
						.getRegionNameByPersonCode(psnInfoMap.get("code"));
				purchase.put("ApplicantCode", accountant_account==null?"":accountant_account.get("userprincipalname"));// 制单人域账号
			}else{
				purchase.put("ApplicantCode", "");
			}
			purchase.put("Applicant", psnInfoMap.get("name"));// 申请人
		}
		// 申请公司
		String applicationorg = (String) aggvo.getParentVO().getAttributeValue(
				"applicationorg");
		if (applicationorg != null && !"~".equals(applicationorg)) {
			Map<String, String> psnInfoMap = util.getOrgmsg(applicationorg);
			purchase.put("ApplicationCompany", psnInfoMap.get("name"));// 申请公司
			purchase.put("ApplicationCompanyCode", psnInfoMap.get("code"));// 申请公司代码
		}

		// 申请日期
		UFDate date = (UFDate) aggvo.getParentVO().getApplicationdate();
		if (date != null) {
			String applicationDate = date.toStdString();
			purchase.put("ApplicationDate", applicationDate);// 申请日期
		}
		// 项目
		String projectMsg = (String) aggvo.getParentVO().getAttributeValue(
				"proname");
		if (projectMsg != null && !"~".equals(projectMsg)) {
			Map<String, String> projectInfoMap = util
					.getProject_name(projectMsg);
			purchase.put("ProjectName", projectInfoMap.get("name"));// 项目名称
		}
		// 合同类别
		String flowMsg = (String) aggvo.getParentVO().getDef2();
		if (flowMsg != null && !"~".equals(flowMsg)) {
			Map<String, String> projectInfoMap = util.getFlowMsg(flowMsg);
			purchase.put("ContractCategory", projectInfoMap.get("name"));// 合同类别名称
		}
		// 合同名称
		String conName = (String) aggvo.getParentVO().getDef3();
		if (StringUtils.isNotBlank(conName)) {
			purchase.put("ContractTitle", conName);// 合同名称
		}
		// 预计签约时间
		if (StringUtils.isNotBlank(aggvo.getParentVO().getDef6())
				&& !"~".equals(aggvo.getParentVO().getDef6())) {
			UFDate signDate = new UFDate(aggvo.getParentVO().getDef6());
			String applicationSignDate = signDate.toStdString();
			purchase.put("ContractTime", applicationSignDate);// 签约时间
		}
		// 合同概述
		if (StringUtils.isNotBlank(aggvo.getParentVO().getBig_text_a())
				&& !"~".equals(aggvo.getParentVO().getBig_text_a())) {
			purchase.put("ContractOverview", aggvo.getParentVO().getBig_text_a());
		}
		// 特殊条款
		if (StringUtils.isNotBlank(aggvo.getParentVO().getBig_text_b())
				&& !"~".equals(aggvo.getParentVO().getBig_text_b())) {
			purchase.put("SpecialClause", aggvo.getParentVO().getBig_text_b());
		}
		// 城市公司
		if (StringUtils.isNotBlank(aggvo.getParentVO().getDef9())
				&& !"~".equals(aggvo.getParentVO().getDef9())) {
			Map<String, String> psnInfoMap = util.getOrgmsg(aggvo.getParentVO()
					.getDef9());
			purchase.put("CityCompany", psnInfoMap.get("name"));
			purchase.put("CityCompanyCode", psnInfoMap.get("code"));
		}
		

		// 合同编号
		// String conCode = (String)
		// aggvo.getParentVO().getAttributeValue("def4");
		// if(StringUtils.isNotBlank(conCode)){
		// purchase.put("ContractCode", conCode);// 项目名称
		// }
		// 收款方
		// Object[] payeeMsg = util.getCustomerMsg((String) parent
		// .getAttributeValue("pk_payee"));
		// String pk_payee = (String)
		// aggvo.getParentVO().getAttributeValue("pk_payee");
		// if (pk_payee != null && !"~".equals(pk_payee)) {
		// Map<String, String> custInfoMap = util.getCustomerMsg(pk_payee);
		// purchase.put("ReceivingUnit", custInfoMap.get("name"));// 收款方名称
		// }
		// 付款方
		// Object[] payerMsg = util.getCustomerMsg((String) parent
		// .getAttributeValue("pk_payer"));
		// purchase.put("PaymentUnit", payerMsg[1]);// 付款方名称
		// String pk_payer = (String)
		// aggvo.getParentVO().getAttributeValue("pk_payer");
		// if (pk_payer != null && !"~".equals(pk_payer)) {
		// Map<String, String> custInfoMap = util.getCustomerMsg(pk_payer);
		// purchase.put("PaymentUnit", custInfoMap.get("name"));// 收款方名称
		// }
		// 集团会计
		// String accountant_name = (String)
		// aggvo.getParentVO().getGroupaccounting();
		// if (accountant_name != null && !"~".equals(accountant_name)) {
		// Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
		// .getPsnInfo(accountant_name);
		// purchase.put("BookKeeper", psnInfoMap.get("code"));// 集团会计代码
		// purchase.put("BookKeeperName", psnInfoMap.get("name"));// 集团会计
		// }
		// String accountant_name = util.getPerson_name((String) parent
		// .getGroupaccounting());
		// purchase.put("Accounting",
		// accountant_name==null?"":accountant_name);// 集团会计名称
		// 出纳
		// String cashier_name = (String) aggvo.getParentVO().getCashier();
		// if (cashier_name != null && !"~".equals(cashier_name)) {
		// Map<String, String> psnInfoMap = QueryDocInfoUtils.getUtils()
		// .getPsnInfo(cashier_name);
		// purchase.put("CaShier", psnInfoMap.get("code"));// 出纳代码
		// purchase.put("CaShierName", psnInfoMap.get("name"));// 出纳
		// }
		// purchase.put("Cashier", cashier_name==null?"":cashier_name);// 出纳名称
		// 用款内容
		// purchase.put("UseContent",
		// parent.getMoneycontent()==null?"":parent.getMoneycontent());
		// 业务信息
		// purchase.put("businessmsg",
		// parent.getBusiinformation()==null?"":parent.getBusiinformation());
		// 累计付款金额
		// purchase.put("AlreadyPaid",parent.getI_totalpayamount()==null?UFDouble.ZERO_DBL.setScale(2,
		// 2).toString():
		// parent.getI_totalpayamount().setScale(2, 2).toString());

		// 合同总金额
		purchase.put("TotalAmount",
				parent.getN_amount() == null ? UFDouble.ZERO_DBL.setScale(2, 2)
						.toString() : parent.getN_amount().setScale(2, 2)
						.toString());
		// 标题
		purchase.put("Title",
				parent.getTitle() == null ? "" : parent.getTitle());
		// 附件
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
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("T_CapitalCenterLoanContract", purchase);
		return formData;
	}

}
