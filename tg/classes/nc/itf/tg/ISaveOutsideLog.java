package nc.itf.tg;

import nc.vo.tg.outside.OutsideLogVO;

public interface ISaveOutsideLog {

	/**
	 * ͨ���ӿڲ��������־��
	 * @param logVO
	 * @return
	 */
	public String SaveLog_RequiresNew(OutsideLogVO logVO);
}
