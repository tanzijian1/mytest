package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;

public class N_FN24_DELETE extends AbstractPfAction<AggIntshareHead> {

	@Override
	protected CompareAroundProcesser<AggIntshareHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggIntshareHead> processor = new CompareAroundProcesser<AggIntshareHead>(
				InterestSharePluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggIntshareHead[] processBP(Object userObj,
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills) {
		IInterestShareMaintain operator = NCLocator.getInstance().lookup(
				IInterestShareMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
