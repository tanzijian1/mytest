package nc.vo.tgfn.marginworkorder;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMarginHVOMeta extends AbstractBillMeta{
	
	public AggMarginHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.marginworkorder.MarginHVO.class);
		this.addChildren(nc.vo.tgfn.marginworkorder.MarginBVO.class);
	}
}