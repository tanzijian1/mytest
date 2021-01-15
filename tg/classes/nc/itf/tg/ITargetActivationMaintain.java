package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.targetactivation.AggTargetactivation;
import nc.vo.pub.BusinessException;

public interface ITargetActivationMaintain {

	public void delete(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] insert(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] update(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggTargetactivation[] save(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] unsave(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] approve(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;

	public AggTargetactivation[] unapprove(AggTargetactivation[] clientFullVOs,
			AggTargetactivation[] originBills) throws BusinessException;
}
