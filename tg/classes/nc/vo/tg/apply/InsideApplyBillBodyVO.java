package nc.vo.tg.apply;

import java.io.Serializable;
/**
 *  ��ϵͳ�������뵥���壨�ڲ����ף�
 * @author lyq
 *
 */
public class InsideApplyBillBodyVO implements Serializable {
	private String cusaccount;// ���������˻�
	private String memo;// ժҪ
	private String payaccount;// �����˺�
	private String balatype;// ���㷽ʽ
	private String notetype;// Ʊ������
	private String payamount;//��˰���
	private String taxrate;// ˰��
	private String taxamount;// ˰��
	private String total;// ��˰�ϼ�
	private String supplier;//ʵ���տ 
	private String proceedtype;//��������
	private String bidnumber;//������
	private String bidtitle;//�б�����
	private String reaccountno;//�տ�������к�
	private String recaccount;//�տ�����˻�
	private String company;//��¥������˾
	public String getCusaccount() {
		return cusaccount;
	}
	public void setCusaccount(String cusaccount) {
		this.cusaccount = cusaccount;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getPayaccount() {
		return payaccount;
	}
	public void setPayaccount(String payaccount) {
		this.payaccount = payaccount;
	}
	public String getBalatype() {
		return balatype;
	}
	public void setBalatype(String balatype) {
		this.balatype = balatype;
	}
	public String getNotetype() {
		return notetype;
	}
	public void setNotetype(String notetype) {
		this.notetype = notetype;
	}
	public String getTaxrate() {
		return taxrate;
	}
	public void setTaxrate(String taxrate) {
		this.taxrate = taxrate;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getProceedtype() {
		return proceedtype;
	}
	public void setProceedtype(String proceedtype) {
		this.proceedtype = proceedtype;
	}
	public String getBidtitle() {
		return bidtitle;
	}
	public void setBidtitle(String bidtitle) {
		this.bidtitle = bidtitle;
	}
	public String getBidnumber() {
		return bidnumber;
	}
	public void setBidnumber(String bidnumber) {
		this.bidnumber = bidnumber;
	}
	public String getPayamount() {
		return payamount;
	}
	public void setPayamount(String payamount) {
		this.payamount = payamount;
	}
	public String getTaxamount() {
		return taxamount;
	}
	public void setTaxamount(String taxamount) {
		this.taxamount = taxamount;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getReaccountno() {
		return reaccountno;
	}
	public void setReaccountno(String reaccountno) {
		this.reaccountno = reaccountno;
	}
	public String getRecaccount() {
		return recaccount;
	}
	public void setRecaccount(String recaccount) {
		this.recaccount = recaccount;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

	

}
