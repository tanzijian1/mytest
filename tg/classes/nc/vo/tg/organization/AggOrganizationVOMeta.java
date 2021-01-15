package nc.vo.tg.organization;

import nc.vo.pubapp.pattern.model.meta.entity.bill.AbstractBillMeta;

public class AggOrganizationVOMeta extends AbstractBillMeta {

	public AggOrganizationVOMeta() {
		this.init();
	}

	private void init() {
		this.setParent(nc.vo.tg.organization.OrganizationVO.class);
	}
}