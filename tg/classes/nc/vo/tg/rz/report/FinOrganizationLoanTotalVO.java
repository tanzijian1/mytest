package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ���ʻ����ſ�ͳ��VO,����������ʷſ��ת����Ϣ
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����11:06:14
 */
public class FinOrganizationLoanTotalVO implements Serializable, Cloneable {
	String cyear;//���
	String pk_organization;// ���ʻ���
	String organizationecode;// ���ʻ�������
	String organizationname;// ���ʻ�������
	UFDouble amount;// ��ȷſ���

	
	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}

	public String getPk_organization() {
		return pk_organization;
	}

	public void setPk_organization(String pk_organization) {
		this.pk_organization = pk_organization;
	}

	public String getOrganizationecode() {
		return organizationecode;
	}

	public void setOrganizationecode(String organizationecode) {
		this.organizationecode = organizationecode;
	}

	public String getOrganizationname() {
		return organizationname;
	}

	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}

	public UFDouble getAmount() {
		return amount;
	}

	public void setAmount(UFDouble amount) {
		this.amount = amount;
	}

}
