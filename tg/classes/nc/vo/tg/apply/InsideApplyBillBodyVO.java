package nc.vo.tg.apply;

import java.io.Serializable;
/**
 *  外系统付款申请单表体（内部交易）
 * @author lyq
 *
 */
public class InsideApplyBillBodyVO implements Serializable {
	private String cusaccount;// 客商银行账户
	private String memo;// 摘要
	private String payaccount;// 付款账号
	private String balatype;// 结算方式
	private String notetype;// 票据类型
	private String payamount;//无税金额
	private String taxrate;// 税率
	private String taxamount;// 税额
	private String total;// 价税合计
	private String supplier;//实际收款方 
	private String proceedtype;//款项类型
	private String bidnumber;//标书编号
	private String bidtitle;//招标名称
	private String reaccountno;//收款方开户行行号
	private String recaccount;//收款方银行账户
	private String company;//抵楼所属公司
	public String getCusaccount() {
		return cusaccount;
	}
	public void setCusaccount(String cusaccount) {
		this.cusaccount = cusaccount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPayaccount() {
		return payaccount;
	}
	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}
	public String getBalatype() {
		return balatype;
	}
	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}
	public String getNotetype() {
		return notetype;
	}
	public void setNotetype(String notetype) {
		this.notetype = notetype;
	}
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getProceedtype() {
		return proceedtype;
	}
	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}
	public String getBidtitle() {
		return bidtitle;
	}
	public void setBidtitle(String bidtitle) {
		this.bidtitle = bidtitle;
	}
	public String getBidnumber() {
		return bidnumber;
	}
	public void setBidnumber(String bidnumber) {
		this.bidnumber = bidnumber;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(String taxamount) {
		this.taxamount = taxamount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getReaccountno() {
		return reaccountno;
	}
	public void setReaccountno(String reaccountno) {
		this.reaccountno = reaccountno;
	}
	public String getRecaccount() {
		return recaccount;
	}
	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	

}
