package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.checkfinance.plugin.bpplugin.CheckfinancePluginPoint;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.itf.tg.ICheckfinanceMaintain;

public class N_RZ11_DELETE extends AbstractPfAction<AggCheckFinanceHVO> {

	@Override
	protected CompareAroundProcesser<AggCheckFinanceHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggCheckFinanceHVO> processor = new CompareAroundProcesser<AggCheckFinanceHVO>(
				CheckfinancePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggCheckFinanceHVO[] processBP(Object userObj,
			AggCheckFinanceHVO[] clientFullVOs, AggCheckFinanceHVO[] originBills) {
		ICheckfinanceMaintain operator = NCLocator.getInstance().lookup(
				ICheckfinanceMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
