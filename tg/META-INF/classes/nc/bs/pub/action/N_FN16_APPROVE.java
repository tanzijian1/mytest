package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.tartingbill.plugin.bpplugin.TartingBillPluginPoint;
import nc.vo.tg.tartingbill.AggTartingBillVO;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.ITartingBillMaintain;

public class N_FN16_APPROVE extends AbstractPfAction<AggTartingBillVO> {

	public N_FN16_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggTartingBillVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggTartingBillVO> processor = new CompareAroundProcesser<AggTartingBillVO>(
				TartingBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggTartingBillVO[] processBP(Object userObj,
			AggTartingBillVO[] clientFullVOs, AggTartingBillVO[] originBills) {
		AggTartingBillVO[] bills = null;
		ITartingBillMaintain operator = NCLocator.getInstance().lookup(
				ITartingBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			for(AggTartingBillVO bill : bills){
				util.addVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
