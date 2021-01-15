package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;

public class N_FN24_APPROVE extends AbstractPfAction<AggIntshareHead> {

	public N_FN24_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggIntshareHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggIntshareHead> processor = new CompareAroundProcesser<AggIntshareHead>(
				InterestSharePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggIntshareHead[] processBP(Object userObj,
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills) {
		AggIntshareHead[] bills = null;
		IInterestShareMaintain operator = NCLocator.getInstance().lookup(
				IInterestShareMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			for(AggIntshareHead aggvo:bills){
				util.addVoucher(aggvo);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
