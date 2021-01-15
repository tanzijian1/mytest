package nc.bs.tg.call.vo;

import java.io.Serializable;

/**
 * 综合商城应付单表体
 * @author zhaozhiying
 *
 */
public class PayableBodyVO implements Serializable{
	private String scomment; // 摘要
	private String quantity_cr; // 数量
	private String taxcodeid; // 税码
	private String taxrate; // 税率
	private String taxprice; // 含税单价
	private String price; // 单价
	private String local_notax_cr; // 组织本币无税金额
	private String local_tax_cr; // 税额
	private String local_money_cr; // 组织本币金额
	private String local_money_bal; // 组织本币余额
	private String contractno; // 合同号
	private String invoiceno; // 发票号
	private String rowid; // 物业收费系统单据行ID
	private String contractname; // 合同名称
	private String customer; // 客户
	private String money_bal;// 原币余额
	private String productline;// 产品线
	private String memo;// 备注
	private String pk_balatype;// 结算方式
	private String ratio;	//分账比例(综合商城)
	private String notax_cr;// 不含税金额
	private String money_cr;// 价税合计
	
	private String payaccount; //付款银行账户
	private String recaccount; //收款银行账户
	public String getScomment() {
		return scomment;
	}
	public void setScomment(String scomment) {
		this.scomment = scomment;
	}
	public String getQuantity_cr() {
		return quantity_cr;
	}
	public void setQuantity_cr(String quantity_cr) {
		this.quantity_cr = quantity_cr;
	}
	public String getTaxcodeid() {
		return taxcodeid;
	}
	public void setTaxcodeid(String taxcodeid) {
		this.taxcodeid = taxcodeid;
	}
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	public String getTaxprice() {
		return taxprice;
	}
	public void setTaxprice(String taxprice) {
		this.taxprice = taxprice;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getLocal_notax_cr() {
		return local_notax_cr;
	}
	public void setLocal_notax_cr(String local_notax_cr) {
		this.local_notax_cr = local_notax_cr;
	}
	public String getLocal_tax_cr() {
		return local_tax_cr;
	}
	public void setLocal_tax_cr(String local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}
	public String getLocal_money_cr() {
		return local_money_cr;
	}
	public void setLocal_money_cr(String local_money_cr) {
		this.local_money_cr = local_money_cr;
	}
	public String getLocal_money_bal() {
		return local_money_bal;
	}
	public void setLocal_money_bal(String local_money_bal) {
		this.local_money_bal = local_money_bal;
	}
	public String getContractno() {
		return contractno;
	}
	public void setContractno(String contractno) {
		this.contractno = contractno;
	}
	public String getInvoiceno() {
		return invoiceno;
	}
	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}
	public String getRowid() {
		return rowid;
	}
	public void setRowid(String rowid) {
		this.rowid = rowid;
	}
	public String getContractname() {
		return contractname;
	}
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getMoney_bal() {
		return money_bal;
	}
	public void setMoney_bal(String money_bal) {
		this.money_bal = money_bal;
	}
	public String getProductline() {
		return productline;
	}
	public void setProductline(String productline) {
		this.productline = productline;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPk_balatype() {
		return pk_balatype;
	}
	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}
	public String getRatio() {
		return ratio;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getNotax_cr() {
		return notax_cr;
	}
	public void setNotax_cr(String notax_cr) {
		this.notax_cr = notax_cr;
	}
	public String getMoney_cr() {
		return money_cr;
	}
	public void setMoney_cr(String money_cr) {
		this.money_cr = money_cr;
	}
	public String getPayaccount() {
		return payaccount;
	}
	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}
	public String getRecaccount() {
		return recaccount;
	}
	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	
}
