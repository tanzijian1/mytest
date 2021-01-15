package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.standard.plugin.bpplugin.StandardPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IStandardMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.standard.AggStandardVO;

public class N_RZ01_SAVEBASE extends AbstractPfAction<AggStandardVO> {

	@Override
	protected CompareAroundProcesser<AggStandardVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggStandardVO> processor = null;
		AggStandardVO[] clientFullVOs = (AggStandardVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggStandardVO>(
					StandardPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggStandardVO>(
					StandardPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggStandardVO> rule = null;

		return processor;
	}

	@Override
	protected AggStandardVO[] processBP(Object userObj,
			AggStandardVO[] clientFullVOs, AggStandardVO[] originBills) {

		AggStandardVO[] bills = null;
		try {
			IStandardMaintain operator = NCLocator.getInstance().lookup(
					IStandardMaintain.class);
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
