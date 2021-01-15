package nc.impl.tg;

import nc.impl.pub.ace.AceFinancingPlanPubServiceImpl;
import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.itf.tg.IFinancingPlanMaintain;
import nc.vo.pub.BusinessException;

public class FinancingPlanMaintainImpl extends AceFinancingPlanPubServiceImpl
		implements IFinancingPlanMaintain {

	@Override
	public void delete(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		super.pubdeleteBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] insert(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubinsertBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] update(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubupdateBills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] query(IQueryScheme queryScheme)
			throws BusinessException {
		return super.pubquerybills(queryScheme);
	}

	@Override
	public AggFinancingPlan[] save(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] unsave(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubunsendapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] approve(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubapprovebills(clientFullVOs, originBills);
	}

	@Override
	public AggFinancingPlan[] unapprove(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException {
		return super.pubunapprovebills(clientFullVOs, originBills);
	}

}
