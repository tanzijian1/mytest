package nc.itf.tg;

import nc.vo.tg.outside.OutsideLogVO;

public interface ISaveOutsideLog {

	/**
	 * 通过接口插入出参日志表
	 * @param logVO
	 * @return
	 */
	public String SaveLog_RequiresNew(OutsideLogVO logVO);
}
