package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApSupplyAgreementJsonVO implements Serializable {
	private String supnumber; // Э����
	private String pk_ebs; // ebs����
	private String supname; // Э������
	private String supamount; // Э����
	private String supdate; // ǩԼ����
	private String msupply; // Э�����ۼ�

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
