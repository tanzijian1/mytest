package nc.impl.tg.outside;

import java.util.HashMap;

import nc.bs.tg.outside.word.utils.QueryOrgUtils;
import nc.bs.tg.outside.word.utils.QueryUserPowerUtils;
import nc.bs.tg.outside.word.utils.VoucherMinTabUtils;
import nc.itf.tg.outside.ISyncWordBillServcie;
import nc.itf.tg.outside.WordBillCont;
import nc.vo.pub.BusinessException;

public class SyncWordBillServcie implements ISyncWordBillServcie {

	@Override
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException {
		String result = null;
		if (WordBillCont.DOC_01.equals(billtype)) {
			result = QueryUserPowerUtils.getUtils().getValue(value);
		} else if (WordBillCont.DOC_02.equals(billtype)) {
			result = QueryOrgUtils.getUtils().getValue(value);
		}
		return result;
	}

	@Override
	public void onSyncVoucherMinTab_RequiresNew(String org, String year,
			String proid) throws BusinessException {
		VoucherMinTabUtils.getUtils().onSyncVoucherMinTab(org, year, proid);
	}

}
