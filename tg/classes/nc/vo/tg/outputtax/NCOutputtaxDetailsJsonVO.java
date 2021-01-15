package nc.vo.tg.outputtax;

import java.io.Serializable;

public class NCOutputtaxDetailsJsonVO implements Serializable {

	/**
	 * 
	 */
	private static final double serialVersionUID = 1L;
	private String dtsGuid;//明细行流水号（唯一）,对应NC字段：发票表体ID
	private String goodsName;//商品名称
	private String standard;//规格型号
	private String unit;//单位
	private String number;//数量
	private String taxPrice;//含税单价（最多12位小数）
	private String withoutTaxPrice;//不含税单价（最多12位小数）
	private double amountWithTax;//含税金额（保留两位小数）
	private double amountWithoutTax;//不含税金额（保留两位小数）
	private int rate;//税率
	private String taxTypeCode;//税收分类编码
	private int discountFlag;//是否享受优惠（0：不享受；1：享受）  默认为不享受,地产板块开具补开营业税发票时需传值1
	private String discountType;//优惠政策类型 ,地产板块开具补开营业税发票时需传值：不征税
	private String discountNote;//优惠政策说明
	private String rowIndex;//行序
	private double taxAmount;//税额
	private int discountFlage;//折扣标记
	private double discountAmountWithoutTax;//折扣金额(不含税)
	private double discountRate;//折扣率
	private double discountTaxAmount;//折扣税额
	private String taxZeroRate;//零税率标志,空串:非零税率,0出口退税,1免税,2不征收,3普通零税率},地产板块开具补开营业税发票时需传值2
	public String getDtsGuid() {
		return dtsGuid;
	}
	public void setDtsGuid(String dtsGuid) {
		this.dtsGuid = dtsGuid;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getStandard() {
		return standard;
	}
	public void setStandard(String standard) {
		this.standard = standard;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getTaxPrice() {
		return taxPrice;
	}
	public void setTaxPrice(String taxPrice) {
		this.taxPrice = taxPrice;
	}
	public String getWithoutTaxPrice() {
		return withoutTaxPrice;
	}
	public void setWithoutTaxPrice(String withoutTaxPrice) {
		this.withoutTaxPrice = withoutTaxPrice;
	}
	public double getAmountWithTax() {
		return amountWithTax;
	}
	public void setAmountWithTax(double amountWithTax) {
		this.amountWithTax = amountWithTax;
	}
	public double getAmountWithoutTax() {
		return amountWithoutTax;
	}
	public void setAmountWithoutTax(double amountWithoutTax) {
		this.amountWithoutTax = amountWithoutTax;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public String getTaxTypeCode() {
		return taxTypeCode;
	}
	public void setTaxTypeCode(String taxTypeCode) {
		this.taxTypeCode = taxTypeCode;
	}
	public String getDiscountType() {
		return discountType;
	}
	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}
	public String getDiscountNote() {
		return discountNote;
	}
	public void setDiscountNote(String discountNote) {
		this.discountNote = discountNote;
	}
	public String getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(String rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getTaxZeroRate() {
		return taxZeroRate;
	}
	public void setTaxZeroRate(String taxZeroRate) {
		this.taxZeroRate = taxZeroRate;
	}
	public int getDiscountFlag() {
		return discountFlag;
	}
	public void setDiscountFlag(int discountFlag) {
		this.discountFlag = discountFlag;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public int getDiscountFlage() {
		return discountFlage;
	}
	public void setDiscountFlage(int discountFlage) {
		this.discountFlage = discountFlage;
	}
	public double getDiscountAmountWithoutTax() {
		return discountAmountWithoutTax;
	}
	public void setDiscountAmountWithoutTax(double discountAmountWithoutTax) {
		this.discountAmountWithoutTax = discountAmountWithoutTax;
	}
	public double getDiscountRate() {
		return discountRate;
	}
	public void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}
	public double getDiscountTaxAmount() {
		return discountTaxAmount;
	}
	public void setDiscountTaxAmount(double discountTaxAmount) {
		this.discountTaxAmount = discountTaxAmount;
	}
	
}
