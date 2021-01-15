package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 水电自动划扣发票明细（成本报表）
 * 
 * @author ASUS
 * 
 */
public class HydropowerDeductionInvVO implements Serializable, Cloneable {

	private UFDouble sortnumglobal;//序号
	private String area;//区域公司
	private String contractcode;//合同编号
	private String contractname;//合同名称
	private String supplier;//供应商名称
	private String payreqnum;//付款申请单号
	private String taxproperties;//计税性质
	private String cyear;//凭证年
	private String cmonth;//凭证月
	private String cday;//凭证日
	private String pk_org;//公司pk
	private String orgname;//公司名称
	private String accchart_name;//科目名称
	private String auxiliary_accounting_name;//辅助核算名称
	private String vouchernum;//凭证号
	private String memo;//摘要
	private String other_accchart;//对方科目
	private UFDouble pay_amount;//付款金额
	private UFDouble reinv_amount;//收回发票金额
	private UFDouble lostinv_amount;//欠票金额
	private String cost_type;//费用类型
	private String invtype;//专票/普票
	private String special_ticket_ria;//专票税率
	private UFDouble pricenotaxtotalamount;//不含税金额
	private UFDouble settlementamount;//税额
	
	
	public String getVouchernum() {
		return vouchernum;
	}
	public void setVouchernum(String vouchernum) {
		this.vouchernum = vouchernum;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPayreqnum() {
		return payreqnum;
	}
	public void setPayreqnum(String payreqnum) {
		this.payreqnum = payreqnum;
	}
	public UFDouble getSortnumglobal() {
		return sortnumglobal;
	}
	public void setSortnumglobal(UFDouble sortnumglobal) {
		this.sortnumglobal = sortnumglobal;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
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
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getTaxproperties() {
		return taxproperties;
	}
	public void setTaxproperties(String taxproperties) {
		this.taxproperties = taxproperties;
	}
	public String getCyear() {
		return cyear;
	}
	public void setCyear(String cyear) {
		this.cyear = cyear;
	}
	public String getCmonth() {
		return cmonth;
	}
	public void setCmonth(String cmonth) {
		this.cmonth = cmonth;
	}
	public String getCday() {
		return cday;
	}
	public void setCday(String cday) {
		this.cday = cday;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getAccchart_name() {
		return accchart_name;
	}
	public void setAccchart_name(String accchart_name) {
		this.accchart_name = accchart_name;
	}
	public String getAuxiliary_accounting_name() {
		return auxiliary_accounting_name;
	}
	public void setAuxiliary_accounting_name(String auxiliary_accounting_name) {
		this.auxiliary_accounting_name = auxiliary_accounting_name;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getOther_accchart() {
		return other_accchart;
	}
	public void setOther_accchart(String other_accchart) {
		this.other_accchart = other_accchart;
	}
	public UFDouble getPay_amount() {
		return pay_amount;
	}
	public void setPay_amount(UFDouble pay_amount) {
		this.pay_amount = pay_amount;
	}
	public UFDouble getReinv_amount() {
		return reinv_amount;
	}
	public void setReinv_amount(UFDouble reinv_amount) {
		this.reinv_amount = reinv_amount;
	}
	public UFDouble getLostinv_amount() {
		return lostinv_amount;
	}
	public void setLostinv_amount(UFDouble lostinv_amount) {
		this.lostinv_amount = lostinv_amount;
	}
	public String getCost_type() {
		return cost_type;
	}
	public void setCost_type(String cost_type) {
		this.cost_type = cost_type;
	}
	public String getInvtype() {
		return invtype;
	}
	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}
	public String getSpecial_ticket_ria() {
		return special_ticket_ria;
	}
	public void setSpecial_ticket_ria(String special_ticket_ria) {
		this.special_ticket_ria = special_ticket_ria;
	}
	public UFDouble getPricenotaxtotalamount() {
		return pricenotaxtotalamount;
	}
	public void setPricenotaxtotalamount(UFDouble pricenotaxtotalamount) {
		this.pricenotaxtotalamount = pricenotaxtotalamount;
	}
	public UFDouble getSettlementamount() {
		return settlementamount;
	}
	public void setSettlementamount(UFDouble settlementamount) {
		this.settlementamount = settlementamount;
	}
	
}
