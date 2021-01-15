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
 
public class TartingBillBVO extends SuperVO {
	
/**
*挞定工单子表主键
*/
public java.lang.String pk_tartingbill_b;
/**
*摘要
*/
public java.lang.String scomment;
/**
*房地产项目
*/
public java.lang.String def1;
/**
*房地产项目明细
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
*业态
*/
public java.lang.String def5;
/**
*计税方式
*/
public java.lang.String def6;
/**
*税率
*/
public java.lang.String def7;
/**
*税额
*/
public java.lang.String def8;
/**
*不含税金额
*/
public java.lang.String def9;
/**
*挞定金额
*/
public java.lang.String def10;
/**
*原因类型
*/
public java.lang.String def11;
/**
*申请说明
*/
public java.lang.String def12;
/**
*款项类型
*/
public java.lang.String def13;
/**
*款项名称
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
*上层单据主键
*/
public String pk_tartingbill_h;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_tartingbill_b的Getter方法.属性名：挞定工单子表主键
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getPk_tartingbill_b() {
return this.pk_tartingbill_b;
} 

/**
* 属性pk_tartingbill_b的Setter方法.属性名：挞定工单子表主键
* 创建日期:2019-9-6
* @param newPk_tartingbill_b java.lang.String
*/
public void setPk_tartingbill_b ( java.lang.String pk_tartingbill_b) {
this.pk_tartingbill_b=pk_tartingbill_b;
} 
 
/**
* 属性 scomment的Getter方法.属性名：摘要
*  创建日期:2019-9-6
* @return nc.vo.fipub.summary.SummaryVO
*/
public java.lang.String getScomment() {
return this.scomment;
} 

/**
* 属性scomment的Setter方法.属性名：摘要
* 创建日期:2019-9-6
* @param newScomment nc.vo.fipub.summary.SummaryVO
*/
public void setScomment ( java.lang.String scomment) {
this.scomment=scomment;
} 
 
/**
* 属性 def1的Getter方法.属性名：房地产项目
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：房地产项目
* 创建日期:2019-9-6
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：房地产项目明细
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：房地产项目明细
* 创建日期:2019-9-6
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：自定义项3
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：自定义项3
* 创建日期:2019-9-6
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：自定义项4
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：自定义项4
* 创建日期:2019-9-6
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：业态
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：业态
* 创建日期:2019-9-6
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：计税方式
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：计税方式
* 创建日期:2019-9-6
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：税率
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：税率
* 创建日期:2019-9-6
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：税额
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：税额
* 创建日期:2019-9-6
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：不含税金额
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：不含税金额
* 创建日期:2019-9-6
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：挞定金额
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：挞定金额
* 创建日期:2019-9-6
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：原因类型
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：原因类型
* 创建日期:2019-9-6
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：申请说明
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：申请说明
* 创建日期:2019-9-6
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：款项类型
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：款项类型
* 创建日期:2019-9-6
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：款项名称
*  创建日期:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：款项名称
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
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2019-9-6
* @return String
*/
public String getPk_tartingbill_h(){
return this.pk_tartingbill_h;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2019-9-6
* @param newPk_tartingbill_h String
*/
public void setPk_tartingbill_h(String pk_tartingbill_h){
this.pk_tartingbill_h=pk_tartingbill_h;
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
    return VOMetaFactory.getInstance().getVOMeta("tg.TartingBillBVO");
    }
   }
    