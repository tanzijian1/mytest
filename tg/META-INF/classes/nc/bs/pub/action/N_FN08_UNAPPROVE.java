package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.itf.tg.ICostAccrueBillMaintain;

public class N_FN08_UNAPPROVE extends AbstractPfAction<AggCostAccrueBill> {

	@Override
	protected CompareAroundProcesser<AggCostAccrueBill> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCostAccrueBill> processor = new CompareAroundProcesser<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggCostAccrueBill[] processBP(Object userObj,
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggCostAccrueBill[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ICostAccrueBillMaintain operator = NCLocator.getInstance()
					.lookup(ICostAccrueBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggCostAccrueBill bill : bills){
				util.delVoucher(bill);
			}
			//====end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
