package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.mortgagedetail.plugin.bpplugin.MortgagedetailPluginPoint;
import nc.vo.tg.mortgagedetail.AggMortgageDetalVO;
import nc.itf.tg.IMortgagedetailMaintain;

public class N_SD05_SAVEBASE extends AbstractPfAction<AggMortgageDetalVO> {

	@Override
	protected CompareAroundProcesser<AggMortgageDetalVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggMortgageDetalVO> processor = null;
		AggMortgageDetalVO[] clientFullVOs = (AggMortgageDetalVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggMortgageDetalVO>(
					MortgagedetailPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggMortgageDetalVO>(
					MortgagedetailPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggMortgageDetalVO> rule = null;

		return processor;
	}

	@Override
	protected AggMortgageDetalVO[] processBP(Object userObj,
			AggMortgageDetalVO[] clientFullVOs, AggMortgageDetalVO[] originBills) {

		AggMortgageDetalVO[] bills = null;
		try {
			IMortgagedetailMaintain operator = NCLocator.getInstance()
					.lookup(IMortgagedetailMaintain.class);
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
