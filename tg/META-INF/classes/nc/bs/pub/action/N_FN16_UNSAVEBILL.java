package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.itf.tg.ITartingBillMaintain;

public class N_FN16_UNSAVEBILL extends AbstractPfAction<AggTartingBillVO> {

	@Override
	protected CompareAroundProcesser<AggTartingBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTartingBillVO> processor = new CompareAroundProcesser<AggTartingBillVO>(
				TartingBillPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggTartingBillVO[] processBP(Object userObj,
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills) {
		ITartingBillMaintain operator = NCLocator.getInstance().lookup(
				ITartingBillMaintain.class);
		AggTartingBillVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
