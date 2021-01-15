package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.paymentrequest.AggPayrequest;
import nc.vo.pub.BusinessException;

public interface IPaymentRequestMaintain {

	public void delete(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] insert(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] update(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggPayrequest[] save(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] unsave(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] approve(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;

	public AggPayrequest[] unapprove(AggPayrequest[] clientFullVOs,
			AggPayrequest[] originBills) throws BusinessException;
}
