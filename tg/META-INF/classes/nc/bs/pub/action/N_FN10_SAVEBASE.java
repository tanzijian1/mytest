package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;

public class N_FN10_SAVEBASE extends AbstractPfAction<AggTaxCalculationHead> {

	@Override
	protected CompareAroundProcesser<AggTaxCalculationHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTaxCalculationHead> processor = null;
		AggTaxCalculationHead[] clientFullVOs = (AggTaxCalculationHead[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggTaxCalculationHead>(
					TaxCalculationPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggTaxCalculationHead>(
					TaxCalculationPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggTaxCalculationHead> rule = null;

		return processor;
	}

	@Override
	protected AggTaxCalculationHead[] processBP(Object userObj,
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills) {

		AggTaxCalculationHead[] bills = null;
		try {
			ITaxCalculationMaintain operator = NCLocator.getInstance()
					.lookup(ITaxCalculationMaintain.class);
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
