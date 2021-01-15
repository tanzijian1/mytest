package nc.bs.tg.rule;

import nc.bs.logging.Logger;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;

@SuppressWarnings("rawtypes")
public class TicketSendDAPRule implements IRule<AggAddTicket> {

	@Override
	public void process(AggAddTicket[] vos) {
		TicketSendVoucherUtil util = new TicketSendVoucherUtil();
		for (AggAddTicket vo : vos) {
			try {
				if(vo.getParentVO().getApprovestatus()==1){
				util.addVoucher(vo);
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage(), e);
				ExceptionUtils.wrappBusinessException(e.getMessage());
			}

		}
	}
}
