package nc.vo.tg.outputtax;

import java.io.Serializable;
import java.util.List;

/**
 * 外系统开票信息字段
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

	private String pk_org;// 组织
	private String pk_psndoc;// 人员
	private String pk_dept;// 部门
	private String saveSysBillid;// 单据流水号（唯一）
	private String saveSysBillno;// 单据编号
	private String sysname;// 外系统名称
	private int invType;// 0：增值税专用发票（当发票类型为0时，不允许开具零税率发票） 2：增值税普通发票 51：增值税电子普通票
	private String billType;// 单据类型（乐税可根据单据类型定义备注，对接时再约定值）
	private String billSource;// 单据来源（固定值，业务系统名称，传入NC）
	private String bizDate;// 业务日期（单据生成日期）
	private String buyName;// 购方名称（业主名称，以英文半角逗号’,’隔开）
	private String buyTaxNo;// 购方纳税人识别号（业主身份证，企业统一信用代码）
	private String buyBank;// 购方开户行
	private String buyAccount;// 账号
	private String buyAddr;// 购方地址
	private String buyTell;// 购方电话
	private String note;// 备注
	private String sellName;// 销方名称
	private String sellTaxNo;// 销方税号
	private String sellAddr;// 销方地址及电话
	private String sellTel;// 销方地址及电话
	private String sellBank;// 销方银行及账号
	private String sellAccount;
	private String goodsVersion;// 税收分类编码版本号（传默认值：33.0）
	private String cashier;// 收款人
	private String checker;// 复核人
	private String invoicer;// 开票人
	private double totalTaxAmount;// 合计税额（不传时，按明细税额累加）
	private double totalAmount;// 价税合计（不传时，按明细价税累加）
	private double totalAmountWithoutTax;// 不含税总额（不传时，按明细不含税金额累加）
	private String sourceInvCode;// 源正数发票代码,红冲票时必传
	private String sourceInvNo;// 源正数发票号码,红冲票时必传
	private int negativeFlag;// 值须为数值0（非红冲）或1（红冲），默认0
	private String negNativeNo;// 专用发票且是负数票时需要输入（需在开票软件中申请红字发票信息表，税局审核通过后可以获取到）
	private String invCode;// 发票代码
	private String invDate;// 开票日期
	private String invNo;// 发票号
	private int printType;// 打印类型
	private int confirmWin;// 是否显示打印对话框 0：不显示 1：显示 传固定值0
	private String billGuid;// NC单据主键
	private String def19;// 影像编码
	private String def20;// 影像状态
	private Boolean isExtraBill;// 是否补票
	private String serialNo;//开票机号

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
