package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.CallImplInfoVO;

/**
 * 调用外部接口
 * 
 * @author ASUS
 * 
 */
public interface ITGCallService {

	/**
	 * 调用外部系统接口
	 * 
	 * @param dessystem
	 * @param info
	 * @return
	 */
	public String onCallMethod(CallImplInfoVO info) throws BusinessException;

	/**
	 * 调用外部系统配置
	 * 
	 * @param dessystem
	 * @param value
	 * @param value2
	 * @return
	 */
	public CallImplInfoVO getImplInfo(String dessystem, String methodname,
			Object value) throws BusinessException;

}
