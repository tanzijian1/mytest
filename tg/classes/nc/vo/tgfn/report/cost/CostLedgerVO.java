package nc.vo.tgfn.report.cost;

import java.io.Serializable;

import nc.vo.pub.lang.UFDouble;

/**
 * 押金保证金台账（成本报表）
 * 
 * @author ASUS
 * 
 */
public class CostLedgerVO implements Serializable, Cloneable {
	private static final long serialVersionUID = 4244770136231438947L;
	/** 序号 **/
	private UFDouble sequeNumGlobal;
	
	/** 凭证主键**/
	private String pkVoucher ;
	
	/** 地区 **/
	private String area;
	
	/** 公司名称 **/
	private String companyName;
	
	/** 会计科目 **/
	private String accountingSubject;
	
	/** 客商 **/
	private String travellingTrader;
	
	/** 保证金或押金内容（凭证摘要） **/
	private String voucherAbstract;
	
	/** 支付金额 **/
	private UFDouble paymentAmount;
	
	/** 支付日期 **/
	private String paymentDate;
	
	/** 凭证制单人 **/
	private String proofMaker;
	
	/** 制单日期 **/
	private String documentationDate;
	
	/** 凭证号 **/
	private String proofNum;
	
	/** 经办部门 **/
	private String transactionDepartment;
	
	/** 经办人（退款责任人） **/
	private String transactionPerson;
	
	/** 退款到期日 **/
	private String refundDate;
	
	/** 来源系统 **/
	private String sourceSystem;
	
	/** 交易类型 **/
	private String tradeType;
	
	/** 单据编号 **/
	private String receiptNum;
	
	/** 影像编码 **/
	private String imageCode;
	
	/** 合同编码 **/
	private String contractCode;
	
	/** 合同名称 **/
	private String contractName;
	
	/** 外系统单据编号 **/
	private String outBillNo;
	
	/** 来源备注 **/
	private String sourceRemark;
	
	//add by 黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 begin
	private String assid;//辅助
	private String pk_org;//组织Pk
	private String pk_accasoa;//科目
	//add by 黄冠华 SDYC-56 押金保证金台账（成本报表）20200818 end
	
	private String balMny;//add by 黄冠华 添加科目余额字段 20201109

	public String getBalMny() {
		return balMny;
	}

	public void setBalMny(String balMny) {
		this.balMny = balMny;
	}

	public String getAssid() {
		return assid;
	}

	public void setAssid(String assid) {
		this.assid = assid;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_accasoa() {
		return pk_accasoa;
	}

	public void setPk_accasoa(String pk_accasoa) {
		this.pk_accasoa = pk_accasoa;
	}

	public UFDouble getSequeNumGlobal() {
		return sequeNumGlobal;
	}

	public void setSequeNumGlobal(UFDouble sequeNumGlobal) {
		this.sequeNumGlobal = sequeNumGlobal;
	}

	public String getPkVoucher() {
		return pkVoucher;
	}

	public void setPkVoucher(String pkVoucher) {
		this.pkVoucher = pkVoucher;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getAccountingSubject() {
		return accountingSubject;
	}

	public void setAccountingSubject(String accountingSubject) {
		this.accountingSubject = accountingSubject;
	}

	public String getTravellingTrader() {
		return travellingTrader;
	}

	public void setTravellingTrader(String travellingTrader) {
		this.travellingTrader = travellingTrader;
	}

	public String getVoucherAbstract() {
		return voucherAbstract;
	}

	public void setVoucherAbstract(String voucherAbstract) {
		this.voucherAbstract = voucherAbstract;
	}

	public UFDouble getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(UFDouble paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public String getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(String paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getProofMaker() {
		return proofMaker;
	}

	public void setProofMaker(String proofMaker) {
		this.proofMaker = proofMaker;
	}

	public String getDocumentationDate() {
		return documentationDate;
	}

	public void setDocumentationDate(String documentationDate) {
		this.documentationDate = documentationDate;
	}

	public String getProofNum() {
		return proofNum;
	}

	public void setProofNum(String proofNum) {
		this.proofNum = proofNum;
	}

	public String getTransactionDepartment() {
		return transactionDepartment;
	}

	public void setTransactionDepartment(String transactionDepartment) {
		this.transactionDepartment = transactionDepartment;
	}

	public String getTransactionPerson() {
		return transactionPerson;
	}

	public void setTransactionPerson(String transactionPerson) {
		this.transactionPerson = transactionPerson;
	}

	public String getRefundDate() {
		return refundDate;
	}

	public void setRefundDate(String refundDate) {
		this.refundDate = refundDate;
	}

	public String getSourceSystem() {
		return sourceSystem;
	}

	public void setSourceSystem(String sourceSystem) {
		this.sourceSystem = sourceSystem;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getReceiptNum() {
		return receiptNum;
	}

	public void setReceiptNum(String receiptNum) {
		this.receiptNum = receiptNum;
	}

	public String getImageCode() {
		return imageCode;
	}

	public void setImageCode(String imageCode) {
		this.imageCode = imageCode;
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

	public String getSourceRemark() {
		return sourceRemark;
	}

	public void setSourceRemark(String sourceRemark) {
		this.sourceRemark = sourceRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getOutBillNo() {
		return outBillNo;
	}

	public void setOutBillNo(String outBillNo) {
		this.outBillNo = outBillNo;
	}

	
}
