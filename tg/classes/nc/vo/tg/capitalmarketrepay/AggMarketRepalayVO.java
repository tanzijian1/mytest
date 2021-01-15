package nc.vo.tg.capitalmarketrepay;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.capitalmarketrepay.MarketRepalayVO")

public class AggMarketRepalayVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMarketRepalayVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MarketRepalayVO getParentVO(){
	  	return (MarketRepalayVO)this.getParent();
	  }
	  
}