package nc.vo.tg.standard;

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
 * ��������:2019-8-22
 * 
 * @author YONYOU NC
 * @version NCPrj ??
 */

public class StandardVO extends SuperVO {
	public static final String PK_STANDARD = "pk_standard";// ����
	public static final String PK_GROUP = "pk_group";// ����
	public static final String PK_ORG = "pk_org";// ��֯
	public static final String PK_ORG_V = "pk_org_v";// ��֯�汾
	public static final String CODE = "code";// ����
	public static final String NAME = "name";// ����
	public static final String PERIODYEAR = "periodyear";// ʱ��ά��
	public static final String PK_FINTYPE = "pk_fintype";// ��������
	public static final String VSTANDARD = "vstandard";// ���ʱ�׼
	public static final String ENABLESTATE = "enablestate";// ����״̬
	public static final String CREATOR = "creator";// ������
	public static final String CREATIONTIME = "creationtime";// ����ʱ��
	public static final String MODIFIER = "modifier";// �޸���
	public static final String MODIFIEDTIME = "modifiedtime";// �޸�ʱ��
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
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2019-8-22
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return this.ts;
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2019-8-22
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.ts = ts;
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("tg.StandardVO");
	}
}
