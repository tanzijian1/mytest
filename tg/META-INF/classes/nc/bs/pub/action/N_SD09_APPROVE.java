package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.itf.tg.IRZreportBIMaintain;

public class N_SD09_APPROVE extends AbstractPfAction<AggRZreportBIVO> {

	public N_SD09_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggRZreportBIVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRZreportBIVO> processor = new CompareAroundProcesser<AggRZreportBIVO>(
				RZreportBIPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggRZreportBIVO[] processBP(Object userObj,
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills) {
		AggRZreportBIVO[] bills = null;
		IRZreportBIMaintain operator = NCLocator.getInstance().lookup(
				IRZreportBIMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
