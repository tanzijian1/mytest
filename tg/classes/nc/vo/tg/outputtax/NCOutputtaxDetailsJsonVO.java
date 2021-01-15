package nc.vo.tg.outputtax;

import java.io.Serializable;

public class NCOutputtaxDetailsJsonVO implements Serializable {

	/**
	 * 
	 */
	private static final double serialVersionUID = 1L;
	private String dtsGuid;//��ϸ����ˮ�ţ�Ψһ��,��ӦNC�ֶΣ���Ʊ����ID
	private String goodsName;//��Ʒ����
	private String standard;//����ͺ�
	private String unit;//��λ
	private String number;//����
	private String taxPrice;//��˰���ۣ����12λС����
	private String withoutTaxPrice;//����˰���ۣ����12λС����
	private double amountWithTax;//��˰��������λС����
	private double amountWithoutTax;//����˰��������λС����
	private int rate;//˰��
	private String taxTypeCode;//˰�շ������
	private int discountFlag;//�Ƿ������Żݣ�0�������ܣ�1�����ܣ�  Ĭ��Ϊ������,�ز���鿪�߲���Ӫҵ˰��Ʊʱ�贫ֵ1
	private String discountType;//�Ż��������� ,�ز���鿪�߲���Ӫҵ˰��Ʊʱ�贫ֵ������˰
	private String discountNote;//�Ż�����˵��
	private String rowIndex;//����
	private double taxAmount;//˰��
	private int discountFlage;//�ۿ۱��
	private double discountAmountWithoutTax;//�ۿ۽��(����˰)
	private double discountRate;//�ۿ���
	private double discountTaxAmount;//�ۿ�˰��
	private String taxZeroRate;//��˰�ʱ�־,�մ�:����˰��,0������˰,1��˰,2������,3��ͨ��˰��},�ز���鿪�߲���Ӫҵ˰��Ʊʱ�贫ֵ2
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
