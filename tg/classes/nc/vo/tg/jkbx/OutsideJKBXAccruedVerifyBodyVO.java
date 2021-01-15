package nc.vo.tg.jkbx;

import java.io.Serializable;

public class OutsideJKBXAccruedVerifyBodyVO implements Serializable {

	private String accrued_billno;//预提单号
	private String pk_accrued_detail;//预提单行pk
	private String verify_amount;//核销金额
	private String verify_date;//核销日期
	public String getAccrued_billno() {
		return accrued_billno;
	}
	public void setAccrued_billno(String accrued_billno) {
		this.accrued_billno = accrued_billno;
	}
	public String getPk_accrued_detail() {
		return pk_accrued_detail;
	}
	public void setPk_accrued_detail(String pk_accrued_detail) {
		this.pk_accrued_detail = pk_accrued_detail;
	}
	public String getVerify_amount() {
		return verify_amount;
	}
	public void setVerify_amount(String verify_amount) {
		this.verify_amount = verify_amount;
	}
	public String getVerify_date() {
		return verify_date;
	}
	public void setVerify_date(String verify_date) {
		this.verify_date = verify_date;
	}
	
	
}
