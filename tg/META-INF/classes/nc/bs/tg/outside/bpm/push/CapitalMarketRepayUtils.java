package nc.bs.tg.outside.bpm.push;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.tg.outside.bpm.utils.PushBPMBillUtils;
import nc.bs.tg.util.SendBPMUtils;
import nc.itf.tg.outside.IBPMBillCont;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.pnt.vo.FileManageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.vo.tg.capitalmarketrepay.MarketRepalayVO;

/**
 * 资本市场还本、还息对接bpm
 * @author wenjie
 *
 */
public class CapitalMarketRepayUtils {
	static CapitalMarketRepayUtils utils;
	BaseDAO baseDAO = null;
	public static CapitalMarketRepayUtils getUtils() {
		if (utils == null) {
			utils = new CapitalMarketRepayUtils();
		}
		return utils;
	}
	
	public AbstractBill onPushBillToBPM(String billCode, AbstractBill bill)
			throws Exception {
		AggMarketRepalayVO aggvo =  (AggMarketRepalayVO)bill;
		String userid = InvocationInfoProxy.getInstance().getUserCode();
		Map<String, Object> formData = pushBillsPrincipalToBpm(aggvo);
		Map<String, String> infoMap = PushBPMBillUtils
				.getUtils()
				.pushBillToBpm(userid, formData, IBPMBillCont.getBillNameMap().get(billCode), 
						null, bill.getPrimaryKey(), aggvo.getParentVO().getDef20());
		aggvo.getParentVO().setDef34(null);//清除驳回通知BPM
		aggvo.getParentVO().setAttributeValue("def20", infoMap.get("taskID"));
		aggvo.getParentVO().setAttributeValue("def41", infoMap.get("openurl"));
		return aggvo;
	}
	
	
	/**
	 * 还本
	 */
	private Map<String, Object> pushBillsPrincipalToBpm(AggMarketRepalayVO aggvo) throws BusinessException {
		MarketRepalayVO parentVO = aggvo.getParentVO();
		Map<String, Object> formData = new HashMap<String, Object>();
		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
		String title = parentVO.getName();
		SendBPMUtils util = new SendBPMUtils();
		title = "~".equals(title) || title==null?"":title;
		purchase.put("Title", title);//标题
		
		String pk_applicant = parentVO.getDef1();
		Map<String, String> psnInfoMap = util.getPerson_name(pk_applicant);
		psnInfoMap = psnInfoMap==null?new HashMap<String, String>():psnInfoMap;
		purchase.put("Applicant", psnInfoMap.get("name"));// 申请人
		Map<String, String> accountant_account = util.getRegionNameByPersonCode(psnInfoMap.get("code"));
		accountant_account = accountant_account==null?new HashMap<String, String>():accountant_account;
		purchase.put("ApplicantCode",accountant_account.get("userprincipalname"));// 申请人账号
		
		String applicationorg = parentVO.getDef3();
		Map<String, String> orgInfoMap = util.getOrgmsg(applicationorg);
		orgInfoMap = orgInfoMap==null?new HashMap<String, String>():orgInfoMap;
		purchase.put("ApplicationCompany", orgInfoMap.get("name"));// 申请公司
		purchase.put("ApplicationCompanyCode", orgInfoMap.get("code"));// 申请公司代码
		
		String pk_applicationdept = parentVO.getDef4();
		Map<String, String> deptInfoMap = util.getDeptmsg(pk_applicationdept);
		deptInfoMap = deptInfoMap==null?new HashMap<String, String>():deptInfoMap;
		String deptid = getHCMDeptID(pk_applicationdept);
		purchase.put("ApplicationDepartmentCode", deptid);// 申请部门编码
		purchase.put("ApplicationDepartment", deptInfoMap.get("name"));// 申请部门
		purchase.put("ApplicationDate",parentVO.getCreationtime().toString());//申请时间
		
		String pk_project = parentVO.getDef6();
		Map<String, String> projectMap = util.getProject_name(pk_project);
		projectMap = projectMap==null?new HashMap<String, String>():projectMap;
		purchase.put("ProjectName", projectMap.get("name")==null?"时代控股":projectMap.get("name"));//项目名称
		
		String pk_payee = parentVO.getDef7();
		purchase.put("ReceivingUnit", util.getPayeeName(pk_payee));//收款单位
		
		String pk_bankaccsub = parentVO.getDef8();
		Map<String, String> bankMap = util.getBankInfo(pk_bankaccsub);
		bankMap = bankMap==null?new HashMap<String, String>():bankMap;
		purchase.put("ReceivingBankAccount", bankMap.get("accnum"));//收款银行账户
		purchase.put("ReceivingBankAccountName", bankMap.get("accname"));//收款银行账户户名
		purchase.put("ReceivingBankName", bankMap.get("name"));//收款银行账户开户行
		
		String pk_defdoc = parentVO.getDef35();
		Map<String, String> defMap = util.getFlowMsg(pk_defdoc);
		defMap = defMap==null?new HashMap<String, String>():defMap;
		purchase.put("PaymentType", defMap.get("name"));//请款方式
		
		String pk_payer = parentVO.getDef9();
		purchase.put("PaymentUnit", util.getPayerName(pk_payer));//付款单位
		
		UFDouble sumMoney = parentVO.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef11());
		purchase.put("TotalAmount", sumMoney);//合同总金额
		
		String pk_singleissue = parentVO.getDef5();
		Map<String, String> info = util.getContractInfo(pk_singleissue);
		purchase.put("ContractName", info.get("name"));//合同名称
		purchase.put("ContractCode", info.get("billno"));//合同编码
		purchase.put("FinancingType", info.get("type"));//融资类型 取业务类型
		purchase.put("UseContent", parentVO.getBig_text_b());//用款内容
		
		purchase.put("RelatedProcesses", IBPMBillCont.PROCESSNAME_SD08);//相关流程
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
		
		UFDouble m = parentVO.getDef13()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef13());
		purchase.put("AlreadyPaid", m);//累计已付本金
		
		purchase.put("RequestAmount", 
				parentVO.getDef23()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef23()));//本次请款本金
		
		purchase.put("AlreadyRates", 
				parentVO.getDef14()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef14()));//累计已付利息
		purchase.put("RequestRates", 
				parentVO.getDef24()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef24()));//本次请款利息
		
		purchase.put("Remarks", parentVO.getDef40());//备注
		purchase.put("BarCode", parentVO.getDef21());//影像编码
		
		purchase.put("ThisRequestAmount", 
				parentVO.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef12()));//本次请款金额 def12
		purchase.put("SecondaryIncome", 
				parentVO.getDef46()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef46()));//次级收益 def46
		purchase.put("RelatedCosts", 
				parentVO.getDef45()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef45()));//相关费用 def45
		formData.put("T_BankRepayment", purchase);
		
		Map<String, Object> purchasebody = new HashMap<String, Object>();// 表体数据
		
		String def33 = parentVO.getDef33();
		Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils().getDefdocInfo(def33);
		invoiceTypeMap = invoiceTypeMap==null?new HashMap<String, String>():invoiceTypeMap;
		purchasebody.put("InvoiceType", invoiceTypeMap.get("name"));//发票类型
		
		UFDouble InvoiceAmount = parentVO.getDef18()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef18());
		purchasebody.put("InvoiceAmount", InvoiceAmount);//发票金额
		UFDouble TaxAmount = parentVO.getDef26()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef26());
		purchasebody.put("TaxAmount", TaxAmount);//税额
		purchasebody.put("WithoutTaxAmount", InvoiceAmount.sub(TaxAmount));//不含税金额
		purchasebody.put("TaxRate", parentVO.getDef30());//税率
		
		formData.put("C_BankRepaymentInvoice_D", purchasebody);
		return formData;
	}
	
	/**
	 * 还息
	 * @param aggvo
	 * @return
	 * @throws BusinessException 
	 */
//	private Map<String, Object> pushBillsInterestToBpm(AggMarketRepalayVO aggvo) throws BusinessException{
//		MarketRepalayVO parentVO = aggvo.getParentVO();
//		Map<String, Object> formData = new HashMap<String, Object>();
//		Map<String, Object> purchase = new HashMap<String, Object>();// 表头数据
//		
//		String title = parentVO.getName();
//		title = "~".equals(title) || title==null?"":title;
//		purchase.put("Title", title);//标题
//		String pk_billmarker = parentVO.getBillmaker();
//		SendBPMUtils util = new SendBPMUtils();
//		Map<String, String> personMap = util.getUserInfoByID(pk_billmarker);
//		personMap = personMap==null?new HashMap<String, String>():personMap;
//		purchase.put("Applicant", personMap.get("psnname"));// 申请人
//		purchase.put("ApplicantCode",personMap.get("psncode"));//申请人账号
//		purchase.put("ApplicationCompany", personMap.get("compname"));// 申请公司
//		purchase.put("ApplicationCompanyCode", personMap.get("compcode"));// 申请公司代码
//		purchase.put("ApplicationDepartment", personMap.get("deptname"));// 申请部门
//		purchase.put("ApplicationDepartmentCode", personMap.get("deptcode"));// 申请部门代码
//		purchase.put("ApplicationDate",parentVO.getCreationtime().toString());//申请时间
//		
//		String pk_project = parentVO.getDef6();
//		Map<String, String> projectMap = util.getProject_name(pk_project);
//		projectMap = projectMap==null?new HashMap<String, String>():projectMap;
//		purchase.put("ProjectName", projectMap.get("name")==null?"时代控股":projectMap.get("name"));//项目名称
//		
//		String pk_payee = parentVO.getDef7();
//		purchase.put("ReceivingUnit", util.getPayeeName(pk_payee));//收款单位
//		
//		String pk_payer = parentVO.getDef9();
//		purchase.put("PaymentUnit", util.getPayerName(pk_payer));//付款单位
//		
//		String pk_bankaccsub = parentVO.getDef8();
//		Map<String, String> bankMap = util.getBankInfo(pk_bankaccsub);
//		bankMap = bankMap==null?new HashMap<String, String>():bankMap;
//		purchase.put("ReceivingBankAccount", bankMap.get("accnum"));//收款银行账户
//		purchase.put("ReceivingBankAccountName", bankMap.get("accname"));//收款银行账户户名
//		purchase.put("ReceivingBankName", bankMap.get("name"));//收款银行账户开户行
//		
//		purchase.put("UseContent", parentVO.getBig_text_b());//用款内容
//		
//		String pk_singleissue = parentVO.getDef5();
//		Map<String, String> info = util.getContractInfo(pk_singleissue);
//		purchase.put("ContractName", info.get("name"));//合同名称
//		purchase.put("ContractCode", info.get("billno"));//合同编码
//		purchase.put("FinancingType", info.get("type"));//融资类型 取业务类型
//		
//		UFDouble sumMoney = parentVO.getDef11()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef11());
//		purchase.put("TotalAmount", sumMoney);//合同总金额
//		
//		purchase.put("CumulativePaidInterest", 
//				parentVO.getDef14()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef14()));//累计已付利息
//		
//		purchase.put("AmountRequestedRates", 
//				parentVO.getDef24()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef24()));//本次请款利息
//		
//		String pk_defdoc = parentVO.getDef35();
//		Map<String, String> defMap = util.getFlowMsg(pk_defdoc);
//		defMap = defMap==null?new HashMap<String, String>():defMap;
//		purchase.put("PaymentType", defMap.get("name"));//请款方式
//		
//		UFDouble InvoiceAmount = parentVO.getDef18()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef18());
//		purchase.put("RequestInvoiceAmount", InvoiceAmount);//请款发票金额
//		
//		// 附件
//		String Attachments = "";
//		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
//				aggvo.getPrimaryKey());
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
//		purchase.put("BarCode", parentVO.getDef21());//影像编码
//		purchase.put("ThisRequestAmount", 
//				parentVO.getDef12()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef12()));//本次请款金额 def12
//		purchase.put("SecondaryIncome", 
//				parentVO.getDef46()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef46()));//次级收益 def46
//		purchase.put("RelatedCosts", 
//				parentVO.getDef45()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef45()));//相关费用 def45
//		Map<String, Object> purchasebody = new HashMap<String, Object>();// 表体数据
//		
//		String def33 = parentVO.getDef33();
//		Map<String, String> invoiceTypeMap = QueryDocInfoUtils.getUtils().getDefdocInfo(def33);
//		invoiceTypeMap = invoiceTypeMap==null?new HashMap<String, String>():invoiceTypeMap;
//		purchasebody.put("InvoiceType", invoiceTypeMap.get("name"));//发票类型
//		
//		purchasebody.put("InvoiceAmount", InvoiceAmount);//发票金额
//		UFDouble TaxAmount = parentVO.getDef26()==null?UFDouble.ZERO_DBL:new UFDouble(parentVO.getDef26());
//		purchasebody.put("TaxAmount", TaxAmount);//税额
//		purchasebody.put("WithoutTaxAmount", InvoiceAmount.sub(TaxAmount));//不含税金额
//		purchasebody.put("TaxRate", parentVO.getDef30());//税率
//		
//		formData.put("T_RepaymentInterest", purchase);
//		formData.put("C_RepaymentInterestInvoice_D", purchasebody);
//		
//		return formData;
//	}
	
	/**
	 * 数据库持久化
	 * 
	 * @return
	 */
	public BaseDAO getBaseDAO() {
		if (baseDAO == null) {
			baseDAO = new BaseDAO();
		}
		return baseDAO;
	}
	/**
	 * 通过NC部门主键从中间表读取HCM部门主键
	 * 
	 * @param code
	 * @return
	 * @throws BusinessException
	 */
	public String getHCMDeptID(String id) throws BusinessException {
		String sql = "select t.id from organizationitem t  inner join org_dept  on org_dept.code = t.seqnum where org_dept.pk_dept ='"
				+ id + "'";
		String hcmid = (String) getBaseDAO().executeQuery(sql,
				new ColumnProcessor());
		return hcmid;
	}
}