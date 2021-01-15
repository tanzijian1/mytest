package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * ��ϵͳ�������뵥��ͷ���ڲ����ף�
 * 
 * @author lyq
 * 
 */
public class InsideApplyBillHeadVO implements Serializable {
	private String org;// ������֯
	private String ebsid;// EBS����
	private String ebsbillcode;// EBS���
	private String purchasecode;// �ɹ�Э����
	private String purchasename;// �ɹ�Э������
	private String emergency;// �����̶�
	private String supplier;// ����
	private String isfactor;// �Ƿ���֧��
	private String money;// �����ܽ��
	private String billdate;// ��������
	private String explain;// ˵��
	// private String creator;// �Ƶ���
	private String balatype;// ���㷽ʽ
	private String deptname;// ���첿������
	private String operatorname;// ����������
	private String finstatus;// �����������״̬
	private String project;// ��Ŀ
	private String projectstages;// ��Ŀ����
	private String pk_tradetypeid; // ��������
	private String pk_balatype;// ���㷽ʽ
	private String imagecode;// Ӱ�����
	private String imagestatus;// Ӱ��״̬
	private String recaccount;// �տ�˻�
	private String pk_currtype;// ����
	private String absmny;// ABS�Ǽǽ��
	private String srcname;// ��ϵͳ����
	private String bondcode;// �ձ�֤����
	private String bondtype;// ��֤������
	private String bondratio; // ��֤�����
	private String arrivalbuild; // ��¥
	private String paymentletter; // �Ǳ�׼ָ�����
	private String repairannex; // ������
	private String completed; // �Ѳ�ȫ
	private String paymentType; // �������
	private String taskID; // BPM����ID
	private String absratio; // ABSʵ������
	private String contractsign;// ��ͬǩԼ��
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
