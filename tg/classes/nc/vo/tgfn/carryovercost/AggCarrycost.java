package nc.vo.tgfn.carryovercost;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.carryovercost.Carrycost")

public class AggCarrycost extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggCarrycostMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Carrycost getParentVO(){
	  	return (Carrycost)this.getParent();
	  }
	  
}