package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

public class FinancingProVO implements Serializable, Cloneable {

	String pk_scheme;// ���ʷ�������
	String schemeno;// �������
	String nfintype;// ����������
	String pk_projectdata;// ��Ŀ����
	String proname;// ��Ŀ����
	String bpaydate;// �ؼ۸���ʱ��
	UFDouble paymny_amount;// �ؼ۸������
	// UFDouble summny_amount;// �ؼ۸����ܶ�
	// String twocompletetime;// ��֤��ȫʱ��
	// String threecompletetime;// ��֤��ȫʱ��
	String fourcompletetime;// ��֤��ȫʱ��
	UFDouble buildingarea_amount;// �ܽ������
	UFDouble exfin_amount;// �������ʽ��

	String norganization;// ���ʻ���
	UFDouble finmny_amount;// �����ʽ��
	String ismain;// �Ƿ�����

	String scheduletype;// ��������,0:ʵ�����ʱ��,1:���
	String note;//���ʷ����ı�ע

	// �������ȣ����У�
	String establishproject;// ����
	String bra_nchdate;// �����ϻ�
	String provi_ncedate;// ʡ���ϻ�
	String headquartersdate;// �����ϻ�
	String replydate;// ����ʱ��
	String costsigndate;// ��ͬǩ��ʱ��
	String remarkschedule;// ���ȱ�ע
	// �������ȣ������У�
	String nestablishproject;// ����
	String complete; // ��ɾ���
	String reviewmeeting;// �����
	String nreplydate;// ����
	String funderapproval;// �ʽ�����
	String record;// ����
	String signcontract;// ǩ��ͬ
	// �������ȣ��ʱ��г���
	String capitalcomplete;// ��ɾ���
	String submitexchange;// �ύ������
	String exchangepast;// ����������ͨ��
	String filesealing;// �ļ����
	String submitsfc;// �ύ֤���
	String sfcpast;// ֤���ͨ��
	String gainapproval;// ��ȡ����
	
	
	

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

	// ��дclone����
	public Object clone() throws CloneNotSupportedException {
		return super.clone();

	}

}
