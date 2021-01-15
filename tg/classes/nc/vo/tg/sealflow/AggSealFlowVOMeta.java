package nc.vo.tg.sealflow;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggSealFlowVOMeta extends AbstractBillMeta{
	
	public AggSealFlowVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.sealflow.SealFlowVO.class);
		this.addChildren(nc.vo.tg.sealflow.SealFlowBodyVO.class);
	}
}