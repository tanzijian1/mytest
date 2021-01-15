package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.capitalmarketrepay.ace.bp.rule.SaveBaseCheckRule;
import nc.bs.tg.capitalmarketrepay.plugin.bpplugin.CapitalmarketrepayPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ICapitalmarketrepayMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.capitalmarketrepay.AggMarketRepalayVO;

public class N_SD08_SAVEBASE extends AbstractPfAction<AggMarketRepalayVO> {

	@Override
	protected CompareAroundProcesser<AggMarketRepalayVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMarketRepalayVO> processor = null;
		AggMarketRepalayVO[] clientFullVOs = (AggMarketRepalayVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggMarketRepalayVO>(
					CapitalmarketrepayPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggMarketRepalayVO>(
					CapitalmarketrepayPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggMarketRepalayVO> rule = new SaveBaseCheckRule();
		processor.addBeforeRule(rule);
		return processor;
	}

	@Override
	protected AggMarketRepalayVO[] processBP(Object userObj,
			AggMarketRepalayVO[] clientFullVOs, AggMarketRepalayVO[] originBills) {

		AggMarketRepalayVO[] bills = null;
		try {
			ICapitalmarketrepayMaintain operator = NCLocator.getInstance()
					.lookup(ICapitalmarketrepayMaintain.class);
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
