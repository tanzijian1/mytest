package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;

public class N_RZ04_DELETE extends AbstractPfAction<AggMortgageAgreementVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggMortgageAgreementVO[] processBP(Object userObj,
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills) {
		ITGRZ_MortgageAgreementMaintain operator = NCLocator.getInstance().lookup(
				ITGRZ_MortgageAgreementMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
