package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.carryovercost.plugin.bpplugin.CarryoverCostPluginPoint;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.itf.tg.ICarryoverCostMaintain;

public class N_FN09_DELETE extends AbstractPfAction<AggCarrycost> {

	@Override
	protected CompareAroundProcesser<AggCarrycost> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarrycost> processor = new CompareAroundProcesser<AggCarrycost>(
				CarryoverCostPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggCarrycost[] processBP(Object userObj,
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills) {
		ICarryoverCostMaintain operator = NCLocator.getInstance().lookup(
				ICarryoverCostMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
