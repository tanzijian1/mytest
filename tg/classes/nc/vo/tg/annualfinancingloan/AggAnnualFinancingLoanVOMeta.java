package nc.vo.tg.annualfinancingloan;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggAnnualFinancingLoanVOMeta extends AbstractBillMeta{
	
	public AggAnnualFinancingLoanVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.annualfinancingloan.AnnualFinancingLoanVO.class);
	}
}