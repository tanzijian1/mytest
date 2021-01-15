package nc.impl.tg;

import nc.impl.pub.ace.DataTypePubServiceUtils;
import nc.itf.tg.IDataTypeMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.tg.datatype.DataTypeVO;

public class DataTypeMaintainImpl implements IDataTypeMaintain {
	@Override
	public void delete(DataTypeVO vo) throws BusinessException {
		DataTypePubServiceUtils.getUtils().delete(vo);

	}

	@Override
	public DataTypeVO insert(DataTypeVO vo) throws BusinessException {
		return DataTypePubServiceUtils.getUtils().insert(vo);
	}

	@Override
	public DataTypeVO update(DataTypeVO vo) throws BusinessException {
		return DataTypePubServiceUtils.getUtils().update(vo);
	}

	@Override
	public DataTypeVO enableTreeVO(DataTypeVO vo) throws BusinessException {
		return DataTypePubServiceUtils.getUtils().enableTreeVO(vo);
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws BusinessException {

		return DataTypePubServiceUtils.getUtils().query(whereSql);
	}
}
