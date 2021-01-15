package nc.impl.tg;

import nc.impl.pub.ace.FinTypePubServiceUtils;
import nc.itf.tg.IFinTypeMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;

public class FinTypeMaintainImpl implements IFinTypeMaintain {
	@Override
	public void delete(FinTypeVO vo) throws BusinessException {
		FinTypePubServiceUtils.getUtils().delete(vo);

	}

	@Override
	public FinTypeVO insert(FinTypeVO vo) throws BusinessException {
		return FinTypePubServiceUtils.getUtils().insert(vo);
	}

	@Override
	public FinTypeVO update(FinTypeVO vo) throws BusinessException {
		return FinTypePubServiceUtils.getUtils().update(vo);
	}

	@Override
	public FinTypeVO enableTreeVO(FinTypeVO vo) throws BusinessException {
		return FinTypePubServiceUtils.getUtils().enableTreeVO(vo);
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws BusinessException {

		return FinTypePubServiceUtils.getUtils().query(whereSql);
	}

}
