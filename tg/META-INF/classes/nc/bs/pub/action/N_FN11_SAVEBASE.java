package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.itf.tg.IChangeBillMaintain;

public class N_FN11_SAVEBASE extends AbstractPfAction<AggChangeBillHVO> {

	@Override
	protected CompareAroundProcesser<AggChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggChangeBillHVO> processor = null;
		AggChangeBillHVO[] clientFullVOs = (AggChangeBillHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggChangeBillHVO>(
					ChangeBillPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggChangeBillHVO>(
					ChangeBillPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggChangeBillHVO> rule = null;

		return processor;
	}

	@Override
	protected AggChangeBillHVO[] processBP(Object userObj,
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills) {

		AggChangeBillHVO[] bills = null;
		try {
			IChangeBillMaintain operator = NCLocator.getInstance()
					.lookup(IChangeBillMaintain.class);
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
