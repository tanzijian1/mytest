package nc.impl.tg.outside;

import nc.bs.dao.BaseDAO;
import nc.itf.tg.outside.ISaveLogService;
import nc.vo.pub.SuperVO;

public class SaveLogServiceImpl implements ISaveLogService{

	@Override
	public void saveLog_RequiresNew(SuperVO vo) throws Exception {
		BaseDAO dao = new BaseDAO();
		dao.insertVO(vo);
	}

	@Override
	public void saveVOByList(SuperVO[] vos) throws Exception {
		BaseDAO dao = new BaseDAO();
		dao.updateVOArray(vos);
	}

	@Override
	public void updateVO(SuperVO vo) throws Exception {
		BaseDAO dao = new BaseDAO();
		dao.updateVO(vo);
	}

}
