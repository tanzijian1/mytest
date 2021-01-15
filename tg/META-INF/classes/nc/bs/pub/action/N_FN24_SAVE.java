package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;

public class N_FN24_SAVE extends AbstractPfAction<AggIntshareHead> {

	protected CompareAroundProcesser<AggIntshareHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggIntshareHead> processor = new CompareAroundProcesser<AggIntshareHead>(
				InterestSharePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggIntshareHead> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggIntshareHead[] processBP(Object userObj,
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills) {
		IInterestShareMaintain operator = NCLocator.getInstance().lookup(
				IInterestShareMaintain.class);
		AggIntshareHead[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
