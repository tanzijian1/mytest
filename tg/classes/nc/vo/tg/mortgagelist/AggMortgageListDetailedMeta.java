package nc.vo.tg.mortgagelist;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMortgageListDetailedMeta extends AbstractBillMeta {

	public AggMortgageListDetailedMeta() {
		this.init();
	}

	private void init() {
		this.setParent(MortgageListDetailedVO.class);
	}
}