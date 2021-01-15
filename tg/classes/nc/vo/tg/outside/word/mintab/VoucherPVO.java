package nc.vo.tg.outside.word.mintab;

import nc.vo.pub.SuperVO;

public class VoucherPVO extends SuperVO {
	private String h_seq;// 凭证主表ID
	private String order_seq;// 主键
	private String p_code;// 影像编号
	private String src_doc_num;// 凭证号
	private String creation_date;// 创建时间

	/** 凭证分录主键 **/
	public static final String ORDER_SEQ = "order_seq";
	/** 凭证主键 **/
	public static final String H_SEQ = "h_seq";
	/** 凭证号 */
	public static final String SRC_DOC_NUM = "src_doc_num";
	/** 影像编号 */
	public static final String P_CODE = "p_code";
	/** 创建时间 */
	public static final String CREATION_DATE = "creation_date";

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getH_seq() {
		return h_seq;
	}

	public void setH_seq(String h_seq) {
		this.h_seq = h_seq;
	}

	public String getOrder_seq() {
		return order_seq;
	}

	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}

	public String getP_code() {
		return p_code;
	}

	public void setP_code(String p_code) {
		this.p_code = p_code;
	}

	public String getSrc_doc_num() {
		return src_doc_num;
	}

	public void setSrc_doc_num(String src_doc_num) {
		this.src_doc_num = src_doc_num;
	}

	public String getPKFieldName() {
		return ORDER_SEQ;
	}

	public String getParentPKFieldName() {
		return null;
	}

	public String getTableName() {
		return "TEMP_ACC_DOC_P";
	}
}
