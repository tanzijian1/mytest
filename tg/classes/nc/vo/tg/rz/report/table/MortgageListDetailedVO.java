package nc.vo.tg.rz.report.table;

import nc.vo.pub.SuperVO;

public class MortgageListDetailedVO extends SuperVO {
	String pk_project;// 项目ID
	String pk_periodization;// 分期主键
	String pk_rep_mortgagelist;
	// 土地
	String land_state;// 项目土地抵押状态
	String land_date;// 抵押时间
	String land_undate;// 解压时间
	String land_note;// 土地抵押说明

	// 在建工程
	String engineering_state;// 项目在建工程抵押状态
	String engineering_date;// 抵押时间
	String engineering_undate;// 解压时间
	String engineering_note;// 在建工程抵押说明

	// 物业
	String property_detailed;// 抵押物业明细
	String property_date;// 抵押时间
	String property_undate;// 解压时间
	String property_note;// 抵押物业说明

	Integer dr;
	String ts;

	public String getPk_rep_mortgagelist() {
		return pk_rep_mortgagelist;
	}

	public void setPk_rep_mortgagelist(String pk_rep_mortgagelist) {
		this.pk_rep_mortgagelist = pk_rep_mortgagelist;
	}

	public String getPk_project() {
		return pk_project;
	}

	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}

	public String getPk_periodization() {
		return pk_periodization;
	}

	public void setPk_periodization(String pk_periodization) {
		this.pk_periodization = pk_periodization;
	}

	public String getLand_state() {
		return land_state;
	}

	public void setLand_state(String land_state) {
		this.land_state = land_state;
	}

	public String getLand_date() {
		return land_date;
	}

	public void setLand_date(String land_date) {
		this.land_date = land_date;
	}

	public String getLand_undate() {
		return land_undate;
	}

	public void setLand_undate(String land_undate) {
		this.land_undate = land_undate;
	}

	public String getLand_note() {
		return land_note;
	}

	public void setLand_note(String land_note) {
		this.land_note = land_note;
	}

	public String getEngineering_state() {
		return engineering_state;
	}

	public void setEngineering_state(String engineering_state) {
		this.engineering_state = engineering_state;
	}

	public String getEngineering_date() {
		return engineering_date;
	}

	public void setEngineering_date(String engineering_date) {
		this.engineering_date = engineering_date;
	}

	public String getEngineering_undate() {
		return engineering_undate;
	}

	public void setEngineering_undate(String engineering_undate) {
		this.engineering_undate = engineering_undate;
	}

	public String getEngineering_note() {
		return engineering_note;
	}

	public void setEngineering_note(String engineering_note) {
		this.engineering_note = engineering_note;
	}

	public String getProperty_detailed() {
		return property_detailed;
	}

	public void setProperty_detailed(String property_detailed) {
		this.property_detailed = property_detailed;
	}

	public String getProperty_date() {
		return property_date;
	}

	public void setProperty_date(String property_date) {
		this.property_date = property_date;
	}

	public String getProperty_undate() {
		return property_undate;
	}

	public void setProperty_undate(String property_undate) {
		this.property_undate = property_undate;
	}

	public String getProperty_note() {
		return property_note;
	}

	public void setProperty_note(String property_note) {
		this.property_note = property_note;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_rep_mortgagelist";
	}

	/**
	 * <p>
	 * 返回表名称
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tgrz_rep_mortgagelist";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2019-6-9
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "tgrz_rep_mortgagelist";
	}

}
