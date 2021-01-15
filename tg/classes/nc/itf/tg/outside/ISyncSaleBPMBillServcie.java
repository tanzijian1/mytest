package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BPMBillStateParaVO;

/**
 * 销售系统BMP对接内部处理接口
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncSaleBPMBillServcie {

	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo,String json)
			throws Exception;

}
