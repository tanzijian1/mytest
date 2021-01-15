package nc.vo.tg.tartingbill;

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
 *  创建日期:2019-9-6
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class TartingBillVO extends SuperVO {
	
/**
*挞定工单主表主键
*/
public java.lang.String pk_tartingbill_h;
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
*最后修改人
*/
public java.lang.String modifier;
/**
*最后修改时间
*/
public UFDateTime modifiedtime;
/**
*单据号
*/
public java.lang.String billno;
/**
*业务类型
*/
public java.lang.String busitype;
/**
*业务流程
*/
public java.lang.String pk_busitype;
/**
*单据状态
*/
public java.lang.String billstatus;
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
*生效状态
*/
public java.lang.Integer effectstatus;
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
*单据确定人
*/
public java.lang.String confirmuser;
/**
*生效日期
*/
public UFDateTime effectdate;
/**
*单据日期
*/
public UFDate billdate;
/**
*支付人
*/
public java.lang.String payman;
/**
*支付日期
*/
public UFDateTime paydate;
/**
*签字人
*/
public java.lang.String signuser;
/**
*签字日期
*/
public UFDateTime signdate;
/**
*正式打印人
*/
public java.lang.String officialprintuser;
/**
*正式打印日期
*/
public UFDateTime officialprintdate;
/**
*单据来源系统
*/
public java.lang.Integer src_syscode;
/**
*直接借记退回人
*/
public java.lang.String sddreversaler;
/**
*直接借记退回日期
*/
public UFDateTime sddreversaldate;
/**
*销售系统单号
*/
public java.lang.String def1;
/**
*影像编码
*/
public java.lang.String def2;
/**
*影像状态
*/
public java.lang.String def3;
/**
*备注
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
*自定义项21
*/
public java.lang.String def21;
/**
*自定义项22
*/
public java.lang.String def22;
/**
*自定义项23
*/
public java.lang.String def23;
/**
*自定义项24
*/
public java.lang.String def24;
/**
*自定义项25
*/
public java.lang.String def25;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_tartingbill_h的Getter方法.属性名：挞定工单主表主键
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getPk_tartingbill_h() {
return this.pk_tartingbill_h;
} 

/**
* 属性pk_tartingbill_h的Setter方法.属性名：挞定工单主表主键
* 创建日期:2019-9-6
* @param newPk_tartingbill_h java.lang.String
*/
public void setPk_tartingbill_h ( java.lang.String pk_tartingbill_h) {
this.pk_tartingbill_h=pk_tartingbill_h;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-9-6
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-9-6
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-9-6
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-9-6
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-9-6
* @return nc.vo.vorg.FinanceOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-9-6
* @param newPk_org_v nc.vo.vorg.FinanceOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-9-6
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-9-6
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：最后修改人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：最后修改人
* 创建日期:2019-9-6
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：最后修改时间
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：最后修改时间
* 创建日期:2019-9-6
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-9-6
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-9-6
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 pk_busitype的Getter方法.属性名：业务流程
*  创建日期:2019-9-6
* @return nc.vo.pf.pub.BusitypeVO
*/
public java.lang.String getPk_busitype() {
return this.pk_busitype;
} 

/**
* 属性pk_busitype的Setter方法.属性名：业务流程
* 创建日期:2019-9-6
* @param newPk_busitype nc.vo.pf.pub.BusitypeVO
*/
public void setPk_busitype ( java.lang.String pk_busitype) {
this.pk_busitype=pk_busitype;
} 
 
/**
* 属性 billstatus的Getter方法.属性名：单据状态
*  创建日期:2019-9-6
* @return nc.voarap.arapgl.BILL_STATUS
*/
public java.lang.String getBillstatus() {
return this.billstatus;
} 

/**
* 属性billstatus的Setter方法.属性名：单据状态
* 创建日期:2019-9-6
* @param newBillstatus nc.voarap.arapgl.BILL_STATUS
*/
public void setBillstatus ( java.lang.String billstatus) {
this.billstatus=billstatus;
} 
 
/**
* 属性 billmaker的Getter方法.属性名：制单人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* 属性billmaker的Setter方法.属性名：制单人
* 创建日期:2019-9-6
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-9-6
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-9-6
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-9-6
* @param newApprovestatus nc.vo.arap.pub.BillEnumCollection
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-9-6
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-9-6
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 effectstatus的Getter方法.属性名：生效状态
*  创建日期:2019-9-6
* @return nc.vo.cmp.bill.Effectflag
*/
public java.lang.Integer getEffectstatus() {
return this.effectstatus;
} 

/**
* 属性effectstatus的Setter方法.属性名：生效状态
* 创建日期:2019-9-6
* @param newEffectstatus nc.vo.cmp.bill.Effectflag
*/
public void setEffectstatus ( java.lang.Integer effectstatus) {
this.effectstatus=effectstatus;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-9-6
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-9-6
* @param newTranstype nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-9-6
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-9-6
* @param newBilltype nc.vo.pub.billtype.BilltypeVO
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-9-6
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 confirmuser的Getter方法.属性名：单据确定人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getConfirmuser() {
return this.confirmuser;
} 

/**
* 属性confirmuser的Setter方法.属性名：单据确定人
* 创建日期:2019-9-6
* @param newConfirmuser nc.vo.sm.UserVO
*/
public void setConfirmuser ( java.lang.String confirmuser) {
this.confirmuser=confirmuser;
} 
 
/**
* 属性 effectdate的Getter方法.属性名：生效日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getEffectdate() {
return this.effectdate;
} 

/**
* 属性effectdate的Setter方法.属性名：生效日期
* 创建日期:2019-9-6
* @param newEffectdate nc.vo.pub.lang.UFDateTime
*/
public void setEffectdate ( UFDateTime effectdate) {
this.effectdate=effectdate;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2019-9-6
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 payman的Getter方法.属性名：支付人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getPayman() {
return this.payman;
} 

/**
* 属性payman的Setter方法.属性名：支付人
* 创建日期:2019-9-6
* @param newPayman nc.vo.sm.UserVO
*/
public void setPayman ( java.lang.String payman) {
this.payman=payman;
} 
 
/**
* 属性 paydate的Getter方法.属性名：支付日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getPaydate() {
return this.paydate;
} 

/**
* 属性paydate的Setter方法.属性名：支付日期
* 创建日期:2019-9-6
* @param newPaydate nc.vo.pub.lang.UFDateTime
*/
public void setPaydate ( UFDateTime paydate) {
this.paydate=paydate;
} 
 
/**
* 属性 signuser的Getter方法.属性名：签字人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getSignuser() {
return this.signuser;
} 

/**
* 属性signuser的Setter方法.属性名：签字人
* 创建日期:2019-9-6
* @param newSignuser nc.vo.sm.UserVO
*/
public void setSignuser ( java.lang.String signuser) {
this.signuser=signuser;
} 
 
/**
* 属性 signdate的Getter方法.属性名：签字日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSigndate() {
return this.signdate;
} 

/**
* 属性signdate的Setter方法.属性名：签字日期
* 创建日期:2019-9-6
* @param newSigndate nc.vo.pub.lang.UFDateTime
*/
public void setSigndate ( UFDateTime signdate) {
this.signdate=signdate;
} 
 
/**
* 属性 officialprintuser的Getter方法.属性名：正式打印人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getOfficialprintuser() {
return this.officialprintuser;
} 

/**
* 属性officialprintuser的Setter方法.属性名：正式打印人
* 创建日期:2019-9-6
* @param newOfficialprintuser nc.vo.sm.UserVO
*/
public void setOfficialprintuser ( java.lang.String officialprintuser) {
this.officialprintuser=officialprintuser;
} 
 
/**
* 属性 officialprintdate的Getter方法.属性名：正式打印日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getOfficialprintdate() {
return this.officialprintdate;
} 

/**
* 属性officialprintdate的Setter方法.属性名：正式打印日期
* 创建日期:2019-9-6
* @param newOfficialprintdate nc.vo.pub.lang.UFDateTime
*/
public void setOfficialprintdate ( UFDateTime officialprintdate) {
this.officialprintdate=officialprintdate;
} 
 
/**
* 属性 src_syscode的Getter方法.属性名：单据来源系统
*  创建日期:2019-9-6
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getSrc_syscode() {
return this.src_syscode;
} 

/**
* 属性src_syscode的Setter方法.属性名：单据来源系统
* 创建日期:2019-9-6
* @param newSrc_syscode nc.vo.arap.pub.BillEnumCollection
*/
public void setSrc_syscode ( java.lang.Integer src_syscode) {
this.src_syscode=src_syscode;
} 
 
/**
* 属性 sddreversaler的Getter方法.属性名：直接借记退回人
*  创建日期:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getSddreversaler() {
return this.sddreversaler;
} 

/**
* 属性sddreversaler的Setter方法.属性名：直接借记退回人
* 创建日期:2019-9-6
* @param newSddreversaler nc.vo.sm.UserVO
*/
public void setSddreversaler ( java.lang.String sddreversaler) {
this.sddreversaler=sddreversaler;
} 
 
/**
* 属性 sddreversaldate的Getter方法.属性名：直接借记退回日期
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSddreversaldate() {
return this.sddreversaldate;
} 

/**
* 属性sddreversaldate的Setter方法.属性名：直接借记退回日期
* 创建日期:2019-9-6
* @param newSddreversaldate nc.vo.pub.lang.UFDateTime
*/
public void setSddreversaldate ( UFDateTime sddreversaldate) {
this.sddreversaldate=sddreversaldate;
} 
 
/**
* 属性 def1的Getter方法.属性名：销售系统单号
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：销售系统单号
* 创建日期:2019-9-6
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：影像编码
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：影像编码
* 创建日期:2019-9-6
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：影像状态
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：影像状态
* 创建日期:2019-9-6
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：备注
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：备注
* 创建日期:2019-9-6
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-9-6
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-9-6
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-9-6
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-9-6
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-9-6
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-9-6
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-9-6
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-9-6
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-9-6
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-9-6
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-9-6
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-9-6
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-9-6
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-9-6
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-9-6
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-9-6
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 def21的Getter方法.属性名：自定义项21
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef21() {
return this.def21;
} 

/**
* 属性def21的Setter方法.属性名：自定义项21
* 创建日期:2019-9-6
* @param newDef21 java.lang.String
*/
public void setDef21 ( java.lang.String def21) {
this.def21=def21;
} 
 
/**
* 属性 def22的Getter方法.属性名：自定义项22
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef22() {
return this.def22;
} 

/**
* 属性def22的Setter方法.属性名：自定义项22
* 创建日期:2019-9-6
* @param newDef22 java.lang.String
*/
public void setDef22 ( java.lang.String def22) {
this.def22=def22;
} 
 
/**
* 属性 def23的Getter方法.属性名：自定义项23
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef23() {
return this.def23;
} 

/**
* 属性def23的Setter方法.属性名：自定义项23
* 创建日期:2019-9-6
* @param newDef23 java.lang.String
*/
public void setDef23 ( java.lang.String def23) {
this.def23=def23;
} 
 
/**
* 属性 def24的Getter方法.属性名：自定义项24
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef24() {
return this.def24;
} 

/**
* 属性def24的Setter方法.属性名：自定义项24
* 创建日期:2019-9-6
* @param newDef24 java.lang.String
*/
public void setDef24 ( java.lang.String def24) {
this.def24=def24;
} 
 
/**
* 属性 def25的Getter方法.属性名：自定义项25
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef25() {
return this.def25;
} 

/**
* 属性def25的Setter方法.属性名：自定义项25
* 创建日期:2019-9-6
* @param newDef25 java.lang.String
*/
public void setDef25 ( java.lang.String def25) {
this.def25=def25;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-9-6
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.TartingBillVO");
    }
   }
    