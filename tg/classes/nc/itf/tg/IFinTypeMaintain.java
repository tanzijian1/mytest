package nc.itf.tg;

import nc.vo.pub.BusinessException;
import nc.vo.tg.fintype.FinTypeVO;

public interface IFinTypeMaintain {

	public void delete(FinTypeVO vo) throws BusinessException;

	public FinTypeVO insert(FinTypeVO vo) throws BusinessException;

	public FinTypeVO update(FinTypeVO vo) throws BusinessException;

	public FinTypeVO enableTreeVO(FinTypeVO object) throws BusinessException;

	public Object[] queryByWhereSql(String whereSql) throws BusinessException;

}