package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.itf.tg.IInvoicebillMaintain;

public class N_FN05_SAVE extends AbstractPfAction<AggInvoiceBillVO> {

	protected CompareAroundProcesser<AggInvoiceBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoiceBillVO> processor = new CompareAroundProcesser<AggInvoiceBillVO>(
				InvoicebillPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggInvoiceBillVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggInvoiceBillVO[] processBP(Object userObj,
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills) {
		IInvoicebillMaintain operator = NCLocator.getInstance().lookup(
				IInvoicebillMaintain.class);
		AggInvoiceBillVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
