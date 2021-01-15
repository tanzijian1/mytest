package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;

public class N_FN19_UNSAVEBILL extends AbstractPfAction<AggExhousetransferbillHVO> {

	@Override
	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {
		IExHouseTransferBillMaintain operator = NCLocator.getInstance().lookup(
				IExHouseTransferBillMaintain.class);
		AggExhousetransferbillHVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
