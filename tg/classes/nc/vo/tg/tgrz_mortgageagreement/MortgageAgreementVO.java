package nc.vo.tg.tgrz_mortgageagreement;

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
 *  创建日期:2019-6-13
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class MortgageAgreementVO extends SuperVO {
	
/**
*按揭协议主键
*/
public java.lang.String pk_moragreement;
/**
*单据日期
*/
public UFDate dbilldate;
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
*id
*/
public java.lang.String id;
/**
*code
*/
public java.lang.String code;
/**
*name
*/
public java.lang.String name;
/**
*制单时间
*/
public UFDateTime maketime;
/**
*最后修改时间
*/
public UFDateTime lastmaketime;
/**
*行号
*/
public java.lang.String rowno;
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
public UFDateTime approvedate;
/**
*交易类型
*/
public java.lang.String transtype;
/**
*单据类型
*/
public java.lang.String billtype;
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
*标题
*/
public java.lang.String title;
/**
*申请人
*/
public java.lang.String proposer;
/**
*申请日期
*/
public UFDate applicationdate;
/**
*申请公司
*/
public java.lang.String applicationorg;
/**
*申请部门
*/
public java.lang.String applicationdept;
/**
*业务信息
*/
public java.lang.String busiinformation;
/**
*项目名称
*/
public java.lang.String proname;
/**
*收款单位
*/
public java.lang.String pk_payee;
/**
*付款单位
*/
public java.lang.String pk_payer;
/**
*合同总金额
*/
public nc.vo.pub.lang.UFDouble n_amount;
/**
*累计已付款
*/
public nc.vo.pub.lang.UFDouble i_totalpayamount;
/**
*本次情款金额
*/
public nc.vo.pub.lang.UFDouble i_applyamount;
/**
*集团会计
*/
public java.lang.String groupaccounting;
/**
*出纳
*/
public java.lang.String cashier;
/**
*用款内容
*/
public java.lang.String moneycontent;
/**
*相关流程
*/
public java.lang.String process;
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
*自定义文本1
*/
public java.lang.String big_text_a;
/**
*自定义文本2
*/
public java.lang.String big_text_b;
/**
*自定义文本3
*/
public java.lang.String big_text_c;
/**
*自定义文本4
*/
public java.lang.String big_text_d;
/**
*自定义文本5
*/
public java.lang.String big_text_e;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_moragreement的Getter方法.属性名：按揭协议主键
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getPk_moragreement() {
return this.pk_moragreement;
} 

/**
* 属性pk_moragreement的Setter方法.属性名：按揭协议主键
* 创建日期:2019-6-13
* @param newPk_moragreement java.lang.String
*/
public void setPk_moragreement ( java.lang.String pk_moragreement) {
this.pk_moragreement=pk_moragreement;
} 
 
/**
* 属性 dbilldate的Getter方法.属性名：单据日期
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* 属性dbilldate的Setter方法.属性名：单据日期
* 创建日期:2019-6-13
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-6-13
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-6-13
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-6-13
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-6-13
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-6-13
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-6-13
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-6-13
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-6-13
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-6-13
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-6-13
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-6-13
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-6-13
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 id的Getter方法.属性名：id
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getId() {
return this.id;
} 

/**
* 属性id的Setter方法.属性名：id
* 创建日期:2019-6-13
* @param newId java.lang.String
*/
public void setId ( java.lang.String id) {
this.id=id;
} 
 
/**
* 属性 code的Getter方法.属性名：code
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getCode() {
return this.code;
} 

/**
* 属性code的Setter方法.属性名：code
* 创建日期:2019-6-13
* @param newCode java.lang.String
*/
public void setCode ( java.lang.String code) {
this.code=code;
} 
 
/**
* 属性 name的Getter方法.属性名：name
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getName() {
return this.name;
} 

/**
* 属性name的Setter方法.属性名：name
* 创建日期:2019-6-13
* @param newName java.lang.String
*/
public void setName ( java.lang.String name) {
this.name=name;
} 
 
/**
* 属性 maketime的Getter方法.属性名：制单时间
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* 属性maketime的Setter方法.属性名：制单时间
* 创建日期:2019-6-13
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* 属性 lastmaketime的Getter方法.属性名：最后修改时间
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* 属性lastmaketime的Setter方法.属性名：最后修改时间
* 创建日期:2019-6-13
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2019-6-13
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-6-13
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-6-13
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2019-6-13
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-6-13
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2019-6-13
* @param newBillmaker java.lang.String
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-6-13
* @param newApprover java.lang.String
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-6-13
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-6-13
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-6-13
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-6-13
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-6-13
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-6-13
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-6-13
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-6-13
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-6-13
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-6-13
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-6-13
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-6-13
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 title的Getter方法.属性名：标题
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTitle() {
return this.title;
} 

/**
* 属性title的Setter方法.属性名：标题
* 创建日期:2019-6-13
* @param newTitle java.lang.String
*/
public void setTitle ( java.lang.String title) {
this.title=title;
} 
 
/**
* 属性 proposer的Getter方法.属性名：申请人
*  创建日期:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getProposer() {
return this.proposer;
} 

/**
* 属性proposer的Setter方法.属性名：申请人
* 创建日期:2019-6-13
* @param newProposer nc.vo.bd.psn.PsndocVO
*/
public void setProposer ( java.lang.String proposer) {
this.proposer=proposer;
} 
 
/**
* 属性 applicationdate的Getter方法.属性名：申请日期
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getApplicationdate() {
return this.applicationdate;
} 

/**
* 属性applicationdate的Setter方法.属性名：申请日期
* 创建日期:2019-6-13
* @param newApplicationdate nc.vo.pub.lang.UFDate
*/
public void setApplicationdate ( UFDate applicationdate) {
this.applicationdate=applicationdate;
} 
 
/**
* 属性 applicationorg的Getter方法.属性名：申请公司
*  创建日期:2019-6-13
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getApplicationorg() {
return this.applicationorg;
} 

/**
* 属性applicationorg的Setter方法.属性名：申请公司
* 创建日期:2019-6-13
* @param newApplicationorg nc.vo.org.FinanceOrgVO
*/
public void setApplicationorg ( java.lang.String applicationorg) {
this.applicationorg=applicationorg;
} 
 
/**
* 属性 applicationdept的Getter方法.属性名：申请部门
*  创建日期:2019-6-13
* @return nc.vo.org.DeptVO
*/
public java.lang.String getApplicationdept() {
return this.applicationdept;
} 

/**
* 属性applicationdept的Setter方法.属性名：申请部门
* 创建日期:2019-6-13
* @param newApplicationdept nc.vo.org.DeptVO
*/
public void setApplicationdept ( java.lang.String applicationdept) {
this.applicationdept=applicationdept;
} 
 
/**
* 属性 busiinformation的Getter方法.属性名：业务信息
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBusiinformation() {
return this.busiinformation;
} 

/**
* 属性busiinformation的Setter方法.属性名：业务信息
* 创建日期:2019-6-13
* @param newBusiinformation java.lang.String
*/
public void setBusiinformation ( java.lang.String busiinformation) {
this.busiinformation=busiinformation;
} 
 
/**
* 属性 proname的Getter方法.属性名：项目名称
*  创建日期:2019-6-13
* @return nc.vo.pmpub.project.ProjectHeadVO
*/
public java.lang.String getProname() {
return this.proname;
} 

/**
* 属性proname的Setter方法.属性名：项目名称
* 创建日期:2019-6-13
* @param newProname nc.vo.pmpub.project.ProjectHeadVO
*/
public void setProname ( java.lang.String proname) {
this.proname=proname;
} 
 
/**
* 属性 pk_payee的Getter方法.属性名：收款单位
*  创建日期:2019-6-13
* @return nc.vo.bd.cust.CustSupplierVO
*/
public java.lang.String getPk_payee() {
return this.pk_payee;
} 

/**
* 属性pk_payee的Setter方法.属性名：收款单位
* 创建日期:2019-6-13
* @param newPk_payee nc.vo.bd.cust.CustSupplierVO
*/
public void setPk_payee ( java.lang.String pk_payee) {
this.pk_payee=pk_payee;
} 
 
/**
* 属性 pk_payer的Getter方法.属性名：付款单位
*  创建日期:2019-6-13
* @return nc.vo.bd.cust.CustSupplierVO
*/
public java.lang.String getPk_payer() {
return this.pk_payer;
} 

/**
* 属性pk_payer的Setter方法.属性名：付款单位
* 创建日期:2019-6-13
* @param newPk_payer nc.vo.bd.cust.CustSupplierVO
*/
public void setPk_payer ( java.lang.String pk_payer) {
this.pk_payer=pk_payer;
} 
 
/**
* 属性 n_amount的Getter方法.属性名：合同总金额
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getN_amount() {
return this.n_amount;
} 

/**
* 属性n_amount的Setter方法.属性名：合同总金额
* 创建日期:2019-6-13
* @param newN_amount nc.vo.pub.lang.UFDouble
*/
public void setN_amount ( nc.vo.pub.lang.UFDouble n_amount) {
this.n_amount=n_amount;
} 
 
/**
* 属性 i_totalpayamount的Getter方法.属性名：累计已付款
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getI_totalpayamount() {
return this.i_totalpayamount;
} 

/**
* 属性i_totalpayamount的Setter方法.属性名：累计已付款
* 创建日期:2019-6-13
* @param newI_totalpayamount nc.vo.pub.lang.UFDouble
*/
public void setI_totalpayamount ( nc.vo.pub.lang.UFDouble i_totalpayamount) {
this.i_totalpayamount=i_totalpayamount;
} 
 
/**
* 属性 i_applyamount的Getter方法.属性名：本次情款金额
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getI_applyamount() {
return this.i_applyamount;
} 

/**
* 属性i_applyamount的Setter方法.属性名：本次情款金额
* 创建日期:2019-6-13
* @param newI_applyamount nc.vo.pub.lang.UFDouble
*/
public void setI_applyamount ( nc.vo.pub.lang.UFDouble i_applyamount) {
this.i_applyamount=i_applyamount;
} 
 
/**
* 属性 groupaccounting的Getter方法.属性名：集团会计
*  创建日期:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getGroupaccounting() {
return this.groupaccounting;
} 

/**
* 属性groupaccounting的Setter方法.属性名：集团会计
* 创建日期:2019-6-13
* @param newGroupaccounting nc.vo.bd.psn.PsndocVO
*/
public void setGroupaccounting ( java.lang.String groupaccounting) {
this.groupaccounting=groupaccounting;
} 
 
/**
* 属性 cashier的Getter方法.属性名：出纳
*  创建日期:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getCashier() {
return this.cashier;
} 

/**
* 属性cashier的Setter方法.属性名：出纳
* 创建日期:2019-6-13
* @param newCashier nc.vo.bd.psn.PsndocVO
*/
public void setCashier ( java.lang.String cashier) {
this.cashier=cashier;
} 
 
/**
* 属性 moneycontent的Getter方法.属性名：用款内容
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getMoneycontent() {
return this.moneycontent;
} 

/**
* 属性moneycontent的Setter方法.属性名：用款内容
* 创建日期:2019-6-13
* @param newMoneycontent java.lang.String
*/
public void setMoneycontent ( java.lang.String moneycontent) {
this.moneycontent=moneycontent;
} 
 
/**
* 属性 process的Getter方法.属性名：相关流程
*  创建日期:2019-6-13
* @return nc.vo.bd.defdoc.DefdocVO
*/
public java.lang.String getProcess() {
return this.process;
} 

/**
* 属性process的Setter方法.属性名：相关流程
* 创建日期:2019-6-13
* @param newProcess nc.vo.bd.defdoc.DefdocVO
*/
public void setProcess ( java.lang.String process) {
this.process=process;
} 
 
/**
* 属性 def1的Getter方法.属性名：自定义项1
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：自定义项1
* 创建日期:2019-6-13
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-6-13
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-6-13
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-6-13
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-6-13
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-6-13
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-6-13
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-6-13
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-6-13
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-6-13
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-6-13
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-6-13
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-6-13
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-6-13
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-6-13
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-6-13
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-6-13
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-6-13
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-6-13
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-6-13
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 


public java.lang.String getBig_text_a() {
	return big_text_a;
}

public void setBig_text_a(java.lang.String big_text_a) {
	this.big_text_a = big_text_a;
}

public java.lang.String getBig_text_b() {
	return big_text_b;
}

public void setBig_text_b(java.lang.String big_text_b) {
	this.big_text_b = big_text_b;
}

public java.lang.String getBig_text_c() {
	return big_text_c;
}

public void setBig_text_c(java.lang.String big_text_c) {
	this.big_text_c = big_text_c;
}

public java.lang.String getBig_text_d() {
	return big_text_d;
}

public void setBig_text_d(java.lang.String big_text_d) {
	this.big_text_d = big_text_d;
}

public java.lang.String getBig_text_e() {
	return big_text_e;
}

public void setBig_text_e(java.lang.String big_text_e) {
	this.big_text_e = big_text_e;
}

/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-6-13
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.MortgageAgreement");
    }
   }
    