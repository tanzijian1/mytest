package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.itf.tg.IFischemeMaintain;

public class N_RZ05_DELETE extends AbstractPfAction<AggFIScemeHVO> {

	@Override
	protected CompareAroundProcesser<AggFIScemeHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFIScemeHVO> processor = new CompareAroundProcesser<AggFIScemeHVO>(
				FischemePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggFIScemeHVO[] processBP(Object userObj,
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills) {
		IFischemeMaintain operator = NCLocator.getInstance().lookup(
				IFischemeMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
