package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingplan.plugin.bpplugin.FinancingPlanPluginPoint;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.itf.tg.IFinancingPlanMaintain;

public class N_RZ08_UNSAVEBILL extends AbstractPfAction<AggFinancingPlan> {

	@Override
	protected CompareAroundProcesser<AggFinancingPlan> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingPlan> processor = new CompareAroundProcesser<AggFinancingPlan>(
				FinancingPlanPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFinancingPlan[] processBP(Object userObj,
			AggFinancingPlan[] clientFullVOs, AggFinancingPlan[] originBills) {
		IFinancingPlanMaintain operator = NCLocator.getInstance().lookup(
				IFinancingPlanMaintain.class);
		AggFinancingPlan[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
