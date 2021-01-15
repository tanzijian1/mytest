package nc.vo.tg.masterdata;

import nc.vo.pub.SuperVO;

/**
 * @author ASUS
 *楼栋信息接口VO 2020-08-24 LJF
 */
@SuppressWarnings("serial")
public class BlockBuildingInfoVO extends SuperVO {
	/**分期楼栋ID；序列：WBD_BlockBuilding_S*/
	private String id ;
	/**项目分期；关联表：WBD_ProjectBlock_T*/
	private String fpbid;
	/**楼栋号*/
	private String fnumber; 
	/**营销楼号*/
	private String fmarketnumber;
	/**产品类型ID；关联表：WBD_Produce_T(关联业态)*/
	private String fprdid;
	/**标段*/
	private String ftender;
	/**是否可售；1 = 可售、0 = 不可售*/
	private String fsaleable;
	/**建筑面积*/
	private String fbuildingarea;
	/**计容面积*/
	private String ffactplotarea;
	/**不计容面积*/
	private String ffactnonplotarea;
	/**可售面积*/
	private String fsaleablearea;
	/**不可售面积*/
	private String fnonsaleablearea;
	/**架空面积*/
	private String foperfloorarea;
	/**入户大堂面积*/
	private String fhallarea;
	/**基底面积*/
	private String fbuildingbasearea;
	/**普通住宅套数*/
	private String fcommonnumber;
	/**非普通住宅套数*/
	private String funcommonnumber;
	/**单元数*/
	private String funitnumber;
	/**总套数*/
	private String froomnumber;
	/**层数-地上层数*/
	private String ffloor;
	/**建筑总高度*/
	private String fbuildingheight;
	/**套内面积*/
	private String finternalarea;
	/**分摊面积*/
	private String fapportionarea;
	/**开发阶段；代码：WBD_BlockStage*/
	private String fblockstage;
	/**组团 代码表：WBD_BuildGroup*/
	private String fbuildgroup;
	/**地下总面积（车库时候使用）测绘和规划总面积*/
	private String ftotalunderarea;
	/**产权车位个数（车库使用）产权车位个数*/
	private String fpropertyparkingplace;
	/**非产权车位个数（车库使用）无产权车位个数*/
	private String fnonpropertyparkingplace;
	/**均价*/
	private String fprice;
	/**地下层数*/
	private String funderfloorcount;
	/**首层商铺面积*/
	private String firstshop;
	/**二层及二层以上面积*/
	private String secondshop;
	/**加建面积*/
	private String faddarea;
	/**承租年限*/
	private String fhireyears;
	/**首层单元数*/
	private String ffirststepunit;
	/**二层以上单元数*/
	private String fsecondstepunit;
	/**租赁加成系数 加成系数 */
	private String fhireratio;
	/**占地面积*/
	private String flandarea;
	/**主体结构*/
	private String fmainstructure;
	/**分摊系数*/
	private String fsharerate;
	/**功能中文名称(指标卡)（业态）*/
	private String FproductFeature;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFpbid() {
		return fpbid;
	}
	public void setFpbid(String fpbid) {
		this.fpbid = fpbid;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFmarketnumber() {
		return fmarketnumber;
	}
	public void setFmarketnumber(String fmarketnumber) {
		this.fmarketnumber = fmarketnumber;
	}
	public String getFprdid() {
		return fprdid;
	}
	public void setFprdid(String fprdid) {
		this.fprdid = fprdid;
	}
	public String getFtender() {
		return ftender;
	}
	public void setFtender(String ftender) {
		this.ftender = ftender;
	}
	public String getFsaleable() {
		return fsaleable;
	}
	public void setFsaleable(String fsaleable) {
		this.fsaleable = fsaleable;
	}
	public String getFbuildingarea() {
		return fbuildingarea;
	}
	public void setFbuildingarea(String fbuildingarea) {
		this.fbuildingarea = fbuildingarea;
	}
	public String getFfactplotarea() {
		return ffactplotarea;
	}
	public void setFfactplotarea(String ffactplotarea) {
		this.ffactplotarea = ffactplotarea;
	}
	public String getFfactnonplotarea() {
		return ffactnonplotarea;
	}
	public void setFfactnonplotarea(String ffactnonplotarea) {
		this.ffactnonplotarea = ffactnonplotarea;
	}
	public String getFsaleablearea() {
		return fsaleablearea;
	}
	public void setFsaleablearea(String fsaleablearea) {
		this.fsaleablearea = fsaleablearea;
	}
	public String getFnonsaleablearea() {
		return fnonsaleablearea;
	}
	public void setFnonsaleablearea(String fnonsaleablearea) {
		this.fnonsaleablearea = fnonsaleablearea;
	}
	public String getFoperfloorarea() {
		return foperfloorarea;
	}
	public void setFoperfloorarea(String foperfloorarea) {
		this.foperfloorarea = foperfloorarea;
	}
	public String getFhallarea() {
		return fhallarea;
	}
	public void setFhallarea(String fhallarea) {
		this.fhallarea = fhallarea;
	}
	public String getFbuildingbasearea() {
		return fbuildingbasearea;
	}
	public void setFbuildingbasearea(String fbuildingbasearea) {
		this.fbuildingbasearea = fbuildingbasearea;
	}
	public String getFcommonnumber() {
		return fcommonnumber;
	}
	public void setFcommonnumber(String fcommonnumber) {
		this.fcommonnumber = fcommonnumber;
	}
	public String getFuncommonnumber() {
		return funcommonnumber;
	}
	public void setFuncommonnumber(String funcommonnumber) {
		this.funcommonnumber = funcommonnumber;
	}
	public String getFunitnumber() {
		return funitnumber;
	}
	public void setFunitnumber(String funitnumber) {
		this.funitnumber = funitnumber;
	}
	public String getFroomnumber() {
		return froomnumber;
	}
	public void setFroomnumber(String froomnumber) {
		this.froomnumber = froomnumber;
	}
	public String getFfloor() {
		return ffloor;
	}
	public void setFfloor(String ffloor) {
		this.ffloor = ffloor;
	}
	public String getFbuildingheight() {
		return fbuildingheight;
	}
	public void setFbuildingheight(String fbuildingheight) {
		this.fbuildingheight = fbuildingheight;
	}
	public String getFinternalarea() {
		return finternalarea;
	}
	public void setFinternalarea(String finternalarea) {
		this.finternalarea = finternalarea;
	}
	public String getFapportionarea() {
		return fapportionarea;
	}
	public void setFapportionarea(String fapportionarea) {
		this.fapportionarea = fapportionarea;
	}
	public String getFblockstage() {
		return fblockstage;
	}
	public void setFblockstage(String fblockstage) {
		this.fblockstage = fblockstage;
	}
	public String getFbuildgroup() {
		return fbuildgroup;
	}
	public void setFbuildgroup(String fbuildgroup) {
		this.fbuildgroup = fbuildgroup;
	}
	public String getFprice() {
		return fprice;
	}
	public void setFprice(String fprice) {
		this.fprice = fprice;
	}
	public String getFunderfloorcount() {
		return funderfloorcount;
	}
	public void setFunderfloorcount(String funderfloorcount) {
		this.funderfloorcount = funderfloorcount;
	}
	public String getFmainstructure() {
		return fmainstructure;
	}
	public void setFmainstructure(String fmainstructure) {
		this.fmainstructure = fmainstructure;
	}
	public String getFsharerate() {
		return fsharerate;
	}
	public void setFsharerate(String fsharerate) {
		this.fsharerate = fsharerate;
	}
	public String getFproductFeature() {
		return FproductFeature;
	}
	public void setFproductFeature(String fproductFeature) {
		FproductFeature = fproductFeature;
	}
	public String getFtotalunderarea() {
		return ftotalunderarea;
	}
	public void setFtotalunderarea(String ftotalunderarea) {
		this.ftotalunderarea = ftotalunderarea;
	}
	public String getFpropertyparkingplace() {
		return fpropertyparkingplace;
	}
	public void setFpropertyparkingplace(String fpropertyparkingplace) {
		this.fpropertyparkingplace = fpropertyparkingplace;
	}
	public String getFnonpropertyparkingplace() {
		return fnonpropertyparkingplace;
	}
	public void setFnonpropertyparkingplace(String fnonpropertyparkingplace) {
		this.fnonpropertyparkingplace = fnonpropertyparkingplace;
	}
	public String getFirstshop() {
		return firstshop;
	}
	public void setFirstshop(String firstshop) {
		this.firstshop = firstshop;
	}
	public String getSecondshop() {
		return secondshop;
	}
	public void setSecondshop(String secondshop) {
		this.secondshop = secondshop;
	}
	public String getFaddarea() {
		return faddarea;
	}
	public void setFaddarea(String faddarea) {
		this.faddarea = faddarea;
	}
	public String getFhireyears() {
		return fhireyears;
	}
	public void setFhireyears(String fhireyears) {
		this.fhireyears = fhireyears;
	}
	public String getFfirststepunit() {
		return ffirststepunit;
	}
	public void setFfirststepunit(String ffirststepunit) {
		this.ffirststepunit = ffirststepunit;
	}
	public String getFsecondstepunit() {
		return fsecondstepunit;
	}
	public void setFsecondstepunit(String fsecondstepunit) {
		this.fsecondstepunit = fsecondstepunit;
	}
	public String getFhireratio() {
		return fhireratio;
	}
	public void setFhireratio(String fhireratio) {
		this.fhireratio = fhireratio;
	}
	public String getFlandarea() {
		return flandarea;
	}
	public void setFlandarea(String flandarea) {
		this.flandarea = flandarea;
	}
	
}
