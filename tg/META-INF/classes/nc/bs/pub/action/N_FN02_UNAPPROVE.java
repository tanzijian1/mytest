package nc.bs.pub.action;

import nc.bs.framework.common.NCLocator;
import nc.bs.pubapp.pf.action.AbstractPfAction;
import nc.bs.pubapp.pub.rule.UnapproveStatusCheckRule;
import nc.impl.pubapp.pattern.rule.processer.CompareAroundProcesser;
import nc.vo.pub.BusinessException;
import nc.vo.pub.VOStatus;
import nc.vo.pubapp.pattern.exception.ExceptionUtils;
import nc.bs.tg.contractapportionment.plugin.bpplugin.ContractApportionmentPluginPoint;
import nc.bs.tg.pub.voucher.SendVoucher;
import nc.vo.tg.contractapportionment.AggContractAptmentVO;
import nc.vo.tg.contractapportionment.ContractAptmentVO;
import nc.itf.tg.IContractApportionmentMaintain;

public class N_FN02_UNAPPROVE extends AbstractPfAction<AggContractAptmentVO> {

	@Override
	protected CompareAroundProcesser<AggContractAptmentVO> getCompareAroundProcesserWithRules(
			Object userObj) {
		CompareAroundProcesser<AggContractAptmentVO> processor = new CompareAroundProcesser<AggContractAptmentVO>(
				ContractApportionmentPluginPoint.UNAPPROVE);
		// TODO 在此处添加前后规则
		processor.addBeforeRule(new UnapproveStatusCheckRule());

		return processor;
	}

	@Override
	protected AggContractAptmentVO[] processBP(Object userObj,
			AggContractAptmentVO[] clientFullVOs, AggContractAptmentVO[] originBills) {
		for (int i = 0; clientFullVOs != null && i < clientFullVOs.length; i++) {
			clientFullVOs[i].getParentVO().setStatus(VOStatus.UPDATED);
		}
		AggContractAptmentVO[] bills = null;
		try {
			//2019年9月2日11:11:19 ln 删除凭证 ====start===={
			SendVoucher voucher = new SendVoucher();
			SendVoucherUtil util = new SendVoucherUtil();
			for(AggContractAptmentVO aggvo:clientFullVOs){
//				ContractAptmentVO hvo = aggvo.getParentVO();
//				voucher.setAggVO(aggvo);
//				voucher.setBilltype(hvo.getBilltype());
//				voucher.setBillcode(hvo.getBillno());
//				voucher.setEffectdate(hvo.getDbilldate());
//				voucher.setPk_group(hvo.getPk_group());
//				voucher.setPk_org(hvo.getPk_org());
//				voucher.setNote("");
//				voucher.setMny(hvo.getConmoney());
//				voucher.setPk_currency("");
//				voucher.setId(hvo.getPrimaryKey());
//				voucher.sendDAPDelMessge(voucher.getMessageVO());
				util.delVoucher(aggvo);
			}
			// ====end=====}
			IContractApportionmentMaintain operator = NCLocator.getInstance()
					.lookup(IContractApportionmentMaintain.class);
			bills = operator.unapprove(clientFullVOs, originBills);
		} catch (BusinessException e) {
			ExceptionUtils.wrappBusinessException(e.getMessage());
		}
		return bills;
	}

}
