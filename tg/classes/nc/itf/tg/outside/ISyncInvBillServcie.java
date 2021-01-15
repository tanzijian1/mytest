package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

public interface ISyncInvBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

}
