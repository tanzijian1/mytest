package nc.vo.tg.tg_groupdata;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.tg_groupdata.GroupDataVO")

public class AggGroupDataVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggGroupDataVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public GroupDataVO getParentVO(){
	  	return (GroupDataVO)this.getParent();
	  }
	  
}