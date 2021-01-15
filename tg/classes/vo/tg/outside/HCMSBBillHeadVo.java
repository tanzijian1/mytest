package vo.tg.outside;

import java.io.Serializable;

public class HCMSBBillHeadVo implements Serializable{
	
	private String pk_org;	//社保购买公司
	private String pk_balatype;	//结算方式
	private String billdate;	//扣款日期
	private String total_gs;	//合计公司部分
	private String total_gr;	//合计个人部分
	private String total_gsgr;	//合计公司+个人合计
	private String def30;	//审批情况
	private String def67;	//工资所属月
	
	private String srcid;	//外系统主键
	private String srcbillno;	//外系统单号
	
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_balatype() {
		return pk_balatype;
	}
	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}
	public String getBilldate() {
		return billdate;
	}
	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}
	public String getTotal_gs() {
		return total_gs;
	}
	public void setTotal_gs(String total_gs) {
		this.total_gs = total_gs;
	}
	public String getTotal_gr() {
		return total_gr;
	}
	public void setTotal_gr(String total_gr) {
		this.total_gr = total_gr;
	}
	public String getTotal_gsgr() {
		return total_gsgr;
	}
	public void setTotal_gsgr(String total_gsgr) {
		this.total_gsgr = total_gsgr;
	}
	public String getDef30() {
		return def30;
	}
	public void setDef30(String def30) {
		this.def30 = def30;
	}
	public String getDef67() {
		return def67;
	}
	public void setDef67(String def67) {
		this.def67 = def67;
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

}
