package nc.vo.tg.organizationtype;

import nc.vo.pub.IVOMeta;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加类的描述信息
 * </p>
 *  创建日期:2019-6-9
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
	 * 属性 pk_organizationtype的Getter方法.属性名：主键
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_organizationtype () {
		return pk_organizationtype;
	}   
	/**
	 * 属性pk_organizationtype的Setter方法.属性名：主键
	 * 创建日期:2019-6-9
	 * @param newPk_organizationtype java.lang.String
	 */
	public void setPk_organizationtype (java.lang.String newPk_organizationtype ) {
	 	this.pk_organizationtype = newPk_organizationtype;
	} 	 
	
	/**
	 * 属性 pk_group的Getter方法.属性名：集团
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_group () {
		return pk_group;
	}   
	/**
	 * 属性pk_group的Setter方法.属性名：集团
	 * 创建日期:2019-6-9
	 * @param newPk_group java.lang.String
	 */
	public void setPk_group (java.lang.String newPk_group ) {
	 	this.pk_group = newPk_group;
	} 	 
	
	/**
	 * 属性 pk_org的Getter方法.属性名：组织
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org () {
		return pk_org;
	}   
	/**
	 * 属性pk_org的Setter方法.属性名：组织
	 * 创建日期:2019-6-9
	 * @param newPk_org java.lang.String
	 */
	public void setPk_org (java.lang.String newPk_org ) {
	 	this.pk_org = newPk_org;
	} 	 
	
	/**
	 * 属性 pk_org_v的Getter方法.属性名：组织版本
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_org_v () {
		return pk_org_v;
	}   
	/**
	 * 属性pk_org_v的Setter方法.属性名：组织版本
	 * 创建日期:2019-6-9
	 * @param newPk_org_v java.lang.String
	 */
	public void setPk_org_v (java.lang.String newPk_org_v ) {
	 	this.pk_org_v = newPk_org_v;
	} 	 
	
	/**
	 * 属性 code的Getter方法.属性名：编码
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getCode () {
		return code;
	}   
	/**
	 * 属性code的Setter方法.属性名：编码
	 * 创建日期:2019-6-9
	 * @param newCode java.lang.String
	 */
	public void setCode (java.lang.String newCode ) {
	 	this.code = newCode;
	} 	 
	
	/**
	 * 属性 name的Getter方法.属性名：名称
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getName () {
		return name;
	}   
	/**
	 * 属性name的Setter方法.属性名：名称
	 * 创建日期:2019-6-9
	 * @param newName java.lang.String
	 */
	public void setName (java.lang.String newName ) {
	 	this.name = newName;
	} 	 
	
	/**
	 * 属性 pk_father的Getter方法.属性名：上级机构类别
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getPk_father () {
		return pk_father;
	}   
	/**
	 * 属性pk_father的Setter方法.属性名：上级机构类别
	 * 创建日期:2019-6-9
	 * @param newPk_father java.lang.String
	 */
	public void setPk_father (java.lang.String newPk_father ) {
	 	this.pk_father = newPk_father;
	} 	 
	
	/**
	 * 属性 enablestate的Getter方法.属性名：启用状态
	 *  创建日期:2019-6-9
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEnablestate () {
		return enablestate;
	}   
	/**
	 * 属性enablestate的Setter方法.属性名：启用状态
	 * 创建日期:2019-6-9
	 * @param newEnablestate java.lang.Integer
	 */
	public void setEnablestate (java.lang.Integer newEnablestate ) {
	 	this.enablestate = newEnablestate;
	} 	 
	
	/**
	 * 属性 creator的Getter方法.属性名：创建人
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getCreator () {
		return creator;
	}   
	/**
	 * 属性creator的Setter方法.属性名：创建人
	 * 创建日期:2019-6-9
	 * @param newCreator java.lang.String
	 */
	public void setCreator (java.lang.String newCreator ) {
	 	this.creator = newCreator;
	} 	 
	
	/**
	 * 属性 creationtime的Getter方法.属性名：创建时间
	 *  创建日期:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getCreationtime () {
		return creationtime;
	}   
	/**
	 * 属性creationtime的Setter方法.属性名：创建时间
	 * 创建日期:2019-6-9
	 * @param newCreationtime nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime (nc.vo.pub.lang.UFDateTime newCreationtime ) {
	 	this.creationtime = newCreationtime;
	} 	 
	
	/**
	 * 属性 modifier的Getter方法.属性名：修改人
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getModifier () {
		return modifier;
	}   
	/**
	 * 属性modifier的Setter方法.属性名：修改人
	 * 创建日期:2019-6-9
	 * @param newModifier java.lang.String
	 */
	public void setModifier (java.lang.String newModifier ) {
	 	this.modifier = newModifier;
	} 	 
	
	/**
	 * 属性 modifiedtime的Getter方法.属性名：修改时间
	 *  创建日期:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getModifiedtime () {
		return modifiedtime;
	}   
	/**
	 * 属性modifiedtime的Setter方法.属性名：修改时间
	 * 创建日期:2019-6-9
	 * @param newModifiedtime nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime (nc.vo.pub.lang.UFDateTime newModifiedtime ) {
	 	this.modifiedtime = newModifiedtime;
	} 	 
	
	/**
	 * 属性 def1的Getter方法.属性名：自定义项1
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef1 () {
		return def1;
	}   
	/**
	 * 属性def1的Setter方法.属性名：自定义项1
	 * 创建日期:2019-6-9
	 * @param newDef1 java.lang.String
	 */
	public void setDef1 (java.lang.String newDef1 ) {
	 	this.def1 = newDef1;
	} 	 
	
	/**
	 * 属性 def2的Getter方法.属性名：自定义项2
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef2 () {
		return def2;
	}   
	/**
	 * 属性def2的Setter方法.属性名：自定义项2
	 * 创建日期:2019-6-9
	 * @param newDef2 java.lang.String
	 */
	public void setDef2 (java.lang.String newDef2 ) {
	 	this.def2 = newDef2;
	} 	 
	
	/**
	 * 属性 def3的Getter方法.属性名：自定义项3
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef3 () {
		return def3;
	}   
	/**
	 * 属性def3的Setter方法.属性名：自定义项3
	 * 创建日期:2019-6-9
	 * @param newDef3 java.lang.String
	 */
	public void setDef3 (java.lang.String newDef3 ) {
	 	this.def3 = newDef3;
	} 	 
	
	/**
	 * 属性 def4的Getter方法.属性名：自定义项4
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef4 () {
		return def4;
	}   
	/**
	 * 属性def4的Setter方法.属性名：自定义项4
	 * 创建日期:2019-6-9
	 * @param newDef4 java.lang.String
	 */
	public void setDef4 (java.lang.String newDef4 ) {
	 	this.def4 = newDef4;
	} 	 
	
	/**
	 * 属性 def5的Getter方法.属性名：自定义项5
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef5 () {
		return def5;
	}   
	/**
	 * 属性def5的Setter方法.属性名：自定义项5
	 * 创建日期:2019-6-9
	 * @param newDef5 java.lang.String
	 */
	public void setDef5 (java.lang.String newDef5 ) {
	 	this.def5 = newDef5;
	} 	 
	
	/**
	 * 属性 def6的Getter方法.属性名：自定义项6
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef6 () {
		return def6;
	}   
	/**
	 * 属性def6的Setter方法.属性名：自定义项6
	 * 创建日期:2019-6-9
	 * @param newDef6 java.lang.String
	 */
	public void setDef6 (java.lang.String newDef6 ) {
	 	this.def6 = newDef6;
	} 	 
	
	/**
	 * 属性 def7的Getter方法.属性名：自定义项7
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef7 () {
		return def7;
	}   
	/**
	 * 属性def7的Setter方法.属性名：自定义项7
	 * 创建日期:2019-6-9
	 * @param newDef7 java.lang.String
	 */
	public void setDef7 (java.lang.String newDef7 ) {
	 	this.def7 = newDef7;
	} 	 
	
	/**
	 * 属性 def8的Getter方法.属性名：自定义项8
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef8 () {
		return def8;
	}   
	/**
	 * 属性def8的Setter方法.属性名：自定义项8
	 * 创建日期:2019-6-9
	 * @param newDef8 java.lang.String
	 */
	public void setDef8 (java.lang.String newDef8 ) {
	 	this.def8 = newDef8;
	} 	 
	
	/**
	 * 属性 def9的Getter方法.属性名：自定义项9
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef9 () {
		return def9;
	}   
	/**
	 * 属性def9的Setter方法.属性名：自定义项9
	 * 创建日期:2019-6-9
	 * @param newDef9 java.lang.String
	 */
	public void setDef9 (java.lang.String newDef9 ) {
	 	this.def9 = newDef9;
	} 	 
	
	/**
	 * 属性 def10的Getter方法.属性名：自定义项10
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef10 () {
		return def10;
	}   
	/**
	 * 属性def10的Setter方法.属性名：自定义项10
	 * 创建日期:2019-6-9
	 * @param newDef10 java.lang.String
	 */
	public void setDef10 (java.lang.String newDef10 ) {
	 	this.def10 = newDef10;
	} 	 
	
	/**
	 * 属性 def11的Getter方法.属性名：自定义项11
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef11 () {
		return def11;
	}   
	/**
	 * 属性def11的Setter方法.属性名：自定义项11
	 * 创建日期:2019-6-9
	 * @param newDef11 java.lang.String
	 */
	public void setDef11 (java.lang.String newDef11 ) {
	 	this.def11 = newDef11;
	} 	 
	
	/**
	 * 属性 def12的Getter方法.属性名：自定义项12
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef12 () {
		return def12;
	}   
	/**
	 * 属性def12的Setter方法.属性名：自定义项12
	 * 创建日期:2019-6-9
	 * @param newDef12 java.lang.String
	 */
	public void setDef12 (java.lang.String newDef12 ) {
	 	this.def12 = newDef12;
	} 	 
	
	/**
	 * 属性 def13的Getter方法.属性名：自定义项13
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef13 () {
		return def13;
	}   
	/**
	 * 属性def13的Setter方法.属性名：自定义项13
	 * 创建日期:2019-6-9
	 * @param newDef13 java.lang.String
	 */
	public void setDef13 (java.lang.String newDef13 ) {
	 	this.def13 = newDef13;
	} 	 
	
	/**
	 * 属性 def14的Getter方法.属性名：自定义项14
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef14 () {
		return def14;
	}   
	/**
	 * 属性def14的Setter方法.属性名：自定义项14
	 * 创建日期:2019-6-9
	 * @param newDef14 java.lang.String
	 */
	public void setDef14 (java.lang.String newDef14 ) {
	 	this.def14 = newDef14;
	} 	 
	
	/**
	 * 属性 def15的Getter方法.属性名：自定义项15
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef15 () {
		return def15;
	}   
	/**
	 * 属性def15的Setter方法.属性名：自定义项15
	 * 创建日期:2019-6-9
	 * @param newDef15 java.lang.String
	 */
	public void setDef15 (java.lang.String newDef15 ) {
	 	this.def15 = newDef15;
	} 	 
	
	/**
	 * 属性 def16的Getter方法.属性名：自定义项16
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef16 () {
		return def16;
	}   
	/**
	 * 属性def16的Setter方法.属性名：自定义项16
	 * 创建日期:2019-6-9
	 * @param newDef16 java.lang.String
	 */
	public void setDef16 (java.lang.String newDef16 ) {
	 	this.def16 = newDef16;
	} 	 
	
	/**
	 * 属性 def17的Getter方法.属性名：自定义项17
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef17 () {
		return def17;
	}   
	/**
	 * 属性def17的Setter方法.属性名：自定义项17
	 * 创建日期:2019-6-9
	 * @param newDef17 java.lang.String
	 */
	public void setDef17 (java.lang.String newDef17 ) {
	 	this.def17 = newDef17;
	} 	 
	
	/**
	 * 属性 def18的Getter方法.属性名：自定义项18
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef18 () {
		return def18;
	}   
	/**
	 * 属性def18的Setter方法.属性名：自定义项18
	 * 创建日期:2019-6-9
	 * @param newDef18 java.lang.String
	 */
	public void setDef18 (java.lang.String newDef18 ) {
	 	this.def18 = newDef18;
	} 	 
	
	/**
	 * 属性 def19的Getter方法.属性名：自定义项19
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef19 () {
		return def19;
	}   
	/**
	 * 属性def19的Setter方法.属性名：自定义项19
	 * 创建日期:2019-6-9
	 * @param newDef19 java.lang.String
	 */
	public void setDef19 (java.lang.String newDef19 ) {
	 	this.def19 = newDef19;
	} 	 
	
	/**
	 * 属性 def20的Getter方法.属性名：自定义项20
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef20 () {
		return def20;
	}   
	/**
	 * 属性def20的Setter方法.属性名：自定义项20
	 * 创建日期:2019-6-9
	 * @param newDef20 java.lang.String
	 */
	public void setDef20 (java.lang.String newDef20 ) {
	 	this.def20 = newDef20;
	} 	 
	
	/**
	 * 属性 def21的Getter方法.属性名：自定义项21
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef21 () {
		return def21;
	}   
	/**
	 * 属性def21的Setter方法.属性名：自定义项21
	 * 创建日期:2019-6-9
	 * @param newDef21 java.lang.String
	 */
	public void setDef21 (java.lang.String newDef21 ) {
	 	this.def21 = newDef21;
	} 	 
	
	/**
	 * 属性 def22的Getter方法.属性名：自定义项22
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef22 () {
		return def22;
	}   
	/**
	 * 属性def22的Setter方法.属性名：自定义项22
	 * 创建日期:2019-6-9
	 * @param newDef22 java.lang.String
	 */
	public void setDef22 (java.lang.String newDef22 ) {
	 	this.def22 = newDef22;
	} 	 
	
	/**
	 * 属性 def23的Getter方法.属性名：自定义项23
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef23 () {
		return def23;
	}   
	/**
	 * 属性def23的Setter方法.属性名：自定义项23
	 * 创建日期:2019-6-9
	 * @param newDef23 java.lang.String
	 */
	public void setDef23 (java.lang.String newDef23 ) {
	 	this.def23 = newDef23;
	} 	 
	
	/**
	 * 属性 def24的Getter方法.属性名：自定义项24
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef24 () {
		return def24;
	}   
	/**
	 * 属性def24的Setter方法.属性名：自定义项24
	 * 创建日期:2019-6-9
	 * @param newDef24 java.lang.String
	 */
	public void setDef24 (java.lang.String newDef24 ) {
	 	this.def24 = newDef24;
	} 	 
	
	/**
	 * 属性 def25的Getter方法.属性名：自定义项25
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef25 () {
		return def25;
	}   
	/**
	 * 属性def25的Setter方法.属性名：自定义项25
	 * 创建日期:2019-6-9
	 * @param newDef25 java.lang.String
	 */
	public void setDef25 (java.lang.String newDef25 ) {
	 	this.def25 = newDef25;
	} 	 
	
	/**
	 * 属性 def26的Getter方法.属性名：自定义项26
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef26 () {
		return def26;
	}   
	/**
	 * 属性def26的Setter方法.属性名：自定义项26
	 * 创建日期:2019-6-9
	 * @param newDef26 java.lang.String
	 */
	public void setDef26 (java.lang.String newDef26 ) {
	 	this.def26 = newDef26;
	} 	 
	
	/**
	 * 属性 def27的Getter方法.属性名：自定义项27
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef27 () {
		return def27;
	}   
	/**
	 * 属性def27的Setter方法.属性名：自定义项27
	 * 创建日期:2019-6-9
	 * @param newDef27 java.lang.String
	 */
	public void setDef27 (java.lang.String newDef27 ) {
	 	this.def27 = newDef27;
	} 	 
	
	/**
	 * 属性 def28的Getter方法.属性名：自定义项28
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef28 () {
		return def28;
	}   
	/**
	 * 属性def28的Setter方法.属性名：自定义项28
	 * 创建日期:2019-6-9
	 * @param newDef28 java.lang.String
	 */
	public void setDef28 (java.lang.String newDef28 ) {
	 	this.def28 = newDef28;
	} 	 
	
	/**
	 * 属性 def29的Getter方法.属性名：自定义项29
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef29 () {
		return def29;
	}   
	/**
	 * 属性def29的Setter方法.属性名：自定义项29
	 * 创建日期:2019-6-9
	 * @param newDef29 java.lang.String
	 */
	public void setDef29 (java.lang.String newDef29 ) {
	 	this.def29 = newDef29;
	} 	 
	
	/**
	 * 属性 def30的Getter方法.属性名：自定义项30
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getDef30 () {
		return def30;
	}   
	/**
	 * 属性def30的Setter方法.属性名：自定义项30
	 * 创建日期:2019-6-9
	 * @param newDef30 java.lang.String
	 */
	public void setDef30 (java.lang.String newDef30 ) {
	 	this.def30 = newDef30;
	} 	 
	
	/**
	 * 属性 billdate的Getter方法.属性名：业务日期
	 *  创建日期:2019-6-9
	 * @return nc.vo.pub.lang.UFDate
	 */
	public nc.vo.pub.lang.UFDate getBilldate () {
		return billdate;
	}   
	/**
	 * 属性billdate的Setter方法.属性名：业务日期
	 * 创建日期:2019-6-9
	 * @param newBilldate nc.vo.pub.lang.UFDate
	 */
	public void setBilldate (nc.vo.pub.lang.UFDate newBilldate ) {
	 	this.billdate = newBilldate;
	} 	 
	
	/**
	 * 属性 innercode的Getter方法.属性名：助记码
	 *  创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getInnercode () {
		return innercode;
	}   
	/**
	 * 属性innercode的Setter方法.属性名：助记码
	 * 创建日期:2019-6-9
	 * @param newInnercode java.lang.String
	 */
	public void setInnercode (java.lang.String newInnercode ) {
	 	this.innercode = newInnercode;
	} 	 
	
	/**
	 * 属性 dr的Getter方法.属性名：dr
	 *  创建日期:2019-6-9
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getDr () {
		return dr;
	}   
	/**
	 * 属性dr的Setter方法.属性名：dr
	 * 创建日期:2019-6-9
	 * @param newDr java.lang.Integer
	 */
	public void setDr (java.lang.Integer newDr ) {
	 	this.dr = newDr;
	} 	 
	
	/**
	 * 属性 ts的Getter方法.属性名：ts
	 *  创建日期:2019-6-9
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public nc.vo.pub.lang.UFDateTime getTs () {
		return ts;
	}   
	/**
	 * 属性ts的Setter方法.属性名：ts
	 * 创建日期:2019-6-9
	 * @param newTs nc.vo.pub.lang.UFDateTime
	 */
	public void setTs (nc.vo.pub.lang.UFDateTime newTs ) {
	 	this.ts = newTs;
	} 	 
	
	
	/**
	  * <p>取得父VO主键字段.
	  * <p>
	  * 创建日期:2019-6-9
	  * @return java.lang.String
	  */
	public java.lang.String getParentPKFieldName() {
	    return null;
	}   
    
	/**
	  * <p>取得表主键.
	  * <p>
	  * 创建日期:2019-6-9
	  * @return java.lang.String
	  */
	public java.lang.String getPKFieldName() {
			
		return "pk_organizationtype";
	}
    
	/**
	 * <p>返回表名称
	 * <p>
	 * 创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public java.lang.String getTableName() {
		return "tgrz_OrganizationType";
	}    
	
	/**
	 * <p>返回表名称.
	 * <p>
	 * 创建日期:2019-6-9
	 * @return java.lang.String
	 */
	public static java.lang.String getDefaultTableName() {
		return "tgrz_OrganizationType";
	}    
    
    /**
	  * 按照默认方式创建构造子.
	  *
	  * 创建日期:2019-6-9
	  */
     public OrganizationTypeVO() {
		super();	
	}    
	
	
	@nc.vo.annotation.MDEntityInfo(beanFullclassName = "nc.vo.tg.organizationtype.OrganizationTypeVO" )
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.OrganizationTypeVO");
		
   	}
     
}