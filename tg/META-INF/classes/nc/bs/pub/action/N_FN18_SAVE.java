package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.itf.tg.IRenameChangeBillMaintain;

public class N_FN18_SAVE extends AbstractPfAction<AggRenameChangeBillHVO> {

	protected CompareAroundProcesser<AggRenameChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRenameChangeBillHVO> processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggRenameChangeBillHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggRenameChangeBillHVO[] processBP(Object userObj,
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills) {
		IRenameChangeBillMaintain operator = NCLocator.getInstance().lookup(
				IRenameChangeBillMaintain.class);
		AggRenameChangeBillHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
