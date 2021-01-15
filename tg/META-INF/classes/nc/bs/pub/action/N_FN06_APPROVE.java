package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.invoicebill.AggInvoiceBillVO;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Internalinterest;
import nc.itf.tg.IInternalInterestMaintain;

public class N_FN06_APPROVE extends AbstractPfAction<AggInternalinterest> {

	public N_FN06_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggInternalinterest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInternalinterest> processor = new CompareAroundProcesser<AggInternalinterest>(
				InternalInterestPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggInternalinterest[] processBP(Object userObj,
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills) {
		AggInternalinterest[] bills = null;
		IInternalInterestMaintain operator = NCLocator.getInstance().lookup(
				IInternalInterestMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggInternalinterest bill : bills){
				util.addVoucher(bill);
			}
			//====end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
