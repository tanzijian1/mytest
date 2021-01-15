package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;

public class N_FN17_SAVE extends AbstractPfAction<AggTargetactivation> {

	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = new CompareAroundProcesser<AggTargetactivation>(
				TargetActivationPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggTargetactivation> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {
		ITargetActivationMaintain operator = NCLocator.getInstance().lookup(
				ITargetActivationMaintain.class);
		AggTargetactivation[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
