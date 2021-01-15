package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.standard.plugin.bpplugin.StandardPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IStandardMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;

public class N_RZ01_DELETE extends AbstractPfAction<AggStandardVO> {

	@Override
	protected CompareAroundProcesser<AggStandardVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggStandardVO> processor = new CompareAroundProcesser<AggStandardVO>(
				StandardPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggStandardVO[] processBP(Object userObj,
			AggStandardVO[] clientFullVOs, AggStandardVO[] originBills) {
		IStandardMaintain operator = NCLocator.getInstance().lookup(
				IStandardMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
