package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ���ʻ����ſ�ͳ��VO,����������ʷſ��ת����Ϣ
 * 
 * @author HUANGDQ
 * @date 2019��7��2�� ����11:06:14
 */
public class FinOrganizationTypeLoanTotalVO implements Serializable, Cloneable {
	String pk_organizationtype;// ���ʻ���
	String organizationetypecode;// ���ʻ�������
	String organizationtypename;// ���ʻ�������
	String fintypecode;// ���ʻ���������
	String fintypename;// ���ʻ����������
	UFDouble amount;// ��ȷſ���
	
	

	public String getFintypecode() {
		return fintypecode;
	}

	public void setFintypecode(String fintypecode) {
		this.fintypecode = fintypecode;
	}

	public String getFintypename() {
		return fintypename;
	}

	public void setFintypename(String fintypename) {
		this.fintypename = fintypename;
	}

	public String getPk_organizationtype() {
		return pk_organizationtype;
	}

	public void setPk_organizationtype(String pk_organizationtype) {
		this.pk_organizationtype = pk_organizationtype;
	}

	public String getOrganizationetypecode() {
		return organizationetypecode;
	}

	public void setOrganizationetypecode(String organizationetypecode) {
		this.organizationetypecode = organizationetypecode;
	}

	public String getOrganizationtypename() {
		return organizationtypename;
	}

	public void setOrganizationtypename(String organizationtypename) {
		this.organizationtypename = organizationtypename;
	}

	public UFDouble getAmount() {
		return amount;
	}

	public void setAmount(UFDouble amount) {
		this.amount = amount;
	}

}
