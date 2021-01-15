package nc.vo.tgfn.marginsheet;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMarginHVOMeta extends AbstractBillMeta{
	
	public AggMarginHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.marginsheet.MarginHVO.class);
		this.addChildren(nc.vo.tgfn.marginsheet.MarginBVO.class);
	}
}