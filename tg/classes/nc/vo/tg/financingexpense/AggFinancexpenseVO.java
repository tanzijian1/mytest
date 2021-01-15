package nc.vo.tg.financingexpense;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.financingexpense.FinancexpenseVO")

public class AggFinancexpenseVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFinancexpenseVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FinancexpenseVO getParentVO(){
	  	return (FinancexpenseVO)this.getParent();
	  }
	  
}