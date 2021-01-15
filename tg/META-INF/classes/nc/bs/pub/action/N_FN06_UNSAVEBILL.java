package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.itf.tg.IInternalInterestMaintain;

public class N_FN06_UNSAVEBILL extends AbstractPfAction<AggInternalinterest> {

	@Override
	protected CompareAroundProcesser<AggInternalinterest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInternalinterest> processor = new CompareAroundProcesser<AggInternalinterest>(
				InternalInterestPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInternalinterest[] processBP(Object userObj,
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills) {
		IInternalInterestMaintain operator = NCLocator.getInstance().lookup(
				IInternalInterestMaintain.class);
		AggInternalinterest[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
