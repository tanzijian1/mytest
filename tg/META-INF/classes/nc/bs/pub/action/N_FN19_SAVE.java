package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;

public class N_FN19_SAVE extends AbstractPfAction<AggExhousetransferbillHVO> {

	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggExhousetransferbillHVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {
		IExHouseTransferBillMaintain operator = NCLocator.getInstance().lookup(
				IExHouseTransferBillMaintain.class);
		AggExhousetransferbillHVO[] bills = null;
		try {
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
