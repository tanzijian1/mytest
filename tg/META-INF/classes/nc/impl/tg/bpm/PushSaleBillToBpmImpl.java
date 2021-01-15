package nc.impl.tg.bpm;

import nc.bs.tg.outside.salebpm.push.AddBillPushtoBpmUtil;
import nc.bs.tg.outside.salebpm.push.AdjustBillPushToBpmUtil;
import nc.bs.tg.outside.salebpm.push.BxbillPushtoBpmUtils;
import nc.bs.tg.outside.salebpm.push.SaleBankCommissionUtils;
import nc.bs.tg.outside.salebpm.push.SaleChargePayUtils;
import nc.bs.tg.outside.salebpm.push.SaleInsideAllotPayBillUtils;
import nc.bs.tg.outside.salebpm.push.SaleUnitePayBillUtils;
import nc.bs.tg.outside.salebpm.push.SaleUniteRePayBillUtils;
import nc.itf.tg.bpm.IPushSaleBillToBpm;
import nc.itf.tg.outside.ISaleBPMBillCont;
import nc.vo.ep.bx.JKBXVO;
import nc.vo.pub.BusinessException;
import nc.vo.pubapp.pattern.model.entity.bill.AbstractBill;

public class PushSaleBillToBpmImpl implements IPushSaleBillToBpm {

	@Override
	public AbstractBill pushBillToBPM(String billCode, AbstractBill bill)
			throws BusinessException {
		try {
			if (ISaleBPMBillCont.F3_Cxx_011.equals(billCode)) {// 内部资金调拨单
				bill = SaleInsideAllotPayBillUtils.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.F3_Cxx_012.equals(billCode)){// 税费缴纳申请单
				bill = SaleChargePayUtils.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.F3_Cxx_016.equals(billCode)){// 统借统还-内部放款单
				bill = SaleUnitePayBillUtils.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.F3_Cxx_017.equals(billCode)){// 统借统还-内部还款单
				bill = SaleUniteRePayBillUtils.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.F3_Cxx_027.equals(billCode)){// 银行手续费单
				bill = SaleBankCommissionUtils.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.D267X_Cxx_001.equals(billCode)){//补票工单
				bill = AddBillPushtoBpmUtil.getUtils().onPushBillToBPM(billCode, bill);
			}else if(ISaleBPMBillCont.BILL_22.equals(billCode) || ISaleBPMBillCont.BILL_23.equals(billCode)){//预算调整单和预算调剂单
				bill = AdjustBillPushToBpmUtil.getUtils().onPushBillToBPM(billCode, bill);
			}
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), e);
		}

		return bill;
	}

	@Override
	public JKBXVO pushBXBillToBPM(JKBXVO vo) throws BusinessException {
		// TODO 自动生成的方法存根
		vo = BxbillPushtoBpmUtils.getUtils().onPushBillToBPM(vo);
		return vo;
	}

}
