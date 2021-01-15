package nc.vo.tg.outside.incomebill;

import java.io.Serializable;

public class IncomeBillHeadVO  implements Serializable {

	String org ;//财务组织pk_org
	String dept ; //经办部门pk_deptid_v
	String psndoc ; //经办人pk_psndoc
	String billno ; //单据号billno
	String billdate ; //单据日期billdate
	String billstatus ; //单据状态billstatus
	String approvestatus ; //审批状态approvestatus
	String effectstatus ; //生效状态effectstatus
	String objtype ; //往来对象objtype
	String supplier ; //供应商supplier
	String checktype ; //票据类型（票种）checktype
	String busitypeflow ; //业务流程pk_busitype
	String busitype ; //业务类型busitype
	String ebsid ; //外系统主键def1
	String ebsbillno ; //外系统单据号def2
	String imgno ; //影像编码def3
	String imgstate ; //影像状态def4
	String contcode ; //合同编码def5
	String contname ; //合同名称def6
	String conttype ; //合同类型def7
	String contcell ; //合同细类def8
	String emergency ; //紧急程度def9
	String ebsname ; //外系统名称def10
	String totalmny_paybythisreq ; //发票总金额（价税合计）def11
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
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getBilldate() {
		return billdate;
	}
	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}
	public String getBillstatus() {
		return billstatus;
	}
	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}
	public String getApprovestatus() {
		return approvestatus;
	}
	public void setApprovestatus(String approvestatus) {
		this.approvestatus = approvestatus;
	}
	public String getEffectstatus() {
		return effectstatus;
	}
	public void setEffectstatus(String effectstatus) {
		this.effectstatus = effectstatus;
	}
	public String getObjtype() {
		return objtype;
	}
	public void setObjtype(String objtype) {
		this.objtype = objtype;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getChecktype() {
		return checktype;
	}
	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}
	public String getBusitypeflow() {
		return busitypeflow;
	}
	public void setBusitypeflow(String busitypeflow) {
		this.busitypeflow = busitypeflow;
	}
	public String getBusitype() {
		return busitype;
	}
	public void setBusitype(String busitype) {
		this.busitype = busitype;
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
	public String getEbsname() {
		return ebsname;
	}
	public void setEbsname(String ebsname) {
		this.ebsname = ebsname;
	}
	public String getTotalmny_paybythisreq() {
		return totalmny_paybythisreq;
	}
	public void setTotalmny_paybythisreq(String totalmny_paybythisreq) {
		this.totalmny_paybythisreq = totalmny_paybythisreq;
	}

	
}
