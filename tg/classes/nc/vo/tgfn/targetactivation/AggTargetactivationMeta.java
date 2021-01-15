package nc.vo.tgfn.targetactivation;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggTargetactivationMeta extends AbstractBillMeta{
	
	public AggTargetactivationMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tgfn.targetactivation.Targetactivation.class);
		this.addChildren(nc.vo.tgfn.targetactivation.Targetbus.class);
	}
}