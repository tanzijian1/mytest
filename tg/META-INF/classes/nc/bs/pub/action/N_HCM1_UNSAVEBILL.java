package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.itf.tg.ISalaryfundaccureMaintain;

public class N_HCM1_UNSAVEBILL extends AbstractPfAction<AggSalaryfundaccure> {

	@Override
	protected CompareAroundProcesser<AggSalaryfundaccure> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalaryfundaccure> processor = new CompareAroundProcesser<AggSalaryfundaccure>(
				SalaryfundaccurePluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSalaryfundaccure[] processBP(Object userObj,
			AggSalaryfundaccure[] clientFullVOs, AggSalaryfundaccure[] originBills) {
		ISalaryfundaccureMaintain operator = NCLocator.getInstance().lookup(
				ISalaryfundaccureMaintain.class);
		AggSalaryfundaccure[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
