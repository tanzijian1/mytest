package nc.vo.tg.standard;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.standard.StandardVO")

public class AggStandardVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggStandardVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public StandardVO getParentVO(){
	  	return (StandardVO)this.getParent();
	  }
	  
}