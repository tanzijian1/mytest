package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.repay.rule.DeleteRePayToImageBeforeRule;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.financingexpense.rule.DeleteFinToBPMBeforeRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class N_RZ06_DELETE extends AbstractPfAction<AggFinancexpenseVO> {

	@Override
	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = new CompareAroundProcesser<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.SCRIPT_DELETE);
		// TODO �ڴ˴����ǰ�����
		//nc����ɾ��ǰ��Ҫ֪ͨӰ��ɾ��
	    processor.addBeforeRule(new DeleteRePayToImageBeforeRule());
		//nc����ɾ��ǰ֪ͨbpm
		processor.addBeforeRule(new DeleteFinToBPMBeforeRule());
		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {
		IFinancingExpenseMaintain operator = NCLocator.getInstance().lookup(
				IFinancingExpenseMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
