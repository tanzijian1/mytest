package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.internalinterest.plugin.bpplugin.InternalInterestPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.tgfn.internalinterest.Internalinterest;
import nc.itf.tg.IInternalInterestMaintain;

public class N_FN06_UNAPPROVE extends AbstractPfAction<AggInternalinterest> {

	@Override
	protected CompareAroundProcesser<AggInternalinterest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggInternalinterest> processor = new CompareAroundProcesser<AggInternalinterest>(
				InternalInterestPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggInternalinterest[] processBP(Object userObj,
			AggInternalinterest[] clientFullVOs, AggInternalinterest[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggInternalinterest[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IInternalInterestMaintain operator = NCLocator.getInstance()
					.lookup(IInternalInterestMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggInternalinterest bill : bills){
				util.delVoucher(bill);
			}
			//====end====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
