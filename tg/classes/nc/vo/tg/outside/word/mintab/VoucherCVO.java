package nc.vo.tg.outside.word.mintab;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class VoucherCVO extends SuperVO {
	private String order_seq;// 主键
	private String org_code;// 法人公司编码
	private String year;// 归档年份
	private String month;// 归档月份
	private UFDouble src_num;// 凭证数量(收)
	private UFDouble attachment_num;// 附件数量(收)
	private String src_start;// 凭证起始号(收)
	private String src_end;// 凭证结束号(收)
	private UFDouble total_credit;// 借方总金额(收)
	private UFDouble total_debit;// 贷方总金额(收)

	private String src_num_pay;// 凭证数量(付)
	private UFDouble attachment_num_pay;// 附件数量(付)
	private String src_start_pay;// 凭证起始号(付)
	private String src_end_end;// 凭证结束号(付)
	private UFDouble total_credit_pay;// 借方总金额(付)
	private UFDouble total_debit_pay;// 贷方总金额(付)

	private UFDouble src_num_tra;// 凭证数量(转)
	private UFDouble attachment_num_tra;// 附件数量(转)
	private String src_start_tra;// 凭证起始号(转)
	private String src_end_tra;// 凭证结束号(转)
	private UFDouble total_credit_tra;// 借方总金额(转)
	private UFDouble total_debit_tra;// 借方总金额(转)

	private UFDouble src_num_mark;// 凭证数量(记)
	private UFDouble attachment_num_mark;// 附件数量(记)
	private String src_start_mark;// 凭证起始号(记)
	private String src_end_mark;// 凭证结束号(转)
	private UFDouble total_credit_mark;// 借方总金额(记)
	private UFDouble total_debit_mark;// 借方总金额(记)

	/** 主键 */
	public static final String ORDER_SEQ = "order_seq";
	/** 法人公司编码 */
	public static final String ORG_CODE = "org_code";
	/** 归档年份 */
	public static final String YEAR = "year";
	/** 归档月份 */
	public static final String MONTH = "month";
	/** 凭证数量(收) */
	public static final String SRC_NUM = "src_num";
	/** 附件数量(收) */
	public static final String ATTACHMENT_NUM = "attachment_num";
	/** 凭证起始号(收) */
	public static final String SRC_START = "src_start";
	/** 凭证结束号(收) */
	public static final String SRC_END = "src_end";
	/** 借方总金额(收) */
	public static final String TOTAL_CREDIT = "total_credit";
	/** 贷方总金额(收) */
	public static final String TOTAL_DEBIT = "total_debit";
	/** 凭证数量(付) */
	public static final String SRC_NUM_PAY = "src_num_pay";
	/** 附件数量(付) */
	public static final String ATTACHMENT_NUM_PAY = "attachment_num_pay";
	/** 凭证起始号(付) */
	public static final String SRC_START_PAY = "src_start_pay";
	/** 凭证结束号(付) */
	public static final String SRC_END_END = "src_end_end";
	/** 借方总金额(付) */
	public static final String TOTAL_CREDIT_PAY = "total_credit_pay";
	/** 贷方总金额(付) */
	public static final String TOTAL_DEBIT_PAY = "total_debit_pay";
	/** 凭证数量(转) */
	public static final String SRC_NUM_TRA = "src_num_tra";
	/** 附件数量(转) */
	public static final String ATTACHMENT_NUM_TRA = "attachment_num_tra";
	/** 凭证起始号(转) */
	public static final String SRC_START_TRA = "src_start_tra";
	/** 凭证结束号(转) */
	public static final String SRC_END_TRA = "src_end_tra";
	/** 借方总金额(转) */
	public static final String TOTAL_CREDIT_TRA = "total_credit_tra";
	/** 贷方总金额(转) */
	public static final String TOTAL_DEBIT_TRA = "total_debit_tra";

	/** 凭证数量(记) */
	public static final String SRC_NUM_MARK = "src_num_mark";
	/** 附件数量(记) */
	public static final String ATTACHMENT_NUM_MARK = "attachment_num_mark";
	/** 凭证起始号(记) */
	public static final String SRC_START_MARK = "src_start_mark";
	/** 凭证结束号(记) */
	public static final String SRC_END_MARK = "src_end_mark";
	/** 借方总金额(记) */
	public static final String TOTAL_CREDIT_MARK = "total_credit_mark";
	/** 贷方总金额(记) */
	public static final String TOTAL_DEBIT_MARK = "total_debit_mark";

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getOrder_seq() {
		return order_seq;
	}

	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getSrc_start() {
		return src_start;
	}

	public void setSrc_start(String src_start) {
		this.src_start = src_start;
	}

	public String getSrc_end() {
		return src_end;
	}

	public void setSrc_end(String src_end) {
		this.src_end = src_end;
	}

	public String getSrc_num_pay() {
		return src_num_pay;
	}

	public void setSrc_num_pay(String src_num_pay) {
		this.src_num_pay = src_num_pay;
	}

	public String getSrc_start_pay() {
		return src_start_pay;
	}

	public void setSrc_start_pay(String src_start_pay) {
		this.src_start_pay = src_start_pay;
	}

	public String getSrc_end_end() {
		return src_end_end;
	}

	public void setSrc_end_end(String src_end_end) {
		this.src_end_end = src_end_end;
	}

	public String getSrc_start_tra() {
		return src_start_tra;
	}

	public void setSrc_start_tra(String src_start_tra) {
		this.src_start_tra = src_start_tra;
	}

	public String getSrc_end_tra() {
		return src_end_tra;
	}

	public void setSrc_end_tra(String src_end_tra) {
		this.src_end_tra = src_end_tra;
	}

	public String getPKFieldName() {
		return ORDER_SEQ;
	}

	public String getParentPKFieldName() {
		return null;
	}

	public UFDouble getSrc_num() {
		return src_num;
	}

	public void setSrc_num(UFDouble src_num) {
		this.src_num = src_num;
	}

	public UFDouble getAttachment_num() {
		return attachment_num;
	}

	public void setAttachment_num(UFDouble attachment_num) {
		this.attachment_num = attachment_num;
	}

	public UFDouble getTotal_credit() {
		return total_credit;
	}

	public void setTotal_credit(UFDouble total_credit) {
		this.total_credit = total_credit;
	}

	public UFDouble getTotal_debit() {
		return total_debit;
	}

	public void setTotal_debit(UFDouble total_debit) {
		this.total_debit = total_debit;
	}

	public UFDouble getAttachment_num_pay() {
		return attachment_num_pay;
	}

	public void setAttachment_num_pay(UFDouble attachment_num_pay) {
		this.attachment_num_pay = attachment_num_pay;
	}

	public UFDouble getTotal_credit_pay() {
		return total_credit_pay;
	}

	public void setTotal_credit_pay(UFDouble total_credit_pay) {
		this.total_credit_pay = total_credit_pay;
	}

	public UFDouble getTotal_debit_pay() {
		return total_debit_pay;
	}

	public void setTotal_debit_pay(UFDouble total_debit_pay) {
		this.total_debit_pay = total_debit_pay;
	}

	public UFDouble getSrc_num_tra() {
		return src_num_tra;
	}

	public void setSrc_num_tra(UFDouble src_num_tra) {
		this.src_num_tra = src_num_tra;
	}

	public UFDouble getAttachment_num_tra() {
		return attachment_num_tra;
	}

	public void setAttachment_num_tra(UFDouble attachment_num_tra) {
		this.attachment_num_tra = attachment_num_tra;
	}

	public UFDouble getTotal_credit_tra() {
		return total_credit_tra;
	}

	public UFDouble getTotal_debit_tra() {
		return total_debit_tra;
	}

	public void setTotal_debit_tra(UFDouble total_debit_tra) {
		this.total_debit_tra = total_debit_tra;
	}

	public void setTotal_credit_tra(UFDouble total_credit_tra) {
		this.total_credit_tra = total_credit_tra;
	}

	public UFDouble getSrc_num_mark() {
		return src_num_mark;
	}

	public void setSrc_num_mark(UFDouble src_num_mark) {
		this.src_num_mark = src_num_mark;
	}

	public String getSrc_start_mark() {
		return src_start_mark;
	}

	public void setSrc_start_mark(String src_start_mark) {
		this.src_start_mark = src_start_mark;
	}

	public String getSrc_end_mark() {
		return src_end_mark;
	}

	public void setSrc_end_mark(String src_end_mark) {
		this.src_end_mark = src_end_mark;
	}

	public UFDouble getTotal_credit_mark() {
		return total_credit_mark;
	}

	public void setTotal_credit_mark(UFDouble total_credit_mark) {
		this.total_credit_mark = total_credit_mark;
	}

	public UFDouble getTotal_debit_mark() {
		return total_debit_mark;
	}

	public void setTotal_debit_mark(UFDouble total_debit_mark) {
		this.total_debit_mark = total_debit_mark;
	}

	public UFDouble getAttachment_num_mark() {
		return attachment_num_mark;
	}

	public void setAttachment_num_mark(UFDouble attachment_num_mark) {
		this.attachment_num_mark = attachment_num_mark;
	}

	public String getTableName() {
		return "TEMP_ACC_DOC_C";
	}
}
