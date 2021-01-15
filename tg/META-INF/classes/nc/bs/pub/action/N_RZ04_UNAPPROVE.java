package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.bs.tg.tgrz_mortgageagreement.rule.UnSaveBillRule;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;

public class N_RZ04_UNAPPROVE extends AbstractPfAction<AggMortgageAgreementVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addAfterRule(new UnSaveBillRule());

		return processor;
	}

	@Override
	protected AggMortgageAgreementVO[] processBP(Object userObj,
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggMortgageAgreementVO[] bills = null;
		try {
			ITGRZ_MortgageAgreementMaintain operator = NCLocator.getInstance()
					.lookup(ITGRZ_MortgageAgreementMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
