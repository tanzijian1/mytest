package nc.vo.tg.outside.word.mintab;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class VoucherLVO extends SuperVO {

	private String order_seq;// ƾ֤��¼����*
	private String h_seq;// ƾ֤����*
	private String src_doc_num;// ƾ֤��
	private String summary;// ժҪ
	private String assid;// ������ID
	private String acc_name;// ��ƿ�Ŀ
	private UFDouble amount_of_credit;// �跽���
	private UFDouble amount_of_debit;// �������
	
	private UFDouble detail_index;// add by  �ƹڻ� ��ӷ�¼���� 20201026

	/** ƾ֤��¼���� **/
	public static final String ORDER_SEQ = "order_seq";
	/** ƾ֤���� **/
	public static final String H_SEQ = "h_seq";
	/** ƾ֤�� */
	public static final String SRC_DOC_NUM = "src_doc_num";
	/** ժҪ */
	public static final String SUMMARY = "summary";
	/** ��ƿ�Ŀ */
	public static final String ACC_NAME = "acc_name";
	/** �跽��� */
	public static final String AMOUNT_OF_CREDIT = "amount_of_credit";
	/** ������� */
	public static final String AMOUNT_OF_DEBIT = "amount_of_debit";
	
	/** ��¼���� */
	public static final String DETAIL_INDEX = "detail_index";

	
	public UFDouble getDetail_index() {
		return detail_index;
	}

	public void setDetail_index(UFDouble detail_index) {
		this.detail_index = detail_index;
	}
	
	
	public String getAssid() {
		return assid;
	}

	public void setAssid(String assid) {
		this.assid = assid;
	}

	public String getOrder_seq() {
		return order_seq;
	}

	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}

	public String getH_seq() {
		return h_seq;
	}

	public void setH_seq(String h_seq) {
		this.h_seq = h_seq;
	}

	public String getSrc_doc_num() {
		return src_doc_num;
	}

	public void setSrc_doc_num(String src_doc_num) {
		this.src_doc_num = src_doc_num;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getAcc_name() {
		return acc_name;
	}

	public void setAcc_name(String acc_name) {
		this.acc_name = acc_name;
	}

	public UFDouble getAmount_of_credit() {
		return amount_of_credit;
	}

	public void setAmount_of_credit(UFDouble amount_of_credit) {
		this.amount_of_credit = amount_of_credit;
	}

	public UFDouble getAmount_of_debit() {
		return amount_of_debit;
	}

	public void setAmount_of_debit(UFDouble amount_of_debit) {
		this.amount_of_debit = amount_of_debit;
	}

	public String getPKFieldName() {
		return ORDER_SEQ;
	}

	public String getParentPKFieldName() {
		return H_SEQ;
	}

	public String getTableName() {
		return "TEMP_ACC_DOC_L";
	}
}
