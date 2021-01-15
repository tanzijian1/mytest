package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.moonfinancingplan.plugin.bpplugin.MoonFinancingPlanPluginPoint;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.itf.tg.IMoonFinancingPlanMaintain;

public class N_RZ07_APPROVE extends AbstractPfAction<AggMoonFinancingPlan> {

	public N_RZ07_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggMoonFinancingPlan> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMoonFinancingPlan> processor = new CompareAroundProcesser<AggMoonFinancingPlan>(
				MoonFinancingPlanPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggMoonFinancingPlan[] processBP(Object userObj,
			AggMoonFinancingPlan[] clientFullVOs, AggMoonFinancingPlan[] originBills) {
		AggMoonFinancingPlan[] bills = null;
		IMoonFinancingPlanMaintain operator = NCLocator.getInstance().lookup(
				IMoonFinancingPlanMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
