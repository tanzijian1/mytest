package nc.vo.tg.jkbx;

import java.io.Serializable;

/**
 * EBS预算预提单推生NC报销单表头VO
 * @author acer
 *
 */
public class OutsideJKBXHeadVO  implements Serializable{

	private String pk_org;//出账公司
	private String pk_tradetypeid;//交易类型
	private String djrq;//单据日期
	private String zyx6;//板块
	private String bzbm;//币种
	private String total;//本次冲预提金额金额
	private String fydwbm;//费用承担单位
	private String fydeptid;//费用承担部门
	private String zyx2;//合同编码
	private String zyx9;//合同名称
	private String zyx32;//合同ID
	private String zyx33;//合同类型
	private String zyx34;//合同细类
	private String jkbxr;//经办人
	private String deptid;//经办人部门
	private String dwbm_v;//经办人单位
	private String paytarget;//收款对象
	private String hbbm;//供应商
	
	private String zyx35;//ebs传入需要nc变更的状态
	private String zyx43;//是否审批
	private String zyx44;//NC报销单主键
	
	
	public String getZyx35() {
		return zyx35;
	}
	public void setZyx35(String zyx35) {
		this.zyx35 = zyx35;
	}
	public String getZyx43() {
		return zyx43;
	}
	public void setZyx43(String zyx43) {
		this.zyx43 = zyx43;
	}
	public String getZyx44() {
		return zyx44;
	}
	public void setZyx44(String zyx44) {
		this.zyx44 = zyx44;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getPk_tradetypeid() {
		return pk_tradetypeid;
	}
	public void setPk_tradetypeid(String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}
	public String getDjrq() {
		return djrq;
	}
	public void setDjrq(String djrq) {
		this.djrq = djrq;
	}
	public String getZyx6() {
		return zyx6;
	}
	public void setZyx6(String zyx6) {
		this.zyx6 = zyx6;
	}
	public String getBzbm() {
		return bzbm;
	}
	public void setBzbm(String bzbm) {
		this.bzbm = bzbm;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getFydwbm() {
		return fydwbm;
	}
	public void setFydwbm(String fydwbm) {
		this.fydwbm = fydwbm;
	}
	public String getFydeptid() {
		return fydeptid;
	}
	public void setFydeptid(String fydeptid) {
		this.fydeptid = fydeptid;
	}
	public String getZyx2() {
		return zyx2;
	}
	public void setZyx2(String zyx2) {
		this.zyx2 = zyx2;
	}
	public String getZyx9() {
		return zyx9;
	}
	public void setZyx9(String zyx9) {
		this.zyx9 = zyx9;
	}
	public String getZyx32() {
		return zyx32;
	}
	public void setZyx32(String zyx32) {
		this.zyx32 = zyx32;
	}
	public String getZyx33() {
		return zyx33;
	}
	public void setZyx33(String zyx33) {
		this.zyx33 = zyx33;
	}
	public String getZyx34() {
		return zyx34;
	}
	public void setZyx34(String zyx34) {
		this.zyx34 = zyx34;
	}
	public String getJkbxr() {
		return jkbxr;
	}
	public void setJkbxr(String jkbxr) {
		this.jkbxr = jkbxr;
	}
	public String getDeptid() {
		return deptid;
	}
	public void setDeptid(String deptid) {
		this.deptid = deptid;
	}
	public String getDwbm_v() {
		return dwbm_v;
	}
	public void setDwbm_v(String dwbm_v) {
		this.dwbm_v = dwbm_v;
	}
	public String getPaytarget() {
		return paytarget;
	}
	public void setPaytarget(String paytarget) {
		this.paytarget = paytarget;
	}
	public String getHbbm() {
		return hbbm;
	}
	public void setHbbm(String hbbm) {
		this.hbbm = hbbm;
	}
	
	
}
