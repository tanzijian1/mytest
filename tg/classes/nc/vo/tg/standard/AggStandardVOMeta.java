package nc.vo.tg.standard;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggStandardVOMeta extends AbstractBillMeta{
	
	public AggStandardVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.standard.StandardVO.class);
		this.addChildren(nc.vo.tg.standard.StandardBVO.class);
	}
}