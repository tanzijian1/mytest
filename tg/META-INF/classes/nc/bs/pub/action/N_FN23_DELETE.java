package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.invoicing.plugin.bpplugin.InvoicingPluginPoint;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.itf.tg.IInvoicingMaintain;

public class N_FN23_DELETE extends AbstractPfAction<AggInvoicingHead> {

	@Override
	protected CompareAroundProcesser<AggInvoicingHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoicingHead> processor = new CompareAroundProcesser<AggInvoicingHead>(
				InvoicingPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggInvoicingHead[] processBP(Object userObj,
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills) {
		IInvoicingMaintain operator = NCLocator.getInstance().lookup(
				IInvoicingMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
