package nc.vo.tg.financingplan;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> 此处简要描述此类功能 </b>
 * <p>
 *   此处添加累的描述信息
 * </p>
 *  创建日期:2019-7-9
 * @author 
 * @version NCPrj ??
 */
 
public class FinancingPlan extends SuperVO {
	
/**
*单据主键
*/
public java.lang.String pk_plan;
/**
*集团
*/
public java.lang.String pk_group;
/**
*组织
*/
public java.lang.String pk_org;
/**
*组织版本
*/
public java.lang.String pk_org_v;
/**
*单据号
*/
public java.lang.String billno;
/**
*单据类型
*/
public java.lang.Integer billtype;
/**
*审批状态
*/
public java.lang.Integer approvestatus;
/**
*单据日期
*/
public UFDate dbilldate;
/**
*项目
*/
public java.lang.String pk_project;
/**
*项目分期
*/
public java.lang.String project_phases;
/**
*融资类型
*/
public java.lang.String financing_type;
/**
*融资金额
*/
public nc.vo.pub.lang.UFDouble nfinancing_money;
/**
*融资年度
*/
public java.lang.Integer dfinancing_year;
/**
*年度计划融资金额
*/
public nc.vo.pub.lang.UFDouble nfinancing_plan;
/**
*备注
*/
public java.lang.String remark;
/**
*自定义项1
*/
public java.lang.String def1;
/**
*自定义项2
*/
public java.lang.String def2;
/**
*自定义项3
*/
public java.lang.String def3;
/**
*自定义项4
*/
public java.lang.String def4;
/**
*自定义项5
*/
public java.lang.String def5;
/**
*自定义项6
*/
public java.lang.String def6;
/**
*自定义项7
*/
public java.lang.String def7;
/**
*自定义项8
*/
public java.lang.String def8;
/**
*自定义项9
*/
public java.lang.String def9;
/**
*自定义项10
*/
public java.lang.String def10;
/**
*自定义项11
*/
public java.lang.String def11;
/**
*自定义项12
*/
public java.lang.String def12;
/**
*自定义项13
*/
public java.lang.String def13;
/**
*自定义项14
*/
public java.lang.String def14;
/**
*自定义项15
*/
public java.lang.String def15;
/**
*自定义项16
*/
public java.lang.String def16;
/**
*自定义项17
*/
public java.lang.String def17;
/**
*自定义项18
*/
public java.lang.String def18;
/**
*自定义项19
*/
public java.lang.String def19;
/**
*自定义项20
*/
public java.lang.String def20;
/**
*创建人
*/
public java.lang.String creator;
/**
*创建时间
*/
public UFDateTime creationtime;
/**
*修改人
*/
public java.lang.String modifier;
/**
*修改时间
*/
public UFDateTime modifiedtime;
/**
*审批人
*/
public java.lang.String approver;
/**
*审批批语
*/
public java.lang.String approvenote;
/**
*审批时间
*/
public UFDateTime approvedate;
/**
*业务类型
*/
public java.lang.String busitype;
/**
*交易类型
*/
public java.lang.String transtype;
/**
*交易类型pk
*/
public java.lang.String transtypepk;
/**
*来源单据类型
*/
public java.lang.String srcbilltype;
/**
*来源单据id
*/
public java.lang.String srcbillid;
/**
*修订枚举
*/
public java.lang.Integer emendenum;
/**
*单据版本pk
*/
public java.lang.String billversionpk;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_plan的Getter方法.属性名：单据主键
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getPk_plan() {
return this.pk_plan;
} 

/**
* 属性pk_plan的Setter方法.属性名：单据主键
* 创建日期:2019-7-9
* @param newPk_plan java.lang.String
*/
public void setPk_plan ( java.lang.String pk_plan) {
this.pk_plan=pk_plan;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-7-9
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-7-9
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-7-9
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-7-9
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-7-9
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-7-9
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-7-9
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-7-9
* @return java.lang.Integer
*/
public java.lang.Integer getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-7-9
* @param newBilltype java.lang.Integer
*/
public void setBilltype ( java.lang.Integer billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-7-9
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-7-9
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 dbilldate的Getter方法.属性名：单据日期
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* 属性dbilldate的Setter方法.属性名：单据日期
* 创建日期:2019-7-9
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* 属性 pk_project的Getter方法.属性名：项目
*  创建日期:2019-7-9
* @return nc.vo.tg.projectdata.ProjectDataVO
*/
public java.lang.String getPk_project() {
return this.pk_project;
} 

/**
* 属性pk_project的Setter方法.属性名：项目
* 创建日期:2019-7-9
* @param newPk_project nc.vo.tg.projectdata.ProjectDataVO
*/
public void setPk_project ( java.lang.String pk_project) {
this.pk_project=pk_project;
} 
 
/**
* 属性 project_phases的Getter方法.属性名：项目分期
*  创建日期:2019-7-9
* @return nc.vo.tg.projectdata.ProjectDataCVO
*/
public java.lang.String getProject_phases() {
return this.project_phases;
} 

/**
* 属性project_phases的Setter方法.属性名：项目分期
* 创建日期:2019-7-9
* @param newProject_phases nc.vo.tg.projectdata.ProjectDataCVO
*/
public void setProject_phases ( java.lang.String project_phases) {
this.project_phases=project_phases;
} 
 
/**
* 属性 financing_type的Getter方法.属性名：融资类型
*  创建日期:2019-7-9
* @return nc.vo.tg.fintype.FinTypeVO
*/
public java.lang.String getFinancing_type() {
return this.financing_type;
} 

/**
* 属性financing_type的Setter方法.属性名：融资类型
* 创建日期:2019-7-9
* @param newFinancing_type nc.vo.tg.fintype.FinTypeVO
*/
public void setFinancing_type ( java.lang.String financing_type) {
this.financing_type=financing_type;
} 
 
/**
* 属性 nfinancing_money的Getter方法.属性名：融资金额
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNfinancing_money() {
return this.nfinancing_money;
} 

/**
* 属性nfinancing_money的Setter方法.属性名：融资金额
* 创建日期:2019-7-9
* @param newNfinancing_money nc.vo.pub.lang.UFDouble
*/
public void setNfinancing_money ( nc.vo.pub.lang.UFDouble nfinancing_money) {
this.nfinancing_money=nfinancing_money;
} 
 
/**
* 属性 dfinancing_year的Getter方法.属性名：融资年度
*  创建日期:2019-7-9
* @return nc.voufob.datasource.yearenum
*/
public java.lang.Integer getDfinancing_year() {
return this.dfinancing_year;
} 

/**
* 属性dfinancing_year的Setter方法.属性名：融资年度
* 创建日期:2019-7-9
* @param newDfinancing_year nc.voufob.datasource.yearenum
*/
public void setDfinancing_year ( java.lang.Integer dfinancing_year) {
this.dfinancing_year=dfinancing_year;
} 
 
/**
* 属性 nfinancing_plan的Getter方法.属性名：年度计划融资金额
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNfinancing_plan() {
return this.nfinancing_plan;
} 

/**
* 属性nfinancing_plan的Setter方法.属性名：年度计划融资金额
* 创建日期:2019-7-9
* @param newNfinancing_plan nc.vo.pub.lang.UFDouble
*/
public void setNfinancing_plan ( nc.vo.pub.lang.UFDouble nfinancing_plan) {
this.nfinancing_plan=nfinancing_plan;
} 
 
/**
* 属性 remark的Getter方法.属性名：备注
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getRemark() {
return this.remark;
} 

/**
* 属性remark的Setter方法.属性名：备注
* 创建日期:2019-7-9
* @param newRemark java.lang.String
*/
public void setRemark ( java.lang.String remark) {
this.remark=remark;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-7-9
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-7-9
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-7-9
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-7-9
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-7-9
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-7-9
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-7-9
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-7-9
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-7-9
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-7-9
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-7-9
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-7-9
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-7-9
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-7-9
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-7-9
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-7-9
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-7-9
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-7-9
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-7-9
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-7-9
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-7-9
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-7-9
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-7-9
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-7-9
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-7-9
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-7-9
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-7-9
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-7-9
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-7-9
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-7-9
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-7-9
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-7-9
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-7-9
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-7-9
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-7-9
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-7-9
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-7-9
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-7-9
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-7-9
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-7-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-7-9
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.FinancingPlan");
    }
   }
    