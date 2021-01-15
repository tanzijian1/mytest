package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArExecJsonVO;

public class LLCtArExecJsonVO extends CtArExecJsonVO {
	// 2020-09-28-tzj
	private java.lang.String advancemoney; // 预收金额
	private java.lang.String invoiceamountincludingtax; // 累计已开发票含税金额
	private java.lang.String invoiceamountexcludingtax; // 累计已开发票不含税金额

	public java.lang.String getAdvancemoney() {
		return advancemoney;
	}

	public void setAdvancemoney(java.lang.String advancemoney) {
		this.advancemoney = advancemoney;
	}

	public java.lang.String getInvoiceamountincludingtax() {
		return invoiceamountincludingtax;
	}

	public void setInvoiceamountincludingtax(
			java.lang.String invoiceamountincludingtax) {
		this.invoiceamountincludingtax = invoiceamountincludingtax;
	}

	public java.lang.String getInvoiceamountexcludingtax() {
		return invoiceamountexcludingtax;
	}

	public void setInvoiceamountexcludingtax(
			java.lang.String invoiceamountexcludingtax) {
		this.invoiceamountexcludingtax = invoiceamountexcludingtax;
	}

}
