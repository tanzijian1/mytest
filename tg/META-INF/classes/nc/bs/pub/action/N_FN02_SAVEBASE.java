package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.contractapportionment.ace.rule.InsertDataRule;
import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IContractApportionmentMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;

public class N_FN02_SAVEBASE extends AbstractPfAction<AggContractAptmentVO> {

	@Override
	protected CompareAroundProcesser<AggContractAptmentVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggContractAptmentVO> processor = null;
		AggContractAptmentVO[] clientFullVOs = (AggContractAptmentVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggContractAptmentVO>(
					ContractApportionmentPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggContractAptmentVO>(
					ContractApportionmentPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggContractAptmentVO> rule = null;
		

		return processor;
	}

	@Override
	protected AggContractAptmentVO[] processBP(Object userObj,
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills) {

		AggContractAptmentVO[] bills = null;
		try {
			IContractApportionmentMaintain operator = NCLocator.getInstance()
					.lookup(IContractApportionmentMaintain.class);
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
