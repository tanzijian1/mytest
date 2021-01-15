package nc.itf.tg;

import java.util.Map;

import nc.vo.tg.outside.NcToSrmLogVO;

public interface ISaveSrmLog {
	public Map<String, String> SaveSrmLog_RequiresNew(NcToSrmLogVO logVO);
}
