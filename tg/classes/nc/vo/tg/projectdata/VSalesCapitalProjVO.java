package nc.vo.tg.projectdata;

import nc.vo.pub.SuperVO;

/**
 * @author LJF
 *select * from sdc.v_sales_capital_proj@link_sale 
 *VO销售系统 视图的VO类  （住宅、商业、车位、办公、公建配套）
 */
public class VSalesCapitalProjVO extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8623434043916570676L;
	/**销售系统主键*/
	private String projid;
	/**编码*/
	private String projfcode;
	/**城市公司*/
	private String cityCompany;
	/**大项目*/
	private String broadHeading;
	/**项目分期*/
	private String projectPhases;
	/**报建用名*/
	private String establishName;
	/**营销用名*/
	private String marketingName;
	/**住宅认购均价*/
	private String averageHome;
	/**车位认购均价*/
	private String parkingSpaceAverage;
	/**商业认购均价*/
	private String commercialAverage;
	/**办公认购均价*/
	private String officeAverage;
	/**住宅总面积*/
	private String totalHome;
	/**车位总面积*/
	private String totalParking;
	/**商业总面积*/
	private String totalCommercia;
	/**办公总面积*/
	private String OfficeTotal;
	/**公建配套总面积*/
	private String publicTotaSacilities;
	/**住宅可售面积*/ 
	private String residentialSaleableArea;
	/**车位可售面积*/
	private String spaceAvailableForSale;
	/**商业可售面积*/
	private String commercialSaleableArea;
	/**办公可售面积*/
	private String officeSaleableSpace;
	/**公建配套可售面积*/
	private String publicSacilitiesSaleableArea;
	/**住宅已售面积_认购*/
	private String residentialSaleableAreaSub;
	/**车位已售面积_认购*/
	private String spaceAvailableForSaleSub;
	/**商业已售面积_认购*/
	private String commercialSaleableAreaSub;
	/**办公已售面积_认购*/
	private String officeSaleableSpaceSub;
	/**公建已售面积_认购*/
	private String publicSacilitiesSaleableAreaSub;
	/**住宅已售面积_签约*/
	private String residentialSaleableAreSigncon;
	/**车位已售面积_签约*/
	private String spaceAvailableForSaleSigncon;
	/**商业已售面积_签约*/
	private String commercialSaleableAreaSigncon;
	/**办公已售面积_签约*/
	private String officeSaleableSpaceSigncon;
	/**公建已售面积_签约*/
	private String publicSacilitiesSaleableAreaSigncon;
	/**项目总建面积*/
	private String projectTotalArea; 
	/**住宅回款金额*/
	private String homeReceivable;
	/**车位回款金额*/
	private String parkingReceivable;
	/**商业回款金额*/
	private String commercialReceivable;
	/**办公回款金额*/
	private String officeSReceivable;
	/**公建配套回款金额*/
	private String publicSacilitiesReceivable;
	/**销售回款金额*/
	private String AmountSalesProceeds;
	
	public String getTotalHome() {
		return totalHome;
	}
	public void setTotalHome(String totalHome) {
		this.totalHome = totalHome;
	}
	public String getTotalParking() {
		return totalParking;
	}
	public void setTotalParking(String totalParking) {
		this.totalParking = totalParking;
	}
	public String getTotalCommercia() {
		return totalCommercia;
	}
	public void setTotalCommercia(String totalCommercia) {
		this.totalCommercia = totalCommercia;
	}
	public String getOfficeTotal() {
		return OfficeTotal;
	}
	public void setOfficeTotal(String officeTotal) {
		OfficeTotal = officeTotal;
	}
	public String getPublicTotaSacilities() {
		return publicTotaSacilities;
	}
	public void setPublicTotaSacilities(String publicTotaSacilities) {
		this.publicTotaSacilities = publicTotaSacilities;
	}
	public String getHomeReceivable() {
		return homeReceivable;
	}
	public void setHomeReceivable(String homeReceivable) {
		this.homeReceivable = homeReceivable;
	}
	public String getParkingReceivable() {
		return parkingReceivable;
	}
	public void setParkingReceivable(String parkingReceivable) {
		this.parkingReceivable = parkingReceivable;
	}
	public String getCommercialReceivable() {
		return commercialReceivable;
	}
	public void setCommercialReceivable(String commercialReceivable) {
		this.commercialReceivable = commercialReceivable;
	}
	public String getOfficeSReceivable() {
		return officeSReceivable;
	}
	public void setOfficeSReceivable(String officeSReceivable) {
		this.officeSReceivable = officeSReceivable;
	}
	public String getPublicSacilitiesReceivable() {
		return publicSacilitiesReceivable;
	}
	public void setPublicSacilitiesReceivable(String publicSacilitiesReceivable) {
		this.publicSacilitiesReceivable = publicSacilitiesReceivable;
	}
	public String getProjid() {
		return projid;
	}
	public void setProjid(String projid) {
		this.projid = projid;
	}
	public String getProjfcode() {
		return projfcode;
	}
	public void setProjfcode(String projfcode) {
		this.projfcode = projfcode;
	}
	public String getCityCompany() {
		return cityCompany;
	}
	public void setCityCompany(String cityCompany) {
		this.cityCompany = cityCompany;
	}
	public String getBroadHeading() {
		return broadHeading;
	}
	public void setBroadHeading(String broadHeading) {
		this.broadHeading = broadHeading;
	}
	public String getProjectPhases() {
		return projectPhases;
	}
	public void setProjectPhases(String projectPhases) {
		this.projectPhases = projectPhases;
	}
	public String getEstablishName() {
		return establishName;
	}
	public void setEstablishName(String establishName) {
		this.establishName = establishName;
	}
	public String getMarketingName() {
		return marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}
	public String getAverageHome() {
		return averageHome;
	}
	public void setAverageHome(String averageHome) {
		this.averageHome = averageHome;
	}
	public String getParkingSpaceAverage() {
		return parkingSpaceAverage;
	}
	public void setParkingSpaceAverage(String parkingSpaceAverage) {
		this.parkingSpaceAverage = parkingSpaceAverage;
	}
	public String getCommercialAverage() {
		return commercialAverage;
	}
	public void setCommercialAverage(String commercialAverage) {
		this.commercialAverage = commercialAverage;
	}
	public String getOfficeAverage() {
		return officeAverage;
	}
	public void setOfficeAverage(String officeAverage) {
		this.officeAverage = officeAverage;
	}
	public String getResidentialSaleableArea() {
		return residentialSaleableArea;
	}
	public void setResidentialSaleableArea(String residentialSaleableArea) {
		this.residentialSaleableArea = residentialSaleableArea;
	}
	public String getSpaceAvailableForSale() {
		return spaceAvailableForSale;
	}
	public void setSpaceAvailableForSale(String spaceAvailableForSale) {
		this.spaceAvailableForSale = spaceAvailableForSale;
	}
	public String getCommercialSaleableArea() {
		return commercialSaleableArea;
	}
	public void setCommercialSaleableArea(String commercialSaleableArea) {
		this.commercialSaleableArea = commercialSaleableArea;
	}
	public String getOfficeSaleableSpace() {
		return officeSaleableSpace;
	}
	public void setOfficeSaleableSpace(String officeSaleableSpace) {
		this.officeSaleableSpace = officeSaleableSpace;
	}
	public String getPublicSacilitiesSaleableArea() {
		return publicSacilitiesSaleableArea;
	}
	public void setPublicSacilitiesSaleableArea(
			String publicSacilitiesSaleableArea) {
		this.publicSacilitiesSaleableArea = publicSacilitiesSaleableArea;
	}
	public String getResidentialSaleableAreaSub() {
		return residentialSaleableAreaSub;
	}
	public void setResidentialSaleableAreaSub(String residentialSaleableAreaSub) {
		this.residentialSaleableAreaSub = residentialSaleableAreaSub;
	}
	public String getSpaceAvailableForSaleSub() {
		return spaceAvailableForSaleSub;
	}
	public void setSpaceAvailableForSaleSub(String spaceAvailableForSaleSub) {
		this.spaceAvailableForSaleSub = spaceAvailableForSaleSub;
	}
	public String getCommercialSaleableAreaSub() {
		return commercialSaleableAreaSub;
	}
	public void setCommercialSaleableAreaSub(String commercialSaleableAreaSub) {
		this.commercialSaleableAreaSub = commercialSaleableAreaSub;
	}
	public String getOfficeSaleableSpaceSub() {
		return officeSaleableSpaceSub;
	}
	public void setOfficeSaleableSpaceSub(String officeSaleableSpaceSub) {
		this.officeSaleableSpaceSub = officeSaleableSpaceSub;
	}
	public String getPublicSacilitiesSaleableAreaSub() {
		return publicSacilitiesSaleableAreaSub;
	}
	public void setPublicSacilitiesSaleableAreaSub(
			String publicSacilitiesSaleableAreaSub) {
		this.publicSacilitiesSaleableAreaSub = publicSacilitiesSaleableAreaSub;
	}
	public String getResidentialSaleableAreSigncon() {
		return residentialSaleableAreSigncon;
	}
	public void setResidentialSaleableAreSigncon(
			String residentialSaleableAreSigncon) {
		this.residentialSaleableAreSigncon = residentialSaleableAreSigncon;
	}
	public String getSpaceAvailableForSaleSigncon() {
		return spaceAvailableForSaleSigncon;
	}
	public void setSpaceAvailableForSaleSigncon(
			String spaceAvailableForSaleSigncon) {
		this.spaceAvailableForSaleSigncon = spaceAvailableForSaleSigncon;
	}
	public String getCommercialSaleableAreaSigncon() {
		return commercialSaleableAreaSigncon;
	}
	public void setCommercialSaleableAreaSigncon(
			String commercialSaleableAreaSigncon) {
		this.commercialSaleableAreaSigncon = commercialSaleableAreaSigncon;
	}
	public String getOfficeSaleableSpaceSigncon() {
		return officeSaleableSpaceSigncon;
	}
	public void setOfficeSaleableSpaceSigncon(String officeSaleableSpaceSigncon) {
		this.officeSaleableSpaceSigncon = officeSaleableSpaceSigncon;
	}
	public String getPublicSacilitiesSaleableAreaSigncon() {
		return publicSacilitiesSaleableAreaSigncon;
	}
	public void setPublicSacilitiesSaleableAreaSigncon(
			String publicSacilitiesSaleableAreaSigncon) {
		this.publicSacilitiesSaleableAreaSigncon = publicSacilitiesSaleableAreaSigncon;
	}
	public String getProjectTotalArea() {
		return projectTotalArea;
	}
	public void setProjectTotalArea(String projectTotalArea) {
		this.projectTotalArea = projectTotalArea;
	}
	public String getAmountSalesProceeds() {
		return AmountSalesProceeds;
	}
	public void setAmountSalesProceeds(String amountSalesProceeds) {
		AmountSalesProceeds = amountSalesProceeds;
	}
}
