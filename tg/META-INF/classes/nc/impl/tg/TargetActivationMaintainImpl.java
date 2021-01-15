package nc.impl.tg;

import nc.impl.pub.ace.AceTargetActivationPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;
import nc.vo.pub.BusinessException;

public class TargetActivationMaintainImpl extends AceTargetActivationPubServiceImpl
		implements ITargetActivationMaintain {

	@Override
	public void delete(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] insert(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] update(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggTargetactivation[] save(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] unsave(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] approve(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggTargetactivation[] unapprove(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
