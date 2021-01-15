package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * �����ڻ�������ʹ�����
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����11:44:49
 */
public class OrganizationApprovalTotalVO implements Serializable, Cloneable {
	String pk_organization;// ���ʻ���
	String organizationcode;// ��������
	String organizationname;// ��������
	UFDouble total_amount;// �����ܶ�
	UFDouble use_amount;// ����ʹ�ö��
	UFDouble use_ratio;// ����ʹ��ռ��
	UFDouble notuse_amount;// δʹ�����Ŷ��
	UFDouble nonwithdrawal_amount;// ������ĩ�����
	String replydate;// ����������
	String expiredate;// ���ŵ�����

	public String getPk_organization() {
		return pk_organization;
	}

	public void setPk_organization(String pk_organization) {
		this.pk_organization = pk_organization;
	}

	public String getOrganizationcode() {
		return organizationcode;
	}

	public void setOrganizationcode(String organizationcode) {
		this.organizationcode = organizationcode;
	}

	public String getOrganizationname() {
		return organizationname;
	}

	public void setOrganizationname(String organizationname) {
		this.organizationname = organizationname;
	}

	public UFDouble getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(UFDouble total_amount) {
		this.total_amount = total_amount;
	}

	public UFDouble getUse_amount() {
		return use_amount;
	}

	public void setUse_amount(UFDouble use_amount) {
		this.use_amount = use_amount;
	}

	public UFDouble getUse_ratio() {
		return use_ratio;
	}

	public void setUse_ratio(UFDouble use_ratio) {
		this.use_ratio = use_ratio;
	}

	public UFDouble getNotuse_amount() {
		return notuse_amount;
	}

	public void setNotuse_amount(UFDouble notuse_amount) {
		this.notuse_amount = notuse_amount;
	}

	public UFDouble getNonwithdrawal_amount() {
		return nonwithdrawal_amount;
	}

	public void setNonwithdrawal_amount(UFDouble nonwithdrawal_amount) {
		this.nonwithdrawal_amount = nonwithdrawal_amount;
	}

	public String getReplydate() {
		return replydate;
	}

	public void setReplydate(String replydate) {
		this.replydate = replydate;
	}

	public String getExpiredate() {
		return expiredate;
	}

	public void setExpiredate(String expiredate) {
		this.expiredate = expiredate;
	}

}
