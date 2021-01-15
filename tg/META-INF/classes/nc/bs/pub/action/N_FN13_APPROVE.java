package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.distribution.plugin.bpplugin.DistributionPluginPoint;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.itf.tg.IDistributionMaintain;

public class N_FN13_APPROVE extends AbstractPfAction<AggDistribution> {

	public N_FN13_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggDistribution> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggDistribution> processor = new CompareAroundProcesser<AggDistribution>(
				DistributionPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggDistribution[] processBP(Object userObj,
			AggDistribution[] clientFullVOs, AggDistribution[] originBills) {
		AggDistribution[] bills = null;
		IDistributionMaintain operator = NCLocator.getInstance().lookup(
				IDistributionMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggDistribution bill : bills){
				util.addVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
