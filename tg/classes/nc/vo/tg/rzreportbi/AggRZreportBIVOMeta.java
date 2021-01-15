package nc.vo.tg.rzreportbi;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggRZreportBIVOMeta extends AbstractBillMeta{
	
	public AggRZreportBIVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.rzreportbi.RZreportBIVO.class);
	}
}