package nc.vo.tg.mortgagelist;

import nc.vo.pub.*;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 * �˴�������������Ϣ
 * </p>
 * ��������:2019-7-22
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

	public static final String PK_GROUP = "pk_group";// ����
	public static final String PK_ORG = "pk_org";// ��֯
	public static final String PK_ORG_V = "pk_org_v";// ��֯�汾
	public static final String PK_PROJECT = "pk_project";// ��Ŀ����
	public static final String PK_PERIODIZATION = "pk_periodization";// ��Ŀ��������
	public static final String LAND_STATE = "land_state";// ��Ŀ���ص�Ѻ״̬
	public static final String LAND_DATE = "land_date";// ����_��Ѻʱ��
	public static final String LAND_UNDATE = "land_undate";// ����_��ѹʱ��
	public static final String LAND_NOTE = "land_note";// ���ص�Ѻ˵��
	public static final String ENGINEERING_STATE = "engineering_state";// ��Ŀ�ڽ����̵�Ѻ״̬
	public static final String ENGINEERING_DATE = "engineering_date";// �ڽ�����_��Ѻʱ��
	public static final String ENGINEERING_UNDATE = "engineering_undate";// �ڽ�����_��ѹʱ��
	public static final String ENGINEERING_NOTE = "engineering_note";// �ڽ����̵�Ѻ˵��
	public static final String PROPERTY_DETAILED = "property_detailed";// ��Ѻ��ҵ��ϸ
	public static final String PROPERTY_DATE = "property_date";// ��ҵ_��Ѻʱ��
	public static final String PROPERTY_UNDATE = "property_undate";// ��ҵ_��ѹʱ��
	public static final String PROPERTY_NOTE = "property_note";// ��Ѻ��ҵ˵��
	public static final String PK_REP_MORTGAGELIST = "pk_rep_mortgagelist";// ����
	public static final String CREATOR = "creator";// ������
	public static final String CREATIONTIME = "creationtime";// ����ʱ��
	public static final String MODIFIER = "modifier";// �޸���
	public static final String MODIFIEDTIME = "modifiedtime";// �޸�ʱ��

	/**
	 * ���� pk_group��Getter����.������������ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group() {
		return pk_group;
	}

	/**
	 * ����pk_group��Setter����.������������ ��������:2019-7-22
	 * 
	 * @param newPk_group
	 *            java.lang.String
	 */
	public void setPk_group(java.lang.String newPk_group) {
		this.pk_group = newPk_group;
	}

	/**
	 * ���� pk_org��Getter����.����������֯ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org() {
		return pk_org;
	}

	/**
	 * ����pk_org��Setter����.����������֯ ��������:2019-7-22
	 * 
	 * @param newPk_org
	 *            java.lang.String
	 */
	public void setPk_org(java.lang.String newPk_org) {
		this.pk_org = newPk_org;
	}

	/**
	 * ���� pk_org_v��Getter����.����������֯�汾 ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v() {
		return pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.����������֯�汾 ��������:2019-7-22
	 * 
	 * @param newPk_org_v
	 *            java.lang.String
	 */
	public void setPk_org_v(java.lang.String newPk_org_v) {
		this.pk_org_v = newPk_org_v;
	}

	/**
	 * ���� pk_project��Getter����.����������Ŀ���� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_project() {
		return pk_project;
	}

	/**
	 * ����pk_project��Setter����.����������Ŀ���� ��������:2019-7-22
	 * 
	 * @param newPk_project
	 *            java.lang.String
	 */
	public void setPk_project(java.lang.String newPk_project) {
		this.pk_project = newPk_project;
	}

	/**
	 * ���� pk_periodization��Getter����.����������Ŀ�������� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_periodization() {
		return pk_periodization;
	}

	/**
	 * ����pk_periodization��Setter����.����������Ŀ�������� ��������:2019-7-22
	 * 
	 * @param newPk_periodization
	 *            java.lang.String
	 */
	public void setPk_periodization(java.lang.String newPk_periodization) {
		this.pk_periodization = newPk_periodization;
	}

	/**
	 * ���� land_state��Getter����.����������Ŀ���ص�Ѻ״̬ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLand_state() {
		return land_state;
	}

	/**
	 * ����land_state��Setter����.����������Ŀ���ص�Ѻ״̬ ��������:2019-7-22
	 * 
	 * @param newLand_state
	 *            java.lang.String
	 */
	public void setLand_state(java.lang.String newLand_state) {
		this.land_state = newLand_state;
	}

	/**
	 * ���� land_date��Getter����.������������_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getLand_date() {
		return land_date;
	}

	/**
	 * ����land_date��Setter����.������������_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @param newLand_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setLand_date(nc.vo.pub.lang.UFDate newLand_date) {
		this.land_date = newLand_date;
	}

	/**
	 * ���� land_undate��Getter����.������������_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getLand_undate() {
		return land_undate;
	}

	/**
	 * ����land_undate��Setter����.������������_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @param newLand_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setLand_undate(nc.vo.pub.lang.UFDate newLand_undate) {
		this.land_undate = newLand_undate;
	}

	/**
	 * ���� land_note��Getter����.�����������ص�Ѻ˵�� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getLand_note() {
		return land_note;
	}

	/**
	 * ����land_note��Setter����.�����������ص�Ѻ˵�� ��������:2019-7-22
	 * 
	 * @param newLand_note
	 *            java.lang.String
	 */
	public void setLand_note(java.lang.String newLand_note) {
		this.land_note = newLand_note;
	}

	/**
	 * ���� engineering_state��Getter����.����������Ŀ�ڽ����̵�Ѻ״̬ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEngineering_state() {
		return engineering_state;
	}

	/**
	 * ����engineering_state��Setter����.����������Ŀ�ڽ����̵�Ѻ״̬ ��������:2019-7-22
	 * 
	 * @param newEngineering_state
	 *            java.lang.String
	 */
	public void setEngineering_state(java.lang.String newEngineering_state) {
		this.engineering_state = newEngineering_state;
	}

	/**
	 * ���� engineering_date��Getter����.���������ڽ�����_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getEngineering_date() {
		return engineering_date;
	}

	/**
	 * ����engineering_date��Setter����.���������ڽ�����_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @param newEngineering_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setEngineering_date(nc.vo.pub.lang.UFDate newEngineering_date) {
		this.engineering_date = newEngineering_date;
	}

	/**
	 * ���� engineering_undate��Getter����.���������ڽ�����_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getEngineering_undate() {
		return engineering_undate;
	}

	/**
	 * ����engineering_undate��Setter����.���������ڽ�����_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @param newEngineering_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setEngineering_undate(
			nc.vo.pub.lang.UFDate newEngineering_undate) {
		this.engineering_undate = newEngineering_undate;
	}

	/**
	 * ���� engineering_note��Getter����.���������ڽ����̵�Ѻ˵�� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEngineering_note() {
		return engineering_note;
	}

	/**
	 * ����engineering_note��Setter����.���������ڽ����̵�Ѻ˵�� ��������:2019-7-22
	 * 
	 * @param newEngineering_note
	 *            java.lang.String
	 */
	public void setEngineering_note(java.lang.String newEngineering_note) {
		this.engineering_note = newEngineering_note;
	}

	/**
	 * ���� property_detailed��Getter����.����������Ѻ��ҵ��ϸ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getProperty_detailed() {
		return property_detailed;
	}

	/**
	 * ����property_detailed��Setter����.����������Ѻ��ҵ��ϸ ��������:2019-7-22
	 * 
	 * @param newProperty_detailed
	 *            java.lang.String
	 */
	public void setProperty_detailed(java.lang.String newProperty_detailed) {
		this.property_detailed = newProperty_detailed;
	}

	/**
	 * ���� property_date��Getter����.����������ҵ_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getProperty_date() {
		return property_date;
	}

	/**
	 * ����property_date��Setter����.����������ҵ_��Ѻʱ�� ��������:2019-7-22
	 * 
	 * @param newProperty_date
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProperty_date(nc.vo.pub.lang.UFDate newProperty_date) {
		this.property_date = newProperty_date;
	}

	/**
	 * ���� property_undate��Getter����.����������ҵ_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getProperty_undate() {
		return property_undate;
	}

	/**
	 * ����property_undate��Setter����.����������ҵ_��ѹʱ�� ��������:2019-7-22
	 * 
	 * @param newProperty_undate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setProperty_undate(nc.vo.pub.lang.UFDate newProperty_undate) {
		this.property_undate = newProperty_undate;
	}

	/**
	 * ���� property_note��Getter����.����������Ѻ��ҵ˵�� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getProperty_note() {
		return property_note;
	}

	/**
	 * ����property_note��Setter����.����������Ѻ��ҵ˵�� ��������:2019-7-22
	 * 
	 * @param newProperty_note
	 *            java.lang.String
	 */
	public void setProperty_note(java.lang.String newProperty_note) {
		this.property_note = newProperty_note;
	}

	/**
	 * ���� pk_rep_mortgagelist��Getter����.������������ ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_rep_mortgagelist() {
		return pk_rep_mortgagelist;
	}

	/**
	 * ����pk_rep_mortgagelist��Setter����.������������ ��������:2019-7-22
	 * 
	 * @param newPk_rep_mortgagelist
	 *            java.lang.String
	 */
	public void setPk_rep_mortgagelist(java.lang.String newPk_rep_mortgagelist) {
		this.pk_rep_mortgagelist = newPk_rep_mortgagelist;
	}

	/**
	 * ���� creator��Getter����.�������������� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getCreator() {
		return creator;
	}

	/**
	 * ����creator��Setter����.�������������� ��������:2019-7-22
	 * 
	 * @param newCreator
	 *            java.lang.String
	 */
	public void setCreator(java.lang.String newCreator) {
		this.creator = newCreator;
	}

	/**
	 * ���� creationtime��Getter����.������������ʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime() {
		return creationtime;
	}

	/**
	 * ����creationtime��Setter����.������������ʱ�� ��������:2019-7-22
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(nc.vo.pub.lang.UFDateTime newCreationtime) {
		this.creationtime = newCreationtime;
	}

	/**
	 * ���� modifier��Getter����.���������޸��� ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getModifier() {
		return modifier;
	}

	/**
	 * ����modifier��Setter����.���������޸��� ��������:2019-7-22
	 * 
	 * @param newModifier
	 *            java.lang.String
	 */
	public void setModifier(java.lang.String newModifier) {
		this.modifier = newModifier;
	}

	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ�� ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime() {
		return modifiedtime;
	}

	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ�� ��������:2019-7-22
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(nc.vo.pub.lang.UFDateTime newModifiedtime) {
		this.modifiedtime = newModifiedtime;
	}

	/**
	 * ���� dr��Getter����.��������dr ��������:2019-7-22
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr() {
		return dr;
	}

	/**
	 * ����dr��Setter����.��������dr ��������:2019-7-22
	 * 
	 * @param newDr
	 *            java.lang.Integer
	 */
	public void setDr(java.lang.Integer newDr) {
		this.dr = newDr;
	}

	/**
	 * ���� ts��Getter����.��������ts ��������:2019-7-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs() {
		return ts;
	}

	/**
	 * ����ts��Setter����.��������ts ��������:2019-7-22
	 * 
	 * @param newTs
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(nc.vo.pub.lang.UFDateTime newTs) {
		this.ts = newTs;
	}

	/**
	 * <p>
	 * ȡ�ø�VO�����ֶ�.
	 * <p>
	 * ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getParentPKFieldName() {
		return null;
	}

	/**
	 * <p>
	 * ȡ�ñ�����.
	 * <p>
	 * ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPKFieldName() {

		return "pk_rep_mortgagelist";
	}

	/**
	 * <p>
	 * ���ر�����
	 * <p>
	 * ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "TGRZ_REP_MORTGAGELIST";
	}

	/**
	 * <p>
	 * ���ر�����.
	 * <p>
	 * ��������:2019-7-22
	 * 
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "TGRZ_REP_MORTGAGELIST";
	}

	/**
	 * ����Ĭ�Ϸ�ʽ����������.
	 * 
	 * ��������:2019-7-22
	 */
	public MortgageListDetailedVO() {
		super();
	}

	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.tg.mortgagelist.MortgageListDetailedVO")
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.MortgageListDetailed");

	}

}