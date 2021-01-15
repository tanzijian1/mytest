package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 已结清融资明细
 * 
 * @author HUANGDQ
 * @date 2019年7月3日 上午11:24:08
 */
public class FinClosedDetailedVO implements Serializable, Cloneable {
	String  contractcode;//贷款合同编号
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
	String isabroad;// 是否境外融资
	UFDouble contratio;// 合同利率
	UFDouble adviserratio; // 财务顾问费率
	UFDouble comprehensiveratio;// 综合利率
	String pk_paytype1;// 利息支付方式
	String paytype1name;// 利息支付方式
	//String financialfee;//财务顾问费合同金额
	String pk_paytype2;// 财务顾问费用支付方式
	String pk_repaymenttype; // 还款方式
	String repaymenttypename;// 还款方式
	String mortgageepro;// 抵押物
	String mortgageerate;//抵押率
	String mortgagee;//抵押权人
	String remark;//备注
	String pledgor1;//出质人（一）	
	String equity1;//标的股权（一）loantatol_amount
	String pledgeproportion1;//质押比例（一）
	String pledgepeople1;//质押权人（一）
	String pledgor2;//出质人（二）
	String equity2;//标的股权（二）
	String  pledgeproportion2;//质押比例（二）
	String pledgepeople2;//质押权人（二）
	String guarantor1;//担保人（一）
	String guarantor2;//担保人（二）
	String guarantor3;//担保人（三）
	String accountregulation;//账户监管
	String offsealcertificate;//公章证照共管	
	String moneyprecipitation;//资金沉淀	
	String capitalprerate;//资金沉淀比例	
	String explain;//其他增信条件
	String repaymentconditions;//还款条件
	UFDouble payFinanceAmount;//累计已付财顾费金额
	UFDouble unPayFinanceAmount;//未付财顾费金额
	UFDouble sumPayinterest;//累计已付利息金额
	String contractremark;//融资贷款合同备注
	UFDouble fin_amount;// 融资金额
	String loantatol_amount;// 累计放款金额
	UFDouble fin_balance;// 融资余额
	String begindate;// 融资起始日
	String enddate; // 融资到期日
	String contractenddate;//合同到期日
	String loandate;// 放款时间
	UFDouble payamount;//放款金额
	String settledate;//贷款结清日
	
	String paytype2name;// 利息支付方式


	/*String pk_pledge; // 抵质押物
	String pledge; // 抵质押物
	String pk_guarantee;// 担保条件
	String guarantee;// 担保条件
	*/

	
	public String getPk_project() {
		return pk_project;
	}

	public UFDouble getPayamount() {
		return payamount;
	}

	public void setPayamount(UFDouble payamount) {
		this.payamount = payamount;
	}

	public String getContractenddate() {
		return contractenddate;
	}

	public void setContractenddate(String contractenddate) {
		this.contractenddate = contractenddate;
	}

	public String getContractremark() {
		return contractremark;
	}

	public void setContractremark(String contractremark) {
		this.contractremark = contractremark;
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

	public String getPk_organizationtype() {
		return pk_organizationtype;
	}

	public void setPk_organizationtype(String pk_organizationtype) {
		this.pk_organizationtype = pk_organizationtype;
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

	public String getIsabroad() {
		return isabroad;
	}

	public void setIsabroad(String isabroad) {
		this.isabroad = isabroad;
	}

	public UFDouble getFin_amount() {
		return fin_amount;
	}

	public void setFin_amount(UFDouble fin_amount) {
		this.fin_amount = fin_amount;
	}

	public String getLoantatol_amount() {
		return loantatol_amount;
	}

	public void setLoantatol_amount(String loantatol_amount) {
		this.loantatol_amount = loantatol_amount;
	}



	public UFDouble getFin_balance() {
		return fin_balance;
	}

	public void setFin_balance(UFDouble fin_balance) {
		this.fin_balance = fin_balance;
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

	public String getLoandate() {
		return loandate;
	}

	public void setLoandate(String loandate) {
		this.loandate = loandate;
	}

	public UFDouble getContratio() {
		return contratio;
	}

	public void setContratio(UFDouble contratio) {
		this.contratio = contratio;
	}

	public UFDouble getAdviserratio() {
		return adviserratio;
	}

	public void setAdviserratio(UFDouble adviserratio) {
		this.adviserratio = adviserratio;
	}

	public UFDouble getComprehensiveratio() {
		return comprehensiveratio;
	}

	public void setComprehensiveratio(UFDouble comprehensiveratio) {
		this.comprehensiveratio = comprehensiveratio;
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

/*	public String getFinancialfee() {
		return financialfee;
	}

	public void setFinancialfee(String financialfee) {
		this.financialfee = financialfee;
	}*/

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public UFDouble getPayFinanceAmount() {
		return payFinanceAmount;
	}

	public void setPayFinanceAmount(UFDouble payFinanceAmount) {
		this.payFinanceAmount = payFinanceAmount;
	}

	public UFDouble getUnPayFinanceAmount() {
		return unPayFinanceAmount;
	}

	public void setUnPayFinanceAmount(UFDouble unPayFinanceAmount) {
		this.unPayFinanceAmount = unPayFinanceAmount;
	}

	public UFDouble getSumPayinterest() {
		return sumPayinterest;
	}

	public void setSumPayinterest(UFDouble sumPayinterest) {
		this.sumPayinterest = sumPayinterest;
	}

	public String getMortgageepro() {
		return mortgageepro;
	}

	public void setMortgageepro(String mortgageepro) {
		this.mortgageepro = mortgageepro;
	}

	public String getMortgageerate() {
		return mortgageerate;
	}

	public void setMortgageerate(String mortgageerate) {
		this.mortgageerate = mortgageerate;
	}

	public String getMortgagee() {
		return mortgagee;
	}

	public void setMortgagee(String mortgagee) {
		this.mortgagee = mortgagee;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPledgor1() {
		return pledgor1;
	}

	public void setPledgor1(String pledgor1) {
		this.pledgor1 = pledgor1;
	}

	public String getEquity1() {
		return equity1;
	}

	public void setEquity1(String equity1) {
		this.equity1 = equity1;
	}

	public String getPledgeproportion1() {
		return pledgeproportion1;
	}

	public void setPledgeproportion1(String pledgeproportion1) {
		this.pledgeproportion1 = pledgeproportion1;
	}

	public String getPledgepeople1() {
		return pledgepeople1;
	}

	public void setPledgepeople1(String pledgepeople1) {
		this.pledgepeople1 = pledgepeople1;
	}

	public String getPledgor2() {
		return pledgor2;
	}

	public void setPledgor2(String pledgor2) {
		this.pledgor2 = pledgor2;
	}

	public String getEquity2() {
		return equity2;
	}

	public void setEquity2(String equity2) {
		this.equity2 = equity2;
	}

	public String getPledgeproportion2() {
		return pledgeproportion2;
	}

	public void setPledgeproportion2(String pledgeproportion2) {
		this.pledgeproportion2 = pledgeproportion2;
	}

	public String getPledgepeople2() {
		return pledgepeople2;
	}

	public void setPledgepeople2(String pledgepeople2) {
		this.pledgepeople2 = pledgepeople2;
	}

	public String getGuarantor1() {
		return guarantor1;
	}

	public void setGuarantor1(String guarantor1) {
		this.guarantor1 = guarantor1;
	}

	public String getGuarantor2() {
		return guarantor2;
	}

	public void setGuarantor2(String guarantor2) {
		this.guarantor2 = guarantor2;
	}

	public String getGuarantor3() {
		return guarantor3;
	}

	public void setGuarantor3(String guarantor3) {
		this.guarantor3 = guarantor3;
	}

	public String getAccountregulation() {
		return accountregulation;
	}

	public void setAccountregulation(String accountregulation) {
		this.accountregulation = accountregulation;
	}

	public String getOffsealcertificate() {
		return offsealcertificate;
	}

	public void setOffsealcertificate(String offsealcertificate) {
		this.offsealcertificate = offsealcertificate;
	}

	public String getMoneyprecipitation() {
		return moneyprecipitation;
	}

	public void setMoneyprecipitation(String moneyprecipitation) {
		this.moneyprecipitation = moneyprecipitation;
	}

	public String getCapitalprerate() {
		return capitalprerate;
	}

	public void setCapitalprerate(String capitalprerate) {
		this.capitalprerate = capitalprerate;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getRepaymentconditions() {
		return repaymentconditions;
	}

	public void setRepaymentconditions(String repaymentconditions) {
		this.repaymentconditions = repaymentconditions;
	}

	public String getSettledate() {
		return settledate;
	}

	public void setSettledate(String settledate) {
		this.settledate = settledate;
	}
	
	
}
