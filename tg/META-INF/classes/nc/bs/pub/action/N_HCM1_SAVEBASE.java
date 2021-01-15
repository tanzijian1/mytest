package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.salaryfundaccure.plugin.bpplugin.SalaryfundaccurePluginPoint;
import nc.vo.tg.salaryfundaccure.AggSalaryfundaccure;
import nc.itf.tg.ISalaryfundaccureMaintain;

public class N_HCM1_SAVEBASE extends AbstractPfAction<AggSalaryfundaccure> {

	@Override
	protected CompareAroundProcesser<AggSalaryfundaccure> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSalaryfundaccure> processor = null;
		AggSalaryfundaccure[] clientFullVOs = (AggSalaryfundaccure[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSalaryfundaccure>(
					SalaryfundaccurePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSalaryfundaccure>(
					SalaryfundaccurePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSalaryfundaccure> rule = null;

		return processor;
	}

	@Override
	protected AggSalaryfundaccure[] processBP(Object userObj,
			AggSalaryfundaccure[] clientFullVOs, AggSalaryfundaccure[] originBills) {

		AggSalaryfundaccure[] bills = null;
		try {
			ISalaryfundaccureMaintain operator = NCLocator.getInstance()
					.lookup(ISalaryfundaccureMaintain.class);
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
