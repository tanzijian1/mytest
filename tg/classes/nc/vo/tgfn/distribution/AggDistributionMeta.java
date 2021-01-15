package nc.vo.tgfn.distribution;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggDistributionMeta extends AbstractBillMeta{
	
	public AggDistributionMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.distribution.Distribution.class);
		this.addChildren(nc.vo.tgfn.distribution.Carrybus.class);
	}
}