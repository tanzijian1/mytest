package nc.vo.baseapp.itfformulacfg;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

/**
 * @Description:外部协同公式AggVO
 * @version with NC V6.5
 */
@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.baseapp.itfformulacfg.FormulaCfgVO")
public class AggFormulaCfgVO extends AbstractBill {

	private static final long serialVersionUID = -6462215793648222915L;

	@Override
	public IBillMeta getMetaData() {
		IBillMeta billMeta = BillMetaFactory.getInstance().getBillMeta(AggFormulaCfgVOMeta.class);
		return billMeta;
	}

	@Override
	public FormulaCfgVO getParentVO() {
		return (FormulaCfgVO) this.getParent();
	}

}