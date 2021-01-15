package nc.vo.tgfn.agoodsdetail;

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
 *  创建日期:2019-9-11
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class AGoodsDetail extends SuperVO {
	
/**
*到货明细表主键
*/
public java.lang.String pk_agoodsdetail_h;
/**
*集团
*/
public java.lang.String pk_group;
/**
*财务组织
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
*单据ID
*/
public java.lang.String billid;
/**
*单据号
*/
public java.lang.String billno;
/**
*单据日期
*/
public UFDate billdate;
/**
*所属组织
*/
public java.lang.String pkorg;
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
*接收编号
*/
public java.lang.String def1;
/**
*接收id
*/
public java.lang.String def2;
/**
*接收行id
*/
public java.lang.String def3;
/**
*订单编码
*/
public java.lang.String def4;
/**
*订单id
*/
public java.lang.String def5;
/**
*订单行id
*/
public java.lang.String def6;
/**
*业务类型
*/
public java.lang.String def7;
/**
*原接收编号
*/
public java.lang.String def8;
/**
*会计科目编码
*/
public java.lang.String def9;
/**
*会计科目名称
*/
public java.lang.String def10;
/**
*供应商编码
*/
public java.lang.String def11;
/**
*采购供应商
*/
public java.lang.String def12;
/**
*协议编号
*/
public java.lang.String def13;
/**
*合同名称
*/
public java.lang.String def14;
/**
*物料编码
*/
public java.lang.String def15;
/**
*物料说明
*/
public java.lang.String def16;
/**
*单位
*/
public java.lang.String def17;
/**
*本次到货数量
*/
public nc.vo.pub.lang.UFDouble def18;
/**
*单价含税
*/
public nc.vo.pub.lang.UFDouble def19;
/**
*单价不含税
*/
public nc.vo.pub.lang.UFDouble def20;
/**
*供应链金额含税
*/
public nc.vo.pub.lang.UFDouble def21;
/**
*供应链税率
*/
public nc.vo.pub.lang.UFDouble def22;
/**
*供应链金额不含税
*/
public nc.vo.pub.lang.UFDouble def23;
/**
*供应链金额税额
*/
public nc.vo.pub.lang.UFDouble def24;
/**
*加成率
*/
public nc.vo.pub.lang.UFDouble def25;
/**
*项目公司金额含税
*/
public nc.vo.pub.lang.UFDouble def26;
/**
*项目公司税率
*/
public nc.vo.pub.lang.UFDouble def27;
/**
*项目公司金额不含税
*/
public nc.vo.pub.lang.UFDouble def28;
/**
*项目公司金额税额
*/
public nc.vo.pub.lang.UFDouble def29;
/**
*到货确认日期
*/
public UFDateTime def30;
/**
*收货项目
*/
public java.lang.String def31;
/**
*收货项目编码
*/
public java.lang.String def32;
/**
*收货项目分期
*/
public java.lang.String def33;
/**
*收货项目分期编码
*/
public java.lang.String def34;
/**
*项目公司
*/
public java.lang.String def35;
/**
*项目公司编码
*/
public java.lang.String def36;
/**
*对账单号
*/
public java.lang.String def37;
/**
*对账单总金额
*/
public nc.vo.pub.lang.UFDouble def38;
/**
*供应商是否开票
*/
public UFBoolean def39;
/**
*供应链是否开票
*/
public UFBoolean def40;
/**
*后台接收ID
*/
public java.lang.String def41;
/**
*nc暂估工单编号
*/
public java.lang.String def42;
/**
*nc应收单据编号
*/
public java.lang.String def43;
/**
*供应链项目公司合同编号
*/
public java.lang.String def44;
/**
*供应链项目公司合同名称
*/
public java.lang.String def45;
/**
*施工合同编号
*/
public java.lang.String def46;
/**
*施工合同名称
*/
public java.lang.String def47;
/**
*入账月份
*/
public java.lang.String def48;
/**
*自定义项49
*/
public java.lang.String def49;
/**
*自定义项50
*/
public java.lang.String def50;
/**
*自定义项51
*/
public java.lang.String def51;
/**
*自定义项52
*/
public java.lang.String def52;
/**
*自定义项53
*/
public java.lang.String def53;
/**
*自定义项54
*/
public java.lang.String def54;
/**
*自定义项55
*/
public java.lang.String def55;
/**
*自定义项56
*/
public java.lang.String def56;
/**
*自定义项57
*/
public java.lang.String def57;
/**
*自定义项58
*/
public java.lang.String def58;
/**
*自定义项59
*/
public java.lang.String def59;
/**
*自定义项60
*/
public java.lang.String def60;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_agoodsdetail_h的Getter方法.属性名：到货明细表主键
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getPk_agoodsdetail_h() {
return this.pk_agoodsdetail_h;
} 

/**
* 属性pk_agoodsdetail_h的Setter方法.属性名：到货明细表主键
* 创建日期:2019-9-11
* @param newPk_agoodsdetail_h java.lang.String
*/
public void setPk_agoodsdetail_h ( java.lang.String pk_agoodsdetail_h) {
this.pk_agoodsdetail_h=pk_agoodsdetail_h;
} 
 
/**
* 属性 pk_group的Getter方法.属性名：集团
*  创建日期:2019-9-11
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* 属性pk_group的Setter方法.属性名：集团
* 创建日期:2019-9-11
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：财务组织
*  创建日期:2019-9-11
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：财务组织
* 创建日期:2019-9-11
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 pk_org_v的Getter方法.属性名：组织版本
*  创建日期:2019-9-11
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* 属性pk_org_v的Setter方法.属性名：组织版本
* 创建日期:2019-9-11
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* 属性 creator的Getter方法.属性名：创建人
*  创建日期:2019-9-11
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* 属性creator的Setter方法.属性名：创建人
* 创建日期:2019-9-11
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* 属性 creationtime的Getter方法.属性名：创建时间
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* 属性creationtime的Setter方法.属性名：创建时间
* 创建日期:2019-9-11
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* 属性 modifier的Getter方法.属性名：修改人
*  创建日期:2019-9-11
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* 属性modifier的Setter方法.属性名：修改人
* 创建日期:2019-9-11
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* 属性 modifiedtime的Getter方法.属性名：修改时间
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* 属性modifiedtime的Setter方法.属性名：修改时间
* 创建日期:2019-9-11
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* 属性 billid的Getter方法.属性名：单据ID
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* 属性billid的Setter方法.属性名：单据ID
* 创建日期:2019-9-11
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* 属性 billno的Getter方法.属性名：单据号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* 属性billno的Setter方法.属性名：单据号
* 创建日期:2019-9-11
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* 属性 billdate的Getter方法.属性名：单据日期
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* 属性billdate的Setter方法.属性名：单据日期
* 创建日期:2019-9-11
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* 属性 pkorg的Getter方法.属性名：所属组织
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* 属性pkorg的Setter方法.属性名：所属组织
* 创建日期:2019-9-11
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* 属性 approver的Getter方法.属性名：审批人
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* 属性approver的Setter方法.属性名：审批人
* 创建日期:2019-9-11
* @param newApprover java.lang.String
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* 属性 approvestatus的Getter方法.属性名：审批状态
*  创建日期:2019-9-11
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* 属性approvestatus的Setter方法.属性名：审批状态
* 创建日期:2019-9-11
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* 属性 approvenote的Getter方法.属性名：审批批语
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* 属性approvenote的Setter方法.属性名：审批批语
* 创建日期:2019-9-11
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* 属性 approvedate的Getter方法.属性名：审批时间
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* 属性approvedate的Setter方法.属性名：审批时间
* 创建日期:2019-9-11
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* 属性 transtype的Getter方法.属性名：交易类型
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* 属性transtype的Setter方法.属性名：交易类型
* 创建日期:2019-9-11
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* 属性 billtype的Getter方法.属性名：单据类型
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* 属性billtype的Setter方法.属性名：单据类型
* 创建日期:2019-9-11
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* 属性 transtypepk的Getter方法.属性名：交易类型pk
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* 属性transtypepk的Setter方法.属性名：交易类型pk
* 创建日期:2019-9-11
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* 属性 srcbilltype的Getter方法.属性名：来源单据类型
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* 属性srcbilltype的Setter方法.属性名：来源单据类型
* 创建日期:2019-9-11
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* 属性 srcbillid的Getter方法.属性名：来源单据id
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* 属性srcbillid的Setter方法.属性名：来源单据id
* 创建日期:2019-9-11
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* 属性 emendenum的Getter方法.属性名：修订枚举
*  创建日期:2019-9-11
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* 属性emendenum的Setter方法.属性名：修订枚举
* 创建日期:2019-9-11
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* 属性 billversionpk的Getter方法.属性名：单据版本pk
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* 属性billversionpk的Setter方法.属性名：单据版本pk
* 创建日期:2019-9-11
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* 属性 def1的Getter方法.属性名：接收编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：接收编号
* 创建日期:2019-9-11
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：接收id
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：接收id
* 创建日期:2019-9-11
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：接收行id
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：接收行id
* 创建日期:2019-9-11
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：订单编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：订单编码
* 创建日期:2019-9-11
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：订单id
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：订单id
* 创建日期:2019-9-11
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：订单行id
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：订单行id
* 创建日期:2019-9-11
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：业务类型
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：业务类型
* 创建日期:2019-9-11
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：原接收编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：原接收编号
* 创建日期:2019-9-11
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：会计科目编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：会计科目编码
* 创建日期:2019-9-11
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：会计科目名称
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：会计科目名称
* 创建日期:2019-9-11
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：供应商编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：供应商编码
* 创建日期:2019-9-11
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：采购供应商
*  创建日期:2019-9-11
* @return nc.vo.bd.supplier.SupplierVO
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：采购供应商
* 创建日期:2019-9-11
* @param newDef12 nc.vo.bd.supplier.SupplierVO
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：协议编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：协议编号
* 创建日期:2019-9-11
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：合同名称
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：合同名称
* 创建日期:2019-9-11
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：物料编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：物料编码
* 创建日期:2019-9-11
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：物料说明
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：物料说明
* 创建日期:2019-9-11
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：单位
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：单位
* 创建日期:2019-9-11
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：本次到货数量
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：本次到货数量
* 创建日期:2019-9-11
* @param newDef18 nc.vo.pub.lang.UFDouble
*/
public void setDef18 ( nc.vo.pub.lang.UFDouble def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：单价含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：单价含税
* 创建日期:2019-9-11
* @param newDef19 nc.vo.pub.lang.UFDouble
*/
public void setDef19 ( nc.vo.pub.lang.UFDouble def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：单价不含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：单价不含税
* 创建日期:2019-9-11
* @param newDef20 nc.vo.pub.lang.UFDouble
*/
public void setDef20 ( nc.vo.pub.lang.UFDouble def20) {
this.def20=def20;
} 
 
/**
* 属性 def21的Getter方法.属性名：供应链金额含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef21() {
return this.def21;
} 

/**
* 属性def21的Setter方法.属性名：供应链金额含税
* 创建日期:2019-9-11
* @param newDef21 nc.vo.pub.lang.UFDouble
*/
public void setDef21 ( nc.vo.pub.lang.UFDouble def21) {
this.def21=def21;
} 
 
/**
* 属性 def22的Getter方法.属性名：供应链税率
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef22() {
return this.def22;
} 

/**
* 属性def22的Setter方法.属性名：供应链税率
* 创建日期:2019-9-11
* @param newDef22 nc.vo.pub.lang.UFDouble
*/
public void setDef22 ( nc.vo.pub.lang.UFDouble def22) {
this.def22=def22;
} 
 
/**
* 属性 def23的Getter方法.属性名：供应链金额不含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef23() {
return this.def23;
} 

/**
* 属性def23的Setter方法.属性名：供应链金额不含税
* 创建日期:2019-9-11
* @param newDef23 nc.vo.pub.lang.UFDouble
*/
public void setDef23 ( nc.vo.pub.lang.UFDouble def23) {
this.def23=def23;
} 
 
/**
* 属性 def24的Getter方法.属性名：供应链金额税额
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef24() {
return this.def24;
} 

/**
* 属性def24的Setter方法.属性名：供应链金额税额
* 创建日期:2019-9-11
* @param newDef24 nc.vo.pub.lang.UFDouble
*/
public void setDef24 ( nc.vo.pub.lang.UFDouble def24) {
this.def24=def24;
} 
 
/**
* 属性 def25的Getter方法.属性名：加成率
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef25() {
return this.def25;
} 

/**
* 属性def25的Setter方法.属性名：加成率
* 创建日期:2019-9-11
* @param newDef25 nc.vo.pub.lang.UFDouble
*/
public void setDef25 ( nc.vo.pub.lang.UFDouble def25) {
this.def25=def25;
} 
 
/**
* 属性 def26的Getter方法.属性名：项目公司金额含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef26() {
return this.def26;
} 

/**
* 属性def26的Setter方法.属性名：项目公司金额含税
* 创建日期:2019-9-11
* @param newDef26 nc.vo.pub.lang.UFDouble
*/
public void setDef26 ( nc.vo.pub.lang.UFDouble def26) {
this.def26=def26;
} 
 
/**
* 属性 def27的Getter方法.属性名：项目公司税率
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef27() {
return this.def27;
} 

/**
* 属性def27的Setter方法.属性名：项目公司税率
* 创建日期:2019-9-11
* @param newDef27 nc.vo.pub.lang.UFDouble
*/
public void setDef27 ( nc.vo.pub.lang.UFDouble def27) {
this.def27=def27;
} 
 
/**
* 属性 def28的Getter方法.属性名：项目公司金额不含税
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef28() {
return this.def28;
} 

/**
* 属性def28的Setter方法.属性名：项目公司金额不含税
* 创建日期:2019-9-11
* @param newDef28 nc.vo.pub.lang.UFDouble
*/
public void setDef28 ( nc.vo.pub.lang.UFDouble def28) {
this.def28=def28;
} 
 
/**
* 属性 def29的Getter方法.属性名：项目公司金额税额
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef29() {
return this.def29;
} 

/**
* 属性def29的Setter方法.属性名：项目公司金额税额
* 创建日期:2019-9-11
* @param newDef29 nc.vo.pub.lang.UFDouble
*/
public void setDef29 ( nc.vo.pub.lang.UFDouble def29) {
this.def29=def29;
} 
 
/**
* 属性 def30的Getter方法.属性名：到货确认日期
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getDef30() {
return this.def30;
} 

/**
* 属性def30的Setter方法.属性名：到货确认日期
* 创建日期:2019-9-11
* @param newDef30 nc.vo.pub.lang.UFDateTime
*/
public void setDef30 ( UFDateTime def30) {
this.def30=def30;
} 
 
/**
* 属性 def31的Getter方法.属性名：收货项目
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef31() {
return this.def31;
} 

/**
* 属性def31的Setter方法.属性名：收货项目
* 创建日期:2019-9-11
* @param newDef31 java.lang.String
*/
public void setDef31 ( java.lang.String def31) {
this.def31=def31;
} 
 
/**
* 属性 def32的Getter方法.属性名：收货项目编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef32() {
return this.def32;
} 

/**
* 属性def32的Setter方法.属性名：收货项目编码
* 创建日期:2019-9-11
* @param newDef32 java.lang.String
*/
public void setDef32 ( java.lang.String def32) {
this.def32=def32;
} 
 
/**
* 属性 def33的Getter方法.属性名：收货项目分期
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef33() {
return this.def33;
} 

/**
* 属性def33的Setter方法.属性名：收货项目分期
* 创建日期:2019-9-11
* @param newDef33 java.lang.String
*/
public void setDef33 ( java.lang.String def33) {
this.def33=def33;
} 
 
/**
* 属性 def34的Getter方法.属性名：收货项目分期编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef34() {
return this.def34;
} 

/**
* 属性def34的Setter方法.属性名：收货项目分期编码
* 创建日期:2019-9-11
* @param newDef34 java.lang.String
*/
public void setDef34 ( java.lang.String def34) {
this.def34=def34;
} 
 
/**
* 属性 def35的Getter方法.属性名：项目公司
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef35() {
return this.def35;
} 

/**
* 属性def35的Setter方法.属性名：项目公司
* 创建日期:2019-9-11
* @param newDef35 java.lang.String
*/
public void setDef35 ( java.lang.String def35) {
this.def35=def35;
} 
 
/**
* 属性 def36的Getter方法.属性名：项目公司编码
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef36() {
return this.def36;
} 

/**
* 属性def36的Setter方法.属性名：项目公司编码
* 创建日期:2019-9-11
* @param newDef36 java.lang.String
*/
public void setDef36 ( java.lang.String def36) {
this.def36=def36;
} 
 
/**
* 属性 def37的Getter方法.属性名：对账单号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef37() {
return this.def37;
} 

/**
* 属性def37的Setter方法.属性名：对账单号
* 创建日期:2019-9-11
* @param newDef37 java.lang.String
*/
public void setDef37 ( java.lang.String def37) {
this.def37=def37;
} 
 
/**
* 属性 def38的Getter方法.属性名：对账单总金额
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDef38() {
return this.def38;
} 

/**
* 属性def38的Setter方法.属性名：对账单总金额
* 创建日期:2019-9-11
* @param newDef38 nc.vo.pub.lang.UFDouble
*/
public void setDef38 ( nc.vo.pub.lang.UFDouble def38) {
this.def38=def38;
} 
 
/**
* 属性 def39的Getter方法.属性名：供应商是否开票
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFBoolean
*/
public UFBoolean getDef39() {
return this.def39;
} 

/**
* 属性def39的Setter方法.属性名：供应商是否开票
* 创建日期:2019-9-11
* @param newDef39 nc.vo.pub.lang.UFBoolean
*/
public void setDef39 ( UFBoolean def39) {
this.def39=def39;
} 
 
/**
* 属性 def40的Getter方法.属性名：供应链是否开票
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFBoolean
*/
public UFBoolean getDef40() {
return this.def40;
} 

/**
* 属性def40的Setter方法.属性名：供应链是否开票
* 创建日期:2019-9-11
* @param newDef40 nc.vo.pub.lang.UFBoolean
*/
public void setDef40 ( UFBoolean def40) {
this.def40=def40;
} 
 
/**
* 属性 def41的Getter方法.属性名：后台接收ID
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef41() {
return this.def41;
} 

/**
* 属性def41的Setter方法.属性名：后台接收ID
* 创建日期:2019-9-11
* @param newDef41 java.lang.String
*/
public void setDef41 ( java.lang.String def41) {
this.def41=def41;
} 
 
/**
* 属性 def42的Getter方法.属性名：nc暂估工单编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef42() {
return this.def42;
} 

/**
* 属性def42的Setter方法.属性名：nc暂估工单编号
* 创建日期:2019-9-11
* @param newDef42 java.lang.String
*/
public void setDef42 ( java.lang.String def42) {
this.def42=def42;
} 
 
/**
* 属性 def43的Getter方法.属性名：nc应收单据编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef43() {
return this.def43;
} 

/**
* 属性def43的Setter方法.属性名：nc应收单据编号
* 创建日期:2019-9-11
* @param newDef43 java.lang.String
*/
public void setDef43 ( java.lang.String def43) {
this.def43=def43;
} 
 
/**
* 属性 def44的Getter方法.属性名：供应链项目公司合同编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef44() {
return this.def44;
} 

/**
* 属性def44的Setter方法.属性名：供应链项目公司合同编号
* 创建日期:2019-9-11
* @param newDef44 java.lang.String
*/
public void setDef44 ( java.lang.String def44) {
this.def44=def44;
} 
 
/**
* 属性 def45的Getter方法.属性名：供应链项目公司合同名称
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef45() {
return this.def45;
} 

/**
* 属性def45的Setter方法.属性名：供应链项目公司合同名称
* 创建日期:2019-9-11
* @param newDef45 java.lang.String
*/
public void setDef45 ( java.lang.String def45) {
this.def45=def45;
} 
 
/**
* 属性 def46的Getter方法.属性名：施工合同编号
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef46() {
return this.def46;
} 

/**
* 属性def46的Setter方法.属性名：施工合同编号
* 创建日期:2019-9-11
* @param newDef46 java.lang.String
*/
public void setDef46 ( java.lang.String def46) {
this.def46=def46;
} 
 
/**
* 属性 def47的Getter方法.属性名：施工合同名称
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef47() {
return this.def47;
} 

/**
* 属性def47的Setter方法.属性名：施工合同名称
* 创建日期:2019-9-11
* @param newDef47 java.lang.String
*/
public void setDef47 ( java.lang.String def47) {
this.def47=def47;
} 
 
/**
* 属性 def48的Getter方法.属性名：入账月份
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef48() {
return this.def48;
} 

/**
* 属性def48的Setter方法.属性名：入账月份
* 创建日期:2019-9-11
* @param newDef48 java.lang.String
*/
public void setDef48 ( java.lang.String def48) {
this.def48=def48;
} 
 
/**
* 属性 def49的Getter方法.属性名：自定义项49
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef49() {
return this.def49;
} 

/**
* 属性def49的Setter方法.属性名：自定义项49
* 创建日期:2019-9-11
* @param newDef49 java.lang.String
*/
public void setDef49 ( java.lang.String def49) {
this.def49=def49;
} 
 
/**
* 属性 def50的Getter方法.属性名：自定义项50
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef50() {
return this.def50;
} 

/**
* 属性def50的Setter方法.属性名：自定义项50
* 创建日期:2019-9-11
* @param newDef50 java.lang.String
*/
public void setDef50 ( java.lang.String def50) {
this.def50=def50;
} 
 
/**
* 属性 def51的Getter方法.属性名：自定义项51
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef51() {
return this.def51;
} 

/**
* 属性def51的Setter方法.属性名：自定义项51
* 创建日期:2019-9-11
* @param newDef51 java.lang.String
*/
public void setDef51 ( java.lang.String def51) {
this.def51=def51;
} 
 
/**
* 属性 def52的Getter方法.属性名：自定义项52
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef52() {
return this.def52;
} 

/**
* 属性def52的Setter方法.属性名：自定义项52
* 创建日期:2019-9-11
* @param newDef52 java.lang.String
*/
public void setDef52 ( java.lang.String def52) {
this.def52=def52;
} 
 
/**
* 属性 def53的Getter方法.属性名：自定义项53
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef53() {
return this.def53;
} 

/**
* 属性def53的Setter方法.属性名：自定义项53
* 创建日期:2019-9-11
* @param newDef53 java.lang.String
*/
public void setDef53 ( java.lang.String def53) {
this.def53=def53;
} 
 
/**
* 属性 def54的Getter方法.属性名：自定义项54
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef54() {
return this.def54;
} 

/**
* 属性def54的Setter方法.属性名：自定义项54
* 创建日期:2019-9-11
* @param newDef54 java.lang.String
*/
public void setDef54 ( java.lang.String def54) {
this.def54=def54;
} 
 
/**
* 属性 def55的Getter方法.属性名：自定义项55
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef55() {
return this.def55;
} 

/**
* 属性def55的Setter方法.属性名：自定义项55
* 创建日期:2019-9-11
* @param newDef55 java.lang.String
*/
public void setDef55 ( java.lang.String def55) {
this.def55=def55;
} 
 
/**
* 属性 def56的Getter方法.属性名：自定义项56
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef56() {
return this.def56;
} 

/**
* 属性def56的Setter方法.属性名：自定义项56
* 创建日期:2019-9-11
* @param newDef56 java.lang.String
*/
public void setDef56 ( java.lang.String def56) {
this.def56=def56;
} 
 
/**
* 属性 def57的Getter方法.属性名：自定义项57
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef57() {
return this.def57;
} 

/**
* 属性def57的Setter方法.属性名：自定义项57
* 创建日期:2019-9-11
* @param newDef57 java.lang.String
*/
public void setDef57 ( java.lang.String def57) {
this.def57=def57;
} 
 
/**
* 属性 def58的Getter方法.属性名：自定义项58
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef58() {
return this.def58;
} 

/**
* 属性def58的Setter方法.属性名：自定义项58
* 创建日期:2019-9-11
* @param newDef58 java.lang.String
*/
public void setDef58 ( java.lang.String def58) {
this.def58=def58;
} 
 
/**
* 属性 def59的Getter方法.属性名：自定义项59
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef59() {
return this.def59;
} 

/**
* 属性def59的Setter方法.属性名：自定义项59
* 创建日期:2019-9-11
* @param newDef59 java.lang.String
*/
public void setDef59 ( java.lang.String def59) {
this.def59=def59;
} 
 
/**
* 属性 def60的Getter方法.属性名：自定义项60
*  创建日期:2019-9-11
* @return java.lang.String
*/
public java.lang.String getDef60() {
return this.def60;
} 

/**
* 属性def60的Setter方法.属性名：自定义项60
* 创建日期:2019-9-11
* @param newDef60 java.lang.String
*/
public void setDef60 ( java.lang.String def60) {
this.def60=def60;
} 
 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-9-11
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-9-11
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.AGoodsDetail");
    }
   }
    