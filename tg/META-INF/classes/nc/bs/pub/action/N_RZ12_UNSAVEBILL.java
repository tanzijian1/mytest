package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;

public class N_RZ12_UNSAVEBILL extends AbstractPfAction<AggFischemePushStandardHVO> {

	@Override
	protected CompareAroundProcesser<AggFischemePushStandardHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFischemePushStandardHVO> processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFischemePushStandardHVO[] processBP(Object userObj,
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills) {
		IFischemepushstandardMaintain operator = NCLocator.getInstance().lookup(
				IFischemepushstandardMaintain.class);
		AggFischemePushStandardHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
