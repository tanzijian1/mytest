package nc.itf.tg;

import java.util.Map;

import nc.vo.tg.outside.NcToEbsLogVO;

public interface ISaveLog {
	public Map<String, String> SaveLog_RequiresNew(NcToEbsLogVO logVO);
}
