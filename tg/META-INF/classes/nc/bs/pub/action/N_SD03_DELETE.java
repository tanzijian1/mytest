package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.vo.tg.approvalpro.AggApprovalProVO;
import nc.itf.tg.IApprovalproMaintain;

public class N_SD03_DELETE extends AbstractPfAction<AggApprovalProVO> {

	@Override
	protected CompareAroundProcesser<AggApprovalProVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggApprovalProVO> processor = new CompareAroundProcesser<AggApprovalProVO>(
				ApprovalproPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggApprovalProVO[] processBP(Object userObj,
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills) {
		IApprovalproMaintain operator = NCLocator.getInstance().lookup(
				IApprovalproMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
