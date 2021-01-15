package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.itf.tg.IRenameChangeBillMaintain;

public class N_FN18_SAVEBASE extends AbstractPfAction<AggRenameChangeBillHVO> {

	@Override
	protected CompareAroundProcesser<AggRenameChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRenameChangeBillHVO> processor = null;
		AggRenameChangeBillHVO[] clientFullVOs = (AggRenameChangeBillHVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
					RenameChangeBillPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
					RenameChangeBillPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggRenameChangeBillHVO> rule = null;

		return processor;
	}

	@Override
	protected AggRenameChangeBillHVO[] processBP(Object userObj,
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills) {

		AggRenameChangeBillHVO[] bills = null;
		try {
			IRenameChangeBillMaintain operator = NCLocator.getInstance()
					.lookup(IRenameChangeBillMaintain.class);
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
