package nc.impl.tg.outside;

import java.util.HashMap;

import nc.bs.tg.outside.sale.utils.distribution.DisrtibutionUtils;
import nc.bs.tg.outside.sale.utils.exhousetransfer.ExHouseTransferBillUtils;
import nc.bs.tg.outside.sale.utils.paybill.PayBillUtils;
import nc.bs.tg.outside.sale.utils.receipt.ReceiptUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BankAccountUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BankArchivesUtils;
import nc.bs.tg.outside.sale.utils.salessystem.BasicInformationUtil;
import nc.bs.tg.outside.sale.utils.salessystem.PayRecBillUtils;
import nc.bs.tg.outside.sale.utils.salessystem.ProjectGroupUtils;
import nc.bs.tg.outside.sale.utils.salessystem.SettlementMethodUtils;
import nc.bs.tg.outside.sale.utils.targetactivation.TargetActivationUtils;
import nc.bs.tg.outside.sale.utils.tartingbill.TartingBillUtils;
import nc.bs.tg.outside.sale.utils.ticketsexchange.TicketsexChangeUtils;
import nc.bs.tg.outside.sale.utils.ticketsrenameex.TicketsRenameExUtils;
import nc.bs.tg.outside.sale.utils.ticketstransfer.TicketsTransferUtils;
import nc.bs.tg.outside.sale.utils.transformbill.TransformBillUtil;
import nc.itf.tg.outside.EBSCont;
import nc.itf.tg.outside.ISyncSaleBillServcie;
import nc.itf.tg.outside.SaleBillCont;
import nc.vo.pub.BusinessException;

public class SyncSaleBillServcieImpl implements ISyncSaleBillServcie {

	@Override
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException {
		String result = null;
		if (SaleBillCont.BILLTYPE_FN11.equals(billtype)) {
			result = TicketsexChangeUtils.getUtils()
					.onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN13.equals(billtype)) {
			result = DisrtibutionUtils.getUtils().onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN15.equals(billtype)) {
			result = TicketsTransferUtils.getUtils()
					.onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN16.equals(billtype)) {
			result = TartingBillUtils.getUtils().onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN17.equals(billtype)) {
			result = TargetActivationUtils.getUtils().onSyncBill(value,
					billtype);
		} else if (SaleBillCont.BILLTYPE_FN18.equals(billtype)) {
			result = TicketsRenameExUtils.getUtils()
					.onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN19.equals(billtype)) {
			result = ExHouseTransferBillUtils.getUtils()
					.onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_36S4.equals(billtype)) {
			result = TransformBillUtil.getUtils().onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_F3.equals(billtype)) {
			result = PayBillUtils.getUtils().onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_F7.equals(billtype)) {
			result = ReceiptUtils.getUtils().onSyncBill(value, billtype);
		} else if (SaleBillCont.BILLTYPE_FN20.equals(billtype)) {
			result = BasicInformationUtil.getUtils().onSyncBill(value, billtype);
		}else if(SaleBillCont.BILLtype_4D10.equals(billtype)){
			result =ProjectGroupUtils.getUtils().onSyncBill(value, billtype);
		}else if(SaleBillCont.BILLtype_F3F7.equals(billtype)){//转备案收付款单
			result=PayRecBillUtils.newInstanceof().onSyncBill(value, billtype);
		}
		return result;
	}

	@Override
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String desdoc) throws BusinessException {

		String result = null;
		if (EBSCont.DOC_16.equals(desdoc)) {// EBS银行档案
			result = BankArchivesUtils.getUtils().onSyncBill(value, desdoc,
					"SALE");
		} else if (EBSCont.DOC_17.equals(desdoc)) {// //EBS银行账户
			result = BankAccountUtils.getUtils().onSyncBill(value, desdoc);
		} else if (EBSCont.DOC_18.equals(desdoc)) {// //EBS结算方式
			result = SettlementMethodUtils.getUtils().onSyncBill(value, desdoc);
		}

		return result;
	}

}
