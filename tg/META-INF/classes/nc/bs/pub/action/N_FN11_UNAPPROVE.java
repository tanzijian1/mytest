package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.bs.tg.pub.rule.UnApproveDeleteGLRule;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;
import nc.itf.tg.IChangeBillMaintain;

public class N_FN11_UNAPPROVE extends AbstractPfAction<AggChangeBillHVO> {

	@Override
	protected CompareAroundProcesser<AggChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggChangeBillHVO> processor = new CompareAroundProcesser<AggChangeBillHVO>(
				ChangeBillPluginPoint.UNAPPROVE);
		// TODO �ڴ˴����ǰ�����
		processor.addBeforeRule(new UnapproveStatusCheckRule());
		processor.addBeforeRule(new UnApproveDeleteGLRule());
		return processor;
	}

	@Override
	protected AggChangeBillHVO[] processBP(Object userObj,
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggChangeBillHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IChangeBillMaintain operator = NCLocator.getInstance()
					.lookup(IChangeBillMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			//2019��9��2��11:11:19 ln ɾ��ƾ֤ ====start===={
			for(AggChangeBillHVO bill : bills){
				util.delVoucher(bill);
			}
			// ====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
