package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;

public class N_FN17_UNSAVEBILL extends AbstractPfAction<AggTargetactivation> {

	@Override
	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = new CompareAroundProcesser<AggTargetactivation>(
				TargetActivationPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {
		ITargetActivationMaintain operator = NCLocator.getInstance().lookup(
				ITargetActivationMaintain.class);
		AggTargetactivation[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
