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

public class StandardBVO extends SuperVO {
	public static final String PK_STANDARD_B = "pk_standard_b";// ����
	public static final String CROWNO = "crowno";// �к�
	public static final String DAYS = "days";// ��Ȼ��
	public static final String RATIO1 = "ratio1";// ռ��
	public static final String RATIO2 = "ratio2";// �����ܶ�ռ��
	public static final String NOTE = "note";// ��ע
	public static final String PK_STANDARD = "pk_standard";// ���ʱ�׼_����

	/**
	 * �ϲ㵥������
	 */
	public String pk_standard;
	/**
	 * ʱ���
	 */
	public UFDateTime ts;

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2019-8-22
	 * 
	 * @return String
	 */
	public String getPk_standard() {
		return this.pk_standard;
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2019-8-22
	 * 
	 * @param newPk_standard
	 *            String
	 */
	public void setPk_standard(String pk_standard) {
		this.pk_standard = pk_standard;
	}

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
		return VOMetaFactory.getInstance().getVOMeta("tg.StandardBVO");
	}
}
