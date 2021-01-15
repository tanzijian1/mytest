package nc.bs.tg.financingexpense.rule;

import nc.impl.pubapp.pattern.rule.IRule;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;

public class TicketCheckIsBackToBpmRule implements IRule<AggAddTicket>{

	@Override
	public void process(AggAddTicket[] vos) {
		for (AggAddTicket aggvo : vos) {
//			if("Y".equals(aggvo.getParentVO().getAttributeValue("def34"))){
//				ExceptionUtils
//				.wrappBusinessException("单据已通知bpm退回，不能进行审核");
//			}
			aggvo.getParentVO().setAttributeValue("def34", "N");
		}
		
	}

}
