package nc.vo.tgfn.taxcalculation;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.taxcalculation.TaxCalculationHead")

public class AggTaxCalculationHead extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggTaxCalculationHeadMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public TaxCalculationHead getParentVO(){
	  	return (TaxCalculationHead)this.getParent();
	  }
	  
}