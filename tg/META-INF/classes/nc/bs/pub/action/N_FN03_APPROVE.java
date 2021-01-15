package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.bs.tg.temporaryestimate.plugin.bpplugin.TemporaryEstimatePluginPoint;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.tgfn.temporaryestimate.Temest;
import nc.itf.tg.ITemporaryEstimateMaintain;

public class N_FN03_APPROVE extends AbstractPfAction<AggTemest> {

	public N_FN03_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggTemest> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTemest> processor = new CompareAroundProcesser<AggTemest>(
				TemporaryEstimatePluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggTemest[] processBP(Object userObj,
			AggTemest[] clientFullVOs, AggTemest[] originBills) {
		AggTemest[] bills = null;
		ITemporaryEstimateMaintain operator = NCLocator.getInstance().lookup(
				ITemporaryEstimateMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggTemest bill : bills){
				util.addVoucher(bill);
			}
			//====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
