package nc.itf.tg.outside;

import nc.vo.pub.SuperVO;

public interface ISaveLogService {

	public void saveLog_RequiresNew(SuperVO vo) throws Exception;
	
	public void saveVOByList(SuperVO[] vos) throws Exception;
	
	public void updateVO(SuperVO vo) throws Exception;
}
