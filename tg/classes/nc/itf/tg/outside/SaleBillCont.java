package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class SaleBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String BILLTYPE_FN11 = "FN11"; // 换票单

	public static final String BILLNAME_FN11 = "换票单"; // 换票单

	public static final String BILLTYPE_FN13 = "FN13"; // 分摊工单

	public static final String BILLNAME_FN13 = "分摊工单"; // 分摊工单

	public static final String BILLTYPE_FN15 = "FN15"; // 筹转定转账单

	public static final String BILLNAME_FN15 = "筹转定转账单"; // 筹转定转账单

	public static final String BILLTYPE_FN16 = "FN16"; // 挞定工单

	public static final String BILLNAME_FN16 = "挞定工单"; // 挞定工单

	public static final String BILLTYPE_FN17 = "FN17"; // 挞定激活工单

	public static final String BILLNAME_FN17 = "挞定激活工单"; // 挞定激活工单

	public static final String BILLTYPE_FN18 = "FN18"; // 更名换票单

	public static final String BILLNAME_FN18 = "换收据单"; // 更名换票单
	
	public static final String BILLTYPE_FN19 = "FN19"; // 换房转账单

	public static final String BILLNAME_FN19 = "换房转账单"; // 换房转账单

	public static final String BILLTYPE_FN3 = "FN3"; // 供应商付款单
	
	public static final String BILLNAME_FN3 = "供应商付款单"; // 供应商付款单

	public static final String BILLTYPE_F3 = "F3"; // 供应商付款单
	
	public static final String BILLNAME_F3 = "供应商付款单"; // 供应商付款单

	public static final String BILLTYPE_36S4 = "36S4"; // 划账结算

	public static final String BILLNAME_36S4 = "划账结算"; // 划账结算

	public static final String BILLTYPE_F7 = "F7"; // 收款单

	public static final String BILLNAME_F7 = "收款单"; // 收款单
	
	public static final String BILLTYPE_FN20 = "FN20"; // 退款单-->客户基本信息

	public static final String BILLNAME_FN20 = "客户基本信息"; // 退款单-->客户基本信息
	
	public static final String BILLtype_4D10="4D10";//项目-集团
	
	public static final String BillNAME_4D10="项目-集团档案";//项目-集团
	
	public static final String BILLtype_F3F7="F3F7";//转备案收付款
	
	public static final String BillNAME_F3F7="转备案收付款";//转备案收付款
	/**
	 * 目标业务单据中文名对照 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILLTYPE_FN11, BILLNAME_FN11);
			billNameMap.put(BILLTYPE_FN13, BILLNAME_FN13);
			billNameMap.put(BILLTYPE_FN15, BILLNAME_FN15);
			billNameMap.put(BILLTYPE_FN16, BILLNAME_FN16);
			billNameMap.put(BILLTYPE_FN17, BILLNAME_FN17);
			billNameMap.put(BILLTYPE_FN18, BILLNAME_FN18);
			billNameMap.put(BILLTYPE_FN19, BILLNAME_FN19);
			billNameMap.put(BILLTYPE_36S4, BILLNAME_36S4);
			billNameMap.put(BILLTYPE_FN3, BILLNAME_FN3);
			billNameMap.put(BILLTYPE_F3, BILLNAME_F3);
			billNameMap.put(BILLTYPE_F7, BILLNAME_F7);
			billNameMap.put(BILLTYPE_FN20, BILLNAME_FN20);
			billNameMap.put(BILLtype_4D10, BillNAME_4D10);
			billNameMap.put(BILLtype_F3F7, BillNAME_F3F7);
		}
		return billNameMap;
	}
}
