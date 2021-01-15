package nc.vo.tg.outside.appaybill;

import java.io.Serializable;

public class PayBillItemVO implements Serializable {
	String supplier;// ʵ���տ supplier
	String subjcode;// ��ƿ�Ŀ subjcode
	String inoutbusiclass;// ��֧��Ŀ pk_subjcode
	String taxrate;// ˰�� taxrate
	String local_money_cr;// ��˰�ϼ�/��Ӧ�̿�Ʊ����˰�� local_money_cr
	String local_notax_cr;// ��˰���/��Ӧ�̿�Ʊ������˰�� local_notax_cr
	String local_tax_cr;// ˰�� /��Ӧ�̿�Ʊ��˰�
	String checktype;// Ʊ������ checktype
	String budgetsub;// Ԥ���Ŀ def1
	String paymenttype;// �������� def2
	String format;// ҵ̬ def3
	String formatratio;// ���� def4
	String recaccount;// �տ�������к� def6
	String costsubject;// �ɱ���Ŀ def7
	String invtype;// Ʊ������ def8
	String scomment;//ժҪ
	String  invoiceno;//��Ʊ��
	String offsetcompany;//�ֳ���˹�˾
	

	// SRM����
	
	String mny_accadj;// ���˵�������˰�� def10
	String mny_notax_accadj;// ���˵���������˰�� def11
	String tax_accadj;// ���˵�����˰� def12
	String mny_scm;// ��Ӧ������˰�� def13
	String mny_notax_scm;// ��Ӧ��������˰�� def14
	String tax_scm;// ��Ӧ����˰� def15
	String mny_evaluatecost;// �ݹ��ɱ� def16
	String mny_cost;// �ɱ� def20
	String accbodyid;// ��̨����id def21
	String evaluatecostbillno;// nc�ݹ�������� def22
	String accbillno;//���˵���
	String deduction;//�ۿ���
	String appliedamount;//������
	String purordercode;// �ɹ��������� def26
	String proejctdata;// ��Ŀ def27
	String invposted;// ��Ʊ�����˱�� def22
	String arrimoney;// �������ս�����˰�� def23
	
	
	
	public String getPurordercode() {
		return purordercode;
	}

	public void setPurordercode(String purordercode) {
		this.purordercode = purordercode;
	}

	public String getProejctdata() {
		return proejctdata;
	}

	public void setProejctdata(String proejctdata) {
		this.proejctdata = proejctdata;
	}

	public String getDeduction() {
		return deduction;
	}

	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}

	public String getAppliedamount() {
		return appliedamount;
	}

	public void setAppliedamount(String appliedamount) {
		this.appliedamount = appliedamount;
	}

	public String getScomment() {
		return scomment;
	}

	public void setScomment(String scomment) {
		this.scomment = scomment;
	}

	public String getInvoiceno() {
		return invoiceno;
	}

	public void setInvoiceno(String invoiceno) {
		this.invoiceno = invoiceno;
	}

	public String getOffsetcompany() {
		return offsetcompany;
	}

	public void setOffsetcompany(String offsetcompany) {
		this.offsetcompany = offsetcompany;
	}

	public String getInvtype() {
		return invtype;
	}

	public void setInvtype(String invtype) {
		this.invtype = invtype;
	}

	public String getLocal_tax_cr() {
		return local_tax_cr;
	}

	public void setLocal_tax_cr(String local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

	public String getSubjcode() {
		return subjcode;
	}

	public void setSubjcode(String subjcode) {
		this.subjcode = subjcode;
	}

	public String getInoutbusiclass() {
		return inoutbusiclass;
	}

	public void setInoutbusiclass(String inoutbusiclass) {
		this.inoutbusiclass = inoutbusiclass;
	}

	public String getTaxrate() {
		return taxrate;
	}

	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}

	public String getLocal_money_cr() {
		return local_money_cr;
	}

	public void setLocal_money_cr(String local_money_cr) {
		this.local_money_cr = local_money_cr;
	}

	public String getChecktype() {
		return checktype;
	}

	public void setChecktype(String checktype) {
		this.checktype = checktype;
	}

	public String getPaymenttype() {
		return paymenttype;
	}

	public void setPaymenttype(String paymenttype) {
		this.paymenttype = paymenttype;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getFormatratio() {
		return formatratio;
	}

	public void setFormatratio(String formatratio) {
		this.formatratio = formatratio;
	}

	public String getRecaccount() {
		return recaccount;
	}

	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}

	public String getCostsubject() {
		return costsubject;
	}

	public void setCostsubject(String costsubject) {
		this.costsubject = costsubject;
	}

	public String getBudgetsub() {
		return budgetsub;
	}

	public void setBudgetsub(String budgetsub) {
		this.budgetsub = budgetsub;
	}



	public String getLocal_notax_cr() {
		return local_notax_cr;
	}

	public void setLocal_notax_cr(String local_notax_cr) {
		this.local_notax_cr = local_notax_cr;
	}

	public String getMny_accadj() {
		return mny_accadj;
	}

	public void setMny_accadj(String mny_accadj) {
		this.mny_accadj = mny_accadj;
	}

	public String getMny_notax_accadj() {
		return mny_notax_accadj;
	}

	public void setMny_notax_accadj(String mny_notax_accadj) {
		this.mny_notax_accadj = mny_notax_accadj;
	}

	public String getTax_accadj() {
		return tax_accadj;
	}

	public void setTax_accadj(String tax_accadj) {
		this.tax_accadj = tax_accadj;
	}

	public String getMny_scm() {
		return mny_scm;
	}

	public void setMny_scm(String mny_scm) {
		this.mny_scm = mny_scm;
	}

	public String getMny_notax_scm() {
		return mny_notax_scm;
	}

	public void setMny_notax_scm(String mny_notax_scm) {
		this.mny_notax_scm = mny_notax_scm;
	}

	public String getTax_scm() {
		return tax_scm;
	}

	public void setTax_scm(String tax_scm) {
		this.tax_scm = tax_scm;
	}

	public String getMny_evaluatecost() {
		return mny_evaluatecost;
	}

	public void setMny_evaluatecost(String mny_evaluatecost) {
		this.mny_evaluatecost = mny_evaluatecost;
	}

	public String getMny_cost() {
		return mny_cost;
	}

	public void setMny_cost(String mny_cost) {
		this.mny_cost = mny_cost;
	}

	public String getAccbodyid() {
		return accbodyid;
	}

	public void setAccbodyid(String accbodyid) {
		this.accbodyid = accbodyid;
	}

	public String getEvaluatecostbillno() {
		return evaluatecostbillno;
	}

	public void setEvaluatecostbillno(String evaluatecostbillno) {
		this.evaluatecostbillno = evaluatecostbillno;
	}

	public String getAccbillno() {
		return accbillno;
	}

	public void setAccbillno(String accbillno) {
		this.accbillno = accbillno;
	}

	public String getInvposted() {
		return invposted;
	}

	public void setInvposted(String invposted) {
		this.invposted = invposted;
	}

	public String getArrimoney() {
		return arrimoney;
	}

	public void setArrimoney(String arrimoney) {
		this.arrimoney = arrimoney;
	}
	
	

}
