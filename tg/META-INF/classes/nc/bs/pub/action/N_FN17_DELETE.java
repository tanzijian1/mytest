package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;

public class N_FN17_DELETE extends AbstractPfAction<AggTargetactivation> {

	@Override
	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = new CompareAroundProcesser<AggTargetactivation>(
				TargetActivationPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {
		ITargetActivationMaintain operator = NCLocator.getInstance().lookup(
				ITargetActivationMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
