package nc.itf.tg.outside;

import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.BPMBillStateParaVO;

/**
 * ����ϵͳBMP�Խ��ڲ�����ӿ�
 * 
 * @author HUANGDQ
 * 
 */
public interface ISyncSaleBPMBillServcie {

	public String onSyncBillState_RequiresNew(BPMBillStateParaVO vo,String json)
			throws Exception;

}
