package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * �ѽ���������ϸ
 * 
 * @author HUANGDQ
 * @date 2019��7��3�� ����11:24:08
 */
public class FinClosedDetailedVO implements Serializable, Cloneable {
	String  contractcode;//�����ͬ���
	String pk_project;// ��ĿID
	String projectcode;// ��Ŀ����
	String projectname;// ��Ŀ����
	String pk_periodization;// ��������
	String periodizationname;// ��������
	String projectcorp;// ��Ŀ��˾
	String borrower;// �����
	String pk_fintype;// ��������
	String fintypecode;// �������ͱ���
	String fintypename;// ������������
	String pk_organization;// ���ʻ���
	String organizationcode;// ��������
	String organizationname;// ��������
	String pk_organizationtype;// ���ʻ������
	String organizationtypecode;// ���ʻ���������
	String organizationtypename;// ���ʻ����������
	String branch;// ��������/�ֲ�
	String subbranch;// ����֧��
	String isabroad;// �Ƿ�������
	UFDouble contratio;// ��ͬ����
	UFDouble adviserratio; // ������ʷ���
	UFDouble comprehensiveratio;// �ۺ�����
	String pk_paytype1;// ��Ϣ֧����ʽ
	String paytype1name;// ��Ϣ֧����ʽ
	//String financialfee;//������ʷѺ�ͬ���
	String pk_paytype2;// ������ʷ���֧����ʽ
	String pk_repaymenttype; // ���ʽ
	String repaymenttypename;// ���ʽ
	String mortgageepro;// ��Ѻ��
	String mortgageerate;//��Ѻ��
	String mortgagee;//��ѺȨ��
	String remark;//��ע
	String pledgor1;//�����ˣ�һ��	
	String equity1;//��Ĺ�Ȩ��һ��loantatol_amount
	String pledgeproportion1;//��Ѻ������һ��
	String pledgepeople1;//��ѺȨ�ˣ�һ��
	String pledgor2;//�����ˣ�����
	String equity2;//��Ĺ�Ȩ������
	String  pledgeproportion2;//��Ѻ����������
	String pledgepeople2;//��ѺȨ�ˣ�����
	String guarantor1;//�����ˣ�һ��
	String guarantor2;//�����ˣ�����
	String guarantor3;//�����ˣ�����
	String accountregulation;//�˻����
	String offsealcertificate;//����֤�չ���	
	String moneyprecipitation;//�ʽ����	
	String capitalprerate;//�ʽ�������	
	String explain;//������������
	String repaymentconditions;//��������
	UFDouble payFinanceAmount;//�ۼ��Ѹ��ƹ˷ѽ��
	UFDouble unPayFinanceAmount;//δ���ƹ˷ѽ��
	UFDouble sumPayinterest;//�ۼ��Ѹ���Ϣ���
	String contractremark;//���ʴ����ͬ��ע
	UFDouble fin_amount;// ���ʽ��
	String loantatol_amount;// �ۼƷſ���
	UFDouble fin_balance;// �������
	String begindate;// ������ʼ��
	String enddate; // ���ʵ�����
	String contractenddate;//��ͬ������
	String loandate;// �ſ�ʱ��
	UFDouble payamount;//�ſ���
	String settledate;//���������
	
	String paytype2name;// ��Ϣ֧����ʽ


	/*String pk_pledge; // ����Ѻ��
	String pledge; // ����Ѻ��
	String pk_guarantee;// ��������
	String guarantee;// ��������
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
