package nc.vo.tgfn.invoicing;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggInvoicingHeadMeta extends AbstractBillMeta{
	
	public AggInvoicingHeadMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.invoicing.InvoicingHead.class);
		this.addChildren(nc.vo.tgfn.invoicing.InvoicingBody.class);
	}
}