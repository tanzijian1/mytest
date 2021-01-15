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
*���Ʊ������������
*/
public java.lang.String pk_invoicebill_h;
/**
*����
*/
public java.lang.String pk_group;
/**
*��֯
*/
public java.lang.String pk_org;
/**
*��֯�汾
*/
public java.lang.String pk_org_v;
/**
*���ݺ�
*/
public java.lang.String billno;
/**
*����ʱ��
*/
public UFDate billdate;
/**
*���첿��
*/
public java.lang.String pk_deptid_v;
/**
*������
*/
public java.lang.String pk_psndoc;
/**
*��������
*/
public java.lang.String objtype;
/**
*��Ӧ��
*/
public java.lang.String supplier;
/**
*Ʊ������
*/
public java.lang.String checktype;
/**
*����״̬
*/
public java.lang.Integer billstatus;
/**
*����״̬
*/
public java.lang.Integer approvestatus;
/**
*��Ч״̬
*/
public java.lang.Integer effectstatus;
/**
*ҵ������
*/
public java.lang.String pk_busitype;
/**
*������
*/
public java.lang.String creator;
/**
*����ʱ��
*/
public UFDateTime creationtime;
/**
*����޸���
*/
public java.lang.String modifier;
/**
*����޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*ҵ������
*/
public java.lang.String busitype;
/**
*������
*/
public java.lang.String approver;
/**
*��������
*/
public java.lang.String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��������
*/
public java.lang.String transtype;
/**
*��������
*/
public java.lang.String billtype;
/**
*��������pk
*/
public java.lang.String transtypepk;
/**
*��Դ��������
*/
public java.lang.String srcbilltype;
/**
*��Դ����id
*/
public java.lang.String srcbillid;
/**
*��Ӧ��ͬ���
*/
public java.lang.String def1;
/**
*��Ʊ��˰���ϼ�
*/
public java.lang.String def2;
/**
*���ز���Ŀ
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
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_invoicebill_h��Getter����.�����������Ʊ������������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getPk_invoicebill_h() {
return this.pk_invoicebill_h;
} 

/**
* ����pk_invoicebill_h��Setter����.�����������Ʊ������������
* ��������:2019-8-19
* @param newPk_invoicebill_h java.lang.String
*/
public void setPk_invoicebill_h ( java.lang.String pk_invoicebill_h) {
this.pk_invoicebill_h=pk_invoicebill_h;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-8-19
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-8-19
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
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
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-8-19
* @return nc.vo.vorg.FinanceOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-8-19
* @param newPk_org_v nc.vo.vorg.FinanceOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-8-19
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* ���� billdate��Getter����.������������ʱ��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* ����billdate��Setter����.������������ʱ��
* ��������:2019-8-19
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* ���� pk_deptid_v��Getter����.�����������첿��
*  ��������:2019-8-19
* @return nc.vo.org.DeptVO
*/
public java.lang.String getPk_deptid_v() {
return this.pk_deptid_v;
} 

/**
* ����pk_deptid_v��Setter����.�����������첿��
* ��������:2019-8-19
* @param newPk_deptid_v nc.vo.org.DeptVO
*/
public void setPk_deptid_v ( java.lang.String pk_deptid_v) {
this.pk_deptid_v=pk_deptid_v;
} 
 
/**
* ���� pk_psndoc��Getter����.��������������
*  ��������:2019-8-19
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getPk_psndoc() {
return this.pk_psndoc;
} 

/**
* ����pk_psndoc��Setter����.��������������
* ��������:2019-8-19
* @param newPk_psndoc nc.vo.bd.psn.PsndocVO
*/
public void setPk_psndoc ( java.lang.String pk_psndoc) {
this.pk_psndoc=pk_psndoc;
} 
 
/**
* ���� objtype��Getter����.����������������
*  ��������:2019-8-19
* @return nc.voarap.arapgl.ASSO_OBJ
*/
public java.lang.String getObjtype() {
return this.objtype;
} 

/**
* ����objtype��Setter����.����������������
* ��������:2019-8-19
* @param newObjtype nc.voarap.arapgl.ASSO_OBJ
*/
public void setObjtype ( java.lang.String objtype) {
this.objtype=objtype;
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
* ���� checktype��Getter����.��������Ʊ������
*  ��������:2019-8-19
* @return nc.vo.bd.notetype.NotetypeVO
*/
public java.lang.String getChecktype() {
return this.checktype;
} 

/**
* ����checktype��Setter����.��������Ʊ������
* ��������:2019-8-19
* @param newChecktype nc.vo.bd.notetype.NotetypeVO
*/
public void setChecktype ( java.lang.String checktype) {
this.checktype=checktype;
} 
 
/**
* ���� billstatus��Getter����.������������״̬
*  ��������:2019-8-19
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getBillstatus() {
return this.billstatus;
} 

/**
* ����billstatus��Setter����.������������״̬
* ��������:2019-8-19
* @param newBillstatus nc.vo.pub.pf.BillStatusEnum
*/
public void setBillstatus ( java.lang.Integer billstatus) {
this.billstatus=billstatus;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-8-19
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-8-19
* @param newApprovestatus nc.vo.arap.pub.BillEnumCollection
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� effectstatus��Getter����.����������Ч״̬
*  ��������:2019-8-19
* @return nc.vo.cmp.bill.Effectflag
*/
public java.lang.Integer getEffectstatus() {
return this.effectstatus;
} 

/**
* ����effectstatus��Setter����.����������Ч״̬
* ��������:2019-8-19
* @param newEffectstatus nc.vo.cmp.bill.Effectflag
*/
public void setEffectstatus ( java.lang.Integer effectstatus) {
this.effectstatus=effectstatus;
} 
 
/**
* ���� pk_busitype��Getter����.��������ҵ������
*  ��������:2019-8-19
* @return nc.vo.pf.pub.BusitypeVO
*/
public java.lang.String getPk_busitype() {
return this.pk_busitype;
} 

/**
* ����pk_busitype��Setter����.��������ҵ������
* ��������:2019-8-19
* @param newPk_busitype nc.vo.pf.pub.BusitypeVO
*/
public void setPk_busitype ( java.lang.String pk_busitype) {
this.pk_busitype=pk_busitype;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-8-19
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-8-19
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.������������޸���
*  ��������:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.������������޸���
* ��������:2019-8-19
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.������������޸�ʱ��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.������������޸�ʱ��
* ��������:2019-8-19
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-8-19
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-8-19
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-8-19
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-8-19
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-8-19
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-8-19
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-8-19
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-8-19
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-8-19
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-8-19
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-8-19
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� def1��Getter����.����������Ӧ��ͬ���
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.����������Ӧ��ͬ���
* ��������:2019-8-19
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.����������Ʊ��˰���ϼ�
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.����������Ʊ��˰���ϼ�
* ��������:2019-8-19
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.�����������ز���Ŀ
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.�����������ز���Ŀ
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
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-8-19
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-8-19
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-8-19
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-8-19
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-8-19
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-8-19
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
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
    return VOMetaFactory.getInstance().getVOMeta("tg.InvoiceBillVO");
    }
   }
    