package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.itf.tg.IRenameChangeBillMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;

public class N_FN18_UNAPPROVE extends AbstractPfAction<AggRenameChangeBillHVO> {

	@Override
	protected CompareAroundProcesser<AggRenameChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRenameChangeBillHVO> processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggRenameChangeBillHVO[] processBP(Object userObj,
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggRenameChangeBillHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IRenameChangeBillMaintain operator = NCLocator.getInstance()
					.lookup(IRenameChangeBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggRenameChangeBillHVO bill : bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
