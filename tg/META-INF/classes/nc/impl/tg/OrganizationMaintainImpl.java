package nc.impl.tg;

import nc.impl.pub.ace.AceOrganizationPubServiceImpl;
import nc.itf.tg.IOrganizationMaintain;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.organization.AggOrganizationVO;

public class OrganizationMaintainImpl extends AceOrganizationPubServiceImpl
		implements IOrganizationMaintain {

	@Override
	public void delete(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] insert(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] update(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggOrganizationVO[] save(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] unsave(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] approve(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggOrganizationVO[] unapprove(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
