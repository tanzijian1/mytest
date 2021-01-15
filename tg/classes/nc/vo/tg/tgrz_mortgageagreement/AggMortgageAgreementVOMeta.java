package nc.vo.tg.tgrz_mortgageagreement;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMortgageAgreementVOMeta extends AbstractBillMeta{
	
	public AggMortgageAgreementVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.tgrz_mortgageagreement.MortgageAgreementVO.class);
	}
}