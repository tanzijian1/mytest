package nc.vo.tg.outside.incomebill;

import java.io.Serializable;

public class IncomeBillBodyVO implements Serializable {
	String scomment ; //摘要scomment
	String supplier ; //供应商supplier
	String quantity_de ; //数量quantity_de
	String taxprice ; //发票含税单价taxprice
	String taxrate ; //发票税率taxrate
	String notax_de ; //发票不含税金额notax_de
	String local_tax_de ; //发票税额local_tax_de
	String money_de ; //发票价税合计money_de
	String contractno ; //供应合同编号def1
	String project ; //项目def2
	String projectphase ; //项目分期def3
	String finbilltype ; //财务票据类型def4
	String originalrate ; //原税率def5
	String originaltax ; //原税额def6
	String originalfreetax ; //原不含税金额def7
	String originalsumtax ; //原价税合计def8
	public String getScomment() {
		return scomment;
	}
	public void setScomment(String scomment) {
		this.scomment = scomment;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getQuantity_de() {
		return quantity_de;
	}
	public void setQuantity_de(String quantity_de) {
		this.quantity_de = quantity_de;
	}
	public String getTaxprice() {
		return taxprice;
	}
	public void setTaxprice(String taxprice) {
		this.taxprice = taxprice;
	}
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	public String getNotax_de() {
		return notax_de;
	}
	public void setNotax_de(String notax_de) {
		this.notax_de = notax_de;
	}
	public String getLocal_tax_de() {
		return local_tax_de;
	}
	public void setLocal_tax_de(String local_tax_de) {
		this.local_tax_de = local_tax_de;
	}
	public String getMoney_de() {
		return money_de;
	}
	public void setMoney_de(String money_de) {
		this.money_de = money_de;
	}
	public String getContractno() {
		return contractno;
	}
	public void setContractno(String contractno) {
		this.contractno = contractno;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getProjectphase() {
		return projectphase;
	}
	public void setProjectphase(String projectphase) {
		this.projectphase = projectphase;
	}
	public String getFinbilltype() {
		return finbilltype;
	}
	public void setFinbilltype(String finbilltype) {
		this.finbilltype = finbilltype;
	}
	public String getOriginalrate() {
		return originalrate;
	}
	public void setOriginalrate(String originalrate) {
		this.originalrate = originalrate;
	}
	public String getOriginaltax() {
		return originaltax;
	}
	public void setOriginaltax(String originaltax) {
		this.originaltax = originaltax;
	}
	public String getOriginalfreetax() {
		return originalfreetax;
	}
	public void setOriginalfreetax(String originalfreetax) {
		this.originalfreetax = originalfreetax;
	}
	public String getOriginalsumtax() {
		return originalsumtax;
	}
	public void setOriginalsumtax(String originalsumtax) {
		this.originalsumtax = originalsumtax;
	}

}
