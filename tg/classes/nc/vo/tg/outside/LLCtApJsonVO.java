package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApJsonVO implements Serializable {
	private String srcid;;// ��ϵͳ��Դ����id
	private String srcbillno;;// ��ϵͳ��Դ���ݱ��(��ͬ���)
	private String ouflag;// ������֯(ebs���˹�˾)
	private String planbudgetname;// Ԥ������
	private String fcreatemode;// ��ͬ����(ebs��������)
	private String fcontracttypenew;// ��ͬ���ͣ�EBS��ͬ���ͣ�
	private String fcontractdetail;// ��ͬϸ��
	private String fnumber;// ��ͬ���
	private String fname;// ��ͬ����
	private String isleasecontract;// �Ƿ����޺�ͬ
	private String isbond;// �Ƿ�����Ѹ�����
	private String iscrossyear;// �Ƿ�Ԥ������ͬ
	private String orgvendorname;// �׷�
	private String vendorname;// �ҷ�
	private String secondparty1;// ����
	private String secondparty2;// ����
	private String secondparty3;// �췽
	private String secondparty4;// ����
	private String llfsecondparty5name;// ����
	private String llfsecondparty6name;// ����
	private String createddate;// ��������
	private String contractoperatorname;// ��ͬ������
	private String fdescription;// ��ͬժҪ
	private String sumboudamount;// Ѻ��/��֤����
	private String fcontractmoney;// ǩԼ���
	private String sumsupamount;// ����Э����
	private String sumdynamicamount;// ��̬���
	private String wflowstate;// ebs��ͬ״̬
	private String ispresupposition;// �Ƿ��Ԥ��
	private String lldatasources;// ������Դ
	private String llstartdate;// ��ͬ��ʼ����
	private String llenddate;// ��ͬ��������
	private String llpayperiod;// ��������
	private String llbusinessperiod;// ҵ��������
	private String llsharecontractflag;// �Ƿ��̯��ͬ
	private String llperiodaccrualflag;// �Ƿ����ڼ������ͬ
	private String llsubcontractflag;// �Ƿ�Ϊ�ְ���ͬ
	private String llmaincontractno;// �ܰ���ͬ����
	private String llmaincontractname;// �ܰ���ͬ����
	private String llMaincontractamt;// �ܰ���ͬ���
	private String cvendorid;// �տ
	private String advancebillno;// Ԥ�ᵥ��
	private String amountwithdrawn;// ���γ�Ԥ����
	private String isdirectdebit; // �Ƿ��Զ��ۿ�
	private String deductiontype; // ��������
	private String signingaccount; // ǩԼ�˻�
	private String isreimbursement; // �Ƿ��ѱ���
	private String expense_employee_number; // �����˱��
	private String contractmanagerdepartment;// ��ͬ�����˲���
	private String agent;// ������
	private String operatordepartment;// �����˲���
	
	

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
