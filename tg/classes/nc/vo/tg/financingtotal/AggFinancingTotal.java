package nc.vo.tg.financingtotal;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.financingtotal.FinancingTotal")

public class AggFinancingTotal extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFinancingTotalMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FinancingTotal getParentVO(){
	  	return (FinancingTotal)this.getParent();
	  }
	  
}