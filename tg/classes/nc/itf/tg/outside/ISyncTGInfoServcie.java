package nc.itf.tg.outside;

import java.util.HashMap;

import com.ufida.dataset.IContext;

import nc.pub.smart.data.DataSet;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.tg.outside.CallImplInfoVO;
import nc.vo.tg.outside.OSImplLogVO;

public interface ISyncTGInfoServcie {
	/**
	 * 外部接口信息同步
	 * 
	 * @param srcsystem
	 * @param method
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncInfo_RequiresNew(String srcsystem, String method,
			HashMap<String, Object> info) throws BusinessException;

	/**
	 * 处理外部系统操作
	 * 
	 * @param dessys
	 * @param method
	 * @param value
	 * @return
	 * @throws BusinessException
	 */
	public CallImplInfoVO getCallImplInfoVO(String dessys, String method,
			Object value) throws BusinessException;

	/**
	 * 调用外部系统接口
	 * 
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public String onCallInfo(CallImplInfoVO info) throws BusinessException;

	/**
	 * 调用外部系统接口
	 * 
	 * @param info
	 * @return
	 * @throws BusinessException
	 */
	public String onCallInfo_RequiresNew(CallImplInfoVO info)
			throws BusinessException;

	/**
	 * 日志插入
	 * 
	 * @param vo
	 * @throws BusinessException
	 */
	public void insertVO_RequiresNew(SuperVO vo) throws BusinessException;

}
