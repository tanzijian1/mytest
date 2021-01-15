package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;

public class N_FN17_SAVEBASE extends AbstractPfAction<AggTargetactivation> {

	@Override
	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = null;
		AggTargetactivation[] clientFullVOs = (AggTargetactivation[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggTargetactivation>(
					TargetActivationPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggTargetactivation>(
					TargetActivationPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggTargetactivation> rule = null;

		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {

		AggTargetactivation[] bills = null;
		try {
			ITargetActivationMaintain operator = NCLocator.getInstance()
					.lookup(ITargetActivationMaintain.class);
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
