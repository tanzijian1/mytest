package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.itf.tg.ITartingBillMaintain;

public class N_FN16_SAVE extends AbstractPfAction<AggTartingBillVO> {

	protected CompareAroundProcesser<AggTartingBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTartingBillVO> processor = new CompareAroundProcesser<AggTartingBillVO>(
				TartingBillPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggTartingBillVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggTartingBillVO[] processBP(Object userObj,
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills) {
		ITartingBillMaintain operator = NCLocator.getInstance().lookup(
				ITartingBillMaintain.class);
		AggTartingBillVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
