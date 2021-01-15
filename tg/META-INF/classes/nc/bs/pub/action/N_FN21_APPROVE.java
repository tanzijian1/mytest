package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.abnormaltaxtransfer.plugin.bpplugin.AbnormalTaxTransferPluginPoint;
import nc.vo.tgfn.abnormaltaxtransfer.AggAbTaxTransferHVO;
import nc.vo.tgfn.transferbill.AggTransferBillHVO;
import nc.itf.tg.IAbnormalTaxTransferMaintain;

public class N_FN21_APPROVE extends AbstractPfAction<AggAbTaxTransferHVO> {

	public N_FN21_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggAbTaxTransferHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggAbTaxTransferHVO> processor = new CompareAroundProcesser<AggAbTaxTransferHVO>(
				AbnormalTaxTransferPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggAbTaxTransferHVO[] processBP(Object userObj,
			AggAbTaxTransferHVO[] clientFullVOs, AggAbTaxTransferHVO[] originBills) {
		AggAbTaxTransferHVO[] bills = null;
		IAbnormalTaxTransferMaintain operator = NCLocator.getInstance().lookup(
				IAbnormalTaxTransferMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggAbTaxTransferHVO bill : bills){
				util.addVoucher(bill);
			}
			//===end===}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
