package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.bs.tg.financingexpense.rule.UnsaveBpmidRule;
import nc.bs.tg.financingexpense.rule.finUnsaveDelBarcodeRule;
import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

public class N_RZ04_UNSAVEBILL extends AbstractPfAction<AggMortgageAgreementVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());
		//清除bpmid
		processor.addBeforeRule(new UnsaveBpmidRule());
		// processor.addAfterRule(new UnSaveBillRule());

		return processor;
	}

	@Override
	protected AggMortgageAgreementVO[] processBP(Object userObj,
			AggMortgageAgreementVO[] clientFullVOs,
			AggMortgageAgreementVO[] originBills) {
		ITGRZ_MortgageAgreementMaintain operator = NCLocator.getInstance()
				.lookup(ITGRZ_MortgageAgreementMaintain.class);
		AggMortgageAgreementVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
