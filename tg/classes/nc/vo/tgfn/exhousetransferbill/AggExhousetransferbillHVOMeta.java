package nc.vo.tgfn.exhousetransferbill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggExhousetransferbillHVOMeta extends AbstractBillMeta{
	
	public AggExhousetransferbillHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.exhousetransferbill.ExhousetransferbillHVO.class);
		this.addChildren(nc.vo.tgfn.exhousetransferbill.ExhousetransferbillBVO.class);
	}
}