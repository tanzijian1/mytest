package nc.itf.tg;

import nc.ui.querytemplate.querytree.IQueryScheme;
import nc.vo.tgfn.invoicing.AggInvoicingHead;
import nc.vo.pub.BusinessException;

public interface IInvoicingMaintain {

	public void delete(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] insert(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] update(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] query(IQueryScheme queryScheme)
			throws BusinessException;

	public AggInvoicingHead[] save(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] unsave(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] approve(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;

	public AggInvoicingHead[] unapprove(AggInvoicingHead[] clientFullVOs,
			AggInvoicingHead[] originBills) throws BusinessException;
}
