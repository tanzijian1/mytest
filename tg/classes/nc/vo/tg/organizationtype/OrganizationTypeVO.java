package nc.vo.tg.organizationtype;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�������������Ϣ
 * </p>
 *  ��������:2019-6-9
 * @author YONYOU NC
 * @version NCPrj ??
 */
public class OrganizationTypeVO extends nc.vo.pub.SuperVO{
	
    private java.lang.String pk_organizationtype;
    private java.lang.String pk_group;
    private java.lang.String pk_org;
    private java.lang.String pk_org_v;
    private java.lang.String code;
    private java.lang.String name;
    private java.lang.String pk_father;
    private java.lang.Integer enablestate;
    private java.lang.String creator;
    private nc.vo.pub.lang.UFDateTime creationtime;
    private java.lang.String modifier;
    private nc.vo.pub.lang.UFDateTime modifiedtime;
    private java.lang.String def1;
    private java.lang.String def2;
    private java.lang.String def3;
    private java.lang.String def4;
    private java.lang.String def5;
    private java.lang.String def6;
    private java.lang.String def7;
    private java.lang.String def8;
    private java.lang.String def9;
    private java.lang.String def10;
    private java.lang.String def11;
    private java.lang.String def12;
    private java.lang.String def13;
    private java.lang.String def14;
    private java.lang.String def15;
    private java.lang.String def16;
    private java.lang.String def17;
    private java.lang.String def18;
    private java.lang.String def19;
    private java.lang.String def20;
    private java.lang.String def21;
    private java.lang.String def22;
    private java.lang.String def23;
    private java.lang.String def24;
    private java.lang.String def25;
    private java.lang.String def26;
    private java.lang.String def27;
    private java.lang.String def28;
    private java.lang.String def29;
    private java.lang.String def30;
    private nc.vo.pub.lang.UFDate billdate;
    private java.lang.String innercode;
    private java.lang.Integer dr = 0;
    private nc.vo.pub.lang.UFDateTime ts;    
	
	
    public static final String PK_ORGANIZATIONTYPE = "pk_organizationtype";
    public static final String PK_GROUP = "pk_group";
    public static final String PK_ORG = "pk_org";
    public static final String PK_ORG_V = "pk_org_v";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String PK_FATHER = "pk_father";
    public static final String ENABLESTATE = "enablestate";
    public static final String CREATOR = "creator";
    public static final String CREATIONTIME = "creationtime";
    public static final String MODIFIER = "modifier";
    public static final String MODIFIEDTIME = "modifiedtime";
    public static final String DEF1 = "def1";
    public static final String DEF2 = "def2";
    public static final String DEF3 = "def3";
    public static final String DEF4 = "def4";
    public static final String DEF5 = "def5";
    public static final String DEF6 = "def6";
    public static final String DEF7 = "def7";
    public static final String DEF8 = "def8";
    public static final String DEF9 = "def9";
    public static final String DEF10 = "def10";
    public static final String DEF11 = "def11";
    public static final String DEF12 = "def12";
    public static final String DEF13 = "def13";
    public static final String DEF14 = "def14";
    public static final String DEF15 = "def15";
    public static final String DEF16 = "def16";
    public static final String DEF17 = "def17";
    public static final String DEF18 = "def18";
    public static final String DEF19 = "def19";
    public static final String DEF20 = "def20";
    public static final String DEF21 = "def21";
    public static final String DEF22 = "def22";
    public static final String DEF23 = "def23";
    public static final String DEF24 = "def24";
    public static final String DEF25 = "def25";
    public static final String DEF26 = "def26";
    public static final String DEF27 = "def27";
    public static final String DEF28 = "def28";
    public static final String DEF29 = "def29";
    public static final String DEF30 = "def30";
    public static final String BILLDATE = "billdate";
    public static final String INNERCODE = "innercode";

	/**
	 * ���� pk_organizationtype��Getter����.������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_organizationtype () {
		return pk_organizationtype;
	}   
	/**
	 * ����pk_organizationtype��Setter����.������������
	 * ��������:2019-6-9
	 * @param newPk_organizationtype java.lang.String
	 */
	public void setPk_organizationtype (java.lang.String newPk_organizationtype ) {
	 	this.pk_organizationtype = newPk_organizationtype;
	} 	 
	
	/**
	 * ���� pk_group��Getter����.������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * ����pk_group��Setter����.������������
	 * ��������:2019-6-9
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * ���� pk_org��Getter����.����������֯
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * ����pk_org��Setter����.����������֯
	 * ��������:2019-6-9
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * ���� pk_org_v��Getter����.����������֯�汾
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v () {
		return pk_org_v;
	}   
	/**
	 * ����pk_org_v��Setter����.����������֯�汾
	 * ��������:2019-6-9
	 * @param newPk_org_v java.lang.String
	 */
	public void setPk_org_v (java.lang.String newPk_org_v ) {
	 	this.pk_org_v = newPk_org_v;
	} 	 
	
	/**
	 * ���� code��Getter����.������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * ����code��Setter����.������������
	 * ��������:2019-6-9
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * ���� name��Getter����.������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * ����name��Setter����.������������
	 * ��������:2019-6-9
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * ���� pk_father��Getter����.���������ϼ��������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_father () {
		return pk_father;
	}   
	/**
	 * ����pk_father��Setter����.���������ϼ��������
	 * ��������:2019-6-9
	 * @param newPk_father java.lang.String
	 */
	public void setPk_father (java.lang.String newPk_father ) {
	 	this.pk_father = newPk_father;
	} 	 
	
	/**
	 * ���� enablestate��Getter����.������������״̬
	 *  ��������:2019-6-9
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEnablestate () {
		return enablestate;
	}   
	/**
	 * ����enablestate��Setter����.������������״̬
	 * ��������:2019-6-9
	 * @param newEnablestate java.lang.Integer
	 */
	public void setEnablestate (java.lang.Integer newEnablestate ) {
	 	this.enablestate = newEnablestate;
	} 	 
	
	/**
	 * ���� creator��Getter����.��������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getCreator () {
		return creator;
	}   
	/**
	 * ����creator��Setter����.��������������
	 * ��������:2019-6-9
	 * @param newCreator java.lang.String
	 */
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	 
	
	/**
	 * ���� creationtime��Getter����.������������ʱ��
	 *  ��������:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime () {
		return creationtime;
	}   
	/**
	 * ����creationtime��Setter����.������������ʱ��
	 * ��������:2019-6-9
	 * @param newCreationtime nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime (nc.vo.pub.lang.UFDateTime newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	 
	
	/**
	 * ���� modifier��Getter����.���������޸���
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getModifier () {
		return modifier;
	}   
	/**
	 * ����modifier��Setter����.���������޸���
	 * ��������:2019-6-9
	 * @param newModifier java.lang.String
	 */
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	 
	
	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ��
	 *  ��������:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime () {
		return modifiedtime;
	}   
	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ��
	 * ��������:2019-6-9
	 * @param newModifiedtime nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime (nc.vo.pub.lang.UFDateTime newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	 
	
	/**
	 * ���� def1��Getter����.���������Զ�����1
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * ����def1��Setter����.���������Զ�����1
	 * ��������:2019-6-9
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * ���� def2��Getter����.���������Զ�����2
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * ����def2��Setter����.���������Զ�����2
	 * ��������:2019-6-9
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * ���� def3��Getter����.���������Զ�����3
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * ����def3��Setter����.���������Զ�����3
	 * ��������:2019-6-9
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * ���� def4��Getter����.���������Զ�����4
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * ����def4��Setter����.���������Զ�����4
	 * ��������:2019-6-9
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * ���� def5��Getter����.���������Զ�����5
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * ����def5��Setter����.���������Զ�����5
	 * ��������:2019-6-9
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * ���� def6��Getter����.���������Զ�����6
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * ����def6��Setter����.���������Զ�����6
	 * ��������:2019-6-9
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * ���� def7��Getter����.���������Զ�����7
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * ����def7��Setter����.���������Զ�����7
	 * ��������:2019-6-9
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * ���� def8��Getter����.���������Զ�����8
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * ����def8��Setter����.���������Զ�����8
	 * ��������:2019-6-9
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * ���� def9��Getter����.���������Զ�����9
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef9 () {
		return def9;
	}   
	/**
	 * ����def9��Setter����.���������Զ�����9
	 * ��������:2019-6-9
	 * @param newDef9 java.lang.String
	 */
	public void setDef9 (java.lang.String newDef9 ) {
	 	this.def9 = newDef9;
	} 	 
	
	/**
	 * ���� def10��Getter����.���������Զ�����10
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * ����def10��Setter����.���������Զ�����10
	 * ��������:2019-6-9
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * ���� def11��Getter����.���������Զ�����11
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef11 () {
		return def11;
	}   
	/**
	 * ����def11��Setter����.���������Զ�����11
	 * ��������:2019-6-9
	 * @param newDef11 java.lang.String
	 */
	public void setDef11 (java.lang.String newDef11 ) {
	 	this.def11 = newDef11;
	} 	 
	
	/**
	 * ���� def12��Getter����.���������Զ�����12
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef12 () {
		return def12;
	}   
	/**
	 * ����def12��Setter����.���������Զ�����12
	 * ��������:2019-6-9
	 * @param newDef12 java.lang.String
	 */
	public void setDef12 (java.lang.String newDef12 ) {
	 	this.def12 = newDef12;
	} 	 
	
	/**
	 * ���� def13��Getter����.���������Զ�����13
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef13 () {
		return def13;
	}   
	/**
	 * ����def13��Setter����.���������Զ�����13
	 * ��������:2019-6-9
	 * @param newDef13 java.lang.String
	 */
	public void setDef13 (java.lang.String newDef13 ) {
	 	this.def13 = newDef13;
	} 	 
	
	/**
	 * ���� def14��Getter����.���������Զ�����14
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef14 () {
		return def14;
	}   
	/**
	 * ����def14��Setter����.���������Զ�����14
	 * ��������:2019-6-9
	 * @param newDef14 java.lang.String
	 */
	public void setDef14 (java.lang.String newDef14 ) {
	 	this.def14 = newDef14;
	} 	 
	
	/**
	 * ���� def15��Getter����.���������Զ�����15
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef15 () {
		return def15;
	}   
	/**
	 * ����def15��Setter����.���������Զ�����15
	 * ��������:2019-6-9
	 * @param newDef15 java.lang.String
	 */
	public void setDef15 (java.lang.String newDef15 ) {
	 	this.def15 = newDef15;
	} 	 
	
	/**
	 * ���� def16��Getter����.���������Զ�����16
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef16 () {
		return def16;
	}   
	/**
	 * ����def16��Setter����.���������Զ�����16
	 * ��������:2019-6-9
	 * @param newDef16 java.lang.String
	 */
	public void setDef16 (java.lang.String newDef16 ) {
	 	this.def16 = newDef16;
	} 	 
	
	/**
	 * ���� def17��Getter����.���������Զ�����17
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef17 () {
		return def17;
	}   
	/**
	 * ����def17��Setter����.���������Զ�����17
	 * ��������:2019-6-9
	 * @param newDef17 java.lang.String
	 */
	public void setDef17 (java.lang.String newDef17 ) {
	 	this.def17 = newDef17;
	} 	 
	
	/**
	 * ���� def18��Getter����.���������Զ�����18
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef18 () {
		return def18;
	}   
	/**
	 * ����def18��Setter����.���������Զ�����18
	 * ��������:2019-6-9
	 * @param newDef18 java.lang.String
	 */
	public void setDef18 (java.lang.String newDef18 ) {
	 	this.def18 = newDef18;
	} 	 
	
	/**
	 * ���� def19��Getter����.���������Զ�����19
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef19 () {
		return def19;
	}   
	/**
	 * ����def19��Setter����.���������Զ�����19
	 * ��������:2019-6-9
	 * @param newDef19 java.lang.String
	 */
	public void setDef19 (java.lang.String newDef19 ) {
	 	this.def19 = newDef19;
	} 	 
	
	/**
	 * ���� def20��Getter����.���������Զ�����20
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef20 () {
		return def20;
	}   
	/**
	 * ����def20��Setter����.���������Զ�����20
	 * ��������:2019-6-9
	 * @param newDef20 java.lang.String
	 */
	public void setDef20 (java.lang.String newDef20 ) {
	 	this.def20 = newDef20;
	} 	 
	
	/**
	 * ���� def21��Getter����.���������Զ�����21
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef21 () {
		return def21;
	}   
	/**
	 * ����def21��Setter����.���������Զ�����21
	 * ��������:2019-6-9
	 * @param newDef21 java.lang.String
	 */
	public void setDef21 (java.lang.String newDef21 ) {
	 	this.def21 = newDef21;
	} 	 
	
	/**
	 * ���� def22��Getter����.���������Զ�����22
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef22 () {
		return def22;
	}   
	/**
	 * ����def22��Setter����.���������Զ�����22
	 * ��������:2019-6-9
	 * @param newDef22 java.lang.String
	 */
	public void setDef22 (java.lang.String newDef22 ) {
	 	this.def22 = newDef22;
	} 	 
	
	/**
	 * ���� def23��Getter����.���������Զ�����23
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef23 () {
		return def23;
	}   
	/**
	 * ����def23��Setter����.���������Զ�����23
	 * ��������:2019-6-9
	 * @param newDef23 java.lang.String
	 */
	public void setDef23 (java.lang.String newDef23 ) {
	 	this.def23 = newDef23;
	} 	 
	
	/**
	 * ���� def24��Getter����.���������Զ�����24
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef24 () {
		return def24;
	}   
	/**
	 * ����def24��Setter����.���������Զ�����24
	 * ��������:2019-6-9
	 * @param newDef24 java.lang.String
	 */
	public void setDef24 (java.lang.String newDef24 ) {
	 	this.def24 = newDef24;
	} 	 
	
	/**
	 * ���� def25��Getter����.���������Զ�����25
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef25 () {
		return def25;
	}   
	/**
	 * ����def25��Setter����.���������Զ�����25
	 * ��������:2019-6-9
	 * @param newDef25 java.lang.String
	 */
	public void setDef25 (java.lang.String newDef25 ) {
	 	this.def25 = newDef25;
	} 	 
	
	/**
	 * ���� def26��Getter����.���������Զ�����26
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef26 () {
		return def26;
	}   
	/**
	 * ����def26��Setter����.���������Զ�����26
	 * ��������:2019-6-9
	 * @param newDef26 java.lang.String
	 */
	public void setDef26 (java.lang.String newDef26 ) {
	 	this.def26 = newDef26;
	} 	 
	
	/**
	 * ���� def27��Getter����.���������Զ�����27
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef27 () {
		return def27;
	}   
	/**
	 * ����def27��Setter����.���������Զ�����27
	 * ��������:2019-6-9
	 * @param newDef27 java.lang.String
	 */
	public void setDef27 (java.lang.String newDef27 ) {
	 	this.def27 = newDef27;
	} 	 
	
	/**
	 * ���� def28��Getter����.���������Զ�����28
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef28 () {
		return def28;
	}   
	/**
	 * ����def28��Setter����.���������Զ�����28
	 * ��������:2019-6-9
	 * @param newDef28 java.lang.String
	 */
	public void setDef28 (java.lang.String newDef28 ) {
	 	this.def28 = newDef28;
	} 	 
	
	/**
	 * ���� def29��Getter����.���������Զ�����29
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef29 () {
		return def29;
	}   
	/**
	 * ����def29��Setter����.���������Զ�����29
	 * ��������:2019-6-9
	 * @param newDef29 java.lang.String
	 */
	public void setDef29 (java.lang.String newDef29 ) {
	 	this.def29 = newDef29;
	} 	 
	
	/**
	 * ���� def30��Getter����.���������Զ�����30
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef30 () {
		return def30;
	}   
	/**
	 * ����def30��Setter����.���������Զ�����30
	 * ��������:2019-6-9
	 * @param newDef30 java.lang.String
	 */
	public void setDef30 (java.lang.String newDef30 ) {
	 	this.def30 = newDef30;
	} 	 
	
	/**
	 * ���� billdate��Getter����.��������ҵ������
	 *  ��������:2019-6-9
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getBilldate () {
		return billdate;
	}   
	/**
	 * ����billdate��Setter����.��������ҵ������
	 * ��������:2019-6-9
	 * @param newBilldate nc.vo.pub.lang.UFDate
	 */
	public void setBilldate (nc.vo.pub.lang.UFDate newBilldate ) {
	 	this.billdate = newBilldate;
	} 	 
	
	/**
	 * ���� innercode��Getter����.��������������
	 *  ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getInnercode () {
		return innercode;
	}   
	/**
	 * ����innercode��Setter����.��������������
	 * ��������:2019-6-9
	 * @param newInnercode java.lang.String
	 */
	public void setInnercode (java.lang.String newInnercode ) {
	 	this.innercode = newInnercode;
	} 	 
	
	/**
	 * ���� dr��Getter����.��������dr
	 *  ��������:2019-6-9
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * ����dr��Setter����.��������dr
	 * ��������:2019-6-9
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * ���� ts��Getter����.��������ts
	 *  ��������:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * ����ts��Setter����.��������ts
	 * ��������:2019-6-9
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>ȡ�ø�VO�����ֶ�.
	  * <p>
	  * ��������:2019-6-9
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>ȡ�ñ�����.
	  * <p>
	  * ��������:2019-6-9
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_organizationtype";
	}
    
	/**
	 * <p>���ر�����
	 * <p>
	 * ��������:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tgrz_OrganizationType";
	}    
	
	/**
	 * <p>���ر�����.
	 * <p>
	 * ��������:2019-6-9
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "tgrz_OrganizationType";
	}    
    
    /**
	  * ����Ĭ�Ϸ�ʽ����������.
	  *
	  * ��������:2019-6-9
	  */
     public OrganizationTypeVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.tg.organizationtype.OrganizationTypeVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.OrganizationTypeVO");
		
   	}
     
}