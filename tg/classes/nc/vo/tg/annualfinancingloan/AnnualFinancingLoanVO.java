package nc.vo.tg.annualfinancingloan;

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
 *  创建日期:2019-6-12
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class AnnualFinancingLoanVO extends SuperVO {
	
/**
*年度融资放款表主键
*/
public java.lang.String pk_annfinloan;
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
*单据ID
*/
public java.lang.String billid;
/**
*单据号
*/
public java.lang.String billno;
/**
*所属组织
*/
public java.lang.String pkorg;
/**
*业务类型
*/
public java.lang.String busitype;
/**
*制单人
*/
public java.lang.String billmaker;
/**
*审批人
*/
public java.lang.String approver;
/**
*审批状态
*/
public java.lang.Integer approvestatus;
/**
*审批批语
*/
public java.lang.String approvenote;
/**
*审批时间
*/
public UFDate approvedate;
/**
*交易类型
*/
public java.lang.String transtype;
/**
*单据类型
*/
public java.lang.Integer billtype;
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
*单据主键
*/
public java.lang.String pk_plan;
/**
*创建人
*/
public java.lang.String creator;
/**
*创建时间
*/
public UFDate creationtime;
/**
*修改人
*/
public java.lang.String modifier;
/**
*修改时间
*/
public UFDate modifiedtime;
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
*单据日期
*/
public UFDate dbilldate;
/**
*年度
*/
public java.lang.String cyear;
/**
*项目名称
*/
public java.lang.String projectname;
/**
*项目公司名称
*/
public java.lang.String org_name;
/**
*借款人
*/
public java.lang.String borrower;
/**
*融资类型
*/
public java.lang.String financing_type;
/**
*融资机构
*/
public java.lang.String financing_org;
/**
*融资机构类别
*/
public java.lang.String financing_orgtype;
/**
*机构分行
*/
public java.lang.String branch;
/**
*合同起始日
*/
public UFDate begindate;
/**
*合同到期日
*/
public UFDate enddate;
/**
*融资金额
*/
public nc.vo.pub.lang.UFDouble nfinancing_money;
/**
*累计放款金额
*/
public nc.vo.pub.lang.UFDouble totalamount;
/**
*上一年放款金额
*/
public nc.vo.pub.lang.UFDouble beforeyearamount;
/**
*当年放款金额
*/
public nc.vo.pub.lang.UFDouble yearamount;
/**
*待放款金额
*/
public nc.vo.pub.lang.UFDouble prepareamount;
/**
*合同利率
*/
public java.lang.String contractrate;
/**
*放款时间
*/
public java.lang.String loantime;
/**
*1月实际放款
*/
public nc.vo.pub.lang.UFDouble january;
/**
*2月实际放款
*/
public nc.vo.pub.lang.UFDouble february;
/**
*3月实际放款
*/
public nc.vo.pub.lang.UFDouble march;
/**
*4月实际放款
*/
public nc.vo.pub.lang.UFDouble april;
/**
*5月实际放款
*/
public nc.vo.pub.lang.UFDouble may;
/**
*6月实际放款
*/
public nc.vo.pub.lang.UFDouble june;
/**
*7月实际放款
*/
public nc.vo.pub.lang.UFDouble july;
/**
*8月实际放款
*/
public nc.vo.pub.lang.UFDouble august;
/**
*9月实际放款
*/
public nc.vo.pub.lang.UFDouble september;
/**
*10月实际放款
*/
public nc.vo.pub.lang.UFDouble october;
/**
*11月实际放款
*/
public nc.vo.pub.lang.UFDouble november;
/**
*12月实际放款
*/
public nc.vo.pub.lang.UFDouble december;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_annfinloan的Getter方法.属性名：年度融资放款表主键
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPk_annfinloan() {
return this.pk_annfinloan;
} 

/**
* 属性pk_annfinloan的Setter方法.属性名：年度融资放款表主键
* 创建日期:2019-6-12
* @param newPk_annfinloan java.lang.String
*/
public void setPk_annfinloan ( java.lang.String pk_annfinloan) {
this.pk_annfinloan=pk_annfinloan;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-6-12
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-6-12
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-6-12
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-6-12
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-6-12
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-6-12
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-6-12
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-6-12
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2019-6-12
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-6-12
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2019-6-12
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-6-12
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-6-12
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-6-12
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-6-12
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-6-12
* @param newApprovedate nc.vo.pub.lang.UFDate
*/
public void setApprovedate ( UFDate approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-6-12
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-6-12
* @return java.lang.Integer
*/
public java.lang.Integer getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-6-12
* @param newBilltype java.lang.Integer
*/
public void setBilltype ( java.lang.Integer billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-6-12
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-6-12
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-6-12
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-6-12
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-6-12
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-6-12
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 pk_plan的Getter方法.属性名：单据主键
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPk_plan() {
return this.pk_plan;
} 

/**
* 属性pk_plan的Setter方法.属性名：单据主键
* 创建日期:2019-6-12
* @param newPk_plan java.lang.String
*/
public void setPk_plan ( java.lang.String pk_plan) {
this.pk_plan=pk_plan;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-6-12
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-6-12
* @param newCreationtime nc.vo.pub.lang.UFDate
*/
public void setCreationtime ( UFDate creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-6-12
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-6-12
* @param newModifiedtime nc.vo.pub.lang.UFDate
*/
public void setModifiedtime ( UFDate modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-6-12
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-6-12
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-6-12
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-6-12
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-6-12
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-6-12
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-6-12
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-6-12
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-6-12
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-6-12
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-6-12
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-6-12
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-6-12
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-6-12
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-6-12
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-6-12
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-6-12
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-6-12
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-6-12
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-6-12
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 dbilldate的Getter方法.属性名：单据日期
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* 属性dbilldate的Setter方法.属性名：单据日期
* 创建日期:2019-6-12
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* 属性 cyear的Getter方法.属性名：年度
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getCyear() {
return this.cyear;
} 

/**
* 属性cyear的Setter方法.属性名：年度
* 创建日期:2019-6-12
* @param newCyear java.lang.String
*/
public void setCyear ( java.lang.String cyear) {
this.cyear=cyear;
} 
 
/**
* 属性 projectname的Getter方法.属性名：项目名称
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getProjectname() {
return this.projectname;
} 

/**
* 属性projectname的Setter方法.属性名：项目名称
* 创建日期:2019-6-12
* @param newProjectname java.lang.String
*/
public void setProjectname ( java.lang.String projectname) {
this.projectname=projectname;
} 
 
/**
* 属性 org_name的Getter方法.属性名：项目公司名称
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getOrg_name() {
return this.org_name;
} 

/**
* 属性org_name的Setter方法.属性名：项目公司名称
* 创建日期:2019-6-12
* @param newOrg_name java.lang.String
*/
public void setOrg_name ( java.lang.String org_name) {
this.org_name=org_name;
} 
 
/**
* 属性 borrower的Getter方法.属性名：借款人
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBorrower() {
return this.borrower;
} 

/**
* 属性borrower的Setter方法.属性名：借款人
* 创建日期:2019-6-12
* @param newBorrower java.lang.String
*/
public void setBorrower ( java.lang.String borrower) {
this.borrower=borrower;
} 
 
/**
* 属性 financing_type的Getter方法.属性名：融资类型
*  创建日期:2019-6-12
* @return nc.vo.tg.fintype.FinTypeVO
*/
public java.lang.String getFinancing_type() {
return this.financing_type;
} 

/**
* 属性financing_type的Setter方法.属性名：融资类型
* 创建日期:2019-6-12
* @param newFinancing_type nc.vo.tg.fintype.FinTypeVO
*/
public void setFinancing_type ( java.lang.String financing_type) {
this.financing_type=financing_type;
} 
 
/**
* 属性 financing_org的Getter方法.属性名：融资机构
*  创建日期:2019-6-12
* @return nc.vo.tg.organization.OrganizationVO
*/
public java.lang.String getFinancing_org() {
return this.financing_org;
} 

/**
* 属性financing_org的Setter方法.属性名：融资机构
* 创建日期:2019-6-12
* @param newFinancing_org nc.vo.tg.organization.OrganizationVO
*/
public void setFinancing_org ( java.lang.String financing_org) {
this.financing_org=financing_org;
} 
 
/**
* 属性 financing_orgtype的Getter方法.属性名：融资机构类别
*  创建日期:2019-6-12
* @return nc.vo.tg.organizationtype.OrganizationTypeVO
*/
public java.lang.String getFinancing_orgtype() {
return this.financing_orgtype;
} 

/**
* 属性financing_orgtype的Setter方法.属性名：融资机构类别
* 创建日期:2019-6-12
* @param newFinancing_orgtype nc.vo.tg.organizationtype.OrganizationTypeVO
*/
public void setFinancing_orgtype ( java.lang.String financing_orgtype) {
this.financing_orgtype=financing_orgtype;
} 
 
/**
* 属性 branch的Getter方法.属性名：机构分行
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBranch() {
return this.branch;
} 

/**
* 属性branch的Setter方法.属性名：机构分行
* 创建日期:2019-6-12
* @param newBranch java.lang.String
*/
public void setBranch ( java.lang.String branch) {
this.branch=branch;
} 
 
/**
* 属性 begindate的Getter方法.属性名：合同起始日
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBegindate() {
return this.begindate;
} 

/**
* 属性begindate的Setter方法.属性名：合同起始日
* 创建日期:2019-6-12
* @param newBegindate nc.vo.pub.lang.UFDate
*/
public void setBegindate ( UFDate begindate) {
this.begindate=begindate;
} 
 
/**
* 属性 enddate的Getter方法.属性名：合同到期日
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getEnddate() {
return this.enddate;
} 

/**
* 属性enddate的Setter方法.属性名：合同到期日
* 创建日期:2019-6-12
* @param newEnddate nc.vo.pub.lang.UFDate
*/
public void setEnddate ( UFDate enddate) {
this.enddate=enddate;
} 
 
/**
* 属性 nfinancing_money的Getter方法.属性名：融资金额
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNfinancing_money() {
return this.nfinancing_money;
} 

/**
* 属性nfinancing_money的Setter方法.属性名：融资金额
* 创建日期:2019-6-12
* @param newNfinancing_money nc.vo.pub.lang.UFDouble
*/
public void setNfinancing_money ( nc.vo.pub.lang.UFDouble nfinancing_money) {
this.nfinancing_money=nfinancing_money;
} 
 
/**
* 属性 totalamount的Getter方法.属性名：累计放款金额
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTotalamount() {
return this.totalamount;
} 

/**
* 属性totalamount的Setter方法.属性名：累计放款金额
* 创建日期:2019-6-12
* @param newTotalamount nc.vo.pub.lang.UFDouble
*/
public void setTotalamount ( nc.vo.pub.lang.UFDouble totalamount) {
this.totalamount=totalamount;
} 
 
/**
* 属性 beforeyearamount的Getter方法.属性名：上一年放款金额
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getBeforeyearamount() {
return this.beforeyearamount;
} 

/**
* 属性beforeyearamount的Setter方法.属性名：上一年放款金额
* 创建日期:2019-6-12
* @param newBeforeyearamount nc.vo.pub.lang.UFDouble
*/
public void setBeforeyearamount ( nc.vo.pub.lang.UFDouble beforeyearamount) {
this.beforeyearamount=beforeyearamount;
} 
 
/**
* 属性 yearamount的Getter方法.属性名：当年放款金额
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getYearamount() {
return this.yearamount;
} 

/**
* 属性yearamount的Setter方法.属性名：当年放款金额
* 创建日期:2019-6-12
* @param newYearamount nc.vo.pub.lang.UFDouble
*/
public void setYearamount ( nc.vo.pub.lang.UFDouble yearamount) {
this.yearamount=yearamount;
} 
 
/**
* 属性 prepareamount的Getter方法.属性名：待放款金额
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getPrepareamount() {
return this.prepareamount;
} 

/**
* 属性prepareamount的Setter方法.属性名：待放款金额
* 创建日期:2019-6-12
* @param newPrepareamount nc.vo.pub.lang.UFDouble
*/
public void setPrepareamount ( nc.vo.pub.lang.UFDouble prepareamount) {
this.prepareamount=prepareamount;
} 
 
/**
* 属性 contractrate的Getter方法.属性名：合同利率
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getContractrate() {
return this.contractrate;
} 

/**
* 属性contractrate的Setter方法.属性名：合同利率
* 创建日期:2019-6-12
* @param newContractrate java.lang.String
*/
public void setContractrate ( java.lang.String contractrate) {
this.contractrate=contractrate;
} 
 
/**
* 属性 loantime的Getter方法.属性名：放款时间
*  创建日期:2019-6-12
* @return java.lang.String
*/
public java.lang.String getLoantime() {
return this.loantime;
} 

/**
* 属性loantime的Setter方法.属性名：放款时间
* 创建日期:2019-6-12
* @param newLoantime java.lang.String
*/
public void setLoantime ( java.lang.String loantime) {
this.loantime=loantime;
} 
 
/**
* 属性 january的Getter方法.属性名：1月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJanuary() {
return this.january;
} 

/**
* 属性january的Setter方法.属性名：1月实际放款
* 创建日期:2019-6-12
* @param newJanuary nc.vo.pub.lang.UFDouble
*/
public void setJanuary ( nc.vo.pub.lang.UFDouble january) {
this.january=january;
} 
 
/**
* 属性 february的Getter方法.属性名：2月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getFebruary() {
return this.february;
} 

/**
* 属性february的Setter方法.属性名：2月实际放款
* 创建日期:2019-6-12
* @param newFebruary nc.vo.pub.lang.UFDouble
*/
public void setFebruary ( nc.vo.pub.lang.UFDouble february) {
this.february=february;
} 
 
/**
* 属性 march的Getter方法.属性名：3月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMarch() {
return this.march;
} 

/**
* 属性march的Setter方法.属性名：3月实际放款
* 创建日期:2019-6-12
* @param newMarch nc.vo.pub.lang.UFDouble
*/
public void setMarch ( nc.vo.pub.lang.UFDouble march) {
this.march=march;
} 
 
/**
* 属性 april的Getter方法.属性名：4月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getApril() {
return this.april;
} 

/**
* 属性april的Setter方法.属性名：4月实际放款
* 创建日期:2019-6-12
* @param newApril nc.vo.pub.lang.UFDouble
*/
public void setApril ( nc.vo.pub.lang.UFDouble april) {
this.april=april;
} 
 
/**
* 属性 may的Getter方法.属性名：5月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMay() {
return this.may;
} 

/**
* 属性may的Setter方法.属性名：5月实际放款
* 创建日期:2019-6-12
* @param newMay nc.vo.pub.lang.UFDouble
*/
public void setMay ( nc.vo.pub.lang.UFDouble may) {
this.may=may;
} 
 
/**
* 属性 june的Getter方法.属性名：6月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJune() {
return this.june;
} 

/**
* 属性june的Setter方法.属性名：6月实际放款
* 创建日期:2019-6-12
* @param newJune nc.vo.pub.lang.UFDouble
*/
public void setJune ( nc.vo.pub.lang.UFDouble june) {
this.june=june;
} 
 
/**
* 属性 july的Getter方法.属性名：7月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJuly() {
return this.july;
} 

/**
* 属性july的Setter方法.属性名：7月实际放款
* 创建日期:2019-6-12
* @param newJuly nc.vo.pub.lang.UFDouble
*/
public void setJuly ( nc.vo.pub.lang.UFDouble july) {
this.july=july;
} 
 
/**
* 属性 august的Getter方法.属性名：8月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getAugust() {
return this.august;
} 

/**
* 属性august的Setter方法.属性名：8月实际放款
* 创建日期:2019-6-12
* @param newAugust nc.vo.pub.lang.UFDouble
*/
public void setAugust ( nc.vo.pub.lang.UFDouble august) {
this.august=august;
} 
 
/**
* 属性 september的Getter方法.属性名：9月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getSeptember() {
return this.september;
} 

/**
* 属性september的Setter方法.属性名：9月实际放款
* 创建日期:2019-6-12
* @param newSeptember nc.vo.pub.lang.UFDouble
*/
public void setSeptember ( nc.vo.pub.lang.UFDouble september) {
this.september=september;
} 
 
/**
* 属性 october的Getter方法.属性名：10月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getOctober() {
return this.october;
} 

/**
* 属性october的Setter方法.属性名：10月实际放款
* 创建日期:2019-6-12
* @param newOctober nc.vo.pub.lang.UFDouble
*/
public void setOctober ( nc.vo.pub.lang.UFDouble october) {
this.october=october;
} 
 
/**
* 属性 november的Getter方法.属性名：11月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNovember() {
return this.november;
} 

/**
* 属性november的Setter方法.属性名：11月实际放款
* 创建日期:2019-6-12
* @param newNovember nc.vo.pub.lang.UFDouble
*/
public void setNovember ( nc.vo.pub.lang.UFDouble november) {
this.november=november;
} 
 
/**
* 属性 december的Getter方法.属性名：12月实际放款
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDecember() {
return this.december;
} 

/**
* 属性december的Setter方法.属性名：12月实际放款
* 创建日期:2019-6-12
* @param newDecember nc.vo.pub.lang.UFDouble
*/
public void setDecember ( nc.vo.pub.lang.UFDouble december) {
this.december=december;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-6-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-6-12
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.AnnualFinancingLoan");
    }
   }
    