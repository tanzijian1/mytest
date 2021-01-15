package nc.vo.tgfn.report.sale;

import nc.vo.pub.lang.UFDouble;

public class SaleContractInvPayVO {
private String	    citycompany;	//城市公司
private String	    projectname;	//项目名称
private String	    contractnum;	//合同编号
private String	    contractname;	//合同名称
private String	    bunit;	//乙方单位
private String      aczCompany;//出账公司
private UFDouble	contractmoneyamount;	//合同动态金额
private String      signdate;	//签订日期
private String	    handleman;	//经办人
private String	    budgetSubject;	//预算科目
private UFDouble	    requestmoneyamount;	//累计已请款
private UFDouble	paymoneyamount;	//累计已付款
private UFDouble	innvocemoneyamount;	//累计已收发票
private UFDouble	notaxmoneyamount;	//不含税金额
private String	    tax;	//税率
private UFDouble	taxmoneyamount;	//税额
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
public UFDouble getRequestmoneyamount() {
	return requestmoneyamount;
}
public void setRequestmoneyamount(UFDouble requestmoneyamount) {
	this.requestmoneyamount = requestmoneyamount;
}
public UFDouble getPaymoneyamount() {
	return paymoneyamount;
}
public void setPaymoneyamount(UFDouble paymoneyamount) {
	this.paymoneyamount = paymoneyamount;
}
public UFDouble getInnvocemoneyamount() {
	return innvocemoneyamount;
}
public void setInnvocemoneyamount(UFDouble innvocemoneyamount) {
	this.innvocemoneyamount = innvocemoneyamount;
}
public UFDouble getNotaxmoneyamount() {
	return notaxmoneyamount;
}
public void setNotaxmoneyamount(UFDouble notaxmoneyamount) {
	this.notaxmoneyamount = notaxmoneyamount;
}
public String getTax() {
	return tax;
}
public void setTax(String tax) {
	this.tax = tax;
}
public UFDouble getTaxmoneyamount() {
	return taxmoneyamount;
}
public void setTaxmoneyamount(UFDouble taxmoneyamount) {
	this.taxmoneyamount = taxmoneyamount;
}

}
