package nc.vo.tg.outside.appaybill;

import java.io.Serializable;

public class PayBillHeaderVO implements Serializable {
	String org;// 财务组织 pk_org
	String dept;// 经办部门 pk_deptid_v
	String psndoc;// 经办人 pk_psndoc
	String balatype;// 结算方式 pk_balatype
	String billamount;// 本次票据金额 billamount
	String ebsid;// 外系统主键 def1
	String ebsbillno;// 外系统单据号 def2
	String imgno;// 影像编码 def3
	String imgstate;// 影像状态 def4
	String contcode;// 合同编码 def5
	String contname;// 合同名称 def6
	String conttype;// 合同类型 def7
	String contcell;// 合同细类 def8
	String ebsname;// 外系统名称 ebsname
	String emergency;// 紧急程度 def9
	String mny_actual;// 实付金额 def11
	String medandlongpro;// 中长期 def12
	String mny_abs;// abs支付金额 def14
	String plate;// 板块 def15
	String accorg;// 出账公司 def17
	String supplier;// 收款方 def18
	String proejctdata;// 项目 def19
	String proejctstages;// 项目分期 def32
	String totalmny_request;// 累计请款金额 def21
	String totalmny_pay;// 累计付款金额 def22
	String isshotgun;// 是否先付款后补票 def23
	String totalmny_paybythisreq;// 本次请款累计付款金额 def24
	String totalmny_invbythis;// 本次票据金额 def25
	String totalmny_inv;// 累计发票金额 def26
	String isdeduction;// 是否有质保金扣除 def28
	String isimgdefect;// 是否缺失影像
	String note;// 说明 def31
	String auditstate;// 地区财务审批状态 def33
	String billdate;// 单据日期
	String signorg;// 签约组织 def34
	String pleasetype;// 请款类型
	String def59; // 经办人
	String def60; // 经办部门
	String pk_currtype; // 币种

	// SRM对账
	String purchaseno;// 采购协议编码 def35
	String purchasename;// 采购协议名称 def37
	String accbillno;// 对账单号 def38
	String purordercode;// 采购订单编码 def49
	String receivedamount;// 到货接收金额（不含税）
	String def43; // 非标准指定付款涵def43
	String def44; // 补附件def44
	String def45; // 已补全def45
	String def55; // EBS请款方式-def55
	String advanceinv; // 预付款发票-def59
	String def61;// 项目性质
	String def62;// 项目状态
	String bpmid;// bpmid
	String bpmaddress;// bpm地址
	
	

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
