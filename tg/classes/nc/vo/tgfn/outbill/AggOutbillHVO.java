package nc.vo.tgfn.outbill;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.outbill.OutbillHVO")

public class AggOutbillHVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggOutbillHVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public OutbillHVO getParentVO(){
	  	return (OutbillHVO)this.getParent();
	  }
	  
}