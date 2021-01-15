package nc.vo.tg.mortgagelist;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 * 此处添加类的描述信息
 * </p>
 * 创建日期:2019-7-22
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */
public class MortgageListDetailedVO extends nc.vo.pub.SuperVO {

	private java.lang.String pk_group;
	private java.lang.String pk_org;
	private java.lang.String pk_org_v;
	private java.lang.String pk_project;
	private java.lang.String pk_periodization;
	private java.lang.String land_state;
	private nc.vo.pub.lang.UFDate land_date;
	private nc.vo.pub.lang.UFDate land_undate;
	private java.lang.String land_note;
	private java.lang.String engineering_state;
	private nc.vo.pub.lang.UFDate engineering_date;
	private nc.vo.pub.lang.UFDate engineering_undate;
	private java.lang.String engineering_note;
	private java.lang.String property_detailed;
	private nc.vo.pub.lang.UFDate property_date;
	private nc.vo.pub.lang.UFDate property_undate;
	private java.lang.String property_note;
	private java.lang.String pk_rep_mortgagelist;
	private java.lang.String creator;
	private nc.vo.pub.lang.UFDateTime creationtime;
	private java.lang.String modifier;
	private nc.vo.pub.lang.UFDateTime modifiedtime;
	private java.lang.Integer dr = 0;
	private nc.vo.pub.lang.UFDateTime ts;

	public static final String PK_GROUP = "pk_group";// 集团
	public static final String PK_ORG = "pk_org";// 组织
	public static final String PK_ORG_V = "pk_org_v";// 组织版本
	public static final String PK_PROJECT = "pk_project";// 项目主键
	public static final String PK_PERIODIZATION = "pk_periodization";// 项目分期主键
	public static final String LAND_STATE = "land_state";// 项目土地抵押状态
	public static final String LAND_DATE = "land_date";// 土地_抵押时间
	public static final String LAND_UNDATE = "land_undate";// 土地_解压时间
	public static final String LAND_NOTE = "land_note";// 土地抵押说明
	public static final String ENGINEERING_STATE = "engineering_state";// 项目在建工程抵押状态
	public static final String ENGINEERING_DATE = "engineering_date";// 在建工程_抵押时间
	public static final String ENGINEERING_UNDATE = "engineering_undate";// 在建工程_解压时间
	public static final String ENGINEERING_NOTE = "engineering_note";// 在建工程抵押说明
	public static final String PROPERTY_DETAILED = "property_detailed";// 抵押物业明细
	public static final String PROPERTY_DATE = "property_date";// 物业_抵押时间
	public static final String PROPERTY_UNDATE = "property_undate";// 物业_解压时间
	public static final String PROPERTY_NOTE = "property_note";// 抵押物业说明
	public static final String PK_REP_MORTGAGELIST = "pk_rep_mortgagelist";// 主键
	public static final String CREATOR = "creator";// 创建人
	public static final String CREATIONTIME = "creationtime";// 创建时间
	public static final String MODIFIER = "modifier";// 修改人
	public static final String MODIFIEDTIME = "modifiedtime";// 修改时间

	/**
	 * 属性 pk_group的Getter方法.属性名：集团 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group() {
		return pk_group;
	}

	/**
	 * 属性pk_group的Setter方法.属性名：集团 创建日期:2019-7-22
	 * 
	 * @param newPk_group
	 *            java.lang.String
	 */
	public void setPk_group(java.lang.String newPk_group) {
		this.pk_group = newPk_group;
	}

	/**
	 * 属性 pk_org的Getter方法.属性名：组织 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org() {
		return pk_org;
	}

	/**
	 * 属性pk_org的Setter方法.属性名：组织 创建日期:2019-7-22
	 * 
	 * @param newPk_org
	 *            java.lang.String
	 */
	public void setPk_org(java.lang.String newPk_org) {
		this.pk_org = newPk_org;
	}

	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v() {
		return pk_org_v;
	}

	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本 创建日期:2019-7-22
	 * 
	 * @param newPk_org_v
	 *            java.lang.String
	 */
	public void setPk_org_v(java.lang.String newPk_org_v) {
		this.pk_org_v = newPk_org_v;
	}

	/**
	 * 属性 pk_project的Getter方法.属性名：项目主键 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_project() {
		return pk_project;
	}

	/**
	 * 属性pk_project的Setter方法.属性名：项目主键 创建日期:2019-7-22
	 * 
	 * @param newPk_project
	 *            java.lang.String
	 */
	public void setPk_project(java.lang.String newPk_project) {
		this.pk_project = newPk_project;
	}

	/**
	 * 属性 pk_periodization的Getter方法.属性名：项目分期主键 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_periodization() {
		return pk_periodization;
	}

	/**
	 * 属性pk_periodization的Setter方法.属性名：项目分期主键 创建日期:2019-7-22
	 * 
	 * @param newPk_periodization
	 *            java.lang.String
	 */
	public void setPk_periodization(java.lang.String newPk_periodization) {
		this.pk_periodization = newPk_periodization;
	}

	/**
	 * 属性 land_state的Getter方法.属性名：项目土地抵押状态 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLand_state() {
		return land_state;
	}

	/**
	 * 属性land_state的Setter方法.属性名：项目土地抵押状态 创建日期:2019-7-22
	 * 
	 * @param newLand_state
	 *            java.lang.String
	 */
	public void setLand_state(java.lang.String newLand_state) {
		this.land_state = newLand_state;
	}

	/**
	 * 属性 land_date的Getter方法.属性名：土地_抵押时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getLand_date() {
		return land_date;
	}

	/**
	 * 属性land_date的Setter方法.属性名：土地_抵押时间 创建日期:2019-7-22
	 * 
	 * @param newLand_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setLand_date(nc.vo.pub.lang.UFDate newLand_date) {
		this.land_date = newLand_date;
	}

	/**
	 * 属性 land_undate的Getter方法.属性名：土地_解压时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getLand_undate() {
		return land_undate;
	}

	/**
	 * 属性land_undate的Setter方法.属性名：土地_解压时间 创建日期:2019-7-22
	 * 
	 * @param newLand_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setLand_undate(nc.vo.pub.lang.UFDate newLand_undate) {
		this.land_undate = newLand_undate;
	}

	/**
	 * 属性 land_note的Getter方法.属性名：土地抵押说明 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLand_note() {
		return land_note;
	}

	/**
	 * 属性land_note的Setter方法.属性名：土地抵押说明 创建日期:2019-7-22
	 * 
	 * @param newLand_note
	 *            java.lang.String
	 */
	public void setLand_note(java.lang.String newLand_note) {
		this.land_note = newLand_note;
	}

	/**
	 * 属性 engineering_state的Getter方法.属性名：项目在建工程抵押状态 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEngineering_state() {
		return engineering_state;
	}

	/**
	 * 属性engineering_state的Setter方法.属性名：项目在建工程抵押状态 创建日期:2019-7-22
	 * 
	 * @param newEngineering_state
	 *            java.lang.String
	 */
	public void setEngineering_state(java.lang.String newEngineering_state) {
		this.engineering_state = newEngineering_state;
	}

	/**
	 * 属性 engineering_date的Getter方法.属性名：在建工程_抵押时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getEngineering_date() {
		return engineering_date;
	}

	/**
	 * 属性engineering_date的Setter方法.属性名：在建工程_抵押时间 创建日期:2019-7-22
	 * 
	 * @param newEngineering_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setEngineering_date(nc.vo.pub.lang.UFDate newEngineering_date) {
		this.engineering_date = newEngineering_date;
	}

	/**
	 * 属性 engineering_undate的Getter方法.属性名：在建工程_解压时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getEngineering_undate() {
		return engineering_undate;
	}

	/**
	 * 属性engineering_undate的Setter方法.属性名：在建工程_解压时间 创建日期:2019-7-22
	 * 
	 * @param newEngineering_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setEngineering_undate(
			nc.vo.pub.lang.UFDate newEngineering_undate) {
		this.engineering_undate = newEngineering_undate;
	}

	/**
	 * 属性 engineering_note的Getter方法.属性名：在建工程抵押说明 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEngineering_note() {
		return engineering_note;
	}

	/**
	 * 属性engineering_note的Setter方法.属性名：在建工程抵押说明 创建日期:2019-7-22
	 * 
	 * @param newEngineering_note
	 *            java.lang.String
	 */
	public void setEngineering_note(java.lang.String newEngineering_note) {
		this.engineering_note = newEngineering_note;
	}

	/**
	 * 属性 property_detailed的Getter方法.属性名：抵押物业明细 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getProperty_detailed() {
		return property_detailed;
	}

	/**
	 * 属性property_detailed的Setter方法.属性名：抵押物业明细 创建日期:2019-7-22
	 * 
	 * @param newProperty_detailed
	 *            java.lang.String
	 */
	public void setProperty_detailed(java.lang.String newProperty_detailed) {
		this.property_detailed = newProperty_detailed;
	}

	/**
	 * 属性 property_date的Getter方法.属性名：物业_抵押时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getProperty_date() {
		return property_date;
	}

	/**
	 * 属性property_date的Setter方法.属性名：物业_抵押时间 创建日期:2019-7-22
	 * 
	 * @param newProperty_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProperty_date(nc.vo.pub.lang.UFDate newProperty_date) {
		this.property_date = newProperty_date;
	}

	/**
	 * 属性 property_undate的Getter方法.属性名：物业_解压时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getProperty_undate() {
		return property_undate;
	}

	/**
	 * 属性property_undate的Setter方法.属性名：物业_解压时间 创建日期:2019-7-22
	 * 
	 * @param newProperty_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProperty_undate(nc.vo.pub.lang.UFDate newProperty_undate) {
		this.property_undate = newProperty_undate;
	}

	/**
	 * 属性 property_note的Getter方法.属性名：抵押物业说明 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getProperty_note() {
		return property_note;
	}

	/**
	 * 属性property_note的Setter方法.属性名：抵押物业说明 创建日期:2019-7-22
	 * 
	 * @param newProperty_note
	 *            java.lang.String
	 */
	public void setProperty_note(java.lang.String newProperty_note) {
		this.property_note = newProperty_note;
	}

	/**
	 * 属性 pk_rep_mortgagelist的Getter方法.属性名：主键 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_rep_mortgagelist() {
		return pk_rep_mortgagelist;
	}

	/**
	 * 属性pk_rep_mortgagelist的Setter方法.属性名：主键 创建日期:2019-7-22
	 * 
	 * @param newPk_rep_mortgagelist
	 *            java.lang.String
	 */
	public void setPk_rep_mortgagelist(java.lang.String newPk_rep_mortgagelist) {
		this.pk_rep_mortgagelist = newPk_rep_mortgagelist;
	}

	/**
	 * 属性 creator的Getter方法.属性名：创建人 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCreator() {
		return creator;
	}

	/**
	 * 属性creator的Setter方法.属性名：创建人 创建日期:2019-7-22
	 * 
	 * @param newCreator
	 *            java.lang.String
	 */
	public void setCreator(java.lang.String newCreator) {
		this.creator = newCreator;
	}

	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime() {
		return creationtime;
	}

	/**
	 * 属性creationtime的Setter方法.属性名：创建时间 创建日期:2019-7-22
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(nc.vo.pub.lang.UFDateTime newCreationtime) {
		this.creationtime = newCreationtime;
	}

	/**
	 * 属性 modifier的Getter方法.属性名：修改人 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getModifier() {
		return modifier;
	}

	/**
	 * 属性modifier的Setter方法.属性名：修改人 创建日期:2019-7-22
	 * 
	 * @param newModifier
	 *            java.lang.String
	 */
	public void setModifier(java.lang.String newModifier) {
		this.modifier = newModifier;
	}

	/**
	 * 属性 modifiedtime的Getter方法.属性名：修改时间 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime() {
		return modifiedtime;
	}

	/**
	 * 属性modifiedtime的Setter方法.属性名：修改时间 创建日期:2019-7-22
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(nc.vo.pub.lang.UFDateTime newModifiedtime) {
		this.modifiedtime = newModifiedtime;
	}

	/**
	 * 属性 dr的Getter方法.属性名：dr 创建日期:2019-7-22
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * 属性dr的Setter方法.属性名：dr 创建日期:2019-7-22
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * 属性 ts的Getter方法.属性名：ts 创建日期:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * 属性ts的Setter方法.属性名：ts 创建日期:2019-7-22
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * <p>
	 * 取得父VO主键字段.
	 * <p>
	 * 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	 * <p>
	 * 取得表主键.
	 * <p>
	 * 创建日期:2019-7-22
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
	 * 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "TGRZ_REP_MORTGAGELIST";
	}

	/**
	 * <p>
	 * 返回表名称.
	 * <p>
	 * 创建日期:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "TGRZ_REP_MORTGAGELIST";
	}

	/**
	 * 按照默认方式创建构造子.
	 * 
	 * 创建日期:2019-7-22
	 */
	public MortgageListDetailedVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.tg.mortgagelist.MortgageListDetailedVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.MortgageListDetailed");

	}

}