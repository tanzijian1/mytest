package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class IMGBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String BILLTYPE_HZ01 = "HZ01"; // 进项发票

	public static final String BILLNAME_HZ01 = "进项发票"; // 进项发票
	
	public static final String BILLTYPE_264XCXX004 = "264X-Cxx-004"; // 合同类费用请款单

	public static final String BILLNAME_264XCXX004 = "合同类费用请款单"; // 合同类费用请款单
    
	public static final String BILLTYPE_264X="264X";//报销单影像状态回调
	
	public static final String BILLNAME_264X="报销单影像状态";
	
	public static final String BILLTYPE_267X="267X-Cxx-001";//补票工单影像状态回调
	
	public static final String BILLNAME_267X="补票工单影像状态";
	/**
	 * 目标业务单据中文名对照 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(BILLTYPE_HZ01, BILLNAME_HZ01);
			billNameMap.put(BILLTYPE_264XCXX004, BILLNAME_264XCXX004);
			billNameMap.put(BILLTYPE_264X, BILLNAME_264X);
			billNameMap.put(BILLTYPE_267X, BILLNAME_267X);
		}
		return billNameMap;
	}
}
