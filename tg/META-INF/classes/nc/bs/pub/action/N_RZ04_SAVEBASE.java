package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;

public class N_RZ04_SAVEBASE extends AbstractPfAction<AggMortgageAgreementVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = null;
		AggMortgageAgreementVO[] clientFullVOs = (AggMortgageAgreementVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
					TGRZ_MortgageAgreementPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
					TGRZ_MortgageAgreementPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggMortgageAgreementVO> rule = null;

		return processor;
	}

	@Override
	protected AggMortgageAgreementVO[] processBP(Object userObj,
			AggMortgageAgreementVO[] clientFullVOs, AggMortgageAgreementVO[] originBills) {

		AggMortgageAgreementVO[] bills = null;
		try {
			ITGRZ_MortgageAgreementMaintain operator = NCLocator.getInstance()
					.lookup(ITGRZ_MortgageAgreementMaintain.class);
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
