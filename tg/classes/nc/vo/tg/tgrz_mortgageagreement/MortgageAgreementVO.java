package nc.vo.tg.tgrz_mortgageagreement;

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
 *  ��������:2019-6-13
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class MortgageAgreementVO extends SuperVO {
	
/**
*����Э������
*/
public java.lang.String pk_moragreement;
/**
*��������
*/
public UFDate dbilldate;
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
*�޸���
*/
public java.lang.String modifier;
/**
*�޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*id
*/
public java.lang.String id;
/**
*code
*/
public java.lang.String code;
/**
*name
*/
public java.lang.String name;
/**
*�Ƶ�ʱ��
*/
public UFDateTime maketime;
/**
*����޸�ʱ��
*/
public UFDateTime lastmaketime;
/**
*�к�
*/
public java.lang.String rowno;
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
*�޶�ö��
*/
public java.lang.Integer emendenum;
/**
*���ݰ汾pk
*/
public java.lang.String billversionpk;
/**
*����
*/
public java.lang.String title;
/**
*������
*/
public java.lang.String proposer;
/**
*��������
*/
public UFDate applicationdate;
/**
*���빫˾
*/
public java.lang.String applicationorg;
/**
*���벿��
*/
public java.lang.String applicationdept;
/**
*ҵ����Ϣ
*/
public java.lang.String busiinformation;
/**
*��Ŀ����
*/
public java.lang.String proname;
/**
*�տλ
*/
public java.lang.String pk_payee;
/**
*���λ
*/
public java.lang.String pk_payer;
/**
*��ͬ�ܽ��
*/
public nc.vo.pub.lang.UFDouble n_amount;
/**
*�ۼ��Ѹ���
*/
public nc.vo.pub.lang.UFDouble i_totalpayamount;
/**
*���������
*/
public nc.vo.pub.lang.UFDouble i_applyamount;
/**
*���Ż��
*/
public java.lang.String groupaccounting;
/**
*����
*/
public java.lang.String cashier;
/**
*�ÿ�����
*/
public java.lang.String moneycontent;
/**
*�������
*/
public java.lang.String process;
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
*�Զ����ı�1
*/
public java.lang.String big_text_a;
/**
*�Զ����ı�2
*/
public java.lang.String big_text_b;
/**
*�Զ����ı�3
*/
public java.lang.String big_text_c;
/**
*�Զ����ı�4
*/
public java.lang.String big_text_d;
/**
*�Զ����ı�5
*/
public java.lang.String big_text_e;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_moragreement��Getter����.������������Э������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getPk_moragreement() {
return this.pk_moragreement;
} 

/**
* ����pk_moragreement��Setter����.������������Э������
* ��������:2019-6-13
* @param newPk_moragreement java.lang.String
*/
public void setPk_moragreement ( java.lang.String pk_moragreement) {
this.pk_moragreement=pk_moragreement;
} 
 
/**
* ���� dbilldate��Getter����.����������������
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* ����dbilldate��Setter����.����������������
* ��������:2019-6-13
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-6-13
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-6-13
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-6-13
* @return nc.vo.org.OrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-6-13
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-6-13
* @return nc.vo.vorg.OrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-6-13
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-6-13
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-6-13
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-6-13
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-6-13
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-6-13
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-6-13
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� id��Getter����.��������id
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getId() {
return this.id;
} 

/**
* ����id��Setter����.��������id
* ��������:2019-6-13
* @param newId java.lang.String
*/
public void setId ( java.lang.String id) {
this.id=id;
} 
 
/**
* ���� code��Getter����.��������code
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getCode() {
return this.code;
} 

/**
* ����code��Setter����.��������code
* ��������:2019-6-13
* @param newCode java.lang.String
*/
public void setCode ( java.lang.String code) {
this.code=code;
} 
 
/**
* ���� name��Getter����.��������name
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getName() {
return this.name;
} 

/**
* ����name��Setter����.��������name
* ��������:2019-6-13
* @param newName java.lang.String
*/
public void setName ( java.lang.String name) {
this.name=name;
} 
 
/**
* ���� maketime��Getter����.���������Ƶ�ʱ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getMaketime() {
return this.maketime;
} 

/**
* ����maketime��Setter����.���������Ƶ�ʱ��
* ��������:2019-6-13
* @param newMaketime nc.vo.pub.lang.UFDateTime
*/
public void setMaketime ( UFDateTime maketime) {
this.maketime=maketime;
} 
 
/**
* ���� lastmaketime��Getter����.������������޸�ʱ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getLastmaketime() {
return this.lastmaketime;
} 

/**
* ����lastmaketime��Setter����.������������޸�ʱ��
* ��������:2019-6-13
* @param newLastmaketime nc.vo.pub.lang.UFDateTime
*/
public void setLastmaketime ( UFDateTime lastmaketime) {
this.lastmaketime=lastmaketime;
} 
 
/**
* ���� rowno��Getter����.���������к�
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getRowno() {
return this.rowno;
} 

/**
* ����rowno��Setter����.���������к�
* ��������:2019-6-13
* @param newRowno java.lang.String
*/
public void setRowno ( java.lang.String rowno) {
this.rowno=rowno;
} 
 
/**
* ���� billid��Getter����.������������ID
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillid() {
return this.billid;
} 

/**
* ����billid��Setter����.������������ID
* ��������:2019-6-13
* @param newBillid java.lang.String
*/
public void setBillid ( java.lang.String billid) {
this.billid=billid;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-6-13
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* ���� pkorg��Getter����.��������������֯
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getPkorg() {
return this.pkorg;
} 

/**
* ����pkorg��Setter����.��������������֯
* ��������:2019-6-13
* @param newPkorg java.lang.String
*/
public void setPkorg ( java.lang.String pkorg) {
this.pkorg=pkorg;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-6-13
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� billmaker��Getter����.���������Ƶ���
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillmaker() {
return this.billmaker;
} 

/**
* ����billmaker��Setter����.���������Ƶ���
* ��������:2019-6-13
* @param newBillmaker java.lang.String
*/
public void setBillmaker ( java.lang.String billmaker) {
this.billmaker=billmaker;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-6-13
* @param newApprover java.lang.String
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-6-13
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-6-13
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-6-13
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( java.lang.String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-6-13
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-6-13
* @param newTranstype java.lang.String
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-6-13
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-6-13
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( java.lang.String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-6-13
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( java.lang.String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-6-13
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( java.lang.String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� emendenum��Getter����.���������޶�ö��
*  ��������:2019-6-13
* @return java.lang.Integer
*/
public java.lang.Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������޶�ö��
* ��������:2019-6-13
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( java.lang.Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� billversionpk��Getter����.�����������ݰ汾pk
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBillversionpk() {
return this.billversionpk;
} 

/**
* ����billversionpk��Setter����.�����������ݰ汾pk
* ��������:2019-6-13
* @param newBillversionpk java.lang.String
*/
public void setBillversionpk ( java.lang.String billversionpk) {
this.billversionpk=billversionpk;
} 
 
/**
* ���� title��Getter����.������������
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getTitle() {
return this.title;
} 

/**
* ����title��Setter����.������������
* ��������:2019-6-13
* @param newTitle java.lang.String
*/
public void setTitle ( java.lang.String title) {
this.title=title;
} 
 
/**
* ���� proposer��Getter����.��������������
*  ��������:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getProposer() {
return this.proposer;
} 

/**
* ����proposer��Setter����.��������������
* ��������:2019-6-13
* @param newProposer nc.vo.bd.psn.PsndocVO
*/
public void setProposer ( java.lang.String proposer) {
this.proposer=proposer;
} 
 
/**
* ���� applicationdate��Getter����.����������������
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getApplicationdate() {
return this.applicationdate;
} 

/**
* ����applicationdate��Setter����.����������������
* ��������:2019-6-13
* @param newApplicationdate nc.vo.pub.lang.UFDate
*/
public void setApplicationdate ( UFDate applicationdate) {
this.applicationdate=applicationdate;
} 
 
/**
* ���� applicationorg��Getter����.�����������빫˾
*  ��������:2019-6-13
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getApplicationorg() {
return this.applicationorg;
} 

/**
* ����applicationorg��Setter����.�����������빫˾
* ��������:2019-6-13
* @param newApplicationorg nc.vo.org.FinanceOrgVO
*/
public void setApplicationorg ( java.lang.String applicationorg) {
this.applicationorg=applicationorg;
} 
 
/**
* ���� applicationdept��Getter����.�����������벿��
*  ��������:2019-6-13
* @return nc.vo.org.DeptVO
*/
public java.lang.String getApplicationdept() {
return this.applicationdept;
} 

/**
* ����applicationdept��Setter����.�����������벿��
* ��������:2019-6-13
* @param newApplicationdept nc.vo.org.DeptVO
*/
public void setApplicationdept ( java.lang.String applicationdept) {
this.applicationdept=applicationdept;
} 
 
/**
* ���� busiinformation��Getter����.��������ҵ����Ϣ
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getBusiinformation() {
return this.busiinformation;
} 

/**
* ����busiinformation��Setter����.��������ҵ����Ϣ
* ��������:2019-6-13
* @param newBusiinformation java.lang.String
*/
public void setBusiinformation ( java.lang.String busiinformation) {
this.busiinformation=busiinformation;
} 
 
/**
* ���� proname��Getter����.����������Ŀ����
*  ��������:2019-6-13
* @return nc.vo.pmpub.project.ProjectHeadVO
*/
public java.lang.String getProname() {
return this.proname;
} 

/**
* ����proname��Setter����.����������Ŀ����
* ��������:2019-6-13
* @param newProname nc.vo.pmpub.project.ProjectHeadVO
*/
public void setProname ( java.lang.String proname) {
this.proname=proname;
} 
 
/**
* ���� pk_payee��Getter����.���������տλ
*  ��������:2019-6-13
* @return nc.vo.bd.cust.CustSupplierVO
*/
public java.lang.String getPk_payee() {
return this.pk_payee;
} 

/**
* ����pk_payee��Setter����.���������տλ
* ��������:2019-6-13
* @param newPk_payee nc.vo.bd.cust.CustSupplierVO
*/
public void setPk_payee ( java.lang.String pk_payee) {
this.pk_payee=pk_payee;
} 
 
/**
* ���� pk_payer��Getter����.�����������λ
*  ��������:2019-6-13
* @return nc.vo.bd.cust.CustSupplierVO
*/
public java.lang.String getPk_payer() {
return this.pk_payer;
} 

/**
* ����pk_payer��Setter����.�����������λ
* ��������:2019-6-13
* @param newPk_payer nc.vo.bd.cust.CustSupplierVO
*/
public void setPk_payer ( java.lang.String pk_payer) {
this.pk_payer=pk_payer;
} 
 
/**
* ���� n_amount��Getter����.����������ͬ�ܽ��
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getN_amount() {
return this.n_amount;
} 

/**
* ����n_amount��Setter����.����������ͬ�ܽ��
* ��������:2019-6-13
* @param newN_amount nc.vo.pub.lang.UFDouble
*/
public void setN_amount ( nc.vo.pub.lang.UFDouble n_amount) {
this.n_amount=n_amount;
} 
 
/**
* ���� i_totalpayamount��Getter����.���������ۼ��Ѹ���
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getI_totalpayamount() {
return this.i_totalpayamount;
} 

/**
* ����i_totalpayamount��Setter����.���������ۼ��Ѹ���
* ��������:2019-6-13
* @param newI_totalpayamount nc.vo.pub.lang.UFDouble
*/
public void setI_totalpayamount ( nc.vo.pub.lang.UFDouble i_totalpayamount) {
this.i_totalpayamount=i_totalpayamount;
} 
 
/**
* ���� i_applyamount��Getter����.�����������������
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getI_applyamount() {
return this.i_applyamount;
} 

/**
* ����i_applyamount��Setter����.�����������������
* ��������:2019-6-13
* @param newI_applyamount nc.vo.pub.lang.UFDouble
*/
public void setI_applyamount ( nc.vo.pub.lang.UFDouble i_applyamount) {
this.i_applyamount=i_applyamount;
} 
 
/**
* ���� groupaccounting��Getter����.�����������Ż��
*  ��������:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getGroupaccounting() {
return this.groupaccounting;
} 

/**
* ����groupaccounting��Setter����.�����������Ż��
* ��������:2019-6-13
* @param newGroupaccounting nc.vo.bd.psn.PsndocVO
*/
public void setGroupaccounting ( java.lang.String groupaccounting) {
this.groupaccounting=groupaccounting;
} 
 
/**
* ���� cashier��Getter����.������������
*  ��������:2019-6-13
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getCashier() {
return this.cashier;
} 

/**
* ����cashier��Setter����.������������
* ��������:2019-6-13
* @param newCashier nc.vo.bd.psn.PsndocVO
*/
public void setCashier ( java.lang.String cashier) {
this.cashier=cashier;
} 
 
/**
* ���� moneycontent��Getter����.���������ÿ�����
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getMoneycontent() {
return this.moneycontent;
} 

/**
* ����moneycontent��Setter����.���������ÿ�����
* ��������:2019-6-13
* @param newMoneycontent java.lang.String
*/
public void setMoneycontent ( java.lang.String moneycontent) {
this.moneycontent=moneycontent;
} 
 
/**
* ���� process��Getter����.���������������
*  ��������:2019-6-13
* @return nc.vo.bd.defdoc.DefdocVO
*/
public java.lang.String getProcess() {
return this.process;
} 

/**
* ����process��Setter����.���������������
* ��������:2019-6-13
* @param newProcess nc.vo.bd.defdoc.DefdocVO
*/
public void setProcess ( java.lang.String process) {
this.process=process;
} 
 
/**
* ���� def1��Getter����.���������Զ�����1
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.���������Զ�����1
* ��������:2019-6-13
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.���������Զ�����2
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.���������Զ�����2
* ��������:2019-6-13
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-6-13
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-6-13
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-6-13
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-6-13
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-6-13
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.���������Զ�����8
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.���������Զ�����8
* ��������:2019-6-13
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-6-13
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.���������Զ�����10
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.���������Զ�����10
* ��������:2019-6-13
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.���������Զ�����11
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.���������Զ�����11
* ��������:2019-6-13
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������Զ�����12
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������Զ�����12
* ��������:2019-6-13
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.���������Զ�����13
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.���������Զ�����13
* ��������:2019-6-13
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-6-13
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-6-13
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-6-13
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-6-13
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-6-13
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-6-13
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-6-13
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-6-13
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 


public java.lang.String getBig_text_a() {
	return big_text_a;
}

public void setBig_text_a(java.lang.String big_text_a) {
	this.big_text_a = big_text_a;
}

public java.lang.String getBig_text_b() {
	return big_text_b;
}

public void setBig_text_b(java.lang.String big_text_b) {
	this.big_text_b = big_text_b;
}

public java.lang.String getBig_text_c() {
	return big_text_c;
}

public void setBig_text_c(java.lang.String big_text_c) {
	this.big_text_c = big_text_c;
}

public java.lang.String getBig_text_d() {
	return big_text_d;
}

public void setBig_text_d(java.lang.String big_text_d) {
	this.big_text_d = big_text_d;
}

public java.lang.String getBig_text_e() {
	return big_text_e;
}

public void setBig_text_e(java.lang.String big_text_e) {
	this.big_text_e = big_text_e;
}

/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-6-13
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-6-13
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.MortgageAgreement");
    }
   }
    