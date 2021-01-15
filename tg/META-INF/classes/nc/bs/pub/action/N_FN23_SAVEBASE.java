package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.invoicing.plugin.bpplugin.InvoicingPluginPoint;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.itf.tg.IInvoicingMaintain;

public class N_FN23_SAVEBASE extends AbstractPfAction<AggInvoicingHead> {

	@Override
	protected CompareAroundProcesser<AggInvoicingHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoicingHead> processor = null;
		AggInvoicingHead[] clientFullVOs = (AggInvoicingHead[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggInvoicingHead>(
					InvoicingPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggInvoicingHead>(
					InvoicingPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggInvoicingHead> rule = null;

		return processor;
	}

	@Override
	protected AggInvoicingHead[] processBP(Object userObj,
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills) {

		AggInvoicingHead[] bills = null;
		try {
			IInvoicingMaintain operator = NCLocator.getInstance()
					.lookup(IInvoicingMaintain.class);
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
