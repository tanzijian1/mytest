package nc.impl.tg;

import nc.impl.pub.ace.OrganizationTypePubServiceUtils;
import nc.itf.tg.IOrganizationTypeMaintain;
import nc.vo.pub.BusinessException;
import nc.vo.tg.organizationtype.OrganizationTypeVO;

public class OrganizationTypeMaintainImpl implements IOrganizationTypeMaintain {

	@Override
	public void delete(OrganizationTypeVO vo) throws BusinessException {
		OrganizationTypePubServiceUtils.getUtils().delete(vo);

	}

	@Override
	public OrganizationTypeVO insert(OrganizationTypeVO vo)
			throws BusinessException {
		return OrganizationTypePubServiceUtils.getUtils().insert(vo);
	}

	@Override
	public OrganizationTypeVO update(OrganizationTypeVO vo)
			throws BusinessException {
		return OrganizationTypePubServiceUtils.getUtils().update(vo);
	}

	@Override
	public OrganizationTypeVO enableTreeVO(OrganizationTypeVO vo)
			throws BusinessException {
		return OrganizationTypePubServiceUtils.getUtils().enableTreeVO(vo);
	}

	@Override
	public Object[] queryByWhereSql(String whereSql) throws BusinessException {

		return OrganizationTypePubServiceUtils.getUtils().query(
				whereSql);
	}

}
