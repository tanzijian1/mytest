package nc.vo.tgfn.report.tax;

import java.io.Serializable;

import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * 税收清算支撑-现金流数据表（税务报表）
 * 
 * @author hzp
 * 
 */
public class TaxLiquidationCashFlowVO implements Serializable, Cloneable {
	
	private String taxid;// 纳税人识别号
	private UFDate formdate;// 填表日期
	
	private String orgname;// 纳税人名称 (付款组织)
	private String pk_org;// 组织pk
	private String projectname;// 项目 
	private String pk_project;// 项目pk
	private String contract;// 合同
	private String applynum;// ebs请款单号（外系统单据号）
	private String billno;// nc支付单号（单据号）
	private UFDouble money;// 支付金额
	private Integer num;// 凭证号
	private String payee;// 收款方
	private String costsubject;// 成本科目
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getContract() {
		return contract;
	}
	public void setContract(String contract) {
		this.contract = contract;
	}
	public String getApplynum() {
		return applynum;
	}
	public void setApplynum(String applynum) {
		this.applynum = applynum;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public UFDouble getMoney() {
		return money;
	}
	public void setMoney(UFDouble money) {
		this.money = money;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public String getCostsubject() {
		return costsubject;
	}
	public void setCostsubject(String costsubject) {
		this.costsubject = costsubject;
	}
	public String getTaxid() {
		return taxid;
	}
	public void setTaxid(String taxid) {
		this.taxid = taxid;
	}
	public UFDate getFormdate() {
		return formdate;
	}
	public void setFormdate(UFDate formdate) {
		this.formdate = formdate;
	}
	public String getOrgname() {
		return orgname;
	}
	public void setOrgname(String orgname) {
		this.orgname = orgname;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_project() {
		return pk_project;
	}
	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}
	
	
	
}
