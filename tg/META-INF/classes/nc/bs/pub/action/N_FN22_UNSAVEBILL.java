package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.marginworkorder.plugin.bpplugin.MarginWorkorderPluginPoint;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.itf.tg.IMarginWorkorderMaintain;

public class N_FN22_UNSAVEBILL extends AbstractPfAction<AggMarginHVO> {

	@Override
	protected CompareAroundProcesser<AggMarginHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarginHVO> processor = new CompareAroundProcesser<AggMarginHVO>(
				MarginWorkorderPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggMarginHVO[] processBP(Object userObj,
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills) {
		IMarginWorkorderMaintain operator = NCLocator.getInstance().lookup(
				IMarginWorkorderMaintain.class);
		AggMarginHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
