package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.bs.tg.pub.paymentrequest.rule.UnapprovePayBillCheckRule;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;

public class N_FN01_UNAPPROVE extends AbstractPfAction<AggPayrequest> {

	@Override
	protected CompareAroundProcesser<AggPayrequest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPayrequest> processor = new CompareAroundProcesser<AggPayrequest>(
				PaymentRequestPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnapprovePayBillCheckRule());

		return processor;
	}

	@Override
	protected AggPayrequest[] processBP(Object userObj,
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggPayrequest[] bills = null;
		try {
			IPaymentRequestMaintain operator = NCLocator.getInstance()
					.lookup(IPaymentRequestMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
