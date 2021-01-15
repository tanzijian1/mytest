package nc.vo.tgfn.invoicing;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.invoicing.InvoicingHead")

public class AggInvoicingHead extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggInvoicingHeadMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public InvoicingHead getParentVO(){
	  	return (InvoicingHead)this.getParent();
	  }
	  
}