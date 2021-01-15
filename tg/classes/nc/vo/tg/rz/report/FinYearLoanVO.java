package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 年度融资放款VO,用于年度融资放款报表转换信息
 * 
 * @author HUANGDQ
 * @date 2019年7月2日 上午11:06:14
 */
public class FinYearLoanVO implements Serializable, Cloneable {
	String cyear ;//年度
	String pk_contract;//合同pk
	String contractcode;//合同编码
	String pk_sxqy;//区域公司
	String pk_project;// 项目ID
	String projectcode;// 项目编码
	String projectname;// 项目名称
	String pk_periodization;// 分期主键
	String periodizationname;// 分期名称
	String projectcorp;// 项目公司
	String borrower;// 借款人
	String pk_fintype;// 融资类型
	String fintypecode;// 融资类型编码
	String fintypename;// 融资类型名称
	String pk_organization;// 融资机构
	String organizationcode;// 机构编码
	String organizationname;// 机构名称
	String pk_organizationtype;// 融资机构类别
	String organizationtypecode;// 融资机构类别编码
	String organizationtypename;// 融资机构类别名称
	String branch;// 机构分行/分部
	String subbranch;// 机构支行
	String begindate;// 合同起始日
	String enddate; // 合同到期日
	UFDouble fin_amount;// 融资金额
	UFDouble loan_totalamount;// 累计放款金额
	UFDouble loan_lastyearamount;// 上一年放款金额
	UFDouble loan_thisyearamount;// 当年放款金额
	UFDouble loan_awaitamount;// 待放款金额
	String cont_lv;// 合同利率
	String multiple_lv;//综合利率
	String loan_datenote;// 放款时间(说明)
//	String month;// 月份[1月份放款~12月份放款]
//	String loan_amount;// 实际放款
//	String jan;//1月实际放款
//	String feb;//2月实际放款
//	String mar;//3月实际放款
//	String apr;//4月实际放款
//	String may;//5月实际放款
//	String jun;//6月实际放款
//	String jul;//7月实际放款
//	String aug;//8月实际放款
//	String sep;//9月实际放款
//	String oct;//10月实际放款
//	String nov;//11月实际放款
//	String dec;//12月实际放款
	String i_cwgwfl;//财务顾问费率
	
	UFDouble jan_amount;//1月实际放款
	UFDouble feb_amount;//2月实际放款
	UFDouble mar_amount;//3月实际放款
	UFDouble apr_amount;//4月实际放款
	UFDouble may_amount;//5月实际放款
	UFDouble jun_amount;//6月实际放款
	UFDouble jul_amount;//7月实际放款
	UFDouble aug_amount;//8月实际放款
	UFDouble sep_amount;//9月实际放款
	UFDouble oct_amount;//10月实际放款
	UFDouble nov_amount;//11月实际放款
	UFDouble dec_amount;//12月实际放款



	public String getMultiple_lv() {
		return multiple_lv;
	}


	public void setMultiple_lv(String multiple_lv) {
		this.multiple_lv = multiple_lv;
	}


	public String getI_cwgwfl() {
		return i_cwgwfl;
	}


	public UFDouble getJan_amount() {
		return jan_amount;
	}


	public void setJan_amount(UFDouble jan_amount) {
		this.jan_amount = jan_amount;
	}


	public UFDouble getFeb_amount() {
		return feb_amount;
	}


	public void setFeb_amount(UFDouble feb_amount) {
		this.feb_amount = feb_amount;
	}


	public UFDouble getMar_amount() {
		return mar_amount;
	}


	public void setMar_amount(UFDouble mar_amount) {
		this.mar_amount = mar_amount;
	}


	public UFDouble getApr_amount() {
		return apr_amount;
	}


	public void setApr_amount(UFDouble apr_amount) {
		this.apr_amount = apr_amount;
	}


	public UFDouble getMay_amount() {
		return may_amount;
	}


	public void setMay_amount(UFDouble may_amount) {
		this.may_amount = may_amount;
	}


	public UFDouble getJun_amount() {
		return jun_amount;
	}


	public void setJun_amount(UFDouble jun_amount) {
		this.jun_amount = jun_amount;
	}


	public UFDouble getJul_amount() {
		return jul_amount;
	}


	public void setJul_amount(UFDouble jul_amount) {
		this.jul_amount = jul_amount;
	}


	public UFDouble getAug_amount() {
		return aug_amount;
	}


	public void setAug_amount(UFDouble aug_amount) {
		this.aug_amount = aug_amount;
	}


	public UFDouble getSep_amount() {
		return sep_amount;
	}


	public void setSep_amount(UFDouble sep_amount) {
		this.sep_amount = sep_amount;
	}


	public UFDouble getOct_amount() {
		return oct_amount;
	}


	public void setOct_amount(UFDouble oct_amount) {
		this.oct_amount = oct_amount;
	}


	public UFDouble getNov_amount() {
		return nov_amount;
	}


	public void setNov_amount(UFDouble nov_amount) {
		this.nov_amount = nov_amount;
	}


	public UFDouble getDec_amount() {
		return dec_amount;
	}


	public void setDec_amount(UFDouble dec_amount) {
		this.dec_amount = dec_amount;
	}


	public void setI_cwgwfl(String i_cwgwfl) {
		this.i_cwgwfl = i_cwgwfl;
	}

	public String getPk_sxqy() {
		return pk_sxqy;
	}

	public void setPk_sxqy(String pk_sxqy) {
		this.pk_sxqy = pk_sxqy;
	}

	public String getCyear() {
		return cyear;
	}

	public void setCyear(String cyear) {
		this.cyear = cyear;
	}





	public String getPk_contract() {
		return pk_contract;
	}

	public void setPk_contract(String pk_contract) {
		this.pk_contract = pk_contract;
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

	public String getPk_periodization() {
		return pk_periodization;
	}

	public void setPk_periodization(String pk_periodization) {
		this.pk_periodization = pk_periodization;
	}

	public String getPeriodizationname() {
		return periodizationname;
	}

	public void setPeriodizationname(String periodizationname) {
		this.periodizationname = periodizationname;
	}

	public String getProjectcorp() {
		return projectcorp;
	}

	public void setProjectcorp(String projectcorp) {
		this.projectcorp = projectcorp;
	}

	public String getBorrower() {
		return borrower;
	}

	public void setBorrower(String borrower) {
		this.borrower = borrower;
	}

	public String getPk_fintype() {
		return pk_fintype;
	}

	public void setPk_fintype(String pk_fintype) {
		this.pk_fintype = pk_fintype;
	}

	public String getPk_organization() {
		return pk_organization;
	}

	public void setPk_organization(String pk_organization) {
		this.pk_organization = pk_organization;
	}

	public String getPk_organizationtype() {
		return pk_organizationtype;
	}

	public void setPk_organizationtype(String pk_organizationtype) {
		this.pk_organizationtype = pk_organizationtype;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getSubbranch() {
		return subbranch;
	}

	public void setSubbranch(String subbranch) {
		this.subbranch = subbranch;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

	public UFDouble getLoan_totalamount() {
		return loan_totalamount;
	}

	public void setLoan_totalamount(UFDouble loan_totalamount) {
		this.loan_totalamount = loan_totalamount;
	}

	public UFDouble getLoan_lastyearamount() {
		return loan_lastyearamount;
	}

	public void setLoan_lastyearamount(UFDouble loan_lastyearamount) {
		this.loan_lastyearamount = loan_lastyearamount;
	}

	public UFDouble getLoan_thisyearamount() {
		return loan_thisyearamount;
	}

	public void setLoan_thisyearamount(UFDouble loan_thisyearamount) {
		this.loan_thisyearamount = loan_thisyearamount;
	}

	public UFDouble getLoan_awaitamount() {
		return loan_awaitamount;
	}

	public void setLoan_awaitamount(UFDouble loan_awaitamount) {
		this.loan_awaitamount = loan_awaitamount;
	}




	public String getCont_lv() {
		return cont_lv;
	}


	public void setCont_lv(String cont_lv) {
		this.cont_lv = cont_lv;
	}


	public String getLoan_datenote() {
		return loan_datenote;
	}

	public void setLoan_datenote(String loan_datenote) {
		this.loan_datenote = loan_datenote;
	}

//	public String getMonth() {
//		return month;
//	}
//
//	public void setMonth(String month) {
//		this.month = month;
//	}
//
//	public String getLoan_amount() {
//		return loan_amount;
//	}
//
//	public void setLoan_amount(String loan_amount) {
//		this.loan_amount = loan_amount;
//	}

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

	public String getOrganizationtypecode() {
		return organizationtypecode;
	}

	public void setOrganizationtypecode(String organizationtypecode) {
		this.organizationtypecode = organizationtypecode;
	}

	public String getOrganizationtypename() {
		return organizationtypename;
	}

	public void setOrganizationtypename(String organizationtypename) {
		this.organizationtypename = organizationtypename;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}
	

}
