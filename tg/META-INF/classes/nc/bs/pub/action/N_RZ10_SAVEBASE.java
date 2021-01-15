package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.organization.plugin.bpplugin.OrganizationPluginPoint;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IOrganizationMaintain;
import nc.vo.jcom.lang.StringUtil;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.AggOrganizationVO;

public class N_RZ10_SAVEBASE extends AbstractPfAction<AggOrganizationVO> {

	@Override
	protected CompareAroundProcesser<AggOrganizationVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOrganizationVO> processor = null;
		AggOrganizationVO[] clientFullVOs = (AggOrganizationVO[]) this.getVos();
		if (!StringUtil.isEmptyWithTrim(clientFullVOs[0].getParentVO()
				.getPrimaryKey())) {
			processor = new CompareAroundProcesser<AggOrganizationVO>(
					OrganizationPluginPoint.SCRIPT_UPDATE);
		} else {
			processor = new CompareAroundProcesser<AggOrganizationVO>(
					OrganizationPluginPoint.SCRIPT_INSERT);
		}
		// TODO 在此处添加前后规则
		IRule<AggOrganizationVO> rule = null;

		return processor;
	}

	@Override
	protected AggOrganizationVO[] processBP(Object userObj,
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills) {

		AggOrganizationVO[] bills = null;
		try {
			IOrganizationMaintain operator = NCLocator.getInstance()
					.lookup(IOrganizationMaintain.class);
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
