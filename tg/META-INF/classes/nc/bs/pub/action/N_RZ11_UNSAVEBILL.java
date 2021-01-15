package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.checkfinance.plugin.bpplugin.CheckfinancePluginPoint;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.itf.tg.ICheckfinanceMaintain;

public class N_RZ11_UNSAVEBILL extends AbstractPfAction<AggCheckFinanceHVO> {

	@Override
	protected CompareAroundProcesser<AggCheckFinanceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCheckFinanceHVO> processor = new CompareAroundProcesser<AggCheckFinanceHVO>(
				CheckfinancePluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggCheckFinanceHVO[] processBP(Object userObj,
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills) {
		ICheckfinanceMaintain operator = NCLocator.getInstance().lookup(
				ICheckfinanceMaintain.class);
		AggCheckFinanceHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
