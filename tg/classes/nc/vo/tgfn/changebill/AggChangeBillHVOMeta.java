package nc.vo.tgfn.changebill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggChangeBillHVOMeta extends AbstractBillMeta{
	
	public AggChangeBillHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.changebill.ChangeBillHVO.class);
		this.addChildren(nc.vo.tgfn.changebill.ChangeBillBVO.class);
	}
}