package nc.vo.baseapp.itfformulacfg;

import nc.vo.pub.IVOMeta;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;
import nc.vo.pubapp.pattern.model.meta.entity.vo.VOMetaFactory;

/**
 * @Description:外部协同公式表头
 * @version with NC V6.5
 */
public class FormulaCfgVO extends SuperVO {

	private static final long serialVersionUID = -4820951759933257085L;
	/**
	 * 时间戳
	 */
	public static final String TS = "ts";;

	/**
	 * 属性 生成时间戳的Getter方法.属性名：时间戳 创建日期:2018-12-27
	 * 
	 * @return nc.vo.pub.lang.UFDateTime
	 */
	public UFDateTime getTs() {
		return (UFDateTime) this.getAttributeValue(FormulaCfgVO.TS);
	}

	/**
	 * 属性生成时间戳的Setter方法.属性名：时间戳 创建日期:2018-12-27
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
