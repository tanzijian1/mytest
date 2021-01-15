package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.addticket.ace.rule.TicketPushImageRule;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.vo.tg.addticket.AggAddTicket;
import nc.itf.tg.IAddticketMaintain;
import nc.itf.uap.busibean.ISysInitQry;

public class N_RZ30_SAVEBASE extends AbstractPfAction<AggAddTicket> {

	@Override
	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = null;
		AggAddTicket[] clientFullVOs = (AggAddTicket[]) this.getVos();
//		ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
//		 String para_ima=null;
//		    try {
//				para_ima = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ04");
//			} catch (BusinessException e) {
//				Logger.error(e.getMessage(), e);
//				nc.vo.pubapp.pattern.exception.ExceptionUtils
//						.wrappBusinessException(e.getMessage());
//			}
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggAddTicket>(
					AddticketPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggAddTicket>(
					AddticketPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggAddTicket> rule = null;
		
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {

		AggAddTicket[] bills = null;
		try {
			IAddticketMaintain operator = NCLocator.getInstance()
					.lookup(IAddticketMaintain.class);
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
