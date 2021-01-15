package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;
import nc.itf.tg.IInvoicebillMaintain;

public class N_FN05_UNAPPROVE extends AbstractPfAction<AggInvoiceBillVO> {

	@Override
	protected CompareAroundProcesser<AggInvoiceBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoiceBillVO> processor = new CompareAroundProcesser<AggInvoiceBillVO>(
				InvoicebillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInvoiceBillVO[] processBP(Object userObj,
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggInvoiceBillVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IInvoicebillMaintain operator = NCLocator.getInstance()
					.lookup(IInvoicebillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggInvoiceBillVO bill : bills){
				util.delVoucher(bill);
			}
			//====end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
