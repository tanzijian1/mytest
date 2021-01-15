package nc.impl.tg;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.itf.tg.ISaveSrmLog;
import nc.vo.tg.outside.NcToSrmLogVO;

public class SaveSrmLogImpl implements ISaveSrmLog {

	@Override
	public Map<String, String> SaveSrmLog_RequiresNew(NcToSrmLogVO logVO) {
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(logVO);
		} catch (DAOException e) {
			Logger.error(e.getMessage());
		}

		return null;

	}

}
