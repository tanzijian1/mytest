package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingplan.plugin.bpplugin.FinancingPlanPluginPoint;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.itf.tg.IFinancingPlanMaintain;

public class N_RZ08_SAVEBASE extends AbstractPfAction<AggFinancingPlan> {

	@Override
	protected CompareAroundProcesser<AggFinancingPlan> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingPlan> processor = null;
		AggFinancingPlan[] clientFullVOs = (AggFinancingPlan[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggFinancingPlan>(
					FinancingPlanPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggFinancingPlan>(
					FinancingPlanPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggFinancingPlan> rule = null;

		return processor;
	}

	@Override
	protected AggFinancingPlan[] processBP(Object userObj,
			AggFinancingPlan[] clientFullVOs, AggFinancingPlan[] originBills) {

		AggFinancingPlan[] bills = null;
		try {
			IFinancingPlanMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingPlanMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
