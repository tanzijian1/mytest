package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 融资机构放款统计VO,用于年度融资放款报表转换信息
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 上午11:06:14
 */
public class FinOrganizationLoanTotalVO implements Serializable, Cloneable {
	String cyear;//年度
	String pk_organization;// 融资机构
	String organizationecode;// 融资机构编码
	String organizationname;// 融资机构名称
	UFDouble amount;// 年度放款金额

	
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
