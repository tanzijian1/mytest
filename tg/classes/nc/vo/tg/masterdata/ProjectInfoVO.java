package nc.vo.tg.masterdata;

import nc.vo.pub.SuperVO;

/**
 * @author ASUS
 *2020-08-27 项目信息接口
 */
public class ProjectInfoVO extends SuperVO{
	/**项目主键*/
	private String id;
	/**项目编码*/
	private String fnumber;
	/**项目名称*/
	private String fname;
	/**产品系列；代码：WBD_ProductSeries*/
	private String fproductseries;
	/**版本*/
	private String fversion;
	/**项目地址*/
	private String faddress;
	/**说明*/
	private String fdescription;
	/**净用地面积*/
	private String flandarea;
	/**曾用名1--报建名*/
	private String fname1;
	/**曾用名2--营销用名*/
	private String fname2;
	/**规划总用地面积*/
	private String fplantotalarea;
	/**毛容积率*/
	private String fglossarea;
	/**净容积率*/
	private String fnetarea;
	/**绿地率*/
	private String fgreenrate;
	/**建筑控高*/
	private String fcontrolhight;
	/**毛建筑密度*/
	private String fglossdensity;
	/**净建筑密度*/
	private String fnetdensity;
	/**项目状态（中文）*/
	private String Fprojectstatus_cn;
	/**区域名称*/
	private String Fregion_name;
	/**产品系列名称*/
	private String fproductseries_name;
	/**项目公司名称*/
	private String org_name;
	/**国家*/
	private String country_name;
	/**省份*/
	private String provience_name;
	/**城市*/
	private String city_name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFnumber() {
		return fnumber;
	}
	public void setFnumber(String fnumber) {
		this.fnumber = fnumber;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getFproductseries() {
		return fproductseries;
	}
	public void setFproductseries(String fproductseries) {
		this.fproductseries = fproductseries;
	}
	public String getFversion() {
		return fversion;
	}
	public void setFversion(String fversion) {
		this.fversion = fversion;
	}
	public String getFaddress() {
		return faddress;
	}
	public void setFaddress(String faddress) {
		this.faddress = faddress;
	}
	public String getFdescription() {
		return fdescription;
	}
	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}
	public String getFlandarea() {
		return flandarea;
	}
	public void setFlandarea(String flandarea) {
		this.flandarea = flandarea;
	}
	public String getFname1() {
		return fname1;
	}
	public void setFname1(String fname1) {
		this.fname1 = fname1;
	}
	public String getFname2() {
		return fname2;
	}
	public void setFname2(String fname2) {
		this.fname2 = fname2;
	}
	public String getFplantotalarea() {
		return fplantotalarea;
	}
	public void setFplantotalarea(String fplantotalarea) {
		this.fplantotalarea = fplantotalarea;
	}
	public String getFglossarea() {
		return fglossarea;
	}
	public void setFglossarea(String fglossarea) {
		this.fglossarea = fglossarea;
	}
	public String getFnetarea() {
		return fnetarea;
	}
	public void setFnetarea(String fnetarea) {
		this.fnetarea = fnetarea;
	}
	public String getFgreenrate() {
		return fgreenrate;
	}
	public void setFgreenrate(String fgreenrate) {
		this.fgreenrate = fgreenrate;
	}
	public String getFcontrolhight() {
		return fcontrolhight;
	}
	public void setFcontrolhight(String fcontrolhight) {
		this.fcontrolhight = fcontrolhight;
	}
	public String getFglossdensity() {
		return fglossdensity;
	}
	public void setFglossdensity(String fglossdensity) {
		this.fglossdensity = fglossdensity;
	}
	public String getFnetdensity() {
		return fnetdensity;
	}
	public void setFnetdensity(String fnetdensity) {
		this.fnetdensity = fnetdensity;
	}
	public String getFprojectstatus_cn() {
		return Fprojectstatus_cn;
	}
	public void setFprojectstatus_cn(String fprojectstatus_cn) {
		Fprojectstatus_cn = fprojectstatus_cn;
	}
	public String getFregion_name() {
		return Fregion_name;
	}
	public void setFregion_name(String fregion_name) {
		Fregion_name = fregion_name;
	}
	public String getFproductseries_name() {
		return fproductseries_name;
	}
	public void setFproductseries_name(String fproductseries_name) {
		this.fproductseries_name = fproductseries_name;
	}
	public String getOrg_name() {
		return org_name;
	}
	public void setOrg_name(String org_name) {
		this.org_name = org_name;
	}
	public String getCountry_name() {
		return country_name;
	}
	public void setCountry_name(String country_name) {
		this.country_name = country_name;
	}
	public String getProvience_name() {
		return provience_name;
	}
	public void setProvience_name(String provience_name) {
		this.provience_name = provience_name;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String city_name) {
		this.city_name = city_name;
	}
	
}
