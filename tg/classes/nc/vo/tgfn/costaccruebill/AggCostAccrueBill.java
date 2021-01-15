package nc.vo.tgfn.costaccruebill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.costaccruebill.CostAccrueBill")

public class AggCostAccrueBill extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggCostAccrueBillMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public CostAccrueBill getParentVO(){
	  	return (CostAccrueBill)this.getParent();
	  }
	  
}