package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.changebill.plugin.bpplugin.ChangeBillPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;
import nc.vo.tgfn.changebill.AggChangeBillHVO;
import nc.vo.tgfn.changebill.ChangeBillHVO;
import nc.vo.tgfn.costaccruebill.AggCostAccrueBill;
import nc.itf.tg.IChangeBillMaintain;

public class N_FN11_APPROVE extends AbstractPfAction<AggChangeBillHVO> {

	public N_FN11_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggChangeBillHVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggChangeBillHVO> processor = new CompareAroundProcesser<AggChangeBillHVO>(
				ChangeBillPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggChangeBillHVO[] processBP(Object userObj,
			AggChangeBillHVO[] clientFullVOs, AggChangeBillHVO[] originBills) {
		AggChangeBillHVO[] bills = null;
		IChangeBillMaintain operator = NCLocator.getInstance().lookup(
				IChangeBillMaintain.class);
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggChangeBillHVO bill : bills){
				util.addVoucher(bill);
			}
			//=====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
