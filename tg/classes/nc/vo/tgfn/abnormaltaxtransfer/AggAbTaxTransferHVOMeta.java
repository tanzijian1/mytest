package nc.vo.tgfn.abnormaltaxtransfer;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggAbTaxTransferHVOMeta extends AbstractBillMeta{
	
	public AggAbTaxTransferHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.abnormaltaxtransfer.AbTaxTransferHVO.class);
		this.addChildren(nc.vo.tgfn.abnormaltaxtransfer.AbTaxTransferBVO.class);
	}
}