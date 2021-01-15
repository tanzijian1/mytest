package nc.vo.tg.invoicebill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggInvoiceBillVOMeta extends AbstractBillMeta{
	
	public AggInvoiceBillVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.invoicebill.InvoiceBillVO.class);
		this.addChildren(nc.vo.tg.invoicebill.InvoiceBillBVO.class);
	}
}