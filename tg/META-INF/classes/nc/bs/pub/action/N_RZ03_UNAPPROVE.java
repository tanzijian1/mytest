package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tg_groupdata.plugin.bpplugin.TG_GroupDataPluginPoint;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.itf.tg.ITG_GroupDataMaintain;

public class N_RZ03_UNAPPROVE extends AbstractPfAction<AggGroupDataVO> {

	@Override
	protected CompareAroundProcesser<AggGroupDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGroupDataVO> processor = new CompareAroundProcesser<AggGroupDataVO>(
				TG_GroupDataPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggGroupDataVO[] processBP(Object userObj,
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggGroupDataVO[] bills = null;
		try {
			ITG_GroupDataMaintain operator = NCLocator.getInstance()
					.lookup(ITG_GroupDataMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
