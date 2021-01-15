package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.itf.tg.IRenameChangeBillMaintain;

public class N_FN18_UNSAVEBILL extends AbstractPfAction<AggRenameChangeBillHVO> {

	@Override
	protected CompareAroundProcesser<AggRenameChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRenameChangeBillHVO> processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggRenameChangeBillHVO[] processBP(Object userObj,
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills) {
		IRenameChangeBillMaintain operator = NCLocator.getInstance().lookup(
				IRenameChangeBillMaintain.class);
		AggRenameChangeBillHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
