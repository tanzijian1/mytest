package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.UnApproveRule;
import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.itf.tg.ICapitalmarketrepayMaintain;

public class N_SD08_UNAPPROVE extends AbstractPfAction<AggMarketRepalayVO> {

	@Override
	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = new CompareAroundProcesser<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveRule());

		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggMarketRepalayVO[] bills = null;
		try {
			ICapitalmarketrepayMaintain operator = NCLocator.getInstance()
					.lookup(ICapitalmarketrepayMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
