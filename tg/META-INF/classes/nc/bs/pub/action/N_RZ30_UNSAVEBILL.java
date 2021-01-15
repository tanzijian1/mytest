package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.bs.tg.addticket.ace.rule.AddTicketUnsaveDeleteBarcodeRule;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IAddticketMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;

public class N_RZ30_UNSAVEBILL extends AbstractPfAction<AggAddTicket> {

	@Override
	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = new CompareAroundProcesser<AggAddTicket>(
				AddticketPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());
		//清除影像编码和bpmid
		processor.addBeforeRule(new AddTicketUnsaveDeleteBarcodeRule());
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {
		IAddticketMaintain operator = NCLocator.getInstance().lookup(
				IAddticketMaintain.class);
		AggAddTicket[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
