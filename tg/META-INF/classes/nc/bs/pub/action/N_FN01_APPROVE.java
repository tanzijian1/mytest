package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.cdm.repayreceiptbankcredit.AggRePayReceiptBankCreditVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.bs.tg.pub.paymentrequest.rule.PushPaybill;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;

public class N_FN01_APPROVE extends AbstractPfAction<AggPayrequest> {

	public N_FN01_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggPayrequest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPayrequest> processor = new CompareAroundProcesser<AggPayrequest>(
				PaymentRequestPluginPoint.APPROVE);
		IRule<AggPayrequest> rule = new PushPaybill();
		processor.addBeforeRule(new ApproveStatusCheckRule());
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggPayrequest[] processBP(Object userObj,
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills) {
		AggPayrequest[] bills = null;
		IPaymentRequestMaintain operator = NCLocator.getInstance().lookup(
				IPaymentRequestMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
