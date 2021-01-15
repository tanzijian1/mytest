package nc.vo.tg.mortgagelist;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.mortgagelist.MortgageListDetailedVO")
public class AggMortgageListDetailedVO extends AbstractBill {

	@Override
	public IBillMeta getMetaData() {
		IBillMeta billMeta = BillMetaFactory.getInstance().getBillMeta(
				AggMortgageListDetailedMeta.class);
		return billMeta;
	}

	@Override
	public MortgageListDetailedVO getParentVO() {
		return (MortgageListDetailedVO) this.getParent();
	}

}