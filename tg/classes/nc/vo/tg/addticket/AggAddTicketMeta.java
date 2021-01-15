package nc.vo.tg.addticket;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggAddTicketMeta extends AbstractBillMeta{
	
	public AggAddTicketMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.addticket.AddTicket.class);
		this.addChildren(nc.vo.tg.addticket.Ticketbody.class);
	}
}