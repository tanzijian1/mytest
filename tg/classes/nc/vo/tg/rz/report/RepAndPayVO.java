package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 还款付息表
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午10:54:34
 */
public class RepAndPayVO  implements Serializable, Cloneable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1420441408876658290L;
	String code;//银行贷款合同编号
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
	UFDouble fin_amount;// 融资金额
	UFDouble loantatol_amount;// 累计放款金额
	UFDouble yeartatol_amount;//年度累计还款金额
	String contnote; // 融资合同(合同执行明细)
	String begindate;// 融资起始日
	String enddate; // 融资到期日
	String pk_repaymenttype; // 还款方式
	String repaymenttypename;// 还款方式
	String  rePaycondition;//还款条件
	UFDouble contratio;// 合同利率
	String pk_paytype1;// 利息支付方式
	String paytype1name;// 利息支付方式
	UFDouble adviserratio; // 财务顾问费率
	String pk_paytype2;// 财务顾问费用支付方式
	String paytype2name;// 财务顾问费用支付方式
	String datatype;
	UFDouble repyearplantotal_amount;// N年计划还款金额合计
	UFDouble repyearactualtotal_amount; // N年实际还款金额合计
	UFDouble ipyearactualtotal_amount;// N年付息金额合计
	UFDouble advyearplantotal_amount;// N年计划财务顾问费金额合计
	UFDouble advyearactualtotal_amount;// N年实际财务顾问费金额合计

	UFDouble repmonplantotal_amount;// N月/n+x年计划还款金额
	UFDouble repmonactualtotal_amount;// N月/n+x实际还款金额
	UFDouble ipmonplantotal_amount;// N月/n+x计划付息金额
	UFDouble ipmonactualtotal_amount;// N月/n+x实际付息金额合计
	UFDouble advmonplantotal_amount;// N月/n+x计划财务顾问费用金额
	UFDouble advmonactualtotal_amount;// N月/n+x实际财务顾问费用金额

	
	
	public UFDouble getYeartatol_amount() {
		return yeartatol_amount;
	}

	public void setYeartatol_amount(UFDouble yeartatol_amount) {
		this.yeartatol_amount = yeartatol_amount;
	}

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

	public String getContnote() {
		return contnote;
	}

	public void setContnote(String contnote) {
		this.contnote = contnote;
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

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

	public UFDouble getLoantatol_amount() {
		return loantatol_amount;
	}

	public void setLoantatol_amount(UFDouble loantatol_amount) {
		this.loantatol_amount = loantatol_amount;
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

	public String getPk_repaymenttype() {
		return pk_repaymenttype;
	}

	public void setPk_repaymenttype(String pk_repaymenttype) {
		this.pk_repaymenttype = pk_repaymenttype;
	}

	public String getRepaymenttypename() {
		return repaymenttypename;
	}

	public void setRepaymenttypename(String repaymenttypename) {
		this.repaymenttypename = repaymenttypename;
	}

	public UFDouble getContratio() {
		return contratio;
	}

	public void setContratio(UFDouble contratio) {
		this.contratio = contratio;
	}

	public String getPk_paytype1() {
		return pk_paytype1;
	}

	public void setPk_paytype1(String pk_paytype1) {
		this.pk_paytype1 = pk_paytype1;
	}

	public String getPaytype1name() {
		return paytype1name;
	}

	public void setPaytype1name(String paytype1name) {
		this.paytype1name = paytype1name;
	}

	public UFDouble getAdviserratio() {
		return adviserratio;
	}

	public void setAdviserratio(UFDouble adviserratio) {
		this.adviserratio = adviserratio;
	}

	public String getPk_paytype2() {
		return pk_paytype2;
	}

	public void setPk_paytype2(String pk_paytype2) {
		this.pk_paytype2 = pk_paytype2;
	}

	public String getPaytype2name() {
		return paytype2name;
	}

	public void setPaytype2name(String paytype2name) {
		this.paytype2name = paytype2name;
	}

	public UFDouble getRepyearplantotal_amount() {
		return repyearplantotal_amount;
	}

	public void setRepyearplantotal_amount(UFDouble repyearplantotal_amount) {
		this.repyearplantotal_amount = repyearplantotal_amount;
	}

	public UFDouble getRepyearactualtotal_amount() {
		return repyearactualtotal_amount;
	}

	public void setRepyearactualtotal_amount(UFDouble repyearactualtotal_amount) {
		this.repyearactualtotal_amount = repyearactualtotal_amount;
	}

	public UFDouble getIpyearactualtotal_amount() {
		return ipyearactualtotal_amount;
	}

	public void setIpyearactualtotal_amount(UFDouble ipyearactualtotal_amount) {
		this.ipyearactualtotal_amount = ipyearactualtotal_amount;
	}

	public UFDouble getAdvyearplantotal_amount() {
		return advyearplantotal_amount;
	}

	public void setAdvyearplantotal_amount(UFDouble advyearplantotal_amount) {
		this.advyearplantotal_amount = advyearplantotal_amount;
	}

	public UFDouble getAdvyearactualtotal_amount() {
		return advyearactualtotal_amount;
	}

	public void setAdvyearactualtotal_amount(UFDouble advyearactualtotal_amount) {
		this.advyearactualtotal_amount = advyearactualtotal_amount;
	}

	public UFDouble getRepmonplantotal_amount() {
		return repmonplantotal_amount;
	}

	public void setRepmonplantotal_amount(UFDouble repmonplantotal_amount) {
		this.repmonplantotal_amount = repmonplantotal_amount;
	}

	public UFDouble getRepmonactualtotal_amount() {
		return repmonactualtotal_amount;
	}

	public void setRepmonactualtotal_amount(UFDouble repmonactualtotal_amount) {
		this.repmonactualtotal_amount = repmonactualtotal_amount;
	}

	public UFDouble getIpmonplantotal_amount() {
		return ipmonplantotal_amount;
	}

	public void setIpmonplantotal_amount(UFDouble ipmonplantotal_amount) {
		this.ipmonplantotal_amount = ipmonplantotal_amount;
	}

	public UFDouble getAdvmonplantotal_amount() {
		return advmonplantotal_amount;
	}

	public void setAdvmonplantotal_amount(UFDouble advmonplantotal_amount) {
		this.advmonplantotal_amount = advmonplantotal_amount;
	}

	public UFDouble getAdvmonactualtotal_amount() {
		return advmonactualtotal_amount;
	}

	public void setAdvmonactualtotal_amount(UFDouble advmonactualtotal_amount) {
		this.advmonactualtotal_amount = advmonactualtotal_amount;
	}

	public UFDouble getIpmonactualtotal_amount() {
		return ipmonactualtotal_amount;
	}

	public void setIpmonactualtotal_amount(UFDouble ipmonactualtotal_amount) {
		this.ipmonactualtotal_amount = ipmonactualtotal_amount;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRePaycondition() {
		return rePaycondition;
	}

	public void setRePaycondition(String rePaycondition) {
		this.rePaycondition = rePaycondition;
	}
	
	
}
