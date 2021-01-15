package nc.vo.tg.fischeme;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFIScemeHVOMeta extends AbstractBillMeta{
	
	public AggFIScemeHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.fischeme.FIScemeHVO.class);
		this.addChildren(nc.vo.tg.fischeme.FISchemeBVO.class);
		this.addChildren(nc.vo.tg.fischeme.NFISchemeBVO.class);
		this.addChildren(nc.vo.tg.fischeme.CapmarketBVO.class);
	}
}