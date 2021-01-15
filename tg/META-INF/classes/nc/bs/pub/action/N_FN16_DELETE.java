package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.itf.tg.ITartingBillMaintain;

public class N_FN16_DELETE extends AbstractPfAction<AggTartingBillVO> {

	@Override
	protected CompareAroundProcesser<AggTartingBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTartingBillVO> processor = new CompareAroundProcesser<AggTartingBillVO>(
				TartingBillPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggTartingBillVO[] processBP(Object userObj,
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills) {
		ITartingBillMaintain operator = NCLocator.getInstance().lookup(
				ITartingBillMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
