package nc.vo.tgfn.targetactivation;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.targetactivation.Targetactivation")

public class AggTargetactivation extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggTargetactivationMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Targetactivation getParentVO(){
	  	return (Targetactivation)this.getParent();
	  }
	  
}