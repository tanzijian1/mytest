package nc.vo.tg.tartingbill;

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
 *  ��������:2019-9-6
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class TartingBillVO extends SuperVO {
	
/**
*̢��������������
*/
public java.lang.String pk_tartingbill_h;
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
*���ݺ�
*/
public java.lang.String billno;
/**
*ҵ������
*/
public java.lang.String busitype;
/**
*ҵ������
*/
public java.lang.String pk_busitype;
/**
*����״̬
*/
public java.lang.String billstatus;
/**
*�Ƶ���
*/
public java.lang.String billmaker;
/**
*������
*/
public java.lang.String approver;
/**
*����״̬
*/
public java.lang.Integer approvestatus;
/**
*��������
*/
public java.lang.String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��Ч״̬
*/
public java.lang.Integer effectstatus;
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
*����ȷ����
*/
public java.lang.String confirmuser;
/**
*��Ч����
*/
public UFDateTime effectdate;
/**
*��������
*/
public UFDate billdate;
/**
*֧����
*/
public java.lang.String payman;
/**
*֧������
*/
public UFDateTime paydate;
/**
*ǩ����
*/
public java.lang.String signuser;
/**
*ǩ������
*/
public UFDateTime signdate;
/**
*��ʽ��ӡ��
*/
public java.lang.String officialprintuser;
/**
*��ʽ��ӡ����
*/
public UFDateTime officialprintdate;
/**
*������Դϵͳ
*/
public java.lang.Integer src_syscode;
/**
*ֱ�ӽ���˻���
*/
public java.lang.String sddreversaler;
/**
*ֱ�ӽ���˻�����
*/
public UFDateTime sddreversaldate;
/**
*����ϵͳ����
*/
public java.lang.String def1;
/**
*Ӱ�����
*/
public java.lang.String def2;
/**
*Ӱ��״̬
*/
public java.lang.String def3;
/**
*��ע
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
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_tartingbill_h��Getter����.��������̢��������������
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getPk_tartingbill_h() {
return this.pk_tartingbill_h;
} 

/**
* ����pk_tartingbill_h��Setter����.��������̢��������������
* ��������:2019-9-6
* @param newPk_tartingbill_h java.lang.String
*/
public void setPk_tartingbill_h ( java.lang.String pk_tartingbill_h) {
this.pk_tartingbill_h=pk_tartingbill_h;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-9-6
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-9-6
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-9-6
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-9-6
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-9-6
* @return nc.vo.vorg.FinanceOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-9-6
* @param newPk_org_v nc.vo.vorg.FinanceOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-9-6
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-9-6
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.������������޸���
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.������������޸���
* ��������:2019-9-6
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.������������޸�ʱ��
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.������������޸�ʱ��
* ��������:2019-9-6
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-9-6
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-9-6
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� pk_busitype��Getter����.��������ҵ������
*  ��������:2019-9-6
* @return nc.vo.pf.pub.BusitypeVO
*/
public java.lang.String getPk_busitype() {
return this.pk_busitype;
} 

/**
* ����pk_busitype��Setter����.��������ҵ������
* ��������:2019-9-6
* @param newPk_busitype nc.vo.pf.pub.BusitypeVO
*/
public void setPk_busitype ( java.lang.String pk_busitype) {
this.pk_busitype=pk_busitype;
} 
 
/**
* ���� billstatus��Getter����.������������״̬
*  ��������:2019-9-6
* @return nc.voarap.arapgl.BILL_STATUS
*/
public java.lang.String getBillstatus() {
return this.billstatus;
} 

/**
* ����billstatus��Setter����.������������״̬
* ��������:2019-9-6
* @param newBillstatus nc.voarap.arapgl.BILL_STATUS
*/
public void setBillstatus ( java.lang.String billstatus) {
this.billstatus=billstatus;
} 
 
/**
* ���� billmaker��Getter����.���������Ƶ���
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* ����billmaker��Setter����.���������Ƶ���
* ��������:2019-9-6
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-9-6
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-9-6
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-9-6
* @param newApprovestatus nc.vo.arap.pub.BillEnumCollection
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-9-6
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-9-6
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� effectstatus��Getter����.����������Ч״̬
*  ��������:2019-9-6
* @return nc.vo.cmp.bill.Effectflag
*/
public java.lang.Integer getEffectstatus() {
return this.effectstatus;
} 

/**
* ����effectstatus��Setter����.����������Ч״̬
* ��������:2019-9-6
* @param newEffectstatus nc.vo.cmp.bill.Effectflag
*/
public void setEffectstatus ( java.lang.Integer effectstatus) {
this.effectstatus=effectstatus;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-9-6
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-9-6
* @param newTranstype nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-9-6
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-9-6
* @param newBilltype nc.vo.pub.billtype.BilltypeVO
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-9-6
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� confirmuser��Getter����.������������ȷ����
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getConfirmuser() {
return this.confirmuser;
} 

/**
* ����confirmuser��Setter����.������������ȷ����
* ��������:2019-9-6
* @param newConfirmuser nc.vo.sm.UserVO
*/
public void setConfirmuser ( java.lang.String confirmuser) {
this.confirmuser=confirmuser;
} 
 
/**
* ���� effectdate��Getter����.����������Ч����
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getEffectdate() {
return this.effectdate;
} 

/**
* ����effectdate��Setter����.����������Ч����
* ��������:2019-9-6
* @param newEffectdate nc.vo.pub.lang.UFDateTime
*/
public void setEffectdate ( UFDateTime effectdate) {
this.effectdate=effectdate;
} 
 
/**
* ���� billdate��Getter����.����������������
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* ����billdate��Setter����.����������������
* ��������:2019-9-6
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* ���� payman��Getter����.��������֧����
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getPayman() {
return this.payman;
} 

/**
* ����payman��Setter����.��������֧����
* ��������:2019-9-6
* @param newPayman nc.vo.sm.UserVO
*/
public void setPayman ( java.lang.String payman) {
this.payman=payman;
} 
 
/**
* ���� paydate��Getter����.��������֧������
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getPaydate() {
return this.paydate;
} 

/**
* ����paydate��Setter����.��������֧������
* ��������:2019-9-6
* @param newPaydate nc.vo.pub.lang.UFDateTime
*/
public void setPaydate ( UFDateTime paydate) {
this.paydate=paydate;
} 
 
/**
* ���� signuser��Getter����.��������ǩ����
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getSignuser() {
return this.signuser;
} 

/**
* ����signuser��Setter����.��������ǩ����
* ��������:2019-9-6
* @param newSignuser nc.vo.sm.UserVO
*/
public void setSignuser ( java.lang.String signuser) {
this.signuser=signuser;
} 
 
/**
* ���� signdate��Getter����.��������ǩ������
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSigndate() {
return this.signdate;
} 

/**
* ����signdate��Setter����.��������ǩ������
* ��������:2019-9-6
* @param newSigndate nc.vo.pub.lang.UFDateTime
*/
public void setSigndate ( UFDateTime signdate) {
this.signdate=signdate;
} 
 
/**
* ���� officialprintuser��Getter����.����������ʽ��ӡ��
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getOfficialprintuser() {
return this.officialprintuser;
} 

/**
* ����officialprintuser��Setter����.����������ʽ��ӡ��
* ��������:2019-9-6
* @param newOfficialprintuser nc.vo.sm.UserVO
*/
public void setOfficialprintuser ( java.lang.String officialprintuser) {
this.officialprintuser=officialprintuser;
} 
 
/**
* ���� officialprintdate��Getter����.����������ʽ��ӡ����
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getOfficialprintdate() {
return this.officialprintdate;
} 

/**
* ����officialprintdate��Setter����.����������ʽ��ӡ����
* ��������:2019-9-6
* @param newOfficialprintdate nc.vo.pub.lang.UFDateTime
*/
public void setOfficialprintdate ( UFDateTime officialprintdate) {
this.officialprintdate=officialprintdate;
} 
 
/**
* ���� src_syscode��Getter����.��������������Դϵͳ
*  ��������:2019-9-6
* @return nc.vo.arap.pub.BillEnumCollection
*/
public java.lang.Integer getSrc_syscode() {
return this.src_syscode;
} 

/**
* ����src_syscode��Setter����.��������������Դϵͳ
* ��������:2019-9-6
* @param newSrc_syscode nc.vo.arap.pub.BillEnumCollection
*/
public void setSrc_syscode ( java.lang.Integer src_syscode) {
this.src_syscode=src_syscode;
} 
 
/**
* ���� sddreversaler��Getter����.��������ֱ�ӽ���˻���
*  ��������:2019-9-6
* @return nc.vo.sm.UserVO
*/
public java.lang.String getSddreversaler() {
return this.sddreversaler;
} 

/**
* ����sddreversaler��Setter����.��������ֱ�ӽ���˻���
* ��������:2019-9-6
* @param newSddreversaler nc.vo.sm.UserVO
*/
public void setSddreversaler ( java.lang.String sddreversaler) {
this.sddreversaler=sddreversaler;
} 
 
/**
* ���� sddreversaldate��Getter����.��������ֱ�ӽ���˻�����
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getSddreversaldate() {
return this.sddreversaldate;
} 

/**
* ����sddreversaldate��Setter����.��������ֱ�ӽ���˻�����
* ��������:2019-9-6
* @param newSddreversaldate nc.vo.pub.lang.UFDateTime
*/
public void setSddreversaldate ( UFDateTime sddreversaldate) {
this.sddreversaldate=sddreversaldate;
} 
 
/**
* ���� def1��Getter����.������������ϵͳ����
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.������������ϵͳ����
* ��������:2019-9-6
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.��������Ӱ�����
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.��������Ӱ�����
* ��������:2019-9-6
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.��������Ӱ��״̬
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.��������Ӱ��״̬
* ��������:2019-9-6
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.����������ע
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.����������ע
* ��������:2019-9-6
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-9-6
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-9-6
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-9-6
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-9-6
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-9-6
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-9-6
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-9-6
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-9-6
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-9-6
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-9-6
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-9-6
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-9-6
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-9-6
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-9-6
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-9-6
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-9-6
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* ���� def21��Getter����.���������Զ�����21
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef21() {
return this.def21;
} 

/**
* ����def21��Setter����.���������Զ�����21
* ��������:2019-9-6
* @param newDef21 java.lang.String
*/
public void setDef21 ( java.lang.String def21) {
this.def21=def21;
} 
 
/**
* ���� def22��Getter����.���������Զ�����22
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef22() {
return this.def22;
} 

/**
* ����def22��Setter����.���������Զ�����22
* ��������:2019-9-6
* @param newDef22 java.lang.String
*/
public void setDef22 ( java.lang.String def22) {
this.def22=def22;
} 
 
/**
* ���� def23��Getter����.���������Զ�����23
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef23() {
return this.def23;
} 

/**
* ����def23��Setter����.���������Զ�����23
* ��������:2019-9-6
* @param newDef23 java.lang.String
*/
public void setDef23 ( java.lang.String def23) {
this.def23=def23;
} 
 
/**
* ���� def24��Getter����.���������Զ�����24
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef24() {
return this.def24;
} 

/**
* ����def24��Setter����.���������Զ�����24
* ��������:2019-9-6
* @param newDef24 java.lang.String
*/
public void setDef24 ( java.lang.String def24) {
this.def24=def24;
} 
 
/**
* ���� def25��Getter����.���������Զ�����25
*  ��������:2019-9-6
* @return java.lang.String
*/
public java.lang.String getDef25() {
return this.def25;
} 

/**
* ����def25��Setter����.���������Զ�����25
* ��������:2019-9-6
* @param newDef25 java.lang.String
*/
public void setDef25 ( java.lang.String def25) {
this.def25=def25;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-9-6
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-9-6
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
    