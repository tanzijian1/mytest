package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.invoicing.ace.rule.InvoicingSendDelDAPRule;
import nc.bs.tg.invoicing.plugin.bpplugin.InvoicingPluginPoint;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.itf.tg.IInvoicingMaintain;

public class N_FN23_UNAPPROVE extends AbstractPfAction<AggInvoicingHead> {

	@Override
	protected CompareAroundProcesser<AggInvoicingHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoicingHead> processor = new CompareAroundProcesser<AggInvoicingHead>(
				InvoicingPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		// add by huangdq 20200403 开票工单保存时推生凭证
		IRule<AggInvoicingHead> rule = new InvoicingSendDelDAPRule();
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggInvoicingHead[] processBP(Object userObj,
			AggInvoicingHead[] clientFullVOs, AggInvoicingHead[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggInvoicingHead[] bills = null;
		try {
			IInvoicingMaintain operator = NCLocator.getInstance().lookup(
					IInvoicingMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
