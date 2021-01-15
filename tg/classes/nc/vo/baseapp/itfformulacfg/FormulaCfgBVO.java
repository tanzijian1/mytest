package nc.vo.baseapp.itfformulacfg;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * @Description:�ⲿЭͬ��ʽ����
 * @version with NC V6.5
 */
public class FormulaCfgBVO extends SuperVO {

	private static final long serialVersionUID = -4909239802864300110L;
	/**
	 * �ϲ㵥������
	 */
	public static final String PK__FORMULACFG_H = "pk__formulacfg_h";
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";;

	/**
	 * ���� �����ϲ�������Getter����.���������ϲ����� ��������:2018-12-27
	 * 
	 * @return String
	 */
	public String getPk__formulacfg_h() {
		return (String) this.getAttributeValue(FormulaCfgBVO.PK__FORMULACFG_H);
	}

	/**
	 * ���������ϲ�������Setter����.���������ϲ����� ��������:2018-12-27
	 * 
	 * @param newPk__formulacfg_h
	 *            String
	 */
	public void setPk__formulacfg_h(String pk__formulacfg_h) {
		this.setAttributeValue(FormulaCfgBVO.PK__FORMULACFG_H, pk__formulacfg_h);
	}

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2018-12-27
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FormulaCfgBVO.TS);
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2018-12-27
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FormulaCfgBVO.TS, ts);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uap.itf_formulacfg_b");
	}
}
