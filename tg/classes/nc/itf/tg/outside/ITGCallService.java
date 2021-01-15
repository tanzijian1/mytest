package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

/**
 * �����ⲿ�ӿ�
 * 
 * @author ASUS
 * 
 */
public interface ITGCallService {

	/**
	 * �����ⲿϵͳ�ӿ�
	 * 
	 * @param dessystem
	 * @param info
	 * @return
	 */
	public String onCallMethod(CallImplInfoVO info) throws BusinessException;

	/**
	 * �����ⲿϵͳ����
	 * 
	 * @param dessystem
	 * @param value
	 * @param value2
	 * @return
	 */
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException;

}
