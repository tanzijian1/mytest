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
 * 内部资金调拨单
 * @author nctanjingliang
 *
 */
public class SaleInsideAllotPayBillUtils extends SaleBPMBillUtils{

	static SaleInsideAllotPayBillUtils utils;

	public static SaleInsideAllotPayBillUtils getUtils() {
		if (utils == null) {
			utils = new SaleInsideAllotPayBillUtils();
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
	 * 获得传参信息
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
		String File = "";
		List<FileManageVO> fileVOs = QueryDocInfoUtils.getUtils().getFileInfos(
				aggVO.getPrimaryKey());
		if (fileVOs != null && fileVOs.size() > 0) {
			for (int i = 0; i < fileVOs.size(); i++) {
				File += fileVOs.get(i).getDocument_name() + "&"
						+ fileVOs.get(i).getFile_id() + ";";
			}
			purchase.put("File",
					File.substring(0, File.lastIndexOf(";")));
		} else {
			purchase.put("File", null);
		}
		Map<String, Object> formData = new HashMap<String, Object>();// 表头数据
		formData.put("I_InternalCapitalAllot", purchase);
		
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
			
			//付款单位银行账号
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
		
		formData.put("C_InternalCapitalAllot_Detail", listPurchase);
		

		return formData;
	
	}

}
