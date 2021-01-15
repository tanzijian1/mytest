package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.tg.organization.plugin.bpplugin.OrganizationPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IOrganizationMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.organization.AggOrganizationVO;

public class N_RZ10_DELETE extends AbstractPfAction<AggOrganizationVO> {

	@Override
	protected CompareAroundProcesser<AggOrganizationVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggOrganizationVO> processor = new CompareAroundProcesser<AggOrganizationVO>(
				OrganizationPluginPoint.SCRIPT_DELETE);
		// TODO 在此处添加前后规则
		return processor;
	}

	@Override
	protected AggOrganizationVO[] processBP(Object userObj,
			AggOrganizationVO[] clientFullVOs, AggOrganizationVO[] originBills) {
		IOrganizationMaintain operator = NCLocator.getInstance().lookup(
				IOrganizationMaintain.class);
		try {
			operator.delete(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return clientFullVOs;
	}

}
