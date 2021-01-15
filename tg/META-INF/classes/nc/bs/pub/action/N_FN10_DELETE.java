package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;

public class N_FN10_DELETE extends AbstractPfAction<AggTaxCalculationHead> {

	@Override
	protected CompareAroundProcesser<AggTaxCalculationHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTaxCalculationHead> processor = new CompareAroundProcesser<AggTaxCalculationHead>(
				TaxCalculationPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggTaxCalculationHead[] processBP(Object userObj,
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills) {
		ITaxCalculationMaintain operator = NCLocator.getInstance().lookup(
				ITaxCalculationMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
