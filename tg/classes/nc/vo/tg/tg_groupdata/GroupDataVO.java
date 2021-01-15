package nc.vo.tg.tg_groupdata;

import java.io.Serializable;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * <b> �˴���Ҫ�������๦�� </b>
 * <p>
 *   �˴�����۵�������Ϣ
 * </p>
 *  ��������:2019-7-17
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class GroupDataVO extends SuperVO implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6199533332603520045L;
	public static final String PK_GROUPDATA = "pk_groupdata";// ����
	public static final String PK_GROUP = "pk_group";// ����
	public static final String PK_ORG = "pk_org";// ��֯
	public static final String PK_ORG_V = "pk_org_v";// ��֯�汾
	public static final String PROJECTNAME = "projectname";// ��Ŀ����
	public static final String PROJECTSTAGING = "projectstaging";// ��Ŀ����
	public static final String PROJECTDATATYPE = "projectdatatype";// ��Ŀ�������
	public static final String FINANCIALOFFICER = "financialofficer";// ��������
	public static final String BILLID = "billid";// ����ID
	public static final String BILLNO = "billno";// ���ݺ�
	public static final String BUSITYPE0 = "busitype0";// ҵ������
	public static final String TRANSTYPE0 = "transtype0";// ��������
	public static final String BILLTYPE = "billtype";// ��������
	public static final String TRANSTYPEPK = "transtypepk";// ��������pk
	public static final String SRCBILLTYPE = "srcbilltype";// ��Դ��������
	public static final String SRCBILLID = "srcbillid";// ��Դ����id
	public static final String BILLVERSIONPK = "billversionpk";// ���ݰ汾pk
	public static final String BILLMAKER = "billmaker";// �Ƶ���
	public static final String APPROVER = "approver";// ������
	public static final String APPROVESTATUS = "approvestatus";// ����״̬
	public static final String APPROVENOTE = "approvenote";// ��������
	public static final String APPROVEDATE = "approvedate";// ����ʱ��
	public static final String EMENDENUM = "emendenum";// �޶�ö��
	public static final String PK_CASHIER = "pk_cashier";// ����
	public static final String EXTENSION = "extension";// Ͷ��
	public static final String DEVELOPMENT = "development";// ����
	public static final String MARKETDEPT = "marketdept";// �г�����
	public static final String ENABLESTATE = "enablestate";// ����״̬
	public static final String CREATOR = "creator";// ������
	public static final String CREATIONTIME = "creationtime";// ����ʱ��
	public static final String MODIFIER = "modifier";// �޸���
	public static final String MODIFIEDTIME = "modifiedtime";// �޸�ʱ��
	public static final String GROUPYEAR = "groupyear";// �޸�ʱ��
	public static final String DEF1 = "def1";// �Զ�����1
	public static final String DEF2 = "def2";// �Զ�����2
	public static final String DEF3 = "def3";// �Զ�����3
	public static final String DEF4 = "def4";// �Զ�����4
	public static final String DEF5 = "def5";// �Զ�����5
	public static final String DEF6 = "def6";// �Զ�����6
	public static final String DEF7 = "def7";// �Զ�����7
	public static final String DEF8 = "def8";// �Զ�����8
	public static final String DEF9 = "def9";// �Զ�����9
	public static final String DEF10 = "def10";// �Զ�����10
	public static final String DEF11 = "def11";// �Զ�����11
	public static final String DEF12 = "def12";// �Զ�����12
	public static final String DEF13 = "def13";// �Զ�����13
	public static final String DEF14 = "def14";// �Զ�����14
	public static final String DEF15 = "def15";// �Զ�����15
	public static final String DEF16 = "def16";// �Զ�����16
	public static final String DEF17 = "def17";// �Զ�����17
	public static final String DEF18 = "def18";// �Զ�����18
	public static final String DEF19 = "def19";// �Զ�����19
	public static final String DEF20 = "def20";// �Զ�����20
	public static final String DEF21 = "def21";// �Զ�����21
	public static final String DEF22 = "def22";// �Զ�����22
	public static final String DEF23 = "def23";// �Զ�����23
	public static final String DEF24 = "def24";// �Զ�����24
	public static final String DEF25 = "def25";// �Զ�����25
	public static final String DEF26 = "def26";// �Զ�����26
	public static final String DEF27 = "def27";// �Զ�����27
	public static final String DEF28 = "def28";// �Զ�����28
	public static final String DEF29 = "def29";// �Զ�����29
	public static final String DEF30 = "def30";// �Զ�����30
	public static final String BILLDATE = "billdate";// ҵ������
	
/**
*����
*/
public String pk_groupdata;
/**
*����
*/
public String pk_group;
/**
*��֯
*/
public String pk_org;
/**
*��֯�汾
*/
public String pk_org_v;
/**
*��Ŀ����
*/
public String projectname;
/**
*��Ŀ����
*/
public String projectstaging;
/**
*��Ŀ�������
*/
public String projectdatatype;
/**
*��������
*/
public String financialofficer;
/**
*����ID
*/
public String billid;
/**
*���ݺ�
*/
public String billno;
/**
*��������
*/
public String billtype;
/**
*������
*/
public String approver;
/**
*����״̬
*/
public Integer approvestatus;
/**
*��������
*/
public String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*�޶�ö��
*/
public Integer emendenum;
/**
*����
*/
public String pk_cashier;
/**
*Ͷ��
*/
public String extension;
/**
*����
*/
public String development;
/**
*�г�����
*/
public String marketdept;
/**
*���
*/
public Integer groupyear;
/**
*����״̬
*/
public String enablestate;
/**
*�Զ�����1
*/
public String def1;
/**
*�Զ�����2
*/
public String def2;
/**
*�Զ�����3
*/
public String def3;
/**
*�Զ�����4
*/
public String def4;
/**
*�Զ�����5
*/
public String def5;
/**
*�Զ�����6
*/
public String def6;
/**
*�Զ�����7
*/
public String def7;
/**
*�Զ�����8
*/
public String def8;
/**
*�Զ�����9
*/
public String def9;
/**
*�Զ�����10
*/
public String def10;
/**
*�Զ�����11
*/
public String def11;
/**
*�Զ�����12
*/
public String def12;
/**
*�Զ�����13
*/
public String def13;
/**
*�Զ�����14
*/
public String def14;
/**
*�Զ�����15
*/
public String def15;
/**
*�Զ�����16
*/
public String def16;
/**
*�Զ�����17
*/
public String def17;
/**
*�Զ�����18
*/
public String def18;
/**
*�Զ�����19
*/
public String def19;
/**
*�Զ�����20
*/
public String def20;
/**
*�Զ�����21
*/
public String def21;
/**
*�Զ�����22
*/
public String def22;
/**
*�Զ�����23
*/
public String def23;
/**
*�Զ�����24
*/
public String def24;
/**
*�Զ�����25
*/
public String def25;
/**
*�Զ�����26
*/
public String def26;
/**
*�Զ�����27
*/
public String def27;
/**
*�Զ�����28
*/
public String def28;
/**
*�Զ�����29
*/
public String def29;
/**
*�Զ�����30
*/
public String def30;
/**
*ҵ��ʱ��
*/
public UFDate billdate;
/**
*������
*/
public String creator;
/**
*����ʱ��
*/
public UFDateTime creationtime;
/**
*�޸���
*/
public String modifier;
/**
*�޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_groupdata��Getter����.������������
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getPk_groupdata() {
return this.pk_groupdata;
} 

/**
* ����pk_groupdata��Setter����.������������
* ��������:2019-7-17
* @param newPk_groupdata java.lang.String
*/
public void setPk_groupdata ( String pk_groupdata) {
this.pk_groupdata=pk_groupdata;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-7-17
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-7-17
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-7-17
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-7-17
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-7-17
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-7-17
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� projectname��Getter����.����������Ŀ����
*  ��������:2019-7-17
* @return nc.vo.tg.projectdata.ProjectDataVO
*/
public String getProjectname() {
return this.projectname;
} 

/**
* ����projectname��Setter����.����������Ŀ����
* ��������:2019-7-17
* @param newProjectname nc.vo.tg.projectdata.ProjectDataVO
*/
public void setProjectname ( String projectname) {
this.projectname=projectname;
} 
 
/**
* ���� projectstaging��Getter����.����������Ŀ����
*  ��������:2019-7-17
* @return nc.vo.tg.projectdata.ProjectDataCVO
*/
public String getProjectstaging() {
return this.projectstaging;
} 

/**
* ����projectstaging��Setter����.����������Ŀ����
* ��������:2019-7-17
* @param newProjectstaging nc.vo.tg.projectdata.ProjectDataCVO
*/
public void setProjectstaging ( String projectstaging) {
this.projectstaging=projectstaging;
} 
 
/**
* ���� projectdatatype��Getter����.����������Ŀ�������
*  ��������:2019-7-17
* @return nc.vo.tg.datatype.DataTypeVO
*/
public String getProjectdatatype() {
return this.projectdatatype;
} 

/**
* ����projectdatatype��Setter����.����������Ŀ�������
* ��������:2019-7-17
* @param newProjectdatatype nc.vo.tg.datatype.DataTypeVO
*/
public void setProjectdatatype ( String projectdatatype) {
this.projectdatatype=projectdatatype;
} 
 
/**
* ���� financialofficer��Getter����.����������������
*  ��������:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getFinancialofficer() {
return this.financialofficer;
} 

/**
* ����financialofficer��Setter����.����������������
* ��������:2019-7-17
* @param newFinancialofficer nc.vo.bd.psn.PsndocVO
*/
public void setFinancialofficer ( String financialofficer) {
this.financialofficer=financialofficer;
} 
 
/**
* ���� billid��Getter����.������������ID
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.������������ID
* ��������:2019-7-17
* @param newBillid java.lang.String
*/
public void setBillid ( String billid) {
this.billid=billid;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-7-17
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-7-17
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-7-17
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-7-17
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-7-17
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-7-17
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-7-17
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� emendenum��Getter����.���������޶�ö��
*  ��������:2019-7-17
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������޶�ö��
* ��������:2019-7-17
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� pk_cashier��Getter����.������������
*  ��������:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getPk_cashier() {
return this.pk_cashier;
} 

/**
* ����pk_cashier��Setter����.������������
* ��������:2019-7-17
* @param newPk_cashier nc.vo.bd.psn.PsndocVO
*/
public void setPk_cashier ( String pk_cashier) {
this.pk_cashier=pk_cashier;
} 
 
/**
* ���� extension��Getter����.��������Ͷ��
*  ��������:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getExtension() {
return this.extension;
} 

/**
* ����extension��Setter����.��������Ͷ��
* ��������:2019-7-17
* @param newExtension nc.vo.bd.psn.PsndocVO
*/
public void setExtension ( String extension) {
this.extension=extension;
} 
 
/**
* ���� development��Getter����.������������
*  ��������:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getDevelopment() {
return this.development;
} 

/**
* ����development��Setter����.������������
* ��������:2019-7-17
* @param newDevelopment nc.vo.bd.psn.PsndocVO
*/
public void setDevelopment ( String development) {
this.development=development;
} 
 
/**
* ���� marketdept��Getter����.���������г�����
*  ��������:2019-7-17
* @return nc.vo.bd.psn.PsndocVO
*/
public String getMarketdept() {
return this.marketdept;
} 

/**
* ����marketdept��Setter����.���������г�����
* ��������:2019-7-17
* @param newMarketdept nc.vo.bd.psn.PsndocVO
*/
public void setMarketdept ( String marketdept) {
this.marketdept=marketdept;
} 
 

/**
* ���� groupyear��Getter����.�����������
*  ��������:2019-7-17
* @return nc.voufob.datasource.yearenum
*/
public java.lang.Integer  getGroupyear() {
return this.groupyear;
} 

/**
* ����groupyear��Setter����.�����������
* ��������:2019-7-17
* @param newGroupyear nc.voufob.datasource.yearenum
*/
public void setGroupyear (java.lang.Integer  groupyear) {
this.groupyear=groupyear;
} 
 
/**
* ���� enablestate��Getter����.������������״̬
*  ��������:2019-7-17
* @return nc.vo.bd.pub.EnableStateEnum
*/
public String getEnablestate() {
return this.enablestate;
} 

/**
* ����enablestate��Setter����.������������״̬
* ��������:2019-7-17
* @param newEnablestate nc.vo.bd.pub.EnableStateEnum
*/
public void setEnablestate ( String enablestate) {
this.enablestate=enablestate;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-7-17
* @param newDef1 java.lang.String
*/
public void setDef1 ( String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-7-17
* @param newDef2 java.lang.String
*/
public void setDef2 ( String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-7-17
* @param newDef3 java.lang.String
*/
public void setDef3 ( String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-7-17
* @param newDef4 java.lang.String
*/
public void setDef4 ( String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-7-17
* @param newDef5 java.lang.String
*/
public void setDef5 ( String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-7-17
* @param newDef6 java.lang.String
*/
public void setDef6 ( String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-7-17
* @param newDef7 java.lang.String
*/
public void setDef7 ( String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-7-17
* @param newDef8 java.lang.String
*/
public void setDef8 ( String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-7-17
* @param newDef9 java.lang.String
*/
public void setDef9 ( String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-7-17
* @param newDef10 java.lang.String
*/
public void setDef10 ( String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-7-17
* @param newDef11 java.lang.String
*/
public void setDef11 ( String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-7-17
* @param newDef12 java.lang.String
*/
public void setDef12 ( String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-7-17
* @param newDef13 java.lang.String
*/
public void setDef13 ( String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-7-17
* @param newDef14 java.lang.String
*/
public void setDef14 ( String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-7-17
* @param newDef15 java.lang.String
*/
public void setDef15 ( String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-7-17
* @param newDef16 java.lang.String
*/
public void setDef16 ( String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-7-17
* @param newDef17 java.lang.String
*/
public void setDef17 ( String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-7-17
* @param newDef18 java.lang.String
*/
public void setDef18 ( String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-7-17
* @param newDef19 java.lang.String
*/
public void setDef19 ( String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-7-17
* @param newDef20 java.lang.String
*/
public void setDef20 ( String def20) {
this.def20=def20;
} 
 
/**
* ���� def21��Getter����.���������Զ�����21
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef21() {
return this.def21;
} 

/**
* ����def21��Setter����.���������Զ�����21
* ��������:2019-7-17
* @param newDef21 java.lang.String
*/
public void setDef21 ( String def21) {
this.def21=def21;
} 
 
/**
* ���� def22��Getter����.���������Զ�����22
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef22() {
return this.def22;
} 

/**
* ����def22��Setter����.���������Զ�����22
* ��������:2019-7-17
* @param newDef22 java.lang.String
*/
public void setDef22 ( String def22) {
this.def22=def22;
} 
 
/**
* ���� def23��Getter����.���������Զ�����23
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef23() {
return this.def23;
} 

/**
* ����def23��Setter����.���������Զ�����23
* ��������:2019-7-17
* @param newDef23 java.lang.String
*/
public void setDef23 ( String def23) {
this.def23=def23;
} 
 
/**
* ���� def24��Getter����.���������Զ�����24
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef24() {
return this.def24;
} 

/**
* ����def24��Setter����.���������Զ�����24
* ��������:2019-7-17
* @param newDef24 java.lang.String
*/
public void setDef24 ( String def24) {
this.def24=def24;
} 
 
/**
* ���� def25��Getter����.���������Զ�����25
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef25() {
return this.def25;
} 

/**
* ����def25��Setter����.���������Զ�����25
* ��������:2019-7-17
* @param newDef25 java.lang.String
*/
public void setDef25 ( String def25) {
this.def25=def25;
} 
 
/**
* ���� def26��Getter����.���������Զ�����26
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef26() {
return this.def26;
} 

/**
* ����def26��Setter����.���������Զ�����26
* ��������:2019-7-17
* @param newDef26 java.lang.String
*/
public void setDef26 ( String def26) {
this.def26=def26;
} 
 
/**
* ���� def27��Getter����.���������Զ�����27
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef27() {
return this.def27;
} 

/**
* ����def27��Setter����.���������Զ�����27
* ��������:2019-7-17
* @param newDef27 java.lang.String
*/
public void setDef27 ( String def27) {
this.def27=def27;
} 
 
/**
* ���� def28��Getter����.���������Զ�����28
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef28() {
return this.def28;
} 

/**
* ����def28��Setter����.���������Զ�����28
* ��������:2019-7-17
* @param newDef28 java.lang.String
*/
public void setDef28 ( String def28) {
this.def28=def28;
} 
 
/**
* ���� def29��Getter����.���������Զ�����29
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef29() {
return this.def29;
} 

/**
* ����def29��Setter����.���������Զ�����29
* ��������:2019-7-17
* @param newDef29 java.lang.String
*/
public void setDef29 ( String def29) {
this.def29=def29;
} 
 
/**
* ���� def30��Getter����.���������Զ�����30
*  ��������:2019-7-17
* @return java.lang.String
*/
public String getDef30() {
return this.def30;
} 

/**
* ����def30��Setter����.���������Զ�����30
* ��������:2019-7-17
* @param newDef30 java.lang.String
*/
public void setDef30 ( String def30) {
this.def30=def30;
} 
 
/**
* ���� billdate��Getter����.��������ҵ��ʱ��
*  ��������:2019-7-17
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBilldate() {
return this.billdate;
} 

/**
* ����billdate��Setter����.��������ҵ��ʱ��
* ��������:2019-7-17
* @param newBilldate nc.vo.pub.lang.UFDate
*/
public void setBilldate ( UFDate billdate) {
this.billdate=billdate;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-7-17
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-7-17
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-7-17
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-7-17
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-7-17
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-7-17
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-7-17
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.GroupDataVO");
    }
   }
    