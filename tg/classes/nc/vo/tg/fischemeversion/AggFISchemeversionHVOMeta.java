package nc.vo.tg.fischemeversion;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFISchemeversionHVOMeta extends AbstractBillMeta{
	
	public AggFISchemeversionHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.fischemeversion.FISchemeversionHVO.class);
		this.addChildren(nc.vo.tg.fischemeversion.FISchemeversionBVO.class);
		this.addChildren(nc.vo.tg.fischemeversion.NFISchemeversionBVO.class);
		this.addChildren(nc.vo.tg.fischemeversion.CapmarketversionBVO.class);
	}
}