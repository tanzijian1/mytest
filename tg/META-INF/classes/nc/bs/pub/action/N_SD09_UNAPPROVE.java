package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.itf.tg.IRZreportBIMaintain;

public class N_SD09_UNAPPROVE extends AbstractPfAction<AggRZreportBIVO> {

	@Override
	protected CompareAroundProcesser<AggRZreportBIVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRZreportBIVO> processor = new CompareAroundProcesser<AggRZreportBIVO>(
				RZreportBIPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggRZreportBIVO[] processBP(Object userObj,
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggRZreportBIVO[] bills = null;
		try {
			IRZreportBIMaintain operator = NCLocator.getInstance()
					.lookup(IRZreportBIMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
