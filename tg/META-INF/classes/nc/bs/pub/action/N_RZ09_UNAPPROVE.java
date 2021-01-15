package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.itf.tg.IFinancingTotalMaintain;

public class N_RZ09_UNAPPROVE extends AbstractPfAction<AggFinancingTotal> {

	@Override
	protected CompareAroundProcesser<AggFinancingTotal> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingTotal> processor = new CompareAroundProcesser<AggFinancingTotal>(
				FinancingTotalPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFinancingTotal[] processBP(Object userObj,
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggFinancingTotal[] bills = null;
		try {
			IFinancingTotalMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingTotalMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
