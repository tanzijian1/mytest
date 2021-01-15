package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * 销售系统对接内部处理接口
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncIMGBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	public void onSyncInv_RequiresNew(String imgid) throws Exception;

	/**
	 * 从影像获取发票数据
	 * @param imgid
	 *            影像编码
	 * @param isLLBill
	 *            是否邻里单据
	 * @throws Exception
	 */
	public void onSyncInv_RequiresNew(String imgid, Boolean isLLBill)
			throws Exception;

}
