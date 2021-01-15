package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.itf.tg.IChangeBillMaintain;

public class N_FN11_SAVE extends AbstractPfAction<AggChangeBillHVO> {

	protected CompareAroundProcesser<AggChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggChangeBillHVO> processor = new CompareAroundProcesser<AggChangeBillHVO>(
				ChangeBillPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggChangeBillHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggChangeBillHVO[] processBP(Object userObj,
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills) {
		IChangeBillMaintain operator = NCLocator.getInstance().lookup(
				IChangeBillMaintain.class);
		AggChangeBillHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
