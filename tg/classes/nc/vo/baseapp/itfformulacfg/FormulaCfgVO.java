package nc.vo.baseapp.itfformulacfg;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * @Description:�ⲿЭͬ��ʽ��ͷ
 * @version with NC V6.5
 */
public class FormulaCfgVO extends SuperVO {

	private static final long serialVersionUID = -4820951759933257085L;
	/**
	 * ʱ���
	 */
	public static final String TS = "ts";;

	/**
	 * ���� ����ʱ�����Getter����.��������ʱ��� ��������:2018-12-27
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FormulaCfgVO.TS);
	}

	/**
	 * ��������ʱ�����Setter����.��������ʱ��� ��������:2018-12-27
	 * 
	 * @param newts
	 *            nc.vo.pub.lang.UFDateTime
	 */
	public void setTs(UFDateTime ts) {
		this.setAttributeValue(FormulaCfgVO.TS, ts);
	}

	@Override
	public IVOMeta getMetaData() {
		return VOMetaFactory.getInstance().getVOMeta("uap.itf_formulacfg_h");
	}
}
