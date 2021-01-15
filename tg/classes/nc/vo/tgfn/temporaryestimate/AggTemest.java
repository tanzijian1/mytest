package nc.vo.tgfn.temporaryestimate;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.temporaryestimate.Temest")

public class AggTemest extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggTemestMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Temest getParentVO(){
	  	return (Temest)this.getParent();
	  }
	  
}