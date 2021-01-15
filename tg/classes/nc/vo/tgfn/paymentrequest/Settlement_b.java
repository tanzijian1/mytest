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
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2019-8-19
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class Settlement_b extends SuperVO {
	
/**
*����ҳǩ����
*/
public java.lang.String pk_settlement_b;
/**
*ժҪ
*/
public java.lang.String scomment;
/**
*����ҵ������
*/
public java.lang.String pk_businesstype;
/**
*���������
*/
public nc.vo.pub.lang.UFDouble mny_req;
/**
*���θ�����
*/
public nc.vo.pub.lang.UFDouble mny_pay;
/**
*���㷽ʽ
*/
public java.lang.String settlement;
/**
*���������˺�
*/
public java.lang.String pk_payaccount;
/**
*Ʊ�ݺ�
*/
public java.lang.String pk_billno;
/**
*��Ӧ��
*/
public java.lang.String pk_supplier;
/**
*�տ������˺�
*/
public java.lang.String pk_recaccount;
/**
*�к�
*/
public java.lang.String rowno;
/**
*�ϲ㵥������
*/
public String pk_payreq;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_settlement_b��Getter����.������������ҳǩ����
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_settlement_b() {
return this.pk_settlement_b;
} 

/**
* ����pk_settlement_b��Setter����.������������ҳǩ����
* ��������:2019-8-19
* @param newPk_settlement_b java.lang.String
*/
public void setPk_settlement_b ( java.lang.String pk_settlement_b) {
this.pk_settlement_b=pk_settlement_b;
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
* ���� pk_businesstype��Getter����.������������ҵ������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_businesstype() {
return this.pk_businesstype;
} 

/**
* ����pk_businesstype��Setter����.������������ҵ������
* ��������:2019-8-19
* @param newPk_businesstype java.lang.String
*/
public void setPk_businesstype ( java.lang.String pk_businesstype) {
this.pk_businesstype=pk_businesstype;
} 
 
/**
* ���� mny_req��Getter����.�����������������
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMny_req() {
return this.mny_req;
} 

/**
* ����mny_req��Setter����.�����������������
* ��������:2019-8-19
* @param newMny_req nc.vo.pub.lang.UFDouble
*/
public void setMny_req ( nc.vo.pub.lang.UFDouble mny_req) {
this.mny_req=mny_req;
} 
 
/**
* ���� mny_pay��Getter����.�����������θ�����
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMny_pay() {
return this.mny_pay;
} 

/**
* ����mny_pay��Setter����.�����������θ�����
* ��������:2019-8-19
* @param newMny_pay nc.vo.pub.lang.UFDouble
*/
public void setMny_pay ( nc.vo.pub.lang.UFDouble mny_pay) {
this.mny_pay=mny_pay;
} 
 
/**
* ���� settlement��Getter����.�����������㷽ʽ
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSettlement() {
return this.settlement;
} 

/**
* ����settlement��Setter����.�����������㷽ʽ
* ��������:2019-8-19
* @param newSettlement java.lang.String
*/
public void setSettlement ( java.lang.String settlement) {
this.settlement=settlement;
} 
 
/**
* ���� pk_payaccount��Getter����.�����������������˺�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_payaccount() {
return this.pk_payaccount;
} 

/**
* ����pk_payaccount��Setter����.�����������������˺�
* ��������:2019-8-19
* @param newPk_payaccount java.lang.String
*/
public void setPk_payaccount ( java.lang.String pk_payaccount) {
this.pk_payaccount=pk_payaccount;
} 
 
/**
* ���� pk_billno��Getter����.��������Ʊ�ݺ�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_billno() {
return this.pk_billno;
} 

/**
* ����pk_billno��Setter����.��������Ʊ�ݺ�
* ��������:2019-8-19
* @param newPk_billno java.lang.String
*/
public void setPk_billno ( java.lang.String pk_billno) {
this.pk_billno=pk_billno;
} 
 
/**
* ���� pk_supplier��Getter����.����������Ӧ��
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_supplier() {
return this.pk_supplier;
} 

/**
* ����pk_supplier��Setter����.����������Ӧ��
* ��������:2019-8-19
* @param newPk_supplier java.lang.String
*/
public void setPk_supplier ( java.lang.String pk_supplier) {
this.pk_supplier=pk_supplier;
} 
 
/**
* ���� pk_recaccount��Getter����.���������տ������˺�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_recaccount() {
return this.pk_recaccount;
} 

/**
* ����pk_recaccount��Setter����.���������տ������˺�
* ��������:2019-8-19
* @param newPk_recaccount java.lang.String
*/
public void setPk_recaccount ( java.lang.String pk_recaccount) {
this.pk_recaccount=pk_recaccount;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2019-8-19
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� �����ϲ�������Getter����.���������ϲ�����
*  ��������:2019-8-19
* @return String
*/
public String getPk_payreq(){
return this.pk_payreq;
}
/**
* ���������ϲ�������Setter����.���������ϲ�����
* ��������:2019-8-19
* @param newPk_payreq String
*/
public void setPk_payreq(String pk_payreq){
this.pk_payreq=pk_payreq;
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
    return VOMetaFactory.getInstance().getVOMeta("tg.settlement_b");
    }
   }
    