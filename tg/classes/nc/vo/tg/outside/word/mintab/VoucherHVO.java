package nc.vo.tg.outside.word.mintab;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class VoucherHVO extends SuperVO {
	private String order_seq;// ƾ֤����*
	private String file_dates;// ƾ֤����
	private String year;// �鵵���
	private String month;// �鵵�·�
	private String acc_book_name;// �����˲�
	private String src_type;// ƾ֤���(��/��/ת)
	private Integer src_doc_num;// ƾ֤��
	private String org_code;// ƾ֤��˾����
	private String org_name;// ƾ֤��˾����
	private String currency_code;// ����
	private Integer attachment_num;// ��������
	private String input_by;// �����˹���
	private String input_by_name;// ����������
	private String checked_by;// ����˹���
	private String checked_by_name;// ���������
	private String cashier;// ���ɹ���
	private String cashier_name;// ��������
	private String prepared_by;// �Ƶ��˹���
	private String prepared_by_name;// �Ƶ�������
	private String transactor;// �����˹���
	private String transactor_name;// ����������
	private UFDouble total_credit;// �跽�ܽ��
	private UFDouble total_debit;// �����ܽ��
	private String remarks;// ��ע
	private String creation_date;// ����ʱ��
	private String from_system;// add by �ƹڻ� �����Դϵͳ�ֶ� 20201023 begin

	/** ��Դϵͳ **/
	public static final String FROM_SYSTEM = "from_system";

	/** ƾ֤���� **/
	public static final String ORDER_SEQ = "order_seq";
	/** ƾ֤���� */
	public static final String FILE_DATE_S = "file_date_s";
	/** �����˲� */
	public static final String ACC_BOOK_NAME = "acc_book_name";
	/** ƾ֤���(��/��/ת) */
	public static final String SRC_TYPE = "src_type";
	/** ƾ֤�� */
	public static final String SRC_DOC_NUM = "src_doc_num";
	/** ƾ֤��˾���� */
	public static final String ORG_CODE = "org_code";
	/** ƾ֤��˾���� */
	public static final String ORG_NAME = "org_name";
	/** ���� */
	public static final String CURRENCY_CODE = "currency_code";
	/** �������� */
	public static final String ATTACHMENT_NUM = "attachment_num";
	/** �����˹��� */
	public static final String INPUT_BY = "input_by";
	/** ���������� */
	public static final String INPUT_BY_NAME = "input_by_name";
	/** ����˹��� */
	public static final String CHECKED_BY = "checked_by";
	/** ��������� */
	public static final String CHECKED_BY_NAME = "checked_by_name";
	/** ���ɹ��� */
	public static final String CASHIER = "cashier";
	/** �������� */
	public static final String CASHIER_NAME = "cashier_name";
	/** �Ƶ��˹��� */
	public static final String PREPARED_BY = "prepared_by";
	/** �Ƶ������� */
	public static final String PREPARED_BY_NAME = "prepared_by_name";
	/** �����˹��� */
	public static final String TRANSACTOR = "transactor";
	/** ���������� */
	public static final String TRANSACTOR_NAME = "transactor_name";
	/** �跽�ܽ�� */
	public static final String TOTAL_CREDIT = "total_credit";
	/** �����ܽ�� */
	public static final String TOTAL_DEBIT = "total_debit";
	/** ��ע */
	public static final String REMARKS = "remarks";
	/** ����ʱ�� */
	public static final String CREATION_DATE = "creation_date";
	
	
	public String getFrom_system() {
		return from_system;
	}

	public void setFrom_system(String from_system) {
		this.from_system = from_system;
	}

	public String getOrder_seq() {
		return order_seq;
	}

	public void setOrder_seq(String order_seq) {
		this.order_seq = order_seq;
	}

	public String getFile_dates() {
		return file_dates;
	}

	public void setFile_dates(String file_dates) {
		this.file_dates = file_dates;
	}

	public String getAcc_book_name() {
		return acc_book_name;
	}

	public void setAcc_book_name(String acc_book_name) {
		this.acc_book_name = acc_book_name;
	}

	public String getSrc_type() {
		return src_type;
	}

	public void setSrc_type(String src_type) {
		this.src_type = src_type;
	}

	public Integer getSrc_doc_num() {
		return src_doc_num;
	}

	public void setSrc_doc_num(Integer src_doc_num) {
		this.src_doc_num = src_doc_num;
	}

	public String getOrg_code() {
		return org_code;
	}

	public void setOrg_code(String org_code) {
		this.org_code = org_code;
	}

	public String getOrg_name() {
		return org_name;
	}

	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}

	public String getCurrency_code() {
		return currency_code;
	}

	public void setCurrency_code(String currency_code) {
		this.currency_code = currency_code;
	}

	public Integer getAttachment_num() {
		return attachment_num;
	}

	public void setAttachment_num(Integer attachment_num) {
		this.attachment_num = attachment_num;
	}

	public String getInput_by() {
		return input_by;
	}

	public void setInput_by(String input_by) {
		this.input_by = input_by;
	}

	public String getInput_by_name() {
		return input_by_name;
	}

	public void setInput_by_name(String input_by_name) {
		this.input_by_name = input_by_name;
	}

	public String getChecked_by() {
		return checked_by;
	}

	public void setChecked_by(String checked_by) {
		this.checked_by = checked_by;
	}

	public String getChecked_by_name() {
		return checked_by_name;
	}

	public void setChecked_by_name(String checked_by_name) {
		this.checked_by_name = checked_by_name;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getCashier_name() {
		return cashier_name;
	}

	public void setCashier_name(String cashier_name) {
		this.cashier_name = cashier_name;
	}

	public String getPrepared_by() {
		return prepared_by;
	}

	public void setPrepared_by(String prepared_by) {
		this.prepared_by = prepared_by;
	}

	public String getPrepared_by_name() {
		return prepared_by_name;
	}

	public void setPrepared_by_name(String prepared_by_name) {
		this.prepared_by_name = prepared_by_name;
	}

	public String getTransactor() {
		return transactor;
	}

	public void setTransactor(String transactor) {
		this.transactor = transactor;
	}

	public String getTransactor_name() {
		return transactor_name;
	}

	public void setTransactor_name(String transactor_name) {
		this.transactor_name = transactor_name;
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

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getCreation_date() {
		return creation_date;
	}

	public void setCreation_date(String creation_date) {
		this.creation_date = creation_date;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public String getPKFieldName() {
		return ORDER_SEQ;
	}

	public String getParentPKFieldName() {
		return null;
	}

	public String getTableName() {
		return "TEMP_ACC_DOC_H";
	}
}
