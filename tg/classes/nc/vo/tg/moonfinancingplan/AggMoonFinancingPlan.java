package nc.vo.tg.moonfinancingplan;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.moonfinancingplan.MoonFinancingPlan")

public class AggMoonFinancingPlan extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMoonFinancingPlanMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MoonFinancingPlan getParentVO(){
	  	return (MoonFinancingPlan)this.getParent();
	  }
	  
}