package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.temporaryestimate.plugin.bpplugin.TemporaryEstimatePluginPoint;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.itf.tg.ITemporaryEstimateMaintain;

public class N_FN03_SAVE extends AbstractPfAction<AggTemest> {

	protected CompareAroundProcesser<AggTemest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTemest> processor = new CompareAroundProcesser<AggTemest>(
				TemporaryEstimatePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggTemest> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggTemest[] processBP(Object userObj,
			AggTemest[] clientFullVOs, AggTemest[] originBills) {
		ITemporaryEstimateMaintain operator = NCLocator.getInstance().lookup(
				ITemporaryEstimateMaintain.class);
		AggTemest[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
