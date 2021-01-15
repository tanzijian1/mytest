package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.singleissue.ace.rule.WriteBack;
import nc.bs.tg.singleissue.plugin.bpplugin.SingleissuePluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ISingleissueMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.singleissue.AggSingleIssueVO;

public class N_SD06_APPROVE extends AbstractPfAction<AggSingleIssueVO> {

	public N_SD06_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggSingleIssueVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSingleIssueVO> processor = new CompareAroundProcesser<AggSingleIssueVO>(
				SingleissuePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		IRule<AggSingleIssueVO> rule = new WriteBack();
		processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggSingleIssueVO[] processBP(Object userObj,
			AggSingleIssueVO[] clientFullVOs, AggSingleIssueVO[] originBills) {
		AggSingleIssueVO[] bills = null;
		ISingleissueMaintain operator = NCLocator.getInstance().lookup(
				ISingleissueMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
