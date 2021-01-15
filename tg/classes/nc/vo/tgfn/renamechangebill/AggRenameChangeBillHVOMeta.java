package nc.vo.tgfn.renamechangebill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggRenameChangeBillHVOMeta extends AbstractBillMeta{
	
	public AggRenameChangeBillHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.renamechangebill.RenameChangeBillHVO.class);
		this.addChildren(nc.vo.tgfn.renamechangebill.RenameChangeBillBVO.class);
	}
}