package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCollectionStatementJsonVO implements Serializable {
	private String pk_org;// �տ������֯
	private String billdate;// ��������
	private String pk_tradetypeid;// ��������
//	private String local_money;// �ϼƽ��
//	private String actualnateyear;// ʵ����������-����
	private String contractno;// ��ͬ��
	private String mailbox;// �����������
	private String imgcode;// Ӱ�����
	private String srcid;// �շ�ϵͳID
	private String srcbillno;// �շ�ϵͳ���
//	private String pk_busiflow;// ҵ������
//
//	public String getActualnateyear() {
//		return actualnateyear;
//	}
//
//	public void setActualnateyear(String actualnateyear) {
//		this.actualnateyear = actualnateyear;
//	}

	public String getPk_tradetypeid() {
		return pk_tradetypeid;
	}

	public void setPk_tradetypeid(String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

//	public String getLocal_money() {
//		return local_money;
//	}
//
//	public void setLocal_money(String local_money) {
//		this.local_money = local_money;
//	}

	public String getContractno() {
		return contractno;
	}

	public void setContractno(String contractno) {
		this.contractno = contractno;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
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

//	public String getPk_busiflow() {
//		return pk_busiflow;
//	}
//
//	public void setPk_busiflow(String pk_busiflow) {
//		this.pk_busiflow = pk_busiflow;
//	}

}
