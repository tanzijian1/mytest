package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;

public class N_RZ12_UNAPPROVE extends AbstractPfAction<AggFischemePushStandardHVO> {

	@Override
	protected CompareAroundProcesser<AggFischemePushStandardHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFischemePushStandardHVO> processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
				FischemepushstandardPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggFischemePushStandardHVO[] processBP(Object userObj,
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggFischemePushStandardHVO[] bills = null;
		try {
			IFischemepushstandardMaintain operator = NCLocator.getInstance()
					.lookup(IFischemepushstandardMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
