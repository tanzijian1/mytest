package nc.vo.tg.masterdata;

import nc.vo.pub.SuperVO;

/**
 * @author ASUS
 *项目分期信息接口VO 2020-08-24 LJF
 */
@SuppressWarnings("serial")
public class ProjectBlockInfoVO extends SuperVO {
	/**所属项目ID；关联表：WBD_Project_T*/
	private String fprjid ;
	/**项目分期ID*/
	private String id ;
	/**分期编码*/
	private String fnumber;
	/**分期名称*/
	private String fname;
	/**产品系列；代码：WBD_ProductSeries*/
	private String fproductseries;
	/**说明*/
	private String fdescription;
	/**分期备案名*/
	private String fremark;

	public String getFprjid() {
		return fprjid;
	}
	public void setFprjid(String fprjid) {
		this.fprjid = fprjid;
	}
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
	public String getFdescription() {
		return fdescription;
	}
	public void setFdescription(String fdescription) {
		this.fdescription = fdescription;
	}
	public String getFremark() {
		return fremark;
	}
	public void setFremark(String fremark) {
		this.fremark = fremark;
	}
	
}
