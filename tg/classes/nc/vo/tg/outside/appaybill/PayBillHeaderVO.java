package nc.vo.tg.outside.appaybill;

import java.io.Serializable;

public class PayBillHeaderVO implements Serializable {
	String org;// ������֯ pk_org
	String dept;// ���첿�� pk_deptid_v
	String psndoc;// ������ pk_psndoc
	String balatype;// ���㷽ʽ pk_balatype
	String billamount;// ����Ʊ�ݽ�� billamount
	String ebsid;// ��ϵͳ���� def1
	String ebsbillno;// ��ϵͳ���ݺ� def2
	String imgno;// Ӱ����� def3
	String imgstate;// Ӱ��״̬ def4
	String contcode;// ��ͬ���� def5
	String contname;// ��ͬ���� def6
	String conttype;// ��ͬ���� def7
	String contcell;// ��ͬϸ�� def8
	String ebsname;// ��ϵͳ���� ebsname
	String emergency;// �����̶� def9
	String mny_actual;// ʵ����� def11
	String medandlongpro;// �г��� def12
	String mny_abs;// abs֧����� def14
	String plate;// ��� def15
	String accorg;// ���˹�˾ def17
	String supplier;// �տ def18
	String proejctdata;// ��Ŀ def19
	String proejctstages;// ��Ŀ���� def32
	String totalmny_request;// �ۼ������ def21
	String totalmny_pay;// �ۼƸ����� def22
	String isshotgun;// �Ƿ��ȸ����Ʊ def23
	String totalmny_paybythisreq;// ��������ۼƸ����� def24
	String totalmny_invbythis;// ����Ʊ�ݽ�� def25
	String totalmny_inv;// �ۼƷ�Ʊ��� def26
	String isdeduction;// �Ƿ����ʱ���۳� def28
	String isimgdefect;// �Ƿ�ȱʧӰ��
	String note;// ˵�� def31
	String auditstate;// ������������״̬ def33
	String billdate;// ��������
	String signorg;// ǩԼ��֯ def34
	String pleasetype;// �������
	String def59; // ������
	String def60; // ���첿��
	String pk_currtype; // ����

	// SRM����
	String purchaseno;// �ɹ�Э����� def35
	String purchasename;// �ɹ�Э������ def37
	String accbillno;// ���˵��� def38
	String purordercode;// �ɹ��������� def49
	String receivedamount;// �������ս�����˰��
	String def43; // �Ǳ�׼ָ�����def43
	String def44; // ������def44
	String def45; // �Ѳ�ȫdef45
	String def55; // EBS��ʽ-def55
	String advanceinv; // Ԥ���Ʊ-def59
	String def61;// ��Ŀ����
	String def62;// ��Ŀ״̬
	String bpmid;// bpmid
	String bpmaddress;// bpm��ַ
	
	

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

	public String getDef55() {
		return def55;
	}

	public void setDef55(String def55) {
		this.def55 = def55;
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

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public String getDef59() {
		return def59;
	}

	public void setDef59(String def59) {
		this.def59 = def59;
	}

	public String getDef60() {
		return def60;
	}

	public void setDef60(String def60) {
		this.def60 = def60;
	}

	public String getPurordercode() {
		return purordercode;
	}

	public void setPurordercode(String purordercode) {
		this.purordercode = purordercode;
	}

	public String getEbsname() {
		return ebsname;
	}

	public void setEbsname(String ebsname) {
		this.ebsname = ebsname;
	}

	public String getBillamount() {
		return billamount;
	}

	public void setBillamount(String billamount) {
		this.billamount = billamount;
	}

	public String getSignorg() {
		return signorg;
	}

	public void setSignorg(String signorg) {
		this.signorg = signorg;
	}

	public String getOrg() {
		return org;
	}

	public void setOrg(String org) {
		this.org = org;
	}

	public String getDept() {
		return dept;
	}

	public void setDept(String dept) {
		this.dept = dept;
	}

	public String getPsndoc() {
		return psndoc;
	}

	public void setPsndoc(String psndoc) {
		this.psndoc = psndoc;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}

	public String getEbsid() {
		return ebsid;
	}

	public void setEbsid(String ebsid) {
		this.ebsid = ebsid;
	}

	public String getEbsbillno() {
		return ebsbillno;
	}

	public void setEbsbillno(String ebsbillno) {
		this.ebsbillno = ebsbillno;
	}

	public String getImgno() {
		return imgno;
	}

	public void setImgno(String imgno) {
		this.imgno = imgno;
	}

	public String getImgstate() {
		return imgstate;
	}

	public void setImgstate(String imgstate) {
		this.imgstate = imgstate;
	}

	public String getContcode() {
		return contcode;
	}

	public void setContcode(String contcode) {
		this.contcode = contcode;
	}

	public String getContname() {
		return contname;
	}

	public void setContname(String contname) {
		this.contname = contname;
	}

	public String getConttype() {
		return conttype;
	}

	public void setConttype(String conttype) {
		this.conttype = conttype;
	}

	public String getContcell() {
		return contcell;
	}

	public void setContcell(String contcell) {
		this.contcell = contcell;
	}

	public String getEmergency() {
		return emergency;
	}

	public void setEmergency(String emergency) {
		this.emergency = emergency;
	}

	public String getMny_actual() {
		return mny_actual;
	}

	public void setMny_actual(String mny_actual) {
		this.mny_actual = mny_actual;
	}

	public String getMny_abs() {
		return mny_abs;
	}

	public void setMny_abs(String mny_abs) {
		this.mny_abs = mny_abs;
	}

	public String getPlate() {
		return plate;
	}

	public void setPlate(String plate) {
		this.plate = plate;
	}

	public String getAccorg() {
		return accorg;
	}

	public void setAccorg(String accorg) {
		this.accorg = accorg;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getProejctdata() {
		return proejctdata;
	}

	public void setProejctdata(String proejctdata) {
		this.proejctdata = proejctdata;
	}

	public String getProejctstages() {
		return proejctstages;
	}

	public void setProejctstages(String proejctstages) {
		this.proejctstages = proejctstages;
	}

	public String getTotalmny_request() {
		return totalmny_request;
	}

	public void setTotalmny_request(String totalmny_request) {
		this.totalmny_request = totalmny_request;
	}

	public String getTotalmny_pay() {
		return totalmny_pay;
	}

	public void setTotalmny_pay(String totalmny_pay) {
		this.totalmny_pay = totalmny_pay;
	}

	public String getIsshotgun() {
		return isshotgun;
	}

	public void setIsshotgun(String isshotgun) {
		this.isshotgun = isshotgun;
	}

	public String getTotalmny_paybythisreq() {
		return totalmny_paybythisreq;
	}

	public void setTotalmny_paybythisreq(String totalmny_paybythisreq) {
		this.totalmny_paybythisreq = totalmny_paybythisreq;
	}

	public String getTotalmny_invbythis() {
		return totalmny_invbythis;
	}

	public void setTotalmny_invbythis(String totalmny_invbythis) {
		this.totalmny_invbythis = totalmny_invbythis;
	}

	public String getTotalmny_inv() {
		return totalmny_inv;
	}

	public void setTotalmny_inv(String totalmny_inv) {
		this.totalmny_inv = totalmny_inv;
	}

	public String getIsdeduction() {
		return isdeduction;
	}

	public void setIsdeduction(String isdeduction) {
		this.isdeduction = isdeduction;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getAuditstate() {
		return auditstate;
	}

	public void setAuditstate(String auditstate) {
		this.auditstate = auditstate;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getMedandlongpro() {
		return medandlongpro;
	}

	public void setMedandlongpro(String medandlongpro) {
		this.medandlongpro = medandlongpro;
	}

	public String getIsimgdefect() {
		return isimgdefect;
	}

	public void setIsimgdefect(String isimgdefect) {
		this.isimgdefect = isimgdefect;
	}

	public String getPurchaseno() {
		return purchaseno;
	}

	public void setPurchaseno(String purchaseno) {
		this.purchaseno = purchaseno;
	}

	public String getPurchasename() {
		return purchasename;
	}

	public void setPurchasename(String purchasename) {
		this.purchasename = purchasename;
	}

	public String getAccbillno() {
		return accbillno;
	}

	public void setAccbillno(String accbillno) {
		this.accbillno = accbillno;
	}

	public String getPleasetype() {
		return pleasetype;
	}

	public void setPleasetype(String pleasetype) {
		this.pleasetype = pleasetype;
	}

	public String getReceivedamount() {
		return receivedamount;
	}

	public void setReceivedamount(String receivedamount) {
		this.receivedamount = receivedamount;
	}

	public String getAdvanceinv() {
		return advanceinv;
	}

	public void setAdvanceinv(String advanceinv) {
		this.advanceinv = advanceinv;
	}

}
