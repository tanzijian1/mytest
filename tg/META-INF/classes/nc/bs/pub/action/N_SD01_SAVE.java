package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.sealflow.plugin.bpplugin.SealflowPluginPoint;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.itf.tg.ISealflowMaintain;

public class N_SD01_SAVE extends AbstractPfAction<AggSealFlowVO> {

	protected CompareAroundProcesser<AggSealFlowVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSealFlowVO> processor = new CompareAroundProcesser<AggSealFlowVO>(
				SealflowPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggSealFlowVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggSealFlowVO[] processBP(Object userObj,
			AggSealFlowVO[] clientFullVOs, AggSealFlowVO[] originBills) {
		ISealflowMaintain operator = NCLocator.getInstance().lookup(
				ISealflowMaintain.class);
		AggSealFlowVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
