package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApLeaseconJsonVO implements Serializable {
	private String leasename; // 租赁物
	private String pk_ebs; // ebs主键
	private String leasearea; // 租赁面积
	private String begindate; // 租赁开始日
	private String enddate; // 租赁结束日
	private String isfixed; // 是否固定租金
	private String remark; // 具体条款

	public String getLeasename() {
		return leasename;
	}

	public void setLeasename(String leasename) {
		this.leasename = leasename;
	}

	public String getPk_ebs() {
		return pk_ebs;
	}

	public void setPk_ebs(String pk_ebs) {
		this.pk_ebs = pk_ebs;
	}

	public String getLeasearea() {
		return leasearea;
	}

	public void setLeasearea(String leasearea) {
		this.leasearea = leasearea;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getIsfixed() {
		return isfixed;
	}

	public void setIsfixed(String isfixed) {
		this.isfixed = isfixed;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
