package nc.vo.tgfn.agoodsdetail;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tgfn.agoodsdetail.AGoodsDetail")

public class AggAGoodsDetail extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggAGoodsDetailMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public AGoodsDetail getParentVO(){
	  	return (AGoodsDetail)this.getParent();
	  }
	  
}