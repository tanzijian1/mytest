package nc.impl.tg.bpm;

import nc.bs.tg.outside.bpm.push.AddTicketUtils;
import nc.bs.tg.outside.bpm.push.ApplyBankCreditUtils;
import nc.bs.tg.outside.bpm.push.CapitalMarketRepayUtils;
import nc.bs.tg.outside.bpm.push.FinancexpenseUtils;
import nc.bs.tg.outside.bpm.push.MortgageAgreementUtils;
import nc.bs.tg.outside.bpm.push.RePayReceiptBankCreditUtils;
import nc.itf.tg.bpm.IPushBillToBpm;
import nc.itf.tg.outside.IBPMBillCont;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class PushBillToBpmImpl implements IPushBillToBpm {

	@Override
	public AbstractBill pushBillToBPM(String billCode, AbstractBill bill)
			throws BusinessException {
		try {
			if (IBPMBillCont.BILL_RZ06_01.equals(billCode)
					|| IBPMBillCont.BILL_RZ06_02.equals(billCode)) {// 财顾费/融资管理
				bill = FinancexpenseUtils.getUtils().onPushBillToBPM(billCode,
						bill);
			} else if (IBPMBillCont.BILL_36FF_01.equals(billCode)
					|| IBPMBillCont.BILL_36FF_02.equals(billCode)) {// 资金部银行还款审批/资金中心偿还贷款利息审批
				bill = RePayReceiptBankCreditUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			} else if (IBPMBillCont.BILL_36FA.equals(billCode)) {// 资金中心贷款合同审批（非理财类）
				bill = ApplyBankCreditUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			} else if (IBPMBillCont.BILL_RZ04.equals(billCode)) {// 按揭协议
				bill = MortgageAgreementUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			}else if (ISaleBPMBillCont.BILL_19.equals(billCode)) {//补票单
				bill = AddTicketUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			}else if(IBPMBillCont.BILL_SD08.equals(billCode)){//资金部银行还款审批（资金业务）
				bill = CapitalMarketRepayUtils.getUtils().onPushBillToBPM(billCode, bill);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return bill;
	}

}
