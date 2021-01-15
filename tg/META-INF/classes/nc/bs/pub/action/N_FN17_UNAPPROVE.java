package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ITargetActivationMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.targetactivation.AggTargetactivation;

public class N_FN17_UNAPPROVE extends AbstractPfAction<AggTargetactivation> {

	@Override
	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = new CompareAroundProcesser<AggTargetactivation>(
				TargetActivationPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
        processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		SendVoucherUtil util = new SendVoucherUtil();
		AggTargetactivation[] bills = null;
		try {
			ITargetActivationMaintain operator = NCLocator.getInstance()
					.lookup(ITargetActivationMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggTargetactivation bill : bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
