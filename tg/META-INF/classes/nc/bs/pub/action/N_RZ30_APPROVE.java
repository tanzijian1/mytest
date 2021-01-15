package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.addticket.ace.rule.QinKuanWriteBackRule;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.bs.tg.financingexpense.rule.TicketCheckIsBackToBpmRule;
import nc.bs.tg.rule.TicketSendDAPRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IAddticketMaintain;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.addticket.AggAddTicket;

public class N_RZ30_APPROVE extends AbstractPfAction<AggAddTicket> {

	public N_RZ30_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = new CompareAroundProcesser<AggAddTicket>(
				AddticketPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		// 添加凭证
		IRule<AggAddTicket> rule = new TicketSendDAPRule();
		processor.addAfterRule(rule);
		processor.addBeforeRule(new TicketCheckIsBackToBpmRule());
		ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
		String para=null;
		try {
			 para = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ99");
		} catch (BusinessException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
//		if("Y".equals(para)){
		processor.addAfterRule(new QinKuanWriteBackRule());
//		}
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {
		AggAddTicket[] bills = null;
		IAddticketMaintain operator = NCLocator.getInstance().lookup(
				IAddticketMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
