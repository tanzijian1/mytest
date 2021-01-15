package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 预算日常执行明细表
 * 
 * @author huangxj
 *
 */
public class BudgetExeDetailsVO implements Serializable, Cloneable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private UFDouble sortnumglobal;//序号
	private String dimension;//维度树
	private String year;//年
	private String month;//月
	private String expenseSub;//费用科目
	private String budgetSubNo;//预算科目编码
	private String budgetSub;//预算科目
	private String budgetSubCom;//预算归属单位
	private String budgetSubdep;//预算归属部门
	private String outAccCompa;//出账公司
	private String billno;//出账公司
	private String currency;//币种
	private UFDouble actualamount;//实际发生数
	private String payee;//收款人
	private String reimburser;//报销人
	private String supplier;//供应商
	private String project;//项目
	private String format;//业态
	private String departmentFloor;//部门楼层
	private String regisDepartment;//车牌部门
	private String billtype;//单据类型
	private String billdate;//单据日期
	private String approvedate;//审批日期
	private String settmentdate;//结算日期
	private String applicant;//申请人
	private String invoceType;//发票类型
	private String invoceno;//发票号
	private UFDouble invocemnyamount;//发票金额
	private UFDouble taxamount;//税额
	private String approvestatus;//审批状态
	private String subjectMatter;//事由
	private String contractno;//合同编码
	private String contractname;//合同名称
	private String accruedbillno;//预提单据编号
	private String pk_org;//组织PK
	private String glbillmake;//凭证制单人
	private String glbilldate;//凭证制单日期
	private String glcode;//凭证号
	
	
	public String getGlbillmake() {
		return glbillmake;
	}
	public void setGlbillmake(String glbillmake) {
		this.glbillmake = glbillmake;
	}
	public String getGlbilldate() {
		return glbilldate;
	}
	public void setGlbilldate(String glbilldate) {
		this.glbilldate = glbilldate;
	}
	public String getGlcode() {
		return glcode;
	}
	public void setGlcode(String glcode) {
		this.glcode = glcode;
	}
	public String getDimension() {
		return dimension;
	}
	public void setDimension(String dimension) {
		this.dimension = dimension;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getExpenseSub() {
		return expenseSub;
	}
	public void setExpenseSub(String expenseSub) {
		this.expenseSub = expenseSub;
	}
	public String getBudgetSub() {
		return budgetSub;
	}
	public void setBudgetSub(String budgetSub) {
		this.budgetSub = budgetSub;
	}
	public String getBudgetSubCom() {
		return budgetSubCom;
	}
	public void setBudgetSubCom(String budgetSubCom) {
		this.budgetSubCom = budgetSubCom;
	}
	public String getBudgetSubdep() {
		return budgetSubdep;
	}
	public void setBudgetSubdep(String budgetSubdep) {
		this.budgetSubdep = budgetSubdep;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getDepartmentFloor() {
		return departmentFloor;
	}
	public void setDepartmentFloor(String departmentFloor) {
		this.departmentFloor = departmentFloor;
	}
	public String getRegisDepartment() {
		return regisDepartment;
	}
	public void setRegisDepartment(String regisDepartment) {
		this.regisDepartment = regisDepartment;
	}
	public String getBilltype() {
		return billtype;
	}
	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}
	public String getBilldate() {
		return billdate;
	}
	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}
	public String getApprovedate() {
		return approvedate;
	}
	public void setApprovedate(String approvedate) {
		this.approvedate = approvedate;
	}
	public String getSettmentdate() {
		return settmentdate;
	}
	public void setSettmentdate(String settmentdate) {
		this.settmentdate = settmentdate;
	}
	public String getApplicant() {
		return applicant;
	}
	public void setApplicant(String applicant) {
		this.applicant = applicant;
	}
	public String getInvoceno() {
		return invoceno;
	}
	public void setInvoceno(String invoceno) {
		this.invoceno = invoceno;
	}
	public UFDouble getInvocemnyamount() {
		return invocemnyamount;
	}
	public void setInvocemnyamount(UFDouble invocemnyamount) {
		this.invocemnyamount = invocemnyamount;
	}
	public UFDouble getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(UFDouble taxamount) {
		this.taxamount = taxamount;
	}
	public String getApprovestatus() {
		return approvestatus;
	}
	public void setApprovestatus(String approvestatus) {
		this.approvestatus = approvestatus;
	}
	public String getSubjectMatter() {
		return subjectMatter;
	}
	public void setSubjectMatter(String subjectMatter) {
		this.subjectMatter = subjectMatter;
	}
	public String getContractno() {
		return contractno;
	}
	public void setContractno(String contractno) {
		this.contractno = contractno;
	}
	public String getContractname() {
		return contractname;
	}
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}
	public String getAccruedbillno() {
		return accruedbillno;
	}
	public void setAccruedbillno(String accruedbillno) {
		this.accruedbillno = accruedbillno;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getOutAccCompa() {
		return outAccCompa;
	}
	public void setOutAccCompa(String outAccCompa) {
		this.outAccCompa = outAccCompa;
	}
	public String getReimburser() {
		return reimburser;
	}
	public void setReimburser(String reimburser) {
		this.reimburser = reimburser;
	}
	public UFDouble getActualamount() {
		return actualamount;
	}
	public void setActualamount(UFDouble actualamount) {
		this.actualamount = actualamount;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getInvoceType() {
		return invoceType;
	}
	public void setInvoceType(String invoceType) {
		this.invoceType = invoceType;
	}
	public String getBudgetSubNo() {
		return budgetSubNo;
	}
	public void setBudgetSubNo(String budgetSubNo) {
		this.budgetSubNo = budgetSubNo;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public UFDouble getSortnumglobal() {
		return sortnumglobal;
	}
	public void setSortnumglobal(UFDouble sortnumglobal) {
		this.sortnumglobal = sortnumglobal;
	}
	
	

}
