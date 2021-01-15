package nc.impl.tg.outside;

import java.util.HashMap;

import nc.bs.logging.Logger;
import nc.bs.tg.outside.ebs.pub.ImgQryUtils;
import nc.bs.tg.outside.img.utils.contractcostcashout.CcCashoutUtils;
import nc.bs.tg.outside.img.utils.contractcostcashout.ImagCallbackutils;
import nc.bs.tg.outside.img.utils.incomeinvoice.IncomeInvoiceUtils;
import nc.itf.tg.outside.IMGBillCont;
import nc.itf.tg.outside.ISyncIMGBillServcie;
import nc.vo.pub.BusinessException;

public class SyncIMGBillServcieImpl implements ISyncIMGBillServcie {

	@Override
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException {
		String result = null;
		if (IMGBillCont.BILLTYPE_HZ01.equals(billtype)) {
			result = IncomeInvoiceUtils.getUtils().onSyncBill(value, billtype);
		} else if (IMGBillCont.BILLTYPE_264XCXX004.equals(billtype)) {
			result = CcCashoutUtils.getUtils().onSyncBill(value, billtype);
		} else if (IMGBillCont.BILLTYPE_264X.equals(billtype)
				|| IMGBillCont.BILLTYPE_267X.equals(billtype)) {
			result = ImagCallbackutils.getUtils().onSyncBill(value, billtype);
		}
		return result;
	}

	@Override
	public void onSyncInv_RequiresNew(String imgid) throws Exception {
		Logger.error("huangxj½øÈë£ºonInsertInv");
		ImgQryUtils.getUtils().onInsertInv(imgid,false);
	}

	@Override
	public void onSyncInv_RequiresNew(String imgid, Boolean isLLBill)
			throws Exception {
		ImgQryUtils.getUtils().onInsertInv(imgid, isLLBill);
	}

}
