package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 各金融机构授信使用情况
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:44:49
 */
public class OrganizationApprovalTotalVO implements Serializable, Cloneable {
	String pk_organization;// 融资机构
	String organizationcode;// 机构编码
	String organizationname;// 机构名称
	UFDouble total_amount;// 授信总额
	UFDouble use_amount;// 授信使用额度
	UFDouble use_ratio;// 授信使用占比
	UFDouble notuse_amount;// 未使用授信额度
	UFDouble nonwithdrawal_amount;// 已批复末提款金额
	String replydate;// 授信批复日
	String expiredate;// 授信到期日

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
