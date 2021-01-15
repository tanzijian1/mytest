package nc.vo.tg.tg_groupdata;

import java.io.Serializable;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2019-7-17
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class GroupDataVO extends SuperVO implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6199533332603520045L;
	public static final String PK_GROUPDATA = "pk_groupdata";// 主键
	public static final String PK_GROUP = "pk_group";// 集团
	public static final String PK_ORG = "pk_org";// 组织
	public static final String PK_ORG_V = "pk_org_v";// 组织版本
	public static final String PROJECTNAME = "projectname";// 项目名称
	public static final String PROJECTSTAGING = "projectstaging";// 项目分期
	public static final String PROJECTDATATYPE = "projectdatatype";// 项目资料类别
	public static final String FINANCIALOFFICER = "financialofficer";// 财务负责人
	public static final String BILLID = "billid";// 单据ID
	public static final String BILLNO = "billno";// 单据号
	public static final String BUSITYPE0 = "busitype0";// 业务类型
	public static final String TRANSTYPE0 = "transtype0";// 交易类型
	public static final String BILLTYPE = "billtype";// 单据类型
	public static final String TRANSTYPEPK = "transtypepk";// 交易类型pk
	public static final String SRCBILLTYPE = "srcbilltype";// 来源单据类型
	public static final String SRCBILLID = "srcbillid";// 来源单据id
	public static final String BILLVERSIONPK = "billversionpk";// 单据版本pk
	public static final String BILLMAKER = "billmaker";// 制单人
	public static final String APPROVER = "approver";// 审批人
	public static final String APPROVESTATUS = "approvestatus";// 审批状态
	public static final String APPROVENOTE = "approvenote";// 审批批语
	public static final String APPROVEDATE = "approvedate";// 审批时间
	public static final String EMENDENUM = "emendenum";// 修订枚举
	public static final String PK_CASHIER = "pk_cashier";// 出纳
	public static final String EXTENSION = "extension";// 投拓
	public static final String DEVELOPMENT = "development";// 开发
	public static final String MARKETDEPT = "marketdept";// 市场部门
	public static final String ENABLESTATE = "enablestate";// 启用状态
	public static final String CREATOR = "creator";// 创建人
	public static final String CREATIONTIME = "creationtime";// 创建时间
	public static final String MODIFIER = "modifier";// 修改人
	public static final String MODIFIEDTIME = "modifiedtime";// 修改时间
	public static final String GROUPYEAR = "groupyear";// 修改时间
	public static final String DEF1 = "def1";// 自定义项1
	public static final String DEF2 = "def2";// 自定义项2
	public static final String DEF3 = "def3";// 自定义项3
	public static final String DEF4 = "def4";// 自定义项4
	public static final String DEF5 = "def5";// 自定义项5
	public static final String DEF6 = "def6";// 自定义项6
	public static final String DEF7 = "def7";// 自定义项7
	public static final String DEF8 = "def8";// 自定义项8
	public static final String DEF9 = "def9";// 自定义项9
	public static final String DEF10 = "def10";// 自定义项10
	public static final String DEF11 = "def11";// 自定义项11
	public static final String DEF12 = "def12";// 自定义项12
	public static final String DEF13 = "def13";// 自定义项13
	public static final String DEF14 = "def14";// 自定义项14
	public static final String DEF15 = "def15";// 自定义项15
	public static final String DEF16 = "def16";// 自定义项16
	public static final String DEF17 = "def17";// 自定义项17
	public static final String DEF18 = "def18";// 自定义项18
	public static final String DEF19 = "def19";// 自定义项19
	public static final String DEF20 = "def20";// 自定义项20
	public static final String DEF21 = "def21";// 自定义项21
	public static final String DEF22 = "def22";// 自定义项22
	public static final String DEF23 = "def23";// 自定义项23
	public static final String DEF24 = "def24";// 自定义项24
	public static final String DEF25 = "def25";// 自定义项25
	public static final String DEF26 = "def26";// 自定义项26
	public static final String DEF27 = "def27";// 自定义项27
	public static final String DEF28 = "def28";// 自定义项28
	public static final String DEF29 = "def29";// 自定义项29
	public static final String DEF30 = "def30";// 自定义项30
	public static final String BILLDATE = "billdate";// 业务日期
	
/**
*主键
*/
public String pk_groupdata;
/**
*集团
*/
public String pk_group;
/**
*组织
*/
public String pk_org;
/**
*组织版本
*/
public String pk_org_v;
/**
*项目名称
*/
public String projectname;
/**
*项目分期
*/
public String projectstaging;
/**
*项目资料类别
*/
public String projectdatatype;
/**
*财务负责人
*/
public String financialofficer;
/**
*单据ID
*/
public String billid;
/**
*单据号
*/
public String billno;
/**
*单据类型
*/
public String billtype;
/**
*审批人
*/
public String approver;
/**
*审批状态
*/
public Integer approvestatus;
/**
*审批批语
*/
public String approvenote;
/**
*审批时间
*/
public UFDateTime approvedate;
/**
*修订枚举
*/
public Integer emendenum;
/**
*出纳
*/
public String pk_cashier;
/**
*投拓
*/
public String extension;
/**
*开发
*/
public String development;
/**
*市场部门
*/
public String marketdept;
/**
*年份
*/
public Integer groupyear;
/**
*启用状态
*/
public String enablestate;
/**
*自定义项1
*/
public String def1;
/**
*自定义项2
*/
public String def2;
/**
*自定义项3
*/
public String def3;
/**
*自定义项4
*/
public String def4;
/**
*自定义项5
*/
public String def5;
/**
*自定义项6
*/
public String def6;
/**
*自定义项7
*/
public String def7;
/**
*自定义项8
*/
public String def8;
/**
*自定义项9
*/
public String def9;
/**
*自定义项10
*/
public String def10;
/**
*自定义项11
*/
public String def11;
/**
*自定义项12
*/
public String def12;
/**
*自定义项13
*/
public String def13;
/**
*自定义项14
*/
public String def14;
/**
*自定义项15
*/
public String def15;
/**
*自定义项16
*/
public String def16;
/**
*自定义项17
*/
public String def17;
/**
*自定义项18
*/
public String def18;
/**
*自定义项19
*/
public String def19;
/**
*自定义项20
*/
public String def20;
/**
*自定义项21
*/
public String def21;
/**
*自定义项22
*/
public String def22;
/**
*自定义项23
*/
public String def23;
/**
*自定义项24
*/
public String def24;
/**
*自定义项25
*/
public String def25;
/**
*自定义项26
*/
public String def26;
/**
*自定义项27
*/
public String def27;
/**
*自定义项28
*/
public String def28;
/**
*自定义项29
*/
public String def29;
/**
*自定义项30
*/
public String def30;
/**
*业务时间
*/
public UFDate billdate;
/**
*创建人
*/
public String creator;
/**
*创建时间
*/
public UFDateTime creationtime;
/**
*修改人
*/
public String modifier;
/**
*修改时间
*/
public UFDateTime modifiedtime;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_groupdata的Getter方法.属性名：主键
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getPk_groupdata() {
return this.pk_groupdata;
} 

/**
* 属性pk_groupdata的Setter方法.属性名：主键
* 创建日期:2019-7-17
* @param newPk_groupdata java.lang.String
*/
public void setPk_groupdata ( String pk_groupdata) {
this.pk_groupdata=pk_groupdata;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-7-17
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-7-17
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-7-17
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-7-17
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-7-17
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-7-17
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 projectname的Getter方法.属性名：项目名称
*  创建日期:2019-7-17
* @return nc.vo.tg.projectdata.ProjectDataVO
*/
public String getProjectname() {
return this.projectname;
} 

/**
* 属性projectname的Setter方法.属性名：项目名称
* 创建日期:2019-7-17
* @param newProjectname nc.vo.tg.projectdata.ProjectDataVO
*/
public void setProjectname ( String projectname) {
this.projectname=projectname;
} 
 
/**
* 属性 projectstaging的Getter方法.属性名：项目分期
*  创建日期:2019-7-17
* @return nc.vo.tg.projectdata.ProjectDataCVO
*/
public String getProjectstaging() {
return this.projectstaging;
} 

/**
* 属性projectstaging的Setter方法.属性名：项目分期
* 创建日期:2019-7-17
* @param newProjectstaging nc.vo.tg.projectdata.ProjectDataCVO
*/
public void setProjectstaging ( String projectstaging) {
this.projectstaging=projectstaging;
} 
 
/**
* 属性 projectdatatype的Getter方法.属性名：项目资料类别
*  创建日期:2019-7-17
* @return nc.vo.tg.datatype.DataTypeVO
*/
public String getProjectdatatype() {
return this.projectdatatype;
} 

/**
* 属性projectdatatype的Setter方法.属性名：项目资料类别
* 创建日期:2019-7-17
* @param newProjectdatatype nc.vo.tg.datatype.DataTypeVO
*/
public void setProjectdatatype ( String projectdatatype) {
this.projectdatatype=projectdatatype;
} 
 
/**
* 属性 financialofficer的Getter方法.属性名：财务负责人
*  创建日期:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getFinancialofficer() {
return this.financialofficer;
} 

/**
* 属性financialofficer的Setter方法.属性名：财务负责人
* 创建日期:2019-7-17
* @param newFinancialofficer nc.vo.bd.psn.PsndocVO
*/
public void setFinancialofficer ( String financialofficer) {
this.financialofficer=financialofficer;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-7-17
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-7-17
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-7-17
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-7-17
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-7-17
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-7-17
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-7-17
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-7-17
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-7-17
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-7-17
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 pk_cashier的Getter方法.属性名：出纳
*  创建日期:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getPk_cashier() {
return this.pk_cashier;
} 

/**
* 属性pk_cashier的Setter方法.属性名：出纳
* 创建日期:2019-7-17
* @param newPk_cashier nc.vo.bd.psn.PsndocVO
*/
public void setPk_cashier ( String pk_cashier) {
this.pk_cashier=pk_cashier;
} 
 
/**
* 属性 extension的Getter方法.属性名：投拓
*  创建日期:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getExtension() {
return this.extension;
} 

/**
* 属性extension的Setter方法.属性名：投拓
* 创建日期:2019-7-17
* @param newExtension nc.vo.bd.psn.PsndocVO
*/
public void setExtension ( String extension) {
this.extension=extension;
} 
 
/**
* 属性 development的Getter方法.属性名：开发
*  创建日期:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getDevelopment() {
return this.development;
} 

/**
* 属性development的Setter方法.属性名：开发
* 创建日期:2019-7-17
* @param newDevelopment nc.vo.bd.psn.PsndocVO
*/
public void setDevelopment ( String development) {
this.development=development;
} 
 
/**
* 属性 marketdept的Getter方法.属性名：市场部门
*  创建日期:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getMarketdept() {
return this.marketdept;
} 

/**
* 属性marketdept的Setter方法.属性名：市场部门
* 创建日期:2019-7-17
* @param newMarketdept nc.vo.bd.psn.PsndocVO
*/
public void setMarketdept ( String marketdept) {
this.marketdept=marketdept;
} 
 

/**
* 属性 groupyear的Getter方法.属性名：年份
*  创建日期:2019-7-17
* @return nc.voufob.datasource.yearenum
*/
public java.lang.Integer  getGroupyear() {
return this.groupyear;
} 

/**
* 属性groupyear的Setter方法.属性名：年份
* 创建日期:2019-7-17
* @param newGroupyear nc.voufob.datasource.yearenum
*/
public void setGroupyear (java.lang.Integer  groupyear) {
this.groupyear=groupyear;
} 
 
/**
* 属性 enablestate的Getter方法.属性名：启用状态
*  创建日期:2019-7-17
* @return nc.vo.bd.pub.EnableStateEnum
*/
public String getEnablestate() {
return this.enablestate;
} 

/**
* 属性enablestate的Setter方法.属性名：启用状态
* 创建日期:2019-7-17
* @param newEnablestate nc.vo.bd.pub.EnableStateEnum
*/
public void setEnablestate ( String enablestate) {
this.enablestate=enablestate;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-7-17
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-7-17
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-7-17
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-7-17
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-7-17
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-7-17
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-7-17
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-7-17
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-7-17
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-7-17
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-7-17
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-7-17
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-7-17
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-7-17
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-7-17
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-7-17
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-7-17
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-7-17
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-7-17
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-7-17
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* 属性 def21的Getter方法.属性名：自定义项21
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef21() {
return this.def21;
} 

/**
* 属性def21的Setter方法.属性名：自定义项21
* 创建日期:2019-7-17
* @param newDef21 java.lang.String
*/
public void setDef21 ( String def21) {
this.def21=def21;
} 
 
/**
* 属性 def22的Getter方法.属性名：自定义项22
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef22() {
return this.def22;
} 

/**
* 属性def22的Setter方法.属性名：自定义项22
* 创建日期:2019-7-17
* @param newDef22 java.lang.String
*/
public void setDef22 ( String def22) {
this.def22=def22;
} 
 
/**
* 属性 def23的Getter方法.属性名：自定义项23
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef23() {
return this.def23;
} 

/**
* 属性def23的Setter方法.属性名：自定义项23
* 创建日期:2019-7-17
* @param newDef23 java.lang.String
*/
public void setDef23 ( String def23) {
this.def23=def23;
} 
 
/**
* 属性 def24的Getter方法.属性名：自定义项24
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef24() {
return this.def24;
} 

/**
* 属性def24的Setter方法.属性名：自定义项24
* 创建日期:2019-7-17
* @param newDef24 java.lang.String
*/
public void setDef24 ( String def24) {
this.def24=def24;
} 
 
/**
* 属性 def25的Getter方法.属性名：自定义项25
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef25() {
return this.def25;
} 

/**
* 属性def25的Setter方法.属性名：自定义项25
* 创建日期:2019-7-17
* @param newDef25 java.lang.String
*/
public void setDef25 ( String def25) {
this.def25=def25;
} 
 
/**
* 属性 def26的Getter方法.属性名：自定义项26
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef26() {
return this.def26;
} 

/**
* 属性def26的Setter方法.属性名：自定义项26
* 创建日期:2019-7-17
* @param newDef26 java.lang.String
*/
public void setDef26 ( String def26) {
this.def26=def26;
} 
 
/**
* 属性 def27的Getter方法.属性名：自定义项27
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef27() {
return this.def27;
} 

/**
* 属性def27的Setter方法.属性名：自定义项27
* 创建日期:2019-7-17
* @param newDef27 java.lang.String
*/
public void setDef27 ( String def27) {
this.def27=def27;
} 
 
/**
* 属性 def28的Getter方法.属性名：自定义项28
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef28() {
return this.def28;
} 

/**
* 属性def28的Setter方法.属性名：自定义项28
* 创建日期:2019-7-17
* @param newDef28 java.lang.String
*/
public void setDef28 ( String def28) {
this.def28=def28;
} 
 
/**
* 属性 def29的Getter方法.属性名：自定义项29
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef29() {
return this.def29;
} 

/**
* 属性def29的Setter方法.属性名：自定义项29
* 创建日期:2019-7-17
* @param newDef29 java.lang.String
*/
public void setDef29 ( String def29) {
this.def29=def29;
} 
 
/**
* 属性 def30的Getter方法.属性名：自定义项30
*  创建日期:2019-7-17
* @return java.lang.String
*/
public String getDef30() {
return this.def30;
} 

/**
* 属性def30的Setter方法.属性名：自定义项30
* 创建日期:2019-7-17
* @param newDef30 java.lang.String
*/
public void setDef30 ( String def30) {
this.def30=def30;
} 
 
/**
* 属性 billdate的Getter方法.属性名：业务时间
*  创建日期:2019-7-17
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：业务时间
* 创建日期:2019-7-17
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-7-17
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-7-17
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-7-17
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-7-17
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-7-17
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.GroupDataVO");
    }
   }
    