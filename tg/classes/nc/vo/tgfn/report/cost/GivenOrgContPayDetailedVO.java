package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 东莞裕景合同付款明细（成本报表）
 * 
 * @author ASUS
 * 
 */
public class GivenOrgContPayDetailedVO implements Serializable, Cloneable {

	private String pk_org;//核算公司
	private String contracttype;//类型
	private String contractcode;//合同编号
	private String contractname;//合同名称
	private String supplier;//供应商名称
	private String payregistercode;//付款登记号码
	private UFDouble payamount;//付款金额
	private String paynum;//请款单号
	private String paytype;//支付方式
	private String stauts;//状态
	private UFDouble pricetaxtotalamount;//发票金额成本+税额（价税合计）
	private UFDouble pricenotaxtotalamount;//成本金额（不含税）
	private String settlementrat;//税率
	private UFDouble settlementamount;//税额
	private String infodate;//入账月份
	private String vouchernum;//凭证号
	private String trantype;//交易类型id
	private String memo;//备注
	
	private String projectname;//项目名称   //add by 黄冠华 SDYC-391 需求调整：合同付款明细（成本报表）添加项目字段 20200819
	
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getSettlementrat() {
		return settlementrat;
	}
	public void setSettlementrat(String settlementrat) {
		this.settlementrat = settlementrat;
	}
	public String getTrantype() {
		return trantype;
	}
	public void setTrantype(String trantype) {
		this.trantype = trantype;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getContracttype() {
		return contracttype;
	}
	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
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
	public String getPayregistercode() {
		return payregistercode;
	}
	public void setPayregistercode(String payregistercode) {
		this.payregistercode = payregistercode;
	}
	public UFDouble getPayamount() {
		return payamount;
	}
	public void setPayamount(UFDouble payamount) {
		this.payamount = payamount;
	}
	public String getPaynum() {
		return paynum;
	}
	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getStauts() {
		return stauts;
	}
	public void setStauts(String stauts) {
		this.stauts = stauts;
	}
	public UFDouble getPricetaxtotalamount() {
		return pricetaxtotalamount;
	}
	public void setPricetaxtotalamount(UFDouble pricetaxtotalamount) {
		this.pricetaxtotalamount = pricetaxtotalamount;
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
	public String getInfodate() {
		return infodate;
	}
	public void setInfodate(String infodate) {
		this.infodate = infodate;
	}
	public String getVouchernum() {
		return vouchernum;
	}
	public void setVouchernum(String vouchernum) {
		this.vouchernum = vouchernum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}
