package nc.itf.tg.outside;

import java.util.HashMap;
import java.util.Map;

public class BusinessBillCont {

	private static Map<String, String> billNameMap = null;

	public static final String DOC_01 = "org"; // 财务组织查询接口

	public static final String DOCNAME_01 = "财务组织查询"; // 财务组织查询接口

	public static final String DOC_02 = "del_receipt"; // 收款单删除

	public static final String DOCNAME_02 = "收款单删除"; // 收款单删除

	public static final String DOC_03 = "statement_query"; // 银行流水查询
    
	public static final String DOC_10="busiPay";//商业退款单接口
	
	public static final String DOCNAME_10="商业退款单接口";
	
	public static final String DOC_11="busiRefWk";//新非合同类费用请款单写入接口
	
	public static final String DOCNAME_11="新非合同类费用请款单写入接口";//新非合同类费用请款单写入接口
	
	public static final String DOC_12="busiHisWk";//历史单据补录单写入接口
	
	public static final String DOCNAME_12="历史单据补录单写入接口";
	
	public static final String DOC_13="busiInvWk";//发票工单写入接口
	
	public static final String DOCNAME_13="发票工单写入接口";
	
	public static final String DOCNAME_03 = "银行流水查询"; // 银行流水查询
	
	public static final String DOCNAME_04 = "BUSI-->NC收款单"; // BUSI-->NC收款单

	public static final String DOC_04 = "busiGat"; // BUSI-->NC收款单

	public static final String DOC_05 = "Income"; // BUSI-->NC收款单

	public static final String DOCNAME_05 = "BUSI-->NC商业收入工单"; // BUSI-->NC收款单

	public static final String DOC_NAME = "历史单据补录单写入接口";

	public static final String SRCSYS = "商业"; // 来源系统

	public static final String BUSIREC = "busiRec"; // 商业应收单

	public static final String BUSIREC_NAME = "商业应收单写入接口"; // 应收单写入接口

	/**
	 * 目标业务单据中文名对照 460219
	 * 
	 * 
	 * @return
	 */
	public static Map<String, String> getBillNameMap() {
		if (billNameMap == null) {
			billNameMap = new HashMap<String, String>();
			billNameMap.put(DOC_01, DOCNAME_01);
			billNameMap.put(DOC_02, DOCNAME_02);
			billNameMap.put(DOC_10, DOCNAME_10);
			billNameMap.put(DOC_04, DOCNAME_04);
			billNameMap.put(DOC_05, DOCNAME_05);
			billNameMap.put(DOC_10, DOCNAME_10);
			billNameMap.put(DOC_11, DOCNAME_11);
			billNameMap.put(DOC_12, DOCNAME_12);
			billNameMap.put(DOC_13, DOCNAME_13);
			billNameMap.put(BUSIREC, BUSIREC_NAME);
		}
		return billNameMap;
	}
}
