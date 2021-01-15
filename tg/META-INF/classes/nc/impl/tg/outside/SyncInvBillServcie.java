package nc.impl.tg.outside;

import java.util.HashMap;

import nc.bs.tg.outside.inv.utils.QryInvInfoUtils;
import nc.itf.tg.outside.ISyncInvBillServcie;
import nc.vo.pub.BusinessException;

public class SyncInvBillServcie implements ISyncInvBillServcie {

	@Override
	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException {
		String result = null;
		if ("01".equals(billtype)) {// 查询发票台账信息
			result = QryInvInfoUtils.getUtils().getValue(value);
		}
		return result;
	}

}
