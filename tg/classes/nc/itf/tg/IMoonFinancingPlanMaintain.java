package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.moonfinancingplan.AggMoonFinancingPlan;
import nc.vo.pub.BusinessException;

public interface IMoonFinancingPlanMaintain {

	public void delete(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] insert(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] update(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggMoonFinancingPlan[] save(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] unsave(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] approve(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;

	public AggMoonFinancingPlan[] unapprove(AggMoonFinancingPlan[] clientFullVOs,
			AggMoonFinancingPlan[] originBills) throws BusinessException;
}
