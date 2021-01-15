package nc.vo.tg.rz.report;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ���ʽ���
 * 
 * @author HUANGXJ
 * @date 2019��9��17��11:31:34
 */

public class FinancingProgressVO implements Serializable, Cloneable {
	String pk_projectdata;// ��Ŀ����
	String proname;// ��Ŀ����
	String nfintype;// ����������
	String paydate;// �ؼ۸���ʱ��
	UFDouble paymny;// �ؼ۸������
	UFDouble summny;// �ؼ۸����ܶ�
	String twocompletetime;// ��֤��ȫʱ��
	String threecompletetime;// ��֤��ȫʱ��
	String fourcompletetime;// ��֤��ȫʱ��
	UFDouble finmny_amount;// �����ʽ��
	String norganization;// ���ʻ���
	String ismain;// �Ƿ�����
	String bra_nchdate;// �����ϻ�
	String provi_ncedate;// ʡ���ϻ�
	String headquartersdate;// �����ϻ�
	String replydate;// ����ʱ��
	String costsigndate;// ��ͬǩ��ʱ��
	String remarkschedule;// ���ȱ�ע

	

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
