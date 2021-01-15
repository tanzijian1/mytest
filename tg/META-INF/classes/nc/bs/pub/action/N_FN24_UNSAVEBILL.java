package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;

public class N_FN24_UNSAVEBILL extends AbstractPfAction<AggIntshareHead> {

	@Override
	protected CompareAroundProcesser<AggIntshareHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggIntshareHead> processor = new CompareAroundProcesser<AggIntshareHead>(
				InterestSharePluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggIntshareHead[] processBP(Object userObj,
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills) {
		IInterestShareMaintain operator = NCLocator.getInstance().lookup(
				IInterestShareMaintain.class);
		AggIntshareHead[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
