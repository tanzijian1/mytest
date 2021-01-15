package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.organization.AggOrganizationVO;

public interface IOrganizationMaintain {

	public void delete(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] insert(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] update(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggOrganizationVO[] save(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] unsave(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] approve(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;

	public AggOrganizationVO[] unapprove(AggOrganizationVO[] clientFullVOs,
			AggOrganizationVO[] originBills) throws BusinessException;
}
