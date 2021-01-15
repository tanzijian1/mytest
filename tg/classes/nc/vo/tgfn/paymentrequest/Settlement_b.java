package nc.vo.tgfn.paymentrequest;

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
 
public class Settlement_b extends SuperVO {
	
/**
*结算页签主键
*/
public java.lang.String pk_settlement_b;
/**
*摘要
*/
public java.lang.String scomment;
/**
*付款业务类型
*/
public java.lang.String pk_businesstype;
/**
*本次请款金额
*/
public nc.vo.pub.lang.UFDouble mny_req;
/**
*本次付款金额
*/
public nc.vo.pub.lang.UFDouble mny_pay;
/**
*结算方式
*/
public java.lang.String settlement;
/**
*付款银行账号
*/
public java.lang.String pk_payaccount;
/**
*票据号
*/
public java.lang.String pk_billno;
/**
*供应商
*/
public java.lang.String pk_supplier;
/**
*收款银行账号
*/
public java.lang.String pk_recaccount;
/**
*行号
*/
public java.lang.String rowno;
/**
*上层单据主键
*/
public String pk_payreq;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_settlement_b的Getter方法.属性名：结算页签主键
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_settlement_b() {
return this.pk_settlement_b;
} 

/**
* 属性pk_settlement_b的Setter方法.属性名：结算页签主键
* 创建日期:2019-8-19
* @param newPk_settlement_b java.lang.String
*/
public void setPk_settlement_b ( java.lang.String pk_settlement_b) {
this.pk_settlement_b=pk_settlement_b;
} 
 
/**
* 属性 scomment的Getter方法.属性名：摘要
*  创建日期:2019-8-19
* @return nc.vo.cdm.bankcontiabill.enumconst
*/
public java.lang.String getScomment() {
return this.scomment;
} 

/**
* 属性scomment的Setter方法.属性名：摘要
* 创建日期:2019-8-19
* @param newScomment nc.vo.cdm.bankcontiabill.enumconst
*/
public void setScomment ( java.lang.String scomment) {
this.scomment=scomment;
} 
 
/**
* 属性 pk_businesstype的Getter方法.属性名：付款业务类型
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_businesstype() {
return this.pk_businesstype;
} 

/**
* 属性pk_businesstype的Setter方法.属性名：付款业务类型
* 创建日期:2019-8-19
* @param newPk_businesstype java.lang.String
*/
public void setPk_businesstype ( java.lang.String pk_businesstype) {
this.pk_businesstype=pk_businesstype;
} 
 
/**
* 属性 mny_req的Getter方法.属性名：本次请款金额
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMny_req() {
return this.mny_req;
} 

/**
* 属性mny_req的Setter方法.属性名：本次请款金额
* 创建日期:2019-8-19
* @param newMny_req nc.vo.pub.lang.UFDouble
*/
public void setMny_req ( nc.vo.pub.lang.UFDouble mny_req) {
this.mny_req=mny_req;
} 
 
/**
* 属性 mny_pay的Getter方法.属性名：本次付款金额
*  创建日期:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMny_pay() {
return this.mny_pay;
} 

/**
* 属性mny_pay的Setter方法.属性名：本次付款金额
* 创建日期:2019-8-19
* @param newMny_pay nc.vo.pub.lang.UFDouble
*/
public void setMny_pay ( nc.vo.pub.lang.UFDouble mny_pay) {
this.mny_pay=mny_pay;
} 
 
/**
* 属性 settlement的Getter方法.属性名：结算方式
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSettlement() {
return this.settlement;
} 

/**
* 属性settlement的Setter方法.属性名：结算方式
* 创建日期:2019-8-19
* @param newSettlement java.lang.String
*/
public void setSettlement ( java.lang.String settlement) {
this.settlement=settlement;
} 
 
/**
* 属性 pk_payaccount的Getter方法.属性名：付款银行账号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_payaccount() {
return this.pk_payaccount;
} 

/**
* 属性pk_payaccount的Setter方法.属性名：付款银行账号
* 创建日期:2019-8-19
* @param newPk_payaccount java.lang.String
*/
public void setPk_payaccount ( java.lang.String pk_payaccount) {
this.pk_payaccount=pk_payaccount;
} 
 
/**
* 属性 pk_billno的Getter方法.属性名：票据号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_billno() {
return this.pk_billno;
} 

/**
* 属性pk_billno的Setter方法.属性名：票据号
* 创建日期:2019-8-19
* @param newPk_billno java.lang.String
*/
public void setPk_billno ( java.lang.String pk_billno) {
this.pk_billno=pk_billno;
} 
 
/**
* 属性 pk_supplier的Getter方法.属性名：供应商
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_supplier() {
return this.pk_supplier;
} 

/**
* 属性pk_supplier的Setter方法.属性名：供应商
* 创建日期:2019-8-19
* @param newPk_supplier java.lang.String
*/
public void setPk_supplier ( java.lang.String pk_supplier) {
this.pk_supplier=pk_supplier;
} 
 
/**
* 属性 pk_recaccount的Getter方法.属性名：收款银行账号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_recaccount() {
return this.pk_recaccount;
} 

/**
* 属性pk_recaccount的Setter方法.属性名：收款银行账号
* 创建日期:2019-8-19
* @param newPk_recaccount java.lang.String
*/
public void setPk_recaccount ( java.lang.String pk_recaccount) {
this.pk_recaccount=pk_recaccount;
} 
 
/**
* 属性 rowno的Getter方法.属性名：行号
*  创建日期:2019-8-19
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* 属性rowno的Setter方法.属性名：行号
* 创建日期:2019-8-19
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2019-8-19
* @return String
*/
public String getPk_payreq(){
return this.pk_payreq;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2019-8-19
* @param newPk_payreq String
*/
public void setPk_payreq(String pk_payreq){
this.pk_payreq=pk_payreq;
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
    return VOMetaFactory.getInstance().getVOMeta("tg.settlement_b");
    }
   }
    