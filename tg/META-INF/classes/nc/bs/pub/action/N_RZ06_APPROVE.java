package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.financingexpense.rule.ApproveAfterRule;
import nc.bs.tg.rule.FinSendDAPRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class N_RZ06_APPROVE extends AbstractPfAction<AggFinancexpenseVO> {

	public N_RZ06_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = new CompareAroundProcesser<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		processor.addAfterRule(new ApproveAfterRule());
		//Ìí¼ÓÆ¾Ö¤
		//IRule<AggFinancexpenseVO> rule = new FinSendDAPRule(); 
		//processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {
		
		AggFinancexpenseVO[] bills = null;
		IFinancingExpenseMaintain operator = NCLocator.getInstance().lookup(
				IFinancingExpenseMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
	
		return bills;
	}

}
