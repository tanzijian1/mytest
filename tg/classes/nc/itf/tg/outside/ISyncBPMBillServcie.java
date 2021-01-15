package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BPMBillStateParaVO;

/**
 * BMP对接内部处理接口
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncBPMBillServcie {

	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo)
			throws BusinessException;

}
