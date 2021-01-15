package nc.itf.tg;

import nc.vo.pub.BusinessException;
import nc.vo.tg.datatype.DataTypeVO;

public interface IDataTypeMaintain {

	public void delete(DataTypeVO vo) throws BusinessException;

	public DataTypeVO insert(DataTypeVO vo) throws BusinessException;

	public DataTypeVO update(DataTypeVO vo) throws BusinessException;

	public DataTypeVO enableTreeVO(DataTypeVO object) throws BusinessException;

	public Object[] queryByWhereSql(String whereSql) throws BusinessException;
}