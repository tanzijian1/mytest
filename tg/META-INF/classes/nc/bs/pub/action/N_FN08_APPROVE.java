package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.costaccruebill.plugin.bpplugin.CostAccrueBillPluginPoint;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.itf.tg.ICostAccrueBillMaintain;

public class N_FN08_APPROVE extends AbstractPfAction<AggCostAccrueBill> {

	public N_FN08_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggCostAccrueBill> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCostAccrueBill> processor = new CompareAroundProcesser<AggCostAccrueBill>(
				CostAccrueBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggCostAccrueBill[] processBP(Object userObj,
			AggCostAccrueBill[] clientFullVOs, AggCostAccrueBill[] originBills) {
		AggCostAccrueBill[] bills = null;
		ICostAccrueBillMaintain operator = NCLocator.getInstance().lookup(
				ICostAccrueBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggCostAccrueBill bill : bills){
				util.addVoucher(bill);
			}
			//===end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
