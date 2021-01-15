package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.temporaryestimate.plugin.bpplugin.TemporaryEstimatePluginPoint;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.itf.tg.ITemporaryEstimateMaintain;

public class N_FN03_SAVEBASE extends AbstractPfAction<AggTemest> {

	@Override
	protected CompareAroundProcesser<AggTemest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTemest> processor = null;
		AggTemest[] clientFullVOs = (AggTemest[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggTemest>(
					TemporaryEstimatePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggTemest>(
					TemporaryEstimatePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggTemest> rule = null;

		return processor;
	}

	@Override
	protected AggTemest[] processBP(Object userObj,
			AggTemest[] clientFullVOs, AggTemest[] originBills) {

		AggTemest[] bills = null;
		try {
			ITemporaryEstimateMaintain operator = NCLocator.getInstance()
					.lookup(ITemporaryEstimateMaintain.class);
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
