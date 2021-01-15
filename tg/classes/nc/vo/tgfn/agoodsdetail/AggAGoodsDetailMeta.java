package nc.vo.tgfn.agoodsdetail;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggAGoodsDetailMeta extends AbstractBillMeta{
	
	public AggAGoodsDetailMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.agoodsdetail.AGoodsDetail.class);
	}
}