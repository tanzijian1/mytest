package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class IBPMBillCont {

	public static final String PROCESSNAME_RZ06_01 = "资金部财务顾问费";
	public static final String PROCESSNAME_RZ06_02 = "资金部融资费用审批";
	public static final String PROCESSNAME_36FF_01 = "资金部银行还款审批";
	public static final String PROCESSNAME_36FF_02 = "资金中心偿还贷款利息审批";
	public static final String PROCESSNAME_SD08 = "资金部银行还款审批（资本业务）";
	public static final String PROCESSNAME_36FA = "资金中心贷款合同审批（非理财类）";
	public static final String PROCESSNAME_RZ04 = "按揭协议";

	private static Map<String, String> billNameMap = null;

	public static final String BILL_RZ06_01 = "01"; // 资金部财务顾问费
	public static final String BILL_RZ06_02 = "02";// 资金部融资费用审批
	public static final String BILL_36FF_01 = "03";// 资金部银行还款审批
	public static final String BILL_36FF_02 = "04";// "资金中心偿还贷款利息审批
	public static final String BILL_36FA = "05";// 资金中心贷款合同审批（非理财类）
	public static final String BILL_RZ04 = "06";// 按揭协议
	public static final String BILL_SD08 = "07";// 资金部银行还款审批

	/**
	 * 目标业务单据中文名对照
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILL_RZ06_01, PROCESSNAME_RZ06_01);
			billNameMap.put(BILL_RZ06_02, PROCESSNAME_RZ06_02);
			billNameMap.put(BILL_36FF_01, PROCESSNAME_36FF_01);
			billNameMap.put(BILL_36FF_02, PROCESSNAME_36FF_02);
			billNameMap.put(BILL_36FA, PROCESSNAME_36FA);
			billNameMap.put(BILL_RZ04, PROCESSNAME_36FA);
			billNameMap.put(BILL_SD08, PROCESSNAME_SD08);
		}
		return billNameMap;
	}
}
