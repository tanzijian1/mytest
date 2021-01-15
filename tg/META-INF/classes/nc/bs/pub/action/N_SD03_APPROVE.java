package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.approvalpro.ace.rule.IssueWriteBackRule;
import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IApprovalproMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;

public class N_SD03_APPROVE extends AbstractPfAction<AggApprovalProVO> {
	public N_SD03_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggApprovalProVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggApprovalProVO> processor = new CompareAroundProcesser<AggApprovalProVO>(
				ApprovalproPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		processor.addAfterRule(new IssueWriteBackRule());
		return processor;
	}

	@Override
	protected AggApprovalProVO[] processBP(Object userObj,
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills) {
		AggApprovalProVO[] bills = null;
		IApprovalproMaintain operator = NCLocator.getInstance().lookup(
				IApprovalproMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
	
	
}
