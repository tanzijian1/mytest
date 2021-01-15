package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.itf.tg.IExHouseTransferBillMaintain;

public class N_FN19_APPROVE extends AbstractPfAction<AggExhousetransferbillHVO> {

	public N_FN19_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {
		AggExhousetransferbillHVO[] bills = null;
		IExHouseTransferBillMaintain operator = NCLocator.getInstance().lookup(
				IExHouseTransferBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			for(AggExhousetransferbillHVO bill : bills){
				util.addVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
