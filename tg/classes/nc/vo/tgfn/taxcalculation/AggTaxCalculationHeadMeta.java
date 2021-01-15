package nc.vo.tgfn.taxcalculation;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggTaxCalculationHeadMeta extends AbstractBillMeta{
	
	public AggTaxCalculationHeadMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.taxcalculation.TaxCalculationHead.class);
		this.addChildren(nc.vo.tgfn.taxcalculation.TaxCalculationBody.class);
	}
}