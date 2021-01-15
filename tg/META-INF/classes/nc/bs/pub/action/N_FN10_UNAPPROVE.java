package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.taxcalculation.plugin.bpplugin.TaxCalculationPluginPoint;
import nc.bs.tg.taxcalculation.rule.RedRule;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.vo.tgfn.taxcalculation.AggTaxCalculationHead;
import nc.itf.tg.ITaxCalculationMaintain;

public class N_FN10_UNAPPROVE extends AbstractPfAction<AggTaxCalculationHead> {

	@Override
	protected CompareAroundProcesser<AggTaxCalculationHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTaxCalculationHead> processor = new CompareAroundProcesser<AggTaxCalculationHead>(
				TaxCalculationPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
        processor.addBeforeRule(new RedRule());
		return processor;
	}

	@Override
	protected AggTaxCalculationHead[] processBP(Object userObj,
			AggTaxCalculationHead[] clientFullVOs, AggTaxCalculationHead[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggTaxCalculationHead[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ITaxCalculationMaintain operator = NCLocator.getInstance()
					.lookup(ITaxCalculationMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggTaxCalculationHead bill : bills){
				util.delVoucher(bill);
			}
			//====end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
