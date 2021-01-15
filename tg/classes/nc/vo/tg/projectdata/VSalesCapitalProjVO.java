package nc.vo.tg.projectdata;

import nc.vo.pub.SuperVO;

/**
 * @author LJF
 *select * from sdc.v_sales_capital_proj@link_sale 
 *VO����ϵͳ ��ͼ��VO��  ��סլ����ҵ����λ���칫���������ף�
 */
public class VSalesCapitalProjVO extends SuperVO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8623434043916570676L;
	/**����ϵͳ����*/
	private String projid;
	/**����*/
	private String projfcode;
	/**���й�˾*/
	private String cityCompany;
	/**����Ŀ*/
	private String broadHeading;
	/**��Ŀ����*/
	private String projectPhases;
	/**��������*/
	private String establishName;
	/**Ӫ������*/
	private String marketingName;
	/**סլ�Ϲ�����*/
	private String averageHome;
	/**��λ�Ϲ�����*/
	private String parkingSpaceAverage;
	/**��ҵ�Ϲ�����*/
	private String commercialAverage;
	/**�칫�Ϲ�����*/
	private String officeAverage;
	/**סլ�����*/
	private String totalHome;
	/**��λ�����*/
	private String totalParking;
	/**��ҵ�����*/
	private String totalCommercia;
	/**�칫�����*/
	private String OfficeTotal;
	/**�������������*/
	private String publicTotaSacilities;
	/**סլ�������*/ 
	private String residentialSaleableArea;
	/**��λ�������*/
	private String spaceAvailableForSale;
	/**��ҵ�������*/
	private String commercialSaleableArea;
	/**�칫�������*/
	private String officeSaleableSpace;
	/**�������׿������*/
	private String publicSacilitiesSaleableArea;
	/**סլ�������_�Ϲ�*/
	private String residentialSaleableAreaSub;
	/**��λ�������_�Ϲ�*/
	private String spaceAvailableForSaleSub;
	/**��ҵ�������_�Ϲ�*/
	private String commercialSaleableAreaSub;
	/**�칫�������_�Ϲ�*/
	private String officeSaleableSpaceSub;
	/**�����������_�Ϲ�*/
	private String publicSacilitiesSaleableAreaSub;
	/**סլ�������_ǩԼ*/
	private String residentialSaleableAreSigncon;
	/**��λ�������_ǩԼ*/
	private String spaceAvailableForSaleSigncon;
	/**��ҵ�������_ǩԼ*/
	private String commercialSaleableAreaSigncon;
	/**�칫�������_ǩԼ*/
	private String officeSaleableSpaceSigncon;
	/**�����������_ǩԼ*/
	private String publicSacilitiesSaleableAreaSigncon;
	/**��Ŀ�ܽ����*/
	private String projectTotalArea; 
	/**סլ�ؿ���*/
	private String homeReceivable;
	/**��λ�ؿ���*/
	private String parkingReceivable;
	/**��ҵ�ؿ���*/
	private String commercialReceivable;
	/**�칫�ؿ���*/
	private String officeSReceivable;
	/**�������׻ؿ���*/
	private String publicSacilitiesReceivable;
	/**���ۻؿ���*/
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
