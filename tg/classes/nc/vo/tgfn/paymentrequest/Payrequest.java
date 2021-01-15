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
 * �˴�����۵�������Ϣ
 * </p>
 * ��������:2019-12-9
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class Payrequest extends SuperVO {

	/**
	 * ��������
	 */
	public java.lang.String pk_payreq;
	/**
	 * ����
	 */
	public java.lang.String pk_group;
	/**
	 * ������֯
	 */
	public java.lang.String pk_org;
	/**
	 * ��֯�汾
	 */
	public java.lang.String pk_org_v;
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
	 * ���ݺ�
	 */
	public java.lang.String billno;
	/**
	 * ������֯
	 */
	public java.lang.String pkorg;
	/**
	 * ҵ������
	 */
	public java.lang.String busitype;
	/**
	 * �Ƶ���
	 */
	public java.lang.String billmaker;
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
	 * �޶�ö��
	 */
	public java.lang.Integer emendenum;
	/**
	 * ���ݰ汾pk
	 */
	public UFDate billversionpk;
	/**
	 * ��������
	 */
	public UFDate billdate;
	/**
	 * ��������
	 */
	public java.lang.String pk_tradetypeid;
	/**
	 * EBS����
	 */
	public java.lang.String def1;
	/**
	 * EBS������
	 */
	public java.lang.String def2;
	/**
	 * Ӱ�����
	 */
	public java.lang.String def3;
	/**
	 * Ӱ��״̬
	 */
	public java.lang.String def4;
	/**
	 * ��ͬ����
	 */
	public java.lang.String def5;
	/**
	 * ��ͬ����
	 */
	public java.lang.String def6;
	/**
	 * ��ͬ����
	 */
	public java.lang.String def7;
	/**
	 * ��ͬϸ��
	 */
	public java.lang.String def8;
	/**
	 * �����̶�
	 */
	public java.lang.String def9;
	/**
	 * ��ϵͳ����
	 */
	public java.lang.String def10;
	/**
	 * �г�����Ŀ
	 */
	public java.lang.String def11;
	/**
	 * ��������
	 */
	public java.lang.Integer objtype;
	/**
	 * �տ
	 */
	public java.lang.String def12;
	/**
	 * ���첿��
	 */
	public java.lang.String pk_deptid_v;
	/**
	 * ������
	 */
	public java.lang.String pk_psndoc;
	/**
	 * ���㷽ʽ
	 */
	public java.lang.String pk_balatype;
	/**
	 * ���������˻�
	 */
	public java.lang.String payaccount;
	/**
	 * ��������֧��
	 */
	public java.lang.String def13;
	/**
	 * �ֽ��˻�
	 */
	public java.lang.String cashaccount;
	/**
	 * ���
	 */
	public nc.vo.pub.lang.UFDouble money;
	/**
	 * ABS֧�����
	 */
	public java.lang.String def14;
	/**
	 * ���������
	 */
	public java.lang.String def15;
	/**
	 * ��������ۼƸ�����
	 */
	public java.lang.String def16;
	/**
	 * �ۼ�Ʊ�ݽ��
	 */
	public java.lang.String def17;
	/**
	 * ����Ʊ�ݽ��
	 */
	public java.lang.String def18;
	/**
	 * ����
	 */
	public java.lang.String pk_currtype;
	/**
	 * �Ƿ�ȱʧӰ��
	 */
	public java.lang.String def19;
	/**
	 * �Ƿ��ȸ����Ʊ
	 */
	public java.lang.String def20;
	/**
	 * ���
	 */
	public java.lang.String def21;
	/**
	 * ǩԼ��˾
	 */
	public java.lang.String def22;
	/**
	 * ���˹�˾
	 */
	public java.lang.String def23;
	/**
	 * ��Ŀ
	 */
	public java.lang.String def24;
	/**
	 * ��Ŀ����
	 */
	public java.lang.String def25;
	/**
	 * �Ƿ�������֤��
	 */
	public java.lang.String def26;
	/**
	 * �Ƿ�����
	 */
	public java.lang.String def27;
	/**
	 * �Ƿ�ȫ��ת��
	 */
	public java.lang.String def28;
	/**
	 * ����״̬
	 */
	public java.lang.Integer billstatus;
	/**
	 * ����״̬
	 */
	public java.lang.Integer approvestatus;
	/**
	 * ��Ч״̬
	 */
	public java.lang.Integer effectstatus;
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
	 * �Զ�����60
	 */
	public java.lang.String def60;
	/**
	 * �Զ�����59
	 */
	public java.lang.String def59;
	/**
	 * �Զ�����58
	 */
	public java.lang.String def58;
	/**
	 * �Զ�����57
	 */
	public java.lang.String def57;
	/**
	 * �Զ�����56
	 */
	public java.lang.String def56;
	/**
	 * �Զ�����55
	 */
	public java.lang.String def55;
	/**
	 * �Զ�����54
	 */
	public java.lang.String def54;
	/**
	 * �Զ�����53
	 */
	public java.lang.String def53;
	/**
	 * �Զ�����52
	 */
	public java.lang.String def52;
	public java.lang.String def61;
	public java.lang.String def62;
	public java.lang.String def63;
	public java.lang.String def64;
	public java.lang.String def65;

	public java.lang.String bpmid;
	public java.lang.String bpmaddress;
	// add by bobo ��ӵ��������ֶ�2020��6��30��17:08:41
	public java.lang.String srcbilltype;
	public java.lang.String srcbillid;

	
	public java.lang.String getSrcbilltype() {
		return srcbilltype;
	}

	public void setSrcbilltype(java.lang.String srcbilltype) {
		this.srcbilltype = srcbilltype;
	}

	public java.lang.String getSrcbillid() {
		return srcbillid;
	}

	public void setSrcbillid(java.lang.String srcbillid) {
		this.srcbillid = srcbillid;
	}

	public java.lang.String getBpmid() {
		return bpmid;
	}

	public void setBpmid(java.lang.String bpmid) {
		this.bpmid = bpmid;
	}

	public java.lang.String getBpmaddress() {
		return bpmaddress;
	}

	public void setBpmaddress(java.lang.String bpmaddress) {
		this.bpmaddress = bpmaddress;
	}

	public java.lang.String getDef61() {
		return def61;
	}

	public void setDef61(java.lang.String def61) {
		this.def61 = def61;
	}

	public java.lang.String getDef62() {
		return def62;
	}

	public void setDef62(java.lang.String def62) {
		this.def62 = def62;
	}

	public java.lang.String getDef63() {
		return def63;
	}

	public void setDef63(java.lang.String def63) {
		this.def63 = def63;
	}

	public java.lang.String getDef64() {
		return def64;
	}

	public void setDef64(java.lang.String def64) {
		this.def64 = def64;
	}

	public java.lang.String getDef65() {
		return def65;
	}

	public void setDef65(java.lang.String def65) {
		this.def65 = def65;
	}

	/**
	 * ʱ���
	 */
	public UFDateTime ts;
	/**
	 * dr
	 */
	public Integer dr;

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * ���� pk_payreq��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_payreq() {
		return this.pk_payreq;
	}

	/**
	 * ����pk_payreq��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newPk_payreq
	 *            java.lang.String
	 */
	public void setPk_payreq(java.lang.String pk_payreq) {
		this.pk_payreq = pk_payreq;
	}

	/**
	 * ���� pk_group��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ����pk_group��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ���� pk_org��Getter����.��������������֯ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ����pk_org��Setter����.��������������֯ ��������:2019-12-9
	 * 
	 * @param newPk_org
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ���� pk_org_v��Getter����.����������֯�汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.����������֯�汾 ��������:2019-12-9
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ���� creator��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getCreator() {
		return this.creator;
	}

	/**
	 * ����creator��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newCreator
	 *            nc.vo.sm.UserVO
	 */
	public void setCreator(java.lang.String creator) {
		this.creator = creator;
	}

	/**
	 * ���� creationtime��Getter����.������������ʱ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getCreationtime() {
		return this.creationtime;
	}

	/**
	 * ����creationtime��Setter����.������������ʱ�� ��������:2019-12-9
	 * 
	 * @param newCreationtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setCreationtime(UFDateTime creationtime) {
		this.creationtime = creationtime;
	}

	/**
	 * ���� modifier��Getter����.���������޸��� ��������:2019-12-9
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getModifier() {
		return this.modifier;
	}

	/**
	 * ����modifier��Setter����.���������޸��� ��������:2019-12-9
	 * 
	 * @param newModifier
	 *            nc.vo.sm.UserVO
	 */
	public void setModifier(java.lang.String modifier) {
		this.modifier = modifier;
	}

	/**
	 * ���� modifiedtime��Getter����.���������޸�ʱ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getModifiedtime() {
		return this.modifiedtime;
	}

	/**
	 * ����modifiedtime��Setter����.���������޸�ʱ�� ��������:2019-12-9
	 * 
	 * @param newModifiedtime
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setModifiedtime(UFDateTime modifiedtime) {
		this.modifiedtime = modifiedtime;
	}

	/**
	 * ���� billno��Getter����.�����������ݺ� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBillno() {
		return this.billno;
	}

	/**
	 * ����billno��Setter����.�����������ݺ� ��������:2019-12-9
	 * 
	 * @param newBillno
	 *            java.lang.String
	 */
	public void setBillno(java.lang.String billno) {
		this.billno = billno;
	}

	/**
	 * ���� pkorg��Getter����.��������������֯ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPkorg() {
		return this.pkorg;
	}

	/**
	 * ����pkorg��Setter����.��������������֯ ��������:2019-12-9
	 * 
	 * @param newPkorg
	 *            java.lang.String
	 */
	public void setPkorg(java.lang.String pkorg) {
		this.pkorg = pkorg;
	}

	/**
	 * ���� busitype��Getter����.��������ҵ������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBusitype() {
		return this.busitype;
	}

	/**
	 * ����busitype��Setter����.��������ҵ������ ��������:2019-12-9
	 * 
	 * @param newBusitype
	 *            java.lang.String
	 */
	public void setBusitype(java.lang.String busitype) {
		this.busitype = busitype;
	}

	/**
	 * ���� billmaker��Getter����.���������Ƶ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getBillmaker() {
		return this.billmaker;
	}

	/**
	 * ����billmaker��Setter����.���������Ƶ��� ��������:2019-12-9
	 * 
	 * @param newBillmaker
	 *            nc.vo.sm.UserVO
	 */
	public void setBillmaker(java.lang.String billmaker) {
		this.billmaker = billmaker;
	}

	/**
	 * ���� approver��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.sm.UserVO
	 */
	public java.lang.String getApprover() {
		return this.approver;
	}

	/**
	 * ����approver��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newApprover
	 *            nc.vo.sm.UserVO
	 */
	public void setApprover(java.lang.String approver) {
		this.approver = approver;
	}

	/**
	 * ���� approvenote��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getApprovenote() {
		return this.approvenote;
	}

	/**
	 * ����approvenote��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newApprovenote
	 *            java.lang.String
	 */
	public void setApprovenote(java.lang.String approvenote) {
		this.approvenote = approvenote;
	}

	/**
	 * ���� approvedate��Getter����.������������ʱ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getApprovedate() {
		return this.approvedate;
	}

	/**
	 * ����approvedate��Setter����.������������ʱ�� ��������:2019-12-9
	 * 
	 * @param newApprovedate
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setApprovedate(UFDateTime approvedate) {
		this.approvedate = approvedate;
	}

	/**
	 * ���� transtype��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTranstype() {
		return this.transtype;
	}

	/**
	 * ����transtype��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newTranstype
	 *            java.lang.String
	 */
	public void setTranstype(java.lang.String transtype) {
		this.transtype = transtype;
	}

	/**
	 * ���� billtype��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getBilltype() {
		return this.billtype;
	}

	/**
	 * ����billtype��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newBilltype
	 *            java.lang.String
	 */
	public void setBilltype(java.lang.String billtype) {
		this.billtype = billtype;
	}

	/**
	 * ���� transtypepk��Getter����.����������������pk ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.billtype.BilltypeVO
	 */
	public java.lang.String getTranstypepk() {
		return this.transtypepk;
	}

	/**
	 * ����transtypepk��Setter����.����������������pk ��������:2019-12-9
	 * 
	 * @param newTranstypepk
	 *            nc.vo.pub.billtype.BilltypeVO
	 */
	public void setTranstypepk(java.lang.String transtypepk) {
		this.transtypepk = transtypepk;
	}

	/**
	 * ���� emendenum��Getter����.���������޶�ö�� ��������:2019-12-9
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getEmendenum() {
		return this.emendenum;
	}

	/**
	 * ����emendenum��Setter����.���������޶�ö�� ��������:2019-12-9
	 * 
	 * @param newEmendenum
	 *            java.lang.Integer
	 */
	public void setEmendenum(java.lang.Integer emendenum) {
		this.emendenum = emendenum;
	}

	/**
	 * ���� billversionpk��Getter����.�����������ݰ汾pk ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBillversionpk() {
		return this.billversionpk;
	}

	/**
	 * ����billversionpk��Setter����.�����������ݰ汾pk ��������:2019-12-9
	 * 
	 * @param newBillversionpk
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBillversionpk(UFDate billversionpk) {
		this.billversionpk = billversionpk;
	}

	/**
	 * ���� billdate��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBilldate() {
		return this.billdate;
	}

	/**
	 * ����billdate��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newBilldate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBilldate(UFDate billdate) {
		this.billdate = billdate;
	}

	/**
	 * ���� pk_tradetypeid��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.fibd.RecpaytypeVO
	 */
	public java.lang.String getPk_tradetypeid() {
		return this.pk_tradetypeid;
	}

	/**
	 * ����pk_tradetypeid��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newPk_tradetypeid
	 *            nc.vo.fibd.RecpaytypeVO
	 */
	public void setPk_tradetypeid(java.lang.String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	/**
	 * ���� def1��Getter����.��������EBS���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.��������EBS���� ��������:2019-12-9
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.��������EBS������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.��������EBS������ ��������:2019-12-9
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.��������Ӱ����� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.��������Ӱ����� ��������:2019-12-9
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.��������Ӱ��״̬ ��������:2019-12-9
	 * 
	 * @return nc.vo.imag.ImageStateEnum
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.��������Ӱ��״̬ ��������:2019-12-9
	 * 
	 * @param newDef4
	 *            nc.vo.imag.ImageStateEnum
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.����������ͬ���� ��������:2019-12-9
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.����������ͬϸ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.����������ͬϸ�� ��������:2019-12-9
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.�������������̶� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.�������������̶� ��������:2019-12-9
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.����������ϵͳ���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.����������ϵͳ���� ��������:2019-12-9
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * ���� def11��Getter����.���������г�����Ŀ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * ����def11��Setter����.���������г�����Ŀ ��������:2019-12-9
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * ���� objtype��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.agiotage.ObjTypeEnum
	 */
	public java.lang.Integer getObjtype() {
		return this.objtype;
	}

	/**
	 * ����objtype��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newObjtype
	 *            nc.vo.arap.agiotage.ObjTypeEnum
	 */
	public void setObjtype(java.lang.Integer objtype) {
		this.objtype = objtype;
	}

	/**
	 * ���� def12��Getter����.���������տ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * ����def12��Setter����.���������տ ��������:2019-12-9
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * ���� pk_deptid_v��Getter����.�����������첿�� ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.DeptVersionVO
	 */
	public java.lang.String getPk_deptid_v() {
		return this.pk_deptid_v;
	}

	/**
	 * ����pk_deptid_v��Setter����.�����������첿�� ��������:2019-12-9
	 * 
	 * @param newPk_deptid_v
	 *            nc.vo.vorg.DeptVersionVO
	 */
	public void setPk_deptid_v(java.lang.String pk_deptid_v) {
		this.pk_deptid_v = pk_deptid_v;
	}

	/**
	 * ���� pk_psndoc��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_psndoc() {
		return this.pk_psndoc;
	}

	/**
	 * ����pk_psndoc��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newPk_psndoc
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_psndoc(java.lang.String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	/**
	 * ���� pk_balatype��Getter����.�����������㷽ʽ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.balatype.BalaTypeVO
	 */
	public java.lang.String getPk_balatype() {
		return this.pk_balatype;
	}

	/**
	 * ����pk_balatype��Setter����.�����������㷽ʽ ��������:2019-12-9
	 * 
	 * @param newPk_balatype
	 *            nc.vo.bd.balatype.BalaTypeVO
	 */
	public void setPk_balatype(java.lang.String pk_balatype) {
		this.pk_balatype = pk_balatype;
	}

	/**
	 * ���� payaccount��Getter����.�����������������˻� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public java.lang.String getPayaccount() {
		return this.payaccount;
	}

	/**
	 * ����payaccount��Setter����.�����������������˻� ��������:2019-12-9
	 * 
	 * @param newPayaccount
	 *            nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public void setPayaccount(java.lang.String payaccount) {
		this.payaccount = payaccount;
	}

	/**
	 * ���� def13��Getter����.����������������֧�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * ����def13��Setter����.����������������֧�� ��������:2019-12-9
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * ���� cashaccount��Getter����.���������ֽ��˻� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.cashaccount.CashAccountVO
	 */
	public java.lang.String getCashaccount() {
		return this.cashaccount;
	}

	/**
	 * ����cashaccount��Setter����.���������ֽ��˻� ��������:2019-12-9
	 * 
	 * @param newCashaccount
	 *            nc.vo.bd.cashaccount.CashAccountVO
	 */
	public void setCashaccount(java.lang.String cashaccount) {
		this.cashaccount = cashaccount;
	}

	/**
	 * ���� money��Getter����.����������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney() {
		return this.money;
	}

	/**
	 * ����money��Setter����.����������� ��������:2019-12-9
	 * 
	 * @param newMoney
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setMoney(nc.vo.pub.lang.UFDouble money) {
		this.money = money;
	}

	/**
	 * ���� def14��Getter����.��������ABS֧����� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * ����def14��Setter����.��������ABS֧����� ��������:2019-12-9
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * ���� def15��Getter����.����������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * ����def15��Setter����.����������������� ��������:2019-12-9
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * ���� def16��Getter����.����������������ۼƸ����� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * ����def16��Setter����.����������������ۼƸ����� ��������:2019-12-9
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * ���� def17��Getter����.���������ۼ�Ʊ�ݽ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * ����def17��Setter����.���������ۼ�Ʊ�ݽ�� ��������:2019-12-9
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * ���� def18��Getter����.������������Ʊ�ݽ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * ����def18��Setter����.������������Ʊ�ݽ�� ��������:2019-12-9
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * ���� pk_currtype��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.currtype.CurrtypeVO
	 */
	public java.lang.String getPk_currtype() {
		return this.pk_currtype;
	}

	/**
	 * ����pk_currtype��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newPk_currtype
	 *            nc.vo.bd.currtype.CurrtypeVO
	 */
	public void setPk_currtype(java.lang.String pk_currtype) {
		this.pk_currtype = pk_currtype;
	}

	/**
	 * ���� def19��Getter����.���������Ƿ�ȱʧӰ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * ����def19��Setter����.���������Ƿ�ȱʧӰ�� ��������:2019-12-9
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * ���� def20��Getter����.���������Ƿ��ȸ����Ʊ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * ����def20��Setter����.���������Ƿ��ȸ����Ʊ ��������:2019-12-9
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * ���� def21��Getter����.����������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * ����def21��Setter����.����������� ��������:2019-12-9
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * ���� def22��Getter����.��������ǩԼ��˾ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * ����def22��Setter����.��������ǩԼ��˾ ��������:2019-12-9
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * ���� def23��Getter����.�����������˹�˾ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * ����def23��Setter����.�����������˹�˾ ��������:2019-12-9
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * ���� def24��Getter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.tg.projectdata.ProjectDataVO
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * ����def24��Setter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @param newDef24
	 *            nc.vo.tg.projectdata.ProjectDataVO
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * ���� def25��Getter����.����������Ŀ���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * ����def25��Setter����.����������Ŀ���� ��������:2019-12-9
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * ���� def26��Getter����.���������Ƿ�������֤�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * ����def26��Setter����.���������Ƿ�������֤�� ��������:2019-12-9
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * ���� def27��Getter����.���������Ƿ����� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * ����def27��Setter����.���������Ƿ����� ��������:2019-12-9
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * ���� def28��Getter����.���������Ƿ�ȫ��ת�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * ����def28��Setter����.���������Ƿ�ȫ��ת�� ��������:2019-12-9
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
	}

	/**
	 * ���� billstatus��Getter����.������������״̬ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.pf.BillStatusEnum
	 */
	public java.lang.Integer getBillstatus() {
		return this.billstatus;
	}

	/**
	 * ����billstatus��Setter����.������������״̬ ��������:2019-12-9
	 * 
	 * @param newBillstatus
	 *            nc.vo.pub.pf.BillStatusEnum
	 */
	public void setBillstatus(java.lang.Integer billstatus) {
		this.billstatus = billstatus;
	}

	/**
	 * ���� approvestatus��Getter����.������������״̬ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.pf.BillStatusEnum
	 */
	public java.lang.Integer getApprovestatus() {
		return this.approvestatus;
	}

	/**
	 * ����approvestatus��Setter����.������������״̬ ��������:2019-12-9
	 * 
	 * @param newApprovestatus
	 *            nc.vo.pub.pf.BillStatusEnum
	 */
	public void setApprovestatus(java.lang.Integer approvestatus) {
		this.approvestatus = approvestatus;
	}

	/**
	 * ���� effectstatus��Getter����.����������Ч״̬ ��������:2019-12-9
	 * 
	 * @return nc.vo.cmp.bill.Effectflag
	 */
	public java.lang.Integer getEffectstatus() {
		return this.effectstatus;
	}

	/**
	 * ����effectstatus��Setter����.����������Ч״̬ ��������:2019-12-9
	 * 
	 * @param newEffectstatus
	 *            nc.vo.cmp.bill.Effectflag
	 */
	public void setEffectstatus(java.lang.Integer effectstatus) {
		this.effectstatus = effectstatus;
	}

	/**
	 * ���� def29��Getter����.���������Զ�����29 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef29() {
		return this.def29;
	}

	/**
	 * ����def29��Setter����.���������Զ�����29 ��������:2019-12-9
	 * 
	 * @param newDef29
	 *            java.lang.String
	 */
	public void setDef29(java.lang.String def29) {
		this.def29 = def29;
	}

	/**
	 * ���� def30��Getter����.���������Զ�����30 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef30() {
		return this.def30;
	}

	/**
	 * ����def30��Setter����.���������Զ�����30 ��������:2019-12-9
	 * 
	 * @param newDef30
	 *            java.lang.String
	 */
	public void setDef30(java.lang.String def30) {
		this.def30 = def30;
	}

	/**
	 * ���� def31��Getter����.���������Զ�����31 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef31() {
		return this.def31;
	}

	/**
	 * ����def31��Setter����.���������Զ�����31 ��������:2019-12-9
	 * 
	 * @param newDef31
	 *            java.lang.String
	 */
	public void setDef31(java.lang.String def31) {
		this.def31 = def31;
	}

	/**
	 * ���� def32��Getter����.���������Զ�����32 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef32() {
		return this.def32;
	}

	/**
	 * ����def32��Setter����.���������Զ�����32 ��������:2019-12-9
	 * 
	 * @param newDef32
	 *            java.lang.String
	 */
	public void setDef32(java.lang.String def32) {
		this.def32 = def32;
	}

	/**
	 * ���� def33��Getter����.���������Զ�����33 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef33() {
		return this.def33;
	}

	/**
	 * ����def33��Setter����.���������Զ�����33 ��������:2019-12-9
	 * 
	 * @param newDef33
	 *            java.lang.String
	 */
	public void setDef33(java.lang.String def33) {
		this.def33 = def33;
	}

	/**
	 * ���� def34��Getter����.���������Զ�����34 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef34() {
		return this.def34;
	}

	/**
	 * ����def34��Setter����.���������Զ�����34 ��������:2019-12-9
	 * 
	 * @param newDef34
	 *            java.lang.String
	 */
	public void setDef34(java.lang.String def34) {
		this.def34 = def34;
	}

	/**
	 * ���� def35��Getter����.���������Զ�����35 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef35() {
		return this.def35;
	}

	/**
	 * ����def35��Setter����.���������Զ�����35 ��������:2019-12-9
	 * 
	 * @param newDef35
	 *            java.lang.String
	 */
	public void setDef35(java.lang.String def35) {
		this.def35 = def35;
	}

	/**
	 * ���� def36��Getter����.���������Զ�����36 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef36() {
		return this.def36;
	}

	/**
	 * ����def36��Setter����.���������Զ�����36 ��������:2019-12-9
	 * 
	 * @param newDef36
	 *            java.lang.String
	 */
	public void setDef36(java.lang.String def36) {
		this.def36 = def36;
	}

	/**
	 * ���� def37��Getter����.���������Զ�����37 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef37() {
		return this.def37;
	}

	/**
	 * ����def37��Setter����.���������Զ�����37 ��������:2019-12-9
	 * 
	 * @param newDef37
	 *            java.lang.String
	 */
	public void setDef37(java.lang.String def37) {
		this.def37 = def37;
	}

	/**
	 * ���� def38��Getter����.���������Զ�����38 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef38() {
		return this.def38;
	}

	/**
	 * ����def38��Setter����.���������Զ�����38 ��������:2019-12-9
	 * 
	 * @param newDef38
	 *            java.lang.String
	 */
	public void setDef38(java.lang.String def38) {
		this.def38 = def38;
	}

	/**
	 * ���� def39��Getter����.���������Զ�����39 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef39() {
		return this.def39;
	}

	/**
	 * ����def39��Setter����.���������Զ�����39 ��������:2019-12-9
	 * 
	 * @param newDef39
	 *            java.lang.String
	 */
	public void setDef39(java.lang.String def39) {
		this.def39 = def39;
	}

	/**
	 * ���� def40��Getter����.���������Զ�����40 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef40() {
		return this.def40;
	}

	/**
	 * ����def40��Setter����.���������Զ�����40 ��������:2019-12-9
	 * 
	 * @param newDef40
	 *            java.lang.String
	 */
	public void setDef40(java.lang.String def40) {
		this.def40 = def40;
	}

	/**
	 * ���� def41��Getter����.���������Զ�����41 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef41() {
		return this.def41;
	}

	/**
	 * ����def41��Setter����.���������Զ�����41 ��������:2019-12-9
	 * 
	 * @param newDef41
	 *            java.lang.String
	 */
	public void setDef41(java.lang.String def41) {
		this.def41 = def41;
	}

	/**
	 * ���� def42��Getter����.���������Զ�����42 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef42() {
		return this.def42;
	}

	/**
	 * ����def42��Setter����.���������Զ�����42 ��������:2019-12-9
	 * 
	 * @param newDef42
	 *            java.lang.String
	 */
	public void setDef42(java.lang.String def42) {
		this.def42 = def42;
	}

	/**
	 * ���� def43��Getter����.���������Զ�����43 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef43() {
		return this.def43;
	}

	/**
	 * ����def43��Setter����.���������Զ�����43 ��������:2019-12-9
	 * 
	 * @param newDef43
	 *            java.lang.String
	 */
	public void setDef43(java.lang.String def43) {
		this.def43 = def43;
	}

	/**
	 * ���� def44��Getter����.���������Զ�����44 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef44() {
		return this.def44;
	}

	/**
	 * ����def44��Setter����.���������Զ�����44 ��������:2019-12-9
	 * 
	 * @param newDef44
	 *            java.lang.String
	 */
	public void setDef44(java.lang.String def44) {
		this.def44 = def44;
	}

	/**
	 * ���� def45��Getter����.���������Զ�����45 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef45() {
		return this.def45;
	}

	/**
	 * ����def45��Setter����.���������Զ�����45 ��������:2019-12-9
	 * 
	 * @param newDef45
	 *            java.lang.String
	 */
	public void setDef45(java.lang.String def45) {
		this.def45 = def45;
	}

	/**
	 * ���� def46��Getter����.���������Զ�����46 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef46() {
		return this.def46;
	}

	/**
	 * ����def46��Setter����.���������Զ�����46 ��������:2019-12-9
	 * 
	 * @param newDef46
	 *            java.lang.String
	 */
	public void setDef46(java.lang.String def46) {
		this.def46 = def46;
	}

	/**
	 * ���� def47��Getter����.���������Զ�����47 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef47() {
		return this.def47;
	}

	/**
	 * ����def47��Setter����.���������Զ�����47 ��������:2019-12-9
	 * 
	 * @param newDef47
	 *            java.lang.String
	 */
	public void setDef47(java.lang.String def47) {
		this.def47 = def47;
	}

	/**
	 * ���� def48��Getter����.���������Զ�����48 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef48() {
		return this.def48;
	}

	/**
	 * ����def48��Setter����.���������Զ�����48 ��������:2019-12-9
	 * 
	 * @param newDef48
	 *            java.lang.String
	 */
	public void setDef48(java.lang.String def48) {
		this.def48 = def48;
	}

	/**
	 * ���� def49��Getter����.���������Զ�����49 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef49() {
		return this.def49;
	}

	/**
	 * ����def49��Setter����.���������Զ�����49 ��������:2019-12-9
	 * 
	 * @param newDef49
	 *            java.lang.String
	 */
	public void setDef49(java.lang.String def49) {
		this.def49 = def49;
	}

	/**
	 * ���� def50��Getter����.���������Զ�����50 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef50() {
		return this.def50;
	}

	/**
	 * ����def50��Setter����.���������Զ�����50 ��������:2019-12-9
	 * 
	 * @param newDef50
	 *            java.lang.String
	 */
	public void setDef50(java.lang.String def50) {
		this.def50 = def50;
	}

	/**
	 * ���� def51��Getter����.���������Զ�����51 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef51() {
		return this.def51;
	}

	/**
	 * ����def51��Setter����.���������Զ�����51 ��������:2019-12-9
	 * 
	 * @param newDef51
	 *            java.lang.String
	 */
	public void setDef51(java.lang.String def51) {
		this.def51 = def51;
	}

	/**
	 * ���� def60��Getter����.���������Զ�����60 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef60() {
		return this.def60;
	}

	/**
	 * ����def60��Setter����.���������Զ�����60 ��������:2019-12-9
	 * 
	 * @param newDef60
	 *            java.lang.String
	 */
	public void setDef60(java.lang.String def60) {
		this.def60 = def60;
	}

	/**
	 * ���� def59��Getter����.���������Զ�����59 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef59() {
		return this.def59;
	}

	/**
	 * ����def59��Setter����.���������Զ�����59 ��������:2019-12-9
	 * 
	 * @param newDef59
	 *            java.lang.String
	 */
	public void setDef59(java.lang.String def59) {
		this.def59 = def59;
	}

	/**
	 * ���� def58��Getter����.���������Զ�����58 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef58() {
		return this.def58;
	}

	/**
	 * ����def58��Setter����.���������Զ�����58 ��������:2019-12-9
	 * 
	 * @param newDef58
	 *            java.lang.String
	 */
	public void setDef58(java.lang.String def58) {
		this.def58 = def58;
	}

	/**
	 * ���� def57��Getter����.���������Զ�����57 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef57() {
		return this.def57;
	}

	/**
	 * ����def57��Setter����.���������Զ�����57 ��������:2019-12-9
	 * 
	 * @param newDef57
	 *            java.lang.String
	 */
	public void setDef57(java.lang.String def57) {
		this.def57 = def57;
	}

	/**
	 * ���� def56��Getter����.���������Զ�����56 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef56() {
		return this.def56;
	}

	/**
	 * ����def56��Setter����.���������Զ�����56 ��������:2019-12-9
	 * 
	 * @param newDef56
	 *            java.lang.String
	 */
	public void setDef56(java.lang.String def56) {
		this.def56 = def56;
	}

	/**
	 * ���� def55��Getter����.���������Զ�����55 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef55() {
		return this.def55;
	}

	/**
	 * ����def55��Setter����.���������Զ�����55 ��������:2019-12-9
	 * 
	 * @param newDef55
	 *            java.lang.String
	 */
	public void setDef55(java.lang.String def55) {
		this.def55 = def55;
	}

	/**
	 * ���� def54��Getter����.���������Զ�����54 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef54() {
		return this.def54;
	}

	/**
	 * ����def54��Setter����.���������Զ�����54 ��������:2019-12-9
	 * 
	 * @param newDef54
	 *            java.lang.String
	 */
	public void setDef54(java.lang.String def54) {
		this.def54 = def54;
	}

	/**
	 * ���� def53��Getter����.���������Զ�����53 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef53() {
		return this.def53;
	}

	/**
	 * ����def53��Setter����.���������Զ�����53 ��������:2019-12-9
	 * 
	 * @param newDef53
	 *            java.lang.String
	 */
	public void setDef53(java.lang.String def53) {
		this.def53 = def53;
	}

	/**
	 * ���� def52��Getter����.���������Զ�����52 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef52() {
		return this.def52;
	}

	/**
	 * ����def52��Setter����.���������Զ�����52 ��������:2019-12-9
	 * 
	 * @param newDef52
	 *            java.lang.String
	 */
	public void setDef52(java.lang.String def52) {
		this.def52 = def52;
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2019-12-9
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.payrequest");
	}
}
