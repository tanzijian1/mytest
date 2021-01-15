package nc.vo.tg.fischemepushstandard;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.fischemepushstandard.FischemePushStandardHVO")

public class AggFischemePushStandardHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggFischemePushStandardHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public FischemePushStandardHVO getParentVO(){
	  	return (FischemePushStandardHVO)this.getParent();
	  }
	  
}