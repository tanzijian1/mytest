package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.sealflow.plugin.bpplugin.SealflowPluginPoint;
import nc.vo.tg.sealflow.AggSealFlowVO;
import nc.itf.tg.ISealflowMaintain;

public class N_SD01_UNAPPROVE extends AbstractPfAction<AggSealFlowVO> {

	@Override
	protected CompareAroundProcesser<AggSealFlowVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggSealFlowVO> processor = new CompareAroundProcesser<AggSealFlowVO>(
				SealflowPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggSealFlowVO[] processBP(Object userObj,
			AggSealFlowVO[] clientFullVOs, AggSealFlowVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggSealFlowVO[] bills = null;
		try {
			ISealflowMaintain operator = NCLocator.getInstance()
					.lookup(ISealflowMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
