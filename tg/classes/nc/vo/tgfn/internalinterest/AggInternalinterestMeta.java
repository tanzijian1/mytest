package nc.vo.tgfn.internalinterest;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggInternalinterestMeta extends AbstractBillMeta{
	
	public AggInternalinterestMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.internalinterest.Internalinterest.class);
		this.addChildren(nc.vo.tgfn.internalinterest.Interbus.class);
	}
}