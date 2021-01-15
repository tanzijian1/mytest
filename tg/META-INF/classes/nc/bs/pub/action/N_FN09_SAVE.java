package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.carryovercost.plugin.bpplugin.CarryoverCostPluginPoint;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.itf.tg.ICarryoverCostMaintain;

public class N_FN09_SAVE extends AbstractPfAction<AggCarrycost> {

	protected CompareAroundProcesser<AggCarrycost> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarrycost> processor = new CompareAroundProcesser<AggCarrycost>(
				CarryoverCostPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggCarrycost> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggCarrycost[] processBP(Object userObj,
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills) {
		ICarryoverCostMaintain operator = NCLocator.getInstance().lookup(
				ICarryoverCostMaintain.class);
		AggCarrycost[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
