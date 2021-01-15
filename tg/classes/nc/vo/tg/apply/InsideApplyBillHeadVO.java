package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * 外系统付款申请单表头（内部交易）
 * 
 * @author lyq
 * 
 */
public class InsideApplyBillHeadVO implements Serializable {
	private String org;// 财务组织
	private String ebsid;// EBS主键
	private String ebsbillcode;// EBS编号
	private String purchasecode;// 采购协议编号
	private String purchasename;// 采购协议名称
	private String emergency;// 紧急程度
	private String supplier;// 客商
	private String isfactor;// 是否保理支付
	private String money;// 申请总金额
	private String billdate;// 单据日期
	private String explain;// 说明
	// private String creator;// 制单人
	private String balatype;// 结算方式
	private String deptname;// 经办部门名称
	private String operatorname;// 经办人名称
	private String finstatus;// 地区财务审核状态
	private String project;// 项目
	private String projectstages;// 项目分期
	private String pk_tradetypeid; // 交易类型
	private String pk_balatype;// 结算方式
	private String imagecode;// 影像编码
	private String imagestatus;// 影像状态
	private String recaccount;// 收款方账户
	private String pk_currtype;// 币种
	private String absmny;// ABS登记金额
	private String srcname;// 外系统名称
	private String bondcode;// 收保证金编号
	private String bondtype;// 保证金类型
	private String bondratio; // 保证金比例
	private String arrivalbuild; // 抵楼
	private String paymentletter; // 非标准指定付款涵
	private String repairannex; // 补附件
	private String completed; // 已补全
	private String paymentType; // 请款类型
	private String taskID; // BPM流程ID
	private String absratio; // ABS实付比例
	private String contractsign;// 合同签约方
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

	public String getPurchasecode() {
		return purchasecode;
	}

	public void setPurchasecode(String purchasecode) {
		this.purchasecode = purchasecode;
	}

	public String getPurchasename() {
		return purchasename;
	}

	public void setPurchasename(String purchasename) {
		this.purchasename = purchasename;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getPk_tradetypeid() {
		return pk_tradetypeid;
	}

	public void setPk_tradetypeid(String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
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

	public String getFinstatus() {
		return finstatus;
	}

	public void setFinstatus(String finstatus) {
		this.finstatus = finstatus;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getProjectstages() {
		return projectstages;
	}

	public void setProjectstages(String projectstages) {
		this.projectstages = projectstages;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
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

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public String getAbsmny() {
		return absmny;
	}

	public void setAbsmny(String absmny) {
		this.absmny = absmny;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}

	public String getIsfactor() {
		return isfactor;
	}

	public void setIsfactor(String isfactor) {
		this.isfactor = isfactor;
	}

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getExplain() {
		return explain;
	}

	public void setExplain(String explain) {
		this.explain = explain;
	}

	public String getSrcname() {
		return srcname;
	}

	public void setSrcname(String srcname) {
		this.srcname = srcname;
	}

	public String getBondcode() {
		return bondcode;
	}

	public void setBondcode(String bondcode) {
		this.bondcode = bondcode;
	}

	public String getBondtype() {
		return bondtype;
	}

	public void setBondtype(String bondtype) {
		this.bondtype = bondtype;
	}

	public String getBondratio() {
		return bondratio;
	}

	public void setBondratio(String bondratio) {
		this.bondratio = bondratio;
	}

	public String getArrivalbuild() {
		return arrivalbuild;
	}

	public void setArrivalbuild(String arrivalbuild) {
		this.arrivalbuild = arrivalbuild;
	}

	public String getPaymentletter() {
		return paymentletter;
	}

	public void setPaymentletter(String paymentletter) {
		this.paymentletter = paymentletter;
	}

	public String getRepairannex() {
		return repairannex;
	}

	public void setRepairannex(String repairannex) {
		this.repairannex = repairannex;
	}

	public String getCompleted() {
		return completed;
	}

	public void setCompleted(String completed) {
		this.completed = completed;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getAbsratio() {
		return absratio;
	}

	public void setAbsratio(String absratio) {
		this.absratio = absratio;
	}

	public String getContractsign() {
		return contractsign;
	}

	public void setContractsign(String contractsign) {
		this.contractsign = contractsign;
	}

}
