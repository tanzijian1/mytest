package nc.vo.tg.paymentrequest;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;
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

public class Payablepage extends SuperVO {

	/**
	 * Ӧ��������֯
	 */
	public java.lang.String pk_org;
	/**
	 * ����������֯
	 */
	public java.lang.String pk_fiorg;
	/**
	 * ��������
	 */
	public java.lang.String pk_pcorg;
	/**
	 * �������İ汾
	 */
	public java.lang.String pk_pcorg_v;
	/**
	 * Ӧ��������֯�汾
	 */
	public java.lang.String pk_org_v;
	/**
	 * ����������֯�汾
	 */
	public java.lang.String pk_fiorg_v;
	/**
	 * ���������֯�汾
	 */
	public java.lang.String sett_org_v;
	/**
	 * ҵ����
	 */
	public java.lang.String pu_deptid;
	/**
	 * ҵ���Ű汾
	 */
	public java.lang.String pu_deptid_v;
	/**
	 * ҵ����Ա
	 */
	public java.lang.String pu_psndoc;
	/**
	 * ҵ����֯
	 */
	public java.lang.String pu_org;
	/**
	 * ҵ����֯�汾
	 */
	public java.lang.String pu_org_v;
	/**
	 * ���������֯
	 */
	public java.lang.String sett_org;
	/**
	 * ����
	 */
	public java.lang.String material;
	/**
	 * ��Ӧ��
	 */
	public java.lang.String supplier;
	/**
	 * ���ۼ�����λ
	 */
	public java.lang.String postunit;
	/**
	 * ���۵�λ��˰����
	 */
	public nc.vo.pub.lang.UFDouble postpricenotax;
	/**
	 * ���۵�λ����
	 */
	public nc.vo.pub.lang.UFDouble postquantity;
	/**
	 * ���۵�λ��˰����
	 */
	public nc.vo.pub.lang.UFDouble postprice;
	/**
	 * ����Эͬ״̬
	 */
	public java.lang.Integer coordflag;
	/**
	 * �豸����
	 */
	public java.lang.String equipmentcode;
	/**
	 * ��Ʒ��
	 */
	public java.lang.String productline;
	/**
	 * �ֽ�������Ŀ
	 */
	public java.lang.String cashitem;
	/**
	 * �ʽ�ƻ���Ŀ
	 */
	public java.lang.String bankrollprojet;
	/**
	 * �����־
	 */
	public UFBoolean pausetransact;
	/**
	 * ��������
	 */
	public UFDate billdate;
	/**
	 * ��������
	 */
	public java.lang.String pk_group;
	/**
	 * �������ͱ���
	 */
	public java.lang.String pk_billtype;
	/**
	 * ���ݴ���
	 */
	public java.lang.String billclass;
	/**
	 * Ӧ������code
	 */
	public java.lang.String pk_tradetype;
	/**
	 * Ӧ������
	 */
	public java.lang.String pk_tradetypeid;
	/**
	 * Ӧ�����б�ʶ
	 */
	public java.lang.String pk_payableitem;
	/**
	 * ��������
	 */
	public UFDate busidate;
	/**
	 * ��֧��Ŀ
	 */
	public java.lang.String pk_subjcode;
	/**
	 * ���ݺ�
	 */
	public java.lang.String billno;
	/**
	 * ��������
	 */
	public java.lang.Integer objtype;
	/**
	 * ���ݷ�¼��
	 */
	public java.lang.Integer rowno;
	/**
	 * ������
	 */
	public java.lang.Integer rowtype;
	/**
	 * ����
	 */
	public java.lang.Integer direction;
	/**
	 * Ʊ������
	 */
	public java.lang.String checktype;
	/**
	 * ����������
	 */
	public java.lang.String pk_ssitem;
	/**
	 * ժҪ
	 */
	public java.lang.String scomment;
	/**
	 * ��Ŀ
	 */
	public java.lang.String subjcode;
	/**
	 * ����
	 */
	public java.lang.String pk_currtype;
	/**
	 * ��֯���һ���
	 */
	public nc.vo.pub.lang.UFDouble rate;
	/**
	 * ����
	 */
	public java.lang.String pk_deptid;
	/**
	 * �� ��
	 */
	public java.lang.String pk_deptid_v;
	/**
	 * ҵ��Ա
	 */
	public java.lang.String pk_psndoc;
	/**
	 * ��������
	 */
	public nc.vo.pub.lang.UFDouble quantity_cr;
	/**
	 * ��֯���ҽ��
	 */
	public nc.vo.pub.lang.UFDouble local_money_cr;
	/**
	 * ����ԭ�ҽ��
	 */
	public nc.vo.pub.lang.UFDouble money_cr;
	/**
	 * ԭ�����
	 */
	public nc.vo.pub.lang.UFDouble money_bal;
	/**
	 * ��֯�������
	 */
	public nc.vo.pub.lang.UFDouble local_money_bal;
	/**
	 * �������
	 */
	public nc.vo.pub.lang.UFDouble quantity_bal;
	/**
	 * ˰��
	 */
	public nc.vo.pub.lang.UFDouble local_tax_cr;
	/**
	 * ����ԭ����˰���
	 */
	public nc.vo.pub.lang.UFDouble notax_cr;
	/**
	 * ��֯������˰���
	 */
	public nc.vo.pub.lang.UFDouble local_notax_cr;
	/**
	 * ����
	 */
	public nc.vo.pub.lang.UFDouble price;
	/**
	 * ��˰����
	 */
	public nc.vo.pub.lang.UFDouble taxprice;
	/**
	 * ˰��
	 */
	public nc.vo.pub.lang.UFDouble taxrate;
	/**
	 * ˰��
	 */
	public java.lang.String taxnum;
	/**
	 * �ϲ㵥������
	 */
	public java.lang.String top_billid;
	/**
	 * �ϲ㵥��������
	 */
	public java.lang.String top_itemid;
	/**
	 * �ϲ㵥������
	 */
	public java.lang.String top_billtype;
	/**
	 * �ϲ㽻������
	 */
	public java.lang.String top_tradetype;
	/**
	 * Դͷ��������
	 */
	public java.lang.String src_tradetype;
	/**
	 * Դͷ��������
	 */
	public java.lang.String src_billtype;
	/**
	 * Դͷ��������
	 */
	public java.lang.String src_billid;
	/**
	 * Դͷ����������
	 */
	public java.lang.String src_itemid;
	/**
	 * ��˰���
	 */
	public java.lang.Integer taxtype;
	/**
	 * ����Э��
	 */
	public java.lang.String pk_payterm;
	/**
	 * ���������˻�
	 */
	public java.lang.String payaccount;
	/**
	 * �տ������˻�
	 */
	public java.lang.String recaccount;
	/**
	 * ������Ӧ��
	 */
	public java.lang.String ordercubasdoc;
	/**
	 * ����������
	 */
	public java.lang.String innerorderno;
	/**
	 * �ʲ���ͬ��
	 */
	public java.lang.String assetpactno;
	/**
	 * ��ͬ��
	 */
	public java.lang.String contractno;
	/**
	 * ɢ��
	 */
	public java.lang.String freecust;
	/**
	 * �̶��ʲ���Ƭ��
	 */
	public java.lang.String facard;
	/**
	 * ������
	 */
	public java.lang.String purchaseorder;
	/**
	 * ��Ʊ��
	 */
	public java.lang.String invoiceno;
	/**
	 * ���ⵥ��
	 */
	public java.lang.String outstoreno;
	/**
	 * ���κ���Ҫ��
	 */
	public java.lang.String checkelement;
	/**
	 * ���ű��һ���
	 */
	public nc.vo.pub.lang.UFDouble grouprate;
	/**
	 * ȫ�ֱ��һ���
	 */
	public nc.vo.pub.lang.UFDouble globalrate;
	/**
	 * ���ű��ҽ��(����)
	 */
	public nc.vo.pub.lang.UFDouble groupcrebit;
	/**
	 * ȫ�ֱ��ҽ��(����)
	 */
	public nc.vo.pub.lang.UFDouble globalcrebit;
	/**
	 * ���ű������
	 */
	public nc.vo.pub.lang.UFDouble groupbalance;
	/**
	 * ȫ�ֱ������
	 */
	public nc.vo.pub.lang.UFDouble globalbalance;
	/**
	 * ���ű�����˰���(����)
	 */
	public nc.vo.pub.lang.UFDouble groupnotax_cre;
	/**
	 * ȫ�ֱ�����˰���(����)
	 */
	public nc.vo.pub.lang.UFDouble globalnotax_cre;
	/**
	 * Ԥռ��ԭ�����
	 */
	public nc.vo.pub.lang.UFDouble occupationmny;
	/**
	 * ��Ŀ
	 */
	public java.lang.String project;
	/**
	 * ��Ŀ����
	 */
	public java.lang.String project_task;
	/**
	 * �����嵥��
	 */
	public java.lang.String settleno;
	/**
	 * ���ҵ���
	 */
	public nc.vo.pub.lang.UFDouble local_price;
	/**
	 * ���Һ�˰����
	 */
	public nc.vo.pub.lang.UFDouble local_taxprice;
	/**
	 * �ɱ�����
	 */
	public java.lang.String costcenter;
	/**
	 * �ڲ����׽����
	 */
	public java.lang.String confernum;
	/**
	 * ������
	 */
	public java.lang.String sendcountryid;
	/**
	 * ��������
	 */
	public java.lang.Integer buysellflag;
	/**
	 * ˰��
	 */
	public java.lang.String taxcodeid;
	/**
	 * ���ɵֿ�˰��
	 */
	public nc.vo.pub.lang.UFDouble nosubtaxrate;
	/**
	 * ���ɵֿ�˰��
	 */
	public nc.vo.pub.lang.UFDouble nosubtax;
	/**
	 * ��˰���
	 */
	public nc.vo.pub.lang.UFDouble caltaxmny;
	/**
	 * �Ƿ�������˰
	 */
	public UFBoolean opptaxflag;
	/**
	 * ��Ӧ��VATע����
	 */
	public java.lang.String vendorvatcode;
	/**
	 * VATע����
	 */
	public java.lang.String vatcode;
	/**
	 * ��Ӧ��Ӧ������ʶ
	 */
	public java.lang.String pk_payablebill;
	/**
	 * ԭʼ����
	 */
	public java.lang.String material_src;
	/**
	 * ������
	 */
	public nc.vo.pub.lang.UFDouble settlemoney;
	/**
	 * �������
	 */
	public java.lang.String settlecurr;
	/**
	 * ���㷽ʽ
	 */
	public java.lang.String pk_balatype;
	/**
	 * CBS
	 */
	public java.lang.String cbs;
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
	 * �ϲ㵥������
	 */
	public String pk_payreq;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ���� pk_org��Getter����.��������Ӧ��������֯ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_org() {
		return this.pk_org;
	}

	/**
	 * ����pk_org��Setter����.��������Ӧ��������֯ ��������:2019-12-9
	 * 
	 * @param newPk_org
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_org(java.lang.String pk_org) {
		this.pk_org = pk_org;
	}

	/**
	 * ���� pk_fiorg��Getter����.������������������֯ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getPk_fiorg() {
		return this.pk_fiorg;
	}

	/**
	 * ����pk_fiorg��Setter����.������������������֯ ��������:2019-12-9
	 * 
	 * @param newPk_fiorg
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setPk_fiorg(java.lang.String pk_fiorg) {
		this.pk_fiorg = pk_fiorg;
	}

	/**
	 * ���� pk_pcorg��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.org.LiabilityCenterVO
	 */
	public java.lang.String getPk_pcorg() {
		return this.pk_pcorg;
	}

	/**
	 * ����pk_pcorg��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newPk_pcorg
	 *            nc.vo.org.LiabilityCenterVO
	 */
	public void setPk_pcorg(java.lang.String pk_pcorg) {
		this.pk_pcorg = pk_pcorg;
	}

	/**
	 * ���� pk_pcorg_v��Getter����.���������������İ汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.LiabilityCenterVersionVO
	 */
	public java.lang.String getPk_pcorg_v() {
		return this.pk_pcorg_v;
	}

	/**
	 * ����pk_pcorg_v��Setter����.���������������İ汾 ��������:2019-12-9
	 * 
	 * @param newPk_pcorg_v
	 *            nc.vo.vorg.LiabilityCenterVersionVO
	 */
	public void setPk_pcorg_v(java.lang.String pk_pcorg_v) {
		this.pk_pcorg_v = pk_pcorg_v;
	}

	/**
	 * ���� pk_org_v��Getter����.��������Ӧ��������֯�汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getPk_org_v() {
		return this.pk_org_v;
	}

	/**
	 * ����pk_org_v��Setter����.��������Ӧ��������֯�汾 ��������:2019-12-9
	 * 
	 * @param newPk_org_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setPk_org_v(java.lang.String pk_org_v) {
		this.pk_org_v = pk_org_v;
	}

	/**
	 * ���� pk_fiorg_v��Getter����.������������������֯�汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getPk_fiorg_v() {
		return this.pk_fiorg_v;
	}

	/**
	 * ����pk_fiorg_v��Setter����.������������������֯�汾 ��������:2019-12-9
	 * 
	 * @param newPk_fiorg_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setPk_fiorg_v(java.lang.String pk_fiorg_v) {
		this.pk_fiorg_v = pk_fiorg_v;
	}

	/**
	 * ���� sett_org_v��Getter����.�����������������֯�汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.FinanceOrgVersionVO
	 */
	public java.lang.String getSett_org_v() {
		return this.sett_org_v;
	}

	/**
	 * ����sett_org_v��Setter����.�����������������֯�汾 ��������:2019-12-9
	 * 
	 * @param newSett_org_v
	 *            nc.vo.vorg.FinanceOrgVersionVO
	 */
	public void setSett_org_v(java.lang.String sett_org_v) {
		this.sett_org_v = sett_org_v;
	}

	/**
	 * ���� pu_deptid��Getter����.��������ҵ���� ��������:2019-12-9
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPu_deptid() {
		return this.pu_deptid;
	}

	/**
	 * ����pu_deptid��Setter����.��������ҵ���� ��������:2019-12-9
	 * 
	 * @param newPu_deptid
	 *            nc.vo.org.DeptVO
	 */
	public void setPu_deptid(java.lang.String pu_deptid) {
		this.pu_deptid = pu_deptid;
	}

	/**
	 * ���� pu_deptid_v��Getter����.��������ҵ���Ű汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.DeptVersionVO
	 */
	public java.lang.String getPu_deptid_v() {
		return this.pu_deptid_v;
	}

	/**
	 * ����pu_deptid_v��Setter����.��������ҵ���Ű汾 ��������:2019-12-9
	 * 
	 * @param newPu_deptid_v
	 *            nc.vo.vorg.DeptVersionVO
	 */
	public void setPu_deptid_v(java.lang.String pu_deptid_v) {
		this.pu_deptid_v = pu_deptid_v;
	}

	/**
	 * ���� pu_psndoc��Getter����.��������ҵ����Ա ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPu_psndoc() {
		return this.pu_psndoc;
	}

	/**
	 * ����pu_psndoc��Setter����.��������ҵ����Ա ��������:2019-12-9
	 * 
	 * @param newPu_psndoc
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPu_psndoc(java.lang.String pu_psndoc) {
		this.pu_psndoc = pu_psndoc;
	}

	/**
	 * ���� pu_org��Getter����.��������ҵ����֯ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.OrgVO
	 */
	public java.lang.String getPu_org() {
		return this.pu_org;
	}

	/**
	 * ����pu_org��Setter����.��������ҵ����֯ ��������:2019-12-9
	 * 
	 * @param newPu_org
	 *            nc.vo.org.OrgVO
	 */
	public void setPu_org(java.lang.String pu_org) {
		this.pu_org = pu_org;
	}

	/**
	 * ���� pu_org_v��Getter����.��������ҵ����֯�汾 ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.OrgVersionVO
	 */
	public java.lang.String getPu_org_v() {
		return this.pu_org_v;
	}

	/**
	 * ����pu_org_v��Setter����.��������ҵ����֯�汾 ��������:2019-12-9
	 * 
	 * @param newPu_org_v
	 *            nc.vo.vorg.OrgVersionVO
	 */
	public void setPu_org_v(java.lang.String pu_org_v) {
		this.pu_org_v = pu_org_v;
	}

	/**
	 * ���� sett_org��Getter����.�����������������֯ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.FinanceOrgVO
	 */
	public java.lang.String getSett_org() {
		return this.sett_org;
	}

	/**
	 * ����sett_org��Setter����.�����������������֯ ��������:2019-12-9
	 * 
	 * @param newSett_org
	 *            nc.vo.org.FinanceOrgVO
	 */
	public void setSett_org(java.lang.String sett_org) {
		this.sett_org = sett_org;
	}

	/**
	 * ���� material��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.material.MaterialVO
	 */
	public java.lang.String getMaterial() {
		return this.material;
	}

	/**
	 * ����material��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newMaterial
	 *            nc.vo.bd.material.MaterialVO
	 */
	public void setMaterial(java.lang.String material) {
		this.material = material;
	}

	/**
	 * ���� supplier��Getter����.����������Ӧ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.supplier.SupplierVO
	 */
	public java.lang.String getSupplier() {
		return this.supplier;
	}

	/**
	 * ����supplier��Setter����.����������Ӧ�� ��������:2019-12-9
	 * 
	 * @param newSupplier
	 *            nc.vo.bd.supplier.SupplierVO
	 */
	public void setSupplier(java.lang.String supplier) {
		this.supplier = supplier;
	}

	/**
	 * ���� postunit��Getter����.�����������ۼ�����λ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.material.measdoc.MeasdocVO
	 */
	public java.lang.String getPostunit() {
		return this.postunit;
	}

	/**
	 * ����postunit��Setter����.�����������ۼ�����λ ��������:2019-12-9
	 * 
	 * @param newPostunit
	 *            nc.vo.bd.material.measdoc.MeasdocVO
	 */
	public void setPostunit(java.lang.String postunit) {
		this.postunit = postunit;
	}

	/**
	 * ���� postpricenotax��Getter����.�����������۵�λ��˰���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostpricenotax() {
		return this.postpricenotax;
	}

	/**
	 * ����postpricenotax��Setter����.�����������۵�λ��˰���� ��������:2019-12-9
	 * 
	 * @param newPostpricenotax
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostpricenotax(nc.vo.pub.lang.UFDouble postpricenotax) {
		this.postpricenotax = postpricenotax;
	}

	/**
	 * ���� postquantity��Getter����.�����������۵�λ���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostquantity() {
		return this.postquantity;
	}

	/**
	 * ����postquantity��Setter����.�����������۵�λ���� ��������:2019-12-9
	 * 
	 * @param newPostquantity
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostquantity(nc.vo.pub.lang.UFDouble postquantity) {
		this.postquantity = postquantity;
	}

	/**
	 * ���� postprice��Getter����.�����������۵�λ��˰���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPostprice() {
		return this.postprice;
	}

	/**
	 * ����postprice��Setter����.�����������۵�λ��˰���� ��������:2019-12-9
	 * 
	 * @param newPostprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPostprice(nc.vo.pub.lang.UFDouble postprice) {
		this.postprice = postprice;
	}

	/**
	 * ���� coordflag��Getter����.������������Эͬ״̬ ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getCoordflag() {
		return this.coordflag;
	}

	/**
	 * ����coordflag��Setter����.������������Эͬ״̬ ��������:2019-12-9
	 * 
	 * @param newCoordflag
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setCoordflag(java.lang.Integer coordflag) {
		this.coordflag = coordflag;
	}

	/**
	 * ���� equipmentcode��Getter����.���������豸���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getEquipmentcode() {
		return this.equipmentcode;
	}

	/**
	 * ����equipmentcode��Setter����.���������豸���� ��������:2019-12-9
	 * 
	 * @param newEquipmentcode
	 *            java.lang.String
	 */
	public void setEquipmentcode(java.lang.String equipmentcode) {
		this.equipmentcode = equipmentcode;
	}

	/**
	 * ���� productline��Getter����.����������Ʒ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.prodline.ProdLineVO
	 */
	public java.lang.String getProductline() {
		return this.productline;
	}

	/**
	 * ����productline��Setter����.����������Ʒ�� ��������:2019-12-9
	 * 
	 * @param newProductline
	 *            nc.vo.bd.prodline.ProdLineVO
	 */
	public void setProductline(java.lang.String productline) {
		this.productline = productline;
	}

	/**
	 * ���� cashitem��Getter����.���������ֽ�������Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.cashflow.CashflowVO
	 */
	public java.lang.String getCashitem() {
		return this.cashitem;
	}

	/**
	 * ����cashitem��Setter����.���������ֽ�������Ŀ ��������:2019-12-9
	 * 
	 * @param newCashitem
	 *            nc.vo.bd.cashflow.CashflowVO
	 */
	public void setCashitem(java.lang.String cashitem) {
		this.cashitem = cashitem;
	}

	/**
	 * ���� bankrollprojet��Getter����.���������ʽ�ƻ���Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.fundplan.FundPlanVO
	 */
	public java.lang.String getBankrollprojet() {
		return this.bankrollprojet;
	}

	/**
	 * ����bankrollprojet��Setter����.���������ʽ�ƻ���Ŀ ��������:2019-12-9
	 * 
	 * @param newBankrollprojet
	 *            nc.vo.bd.fundplan.FundPlanVO
	 */
	public void setBankrollprojet(java.lang.String bankrollprojet) {
		this.bankrollprojet = bankrollprojet;
	}

	/**
	 * ���� pausetransact��Getter����.�������������־ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getPausetransact() {
		return this.pausetransact;
	}

	/**
	 * ����pausetransact��Setter����.�������������־ ��������:2019-12-9
	 * 
	 * @param newPausetransact
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setPausetransact(UFBoolean pausetransact) {
		this.pausetransact = pausetransact;
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
	 * ���� pk_group��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.org.GroupVO
	 */
	public java.lang.String getPk_group() {
		return this.pk_group;
	}

	/**
	 * ����pk_group��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newPk_group
	 *            nc.vo.org.GroupVO
	 */
	public void setPk_group(java.lang.String pk_group) {
		this.pk_group = pk_group;
	}

	/**
	 * ���� pk_billtype��Getter����.���������������ͱ��� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_billtype() {
		return this.pk_billtype;
	}

	/**
	 * ����pk_billtype��Setter����.���������������ͱ��� ��������:2019-12-9
	 * 
	 * @param newPk_billtype
	 *            java.lang.String
	 */
	public void setPk_billtype(java.lang.String pk_billtype) {
		this.pk_billtype = pk_billtype;
	}

	/**
	 * ���� billclass��Getter����.�����������ݴ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.String getBillclass() {
		return this.billclass;
	}

	/**
	 * ����billclass��Setter����.�����������ݴ��� ��������:2019-12-9
	 * 
	 * @param newBillclass
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setBillclass(java.lang.String billclass) {
		this.billclass = billclass;
	}

	/**
	 * ���� pk_tradetype��Getter����.��������Ӧ������code ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_tradetype() {
		return this.pk_tradetype;
	}

	/**
	 * ����pk_tradetype��Setter����.��������Ӧ������code ��������:2019-12-9
	 * 
	 * @param newPk_tradetype
	 *            java.lang.String
	 */
	public void setPk_tradetype(java.lang.String pk_tradetype) {
		this.pk_tradetype = pk_tradetype;
	}

	/**
	 * ���� pk_tradetypeid��Getter����.��������Ӧ������ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.billtype.BilltypeVO
	 */
	public java.lang.String getPk_tradetypeid() {
		return this.pk_tradetypeid;
	}

	/**
	 * ����pk_tradetypeid��Setter����.��������Ӧ������ ��������:2019-12-9
	 * 
	 * @param newPk_tradetypeid
	 *            nc.vo.pub.billtype.BilltypeVO
	 */
	public void setPk_tradetypeid(java.lang.String pk_tradetypeid) {
		this.pk_tradetypeid = pk_tradetypeid;
	}

	/**
	 * ���� pk_payableitem��Getter����.��������Ӧ�����б�ʶ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_payableitem() {
		return this.pk_payableitem;
	}

	/**
	 * ����pk_payableitem��Setter����.��������Ӧ�����б�ʶ ��������:2019-12-9
	 * 
	 * @param newPk_payableitem
	 *            java.lang.String
	 */
	public void setPk_payableitem(java.lang.String pk_payableitem) {
		this.pk_payableitem = pk_payableitem;
	}

	/**
	 * ���� busidate��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDate
	 */
	public UFDate getBusidate() {
		return this.busidate;
	}

	/**
	 * ����busidate��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newBusidate
	 *            nc.vo.pub.lang.UFDate
	 */
	public void setBusidate(UFDate busidate) {
		this.busidate = busidate;
	}

	/**
	 * ���� pk_subjcode��Getter����.����������֧��Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.inoutbusiclass.InoutBusiClassVO
	 */
	public java.lang.String getPk_subjcode() {
		return this.pk_subjcode;
	}

	/**
	 * ����pk_subjcode��Setter����.����������֧��Ŀ ��������:2019-12-9
	 * 
	 * @param newPk_subjcode
	 *            nc.vo.bd.inoutbusiclass.InoutBusiClassVO
	 */
	public void setPk_subjcode(java.lang.String pk_subjcode) {
		this.pk_subjcode = pk_subjcode;
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
	 * ���� objtype��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getObjtype() {
		return this.objtype;
	}

	/**
	 * ����objtype��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newObjtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setObjtype(java.lang.Integer objtype) {
		this.objtype = objtype;
	}

	/**
	 * ���� rowno��Getter����.�����������ݷ�¼�� ��������:2019-12-9
	 * 
	 * @return java.lang.Integer
	 */
	public java.lang.Integer getRowno() {
		return this.rowno;
	}

	/**
	 * ����rowno��Setter����.�����������ݷ�¼�� ��������:2019-12-9
	 * 
	 * @param newRowno
	 *            java.lang.Integer
	 */
	public void setRowno(java.lang.Integer rowno) {
		this.rowno = rowno;
	}

	/**
	 * ���� rowtype��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getRowtype() {
		return this.rowtype;
	}

	/**
	 * ����rowtype��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newRowtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setRowtype(java.lang.Integer rowtype) {
		this.rowtype = rowtype;
	}

	/**
	 * ���� direction��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getDirection() {
		return this.direction;
	}

	/**
	 * ����direction��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newDirection
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setDirection(java.lang.Integer direction) {
		this.direction = direction;
	}

	/**
	 * ���� checktype��Getter����.��������Ʊ������ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.notetype.NotetypeVO
	 */
	public java.lang.String getChecktype() {
		return this.checktype;
	}

	/**
	 * ����checktype��Setter����.��������Ʊ������ ��������:2019-12-9
	 * 
	 * @param newChecktype
	 *            nc.vo.bd.notetype.NotetypeVO
	 */
	public void setChecktype(java.lang.String checktype) {
		this.checktype = checktype;
	}

	/**
	 * ���� pk_ssitem��Getter����.������������������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPk_ssitem() {
		return this.pk_ssitem;
	}

	/**
	 * ����pk_ssitem��Setter����.������������������ ��������:2019-12-9
	 * 
	 * @param newPk_ssitem
	 *            java.lang.String
	 */
	public void setPk_ssitem(java.lang.String pk_ssitem) {
		this.pk_ssitem = pk_ssitem;
	}

	/**
	 * ���� scomment��Getter����.��������ժҪ ��������:2019-12-9
	 * 
	 * @return nc.vo.fipub.summary.SummaryVO
	 */
	public java.lang.String getScomment() {
		return this.scomment;
	}

	/**
	 * ����scomment��Setter����.��������ժҪ ��������:2019-12-9
	 * 
	 * @param newScomment
	 *            nc.vo.fipub.summary.SummaryVO
	 */
	public void setScomment(java.lang.String scomment) {
		this.scomment = scomment;
	}

	/**
	 * ���� subjcode��Getter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.account.AccAsoaVO
	 */
	public java.lang.String getSubjcode() {
		return this.subjcode;
	}

	/**
	 * ����subjcode��Setter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @param newSubjcode
	 *            nc.vo.bd.account.AccAsoaVO
	 */
	public void setSubjcode(java.lang.String subjcode) {
		this.subjcode = subjcode;
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
	 * ���� rate��Getter����.����������֯���һ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getRate() {
		return this.rate;
	}

	/**
	 * ����rate��Setter����.����������֯���һ��� ��������:2019-12-9
	 * 
	 * @param newRate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setRate(nc.vo.pub.lang.UFDouble rate) {
		this.rate = rate;
	}

	/**
	 * ���� pk_deptid��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.org.DeptVO
	 */
	public java.lang.String getPk_deptid() {
		return this.pk_deptid;
	}

	/**
	 * ����pk_deptid��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newPk_deptid
	 *            nc.vo.org.DeptVO
	 */
	public void setPk_deptid(java.lang.String pk_deptid) {
		this.pk_deptid = pk_deptid;
	}

	/**
	 * ���� pk_deptid_v��Getter����.���������� �� ��������:2019-12-9
	 * 
	 * @return nc.vo.vorg.DeptVersionVO
	 */
	public java.lang.String getPk_deptid_v() {
		return this.pk_deptid_v;
	}

	/**
	 * ����pk_deptid_v��Setter����.���������� �� ��������:2019-12-9
	 * 
	 * @param newPk_deptid_v
	 *            nc.vo.vorg.DeptVersionVO
	 */
	public void setPk_deptid_v(java.lang.String pk_deptid_v) {
		this.pk_deptid_v = pk_deptid_v;
	}

	/**
	 * ���� pk_psndoc��Getter����.��������ҵ��Ա ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.psn.PsndocVO
	 */
	public java.lang.String getPk_psndoc() {
		return this.pk_psndoc;
	}

	/**
	 * ����pk_psndoc��Setter����.��������ҵ��Ա ��������:2019-12-9
	 * 
	 * @param newPk_psndoc
	 *            nc.vo.bd.psn.PsndocVO
	 */
	public void setPk_psndoc(java.lang.String pk_psndoc) {
		this.pk_psndoc = pk_psndoc;
	}

	/**
	 * ���� quantity_cr��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getQuantity_cr() {
		return this.quantity_cr;
	}

	/**
	 * ����quantity_cr��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newQuantity_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setQuantity_cr(nc.vo.pub.lang.UFDouble quantity_cr) {
		this.quantity_cr = quantity_cr;
	}

	/**
	 * ���� local_money_cr��Getter����.����������֯���ҽ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_money_cr() {
		return this.local_money_cr;
	}

	/**
	 * ����local_money_cr��Setter����.����������֯���ҽ�� ��������:2019-12-9
	 * 
	 * @param newLocal_money_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_money_cr(nc.vo.pub.lang.UFDouble local_money_cr) {
		this.local_money_cr = local_money_cr;
	}

	/**
	 * ���� money_cr��Getter����.������������ԭ�ҽ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney_cr() {
		return this.money_cr;
	}

	/**
	 * ����money_cr��Setter����.������������ԭ�ҽ�� ��������:2019-12-9
	 * 
	 * @param newMoney_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setMoney_cr(nc.vo.pub.lang.UFDouble money_cr) {
		this.money_cr = money_cr;
	}

	/**
	 * ���� money_bal��Getter����.��������ԭ����� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getMoney_bal() {
		return this.money_bal;
	}

	/**
	 * ����money_bal��Setter����.��������ԭ����� ��������:2019-12-9
	 * 
	 * @param newMoney_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setMoney_bal(nc.vo.pub.lang.UFDouble money_bal) {
		this.money_bal = money_bal;
	}

	/**
	 * ���� local_money_bal��Getter����.����������֯������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_money_bal() {
		return this.local_money_bal;
	}

	/**
	 * ����local_money_bal��Setter����.����������֯������� ��������:2019-12-9
	 * 
	 * @param newLocal_money_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_money_bal(nc.vo.pub.lang.UFDouble local_money_bal) {
		this.local_money_bal = local_money_bal;
	}

	/**
	 * ���� quantity_bal��Getter����.��������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getQuantity_bal() {
		return this.quantity_bal;
	}

	/**
	 * ����quantity_bal��Setter����.��������������� ��������:2019-12-9
	 * 
	 * @param newQuantity_bal
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setQuantity_bal(nc.vo.pub.lang.UFDouble quantity_bal) {
		this.quantity_bal = quantity_bal;
	}

	/**
	 * ���� local_tax_cr��Getter����.��������˰�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_tax_cr() {
		return this.local_tax_cr;
	}

	/**
	 * ����local_tax_cr��Setter����.��������˰�� ��������:2019-12-9
	 * 
	 * @param newLocal_tax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_tax_cr(nc.vo.pub.lang.UFDouble local_tax_cr) {
		this.local_tax_cr = local_tax_cr;
	}

	/**
	 * ���� notax_cr��Getter����.������������ԭ����˰��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNotax_cr() {
		return this.notax_cr;
	}

	/**
	 * ����notax_cr��Setter����.������������ԭ����˰��� ��������:2019-12-9
	 * 
	 * @param newNotax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNotax_cr(nc.vo.pub.lang.UFDouble notax_cr) {
		this.notax_cr = notax_cr;
	}

	/**
	 * ���� local_notax_cr��Getter����.����������֯������˰��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_notax_cr() {
		return this.local_notax_cr;
	}

	/**
	 * ����local_notax_cr��Setter����.����������֯������˰��� ��������:2019-12-9
	 * 
	 * @param newLocal_notax_cr
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_notax_cr(nc.vo.pub.lang.UFDouble local_notax_cr) {
		this.local_notax_cr = local_notax_cr;
	}

	/**
	 * ���� price��Getter����.������������ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getPrice() {
		return this.price;
	}

	/**
	 * ����price��Setter����.������������ ��������:2019-12-9
	 * 
	 * @param newPrice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setPrice(nc.vo.pub.lang.UFDouble price) {
		this.price = price;
	}

	/**
	 * ���� taxprice��Getter����.����������˰���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getTaxprice() {
		return this.taxprice;
	}

	/**
	 * ����taxprice��Setter����.����������˰���� ��������:2019-12-9
	 * 
	 * @param newTaxprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setTaxprice(nc.vo.pub.lang.UFDouble taxprice) {
		this.taxprice = taxprice;
	}

	/**
	 * ���� taxrate��Getter����.��������˰�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getTaxrate() {
		return this.taxrate;
	}

	/**
	 * ����taxrate��Setter����.��������˰�� ��������:2019-12-9
	 * 
	 * @param newTaxrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setTaxrate(nc.vo.pub.lang.UFDouble taxrate) {
		this.taxrate = taxrate;
	}

	/**
	 * ���� taxnum��Getter����.��������˰�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTaxnum() {
		return this.taxnum;
	}

	/**
	 * ����taxnum��Setter����.��������˰�� ��������:2019-12-9
	 * 
	 * @param newTaxnum
	 *            java.lang.String
	 */
	public void setTaxnum(java.lang.String taxnum) {
		this.taxnum = taxnum;
	}

	/**
	 * ���� top_billid��Getter����.���������ϲ㵥������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_billid() {
		return this.top_billid;
	}

	/**
	 * ����top_billid��Setter����.���������ϲ㵥������ ��������:2019-12-9
	 * 
	 * @param newTop_billid
	 *            java.lang.String
	 */
	public void setTop_billid(java.lang.String top_billid) {
		this.top_billid = top_billid;
	}

	/**
	 * ���� top_itemid��Getter����.���������ϲ㵥�������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_itemid() {
		return this.top_itemid;
	}

	/**
	 * ����top_itemid��Setter����.���������ϲ㵥�������� ��������:2019-12-9
	 * 
	 * @param newTop_itemid
	 *            java.lang.String
	 */
	public void setTop_itemid(java.lang.String top_itemid) {
		this.top_itemid = top_itemid;
	}

	/**
	 * ���� top_billtype��Getter����.���������ϲ㵥������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_billtype() {
		return this.top_billtype;
	}

	/**
	 * ����top_billtype��Setter����.���������ϲ㵥������ ��������:2019-12-9
	 * 
	 * @param newTop_billtype
	 *            java.lang.String
	 */
	public void setTop_billtype(java.lang.String top_billtype) {
		this.top_billtype = top_billtype;
	}

	/**
	 * ���� top_tradetype��Getter����.���������ϲ㽻������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getTop_tradetype() {
		return this.top_tradetype;
	}

	/**
	 * ����top_tradetype��Setter����.���������ϲ㽻������ ��������:2019-12-9
	 * 
	 * @param newTop_tradetype
	 *            java.lang.String
	 */
	public void setTop_tradetype(java.lang.String top_tradetype) {
		this.top_tradetype = top_tradetype;
	}

	/**
	 * ���� src_tradetype��Getter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_tradetype() {
		return this.src_tradetype;
	}

	/**
	 * ����src_tradetype��Setter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @param newSrc_tradetype
	 *            java.lang.String
	 */
	public void setSrc_tradetype(java.lang.String src_tradetype) {
		this.src_tradetype = src_tradetype;
	}

	/**
	 * ���� src_billtype��Getter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_billtype() {
		return this.src_billtype;
	}

	/**
	 * ����src_billtype��Setter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @param newSrc_billtype
	 *            java.lang.String
	 */
	public void setSrc_billtype(java.lang.String src_billtype) {
		this.src_billtype = src_billtype;
	}

	/**
	 * ���� src_billid��Getter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_billid() {
		return this.src_billid;
	}

	/**
	 * ����src_billid��Setter����.��������Դͷ�������� ��������:2019-12-9
	 * 
	 * @param newSrc_billid
	 *            java.lang.String
	 */
	public void setSrc_billid(java.lang.String src_billid) {
		this.src_billid = src_billid;
	}

	/**
	 * ���� src_itemid��Getter����.��������Դͷ���������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSrc_itemid() {
		return this.src_itemid;
	}

	/**
	 * ����src_itemid��Setter����.��������Դͷ���������� ��������:2019-12-9
	 * 
	 * @param newSrc_itemid
	 *            java.lang.String
	 */
	public void setSrc_itemid(java.lang.String src_itemid) {
		this.src_itemid = src_itemid;
	}

	/**
	 * ���� taxtype��Getter����.����������˰��� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getTaxtype() {
		return this.taxtype;
	}

	/**
	 * ����taxtype��Setter����.����������˰��� ��������:2019-12-9
	 * 
	 * @param newTaxtype
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setTaxtype(java.lang.Integer taxtype) {
		this.taxtype = taxtype;
	}

	/**
	 * ���� pk_payterm��Getter����.������������Э�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.payment.PaymentVO
	 */
	public java.lang.String getPk_payterm() {
		return this.pk_payterm;
	}

	/**
	 * ����pk_payterm��Setter����.������������Э�� ��������:2019-12-9
	 * 
	 * @param newPk_payterm
	 *            nc.vo.bd.payment.PaymentVO
	 */
	public void setPk_payterm(java.lang.String pk_payterm) {
		this.pk_payterm = pk_payterm;
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
	 * ���� recaccount��Getter����.���������տ������˻� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public java.lang.String getRecaccount() {
		return this.recaccount;
	}

	/**
	 * ����recaccount��Setter����.���������տ������˻� ��������:2019-12-9
	 * 
	 * @param newRecaccount
	 *            nc.vo.bd.bankaccount.BankAccSubVO
	 */
	public void setRecaccount(java.lang.String recaccount) {
		this.recaccount = recaccount;
	}

	/**
	 * ���� ordercubasdoc��Getter����.��������������Ӧ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.supplier.SupplierVO
	 */
	public java.lang.String getOrdercubasdoc() {
		return this.ordercubasdoc;
	}

	/**
	 * ����ordercubasdoc��Setter����.��������������Ӧ�� ��������:2019-12-9
	 * 
	 * @param newOrdercubasdoc
	 *            nc.vo.bd.supplier.SupplierVO
	 */
	public void setOrdercubasdoc(java.lang.String ordercubasdoc) {
		this.ordercubasdoc = ordercubasdoc;
	}

	/**
	 * ���� innerorderno��Getter����.������������������ ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInnerorderno() {
		return this.innerorderno;
	}

	/**
	 * ����innerorderno��Setter����.������������������ ��������:2019-12-9
	 * 
	 * @param newInnerorderno
	 *            java.lang.String
	 */
	public void setInnerorderno(java.lang.String innerorderno) {
		this.innerorderno = innerorderno;
	}

	/**
	 * ���� assetpactno��Getter����.���������ʲ���ͬ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getAssetpactno() {
		return this.assetpactno;
	}

	/**
	 * ����assetpactno��Setter����.���������ʲ���ͬ�� ��������:2019-12-9
	 * 
	 * @param newAssetpactno
	 *            java.lang.String
	 */
	public void setAssetpactno(java.lang.String assetpactno) {
		this.assetpactno = assetpactno;
	}

	/**
	 * ���� contractno��Getter����.����������ͬ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getContractno() {
		return this.contractno;
	}

	/**
	 * ����contractno��Setter����.����������ͬ�� ��������:2019-12-9
	 * 
	 * @param newContractno
	 *            java.lang.String
	 */
	public void setContractno(java.lang.String contractno) {
		this.contractno = contractno;
	}

	/**
	 * ���� freecust��Getter����.��������ɢ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.freecustom.FreeCustomVO
	 */
	public java.lang.String getFreecust() {
		return this.freecust;
	}

	/**
	 * ����freecust��Setter����.��������ɢ�� ��������:2019-12-9
	 * 
	 * @param newFreecust
	 *            nc.vo.bd.freecustom.FreeCustomVO
	 */
	public void setFreecust(java.lang.String freecust) {
		this.freecust = freecust;
	}

	/**
	 * ���� facard��Getter����.���������̶��ʲ���Ƭ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.fa.asset.AssetVO
	 */
	public java.lang.String getFacard() {
		return this.facard;
	}

	/**
	 * ����facard��Setter����.���������̶��ʲ���Ƭ�� ��������:2019-12-9
	 * 
	 * @param newFacard
	 *            nc.vo.fa.asset.AssetVO
	 */
	public void setFacard(java.lang.String facard) {
		this.facard = facard;
	}

	/**
	 * ���� purchaseorder��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getPurchaseorder() {
		return this.purchaseorder;
	}

	/**
	 * ����purchaseorder��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newPurchaseorder
	 *            java.lang.String
	 */
	public void setPurchaseorder(java.lang.String purchaseorder) {
		this.purchaseorder = purchaseorder;
	}

	/**
	 * ���� invoiceno��Getter����.����������Ʊ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getInvoiceno() {
		return this.invoiceno;
	}

	/**
	 * ����invoiceno��Setter����.����������Ʊ�� ��������:2019-12-9
	 * 
	 * @param newInvoiceno
	 *            java.lang.String
	 */
	public void setInvoiceno(java.lang.String invoiceno) {
		this.invoiceno = invoiceno;
	}

	/**
	 * ���� outstoreno��Getter����.�����������ⵥ�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getOutstoreno() {
		return this.outstoreno;
	}

	/**
	 * ����outstoreno��Setter����.�����������ⵥ�� ��������:2019-12-9
	 * 
	 * @param newOutstoreno
	 *            java.lang.String
	 */
	public void setOutstoreno(java.lang.String outstoreno) {
		this.outstoreno = outstoreno;
	}

	/**
	 * ���� checkelement��Getter����.�����������κ���Ҫ�� ��������:2019-12-9
	 * 
	 * @return nc.vo.resa.factor.FactorAsoaVO
	 */
	public java.lang.String getCheckelement() {
		return this.checkelement;
	}

	/**
	 * ����checkelement��Setter����.�����������κ���Ҫ�� ��������:2019-12-9
	 * 
	 * @param newCheckelement
	 *            nc.vo.resa.factor.FactorAsoaVO
	 */
	public void setCheckelement(java.lang.String checkelement) {
		this.checkelement = checkelement;
	}

	/**
	 * ���� grouprate��Getter����.�����������ű��һ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGrouprate() {
		return this.grouprate;
	}

	/**
	 * ����grouprate��Setter����.�����������ű��һ��� ��������:2019-12-9
	 * 
	 * @param newGrouprate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGrouprate(nc.vo.pub.lang.UFDouble grouprate) {
		this.grouprate = grouprate;
	}

	/**
	 * ���� globalrate��Getter����.��������ȫ�ֱ��һ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalrate() {
		return this.globalrate;
	}

	/**
	 * ����globalrate��Setter����.��������ȫ�ֱ��һ��� ��������:2019-12-9
	 * 
	 * @param newGlobalrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalrate(nc.vo.pub.lang.UFDouble globalrate) {
		this.globalrate = globalrate;
	}

	/**
	 * ���� groupcrebit��Getter����.�����������ű��ҽ��(����) ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupcrebit() {
		return this.groupcrebit;
	}

	/**
	 * ����groupcrebit��Setter����.�����������ű��ҽ��(����) ��������:2019-12-9
	 * 
	 * @param newGroupcrebit
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupcrebit(nc.vo.pub.lang.UFDouble groupcrebit) {
		this.groupcrebit = groupcrebit;
	}

	/**
	 * ���� globalcrebit��Getter����.��������ȫ�ֱ��ҽ��(����) ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalcrebit() {
		return this.globalcrebit;
	}

	/**
	 * ����globalcrebit��Setter����.��������ȫ�ֱ��ҽ��(����) ��������:2019-12-9
	 * 
	 * @param newGlobalcrebit
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalcrebit(nc.vo.pub.lang.UFDouble globalcrebit) {
		this.globalcrebit = globalcrebit;
	}

	/**
	 * ���� groupbalance��Getter����.�����������ű������ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupbalance() {
		return this.groupbalance;
	}

	/**
	 * ����groupbalance��Setter����.�����������ű������ ��������:2019-12-9
	 * 
	 * @param newGroupbalance
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupbalance(nc.vo.pub.lang.UFDouble groupbalance) {
		this.groupbalance = groupbalance;
	}

	/**
	 * ���� globalbalance��Getter����.��������ȫ�ֱ������ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalbalance() {
		return this.globalbalance;
	}

	/**
	 * ����globalbalance��Setter����.��������ȫ�ֱ������ ��������:2019-12-9
	 * 
	 * @param newGlobalbalance
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalbalance(nc.vo.pub.lang.UFDouble globalbalance) {
		this.globalbalance = globalbalance;
	}

	/**
	 * ���� groupnotax_cre��Getter����.�����������ű�����˰���(����) ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGroupnotax_cre() {
		return this.groupnotax_cre;
	}

	/**
	 * ����groupnotax_cre��Setter����.�����������ű�����˰���(����) ��������:2019-12-9
	 * 
	 * @param newGroupnotax_cre
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGroupnotax_cre(nc.vo.pub.lang.UFDouble groupnotax_cre) {
		this.groupnotax_cre = groupnotax_cre;
	}

	/**
	 * ���� globalnotax_cre��Getter����.��������ȫ�ֱ�����˰���(����) ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getGlobalnotax_cre() {
		return this.globalnotax_cre;
	}

	/**
	 * ����globalnotax_cre��Setter����.��������ȫ�ֱ�����˰���(����) ��������:2019-12-9
	 * 
	 * @param newGlobalnotax_cre
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setGlobalnotax_cre(nc.vo.pub.lang.UFDouble globalnotax_cre) {
		this.globalnotax_cre = globalnotax_cre;
	}

	/**
	 * ���� occupationmny��Getter����.��������Ԥռ��ԭ����� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getOccupationmny() {
		return this.occupationmny;
	}

	/**
	 * ����occupationmny��Setter����.��������Ԥռ��ԭ����� ��������:2019-12-9
	 * 
	 * @param newOccupationmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setOccupationmny(nc.vo.pub.lang.UFDouble occupationmny) {
		this.occupationmny = occupationmny;
	}

	/**
	 * ���� project��Getter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @return nc.vo.pmpub.project.ProjectHeadVO
	 */
	public java.lang.String getProject() {
		return this.project;
	}

	/**
	 * ����project��Setter����.����������Ŀ ��������:2019-12-9
	 * 
	 * @param newProject
	 *            nc.vo.pmpub.project.ProjectHeadVO
	 */
	public void setProject(java.lang.String project) {
		this.project = project;
	}

	/**
	 * ���� project_task��Getter����.����������Ŀ���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pmpub.wbs.WbsVO
	 */
	public java.lang.String getProject_task() {
		return this.project_task;
	}

	/**
	 * ����project_task��Setter����.����������Ŀ���� ��������:2019-12-9
	 * 
	 * @param newProject_task
	 *            nc.vo.pmpub.wbs.WbsVO
	 */
	public void setProject_task(java.lang.String project_task) {
		this.project_task = project_task;
	}

	/**
	 * ���� settleno��Getter����.�������������嵥�� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getSettleno() {
		return this.settleno;
	}

	/**
	 * ����settleno��Setter����.�������������嵥�� ��������:2019-12-9
	 * 
	 * @param newSettleno
	 *            java.lang.String
	 */
	public void setSettleno(java.lang.String settleno) {
		this.settleno = settleno;
	}

	/**
	 * ���� local_price��Getter����.�����������ҵ��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_price() {
		return this.local_price;
	}

	/**
	 * ����local_price��Setter����.�����������ҵ��� ��������:2019-12-9
	 * 
	 * @param newLocal_price
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_price(nc.vo.pub.lang.UFDouble local_price) {
		this.local_price = local_price;
	}

	/**
	 * ���� local_taxprice��Getter����.�����������Һ�˰���� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getLocal_taxprice() {
		return this.local_taxprice;
	}

	/**
	 * ����local_taxprice��Setter����.�����������Һ�˰���� ��������:2019-12-9
	 * 
	 * @param newLocal_taxprice
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setLocal_taxprice(nc.vo.pub.lang.UFDouble local_taxprice) {
		this.local_taxprice = local_taxprice;
	}

	/**
	 * ���� costcenter��Getter����.���������ɱ����� ��������:2019-12-9
	 * 
	 * @return nc.vo.resa.costcenter.CostCenterVO
	 */
	public java.lang.String getCostcenter() {
		return this.costcenter;
	}

	/**
	 * ����costcenter��Setter����.���������ɱ����� ��������:2019-12-9
	 * 
	 * @param newCostcenter
	 *            nc.vo.resa.costcenter.CostCenterVO
	 */
	public void setCostcenter(java.lang.String costcenter) {
		this.costcenter = costcenter;
	}

	/**
	 * ���� confernum��Getter����.���������ڲ����׽���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getConfernum() {
		return this.confernum;
	}

	/**
	 * ����confernum��Setter����.���������ڲ����׽���� ��������:2019-12-9
	 * 
	 * @param newConfernum
	 *            java.lang.String
	 */
	public void setConfernum(java.lang.String confernum) {
		this.confernum = confernum;
	}

	/**
	 * ���� sendcountryid��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.countryzone.CountryZoneVO
	 */
	public java.lang.String getSendcountryid() {
		return this.sendcountryid;
	}

	/**
	 * ����sendcountryid��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newSendcountryid
	 *            nc.vo.bd.countryzone.CountryZoneVO
	 */
	public void setSendcountryid(java.lang.String sendcountryid) {
		this.sendcountryid = sendcountryid;
	}

	/**
	 * ���� buysellflag��Getter����.���������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.pub.BillEnumCollection
	 */
	public java.lang.Integer getBuysellflag() {
		return this.buysellflag;
	}

	/**
	 * ����buysellflag��Setter����.���������������� ��������:2019-12-9
	 * 
	 * @param newBuysellflag
	 *            nc.vo.arap.pub.BillEnumCollection
	 */
	public void setBuysellflag(java.lang.Integer buysellflag) {
		this.buysellflag = buysellflag;
	}

	/**
	 * ���� taxcodeid��Getter����.��������˰�� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.taxcode.TaxcodeVO
	 */
	public java.lang.String getTaxcodeid() {
		return this.taxcodeid;
	}

	/**
	 * ����taxcodeid��Setter����.��������˰�� ��������:2019-12-9
	 * 
	 * @param newTaxcodeid
	 *            nc.vo.bd.taxcode.TaxcodeVO
	 */
	public void setTaxcodeid(java.lang.String taxcodeid) {
		this.taxcodeid = taxcodeid;
	}

	/**
	 * ���� nosubtaxrate��Getter����.�����������ɵֿ�˰�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNosubtaxrate() {
		return this.nosubtaxrate;
	}

	/**
	 * ����nosubtaxrate��Setter����.�����������ɵֿ�˰�� ��������:2019-12-9
	 * 
	 * @param newNosubtaxrate
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNosubtaxrate(nc.vo.pub.lang.UFDouble nosubtaxrate) {
		this.nosubtaxrate = nosubtaxrate;
	}

	/**
	 * ���� nosubtax��Getter����.�����������ɵֿ�˰�� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getNosubtax() {
		return this.nosubtax;
	}

	/**
	 * ����nosubtax��Setter����.�����������ɵֿ�˰�� ��������:2019-12-9
	 * 
	 * @param newNosubtax
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setNosubtax(nc.vo.pub.lang.UFDouble nosubtax) {
		this.nosubtax = nosubtax;
	}

	/**
	 * ���� caltaxmny��Getter����.����������˰��� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getCaltaxmny() {
		return this.caltaxmny;
	}

	/**
	 * ����caltaxmny��Setter����.����������˰��� ��������:2019-12-9
	 * 
	 * @param newCaltaxmny
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setCaltaxmny(nc.vo.pub.lang.UFDouble caltaxmny) {
		this.caltaxmny = caltaxmny;
	}

	/**
	 * ���� opptaxflag��Getter����.���������Ƿ�������˰ ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFBoolean
	 */
	public UFBoolean getOpptaxflag() {
		return this.opptaxflag;
	}

	/**
	 * ����opptaxflag��Setter����.���������Ƿ�������˰ ��������:2019-12-9
	 * 
	 * @param newOpptaxflag
	 *            nc.vo.pub.lang.UFBoolean
	 */
	public void setOpptaxflag(UFBoolean opptaxflag) {
		this.opptaxflag = opptaxflag;
	}

	/**
	 * ���� vendorvatcode��Getter����.����������Ӧ��VATע���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVendorvatcode() {
		return this.vendorvatcode;
	}

	/**
	 * ����vendorvatcode��Setter����.����������Ӧ��VATע���� ��������:2019-12-9
	 * 
	 * @param newVendorvatcode
	 *            java.lang.String
	 */
	public void setVendorvatcode(java.lang.String vendorvatcode) {
		this.vendorvatcode = vendorvatcode;
	}

	/**
	 * ���� vatcode��Getter����.��������VATע���� ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getVatcode() {
		return this.vatcode;
	}

	/**
	 * ����vatcode��Setter����.��������VATע���� ��������:2019-12-9
	 * 
	 * @param newVatcode
	 *            java.lang.String
	 */
	public void setVatcode(java.lang.String vatcode) {
		this.vatcode = vatcode;
	}

	/**
	 * ���� pk_payablebill��Getter����.����������Ӧ��Ӧ������ʶ ��������:2019-12-9
	 * 
	 * @return nc.vo.arap.payable.PayableBillVO
	 */
	public java.lang.String getPk_payablebill() {
		return this.pk_payablebill;
	}

	/**
	 * ����pk_payablebill��Setter����.����������Ӧ��Ӧ������ʶ ��������:2019-12-9
	 * 
	 * @param newPk_payablebill
	 *            nc.vo.arap.payable.PayableBillVO
	 */
	public void setPk_payablebill(java.lang.String pk_payablebill) {
		this.pk_payablebill = pk_payablebill;
	}

	/**
	 * ���� material_src��Getter����.��������ԭʼ���� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.material.MaterialVO
	 */
	public java.lang.String getMaterial_src() {
		return this.material_src;
	}

	/**
	 * ����material_src��Setter����.��������ԭʼ���� ��������:2019-12-9
	 * 
	 * @param newMaterial_src
	 *            nc.vo.bd.material.MaterialVO
	 */
	public void setMaterial_src(java.lang.String material_src) {
		this.material_src = material_src;
	}

	/**
	 * ���� settlemoney��Getter����.�������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.pub.lang.UFDouble
	 */
	public nc.vo.pub.lang.UFDouble getSettlemoney() {
		return this.settlemoney;
	}

	/**
	 * ����settlemoney��Setter����.�������������� ��������:2019-12-9
	 * 
	 * @param newSettlemoney
	 *            nc.vo.pub.lang.UFDouble
	 */
	public void setSettlemoney(nc.vo.pub.lang.UFDouble settlemoney) {
		this.settlemoney = settlemoney;
	}

	/**
	 * ���� settlecurr��Getter����.��������������� ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.currtype.CurrtypeVO
	 */
	public java.lang.String getSettlecurr() {
		return this.settlecurr;
	}

	/**
	 * ����settlecurr��Setter����.��������������� ��������:2019-12-9
	 * 
	 * @param newSettlecurr
	 *            nc.vo.bd.currtype.CurrtypeVO
	 */
	public void setSettlecurr(java.lang.String settlecurr) {
		this.settlecurr = settlecurr;
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
	 * ���� cbs��Getter����.��������CBS ��������:2019-12-9
	 * 
	 * @return nc.vo.bd.cbs.CBSNodeVO
	 */
	public java.lang.String getCbs() {
		return this.cbs;
	}

	/**
	 * ����cbs��Setter����.��������CBS ��������:2019-12-9
	 * 
	 * @param newCbs
	 *            nc.vo.bd.cbs.CBSNodeVO
	 */
	public void setCbs(java.lang.String cbs) {
		this.cbs = cbs;
	}

	/**
	 * ���� def1��Getter����.���������Զ�����1 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef1() {
		return this.def1;
	}

	/**
	 * ����def1��Setter����.���������Զ�����1 ��������:2019-12-9
	 * 
	 * @param newDef1
	 *            java.lang.String
	 */
	public void setDef1(java.lang.String def1) {
		this.def1 = def1;
	}

	/**
	 * ���� def2��Getter����.���������Զ�����2 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef2() {
		return this.def2;
	}

	/**
	 * ����def2��Setter����.���������Զ�����2 ��������:2019-12-9
	 * 
	 * @param newDef2
	 *            java.lang.String
	 */
	public void setDef2(java.lang.String def2) {
		this.def2 = def2;
	}

	/**
	 * ���� def3��Getter����.���������Զ�����3 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef3() {
		return this.def3;
	}

	/**
	 * ����def3��Setter����.���������Զ�����3 ��������:2019-12-9
	 * 
	 * @param newDef3
	 *            java.lang.String
	 */
	public void setDef3(java.lang.String def3) {
		this.def3 = def3;
	}

	/**
	 * ���� def4��Getter����.���������Զ�����4 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef4() {
		return this.def4;
	}

	/**
	 * ����def4��Setter����.���������Զ�����4 ��������:2019-12-9
	 * 
	 * @param newDef4
	 *            java.lang.String
	 */
	public void setDef4(java.lang.String def4) {
		this.def4 = def4;
	}

	/**
	 * ���� def5��Getter����.���������Զ�����5 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef5() {
		return this.def5;
	}

	/**
	 * ����def5��Setter����.���������Զ�����5 ��������:2019-12-9
	 * 
	 * @param newDef5
	 *            java.lang.String
	 */
	public void setDef5(java.lang.String def5) {
		this.def5 = def5;
	}

	/**
	 * ���� def6��Getter����.���������Զ�����6 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef6() {
		return this.def6;
	}

	/**
	 * ����def6��Setter����.���������Զ�����6 ��������:2019-12-9
	 * 
	 * @param newDef6
	 *            java.lang.String
	 */
	public void setDef6(java.lang.String def6) {
		this.def6 = def6;
	}

	/**
	 * ���� def7��Getter����.���������Զ�����7 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef7() {
		return this.def7;
	}

	/**
	 * ����def7��Setter����.���������Զ�����7 ��������:2019-12-9
	 * 
	 * @param newDef7
	 *            java.lang.String
	 */
	public void setDef7(java.lang.String def7) {
		this.def7 = def7;
	}

	/**
	 * ���� def8��Getter����.���������Զ�����8 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef8() {
		return this.def8;
	}

	/**
	 * ����def8��Setter����.���������Զ�����8 ��������:2019-12-9
	 * 
	 * @param newDef8
	 *            java.lang.String
	 */
	public void setDef8(java.lang.String def8) {
		this.def8 = def8;
	}

	/**
	 * ���� def9��Getter����.���������Զ�����9 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef9() {
		return this.def9;
	}

	/**
	 * ����def9��Setter����.���������Զ�����9 ��������:2019-12-9
	 * 
	 * @param newDef9
	 *            java.lang.String
	 */
	public void setDef9(java.lang.String def9) {
		this.def9 = def9;
	}

	/**
	 * ���� def10��Getter����.���������Զ�����10 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef10() {
		return this.def10;
	}

	/**
	 * ����def10��Setter����.���������Զ�����10 ��������:2019-12-9
	 * 
	 * @param newDef10
	 *            java.lang.String
	 */
	public void setDef10(java.lang.String def10) {
		this.def10 = def10;
	}

	/**
	 * ���� def11��Getter����.���������Զ�����11 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef11() {
		return this.def11;
	}

	/**
	 * ����def11��Setter����.���������Զ�����11 ��������:2019-12-9
	 * 
	 * @param newDef11
	 *            java.lang.String
	 */
	public void setDef11(java.lang.String def11) {
		this.def11 = def11;
	}

	/**
	 * ���� def12��Getter����.���������Զ�����12 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef12() {
		return this.def12;
	}

	/**
	 * ����def12��Setter����.���������Զ�����12 ��������:2019-12-9
	 * 
	 * @param newDef12
	 *            java.lang.String
	 */
	public void setDef12(java.lang.String def12) {
		this.def12 = def12;
	}

	/**
	 * ���� def13��Getter����.���������Զ�����13 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef13() {
		return this.def13;
	}

	/**
	 * ����def13��Setter����.���������Զ�����13 ��������:2019-12-9
	 * 
	 * @param newDef13
	 *            java.lang.String
	 */
	public void setDef13(java.lang.String def13) {
		this.def13 = def13;
	}

	/**
	 * ���� def14��Getter����.���������Զ�����14 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef14() {
		return this.def14;
	}

	/**
	 * ����def14��Setter����.���������Զ�����14 ��������:2019-12-9
	 * 
	 * @param newDef14
	 *            java.lang.String
	 */
	public void setDef14(java.lang.String def14) {
		this.def14 = def14;
	}

	/**
	 * ���� def15��Getter����.���������Զ�����15 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef15() {
		return this.def15;
	}

	/**
	 * ����def15��Setter����.���������Զ�����15 ��������:2019-12-9
	 * 
	 * @param newDef15
	 *            java.lang.String
	 */
	public void setDef15(java.lang.String def15) {
		this.def15 = def15;
	}

	/**
	 * ���� def16��Getter����.���������Զ�����16 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef16() {
		return this.def16;
	}

	/**
	 * ����def16��Setter����.���������Զ�����16 ��������:2019-12-9
	 * 
	 * @param newDef16
	 *            java.lang.String
	 */
	public void setDef16(java.lang.String def16) {
		this.def16 = def16;
	}

	/**
	 * ���� def17��Getter����.���������Զ�����17 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef17() {
		return this.def17;
	}

	/**
	 * ����def17��Setter����.���������Զ�����17 ��������:2019-12-9
	 * 
	 * @param newDef17
	 *            java.lang.String
	 */
	public void setDef17(java.lang.String def17) {
		this.def17 = def17;
	}

	/**
	 * ���� def18��Getter����.���������Զ�����18 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef18() {
		return this.def18;
	}

	/**
	 * ����def18��Setter����.���������Զ�����18 ��������:2019-12-9
	 * 
	 * @param newDef18
	 *            java.lang.String
	 */
	public void setDef18(java.lang.String def18) {
		this.def18 = def18;
	}

	/**
	 * ���� def19��Getter����.���������Զ�����19 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef19() {
		return this.def19;
	}

	/**
	 * ����def19��Setter����.���������Զ�����19 ��������:2019-12-9
	 * 
	 * @param newDef19
	 *            java.lang.String
	 */
	public void setDef19(java.lang.String def19) {
		this.def19 = def19;
	}

	/**
	 * ���� def20��Getter����.���������Զ�����20 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef20() {
		return this.def20;
	}

	/**
	 * ����def20��Setter����.���������Զ�����20 ��������:2019-12-9
	 * 
	 * @param newDef20
	 *            java.lang.String
	 */
	public void setDef20(java.lang.String def20) {
		this.def20 = def20;
	}

	/**
	 * ���� def21��Getter����.���������Զ�����21 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef21() {
		return this.def21;
	}

	/**
	 * ����def21��Setter����.���������Զ�����21 ��������:2019-12-9
	 * 
	 * @param newDef21
	 *            java.lang.String
	 */
	public void setDef21(java.lang.String def21) {
		this.def21 = def21;
	}

	/**
	 * ���� def22��Getter����.���������Զ�����22 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef22() {
		return this.def22;
	}

	/**
	 * ����def22��Setter����.���������Զ�����22 ��������:2019-12-9
	 * 
	 * @param newDef22
	 *            java.lang.String
	 */
	public void setDef22(java.lang.String def22) {
		this.def22 = def22;
	}

	/**
	 * ���� def23��Getter����.���������Զ�����23 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef23() {
		return this.def23;
	}

	/**
	 * ����def23��Setter����.���������Զ�����23 ��������:2019-12-9
	 * 
	 * @param newDef23
	 *            java.lang.String
	 */
	public void setDef23(java.lang.String def23) {
		this.def23 = def23;
	}

	/**
	 * ���� def24��Getter����.���������Զ�����24 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef24() {
		return this.def24;
	}

	/**
	 * ����def24��Setter����.���������Զ�����24 ��������:2019-12-9
	 * 
	 * @param newDef24
	 *            java.lang.String
	 */
	public void setDef24(java.lang.String def24) {
		this.def24 = def24;
	}

	/**
	 * ���� def25��Getter����.���������Զ�����25 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef25() {
		return this.def25;
	}

	/**
	 * ����def25��Setter����.���������Զ�����25 ��������:2019-12-9
	 * 
	 * @param newDef25
	 *            java.lang.String
	 */
	public void setDef25(java.lang.String def25) {
		this.def25 = def25;
	}

	/**
	 * ���� def26��Getter����.���������Զ�����26 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef26() {
		return this.def26;
	}

	/**
	 * ����def26��Setter����.���������Զ�����26 ��������:2019-12-9
	 * 
	 * @param newDef26
	 *            java.lang.String
	 */
	public void setDef26(java.lang.String def26) {
		this.def26 = def26;
	}

	/**
	 * ���� def27��Getter����.���������Զ�����27 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef27() {
		return this.def27;
	}

	/**
	 * ����def27��Setter����.���������Զ�����27 ��������:2019-12-9
	 * 
	 * @param newDef27
	 *            java.lang.String
	 */
	public void setDef27(java.lang.String def27) {
		this.def27 = def27;
	}

	/**
	 * ���� def28��Getter����.���������Զ�����28 ��������:2019-12-9
	 * 
	 * @return java.lang.String
	 */
	public java.lang.String getDef28() {
		return this.def28;
	}

	/**
	 * ����def28��Setter����.���������Զ�����28 ��������:2019-12-9
	 * 
	 * @param newDef28
	 *            java.lang.String
	 */
	public void setDef28(java.lang.String def28) {
		this.def28 = def28;
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
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2019-12-9
	 * 
	 * @return String
	 */
	public String getPk_payreq() {
		return this.pk_payreq;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2019-12-9
	 * 
	 * @param newPk_payreq
	 *            String
	 */
	public void setPk_payreq(String pk_payreq) {
		this.pk_payreq = pk_payreq;
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
		return VOMetaFactory.getInstance().getVOMeta("tg.payablepage");
	}
}
