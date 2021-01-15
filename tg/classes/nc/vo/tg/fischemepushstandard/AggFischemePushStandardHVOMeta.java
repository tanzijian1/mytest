package nc.vo.tg.fischemepushstandard;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggFischemePushStandardHVOMeta extends AbstractBillMeta{
	
	public AggFischemePushStandardHVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.fischemepushstandard.FischemePushStandardHVO.class);
		this.addChildren(nc.vo.tg.fischemepushstandard.FischemePushStandardBVO.class);
		this.addChildren(nc.vo.tg.fischemepushstandard.FischemePushStandardNVO.class);
		this.addChildren(nc.vo.tg.fischemepushstandard.FischemePushStandardCVO.class);
	}
}