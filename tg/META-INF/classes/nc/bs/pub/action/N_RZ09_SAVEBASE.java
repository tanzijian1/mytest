package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.financingtotal.plugin.bpplugin.FinancingTotalPluginPoint;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.itf.tg.IFinancingTotalMaintain;

public class N_RZ09_SAVEBASE extends AbstractPfAction<AggFinancingTotal> {

	@Override
	protected CompareAroundProcesser<AggFinancingTotal> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancingTotal> processor = null;
		AggFinancingTotal[] clientFullVOs = (AggFinancingTotal[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggFinancingTotal>(
					FinancingTotalPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggFinancingTotal>(
					FinancingTotalPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggFinancingTotal> rule = null;

		return processor;
	}

	@Override
	protected AggFinancingTotal[] processBP(Object userObj,
			AggFinancingTotal[] clientFullVOs, AggFinancingTotal[] originBills) {

		AggFinancingTotal[] bills = null;
		try {
			IFinancingTotalMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingTotalMaintain.class);
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
