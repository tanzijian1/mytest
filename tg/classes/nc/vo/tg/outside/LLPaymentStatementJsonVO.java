package nc.vo.tg.outside;

import java.io.Serializable;

public class LLPaymentStatementJsonVO implements Serializable {

	private String pk_org; // ���������֯
	private String billdate; // ��������
	private String objecttype; // ���׶�������
	private String local_money; // �ϼƽ��
	private String imgcode; // Ӱ�����
	private String mailbox; // �����������
	private String pk_tradetypeid; // ��������
	private String businesstype; // ҵ������
	private String contractno; // ��ͬ��
	private String srcid; // ��ҵ�շ�ϵͳ���յ�ID
	private String srcbillno; // ��ҵ�շ�ϵͳ���ձ��
	private String memo; // ��ע
	private String bill_status; // ����״̬
	private String effect_flag; // ��Ч״̬
	private String paystatus; // ֧��״̬

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
