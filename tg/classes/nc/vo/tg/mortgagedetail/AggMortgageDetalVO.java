package nc.vo.tg.mortgagedetail;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.mortgagedetail.MortgageDetalVO")

public class AggMortgageDetalVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMortgageDetalVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MortgageDetalVO getParentVO(){
	  	return (MortgageDetalVO)this.getParent();
	  }
	  
}