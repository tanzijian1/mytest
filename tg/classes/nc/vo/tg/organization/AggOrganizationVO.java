package nc.vo.tg.organization;

import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;
import nc.vo.pubapp.pattern.model.meta.entity.bill.BillMetaFactory;
import nc.vo.pubapp.pattern.model.meta.entity.bill.IBillMeta;

@nc.vo.annotation.AggVoInfo(parentVO = "nc.vo.tg.organization.OrganizationVO")
public class AggOrganizationVO extends AbstractBill {

	@Override
	public IBillMeta getMetaData() {
		IBillMeta billMeta = BillMetaFactory.getInstance().getBillMeta(
				AggOrganizationVOMeta.class);
		return billMeta;
	}

	@Override
	public OrganizationVO getParentVO() {
		return (OrganizationVO) this.getParent();
	}

}