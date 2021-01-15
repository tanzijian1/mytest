package nc.vo.tgfn.changebill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.changebill.ChangeBillHVO")

public class AggChangeBillHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggChangeBillHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public ChangeBillHVO getParentVO(){
	  	return (ChangeBillHVO)this.getParent();
	  }
	  
}