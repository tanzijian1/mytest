package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;

public class N_FN01_SAVE extends AbstractPfAction<AggPayrequest> {

	protected CompareAroundProcesser<AggPayrequest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPayrequest> processor = new CompareAroundProcesser<AggPayrequest>(
				PaymentRequestPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggPayrequest> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggPayrequest[] processBP(Object userObj,
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills) {
		IPaymentRequestMaintain operator = NCLocator.getInstance().lookup(
				IPaymentRequestMaintain.class);
		AggPayrequest[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
