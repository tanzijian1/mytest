package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class ISaleBPMBillCont {

	public static final String PROCESSNAME_F3_Cxx_011 = "内部资金调拨单";
	public static final String PROCESSNAME_F3_Cxx_012 = "税费缴纳申请单";
	public static final String PROCESSNAME_F3_Cxx_016 = "统借统还-内部放款单";
	public static final String PROCESSNAME_F3_Cxx_017 = "统借统还-内部还款单";
	public static final String PROCESSNAME_F3_Cxx_027 = "银行手续费审批";
	public static final String PROCESSNAME_267X_Cxx_001 = "仅补票审批流程（费控）";
	
	public static final String PROCESSNAMETOBPM_F3_Cxx_011 = "内部资金调拨";
	public static final String PROCESSNAMETOBPM_F3_Cxx_012 = "税费请款审批";
	public static final String PROCESSNAMETOBPM_F3_Cxx_016= "统借统还审批";
	public static final String PROCESSNAMETOBPM_F3_Cxx_027= "银行手续费出款审批";
	public static final String PROCESSNAMETOBPM_267X_Cxx_001= "仅补票审批流程（费控）";//补票工单
	

	private static Map<String, String> billNameMap = null;

	public static final String F3_Cxx_011 = "01"; // 内部资金调拨单
	public static final String F3_Cxx_012 = "02";// 税费缴纳申请单
	public static final String F3_Cxx_016 = "03";// 统借统还-内部放款单
	public static final String F3_Cxx_017 = "04";// 统借统还-内部还款单
	public static final String D267X_Cxx_001 = "14";// 补票工单
    
	public static final String BILL_05 = "05"; // 差旅费报销-地产
	public static final String BILLNAME_05 = "差旅费报销-地产";
	
	public static final String BILL_06 = "06";
	public static final String BILLNAME_06 = "差旅费报销-物业";
	
	
	public static final String BILL_07 = "07"; // 
	public static final String BILLNAME_07 = "差旅费报销-商业";
	
	public static final String BILL_08 = "08"; //
	public static final String BILLNAME_08 = "非合同费用请款-地产";
	
	public static final String BILL_09 = "09"; // 
	public static final String BILLNAME_09 = "非合同费用请款-物业";
	
	public static final String BILL_10 = "10"; 
	public static final String BILLNAME_10 = "非合同费用请款-商业";
	
	public static final String BILL_11 = "11"; 
	public static final String BILLNAME_11 = "合同费用请款-地产";
	
	public static final String BILL_12 = "12"; 
	public static final String BILLNAME_12 = "合同费用请款-地产";
	
	public static final String BILL_13 = "13"; 
	public static final String BILLNAME_13 = "合同费用请款-商业";
	
	public static final String BILL_15 = "15"; 
	public static final String BILLNAME_15 = "资金部银行还款审批";
	
	public static final String BILL_16 = "16"; 
	public static final String BILLNAME_16 = "资金中心偿还贷款利息审批";
	
	public static final String BILL_17 = "17"; 
	public static final String BILLNAME_17 = "资金部财务顾问费";
	
	public static final String BILL_18 = "18"; 
	public static final String BILLNAME_18 = "资金部融资费用审批";
	
	public static final String BILL_19 = "19"; 
	public static final String BILLNAME_19 = "仅补票流程审批（资金类）";
	
	//add by tjl 2020-05-26
	public static final String F3_Cxx_027 = "20";// 银行手续费单
	public static final String BILL_22 = "22";//预算调整单
	public static final String BILLNAME_22_23 = "管理费用预算审批流程";//预算调整单
	public static final String BILL_23 = "23";//预算调剂单
	
	public static final String BILL_24 = "24";//资本市场还款
	public static final String BILLNAME_24 = "资金部银行还款审批（资本业务）";
	
	//end
	
	/**
	 * 目标业务单据中文名对照
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(F3_Cxx_011, PROCESSNAME_F3_Cxx_011);
			billNameMap.put(F3_Cxx_012, PROCESSNAME_F3_Cxx_012);
			billNameMap.put(F3_Cxx_016, PROCESSNAME_F3_Cxx_016);
			billNameMap.put(F3_Cxx_017, PROCESSNAME_F3_Cxx_017);
			billNameMap.put(F3_Cxx_027, PROCESSNAME_F3_Cxx_027);//add by tjl 2020-05-26
			billNameMap.put(D267X_Cxx_001, PROCESSNAME_267X_Cxx_001);
			billNameMap.put(BILL_05, BILLNAME_05);
			billNameMap.put(BILL_06, BILLNAME_06);
			billNameMap.put(BILL_07, BILLNAME_07);
			billNameMap.put(BILL_08, BILLNAME_08);
			billNameMap.put(BILL_09, BILLNAME_09);
			billNameMap.put(BILL_10, BILLNAME_10);
			billNameMap.put(BILL_11, BILLNAME_11);
			billNameMap.put(BILL_12, BILLNAME_12);
			billNameMap.put(BILL_13, BILLNAME_13);
			billNameMap.put(BILL_15, BILLNAME_15);
			billNameMap.put(BILL_16, BILLNAME_16);
			billNameMap.put(BILL_17, BILLNAME_17);
			billNameMap.put(BILL_18, BILLNAME_18);
			billNameMap.put(BILL_19, BILLNAME_19);
			billNameMap.put(BILL_22, BILLNAME_22_23);
			billNameMap.put(BILL_23, BILLNAME_22_23);
			billNameMap.put(BILL_24, BILLNAME_24);
		}
		return billNameMap;
	}
}
