package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApJsonVO implements Serializable {
	private String srcid;;// 外系统来源单据id
	private String srcbillno;;// 外系统来源单据编号(合同编号)
	private String ouflag;// 财务组织(ebs出账公司)
	private String planbudgetname;// 预算主体
	private String fcreatemode;// 合同属性(ebs单据类型)
	private String fcontracttypenew;// 合同类型（EBS合同类型）
	private String fcontractdetail;// 合同细类
	private String fnumber;// 合同编号
	private String fname;// 合同名称
	private String isleasecontract;// 是否租赁合同
	private String isbond;// 是否存在已付款项
	private String iscrossyear;// 是否预算跨年合同
	private String orgvendorname;// 甲方
	private String vendorname;// 乙方
	private String secondparty1;// 丁方
	private String secondparty2;// 丙方
	private String secondparty3;// 戊方
	private String secondparty4;// 己方
	private String llfsecondparty5name;// 庚方
	private String llfsecondparty6name;// 辛方
	private String createddate;// 创建日期
	private String contractoperatorname;// 合同管理人
	private String fdescription;// 合同摘要
	private String sumboudamount;// 押金/保证金金额
	private String fcontractmoney;// 签约金额
	private String sumsupamount;// 补充协议金额
	private String sumdynamicamount;// 动态金额
	private String wflowstate;// ebs合同状态
	private String ispresupposition;// 是否冲预提
	private String lldatasources;// 数据来源
	private String llstartdate;// 合同开始日期
	private String llenddate;// 合同结束日期
	private String llpayperiod;// 付款周期
	private String llbusinessperiod;// 业务所属期
	private String llsharecontractflag;// 是否分摊合同
	private String llperiodaccrualflag;// 是否周期计提类合同
	private String llsubcontractflag;// 是否为分包合同
	private String llmaincontractno;// 总包合同编码
	private String llmaincontractname;// 总包合同名称
	private String llMaincontractamt;// 总包合同金额
	private String cvendorid;// 收款方
	private String advancebillno;// 预提单号
	private String amountwithdrawn;// 本次冲预提金额
	private String isdirectdebit; // 是否自动扣款
	private String deductiontype; // 划扣类型
	private String signingaccount; // 签约账户
	private String isreimbursement; // 是否已报销
	private String expense_employee_number; // 报销人编号
	private String contractmanagerdepartment;// 合同管理人部门
	private String agent;// 经办人
	private String operatordepartment;// 经办人部门
	
	

	public String getContractmanagerdepartment() {
		return contractmanagerdepartment;
	}

	public void setContractmanagerdepartment(String contractmanagerdepartment) {
		this.contractmanagerdepartment = contractmanagerdepartment;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getOperatordepartment() {
		return operatordepartment;
	}

	public void setOperatordepartment(String operatordepartment) {
		this.operatordepartment = operatordepartment;
	}

	public String getIsreimbursement() {
		return isreimbursement;
	}

	public void setIsreimbursement(String isreimbursement) {
		this.isreimbursement = isreimbursement;
	}

	public String getExpense_employee_number() {
		return expense_employee_number;
	}

	public void setExpense_employee_number(String expense_employee_number) {
		this.expense_employee_number = expense_employee_number;
	}

	public String getIsdirectdebit() {
		return isdirectdebit;
	}

	public void setIsdirectdebit(String isdirectdebit) {
		this.isdirectdebit = isdirectdebit;
	}

	public String getDeductiontype() {
		return deductiontype;
	}

	public void setDeductiontype(String deductiontype) {
		this.deductiontype = deductiontype;
	}

	public String getSigningaccount() {
		return signingaccount;
	}

	public void setSigningaccount(String signingaccount) {
		this.signingaccount = signingaccount;
	}

	public String getAdvancebillno() {
		return advancebillno;
	}

	public void setAdvancebillno(String advancebillno) {
		this.advancebillno = advancebillno;
	}

	public String getAmountwithdrawn() {
		return amountwithdrawn;
	}

	public void setAmountwithdrawn(String amountwithdrawn) {
		this.amountwithdrawn = amountwithdrawn;
	}

	public String getCvendorid() {
		return cvendorid;
	}

	public void setCvendorid(String cvendorid) {
		this.cvendorid = cvendorid;
	}

	public String getSrcid() {
		return srcid;
	}

	public void setSrcid(String srcid) {
		this.srcid = srcid;
	}

	public String getSrcbillno() {
		return srcbillno;
	}

	public void setSrcbillno(String srcbillno) {
		this.srcbillno = srcbillno;
	}

	public String getOuflag() {
		return ouflag;
	}

	public void setOuflag(String ouflag) {
		this.ouflag = ouflag;
	}

	public String getPlanbudgetname() {
		return planbudgetname;
	}

	public void setPlanbudgetname(String planbudgetname) {
		this.planbudgetname = planbudgetname;
	}

	public String getFcreatemode() {
		return fcreatemode;
	}

	public void setFcreatemode(String fcreatemode) {
		this.fcreatemode = fcreatemode;
	}

	public String getFcontracttypenew() {
		return fcontracttypenew;
	}

	public void setFcontracttypenew(String fcontracttypenew) {
		this.fcontracttypenew = fcontracttypenew;
	}

	public String getFcontractdetail() {
		return fcontractdetail;
	}

	public void setFcontractdetail(String fcontractdetail) {
		this.fcontractdetail = fcontractdetail;
	}

	public String getFnumber() {
		return fnumber;
	}

	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getIsleasecontract() {
		return isleasecontract;
	}

	public void setIsleasecontract(String isleasecontract) {
		this.isleasecontract = isleasecontract;
	}

	public String getIsbond() {
		return isbond;
	}

	public void setIsbond(String isbond) {
		this.isbond = isbond;
	}

	public String getIscrossyear() {
		return iscrossyear;
	}

	public void setIscrossyear(String iscrossyear) {
		this.iscrossyear = iscrossyear;
	}

	public String getOrgvendorname() {
		return orgvendorname;
	}

	public void setOrgvendorname(String orgvendorname) {
		this.orgvendorname = orgvendorname;
	}

	public String getVendorname() {
		return vendorname;
	}

	public void setVendorname(String vendorname) {
		this.vendorname = vendorname;
	}

	public String getSecondparty1() {
		return secondparty1;
	}

	public void setSecondparty1(String secondparty1) {
		this.secondparty1 = secondparty1;
	}

	public String getSecondparty2() {
		return secondparty2;
	}

	public void setSecondparty2(String secondparty2) {
		this.secondparty2 = secondparty2;
	}

	public String getSecondparty3() {
		return secondparty3;
	}

	public void setSecondparty3(String secondparty3) {
		this.secondparty3 = secondparty3;
	}

	public String getSecondparty4() {
		return secondparty4;
	}

	public void setSecondparty4(String secondparty4) {
		this.secondparty4 = secondparty4;
	}

	public String getLlfsecondparty5name() {
		return llfsecondparty5name;
	}

	public void setLlfsecondparty5name(String llfsecondparty5name) {
		this.llfsecondparty5name = llfsecondparty5name;
	}

	public String getLlfsecondparty6name() {
		return llfsecondparty6name;
	}

	public void setLlfsecondparty6name(String llfsecondparty6name) {
		this.llfsecondparty6name = llfsecondparty6name;
	}

	public String getCreateddate() {
		return createddate;
	}

	public void setCreateddate(String createddate) {
		this.createddate = createddate;
	}

	public String getContractoperatorname() {
		return contractoperatorname;
	}

	public void setContractoperatorname(String contractoperatorname) {
		this.contractoperatorname = contractoperatorname;
	}

	public String getFdescription() {
		return fdescription;
	}

	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}

	public String getSumboudamount() {
		return sumboudamount;
	}

	public void setSumboudamount(String sumboudamount) {
		this.sumboudamount = sumboudamount;
	}

	public String getFcontractmoney() {
		return fcontractmoney;
	}

	public void setFcontractmoney(String fcontractmoney) {
		this.fcontractmoney = fcontractmoney;
	}

	public String getSumsupamount() {
		return sumsupamount;
	}

	public void setSumsupamount(String sumsupamount) {
		this.sumsupamount = sumsupamount;
	}

	public String getSumdynamicamount() {
		return sumdynamicamount;
	}

	public void setSumdynamicamount(String sumdynamicamount) {
		this.sumdynamicamount = sumdynamicamount;
	}

	public String getWflowstate() {
		return wflowstate;
	}

	public void setWflowstate(String wflowstate) {
		this.wflowstate = wflowstate;
	}

	public String getIspresupposition() {
		return ispresupposition;
	}

	public void setIspresupposition(String ispresupposition) {
		this.ispresupposition = ispresupposition;
	}

	public String getLldatasources() {
		return lldatasources;
	}

	public void setLldatasources(String lldatasources) {
		this.lldatasources = lldatasources;
	}

	public String getLlstartdate() {
		return llstartdate;
	}

	public void setLlstartdate(String llstartdate) {
		this.llstartdate = llstartdate;
	}

	public String getLlenddate() {
		return llenddate;
	}

	public void setLlenddate(String llenddate) {
		this.llenddate = llenddate;
	}

	public String getLlpayperiod() {
		return llpayperiod;
	}

	public void setLlpayperiod(String llpayperiod) {
		this.llpayperiod = llpayperiod;
	}

	public String getLlbusinessperiod() {
		return llbusinessperiod;
	}

	public void setLlbusinessperiod(String llbusinessperiod) {
		this.llbusinessperiod = llbusinessperiod;
	}

	public String getLlsharecontractflag() {
		return llsharecontractflag;
	}

	public void setLlsharecontractflag(String llsharecontractflag) {
		this.llsharecontractflag = llsharecontractflag;
	}

	public String getLlperiodaccrualflag() {
		return llperiodaccrualflag;
	}

	public void setLlperiodaccrualflag(String llperiodaccrualflag) {
		this.llperiodaccrualflag = llperiodaccrualflag;
	}

	public String getLlsubcontractflag() {
		return llsubcontractflag;
	}

	public void setLlsubcontractflag(String llsubcontractflag) {
		this.llsubcontractflag = llsubcontractflag;
	}

	public String getLlmaincontractno() {
		return llmaincontractno;
	}

	public void setLlmaincontractno(String llmaincontractno) {
		this.llmaincontractno = llmaincontractno;
	}

	public String getLlmaincontractname() {
		return llmaincontractname;
	}

	public void setLlmaincontractname(String llmaincontractname) {
		this.llmaincontractname = llmaincontractname;
	}

	public String getLlMaincontractamt() {
		return llMaincontractamt;
	}

	public void setLlMaincontractamt(String llMaincontractamt) {
		this.llMaincontractamt = llMaincontractamt;
	}

}
