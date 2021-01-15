package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.exhousetransferbill.plugin.bpplugin.ExHouseTransferBillPluginPoint;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IExHouseTransferBillMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;

public class N_FN19_UNAPPROVE extends AbstractPfAction<AggExhousetransferbillHVO> {

	@Override
	protected CompareAroundProcesser<AggExhousetransferbillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggExhousetransferbillHVO> processor = new CompareAroundProcesser<AggExhousetransferbillHVO>(
				ExHouseTransferBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggExhousetransferbillHVO[] processBP(Object userObj,
			AggExhousetransferbillHVO[] clientFullVOs, AggExhousetransferbillHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggExhousetransferbillHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			
			IExHouseTransferBillMaintain operator = NCLocator.getInstance()
					.lookup(IExHouseTransferBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggExhousetransferbillHVO bill :bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
