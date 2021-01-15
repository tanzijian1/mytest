package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 信贷融资费用发票明细（成本报表）
 * 
 * @author ASUS
 * 
 */
public class FinOutlayInvDetailedVO implements Serializable, Cloneable {

	private static final long serialVersionUID = 2774399944453685569L;
	
	private String isreceiptinvoicedef31;// 本次流程是否产生发票:def31
	private String isreceiptinvoicevdef4;//是否先付款后补票:vdef4 
	private String ticketno;//补票单单号

	/**
	 * 单据号
	 */
	private String billno;
	
	/**
	 * 地区
	 */
	private String region;
	/**
	 * 公司名称
	 */
	private String companyName;
	/**
	 * 贷款合同名称
	 */
	private String loanContractName;
	/**
	 * 贷款银行
	 */
	private String loanBank;
	/**
	 * 制单日期
	 */
	private String makebilldate;
	/**
	 * 审批日期
	 */
	private String approvedate;
	/**
	 * 凭证号
	 */
	private String certificateno;
	/**
	 * 摘要
	 */
	private String digest;
	/**
	 * 支付金额
	 */
	private UFDouble payamount;
	/**
	 * 费用类型
	 */
	private String costtype;
	/**
	 * 资金中心经办人
	 */
	private String fundcentermanager;
	/**
	 * 是否收到发票
	 */
	private String isreceiptinvoice;
	/**
	 * 收到发票金额
	 */
	private UFDouble receiptinvoiceamount;
	/**
	 * 欠票金额
	 */
	private UFDouble oweticketamount;
	/**
	 * 会计备注
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
