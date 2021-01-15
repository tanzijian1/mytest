package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingplan.AggFinancingPlan;
import nc.vo.pub.BusinessException;

public interface IFinancingPlanMaintain {

	public void delete(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] insert(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] update(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggFinancingPlan[] save(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] unsave(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] approve(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;

	public AggFinancingPlan[] unapprove(AggFinancingPlan[] clientFullVOs,
			AggFinancingPlan[] originBills) throws BusinessException;
}
