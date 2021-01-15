package nc.vo.baseapp.itfformulacfg;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

/**
 * @Description:
 * @version with NC V6.5
 */
public class AggFormulaCfgVOMeta extends AbstractBillMeta {

	public AggFormulaCfgVOMeta() {
		this.init();
	}

	private void init() {
		this.setParent(nc.vo.baseapp.itfformulacfg.FormulaCfgVO.class);
		this.addChildren(nc.vo.baseapp.itfformulacfg.FormulaCfgBVO.class);
	}
}