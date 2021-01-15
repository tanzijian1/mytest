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
					|| IBPMBillCont.BILL_RZ06_02.equals(billCode)) {// �ƹ˷�/���ʹ���
				bill = FinancexpenseUtils.getUtils().onPushBillToBPM(billCode,
						bill);
			} else if (IBPMBillCont.BILL_36FF_01.equals(billCode)
					|| IBPMBillCont.BILL_36FF_02.equals(billCode)) {// �ʽ����л�������/�ʽ����ĳ���������Ϣ����
				bill = RePayReceiptBankCreditUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			} else if (IBPMBillCont.BILL_36FA.equals(billCode)) {// �ʽ����Ĵ����ͬ������������ࣩ
				bill = ApplyBankCreditUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			} else if (IBPMBillCont.BILL_RZ04.equals(billCode)) {// ����Э��
				bill = MortgageAgreementUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			}else if (ISaleBPMBillCont.BILL_19.equals(billCode)) {//��Ʊ��
				bill = AddTicketUtils.getUtils().onPushBillToBPM(
						billCode, bill);
			}else if(IBPMBillCont.BILL_SD08.equals(billCode)){//�ʽ����л����������ʽ�ҵ��
				bill = CapitalMarketRepayUtils.getUtils().onPushBillToBPM(billCode, bill);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return bill;
	}

}
