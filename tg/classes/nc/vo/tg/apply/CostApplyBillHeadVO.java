package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * ��ϵͳ�������뵥��ͷ���ɱ���ͷVO,������һ����������Ҳ�������
 * 
 * @author lyq
 * 
 */
public class CostApplyBillHeadVO implements Serializable {
	private String org;// ������֯
	private String ebsid;// EBS����
	private String ebsbillcode;// EBS������
	private String imagecode;// Ӱ�����
	private String imagestatus;// Ӱ��״̬
	private String contractcode;// ��ͬ���
	private String contractname;// ��ͬ����
	private String contracttype;// ��ͬ����
	private String contractsubclass;// ��ͬϸ��
	private String emergency;// �����̶�
	private String dept;// ���첿�ű���
	private String operator;// �����˱���
	private String tradetype;// ��������
	private String busitype;// ҵ������
	private String payaccount;// ���������˻�
	private String balatype;// ���㷽ʽ
	private String applymoney;// ���������
	private String totalapplymoney;// �ۼ������
	private String absregisterdate;// abs�Ǽ�����
	private String plate;// ���
	private String accountingorg;// ���˹�˾
	private String supplier;// ��Ӧ��
	private String receiveraccount;// �տ�˻�
	private String project;// ��Ŀ
	private String totalamount;// �ۼƸ�����
	private String applytotalamount;// ��������ۼƸ�����
	private String actuallypaidmoney;// ʵ�����
	private String ispaynote;// �Ƿ��ȸ����Ʊ
	private String ismargin;// �Ƿ��ʱ���۳�
	private String ispaid;// �Ƿ���
	private String money;// ���θ�����
	private String notemoney;// ���η�Ʊ���
	private String totalnotemoney;// �ۼƷ�Ʊ���
	private String creator;// ������
	private String srcsysname;// ��ϵͳ����
	private String explain;// ˵��
	private String signorg;// ǩԼ��˾
	private String projectstages;// ��Ŀ����
	private String finstatus;// �����������״̬
	private String deptname;// ���첿������
	private String operatorname;// ����������
	private String def16;// ����������
	private String def37;// �Ǽǽ��
	private String def38;
	private String def39;
	private String def40;// ��Ʊ����Ƿ�Ϊ0
	private String paymentletter; // �Ǳ�׼ָ�����def43
	private String repairannex; // ������def44
	private String completed; // �Ѳ�ȫdef45
	private String def46;// �����ۿ�
	private String def47;// �Ƿ��¥��ʶ
	private String def30;// ��֤�����
	private String def51;// EBS��ʽ
	private String def52;// ��������
	private String def54;// ��֤�����
	private String def55;// ABSʵ������
	private String def56;// ��Ŀ����
	private String def57;// ��Ŀ״̬
	private String bpmid;// bpmid
	public String bpmaddress;// bpmaddress
	private String def61;// ��������
	private String def62;// �ʲ�����

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
