package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;

public class N_FN10_APPROVE extends AbstractPfAction<AggTaxCalculationHead> {

	public N_FN10_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggTaxCalculationHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTaxCalculationHead> processor = new CompareAroundProcesser<AggTaxCalculationHead>(
				TaxCalculationPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggTaxCalculationHead[] processBP(Object userObj,
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills) {
		AggTaxCalculationHead[] bills = null;
		ITaxCalculationMaintain operator = NCLocator.getInstance().lookup(
				ITaxCalculationMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggTaxCalculationHead bill : bills){
				util.addVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
