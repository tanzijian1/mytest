package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.bs.tg.transferbill.plugin.bpplugin.TransferBillPluginPoint;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.ITransferBillMaintain;

public class N_FN15_UNAPPROVE extends AbstractPfAction<AggTransferBillHVO> {

	@Override
	protected CompareAroundProcesser<AggTransferBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTransferBillHVO> processor = new CompareAroundProcesser<AggTransferBillHVO>(
				TransferBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggTransferBillHVO[] processBP(Object userObj,
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggTransferBillHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ITransferBillMaintain operator = NCLocator.getInstance()
					.lookup(ITransferBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			
			for(AggTransferBillHVO bill : bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
