package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.itf.tg.ICostAccrueBillMaintain;

public class N_FN08_UNSAVEBILL extends AbstractPfAction<AggCostAccrueBill> {

	@Override
	protected CompareAroundProcesser<AggCostAccrueBill> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCostAccrueBill> processor = new CompareAroundProcesser<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggCostAccrueBill[] processBP(Object userObj,
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills) {
		ICostAccrueBillMaintain operator = NCLocator.getInstance().lookup(
				ICostAccrueBillMaintain.class);
		AggCostAccrueBill[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
