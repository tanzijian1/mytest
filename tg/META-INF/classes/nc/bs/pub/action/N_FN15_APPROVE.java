package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.transferbill.plugin.bpplugin.TransferBillPluginPoint;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.ITransferBillMaintain;

public class N_FN15_APPROVE extends AbstractPfAction<AggTransferBillHVO> {

	public N_FN15_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggTransferBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTransferBillHVO> processor = new CompareAroundProcesser<AggTransferBillHVO>(
				TransferBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggTransferBillHVO[] processBP(Object userObj,
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills) {
		AggTransferBillHVO[] bills = null;
		ITransferBillMaintain operator = NCLocator.getInstance().lookup(
				ITransferBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggTransferBillHVO bill : bills){
				util.addVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
