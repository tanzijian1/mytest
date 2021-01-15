package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.internalinterest.AggInternalinterest;
import nc.vo.pub.BusinessException;

public interface IInternalInterestMaintain {

	public void delete(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] insert(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] update(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggInternalinterest[] save(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] unsave(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] approve(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;

	public AggInternalinterest[] unapprove(AggInternalinterest[] clientFullVOs,
			AggInternalinterest[] originBills) throws BusinessException;
}
