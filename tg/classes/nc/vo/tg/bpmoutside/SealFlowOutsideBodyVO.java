package nc.vo.tg.bpmoutside;

import nc.vo.pub.SuperVO;

public class SealFlowOutsideBodyVO extends SuperVO{

	/**
	 * 
	 * BPM推送过来的盖章资料表体数据VO 
	 *
	 */
	private static final long serialVersionUID = 2157957312130134789L;
	/**盖章文件*/
	public java.lang.String sealfile;
	/**一级目录*/
	public java.lang.String primarydirectory;
	/**二级目录*/
	public java.lang.String secondarydirectory;
	/**三级目录*/ 
	public java.lang.String threedirectories;
	/**印章类型*/
	public java.lang.String sealtype;
	/**公司名称全称*/
	public java.lang.String companyname;
	/**数量*/
	public java.lang.String number;
	/**备注*/
	public java.lang.String remark;
	public java.lang.String getSealfile() {
		return sealfile;
	}
	public void setSealfile(java.lang.String sealfile) {
		this.sealfile = sealfile;
	}
	public java.lang.String getPrimarydirectory() {
		return primarydirectory;
	}
	public void setPrimarydirectory(java.lang.String primarydirectory) {
		this.primarydirectory = primarydirectory;
	}
	public java.lang.String getSecondarydirectory() {
		return secondarydirectory;
	}
	public void setSecondarydirectory(java.lang.String secondarydirectory) {
		this.secondarydirectory = secondarydirectory;
	}
	public java.lang.String getThreedirectories() {
		return threedirectories;
	}
	public void setThreedirectories(java.lang.String threedirectories) {
		this.threedirectories = threedirectories;
	}
	public java.lang.String getSealtype() {
		return sealtype;
	}
	public void setSealtype(java.lang.String sealtype) {
		this.sealtype = sealtype;
	}
	public java.lang.String getCompanyname() {
		return companyname;
	}
	public void setCompanyname(java.lang.String companyname) {
		this.companyname = companyname;
	}
	public java.lang.String getNumber() {
		return number;
	}
	public void setNumber(java.lang.String number) {
		this.number = number;
	}
	public java.lang.String getRemark() {
		return remark;
	}
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
