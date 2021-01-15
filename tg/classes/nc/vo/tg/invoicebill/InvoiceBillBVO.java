package nc.vo.tg.invoicebill;

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
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2019-8-19
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class InvoiceBillBVO extends SuperVO {
	public static final String SUPPLIER = "supplier";
	public static final String QUANTITY_DE = "quantity_de";
	public static final String TAXPRICE = "taxprice";
	public static final String TAXRATE = "taxrate";
	public static final String LOCAL_TAX_DE = "local_tax_de";
	public static final String NOTAX_DE = "notax_de";
	public static final String MONEY_DE = "money_de";
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
	public static final String PK_INVOICEBILL_H = "pk_invoicebill_h";
	public static final String TS = "ts";
	public static final String DR = "dr";
	public static final String PK_INVOICEBILL_B = "pk_invoicebill_b";
	public static final String PK_ORG = "pk_org";
	public static final String SCOMMENT = "scomment";
	
/**
*���Ʊ�����ֱ�����
*/
public java.lang.String pk_invoicebill_b;
/**
*��֯
*/
public java.lang.String pk_org;
/**
*ժҪ
*/
public java.lang.String scomment;
/**
*��Ӧ��
*/
public java.lang.String supplier;
/**
*����
*/
public nc.vo.pub.lang.UFDouble quantity_de;
/**
*��˰����
*/
public nc.vo.pub.lang.UFDouble taxprice;
/**
*˰��
*/
public nc.vo.pub.lang.UFDouble taxrate;
/**
*˰��
*/
public nc.vo.pub.lang.UFDouble local_tax_de;
/**
*����˰���
*/
public nc.vo.pub.lang.UFDouble notax_de;
/**
*��˰�ϼ�
*/
public nc.vo.pub.lang.UFDouble money_de;
/**
*�Զ�����1
*/
public java.lang.String def1;
/**
*�Զ�����2
*/
public java.lang.String def2;
/**
*�Զ�����3
*/
public java.lang.String def3;
/**
*�Զ�����4
*/
public java.lang.String def4;
/**
*�Զ�����5
*/
public java.lang.String def5;
/**
*�Զ�����6
*/
public java.lang.String def6;
/**
*�Զ�����7
*/
public java.lang.String def7;
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
*�ϲ㵥������
*/
public String pk_invoicebill_h;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_invoicebill_b��Getter����.�����������Ʊ�����ֱ�����
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_invoicebill_b() {
return this.pk_invoicebill_b;
} 

/**
* ����pk_invoicebill_b��Setter����.�����������Ʊ�����ֱ�����
* ��������:2019-8-19
* @param newPk_invoicebill_b java.lang.String
*/
public void setPk_invoicebill_b ( java.lang.String pk_invoicebill_b) {
this.pk_invoicebill_b=pk_invoicebill_b;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-8-19
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-8-19
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� scomment��Getter����.��������ժҪ
*  ��������:2019-8-19
* @return nc.vo.cdm.bankcontiabill.enumconst
*/
public java.lang.String getScomment() {
return this.scomment;
} 

/**
* ����scomment��Setter����.��������ժҪ
* ��������:2019-8-19
* @param newScomment nc.vo.cdm.bankcontiabill.enumconst
*/
public void setScomment ( java.lang.String scomment) {
this.scomment=scomment;
} 
 
/**
* ���� supplier��Getter����.����������Ӧ��
*  ��������:2019-8-19
* @return nc.vo.bd.supplier.SupplierVO
*/
public java.lang.String getSupplier() {
return this.supplier;
} 

/**
* ����supplier��Setter����.����������Ӧ��
* ��������:2019-8-19
* @param newSupplier nc.vo.bd.supplier.SupplierVO
*/
public void setSupplier ( java.lang.String supplier) {
this.supplier=supplier;
} 
 
/**
* ���� quantity_de��Getter����.������������
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getQuantity_de() {
return this.quantity_de;
} 

/**
* ����quantity_de��Setter����.������������
* ��������:2019-8-19
* @param newQuantity_de nc.vo.pub.lang.UFDouble
*/
public void setQuantity_de ( nc.vo.pub.lang.UFDouble quantity_de) {
this.quantity_de=quantity_de;
} 
 
/**
* ���� taxprice��Getter����.����������˰����
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTaxprice() {
return this.taxprice;
} 

/**
* ����taxprice��Setter����.����������˰����
* ��������:2019-8-19
* @param newTaxprice nc.vo.pub.lang.UFDouble
*/
public void setTaxprice ( nc.vo.pub.lang.UFDouble taxprice) {
this.taxprice=taxprice;
} 
 
/**
* ���� taxrate��Getter����.��������˰��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTaxrate() {
return this.taxrate;
} 

/**
* ����taxrate��Setter����.��������˰��
* ��������:2019-8-19
* @param newTaxrate nc.vo.pub.lang.UFDouble
*/
public void setTaxrate ( nc.vo.pub.lang.UFDouble taxrate) {
this.taxrate=taxrate;
} 
 
/**
* ���� local_tax_de��Getter����.��������˰��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getLocal_tax_de() {
return this.local_tax_de;
} 

/**
* ����local_tax_de��Setter����.��������˰��
* ��������:2019-8-19
* @param newLocal_tax_de nc.vo.pub.lang.UFDouble
*/
public void setLocal_tax_de ( nc.vo.pub.lang.UFDouble local_tax_de) {
this.local_tax_de=local_tax_de;
} 
 
/**
* ���� notax_de��Getter����.������������˰���
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNotax_de() {
return this.notax_de;
} 

/**
* ����notax_de��Setter����.������������˰���
* ��������:2019-8-19
* @param newNotax_de nc.vo.pub.lang.UFDouble
*/
public void setNotax_de ( nc.vo.pub.lang.UFDouble notax_de) {
this.notax_de=notax_de;
} 
 
/**
* ���� money_de��Getter����.����������˰�ϼ�
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMoney_de() {
return this.money_de;
} 

/**
* ����money_de��Setter����.����������˰�ϼ�
* ��������:2019-8-19
* @param newMoney_de nc.vo.pub.lang.UFDouble
*/
public void setMoney_de ( nc.vo.pub.lang.UFDouble money_de) {
this.money_de=money_de;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-8-19
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-8-19
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-8-19
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-8-19
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-8-19
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-8-19
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-8-19
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-8-19
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-8-19
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-8-19
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2019-8-19
* @return String
*/
public String getPk_invoicebill_h(){
return this.pk_invoicebill_h;
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2019-8-19
* @param newPk_invoicebill_h String
*/
public void setPk_invoicebill_h(String pk_invoicebill_h){
this.pk_invoicebill_h=pk_invoicebill_h;
} 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-8-19
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.InvoiceBillBVO");
    }
   }
    