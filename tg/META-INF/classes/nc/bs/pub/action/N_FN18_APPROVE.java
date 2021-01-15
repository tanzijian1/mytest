package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.renamechangebill.plugin.bpplugin.RenameChangeBillPluginPoint;
import nc.vo.tgfn.exhousetransferbill.AggExhousetransferbillHVO;
import nc.vo.tgfn.renamechangebill.AggRenameChangeBillHVO;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.itf.tg.IRenameChangeBillMaintain;

public class N_FN18_APPROVE extends AbstractPfAction<AggRenameChangeBillHVO> {

	public N_FN18_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggRenameChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggRenameChangeBillHVO> processor = new CompareAroundProcesser<AggRenameChangeBillHVO>(
				RenameChangeBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggRenameChangeBillHVO[] processBP(Object userObj,
			AggRenameChangeBillHVO[] clientFullVOs, AggRenameChangeBillHVO[] originBills) {
		AggRenameChangeBillHVO[] bills = null;
		IRenameChangeBillMaintain operator = NCLocator.getInstance().lookup(
				IRenameChangeBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			for(AggRenameChangeBillHVO bill : bills){
				util.addVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
