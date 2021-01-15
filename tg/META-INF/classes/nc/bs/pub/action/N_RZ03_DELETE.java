package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tg_groupdata.plugin.bpplugin.TG_GroupDataPluginPoint;
import nc.vo.tg.tg_groupdata.AggGroupDataVO;
import nc.itf.tg.ITG_GroupDataMaintain;

public class N_RZ03_DELETE extends AbstractPfAction<AggGroupDataVO> {

	@Override
	protected CompareAroundProcesser<AggGroupDataVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggGroupDataVO> processor = new CompareAroundProcesser<AggGroupDataVO>(
				TG_GroupDataPluginPoint.SCRIPT_DELETE);
		// TODO �ڴ˴����ǰ�����
		return processor;
	}

	@Override
	protected AggGroupDataVO[] processBP(Object userObj,
			AggGroupDataVO[] clientFullVOs, AggGroupDataVO[] originBills) {
		ITG_GroupDataMaintain operator = NCLocator.getInstance().lookup(
				ITG_GroupDataMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
