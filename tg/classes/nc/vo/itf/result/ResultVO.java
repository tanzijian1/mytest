package nc.vo.itf.result;
/**
 * 
 * ½Ó¿Ú·µ»ØVO
 *
 */
public class ResultVO {

	private String billid;
	private String issuccess;
	private String msg;
	public String getBillid() {
		return billid;
	}
	public void setBillid(String billid) {
		this.billid = billid;
	}
	public String getIssuccess() {
		return issuccess;
	}
	public void setIssuccess(String issuccess) {
		this.issuccess = issuccess;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}