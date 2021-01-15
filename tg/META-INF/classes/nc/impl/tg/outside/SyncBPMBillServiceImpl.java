package nc.impl.tg.outside;

import nc.bs.tg.outside.bpm.utils.SyncBPMBillStatesUtils;
import nc.itf.tg.outside.ISyncBPMBillServcie;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BPMBillStateParaVO;

public class SyncBPMBillServiceImpl implements ISyncBPMBillServcie {

	@Override
	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo)
			throws BusinessException {
		String result = SyncBPMBillStatesUtils.getUtils().onSyncBillState(vo);
		return result;
	}
}
