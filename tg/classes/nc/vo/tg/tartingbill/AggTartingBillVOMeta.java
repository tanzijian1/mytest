package nc.vo.tg.tartingbill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggTartingBillVOMeta extends AbstractBillMeta{
	
	public AggTartingBillVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.tartingbill.TartingBillVO.class);
		this.addChildren(nc.vo.tg.tartingbill.TartingBillBVO.class);
	}
}