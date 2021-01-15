package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.financingtotal.AggFinancingTotal;
import nc.vo.pub.BusinessException;

public interface IFinancingTotalMaintain {

	public void delete(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] insert(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] update(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggFinancingTotal[] save(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] unsave(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] approve(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;

	public AggFinancingTotal[] unapprove(AggFinancingTotal[] clientFullVOs,
			AggFinancingTotal[] originBills) throws BusinessException;
}
