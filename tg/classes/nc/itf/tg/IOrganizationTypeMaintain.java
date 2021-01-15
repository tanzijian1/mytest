package nc.itf.tg;

import nc.vo.pub.BusinessException;
import nc.vo.tg.organizationtype.OrganizationTypeVO;

public interface IOrganizationTypeMaintain {

	public void delete(OrganizationTypeVO vo) throws BusinessException;

	public OrganizationTypeVO insert(OrganizationTypeVO vo)
			throws BusinessException;

	public OrganizationTypeVO update(OrganizationTypeVO vo)
			throws BusinessException;

	public OrganizationTypeVO enableTreeVO(OrganizationTypeVO object)
			throws BusinessException;

	public Object[] queryByWhereSql(String whereSql) throws BusinessException;

}