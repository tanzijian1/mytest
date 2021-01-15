package nc.vo.tgfn.report.sale;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;
/**
 * Ӫ����̨ͬ��VO
 * @author yy
 *
 */
public class MarketInvVO implements Serializable, Cloneable {
	private String citycompany;//�������ͬ���й�˾�ֶΣ�	
	private String projectname; //�������ͬ��Ŀ�����ֶΣ�	
	private String contractnum; //�������ͬ��ͬ�����ֶΣ�	
	private String contractname; //�������ͬ��ͬ�����ֶΣ�	
	private String bunit; //�������ͬ�ҷ��ֶΣ�	
	private String aczCompany; //�������ͬ�׷��ֶΣ�	
	private UFDouble contractmoneyamount; //�������ͬ��̬��˰�	
	private String signdate; //�������ͬǩԼ���ڣ�	
	private String handleman; //�������ͬ��ͬ�����ˣ�	
	private String budgetSubject	; //Ԥ���Ŀ
	private String qkBill; //�������ִͬ������������뵥�ֶΣ�	
	private String invoiceNum; //�����Ʊ��Ʊ�ţ�	
	private UFDouble taxMoneyamount; //����˰�ϼ��ܶ	
	private UFDouble noTaxMoneyamount; //(���Ʊ��˰����ܶ�)	
	private String taxrat; //���Ʊ����˰�ʣ�	
	 private UFDouble invoiceSumamount; //	˰��
	 private String billno;//���ݱ��
	 private UFDouble qkmoneyamount;//�����
	 private UFDouble fkmoneyamount;//������
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
	private String rzmonth; //(ȡƾ֤����)	
	private String glnum; //	
	private String qzfla; //(Ӧ����Ʊ��Ȩ������)	
}
