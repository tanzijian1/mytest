package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tg.checkfinance.AggCheckFinanceHVO;
import nc.vo.pub.BusinessException;

public interface ICheckfinanceMaintain {

	public void delete(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] insert(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] update(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggCheckFinanceHVO[] save(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] unsave(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] approve(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;

	public AggCheckFinanceHVO[] unapprove(AggCheckFinanceHVO[] clientFullVOs,
			AggCheckFinanceHVO[] originBills) throws BusinessException;
}
