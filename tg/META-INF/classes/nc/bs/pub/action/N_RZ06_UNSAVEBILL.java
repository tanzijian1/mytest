package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.financingexpense.rule.finUnsaveDelBarcodeRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class N_RZ06_UNSAVEBILL extends AbstractPfAction<AggFinancexpenseVO> {

	@Override
	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = new CompareAroundProcesser<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());
//		 processor.addBeforeRule(new UncommitBeforeRule());
		//清除影像编码和bpmid
        processor.addBeforeRule(new finUnsaveDelBarcodeRule());
		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {
		IFinancingExpenseMaintain operator = NCLocator.getInstance().lookup(
				IFinancingExpenseMaintain.class);
		AggFinancexpenseVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
