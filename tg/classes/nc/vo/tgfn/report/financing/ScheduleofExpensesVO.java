package nc.vo.tgfn.report.financing;

import java.io.Serializable;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

/**
 * ���ÿ�Ŀ��ϸ��
 * 
 * @param ljf
 * @return
 * @throws BusinessException
 */
public class ScheduleofExpensesVO extends SuperVO {
	private static final long serialVersionUID = -1721510589130836493L;
	private String pk_schedule;
	/** ҵ�񵥾�PK */
	private String billskey;
	/** Ԥ���Ŀ���� */
	private String code;
	/** Ԥ���Ŀ */
	private String subject;
	/** Ԥ�������λ */
	private String units;
	/** Ԥ��������� */
	private String department;
	/** ���˹�˾ */
	private String account;
	/** ���ݱ�� */
	private String billno;
	/** ���� */
	private String currency;
	/** ������ */
	private UFDouble appliedamount;
	/** �տ��� */
	private String receiver;
	/** ������ */
	private String reimbursement;
	/** ��Ӧ�� */
	private String supplier;
	/** ��Ŀ */
	private String project;
	/** ҵ̬ */
	private String formats;
	/** ����¥�� */
	private String floor;
	/** ���Ʋ��� */
	private String platenumber;
	/** �������� */
	private String billtype;
	/** ����״̬ */
	private String billsstate;
	/** �������� */
	private String billsdate;
	/** �������� */
	private String approvaldate;
	/** �������� */
	private String settlementdate;
	/** ������ */
	private String applicant;
	/** ��Ʊ���� */
	private String invoicetype;
	/** ��Ʊ�� */
	private String invoiceon;
	/** ��Ʊ��� */
	private UFDouble invoiceamount;
	/** ˰�� */
	private UFDouble taxamount;
	/** ���û�ƿ�Ŀ */
	private String expenseaccount;
	/** ժҪ */
	private String abstraction;
	/** ������ */
	private UFDouble incurredamount;
	/** �Ƶ��� */
	private String makingpeople;
	/** �Ƶ����� */
	private String voucherdate;
	/** ��Դϵͳ */
	private String srcsystem;
	/** ƾ֤�� */
	private String vertificatenumber;
	/** ����״̬ */
	private String approvalstatus;
	/** ���� */
	private String reason;
	/** ��ͬ���� */
	private String contractcode;
	/** ��ͬ���� */
	private String contractname;
	/** Ԥ�ᵥ�ݱ�� */
	private String documentone;
	/** EBS������ */
	private String ebsappnum;
	private String def1;
	/** Ԥ���ֶ�1 */
	private String ylzd1;
	/** Ԥ���ֶ�2 */
	private String ylzd2;
	/** Ԥ���ֶ�3 */
	private String ylzd3;
	/** Ԥ���ֶ�4 */
	private String ylzd4;
	/** Ԥ���ֶ�5 */
	private String ylzd5;

	public String getSrcsystem() {
		return srcsystem;
	}

	public void setSrcsystem(String srcsystem) {
		this.srcsystem = srcsystem;
	}

	public String getBillskey() {
		return billskey;
	}

	public void setBillskey(String billskey) {
		this.billskey = billskey;
	}

	public String getDef1() {
		return def1;
	}

	public void setDef1(String def1) {
		this.def1 = def1;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public UFDouble getAppliedamount() {
		return appliedamount;
	}

	public void setAppliedamount(UFDouble appliedamount) {
		this.appliedamount = appliedamount;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReimbursement() {
		return reimbursement;
	}

	public void setReimbursement(String reimbursement) {
		this.reimbursement = reimbursement;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getFormats() {
		return formats;
	}

	public void setFormats(String formats) {
		this.formats = formats;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getPlatenumber() {
		return platenumber;
	}

	public void setPlatenumber(String platenumber) {
		this.platenumber = platenumber;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getBillsstate() {
		return billsstate;
	}

	public void setBillsstate(String billsstate) {
		this.billsstate = billsstate;
	}

	public String getBillsdate() {
		return billsdate;
	}

	public void setBillsdate(String billsdate) {
		this.billsdate = billsdate;
	}

	public String getApprovaldate() {
		return approvaldate;
	}

	public void setApprovaldate(String approvaldate) {
		this.approvaldate = approvaldate;
	}

	public String getSettlementdate() {
		return settlementdate;
	}

	public void setSettlementdate(String settlementdate) {
		this.settlementdate = settlementdate;
	}

	public String getApplicant() {
		return applicant;
	}

	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}

	public String getInvoicetype() {
		return invoicetype;
	}

	public void setInvoicetype(String invoicetype) {
		this.invoicetype = invoicetype;
	}

	public String getInvoiceon() {
		return invoiceon;
	}

	public void setInvoiceon(String invoiceon) {
		this.invoiceon = invoiceon;
	}

	public UFDouble getInvoiceamount() {
		return invoiceamount;
	}

	public void setInvoiceamount(UFDouble invoiceamount) {
		this.invoiceamount = invoiceamount;
	}

	public UFDouble getTaxamount() {
		return taxamount;
	}

	public void setTaxamount(UFDouble taxamount) {
		this.taxamount = taxamount;
	}

	public String getExpenseaccount() {
		return expenseaccount;
	}

	public void setExpenseaccount(String expenseaccount) {
		this.expenseaccount = expenseaccount;
	}

	public String getAbstraction() {
		return abstraction;
	}

	public void setAbstraction(String abstraction) {
		this.abstraction = abstraction;
	}

	public UFDouble getIncurredamount() {
		return incurredamount;
	}

	public void setIncurredamount(UFDouble incurredamount) {
		this.incurredamount = incurredamount;
	}

	public String getMakingpeople() {
		return makingpeople;
	}

	public void setMakingpeople(String makingpeople) {
		this.makingpeople = makingpeople;
	}

	public String getVoucherdate() {
		return voucherdate;
	}

	public void setVoucherdate(String voucherdate) {
		this.voucherdate = voucherdate;
	}

	public String getVertificatenumber() {
		return vertificatenumber;
	}

	public void setVertificatenumber(String vertificatenumber) {
		this.vertificatenumber = vertificatenumber;
	}

	public String getApprovalstatus() {
		return approvalstatus;
	}

	public void setApprovalstatus(String approvalstatus) {
		this.approvalstatus = approvalstatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public String getContractname() {
		return contractname;
	}

	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	public String getDocumentone() {
		return documentone;
	}

	public void setDocumentone(String documentone) {
		this.documentone = documentone;
	}

	public String getEbsappnum() {
		return ebsappnum;
	}

	public void setEbsappnum(String ebsappnum) {
		this.ebsappnum = ebsappnum;
	}

	public String getYlzd1() {
		return ylzd1;
	}

	public void setYlzd1(String ylzd1) {
		this.ylzd1 = ylzd1;
	}

	public String getYlzd2() {
		return ylzd2;
	}

	public void setYlzd2(String ylzd2) {
		this.ylzd2 = ylzd2;
	}

	public String getYlzd3() {
		return ylzd3;
	}

	public void setYlzd3(String ylzd3) {
		this.ylzd3 = ylzd3;
	}

	public String getYlzd4() {
		return ylzd4;
	}

	public void setYlzd4(String ylzd4) {
		this.ylzd4 = ylzd4;
	}

	public String getYlzd5() {
		return ylzd5;
	}

	public void setYlzd5(String ylzd5) {
		this.ylzd5 = ylzd5;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPk_schedule() {
		return pk_schedule;
	}

	public void setPk_schedule(String pk_schedule) {
		this.pk_schedule = pk_schedule;
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {

		return "temp_tg_schedule";
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2016-4-18
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {
		return "pk_schedule";
	}
}
