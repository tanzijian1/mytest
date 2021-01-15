package nc.vo.tg.moonfinancingplan;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMoonFinancingPlanMeta extends AbstractBillMeta{
	
	public AggMoonFinancingPlanMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.moonfinancingplan.MoonFinancingPlan.class);
	}
}