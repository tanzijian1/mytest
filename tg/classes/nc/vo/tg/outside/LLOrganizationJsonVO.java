package nc.vo.tg.outside;

import java.io.Serializable;

public class LLOrganizationJsonVO implements Serializable {
	private java.lang.String pk_organization;// ����
	private java.lang.String postname;// ��λ����
	private java.lang.String univalence;// ����
	private java.lang.String people;// ����
	private java.lang.String billingperiod;// �Ʒ�����
	private java.lang.String billingamount;// �Ʒѽ��

	public java.lang.String getPk_organization() {
		return pk_organization;
	}

	public void setPk_organization(java.lang.String pk_organization) {
		this.pk_organization = pk_organization;
	}

	public java.lang.String getPostname() {
		return postname;
	}

	public void setPostname(java.lang.String postname) {
		this.postname = postname;
	}

	public java.lang.String getPeople() {
		return people;
	}

	public void setPeople(java.lang.String people) {
		this.people = people;
	}

	public java.lang.String getBillingperiod() {
		return billingperiod;
	}

	public void setBillingperiod(java.lang.String billingperiod) {
		this.billingperiod = billingperiod;
	}

	public java.lang.String getUnivalence() {
		return univalence;
	}

	public void setUnivalence(java.lang.String univalence) {
		this.univalence = univalence;
	}

	public java.lang.String getBillingamount() {
		return billingamount;
	}

	public void setBillingamount(java.lang.String billingamount) {
		this.billingamount = billingamount;
	}

}
