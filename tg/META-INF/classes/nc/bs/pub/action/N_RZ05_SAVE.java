package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.fischeme.plugin.bpplugin.FischemePluginPoint;
import nc.vo.tg.fischeme.AggFIScemeHVO;
import nc.itf.tg.IFischemeMaintain;

public class N_RZ05_SAVE extends AbstractPfAction<AggFIScemeHVO> {

	protected CompareAroundProcesser<AggFIScemeHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggFIScemeHVO> processor = new CompareAroundProcesser<AggFIScemeHVO>(
				FischemePluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggFIScemeHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggFIScemeHVO[] processBP(Object userObj,
			AggFIScemeHVO[] clientFullVOs, AggFIScemeHVO[] originBills) {
		IFischemeMaintain operator = NCLocator.getInstance().lookup(
				IFischemeMaintain.class);
		AggFIScemeHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
