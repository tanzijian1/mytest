package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApSupplyAgreementJsonVO implements Serializable {
	private String supnumber; // 协议编号
	private String pk_ebs; // ebs主键
	private String supname; // 协议名称
	private String supamount; // 协议金额
	private String supdate; // 签约日期
	private String msupply; // 协议金额累计

	public String getSupnumber() {
		return supnumber;
	}

	public void setSupnumber(String supnumber) {
		this.supnumber = supnumber;
	}

	public String getPk_ebs() {
		return pk_ebs;
	}

	public void setPk_ebs(String pk_ebs) {
		this.pk_ebs = pk_ebs;
	}

	public String getSupname() {
		return supname;
	}

	public void setSupname(String supname) {
		this.supname = supname;
	}

	public String getSupamount() {
		return supamount;
	}

	public void setSupamount(String supamount) {
		this.supamount = supamount;
	}

	public String getSupdate() {
		return supdate;
	}

	public void setSupdate(String supdate) {
		this.supdate = supdate;
	}

	public String getMsupply() {
		return msupply;
	}

	public void setMsupply(String msupply) {
		this.msupply = msupply;
	}

}
