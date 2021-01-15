package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tg_groupdata.plugin.bpplugin.TG_GroupDataPluginPoint;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.itf.tg.ITG_GroupDataMaintain;

public class N_RZ03_SAVE extends AbstractPfAction<AggGroupDataVO> {

	protected CompareAroundProcesser<AggGroupDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGroupDataVO> processor = new CompareAroundProcesser<AggGroupDataVO>(
				TG_GroupDataPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggGroupDataVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggGroupDataVO[] processBP(Object userObj,
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills) {
		ITG_GroupDataMaintain operator = NCLocator.getInstance().lookup(
				ITG_GroupDataMaintain.class);
		AggGroupDataVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
