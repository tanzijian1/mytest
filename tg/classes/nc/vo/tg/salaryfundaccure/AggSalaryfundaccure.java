package nc.vo.tg.salaryfundaccure;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.salaryfundaccure.SalaryFundAccureVO")

public class AggSalaryfundaccure extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggSalaryfundaccureMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public SalaryFundAccureVO getParentVO(){
	  	return (SalaryFundAccureVO)this.getParent();
	  }
	  
}