package nc.vo.tgfn.paymentrequest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴������۵�������Ϣ
 * </p>
 *  ��������:2019-12-9
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class Business_b extends SuperVO {
	
/**
*ҵ��ҳǩ����
*/
public java.lang.String pk_business_b;
/**
*ժҪ
*/
public java.lang.String scomment;
/**
*Ԥ���Ŀ
*/
public java.lang.String def1;
/**
*��֧��Ŀ
*/
public java.lang.String pk_subjcode;
/**
*��������
*/
public java.lang.String def2;
/**
*ҵ̬
*/
public java.lang.String def3;
/**
*����
*/
public java.lang.String def4;
/**
*��������
*/
public java.lang.Integer objtype;
/**
*��Ӧ��
*/
public java.lang.String supplier;
/**
*ʵ���տ�����˻�
*/
public java.lang.String def5;
/**
*�տ�������к�
*/
public java.lang.String def6;
/**
*�տ�˻�
*/
public java.lang.String def7;
/**
*������
*/
public nc.vo.pub.lang.UFDouble local_money_de;
/**
*�Զ�����8
*/
public java.lang.String def8;
/**
*�Զ�����9
*/
public java.lang.String def9;
/**
*�Զ�����10
*/
public java.lang.String def10;
/**
*�Զ�����11
*/
public java.lang.String def11;
/**
*�Զ�����12
*/
public java.lang.String def12;
/**
*�Զ�����13
*/
public java.lang.String def13;
/**
*�Զ�����14
*/
public java.lang.String def14;
/**
*�Զ�����15
*/
public java.lang.String def15;
/**
*�Զ�����16
*/
public java.lang.String def16;
/**
*�Զ�����17
*/
public java.lang.String def17;
/**
*�Զ�����18
*/
public java.lang.String def18;
/**
*�Զ�����19
*/
public java.lang.String def19;
/**
*�Զ�����20
*/
public java.lang.String def20;
/**
*�Զ�����21
*/
public java.lang.String def21;
/**
*�Զ�����22
*/
public java.lang.String def22;
/**
*�Զ�����23
*/
public java.lang.String def23;
/**
*�Զ�����24
*/
public java.lang.String def24;
/**
*�Զ�����25
*/
public java.lang.String def25;
/**
*�Զ�����26
*/
public java.lang.String def26;
/**
*�Զ�����27
*/
public java.lang.String def27;
/**
*�Զ�����28
*/
public java.lang.String def28;
/**
*�Զ�����29
*/
public java.lang.String def29;
/**
*�Զ�����30
*/
public java.lang.String def30;
/**
*�Զ�����31
*/
public java.lang.String def31;
/**
*�Զ�����32
*/
public java.lang.String def32;
/**
*�Զ�����33
*/
public java.lang.String def33;
/**
*�Զ�����34
*/
public java.lang.String def34;
/**
*�Զ�����35
*/
public java.lang.String def35;
/**
*�Զ�����36
*/
public java.lang.String def36;
/**
*�Զ�����37
*/
public java.lang.String def37;
/**
*�Զ�����38
*/
public java.lang.String def38;
/**
*�Զ�����39
*/
public java.lang.String def39;
/**
*�Զ�����40
*/
public java.lang.String def40;
/**
*�Զ�����41
*/
public java.lang.String def41;
/**
*�Զ�����42
*/
public java.lang.String def42;
/**
*�Զ�����43
*/
public java.lang.String def43;
/**
*�Զ�����44
*/
public java.lang.String def44;
/**
*�Զ�����45
*/
public java.lang.String def45;
/**
*�Զ�����46
*/
public java.lang.String def46;
/**
*�Զ�����47
*/
public java.lang.String def47;
/**
*�Զ�����48
*/
public java.lang.String def48;
/**
*�Զ�����49
*/
public java.lang.String def49;
/**
*�Զ�����50
*/
public java.lang.String def50;
/**
*�Զ�����51
*/
public java.lang.String def51;
/**
*�Զ�����59
*/
public java.lang.String def59;
/**
*�Զ�����58
*/
public java.lang.String def58;
/**
*�Զ�����57
*/
public java.lang.String def57;
/**
*�Զ�����56
*/
public java.lang.String def56;
/**
*�Զ�����55
*/
public java.lang.String def55;
/**
*�Զ�����54
*/
public java.lang.String def54;
/**
*�Զ�����53
*/
public java.lang.String def53;
/**
*�Զ�����52
*/
public java.lang.String def52;
/**
*�Զ�����60
*/
public java.lang.String def60;
/**
*�ϲ㵥������
*/
public String pk_payreq;
/**
*ʱ���
*/
public UFDateTime ts;
/**
 * dr
 */
public Integer  dr;

public Integer getDr() {
	return dr;
}

public void setDr(Integer dr) {
	this.dr = dr;
}

/**
* ���� pk_business_b��Getter����.��������ҵ��ҳǩ����
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getPk_business_b() {
return this.pk_business_b;
} 

/**
* ����pk_business_b��Setter����.��������ҵ��ҳǩ����
* ��������:2019-12-9
* @param newPk_business_b java.lang.String
*/
public void setPk_business_b ( java.lang.String pk_business_b) {
this.pk_business_b=pk_business_b;
} 
 
/**
* ���� scomment��Getter����.��������ժҪ
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getScomment() {
return this.scomment;
} 

/**
* ����scomment��Setter����.��������ժҪ
* ��������:2019-12-9
* @param newScomment java.lang.String
*/
public void setScomment ( java.lang.String scomment) {
this.scomment=scomment;
} 
 
/**
* ���� def1��Getter����.��������Ԥ���Ŀ
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.��������Ԥ���Ŀ
* ��������:2019-12-9
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� pk_subjcode��Getter����.����������֧��Ŀ
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getPk_subjcode() {
return this.pk_subjcode;
} 

/**
* ����pk_subjcode��Setter����.����������֧��Ŀ
* ��������:2019-12-9
* @param newPk_subjcode java.lang.String
*/
public void setPk_subjcode ( java.lang.String pk_subjcode) {
this.pk_subjcode=pk_subjcode;
} 
 
/**
* ���� def2��Getter����.����������������
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.����������������
* ��������:2019-12-9
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.��������ҵ̬
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.��������ҵ̬
* ��������:2019-12-9
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.������������
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.������������
* ��������:2019-12-9
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� objtype��Getter����.����������������
*  ��������:2019-12-9
* @return nc.vo.arap.agiotage.ObjTypeEnum
*/
public java.lang.Integer getObjtype() {
return this.objtype;
} 

/**
* ����objtype��Setter����.����������������
* ��������:2019-12-9
* @param newObjtype nc.vo.arap.agiotage.ObjTypeEnum
*/
public void setObjtype ( java.lang.Integer objtype) {
this.objtype=objtype;
} 
 
/**
* ���� supplier��Getter����.����������Ӧ��
*  ��������:2019-12-9
* @return nc.vo.bd.cust.CustSupplierVO
*/
public java.lang.String getSupplier() {
return this.supplier;
} 

/**
* ����supplier��Setter����.����������Ӧ��
* ��������:2019-12-9
* @param newSupplier nc.vo.bd.cust.CustSupplierVO
*/
public void setSupplier ( java.lang.String supplier) {
this.supplier=supplier;
} 
 
/**
* ���� def5��Getter����.��������ʵ���տ�����˻�
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.��������ʵ���տ�����˻�
* ��������:2019-12-9
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������տ�������к�
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������տ�������к�
* ��������:2019-12-9
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������տ�˻�
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������տ�˻�
* ��������:2019-12-9
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� local_money_de��Getter����.��������������
*  ��������:2019-12-9
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getLocal_money_de() {
return this.local_money_de;
} 

/**
* ����local_money_de��Setter����.��������������
* ��������:2019-12-9
* @param newLocal_money_de nc.vo.pub.lang.UFDouble
*/
public void setLocal_money_de ( nc.vo.pub.lang.UFDouble local_money_de) {
this.local_money_de=local_money_de;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-12-9
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-12-9
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-12-9
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-12-9
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-12-9
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-12-9
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-12-9
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-12-9
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-12-9
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-12-9
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-12-9
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-12-9
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-12-9
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* ���� def21��Getter����.���������Զ�����21
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef21() {
return this.def21;
} 

/**
* ����def21��Setter����.���������Զ�����21
* ��������:2019-12-9
* @param newDef21 java.lang.String
*/
public void setDef21 ( java.lang.String def21) {
this.def21=def21;
} 
 
/**
* ���� def22��Getter����.���������Զ�����22
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef22() {
return this.def22;
} 

/**
* ����def22��Setter����.���������Զ�����22
* ��������:2019-12-9
* @param newDef22 java.lang.String
*/
public void setDef22 ( java.lang.String def22) {
this.def22=def22;
} 
 
/**
* ���� def23��Getter����.���������Զ�����23
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef23() {
return this.def23;
} 

/**
* ����def23��Setter����.���������Զ�����23
* ��������:2019-12-9
* @param newDef23 java.lang.String
*/
public void setDef23 ( java.lang.String def23) {
this.def23=def23;
} 
 
/**
* ���� def24��Getter����.���������Զ�����24
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef24() {
return this.def24;
} 

/**
* ����def24��Setter����.���������Զ�����24
* ��������:2019-12-9
* @param newDef24 java.lang.String
*/
public void setDef24 ( java.lang.String def24) {
this.def24=def24;
} 
 
/**
* ���� def25��Getter����.���������Զ�����25
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef25() {
return this.def25;
} 

/**
* ����def25��Setter����.���������Զ�����25
* ��������:2019-12-9
* @param newDef25 java.lang.String
*/
public void setDef25 ( java.lang.String def25) {
this.def25=def25;
} 
 
/**
* ���� def26��Getter����.���������Զ�����26
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef26() {
return this.def26;
} 

/**
* ����def26��Setter����.���������Զ�����26
* ��������:2019-12-9
* @param newDef26 java.lang.String
*/
public void setDef26 ( java.lang.String def26) {
this.def26=def26;
} 
 
/**
* ���� def27��Getter����.���������Զ�����27
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef27() {
return this.def27;
} 

/**
* ����def27��Setter����.���������Զ�����27
* ��������:2019-12-9
* @param newDef27 java.lang.String
*/
public void setDef27 ( java.lang.String def27) {
this.def27=def27;
} 
 
/**
* ���� def28��Getter����.���������Զ�����28
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef28() {
return this.def28;
} 

/**
* ����def28��Setter����.���������Զ�����28
* ��������:2019-12-9
* @param newDef28 java.lang.String
*/
public void setDef28 ( java.lang.String def28) {
this.def28=def28;
} 
 
/**
* ���� def29��Getter����.���������Զ�����29
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef29() {
return this.def29;
} 

/**
* ����def29��Setter����.���������Զ�����29
* ��������:2019-12-9
* @param newDef29 java.lang.String
*/
public void setDef29 ( java.lang.String def29) {
this.def29=def29;
} 
 
/**
* ���� def30��Getter����.���������Զ�����30
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef30() {
return this.def30;
} 

/**
* ����def30��Setter����.���������Զ�����30
* ��������:2019-12-9
* @param newDef30 java.lang.String
*/
public void setDef30 ( java.lang.String def30) {
this.def30=def30;
} 
 
/**
* ���� def31��Getter����.���������Զ�����31
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef31() {
return this.def31;
} 

/**
* ����def31��Setter����.���������Զ�����31
* ��������:2019-12-9
* @param newDef31 java.lang.String
*/
public void setDef31 ( java.lang.String def31) {
this.def31=def31;
} 
 
/**
* ���� def32��Getter����.���������Զ�����32
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef32() {
return this.def32;
} 

/**
* ����def32��Setter����.���������Զ�����32
* ��������:2019-12-9
* @param newDef32 java.lang.String
*/
public void setDef32 ( java.lang.String def32) {
this.def32=def32;
} 
 
/**
* ���� def33��Getter����.���������Զ�����33
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef33() {
return this.def33;
} 

/**
* ����def33��Setter����.���������Զ�����33
* ��������:2019-12-9
* @param newDef33 java.lang.String
*/
public void setDef33 ( java.lang.String def33) {
this.def33=def33;
} 
 
/**
* ���� def34��Getter����.���������Զ�����34
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef34() {
return this.def34;
} 

/**
* ����def34��Setter����.���������Զ�����34
* ��������:2019-12-9
* @param newDef34 java.lang.String
*/
public void setDef34 ( java.lang.String def34) {
this.def34=def34;
} 
 
/**
* ���� def35��Getter����.���������Զ�����35
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef35() {
return this.def35;
} 

/**
* ����def35��Setter����.���������Զ�����35
* ��������:2019-12-9
* @param newDef35 java.lang.String
*/
public void setDef35 ( java.lang.String def35) {
this.def35=def35;
} 
 
/**
* ���� def36��Getter����.���������Զ�����36
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef36() {
return this.def36;
} 

/**
* ����def36��Setter����.���������Զ�����36
* ��������:2019-12-9
* @param newDef36 java.lang.String
*/
public void setDef36 ( java.lang.String def36) {
this.def36=def36;
} 
 
/**
* ���� def37��Getter����.���������Զ�����37
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef37() {
return this.def37;
} 

/**
* ����def37��Setter����.���������Զ�����37
* ��������:2019-12-9
* @param newDef37 java.lang.String
*/
public void setDef37 ( java.lang.String def37) {
this.def37=def37;
} 
 
/**
* ���� def38��Getter����.���������Զ�����38
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef38() {
return this.def38;
} 

/**
* ����def38��Setter����.���������Զ�����38
* ��������:2019-12-9
* @param newDef38 java.lang.String
*/
public void setDef38 ( java.lang.String def38) {
this.def38=def38;
} 
 
/**
* ���� def39��Getter����.���������Զ�����39
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef39() {
return this.def39;
} 

/**
* ����def39��Setter����.���������Զ�����39
* ��������:2019-12-9
* @param newDef39 java.lang.String
*/
public void setDef39 ( java.lang.String def39) {
this.def39=def39;
} 
 
/**
* ���� def40��Getter����.���������Զ�����40
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef40() {
return this.def40;
} 

/**
* ����def40��Setter����.���������Զ�����40
* ��������:2019-12-9
* @param newDef40 java.lang.String
*/
public void setDef40 ( java.lang.String def40) {
this.def40=def40;
} 
 
/**
* ���� def41��Getter����.���������Զ�����41
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef41() {
return this.def41;
} 

/**
* ����def41��Setter����.���������Զ�����41
* ��������:2019-12-9
* @param newDef41 java.lang.String
*/
public void setDef41 ( java.lang.String def41) {
this.def41=def41;
} 
 
/**
* ���� def42��Getter����.���������Զ�����42
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef42() {
return this.def42;
} 

/**
* ����def42��Setter����.���������Զ�����42
* ��������:2019-12-9
* @param newDef42 java.lang.String
*/
public void setDef42 ( java.lang.String def42) {
this.def42=def42;
} 
 
/**
* ���� def43��Getter����.���������Զ�����43
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef43() {
return this.def43;
} 

/**
* ����def43��Setter����.���������Զ�����43
* ��������:2019-12-9
* @param newDef43 java.lang.String
*/
public void setDef43 ( java.lang.String def43) {
this.def43=def43;
} 
 
/**
* ���� def44��Getter����.���������Զ�����44
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef44() {
return this.def44;
} 

/**
* ����def44��Setter����.���������Զ�����44
* ��������:2019-12-9
* @param newDef44 java.lang.String
*/
public void setDef44 ( java.lang.String def44) {
this.def44=def44;
} 
 
/**
* ���� def45��Getter����.���������Զ�����45
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef45() {
return this.def45;
} 

/**
* ����def45��Setter����.���������Զ�����45
* ��������:2019-12-9
* @param newDef45 java.lang.String
*/
public void setDef45 ( java.lang.String def45) {
this.def45=def45;
} 
 
/**
* ���� def46��Getter����.���������Զ�����46
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef46() {
return this.def46;
} 

/**
* ����def46��Setter����.���������Զ�����46
* ��������:2019-12-9
* @param newDef46 java.lang.String
*/
public void setDef46 ( java.lang.String def46) {
this.def46=def46;
} 
 
/**
* ���� def47��Getter����.���������Զ�����47
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef47() {
return this.def47;
} 

/**
* ����def47��Setter����.���������Զ�����47
* ��������:2019-12-9
* @param newDef47 java.lang.String
*/
public void setDef47 ( java.lang.String def47) {
this.def47=def47;
} 
 
/**
* ���� def48��Getter����.���������Զ�����48
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef48() {
return this.def48;
} 

/**
* ����def48��Setter����.���������Զ�����48
* ��������:2019-12-9
* @param newDef48 java.lang.String
*/
public void setDef48 ( java.lang.String def48) {
this.def48=def48;
} 
 
/**
* ���� def49��Getter����.���������Զ�����49
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef49() {
return this.def49;
} 

/**
* ����def49��Setter����.���������Զ�����49
* ��������:2019-12-9
* @param newDef49 java.lang.String
*/
public void setDef49 ( java.lang.String def49) {
this.def49=def49;
} 
 
/**
* ���� def50��Getter����.���������Զ�����50
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef50() {
return this.def50;
} 

/**
* ����def50��Setter����.���������Զ�����50
* ��������:2019-12-9
* @param newDef50 java.lang.String
*/
public void setDef50 ( java.lang.String def50) {
this.def50=def50;
} 
 
/**
* ���� def51��Getter����.���������Զ�����51
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef51() {
return this.def51;
} 

/**
* ����def51��Setter����.���������Զ�����51
* ��������:2019-12-9
* @param newDef51 java.lang.String
*/
public void setDef51 ( java.lang.String def51) {
this.def51=def51;
} 
 
/**
* ���� def59��Getter����.���������Զ�����59
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef59() {
return this.def59;
} 

/**
* ����def59��Setter����.���������Զ�����59
* ��������:2019-12-9
* @param newDef59 java.lang.String
*/
public void setDef59 ( java.lang.String def59) {
this.def59=def59;
} 
 
/**
* ���� def58��Getter����.���������Զ�����58
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef58() {
return this.def58;
} 

/**
* ����def58��Setter����.���������Զ�����58
* ��������:2019-12-9
* @param newDef58 java.lang.String
*/
public void setDef58 ( java.lang.String def58) {
this.def58=def58;
} 
 
/**
* ���� def57��Getter����.���������Զ�����57
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef57() {
return this.def57;
} 

/**
* ����def57��Setter����.���������Զ�����57
* ��������:2019-12-9
* @param newDef57 java.lang.String
*/
public void setDef57 ( java.lang.String def57) {
this.def57=def57;
} 
 
/**
* ���� def56��Getter����.���������Զ�����56
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef56() {
return this.def56;
} 

/**
* ����def56��Setter����.���������Զ�����56
* ��������:2019-12-9
* @param newDef56 java.lang.String
*/
public void setDef56 ( java.lang.String def56) {
this.def56=def56;
} 
 
/**
* ���� def55��Getter����.���������Զ�����55
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef55() {
return this.def55;
} 

/**
* ����def55��Setter����.���������Զ�����55
* ��������:2019-12-9
* @param newDef55 java.lang.String
*/
public void setDef55 ( java.lang.String def55) {
this.def55=def55;
} 
 
/**
* ���� def54��Getter����.���������Զ�����54
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef54() {
return this.def54;
} 

/**
* ����def54��Setter����.���������Զ�����54
* ��������:2019-12-9
* @param newDef54 java.lang.String
*/
public void setDef54 ( java.lang.String def54) {
this.def54=def54;
} 
 
/**
* ���� def53��Getter����.���������Զ�����53
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef53() {
return this.def53;
} 

/**
* ����def53��Setter����.���������Զ�����53
* ��������:2019-12-9
* @param newDef53 java.lang.String
*/
public void setDef53 ( java.lang.String def53) {
this.def53=def53;
} 
 
/**
* ���� def52��Getter����.���������Զ�����52
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef52() {
return this.def52;
} 

/**
* ����def52��Setter����.���������Զ�����52
* ��������:2019-12-9
* @param newDef52 java.lang.String
*/
public void setDef52 ( java.lang.String def52) {
this.def52=def52;
} 
 
/**
* ���� def60��Getter����.���������Զ�����60
*  ��������:2019-12-9
* @return java.lang.String
*/
public java.lang.String getDef60() {
return this.def60;
} 

/**
* ����def60��Setter����.���������Զ�����60
* ��������:2019-12-9
* @param newDef60 java.lang.String
*/
public void setDef60 ( java.lang.String def60) {
this.def60=def60;
} 
 
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2019-12-9
* @return String
*/
public String getPk_payreq(){
return this.pk_payreq;
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2019-12-9
* @param newPk_payreq String
*/
public void setPk_payreq(String pk_payreq){
this.pk_payreq=pk_payreq;
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-12-9
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-12-9
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.business_b");
    }
   }
    