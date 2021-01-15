package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.targetactivation.plugin.bpplugin.TargetActivationPluginPoint;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.ITargetActivationMaintain;

public class N_FN17_APPROVE extends AbstractPfAction<AggTargetactivation> {

	public N_FN17_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggTargetactivation> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTargetactivation> processor = new CompareAroundProcesser<AggTargetactivation>(
				TargetActivationPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggTargetactivation[] processBP(Object userObj,
			AggTargetactivation[] clientFullVOs, AggTargetactivation[] originBills) {
		AggTargetactivation[] bills = null;
		ITargetActivationMaintain operator = NCLocator.getInstance().lookup(
				ITargetActivationMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			for(AggTargetactivation bill : bills){
				util.addVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
