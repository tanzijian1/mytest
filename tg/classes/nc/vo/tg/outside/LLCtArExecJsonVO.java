package nc.vo.tg.outside;

import nc.vo.tg.ctar.CtArExecJsonVO;

public class LLCtArExecJsonVO extends CtArExecJsonVO {
	// 2020-09-28-tzj
	private java.lang.String advancemoney; // Ԥ�ս��
	private java.lang.String invoiceamountincludingtax; // �ۼ��ѿ���Ʊ��˰���
	private java.lang.String invoiceamountexcludingtax; // �ۼ��ѿ���Ʊ����˰���

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
