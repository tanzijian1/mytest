package vo.tg.outside;

public class SalaryFundAccureTicketHeadVO {

	private String pk_org;// 财务组织
	private String def18;// 合同签订公司
	private String billdate;// 扣款日期
	private String def67;// 工资所属月
	private String srcid;// 外系统主键
	private String srcbillno;// 外系统单号

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getDef18() {
		return def18;
	}

	public void setDef18(String def18) {
		this.def18 = def18;
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
