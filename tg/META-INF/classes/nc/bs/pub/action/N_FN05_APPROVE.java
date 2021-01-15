package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.invoicebill.plugin.bpplugin.InvoicebillPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IInvoicebillMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tg.invoicebill.InvoiceBillVO;
import nc.vo.tgfn.outbill.AggOutbillHVO;

public class N_FN05_APPROVE extends AbstractPfAction<AggInvoiceBillVO> {

	public N_FN05_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggInvoiceBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInvoiceBillVO> processor = new CompareAroundProcesser<AggInvoiceBillVO>(
				InvoicebillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggInvoiceBillVO[] processBP(Object userObj,
			AggInvoiceBillVO[] clientFullVOs, AggInvoiceBillVO[] originBills) {
		AggInvoiceBillVO[] bills = null;
		IInvoicebillMaintain operator = NCLocator.getInstance().lookup(
				IInvoicebillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggInvoiceBillVO bill : bills){
				util.addVoucher(bill);
			}
			//=====end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
