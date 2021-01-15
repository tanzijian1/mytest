package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.outbill.plugin.bpplugin.OutBillPluginPoint;
import nc.vo.tgfn.outbill.AggOutbillHVO;
import nc.itf.tg.IOutBillMaintain;

public class N_FN04_DELETE extends AbstractPfAction<AggOutbillHVO> {

	@Override
	protected CompareAroundProcesser<AggOutbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOutbillHVO> processor = new CompareAroundProcesser<AggOutbillHVO>(
				OutBillPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggOutbillHVO[] processBP(Object userObj,
			AggOutbillHVO[] clientFullVOs, AggOutbillHVO[] originBills) {
		IOutBillMaintain operator = NCLocator.getInstance().lookup(
				IOutBillMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
