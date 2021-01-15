package nc.vo.tg.capitalmarketrepay;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMarketRepalayVOMeta extends AbstractBillMeta{
	
	public AggMarketRepalayVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.capitalmarketrepay.MarketRepalayVO.class);
		this.addChildren(nc.vo.tg.capitalmarketrepay.MarketRepaleyBVO.class);
		this.addChildren(nc.vo.tg.capitalmarketrepay.GroupCreditBVO.class);
		this.addChildren(nc.vo.tg.capitalmarketrepay.MarketReplayCVO.class);
	}
}