package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.internalinterest.ace.rule.FN06_DelAfterCuRule;
import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IInternalInterestMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.internalinterest.AggInternalinterest;

public class N_FN06_DELETE extends AbstractPfAction<AggInternalinterest> {

	@Override
	protected CompareAroundProcesser<AggInternalinterest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInternalinterest> processor = new CompareAroundProcesser<AggInternalinterest>(
				InternalInterestPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		IRule<AggInternalinterest> FN06_DelAfterCuRule = new FN06_DelAfterCuRule();
		processor.addAfterRule(FN06_DelAfterCuRule);
		return processor;
	}

	@Override
	protected AggInternalinterest[] processBP(Object userObj,
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills) {
		IInternalInterestMaintain operator = NCLocator.getInstance().lookup(
				IInternalInterestMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
