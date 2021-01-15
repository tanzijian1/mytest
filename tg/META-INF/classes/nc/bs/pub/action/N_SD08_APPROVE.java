package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.ApproveBeforeRule;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.WriteBackRule;
import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class N_SD08_APPROVE extends AbstractPfAction<AggMarketRepalayVO> {

	public N_SD08_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = new CompareAroundProcesser<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		processor.addBeforeRule(new ApproveBeforeRule());
		processor.addAfterRule(new WriteBackRule());
		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {
		AggMarketRepalayVO[] bills = null;
		ICapitalmarketrepayMaintain operator = NCLocator.getInstance().lookup(
				ICapitalmarketrepayMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
