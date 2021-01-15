package nc.vo.tg.outside.appaybill;

import java.io.Serializable;

public class PayBillItemVO implements Serializable {
	String supplier;// 实际收款方 supplier
	String subjcode;// 会计科目 subjcode
	String inoutbusiclass;// 收支项目 pk_subjcode
	String taxrate;// 税率 taxrate
	String local_money_cr;// 价税合计/供应商开票（含税） local_money_cr
	String local_notax_cr;// 无税金额/供应商开票（不含税） local_notax_cr
	String local_tax_cr;// 税额 /供应商开票（税额）
	String checktype;// 票据类型 checktype
	String budgetsub;// 预算科目 def1
	String paymenttype;// 款项类型 def2
	String format;// 业态 def3
	String formatratio;// 比例 def4
	String recaccount;// 收款方开户行行号 def6
	String costsubject;// 成本科目 def7
	String invtype;// 票据类型 def8
	String scomment;//摘要
	String  invoiceno;//发票号
	String offsetcompany;//抵冲出账公司
	

	// SRM对账
	
	String mny_accadj;// 对账调整（含税） def10
	String mny_notax_accadj;// 对账调整（不含税） def11
	String tax_accadj;// 对账调整（税额） def12
	String mny_scm;// 供应链金额（含税） def13
	String mny_notax_scm;// 供应链金额（不含税） def14
	String tax_scm;// 供应链金额（税额） def15
	String mny_evaluatecost;// 暂估成本 def16
	String mny_cost;// 成本 def20
	String accbodyid;// 后台接收id def21
	String evaluatecostbillno;// nc暂估工单编号 def22
	String accbillno;//对账单号
	String deduction;//扣款金额
	String appliedamount;//申请金额
	String purordercode;// 采购订单编码 def26
	String proejctdata;// 项目 def27
	String invposted;// 发票已入账标记 def22
	String arrimoney;// 到货接收金额（不含税） def23
	
	
	
	public String getPurordercode() {
		return purordercode;
	}

	public void setPurordercode(String purordercode) {
		this.purordercode = purordercode;
	}

	public String getProejctdata() {
		return proejctdata;
	}

	public void setProejctdata(String proejctdata) {
		this.proejctdata = proejctdata;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getAppliedamount() {
		return appliedamount;
	}

	public void setAppliedamount(String appliedamount) {
		this.appliedamount = appliedamount;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getOffsetcompany() {
		return offsetcompany;
	}

	public void setOffsetcompany(String offsetcompany) {
		this.offsetcompany = offsetcompany;
	}

	public String getInvtype() {
		return invtype;
	}

	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}

	public String getLocal_tax_cr() {
		return local_tax_cr;
	}

	public void setLocal_tax_cr(String local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getInoutbusiclass() {
		return inoutbusiclass;
	}

	public void setInoutbusiclass(String inoutbusiclass) {
		this.inoutbusiclass = inoutbusiclass;
	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getLocal_money_cr() {
		return local_money_cr;
	}

	public void setLocal_money_cr(String local_money_cr) {
		this.local_money_cr = local_money_cr;
	}

	public String getChecktype() {
		return checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormatratio() {
		return formatratio;
	}

	public void setFormatratio(String formatratio) {
		this.formatratio = formatratio;
	}

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getCostsubject() {
		return costsubject;
	}

	public void setCostsubject(String costsubject) {
		this.costsubject = costsubject;
	}

	public String getBudgetsub() {
		return budgetsub;
	}

	public void setBudgetsub(String budgetsub) {
		this.budgetsub = budgetsub;
	}



	public String getLocal_notax_cr() {
		return local_notax_cr;
	}

	public void setLocal_notax_cr(String local_notax_cr) {
		this.local_notax_cr = local_notax_cr;
	}

	public String getMny_accadj() {
		return mny_accadj;
	}

	public void setMny_accadj(String mny_accadj) {
		this.mny_accadj = mny_accadj;
	}

	public String getMny_notax_accadj() {
		return mny_notax_accadj;
	}

	public void setMny_notax_accadj(String mny_notax_accadj) {
		this.mny_notax_accadj = mny_notax_accadj;
	}

	public String getTax_accadj() {
		return tax_accadj;
	}

	public void setTax_accadj(String tax_accadj) {
		this.tax_accadj = tax_accadj;
	}

	public String getMny_scm() {
		return mny_scm;
	}

	public void setMny_scm(String mny_scm) {
		this.mny_scm = mny_scm;
	}

	public String getMny_notax_scm() {
		return mny_notax_scm;
	}

	public void setMny_notax_scm(String mny_notax_scm) {
		this.mny_notax_scm = mny_notax_scm;
	}

	public String getTax_scm() {
		return tax_scm;
	}

	public void setTax_scm(String tax_scm) {
		this.tax_scm = tax_scm;
	}

	public String getMny_evaluatecost() {
		return mny_evaluatecost;
	}

	public void setMny_evaluatecost(String mny_evaluatecost) {
		this.mny_evaluatecost = mny_evaluatecost;
	}

	public String getMny_cost() {
		return mny_cost;
	}

	public void setMny_cost(String mny_cost) {
		this.mny_cost = mny_cost;
	}

	public String getAccbodyid() {
		return accbodyid;
	}

	public void setAccbodyid(String accbodyid) {
		this.accbodyid = accbodyid;
	}

	public String getEvaluatecostbillno() {
		return evaluatecostbillno;
	}

	public void setEvaluatecostbillno(String evaluatecostbillno) {
		this.evaluatecostbillno = evaluatecostbillno;
	}

	public String getAccbillno() {
		return accbillno;
	}

	public void setAccbillno(String accbillno) {
		this.accbillno = accbillno;
	}

	public String getInvposted() {
		return invposted;
	}

	public void setInvposted(String invposted) {
		this.invposted = invposted;
	}

	public String getArrimoney() {
		return arrimoney;
	}

	public void setArrimoney(String arrimoney) {
		this.arrimoney = arrimoney;
	}
	
	

}
