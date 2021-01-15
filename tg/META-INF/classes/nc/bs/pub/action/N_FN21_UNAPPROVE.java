package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;

public class N_FN21_UNAPPROVE extends AbstractPfAction<AggAbTaxTransferHVO> {

	@Override
	protected CompareAroundProcesser<AggAbTaxTransferHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAbTaxTransferHVO> processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggAbTaxTransferHVO[] processBP(Object userObj,
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggAbTaxTransferHVO[] bills = null;
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			IAbnormalTaxTransferMaintain operator = NCLocator.getInstance()
					.lookup(IAbnormalTaxTransferMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
			for(AggAbTaxTransferHVO bill : bills){
				util.delVoucher(bill);
			}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
