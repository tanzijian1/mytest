package nc.vo.tg.fischemeversion;

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
 *  ��������:2019-6-23
 * @author 
 * @version NCPrj ??
 */
 
public class FISchemeversionHVO extends SuperVO {
	public static final String FISCEMEVERSIONNUM="fiscemeversionnum";//�汾��
	   public Integer fiscemeversionnum;
		public Integer getFiscemeversionnum() {
		return fiscemeversionnum;
	}

	public void setFiscemeversionnum(Integer fiscemeversionnum) {
		this.fiscemeversionnum = fiscemeversionnum;
	}
/**
*���ʷ����汾��ͷ����
*/
public String pk_fiscemeversion;
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
*���ݺ�
*/
public String billno;
/**
*��������
*/
public String name;
/**
*����״̬
*/
public Integer approvestatus;
/**
*��������
*/
public UFDate dbilldate;
/**
*�汾����
*/
public UFDate billversiondate;
/**
*��Ŀ����
*/
public String pk_project;
/**
*�Ƿ�����
*/
public UFBoolean bmain;
/**
*����������
*/
public String pk_organizationtype;
/**
*�����ʻ���
*/
public String pk_organization;
/**
*�����ʽ��
*/
public UFDouble nmy;
/**
*����������
*/
public String fiterm;
/**
*����������
*/
public UFDouble rate;
/**
*�������ۺ�����
*/
public UFDouble zhrate;
/**
*��������
*/
public String ecreditcondition;
/**
*��������
*/
public String repaymentcond;
/**
*������
*/
public String followper;
/**
*����������
*/
public String percharge;
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
*ҵ������
*/
public String busitype;
/**
*������
*/
public String approver;
/**
*��������
*/
public String approvenote;
/**
*����ʱ��
*/
public UFDateTime approvedate;
/**
*��������
*/
public String transtype;
/**
*��������
*/
public String billtype;
/**
*��������pk
*/
public String transtypepk;
/**
*��Դ��������
*/
public String srcbilltype;
/**
*��Դ����id
*/
public String srcbillid;
/**
*�����޶�
*/
public Integer emendenum;
/**
*��Դ����pk
*/
public String pk_scheme;
/**
*�Զ�����1
*/
public String vdef1;
/**
*�Զ�����2
*/
public String vdef2;
/**
*�Զ�����3
*/
public String vdef3;
/**
*�Զ�����4
*/
public String vdef4;
/**
*�Զ�����5
*/
public String vdef5;
/**
*�Զ�����6
*/
public String vdef6;
/**
*�Զ�����7
*/
public String vdef7;
/**
*�Զ�����8
*/
public String vdef8;
/**
*�Զ�����9
*/
public String vdef9;
/**
*�Զ�����10
*/
public String vdef10;
/**
*�Զ�����11
*/
public String vdef11;
/**
*�Զ�����12
*/
public String vdef12;
/**
*�Զ�����13
*/
public String vdef13;
/**
*�Զ�����14
*/
public String vdef14;
/**
*�Զ�����15
*/
public String vdef15;
/**
*�Զ�����16
*/
public String vdef16;
/**
*�Զ�����17
*/
public String vdef17;
/**
*�Զ�����18
*/
public String vdef18;
/**
*�Զ�����19
*/
public String vdef19;
/**
*�Զ�����20
*/
public String vdef20;
/**
*ʱ���
*/
public UFDateTime ts;
    
    
//add by tjl
/**
 * �Զ�����21
 */
public String def21;

/**
 * �Զ�����22
 */
public String def22;
/**
 * �Զ�����23
 */
public String def23;

/**
 * �Զ�����24
 */
public String def24;
/**
 * �Զ�����25
 */
public String def25;

/**
 * �Զ�����26
 */
public String def26;
/**
 * �Զ�����27
 */
public String def27;

/**
 * �Զ�����28
 */
public String def28;
/**
 * �Զ�����29
 */
public String def29;
/**
 * �Զ�����30
 */
public String def30;
/**
 * �Զ�����31
 */
public String def31;
/**
 * �Զ�����32
 */
public String def32;
/**
 * �Զ�����33
 */
public String def33;
/**
 * �Զ�����34
 */
public String def34;
/**
 * �Զ�����35
 */
public String def35;
/**
 * �Զ�����36
 */
public String def36;
/**
 * �Զ�����37
 */
public String def37;
/**
 * �Զ�����38
 */
public String def38;
/**
 * �Զ�����39
 */
public String def39;
/**
 * �Զ�����40
 */
public String def40;
/**
 * �Զ�����41
 */
public String def41;
/**
 * �Զ�����42
 */
public String def42;
/**
 * �Զ�����43
 */
public String def43;
/**
 * �Զ�����44
 */
public String def44;
/**
 * �Զ�����45
 */
public String def45;
/**
 * �Զ�����46
 */
public String def46;
/**
 * �Զ�����47
 */
public String def47;
/**
 * �Զ�����48
 */
public String def48;
/**
 * �Զ�����49
 */
public String def49;
/**
 * �Զ�����50
 */
public String def50;
/**
 * �Զ�����51
 */
public String def51;
/**
 * �Զ�����52
 */
public String def52;
/**
 * �Զ�����53
 */
public String def53;
/**
 * �Զ�����54
 */
public String def54;
/**
 * �Զ�����55
 */
public String def55;
/**
 * �Զ�����56
 */
public String def56;
/**
 * �Զ�����57
 */
public String def57;
/**
 * �Զ�����58
 */
public String def58;
/**
 * �Զ�����59
 */
public String def59;
/**
 * �Զ�����60
 */
public String def60;
/**
 * ���ı�a
 */
public String big_text_a;
/**
 * ���ı�b
 */
public String big_text_b;
/**
 * ���ı�c
 */
public String big_text_c;
/**
 * ���ı�d
 */
public String big_text_d;
/**
 * ���ı�e
 */
public String big_text_e;

/**
 * dr
 */
public  Integer dr;    


    
public Integer getDr() {
	return dr;
}

public void setDr(Integer dr) {
	this.dr = dr;
}
public String getDef21() {
	return def21;
}

public void setDef21(String def21) {
	this.def21 = def21;
}

public String getDef22() {
	return def22;
}

public void setDef22(String def22) {
	this.def22 = def22;
}

public String getDef23() {
	return def23;
}

public void setDef23(String def23) {
	this.def23 = def23;
}

public String getDef24() {
	return def24;
}

public void setDef24(String def24) {
	this.def24 = def24;
}

public String getDef25() {
	return def25;
}

public void setDef25(String def25) {
	this.def25 = def25;
}

public String getDef26() {
	return def26;
}

public void setDef26(String def26) {
	this.def26 = def26;
}

public String getDef27() {
	return def27;
}

public void setDef27(String def27) {
	this.def27 = def27;
}

public String getDef28() {
	return def28;
}

public void setDef28(String def28) {
	this.def28 = def28;
}

public String getDef29() {
	return def29;
}

public void setDef29(String def29) {
	this.def29 = def29;
}

public String getDef30() {
	return def30;
}

public void setDef30(String def30) {
	this.def30 = def30;
}

public String getDef31() {
	return def31;
}

public void setDef31(String def31) {
	this.def31 = def31;
}

public String getDef32() {
	return def32;
}

public void setDef32(String def32) {
	this.def32 = def32;
}

public String getDef33() {
	return def33;
}

public void setDef33(String def33) {
	this.def33 = def33;
}

public String getDef34() {
	return def34;
}

public void setDef34(String def34) {
	this.def34 = def34;
}

public String getDef35() {
	return def35;
}

public void setDef35(String def35) {
	this.def35 = def35;
}

public String getDef36() {
	return def36;
}

public void setDef36(String def36) {
	this.def36 = def36;
}

public String getDef37() {
	return def37;
}

public void setDef37(String def37) {
	this.def37 = def37;
}

public String getDef38() {
	return def38;
}

public void setDef38(String def38) {
	this.def38 = def38;
}

public String getDef39() {
	return def39;
}

public void setDef39(String def39) {
	this.def39 = def39;
}

public String getDef40() {
	return def40;
}

public void setDef40(String def40) {
	this.def40 = def40;
}

public String getDef41() {
	return def41;
}

public void setDef41(String def41) {
	this.def41 = def41;
}

public String getDef42() {
	return def42;
}

public void setDef42(String def42) {
	this.def42 = def42;
}

public String getDef43() {
	return def43;
}

public void setDef43(String def43) {
	this.def43 = def43;
}

public String getDef44() {
	return def44;
}

public void setDef44(String def44) {
	this.def44 = def44;
}

public String getDef45() {
	return def45;
}

public void setDef45(String def45) {
	this.def45 = def45;
}

public String getDef46() {
	return def46;
}

public void setDef46(String def46) {
	this.def46 = def46;
}

public String getDef47() {
	return def47;
}

public void setDef47(String def47) {
	this.def47 = def47;
}

public String getDef48() {
	return def48;
}

public void setDef48(String def48) {
	this.def48 = def48;
}

public String getDef49() {
	return def49;
}

public void setDef49(String def49) {
	this.def49 = def49;
}

public String getDef50() {
	return def50;
}

public void setDef50(String def50) {
	this.def50 = def50;
}

public String getDef51() {
	return def51;
}

public void setDef51(String def51) {
	this.def51 = def51;
}

public String getDef52() {
	return def52;
}

public void setDef52(String def52) {
	this.def52 = def52;
}

public String getDef53() {
	return def53;
}

public void setDef53(String def53) {
	this.def53 = def53;
}

public String getDef54() {
	return def54;
}

public void setDef54(String def54) {
	this.def54 = def54;
}

public String getDef55() {
	return def55;
}

public void setDef55(String def55) {
	this.def55 = def55;
}

public String getDef56() {
	return def56;
}

public void setDef56(String def56) {
	this.def56 = def56;
}

public String getDef57() {
	return def57;
}

public void setDef57(String def57) {
	this.def57 = def57;
}

public String getDef58() {
	return def58;
}

public void setDef58(String def58) {
	this.def58 = def58;
}

public String getDef59() {
	return def59;
}

public void setDef59(String def59) {
	this.def59 = def59;
}

public String getDef60() {
	return def60;
}

public void setDef60(String def60) {
	this.def60 = def60;
}

public String getBig_text_a() {
	return big_text_a;
}

public void setBig_text_a(String big_text_a) {
	this.big_text_a = big_text_a;
}

public String getBig_text_b() {
	return big_text_b;
}

public void setBig_text_b(String big_text_b) {
	this.big_text_b = big_text_b;
}

public String getBig_text_c() {
	return big_text_c;
}

public void setBig_text_c(String big_text_c) {
	this.big_text_c = big_text_c;
}

public String getBig_text_d() {
	return big_text_d;
}

public void setBig_text_d(String big_text_d) {
	this.big_text_d = big_text_d;
}

public String getBig_text_e() {
	return big_text_e;
}

public void setBig_text_e(String big_text_e) {
	this.big_text_e = big_text_e;
}
/**
* ���� pk_fiscemeversion��Getter����.�����������ʷ����汾��ͷ����
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getPk_fiscemeversion() {
return this.pk_fiscemeversion;
} 

/**
* ����pk_fiscemeversion��Setter����.�����������ʷ����汾��ͷ����
* ��������:2019-6-23
* @param newPk_fiscemeversion java.lang.String
*/
public void setPk_fiscemeversion ( String pk_fiscemeversion) {
this.pk_fiscemeversion=pk_fiscemeversion;
} 
 
/**
* ���� pk_group��Getter����.������������
*  ��������:2019-6-23
* @return nc.vo.org.GroupVO
*/
public String getPk_group() {
return this.pk_group;
} 

/**
* ����pk_group��Setter����.������������
* ��������:2019-6-23
* @param newPk_group nc.vo.org.GroupVO
*/
public void setPk_group ( String pk_group) {
this.pk_group=pk_group;
} 
 
/**
* ���� pk_org��Getter����.����������֯
*  ��������:2019-6-23
* @return nc.vo.org.OrgVO
*/
public String getPk_org() {
return this.pk_org;
} 

/**
* ����pk_org��Setter����.����������֯
* ��������:2019-6-23
* @param newPk_org nc.vo.org.OrgVO
*/
public void setPk_org ( String pk_org) {
this.pk_org=pk_org;
} 
 
/**
* ���� pk_org_v��Getter����.����������֯�汾
*  ��������:2019-6-23
* @return nc.vo.vorg.OrgVersionVO
*/
public String getPk_org_v() {
return this.pk_org_v;
} 

/**
* ����pk_org_v��Setter����.����������֯�汾
* ��������:2019-6-23
* @param newPk_org_v nc.vo.vorg.OrgVersionVO
*/
public void setPk_org_v ( String pk_org_v) {
this.pk_org_v=pk_org_v;
} 
 
/**
* ���� billno��Getter����.�����������ݺ�
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getBillno() {
return this.billno;
} 

/**
* ����billno��Setter����.�����������ݺ�
* ��������:2019-6-23
* @param newBillno java.lang.String
*/
public void setBillno ( String billno) {
this.billno=billno;
} 
 
/**
* ���� name��Getter����.����������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getName() {
return this.name;
} 

/**
* ����name��Setter����.����������������
* ��������:2019-6-23
* @param newName java.lang.String
*/
public void setName ( String name) {
this.name=name;
} 
 
/**
* ���� approvestatus��Getter����.������������״̬
*  ��������:2019-6-23
* @return nc.vo.pub.pf.BillStatusEnum
*/
public Integer getApprovestatus() {
return this.approvestatus;
} 

/**
* ����approvestatus��Setter����.������������״̬
* ��������:2019-6-23
* @param newApprovestatus nc.vo.pub.pf.BillStatusEnum
*/
public void setApprovestatus ( Integer approvestatus) {
this.approvestatus=approvestatus;
} 
 
/**
* ���� dbilldate��Getter����.����������������
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getDbilldate() {
return this.dbilldate;
} 

/**
* ����dbilldate��Setter����.����������������
* ��������:2019-6-23
* @param newDbilldate nc.vo.pub.lang.UFDate
*/
public void setDbilldate ( UFDate dbilldate) {
this.dbilldate=dbilldate;
} 
 
/**
* ���� billversiondate��Getter����.���������汾����
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDate
*/
public UFDate getBillversiondate() {
return this.billversiondate;
} 

/**
* ����billversiondate��Setter����.���������汾����
* ��������:2019-6-23
* @param newBillversiondate nc.vo.pub.lang.UFDate
*/
public void setBillversiondate ( UFDate billversiondate) {
this.billversiondate=billversiondate;
} 
 
/**
* ���� pk_project��Getter����.����������Ŀ����
*  ��������:2019-6-23
* @return nc.vo.tg.projectdata.ProjectDataVO
*/
public String getPk_project() {
return this.pk_project;
} 

/**
* ����pk_project��Setter����.����������Ŀ����
* ��������:2019-6-23
* @param newPk_project nc.vo.tg.projectdata.ProjectDataVO
*/
public void setPk_project ( String pk_project) {
this.pk_project=pk_project;
} 
 
/**
* ���� bmain��Getter����.���������Ƿ�����
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFBoolean
*/
public UFBoolean getBmain() {
return this.bmain;
} 

/**
* ����bmain��Setter����.���������Ƿ�����
* ��������:2019-6-23
* @param newBmain nc.vo.pub.lang.UFBoolean
*/
public void setBmain ( UFBoolean bmain) {
this.bmain=bmain;
} 
 
/**
* ���� pk_organizationtype��Getter����.������������������
*  ��������:2019-6-23
* @return nc.vo.tg.fintype.FinTypeVO
*/
public String getPk_organizationtype() {
return this.pk_organizationtype;
} 

/**
* ����pk_organizationtype��Setter����.������������������
* ��������:2019-6-23
* @param newPk_organizationtype nc.vo.tg.fintype.FinTypeVO
*/
public void setPk_organizationtype ( String pk_organizationtype) {
this.pk_organizationtype=pk_organizationtype;
} 
 
/**
* ���� pk_organization��Getter����.�������������ʻ���
*  ��������:2019-6-23
* @return nc.vo.tg.organization.OrganizationVO
*/
public String getPk_organization() {
return this.pk_organization;
} 

/**
* ����pk_organization��Setter����.�������������ʻ���
* ��������:2019-6-23
* @param newPk_organization nc.vo.tg.organization.OrganizationVO
*/
public void setPk_organization ( String pk_organization) {
this.pk_organization=pk_organization;
} 
 
/**
* ���� nmy��Getter����.�������������ʽ��
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDouble
*/
public UFDouble getNmy() {
return this.nmy;
} 

/**
* ����nmy��Setter����.�������������ʽ��
* ��������:2019-6-23
* @param newNmy nc.vo.pub.lang.UFDouble
*/
public void setNmy ( UFDouble nmy) {
this.nmy=nmy;
} 
 
/**
* ���� fiterm��Getter����.������������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getFiterm() {
return this.fiterm;
} 

/**
* ����fiterm��Setter����.������������������
* ��������:2019-6-23
* @param newFiterm java.lang.String
*/
public void setFiterm ( String fiterm) {
this.fiterm=fiterm;
} 
 
/**
* ���� rate��Getter����.������������������
*  ��������:2019-6-23
* @return nc.vo.fi.rateconfig.RateCodeVO
*/
public UFDouble getRate() {
return this.rate;
} 

/**
* ����rate��Setter����.������������������
* ��������:2019-6-23
* @param newRate nc.vo.fi.rateconfig.RateCodeVO
*/
public void setRate ( UFDouble rate) {
this.rate=rate;
} 
 
/**
* ���� zhrate��Getter����.���������������ۺ�����
*  ��������:2019-6-23
* @return java.lang.String
*/
public UFDouble getZhrate() {
return this.zhrate;
} 

/**
* ����zhrate��Setter����.���������������ۺ�����
* ��������:2019-6-23
* @param newZhrate java.lang.String
*/
public void setZhrate ( UFDouble zhrate) {
this.zhrate=zhrate;
} 
 
/**
* ���� ecreditcondition��Getter����.����������������
*  ��������:2019-6-23
* @return nc.vo.tg.fischeme.EcreditconditionEnum
*/
public String getEcreditcondition() {
return this.ecreditcondition;
} 

/**
* ����ecreditcondition��Setter����.����������������
* ��������:2019-6-23
* @param newEcreditcondition nc.vo.tg.fischeme.EcreditconditionEnum
*/
public void setEcreditcondition ( String ecreditcondition) {
this.ecreditcondition=ecreditcondition;
} 
 
/**
* ���� repaymentcond��Getter����.����������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getRepaymentcond() {
return this.repaymentcond;
} 

/**
* ����repaymentcond��Setter����.����������������
* ��������:2019-6-23
* @param newRepaymentcond java.lang.String
*/
public void setRepaymentcond ( String repaymentcond) {
this.repaymentcond=repaymentcond;
} 
 
/**
* ���� followper��Getter����.��������������
*  ��������:2019-6-23
* @return nc.vo.bd.psn.PsndocVO
*/
public String getFollowper() {
return this.followper;
} 

/**
* ����followper��Setter����.��������������
* ��������:2019-6-23
* @param newFollowper nc.vo.bd.psn.PsndocVO
*/
public void setFollowper ( String followper) {
this.followper=followper;
} 
 
/**
* ���� percharge��Getter����.������������������
*  ��������:2019-6-23
* @return nc.vo.bd.psn.PsndocVO
*/
public String getPercharge() {
return this.percharge;
} 

/**
* ����percharge��Setter����.������������������
* ��������:2019-6-23
* @param newPercharge nc.vo.bd.psn.PsndocVO
*/
public void setPercharge ( String percharge) {
this.percharge=percharge;
} 
 
/**
* ���� creator��Getter����.��������������
*  ��������:2019-6-23
* @return nc.vo.sm.UserVO
*/
public String getCreator() {
return this.creator;
} 

/**
* ����creator��Setter����.��������������
* ��������:2019-6-23
* @param newCreator nc.vo.sm.UserVO
*/
public void setCreator ( String creator) {
this.creator=creator;
} 
 
/**
* ���� creationtime��Getter����.������������ʱ��
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getCreationtime() {
return this.creationtime;
} 

/**
* ����creationtime��Setter����.������������ʱ��
* ��������:2019-6-23
* @param newCreationtime nc.vo.pub.lang.UFDateTime
*/
public void setCreationtime ( UFDateTime creationtime) {
this.creationtime=creationtime;
} 
 
/**
* ���� modifier��Getter����.���������޸���
*  ��������:2019-6-23
* @return nc.vo.sm.UserVO
*/
public String getModifier() {
return this.modifier;
} 

/**
* ����modifier��Setter����.���������޸���
* ��������:2019-6-23
* @param newModifier nc.vo.sm.UserVO
*/
public void setModifier ( String modifier) {
this.modifier=modifier;
} 
 
/**
* ���� modifiedtime��Getter����.���������޸�ʱ��
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getModifiedtime() {
return this.modifiedtime;
} 

/**
* ����modifiedtime��Setter����.���������޸�ʱ��
* ��������:2019-6-23
* @param newModifiedtime nc.vo.pub.lang.UFDateTime
*/
public void setModifiedtime ( UFDateTime modifiedtime) {
this.modifiedtime=modifiedtime;
} 
 
/**
* ���� busitype��Getter����.��������ҵ������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getBusitype() {
return this.busitype;
} 

/**
* ����busitype��Setter����.��������ҵ������
* ��������:2019-6-23
* @param newBusitype java.lang.String
*/
public void setBusitype ( String busitype) {
this.busitype=busitype;
} 
 
/**
* ���� approver��Getter����.��������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getApprover() {
return this.approver;
} 

/**
* ����approver��Setter����.��������������
* ��������:2019-6-23
* @param newApprover java.lang.String
*/
public void setApprover ( String approver) {
this.approver=approver;
} 
 
/**
* ���� approvenote��Getter����.����������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getApprovenote() {
return this.approvenote;
} 

/**
* ����approvenote��Setter����.����������������
* ��������:2019-6-23
* @param newApprovenote java.lang.String
*/
public void setApprovenote ( String approvenote) {
this.approvenote=approvenote;
} 
 
/**
* ���� approvedate��Getter����.������������ʱ��
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getApprovedate() {
return this.approvedate;
} 

/**
* ����approvedate��Setter����.������������ʱ��
* ��������:2019-6-23
* @param newApprovedate nc.vo.pub.lang.UFDateTime
*/
public void setApprovedate ( UFDateTime approvedate) {
this.approvedate=approvedate;
} 
 
/**
* ���� transtype��Getter����.����������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getTranstype() {
return this.transtype;
} 

/**
* ����transtype��Setter����.����������������
* ��������:2019-6-23
* @param newTranstype java.lang.String
*/
public void setTranstype ( String transtype) {
this.transtype=transtype;
} 
 
/**
* ���� billtype��Getter����.����������������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getBilltype() {
return this.billtype;
} 

/**
* ����billtype��Setter����.����������������
* ��������:2019-6-23
* @param newBilltype java.lang.String
*/
public void setBilltype ( String billtype) {
this.billtype=billtype;
} 
 
/**
* ���� transtypepk��Getter����.����������������pk
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getTranstypepk() {
return this.transtypepk;
} 

/**
* ����transtypepk��Setter����.����������������pk
* ��������:2019-6-23
* @param newTranstypepk java.lang.String
*/
public void setTranstypepk ( String transtypepk) {
this.transtypepk=transtypepk;
} 
 
/**
* ���� srcbilltype��Getter����.����������Դ��������
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getSrcbilltype() {
return this.srcbilltype;
} 

/**
* ����srcbilltype��Setter����.����������Դ��������
* ��������:2019-6-23
* @param newSrcbilltype java.lang.String
*/
public void setSrcbilltype ( String srcbilltype) {
this.srcbilltype=srcbilltype;
} 
 
/**
* ���� srcbillid��Getter����.����������Դ����id
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getSrcbillid() {
return this.srcbillid;
} 

/**
* ����srcbillid��Setter����.����������Դ����id
* ��������:2019-6-23
* @param newSrcbillid java.lang.String
*/
public void setSrcbillid ( String srcbillid) {
this.srcbillid=srcbillid;
} 
 
/**
* ���� emendenum��Getter����.���������汾��
*  ��������:2019-6-23
* @return java.lang.Integer
*/
public Integer getEmendenum() {
return this.emendenum;
} 

/**
* ����emendenum��Setter����.���������汾��
* ��������:2019-6-23
* @param newEmendenum java.lang.Integer
*/
public void setEmendenum ( Integer emendenum) {
this.emendenum=emendenum;
} 
 
/**
* ���� pk_scheme��Getter����.����������Դ����pk
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getPk_scheme() {
return this.pk_scheme;
} 

/**
* ����pk_scheme��Setter����.����������Դ����pk
* ��������:2019-6-23
* @param newPk_scheme java.lang.String
*/
public void setPk_scheme ( String pk_scheme) {
this.pk_scheme=pk_scheme;
} 
 
/**
* ���� vdef1��Getter����.���������Զ�����1
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef1() {
return this.vdef1;
} 

/**
* ����vdef1��Setter����.���������Զ�����1
* ��������:2019-6-23
* @param newVdef1 java.lang.String
*/
public void setVdef1 ( String vdef1) {
this.vdef1=vdef1;
} 
 
/**
* ���� vdef2��Getter����.���������Զ�����2
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef2() {
return this.vdef2;
} 

/**
* ����vdef2��Setter����.���������Զ�����2
* ��������:2019-6-23
* @param newVdef2 java.lang.String
*/
public void setVdef2 ( String vdef2) {
this.vdef2=vdef2;
} 
 
/**
* ���� vdef3��Getter����.���������Զ�����3
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef3() {
return this.vdef3;
} 

/**
* ����vdef3��Setter����.���������Զ�����3
* ��������:2019-6-23
* @param newVdef3 java.lang.String
*/
public void setVdef3 ( String vdef3) {
this.vdef3=vdef3;
} 
 
/**
* ���� vdef4��Getter����.���������Զ�����4
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef4() {
return this.vdef4;
} 

/**
* ����vdef4��Setter����.���������Զ�����4
* ��������:2019-6-23
* @param newVdef4 java.lang.String
*/
public void setVdef4 ( String vdef4) {
this.vdef4=vdef4;
} 
 
/**
* ���� vdef5��Getter����.���������Զ�����5
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef5() {
return this.vdef5;
} 

/**
* ����vdef5��Setter����.���������Զ�����5
* ��������:2019-6-23
* @param newVdef5 java.lang.String
*/
public void setVdef5 ( String vdef5) {
this.vdef5=vdef5;
} 
 
/**
* ���� vdef6��Getter����.���������Զ�����6
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef6() {
return this.vdef6;
} 

/**
* ����vdef6��Setter����.���������Զ�����6
* ��������:2019-6-23
* @param newVdef6 java.lang.String
*/
public void setVdef6 ( String vdef6) {
this.vdef6=vdef6;
} 
 
/**
* ���� vdef7��Getter����.���������Զ�����7
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef7() {
return this.vdef7;
} 

/**
* ����vdef7��Setter����.���������Զ�����7
* ��������:2019-6-23
* @param newVdef7 java.lang.String
*/
public void setVdef7 ( String vdef7) {
this.vdef7=vdef7;
} 
 
/**
* ���� vdef8��Getter����.���������Զ�����8
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef8() {
return this.vdef8;
} 

/**
* ����vdef8��Setter����.���������Զ�����8
* ��������:2019-6-23
* @param newVdef8 java.lang.String
*/
public void setVdef8 ( String vdef8) {
this.vdef8=vdef8;
} 
 
/**
* ���� vdef9��Getter����.���������Զ�����9
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef9() {
return this.vdef9;
} 

/**
* ����vdef9��Setter����.���������Զ�����9
* ��������:2019-6-23
* @param newVdef9 java.lang.String
*/
public void setVdef9 ( String vdef9) {
this.vdef9=vdef9;
} 
 
/**
* ���� vdef10��Getter����.���������Զ�����10
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef10() {
return this.vdef10;
} 

/**
* ����vdef10��Setter����.���������Զ�����10
* ��������:2019-6-23
* @param newVdef10 java.lang.String
*/
public void setVdef10 ( String vdef10) {
this.vdef10=vdef10;
} 
 
/**
* ���� vdef11��Getter����.���������Զ�����11
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef11() {
return this.vdef11;
} 

/**
* ����vdef11��Setter����.���������Զ�����11
* ��������:2019-6-23
* @param newVdef11 java.lang.String
*/
public void setVdef11 ( String vdef11) {
this.vdef11=vdef11;
} 
 
/**
* ���� vdef12��Getter����.���������Զ�����12
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef12() {
return this.vdef12;
} 

/**
* ����vdef12��Setter����.���������Զ�����12
* ��������:2019-6-23
* @param newVdef12 java.lang.String
*/
public void setVdef12 ( String vdef12) {
this.vdef12=vdef12;
} 
 
/**
* ���� vdef13��Getter����.���������Զ�����13
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef13() {
return this.vdef13;
} 

/**
* ����vdef13��Setter����.���������Զ�����13
* ��������:2019-6-23
* @param newVdef13 java.lang.String
*/
public void setVdef13 ( String vdef13) {
this.vdef13=vdef13;
} 
 
/**
* ���� vdef14��Getter����.���������Զ�����14
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef14() {
return this.vdef14;
} 

/**
* ����vdef14��Setter����.���������Զ�����14
* ��������:2019-6-23
* @param newVdef14 java.lang.String
*/
public void setVdef14 ( String vdef14) {
this.vdef14=vdef14;
} 
 
/**
* ���� vdef15��Getter����.���������Զ�����15
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef15() {
return this.vdef15;
} 

/**
* ����vdef15��Setter����.���������Զ�����15
* ��������:2019-6-23
* @param newVdef15 java.lang.String
*/
public void setVdef15 ( String vdef15) {
this.vdef15=vdef15;
} 
 
/**
* ���� vdef16��Getter����.���������Զ�����16
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef16() {
return this.vdef16;
} 

/**
* ����vdef16��Setter����.���������Զ�����16
* ��������:2019-6-23
* @param newVdef16 java.lang.String
*/
public void setVdef16 ( String vdef16) {
this.vdef16=vdef16;
} 
 
/**
* ���� vdef17��Getter����.���������Զ�����17
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef17() {
return this.vdef17;
} 

/**
* ����vdef17��Setter����.���������Զ�����17
* ��������:2019-6-23
* @param newVdef17 java.lang.String
*/
public void setVdef17 ( String vdef17) {
this.vdef17=vdef17;
} 
 
/**
* ���� vdef18��Getter����.���������Զ�����18
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef18() {
return this.vdef18;
} 

/**
* ����vdef18��Setter����.���������Զ�����18
* ��������:2019-6-23
* @param newVdef18 java.lang.String
*/
public void setVdef18 ( String vdef18) {
this.vdef18=vdef18;
} 
 
/**
* ���� vdef19��Getter����.���������Զ�����19
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef19() {
return this.vdef19;
} 

/**
* ����vdef19��Setter����.���������Զ�����19
* ��������:2019-6-23
* @param newVdef19 java.lang.String
*/
public void setVdef19 ( String vdef19) {
this.vdef19=vdef19;
} 
 
/**
* ���� vdef20��Getter����.���������Զ�����20
*  ��������:2019-6-23
* @return java.lang.String
*/
public String getVdef20() {
return this.vdef20;
} 

/**
* ����vdef20��Setter����.���������Զ�����20
* ��������:2019-6-23
* @param newVdef20 java.lang.String
*/
public void setVdef20 ( String vdef20) {
this.vdef20=vdef20;
} 
 
/**
* ���� ����ʱ�����Getter����.��������ʱ���
*  ��������:2019-6-23
* @return nc.vo.pub.lang.UFDateTime
*/
public UFDateTime getTs() {
return this.ts;
}
/**
* ��������ʱ�����Setter����.��������ʱ���
* ��������:2019-6-23
* @param newts nc.vo.pub.lang.UFDateTime
*/
public void setTs(UFDateTime ts){
this.ts=ts;
} 
     
    @Override
    public IVOMeta getMetaData() {
    return VOMetaFactory.getInstance().getVOMeta("tg.tgrz_fiscemeversion");
    }
   }
    