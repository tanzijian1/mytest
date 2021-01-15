package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.interestshare.plugin.bpplugin.InterestSharePluginPoint;
import nc.vo.tgfn.interestshare.AggIntshareHead;
import nc.itf.tg.IInterestShareMaintain;

public class N_FN24_SAVEBASE extends AbstractPfAction<AggIntshareHead> {

	@Override
	protected CompareAroundProcesser<AggIntshareHead> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggIntshareHead> processor = null;
		AggIntshareHead[] clientFullVOs = (AggIntshareHead[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggIntshareHead>(
					InterestSharePluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggIntshareHead>(
					InterestSharePluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggIntshareHead> rule = null;

		return processor;
	}

	@Override
	protected AggIntshareHead[] processBP(Object userObj,
			AggIntshareHead[] clientFullVOs, AggIntshareHead[] originBills) {

		AggIntshareHead[] bills = null;
		try {
			IInterestShareMaintain operator = NCLocator.getInstance()
					.lookup(IInterestShareMaintain.class);
			if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
					.getPrimaryKey())) {
				bills = operator.update(clientFullVOs, originBills);
			} else {
				bills = operator.insert(clientFullVOs, originBills);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}
}
