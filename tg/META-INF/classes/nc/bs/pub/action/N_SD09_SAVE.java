package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.rzreportbi.plugin.bpplugin.RZreportBIPluginPoint;
import nc.vo.tg.rzreportbi.AggRZreportBIVO;
import nc.itf.tg.IRZreportBIMaintain;

public class N_SD09_SAVE extends AbstractPfAction<AggRZreportBIVO> {

	protected CompareAroundProcesser<AggRZreportBIVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRZreportBIVO> processor = new CompareAroundProcesser<AggRZreportBIVO>(
				RZreportBIPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggRZreportBIVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggRZreportBIVO[] processBP(Object userObj,
			AggRZreportBIVO[] clientFullVOs, AggRZreportBIVO[] originBills) {
		IRZreportBIMaintain operator = NCLocator.getInstance().lookup(
				IRZreportBIMaintain.class);
		AggRZreportBIVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
