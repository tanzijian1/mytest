package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.salaryfundaccure.ace.rule.HCMSalaryFundAccureTicketToPayBillRule;
import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ISalaryfundaccureMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;

public class N_HCM1_APPROVE extends AbstractPfAction<AggSalaryfundaccure> {

	public N_HCM1_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSalaryfundaccure> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalaryfundaccure> processor = new CompareAroundProcesser<AggSalaryfundaccure>(
				SalaryfundaccurePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		processor.addAfterRule(new HCMSalaryFundAccureTicketToPayBillRule());
		return processor;
	}

	@Override
	protected AggSalaryfundaccure[] processBP(Object userObj,
			AggSalaryfundaccure[] clientFullVOs,
			AggSalaryfundaccure[] originBills) {
		AggSalaryfundaccure[] bills = null;
		ISalaryfundaccureMaintain operator = NCLocator.getInstance().lookup(
				ISalaryfundaccureMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
