package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;
import nc.itf.tg.ICapitalmarketrepayMaintain;

public class N_SD08_DELETE extends AbstractPfAction<AggMarketRepalayVO> {

	@Override
	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = new CompareAroundProcesser<AggMarketRepalayVO>(
				CapitalmarketrepayPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {
		ICapitalmarketrepayMaintain operator = NCLocator.getInstance().lookup(
				ICapitalmarketrepayMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
