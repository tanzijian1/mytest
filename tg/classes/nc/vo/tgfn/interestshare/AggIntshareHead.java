package nc.vo.tgfn.interestshare;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.interestshare.IntshareHead")

public class AggIntshareHead extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggIntshareHeadMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public IntshareHead getParentVO(){
	  	return (IntshareHead)this.getParent();
	  }
	  
}