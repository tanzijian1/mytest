package nc.vo.tg.mortgagedetail;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMortgageDetalVOMeta extends AbstractBillMeta{
	
	public AggMortgageDetalVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.mortgagedetail.MortgageDetalVO.class);
	}
}