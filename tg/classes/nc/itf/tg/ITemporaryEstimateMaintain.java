package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.temporaryestimate.AggTemest;
import nc.vo.pub.BusinessException;

public interface ITemporaryEstimateMaintain {

	public void delete(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] insert(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] update(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggTemest[] save(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] unsave(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] approve(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;

	public AggTemest[] unapprove(AggTemest[] clientFullVOs,
			AggTemest[] originBills) throws BusinessException;
}
