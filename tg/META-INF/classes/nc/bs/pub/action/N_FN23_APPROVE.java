package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.invoicing.ace.rule.InvoicingSendAddDAPRule;
import nc.bs.tg.invoicing.plugin.bpplugin.InvoicingPluginPoint;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.itf.tg.IInvoicingMaintain;

public class N_FN23_APPROVE extends AbstractPfAction<AggInvoicingHead> {

	public N_FN23_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggInvoicingHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoicingHead> processor = new CompareAroundProcesser<AggInvoicingHead>(
				InvoicingPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		// add by huangdq 20200403 开票工单保存时推生凭证
		IRule<AggInvoicingHead> rule = new InvoicingSendAddDAPRule();
		processor.addAfterRule(rule);

		return processor;
	}

	@Override
	protected AggInvoicingHead[] processBP(Object userObj,
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills) {
		AggInvoicingHead[] bills = null;
		IInvoicingMaintain operator = NCLocator.getInstance().lookup(
				IInvoicingMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
