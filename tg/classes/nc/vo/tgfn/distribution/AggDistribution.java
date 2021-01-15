package nc.vo.tgfn.distribution;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.distribution.Distribution")

public class AggDistribution extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggDistributionMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public Distribution getParentVO(){
	  	return (Distribution)this.getParent();
	  }
	  
}