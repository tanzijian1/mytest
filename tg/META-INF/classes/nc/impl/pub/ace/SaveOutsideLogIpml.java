package nc.impl.pub.ace;

import java.util.Map;

import nc.bs.dao.BaseDAO;
import nc.bs.dao.DAOException;
import nc.itf.tg.ISaveOutsideLog;
import nc.vo.tg.outside.OutsideLogVO;

public class SaveOutsideLogIpml implements ISaveOutsideLog {

	@Override
	public String SaveLog_RequiresNew(OutsideLogVO logVO) {
		BaseDAO dao = new BaseDAO();
		String pk = "";
		try {
			pk = dao.insertVO(logVO);
		} catch (DAOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		return pk;
	}

}
