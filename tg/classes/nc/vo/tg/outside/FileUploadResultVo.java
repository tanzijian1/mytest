package nc.vo.tg.outside;

import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDouble;

public class FileUploadResultVo extends SuperVO{
	private static final long serialVersionUID = 1L;
	
	public String pk_upload;//����
	public String billno; //���ݱ��
	public String file_name;	//�ļ�����
	public String file_size; //�ļ���С
	public String creator;	//�ϴ���
	public String check_status;	//����״̬
	public String invoicecode; //��Ʊ����
	public String invoiceckcode; //У����
	public String invoicenum;	//��Ʊ����
	public String invoicedate; //��Ʊ����
	public String pdfuuid;	//Ӱ�񸽼�����
	public String invoice_type; //��Ʊ����
	public UFDouble tax_included_money;	//��˰���
	public UFDouble tax_money;	//˰��
	public UFDouble tax_notincluded_money;	//����˰���
	public String ts;	//��������
	public String upload_status; //�ϴ�״̬��0Ϊ�ɹ���1Ϊʧ�ܣ�
	public String tax_rate; //˰��
	
	public String pk_org;
	public String barcode;
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	
	
	public String getTax_rate() {
		return tax_rate;
	}
	public void setTax_rate(String tax_rate) {
		this.tax_rate = tax_rate;
	}
	public String getUpload_status() {
		return upload_status;
	}
	public void setUpload_status(String upload_status) {
		this.upload_status = upload_status;
	}
	public String getPk_upload() {
		return pk_upload;
	}
	public void setPk_upload(String pk_upload) {
		this.pk_upload = pk_upload;
	}
	public String getBillno() {
		return billno;
	}
	public void setBillno(String billno) {
		this.billno = billno;
	}
	public String getFile_name() {
		return file_name;
	}
	public void setFile_name(String file_name) {
		this.file_name = file_name;
	}
	public String getFile_size() {
		return file_size;
	}
	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCheck_status() {
		return check_status;
	}
	public void setCheck_status(String check_status) {
		this.check_status = check_status;
	}
	public String getInvoicecode() {
		return invoicecode;
	}
	public void setInvoicecode(String invoicecode) {
		this.invoicecode = invoicecode;
	}
	public String getInvoiceckcode() {
		return invoiceckcode;
	}
	public void setInvoiceckcode(String invoiceckcode) {
		this.invoiceckcode = invoiceckcode;
	}
	public String getInvoicenum() {
		return invoicenum;
	}
	public void setInvoicenum(String invoicenum) {
		this.invoicenum = invoicenum;
	}
	public String getInvoicedate() {
		return invoicedate;
	}
	public void setInvoicedate(String invoicedate) {
		this.invoicedate = invoicedate;
	}
	public String getPdfuuid() {
		return pdfuuid;
	}
	public void setPdfuuid(String pdfuuid) {
		this.pdfuuid = pdfuuid;
	}
	public String getInvoice_type() {
		return invoice_type;
	}
	public void setInvoice_type(String invoice_type) {
		this.invoice_type = invoice_type;
	}
	public UFDouble getTax_included_money() {
		return tax_included_money;
	}
	public void setTax_included_money(UFDouble tax_included_money) {
		this.tax_included_money = tax_included_money;
	}
	public UFDouble getTax_money() {
		return tax_money;
	}
	public void setTax_money(UFDouble tax_money) {
		this.tax_money = tax_money;
	}
	public UFDouble getTax_notincluded_money() {
		return tax_notincluded_money;
	}
	public void setTax_notincluded_money(UFDouble tax_notincluded_money) {
		this.tax_notincluded_money = tax_notincluded_money;
	}
	public String getTs() {
		return ts;
	}
	public void setTs(String ts) {
		this.ts = ts;
	}
	public String getTableName() {
		return "tg_guoxinfileuploadresult";
	}
	
	

}
