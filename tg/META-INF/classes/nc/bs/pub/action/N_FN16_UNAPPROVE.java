package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.bs.tg.tartingbill.rule.TartingUnApproveDelGL;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.ITartingBillMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tg.tartingbill.AggTartingBillVO;

public class N_FN16_UNAPPROVE extends AbstractPfAction<AggTartingBillVO> {

	@Override
	protected CompareAroundProcesser<AggTartingBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTartingBillVO> processor = new CompareAroundProcesser<AggTartingBillVO>(
				TartingBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
        processor.addBeforeRule(new TartingUnApproveDelGL());
		return processor;
	}

	@Override
	protected AggTartingBillVO[] processBP(Object userObj,
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggTartingBillVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			ITartingBillMaintain operator = NCLocator.getInstance()
					.lookup(ITartingBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggTartingBillVO bill : bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
