package nc.vo.tg.outside;

import java.io.Serializable;

/**
 * ��ϵͳ�տ����
 * 
 * @author ̸�ӽ�
 * 
 */
public class GatheringBodyVO implements Serializable {
	private String scomment; // ժҪ
	private String customer; // �ͻ�
	private String taxrate; // ˰��
	private String local_tax_cr; // ˰��
	private String notax_cr; // ԭ����˰���
	private String money_cr; // ԭ�ҽ��
	private String money_bal; // ԭ�����
	private String recaccount; // �տ������˻�
	private String recaccountname;// �տ������˻�����
	private String payaccount; // ���������˻�
	private String contractno; // ��ͬ��
	private String invoiceno; // ��Ʊ��
	private String taxprice;// ��˰����
	private String pk_balatype;// ���㷽ʽ
	private String rowid; // ��ҵ�շ�ϵͳ������ID
	private String contractname; // ��ͬ����
	private String productline;// ��Ʒ��
	private String memo;// ��ע
	private String itemcode; // �շ���Ŀ����
	private String projectphase;// ��Ŀ�׶�
	private String project;// ��Ŀ
	private String arrears;// �Ƿ�����Ƿ��
	private String businessbreakdown;// ҵ��ϸ��
	private String projectproperties;// ��Ŀ���� projectproperties
	private String pk_custclass;// �ͻ����� pk_custclass
	private String propertycode;// �������� propertycode
	private String propertyname;// �������� propertyname
	private String paymenttype;// �������� paymenttype
	private String subordinateyear;// Ӧ�յ��������� subordinateyear
	private String pk_recpaytype;// �տ�ҵ������ pk_recpaytype
	private String cashaccount;// �ֽ��˻� cashaccount
	private String bankofdeposit;// �������к�
	private String actualnateyear; // ʵ����������-����
	private String subordiperiod; // Ӧ�չ�����
	private String actualperiod; // ʵ�չ�����

	public String getRecaccountname() {
		return recaccountname;
	}

	public void setRecaccountname(String recaccountname) {
		this.recaccountname = recaccountname;
	}

	public String getActualnateyear() {
		return actualnateyear;
	}

	public void setActualnateyear(String actualnateyear) {
		this.actualnateyear = actualnateyear;
	}

	public String getSubordiperiod() {
		return subordiperiod;
	}

	public void setSubordiperiod(String subordiperiod) {
		this.subordiperiod = subordiperiod;
	}

	public String getActualperiod() {
		return actualperiod;
	}

	public void setActualperiod(String actualperiod) {
		this.actualperiod = actualperiod;
	}

	public String getBankofdeposit() {
		return bankofdeposit;
	}

	public void setBankofdeposit(String bankofdeposit) {
		this.bankofdeposit = bankofdeposit;
	}

	public String getProjectproperties() {
		return projectproperties;
	}

	public void setProjectproperties(String projectproperties) {
		this.projectproperties = projectproperties;
	}

	public String getPk_custclass() {
		return pk_custclass;
	}

	public void setPk_custclass(String pk_custclass) {
		this.pk_custclass = pk_custclass;
	}

	public String getPropertycode() {
		return propertycode;
	}

	public void setPropertycode(String propertycode) {
		this.propertycode = propertycode;
	}

	public String getPropertyname() {
		return propertyname;
	}

	public void setPropertyname(String propertyname) {
		this.propertyname = propertyname;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getSubordinateyear() {
		return subordinateyear;
	}

	public void setSubordinateyear(String subordinateyear) {
		this.subordinateyear = subordinateyear;
	}

	public String getPk_recpaytype() {
		return pk_recpaytype;
	}

	public void setPk_recpaytype(String pk_recpaytype) {
		this.pk_recpaytype = pk_recpaytype;
	}

	public String getCashaccount() {
		return cashaccount;
	}

	public void setCashaccount(String cashaccount) {
		this.cashaccount = cashaccount;
	}

	public String getArrears() {
		return arrears;
	}

	public void setArrears(String arrears) {
		this.arrears = arrears;
	}

	public String getBusinessbreakdown() {
		return businessbreakdown;
	}

	public void setBusinessbreakdown(String businessbreakdown) {
		this.businessbreakdown = businessbreakdown;
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

	public String getItemcode() {
		return itemcode;
	}

	public void setItemcode(String itemcode) {
		this.itemcode = itemcode;
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

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getTaxprice() {
		return taxprice;
	}

	public void setTaxprice(String taxprice) {
		this.taxprice = taxprice;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getLocal_tax_cr() {
		return local_tax_cr;
	}

	public void setLocal_tax_cr(String local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
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

	public String getMoney_bal() {
		return money_bal;
	}

	public void setMoney_bal(String money_bal) {
		this.money_bal = money_bal;
	}

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
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

}
