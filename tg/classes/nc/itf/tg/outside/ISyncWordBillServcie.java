package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

public interface ISyncWordBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	/**
	 * 同步凭证中间表
	 * 
	 * @param org
	 *            组织
	 * @param year
	 *            年度
	 * @param proid
	 *            期间
	 * @throws BusinessException
	 */
	public void onSyncVoucherMinTab_RequiresNew(String org, String year,
			String proid) throws BusinessException;
}
