package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;

public class N_FN10_SAVE extends AbstractPfAction<AggTaxCalculationHead> {

	protected CompareAroundProcesser<AggTaxCalculationHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTaxCalculationHead> processor = new CompareAroundProcesser<AggTaxCalculationHead>(
				TaxCalculationPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggTaxCalculationHead> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggTaxCalculationHead[] processBP(Object userObj,
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills) {
		ITaxCalculationMaintain operator = NCLocator.getInstance().lookup(
				ITaxCalculationMaintain.class);
		AggTaxCalculationHead[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
