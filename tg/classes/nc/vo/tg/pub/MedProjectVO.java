package nc.vo.tg.pub;

import nc.vo.pub.SuperVO;

/**
 * �г�����Ŀ��������
 * @author yy
 *
 */
public class MedProjectVO extends SuperVO{
/**
	 * 
	 */
	private static final long serialVersionUID = -6361848433928267700L;
private String	landName;//��������
private String	cycleTypeName;//��Ŀ����
private String	corpId;//���й�˾����
private String	corpName;//���й�˾����
private String	landId	;//��Ŀ���루��Ŀ��ţ�
private String	isHistory;//�Ƿ���ʷ����
private String	baseLandId;//��ʷ�����ڵ�ID
private String	grantTypeName;//��Ŀ����
private String	provinceName;//ʡ��
private String	cityName;//��
private String	areaName;//��
private String	streetName;//�ֵ�
private String	landAddress;//��ϸ��ַ
private String	equityPercent;//Ȩ�����
private String	startCycle;//��������
private String	landPlan;//����
private String	controlPlan;//�ع�
private String	tetLandArea;//���õ����
private String	totalNetLandArea;//���õ����
private String	far;//�ݻ���
private String	limitedHeight;//�ظ�
private String	capacityArea;//��˾�������
private String	totalCapacityArea;//�ܼ������
private String	buildingDensity;//�����ܶ�
private String	greenRate;//�̵���
private String	parkingSpaceRate;//��λ����
private String	commercialVolume;//��ҵ���
private String	publicMatingArea;//�����������
private String	affordableArea;//���Ϸ����
private String	otherRemark;//����˵��
private String	isFromLong;//�Ƿ�Ϊ�г�����Ŀת��
private String	originLandPrice;//�ܵؼ�
private String	originFloorLandPrice;//¥��ؼ�
private String	earthPrice;//�����
private String	deposit;//��֤��
private String	bulletinTime;//����ʱ��
private String	registrationDeadline;//��֤���ֹʱ��
private String	auctionTime;//����ʱ��
private String	reserveOrgans;//��������
private String landPayTime;//�������
private String bidCondition;//������������
private String	remark;//��ע
private String	landOperator;//�������
private String pk_project;//����
private String dr;
private String result_CreateName;//������
private String result_ProjectAddres;//��Ŀ��ַ
private String result_Deposit;//��֤��
private String result_TotalLandPrice;//�ܵؼ�
private String result_NetLandArea;//���õ����
private String result_CapacityArea;//�������
private String result_FAR;//�ݻ���
private String result_SaleAmount;//��ֵ
private String result_BelongCorp;//���ع�����˾
private String result_IsDelistingSign;//��Ŀ�Ƿ�ǩԼ
private String result_DelistingFinishDate;//���ʱ��
private String result_Remark;//��ע
private String result_PerformanceCognizance;//��Ч�϶�����
private String resultLong_AgreementSignDate;//Э��ǩ��ʱ��
private String resultLong_AgreementExpireDate;//Э�鵽��ʱ��
private String resultLong_TotalNetLandArea;//�ܾ�ռ�����
private String resultLong_OurDivisionLandArea;//��˾�ؿ�ռ�����
private String resultLong_OurDivisionLandFAR;//��˾�ؿ��ݻ���
private String resultLong_TotalCapacityArea;//�ܼ������
private String resultLong_OurCapacityArea;//��˾�ؿ�������
private String resultLong_ExpectedFinishDate;//Ԥ��ת�����ʱ��
private String resultLong_RealFloorLandPrice;//���ػ�ȡʵ��¥��ؼ�
private String resultLong_RealPrice;//���ػ�ȡʵ�ʼ۸�
private String resultLong_ExpectedPrice;//Ԥ���ۼ�
private String resultLong_EstimateAmount;//Ԥ����ֵR

public String getResult_CreateName() {
	return result_CreateName;
}

public void setResult_CreateName(String result_CreateName) {
	this.result_CreateName = result_CreateName;
}

public String getResult_ProjectAddres() {
	return result_ProjectAddres;
}

public void setResult_ProjectAddres(String result_ProjectAddres) {
	this.result_ProjectAddres = result_ProjectAddres;
}

public String getResult_Deposit() {
	return result_Deposit;
}

public void setResult_Deposit(String result_Deposit) {
	this.result_Deposit = result_Deposit;
}

public String getResult_TotalLandPrice() {
	return result_TotalLandPrice;
}

public void setResult_TotalLandPrice(String result_TotalLandPrice) {
	this.result_TotalLandPrice = result_TotalLandPrice;
}

public String getResult_NetLandArea() {
	return result_NetLandArea;
}

public void setResult_NetLandArea(String result_NetLandArea) {
	this.result_NetLandArea = result_NetLandArea;
}

public String getResult_CapacityArea() {
	return result_CapacityArea;
}

public void setResult_CapacityArea(String result_CapacityArea) {
	this.result_CapacityArea = result_CapacityArea;
}

public String getResult_FAR() {
	return result_FAR;
}

public void setResult_FAR(String result_FAR) {
	this.result_FAR = result_FAR;
}

public String getResult_SaleAmount() {
	return result_SaleAmount;
}

public void setResult_SaleAmount(String result_SaleAmount) {
	this.result_SaleAmount = result_SaleAmount;
}

public String getResult_BelongCorp() {
	return result_BelongCorp;
}

public void setResult_BelongCorp(String result_BelongCorp) {
	this.result_BelongCorp = result_BelongCorp;
}

public String getResult_IsDelistingSign() {
	return result_IsDelistingSign;
}

public void setResult_IsDelistingSign(String result_IsDelistingSign) {
	this.result_IsDelistingSign = result_IsDelistingSign;
}

public String getResult_DelistingFinishDate() {
	return result_DelistingFinishDate;
}

public void setResult_DelistingFinishDate(String result_DelistingFinishDate) {
	this.result_DelistingFinishDate = result_DelistingFinishDate;
}

public String getResult_Remark() {
	return result_Remark;
}

public void setResult_Remark(String result_Remark) {
	this.result_Remark = result_Remark;
}

public String getResult_PerformanceCognizance() {
	return result_PerformanceCognizance;
}

public void setResult_PerformanceCognizance(String result_PerformanceCognizance) {
	this.result_PerformanceCognizance = result_PerformanceCognizance;
}

public String getResultLong_AgreementSignDate() {
	return resultLong_AgreementSignDate;
}

public void setResultLong_AgreementSignDate(String resultLong_AgreementSignDate) {
	this.resultLong_AgreementSignDate = resultLong_AgreementSignDate;
}

public String getResultLong_AgreementExpireDate() {
	return resultLong_AgreementExpireDate;
}

public void setResultLong_AgreementExpireDate(
		String resultLong_AgreementExpireDate) {
	this.resultLong_AgreementExpireDate = resultLong_AgreementExpireDate;
}

public String getResultLong_TotalNetLandArea() {
	return resultLong_TotalNetLandArea;
}

public void setResultLong_TotalNetLandArea(String resultLong_TotalNetLandArea) {
	this.resultLong_TotalNetLandArea = resultLong_TotalNetLandArea;
}

public String getResultLong_OurDivisionLandArea() {
	return resultLong_OurDivisionLandArea;
}

public void setResultLong_OurDivisionLandArea(
		String resultLong_OurDivisionLandArea) {
	this.resultLong_OurDivisionLandArea = resultLong_OurDivisionLandArea;
}

public String getResultLong_OurDivisionLandFAR() {
	return resultLong_OurDivisionLandFAR;
}

public void setResultLong_OurDivisionLandFAR(
		String resultLong_OurDivisionLandFAR) {
	this.resultLong_OurDivisionLandFAR = resultLong_OurDivisionLandFAR;
}

public String getResultLong_TotalCapacityArea() {
	return resultLong_TotalCapacityArea;
}

public void setResultLong_TotalCapacityArea(String resultLong_TotalCapacityArea) {
	this.resultLong_TotalCapacityArea = resultLong_TotalCapacityArea;
}

public String getResultLong_OurCapacityArea() {
	return resultLong_OurCapacityArea;
}

public void setResultLong_OurCapacityArea(String resultLong_OurCapacityArea) {
	this.resultLong_OurCapacityArea = resultLong_OurCapacityArea;
}

public String getResultLong_ExpectedFinishDate() {
	return resultLong_ExpectedFinishDate;
}

public void setResultLong_ExpectedFinishDate(
		String resultLong_ExpectedFinishDate) {
	this.resultLong_ExpectedFinishDate = resultLong_ExpectedFinishDate;
}

public String getResultLong_RealFloorLandPrice() {
	return resultLong_RealFloorLandPrice;
}

public void setResultLong_RealFloorLandPrice(
		String resultLong_RealFloorLandPrice) {
	this.resultLong_RealFloorLandPrice = resultLong_RealFloorLandPrice;
}

public String getResultLong_RealPrice() {
	return resultLong_RealPrice;
}

public void setResultLong_RealPrice(String resultLong_RealPrice) {
	this.resultLong_RealPrice = resultLong_RealPrice;
}

public String getResultLong_ExpectedPrice() {
	return resultLong_ExpectedPrice;
}

public void setResultLong_ExpectedPrice(String resultLong_ExpectedPrice) {
	this.resultLong_ExpectedPrice = resultLong_ExpectedPrice;
}

public String getResultLong_EstimateAmount() {
	return resultLong_EstimateAmount;
}

public void setResultLong_EstimateAmount(String resultLong_EstimateAmount) {
	this.resultLong_EstimateAmount = resultLong_EstimateAmount;
}

public String getPk_project() {
	return pk_project;
}

public void setPk_project(String pk_project) {
	this.pk_project = pk_project;
}

public String getDr() {
	return dr;
}

public void setDr(String dr) {
	this.dr = dr;
}

public String getTableName() {
	return "med_project";
}

public String getPKFieldName() {
	return "pk_project";
}
public String getLandName() {
	return landName;
}
public void setLandName(String landName) {
	this.landName = landName;
}
public String getCycleTypeName() {
	return cycleTypeName;
}
public void setCycleTypeName(String cycleTypeName) {
	this.cycleTypeName = cycleTypeName;
}
public String getCorpId() {
	return corpId;
}
public void setCorpId(String corpId) {
	this.corpId = corpId;
}
public String getCorpName() {
	return corpName;
}
public void setCorpName(String corpName) {
	this.corpName = corpName;
}
public String getLandId() {
	return landId;
}
public void setLandId(String landId) {
	this.landId = landId;
}
public String getIsHistory() {
	return isHistory;
}
public void setIsHistory(String isHistory) {
	this.isHistory = isHistory;
}
public String getBaseLandId() {
	return baseLandId;
}
public void setBaseLandId(String baseLandId) {
	this.baseLandId = baseLandId;
}
public String getGrantTypeName() {
	return grantTypeName;
}
public void setGrantTypeName(String grantTypeName) {
	this.grantTypeName = grantTypeName;
}
public String getProvinceName() {
	return provinceName;
}
public void setProvinceName(String provinceName) {
	this.provinceName = provinceName;
}
public String getCityName() {
	return cityName;
}
public void setCityName(String cityName) {
	this.cityName = cityName;
}
public String getAreaName() {
	return areaName;
}
public void setAreaName(String areaName) {
	this.areaName = areaName;
}
public String getStreetName() {
	return streetName;
}
public void setStreetName(String streetName) {
	this.streetName = streetName;
}
public String getLandAddress() {
	return landAddress;
}
public void setLandAddress(String landAddress) {
	this.landAddress = landAddress;
}
public String getEquityPercent() {
	return equityPercent;
}
public void setEquityPercent(String equityPercent) {
	this.equityPercent = equityPercent;
}
public String getStartCycle() {
	return startCycle;
}
public void setStartCycle(String startCycle) {
	this.startCycle = startCycle;
}
public String getLandPlan() {
	return landPlan;
}
public void setLandPlan(String landPlan) {
	this.landPlan = landPlan;
}
public String getControlPlan() {
	return controlPlan;
}
public void setControlPlan(String controlPlan) {
	this.controlPlan = controlPlan;
}
public String getTetLandArea() {
	return tetLandArea;
}
public void setTetLandArea(String tetLandArea) {
	this.tetLandArea = tetLandArea;
}
public String getTotalNetLandArea() {
	return totalNetLandArea;
}
public void setTotalNetLandArea(String totalNetLandArea) {
	this.totalNetLandArea = totalNetLandArea;
}
public String getFar() {
	return far;
}
public void setFar(String far) {
	this.far = far;
}
public String getLimitedHeight() {
	return limitedHeight;
}
public void setLimitedHeight(String limitedHeight) {
	this.limitedHeight = limitedHeight;
}
public String getCapacityArea() {
	return capacityArea;
}
public void setCapacityArea(String capacityArea) {
	this.capacityArea = capacityArea;
}
public String getTotalCapacityArea() {
	return totalCapacityArea;
}
public void setTotalCapacityArea(String totalCapacityArea) {
	this.totalCapacityArea = totalCapacityArea;
}
public String getBuildingDensity() {
	return buildingDensity;
}
public void setBuildingDensity(String buildingDensity) {
	this.buildingDensity = buildingDensity;
}
public String getGreenRate() {
	return greenRate;
}
public void setGreenRate(String greenRate) {
	this.greenRate = greenRate;
}
public String getParkingSpaceRate() {
	return parkingSpaceRate;
}
public void setParkingSpaceRate(String parkingSpaceRate) {
	this.parkingSpaceRate = parkingSpaceRate;
}
public String getCommercialVolume() {
	return commercialVolume;
}
public void setCommercialVolume(String commercialVolume) {
	this.commercialVolume = commercialVolume;
}
public String getPublicMatingArea() {
	return publicMatingArea;
}
public void setPublicMatingArea(String publicMatingArea) {
	this.publicMatingArea = publicMatingArea;
}
public String getAffordableArea() {
	return affordableArea;
}
public void setAffordableArea(String affordableArea) {
	this.affordableArea = affordableArea;
}
public String getOtherRemark() {
	return otherRemark;
}
public void setOtherRemark(String otherRemark) {
	this.otherRemark = otherRemark;
}
public String getIsFromLong() {
	return isFromLong;
}
public void setIsFromLong(String isFromLong) {
	this.isFromLong = isFromLong;
}
public String getOriginLandPrice() {
	return originLandPrice;
}
public void setOriginLandPrice(String originLandPrice) {
	this.originLandPrice = originLandPrice;
}
public String getOriginFloorLandPrice() {
	return originFloorLandPrice;
}
public void setOriginFloorLandPrice(String originFloorLandPrice) {
	this.originFloorLandPrice = originFloorLandPrice;
}
public String getEarthPrice() {
	return earthPrice;
}
public void setEarthPrice(String earthPrice) {
	this.earthPrice = earthPrice;
}
public String getDeposit() {
	return deposit;
}
public void setDeposit(String deposit) {
	this.deposit = deposit;
}
public String getBulletinTime() {
	return bulletinTime;
}
public void setBulletinTime(String bulletinTime) {
	this.bulletinTime = bulletinTime;
}
public String getRegistrationDeadline() {
	return registrationDeadline;
}
public void setRegistrationDeadline(String registrationDeadline) {
	this.registrationDeadline = registrationDeadline;
}
public String getAuctionTime() {
	return auctionTime;
}
public void setAuctionTime(String auctionTime) {
	this.auctionTime = auctionTime;
}
public String getReserveOrgans() {
	return reserveOrgans;
}
public void setReserveOrgans(String reserveOrgans) {
	this.reserveOrgans = reserveOrgans;
}
public String getLandPayTime() {
	return landPayTime;
}
public void setLandPayTime(String landPayTime) {
	this.landPayTime = landPayTime;
}
public String getBidCondition() {
	return bidCondition;
}
public void setBidCondition(String bidCondition) {
	this.bidCondition = bidCondition;
}
public String getRemark() {
	return remark;
}
public void setRemark(String remark) {
	this.remark = remark;
}
public String getLandOperator() {
	return landOperator;
}
public void setLandOperator(String landOperator) {
	this.landOperator = landOperator;
}
}
