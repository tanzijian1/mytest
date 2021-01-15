package nc.vo.tg.checkfinance;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggCheckFinanceHVOMeta extends AbstractBillMeta{
	
	public AggCheckFinanceHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.checkfinance.CheckFinanceHVO.class);
		this.addChildren(nc.vo.tg.checkfinance.CheckFinanceBVO.class);
	}
}