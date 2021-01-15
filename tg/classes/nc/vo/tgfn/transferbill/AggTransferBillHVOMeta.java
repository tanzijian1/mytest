package nc.vo.tgfn.transferbill;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggTransferBillHVOMeta extends AbstractBillMeta{
	
	public AggTransferBillHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.transferbill.TransferBillHVO.class);
		this.addChildren(nc.vo.tgfn.transferbill.TransferBillBVO.class);
	}
}