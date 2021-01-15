package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * 外系统付款申请单表体（成本）
 * 
 * @author lyq
 * 
 */
public class CostApplyBillBodyVO implements Serializable {
	private String costsubjects;// 成本科目
	private String subjcode;// 收支项目
	private String accountsubjcode;// 会计科目
	private String memo;// 摘要
	private String proceedtype;// 款项类型
	private String businessformat;// 业态
	private String scale;// 比例
	private String notetype;// 票据类型
	private String noteno;// 票据号
	// private String objtype;// 往来对象
	private String supplier;// 实际收款方
	// private String realitybankraccount;// 实际收款方银行账户
	private String bankraccountcode;// 收款方开户行行号
	// private String bankaccount;// 收款方账户
	private String payamount;// 实际付款金额
	private String balatype;// 结算方式
	private String payaccount;// 付款银行账户
	private String recaccount;// 收款银行账户
	private String castaccount;// 现金账户
	private String def48;// 水电费金额
	private String def49;// 罚款金额
	private String def38;// EBS行ID-def38
	private String def40;// EBS表名-def40
	private String objtype;// 往来对象
	private String psndoc;// 业务员
	

	public String getObjtype() {
		return objtype;
	}

	public void setObjtype(String objtype) {
		this.objtype = objtype;
	}

	public String getPsndoc() {
		return psndoc;
	}

	public void setPsndoc(String psndoc) {
		this.psndoc = psndoc;
	}

	public String getDef38() {
		return def38;
	}

	public void setDef38(String def38) {
		this.def38 = def38;
	}

	public String getDef40() {
		return def40;
	}

	public void setDef40(String def40) {
		this.def40 = def40;
	}

	public String getDef48() {
		return def48;
	}

	public void setDef48(String def48) {
		this.def48 = def48;
	}

	public String getDef49() {
		return def49;
	}

	public void setDef49(String def49) {
		this.def49 = def49;
	}

	public String getNotetype() {
		return notetype;
	}

	public void setNotetype(String notetype) {
		this.notetype = notetype;
	}

	public String getNoteno() {
		return noteno;
	}

	public void setNoteno(String noteno) {
		this.noteno = noteno;
	}

	public String getAccountsubjcode() {
		return accountsubjcode;
	}

	public void setAccountsubjcode(String accountsubjcode) {
		this.accountsubjcode = accountsubjcode;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getBankraccountcode() {
		return bankraccountcode;
	}

	public void setBankraccountcode(String bankraccountcode) {
		this.bankraccountcode = bankraccountcode;
	}

	public String getProceedtype() {
		return proceedtype;
	}

	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}

	public String getCostsubjects() {
		return costsubjects;
	}

	public void setCostsubjects(String costsubjects) {
		this.costsubjects = costsubjects;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
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

	public String getCastaccount() {
		return castaccount;
	}

	public void setCastaccount(String castaccount) {
		this.castaccount = castaccount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

}
