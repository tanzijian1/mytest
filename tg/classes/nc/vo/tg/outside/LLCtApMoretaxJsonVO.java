package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApMoretaxJsonVO implements Serializable {
	private java.lang.String finvoicetype; // 主要发票类型
	private java.lang.String pk_ebs; // EBS主表ID
	private java.lang.String ftaxrateid; // 税率
	private java.lang.String famount; // 含税金额
	private java.lang.String ftaxvalue; // 税额
	private java.lang.String notaxmny; // 不含税金额

	public java.lang.String getFinvoicetype() {
		return finvoicetype;
	}

	public void setFinvoicetype(java.lang.String finvoicetype) {
		this.finvoicetype = finvoicetype;
	}

	public java.lang.String getPk_ebs() {
		return pk_ebs;
	}

	public void setPk_ebs(java.lang.String pk_ebs) {
		this.pk_ebs = pk_ebs;
	}

	public java.lang.String getFtaxrateid() {
		return ftaxrateid;
	}

	public void setFtaxrateid(java.lang.String ftaxrateid) {
		this.ftaxrateid = ftaxrateid;
	}

	public java.lang.String getFamount() {
		return famount;
	}

	public void setFamount(java.lang.String famount) {
		this.famount = famount;
	}

	public java.lang.String getFtaxvalue() {
		return ftaxvalue;
	}

	public void setFtaxvalue(java.lang.String ftaxvalue) {
		this.ftaxvalue = ftaxvalue;
	}

	public java.lang.String getNotaxmny() {
		return notaxmny;
	}

	public void setNotaxmny(java.lang.String notaxmny) {
		this.notaxmny = notaxmny;
	}

}
