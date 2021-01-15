package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.sealflow.plugin.bpplugin.SealflowPluginPoint;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.itf.tg.ISealflowMaintain;

public class N_SD01_UNSAVEBILL extends AbstractPfAction<AggSealFlowVO> {

	@Override
	protected CompareAroundProcesser<AggSealFlowVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSealFlowVO> processor = new CompareAroundProcesser<AggSealFlowVO>(
				SealflowPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSealFlowVO[] processBP(Object userObj,
			AggSealFlowVO[] clientFullVOs, AggSealFlowVO[] originBills) {
		ISealflowMaintain operator = NCLocator.getInstance().lookup(
				ISealflowMaintain.class);
		AggSealFlowVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
