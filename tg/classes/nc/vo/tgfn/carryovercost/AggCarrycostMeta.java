package nc.vo.tgfn.carryovercost;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggCarrycostMeta extends AbstractBillMeta{
	
	public AggCarrycostMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.carryovercost.Carrycost.class);
		this.addChildren(nc.vo.tgfn.carryovercost.Carrycostbus.class);
	}
}