package nc.vo.tg.tgrz_mortgageagreement;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO")

public class AggMortgageAgreementVO extends AbstractBill {
	
	  @Override
	  public IBillMeta getMetaData() {
	  	IBillMeta billMeta =BillMetaFactory.getInstance().getBillMeta(AggMortgageAgreementVOMeta.class);
	  	return billMeta;
	  }
	    
	  @Override
	  public MortgageAgreementVO getParentVO(){
	  	return (MortgageAgreementVO)this.getParent();
	  }
	  
}