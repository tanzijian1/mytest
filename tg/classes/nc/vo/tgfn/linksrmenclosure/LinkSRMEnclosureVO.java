package nc.vo.tgfn.linksrmenclosure;

import nc.vo.pub.SuperVO;

public class LinkSRMEnclosureVO extends SuperVO{

	//附件编码
	public String srmno;
	//SRMID
	public String srmid;
	//附件类型
	public String srmtype;
	//附件名
	public String att_name;
	//附件地址
	public String att_address;
	
	
	public String getSrmno() {
		return srmno;
	}
	public void setSrmno(String srmno) {
		this.srmno = srmno;
	}
	public String getSrmid() {
		return srmid;
	}
	public void setSrmid(String srmid) {
		this.srmid = srmid;
	}
	public String getSrmtype() {
		return srmtype;
	}
	public void setSrmtype(String srmtype) {
		this.srmtype = srmtype;
	}
	public String getAtt_name() {
		return att_name;
	}
	public void setAtt_name(String att_name) {
		this.att_name = att_name;
	}
	public String getAtt_address() {
		return att_address;
	}
	public void setAtt_address(String att_address) {
		this.att_address = att_address;
	}
	
	@Override
	public String getTableName() {
		return "attachment";
	}
	
}
