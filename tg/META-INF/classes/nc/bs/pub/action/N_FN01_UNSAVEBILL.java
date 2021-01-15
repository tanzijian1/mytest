package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;

public class N_FN01_UNSAVEBILL extends AbstractPfAction<AggPayrequest> {

	@Override
	protected CompareAroundProcesser<AggPayrequest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPayrequest> processor = new CompareAroundProcesser<AggPayrequest>(
				PaymentRequestPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggPayrequest[] processBP(Object userObj,
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills) {
		IPaymentRequestMaintain operator = NCLocator.getInstance().lookup(
				IPaymentRequestMaintain.class);
		AggPayrequest[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
