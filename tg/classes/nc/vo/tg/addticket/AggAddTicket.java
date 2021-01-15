package nc.vo.tg.addticket;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.addticket.AddTicket")

public class AggAddTicket extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggAddTicketMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public AddTicket getParentVO(){
	  	return (AddTicket)this.getParent();
	  }
	  
}