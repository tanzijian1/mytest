package nc.impl.tg;

import nc.impl.pub.ace.AceMoonFinancingPlanPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.itf.tg.IMoonFinancingPlanMaintain;
import nc.vo.pub.BusinessException;

public class MoonFinancingPlanMaintainImpl extends AceMoonFinancingPlanPubServiceImpl
		implements IMoonFinancingPlanMaintain {

	@Override
	public void delete(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] insert(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] update(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggMoonFinancingPlan[] save(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] unsave(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] approve(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggMoonFinancingPlan[] unapprove(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
