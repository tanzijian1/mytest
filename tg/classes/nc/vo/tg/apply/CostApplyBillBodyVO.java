package nc.vo.tg.apply;

import java.io.Serializable;

/**
 * ��ϵͳ�������뵥���壨�ɱ���
 * 
 * @author lyq
 * 
 */
public class CostApplyBillBodyVO implements Serializable {
	private String costsubjects;// �ɱ���Ŀ
	private String subjcode;// ��֧��Ŀ
	private String accountsubjcode;// ��ƿ�Ŀ
	private String memo;// ժҪ
	private String proceedtype;// ��������
	private String businessformat;// ҵ̬
	private String scale;// ����
	private String notetype;// Ʊ������
	private String noteno;// Ʊ�ݺ�
	// private String objtype;// ��������
	private String supplier;// ʵ���տ
	// private String realitybankraccount;// ʵ���տ�����˻�
	private String bankraccountcode;// �տ�������к�
	// private String bankaccount;// �տ�˻�
	private String payamount;// ʵ�ʸ�����
	private String balatype;// ���㷽ʽ
	private String payaccount;// ���������˻�
	private String recaccount;// �տ������˻�
	private String castaccount;// �ֽ��˻�
	private String def48;// ˮ��ѽ��
	private String def49;// ������
	private String def38;// EBS��ID-def38
	private String def40;// EBS����-def40
	private String objtype;// ��������
	private String psndoc;// ҵ��Ա
	

	public String getObjtype() {
		return objtype;
	}

	public void setObjtype(String objtype) {
		this.objtype = objtype;
	}

	public String getPsndoc() {
		return psndoc;
	}

	public void setPsndoc(String psndoc) {
		this.psndoc = psndoc;
	}

	public String getDef38() {
		return def38;
	}

	public void setDef38(String def38) {
		this.def38 = def38;
	}

	public String getDef40() {
		return def40;
	}

	public void setDef40(String def40) {
		this.def40 = def40;
	}

	public String getDef48() {
		return def48;
	}

	public void setDef48(String def48) {
		this.def48 = def48;
	}

	public String getDef49() {
		return def49;
	}

	public void setDef49(String def49) {
		this.def49 = def49;
	}

	public String getNotetype() {
		return notetype;
	}

	public void setNotetype(String notetype) {
		this.notetype = notetype;
	}

	public String getNoteno() {
		return noteno;
	}

	public void setNoteno(String noteno) {
		this.noteno = noteno;
	}

	public String getAccountsubjcode() {
		return accountsubjcode;
	}

	public void setAccountsubjcode(String accountsubjcode) {
		this.accountsubjcode = accountsubjcode;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getBankraccountcode() {
		return bankraccountcode;
	}

	public void setBankraccountcode(String bankraccountcode) {
		this.bankraccountcode = bankraccountcode;
	}

	public String getProceedtype() {
		return proceedtype;
	}

	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}

	public String getCostsubjects() {
		return costsubjects;
	}

	public void setCostsubjects(String costsubjects) {
		this.costsubjects = costsubjects;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getPayamount() {
		return payamount;
	}

	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}

	public String getBalatype() {
		return balatype;
	}

	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}

	public String getPayaccount() {
		return payaccount;
	}

	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getCastaccount() {
		return castaccount;
	}

	public void setCastaccount(String castaccount) {
		this.castaccount = castaccount;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getBusinessformat() {
		return businessformat;
	}

	public void setBusinessformat(String businessformat) {
		this.businessformat = businessformat;
	}

	public String getScale() {
		return scale;
	}

	public void setScale(String scale) {
		this.scale = scale;
	}

}
