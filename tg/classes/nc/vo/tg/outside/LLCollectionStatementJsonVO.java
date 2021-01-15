package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCollectionStatementJsonVO implements Serializable {
	private String pk_org;// 收款财务组织
	private String billdate;// 单据日期
	private String pk_tradetypeid;// 交易类型
//	private String local_money;// 合计金额
//	private String actualnateyear;// 实收所属年月-入账
	private String contractno;// 合同号
	private String mailbox;// 经办人邮箱号
	private String imgcode;// 影像编码
	private String srcid;// 收费系统ID
	private String srcbillno;// 收费系统编号
//	private String pk_busiflow;// 业务流程
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
