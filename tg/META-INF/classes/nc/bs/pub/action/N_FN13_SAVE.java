package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;

public class N_FN13_SAVE extends AbstractPfAction<AggDistribution> {

	protected CompareAroundProcesser<AggDistribution> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDistribution> processor = new CompareAroundProcesser<AggDistribution>(
				DistributionPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggDistribution> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggDistribution[] processBP(Object userObj,
			AggDistribution[] clientFullVOs, AggDistribution[] originBills) {
		IDistributionMaintain operator = NCLocator.getInstance().lookup(
				IDistributionMaintain.class);
		AggDistribution[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
