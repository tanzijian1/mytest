package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.paymentrequest.plugin.bpplugin.PaymentRequestPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.itf.tg.IPaymentRequestMaintain;

public class N_FN01_SAVEBASE extends AbstractPfAction<AggPayrequest> {

	@Override
	protected CompareAroundProcesser<AggPayrequest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggPayrequest> processor = null;
		AggPayrequest[] clientFullVOs = (AggPayrequest[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggPayrequest>(
					PaymentRequestPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggPayrequest>(
					PaymentRequestPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggPayrequest> rule = null;

		return processor;
	}

	@Override
	protected AggPayrequest[] processBP(Object userObj,
			AggPayrequest[] clientFullVOs, AggPayrequest[] originBills) {

		AggPayrequest[] bills = null;
		try {
			IPaymentRequestMaintain operator = NCLocator.getInstance()
					.lookup(IPaymentRequestMaintain.class);
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
