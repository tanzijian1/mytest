package nc.itf.tg.outside;

public class ItfConstants {

	public static final String FORMULAR_HEAD = "itf_formulacfg_h"; // 接口公式配置表头

	public static final String FORMULAR_BODY = "itf_formulacfg_b"; // 接口公式配置表体

	public static final String BUSIREC_CODEH = "F0-Cxx-SY001_HEAD"; // 商业应收单表头编码

	public static final String BUSIREC_CODEB = "F0-Cxx-SY001_BODY"; // 商业应收单表体编码

	public static final String RECEIPT_HEAD = "F2-Cxx-SY001_HEAD"; // 商业应收单表头编码

	public static final String RECEIPT_BODY = "F2-Cxx-SY001_BODY"; // 商业应收单表体编码

	public static final String INCOME_HEAD = "FN11-Cxx-SY001_HEAD"; // 商业收入工单表头编码

	public static final String INCOME_BODY = "FN11-Cxx-SY001_BODY"; // 商业收入工单表体编码
	/**
	 * 非重复字段key
	 */
	public static final String NOTREP_FIELD_KEY = "NOTREPFIELD";

	/**
	 * 转换前非空字段key
	 */
	public static final String NOTNULL_BEFORE_FIELD_KEY = "BEFORENOTNULLFIELD";

	/**
	 * 转换后非空字段key
	 */
	public static final String NOTNULL_AFTER_FIELD_KEY = "AFTERNOTNULLFIELD";

	/**
	 * NC字段编码
	 */
	public static final String NC_FIELD_CODE = "NC_FIELD_CODE";
	/**
	 * 转换前非空字段Name
	 */
	public static final String NC_BEFORE_FIELD_NAME = "NC_BEFORE_FIELD_NAME";
	
	/**
	 * 转换后非空字段Name
	 */
	public static final String NC_AFTER_FIELD_NAME = "NC_AFTER_FIELD_NAME";
	/**
	 * 外系统字段编码
	 */
	public static final String WB_FIELD_CODE = "WB_FIELD_CODE";
	
	/**
	 * 非空校验编码
	 */
	public static final String NC_NOTREP_CODE = "NC_NOTREP_CODE";
	
	
	/**
	 * 非空校验名称
	 */
	public static final String NC_NOTREP_NAME = "NC_NOTREP_NAME";
	
	/**
	 * 公式 key
	 */
	public static final String FORMULA_KEY = "FORMULA";
	
	/**
	 * 外系统->NC
	 */
	public static final int DIRECTION_WB2NC = 0;

	/**
	 * NC->外系统
	 */
	public static final int DIRECTION_NC2WB = 1;

	/**
	 * NC-->外系统 最大传输数量 luo
	 */
	public static final int SEND_MAXNUM = 500;

	/**
	 * 公共参数编码
	 */
	public static final String SU_TRANS = "是";

	public static final String FAIL_TRANS = "否";
}
