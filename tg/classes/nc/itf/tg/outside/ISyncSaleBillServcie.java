package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * 销售系统对接内部处理接口
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncSaleBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	/**
	 * 档案信息同步
	 * 
	 * @param value
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String desdoc) throws BusinessException;

}
