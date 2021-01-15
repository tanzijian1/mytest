package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.mortgagedetail.plugin.bpplugin.MortgagedetailPluginPoint;
import nc.vo.tg.mortgagedetail.AggMortgageDetalVO;
import nc.itf.tg.IMortgagedetailMaintain;

public class N_SD05_UNSAVEBILL extends AbstractPfAction<AggMortgageDetalVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageDetalVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageDetalVO> processor = new CompareAroundProcesser<AggMortgageDetalVO>(
				MortgagedetailPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggMortgageDetalVO[] processBP(Object userObj,
			AggMortgageDetalVO[] clientFullVOs, AggMortgageDetalVO[] originBills) {
		IMortgagedetailMaintain operator = NCLocator.getInstance().lookup(
				IMortgagedetailMaintain.class);
		AggMortgageDetalVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
