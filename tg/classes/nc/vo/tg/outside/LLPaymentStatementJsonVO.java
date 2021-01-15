package nc.vo.tg.outside;

import java.io.Serializable;

public class LLPaymentStatementJsonVO implements Serializable {

	private String pk_org; // 付款财务组织
	private String billdate; // 单据日期
	private String objecttype; // 交易对象类型
	private String local_money; // 合计金额
	private String imgcode; // 影像编码
	private String mailbox; // 经办人邮箱号
	private String pk_tradetypeid; // 交易类型
	private String businesstype; // 业务类型
	private String contractno; // 合同号
	private String srcid; // 物业收费系统接收单ID
	private String srcbillno; // 物业收费系统接收编号
	private String memo; // 备注
	private String bill_status; // 单据状态
	private String effect_flag; // 生效状态
	private String paystatus; // 支付状态

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getObjecttype() {
		return objecttype;
	}

	public void setObjecttype(String objecttype) {
		this.objecttype = objecttype;
	}

	public String getLocal_money() {
		return local_money;
	}

	public void setLocal_money(String local_money) {
		this.local_money = local_money;
	}

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getPk_tradetypeid() {
		return pk_tradetypeid;
	}

	public void setPk_tradetypeid(String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getContractno() {
		return contractno;
	}

	public void setContractno(String contractno) {
		this.contractno = contractno;
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

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBill_status() {
		return bill_status;
	}

	public void setBill_status(String bill_status) {
		this.bill_status = bill_status;
	}

	public String getEffect_flag() {
		return effect_flag;
	}

	public void setEffect_flag(String effect_flag) {
		this.effect_flag = effect_flag;
	}

	public String getPaystatus() {
		return paystatus;
	}

	public void setPaystatus(String paystatus) {
		this.paystatus = paystatus;
	}

}
