package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;

public class N_RZ12_APPROVE extends AbstractPfAction<AggFischemePushStandardHVO> {

	public N_RZ12_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggFischemePushStandardHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFischemePushStandardHVO> processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggFischemePushStandardHVO[] processBP(Object userObj,
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills) {
		AggFischemePushStandardHVO[] bills = null;
		IFischemepushstandardMaintain operator = NCLocator.getInstance().lookup(
				IFischemepushstandardMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
