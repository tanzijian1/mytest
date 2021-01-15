package nc.vo.tg.financingexpense;

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
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2020-4-5
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class FinancexpenseVO extends SuperVO {

	/**
	 * ��������
	 */
	public java.lang.String pk_finexpense;
	/**
	 * ����
	 */
	public java.lang.String pk_group;
	/**
	 * ��֯
	 */
	public java.lang.String pk_org;
	/**
	 * ��֯�汾
	 */
	public java.lang.String pk_org_v;
	/**
	 * ����ID
	 */
	public java.lang.String billid;
	/**
	 * ���ݺ�
	 */
	public java.lang.String billno;
	/**
	 * ��������
	 */
	public UFDate dbilldate;
	/**
	 * ����״̬
	 */
	public java.lang.Integer approvestatus;
	/**
	 * �Ƿ����
	 */
	public UFBoolean isqk;
	/**
	 * ������Ϣ
	 */
	public java.lang.String message;
	/**
	 * ���벿��
	 */
	public java.lang.String pk_applicationdept;
	/**
	 * ����
	 */
	public java.lang.String title;
	/**
	 * ������
	 */
	public java.lang.String pk_applicant;
	/**
	 * ��������
	 */
	public UFDate applicationdate;
	/**
	 * ���빫˾
	 */
	public java.lang.String pk_applicationorg;
	/**
	 * ҵ����Ϣ
	 */
	public java.lang.String businessmsg;
	/**
	 * ��Ŀ����
	 */
	public java.lang.String pk_project;
	/**
	 * �տλ
	 */
	public java.lang.String pk_payee;
	/**
	 * ���λ
	 */
	public java.lang.String pk_payer;
	/**
	 * ��ͬ���
	 */
	public nc.vo.pub.lang.UFDouble contractmoney;
	/**
	 * �ۼ��Ѹ�����
	 */
	public nc.vo.pub.lang.UFDouble paymentamount;
	/**
	 * ���������
	 */
	public nc.vo.pub.lang.UFDouble applyamount;
	/**
	 * ���Ż��
	 */
	public java.lang.String pk_accountant;
	/**
	 * ����
	 */
	public java.lang.String pk_cashier;
	/**
	 * �ÿ�����
	 */
	public java.lang.String usecontent;
	/**
	 * ������
	 */
	public java.lang.String creator;
	/**
	 * ����ʱ��
	 */
	public UFDateTime creationtime;
	/**
	 * �޸���
	 */
	public java.lang.String modifier;
	/**
	 * �޸�ʱ��
	 */
	public UFDateTime modifiedtime;
	/**
	 * ҵ������
	 */
	public java.lang.String busitype;
	/**
	 * ������
	 */
	public java.lang.String approver;
	/**
	 * ��������
	 */
	public java.lang.String approvenote;
	/**
	 * ����ʱ��
	 */
	public UFDateTime approvedate;
	/**
	 * ��������
	 */
	public java.lang.String transtype;
	/**
	 * ��������
	 */
	public java.lang.String billtype;
	/**
	 * ��������pk
	 */
	public java.lang.String transtypepk;
	/**
	 * �Ƿ񸶿��Ʊ
	 */
	public nc.vo.pub.lang.UFDouble ispay;
	/**
	 * ժҪ
	 */
	public java.lang.String summary;
	/**
	 * ���񲿷�Ʊ��Ա
	 */
	public java.lang.String noteman;
	/**
	 * ����������Ա˵��
	 */
	public java.lang.String explain;
	/**
	 * ��ŵ�黹��Ʊ����
	 */
	public UFDate notedate;
	/**
	 * ��ŵ�黹��Ʊ����˵��
	 */
	public java.lang.String noteexplain;
	/**
	 * δȡ�÷�Ʊԭ��
	 */
	public java.lang.String reason;
	/**
	 * ��Դ��������
	 */
	public java.lang.String srcbilltype;
	/**
	 * ��Դ����id
	 */
	public java.lang.String srcbillid;
	/**
	 * �޶�ö��
	 */
	public java.lang.Integer emendenum;
	/**
	 * ���ݰ汾pk
	 */
	public java.lang.String billversionpk;
	/**
	 * �Զ�����1
	 */
	public java.lang.String def1;
	/**
	 * �Զ�����2
	 */
	public java.lang.String def2;
	/**
	 * �Զ�����3
	 */
	public java.lang.String def3;
	/**
	 * �Զ�����4
	 */
	public java.lang.String def4;
	/**
	 * �Զ�����5
	 */
	public java.lang.String def5;
	/**
	 * �Զ�����6
	 */
	public java.lang.String def6;
	/**
	 * �Զ�����7
	 */
	public java.lang.String def7;
	/**
	 * �Զ�����8
	 */
	public java.lang.String def8;
	/**
	 * �Զ�����9
	 */
	public java.lang.String def9;
	/**
	 * �Զ�����10
	 */
	public java.lang.String def10;
	/**
	 * �Զ�����11
	 */
	public java.lang.String def11;
	/**
	 * �Զ�����12
	 */
	public java.lang.String def12;
	/**
	 * �Զ�����13
	 */
	public java.lang.String def13;
	/**
	 * �Զ�����14
	 */
	public java.lang.String def14;
	/**
	 * �Զ�����15
	 */
	public java.lang.String def15;
	/**
	 * �Զ�����16
	 */
	public java.lang.String def16;
	/**
	 * �Զ�����17
	 */
	public java.lang.String def17;
	/**
	 * �Զ�����18
	 */
	public java.lang.String def18;
	/**
	 * �Զ�����19
	 */
	public java.lang.String def19;
	/**
	 * �Զ�����20
	 */
	public java.lang.String def20;
	/**
	 * �Զ�����21
	 */
	public java.lang.String def21;
	/**
	 * �Զ�����22
	 */
	public java.lang.String def22;
	/**
	 * �Զ�����23
	 */
	public java.lang.String def23;
	/**
	 * �Զ�����24
	 */
	public java.lang.String def24;
	/**
	 * �Զ�����25
	 */
	public java.lang.String def25;
	/**
	 * �Զ�����26
	 */
	public java.lang.String def26;
	/**
	 * �Զ�����27
	 */
	public java.lang.String def27;
	/**
	 * �Զ�����28
	 */
	public java.lang.String def28;
	/**
	 * �Զ�����29
	 */
	public java.lang.String def29;
	/**
	 * �Զ�����30
	 */
	public java.lang.String def30;
	/**
	 * �Զ�����31
	 */
	public java.lang.String def31;
	/**
	 * �Զ�����32
	 */
	public java.lang.String def32;
	/**
	 * �Զ�����33
	 */
	public java.lang.String def33;
	/**
	 * �Զ�����34
	 */
	public java.lang.String def34;
	/**
	 * �Զ�����35
	 */
	public java.lang.String def35;
	/**
	 * �Զ�����36
	 */
	public java.lang.String def36;
	/**
	 * �Զ�����37
	 */
	public java.lang.String def37;
	/**
	 * �Զ�����38
	 */
	public java.lang.String def38;
	/**
	 * �Զ�����39
	 */
	public java.lang.String def39;
	/**
	 * �Զ�����40
	 */
	public java.lang.String def40;
	/**
	 * �Զ�����41
	 */
	public java.lang.String def41;
	/**
	 * �Զ�����42
	 */
	public java.lang.String def42;
	/**
	 * �Զ�����43
	 */
	public java.lang.String def43;
	/**
	 * �Զ�����44
	 */
	public java.lang.String def44;
	/**
	 * �Զ�����45
	 */
	public java.lang.String def45;
	/**
	 * �Զ�����46
	 */
	public java.lang.String def46;
	/**
	 * �Զ�����47
	 */
	public java.lang.String def47;
	/**
	 * �Զ�����48
	 */
	public java.lang.String def48;
	/**
	 * �Զ�����49
	 */
	public java.lang.String def49;
	/**
	 * �Զ�����50
	 */
	public java.lang.String def50;
	/**
	 * �Զ�����51
	 */
	public java.lang.String def51;
	/**
	 * �Զ�����52
	 */
	public java.lang.String def52;
	/**
	 * �Զ�����53
	 */
	public java.lang.String def53;
	/**
	 * �Զ�����54
	 */
	public java.lang.String def54;
	/**
	 * �Զ�����55
	 */
	public java.lang.String def55;
	/**
	 * �Զ�����56
	 */
	public java.lang.String def56;
	/**
	 * �Զ�����57
	 */
	public java.lang.String def57;
	/**
	 * �Զ�����58
	 */
	public java.lang.String def58;
	/**
	 * �Զ�����59
	 */
	public java.lang.String def59;
	/**
	 * �Զ�����60
	 */
	public java.lang.String def60;
	/**
	 * �Զ�����ı�a
	 */
	public java.lang.String big_text_a;
	/**
	 * �Զ�����ı�b
	 */
	public java.lang.String big_text_b;
	/**
	 * �Զ�����ı�c
	 */
	public java.lang.String big_text_c;
	/**
	 * �Զ�����61
	 */
	public java.lang.String def61;
	/**
	 * �Զ�����62
	 */
	public java.lang.String def62;
	/**
	 * �Զ�����63
	 */
	public java.lang.String def63;
	/**
	 * �Զ�����64
	 */
	public java.lang.String def64;
	/**
	 * �Զ�����65
	 */
	public java.lang.String def65;
	/**
	 * �Զ�����66
	 */
	public java.lang.String def66;
	/**
	 * �Զ�����67
	 */
	public java.lang.String def67;
	/**
	 * �Զ�����68
	 */
	public java.lang.String def68;
	/**
	 * �Զ�����69
	 */
	public java.lang.String def69;
	/**
	 * �Զ�����70
	 */
	public java.lang.String def70;
	/**
	 * �Զ�����71
	 */
	public java.lang.String def71;
	/**
	 * �Զ�����72
	 */
	public java.lang.String def72;
	/**
	 * �Զ�����73
	 */
	public java.lang.String def73;
	/**
	 * �Զ�����74
	 */
	public java.lang.String def74;
	/**
	 * �Զ�����75
	 */
	public java.lang.String def75;
	/**
	 * �Զ�����76
	 */
	public java.lang.String def76;
	/**
	 * �Զ�����77
	 */
	public java.lang.String def77;
	/**
	 * �Զ�����78
	 */
	public java.lang.String def78;
	/**
	 * �Զ�����79
	 */
	public java.lang.String def79;
	/**
	 * �Զ�����80
	 */
	public java.lang.String def80;
	/**
	 * �Զ�����81
	 */
	public java.lang.String def81;
	/**
	 * �Զ�����82
	 */
	public java.lang.String def82;
	/**
	 * �Զ�����83
	 */
	public java.lang.String def83;
	/**
	 * �Զ�����84
	 */
	public java.lang.String def84;
	/**
	 * �Զ�����85
	 */
	public java.lang.String def85;
	/**
	 * �Զ�����86
	 */
	public java.lang.String def86;
	/**
	 * �Զ�����87
	 */
	public java.lang.String def87;
	/**
	 * �Զ�����88
	 */
	public java.lang.String def88;
	/**
	 * �Զ�����89
	 */
	public java.lang.String def89;
	/**
	 * �Զ�����90
	 */
	public java.lang.String def90;
	/**
	 * �Զ�����91
	 */
	public java.lang.String def91;
	/**
	 * �Զ�����92
	 */
	public java.lang.String def92;
	/**
	 * �Զ�����93
	 */
	public java.lang.String def93;
	/**
	 * �Զ�����94
	 */
	public java.lang.String def94;
	/**
	 * �Զ�����95
	 */
	public java.lang.String def95;
	/**
	 * �Զ�����96
	 */
	public java.lang.String def96;
	/**
	 * �Զ�����97
	 */
	public java.lang.String def97;
	/**
	 * �Զ�����98
	 */
	public java.lang.String def98;
	/**
	 * �Զ�����99
	 */
	public java.lang.String def99;
	/**
	 * �Զ�����100
	 */
	public java.lang.String def100;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * drֵ
	 */
	public Integer dr;

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * ���� pk_finexpense��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_finexpense() {
		return this.pk_finexpense;
	}

	/**
	 * ����pk_finexpense��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newPk_finexpense
	 *            java.lang.String
	 */
	public void setPk_finexpense(java.lang.String pk_finexpense) {
		this.pk_finexpense = pk_finexpense;
	}

	/**
	 * ���� pk_group��Getter����.������������ ��������:2020-4-5
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ����pk_group��Setter����.������������ ��������:2020-4-5
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ���� pk_org��Getter����.����������֯ ��������:2020-4-5
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ����pk_org��Setter����.����������֯ ��������:2020-4-5
	 * 
	 * @param newPk_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ���� pk_org_v��Getter����.����������֯�汾 ��������:2020-4-5
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.����������֯�汾 ��������:2020-4-5
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ���� billid��Getter����.������������ID ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillid() {
		return this.billid;
	}

	/**
	 * ����billid��Setter����.������������ID ��������:2020-4-5
	 * 
	 * @param newBillid
	 *            java.lang.String
	 */
	public void setBillid(java.lang.String billid) {
		this.billid = billid;
	}

	/**
	 * ���� billno��Getter����.�����������ݺ� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillno() {
		return this.billno;
	}

	/**
	 * ����billno��Setter����.�����������ݺ� ��������:2020-4-5
	 * 
	 * @param newBillno
	 *            java.lang.String
	 */
	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	/**
	 * ���� dbilldate��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getDbilldate() {
		return this.dbilldate;
	}

	/**
	 * ����dbilldate��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newDbilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setDbilldate(UFDate dbilldate) {
		this.dbilldate = dbilldate;
	}

	/**
	 * ���� approvestatus��Getter����.������������״̬ ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.pf.BillStatusEnum
	 */
	public java.lang.Integer getApprovestatus() {
		return this.approvestatus;
	}

	/**
	 * ����approvestatus��Setter����.������������״̬ ��������:2020-4-5
	 * 
	 * @param newApprovestatus
	 *            nc.vo.pub.pf.BillStatusEnum
	 */
	public void setApprovestatus(java.lang.Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	/**
	 * ���� isqk��Getter����.���������Ƿ���� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getIsqk() {
		return this.isqk;
	}

	/**
	 * ����isqk��Setter����.���������Ƿ���� ��������:2020-4-5
	 * 
	 * @param newIsqk
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setIsqk(UFBoolean isqk) {
		this.isqk = isqk;
	}

	/**
	 * ���� message��Getter����.��������������Ϣ ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getMessage() {
		return this.message;
	}

	/**
	 * ����message��Setter����.��������������Ϣ ��������:2020-4-5
	 * 
	 * @param newMessage
	 *            java.lang.String
	 */
	public void setMessage(java.lang.String message) {
		this.message = message;
	}

	/**
	 * ���� pk_applicationdept��Getter����.�����������벿�� ��������:2020-4-5
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPk_applicationdept() {
		return this.pk_applicationdept;
	}

	/**
	 * ����pk_applicationdept��Setter����.�����������벿�� ��������:2020-4-5
	 * 
	 * @param newPk_applicationdept
	 *            nc.vo.org.DeptVO
	 */
	public void setPk_applicationdept(java.lang.String pk_applicationdept) {
		this.pk_applicationdept = pk_applicationdept;
	}

	/**
	 * ���� title��Getter����.������������ ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTitle() {
		return this.title;
	}

	/**
	 * ����title��Setter����.������������ ��������:2020-4-5
	 * 
	 * @param newTitle
	 *            java.lang.String
	 */
	public void setTitle(java.lang.String title) {
		this.title = title;
	}

	/**
	 * ���� pk_applicant��Getter����.�������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_applicant() {
		return this.pk_applicant;
	}

	/**
	 * ����pk_applicant��Setter����.�������������� ��������:2020-4-5
	 * 
	 * @param newPk_applicant
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_applicant(java.lang.String pk_applicant) {
		this.pk_applicant = pk_applicant;
	}

	/**
	 * ���� applicationdate��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getApplicationdate() {
		return this.applicationdate;
	}

	/**
	 * ����applicationdate��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newApplicationdate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setApplicationdate(UFDate applicationdate) {
		this.applicationdate = applicationdate;
	}

	/**
	 * ���� pk_applicationorg��Getter����.�����������빫˾ ��������:2020-4-5
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_applicationorg() {
		return this.pk_applicationorg;
	}

	/**
	 * ����pk_applicationorg��Setter����.�����������빫˾ ��������:2020-4-5
	 * 
	 * @param newPk_applicationorg
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_applicationorg(java.lang.String pk_applicationorg) {
		this.pk_applicationorg = pk_applicationorg;
	}

	/**
	 * ���� businessmsg��Getter����.��������ҵ����Ϣ ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBusinessmsg() {
		return this.businessmsg;
	}

	/**
	 * ����businessmsg��Setter����.��������ҵ����Ϣ ��������:2020-4-5
	 * 
	 * @param newBusinessmsg
	 *            java.lang.String
	 */
	public void setBusinessmsg(java.lang.String businessmsg) {
		this.businessmsg = businessmsg;
	}

	/**
	 * ���� pk_project��Getter����.����������Ŀ���� ��������:2020-4-5
	 * 
	 * @return nc.vo.tg.projectdata.ProjectDataVO
	 */
	public java.lang.String getPk_project() {
		return this.pk_project;
	}

	/**
	 * ����pk_project��Setter����.����������Ŀ���� ��������:2020-4-5
	 * 
	 * @param newPk_project
	 *            nc.vo.tg.projectdata.ProjectDataVO
	 */
	public void setPk_project(java.lang.String pk_project) {
		this.pk_project = pk_project;
	}

	/**
	 * ���� pk_payee��Getter����.���������տλ ��������:2020-4-5
	 * 
	 * @return nc.vo.bd.cust.CustSupplierVO
	 */
	public java.lang.String getPk_payee() {
		return this.pk_payee;
	}

	/**
	 * ����pk_payee��Setter����.���������տλ ��������:2020-4-5
	 * 
	 * @param newPk_payee
	 *            nc.vo.bd.cust.CustSupplierVO
	 */
	public void setPk_payee(java.lang.String pk_payee) {
		this.pk_payee = pk_payee;
	}

	/**
	 * ���� pk_payer��Getter����.�����������λ ��������:2020-4-5
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_payer() {
		return this.pk_payer;
	}

	/**
	 * ����pk_payer��Setter����.�����������λ ��������:2020-4-5
	 * 
	 * @param newPk_payer
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_payer(java.lang.String pk_payer) {
		this.pk_payer = pk_payer;
	}

	/**
	 * ���� contractmoney��Getter����.����������ͬ��� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getContractmoney() {
		return this.contractmoney;
	}

	/**
	 * ����contractmoney��Setter����.����������ͬ��� ��������:2020-4-5
	 * 
	 * @param newContractmoney
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setContractmoney(nc.vo.pub.lang.UFDouble contractmoney) {
		this.contractmoney = contractmoney;
	}

	/**
	 * ���� paymentamount��Getter����.���������ۼ��Ѹ����� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPaymentamount() {
		return this.paymentamount;
	}

	/**
	 * ����paymentamount��Setter����.���������ۼ��Ѹ����� ��������:2020-4-5
	 * 
	 * @param newPaymentamount
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPaymentamount(nc.vo.pub.lang.UFDouble paymentamount) {
		this.paymentamount = paymentamount;
	}

	/**
	 * ���� applyamount��Getter����.����������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getApplyamount() {
		return this.applyamount;
	}

	/**
	 * ����applyamount��Setter����.����������������� ��������:2020-4-5
	 * 
	 * @param newApplyamount
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setApplyamount(nc.vo.pub.lang.UFDouble applyamount) {
		this.applyamount = applyamount;
	}

	/**
	 * ���� pk_accountant��Getter����.�����������Ż�� ��������:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_accountant() {
		return this.pk_accountant;
	}

	/**
	 * ����pk_accountant��Setter����.�����������Ż�� ��������:2020-4-5
	 * 
	 * @param newPk_accountant
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_accountant(java.lang.String pk_accountant) {
		this.pk_accountant = pk_accountant;
	}

	/**
	 * ���� pk_cashier��Getter����.������������ ��������:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_cashier() {
		return this.pk_cashier;
	}

	/**
	 * ����pk_cashier��Setter����.������������ ��������:2020-4-5
	 * 
	 * @param newPk_cashier
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_cashier(java.lang.String pk_cashier) {
		this.pk_cashier = pk_cashier;
	}

	/**
	 * ���� usecontent��Getter����.���������ÿ����� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getUsecontent() {
		return this.usecontent;
	}

	/**
	 * ����usecontent��Setter����.���������ÿ����� ��������:2020-4-5
	 * 
	 * @param newUsecontent
	 *            java.lang.String
	 */
	public void setUsecontent(java.lang.String usecontent) {
		this.usecontent = usecontent;
	}

	/**
	 * ���� creator��Getter����.�������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * ����creator��Setter����.�������������� ��������:2020-4-5
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * ���� creationtime��Getter����.������������ʱ�� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * ����creationtime��Setter����.������������ʱ�� ��������:2020-4-5
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * ���� modifier��Getter����.���������޸��� ��������:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * ����modifier��Setter����.���������޸��� ��������:2020-4-5
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ�� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ�� ��������:2020-4-5
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * ���� busitype��Getter����.��������ҵ������ ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBusitype() {
		return this.busitype;
	}

	/**
	 * ����busitype��Setter����.��������ҵ������ ��������:2020-4-5
	 * 
	 * @param newBusitype
	 *            java.lang.String
	 */
	public void setBusitype(java.lang.String busitype) {
		this.busitype = busitype;
	}

	/**
	 * ���� approver��Getter����.�������������� ��������:2020-4-5
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getApprover() {
		return this.approver;
	}

	/**
	 * ����approver��Setter����.�������������� ��������:2020-4-5
	 * 
	 * @param newApprover
	 *            nc.vo.sm.UserVO
	 */
	public void setApprover(java.lang.String approver) {
		this.approver = approver;
	}

	/**
	 * ���� approvenote��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getApprovenote() {
		return this.approvenote;
	}

	/**
	 * ����approvenote��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newApprovenote
	 *            java.lang.String
	 */
	public void setApprovenote(java.lang.String approvenote) {
		this.approvenote = approvenote;
	}

	/**
	 * ���� approvedate��Getter����.������������ʱ�� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getApprovedate() {
		return this.approvedate;
	}

	/**
	 * ����approvedate��Setter����.������������ʱ�� ��������:2020-4-5
	 * 
	 * @param newApprovedate
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.approvedate = approvedate;
	}

	/**
	 * ���� transtype��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTranstype() {
		return this.transtype;
	}

	/**
	 * ����transtype��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newTranstype
	 *            java.lang.String
	 */
	public void setTranstype(java.lang.String transtype) {
		this.transtype = transtype;
	}

	/**
	 * ���� billtype��Getter����.���������������� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBilltype() {
		return this.billtype;
	}

	/**
	 * ����billtype��Setter����.���������������� ��������:2020-4-5
	 * 
	 * @param newBilltype
	 *            java.lang.String
	 */
	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	/**
	 * ���� transtypepk��Getter����.����������������pk ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.billtype.BilltypeVO
	 */
	public java.lang.String getTranstypepk() {
		return this.transtypepk;
	}

	/**
	 * ����transtypepk��Setter����.����������������pk ��������:2020-4-5
	 * 
	 * @param newTranstypepk
	 *            nc.vo.pub.billtype.BilltypeVO
	 */
	public void setTranstypepk(java.lang.String transtypepk) {
		this.transtypepk = transtypepk;
	}

	/**
	 * ���� ispay��Getter����.���������Ƿ񸶿��Ʊ ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getIspay() {
		return this.ispay;
	}

	/**
	 * ����ispay��Setter����.���������Ƿ񸶿��Ʊ ��������:2020-4-5
	 * 
	 * @param newIspay
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setIspay(nc.vo.pub.lang.UFDouble ispay) {
		this.ispay = ispay;
	}

	/**
	 * ���� summary��Getter����.��������ժҪ ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSummary() {
		return this.summary;
	}

	/**
	 * ����summary��Setter����.��������ժҪ ��������:2020-4-5
	 * 
	 * @param newSummary
	 *            java.lang.String
	 */
	public void setSummary(java.lang.String summary) {
		this.summary = summary;
	}

	/**
	 * ���� noteman��Getter����.�����������񲿷�Ʊ��Ա ��������:2020-4-5
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getNoteman() {
		return this.noteman;
	}

	/**
	 * ����noteman��Setter����.�����������񲿷�Ʊ��Ա ��������:2020-4-5
	 * 
	 * @param newNoteman
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setNoteman(java.lang.String noteman) {
		this.noteman = noteman;
	}

	/**
	 * ���� explain��Getter����.������������������Ա˵�� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getExplain() {
		return this.explain;
	}

	/**
	 * ����explain��Setter����.������������������Ա˵�� ��������:2020-4-5
	 * 
	 * @param newExplain
	 *            java.lang.String
	 */
	public void setExplain(java.lang.String explain) {
		this.explain = explain;
	}

	/**
	 * ���� notedate��Getter����.����������ŵ�黹��Ʊ���� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getNotedate() {
		return this.notedate;
	}

	/**
	 * ����notedate��Setter����.����������ŵ�黹��Ʊ���� ��������:2020-4-5
	 * 
	 * @param newNotedate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setNotedate(UFDate notedate) {
		this.notedate = notedate;
	}

	/**
	 * ���� noteexplain��Getter����.����������ŵ�黹��Ʊ����˵�� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getNoteexplain() {
		return this.noteexplain;
	}

	/**
	 * ����noteexplain��Setter����.����������ŵ�黹��Ʊ����˵�� ��������:2020-4-5
	 * 
	 * @param newNoteexplain
	 *            java.lang.String
	 */
	public void setNoteexplain(java.lang.String noteexplain) {
		this.noteexplain = noteexplain;
	}

	/**
	 * ���� reason��Getter����.��������δȡ�÷�Ʊԭ�� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getReason() {
		return this.reason;
	}

	/**
	 * ����reason��Setter����.��������δȡ�÷�Ʊԭ�� ��������:2020-4-5
	 * 
	 * @param newReason
	 *            java.lang.String
	 */
	public void setReason(java.lang.String reason) {
		this.reason = reason;
	}

	/**
	 * ���� srcbilltype��Getter����.����������Դ�������� ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbilltype() {
		return this.srcbilltype;
	}

	/**
	 * ����srcbilltype��Setter����.����������Դ�������� ��������:2020-4-5
	 * 
	 * @param newSrcbilltype
	 *            java.lang.String
	 */
	public void setSrcbilltype(java.lang.String srcbilltype) {
		this.srcbilltype = srcbilltype;
	}

	/**
	 * ���� srcbillid��Getter����.����������Դ����id ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrcbillid() {
		return this.srcbillid;
	}

	/**
	 * ����srcbillid��Setter����.����������Դ����id ��������:2020-4-5
	 * 
	 * @param newSrcbillid
	 *            java.lang.String
	 */
	public void setSrcbillid(java.lang.String srcbillid) {
		this.srcbillid = srcbillid;
	}

	/**
	 * ���� emendenum��Getter����.���������޶�ö�� ��������:2020-4-5
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEmendenum() {
		return this.emendenum;
	}

	/**
	 * ����emendenum��Setter����.���������޶�ö�� ��������:2020-4-5
	 * 
	 * @param newEmendenum
	 *            java.lang.Integer
	 */
	public void setEmendenum(java.lang.Integer emendenum) {
		this.emendenum = emendenum;
	}

	/**
	 * ���� billversionpk��Getter����.�����������ݰ汾pk ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillversionpk() {
		return this.billversionpk;
	}

	/**
	 * ����billversionpk��Setter����.�����������ݰ汾pk ��������:2020-4-5
	 * 
	 * @param newBillversionpk
	 *            java.lang.String
	 */
	public void setBillversionpk(java.lang.String billversionpk) {
		this.billversionpk = billversionpk;
	}

	/**
	 * ���� def1��Getter����.���������Զ�����1 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.���������Զ�����1 ��������:2020-4-5
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.���������Զ�����2 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.���������Զ�����2 ��������:2020-4-5
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.���������Զ�����3 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.���������Զ�����3 ��������:2020-4-5
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.���������Զ�����4 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.���������Զ�����4 ��������:2020-4-5
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.���������Զ�����5 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.���������Զ�����5 ��������:2020-4-5
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.���������Զ�����6 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.���������Զ�����6 ��������:2020-4-5
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.���������Զ�����7 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.���������Զ�����7 ��������:2020-4-5
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.���������Զ�����8 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.���������Զ�����8 ��������:2020-4-5
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.���������Զ�����9 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.���������Զ�����9 ��������:2020-4-5
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.���������Զ�����10 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.���������Զ�����10 ��������:2020-4-5
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * ���� def11��Getter����.���������Զ�����11 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * ����def11��Setter����.���������Զ�����11 ��������:2020-4-5
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * ���� def12��Getter����.���������Զ�����12 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * ����def12��Setter����.���������Զ�����12 ��������:2020-4-5
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * ���� def13��Getter����.���������Զ�����13 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * ����def13��Setter����.���������Զ�����13 ��������:2020-4-5
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * ���� def14��Getter����.���������Զ�����14 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * ����def14��Setter����.���������Զ�����14 ��������:2020-4-5
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * ���� def15��Getter����.���������Զ�����15 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * ����def15��Setter����.���������Զ�����15 ��������:2020-4-5
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * ���� def16��Getter����.���������Զ�����16 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * ����def16��Setter����.���������Զ�����16 ��������:2020-4-5
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * ���� def17��Getter����.���������Զ�����17 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * ����def17��Setter����.���������Զ�����17 ��������:2020-4-5
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * ���� def18��Getter����.���������Զ�����18 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * ����def18��Setter����.���������Զ�����18 ��������:2020-4-5
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * ���� def19��Getter����.���������Զ�����19 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * ����def19��Setter����.���������Զ�����19 ��������:2020-4-5
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * ���� def20��Getter����.���������Զ�����20 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * ����def20��Setter����.���������Զ�����20 ��������:2020-4-5
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * ���� def21��Getter����.���������Զ�����21 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * ����def21��Setter����.���������Զ�����21 ��������:2020-4-5
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * ���� def22��Getter����.���������Զ�����22 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * ����def22��Setter����.���������Զ�����22 ��������:2020-4-5
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * ���� def23��Getter����.���������Զ�����23 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * ����def23��Setter����.���������Զ�����23 ��������:2020-4-5
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * ���� def24��Getter����.���������Զ�����24 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * ����def24��Setter����.���������Զ�����24 ��������:2020-4-5
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * ���� def25��Getter����.���������Զ�����25 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * ����def25��Setter����.���������Զ�����25 ��������:2020-4-5
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * ���� def26��Getter����.���������Զ�����26 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * ����def26��Setter����.���������Զ�����26 ��������:2020-4-5
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * ���� def27��Getter����.���������Զ�����27 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * ����def27��Setter����.���������Զ�����27 ��������:2020-4-5
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * ���� def28��Getter����.���������Զ�����28 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * ����def28��Setter����.���������Զ�����28 ��������:2020-4-5
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
	}

	/**
	 * ���� def29��Getter����.���������Զ�����29 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef29() {
		return this.def29;
	}

	/**
	 * ����def29��Setter����.���������Զ�����29 ��������:2020-4-5
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(java.lang.String def29) {
		this.def29 = def29;
	}

	/**
	 * ���� def30��Getter����.���������Զ�����30 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef30() {
		return this.def30;
	}

	/**
	 * ����def30��Setter����.���������Զ�����30 ��������:2020-4-5
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(java.lang.String def30) {
		this.def30 = def30;
	}

	/**
	 * ���� def31��Getter����.���������Զ�����31 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef31() {
		return this.def31;
	}

	/**
	 * ����def31��Setter����.���������Զ�����31 ��������:2020-4-5
	 * 
	 * @param newDef31
	 *            java.lang.String
	 */
	public void setDef31(java.lang.String def31) {
		this.def31 = def31;
	}

	/**
	 * ���� def32��Getter����.���������Զ�����32 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef32() {
		return this.def32;
	}

	/**
	 * ����def32��Setter����.���������Զ�����32 ��������:2020-4-5
	 * 
	 * @param newDef32
	 *            java.lang.String
	 */
	public void setDef32(java.lang.String def32) {
		this.def32 = def32;
	}

	/**
	 * ���� def33��Getter����.���������Զ�����33 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef33() {
		return this.def33;
	}

	/**
	 * ����def33��Setter����.���������Զ�����33 ��������:2020-4-5
	 * 
	 * @param newDef33
	 *            java.lang.String
	 */
	public void setDef33(java.lang.String def33) {
		this.def33 = def33;
	}

	/**
	 * ���� def34��Getter����.���������Զ�����34 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef34() {
		return this.def34;
	}

	/**
	 * ����def34��Setter����.���������Զ�����34 ��������:2020-4-5
	 * 
	 * @param newDef34
	 *            java.lang.String
	 */
	public void setDef34(java.lang.String def34) {
		this.def34 = def34;
	}

	/**
	 * ���� def35��Getter����.���������Զ�����35 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef35() {
		return this.def35;
	}

	/**
	 * ����def35��Setter����.���������Զ�����35 ��������:2020-4-5
	 * 
	 * @param newDef35
	 *            java.lang.String
	 */
	public void setDef35(java.lang.String def35) {
		this.def35 = def35;
	}

	/**
	 * ���� def36��Getter����.���������Զ�����36 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef36() {
		return this.def36;
	}

	/**
	 * ����def36��Setter����.���������Զ�����36 ��������:2020-4-5
	 * 
	 * @param newDef36
	 *            java.lang.String
	 */
	public void setDef36(java.lang.String def36) {
		this.def36 = def36;
	}

	/**
	 * ���� def37��Getter����.���������Զ�����37 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef37() {
		return this.def37;
	}

	/**
	 * ����def37��Setter����.���������Զ�����37 ��������:2020-4-5
	 * 
	 * @param newDef37
	 *            java.lang.String
	 */
	public void setDef37(java.lang.String def37) {
		this.def37 = def37;
	}

	/**
	 * ���� def38��Getter����.���������Զ�����38 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef38() {
		return this.def38;
	}

	/**
	 * ����def38��Setter����.���������Զ�����38 ��������:2020-4-5
	 * 
	 * @param newDef38
	 *            java.lang.String
	 */
	public void setDef38(java.lang.String def38) {
		this.def38 = def38;
	}

	/**
	 * ���� def39��Getter����.���������Զ�����39 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef39() {
		return this.def39;
	}

	/**
	 * ����def39��Setter����.���������Զ�����39 ��������:2020-4-5
	 * 
	 * @param newDef39
	 *            java.lang.String
	 */
	public void setDef39(java.lang.String def39) {
		this.def39 = def39;
	}

	/**
	 * ���� def40��Getter����.���������Զ�����40 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef40() {
		return this.def40;
	}

	/**
	 * ����def40��Setter����.���������Զ�����40 ��������:2020-4-5
	 * 
	 * @param newDef40
	 *            java.lang.String
	 */
	public void setDef40(java.lang.String def40) {
		this.def40 = def40;
	}

	/**
	 * ���� def41��Getter����.���������Զ�����41 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef41() {
		return this.def41;
	}

	/**
	 * ����def41��Setter����.���������Զ�����41 ��������:2020-4-5
	 * 
	 * @param newDef41
	 *            java.lang.String
	 */
	public void setDef41(java.lang.String def41) {
		this.def41 = def41;
	}

	/**
	 * ���� def42��Getter����.���������Զ�����42 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef42() {
		return this.def42;
	}

	/**
	 * ����def42��Setter����.���������Զ�����42 ��������:2020-4-5
	 * 
	 * @param newDef42
	 *            java.lang.String
	 */
	public void setDef42(java.lang.String def42) {
		this.def42 = def42;
	}

	/**
	 * ���� def43��Getter����.���������Զ�����43 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef43() {
		return this.def43;
	}

	/**
	 * ����def43��Setter����.���������Զ�����43 ��������:2020-4-5
	 * 
	 * @param newDef43
	 *            java.lang.String
	 */
	public void setDef43(java.lang.String def43) {
		this.def43 = def43;
	}

	/**
	 * ���� def44��Getter����.���������Զ�����44 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef44() {
		return this.def44;
	}

	/**
	 * ����def44��Setter����.���������Զ�����44 ��������:2020-4-5
	 * 
	 * @param newDef44
	 *            java.lang.String
	 */
	public void setDef44(java.lang.String def44) {
		this.def44 = def44;
	}

	/**
	 * ���� def45��Getter����.���������Զ�����45 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef45() {
		return this.def45;
	}

	/**
	 * ����def45��Setter����.���������Զ�����45 ��������:2020-4-5
	 * 
	 * @param newDef45
	 *            java.lang.String
	 */
	public void setDef45(java.lang.String def45) {
		this.def45 = def45;
	}

	/**
	 * ���� def46��Getter����.���������Զ�����46 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef46() {
		return this.def46;
	}

	/**
	 * ����def46��Setter����.���������Զ�����46 ��������:2020-4-5
	 * 
	 * @param newDef46
	 *            java.lang.String
	 */
	public void setDef46(java.lang.String def46) {
		this.def46 = def46;
	}

	/**
	 * ���� def47��Getter����.���������Զ�����47 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef47() {
		return this.def47;
	}

	/**
	 * ����def47��Setter����.���������Զ�����47 ��������:2020-4-5
	 * 
	 * @param newDef47
	 *            java.lang.String
	 */
	public void setDef47(java.lang.String def47) {
		this.def47 = def47;
	}

	/**
	 * ���� def48��Getter����.���������Զ�����48 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef48() {
		return this.def48;
	}

	/**
	 * ����def48��Setter����.���������Զ�����48 ��������:2020-4-5
	 * 
	 * @param newDef48
	 *            java.lang.String
	 */
	public void setDef48(java.lang.String def48) {
		this.def48 = def48;
	}

	/**
	 * ���� def49��Getter����.���������Զ�����49 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef49() {
		return this.def49;
	}

	/**
	 * ����def49��Setter����.���������Զ�����49 ��������:2020-4-5
	 * 
	 * @param newDef49
	 *            java.lang.String
	 */
	public void setDef49(java.lang.String def49) {
		this.def49 = def49;
	}

	/**
	 * ���� def50��Getter����.���������Զ�����50 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef50() {
		return this.def50;
	}

	/**
	 * ����def50��Setter����.���������Զ�����50 ��������:2020-4-5
	 * 
	 * @param newDef50
	 *            java.lang.String
	 */
	public void setDef50(java.lang.String def50) {
		this.def50 = def50;
	}

	/**
	 * ���� def51��Getter����.���������Զ�����51 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef51() {
		return this.def51;
	}

	/**
	 * ����def51��Setter����.���������Զ�����51 ��������:2020-4-5
	 * 
	 * @param newDef51
	 *            java.lang.String
	 */
	public void setDef51(java.lang.String def51) {
		this.def51 = def51;
	}

	/**
	 * ���� def52��Getter����.���������Զ�����52 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef52() {
		return this.def52;
	}

	/**
	 * ����def52��Setter����.���������Զ�����52 ��������:2020-4-5
	 * 
	 * @param newDef52
	 *            java.lang.String
	 */
	public void setDef52(java.lang.String def52) {
		this.def52 = def52;
	}

	/**
	 * ���� def53��Getter����.���������Զ�����53 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef53() {
		return this.def53;
	}

	/**
	 * ����def53��Setter����.���������Զ�����53 ��������:2020-4-5
	 * 
	 * @param newDef53
	 *            java.lang.String
	 */
	public void setDef53(java.lang.String def53) {
		this.def53 = def53;
	}

	/**
	 * ���� def54��Getter����.���������Զ�����54 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef54() {
		return this.def54;
	}

	/**
	 * ����def54��Setter����.���������Զ�����54 ��������:2020-4-5
	 * 
	 * @param newDef54
	 *            java.lang.String
	 */
	public void setDef54(java.lang.String def54) {
		this.def54 = def54;
	}

	/**
	 * ���� def55��Getter����.���������Զ�����55 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef55() {
		return this.def55;
	}

	/**
	 * ����def55��Setter����.���������Զ�����55 ��������:2020-4-5
	 * 
	 * @param newDef55
	 *            java.lang.String
	 */
	public void setDef55(java.lang.String def55) {
		this.def55 = def55;
	}

	/**
	 * ���� def56��Getter����.���������Զ�����56 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef56() {
		return this.def56;
	}

	/**
	 * ����def56��Setter����.���������Զ�����56 ��������:2020-4-5
	 * 
	 * @param newDef56
	 *            java.lang.String
	 */
	public void setDef56(java.lang.String def56) {
		this.def56 = def56;
	}

	/**
	 * ���� def57��Getter����.���������Զ�����57 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef57() {
		return this.def57;
	}

	/**
	 * ����def57��Setter����.���������Զ�����57 ��������:2020-4-5
	 * 
	 * @param newDef57
	 *            java.lang.String
	 */
	public void setDef57(java.lang.String def57) {
		this.def57 = def57;
	}

	/**
	 * ���� def58��Getter����.���������Զ�����58 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef58() {
		return this.def58;
	}

	/**
	 * ����def58��Setter����.���������Զ�����58 ��������:2020-4-5
	 * 
	 * @param newDef58
	 *            java.lang.String
	 */
	public void setDef58(java.lang.String def58) {
		this.def58 = def58;
	}

	/**
	 * ���� def59��Getter����.���������Զ�����59 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef59() {
		return this.def59;
	}

	/**
	 * ����def59��Setter����.���������Զ�����59 ��������:2020-4-5
	 * 
	 * @param newDef59
	 *            java.lang.String
	 */
	public void setDef59(java.lang.String def59) {
		this.def59 = def59;
	}

	/**
	 * ���� def60��Getter����.���������Զ�����60 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef60() {
		return this.def60;
	}

	/**
	 * ����def60��Setter����.���������Զ�����60 ��������:2020-4-5
	 * 
	 * @param newDef60
	 *            java.lang.String
	 */
	public void setDef60(java.lang.String def60) {
		this.def60 = def60;
	}

	/**
	 * ���� big_text_a��Getter����.���������Զ�����ı�a ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_a() {
		return this.big_text_a;
	}

	/**
	 * ����big_text_a��Setter����.���������Զ�����ı�a ��������:2020-4-5
	 * 
	 * @param newBig_text_a
	 *            java.lang.String
	 */
	public void setBig_text_a(java.lang.String big_text_a) {
		this.big_text_a = big_text_a;
	}

	/**
	 * ���� big_text_b��Getter����.���������Զ�����ı�b ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_b() {
		return this.big_text_b;
	}

	/**
	 * ����big_text_b��Setter����.���������Զ�����ı�b ��������:2020-4-5
	 * 
	 * @param newBig_text_b
	 *            java.lang.String
	 */
	public void setBig_text_b(java.lang.String big_text_b) {
		this.big_text_b = big_text_b;
	}

	/**
	 * ���� big_text_c��Getter����.���������Զ�����ı�c ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBig_text_c() {
		return this.big_text_c;
	}

	/**
	 * ����big_text_c��Setter����.���������Զ�����ı�c ��������:2020-4-5
	 * 
	 * @param newBig_text_c
	 *            java.lang.String
	 */
	public void setBig_text_c(java.lang.String big_text_c) {
		this.big_text_c = big_text_c;
	}

	/**
	 * ���� def61��Getter����.���������Զ�����61 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef61() {
		return this.def61;
	}

	/**
	 * ����def61��Setter����.���������Զ�����61 ��������:2020-4-5
	 * 
	 * @param newDef61
	 *            java.lang.String
	 */
	public void setDef61(java.lang.String def61) {
		this.def61 = def61;
	}

	/**
	 * ���� def62��Getter����.���������Զ�����62 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef62() {
		return this.def62;
	}

	/**
	 * ����def62��Setter����.���������Զ�����62 ��������:2020-4-5
	 * 
	 * @param newDef62
	 *            java.lang.String
	 */
	public void setDef62(java.lang.String def62) {
		this.def62 = def62;
	}

	/**
	 * ���� def63��Getter����.���������Զ�����63 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef63() {
		return this.def63;
	}

	/**
	 * ����def63��Setter����.���������Զ�����63 ��������:2020-4-5
	 * 
	 * @param newDef63
	 *            java.lang.String
	 */
	public void setDef63(java.lang.String def63) {
		this.def63 = def63;
	}

	/**
	 * ���� def64��Getter����.���������Զ�����64 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef64() {
		return this.def64;
	}

	/**
	 * ����def64��Setter����.���������Զ�����64 ��������:2020-4-5
	 * 
	 * @param newDef64
	 *            java.lang.String
	 */
	public void setDef64(java.lang.String def64) {
		this.def64 = def64;
	}

	/**
	 * ���� def65��Getter����.���������Զ�����65 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef65() {
		return this.def65;
	}

	/**
	 * ����def65��Setter����.���������Զ�����65 ��������:2020-4-5
	 * 
	 * @param newDef65
	 *            java.lang.String
	 */
	public void setDef65(java.lang.String def65) {
		this.def65 = def65;
	}

	/**
	 * ���� def66��Getter����.���������Զ�����66 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef66() {
		return this.def66;
	}

	/**
	 * ����def66��Setter����.���������Զ�����66 ��������:2020-4-5
	 * 
	 * @param newDef66
	 *            java.lang.String
	 */
	public void setDef66(java.lang.String def66) {
		this.def66 = def66;
	}

	/**
	 * ���� def67��Getter����.���������Զ�����67 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef67() {
		return this.def67;
	}

	/**
	 * ����def67��Setter����.���������Զ�����67 ��������:2020-4-5
	 * 
	 * @param newDef67
	 *            java.lang.String
	 */
	public void setDef67(java.lang.String def67) {
		this.def67 = def67;
	}

	/**
	 * ���� def68��Getter����.���������Զ�����68 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef68() {
		return this.def68;
	}

	/**
	 * ����def68��Setter����.���������Զ�����68 ��������:2020-4-5
	 * 
	 * @param newDef68
	 *            java.lang.String
	 */
	public void setDef68(java.lang.String def68) {
		this.def68 = def68;
	}

	/**
	 * ���� def69��Getter����.���������Զ�����69 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef69() {
		return this.def69;
	}

	/**
	 * ����def69��Setter����.���������Զ�����69 ��������:2020-4-5
	 * 
	 * @param newDef69
	 *            java.lang.String
	 */
	public void setDef69(java.lang.String def69) {
		this.def69 = def69;
	}

	/**
	 * ���� def70��Getter����.���������Զ�����70 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef70() {
		return this.def70;
	}

	/**
	 * ����def70��Setter����.���������Զ�����70 ��������:2020-4-5
	 * 
	 * @param newDef70
	 *            java.lang.String
	 */
	public void setDef70(java.lang.String def70) {
		this.def70 = def70;
	}

	/**
	 * ���� def71��Getter����.���������Զ�����71 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef71() {
		return this.def71;
	}

	/**
	 * ����def71��Setter����.���������Զ�����71 ��������:2020-4-5
	 * 
	 * @param newDef71
	 *            java.lang.String
	 */
	public void setDef71(java.lang.String def71) {
		this.def71 = def71;
	}

	/**
	 * ���� def72��Getter����.���������Զ�����72 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef72() {
		return this.def72;
	}

	/**
	 * ����def72��Setter����.���������Զ�����72 ��������:2020-4-5
	 * 
	 * @param newDef72
	 *            java.lang.String
	 */
	public void setDef72(java.lang.String def72) {
		this.def72 = def72;
	}

	/**
	 * ���� def73��Getter����.���������Զ�����73 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef73() {
		return this.def73;
	}

	/**
	 * ����def73��Setter����.���������Զ�����73 ��������:2020-4-5
	 * 
	 * @param newDef73
	 *            java.lang.String
	 */
	public void setDef73(java.lang.String def73) {
		this.def73 = def73;
	}

	/**
	 * ���� def74��Getter����.���������Զ�����74 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef74() {
		return this.def74;
	}

	/**
	 * ����def74��Setter����.���������Զ�����74 ��������:2020-4-5
	 * 
	 * @param newDef74
	 *            java.lang.String
	 */
	public void setDef74(java.lang.String def74) {
		this.def74 = def74;
	}

	/**
	 * ���� def75��Getter����.���������Զ�����75 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef75() {
		return this.def75;
	}

	/**
	 * ����def75��Setter����.���������Զ�����75 ��������:2020-4-5
	 * 
	 * @param newDef75
	 *            java.lang.String
	 */
	public void setDef75(java.lang.String def75) {
		this.def75 = def75;
	}

	/**
	 * ���� def76��Getter����.���������Զ�����76 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef76() {
		return this.def76;
	}

	/**
	 * ����def76��Setter����.���������Զ�����76 ��������:2020-4-5
	 * 
	 * @param newDef76
	 *            java.lang.String
	 */
	public void setDef76(java.lang.String def76) {
		this.def76 = def76;
	}

	/**
	 * ���� def77��Getter����.���������Զ�����77 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef77() {
		return this.def77;
	}

	/**
	 * ����def77��Setter����.���������Զ�����77 ��������:2020-4-5
	 * 
	 * @param newDef77
	 *            java.lang.String
	 */
	public void setDef77(java.lang.String def77) {
		this.def77 = def77;
	}

	/**
	 * ���� def78��Getter����.���������Զ�����78 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef78() {
		return this.def78;
	}

	/**
	 * ����def78��Setter����.���������Զ�����78 ��������:2020-4-5
	 * 
	 * @param newDef78
	 *            java.lang.String
	 */
	public void setDef78(java.lang.String def78) {
		this.def78 = def78;
	}

	/**
	 * ���� def79��Getter����.���������Զ�����79 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef79() {
		return this.def79;
	}

	/**
	 * ����def79��Setter����.���������Զ�����79 ��������:2020-4-5
	 * 
	 * @param newDef79
	 *            java.lang.String
	 */
	public void setDef79(java.lang.String def79) {
		this.def79 = def79;
	}

	/**
	 * ���� def80��Getter����.���������Զ�����80 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef80() {
		return this.def80;
	}

	/**
	 * ����def80��Setter����.���������Զ�����80 ��������:2020-4-5
	 * 
	 * @param newDef80
	 *            java.lang.String
	 */
	public void setDef80(java.lang.String def80) {
		this.def80 = def80;
	}

	/**
	 * ���� def81��Getter����.���������Զ�����81 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef81() {
		return this.def81;
	}

	/**
	 * ����def81��Setter����.���������Զ�����81 ��������:2020-4-5
	 * 
	 * @param newDef81
	 *            java.lang.String
	 */
	public void setDef81(java.lang.String def81) {
		this.def81 = def81;
	}

	/**
	 * ���� def82��Getter����.���������Զ�����82 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef82() {
		return this.def82;
	}

	/**
	 * ����def82��Setter����.���������Զ�����82 ��������:2020-4-5
	 * 
	 * @param newDef82
	 *            java.lang.String
	 */
	public void setDef82(java.lang.String def82) {
		this.def82 = def82;
	}

	/**
	 * ���� def83��Getter����.���������Զ�����83 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef83() {
		return this.def83;
	}

	/**
	 * ����def83��Setter����.���������Զ�����83 ��������:2020-4-5
	 * 
	 * @param newDef83
	 *            java.lang.String
	 */
	public void setDef83(java.lang.String def83) {
		this.def83 = def83;
	}

	/**
	 * ���� def84��Getter����.���������Զ�����84 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef84() {
		return this.def84;
	}

	/**
	 * ����def84��Setter����.���������Զ�����84 ��������:2020-4-5
	 * 
	 * @param newDef84
	 *            java.lang.String
	 */
	public void setDef84(java.lang.String def84) {
		this.def84 = def84;
	}

	/**
	 * ���� def85��Getter����.���������Զ�����85 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef85() {
		return this.def85;
	}

	/**
	 * ����def85��Setter����.���������Զ�����85 ��������:2020-4-5
	 * 
	 * @param newDef85
	 *            java.lang.String
	 */
	public void setDef85(java.lang.String def85) {
		this.def85 = def85;
	}

	/**
	 * ���� def86��Getter����.���������Զ�����86 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef86() {
		return this.def86;
	}

	/**
	 * ����def86��Setter����.���������Զ�����86 ��������:2020-4-5
	 * 
	 * @param newDef86
	 *            java.lang.String
	 */
	public void setDef86(java.lang.String def86) {
		this.def86 = def86;
	}

	/**
	 * ���� def87��Getter����.���������Զ�����87 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef87() {
		return this.def87;
	}

	/**
	 * ����def87��Setter����.���������Զ�����87 ��������:2020-4-5
	 * 
	 * @param newDef87
	 *            java.lang.String
	 */
	public void setDef87(java.lang.String def87) {
		this.def87 = def87;
	}

	/**
	 * ���� def88��Getter����.���������Զ�����88 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef88() {
		return this.def88;
	}

	/**
	 * ����def88��Setter����.���������Զ�����88 ��������:2020-4-5
	 * 
	 * @param newDef88
	 *            java.lang.String
	 */
	public void setDef88(java.lang.String def88) {
		this.def88 = def88;
	}

	/**
	 * ���� def89��Getter����.���������Զ�����89 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef89() {
		return this.def89;
	}

	/**
	 * ����def89��Setter����.���������Զ�����89 ��������:2020-4-5
	 * 
	 * @param newDef89
	 *            java.lang.String
	 */
	public void setDef89(java.lang.String def89) {
		this.def89 = def89;
	}

	/**
	 * ���� def90��Getter����.���������Զ�����90 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef90() {
		return this.def90;
	}

	/**
	 * ����def90��Setter����.���������Զ�����90 ��������:2020-4-5
	 * 
	 * @param newDef90
	 *            java.lang.String
	 */
	public void setDef90(java.lang.String def90) {
		this.def90 = def90;
	}

	/**
	 * ���� def91��Getter����.���������Զ�����91 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef91() {
		return this.def91;
	}

	/**
	 * ����def91��Setter����.���������Զ�����91 ��������:2020-4-5
	 * 
	 * @param newDef91
	 *            java.lang.String
	 */
	public void setDef91(java.lang.String def91) {
		this.def91 = def91;
	}

	/**
	 * ���� def92��Getter����.���������Զ�����92 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef92() {
		return this.def92;
	}

	/**
	 * ����def92��Setter����.���������Զ�����92 ��������:2020-4-5
	 * 
	 * @param newDef92
	 *            java.lang.String
	 */
	public void setDef92(java.lang.String def92) {
		this.def92 = def92;
	}

	/**
	 * ���� def93��Getter����.���������Զ�����93 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef93() {
		return this.def93;
	}

	/**
	 * ����def93��Setter����.���������Զ�����93 ��������:2020-4-5
	 * 
	 * @param newDef93
	 *            java.lang.String
	 */
	public void setDef93(java.lang.String def93) {
		this.def93 = def93;
	}

	/**
	 * ���� def94��Getter����.���������Զ�����94 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef94() {
		return this.def94;
	}

	/**
	 * ����def94��Setter����.���������Զ�����94 ��������:2020-4-5
	 * 
	 * @param newDef94
	 *            java.lang.String
	 */
	public void setDef94(java.lang.String def94) {
		this.def94 = def94;
	}

	/**
	 * ���� def95��Getter����.���������Զ�����95 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef95() {
		return this.def95;
	}

	/**
	 * ����def95��Setter����.���������Զ�����95 ��������:2020-4-5
	 * 
	 * @param newDef95
	 *            java.lang.String
	 */
	public void setDef95(java.lang.String def95) {
		this.def95 = def95;
	}

	/**
	 * ���� def96��Getter����.���������Զ�����96 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef96() {
		return this.def96;
	}

	/**
	 * ����def96��Setter����.���������Զ�����96 ��������:2020-4-5
	 * 
	 * @param newDef96
	 *            java.lang.String
	 */
	public void setDef96(java.lang.String def96) {
		this.def96 = def96;
	}

	/**
	 * ���� def97��Getter����.���������Զ�����97 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef97() {
		return this.def97;
	}

	/**
	 * ����def97��Setter����.���������Զ�����97 ��������:2020-4-5
	 * 
	 * @param newDef97
	 *            java.lang.String
	 */
	public void setDef97(java.lang.String def97) {
		this.def97 = def97;
	}

	/**
	 * ���� def98��Getter����.���������Զ�����98 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef98() {
		return this.def98;
	}

	/**
	 * ����def98��Setter����.���������Զ�����98 ��������:2020-4-5
	 * 
	 * @param newDef98
	 *            java.lang.String
	 */
	public void setDef98(java.lang.String def98) {
		this.def98 = def98;
	}

	/**
	 * ���� def99��Getter����.���������Զ�����99 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef99() {
		return this.def99;
	}

	/**
	 * ����def99��Setter����.���������Զ�����99 ��������:2020-4-5
	 * 
	 * @param newDef99
	 *            java.lang.String
	 */
	public void setDef99(java.lang.String def99) {
		this.def99 = def99;
	}

	/**
	 * ���� def100��Getter����.���������Զ�����100 ��������:2020-4-5
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef100() {
		return this.def100;
	}

	/**
	 * ����def100��Setter����.���������Զ�����100 ��������:2020-4-5
	 * 
	 * @param newDef100
	 *            java.lang.String
	 */
	public void setDef100(java.lang.String def100) {
		this.def100 = def100;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2020-4-5
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2020-4-5
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.FinancexpenseVO");
	}
}
