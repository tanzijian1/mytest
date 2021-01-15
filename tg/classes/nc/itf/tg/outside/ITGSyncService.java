package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * 外部同步接口
 * 
 * @author ASUS
 * 
 */
public interface ITGSyncService {
	/**
	 * 外部系统同步
	 * 
	 * @param info
	 * @param string
	 * @return
	 * @throws BusinessExceptions
	 */
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException;

}
