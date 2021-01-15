package nc.vo.tg.sealflow;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.sealflow.SealFlowVO")

public class AggSealFlowVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSealFlowVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SealFlowVO getParentVO(){
	  	return (SealFlowVO)this.getParent();
	  }
	  
}