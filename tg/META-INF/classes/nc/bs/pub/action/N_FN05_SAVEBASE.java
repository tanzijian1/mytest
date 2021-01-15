package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.itf.tg.IInvoicebillMaintain;

public class N_FN05_SAVEBASE extends AbstractPfAction<AggInvoiceBillVO> {

	@Override
	protected CompareAroundProcesser<AggInvoiceBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoiceBillVO> processor = null;
		AggInvoiceBillVO[] clientFullVOs = (AggInvoiceBillVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggInvoiceBillVO>(
					InvoicebillPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggInvoiceBillVO>(
					InvoicebillPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggInvoiceBillVO> rule = null;

		return processor;
	}

	@Override
	protected AggInvoiceBillVO[] processBP(Object userObj,
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills) {

		AggInvoiceBillVO[] bills = null;
		try {
			IInvoicebillMaintain operator = NCLocator.getInstance()
					.lookup(IInvoicebillMaintain.class);
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
