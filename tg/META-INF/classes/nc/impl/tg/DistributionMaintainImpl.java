package nc.impl.tg;

import nc.impl.pub.ace.AceDistributionPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;
import nc.vo.pub.BusinessException;

public class DistributionMaintainImpl extends AceDistributionPubServiceImpl
		implements IDistributionMaintain {

	@Override
	public void delete(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] insert(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] update(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggDistribution[] save(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] unsave(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] approve(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggDistribution[] unapprove(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
