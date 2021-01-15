package nc.vo.tg.projectdata;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggProjectDataVOMeta extends AbstractBillMeta{
	
	public AggProjectDataVOMeta(){
		this.init();
	}
	
	private void init() {
		this.setParent(nc.vo.tg.projectdata.ProjectDataVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataBVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataVVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataCVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataNVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataMVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataPVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataFVO.class);
		this.addChildren(nc.vo.tg.projectdata.ProjectDataFBVO.class);
		this.addChildren(nc.vo.tg.projectdata.ResidentialSalesInformationVO.class);
		this.addChildren(nc.vo.tg.projectdata.CommercialSalesInformationVO.class);
		this.addChildren(nc.vo.tg.projectdata.OfficeSalesVO.class);
		this.addChildren(nc.vo.tg.projectdata.ConstructionSupportingSalesInformationVO.class);
		this.addChildren(nc.vo.tg.projectdata.ParkingSalesInformationVO.class);
	}
}