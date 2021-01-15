package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;

public class N_RZ12_SAVE extends AbstractPfAction<AggFischemePushStandardHVO> {

	protected CompareAroundProcesser<AggFischemePushStandardHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFischemePushStandardHVO> processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggFischemePushStandardHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggFischemePushStandardHVO[] processBP(Object userObj,
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills) {
		IFischemepushstandardMaintain operator = NCLocator.getInstance().lookup(
				IFischemepushstandardMaintain.class);
		AggFischemePushStandardHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
