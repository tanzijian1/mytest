package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.itf.tg.IFinancingTotalMaintain;

public class N_RZ09_APPROVE extends AbstractPfAction<AggFinancingTotal> {

	public N_RZ09_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggFinancingTotal> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingTotal> processor = new CompareAroundProcesser<AggFinancingTotal>(
				FinancingTotalPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggFinancingTotal[] processBP(Object userObj,
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills) {
		AggFinancingTotal[] bills = null;
		IFinancingTotalMaintain operator = NCLocator.getInstance().lookup(
				IFinancingTotalMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
