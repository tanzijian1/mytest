package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * 外系统付款申请单表头（费用）
 * 
 * @author lyq
 * 
 */
public class ApplyBillHeadVO implements Serializable {
	private String org;// 财务组织
	private String tradetype;// 付款类型
	private String billdate;// 请款日期
	private String ebsid;// EBS主键
	private String ebsbillcode;// EBS申请编号
	private String imagecode;// 影像编码
	private String imagestatus;// 影像状态
	private String contractcode;// 合同编号
	private String contractname;// 合同名称
	private String contracttype;// 合同类型
	private String emergency;// 紧急程度
	private String sysname;// 外系统名称
	private String contractsubclass;// 合同细类
	private String longproject;// 中长期项目
	private String dept;// 经办部门
	private String receiver;// 收款方
	private String operator;// 经办人
	private String balatype;// 结算方式
	private String payaccount;// 付款银行账户
	private String paybranch;// 付款银行支行
	private String money;// 金额
	private String applymoney;// 本次请款金额
	private String totalamount;// 累计付款金额
	private String totalapplyamount;// 累计请款金额
	private String applytotalamount;// 本次请款累计请款金额
	private boolean islostimg;// 是否缺失影像
	private String plate;// 板块
	private String accountingorg;// 出账公司
	private String project;// 项目
	private String creator;// 创建人
	private boolean ismargin;// 是否保证金
	private boolean isdelete;// 是否作废
	private String explan;// 说明
	private String bankaccount;// 收款方账户
	private String pk_currtype;// 币种
	private String def11;
	private String def40;// 发票是否为0
	private String def43;// 非标准指定付款函
	private String def44;// 补附件
	private String def45;// 已补全
	private String finstatus;// 地区财务审核状态
	private String ispaynote;// 是否先付款后补票
	private String def61;// 请款类型
	private String def47;// 是否抵楼标识
	private String def37;// 保证金比例
	private String def51;// EBS请款方式
	private String def54;// ABS实付比例
	private String def55;// ABS实付金额
	private String bpmid;// bpmid
	public String bpmaddress;// bpmaddress

	public String getDef51() {
		return def51;
	}

	public void setDef51(String def51) {
		this.def51 = def51;
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

	public String getDef37() {
		return def37;
	}

	public void setDef37(String def37) {
		this.def37 = def37;
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

	public String getDef47() {
		return def47;
	}

	public void setDef47(String def47) {
		this.def47 = def47;
	}

	public String getDef43() {
		return def43;
	}

	public void setDef43(String def43) {
		this.def43 = def43;
	}

	public String getDef44() {
		return def44;
	}

	public void setDef44(String def44) {
		this.def44 = def44;
	}

	public String getDef45() {
		return def45;
	}

	public void setDef45(String def45) {
		this.def45 = def45;
	}

	public String getDef61() {
		return def61;
	}

	public void setDef61(String def61) {
		this.def61 = def61;
	}

	public String getIspaynote() {
		return ispaynote;
	}

	public void setIspaynote(String ispaynote) {
		this.ispaynote = ispaynote;
	}

	public String getDef11() {
		return def11;
	}

	public void setDef11(String def11) {
		this.def11 = def11;
	}

	private String isbuyticket;// 是否先付款后补票

	public String getIsbuyticket() {
		return isbuyticket;
	}

	public void setIsbuyticket(String isbuyticket) {
		this.isbuyticket = isbuyticket;
	}

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBankaccount() {
		return bankaccount;
	}

	public void setBankaccount(String bankaccount) {
		this.bankaccount = bankaccount;
	}

	public String getTotalapplyamount() {
		return totalapplyamount;
	}

	public void setTotalapplyamount(String totalapplyamount) {
		this.totalapplyamount = totalapplyamount;
	}

	public String getApplytotalamount() {
		return applytotalamount;
	}

	public void setApplytotalamount(String applytotalamount) {
		this.applytotalamount = applytotalamount;
	}

	public String getExplan() {
		return explan;
	}

	public void setExplan(String explan) {
		this.explan = explan;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}

	public String getPaybranch() {
		return paybranch;
	}

	public void setPaybranch(String paybranch) {
		this.paybranch = paybranch;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
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

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public String getContractsubclass() {
		return contractsubclass;
	}

	public void setContractsubclass(String contractsubclass) {
		this.contractsubclass = contractsubclass;
	}

	public String getLongproject() {
		return longproject;
	}

	public void setLongproject(String longproject) {
		this.longproject = longproject;
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

	public String getApplymoney() {
		return applymoney;
	}

	public void setApplymoney(String applymoney) {
		this.applymoney = applymoney;
	}

	public String getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(String totalamount) {
		this.totalamount = totalamount;
	}

	public boolean isIslostimg() {
		return islostimg;
	}

	public void setIslostimg(boolean islostimg) {
		this.islostimg = islostimg;
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

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public boolean isIsmargin() {
		return ismargin;
	}

	public void setIsmargin(boolean ismargin) {
		this.ismargin = ismargin;
	}

	public boolean isIsdelete() {
		return isdelete;
	}

	public void setIsdelete(boolean isdelete) {
		this.isdelete = isdelete;
	}

	public String getDef40() {
		return def40;
	}

	public void setDef40(String def40) {
		this.def40 = def40;
	}

	public String getFinstatus() {
		return finstatus;
	}

	public void setFinstatus(String finstatus) {
		this.finstatus = finstatus;
	}

}
