package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.IsClearBPMIDRule;
import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class N_SD08_UNSAVEBILL extends AbstractPfAction<AggMarketRepalayVO> {

	@Override
	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = new CompareAroundProcesser<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());
		IsClearBPMIDRule rule = new IsClearBPMIDRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {
		ICapitalmarketrepayMaintain operator = NCLocator.getInstance().lookup(
				ICapitalmarketrepayMaintain.class);
		AggMarketRepalayVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
