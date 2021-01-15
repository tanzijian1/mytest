package nc.vo.tg.bpmoutside;

import nc.vo.pub.SuperVO;

public class SealFlowOutsideHeadVO extends SuperVO{

	/**
	 * BPM推送过来的盖章资料表头数据VO
	 */
	private static final long serialVersionUID = 6497595904698113996L;
	/**BPM业务单据主键*/
	public java.lang.String bpmid;
	/**集团*/
	public java.lang.String pk_group;
	/**单据类型*/
	public java.lang.String billtype;
	/**财务组织*/
	public java.lang.String pk_org;
	/**贷款合同编码*/
	public java.lang.String contractcode;
	/**贷款合同名称*/
	public java.lang.String contractname;
	/**申请人*/
	public java.lang.String proposer;
	/**申请时间*/
	public java.lang.String applicationtime;
	/**申请公司*/
	public java.lang.String applicationcompany;
	/**申请部门*/
	public java.lang.String applicationdepartment;
	/**盖章人*/
	public java.lang.String sealer;
	/**盖章原因*/
	public java.lang.String sealreason;
	/**盖章附件*/
	public java.lang.String sealname;
	/**附件ID*/
	public java.lang.String sealID;
	/**标题*/
	public java.lang.String name;
	
	
	
	public java.lang.String getBpmid() {
		return bpmid;
	}

	public void setBpmid(java.lang.String bpmid) {
		this.bpmid = bpmid;
	}

	public java.lang.String getPk_group() {
		return pk_group;
	}

	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	public java.lang.String getBilltype() {
		return billtype;
	}

	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	public java.lang.String getPk_org() {
		return pk_org;
	}

	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	public java.lang.String getContractcode() {
		return contractcode;
	}

	public void setContractcode(java.lang.String contractcode) {
		this.contractcode = contractcode;
	}

	public java.lang.String getContractname() {
		return contractname;
	}

	public void setContractname(java.lang.String contractname) {
		this.contractname = contractname;
	}

	public java.lang.String getProposer() {
		return proposer;
	}

	public void setProposer(java.lang.String proposer) {
		this.proposer = proposer;
	}

	public java.lang.String getApplicationtime() {
		return applicationtime;
	}

	public void setApplicationtime(java.lang.String applicationtime) {
		this.applicationtime = applicationtime;
	}

	public java.lang.String getApplicationcompany() {
		return applicationcompany;
	}

	public void setApplicationcompany(java.lang.String applicationcompany) {
		this.applicationcompany = applicationcompany;
	}

	public java.lang.String getApplicationdepartment() {
		return applicationdepartment;
	}

	public void setApplicationdepartment(java.lang.String applicationdepartment) {
		this.applicationdepartment = applicationdepartment;
	}

	public java.lang.String getSealer() {
		return sealer;
	}

	public void setSealer(java.lang.String sealer) {
		this.sealer = sealer;
	}

	public java.lang.String getSealreason() {
		return sealreason;
	}

	public void setSealreason(java.lang.String sealreason) {
		this.sealreason = sealreason;
	}

	public java.lang.String getSealname() {
		return sealname;
	}

	public void setSealname(java.lang.String sealname) {
		this.sealname = sealname;
	}

	public java.lang.String getSealID() {
		return sealID;
	}

	public void setSealID(java.lang.String sealID) {
		this.sealID = sealID;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}	
}
