package nc.vo.tg.outside;

import java.io.Serializable;

/**
 * ��ϵͳӦ�յ�����
 * 
 * @author ̸�ӽ�
 * 
 */
public class ReceivableBodyVO implements Serializable {
	private String scomment; // ժҪ
	private String quantity_de; // ����
	private String taxcodeid; // ˰��
	private String taxrate; // ˰��
	private String taxprice; // ��˰����
	private String price; // ����
	private String local_notax_de; // ��֯������˰���
	private String local_tax_de; // ˰��
	private String local_money_de; // ��֯���ҽ��
	private String local_money_bal; // ��֯�������
	private String contractno; // ��ͬ��
	private String invoiceno; // ��Ʊ��
	private String rowid; // ��ҵ�շ�ϵͳ������ID
	private String contractname; // ��ͬ����
	private String customer; // �ͻ�
	private String money_bal;// ԭ�����
	private String productline;// ��Ʒ��
	private String memo;// ��ע
	private String pk_balatype;// ���㷽ʽ
	private String itemcode; // �շ���Ŀ����
	private String projectphase;// ��Ŀ�׶�
	private String supplier;// Ͷ�깩Ӧ�̱���
	private String payaccount;// ���������˻�
	private String recaccount;// �տ������˻�
	private String deposittype;// �б걣֤������
	private String project;// ��Ŀ
	private String arrears;// �Ƿ�����Ƿ��
	private String businessbreakdown;// ҵ��ϸ��
	private String ratio; // ���˱���

	private String projectproperties;// ��Ŀ���� projectproperties
	private String pk_custclass;// �ͻ����� pk_custclass
	private String propertycode;// �������� propertycode
	private String propertyname;// �������� propertyname
	private String paymenttype;// �������� paymenttype
	private String subordinateyear;// Ӧ�յ��������� subordinateyear
	private String budgetsub;// Ԥ���Ŀ
	private String subordiperiod; // Ӧ�չ�����

	public String getSubordiperiod() {
		return subordiperiod;
	}

	public void setSubordiperiod(String subordiperiod) {
		this.subordiperiod = subordiperiod;
	}

	public String getBudgetsub() {
		return budgetsub;
	}

	public void setBudgetsub(String budgetsub) {
		this.budgetsub = budgetsub;
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

	public String getSubordinateyear() {
		return subordinateyear;
	}

	public void setSubordinateyear(String subordinateyear) {
		this.subordinateyear = subordinateyear;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
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

	public String getDeposittype() {
		return deposittype;
	}

	public void setDeposittype(String deposittype) {
		this.deposittype = deposittype;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
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

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
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

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getProductline() {
		return productline;
	}

	public void setProductline(String productline) {
		this.productline = productline;
	}

	public String getMoney_bal() {
		return money_bal;
	}

	public void setMoney_bal(String money_bal) {
		this.money_bal = money_bal;
	}

	public float getNotax_de() {
		return notax_de;
	}

	public void setNotax_de(float notax_de) {
		this.notax_de = notax_de;
	}

	public float getMoney_de() {
		return money_de;
	}

	public void setMoney_de(float money_de) {
		this.money_de = money_de;
	}

	private float notax_de;// ����˰���
	private float money_de;// ��˰�ϼ�

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getQuantity_de() {
		return quantity_de;
	}

	public void setQuantity_de(String quantity_de) {
		this.quantity_de = quantity_de;
	}

	public String getTaxcodeid() {
		return taxcodeid;
	}

	public void setTaxcodeid(String taxcodeid) {
		this.taxcodeid = taxcodeid;
	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getTaxprice() {
		return taxprice;
	}

	public void setTaxprice(String taxprice) {
		this.taxprice = taxprice;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getLocal_notax_de() {
		return local_notax_de;
	}

	public void setLocal_notax_de(String local_notax_de) {
		this.local_notax_de = local_notax_de;
	}

	public String getLocal_tax_de() {
		return local_tax_de;
	}

	public void setLocal_tax_de(String local_tax_de) {
		this.local_tax_de = local_tax_de;
	}

	public String getLocal_money_de() {
		return local_money_de;
	}

	public void setLocal_money_de(String local_money_de) {
		this.local_money_de = local_money_de;
	}

	public String getLocal_money_bal() {
		return local_money_bal;
	}

	public void setLocal_money_bal(String local_money_bal) {
		this.local_money_bal = local_money_bal;
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

	public String getRatio() {
		return ratio;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

}
