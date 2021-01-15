package nc.vo.tg.costadvance;

import java.io.Serializable;
/**
 * 
 * @author lyq
 *	外系统费用预提单表头
 */
public class CostHeadVO implements Serializable {
	private String org;//财务组织
	private String tradetype;//交易类型
	private String billdate;//单据日期
	private String ebsid;//ebs主键
	private String ebsbillcode;//ebs单号
	private String contractcode;//合同编号
	private String contractname;//合同名称
	private String contractversion;//合同版本
	private String billtype;//单据类型
	private String budget;//预算主体
	private String plate;//板块
	private String platedetail;//商业板块明细
	private float amount;//金额
	private String operatororg;//经办人单位
	private String operator;//经办人
	private String operatordept;//经办人部门
	private String reason;//事由
	private String creator;//制单人
    private String isdelete;//是否删除
    private String def29;//
    private String def30;//
    private String ispostcontract;//是否跨年合同
	private String contractmoney;//合同动态金额
	private String currency;//币种
	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getContractmoney() {
		return contractmoney;
	}

	public void setContractmoney(String contractmoney) {
		this.contractmoney = contractmoney;
	}

	public String getDef29() {
		return def29;
	}

	public String getIspostcontract() {
		return ispostcontract;
	}

	public void setIspostcontract(String ispostcontract) {
		this.ispostcontract = ispostcontract;
	}

	public void setDef29(String def29) {
		this.def29 = def29;
	}

	public String getDef30() {
		return def30;
	}

	public void setDef30(String def30) {
		this.def30 = def30;
	}

	public String getNcid() {
		return ncid;
	}

	public void setNcid(String ncid) {
		this.ncid = ncid;
	}
    private String isapprove;//是否走审批
	public String getIsapprove() {
		return isapprove;
	}

	public void setIsapprove(String isapprove) {
		this.isapprove = isapprove;
	}
	private String ncid;//nc主键
	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getEbsid() {
		return ebsid;
	}

	public void setEbsid(String ebsid) {
		this.ebsid = ebsid;
	}

	public String getEbsbillcode() {
		return ebsbillcode;
	}

	public void setEbsbillcode(String ebsbillcode) {
		this.ebsbillcode = ebsbillcode;
	}

	public String getContractcode() {
		return contractcode;
	}

	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}

	public String getContractname() {
		return contractname;
	}

	public void setContractname(String contractname) {
		this.contractname = contractname;
	}

	public String getContractversion() {
		return contractversion;
	}

	public void setContractversion(String contractversion) {
		this.contractversion = contractversion;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getBudget() {
		return budget;
	}

	public void setBudget(String budget) {
		this.budget = budget;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getPlatedetail() {
		return platedetail;
	}

	public void setPlatedetail(String platedetail) {
		this.platedetail = platedetail;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getOperatororg() {
		return operatororg;
	}

	public void setOperatororg(String operatororg) {
		this.operatororg = operatororg;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOperatordept() {
		return operatordept;
	}

	public void setOperatordept(String operatordept) {
		this.operatordept = operatordept;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
