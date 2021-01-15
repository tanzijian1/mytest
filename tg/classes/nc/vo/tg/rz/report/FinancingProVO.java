package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

public class FinancingProVO implements Serializable, Cloneable {

	String pk_scheme;// 融资方案主键
	String schemeno;// 方案编号
	String nfintype;// 拟融资类型
	String pk_projectdata;// 项目主键
	String proname;// 项目名称
	String bpaydate;// 地价付讫时间
	UFDouble paymny_amount;// 地价付讫金额
	// UFDouble summny_amount;// 地价付讫总额
	// String twocompletetime;// 两证齐全时间
	// String threecompletetime;// 三证齐全时间
	String fourcompletetime;// 四证齐全时间
	UFDouble buildingarea_amount;// 总建筑面积
	UFDouble exfin_amount;// 考核融资金额

	String norganization;// 融资机构
	UFDouble finmny_amount;// 拟融资金额
	String ismain;// 是否主推

	String scheduletype;// 进度类型,0:实际完成时间,1:差额
	String note;//融资方案的备注

	// 方案进度（银行）
	String establishproject;// 立项
	String bra_nchdate;// 分行上会
	String provi_ncedate;// 省行上会
	String headquartersdate;// 总行上会
	String replydate;// 批复时间
	String costsigndate;// 合同签定时间
	String remarkschedule;// 进度备注
	// 方案进度（非银行）
	String nestablishproject;// 立项
	String complete; // 完成尽调
	String reviewmeeting;// 评审会
	String nreplydate;// 批复
	String funderapproval;// 资金方批复
	String record;// 备案
	String signcontract;// 签合同
	// 方案进度（资本市场）
	String capitalcomplete;// 完成尽调
	String submitexchange;// 提交交易所
	String exchangepast;// 交易所审批通过
	String filesealing;// 文件封卷
	String submitsfc;// 提交证监会
	String sfcpast;// 证监会通过
	String gainapproval;// 获取批文
	
	
	

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getScheduletype() {
		return scheduletype;
	}

	public void setScheduletype(String scheduletype) {
		this.scheduletype = scheduletype;
	}

	public String getPk_scheme() {
		return pk_scheme;
	}

	public void setPk_scheme(String pk_scheme) {
		this.pk_scheme = pk_scheme;
	}

	public String getSchemeno() {
		return schemeno;
	}

	public void setSchemeno(String schemeno) {
		this.schemeno = schemeno;
	}

	public String getNfintype() {
		return nfintype;
	}

	public void setNfintype(String nfintype) {
		this.nfintype = nfintype;
	}

	public String getPk_projectdata() {
		return pk_projectdata;
	}

	public void setPk_projectdata(String pk_projectdata) {
		this.pk_projectdata = pk_projectdata;
	}

	public String getProname() {
		return proname;
	}

	public void setProname(String proname) {
		this.proname = proname;
	}

	public String getBpaydate() {
		return bpaydate;
	}

	public void setBpaydate(String bpaydate) {
		this.bpaydate = bpaydate;
	}

	public UFDouble getPaymny_amount() {
		return paymny_amount;
	}

	public void setPaymny_amount(UFDouble paymny_amount) {
		this.paymny_amount = paymny_amount;
	}

	public String getFourcompletetime() {
		return fourcompletetime;
	}

	public void setFourcompletetime(String fourcompletetime) {
		this.fourcompletetime = fourcompletetime;
	}

	public UFDouble getBuildingarea_amount() {
		return buildingarea_amount;
	}

	public void setBuildingarea_amount(UFDouble buildingarea_amount) {
		this.buildingarea_amount = buildingarea_amount;
	}

	public UFDouble getExfin_amount() {
		return exfin_amount;
	}

	public void setExfin_amount(UFDouble exfin_amount) {
		this.exfin_amount = exfin_amount;
	}

	public String getNorganization() {
		return norganization;
	}

	public void setNorganization(String norganization) {
		this.norganization = norganization;
	}

	public UFDouble getFinmny_amount() {
		return finmny_amount;
	}

	public void setFinmny_amount(UFDouble finmny_amount) {
		this.finmny_amount = finmny_amount;
	}

	public String getIsmain() {
		return ismain;
	}

	public void setIsmain(String ismain) {
		this.ismain = ismain;
	}

	public String getEstablishproject() {
		return establishproject;
	}

	public void setEstablishproject(String establishproject) {
		this.establishproject = establishproject;
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

	public String getNestablishproject() {
		return nestablishproject;
	}

	public void setNestablishproject(String nestablishproject) {
		this.nestablishproject = nestablishproject;
	}

	public String getComplete() {
		return complete;
	}

	public void setComplete(String complete) {
		this.complete = complete;
	}

	public String getReviewmeeting() {
		return reviewmeeting;
	}

	public void setReviewmeeting(String reviewmeeting) {
		this.reviewmeeting = reviewmeeting;
	}

	public String getNreplydate() {
		return nreplydate;
	}

	public void setNreplydate(String nreplydate) {
		this.nreplydate = nreplydate;
	}

	public String getFunderapproval() {
		return funderapproval;
	}

	public void setFunderapproval(String funderapproval) {
		this.funderapproval = funderapproval;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getSigncontract() {
		return signcontract;
	}

	public void setSigncontract(String signcontract) {
		this.signcontract = signcontract;
	}

	public String getCapitalcomplete() {
		return capitalcomplete;
	}

	public void setCapitalcomplete(String capitalcomplete) {
		this.capitalcomplete = capitalcomplete;
	}

	public String getSubmitexchange() {
		return submitexchange;
	}

	public void setSubmitexchange(String submitexchange) {
		this.submitexchange = submitexchange;
	}

	public String getExchangepast() {
		return exchangepast;
	}

	public void setExchangepast(String exchangepast) {
		this.exchangepast = exchangepast;
	}

	public String getFilesealing() {
		return filesealing;
	}

	public void setFilesealing(String filesealing) {
		this.filesealing = filesealing;
	}

	public String getSubmitsfc() {
		return submitsfc;
	}

	public void setSubmitsfc(String submitsfc) {
		this.submitsfc = submitsfc;
	}

	public String getSfcpast() {
		return sfcpast;
	}

	public void setSfcpast(String sfcpast) {
		this.sfcpast = sfcpast;
	}

	public String getGainapproval() {
		return gainapproval;
	}

	public void setGainapproval(String gainapproval) {
		this.gainapproval = gainapproval;
	}

	// 重写clone方法
	public Object clone() throws CloneNotSupportedException {
		return super.clone();

	}

}
