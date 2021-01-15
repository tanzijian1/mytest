package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UncommitStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.itf.tg.IContractApportionmentMaintain;

public class N_FN02_UNSAVEBILL extends AbstractPfAction<AggContractAptmentVO> {

	@Override
	protected CompareAroundProcesser<AggContractAptmentVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggContractAptmentVO> processor = new CompareAroundProcesser<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.UNSEND_APPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UncommitStatusCheckRule());

		return processor;
	}

	@Override
	protected AggContractAptmentVO[] processBP(Object userObj,
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills) {
		IContractApportionmentMaintain operator = NCLocator.getInstance().lookup(
				IContractApportionmentMaintain.class);
		AggContractAptmentVO[] bills = null;
		try {
			bills = operator.unsave(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
