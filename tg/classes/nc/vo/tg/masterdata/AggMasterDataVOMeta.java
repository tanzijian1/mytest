package nc.vo.tg.masterdata;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggMasterDataVOMeta extends AbstractBillMeta{
	
	public AggMasterDataVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.masterdata.MasterDataVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataZKVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataZJVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataXFVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataSJVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataSKVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataBJVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataBKVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataGPVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataGKVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataDJVO.class);
		this.addChildren(nc.vo.tg.masterdata.MasterDataDKVO.class);
	}
}