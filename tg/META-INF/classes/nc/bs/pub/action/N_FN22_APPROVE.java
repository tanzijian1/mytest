package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.marginworkorder.plugin.bpplugin.MarginWorkorderPluginPoint;
import nc.vo.tgfn.marginworkorder.AggMarginHVO;
import nc.itf.tg.IMarginWorkorderMaintain;

public class N_FN22_APPROVE extends AbstractPfAction<AggMarginHVO> {

	public N_FN22_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggMarginHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarginHVO> processor = new CompareAroundProcesser<AggMarginHVO>(
				MarginWorkorderPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggMarginHVO[] processBP(Object userObj,
			AggMarginHVO[] clientFullVOs, AggMarginHVO[] originBills) {
		AggMarginHVO[] bills = null;
		IMarginWorkorderMaintain operator = NCLocator.getInstance().lookup(
				IMarginWorkorderMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggMarginHVO bill : bills){
				util.addVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
