package nc.itf.tg.outside;

import java.util.HashMap;

import nc.vo.pub.BusinessException;

/**
 * ����ϵͳ�Խ��ڲ�����ӿ�
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncSaleBillServcie {

	public String onSyncBill_RequiresNew(HashMap<String, Object> value,
			String billtype) throws BusinessException;

	/**
	 * ������Ϣͬ��
	 * 
	 * @param value
	 * @param billtype
	 * @return
	 * @throws BusinessException
	 */
	public String onSyncDoc_RequiresNew(HashMap<String, Object> value,
			String desdoc) throws BusinessException;

}
