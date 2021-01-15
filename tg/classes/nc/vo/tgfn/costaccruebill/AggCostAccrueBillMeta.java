package nc.vo.tgfn.costaccruebill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggCostAccrueBillMeta extends AbstractBillMeta{
	
	public AggCostAccrueBillMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.costaccruebill.CostAccrueBill.class);
		this.addChildren(nc.vo.tgfn.costaccruebill.CostAccrueBilltab.class);
	}
}