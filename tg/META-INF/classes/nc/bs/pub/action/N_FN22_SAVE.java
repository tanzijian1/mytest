package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.marginworkorder.plugin.bpplugin.MarginWorkorderPluginPoint;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.itf.tg.IMarginWorkorderMaintain;

public class N_FN22_SAVE extends AbstractPfAction<AggMarginHVO> {

	protected CompareAroundProcesser<AggMarginHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarginHVO> processor = new CompareAroundProcesser<AggMarginHVO>(
				MarginWorkorderPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggMarginHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggMarginHVO[] processBP(Object userObj,
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills) {
		IMarginWorkorderMaintain operator = NCLocator.getInstance().lookup(
				IMarginWorkorderMaintain.class);
		AggMarginHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
