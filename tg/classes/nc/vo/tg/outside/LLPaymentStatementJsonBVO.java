package nc.vo.tg.outside;

import java.io.Serializable;

public class LLPaymentStatementJsonBVO implements Serializable {

	private String memo;// ժҪ
	private String pk_supplier;// ��Ӧ��
	private String pk_balatype;// ���㷽ʽ
	private String pk_oppaccount;// ���λ�����˻�
	private String bd_bankdoc;// ���λ������
	private String pay_local;// ���
	private String deposit;// ��Ѻ����
	private String accounttype;// �տ��˻�����
	private String pk_account;// �տ������˺�
	private String accountname;// �տ��˻�����
	private String accountopenbank;// �տ���������
	private String rowid;// ��ҵ�շ�ϵͳ������ID
	private String project;// ��Ŀ
	private String arrears;// ���Ƿ�����Ƿ�ѡ�
	private String businessbreakdown;// ҵ��ϸ��
	private String itemtype;// �շ���Ŀ����
	private String itemname;// �շ���Ŀ����
	private String projectphase;// ��Ŀ�׶�
	private String properties;// ��Ŀ����
	private String paymenttype;// ��������
	private String subordinateyear;// Ӧ�յ���������
	private String supplierclassification;// ��Ӧ�̷���
	private String propertycode;// ��������
	private String propertyname;// ��������

	public String getSupplierclassification() {
		return supplierclassification;
	}

	public void setSupplierclassification(String supplierclassification) {
		this.supplierclassification = supplierclassification;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPk_supplier() {
		return pk_supplier;
	}

	public void setPk_supplier(String pk_supplier) {
		this.pk_supplier = pk_supplier;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	public String getPk_oppaccount() {
		return pk_oppaccount;
	}

	public void setPk_oppaccount(String pk_oppaccount) {
		this.pk_oppaccount = pk_oppaccount;
	}

	public String getPay_local() {
		return pay_local;
	}

	public void setPay_local(String pay_local) {
		this.pay_local = pay_local;
	}

	public String getDeposit() {
		return deposit;
	}

	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
	}

	public String getPk_account() {
		return pk_account;
	}

	public void setPk_account(String pk_account) {
		this.pk_account = pk_account;
	}

	public String getAccountname() {
		return accountname;
	}

	public void setAccountname(String accountname) {
		this.accountname = accountname;
	}

	public String getAccountopenbank() {
		return accountopenbank;
	}

	public void setAccountopenbank(String accountopenbank) {
		this.accountopenbank = accountopenbank;
	}

	public String getRowid() {
		return rowid;
	}

	public void setRowid(String rowid) {
		this.rowid = rowid;
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

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getProjectphase() {
		return projectphase;
	}

	public void setProjectphase(String projectphase) {
		this.projectphase = projectphase;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
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

	public String getBd_bankdoc() {
		return bd_bankdoc;
	}

	public void setBd_bankdoc(String bd_bankdoc) {
		this.bd_bankdoc = bd_bankdoc;
	}

}
