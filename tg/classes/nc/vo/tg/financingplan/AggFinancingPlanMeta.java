package nc.vo.tg.financingplan;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFinancingPlanMeta extends AbstractBillMeta{
	
	public AggFinancingPlanMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.financingplan.FinancingPlan.class);
	}
}