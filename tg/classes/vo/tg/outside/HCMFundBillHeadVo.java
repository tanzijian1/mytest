package vo.tg.outside;

import java.io.Serializable;

public class HCMFundBillHeadVo implements Serializable{
	
	private String pk_org;	//社保购买公司
	private String pk_balatype;	//结算方式
	private String billdate;	//扣款日期
	private String def67;	//工资所属月
	private String total;	//合计
	private String def30;	//审批情况
	
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
	public String getDef67() {
		return def67;
	}
	public void setDef67(String def67) {
		this.def67 = def67;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getDef30() {
		return def30;
	}
	public void setDef30(String def30) {
		this.def30 = def30;
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
