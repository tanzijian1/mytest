package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 各金融机构报批客商汇总表
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:44:49
 */
public class OrganizationCreditUsageVO implements Serializable, Cloneable {
	String pk_organization;// 融资机构
	String organizationcode;// 机构编码
	String organizationname;// 机构名称
	UFDouble total_amount;// 授信总额
	UFDouble use_amount;// 授信使用额度
	UFDouble approval_amount;// 正在报批金额
	String pk_project;// 项目ID
	String projectcode;// 项目编码
	String projectname;// 项目名称
	UFDouble fin_amount; // 拟融资金额(亿元)

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

	public UFDouble getApproval_amount() {
		return approval_amount;
	}

	public void setApproval_amount(UFDouble approval_amount) {
		this.approval_amount = approval_amount;
	}

	public String getPk_project() {
		return pk_project;
	}

	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

}
