package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.transferbill.plugin.bpplugin.TransferBillPluginPoint;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.ITransferBillMaintain;

public class N_FN15_UNSAVEBILL extends AbstractPfAction<AggTransferBillHVO> {

	@Override
	protected CompareAroundProcesser<AggTransferBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTransferBillHVO> processor = new CompareAroundProcesser<AggTransferBillHVO>(
				TransferBillPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggTransferBillHVO[] processBP(Object userObj,
			AggTransferBillHVO[] clientFullVOs, AggTransferBillHVO[] originBills) {
		ITransferBillMaintain operator = NCLocator.getInstance().lookup(
				ITransferBillMaintain.class);
		AggTransferBillHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
