package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.financingexpense.plugin.bpplugin.FinancingExpensePluginPoint;
import nc.bs.tg.financingexpense.rule.UnApproveAfterRule;
import nc.bs.tg.financingexpense.rule.UnApproveBeforeRule;
import nc.bs.tg.rule.UnFinSendDAPRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IFinancingExpenseMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public class N_RZ06_UNAPPROVE extends AbstractPfAction<AggFinancexpenseVO> {

	@Override
	protected CompareAroundProcesser<AggFinancexpenseVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFinancexpenseVO> processor = new CompareAroundProcesser<AggFinancexpenseVO>(
				FinancingExpensePluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveBeforeRule());
		//添加凭证
		//IRule<AggFinancexpenseVO> rule = new UnFinSendDAPRule(); 
		//processor.addAfterRule(rule);
		//删除贷款合同明细的财顾费执行情况
		processor.addAfterRule(new UnApproveAfterRule());

		return processor;
	}

	@Override
	protected AggFinancexpenseVO[] processBP(Object userObj,
			AggFinancexpenseVO[] clientFullVOs, AggFinancexpenseVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggFinancexpenseVO[] bills = null;
		try {
			IFinancingExpenseMaintain operator = NCLocator.getInstance()
					.lookup(IFinancingExpenseMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
