package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.moonfinancingplan.plugin.bpplugin.MoonFinancingPlanPluginPoint;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.itf.tg.IMoonFinancingPlanMaintain;

public class N_RZ07_SAVE extends AbstractPfAction<AggMoonFinancingPlan> {

	protected CompareAroundProcesser<AggMoonFinancingPlan> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMoonFinancingPlan> processor = new CompareAroundProcesser<AggMoonFinancingPlan>(
				MoonFinancingPlanPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggMoonFinancingPlan> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggMoonFinancingPlan[] processBP(Object userObj,
			AggMoonFinancingPlan[] clientFullVOs, AggMoonFinancingPlan[] originBills) {
		IMoonFinancingPlanMaintain operator = NCLocator.getInstance().lookup(
				IMoonFinancingPlanMaintain.class);
		AggMoonFinancingPlan[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
