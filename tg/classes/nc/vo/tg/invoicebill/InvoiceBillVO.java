package nc.vo.tg.invoicebill;

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
 *  创建日期:2019-8-19
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class InvoiceBillVO extends SuperVO {
	public static final String PK_INVOICEBILL_H = "pk_invoicebill_h";
	public static final String PK_GROUP = "pk_group";
	public static final String PK_ORG = "pk_org";
	public static final String PK_ORG_V = "pk_org_v";
	public static final String BILLNO = "billno";
	public static final String BILLDATE = "billdate";
	public static final String PK_DEPTID_V = "pk_deptid_v";
	public static final String PK_PSNDOC = "pk_psndoc";
	public static final String OBJTYPE = "objtype";
	public static final String SUPPLIER = "supplier";
	public static final String CHECKTYPE = "checktype";
	public static final String BILLSTATUS = "billstatus";
	public static final String APPROVESTATUS = "approvestatus";
	public static final String EFFECTSTATUS = "effectstatus";
	public static final String PK_BUSITYPE = "pk_busitype";
	public static final String CREATOR = "creator";
	public static final String CREATIONTIME = "creationtime";
	public static final String MODIFIER = "modifier";
	public static final String MODIFIEDTIME = "modifiedtime";
	public static final String BUSITYPE = "busitype";
	public static final String APPROVER = "approver";
	public static final String APPROVENOTE = "approvenote";
	public static final String APPROVEDATE = "approvedate";
	public static final String TRANSTYPE = "transtype";
	public static final String BILLTYPE = "billtype";
	public static final String TRANSTYPEPK = "transtypepk";
	public static final String SRCBILLTYPE = "srcbilltype";
	public static final String SRCBILLID = "srcbillid";
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
	public static final String DEF31 = "def31";
	public static final String DEF32 = "def32";
	public static final String DEF33 = "def33";
	public static final String DEF34 = "def34";
	public static final String DEF35 = "def35";
	public static final String DEF36 = "def36";
	public static final String DEF37 = "def37";
	public static final String DEF38 = "def38";
	public static final String DEF39 = "def39";
	public static final String DEF40 = "def40";
	public static final String DEF41 = "def41";
	public static final String DEF42 = "def42";
	public static final String DEF43 = "def43";
	public static final String DEF44 = "def44";
	public static final String DEF45 = "def45";
	public static final String DEF46 = "def46";
	public static final String DEF47 = "def47";
	public static final String DEF48 = "def48";
	public static final String DEF49 = "def49";
	public static final String DEF50 = "def50";
	public static final String TS = "ts";
	public static final String DR = "dr";

	
	
/**
*进项发票工单主表主键
*/
public java.lang.String pk_invoicebill_h;
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
*单据时间
*/
public UFDate billdate;
/**
*经办部门
*/
public java.lang.String pk_deptid_v;
/**
*经办人
*/
public java.lang.String pk_psndoc;
/**
*往来对象
*/
public java.lang.String objtype;
/**
*供应商
*/
public java.lang.String supplier;
/**
*票据类型
*/
public java.lang.String checktype;
/**
*单据状态
*/
public java.lang.Integer billstatus;
/**
*审批状态
*/
public java.lang.Integer approvestatus;
/**
*生效状态
*/
public java.lang.Integer effectstatus;
/**
*业务流程
*/
public java.lang.String pk_busitype;
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
*业务类型
*/
public java.lang.String busitype;
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
*供应合同编号
*/
public java.lang.String def1;
/**
*发票含税金额合计
*/
public java.lang.String def2;
/**
*房地产项目
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
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_invoicebill_h的Getter方法.属性名：进项发票工单主表主键
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_invoicebill_h() {
return this.pk_invoicebill_h;
} 

/**
* 属性pk_invoicebill_h的Setter方法.属性名：进项发票工单主表主键
* 创建日期:2019-8-19
* @param newPk_invoicebill_h java.lang.String
*/
public void setPk_invoicebill_h ( java.lang.String pk_invoicebill_h) {
this.pk_invoicebill_h=pk_invoicebill_h;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-8-19
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-8-19
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-8-19
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-8-19
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-8-19
* @return nc.vo.vorg.FinanceOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-8-19
* @param newPk_org_v nc.vo.vorg.FinanceOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-8-19
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据时间
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据时间
* 创建日期:2019-8-19
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 pk_deptid_v的Getter方法.属性名：经办部门
*  创建日期:2019-8-19
* @return nc.vo.org.DeptVO
*/
public java.lang.String getPk_deptid_v() {
return this.pk_deptid_v;
} 

/**
* 属性pk_deptid_v的Setter方法.属性名：经办部门
* 创建日期:2019-8-19
* @param newPk_deptid_v nc.vo.org.DeptVO
*/
public void setPk_deptid_v ( java.lang.String pk_deptid_v) {
this.pk_deptid_v=pk_deptid_v;
} 
 
/**
* 属性 pk_psndoc的Getter方法.属性名：经办人
*  创建日期:2019-8-19
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getPk_psndoc() {
return this.pk_psndoc;
} 

/**
* 属性pk_psndoc的Setter方法.属性名：经办人
* 创建日期:2019-8-19
* @param newPk_psndoc nc.vo.bd.psn.PsndocVO
*/
public void setPk_psndoc ( java.lang.String pk_psndoc) {
this.pk_psndoc=pk_psndoc;
} 
 
/**
* 属性 objtype的Getter方法.属性名：往来对象
*  创建日期:2019-8-19
* @return nc.voarap.arapgl.ASSO_OBJ
*/
public java.lang.String getObjtype() {
return this.objtype;
} 

/**
* 属性objtype的Setter方法.属性名：往来对象
* 创建日期:2019-8-19
* @param newObjtype nc.voarap.arapgl.ASSO_OBJ
*/
public void setObjtype ( java.lang.String objtype) {
this.objtype=objtype;
} 
 
/**
* 属性 supplier的Getter方法.属性名：供应商
*  创建日期:2019-8-19
* @return nc.vo.bd.supplier.SupplierVO
*/
public java.lang.String getSupplier() {
return this.supplier;
} 

/**
* 属性supplier的Setter方法.属性名：供应商
* 创建日期:2019-8-19
* @param newSupplier nc.vo.bd.supplier.SupplierVO
*/
public void setSupplier ( java.lang.String supplier) {
this.supplier=supplier;
} 
 
/**
* 属性 checktype的Getter方法.属性名：票据类型
*  创建日期:2019-8-19
* @return nc.vo.bd.notetype.NotetypeVO
*/
public java.lang.String getChecktype() {
return this.checktype;
} 

/**
* 属性checktype的Setter方法.属性名：票据类型
* 创建日期:2019-8-19
* @param newChecktype nc.vo.bd.notetype.NotetypeVO
*/
public void setChecktype ( java.lang.String checktype) {
this.checktype=checktype;
} 
 
/**
* 属性 billstatus的Getter方法.属性名：单据状态
*  创建日期:2019-8-19
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getBillstatus() {
return this.billstatus;
} 

/**
* 属性billstatus的Setter方法.属性名：单据状态
* 创建日期:2019-8-19
* @param newBillstatus nc.vo.pub.pf.BillStatusEnum
*/
public void setBillstatus ( java.lang.Integer billstatus) {
this.billstatus=billstatus;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-8-19
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-8-19
* @param newApprovestatus nc.vo.arap.pub.BillEnumCollection
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 effectstatus的Getter方法.属性名：生效状态
*  创建日期:2019-8-19
* @return nc.vo.cmp.bill.Effectflag
*/
public java.lang.Integer getEffectstatus() {
return this.effectstatus;
} 

/**
* 属性effectstatus的Setter方法.属性名：生效状态
* 创建日期:2019-8-19
* @param newEffectstatus nc.vo.cmp.bill.Effectflag
*/
public void setEffectstatus ( java.lang.Integer effectstatus) {
this.effectstatus=effectstatus;
} 
 
/**
* 属性 pk_busitype的Getter方法.属性名：业务流程
*  创建日期:2019-8-19
* @return nc.vo.pf.pub.BusitypeVO
*/
public java.lang.String getPk_busitype() {
return this.pk_busitype;
} 

/**
* 属性pk_busitype的Setter方法.属性名：业务流程
* 创建日期:2019-8-19
* @param newPk_busitype nc.vo.pf.pub.BusitypeVO
*/
public void setPk_busitype ( java.lang.String pk_busitype) {
this.pk_busitype=pk_busitype;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-8-19
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-8-19
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：最后修改人
*  创建日期:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：最后修改人
* 创建日期:2019-8-19
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：最后修改时间
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：最后修改时间
* 创建日期:2019-8-19
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 busitype的Getter方法.属性名：业务类型
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* 属性busitype的Setter方法.属性名：业务类型
* 创建日期:2019-8-19
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-8-19
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-8-19
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-8-19
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-8-19
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-8-19
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-8-19
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-8-19
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-8-19
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 def1的Getter方法.属性名：供应合同编号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：供应合同编号
* 创建日期:2019-8-19
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：发票含税金额合计
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：发票含税金额合计
* 创建日期:2019-8-19
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：房地产项目
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：房地产项目
* 创建日期:2019-8-19
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-8-19
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：自定义项5
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：自定义项5
* 创建日期:2019-8-19
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-8-19
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-8-19
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-8-19
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-8-19
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-8-19
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-8-19
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-8-19
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-8-19
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-8-19
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-8-19
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-8-19
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.InvoiceBillVO");
    }
   }
    