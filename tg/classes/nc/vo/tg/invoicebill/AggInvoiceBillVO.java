package nc.vo.tg.invoicebill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.invoicebill.InvoiceBillVO")

public class AggInvoiceBillVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggInvoiceBillVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public InvoiceBillVO getParentVO(){
	  	return (InvoiceBillVO)this.getParent();
	  }
	  
}