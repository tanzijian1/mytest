package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.checkfinance.plugin.bpplugin.CheckfinancePluginPoint;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.itf.tg.ICheckfinanceMaintain;

public class N_RZ11_SAVEBASE extends AbstractPfAction<AggCheckFinanceHVO> {

	@Override
	protected CompareAroundProcesser<AggCheckFinanceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCheckFinanceHVO> processor = null;
		AggCheckFinanceHVO[] clientFullVOs = (AggCheckFinanceHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggCheckFinanceHVO>(
					CheckfinancePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggCheckFinanceHVO>(
					CheckfinancePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggCheckFinanceHVO> rule = null;

		return processor;
	}

	@Override
	protected AggCheckFinanceHVO[] processBP(Object userObj,
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills) {

		AggCheckFinanceHVO[] bills = null;
		try {
			ICheckfinanceMaintain operator = NCLocator.getInstance()
					.lookup(ICheckfinanceMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
