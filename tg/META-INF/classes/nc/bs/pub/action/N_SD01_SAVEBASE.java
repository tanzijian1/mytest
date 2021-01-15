package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.sealflow.plugin.bpplugin.SealflowPluginPoint;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.itf.tg.ISealflowMaintain;

public class N_SD01_SAVEBASE extends AbstractPfAction<AggSealFlowVO> {

	@Override
	protected CompareAroundProcesser<AggSealFlowVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSealFlowVO> processor = null;
		AggSealFlowVO[] clientFullVOs = (AggSealFlowVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggSealFlowVO>(
					SealflowPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggSealFlowVO>(
					SealflowPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggSealFlowVO> rule = new nc.bs.pub.action.rule.ApprovalFileRule();
		  processor.addAfterRule(rule);
		return processor;
	}

	@Override
	protected AggSealFlowVO[] processBP(Object userObj,
			AggSealFlowVO[] clientFullVOs, AggSealFlowVO[] originBills) {

		AggSealFlowVO[] bills = null;
		try {
			ISealflowMaintain operator = NCLocator.getInstance()
					.lookup(ISealflowMaintain.class);
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
