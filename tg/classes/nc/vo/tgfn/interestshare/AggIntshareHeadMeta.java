package nc.vo.tgfn.interestshare;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggIntshareHeadMeta extends AbstractBillMeta{
	
	public AggIntshareHeadMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.interestshare.IntshareHead.class);
		this.addChildren(nc.vo.tgfn.interestshare.IntshareBody.class);
	}
}