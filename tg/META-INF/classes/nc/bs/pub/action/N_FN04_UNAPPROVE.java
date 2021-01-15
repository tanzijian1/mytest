package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.outbill.plugin.bpplugin.OutBillPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.vo.tgfn.outbill.OutbillHVO;
import nc.itf.tg.IOutBillMaintain;

public class N_FN04_UNAPPROVE extends AbstractPfAction<AggOutbillHVO> {

	@Override
	protected CompareAroundProcesser<AggOutbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOutbillHVO> processor = new CompareAroundProcesser<AggOutbillHVO>(
				OutBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggOutbillHVO[] processBP(Object userObj,
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggOutbillHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IOutBillMaintain operator = NCLocator.getInstance()
					.lookup(IOutBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			for(AggOutbillHVO bill : bills){
				util.delVoucher(bill);
			}
			//====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
