package nc.vo.tgfn.internalinterest;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.internalinterest.Internalinterest")

public class AggInternalinterest extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggInternalinterestMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Internalinterest getParentVO(){
	  	return (Internalinterest)this.getParent();
	  }
	  
}