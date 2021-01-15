package nc.vo.tg.outside;

import java.io.Serializable;

public class LLPaymentStatementJsonBVO implements Serializable {

	private String memo;// 摘要
	private String pk_supplier;// 供应商
	private String pk_balatype;// 结算方式
	private String pk_oppaccount;// 付款单位银行账户
	private String bd_bankdoc;// 付款单位开户行
	private String pay_local;// 金额
	private String deposit;// 退押金人
	private String accounttype;// 收款账户性质
	private String pk_account;// 收款银行账号
	private String accountname;// 收款账户户名
	private String accountopenbank;// 收款银行名称
	private String rowid;// 物业收费系统单据行ID
	private String project;// 项目
	private String arrears;// 【是否往年欠费】
	private String businessbreakdown;// 业务细类
	private String itemtype;// 收费项目类型
	private String itemname;// 收费项目名称
	private String projectphase;// 项目阶段
	private String properties;// 项目属性
	private String paymenttype;// 款项类型
	private String subordinateyear;// 应收单所属年月
	private String supplierclassification;// 供应商分类
	private String propertycode;// 房产编码
	private String propertyname;// 房产名称

	public String getSupplierclassification() {
		return supplierclassification;
	}

	public void setSupplierclassification(String supplierclassification) {
		this.supplierclassification = supplierclassification;
	}

	public String getPropertycode() {
		return propertycode;
	}

	public void setPropertycode(String propertycode) {
		this.propertycode = propertycode;
	}

	public String getPropertyname() {
		return propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_supplier() {
		return pk_supplier;
	}

	public void setPk_supplier(String pk_supplier) {
		this.pk_supplier = pk_supplier;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getPk_oppaccount() {
		return pk_oppaccount;
	}

	public void setPk_oppaccount(String pk_oppaccount) {
		this.pk_oppaccount = pk_oppaccount;
	}

	public String getPay_local() {
		return pay_local;
	}

	public void setPay_local(String pay_local) {
		this.pay_local = pay_local;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getPk_account() {
		return pk_account;
	}

	public void setPk_account(String pk_account) {
		this.pk_account = pk_account;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountopenbank() {
		return accountopenbank;
	}

	public void setAccountopenbank(String accountopenbank) {
		this.accountopenbank = accountopenbank;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getBusinessbreakdown() {
		return businessbreakdown;
	}

	public void setBusinessbreakdown(String businessbreakdown) {
		this.businessbreakdown = businessbreakdown;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getProjectphase() {
		return projectphase;
	}

	public void setProjectphase(String projectphase) {
		this.projectphase = projectphase;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getSubordinateyear() {
		return subordinateyear;
	}

	public void setSubordinateyear(String subordinateyear) {
		this.subordinateyear = subordinateyear;
	}

	public String getBd_bankdoc() {
		return bd_bankdoc;
	}

	public void setBd_bankdoc(String bd_bankdoc) {
		this.bd_bankdoc = bd_bankdoc;
	}

}
