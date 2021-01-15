package nc.vo.tgfn.marginworkorder;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.marginworkorder.MarginHVO")

public class AggMarginHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMarginHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MarginHVO getParentVO(){
	  	return (MarginHVO)this.getParent();
	  }
	  
}