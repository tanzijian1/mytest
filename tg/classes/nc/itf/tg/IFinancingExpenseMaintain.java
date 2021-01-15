package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.pub.BusinessException;
import nc.vo.tg.financingexpense.AggFinancexpenseVO;

public interface IFinancingExpenseMaintain {

	public void delete(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] insert(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] update(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggFinancexpenseVO[] save(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] unsave(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] approve(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;

	public AggFinancexpenseVO[] unapprove(AggFinancexpenseVO[] clientFullVOs,
			AggFinancexpenseVO[] originBills) throws BusinessException;
}
