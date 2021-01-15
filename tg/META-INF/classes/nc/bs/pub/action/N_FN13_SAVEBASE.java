package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;

public class N_FN13_SAVEBASE extends AbstractPfAction<AggDistribution> {

	@Override
	protected CompareAroundProcesser<AggDistribution> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDistribution> processor = null;
		AggDistribution[] clientFullVOs = (AggDistribution[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggDistribution>(
					DistributionPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggDistribution>(
					DistributionPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggDistribution> rule = null;

		return processor;
	}

	@Override
	protected AggDistribution[] processBP(Object userObj,
			AggDistribution[] clientFullVOs, AggDistribution[] originBills) {

		AggDistribution[] bills = null;
		try {
			IDistributionMaintain operator = NCLocator.getInstance()
					.lookup(IDistributionMaintain.class);
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
