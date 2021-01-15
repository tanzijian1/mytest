package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingplan.plugin.bpplugin.FinancingPlanPluginPoint;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.itf.tg.IFinancingPlanMaintain;

public class N_RZ08_UNAPPROVE extends AbstractPfAction<AggFinancingPlan> {

	@Override
	protected CompareAroundProcesser<AggFinancingPlan> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingPlan> processor = new CompareAroundProcesser<AggFinancingPlan>(
				FinancingPlanPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFinancingPlan[] processBP(Object userObj,
			AggFinancingPlan[] clientFullVOs, AggFinancingPlan[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggFinancingPlan[] bills = null;
		try {
			IFinancingPlanMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingPlanMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
