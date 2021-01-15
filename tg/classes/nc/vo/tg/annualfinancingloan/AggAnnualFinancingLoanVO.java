package nc.vo.tg.annualfinancingloan;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.annualfinancingloan.AnnualFinancingLoanVO")

public class AggAnnualFinancingLoanVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggAnnualFinancingLoanVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public AnnualFinancingLoanVO getParentVO(){
	  	return (AnnualFinancingLoanVO)this.getParent();
	  }
	  
}