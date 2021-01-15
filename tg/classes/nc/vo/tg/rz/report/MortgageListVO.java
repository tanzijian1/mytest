package nc.vo.tg.rz.report;

import java.io.Serializable;

/**
 * ��Ѻ���嵥
 * 
 * @author HUANGDQ
 * @date 2019��7��4�� ����11:13:46
 */
public class MortgageListVO implements Serializable, Cloneable {
	String pk_project;// ��ĿID
	String projectcode;// ��Ŀ����
	String projectname;// ��Ŀ����
	String pk_periodization;// ��������
	String periodizationname;// ��������

	// ����
	String land_state;// ��Ŀ���ص�Ѻ״̬
	String land_date;// ��Ѻʱ��
	String land_undate;// ��ѹʱ��
	String land_note;// ���ص�Ѻ˵��

	// �ڽ�����
	String engineering_state;// ��Ŀ�ڽ����̵�Ѻ״̬
	String mortgagedetails ;//��Ѻ�ڽ�������ϸ
	String engineering_date;// ��Ѻʱ��
	String engineering_undate;// ��ѹʱ��
	String engineering_note;// �ڽ����̵�Ѻ˵��

	// ��ҵ
	String property_detailed;// ��Ѻ��ҵ��ϸ
	String property_date;// ��Ѻʱ��
	String property_undate;// ��ѹʱ��
	String property_note;// ��Ѻ��ҵ˵��

	String creator;
	String creationtime;
	String modifier;
	String modifiedtime;
	
	

	public String getMortgagedetails() {
		return mortgagedetails;
	}

	public void setMortgagedetails(String mortgagedetails) {
		this.mortgagedetails = mortgagedetails;
	}

	public String getPk_project() {
		return pk_project;
	}

	public void setPk_project(String pk_project) {
		this.pk_project = pk_project;
	}

	public String getProjectcode() {
		return projectcode;
	}

	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}

	public String getProjectname() {
		return projectname;
	}

	public void setProjectname(String projectname) {
		this.projectname = projectname;
	}

	public String getPk_periodization() {
		return pk_periodization;
	}

	public void setPk_periodization(String pk_periodization) {
		this.pk_periodization = pk_periodization;
	}

	public String getPeriodizationname() {
		return periodizationname;
	}

	public void setPeriodizationname(String periodizationname) {
		this.periodizationname = periodizationname;
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

	public java.lang.String getCreator() {
		return creator;
	}

	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	public String getCreationtime() {
		return creationtime;
	}

	public void setCreationtime(String creationtime) {
		this.creationtime = creationtime;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	public String getModifiedtime() {
		return modifiedtime;
	}

	public void setModifiedtime(String modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

}
