package nc.vo.tg.financingplan;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.financingplan.FinancingPlan")

public class AggFinancingPlan extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFinancingPlanMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FinancingPlan getParentVO(){
	  	return (FinancingPlan)this.getParent();
	  }
	  
}