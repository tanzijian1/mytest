package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.addticket.ace.rule.TicketPushImageRule;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.bs.tg.financingexpense.rule.PushAddTicketToBPMBillRule;
import nc.bs.tg.financingexpense.rule.PushImageRule;
import nc.vo.tg.addticket.AggAddTicket;
import nc.itf.tg.IAddticketMaintain;
import nc.itf.uap.busibean.ISysInitQry;

public class N_RZ30_SAVE extends AbstractPfAction<AggAddTicket> {

	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = new CompareAroundProcesser<AggAddTicket>(
				AddticketPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则

		ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
	    String para = null;
	    String para_ima=null;
	    try {
			para = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ02");
			para_ima = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ04");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			nc.vo.pubapp.pattern.exception.ExceptionUtils
					.wrappBusinessException(e.getMessage());
		}
	    IRule<AggAddTicket> pushimagerule=new TicketPushImageRule(para_ima);
		processor.addBeforeRule(pushimagerule);
	    if ("Y".equals(para)) {
	    	IRule<AggAddTicket> pushrule= new PushAddTicketToBPMBillRule();
	    	processor.addBeforeRule(pushrule);
	    }
	    
		IRule<AggAddTicket> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {
		IAddticketMaintain operator = NCLocator.getInstance().lookup(
				IAddticketMaintain.class);
		AggAddTicket[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
