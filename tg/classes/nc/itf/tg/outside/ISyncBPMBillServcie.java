package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BPMBillStateParaVO;

/**
 * BMP�Խ��ڲ�����ӿ�
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncBPMBillServcie {

	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo)
			throws BusinessException;

}
