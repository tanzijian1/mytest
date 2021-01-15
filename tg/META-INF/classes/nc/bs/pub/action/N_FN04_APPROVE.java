package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.outbill.plugin.bpplugin.OutBillPluginPoint;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.itf.tg.IOutBillMaintain;


public class N_FN04_APPROVE extends AbstractPfAction<AggOutbillHVO> {

	public N_FN04_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggOutbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOutbillHVO> processor = new CompareAroundProcesser<AggOutbillHVO>(
				OutBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggOutbillHVO[] processBP(Object userObj,
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills) {
		AggOutbillHVO[] bills = null;
		IOutBillMaintain operator = NCLocator.getInstance().lookup(
				IOutBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			// ====start====
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggOutbillHVO bill : bills){
				util.addVoucher(bill);
			}
			//====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
