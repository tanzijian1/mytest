package nc.vo.tg.checkfinance;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.checkfinance.CheckFinanceHVO")

public class AggCheckFinanceHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggCheckFinanceHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public CheckFinanceHVO getParentVO(){
	  	return (CheckFinanceHVO)this.getParent();
	  }
	  
}