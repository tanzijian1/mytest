package nc.vo.tg.outputtax;

import java.io.Serializable;
import java.util.List;

/**
 * ��ϵͳ��Ʊ��Ϣ�ֶ�
 * 
 * @author lhh
 * @since 2019-09-17
 * 
 */
public class NcOutputtaxJsonVO implements Serializable {

	/**
	 * 
	 */
	private static final double serialVersionUID = 1L;

	private String pk_org;// ��֯
	private String pk_psndoc;// ��Ա
	private String pk_dept;// ����
	private String saveSysBillid;// ������ˮ�ţ�Ψһ��
	private String saveSysBillno;// ���ݱ��
	private String sysname;// ��ϵͳ����
	private int invType;// 0����ֵ˰ר�÷�Ʊ������Ʊ����Ϊ0ʱ������������˰�ʷ�Ʊ�� 2����ֵ˰��ͨ��Ʊ 51����ֵ˰������ͨƱ
	private String billType;// �������ͣ���˰�ɸ��ݵ������Ͷ��屸ע���Խ�ʱ��Լ��ֵ��
	private String billSource;// ������Դ���̶�ֵ��ҵ��ϵͳ���ƣ�����NC��
	private String bizDate;// ҵ�����ڣ������������ڣ�
	private String buyName;// �������ƣ�ҵ�����ƣ���Ӣ�İ�Ƕ��š�,��������
	private String buyTaxNo;// ������˰��ʶ��ţ�ҵ�����֤����ҵͳһ���ô��룩
	private String buyBank;// ����������
	private String buyAccount;// �˺�
	private String buyAddr;// ������ַ
	private String buyTell;// �����绰
	private String note;// ��ע
	private String sellName;// ��������
	private String sellTaxNo;// ����˰��
	private String sellAddr;// ������ַ���绰
	private String sellTel;// ������ַ���绰
	private String sellBank;// �������м��˺�
	private String sellAccount;
	private String goodsVersion;// ˰�շ������汾�ţ���Ĭ��ֵ��33.0��
	private String cashier;// �տ���
	private String checker;// ������
	private String invoicer;// ��Ʊ��
	private double totalTaxAmount;// �ϼ�˰�����ʱ������ϸ˰���ۼӣ�
	private double totalAmount;// ��˰�ϼƣ�����ʱ������ϸ��˰�ۼӣ�
	private double totalAmountWithoutTax;// ����˰�ܶ����ʱ������ϸ����˰����ۼӣ�
	private String sourceInvCode;// Դ������Ʊ����,���Ʊʱ�ش�
	private String sourceInvNo;// Դ������Ʊ����,���Ʊʱ�ش�
	private int negativeFlag;// ֵ��Ϊ��ֵ0���Ǻ�壩��1����壩��Ĭ��0
	private String negNativeNo;// ר�÷�Ʊ���Ǹ���Ʊʱ��Ҫ���루���ڿ�Ʊ�����������ַ�Ʊ��Ϣ��˰�����ͨ������Ի�ȡ����
	private String invCode;// ��Ʊ����
	private String invDate;// ��Ʊ����
	private String invNo;// ��Ʊ��
	private int printType;// ��ӡ����
	private int confirmWin;// �Ƿ���ʾ��ӡ�Ի��� 0������ʾ 1����ʾ ���̶�ֵ0
	private String billGuid;// NC��������
	private String def19;// Ӱ�����
	private String def20;// Ӱ��״̬
	private Boolean isExtraBill;// �Ƿ�Ʊ
	private String serialNo;//��Ʊ����

	private NCOutputtaxDetailsJsonVO[] details;

	public String getBillType() {
		return billType;
	}

	public void setBillType(String billType) {
		this.billType = billType;
	}

	public String getBillSource() {
		return billSource;
	}

	public void setBillSource(String billSource) {
		this.billSource = billSource;
	}

	public String getBizDate() {
		return bizDate;
	}

	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}

	public String getBuyName() {
		return buyName;
	}

	public void setBuyName(String buyName) {
		this.buyName = buyName;
	}

	public String getBuyTaxNo() {
		return buyTaxNo;
	}

	public void setBuyTaxNo(String buyTaxNo) {
		this.buyTaxNo = buyTaxNo;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getSellName() {
		return sellName;
	}

	public void setSellName(String sellName) {
		this.sellName = sellName;
	}

	public String getSellTaxNo() {
		return sellTaxNo;
	}

	public void setSellTaxNo(String sellTaxNo) {
		this.sellTaxNo = sellTaxNo;
	}

	public String getGoodsVersion() {
		return goodsVersion;
	}

	public void setGoodsVersion(String goodsVersion) {
		this.goodsVersion = goodsVersion;
	}

	public String getCashier() {
		return cashier;
	}

	public void setCashier(String cashier) {
		this.cashier = cashier;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getInvoicer() {
		return invoicer;
	}

	public void setInvoicer(String invoicer) {
		this.invoicer = invoicer;
	}

	public String getSourceInvCode() {
		return sourceInvCode;
	}

	public void setSourceInvCode(String sourceInvCode) {
		this.sourceInvCode = sourceInvCode;
	}

	public String getSourceInvNo() {
		return sourceInvNo;
	}

	public void setSourceInvNo(String sourceInvNo) {
		this.sourceInvNo = sourceInvNo;
	}

	public int getNegativeFlag() {
		return negativeFlag;
	}

	public void setNegativeFlag(int negativeFlag) {
		this.negativeFlag = negativeFlag;
	}

	public String getNegNativeNo() {
		return negNativeNo;
	}

	public void setNegNativeNo(String negNativeNo) {
		this.negNativeNo = negNativeNo;
	}

	public String getInvCode() {
		return invCode;
	}

	public void setInvCode(String invCode) {
		this.invCode = invCode;
	}

	public String getInvDate() {
		return invDate;
	}

	public void setInvDate(String invDate) {
		this.invDate = invDate;
	}

	public String getInvNo() {
		return invNo;
	}

	public void setInvNo(String invNo) {
		this.invNo = invNo;
	}

	public int getInvType() {
		return invType;
	}

	public void setInvType(int invType) {
		this.invType = invType;
	}

	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public double getTotalAmountWithoutTax() {
		return totalAmountWithoutTax;
	}

	public void setTotalAmountWithoutTax(double totalAmountWithoutTax) {
		this.totalAmountWithoutTax = totalAmountWithoutTax;
	}

	public int getPrintType() {
		return printType;
	}

	public void setPrintType(int printType) {
		this.printType = printType;
	}

	public int getConfirmWin() {
		return confirmWin;
	}

	public void setConfirmWin(int confirmWin) {
		this.confirmWin = confirmWin;
	}

	public String getSaveSysBillid() {
		return saveSysBillid;
	}

	public void setSaveSysBillid(String saveSysBillid) {
		this.saveSysBillid = saveSysBillid;
	}

	public String getSaveSysBillno() {
		return saveSysBillno;
	}

	public void setSaveSysBillno(String saveSysBillno) {
		this.saveSysBillno = saveSysBillno;
	}

	public String getBuyAddr() {
		return buyAddr;
	}

	public void setBuyAddr(String buyAddr) {
		this.buyAddr = buyAddr;
	}

	public String getBuyTell() {
		return buyTell;
	}

	public void setBuyTell(String buyTell) {
		this.buyTell = buyTell;
	}

	public String getSellAddr() {
		return sellAddr;
	}

	public void setSellAddr(String sellAddr) {
		this.sellAddr = sellAddr;
	}

	public String getSellTel() {
		return sellTel;
	}

	public void setSellTel(String sellTel) {
		this.sellTel = sellTel;
	}

	public String getSellBank() {
		return sellBank;
	}

	public void setSellBank(String sellBank) {
		this.sellBank = sellBank;
	}

	public String getSellAccount() {
		return sellAccount;
	}

	public void setSellAccount(String sellAccount) {
		this.sellAccount = sellAccount;
	}

	public String getBuyBank() {
		return buyBank;
	}

	public void setBuyBank(String buyBank) {
		this.buyBank = buyBank;
	}

	public String getBuyAccount() {
		return buyAccount;
	}

	public void setBuyAccount(String buyAccount) {
		this.buyAccount = buyAccount;
	}

	public String getPk_org() {
		return pk_org;
	}

	public void setPk_org(String pk_org) {
		this.pk_org = pk_org;
	}

	public String getPk_psndoc() {
		return pk_psndoc;
	}

	public void setPk_psndoc(String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	public String getPk_dept() {
		return pk_dept;
	}

	public void setPk_dept(String pk_dept) {
		this.pk_dept = pk_dept;
	}

	public String getSysname() {
		return sysname;
	}

	public void setSysname(String sysname) {
		this.sysname = sysname;
	}

	public NCOutputtaxDetailsJsonVO[] getDetails() {
		return details;
	}

	public void setDetails(NCOutputtaxDetailsJsonVO[] details) {
		this.details = details;
	}

	public String getBillGuid() {
		return billGuid;
	}

	public void setBillGuid(String billGuid) {
		this.billGuid = billGuid;
	}

	public String getDef19() {
		return def19;
	}

	public void setDef19(String def19) {
		this.def19 = def19;
	}

	public String getDef20() {
		return def20;
	}

	public void setDef20(String def20) {
		this.def20 = def20;
	}

	public Boolean getIsExtraBill() {
		return isExtraBill;
	}

	public void setIsExtraBill(Boolean isExtraBill) {
		this.isExtraBill = isExtraBill;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

}
