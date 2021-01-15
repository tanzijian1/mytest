package nc.itf.tg.outside;

import java.util.HashMap;

import uap.json.JSONObject;

import nc.vo.pub.BusinessException;

public interface ISyncBusinessBillService {
	
	public String onSyncBill_RequiresNew(JSONObject jsonObj ,
			String billtype) throws Exception;

}
