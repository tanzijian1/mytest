package nc.vo.tg.salaryfundaccure;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSalaryfundaccureMeta extends AbstractBillMeta{
	
	public AggSalaryfundaccureMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.salaryfundaccure.SalaryFundAccureVO.class);
		this.addChildren(nc.vo.tg.salaryfundaccure.SalaryFundAccureItem.class);
	}
}