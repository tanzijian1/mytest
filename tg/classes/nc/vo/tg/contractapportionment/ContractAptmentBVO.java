package nc.vo.tg.contractapportionment;

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
 *  创建日期:2019-9-18
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class ContractAptmentBVO extends SuperVO {
	
/**
*合同分摊子表主键
*/
public java.lang.String pk_contractaptment_b;
/**
*组织
*/
public java.lang.String pk_org;
/**
*摘要
*/
public java.lang.String scomment;
/**
*成本合同编号
*/
public java.lang.String conno;
/**
*分摊金额
*/
public nc.vo.pub.lang.UFDouble aptionedoney;
/**
*分摊合计金额
*/
public java.lang.String def1;
/**
*自定义项2
*/
public java.lang.String def2;
/**
*一期分摊金额
*/
public java.lang.String def3;
/**
*二期分摊金额
*/
public java.lang.String def4;
/**
*三期分摊金额
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
*自定义项26
*/
public java.lang.String def26;
/**
*自定义项27
*/
public java.lang.String def27;
/**
*自定义项28
*/
public java.lang.String def28;
/**
*自定义项29
*/
public java.lang.String def29;
/**
*自定义项30
*/
public java.lang.String def30;
/**
*自定义项31
*/
public java.lang.String def31;
/**
*自定义项32
*/
public java.lang.String def32;
/**
*自定义项33
*/
public java.lang.String def33;
/**
*自定义项34
*/
public java.lang.String def34;
/**
*自定义项35
*/
public java.lang.String def35;
/**
*自定义项36
*/
public java.lang.String def36;
/**
*自定义项37
*/
public java.lang.String def37;
/**
*自定义项38
*/
public java.lang.String def38;
/**
*自定义项39
*/
public java.lang.String def39;
/**
*自定义项40
*/
public java.lang.String def40;
/**
*自定义项41
*/
public java.lang.String def41;
/**
*自定义项42
*/
public java.lang.String def42;
/**
*自定义项43
*/
public java.lang.String def43;
/**
*自定义项44
*/
public java.lang.String def44;
/**
*自定义项45
*/
public java.lang.String def45;
/**
*自定义项46
*/
public java.lang.String def46;
/**
*自定义项47
*/
public java.lang.String def47;
/**
*自定义项48
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
*上层单据主键
*/
public String pk_contractaptment_h;
/**
*时间戳
*/
public UFDateTime ts;
    
    
/**
* 属性 pk_contractaptment_b的Getter方法.属性名：合同分摊子表主键
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getPk_contractaptment_b() {
return this.pk_contractaptment_b;
} 

/**
* 属性pk_contractaptment_b的Setter方法.属性名：合同分摊子表主键
* 创建日期:2019-9-18
* @param newPk_contractaptment_b java.lang.String
*/
public void setPk_contractaptment_b ( java.lang.String pk_contractaptment_b) {
this.pk_contractaptment_b=pk_contractaptment_b;
} 
 
/**
* 属性 pk_org的Getter方法.属性名：组织
*  创建日期:2019-9-18
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* 属性pk_org的Setter方法.属性名：组织
* 创建日期:2019-9-18
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* 属性 scomment的Getter方法.属性名：摘要
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getScomment() {
return this.scomment;
} 

/**
* 属性scomment的Setter方法.属性名：摘要
* 创建日期:2019-9-18
* @param newScomment java.lang.String
*/
public void setScomment ( java.lang.String scomment) {
this.scomment=scomment;
} 
 
/**
* 属性 conno的Getter方法.属性名：成本合同编号
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getConno() {
return this.conno;
} 

/**
* 属性conno的Setter方法.属性名：成本合同编号
* 创建日期:2019-9-18
* @param newConno java.lang.String
*/
public void setConno ( java.lang.String conno) {
this.conno=conno;
} 
 
/**
* 属性 aptionedoney的Getter方法.属性名：分摊金额
*  创建日期:2019-9-18
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getAptionedoney() {
return this.aptionedoney;
} 

/**
* 属性aptionedoney的Setter方法.属性名：分摊金额
* 创建日期:2019-9-18
* @param newAptionedoney nc.vo.pub.lang.UFDouble
*/
public void setAptionedoney ( nc.vo.pub.lang.UFDouble aptionedoney) {
this.aptionedoney=aptionedoney;
} 
 
/**
* 属性 def1的Getter方法.属性名：分摊合计金额
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* 属性def1的Setter方法.属性名：分摊合计金额
* 创建日期:2019-9-18
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* 属性 def2的Getter方法.属性名：自定义项2
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* 属性def2的Setter方法.属性名：自定义项2
* 创建日期:2019-9-18
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* 属性 def3的Getter方法.属性名：一期分摊金额
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* 属性def3的Setter方法.属性名：一期分摊金额
* 创建日期:2019-9-18
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* 属性 def4的Getter方法.属性名：二期分摊金额
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* 属性def4的Setter方法.属性名：二期分摊金额
* 创建日期:2019-9-18
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* 属性 def5的Getter方法.属性名：三期分摊金额
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* 属性def5的Setter方法.属性名：三期分摊金额
* 创建日期:2019-9-18
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* 属性 def6的Getter方法.属性名：自定义项6
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* 属性def6的Setter方法.属性名：自定义项6
* 创建日期:2019-9-18
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* 属性 def7的Getter方法.属性名：自定义项7
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* 属性def7的Setter方法.属性名：自定义项7
* 创建日期:2019-9-18
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* 属性 def8的Getter方法.属性名：自定义项8
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* 属性def8的Setter方法.属性名：自定义项8
* 创建日期:2019-9-18
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* 属性 def9的Getter方法.属性名：自定义项9
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* 属性def9的Setter方法.属性名：自定义项9
* 创建日期:2019-9-18
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* 属性 def10的Getter方法.属性名：自定义项10
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* 属性def10的Setter方法.属性名：自定义项10
* 创建日期:2019-9-18
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* 属性 def11的Getter方法.属性名：自定义项11
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* 属性def11的Setter方法.属性名：自定义项11
* 创建日期:2019-9-18
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* 属性 def12的Getter方法.属性名：自定义项12
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* 属性def12的Setter方法.属性名：自定义项12
* 创建日期:2019-9-18
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* 属性 def13的Getter方法.属性名：自定义项13
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* 属性def13的Setter方法.属性名：自定义项13
* 创建日期:2019-9-18
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* 属性 def14的Getter方法.属性名：自定义项14
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* 属性def14的Setter方法.属性名：自定义项14
* 创建日期:2019-9-18
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* 属性 def15的Getter方法.属性名：自定义项15
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* 属性def15的Setter方法.属性名：自定义项15
* 创建日期:2019-9-18
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* 属性 def16的Getter方法.属性名：自定义项16
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* 属性def16的Setter方法.属性名：自定义项16
* 创建日期:2019-9-18
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* 属性 def17的Getter方法.属性名：自定义项17
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* 属性def17的Setter方法.属性名：自定义项17
* 创建日期:2019-9-18
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* 属性 def18的Getter方法.属性名：自定义项18
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* 属性def18的Setter方法.属性名：自定义项18
* 创建日期:2019-9-18
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* 属性 def19的Getter方法.属性名：自定义项19
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* 属性def19的Setter方法.属性名：自定义项19
* 创建日期:2019-9-18
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* 属性 def20的Getter方法.属性名：自定义项20
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* 属性def20的Setter方法.属性名：自定义项20
* 创建日期:2019-9-18
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* 属性 def21的Getter方法.属性名：自定义项21
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef21() {
return this.def21;
} 

/**
* 属性def21的Setter方法.属性名：自定义项21
* 创建日期:2019-9-18
* @param newDef21 java.lang.String
*/
public void setDef21 ( java.lang.String def21) {
this.def21=def21;
} 
 
/**
* 属性 def22的Getter方法.属性名：自定义项22
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef22() {
return this.def22;
} 

/**
* 属性def22的Setter方法.属性名：自定义项22
* 创建日期:2019-9-18
* @param newDef22 java.lang.String
*/
public void setDef22 ( java.lang.String def22) {
this.def22=def22;
} 
 
/**
* 属性 def23的Getter方法.属性名：自定义项23
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef23() {
return this.def23;
} 

/**
* 属性def23的Setter方法.属性名：自定义项23
* 创建日期:2019-9-18
* @param newDef23 java.lang.String
*/
public void setDef23 ( java.lang.String def23) {
this.def23=def23;
} 
 
/**
* 属性 def24的Getter方法.属性名：自定义项24
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef24() {
return this.def24;
} 

/**
* 属性def24的Setter方法.属性名：自定义项24
* 创建日期:2019-9-18
* @param newDef24 java.lang.String
*/
public void setDef24 ( java.lang.String def24) {
this.def24=def24;
} 
 
/**
* 属性 def25的Getter方法.属性名：自定义项25
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef25() {
return this.def25;
} 

/**
* 属性def25的Setter方法.属性名：自定义项25
* 创建日期:2019-9-18
* @param newDef25 java.lang.String
*/
public void setDef25 ( java.lang.String def25) {
this.def25=def25;
} 
 
/**
* 属性 def26的Getter方法.属性名：自定义项26
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef26() {
return this.def26;
} 

/**
* 属性def26的Setter方法.属性名：自定义项26
* 创建日期:2019-9-18
* @param newDef26 java.lang.String
*/
public void setDef26 ( java.lang.String def26) {
this.def26=def26;
} 
 
/**
* 属性 def27的Getter方法.属性名：自定义项27
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef27() {
return this.def27;
} 

/**
* 属性def27的Setter方法.属性名：自定义项27
* 创建日期:2019-9-18
* @param newDef27 java.lang.String
*/
public void setDef27 ( java.lang.String def27) {
this.def27=def27;
} 
 
/**
* 属性 def28的Getter方法.属性名：自定义项28
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef28() {
return this.def28;
} 

/**
* 属性def28的Setter方法.属性名：自定义项28
* 创建日期:2019-9-18
* @param newDef28 java.lang.String
*/
public void setDef28 ( java.lang.String def28) {
this.def28=def28;
} 
 
/**
* 属性 def29的Getter方法.属性名：自定义项29
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef29() {
return this.def29;
} 

/**
* 属性def29的Setter方法.属性名：自定义项29
* 创建日期:2019-9-18
* @param newDef29 java.lang.String
*/
public void setDef29 ( java.lang.String def29) {
this.def29=def29;
} 
 
/**
* 属性 def30的Getter方法.属性名：自定义项30
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef30() {
return this.def30;
} 

/**
* 属性def30的Setter方法.属性名：自定义项30
* 创建日期:2019-9-18
* @param newDef30 java.lang.String
*/
public void setDef30 ( java.lang.String def30) {
this.def30=def30;
} 
 
/**
* 属性 def31的Getter方法.属性名：自定义项31
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef31() {
return this.def31;
} 

/**
* 属性def31的Setter方法.属性名：自定义项31
* 创建日期:2019-9-18
* @param newDef31 java.lang.String
*/
public void setDef31 ( java.lang.String def31) {
this.def31=def31;
} 
 
/**
* 属性 def32的Getter方法.属性名：自定义项32
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef32() {
return this.def32;
} 

/**
* 属性def32的Setter方法.属性名：自定义项32
* 创建日期:2019-9-18
* @param newDef32 java.lang.String
*/
public void setDef32 ( java.lang.String def32) {
this.def32=def32;
} 
 
/**
* 属性 def33的Getter方法.属性名：自定义项33
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef33() {
return this.def33;
} 

/**
* 属性def33的Setter方法.属性名：自定义项33
* 创建日期:2019-9-18
* @param newDef33 java.lang.String
*/
public void setDef33 ( java.lang.String def33) {
this.def33=def33;
} 
 
/**
* 属性 def34的Getter方法.属性名：自定义项34
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef34() {
return this.def34;
} 

/**
* 属性def34的Setter方法.属性名：自定义项34
* 创建日期:2019-9-18
* @param newDef34 java.lang.String
*/
public void setDef34 ( java.lang.String def34) {
this.def34=def34;
} 
 
/**
* 属性 def35的Getter方法.属性名：自定义项35
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef35() {
return this.def35;
} 

/**
* 属性def35的Setter方法.属性名：自定义项35
* 创建日期:2019-9-18
* @param newDef35 java.lang.String
*/
public void setDef35 ( java.lang.String def35) {
this.def35=def35;
} 
 
/**
* 属性 def36的Getter方法.属性名：自定义项36
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef36() {
return this.def36;
} 

/**
* 属性def36的Setter方法.属性名：自定义项36
* 创建日期:2019-9-18
* @param newDef36 java.lang.String
*/
public void setDef36 ( java.lang.String def36) {
this.def36=def36;
} 
 
/**
* 属性 def37的Getter方法.属性名：自定义项37
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef37() {
return this.def37;
} 

/**
* 属性def37的Setter方法.属性名：自定义项37
* 创建日期:2019-9-18
* @param newDef37 java.lang.String
*/
public void setDef37 ( java.lang.String def37) {
this.def37=def37;
} 
 
/**
* 属性 def38的Getter方法.属性名：自定义项38
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef38() {
return this.def38;
} 

/**
* 属性def38的Setter方法.属性名：自定义项38
* 创建日期:2019-9-18
* @param newDef38 java.lang.String
*/
public void setDef38 ( java.lang.String def38) {
this.def38=def38;
} 
 
/**
* 属性 def39的Getter方法.属性名：自定义项39
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef39() {
return this.def39;
} 

/**
* 属性def39的Setter方法.属性名：自定义项39
* 创建日期:2019-9-18
* @param newDef39 java.lang.String
*/
public void setDef39 ( java.lang.String def39) {
this.def39=def39;
} 
 
/**
* 属性 def40的Getter方法.属性名：自定义项40
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef40() {
return this.def40;
} 

/**
* 属性def40的Setter方法.属性名：自定义项40
* 创建日期:2019-9-18
* @param newDef40 java.lang.String
*/
public void setDef40 ( java.lang.String def40) {
this.def40=def40;
} 
 
/**
* 属性 def41的Getter方法.属性名：自定义项41
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef41() {
return this.def41;
} 

/**
* 属性def41的Setter方法.属性名：自定义项41
* 创建日期:2019-9-18
* @param newDef41 java.lang.String
*/
public void setDef41 ( java.lang.String def41) {
this.def41=def41;
} 
 
/**
* 属性 def42的Getter方法.属性名：自定义项42
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef42() {
return this.def42;
} 

/**
* 属性def42的Setter方法.属性名：自定义项42
* 创建日期:2019-9-18
* @param newDef42 java.lang.String
*/
public void setDef42 ( java.lang.String def42) {
this.def42=def42;
} 
 
/**
* 属性 def43的Getter方法.属性名：自定义项43
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef43() {
return this.def43;
} 

/**
* 属性def43的Setter方法.属性名：自定义项43
* 创建日期:2019-9-18
* @param newDef43 java.lang.String
*/
public void setDef43 ( java.lang.String def43) {
this.def43=def43;
} 
 
/**
* 属性 def44的Getter方法.属性名：自定义项44
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef44() {
return this.def44;
} 

/**
* 属性def44的Setter方法.属性名：自定义项44
* 创建日期:2019-9-18
* @param newDef44 java.lang.String
*/
public void setDef44 ( java.lang.String def44) {
this.def44=def44;
} 
 
/**
* 属性 def45的Getter方法.属性名：自定义项45
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef45() {
return this.def45;
} 

/**
* 属性def45的Setter方法.属性名：自定义项45
* 创建日期:2019-9-18
* @param newDef45 java.lang.String
*/
public void setDef45 ( java.lang.String def45) {
this.def45=def45;
} 
 
/**
* 属性 def46的Getter方法.属性名：自定义项46
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef46() {
return this.def46;
} 

/**
* 属性def46的Setter方法.属性名：自定义项46
* 创建日期:2019-9-18
* @param newDef46 java.lang.String
*/
public void setDef46 ( java.lang.String def46) {
this.def46=def46;
} 
 
/**
* 属性 def47的Getter方法.属性名：自定义项47
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef47() {
return this.def47;
} 

/**
* 属性def47的Setter方法.属性名：自定义项47
* 创建日期:2019-9-18
* @param newDef47 java.lang.String
*/
public void setDef47 ( java.lang.String def47) {
this.def47=def47;
} 
 
/**
* 属性 def48的Getter方法.属性名：自定义项48
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef48() {
return this.def48;
} 

/**
* 属性def48的Setter方法.属性名：自定义项48
* 创建日期:2019-9-18
* @param newDef48 java.lang.String
*/
public void setDef48 ( java.lang.String def48) {
this.def48=def48;
} 
 
/**
* 属性 def49的Getter方法.属性名：自定义项49
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef49() {
return this.def49;
} 

/**
* 属性def49的Setter方法.属性名：自定义项49
* 创建日期:2019-9-18
* @param newDef49 java.lang.String
*/
public void setDef49 ( java.lang.String def49) {
this.def49=def49;
} 
 
/**
* 属性 def50的Getter方法.属性名：自定义项50
*  创建日期:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef50() {
return this.def50;
} 

/**
* 属性def50的Setter方法.属性名：自定义项50
* 创建日期:2019-9-18
* @param newDef50 java.lang.String
*/
public void setDef50 ( java.lang.String def50) {
this.def50=def50;
} 
 
/**
* 属性 生成上层主键的Getter方法.属性名：上层主键
*  创建日期:2019-9-18
* @return String
*/
public String getPk_contractaptment_h(){
return this.pk_contractaptment_h;
}
/**
* 属性生成上层主键的Setter方法.属性名：上层主键
* 创建日期:2019-9-18
* @param newPk_contractaptment_h String
*/
public void setPk_contractaptment_h(String pk_contractaptment_h){
this.pk_contractaptment_h=pk_contractaptment_h;
} 
/**
* 属性 生成时间戳的Getter方法.属性名：时间戳
*  创建日期:2019-9-18
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* 属性生成时间戳的Setter方法.属性名：时间戳
* 创建日期:2019-9-18
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.ContractAptmentBVO");
    }
   }
    