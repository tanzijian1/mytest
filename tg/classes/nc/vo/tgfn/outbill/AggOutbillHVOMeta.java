package nc.vo.tgfn.outbill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggOutbillHVOMeta extends AbstractBillMeta{
	
	public AggOutbillHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.outbill.OutbillHVO.class);
		this.addChildren(nc.vo.tgfn.outbill.OutbillBVO.class);
	}
}