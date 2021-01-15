package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.vo.tg.singleissue.AggSingleIssueVO;
import nc.itf.tg.ISingleissueMaintain;

public class N_SD06_SAVE extends AbstractPfAction<AggSingleIssueVO> {

	protected CompareAroundProcesser<AggSingleIssueVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSingleIssueVO> processor = new CompareAroundProcesser<AggSingleIssueVO>(
				SingleissuePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggSingleIssueVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggSingleIssueVO[] processBP(Object userObj,
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills) {
		ISingleissueMaintain operator = NCLocator.getInstance().lookup(
				ISingleissueMaintain.class);
		AggSingleIssueVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
