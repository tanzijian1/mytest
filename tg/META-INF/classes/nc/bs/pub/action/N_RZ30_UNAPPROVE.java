package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.bs.tg.rule.UnTicketSendDAPRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IAddticketMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;

public class N_RZ30_UNAPPROVE extends AbstractPfAction<AggAddTicket> {

	@Override
	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = new CompareAroundProcesser<AggAddTicket>(
				AddticketPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		//删除凭证
		IRule<AggAddTicket> rule = new UnTicketSendDAPRule(); 
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggAddTicket[] bills = null;
		try {
			IAddticketMaintain operator = NCLocator.getInstance()
					.lookup(IAddticketMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
