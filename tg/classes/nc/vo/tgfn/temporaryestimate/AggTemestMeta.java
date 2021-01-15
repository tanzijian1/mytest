package nc.vo.tgfn.temporaryestimate;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggTemestMeta extends AbstractBillMeta{
	
	public AggTemestMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.temporaryestimate.Temest.class);
		this.addChildren(nc.vo.tgfn.temporaryestimate.Business.class);
	}
}