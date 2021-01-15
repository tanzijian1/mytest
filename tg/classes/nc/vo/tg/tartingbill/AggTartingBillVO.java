package nc.vo.tg.tartingbill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.tartingbill.TartingBillVO")

public class AggTartingBillVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggTartingBillVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public TartingBillVO getParentVO(){
	  	return (TartingBillVO)this.getParent();
	  }
	  
}