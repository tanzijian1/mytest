package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 融资机构放款统计VO,用于年度融资放款报表转换信息
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 上午11:06:14
 */
public class FinOrganizationTypeLoanTotalVO implements Serializable, Cloneable {
	String pk_organizationtype;// 融资机构
	String organizationetypecode;// 融资机构编码
	String organizationtypename;// 融资机构名称
	String fintypecode;// 融资机构类别编码
	String fintypename;// 融资机构类别名称
	UFDouble amount;// 年度放款金额
	
	

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
