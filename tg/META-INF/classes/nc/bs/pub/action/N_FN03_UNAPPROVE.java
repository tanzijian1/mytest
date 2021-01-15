package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.bs.tg.temporaryestimate.plugin.bpplugin.TemporaryEstimatePluginPoint;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.tgfn.temporaryestimate.Temest;
import nc.itf.tg.ITemporaryEstimateMaintain;

public class N_FN03_UNAPPROVE extends AbstractPfAction<AggTemest> {

	@Override
	protected CompareAroundProcesser<AggTemest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTemest> processor = new CompareAroundProcesser<AggTemest>(
				TemporaryEstimatePluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggTemest[] processBP(Object userObj,
			AggTemest[] clientFullVOs, AggTemest[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggTemest[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ITemporaryEstimateMaintain operator = NCLocator.getInstance()
					.lookup(ITemporaryEstimateMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggTemest bill : bills){
				util.delVoucher(bill);
			}
			//====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
