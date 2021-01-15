package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.repay.rule.DeleteRePayToImageBeforeRule;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.addticket.plugin.bpplugin.AddticketPluginPoint;
import nc.bs.tg.financingexpense.rule.DeleteAddticketBeforeRule;
import nc.bs.tg.financingexpense.rule.DeleteFinToBPMBeforeRule;
import nc.vo.tg.addticket.AggAddTicket;
import nc.itf.tg.IAddticketMaintain;

public class N_RZ30_DELETE extends AbstractPfAction<AggAddTicket> {

	@Override
	protected CompareAroundProcesser<AggAddTicket> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAddTicket> processor = new CompareAroundProcesser<AggAddTicket>(
				AddticketPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		//nc单据删除前需要通知影像删除
	    processor.addBeforeRule(new DeleteRePayToImageBeforeRule());
		processor.addBeforeRule(new DeleteAddticketBeforeRule());
		return processor;
	}

	@Override
	protected AggAddTicket[] processBP(Object userObj,
			AggAddTicket[] clientFullVOs, AggAddTicket[] originBills) {
		IAddticketMaintain operator = NCLocator.getInstance().lookup(
				IAddticketMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
