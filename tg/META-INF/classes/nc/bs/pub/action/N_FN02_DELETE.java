package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;

import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.itf.tg.IContractApportionmentMaintain;

public class N_FN02_DELETE extends AbstractPfAction<AggContractAptmentVO> {

	@Override
	protected CompareAroundProcesser<AggContractAptmentVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggContractAptmentVO> processor = new CompareAroundProcesser<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggContractAptmentVO[] processBP(Object userObj,
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills) {
		IContractApportionmentMaintain operator = NCLocator.getInstance().lookup(
				IContractApportionmentMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
