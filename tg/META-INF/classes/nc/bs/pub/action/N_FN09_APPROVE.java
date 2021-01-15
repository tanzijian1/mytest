package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.carryovercost.plugin.bpplugin.CarryoverCostPluginPoint;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.itf.tg.ICarryoverCostMaintain;

public class N_FN09_APPROVE extends AbstractPfAction<AggCarrycost> {

	public N_FN09_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggCarrycost> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarrycost> processor = new CompareAroundProcesser<AggCarrycost>(
				CarryoverCostPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggCarrycost[] processBP(Object userObj,
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills) {
		AggCarrycost[] bills = null;
		ICarryoverCostMaintain operator = NCLocator.getInstance().lookup(
				ICarryoverCostMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggCarrycost bill : bills){
				util.addVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
