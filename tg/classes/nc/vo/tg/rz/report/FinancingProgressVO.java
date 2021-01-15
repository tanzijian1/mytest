package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 融资进度
 * 
 * @author HUANGXJ
 * @date 2019年9月17日11:31:34
 */

public class FinancingProgressVO implements Serializable, Cloneable {
	String pk_projectdata;// 项目主键
	String proname;// 项目名称
	String nfintype;// 拟融资类型
	String paydate;// 地价付讫时间
	UFDouble paymny;// 地价付讫金额
	UFDouble summny;// 地价付讫总额
	String twocompletetime;// 两证齐全时间
	String threecompletetime;// 三证齐全时间
	String fourcompletetime;// 四证齐全时间
	UFDouble finmny_amount;// 拟融资金额
	String norganization;// 融资机构
	String ismain;// 是否主推
	String bra_nchdate;// 分行上会
	String provi_ncedate;// 省行上会
	String headquartersdate;// 总行上会
	String replydate;// 批复时间
	String costsigndate;// 合同签定时间
	String remarkschedule;// 进度备注

	

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getPaydate() {
		return paydate;
	}

	public void setPaydate(String paydate) {
		this.paydate = paydate;
	}

	public String getTwocompletetime() {
		return twocompletetime;
	}

	public void setTwocompletetime(String twocompletetime) {
		this.twocompletetime = twocompletetime;
	}

	public String getThreecompletetime() {
		return threecompletetime;
	}

	public void setThreecompletetime(String threecompletetime) {
		this.threecompletetime = threecompletetime;
	}

	public String getFourcompletetime() {
		return fourcompletetime;
	}

	public void setFourcompletetime(String fourcompletetime) {
		this.fourcompletetime = fourcompletetime;
	}

	public UFDouble getFinmny_amount() {
		return finmny_amount;
	}

	public void setFinmny_amount(UFDouble finmny_amount) {
		this.finmny_amount = finmny_amount;
	}

	public String getOrganization() {
		return norganization;
	}

	public String getNfintype() {
		return nfintype;
	}

	public void setNfintype(String nfintype) {
		this.nfintype = nfintype;
	}

	public String getNorganization() {
		return norganization;
	}

	public void setNorganization(String norganization) {
		this.norganization = norganization;
	}

	public String getIsmain() {
		return ismain;
	}

	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}

	public String getBra_nchdate() {
		return bra_nchdate;
	}

	public void setBra_nchdate(String bra_nchdate) {
		this.bra_nchdate = bra_nchdate;
	}

	public String getProvi_ncedate() {
		return provi_ncedate;
	}

	public void setProvi_ncedate(String provi_ncedate) {
		this.provi_ncedate = provi_ncedate;
	}

	public String getHeadquartersdate() {
		return headquartersdate;
	}

	public void setHeadquartersdate(String headquartersdate) {
		this.headquartersdate = headquartersdate;
	}

	public String getReplydate() {
		return replydate;
	}

	public void setReplydate(String replydate) {
		this.replydate = replydate;
	}

	public String getCostsigndate() {
		return costsigndate;
	}

	public void setCostsigndate(String costsigndate) {
		this.costsigndate = costsigndate;
	}

	public String getRemarkschedule() {
		return remarkschedule;
	}

	public void setRemarkschedule(String remarkschedule) {
		this.remarkschedule = remarkschedule;
	}

	public String getPk_projectdata() {
		return pk_projectdata;
	}

	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}

	public UFDouble getPaymny() {
		return paymny;
	}

	public void setPaymny(UFDouble paymny) {
		this.paymny = paymny;
	}

	public UFDouble getSummny() {
		return summny;
	}

	public void setSummny(UFDouble summny) {
		this.summny = summny;
	}

}
