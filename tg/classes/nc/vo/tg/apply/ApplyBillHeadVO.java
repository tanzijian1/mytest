package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * ��ϵͳ�������뵥��ͷ�����ã�
 * 
 * @author lyq
 * 
 */
public class ApplyBillHeadVO implements Serializable {
	private String org;// ������֯
	private String tradetype;// ��������
	private String billdate;// �������
	private String ebsid;// EBS����
	private String ebsbillcode;// EBS������
	private String imagecode;// Ӱ�����
	private String imagestatus;// Ӱ��״̬
	private String contractcode;// ��ͬ���
	private String contractname;// ��ͬ����
	private String contracttype;// ��ͬ����
	private String emergency;// �����̶�
	private String sysname;// ��ϵͳ����
	private String contractsubclass;// ��ͬϸ��
	private String longproject;// �г�����Ŀ
	private String dept;// ���첿��
	private String receiver;// �տ
	private String operator;// ������
	private String balatype;// ���㷽ʽ
	private String payaccount;// ���������˻�
	private String paybranch;// ��������֧��
	private String money;// ���
	private String applymoney;// ���������
	private String totalamount;// �ۼƸ�����
	private String totalapplyamount;// �ۼ������
	private String applytotalamount;// ��������ۼ������
	private boolean islostimg;// �Ƿ�ȱʧӰ��
	private String plate;// ���
	private String accountingorg;// ���˹�˾
	private String project;// ��Ŀ
	private String creator;// ������
	private boolean ismargin;// �Ƿ�֤��
	private boolean isdelete;// �Ƿ�����
	private String explan;// ˵��
	private String bankaccount;// �տ�˻�
	private String pk_currtype;// ����
	private String def11;
	private String def40;// ��Ʊ�Ƿ�Ϊ0
	private String def43;// �Ǳ�׼ָ�����
	private String def44;// ������
	private String def45;// �Ѳ�ȫ
	private String finstatus;// �����������״̬
	private String ispaynote;// �Ƿ��ȸ����Ʊ
	private String def61;// �������
	private String def47;// �Ƿ��¥��ʶ
	private String def37;// ��֤�����
	private String def51;// EBS��ʽ
	private String def54;// ABSʵ������
	private String def55;// ABSʵ�����
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

	private String isbuyticket;// �Ƿ��ȸ����Ʊ

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
