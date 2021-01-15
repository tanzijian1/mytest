package nc.vo.tg.annualfinancingloan;

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
 *  ��������:2019-6-12
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class AnnualFinancingLoanVO extends SuperVO {
	
/**
*������ʷſ������
*/
public java.lang.String pk_annfinloan;
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
*����ID
*/
public java.lang.String billid;
/**
*���ݺ�
*/
public java.lang.String billno;
/**
*������֯
*/
public java.lang.String pkorg;
/**
*ҵ������
*/
public java.lang.String busitype;
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
public UFDate approvedate;
/**
*��������
*/
public java.lang.String transtype;
/**
*��������
*/
public java.lang.Integer billtype;
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
*�޶�ö��
*/
public java.lang.Integer emendenum;
/**
*���ݰ汾pk
*/
public java.lang.String billversionpk;
/**
*��������
*/
public java.lang.String pk_plan;
/**
*������
*/
public java.lang.String creator;
/**
*����ʱ��
*/
public UFDate creationtime;
/**
*�޸���
*/
public java.lang.String modifier;
/**
*�޸�ʱ��
*/
public UFDate modifiedtime;
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
*��������
*/
public UFDate dbilldate;
/**
*���
*/
public java.lang.String cyear;
/**
*��Ŀ����
*/
public java.lang.String projectname;
/**
*��Ŀ��˾����
*/
public java.lang.String org_name;
/**
*�����
*/
public java.lang.String borrower;
/**
*��������
*/
public java.lang.String financing_type;
/**
*���ʻ���
*/
public java.lang.String financing_org;
/**
*���ʻ������
*/
public java.lang.String financing_orgtype;
/**
*��������
*/
public java.lang.String branch;
/**
*��ͬ��ʼ��
*/
public UFDate begindate;
/**
*��ͬ������
*/
public UFDate enddate;
/**
*���ʽ��
*/
public nc.vo.pub.lang.UFDouble nfinancing_money;
/**
*�ۼƷſ���
*/
public nc.vo.pub.lang.UFDouble totalamount;
/**
*��һ��ſ���
*/
public nc.vo.pub.lang.UFDouble beforeyearamount;
/**
*����ſ���
*/
public nc.vo.pub.lang.UFDouble yearamount;
/**
*���ſ���
*/
public nc.vo.pub.lang.UFDouble prepareamount;
/**
*��ͬ����
*/
public java.lang.String contractrate;
/**
*�ſ�ʱ��
*/
public java.lang.String loantime;
/**
*1��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble january;
/**
*2��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble february;
/**
*3��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble march;
/**
*4��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble april;
/**
*5��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble may;
/**
*6��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble june;
/**
*7��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble july;
/**
*8��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble august;
/**
*9��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble september;
/**
*10��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble october;
/**
*11��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble november;
/**
*12��ʵ�ʷſ�
*/
public nc.vo.pub.lang.UFDouble december;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_annfinloan��Getter����.��������������ʷſ������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPk_annfinloan() {
return this.pk_annfinloan;
} 

/**
* ����pk_annfinloan��Setter����.��������������ʷſ������
* ��������:2019-6-12
* @param newPk_annfinloan java.lang.String
*/
public void setPk_annfinloan ( java.lang.String pk_annfinloan) {
this.pk_annfinloan=pk_annfinloan;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-6-12
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-6-12
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-6-12
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-6-12
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-6-12
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-6-12
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� billid��Getter����.������������ID
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.������������ID
* ��������:2019-6-12
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-6-12
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* ���� pkorg��Getter����.��������������֯
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* ����pkorg��Setter����.��������������֯
* ��������:2019-6-12
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-6-12
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� billmaker��Getter����.���������Ƶ���
*  ��������:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* ����billmaker��Setter����.���������Ƶ���
* ��������:2019-6-12
* @param newBillmaker nc.vo.sm.UserVO
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-6-12
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-6-12
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-6-12
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-6-12
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-6-12
* @param newApprovedate nc.vo.pub.lang.UFDate
*/
public void setApprovedate ( UFDate approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-6-12
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-6-12
* @return java.lang.Integer
*/
public java.lang.Integer getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-6-12
* @param newBilltype java.lang.Integer
*/
public void setBilltype ( java.lang.Integer billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-6-12
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-6-12
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-6-12
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� emendenum��Getter����.���������޶�ö��
*  ��������:2019-6-12
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������޶�ö��
* ��������:2019-6-12
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� billversionpk��Getter����.�����������ݰ汾pk
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* ����billversionpk��Setter����.�����������ݰ汾pk
* ��������:2019-6-12
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* ���� pk_plan��Getter����.����������������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getPk_plan() {
return this.pk_plan;
} 

/**
* ����pk_plan��Setter����.����������������
* ��������:2019-6-12
* @param newPk_plan java.lang.String
*/
public void setPk_plan ( java.lang.String pk_plan) {
this.pk_plan=pk_plan;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-6-12
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-6-12
* @param newCreationtime nc.vo.pub.lang.UFDate
*/
public void setCreationtime ( UFDate creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-6-12
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-6-12
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-6-12
* @param newModifiedtime nc.vo.pub.lang.UFDate
*/
public void setModifiedtime ( UFDate modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-6-12
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-6-12
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-6-12
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-6-12
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-6-12
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-6-12
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-6-12
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-6-12
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-6-12
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-6-12
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-6-12
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-6-12
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-6-12
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-6-12
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-6-12
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-6-12
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-6-12
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-6-12
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-6-12
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-6-12
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* ���� dbilldate��Getter����.����������������
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* ����dbilldate��Setter����.����������������
* ��������:2019-6-12
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* ���� cyear��Getter����.�����������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getCyear() {
return this.cyear;
} 

/**
* ����cyear��Setter����.�����������
* ��������:2019-6-12
* @param newCyear java.lang.String
*/
public void setCyear ( java.lang.String cyear) {
this.cyear=cyear;
} 
 
/**
* ���� projectname��Getter����.����������Ŀ����
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getProjectname() {
return this.projectname;
} 

/**
* ����projectname��Setter����.����������Ŀ����
* ��������:2019-6-12
* @param newProjectname java.lang.String
*/
public void setProjectname ( java.lang.String projectname) {
this.projectname=projectname;
} 
 
/**
* ���� org_name��Getter����.����������Ŀ��˾����
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getOrg_name() {
return this.org_name;
} 

/**
* ����org_name��Setter����.����������Ŀ��˾����
* ��������:2019-6-12
* @param newOrg_name java.lang.String
*/
public void setOrg_name ( java.lang.String org_name) {
this.org_name=org_name;
} 
 
/**
* ���� borrower��Getter����.�������������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBorrower() {
return this.borrower;
} 

/**
* ����borrower��Setter����.�������������
* ��������:2019-6-12
* @param newBorrower java.lang.String
*/
public void setBorrower ( java.lang.String borrower) {
this.borrower=borrower;
} 
 
/**
* ���� financing_type��Getter����.����������������
*  ��������:2019-6-12
* @return nc.vo.tg.fintype.FinTypeVO
*/
public java.lang.String getFinancing_type() {
return this.financing_type;
} 

/**
* ����financing_type��Setter����.����������������
* ��������:2019-6-12
* @param newFinancing_type nc.vo.tg.fintype.FinTypeVO
*/
public void setFinancing_type ( java.lang.String financing_type) {
this.financing_type=financing_type;
} 
 
/**
* ���� financing_org��Getter����.�����������ʻ���
*  ��������:2019-6-12
* @return nc.vo.tg.organization.OrganizationVO
*/
public java.lang.String getFinancing_org() {
return this.financing_org;
} 

/**
* ����financing_org��Setter����.�����������ʻ���
* ��������:2019-6-12
* @param newFinancing_org nc.vo.tg.organization.OrganizationVO
*/
public void setFinancing_org ( java.lang.String financing_org) {
this.financing_org=financing_org;
} 
 
/**
* ���� financing_orgtype��Getter����.�����������ʻ������
*  ��������:2019-6-12
* @return nc.vo.tg.organizationtype.OrganizationTypeVO
*/
public java.lang.String getFinancing_orgtype() {
return this.financing_orgtype;
} 

/**
* ����financing_orgtype��Setter����.�����������ʻ������
* ��������:2019-6-12
* @param newFinancing_orgtype nc.vo.tg.organizationtype.OrganizationTypeVO
*/
public void setFinancing_orgtype ( java.lang.String financing_orgtype) {
this.financing_orgtype=financing_orgtype;
} 
 
/**
* ���� branch��Getter����.����������������
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getBranch() {
return this.branch;
} 

/**
* ����branch��Setter����.����������������
* ��������:2019-6-12
* @param newBranch java.lang.String
*/
public void setBranch ( java.lang.String branch) {
this.branch=branch;
} 
 
/**
* ���� begindate��Getter����.����������ͬ��ʼ��
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBegindate() {
return this.begindate;
} 

/**
* ����begindate��Setter����.����������ͬ��ʼ��
* ��������:2019-6-12
* @param newBegindate nc.vo.pub.lang.UFDate
*/
public void setBegindate ( UFDate begindate) {
this.begindate=begindate;
} 
 
/**
* ���� enddate��Getter����.����������ͬ������
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getEnddate() {
return this.enddate;
} 

/**
* ����enddate��Setter����.����������ͬ������
* ��������:2019-6-12
* @param newEnddate nc.vo.pub.lang.UFDate
*/
public void setEnddate ( UFDate enddate) {
this.enddate=enddate;
} 
 
/**
* ���� nfinancing_money��Getter����.�����������ʽ��
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNfinancing_money() {
return this.nfinancing_money;
} 

/**
* ����nfinancing_money��Setter����.�����������ʽ��
* ��������:2019-6-12
* @param newNfinancing_money nc.vo.pub.lang.UFDouble
*/
public void setNfinancing_money ( nc.vo.pub.lang.UFDouble nfinancing_money) {
this.nfinancing_money=nfinancing_money;
} 
 
/**
* ���� totalamount��Getter����.���������ۼƷſ���
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTotalamount() {
return this.totalamount;
} 

/**
* ����totalamount��Setter����.���������ۼƷſ���
* ��������:2019-6-12
* @param newTotalamount nc.vo.pub.lang.UFDouble
*/
public void setTotalamount ( nc.vo.pub.lang.UFDouble totalamount) {
this.totalamount=totalamount;
} 
 
/**
* ���� beforeyearamount��Getter����.����������һ��ſ���
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getBeforeyearamount() {
return this.beforeyearamount;
} 

/**
* ����beforeyearamount��Setter����.����������һ��ſ���
* ��������:2019-6-12
* @param newBeforeyearamount nc.vo.pub.lang.UFDouble
*/
public void setBeforeyearamount ( nc.vo.pub.lang.UFDouble beforeyearamount) {
this.beforeyearamount=beforeyearamount;
} 
 
/**
* ���� yearamount��Getter����.������������ſ���
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getYearamount() {
return this.yearamount;
} 

/**
* ����yearamount��Setter����.������������ſ���
* ��������:2019-6-12
* @param newYearamount nc.vo.pub.lang.UFDouble
*/
public void setYearamount ( nc.vo.pub.lang.UFDouble yearamount) {
this.yearamount=yearamount;
} 
 
/**
* ���� prepareamount��Getter����.�����������ſ���
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getPrepareamount() {
return this.prepareamount;
} 

/**
* ����prepareamount��Setter����.�����������ſ���
* ��������:2019-6-12
* @param newPrepareamount nc.vo.pub.lang.UFDouble
*/
public void setPrepareamount ( nc.vo.pub.lang.UFDouble prepareamount) {
this.prepareamount=prepareamount;
} 
 
/**
* ���� contractrate��Getter����.����������ͬ����
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getContractrate() {
return this.contractrate;
} 

/**
* ����contractrate��Setter����.����������ͬ����
* ��������:2019-6-12
* @param newContractrate java.lang.String
*/
public void setContractrate ( java.lang.String contractrate) {
this.contractrate=contractrate;
} 
 
/**
* ���� loantime��Getter����.���������ſ�ʱ��
*  ��������:2019-6-12
* @return java.lang.String
*/
public java.lang.String getLoantime() {
return this.loantime;
} 

/**
* ����loantime��Setter����.���������ſ�ʱ��
* ��������:2019-6-12
* @param newLoantime java.lang.String
*/
public void setLoantime ( java.lang.String loantime) {
this.loantime=loantime;
} 
 
/**
* ���� january��Getter����.��������1��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJanuary() {
return this.january;
} 

/**
* ����january��Setter����.��������1��ʵ�ʷſ�
* ��������:2019-6-12
* @param newJanuary nc.vo.pub.lang.UFDouble
*/
public void setJanuary ( nc.vo.pub.lang.UFDouble january) {
this.january=january;
} 
 
/**
* ���� february��Getter����.��������2��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getFebruary() {
return this.february;
} 

/**
* ����february��Setter����.��������2��ʵ�ʷſ�
* ��������:2019-6-12
* @param newFebruary nc.vo.pub.lang.UFDouble
*/
public void setFebruary ( nc.vo.pub.lang.UFDouble february) {
this.february=february;
} 
 
/**
* ���� march��Getter����.��������3��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMarch() {
return this.march;
} 

/**
* ����march��Setter����.��������3��ʵ�ʷſ�
* ��������:2019-6-12
* @param newMarch nc.vo.pub.lang.UFDouble
*/
public void setMarch ( nc.vo.pub.lang.UFDouble march) {
this.march=march;
} 
 
/**
* ���� april��Getter����.��������4��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getApril() {
return this.april;
} 

/**
* ����april��Setter����.��������4��ʵ�ʷſ�
* ��������:2019-6-12
* @param newApril nc.vo.pub.lang.UFDouble
*/
public void setApril ( nc.vo.pub.lang.UFDouble april) {
this.april=april;
} 
 
/**
* ���� may��Getter����.��������5��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getMay() {
return this.may;
} 

/**
* ����may��Setter����.��������5��ʵ�ʷſ�
* ��������:2019-6-12
* @param newMay nc.vo.pub.lang.UFDouble
*/
public void setMay ( nc.vo.pub.lang.UFDouble may) {
this.may=may;
} 
 
/**
* ���� june��Getter����.��������6��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJune() {
return this.june;
} 

/**
* ����june��Setter����.��������6��ʵ�ʷſ�
* ��������:2019-6-12
* @param newJune nc.vo.pub.lang.UFDouble
*/
public void setJune ( nc.vo.pub.lang.UFDouble june) {
this.june=june;
} 
 
/**
* ���� july��Getter����.��������7��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getJuly() {
return this.july;
} 

/**
* ����july��Setter����.��������7��ʵ�ʷſ�
* ��������:2019-6-12
* @param newJuly nc.vo.pub.lang.UFDouble
*/
public void setJuly ( nc.vo.pub.lang.UFDouble july) {
this.july=july;
} 
 
/**
* ���� august��Getter����.��������8��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getAugust() {
return this.august;
} 

/**
* ����august��Setter����.��������8��ʵ�ʷſ�
* ��������:2019-6-12
* @param newAugust nc.vo.pub.lang.UFDouble
*/
public void setAugust ( nc.vo.pub.lang.UFDouble august) {
this.august=august;
} 
 
/**
* ���� september��Getter����.��������9��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getSeptember() {
return this.september;
} 

/**
* ����september��Setter����.��������9��ʵ�ʷſ�
* ��������:2019-6-12
* @param newSeptember nc.vo.pub.lang.UFDouble
*/
public void setSeptember ( nc.vo.pub.lang.UFDouble september) {
this.september=september;
} 
 
/**
* ���� october��Getter����.��������10��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getOctober() {
return this.october;
} 

/**
* ����october��Setter����.��������10��ʵ�ʷſ�
* ��������:2019-6-12
* @param newOctober nc.vo.pub.lang.UFDouble
*/
public void setOctober ( nc.vo.pub.lang.UFDouble october) {
this.october=october;
} 
 
/**
* ���� november��Getter����.��������11��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getNovember() {
return this.november;
} 

/**
* ����november��Setter����.��������11��ʵ�ʷſ�
* ��������:2019-6-12
* @param newNovember nc.vo.pub.lang.UFDouble
*/
public void setNovember ( nc.vo.pub.lang.UFDouble november) {
this.november=november;
} 
 
/**
* ���� december��Getter����.��������12��ʵ�ʷſ�
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getDecember() {
return this.december;
} 

/**
* ����december��Setter����.��������12��ʵ�ʷſ�
* ��������:2019-6-12
* @param newDecember nc.vo.pub.lang.UFDouble
*/
public void setDecember ( nc.vo.pub.lang.UFDouble december) {
this.december=december;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-6-12
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-6-12
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.AnnualFinancingLoan");
    }
   }
    