package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.itf.tg.ICostAccrueBillMaintain;

public class N_FN08_DELETE extends AbstractPfAction<AggCostAccrueBill> {

	@Override
	protected CompareAroundProcesser<AggCostAccrueBill> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCostAccrueBill> processor = new CompareAroundProcesser<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggCostAccrueBill[] processBP(Object userObj,
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills) {
		ICostAccrueBillMaintain operator = NCLocator.getInstance().lookup(
				ICostAccrueBillMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
