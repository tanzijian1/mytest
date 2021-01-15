package nc.vo.tg.apply;

import java.io.Serializable;

public class ApplyBillBodyVO implements Serializable {
	private String budgetsubjects;// 预算科目
	private String subjcode;// 收支项目
	private String memo;// 摘要
	private String proceedtype;// 款项类型
	private String businessformat;// 业态
	private String scale;// 比例
	private String objtype;// 往来对象
	private String supplier;// 供应商
	private String realitybankraccount;// 实际收款方银行账户
	private String bankraccountcode;// 收款方开户行行号
	private String bankaccount;// 收款方账户
	private String payamount;// 申请金额
	private String payaccount;// 付款银行账户
	private String cashaccount;// 现金账户
	private String offsetorg;// 抵冲出账公司
	private String checkno;// 票据号
	private String checktype;// 票据类型
	private String payoffaccomunt;// 解付账户
	private String payoffamount;// 抵冲/解付金额
	private String operationmode;// 操作方式
	private String def51;// 商票开户行号
	private String def52;// 是否共管账户
	private String psndoc;// 业务员

	public String getPsndoc() {
		return psndoc;
	}

	public void setPsndoc(String psndoc) {
		this.psndoc = psndoc;
	}

	public String getDef51() {
		return def51;
	}

	public void setDef51(String def51) {
		this.def51 = def51;
	}

	public String getDef52() {
		return def52;
	}

	public void setDef52(String def52) {
		this.def52 = def52;
	}

	public String getPayoffaccomunt() {
		return payoffaccomunt;
	}

	public void setPayoffaccomunt(String payoffaccomunt) {
		this.payoffaccomunt = payoffaccomunt;
	}

	public String getPayoffamount() {
		return payoffamount;
	}

	public void setPayoffamount(String payoffamount) {
		this.payoffamount = payoffamount;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}

	public String getCashaccount() {
		return cashaccount;
	}

	public void setCashaccount(String cashaccount) {
		this.cashaccount = cashaccount;
	}

	public String getOffsetorg() {
		return offsetorg;
	}

	public void setOffsetorg(String offsetorg) {
		this.offsetorg = offsetorg;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getChecktype() {
		return checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getObjtype() {
		return objtype;
	}

	public void setObjtype(String objtype) {
		this.objtype = objtype;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getBudgetsubjects() {
		return budgetsubjects;
	}

	public void setBudgetsubjects(String budgetsubjects) {
		this.budgetsubjects = budgetsubjects;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getProceedtype() {
		return proceedtype;
	}

	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}

	public String getBusinessformat() {
		return businessformat;
	}

	public void setBusinessformat(String businessformat) {
		this.businessformat = businessformat;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getRealitybankraccount() {
		return realitybankraccount;
	}

	public void setRealitybankraccount(String realitybankraccount) {
		this.realitybankraccount = realitybankraccount;
	}

	public String getBankraccountcode() {
		return bankraccountcode;
	}

	public void setBankraccountcode(String bankraccountcode) {
		this.bankraccountcode = bankraccountcode;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getOperationmode() {
		return operationmode;
	}

	public void setOperationmode(String operationmode) {
		this.operationmode = operationmode;
	}

}
