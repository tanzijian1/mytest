package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;

public class N_FN13_UNAPPROVE extends AbstractPfAction<AggDistribution> {

	@Override
	protected CompareAroundProcesser<AggDistribution> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDistribution> processor = new CompareAroundProcesser<AggDistribution>(
				DistributionPluginPoint.UNAPPROVE);
		// TODO �ڴ˴����ǰ�����
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggDistribution[] processBP(Object userObj,
			AggDistribution[] clientFullVOs, AggDistribution[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggDistribution[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IDistributionMaintain operator = NCLocator.getInstance()
					.lookup(IDistributionMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019��9��2��11:11:19 ln ɾ��ƾ֤ ====start===={
			for(AggDistribution bill : bills){
				util.delVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
