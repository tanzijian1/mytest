package nc.vo.tg.rzreportbi;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.rzreportbi.RZreportBIVO")

public class AggRZreportBIVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggRZreportBIVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public RZreportBIVO getParentVO(){
	  	return (RZreportBIVO)this.getParent();
	  }
	  
}