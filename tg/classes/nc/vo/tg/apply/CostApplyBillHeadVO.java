package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * 外系统付款申请单表头（成本表头VO,费用有一个交易类型也用这个）
 * 
 * @author lyq
 * 
 */
public class CostApplyBillHeadVO implements Serializable {
	private String org;// 财务组织
	private String ebsid;// EBS主键
	private String ebsbillcode;// EBS申请编号
	private String imagecode;// 影像编码
	private String imagestatus;// 影像状态
	private String contractcode;// 合同编号
	private String contractname;// 合同名称
	private String contracttype;// 合同类型
	private String contractsubclass;// 合同细类
	private String emergency;// 紧急程度
	private String dept;// 经办部门编码
	private String operator;// 经办人编码
	private String tradetype;// 付款类型
	private String busitype;// 业务流程
	private String payaccount;// 付款银行账户
	private String balatype;// 结算方式
	private String applymoney;// 本次请款金额
	private String totalapplymoney;// 累计请款金额
	private String absregisterdate;// abs登记日期
	private String plate;// 板块
	private String accountingorg;// 出账公司
	private String supplier;// 供应商
	private String receiveraccount;// 收款方账户
	private String project;// 项目
	private String totalamount;// 累计付款金额
	private String applytotalamount;// 本次请款累计付款金额
	private String actuallypaidmoney;// 实付金额
	private String ispaynote;// 是否先付款后补票
	private String ismargin;// 是否质保金扣除
	private String ispaid;// 是否付讫
	private String money;// 本次付款金额
	private String notemoney;// 本次发票金额
	private String totalnotemoney;// 累计发票金额
	private String creator;// 创建人
	private String srcsysname;// 外系统名称
	private String explain;// 说明
	private String signorg;// 签约公司
	private String projectstages;// 项目分期
	private String finstatus;// 地区财务审核状态
	private String deptname;// 经办部门名称
	private String operatorname;// 经办人名称
	private String def16;// 本次申请金额
	private String def37;// 登记金额
	private String def38;
	private String def39;
	private String def40;// 发票金额是否为0
	private String paymentletter; // 非标准指定付款涵def43
	private String repairannex; // 补附件def44
	private String completed; // 已补全def45
	private String def46;// 其他扣款
	private String def47;// 是否抵楼标识
	private String def30;// 保证金比例
	private String def51;// EBS请款方式
	private String def52;// 款项类型
	private String def54;// 保证金比例
	private String def55;// ABS实付比例
	private String def56;// 项目性质
	private String def57;// 项目状态
	private String bpmid;// bpmid
	public String bpmaddress;// bpmaddress
	private String def61;// 保理类型
	private String def62;// 资产编码

	public String getDef61() {
		return def61;
	}

	public void setDef61(String def61) {
		this.def61 = def61;
	}

	public String getDef62() {
		return def62;
	}

	public void setDef62(String def62) {
		this.def62 = def62;
	}

	public String getBpmid() {
		return bpmid;
	}

	public void setBpmid(String bpmid) {
		this.bpmid = bpmid;
	}

	public String getBpmaddress() {
		return bpmaddress;
	}

	public void setBpmaddress(String bpmaddress) {
		this.bpmaddress = bpmaddress;
	}

	public String getDef56() {
		return def56;
	}

	public void setDef56(String def56) {
		this.def56 = def56;
	}

	public String getDef57() {
		return def57;
	}

	public void setDef57(String def57) {
		this.def57 = def57;
	}

	public String getDef54() {
		return def54;
	}

	public void setDef54(String def54) {
		this.def54 = def54;
	}

	public String getDef55() {
		return def55;
	}

	public void setDef55(String def55) {
		this.def55 = def55;
	}

	public String getDef51() {
		return def51;
	}

	public void setDef51(String def51) {
		this.def51 = def51;
	}

	public String getDef52() {
		return def52;
	}

	public void setDef52(String def52) {
		this.def52 = def52;
	}

	public String getDef30() {
		return def30;
	}

	public void setDef30(String def30) {
		this.def30 = def30;
	}

	public String getDef16() {
		return def16;
	}

	public void setDef16(String def16) {
		this.def16 = def16;
	}

	public String getDef46() {
		return def46;
	}

	public void setDef46(String def46) {
		this.def46 = def46;
	}

	public String getDef47() {
		return def47;
	}

	public void setDef47(String def47) {
		this.def47 = def47;
	}

	public String getDef37() {
		return def37;
	}

	public void setDef37(String def37) {
		this.def37 = def37;
	}

	public String getDef40() {
		return def40;
	}

	public void setDef40(String def40) {
		this.def40 = def40;
	}

	public String getDef38() {
		return def38;
	}

	public void setDef38(String def38) {
		this.def38 = def38;
	}

	public String getDef39() {
		return def39;
	}

	public void setDef39(String def39) {
		this.def39 = def39;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getOperatorname() {
		return operatorname;
	}

	public void setOperatorname(String operatorname) {
		this.operatorname = operatorname;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
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

	public String getImagecode() {
		return imagecode;
	}

	public void setImagecode(String imagecode) {
		this.imagecode = imagecode;
	}

	public String getImagestatus() {
		return imagestatus;
	}

	public void setImagestatus(String imagestatus) {
		this.imagestatus = imagestatus;
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

	public String getContracttype() {
		return contracttype;
	}

	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}

	public String getContractsubclass() {
		return contractsubclass;
	}

	public void setContractsubclass(String contractsubclass) {
		this.contractsubclass = contractsubclass;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getTradetype() {
		return tradetype;
	}

	public void setTradetype(String tradetype) {
		this.tradetype = tradetype;
	}

	public String getBusitype() {
		return busitype;
	}

	public void setBusitype(String busitype) {
		this.busitype = busitype;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}

	public String getApplymoney() {
		return applymoney;
	}

	public void setApplymoney(String applymoney) {
		this.applymoney = applymoney;
	}

	public String getTotalapplymoney() {
		return totalapplymoney;
	}

	public void setTotalapplymoney(String totalapplymoney) {
		this.totalapplymoney = totalapplymoney;
	}

	public String getAbsregisterdate() {
		return absregisterdate;
	}

	public void setAbsregisterdate(String absregisterdate) {
		this.absregisterdate = absregisterdate;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getAccountingorg() {
		return accountingorg;
	}

	public void setAccountingorg(String accountingorg) {
		this.accountingorg = accountingorg;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getReceiveraccount() {
		return receiveraccount;
	}

	public void setReceiveraccount(String receiveraccount) {
		this.receiveraccount = receiveraccount;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public String getApplytotalamount() {
		return applytotalamount;
	}

	public void setApplytotalamount(String applytotalamount) {
		this.applytotalamount = applytotalamount;
	}

	public String getActuallypaidmoney() {
		return actuallypaidmoney;
	}

	public void setActuallypaidmoney(String actuallypaidmoney) {
		this.actuallypaidmoney = actuallypaidmoney;
	}

	public String getIspaynote() {
		return ispaynote;
	}

	public void setIspaynote(String ispaynote) {
		this.ispaynote = ispaynote;
	}

	public String getIsmargin() {
		return ismargin;
	}

	public void setIsmargin(String ismargin) {
		this.ismargin = ismargin;
	}

	public String getIspaid() {
		return ispaid;
	}

	public void setIspaid(String ispaid) {
		this.ispaid = ispaid;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getNotemoney() {
		return notemoney;
	}

	public void setNotemoney(String notemoney) {
		this.notemoney = notemoney;
	}

	public String getTotalnotemoney() {
		return totalnotemoney;
	}

	public void setTotalnotemoney(String totalnotemoney) {
		this.totalnotemoney = totalnotemoney;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getSrcsysname() {
		return srcsysname;
	}

	public void setSrcsysname(String srcsysname) {
		this.srcsysname = srcsysname;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getSignorg() {
		return signorg;
	}

	public void setSignorg(String signorg) {
		this.signorg = signorg;
	}

	public String getProjectstages() {
		return projectstages;
	}

	public void setProjectstages(String projectstages) {
		this.projectstages = projectstages;
	}

	public String getFinstatus() {
		return finstatus;
	}

	public void setFinstatus(String finstatus) {
		this.finstatus = finstatus;
	}

	public String getRepairannex() {
		return repairannex;
	}

	public void setRepairannex(String repairannex) {
		this.repairannex = repairannex;
	}

	public String getPaymentletter() {
		return paymentletter;
	}

	public void setPaymentletter(String paymentletter) {
		this.paymentletter = paymentletter;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

}
