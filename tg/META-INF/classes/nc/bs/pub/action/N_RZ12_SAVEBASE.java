package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischemepushstandard.plugin.bpplugin.FischemepushstandardPluginPoint;
import nc.vo.tg.fischemepushstandard.AggFischemePushStandardHVO;
import nc.itf.tg.IFischemepushstandardMaintain;

public class N_RZ12_SAVEBASE extends AbstractPfAction<AggFischemePushStandardHVO> {

	@Override
	protected CompareAroundProcesser<AggFischemePushStandardHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFischemePushStandardHVO> processor = null;
		AggFischemePushStandardHVO[] clientFullVOs = (AggFischemePushStandardHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
					FischemepushstandardPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggFischemePushStandardHVO>(
					FischemepushstandardPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggFischemePushStandardHVO> rule = null;

		return processor;
	}

	@Override
	protected AggFischemePushStandardHVO[] processBP(Object userObj,
			AggFischemePushStandardHVO[] clientFullVOs, AggFischemePushStandardHVO[] originBills) {

		AggFischemePushStandardHVO[] bills = null;
		try {
			IFischemepushstandardMaintain operator = NCLocator.getInstance()
					.lookup(IFischemepushstandardMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
