package nc.vo.tg.financingexpense;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFinancexpenseVOMeta extends AbstractBillMeta{
	
	public AggFinancexpenseVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.financingexpense.FinancexpenseVO.class);
	}
}