package nc.impl.tg;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.bs.logging.Logger;
import nc.itf.tg.ISaveLog;
import nc.vo.tg.outside.NcToEbsLogVO;

public class SaveLogImpl implements ISaveLog {

	@Override
	public Map<String, String> SaveLog_RequiresNew(NcToEbsLogVO logVO) {
		BaseDAO dao = new BaseDAO();
		try {
			dao.insertVO(logVO);
		} catch (DAOException e) {
			Logger.error(e.getMessage());
		}

		return null;
	}

}
