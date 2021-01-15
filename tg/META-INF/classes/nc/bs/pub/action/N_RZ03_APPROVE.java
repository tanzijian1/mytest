package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tg_groupdata.plugin.bpplugin.TG_GroupDataPluginPoint;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.itf.tg.ITG_GroupDataMaintain;

public class N_RZ03_APPROVE extends AbstractPfAction<AggGroupDataVO> {

	public N_RZ03_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggGroupDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGroupDataVO> processor = new CompareAroundProcesser<AggGroupDataVO>(
				TG_GroupDataPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggGroupDataVO[] processBP(Object userObj,
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills) {
		AggGroupDataVO[] bills = null;
		ITG_GroupDataMaintain operator = NCLocator.getInstance().lookup(
				ITG_GroupDataMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
