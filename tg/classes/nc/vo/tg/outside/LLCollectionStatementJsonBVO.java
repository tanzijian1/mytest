package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCollectionStatementJsonBVO implements Serializable {
	private String memo;
	private String pk_customer;
	private String pk_balatype;
	private String accnum;
	private String taxrate;// 税率 taxrate
	private String local_tax_cr;// 税额 local_tax_cr
	private String notax_cr;// 无税金额 notax_cr
	private String rec_local;// 含税金额 rec_local
	private String project;// 项目 project
	private String itemtype;// 收费项目类型 itemtype
	private String itemname;// 收费项目名称 itemname
	private String projectphase;// 项目阶段 projectphase
	private String properties;// 项目属性 properties
	private String paymenttype;// 款项类型 paymenttype
	private String propertycode;// 房产编码 propertycode
	private String propertyname;// 房产名称 propertyname
	private String subordinateyear;// 应收单所属年月 subordinateyear
	private String arrears;// 是否往年欠费 arrears
	private String rowid;// 物业收费系统单据行ID rowid
	private String subordiperiod;// 应收归属期 subordiperiod
	private String actualnateyear;// 实收所属年月-入账 actualnateyear
	private String actualperiod;// 实收归属期 actualperiod

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_customer() {
		return pk_customer;
	}

	public void setPk_customer(String pk_customer) {
		this.pk_customer = pk_customer;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getAccnum() {
		return accnum;
	}

	public void setAccnum(String accnum) {
		this.accnum = accnum;
	}

//	public String getPk_account() {
//		return pk_account;
//	}
//
//	public void setPk_account(String pk_account) {
//		this.pk_account = pk_account;
//	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getLocal_tax_cr() {
		return local_tax_cr;
	}

	public void setLocal_tax_cr(String local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}

	public String getNotax_cr() {
		return notax_cr;
	}

	public void setNotax_cr(String notax_cr) {
		this.notax_cr = notax_cr;
	}

	public String getRec_local() {
		return rec_local;
	}

	public void setRec_local(String rec_local) {
		this.rec_local = rec_local;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
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

	public String getSubordinateyear() {
		return subordinateyear;
	}

	public void setSubordinateyear(String subordinateyear) {
		this.subordinateyear = subordinateyear;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
	}

	public String getSubordiperiod() {
		return subordiperiod;
	}

	public void setSubordiperiod(String subordiperiod) {
		this.subordiperiod = subordiperiod;
	}

	public String getActualnateyear() {
		return actualnateyear;
	}

	public void setActualnateyear(String actualnateyear) {
		this.actualnateyear = actualnateyear;
	}

	public String getActualperiod() {
		return actualperiod;
	}

	public void setActualperiod(String actualperiod) {
		this.actualperiod = actualperiod;
	}

}
