package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.ApproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.fip.service.FipMessageVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.itf.tg.IContractApportionmentMaintain;

public class N_FN02_APPROVE extends AbstractPfAction<AggContractAptmentVO> {

	public N_FN02_APPROVE() {
		super();
	}

	@Override
	protected CompareAroundProcesser<AggContractAptmentVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggContractAptmentVO> processor = new CompareAroundProcesser<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.APPROVE);
		processor.addBeforeRule(new ApproveStatusCheckRule());
		return processor;
	}

	@Override
	protected AggContractAptmentVO[] processBP(Object userObj,
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills) {
		AggContractAptmentVO[] bills = null;
		IContractApportionmentMaintain operator = NCLocator.getInstance().lookup(
				IContractApportionmentMaintain.class);
		SendVoucher voucher = new SendVoucher();
		SendVoucherUtil util = new SendVoucherUtil();
		try {
			bills = operator.approve(clientFullVOs, originBills);
			
			//2019年9月2日11:11:19 ln 生成凭证 ====start===={
			for(AggContractAptmentVO bill : bills){
//				ContractAptmentVO parentVO = bill.getParentVO();
//				voucher.setAggVO(bill);
//				voucher.setBilltype(parentVO.getBilltype());
//				voucher.setBillcode(parentVO.getBillno());
//				voucher.setEffectdate(parentVO.getDbilldate());
//				voucher.setPk_group(parentVO.getPk_group());
//				voucher.setPk_org(parentVO.getPk_org());
//				voucher.setNote("");
//				voucher.setMny(parentVO.getConmoney());
//				voucher.setPk_currency("");
//				voucher.setId(parentVO.getPrimaryKey());
//				voucher.sendDAPAddMessge(voucher.getMessageVO());
				util.addVoucher(bill);
			}
			//====end=====}
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
