package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.CommitStatusCheckRule;
import nc.bs.tg.tgrz_mortgageagreement.plugin.bpplugin.TGRZ_MortgageAgreementPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ITGRZ_MortgageAgreementMaintain;
import nc.itf.uap.busibean.ISysInitQry;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.tgrz_mortgageagreement.AggMortgageAgreementVO;

public class N_RZ04_SAVE extends AbstractPfAction<AggMortgageAgreementVO> {

	protected CompareAroundProcesser<AggMortgageAgreementVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageAgreementVO> processor = new CompareAroundProcesser<AggMortgageAgreementVO>(
				TGRZ_MortgageAgreementPluginPoint.SEND_APPROVE);
		// TODO 在此处添加审核前后规则
		IRule<AggMortgageAgreementVO> rule = new CommitStatusCheckRule();
		processor.addBeforeRule(rule);
		 //获取全局参数
	    ISysInitQry SysInitQry = NCLocator.getInstance().lookup(ISysInitQry.class);
	    String para = null;
	    try {
			para = SysInitQry.getParaString(nc.itf.org.IOrgConst.GLOBEORG, "TGRZ02");
		} catch (BusinessException e) {
			Logger.error(e.getMessage(), e);
			nc.vo.pubapp.pattern.exception.ExceptionUtils
					.wrappBusinessException(e.getMessage());
		}
	    if ("Y".equals(para)) {
		rule = new nc.bs.tg.rule.SendBPMBillRule();
		processor.addBeforeRule(rule);
		}
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
			bills = operator.save(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
