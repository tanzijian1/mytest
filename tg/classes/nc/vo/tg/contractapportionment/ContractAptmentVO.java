package nc.vo.tg.contractapportionment;

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
 *  ��������:2019-9-18
 * @author YONYOU NC
 * @version NCPrj ??
 */
 
public class ContractAptmentVO extends SuperVO {
	
/**
*��ͬ��̯��������
*/
public java.lang.String pk_contractaptment_h;
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
*��������
*/
public UFDate dbilldate;
/**
*����״̬
*/
public java.lang.Integer approvestatus;
/**
*��������
*/
public java.lang.String billtype;
/**
*��������
*/
public java.lang.String transtype;
/**
*ҵ������
*/
public java.lang.String pk_busitype;
/**
*ҵ������
*/
public java.lang.String busitype;
/**
*��ͬ����
*/
public java.lang.String billno;
/**
*��ͬ����
*/
public java.lang.String contractname;
/**
*��ͬ����
*/
public java.lang.String contracttype;
/**
*ǩ�ָ�������
*/
public UFDate signdate;
/**
*��ͬ��Ч����
*/
public UFDate condealdate;
/**
*��ͬ��ֹ����
*/
public UFDate conenddate;
/**
*��Ӧ��
*/
public java.lang.String supplier;
/**
*�տ������˺�
*/
public java.lang.Integer recbankacc;
/**
*��ͬ��������
*/
public java.lang.String conflwdept;
/**
*��ͬ������
*/
public java.lang.String conflwperson;
/**
*��ͬ���
*/
public nc.vo.pub.lang.UFDouble conmoney;
/**
*ǩԼ��˾
*/
public java.lang.String pk_signorg;
/**
*������˾
*/
public java.lang.String pk_flworg;
/**
*�ѷ�̯���ϼ�
*/
public nc.vo.pub.lang.UFDouble totalyftmoney;
/**
*�ۼƸ�����
*/
public nc.vo.pub.lang.UFDouble totalpaymoney;
/**
*�ۼƿ�Ʊ���
*/
public nc.vo.pub.lang.UFDouble totalkpmoney;
/**
*������
*/
public java.lang.String creator;
/**
*����ʱ��
*/
public UFDate creationtime;
/**
*����޸���
*/
public java.lang.String modifier;
/**
*����޸�ʱ��
*/
public UFDateTime modifiedtime;
/**
*������
*/
public java.lang.String approver;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��������
*/
public java.lang.String objtype;
/**
*����
*/
public java.lang.String pk_currtype;
/**
*��Ч״̬
*/
public java.lang.String effectstatus;
/**
*��ϵͳ����
*/
public java.lang.String def1;
/**
*��ϵͳ���ݺ�
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
*��ͬϸ��
*/
public java.lang.String def8;
/**
*�Զ�����9
*/
public java.lang.String def9;
/**
*��ϵͳ����
*/
public java.lang.String def10;
/**
*��̯���ϼ�
*/
public java.lang.String def11;
/**
*�������ű��
*/
public java.lang.String def12;
/**
*�����˱��
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
*���ز���Ŀһ������
*/
public java.lang.String def24;
/**
*���ز���Ŀ��������
*/
public java.lang.String def25;
/**
*���ز���Ŀ��������
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
*ʱ���
*/
public UFDateTime ts;
    
    
/**
* ���� pk_contractaptment_h��Getter����.����������ͬ��̯��������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getPk_contractaptment_h() {
return this.pk_contractaptment_h;
} 

/**
* ����pk_contractaptment_h��Setter����.����������ͬ��̯��������
* ��������:2019-9-18
* @param newPk_contractaptment_h java.lang.String
*/
public void setPk_contractaptment_h ( java.lang.String pk_contractaptment_h) {
this.pk_contractaptment_h=pk_contractaptment_h;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-9-18
* @return nc.vo.org.GroupVO
*/
public java.lang.String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-9-18
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( java.lang.String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-9-18
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-9-18
* @param newPk_org nc.vo.org.FinanceOrgVO
*/
public void setPk_org ( java.lang.String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-9-18
* @return nc.vo.vorg.FinanceOrgVersionVO
*/
public java.lang.String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-9-18
* @param newPk_org_v nc.vo.vorg.FinanceOrgVersionVO
*/
public void setPk_org_v ( java.lang.String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� dbilldate��Getter����.����������������
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* ����dbilldate��Setter����.����������������
* ��������:2019-9-18
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-9-18
* @return nc.vo.pub.pf.BillStatusEnum
*/
public java.lang.Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-9-18
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( java.lang.Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-9-18
* @param newBilltype java.lang.String
*/
public void setBilltype ( java.lang.String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-9-18
* @return nc.vo.pub.billtype.BilltypeVO
*/
public java.lang.String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-9-18
* @param newTranstype nc.vo.pub.billtype.BilltypeVO
*/
public void setTranstype ( java.lang.String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� pk_busitype��Getter����.��������ҵ������
*  ��������:2019-9-18
* @return nc.vo.pf.pub.BusitypeVO
*/
public java.lang.String getPk_busitype() {
return this.pk_busitype;
} 

/**
* ����pk_busitype��Setter����.��������ҵ������
* ��������:2019-9-18
* @param newPk_busitype nc.vo.pf.pub.BusitypeVO
*/
public void setPk_busitype ( java.lang.String pk_busitype) {
this.pk_busitype=pk_busitype;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-9-18
* @param newBusitype java.lang.String
*/
public void setBusitype ( java.lang.String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� billno��Getter����.����������ͬ����
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.����������ͬ����
* ��������:2019-9-18
* @param newBillno java.lang.String
*/
public void setBillno ( java.lang.String billno) {
this.billno=billno;
} 
 
/**
* ���� contractname��Getter����.����������ͬ����
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getContractname() {
return this.contractname;
} 

/**
* ����contractname��Setter����.����������ͬ����
* ��������:2019-9-18
* @param newContractname java.lang.String
*/
public void setContractname ( java.lang.String contractname) {
this.contractname=contractname;
} 
 
/**
* ���� contracttype��Getter����.����������ͬ����
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getContracttype() {
return this.contracttype;
} 

/**
* ����contracttype��Setter����.����������ͬ����
* ��������:2019-9-18
* @param newContracttype java.lang.String
*/
public void setContracttype ( java.lang.String contracttype) {
this.contracttype=contracttype;
} 
 
/**
* ���� signdate��Getter����.��������ǩ�ָ�������
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getSigndate() {
return this.signdate;
} 

/**
* ����signdate��Setter����.��������ǩ�ָ�������
* ��������:2019-9-18
* @param newSigndate nc.vo.pub.lang.UFDate
*/
public void setSigndate ( UFDate signdate) {
this.signdate=signdate;
} 
 
/**
* ���� condealdate��Getter����.����������ͬ��Ч����
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getCondealdate() {
return this.condealdate;
} 

/**
* ����condealdate��Setter����.����������ͬ��Ч����
* ��������:2019-9-18
* @param newCondealdate nc.vo.pub.lang.UFDate
*/
public void setCondealdate ( UFDate condealdate) {
this.condealdate=condealdate;
} 
 
/**
* ���� conenddate��Getter����.����������ͬ��ֹ����
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getConenddate() {
return this.conenddate;
} 

/**
* ����conenddate��Setter����.����������ͬ��ֹ����
* ��������:2019-9-18
* @param newConenddate nc.vo.pub.lang.UFDate
*/
public void setConenddate ( UFDate conenddate) {
this.conenddate=conenddate;
} 
 
/**
* ���� supplier��Getter����.����������Ӧ��
*  ��������:2019-9-18
* @return nc.vo.bd.supplier.SupplierVO
*/
public java.lang.String getSupplier() {
return this.supplier;
} 

/**
* ����supplier��Setter����.����������Ӧ��
* ��������:2019-9-18
* @param newSupplier nc.vo.bd.supplier.SupplierVO
*/
public void setSupplier ( java.lang.String supplier) {
this.supplier=supplier;
} 
 
/**
* ���� recbankacc��Getter����.���������տ������˺�
*  ��������:2019-9-18
* @return java.lang.Integer
*/
public java.lang.Integer getRecbankacc() {
return this.recbankacc;
} 

/**
* ����recbankacc��Setter����.���������տ������˺�
* ��������:2019-9-18
* @param newRecbankacc java.lang.Integer
*/
public void setRecbankacc ( java.lang.Integer recbankacc) {
this.recbankacc=recbankacc;
} 
 
/**
* ���� conflwdept��Getter����.����������ͬ��������
*  ��������:2019-9-18
* @return nc.vo.org.DeptVO
*/
public java.lang.String getConflwdept() {
return this.conflwdept;
} 

/**
* ����conflwdept��Setter����.����������ͬ��������
* ��������:2019-9-18
* @param newConflwdept nc.vo.org.DeptVO
*/
public void setConflwdept ( java.lang.String conflwdept) {
this.conflwdept=conflwdept;
} 
 
/**
* ���� conflwperson��Getter����.����������ͬ������
*  ��������:2019-9-18
* @return nc.vo.bd.psn.PsndocVO
*/
public java.lang.String getConflwperson() {
return this.conflwperson;
} 

/**
* ����conflwperson��Setter����.����������ͬ������
* ��������:2019-9-18
* @param newConflwperson nc.vo.bd.psn.PsndocVO
*/
public void setConflwperson ( java.lang.String conflwperson) {
this.conflwperson=conflwperson;
} 
 
/**
* ���� conmoney��Getter����.����������ͬ���
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getConmoney() {
return this.conmoney;
} 

/**
* ����conmoney��Setter����.����������ͬ���
* ��������:2019-9-18
* @param newConmoney nc.vo.pub.lang.UFDouble
*/
public void setConmoney ( nc.vo.pub.lang.UFDouble conmoney) {
this.conmoney=conmoney;
} 
 
/**
* ���� pk_signorg��Getter����.��������ǩԼ��˾
*  ��������:2019-9-18
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_signorg() {
return this.pk_signorg;
} 

/**
* ����pk_signorg��Setter����.��������ǩԼ��˾
* ��������:2019-9-18
* @param newPk_signorg nc.vo.org.FinanceOrgVO
*/
public void setPk_signorg ( java.lang.String pk_signorg) {
this.pk_signorg=pk_signorg;
} 
 
/**
* ���� pk_flworg��Getter����.��������������˾
*  ��������:2019-9-18
* @return nc.vo.org.FinanceOrgVO
*/
public java.lang.String getPk_flworg() {
return this.pk_flworg;
} 

/**
* ����pk_flworg��Setter����.��������������˾
* ��������:2019-9-18
* @param newPk_flworg nc.vo.org.FinanceOrgVO
*/
public void setPk_flworg ( java.lang.String pk_flworg) {
this.pk_flworg=pk_flworg;
} 
 
/**
* ���� totalyftmoney��Getter����.���������ѷ�̯���ϼ�
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTotalyftmoney() {
return this.totalyftmoney;
} 

/**
* ����totalyftmoney��Setter����.���������ѷ�̯���ϼ�
* ��������:2019-9-18
* @param newTotalyftmoney nc.vo.pub.lang.UFDouble
*/
public void setTotalyftmoney ( nc.vo.pub.lang.UFDouble totalyftmoney) {
this.totalyftmoney=totalyftmoney;
} 
 
/**
* ���� totalpaymoney��Getter����.���������ۼƸ�����
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTotalpaymoney() {
return this.totalpaymoney;
} 

/**
* ����totalpaymoney��Setter����.���������ۼƸ�����
* ��������:2019-9-18
* @param newTotalpaymoney nc.vo.pub.lang.UFDouble
*/
public void setTotalpaymoney ( nc.vo.pub.lang.UFDouble totalpaymoney) {
this.totalpaymoney=totalpaymoney;
} 
 
/**
* ���� totalkpmoney��Getter����.���������ۼƿ�Ʊ���
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDouble
*/
public nc.vo.pub.lang.UFDouble getTotalkpmoney() {
return this.totalkpmoney;
} 

/**
* ����totalkpmoney��Setter����.���������ۼƿ�Ʊ���
* ��������:2019-9-18
* @param newTotalkpmoney nc.vo.pub.lang.UFDouble
*/
public void setTotalkpmoney ( nc.vo.pub.lang.UFDouble totalkpmoney) {
this.totalkpmoney=totalkpmoney;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-9-18
* @return nc.vo.sm.UserVO
*/
public java.lang.String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-9-18
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( java.lang.String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-9-18
* @param newCreationtime nc.vo.pub.lang.UFDate
*/
public void setCreationtime ( UFDate creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.������������޸���
*  ��������:2019-9-18
* @return nc.vo.sm.UserVO
*/
public java.lang.String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.������������޸���
* ��������:2019-9-18
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( java.lang.String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.������������޸�ʱ��
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.������������޸�ʱ��
* ��������:2019-9-18
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-9-18
* @return nc.vo.sm.UserVO
*/
public java.lang.String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-9-18
* @param newApprover nc.vo.sm.UserVO
*/
public void setApprover ( java.lang.String approver) {
this.approver=approver;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-9-18
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� objtype��Getter����.����������������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getObjtype() {
return this.objtype;
} 

/**
* ����objtype��Setter����.����������������
* ��������:2019-9-18
* @param newObjtype java.lang.String
*/
public void setObjtype ( java.lang.String objtype) {
this.objtype=objtype;
} 
 
/**
* ���� pk_currtype��Getter����.������������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getPk_currtype() {
return this.pk_currtype;
} 

/**
* ����pk_currtype��Setter����.������������
* ��������:2019-9-18
* @param newPk_currtype java.lang.String
*/
public void setPk_currtype ( java.lang.String pk_currtype) {
this.pk_currtype=pk_currtype;
} 
 
/**
* ���� effectstatus��Getter����.����������Ч״̬
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getEffectstatus() {
return this.effectstatus;
} 

/**
* ����effectstatus��Setter����.����������Ч״̬
* ��������:2019-9-18
* @param newEffectstatus java.lang.String
*/
public void setEffectstatus ( java.lang.String effectstatus) {
this.effectstatus=effectstatus;
} 
 
/**
* ���� def1��Getter����.����������ϵͳ����
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef1() {
return this.def1;
} 

/**
* ����def1��Setter����.����������ϵͳ����
* ��������:2019-9-18
* @param newDef1 java.lang.String
*/
public void setDef1 ( java.lang.String def1) {
this.def1=def1;
} 
 
/**
* ���� def2��Getter����.����������ϵͳ���ݺ�
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef2() {
return this.def2;
} 

/**
* ����def2��Setter����.����������ϵͳ���ݺ�
* ��������:2019-9-18
* @param newDef2 java.lang.String
*/
public void setDef2 ( java.lang.String def2) {
this.def2=def2;
} 
 
/**
* ���� def3��Getter����.���������Զ�����3
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef3() {
return this.def3;
} 

/**
* ����def3��Setter����.���������Զ�����3
* ��������:2019-9-18
* @param newDef3 java.lang.String
*/
public void setDef3 ( java.lang.String def3) {
this.def3=def3;
} 
 
/**
* ���� def4��Getter����.���������Զ�����4
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef4() {
return this.def4;
} 

/**
* ����def4��Setter����.���������Զ�����4
* ��������:2019-9-18
* @param newDef4 java.lang.String
*/
public void setDef4 ( java.lang.String def4) {
this.def4=def4;
} 
 
/**
* ���� def5��Getter����.���������Զ�����5
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef5() {
return this.def5;
} 

/**
* ����def5��Setter����.���������Զ�����5
* ��������:2019-9-18
* @param newDef5 java.lang.String
*/
public void setDef5 ( java.lang.String def5) {
this.def5=def5;
} 
 
/**
* ���� def6��Getter����.���������Զ�����6
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef6() {
return this.def6;
} 

/**
* ����def6��Setter����.���������Զ�����6
* ��������:2019-9-18
* @param newDef6 java.lang.String
*/
public void setDef6 ( java.lang.String def6) {
this.def6=def6;
} 
 
/**
* ���� def7��Getter����.���������Զ�����7
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef7() {
return this.def7;
} 

/**
* ����def7��Setter����.���������Զ�����7
* ��������:2019-9-18
* @param newDef7 java.lang.String
*/
public void setDef7 ( java.lang.String def7) {
this.def7=def7;
} 
 
/**
* ���� def8��Getter����.����������ͬϸ��
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef8() {
return this.def8;
} 

/**
* ����def8��Setter����.����������ͬϸ��
* ��������:2019-9-18
* @param newDef8 java.lang.String
*/
public void setDef8 ( java.lang.String def8) {
this.def8=def8;
} 
 
/**
* ���� def9��Getter����.���������Զ�����9
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef9() {
return this.def9;
} 

/**
* ����def9��Setter����.���������Զ�����9
* ��������:2019-9-18
* @param newDef9 java.lang.String
*/
public void setDef9 ( java.lang.String def9) {
this.def9=def9;
} 
 
/**
* ���� def10��Getter����.����������ϵͳ����
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef10() {
return this.def10;
} 

/**
* ����def10��Setter����.����������ϵͳ����
* ��������:2019-9-18
* @param newDef10 java.lang.String
*/
public void setDef10 ( java.lang.String def10) {
this.def10=def10;
} 
 
/**
* ���� def11��Getter����.����������̯���ϼ�
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef11() {
return this.def11;
} 

/**
* ����def11��Setter����.����������̯���ϼ�
* ��������:2019-9-18
* @param newDef11 java.lang.String
*/
public void setDef11 ( java.lang.String def11) {
this.def11=def11;
} 
 
/**
* ���� def12��Getter����.���������������ű��
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef12() {
return this.def12;
} 

/**
* ����def12��Setter����.���������������ű��
* ��������:2019-9-18
* @param newDef12 java.lang.String
*/
public void setDef12 ( java.lang.String def12) {
this.def12=def12;
} 
 
/**
* ���� def13��Getter����.�������������˱��
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef13() {
return this.def13;
} 

/**
* ����def13��Setter����.�������������˱��
* ��������:2019-9-18
* @param newDef13 java.lang.String
*/
public void setDef13 ( java.lang.String def13) {
this.def13=def13;
} 
 
/**
* ���� def14��Getter����.���������Զ�����14
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef14() {
return this.def14;
} 

/**
* ����def14��Setter����.���������Զ�����14
* ��������:2019-9-18
* @param newDef14 java.lang.String
*/
public void setDef14 ( java.lang.String def14) {
this.def14=def14;
} 
 
/**
* ���� def15��Getter����.���������Զ�����15
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef15() {
return this.def15;
} 

/**
* ����def15��Setter����.���������Զ�����15
* ��������:2019-9-18
* @param newDef15 java.lang.String
*/
public void setDef15 ( java.lang.String def15) {
this.def15=def15;
} 
 
/**
* ���� def16��Getter����.���������Զ�����16
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef16() {
return this.def16;
} 

/**
* ����def16��Setter����.���������Զ�����16
* ��������:2019-9-18
* @param newDef16 java.lang.String
*/
public void setDef16 ( java.lang.String def16) {
this.def16=def16;
} 
 
/**
* ���� def17��Getter����.���������Զ�����17
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef17() {
return this.def17;
} 

/**
* ����def17��Setter����.���������Զ�����17
* ��������:2019-9-18
* @param newDef17 java.lang.String
*/
public void setDef17 ( java.lang.String def17) {
this.def17=def17;
} 
 
/**
* ���� def18��Getter����.���������Զ�����18
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef18() {
return this.def18;
} 

/**
* ����def18��Setter����.���������Զ�����18
* ��������:2019-9-18
* @param newDef18 java.lang.String
*/
public void setDef18 ( java.lang.String def18) {
this.def18=def18;
} 
 
/**
* ���� def19��Getter����.���������Զ�����19
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef19() {
return this.def19;
} 

/**
* ����def19��Setter����.���������Զ�����19
* ��������:2019-9-18
* @param newDef19 java.lang.String
*/
public void setDef19 ( java.lang.String def19) {
this.def19=def19;
} 
 
/**
* ���� def20��Getter����.���������Զ�����20
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef20() {
return this.def20;
} 

/**
* ����def20��Setter����.���������Զ�����20
* ��������:2019-9-18
* @param newDef20 java.lang.String
*/
public void setDef20 ( java.lang.String def20) {
this.def20=def20;
} 
 
/**
* ���� def21��Getter����.���������Զ�����21
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef21() {
return this.def21;
} 

/**
* ����def21��Setter����.���������Զ�����21
* ��������:2019-9-18
* @param newDef21 java.lang.String
*/
public void setDef21 ( java.lang.String def21) {
this.def21=def21;
} 
 
/**
* ���� def22��Getter����.���������Զ�����22
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef22() {
return this.def22;
} 

/**
* ����def22��Setter����.���������Զ�����22
* ��������:2019-9-18
* @param newDef22 java.lang.String
*/
public void setDef22 ( java.lang.String def22) {
this.def22=def22;
} 
 
/**
* ���� def23��Getter����.���������Զ�����23
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef23() {
return this.def23;
} 

/**
* ����def23��Setter����.���������Զ�����23
* ��������:2019-9-18
* @param newDef23 java.lang.String
*/
public void setDef23 ( java.lang.String def23) {
this.def23=def23;
} 
 
/**
* ���� def24��Getter����.�����������ز���Ŀһ������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef24() {
return this.def24;
} 

/**
* ����def24��Setter����.�����������ز���Ŀһ������
* ��������:2019-9-18
* @param newDef24 java.lang.String
*/
public void setDef24 ( java.lang.String def24) {
this.def24=def24;
} 
 
/**
* ���� def25��Getter����.�����������ز���Ŀ��������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef25() {
return this.def25;
} 

/**
* ����def25��Setter����.�����������ز���Ŀ��������
* ��������:2019-9-18
* @param newDef25 java.lang.String
*/
public void setDef25 ( java.lang.String def25) {
this.def25=def25;
} 
 
/**
* ���� def26��Getter����.�����������ز���Ŀ��������
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef26() {
return this.def26;
} 

/**
* ����def26��Setter����.�����������ز���Ŀ��������
* ��������:2019-9-18
* @param newDef26 java.lang.String
*/
public void setDef26 ( java.lang.String def26) {
this.def26=def26;
} 
 
/**
* ���� def27��Getter����.���������Զ�����27
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef27() {
return this.def27;
} 

/**
* ����def27��Setter����.���������Զ�����27
* ��������:2019-9-18
* @param newDef27 java.lang.String
*/
public void setDef27 ( java.lang.String def27) {
this.def27=def27;
} 
 
/**
* ���� def28��Getter����.���������Զ�����28
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef28() {
return this.def28;
} 

/**
* ����def28��Setter����.���������Զ�����28
* ��������:2019-9-18
* @param newDef28 java.lang.String
*/
public void setDef28 ( java.lang.String def28) {
this.def28=def28;
} 
 
/**
* ���� def29��Getter����.���������Զ�����29
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef29() {
return this.def29;
} 

/**
* ����def29��Setter����.���������Զ�����29
* ��������:2019-9-18
* @param newDef29 java.lang.String
*/
public void setDef29 ( java.lang.String def29) {
this.def29=def29;
} 
 
/**
* ���� def30��Getter����.���������Զ�����30
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef30() {
return this.def30;
} 

/**
* ����def30��Setter����.���������Զ�����30
* ��������:2019-9-18
* @param newDef30 java.lang.String
*/
public void setDef30 ( java.lang.String def30) {
this.def30=def30;
} 
 
/**
* ���� def31��Getter����.���������Զ�����31
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef31() {
return this.def31;
} 

/**
* ����def31��Setter����.���������Զ�����31
* ��������:2019-9-18
* @param newDef31 java.lang.String
*/
public void setDef31 ( java.lang.String def31) {
this.def31=def31;
} 
 
/**
* ���� def32��Getter����.���������Զ�����32
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef32() {
return this.def32;
} 

/**
* ����def32��Setter����.���������Զ�����32
* ��������:2019-9-18
* @param newDef32 java.lang.String
*/
public void setDef32 ( java.lang.String def32) {
this.def32=def32;
} 
 
/**
* ���� def33��Getter����.���������Զ�����33
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef33() {
return this.def33;
} 

/**
* ����def33��Setter����.���������Զ�����33
* ��������:2019-9-18
* @param newDef33 java.lang.String
*/
public void setDef33 ( java.lang.String def33) {
this.def33=def33;
} 
 
/**
* ���� def34��Getter����.���������Զ�����34
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef34() {
return this.def34;
} 

/**
* ����def34��Setter����.���������Զ�����34
* ��������:2019-9-18
* @param newDef34 java.lang.String
*/
public void setDef34 ( java.lang.String def34) {
this.def34=def34;
} 
 
/**
* ���� def35��Getter����.���������Զ�����35
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef35() {
return this.def35;
} 

/**
* ����def35��Setter����.���������Զ�����35
* ��������:2019-9-18
* @param newDef35 java.lang.String
*/
public void setDef35 ( java.lang.String def35) {
this.def35=def35;
} 
 
/**
* ���� def36��Getter����.���������Զ�����36
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef36() {
return this.def36;
} 

/**
* ����def36��Setter����.���������Զ�����36
* ��������:2019-9-18
* @param newDef36 java.lang.String
*/
public void setDef36 ( java.lang.String def36) {
this.def36=def36;
} 
 
/**
* ���� def37��Getter����.���������Զ�����37
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef37() {
return this.def37;
} 

/**
* ����def37��Setter����.���������Զ�����37
* ��������:2019-9-18
* @param newDef37 java.lang.String
*/
public void setDef37 ( java.lang.String def37) {
this.def37=def37;
} 
 
/**
* ���� def38��Getter����.���������Զ�����38
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef38() {
return this.def38;
} 

/**
* ����def38��Setter����.���������Զ�����38
* ��������:2019-9-18
* @param newDef38 java.lang.String
*/
public void setDef38 ( java.lang.String def38) {
this.def38=def38;
} 
 
/**
* ���� def39��Getter����.���������Զ�����39
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef39() {
return this.def39;
} 

/**
* ����def39��Setter����.���������Զ�����39
* ��������:2019-9-18
* @param newDef39 java.lang.String
*/
public void setDef39 ( java.lang.String def39) {
this.def39=def39;
} 
 
/**
* ���� def40��Getter����.���������Զ�����40
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef40() {
return this.def40;
} 

/**
* ����def40��Setter����.���������Զ�����40
* ��������:2019-9-18
* @param newDef40 java.lang.String
*/
public void setDef40 ( java.lang.String def40) {
this.def40=def40;
} 
 
/**
* ���� def41��Getter����.���������Զ�����41
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef41() {
return this.def41;
} 

/**
* ����def41��Setter����.���������Զ�����41
* ��������:2019-9-18
* @param newDef41 java.lang.String
*/
public void setDef41 ( java.lang.String def41) {
this.def41=def41;
} 
 
/**
* ���� def42��Getter����.���������Զ�����42
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef42() {
return this.def42;
} 

/**
* ����def42��Setter����.���������Զ�����42
* ��������:2019-9-18
* @param newDef42 java.lang.String
*/
public void setDef42 ( java.lang.String def42) {
this.def42=def42;
} 
 
/**
* ���� def43��Getter����.���������Զ�����43
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef43() {
return this.def43;
} 

/**
* ����def43��Setter����.���������Զ�����43
* ��������:2019-9-18
* @param newDef43 java.lang.String
*/
public void setDef43 ( java.lang.String def43) {
this.def43=def43;
} 
 
/**
* ���� def44��Getter����.���������Զ�����44
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef44() {
return this.def44;
} 

/**
* ����def44��Setter����.���������Զ�����44
* ��������:2019-9-18
* @param newDef44 java.lang.String
*/
public void setDef44 ( java.lang.String def44) {
this.def44=def44;
} 
 
/**
* ���� def45��Getter����.���������Զ�����45
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef45() {
return this.def45;
} 

/**
* ����def45��Setter����.���������Զ�����45
* ��������:2019-9-18
* @param newDef45 java.lang.String
*/
public void setDef45 ( java.lang.String def45) {
this.def45=def45;
} 
 
/**
* ���� def46��Getter����.���������Զ�����46
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef46() {
return this.def46;
} 

/**
* ����def46��Setter����.���������Զ�����46
* ��������:2019-9-18
* @param newDef46 java.lang.String
*/
public void setDef46 ( java.lang.String def46) {
this.def46=def46;
} 
 
/**
* ���� def47��Getter����.���������Զ�����47
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef47() {
return this.def47;
} 

/**
* ����def47��Setter����.���������Զ�����47
* ��������:2019-9-18
* @param newDef47 java.lang.String
*/
public void setDef47 ( java.lang.String def47) {
this.def47=def47;
} 
 
/**
* ���� def48��Getter����.���������Զ�����48
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef48() {
return this.def48;
} 

/**
* ����def48��Setter����.���������Զ�����48
* ��������:2019-9-18
* @param newDef48 java.lang.String
*/
public void setDef48 ( java.lang.String def48) {
this.def48=def48;
} 
 
/**
* ���� def49��Getter����.���������Զ�����49
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef49() {
return this.def49;
} 

/**
* ����def49��Setter����.���������Զ�����49
* ��������:2019-9-18
* @param newDef49 java.lang.String
*/
public void setDef49 ( java.lang.String def49) {
this.def49=def49;
} 
 
/**
* ���� def50��Getter����.���������Զ�����50
*  ��������:2019-9-18
* @return java.lang.String
*/
public java.lang.String getDef50() {
return this.def50;
} 

/**
* ����def50��Setter����.���������Զ�����50
* ��������:2019-9-18
* @param newDef50 java.lang.String
*/
public void setDef50 ( java.lang.String def50) {
this.def50=def50;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-9-18
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-9-18
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.ContractAptmentVO");
    }
   }
    