package nc.vo.tg.outside;

import java.io.Serializable;

public class LLCtApLeaseconJsonVO implements Serializable {
	private String leasename; // ������
	private String pk_ebs; // ebs����
	private String leasearea; // �������
	private String begindate; // ���޿�ʼ��
	private String enddate; // ���޽�����
	private String isfixed; // �Ƿ�̶����
	private String remark; // ��������

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
