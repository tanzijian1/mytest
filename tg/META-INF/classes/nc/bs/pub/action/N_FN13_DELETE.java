package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;

public class N_FN13_DELETE extends AbstractPfAction<AggDistribution> {

	@Override
	protected CompareAroundProcesser<AggDistribution> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDistribution> processor = new CompareAroundProcesser<AggDistribution>(
				DistributionPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggDistribution[] processBP(Object userObj,
			AggDistribution[] clientFullVOs, AggDistribution[] originBills) {
		IDistributionMaintain operator = NCLocator.getInstance().lookup(
				IDistributionMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
