package nc.vo.tgfn.paymentrequest;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggPayrequestMeta extends AbstractBillMeta{
	
	public AggPayrequestMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.paymentrequest.Payrequest.class);
		this.addChildren(nc.vo.tgfn.paymentrequest.Business_b.class);
		this.addChildren(nc.vo.tg.paymentrequest.Payablepage.class);
	}
}