package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.distribution.AggDistribution;
import nc.vo.pub.BusinessException;

public interface IDistributionMaintain {

	public void delete(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] insert(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] update(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggDistribution[] save(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] unsave(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] approve(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;

	public AggDistribution[] unapprove(AggDistribution[] clientFullVOs,
			AggDistribution[] originBills) throws BusinessException;
}
