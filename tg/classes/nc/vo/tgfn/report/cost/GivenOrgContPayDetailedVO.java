package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * ��ݸԣ����ͬ������ϸ���ɱ�����
 * 
 * @author ASUS
 * 
 */
public class GivenOrgContPayDetailedVO implements Serializable, Cloneable {

	private String pk_org;//���㹫˾
	private String contracttype;//����
	private String contractcode;//��ͬ���
	private String contractname;//��ͬ����
	private String supplier;//��Ӧ������
	private String payregistercode;//����ǼǺ���
	private UFDouble payamount;//������
	private String paynum;//����
	private String paytype;//֧����ʽ
	private String stauts;//״̬
	private UFDouble pricetaxtotalamount;//��Ʊ���ɱ�+˰���˰�ϼƣ�
	private UFDouble pricenotaxtotalamount;//�ɱ�������˰��
	private String settlementrat;//˰��
	private UFDouble settlementamount;//˰��
	private String infodate;//�����·�
	private String vouchernum;//ƾ֤��
	private String trantype;//��������id
	private String memo;//��ע
	
	private String projectname;//��Ŀ����   //add by �ƹڻ� SDYC-391 �����������ͬ������ϸ���ɱ����������Ŀ�ֶ� 20200819
	
	
	public String getProjectname() {
		return projectname;
	}
	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}
	public String getSettlementrat() {
		return settlementrat;
	}
	public void setSettlementrat(String settlementrat) {
		this.settlementrat = settlementrat;
	}
	public String getTrantype() {
		return trantype;
	}
	public void setTrantype(String trantype) {
		this.trantype = trantype;
	}
	public String getPk_org() {
		return pk_org;
	}
	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}
	public String getContracttype() {
		return contracttype;
	}
	public void setContracttype(String contracttype) {
		this.contracttype = contracttype;
	}
	public String getContractcode() {
		return contractcode;
	}
	public void setContractcode(String contractcode) {
		this.contractcode = contractcode;
	}
	public String getContractname() {
		return contractname;
	}
	public void setContractname(String contractname) {
		this.contractname = contractname;
	}
	public String getSupplier() {
		return supplier;
	}
	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}
	public String getPayregistercode() {
		return payregistercode;
	}
	public void setPayregistercode(String payregistercode) {
		this.payregistercode = payregistercode;
	}
	public UFDouble getPayamount() {
		return payamount;
	}
	public void setPayamount(UFDouble payamount) {
		this.payamount = payamount;
	}
	public String getPaynum() {
		return paynum;
	}
	public void setPaynum(String paynum) {
		this.paynum = paynum;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	public String getStauts() {
		return stauts;
	}
	public void setStauts(String stauts) {
		this.stauts = stauts;
	}
	public UFDouble getPricetaxtotalamount() {
		return pricetaxtotalamount;
	}
	public void setPricetaxtotalamount(UFDouble pricetaxtotalamount) {
		this.pricetaxtotalamount = pricetaxtotalamount;
	}
	public UFDouble getPricenotaxtotalamount() {
		return pricenotaxtotalamount;
	}
	public void setPricenotaxtotalamount(UFDouble pricenotaxtotalamount) {
		this.pricenotaxtotalamount = pricenotaxtotalamount;
	}
	public UFDouble getSettlementamount() {
		return settlementamount;
	}
	public void setSettlementamount(UFDouble settlementamount) {
		this.settlementamount = settlementamount;
	}
	public String getInfodate() {
		return infodate;
	}
	public void setInfodate(String infodate) {
		this.infodate = infodate;
	}
	public String getVouchernum() {
		return vouchernum;
	}
	public void setVouchernum(String vouchernum) {
		this.vouchernum = vouchernum;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
	
	
}
