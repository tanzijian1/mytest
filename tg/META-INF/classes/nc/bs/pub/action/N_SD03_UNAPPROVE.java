package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.approvalpro.ace.rule.UnApproveRule;
import nc.bs.tg.approvalpro.plugin.bpplugin.ApprovalproPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IApprovalproMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.approvalpro.AggApprovalProVO;

public class N_SD03_UNAPPROVE extends AbstractPfAction<AggApprovalProVO> {

	@Override
	protected CompareAroundProcesser<AggApprovalProVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggApprovalProVO> processor = new CompareAroundProcesser<AggApprovalProVO>(
				ApprovalproPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveRule());

		return processor;
	}

	@Override
	protected AggApprovalProVO[] processBP(Object userObj,
			AggApprovalProVO[] clientFullVOs, AggApprovalProVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggApprovalProVO[] bills = null;
		try {
			IApprovalproMaintain operator = NCLocator.getInstance()
					.lookup(IApprovalproMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
