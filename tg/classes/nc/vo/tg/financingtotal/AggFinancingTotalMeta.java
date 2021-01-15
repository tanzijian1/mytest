package nc.vo.tg.financingtotal;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFinancingTotalMeta extends AbstractBillMeta{
	
	public AggFinancingTotalMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.financingtotal.FinancingTotal.class);
	}
}