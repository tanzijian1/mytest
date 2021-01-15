package nc.bs.tg.outside.ebs.pub;

import nc.bs.dao.BaseDAO;
import nc.itf.tg.outside.IlogRecord;
import nc.vo.pub.BusinessException;
import nc.vo.tg.outside.OutsideLogVO;

public class logRecordImpl implements IlogRecord {

	@Override
	public void logRecord(OutsideLogVO logVO) throws BusinessException {
		BaseDAO dao = new BaseDAO();
		dao.insertVO(logVO);
	}

}
