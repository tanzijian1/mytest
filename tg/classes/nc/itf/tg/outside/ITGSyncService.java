package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * �ⲿͬ���ӿ�
 * 
 * @author ASUS
 * 
 */
public interface ITGSyncService {
	/**
	 * �ⲿϵͳͬ��
	 * 
	 * @param info
	 * @param string
	 * @return
	 * @throws BusinessExceptions
	 */
	public String onSyncInfo(HashMap<String, Object> info, String methodname)
			throws BusinessException;

}
