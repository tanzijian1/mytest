package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * �Ŵ����ʷ��÷�Ʊ��ϸ���ɱ�����
 * 
 * @author ASUS
 * 
 */
public class FinOutlayInvDetailedVO implements Serializable, Cloneable {

	private static final long serialVersionUID = 2774399944453685569L;
	
	private String isreceiptinvoicedef31;// ���������Ƿ������Ʊ:def31
	private String isreceiptinvoicevdef4;//�Ƿ��ȸ����Ʊ:vdef4 
	private String ticketno;//��Ʊ������

	/**
	 * ���ݺ�
	 */
	private String billno;
	
	/**
	 * ����
	 */
	private String region;
	/**
	 * ��˾����
	 */
	private String companyName;
	/**
	 * �����ͬ����
	 */
	private String loanContractName;
	/**
	 * ��������
	 */
	private String loanBank;
	/**
	 * �Ƶ�����
	 */
	private String makebilldate;
	/**
	 * ��������
	 */
	private String approvedate;
	/**
	 * ƾ֤��
	 */
	private String certificateno;
	/**
	 * ժҪ
	 */
	private String digest;
	/**
	 * ֧�����
	 */
	private UFDouble payamount;
	/**
	 * ��������
	 */
	private String costtype;
	/**
	 * �ʽ����ľ�����
	 */
	private String fundcentermanager;
	/**
	 * �Ƿ��յ���Ʊ
	 */
	private String isreceiptinvoice;
	/**
	 * �յ���Ʊ���
	 */
	private UFDouble receiptinvoiceamount;
	/**
	 * ǷƱ���
	 */
	private UFDouble oweticketamount;
	/**
	 * ��Ʊ�ע
	 */
	private String accountnote;
	
	

	public String getIsreceiptinvoicedef31() {
		return isreceiptinvoicedef31;
	}
	public void setIsreceiptinvoicedef31(String isreceiptinvoicedef31) {
		this.isreceiptinvoicedef31 = isreceiptinvoicedef31;
	}
	public String getIsreceiptinvoicevdef4() {
		return isreceiptinvoicevdef4;
	}
	public void setIsreceiptinvoicevdef4(String isreceiptinvoicevdef4) {
		this.isreceiptinvoicevdef4 = isreceiptinvoicevdef4;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getLoanContractName() {
		return loanContractName;
	}
	public void setLoanContractName(String loanContractName) {
		this.loanContractName = loanContractName;
	}
	public String getLoanBank() {
		return loanBank;
	}
	public void setLoanBank(String loanBank) {
		this.loanBank = loanBank;
	}
	public String getMakebilldate() {
		return makebilldate;
	}
	public void setMakebilldate(String makebilldate) {
		this.makebilldate = makebilldate;
	}
	public String getApprovedate() {
		return approvedate;
	}
	public void setApprovedate(String approvedate) {
		this.approvedate = approvedate;
	}
	public String getCertificateno() {
		return certificateno;
	}
	public void setCertificateno(String certificateno) {
		this.certificateno = certificateno;
	}
	public String getDigest() {
		return digest;
	}
	public void setDigest(String digest) {
		this.digest = digest;
	}
	public UFDouble getPayamount() {
		return payamount;
	}
	public void setPayamount(UFDouble payamount) {
		this.payamount = payamount;
	}
	public String getCosttype() {
		return costtype;
	}
	public void setCosttype(String costtype) {
		this.costtype = costtype;
	}
	public String getFundcentermanager() {
		return fundcentermanager;
	}
	public void setFundcentermanager(String fundcentermanager) {
		this.fundcentermanager = fundcentermanager;
	}
	public String getIsreceiptinvoice() {
		return isreceiptinvoice;
	}
	public void setIsreceiptinvoice(String isreceiptinvoice) {
		this.isreceiptinvoice = isreceiptinvoice;
	}
	public UFDouble getReceiptinvoiceamount() {
		return receiptinvoiceamount;
	}
	public void setReceiptinvoiceamount(UFDouble receiptinvoiceamount) {
		this.receiptinvoiceamount = receiptinvoiceamount;
	}
	public UFDouble getOweticketamount() {
		return oweticketamount;
	}
	public void setOweticketamount(UFDouble oweticketamount) {
		this.oweticketamount = oweticketamount;
	}
	public String getAccountnote() {
		return accountnote;
	}
	public void setAccountnote(String accountnote) {
		this.accountnote = accountnote;
	}
	public String getTicketno() {
		return ticketno;
	}
	public void setTicketno(String ticketno) {
		this.ticketno = ticketno;
	}
	
}
