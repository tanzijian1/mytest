package nc.vo.tg.tg_groupdata;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggGroupDataVOMeta extends AbstractBillMeta{
	
	public AggGroupDataVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.tg_groupdata.GroupDataVO.class);
	}
}