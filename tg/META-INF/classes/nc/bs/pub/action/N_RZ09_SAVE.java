package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.itf.tg.IFinancingTotalMaintain;

public class N_RZ09_SAVE extends AbstractPfAction<AggFinancingTotal> {

	protected CompareAroundProcesser<AggFinancingTotal> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingTotal> processor = new CompareAroundProcesser<AggFinancingTotal>(
				FinancingTotalPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggFinancingTotal> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggFinancingTotal[] processBP(Object userObj,
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills) {
		IFinancingTotalMaintain operator = NCLocator.getInstance().lookup(
				IFinancingTotalMaintain.class);
		AggFinancingTotal[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
