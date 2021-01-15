package nc.vo.tg.outside;

import java.io.Serializable;

/**
 * 外系统收款单
 * 
 * @author 谈子健
 * 
 */
public class GatheringHeadVO implements Serializable {
	private String srcid;// 外系统来源单据id
	private String srcbillno;// 外系统来源单据编号
	private String pk_org; // 应收财务组织
	private String billdate; // 单据日期
	private String busidate; // 起算日期
	private String objtype; // 往来对象
	private String customer; // 客户
	private String pk_deptid; // 部门
	private String pk_psndoc; // 业务员
	private String pk_tradetype; // 应收类型
	private String pk_tradetypeid; // 应收类型
	private String pk_currtype; // 币种
	private String local_money; // 组织本币
	private String accessorynum; // 附件张数
	private String isreded; // 红冲标志
	private String billclass; // 单据大类
	private String pk_balatype; // 支付方式
	private String settleflag;// 支付状态
	private String settlenum;// 结算号
	private String bpmid;// bpmid
	private String imgcode;// 影像编码
	private String imgstate;// 影像状态
	private String money;// 原币金额
	private String itemtype; // 收费项目类型
	private String itemname; // 收费项目名称
	private String businesstype; // 业务类型
	private String businessbreakdownpublic; // 业务细类
	private String mailbox;// 经办人邮箱号
	private String projectphase;// 项目阶段

	public String getProjectphase() {
		return projectphase;
	}

	public void setProjectphase(String projectphase) {
		this.projectphase = projectphase;
	}

	public String getMailbox() {
		return mailbox;
	}

	public void setMailbox(String mailbox) {
		this.mailbox = mailbox;
	}

	public String getItemtype() {
		return itemtype;
	}

	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
	}

	public String getBusinessbreakdownpublic() {
		return businessbreakdownpublic;
	}

	public void setBusinessbreakdownpublic(String businessbreakdownpublic) {
		this.businessbreakdownpublic = businessbreakdownpublic;
	}

	public String getSettleflag() {
		return settleflag;
	}

	public void setSettleflag(String settleflag) {
		this.settleflag = settleflag;
	}

	public String getSettlenum() {
		return settlenum;
	}

	public void setSettlenum(String settlenum) {
		this.settlenum = settlenum;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getImgcode() {
		return imgcode;
	}

	public void setImgcode(String imgcode) {
		this.imgcode = imgcode;
	}

	public String getImgstate() {
		return imgstate;
	}

	public void setImgstate(String imgstate) {
		this.imgstate = imgstate;
	}

	public String getBpmid() {
		return bpmid;
	}

	public void setBpmid(String bpmid) {
		this.bpmid = bpmid;
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

	public String getBusidate() {
		return busidate;
	}

	public void setBusidate(String busidate) {
		this.busidate = busidate;
	}

	public String getObjtype() {
		return objtype;
	}

	public void setObjtype(String objtype) {
		this.objtype = objtype;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getPk_deptid() {
		return pk_deptid;
	}

	public void setPk_deptid(String pk_deptid) {
		this.pk_deptid = pk_deptid;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_tradetype() {
		return pk_tradetype;
	}

	public void setPk_tradetype(String pk_tradetype) {
		this.pk_tradetype = pk_tradetype;
	}

	public String getPk_tradetypeid() {
		return pk_tradetypeid;
	}

	public void setPk_tradetypeid(String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	public String getPk_currtype() {
		return pk_currtype;
	}

	public void setPk_currtype(String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	public String getLocal_money() {
		return local_money;
	}

	public void setLocal_money(String local_money) {
		this.local_money = local_money;
	}

	public String getAccessorynum() {
		return accessorynum;
	}

	public void setAccessorynum(String accessorynum) {
		this.accessorynum = accessorynum;
	}

	public String getIsreded() {
		return isreded;
	}

	public void setIsreded(String isreded) {
		this.isreded = isreded;
	}

	public String getBillclass() {
		return billclass;
	}

	public void setBillclass(String billclass) {
		this.billclass = billclass;
	}

	public String getPk_balatype() {
		return pk_balatype;
	}

	public void setPk_balatype(String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

}
