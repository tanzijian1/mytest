package nc.vo.tgfn.paymentrequest;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.paymentrequest.Payrequest")

public class AggPayrequest extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggPayrequestMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Payrequest getParentVO(){
	  	return (Payrequest)this.getParent();
	  }
	  
}