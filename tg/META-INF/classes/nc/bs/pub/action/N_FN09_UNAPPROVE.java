package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.carryovercost.plugin.bpplugin.CarryoverCostPluginPoint;
import nc.vo.tgfn.carryovercost.AggCarrycost;
import nc.itf.tg.ICarryoverCostMaintain;

public class N_FN09_UNAPPROVE extends AbstractPfAction<AggCarrycost> {

	@Override
	protected CompareAroundProcesser<AggCarrycost> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCarrycost> processor = new CompareAroundProcesser<AggCarrycost>(
				CarryoverCostPluginPoint.UNAPPROVE);
		// TODO �ڴ˴����ǰ�����
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggCarrycost[] processBP(Object userObj,
			AggCarrycost[] clientFullVOs, AggCarrycost[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggCarrycost[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ICarryoverCostMaintain operator = NCLocator.getInstance()
					.lookup(ICarryoverCostMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019��9��2��11:11:19 ln ɾ��ƾ֤ ====start===={
			for(AggCarrycost bill : bills){
				util.delVoucher(bill);
			}
			//====end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
