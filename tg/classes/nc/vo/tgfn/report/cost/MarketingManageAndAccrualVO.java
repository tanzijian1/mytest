package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * Ӫ����������ú�Ʊ��Ȩ��������ϸ ʵ��
 * 
 * @author Administrator
 */
public class MarketingManageAndAccrualVO implements Serializable, Cloneable {

	private static final long serialVersionUID = 5351825664889742635L;
	
	/**
	 * ����
	 */
	private String region;
	
	/**
	 * ���˹�˾
	 */
	private String outAccountCompany;
	
	/**
	 * ������
	 */
	private String years;
	private String months;
	private String dates;
	
	/**
	 * ��Ŀ����
	 */
	private String courseTitle;
	
	/**
	 * ���̸�����������
	 */
	private String merchantAssistantAccountingName;
	/**
	 * ƾ֤��
	 */
	private String certificateNumber;
	/**
	 * ƾ֤ժҪ
	 */
	private String certificateDigest;
	/**
	 * ֧�����
	 */
	private UFDouble payAmount; 
	/**
	 * ��Ʊ���
	 */
	private UFDouble invoiceAmount; 
	/**
	 * δ�ջط�Ʊ���
	 */
	private UFDouble notTakebackInvoiceAmount;
	
	/**
	 * �Ƶ���
	 */
	private String certificateMaker;
	
	/**
	 * ������
	 */
	private String billmaker;
	
	/**
	 * ���ݱ��
	 */
	private String billno;
	
	/**
	 * ��ͬ����
	 */
	private String contractCode;
	
	/**
	 * ��ͬ����
	 */
	private String contractName;
	
	/**
	 * ��ͬ˰��
	 */
	private String contractRate;
	
	/**
	 * �������
	 */
	private String costType;
	
	/**
	 * �ȸ����Ʊ���
	 */
	private String payTicketTag;
	
	/**
	 * Ȩ�����Ʊ��
	 */
	private String accrualTag;
	
	/**
	 * ��˾��֯
	 * @return
	 */
	private String pk_org;
	/**
	 * ���
	 * @return
	 */
	private UFDouble sortnumglobal;
	
	private String billtype;//��������
	
	private String outsideno;//��ϵͳ���ݺ�
	
	private String billid;//ҵ�񵥾�pk
	

	public String getBillid() {
		return billid;
	}

	public void setBillid(String billid) {
		this.billid = billid;
	}

	public String getBilltype() {
		return billtype;
	}

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getOutsideno() {
		return outsideno;
	}

	public void setOutsideno(String outsideno) {
		this.outsideno = outsideno;
	}

	public UFDouble getSortnumglobal() {
		return sortnumglobal;
	}

	public void setSortnumglobal(UFDouble sortnumglobal) {
		this.sortnumglobal = sortnumglobal;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getOutAccountCompany() {
		return outAccountCompany;
	}

	public void setOutAccountCompany(String outAccountCompany) {
		this.outAccountCompany = outAccountCompany;
	}

	public String getYears() {
		return years;
	}

	public void setYears(String years) {
		this.years = years;
	}

	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}

	public String getDates() {
		return dates;
	}

	public void setDates(String dates) {
		this.dates = dates;
	}

	public String getCourseTitle() {
		return courseTitle;
	}

	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}

	public String getMerchantAssistantAccountingName() {
		return merchantAssistantAccountingName;
	}

	public void setMerchantAssistantAccountingName(String merchantAssistantAccountingName) {
		this.merchantAssistantAccountingName = merchantAssistantAccountingName;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getCertificateDigest() {
		return certificateDigest;
	}

	public void setCertificateDigest(String certificateDigest) {
		this.certificateDigest = certificateDigest;
	}

	public UFDouble getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(UFDouble payAmount) {
		this.payAmount = payAmount;
	}

	public UFDouble getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(UFDouble invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public UFDouble getNotTakebackInvoiceAmount() {
		return notTakebackInvoiceAmount;
	}

	public void setNotTakebackInvoiceAmount(UFDouble notTakebackInvoiceAmount) {
		this.notTakebackInvoiceAmount = notTakebackInvoiceAmount;
	}

	public String getCertificateMaker() {
		return certificateMaker;
	}

	public void setCertificateMaker(String certificateMaker) {
		this.certificateMaker = certificateMaker;
	}

	public String getBillmaker() {
		return billmaker;
	}

	public void setBillmaker(String billmaker) {
		this.billmaker = billmaker;
	}

	public String getBillno() {
		return billno;
	}

	public void setBillno(String billno) {
		this.billno = billno;
	}

	public String getContractCode() {
		return contractCode;
	}

	public void setContractCode(String contractCode) {
		this.contractCode = contractCode;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	public String getContractRate() {
		return contractRate;
	}

	public void setContractRate(String contractRate) {
		this.contractRate = contractRate;
	}

	public String getCostType() {
		return costType;
	}

	public void setCostType(String costType) {
		this.costType = costType;
	}

	public String getAccrualTag() {
		return accrualTag;
	}

	public void setAccrualTag(String accrualTag) {
		this.accrualTag = accrualTag;
	}

	public String getPayTicketTag() {
		return payTicketTag;
	}

	public void setPayTicketTag(String payTicketTag) {
		this.payTicketTag = payTicketTag;
	}
}
