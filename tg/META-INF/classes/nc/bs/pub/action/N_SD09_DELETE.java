package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.itf.tg.IRZreportBIMaintain;

public class N_SD09_DELETE extends AbstractPfAction<AggRZreportBIVO> {

	@Override
	protected CompareAroundProcesser<AggRZreportBIVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRZreportBIVO> processor = new CompareAroundProcesser<AggRZreportBIVO>(
				RZreportBIPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggRZreportBIVO[] processBP(Object userObj,
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills) {
		IRZreportBIMaintain operator = NCLocator.getInstance().lookup(
				IRZreportBIMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
