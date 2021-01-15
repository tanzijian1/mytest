package nc.vo.tgfn.report.sale;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;
/**
 * 营销合同台账VO
 * @author yy
 *
 */
public class MarketInvVO implements Serializable, Cloneable {
	private String citycompany;//（付款合同城市公司字段）	
	private String projectname; //（付款合同项目名称字段）	
	private String contractnum; //（付款合同合同编码字段）	
	private String contractname; //（付款合同合同名称字段）	
	private String bunit; //（付款合同乙方字段）	
	private String aczCompany; //（付款合同甲方字段）	
	private UFDouble contractmoneyamount; //（付款合同动态金额（税额）	
	private String signdate; //（付款合同签约日期）	
	private String handleman; //（付款合同合同管理人）	
	private String budgetSubject	; //预算科目
	private String qkBill; //（付款合同执行情况付款申请单字段）	
	private String invoiceNum; //（进项发票发票号）	
	private UFDouble taxMoneyamount; //（价税合计总额）	
	private UFDouble noTaxMoneyamount; //(进项发票无税金额总额)	
	private String taxrat; //进项发票表体税率）	
	 private UFDouble invoiceSumamount; //	税额
	 private String billno;//单据编号
	 private UFDouble qkmoneyamount;//请款金额
	 private UFDouble fkmoneyamount;//付款金额
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public UFDouble getQkmoneyamount() {
		return qkmoneyamount;
	}
	public void setQkmoneyamount(UFDouble qkmoneyamount) {
		this.qkmoneyamount = qkmoneyamount;
	}
	public UFDouble getFkmoneyamount() {
		return fkmoneyamount;
	}
	public void setFkmoneyamount(UFDouble fkmoneyamount) {
		this.fkmoneyamount = fkmoneyamount;
	}
	public String getCitycompany() {
		return citycompany;
	}
	public void setCitycompany(String citycompany) {
		this.citycompany = citycompany;
	}
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getContractnum() {
		return contractnum;
	}
	public void setContractnum(String contractnum) {
		this.contractnum = contractnum;
	}
	public String getContractname() {
		return contractname;
	}
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}
	public String getBunit() {
		return bunit;
	}
	public void setBunit(String bunit) {
		this.bunit = bunit;
	}
	public String getAczCompany() {
		return aczCompany;
	}
	public void setAczCompany(String aczCompany) {
		this.aczCompany = aczCompany;
	}
	public UFDouble getContractmoneyamount() {
		return contractmoneyamount;
	}
	public void setContractmoneyamount(UFDouble contractmoneyamount) {
		this.contractmoneyamount = contractmoneyamount;
	}
	
	public String getSigndate() {
		return signdate;
	}
	public void setSigndate(String signdate) {
		this.signdate = signdate;
	}
	public String getHandleman() {
		return handleman;
	}
	public void setHandleman(String handleman) {
		this.handleman = handleman;
	}
	public String getBudgetSubject() {
		return budgetSubject;
	}
	public void setBudgetSubject(String budgetSubject) {
		this.budgetSubject = budgetSubject;
	}
	public String getQkBill() {
		return qkBill;
	}
	public void setQkBill(String qkBill) {
		this.qkBill = qkBill;
	}
	public String getInvoiceNum() {
		return invoiceNum;
	}
	public void setInvoiceNum(String invoiceNum) {
		this.invoiceNum = invoiceNum;
	}
	public UFDouble getTaxMoneyamount() {
		return taxMoneyamount;
	}
	public void setTaxMoneyamount(UFDouble taxMoneyamount) {
		this.taxMoneyamount = taxMoneyamount;
	}
	public UFDouble getNoTaxMoneyamount() {
		return noTaxMoneyamount;
	}
	public void setNoTaxMoneyamount(UFDouble noTaxMoneyamount) {
		this.noTaxMoneyamount = noTaxMoneyamount;
	}
	public String getTaxrat() {
		return taxrat;
	}
	public void setTaxrat(String taxrat) {
		this.taxrat = taxrat;
	}
	public UFDouble getInvoiceSumamount() {
		return invoiceSumamount;
	}
	public void setInvoiceSumamount(UFDouble invoiceSumamount) {
		this.invoiceSumamount = invoiceSumamount;
	}
	public String getRzmonth() {
		return rzmonth;
	}
	public void setRzmonth(String rzmonth) {
		this.rzmonth = rzmonth;
	}
	public String getGlnum() {
		return glnum;
	}
	public void setGlnum(String glnum) {
		this.glnum = glnum;
	}
	public String getQzfla() {
		return qzfla;
	}
	public void setQzfla(String qzfla) {
		this.qzfla = qzfla;
	}
	private String rzmonth; //(取凭证日期)	
	private String glnum; //	
	private String qzfla; //(应付单票据权责发生制)	
}
