package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;

public class N_RZ04_APPROVE extends AbstractPfAction<AggMortgageAgreementVO> {

	public N_RZ04_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggMortgageAgreementVO[] processBP(Object userObj,
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills) {
		AggMortgageAgreementVO[] bills = null;
		ITGRZ_MortgageAgreementMaintain operator = NCLocator.getInstance().lookup(
				ITGRZ_MortgageAgreementMaintain.class);
		try {
			bills = operator.approve(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
